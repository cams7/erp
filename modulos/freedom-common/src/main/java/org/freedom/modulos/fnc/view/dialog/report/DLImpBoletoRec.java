/**
 * @version 18/11/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLImpBoletoRec.java <BR>
 * 
 *                         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                         Comentários sobre a classe...
 */

package org.freedom.modulos.fnc.view.dialog.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JInternalFrame;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Empresa;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.frame.report.FRBoleto;

public class DLImpBoletoRec extends FDialogo {

	private static final long serialVersionUID = 1L;

	public JTextFieldPad txtCodModBol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtImpInst = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtPreImpModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtClassModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private ListaCampos lcModBol = new ListaCampos( this );

	private int codRec;

	private int parcRec;

	DbConnection con = null;

	private JInternalFrame owner = null;

	public DLImpBoletoRec( JInternalFrame owner, DbConnection con, int codRec, int parcRec ) {

		super();
		setAtribos( 370, 150 );
		setTitulo( "Impressão de Boleto" );

		this.con = con;
		this.codRec = codRec;
		this.parcRec = parcRec;
		this.owner = owner;

		montaListaCampos();
		montaTela();

	}

	private void montaListaCampos() {

		/********************
		 * MODELO DE BOLETO *
		 ********************/
		lcModBol.add( new GuardaCampo( txtCodModBol, "CodModBol", "Cód.mod.", ListaCampos.DB_PK, true ) );
		lcModBol.add( new GuardaCampo( txtImpInst, "ImpInfoParc", "Imp.Info.", ListaCampos.DB_SI, false ) );
		lcModBol.add( new GuardaCampo( txtDescModBol, "DescModBol", "Descrição do modelo de boleto", ListaCampos.DB_SI, false ) );
		lcModBol.add( new GuardaCampo( txtPreImpModBol, "PreImpModBol", "Pré-impr.", ListaCampos.DB_SI, false ) );
		lcModBol.add( new GuardaCampo( txtClassModBol, "ClassModBol", "Classe do modelo", ListaCampos.DB_SI, false ) );
		lcModBol.setReadOnly( true );
		lcModBol.montaSql( false, "MODBOLETO", "FN" );
		txtCodModBol.setTabelaExterna( lcModBol, null );
		txtCodModBol.setFK( true );
		txtCodModBol.setNomeCampo( "CodModBol" );
		lcModBol.setConexao( con );
	}

	private void montaTela() {

		adic( new JLabelPad( "Cód.Mod.Bol." ), 7, 10, 80, 20 );
		adic( txtCodModBol, 7, 30, 80, 20 );
		adic( new JLabelPad( "Descrição do modelo de boleto" ), 90, 10, 220, 20 );
		adic( txtDescModBol, 90, 30, 250, 20 );
	}

	public void imprimir() {

		if ( txtCodModBol.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Modelo de boleto não selecionado!" );
			txtCodModBol.requestFocus();
			return;
		}

		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		sSQL.append( "SELECT IM.DVCONVCOB, ITR.DTVENCITREC,ITR.NPARCITREC,ITR.VLRAPAGITREC, ITR.VLRPARCITREC, MB.PREIMPMODBOL, " );
		sSQL.append( "F.UNIDFRANQUEADA, F.RAZFILIAL, F.ENDFILIAL, F.NUMFILIAL, F.CNPJFILIAL, F.BAIRFILIAL, F.COMPLFILIAL, F.SIGLAUF, F.EMAILFILIAL, " );
		sSQL.append( "ITR.VLRDESCITREC, (ITR.VLRJUROSITREC+ITR.VLRMULTAITREC) VLRMULTA, R.DOCREC,ITR.CODBANCO, B.DVBANCO, " );
		sSQL.append( "B.IMGBOLBANCO LOGOBANCO01, COALESCE(B.IMGBOLBANCO2,B.IMGBOLBANCO) LOGOBANCO02, B.IMGBOLBANCO LOGOBANCO03, B.IMGBOLBANCO LOGOBANCO04, IM.CODCARTCOB, " );
		sSQL.append( "MB.ESPDOCMODBOL ESPDOC, MB.ACEITEMODBOL ACEITE, MB.MDECOB, ITR.dtitrec AS DTEMITVENDA, " );
		sSQL.append( "C.RAZCLI,C.CPFCLI,C.CNPJCLI, C.ENDCLI,C.NUMCLI,C.COMPLCLI,C.CEPCLI,C.BAIRCLI, " );
		sSQL.append( "C.CIDCLI,C.UFCLI, COALESCE(C.ENDCOB, C.ENDCLI) ENDCOB,C.NUMCOB,C.COMPLCOB,C.CEPCOB,C.BAIRCOB,C.CIDCOB,C.UFCOB, P.CODMOEDA, " );
		sSQL.append( "C.PESSOACLI, (ITR.DTVENCITREC-CAST('07.10.1997' AS DATE)) FATVENC, M.CODFBNMOEDA, " );
		sSQL.append( "M.SINGMOEDA, M.PLURMOEDA, M.DECSMOEDA, M.DECPMOEDA, " );
		sSQL.append( "CT.AGENCIACONTA, CT.POSTOCONTA, IM.NUMCONTA, MB.DESCLPMODBOL, MB.INSTPAGMODBOL, IM.CONVCOB, ITR.DESCPONT, C.INSCCLI, ITR.OBSITREC OBS, TCO.VARIACAOCARTCOB, ");
		sSQL.append( "R.CODREC, R.DATAREC, itr.seqnossonumero, r.vlrrec, mb.TxaModBol, ITR.DOCLANCAITREC " );
		// Implementação para permitir a impressão de boleto pré-impresso.
		sSQL.append( ",'' codorc, '' nomeconv, '' obsorc, r.docrec docvenda, 0 reciboitrec,");
		sSQL.append( "(SELECT COUNT(*) FROM FNITRECEBER ITR2 WHERE ITR2.CODREC=R.CODREC AND ITR2.CODEMP=R.CODEMP AND ITR2.CODFILIAL=R.CODFILIAL) PARCS, ");
		sSQL.append( "r.codcli, c.nomecli, c.rgcli, c.fonecli, c.dddcli, r.codvenda, r.vlrapagrec, '' nomevend, '' nomevend2, '' nomevend3, '' nomevend4, ");
		sSQL.append( "f.endfilial, f.numfilial, f.cnpjfilial, f.cepfilial, f.uffilial, f.cidfilial, f.UnidFranqueada, f.WWWFranqueadora, f.MarcaFranqueadora, " );
		sSQL.append( "mu.nomemunic cidcli ");
		sSQL.append( "FROM FNRECEBER R, SGPREFERE1 P, FNMOEDA M, FNBANCO B, FNMODBOLETO MB, " );
		sSQL.append( "FNITMODBOLETO IM, FNITRECEBER ITR, SGFILIAL F, FNCONTA CT, FNCARTCOB TCO, VDCLIENTE C " );
		sSQL.append( "LEFT OUTER JOIN SGMUNICIPIO MU ON MU.CODPAIS=C.CODPAIS AND MU.SIGLAUF=C.SIGLAUF AND MU.CODMUNIC=C.CODMUNIC ");
		sSQL.append( "WHERE " );
		sSQL.append( "C.CODEMP=R.CODEMPCL AND C.CODFILIAL=R.CODFILIALCL AND C.CODCLI=R.CODCLI " );
		sSQL.append( "AND P.CODEMP=R.CODEMP AND P.CODFILIAL=R.CODFILIAL " );
		sSQL.append( "AND F.CODEMP=R.CODEMP AND F.CODFILIAL=R.CODFILIAL " );
		sSQL.append( "AND M.CODEMP=P.CODEMPMO AND M.CODFILIAL=P.CODFILIALMO AND M.CODMOEDA=P.CODMOEDA " );
		sSQL.append( "AND B.CODEMP=ITR.CODEMPBO AND B.CODFILIAL=ITR.CODFILIALBO AND B.CODBANCO=ITR.CODBANCO " );
		sSQL.append( "AND IM.CODEMP=MB.CODEMP AND IM.CODFILIAL=MB.CODFILIAL AND IM.CODMODBOL=MB.CODMODBOL " );
		sSQL.append( "AND IM.CODEMPBO=B.CODEMP AND IM.CODFILIALBO=B.CODFILIAL AND IM.CODBANCO=B.CODBANCO " );
		sSQL.append( "AND IM.CODEMPCB=ITR.CODEMPCB AND IM.CODFILIALCB=ITR.CODFILIALCB AND IM.CODCARTCOB=ITR.CODCARTCOB " );
		sSQL.append( "AND CT.CODEMP=IM.CODEMPCT AND CT.CODFILIAL=IM.CODFILIALCT AND CT.NUMCONTA=IM.NUMCONTA " );
		sSQL.append( "AND ITR.CODEMP=R.CODEMP AND ITR.CODFILIAL=R.CODFILIAL AND ITR.CODREC=R.CODREC " );
		sSQL.append( "AND ITR.STATUSITREC IN ('R1','RL','RB','RR') " );
		sSQL.append( "AND MB.CODEMP=? AND MB.CODFILIAL=? AND MB.CODMODBOL=?" );
		sSQL.append( "AND R.CODEMP=? AND R.CODFILIAL=? AND R.CODREC=? AND ITR.nparcitrec=? " );
		sSQL.append( "AND TCO.CODEMP=ITR.CODEMPCB AND TCO.CODFILIAL=ITR.CODFILIALCB AND TCO.CODCARTCOB=ITR.CODCARTCOB ");
		sSQL.append( "AND TCO.CODEMPBO=B.CODEMP AND TCO.CODFILIALBO=B.CODFILIAL AND TCO.CODBANCO=B.CODBANCO ");

		try {

			System.out.println( "QUERY DUPLICATA:" + sSQL.toString() );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 3, txtCodModBol.getVlrInteger() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 6, codRec );
			ps.setInt( 7, parcRec );
			rs = ps.executeQuery();
		
			if ( txtClassModBol.getVlrString() == null || "".equals( txtClassModBol.getVlrString() )) {
				imprimeTexto( TYPE_PRINT.VIEW, rs );
			}
			else {
				imprimeGrafico( TYPE_PRINT.VIEW, rs );
			}
			
			

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados do boleto!" );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void imprimeTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) throws Exception {

		String sVal = null;
		ImprimeOS imp = null;
		imp = new ImprimeOS( "", con );
		imp.verifLinPag();
		imp.setTitulo( "Boleto" );
		String[] sNat = null;
		
		if ( rs.next() ) {
			
			sNat = new String[ 2 ];
			sNat[ 0 ] = "" ;
			sNat[ 1 ] = "" ;
			sVal = FRBoleto.aplicCampos( rs, sNat, FRBoleto.getMoeda() );

			if ( sVal != null ) {

				String[] sLinhas = ( sVal + " " ).split( "\n" );

				for ( int i = 0; i < sLinhas.length; i++ ) {
					if ( i == 0 ) {
						imp.say( imp.pRow() + 1, 0, imp.normal() + imp.comprimido() + "" );
						imp.say( imp.pRow(), 0, sLinhas[ i ] );
					}
					else {
						imp.say( imp.pRow() + 1, 0, sLinhas[ i ] );
					}
				}
			}
		}

		rs.close();
		con.commit();
		
		imp.fechaGravacao();

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			
			imp.preview( owner );
			
		}
		else {
			imp.print();
		}
		
	}

	private HashMap<String, Object> getParametros() {
		
		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		try {

			// Carregando parametros da empresa

			Empresa empresa = new Empresa( con );
	
			parametros.put( "CODEMP", Aplicativo.iCodEmp );
			parametros.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNITRECEBER" ) );
			parametros.put( "CODEMPMB", Aplicativo.iCodEmp );
			parametros.put( "CODFILIALMB", ListaCampos.getMasterFilial( "FNMODBOLETO" ) );
			parametros.put( "IMPDOC", txtImpInst.getVlrString() );
			parametros.put( "CODMODBOL", txtCodModBol.getVlrInteger() );
			parametros.put( "CODEMPRC", Aplicativo.iCodEmp );
			parametros.put( "CODFILIALRC", ListaCampos.getMasterFilial( "FNRECEBER" ) );
			parametros.put( "CODREC", codRec );
			parametros.put( "NPARCITREC", parcRec  );
			parametros.put( "SUBREPORT_DIR", "org/freedom/layout/dup/" );
			parametros.put( "REPORT_CONNECTION", con.getConnection() );
			
	
			if ( empresa != null ) {
				parametros.put( "RAZEMP", empresa.getAll().get( "RAZEMP" ) );
				if (empresa.getAll().get( "LOGOEMP" )!=null) { 
					parametros.put( "LOGOEMP", empresa.getAll().get( "LOGOEMP" ) );
				}
				if (empresa.getCidFilial()!=null) {
					parametros.put( "CIDFILIAL", empresa.getCidFilial());
				}
			}

			// Carregando parametros preferênciais
			
			sql.append( "SELECT COALESCE(P.TPNOSSONUMERO,'D') TPNOSSONUMERO, COALESCE(P.IMPDOCBOL,'N') IMPDOCBOL FROM SGPREFERE1 P " );
			sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=?" );

			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				
				parametros.put( "TPNOSSONUMERO", rs.getString( "TPNOSSONUMERO" ) );
				parametros.put( "IMPDOCBOL", rs.getString( "IMPDOCBOL" ) );
			
			}
			
			rs.close();
			ps.close();			
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return parametros;
	}

	private void imprimeGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		String classBol = "";
		if ( txtClassModBol.getVlrString().indexOf( '/', 0 ) == -1 ) {
			classBol = "layout/bol/" + txtClassModBol.getVlrString();
		}
		else {
			classBol = txtClassModBol.getVlrString();
		}

		FPrinterJob dlGr = new FPrinterJob( classBol, "Boleto", null, rs, getParametros(), owner );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro ao tentar imprimir boleto!" + err.getMessage(), true, con, err );
			}
		}
	}
}
