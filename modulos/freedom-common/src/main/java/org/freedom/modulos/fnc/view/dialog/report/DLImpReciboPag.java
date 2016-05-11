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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JInternalFrame;

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
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.frame.report.FRBoleto;

public class DLImpReciboPag extends FDialogo {

	private static final long serialVersionUID = 1L;

	public JTextFieldPad txtCodModBol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	public JTextFieldPad txtNumCheque = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtImpInst = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtPreImpModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

//	private JTextFieldFK txtClassModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private ListaCampos lcModBol = new ListaCampos( this );

	private int codPag;

	private int parcPag;
	
	private DbConnection con = null;

	private JInternalFrame owner = null;
	
	private ListaCampos lcCheque = new ListaCampos( this );
	
	private Vector<String> numscheques = null;

	public DLImpReciboPag( JInternalFrame owner, DbConnection con, int codRec, int parcRec, Vector<String> numschequesp ) {

		super();
		
		setAtribos( 370, 160 );
		setTitulo( "Impressão de Recibo" );

		this.con = con;
		this.codPag = codRec;
		this.parcPag = parcRec;
		this.owner = owner;
		this.numscheques = numschequesp;
		
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
//		lcModBol.add( new GuardaCampo( txtClassModBol, "ClassModBol", "Classe do modelo", ListaCampos.DB_SI, false ) );
		lcModBol.setReadOnly( true );
		lcModBol.montaSql( false, "MODBOLETO", "FN" );
		txtCodModBol.setTabelaExterna( lcModBol, null );
		txtCodModBol.setFK( true );
		txtCodModBol.setNomeCampo( "CodModBol" );
		lcModBol.setConexao( con );
	}

	private void montaTela() {

		adic( new JLabelPad( "Cód.Mod.rec." ), 7, 10, 80, 20 );
		adic( txtCodModBol, 7, 30, 80, 20 );
		adic( new JLabelPad( "Descrição do modelo de recibo" ), 90, 10, 220, 20 );
		adic( txtDescModBol, 90, 30, 250, 20 );
		
		adic( new JLabelPad( "Cheque" ), 7, 50, 80, 20 );
		adic( txtNumCheque, 7, 70, 80, 20 );
		
		if(numscheques!=null && numscheques.size()>0) {
			txtNumCheque.setVlrString( numscheques.elementAt( 0 ) );
		}
		
		
	}

	public void imprimir() {

		if ( txtCodModBol.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Modelo de recibo não selecionado!" );
			txtCodModBol.requestFocus();
			return;
		}

		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		sSQL.append( "SELECT P.CODPAG codrec, ip.dtvencitpag dtvencitrec,IP.nparcpag nparcitrec,IP.vlrapagitpag vlrapagitrec, p.vlrapagpag vlrapagrec, IP.vlrparcitpag vlrparcitrec, MB.PREIMPMODBOL, ");
		sSQL.append( "IP.vlrdescitpag vlrdescitrec, (IP.vlrjurositpag+IP.vlrmultaitpag) VLRMULTA, P.docpag docrec,P.codbanco, MB.ESPDOCMODBOL ESPDOC, MB.ACEITEMODBOL ACEITE, MB.MDECOB,");
		sSQL.append( "IP.dtitpag dtemitvenda, F.cidfor cidcli,F.uffor ufcli, F.endfor endcli,F.numfor numcli,F.complfor complcli,F.cepfor cepcli,F.bairfor baircli,PF.codmoeda,");
		sSQL.append( "F.pessoafor, (IP.dtvencitpag-CAST('07.10.1997' AS DATE)) FATVENC, M.CODFBNMOEDA,");
		sSQL.append( "MB.DESCLPMODBOL, MB.INSTPAGMODBOL, F.inscfor, mb.txamodbol, ");
		
		sSQL.append( "p.codfor codcli, f.razfor razcli, f.nomefor nomecli, f.cpffor cpfcli, f.rgfor rgcli, f.cnpjfor cnpjcli, f.inscfor insccli, ");
		sSQL.append( "f.endfor endcob, f.numfor numcob, f.complfor complcob, f.bairfor baircob, f.cepfor cepcob, f.cidfor cidcob, f.uffor ufcob, f.fonefor fonecli, f.dddfonefor dddcli, ");
		
		sSQL.append( "(SELECT COUNT(*) FROM FNITPAGAR ITP2 " );
		sSQL.append( "WHERE ITP2.CODPAG=P.CODPAG AND ITP2.CODEMP=P.CODEMP AND ITP2.CODFILIAL=P.CODFILIAL) PARCS, ");
		
		sSQL.append( "null codorc, null nomeconv, ip.obsitpag obsorc, coalesce(cp.doccompra,p.docpag) docvenda, p.codcompra codvenda, p.codpag reciboitrec, null nomevend, null nomevend2, null nomevend3, null nomevend4, " );
		
		if(txtNumCheque.getVlrInteger()>0) {
			sSQL.append( "(select first 1 ch.vlrcheq from fncheque ch where ch.contacheq=(select cv.numconta from fncontavinculada cv where cv.codempcv=ip.codempca and cv.codfilialcv=ip.codfilialca and cv.numcontacv=ip.numconta) and ch.numcheq=" + txtNumCheque.getVlrInteger() + ") vlrpagoitrec, ");	
			sSQL.append( "(select first 1 ch.numcheq from fncheque ch where ch.contacheq=(select cv.numconta from fncontavinculada cv where cv.codempcv=ip.codempca and cv.codfilialcv=ip.codfilialca and cv.numcontacv=ip.numconta) and ch.numcheq=" + txtNumCheque.getVlrInteger() + ") numcheq, ");
		}
		else {
		
			sSQL.append( "ip.vlrpagoitpag vlrpagoitrec, " );
		}
		
		sSQL.append( "coalesce(p.vlrbaseinss,0) baseinss, coalesce(p.vlrbaseirrf,0) baseirrf , coalesce(p.vlrretirrf,0) retirrf, coalesce(p.vlrretinss,0) retinss,");
		sSQL.append( "(((coalesce(p.vlrretinss,0)) * (100.00)) / (coalesce(p.vlrbaseinss,1))) aliqinss,");
		sSQL.append( "cast((((coalesce(p.vlrretirrf,0)) * (100.00)) / (coalesce(p.vlrbaseirrf,1))) as decimal(15,2)) aliqirrf, ");
		
		sSQL.append( "(ip.vlrparcitpag + coalesce(p.vlrretirrf,0) + coalesce(p.vlrretinss,0)) vlrbruto ");
		
		
		sSQL.append( "FROM CPFORNECED F, SGPREFERE1 PF, FNMOEDA M, FNMODBOLETO MB, FNITPAGAR IP, SGFILIAL FI, FNPAGAR P ");

		sSQL.append( "LEFT OUTER JOIN CPCOMPRA CP ON CP.CODEMP=P.CODEMPCP AND CP.CODFILIAL=P.CODFILIALCP AND CP.CODCOMPRA=P.CODCOMPRA " );
		
		sSQL.append( "WHERE ");
		sSQL.append( "F.CODEMP=P.codempfr AND F.CODFILIAL=P.codfilialfr AND F.codfor=P.codfor ");

		sSQL.append( "AND PF.CODEMP=P.CODEMP AND PF.CODFILIAL=P.CODFILIAL ");
		sSQL.append( "AND FI.CODEMP=P.CODEMP AND FI.CODFILIAL=P.CODFILIAL ");

		sSQL.append( "AND M.CODEMP=PF.CODEMPMO AND M.CODFILIAL=PF.CODFILIALMO AND M.CODMOEDA=PF.CODMOEDA ");

		sSQL.append( "AND IP.CODEMP=P.CODEMP AND IP.CODFILIAL=P.CODFILIAL AND IP.codpag=P.codpag ");
		sSQL.append( "AND MB.CODEMP=? AND MB.CODFILIAL=? AND MB.CODMODBOL=? ");
		sSQL.append( "AND P.CODEMP=? AND P.CODFILIAL=? AND P.codpag=? AND IP.nparcpag=? ");
		
		
		try {

			System.out.println( "QUERY DUPLICATA:" + sSQL.toString() );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNMODBOLETO" ) );
			ps.setInt( 3, txtCodModBol.getVlrInteger() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "FNPAGAR" ) );
			ps.setInt( 6, codPag );
			ps.setInt( 7, parcPag );
			rs = ps.executeQuery();
			
			imprimeTexto( TYPE_PRINT.VIEW, rs );

		} catch ( Exception err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados do recibo!" );
		}
	}

	public void imprimeTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) throws Exception {

		String sVal = null;
		ImprimeOS imp = null;
		imp = new ImprimeOS( "", con );
		imp.verifLinPag();
		imp.setTitulo( "Recibo" );

		while ( rs.next() ) {
			
			sVal = FRBoleto.aplicCampos( rs, null, FRBoleto.getMoeda() );

			if ( sVal != null ) {

				String[] sLinhas = ( sVal + " " ).split( "\n" );

				for ( int i = 0; i < sLinhas.length; i++ ) {
					if ( i == 0 ) {
						imp.say( imp.pRow() + 1, 0, imp.normal() + imp.expandido() + "" );
//						imp.say( imp.pRow() + 1, 0  + "" );
						imp.say( imp.pRow() + 1, 0, sLinhas[ i ] ); 
					}
					else {
						imp.say( imp.pRow() + 1, 0, sLinhas[ i ] );
					}
				}
			}
		}

		imp.fechaGravacao();

		if ( bVisualizar==TYPE_PRINT.VIEW ) {

			imp.preview( owner );
		}
		else {
			imp.print();
		}
	}
	
	
	private HashMap<String, Object> getParametros() {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		Empresa empresa = new Empresa( con );

		parametros.put( "CODEMP", Aplicativo.iCodEmp );
		parametros.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		parametros.put( "IMPDOC", txtImpInst.getVlrString() );

		if ( Aplicativo.empresa != null ) {
			parametros.put( "RAZEMP", empresa.getAll().get( "RAZEMP" ) );
//			parametros.put( "RAZEMP", empresa.getAll().get( "RAZEMP" ) );
		}

		return parametros;
	}
/*
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
	*/
}
