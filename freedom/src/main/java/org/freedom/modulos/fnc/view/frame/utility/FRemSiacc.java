/**
 * @version 07/2007 <BR>
 * @author Setpoint Informática Ltda. / Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRemSiacc.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Tela de remessa de arquivo de SIACC.
 * 
 */

package org.freedom.modulos.fnc.view.frame.utility;

import java.awt.FileDialog;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.business.component.siacc.Reg;
import org.freedom.modulos.fnc.business.component.siacc.RegA;
import org.freedom.modulos.fnc.business.component.siacc.RegE;
import org.freedom.modulos.fnc.business.component.siacc.RegZ;
import org.freedom.modulos.fnc.business.component.siacc.SiaccUtil;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EPrefs;

public class FRemSiacc extends FRemFBN {

	private static final long serialVersionUID = 1L;

	public FRemSiacc() {

		super( TIPO_FEBRABAN_SIACC );
	}

	public boolean execExporta(String convCob) {

		boolean retorno = false;
		String sFileName = null;
		File fileSiacc = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		HashSet<SiaccUtil.StuffCli> hsCli = new HashSet<SiaccUtil.StuffCli>();
		HashSet<SiaccUtil.StuffRec> hsRec = new HashSet<SiaccUtil.StuffRec>();

		if ( consisteExporta( hsCli, hsRec, true, null ) ) {

			retorno = setPrefs(convCob);

			if ( retorno ) {

				lbStatus.setText( "     criando arquivo ..." );

				FileDialog fileDialogSiacc = null;
				fileDialogSiacc = new FileDialog( Aplicativo.telaPrincipal, "Exportar arquivo.", FileDialog.SAVE );
				sFileName = "remessa" + prefs.get( EPrefs.NROSEQ ) + ".txt";
				fileDialogSiacc.setFile( sFileName );
				fileDialogSiacc.setVisible( true );

				if ( fileDialogSiacc.getFile() == null ) {
					lbStatus.setText( "" );
					return retorno;
				}

				sFileName = fileDialogSiacc.getDirectory() + fileDialogSiacc.getFile();

				fileSiacc = new File( sFileName );

				try {
					fileSiacc.createNewFile();
					fw = new FileWriter( fileSiacc );
					bw = new BufferedWriter( fw );

					lbStatus.setText( "     gravando arquivo ..." );
					retorno = gravaRemessa( bw, hsCli, hsRec );
				} catch ( IOException ioError ) {
					Funcoes.mensagemErro( this, "Erro Criando o arquivo!\n " + sFileName + "\n" + ioError.getMessage() );
					lbStatus.setText( "" );
					return retorno;
				}

				lbStatus.setText( "     pronto ..." );
				atualizaSitremessaExp( hsCli, hsRec );
			}

		}
		return retorno;
	}

	private void atualizaSitremessaExp( HashSet<SiaccUtil.StuffCli> hsCli, HashSet<SiaccUtil.StuffRec> hsRec ) {

		setSitremessa( hsRec, "01" );
		persisteDados( hsCli, hsRec, null );
		updatePrefere();
	}

	private void setSitremessa( HashSet<SiaccUtil.StuffRec> hsRec, final String sit ) {

		for ( SiaccUtil.StuffRec sr : hsRec ) {
			sr.setSitremessa( sit );
		}
	}

	private boolean gravaRemessa( final BufferedWriter bw, HashSet<SiaccUtil.StuffCli> hsCli, HashSet<SiaccUtil.StuffRec> hsRec ) {

		boolean retorno = false;
		Integer numReg = new Integer( 0 );
		Integer nroSeq = (Integer) prefs.get( EPrefs.NROSEQ );

		try {

			ArrayList<Reg> list = new ArrayList<Reg>();
			list.add( new RegA( '1', prefs, numReg++ ) );
			int numAgenda = 1;
			BigDecimal vlrtotal = new BigDecimal( 0 );
			RegE e = null;

			// Implementar no futuro (Registro de clientes não podem ser enviados com Registro E)

			/*
			 * for ( SiaccUtil.StuffCli c : hsCli ) { if ( "B".equals( c.getArgs()[ SiaccUtil.EColcli.TIPOREMCLI.ordinal() ] ) ) { list.add( new SiaccUtil().new RegB( 'B', c ) ) ; numReg++; } } for ( SiaccUtil.StuffCli c : hsCli ) { if ( "C".equals( c.getArgs()[SiaccUtil.EColcli.TIPOREMCLI.ordinal()
			 * ] ) ) { list.add( new SiaccUtil().new RegC( 'C', c, numReg++ ) ); } } for ( SiaccUtil.StuffCli c : hsCli ) { if ( "D".equals( c.getArgs()[ SiaccUtil.EColcli.TIPOREMCLI.ordinal() ] ) ) { list.add( new SiaccUtil().new RegD( 'D', c, numReg++ ) ); } }
			 */
			for ( SiaccUtil.StuffRec r : hsRec ) {
				// if ( sitRemessa.indexOf(( r.getArgs()[ SiaccUtil.EColrec.SITREMESSA.ordinal() ] ))>-1 ) {
				e = new RegE( 'E', r, numReg++, numAgenda );
				list.add( e );
				vlrtotal = vlrtotal.add( e.getVlrParc() );
				numAgenda++;
				// }
			}

			list.add( new RegZ( numReg + 1, vlrtotal.floatValue(), numReg++ ) );

			for ( Reg reg : list ) {
				bw.write( reg.toString() );
			}
			bw.flush();
			bw.close();
			prefs.put( EPrefs.NROSEQ, ++nroSeq );

		} catch ( IOException ioError ) {
			Funcoes.mensagemErro( this, "Erro gravando no arquivo!\n" + ioError.getMessage() );
			lbStatus.setText( "" );
			retorno = false;
		}

		return retorno;
	}

	public void imprimir( TYPE_PRINT visualizar ) {

		ResultSet rs = null;
		String sDtFiltro = "E".equals( rgData.getVlrString() ) ? "IR.DTITREC" : "IR.DTVENCITREC";
		PreparedStatement ps = null;

		if ( txtCodBanco.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Código do banco é requerido!" );
			return;
		}

		try {

			StringBuilder sSQL = new StringBuilder();

			sSQL.append( "SELECT IR.CODREC, IR.NPARCITREC, R.DOCREC, R.CODCLI, C.RAZCLI, IR.DTITREC, IR.DTVENCITREC," );
			sSQL.append( "IR.VLRAPAGITREC, FC.AGENCIACLI, FC.IDENTCLI, COALESCE(FR.SITREMESSA,'00') SITREMESSA, " );
			sSQL.append( "FR.SITRETORNO, COALESCE(COALESCE(FR.STIPOFEBRABAN,FC.STIPOFEBRABAN),'02') STIPOFEBRABAN, " );
			sSQL.append( "COALESCE(FC.TIPOREMCLI,'B') TIPOREMCLI, C.PESSOACLI, C.CPFCLI, C.CNPJCLI " );
			sSQL.append( "FROM VDCLIENTE C," );
			sSQL.append( "FNRECEBER R LEFT OUTER JOIN FNFBNCLI FC ON " );
			sSQL.append( "FC.CODEMP=R.CODEMPCL AND FC.CODFILIAL=R.CODFILIALCL AND FC.CODCLI=R.CODCLI ," );
			sSQL.append( "FNITRECEBER IR LEFT OUTER JOIN FNFBNREC FR ON " );
			sSQL.append( "FR.CODEMP=IR.CODEMP AND FR.CODFILIAL=IR.CODFILIAL AND " );
			sSQL.append( "FR.CODREC=IR.CODREC AND FR.NPARCITREC=IR.NPARCITREC AND " );
			sSQL.append( "FR.CODEMPBO=IR.CODEMPBO AND FR.CODFILIALBO=IR.CODFILIALBO AND FR.CODBANCO=IR.CODBANCO " );
			sSQL.append( "WHERE R.CODEMP=IR.CODEMP AND R.CODFILIAL=IR.CODFILIAL AND R.CODREC=IR.CODREC AND " );
			sSQL.append( "C.CODEMP=R.CODEMPCL AND C.CODFILIAL=R.CODFILIALCL AND C.CODCLI=R.CODCLI AND " );
			sSQL.append( sDtFiltro );
			sSQL.append( " BETWEEN ? AND ? AND IR.STATUSITREC IN ('R1','RL') AND " );
			sSQL.append( "IR.CODEMPBO=? AND IR.CODFILIALBO=? AND IR.CODBANCO=? " );
			sSQL.append( where );
			sSQL.append( "ORDER BY C.RAZCLI, R.CODREC, IR.NPARCITREC " );
			ps = con.prepareStatement( sSQL.toString() );

			ps.setDate( 1, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, Aplicativo.iCodFilial );
			ps.setInt( 5, txtCodBanco.getVlrInteger() );

			rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con );

			FPrinterJob dlGr = new FPrinterJob( "relatorios/RemSiacci.jasper", "RELATÓRIO DE REMESSA", null, rs, hParam, this );

			if ( visualizar == TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	@ Override
	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 && e.getSource() == tab && tab.getLinhaSel() > -1 ) {

			if ( !"00".equals( tab.getValor( tab.getLinhaSel(), EColTab.COL_SITRET.ordinal() ) ) ) {

				Funcoes.mensagemInforma( this, "Registro rejeitado!\n" + getMenssagemRet( (String) tab.getValor( tab.getLinhaSel(), EColTab.COL_SITRET.ordinal() ) ) );
			}
			completaTabela( tab.getLinhaSel(), (Integer) tab.getValor( tab.getLinhaSel(), EColTab.COL_CODCLI.ordinal() ), (String) tab.getValor( tab.getLinhaSel(), EColTab.COL_RAZCLI.ordinal() ), (String) tab.getValor( tab.getLinhaSel(), EColTab.COL_AGENCIACLI.ordinal() ), (String) tab.getValor(
					tab.getLinhaSel(), EColTab.COL_IDENTCLI.ordinal() ), (String) tab.getValor( tab.getLinhaSel(), EColTab.COL_STIPOFEBRABAN.ordinal() ) );
		}
	}

	@ Override
	protected void ajustaNossoNumero() {

		// TODO Auto-generated method stub
		
	}
}
