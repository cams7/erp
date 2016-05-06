/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRReceber.java <BR>
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
 *                    Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.fnc.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPD;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;

public class FRReceber extends FRelatorio implements RadioGroupListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtDescBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTpCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtDescTpCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbObs = new JCheckBoxPad( "Imprimir observações?", "S", "N" );

	private JCheckBoxPad cbImpTotDia = new JCheckBoxPad( "Imprimir totalizador diário?", "S", "N" );

	private JCheckBoxPad cbRecPar = new JCheckBoxPad( "Imprimir recebimentos parciais?", "S", "N" );

	// private JCheckBoxPad cbAgrupCli = new JCheckBoxPad( "Agrupar por cliente?", "S", "N" );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JRadioGroup<String, String> rgModo = null;

	private JRadioGroup<String, String> rgTipoRel = null;

	private JRadioGroup<String, String> rgOrdem = null;

	private JRadioGroup<String, String> rgOrdem2 = null;

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcTipoCli = new ListaCampos( this );

	private ListaCampos lcSetor = new ListaCampos( this );

	private ListaCampos lcVendedor = new ListaCampos( this );

	private ListaCampos lcBanco = new ListaCampos( this );

	private ListaCampos lcPlanoPag = new ListaCampos( this );

	private ListaCampos lcTipoCob = new ListaCampos( this );
	
	private ListaCampos lcConta = new ListaCampos( this );
	
	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );


	private boolean bPref = false;

	public FRReceber() {

		super( false );
		setTitulo( "Receber/Recebidas" );
		setAtribos( 40, 50, 680, 470 );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		cbObs.setVlrString( "S" );
		cbImpTotDia.setVlrString( "S" );

		montaListaCampos();
		montaRadioGroups();
		montaTela();

	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCnpjCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );

		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, false ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setReadOnly( true );
		txtCodSetor.setTabelaExterna( lcSetor, null );
		txtCodSetor.setFK( true );
		txtCodSetor.setNomeCampo( "CodSetor" );

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco.", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtDescBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setFK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.pl.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );

		txtCodTpCob.setNomeCampo( "CodTipoCob" );
		lcTipoCob.add( new GuardaCampo( txtCodTpCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTpCob, "DescTipoCob", "Descrição do tipo de cobrança.", ListaCampos.DB_SI, false ) );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		lcTipoCob.setQueryCommit( false );
		lcTipoCob.setReadOnly( true );
		txtCodTpCob.setTabelaExterna( lcTipoCob, null );
		txtCodTpCob.setListaCampos( lcTipoCob );
		txtDescTpCob.setListaCampos( lcTipoCob );
		txtCodTpCob.setFK( true );

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );
		lcTipoCli.setReadOnly( true );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, null );
		txtCodTipoCli.setFK( true );
		txtCodTipoCli.setNomeCampo( "CodTipoCli" );
		
		lcConta.add( new GuardaCampo( txtNumConta, "numconta", "Num.Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "descconta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtNumConta.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );
		txtNumConta.setFK( true );
		txtNumConta.setNomeCampo( "numconta" );


	}

	private void montaRadioGroups() {

		Vector<String> vVals = new Vector<String>();
		Vector<String> vLabs = new Vector<String>();
		vVals.addElement( "G" );
		vVals.addElement( "T" );
		vLabs.addElement( "Grafico" );
		vLabs.addElement( "Texto" );
		rgModo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgModo.setVlrString( "G" );

		Vector<String> vVals0 = new Vector<String>();
		Vector<String> vLabs0 = new Vector<String>();
		vLabs0.addElement( "Contas a receber" );
		vLabs0.addElement( "Contas recebidas" );
		vLabs0.addElement( "Ambas as contas" );
		vVals0.addElement( "R" );
		vVals0.addElement( "P" );
		vVals0.addElement( "A" );
		rgTipoRel = new JRadioGroup<String, String>( 3, 1, vLabs0, vVals0 );
		rgTipoRel.addRadioGroupListener( this );

		Vector<String> vVals1 = new Vector<String>();
		Vector<String> vLabs1 = new Vector<String>();
		vLabs1.addElement( "Emissão" );
		vLabs1.addElement( "Vencimento" );
		vLabs1.addElement( "Pagamento" );
		vVals1.addElement( "E" );
		vVals1.addElement( "V" );
		vVals1.addElement( "P" );
		rgOrdem = new JRadioGroup<String, String>( 1, 3, vLabs1, vVals1 );
		rgOrdem.setVlrString( "V" );

		Vector<String> vVals2 = new Vector<String>();
		Vector<String> vLabs2 = new Vector<String>();
		vLabs2.addElement( "Razão Social" );
		vLabs2.addElement( "Documento" );
		vVals2.addElement( "R" );
		vVals2.addElement( "D" );
		rgOrdem2 = new JRadioGroup<String, String>( 1, 2, vLabs2, vVals2 );
		rgOrdem2.setVlrString( "R" );
	}

	private void montaTela() {

		cbRecPar.setVlrString( "S" );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( new JLabelPad( "Periodo:" ), 7, 0, 80, 20 );
		adic( lbLinha, 7, 20, 247, 40 );
		adic( new JLabelPad( "De:", SwingConstants.CENTER ), 17, 30, 30, 20 );
		adic( txtDataini, 47, 30, 80, 20 );
		adic( new JLabelPad( "Até:", SwingConstants.CENTER ), 127, 30, 30, 20 );
		adic( txtDatafim, 157, 30, 80, 20 );
		adic( new JLabelPad( "Modo de exibição:" ), 7, 60, 287, 20 );
		adic( rgModo, 7, 80, 247, 30 );
		adic( new JLabelPad( "Modo:" ), 257, 0, 170, 20 );
		adic( rgTipoRel, 257, 20, 160, 90 );

		adic( cbObs, 420, 15, 180, 20 );
		adic( cbImpTotDia, 420, 40, 180, 20 );
		adic( cbRecPar, 420, 65, 250, 20 );
		// adic( cbAgrupCli, 420, 90, 250, 20 );

		adic( new JLabelPad( "Primeira ordem:" ), 7, 110, 390, 20 );
		adic( rgOrdem, 7, 130, 410, 30 );
		adic( new JLabelPad( "Segunda ordem:" ), 7, 160, 390, 20 );
		adic( rgOrdem2, 7, 180, 410, 30 );

		adic( new JLabelPad( "Cód.cli." ), 7, 220, 80, 20 );
		adic( txtCodCli, 7, 240, 80, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 90, 220, 210, 20 );
		adic( txtRazCli, 90, 240, 210, 20 );
		adic( new JLabelPad( "Cód.setor" ), 7, 260, 80, 20 );
		adic( txtCodSetor, 7, 280, 80, 20 );
		adic( new JLabelPad( "Descrição do setor" ), 90, 260, 210, 20 );
		adic( txtDescSetor, 90, 280, 210, 20 );
		adic( new JLabelPad( "Cód.comis." ), 7, 300, 80, 20 );
		adic( txtCodVend, 7, 320, 80, 20 );
		adic( new JLabelPad( "Nome do comissionado" ), 90, 300, 210, 20 );
		adic( txtNomeVend, 90, 320, 210, 20 );

		adic( new JLabelPad( "Cod.Tp.Cob" ), 303, 220, 80, 20 );
		adic( txtCodTpCob, 303, 240, 80, 20 );
		adic( new JLabelPad( "Descrição do Tipo de Cobrança" ), 386, 220, 220, 20 );
		adic( txtDescTpCob, 386, 240, 220, 20 );

		adic( txtCodBanco, 303, 280, 80, 20, "Cód.banco" );
		adic( txtDescBanco, 386, 280, 220, 20, "Descrição do banco" );

		adic( txtNumConta, 303, 320, 80, 20, "Num.Conta" );
		adic( txtDescConta, 386, 320, 220, 20, "Descrição da conta" );
		
		adic( txtCodPlanoPag, 303, 360, 80, 20, "Cód.pl.pag." );
		adic( txtDescPlanoPag, 386, 360, 220, 20, "Descrição do plano de pagamento" );

		adic( txtCodTipoCli, 7, 360, 80, 20, "Cód.Tipo.Cli" );
		adic( txtDescTipoCli, 90, 360, 210, 20, "Descrição do tipo de cliente" );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String sFrom = " ";
		String sFiltro = "";
		String sTipoRel = null;
		String sTitRel = null;
		String sTitRel1 = null;
		String sDtTotal = "";
		String sDtPago = "";
		String sObs = "";
		String sImpTotDia = "";
		String sCodBanco = null;
		String sNumConta = null;
		String sCodTpCob = null;
		String sCodPlanoPag = null;
		String sCampoTotal = null;
		String sCampoOrdem = null;
		String sCampoOrdem2 = null;
		String sRecParc = cbRecPar.getVlrString();
		
		int iCodCli = 0;
		int iCodSetor = 0;
		int iCodVend = 0;
		int iParans = 0;

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}
		if ( ( ( rgTipoRel.getVlrString().equals( "R" ) ) || ( rgTipoRel.getVlrString().equals( "A" ) ) ) && ( rgOrdem.getVlrString().equals( "P" ) ) ) {
			Funcoes.mensagemInforma( this, "Não pode ser ordenado por data de pagamento!" );
			return;
		}

		sTipoRel = rgTipoRel.getVlrString();

		if ( sTipoRel.equals( "R" ) ) {
			sTitRel = "A RECEBER";
		}
		else if ( sTipoRel.equals( "P" ) ) {
			sTitRel = "RECEBIDAS";
		}
		else if ( sTipoRel.equals( "A" ) ) {
			sTitRel = "A RECEBER/RECEBIDAS";
		}

		if ( rgOrdem.getVlrString().equals( "P" ) ) {
			sTitRel1 = "PAGAMENTO";
			sCampoOrdem = "IT.DTPAGOITREC";
			sCampoTotal = "DTPAGOITREC";
		}
		else if ( rgOrdem.getVlrString().equals( "E" ) ) {
			sTitRel1 = "EMISSÂO";
			sCampoOrdem = "IT.DTITREC";
			sCampoTotal = "DTITREC";
		}
		else {
			sTitRel1 = "VENCIMENTO";
			sCampoOrdem = "IT.DTVENCITREC";
			sCampoTotal = "DTVENCITREC";
		}

		if ( rgOrdem2.getVlrString().equals( "R" ) ) {
			sCampoOrdem2 = "C.RAZCLI";
		}
		else {
			sCampoOrdem2 = "R.DOCREC";
		}

		iCodCli = txtCodCli.getVlrInteger().intValue();
		iCodSetor = txtCodSetor.getVlrInteger().intValue();
		iCodVend = txtCodVend.getVlrInteger().intValue();
		sCodBanco = txtCodBanco.getVlrString();
		sNumConta = txtNumConta.getVlrString();
		sCodPlanoPag = txtCodPlanoPag.getVlrString();
		sCodTpCob = txtCodTpCob.getVlrString();

		if ( iCodCli != 0 ) {
			sWhere.append( " AND R.CODEMPCL=? AND R.CODFILIALCL=? AND R.CODCLI=? " );
			sFiltro = "Cli.: " + iCodCli + " - " + Funcoes.copy( txtRazCli.getVlrString(), 30 ).trim();
		}
		if ( iCodSetor != 0 ) {
			if ( bPref ) {
				sWhere.append( " AND C.CODEMPSR=? AND C.CODFILIALSR=? AND C.CODSETOR=?" );
			}
			else {
				sWhere.append( " AND VD.CODEMPSE=? AND VD.CODFILIALSE=? AND VD.CODSETOR=? ");
//				sWhere.append( " AND VD.CODEMPSE=? AND VD.CODFILIALSE=? AND VD.CODSETOR=? AND  VD.CODEMP=R.CODEMPVD AND VD.CODFILIAL=R.CODFILIALVD AND VD.CODVEND=R.CODVEND " );
//				sFrom = ",VDVENDEDOR VD ";
			}
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Setor: " + iCodSetor + " - " + Funcoes.copy( txtDescSetor.getVlrString(), 30 ).trim();
		}
		if ( iCodVend != 0 ) {
			sWhere.append( " AND R.CODEMPVD=? AND R.CODFILIALVD=? AND R.CODVEND=? " );
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Repr.: " + iCodVend + " - " + Funcoes.copy( txtNomeVend.getVlrString(), 30 ).trim();
		}
		if ( sCodBanco.length() > 0 ) {
			sWhere.append( " AND IT.CODEMPBO=? AND IT.CODFILIALBO=? AND IT.CODBANCO=? " );
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Repr.: " + sCodBanco + " - " + Funcoes.copy( txtCodBanco.getVlrString(), 30 ).trim();
		}
		if ( sNumConta.length() > 0 ) {
			sWhere.append( " AND IT.CODEMPCA=? AND IT.CODFILIALCA=? AND IT.NUMCONTA=? " );
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Conta.: " + sNumConta + " - " + Funcoes.copy( txtDescConta.getVlrString(), 30 ).trim();
		}
		if ( sCodPlanoPag.length() > 0 ) {
			sWhere.append( " AND R.CODEMPPG=? AND R.CODFILIALPG=? AND R.CODPLANOPAG=? " );
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Repr.: " + sCodPlanoPag + " - " + Funcoes.copy( txtCodPlanoPag.getVlrString(), 30 ).trim();
		}
		if ( sCodTpCob.length() > 0 ) {
			sWhere.append( "AND IT.CODEMPTC=? AND IT.CODFILIALTC=? AND IT.CODTIPOCOB=? " );
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Repr.: " + sCodTpCob + " - " + Funcoes.copy( txtCodTpCob.getVlrString(), 30 ).trim();
		}

		if ( txtCodTipoCli.getVlrInteger() > 0 ) {
			sWhere.append( "AND C.CODEMPTI=? AND C.CODFILIALTI=? AND C.CODTIPOCLI=? " );
			sFiltro += ( !sFiltro.equals( "" ) ? " / " : "" ) + "Tipo Cli.: " + txtCodTipoCli.getVlrString() + " - " + Funcoes.copy( txtDescTipoCli.getVlrString(), 30 ).trim();
		}
		
		sSQL.append( "SELECT IT.NPARCITREC, IT.CODREC, IT.DTITREC, IT.DTVENCITREC,IT.NPARCITREC,R.CODVENDA,R.CODCLI,C.RAZCLI, IT.DTPAGOITREC, R.DOCREC, " );
		sSQL.append( "coalesce(L.HISTSUBLANCA,IT.OBSITREC) OBSITREC, V.STATUSVENDA, IT.VLRPARCITREC");
		sSQL.append( ", (CASE WHEN COALESCE(IT.DESCPONT,'N')='S' ");
		sSQL.append( " AND IT.DTLIQITREC IS NULL AND (CAST('today' AS DATE)-IT.DTVENCITREC)>0 THEN IT.VLRITREC+COALESCE(IT.VLRDESCITREC,0) " );
		sSQL.append( "ELSE IT.VLRITREC END ) VLRITREC "); 
		sSQL.append( ", (CASE WHEN COALESCE(IT.DESCPONT,'N')='S' ");
		sSQL.append( " AND IT.DTLIQITREC IS NULL AND (CAST('today' AS DATE)-IT.DTVENCITREC)>0 THEN IT.VLRAPAGITREC+COALESCE(IT.VLRDESCITREC,0) " );
		sSQL.append( "ELSE IT.VLRAPAGITREC END) VLRAPAGITREC, COALESCE(L.VLRSUBLANCA*-1 , IT.VLRPAGOITREC) VLRPAGOITREC, ");
		sSQL.append( "IT.VLRPAGOITREC AS VLRPAGOITRECTOT, L.DATASUBLANCA DATALANCA FROM FNITRECEBER IT " );
		sSQL.append( "LEFT OUTER JOIN FNRECEBER R ON (IT.CODEMP = R.CODEMP AND IT.CODFILIAL = R.CODFILIAL AND IT.CODREC = R.CODREC) " );
		sSQL.append( "LEFT OUTER JOIN VDCLIENTE C ON (C.CODEMP = R.CODEMP AND C.CODFILIAL = R.CODFILIAL AND C.CODCLI = R.CODCLI) " );
		sSQL.append( "LEFT OUTER JOIN VDVENDA V ON (V.CODEMP = R.CODEMP AND V.CODFILIAL = R.CODFILIALVA AND V.CODVENDA = R.CODVENDA AND V.TIPOVENDA = R.TIPOVENDA ) " );
		sSQL.append( "LEFT OUTER JOIN FNSUBLANCA L ON (L.CODEMP = R.CODEMP AND L.CODFILIAL = IT.CODFILIAL AND L.CODREC = IT.CODREC AND L.NPARCITREC = IT.NPARCITREC AND L.CODSUBLANCA<>0)" );
		
		if(iCodSetor != 0 && !bPref ){
			sSQL.append( "LEFT OUTER JOIN VDVENDEDOR VD on(VD.CODEMP = R.CODEMPVD AND VD.CODFILIAL = R.CODFILIALVD AND VD.CODVEND = R.CODVEND )" );
		}
		
		sSQL.append( "WHERE R.CODEMP = ? AND R.CODFILIAL = ? AND "+ sCampoOrdem +" BETWEEN ? AND ? " );
		sSQL.append( "AND IT.STATUSITREC IN (?,?,?) " );
		sSQL.append( "AND R.FLAG IN " + AplicativoPD.carregaFiltro( con, org.freedom.library.swing.frame.Aplicativo.iCodEmp ) + " ");

		sSQL.append( sWhere.toString() );
		sSQL.append( " ORDER BY " + sCampoOrdem + " ," + sCampoOrdem2 );

		try {
			iParans = 1;
			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( iParans++, Aplicativo.iCodEmp );
			ps.setInt( iParans++, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setDate( iParans++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iParans++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			StringBuilder sCab = new StringBuilder();
			sCab.append( "CONTAS ");
			sCab.append( sTitRel ); 
			sCab.append( " DE :" );
			sCab.append( txtDataini.getVlrString() );
			sCab.append( " ATE: " );
			sCab.append( txtDatafim.getVlrString() );
			sCab.append( " POR: " );
			sCab.append( sTitRel1 );

			if ( sNumConta.length() > 0 ) {
				sCab.append( " - CONTA: " + sNumConta );
			}
			
			if ( sTipoRel.equals( "R" ) ) {
				ps.setString( iParans++, "R1" );
				if ("S".equals( sRecParc )) {
					ps.setString( iParans++, "RL" );
					ps.setString( iParans++, "RL" );
				} else {
					ps.setString( iParans++, "R1" );
					ps.setString( iParans++, "R1" );
				}
			}
			else if ( sTipoRel.equals( "P" ) ) {
				ps.setString( iParans++, "RP" );
				if ("S".equals( sRecParc )) {
					ps.setString( iParans++, "RL" );
					ps.setString( iParans++, "RL" );
				} else {
					ps.setString( iParans++, "RP" );
					ps.setString( iParans++, "RP" );
				}
			}
			else if ( sTipoRel.equals( "A" ) ) {
				ps.setString( iParans++, "R1" );
				ps.setString( iParans++, "RL" );
				ps.setString( iParans++, "RP" );
			}

			if ( iCodCli != 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
				ps.setInt( iParans++, iCodCli );
			}
			if ( iCodSetor != 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "VDSETOR" ) );
				ps.setInt( iParans++, iCodSetor );
			}
			if ( iCodVend != 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
				ps.setInt( iParans++, iCodVend );
			}
			if ( sCodBanco.length() > 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( iParans++, sCodBanco );
			}
			if ( sNumConta.length() > 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "FNCONTA" ) );
				ps.setString( iParans++, sNumConta );
			}

			if ( sCodPlanoPag.length() > 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
				ps.setString( iParans++, sCodPlanoPag );
			}
			if ( sCodTpCob.length() > 0 ) {
				ps.setInt( iParans++, Aplicativo.iCodEmp );
				ps.setInt( iParans++, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
				ps.setString( iParans++, sCodTpCob );
			}
			if ( txtCodTipoCli.getVlrInteger() > 0 ) {
				ps.setInt( iParans++, lcTipoCli.getCodEmp() );
				ps.setInt( iParans++, lcTipoCli.getCodFilial() );
				ps.setInt( iParans++, txtCodTipoCli.getVlrInteger() );
			}

			rs = ps.executeQuery();

			if ( "G".equals( rgModo.getVlrString() ) ) {
				imprimirGrafico( bVisualizar, rs, sCab.toString() );
			}
			else {
				imprimirTexto( bVisualizar, rs, sCab.toString(), sCampoTotal );
			}

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de contas a receber!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNPAGAR" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "TOTDIARIO", cbImpTotDia.getVlrString() );

		
		dlGr = new FPrinterJob( "relatorios/ReceberRecebidas.jasper", "Relatório de contas", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório por razão!" + err.getMessage(), true, con, err );
			}
		}
	}

	private void imprimirTexto( TYPE_PRINT bVisualizar, ResultSet rs, String sCab, String sCampoTotal ) {

		String sDtTotal = "";
		String sDtPago = "";
		Vector<String> vObs = null;
		ImprimeOS imp = null;
		int linPag = 0;
		double deTotalDiaParc = 0;
		double deTotalDiaPago = 0;
		double deTotalDiaApag = 0;
		double deTotParc = 0;
		double deTotalPago = 0;
		double deTotalApag = 0;
		boolean bFimDia = false;
		int codrec = -1;
		int nparcitrec = -1;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de contas" );
			imp.addSubTitulo( sCab );

			while ( rs.next() ) {
				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {
					imp.impCab( 136, true );
					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Vencto.    |" );
					imp.say( 15, " Cliente                                  |" );
					imp.say( 59, " Doc.      |" );
					imp.say( 72, " Vlr. da Parc. |" );
					imp.say( 89, " Vlr Recebido  |" );
					imp.say( 106, " Vlr Aberto   |" );
					imp.say( 122, " Data Receb. |" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}

				if ( ( !StringFunctions.sqlDateToStrDate( rs.getDate( sCampoTotal ) ).equals( sDtTotal ) ) && ( bFimDia ) && ( cbImpTotDia.getVlrString().equals( "S" ) ) ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" );
					imp.say( 41, "Totais do Dia-> | " + sDtTotal + " | " + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( deTotalDiaParc ) ) + " | " + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( deTotalDiaPago ) ) + " | "
							+ Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( deTotalDiaApag ) ) + " | " );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					deTotalDiaParc = 0;
					deTotalDiaPago = 0;
					deTotalDiaApag = 0;
					bFimDia = false;
				}

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );

				if ( ( !"V".equals( rgOrdem.getVlrString() ) ) || ( !StringFunctions.sqlDateToStrDate( rs.getDate( sCampoTotal ) ).equals( sDtTotal ) ) ) {
					imp.say( 3, StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencItRec" ) ) );
				}

				imp.say( 14, "| " + Funcoes.copy( rs.getString( "CodCli" ), 0, 6 ) + "-" + Funcoes.copy( rs.getString( "RazCli" ), 0, 33 ) + " |" );

				if ( rs.getString( "DtPagoItRec" ) != null ) {
					sDtPago = StringFunctions.sqlDateToStrDate( rs.getDate( "DtPagoItRec" ) );
				}
				else {
					sDtPago = " ";
				}

				sDtPago = Funcoes.copy( sDtPago, 0, 10 );

				imp.say( 61, ( Funcoes.copy( rs.getString( 10 ), 0, 1 ).equals( "P" ) ? Funcoes.copy( rs.getString( "CodVenda" ), 0, 6 ) : Funcoes.copy( rs.getString( "DocRec" ), 0, 6 ) ) + "/" + 
						Funcoes.copy( rs.getString( "NParcItRec" ), 0, 2 ) + "| " +
						( (codrec!=rs.getInt( "CODREC" ) ) || (nparcitrec!=rs.getInt( "NPARCITREC" )) ?
						Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrItRec" ) ) : StringFunctions.replicate( " ", 14 ))
						+ " | " + Funcoes.strDecimalToStrCurrency( 14, 2, rs.getString( "VlrPagoItRec" ) ) + 
						" | " +
						( (codrec!=rs.getInt( "CODREC" ) ) || (nparcitrec!=rs.getInt( "NPARCITREC" )) ? 
						Funcoes.strDecimalToStrCurrency( 13, 2, rs.getString( "VlrApagItRec" ) ) : StringFunctions.replicate( " ", 13 ) )
						+ " | " + " " + sDtPago + "  |" );
				if ( cbObs.getVlrString().equals( "S" ) ) {
					if ( rs.getString( "OBSITREC" ) != null ) {
						vObs = getObs( rs.getString( "OBSITREC" ), 108 );
						for ( int i = 0; i < vObs.size(); i++ ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 0, "|" );
							imp.say( 16, ( i == 0 ? "OBS.: " : "      " ) + vObs.elementAt( i ).toString() );
							imp.say( 135, "|" );
						}
					}
				}
				if ( (codrec!=rs.getInt( "CODREC" ) ) || (nparcitrec!=rs.getInt( "NPARCITREC" )) ) {
					if ( rs.getString( "VlrItRec" ) != null ) {
						deTotalDiaParc += rs.getDouble( "VlrItRec" );
						deTotParc += rs.getDouble( "VlrItRec" );
					}
					if ( rs.getString( "VlrApagItRec" ) != null ) {
						deTotalDiaApag += rs.getDouble( "VlrApagItRec" );
						deTotalApag += rs.getDouble( "VlrApagItRec" );
					}
				}
				if ( rs.getString( "VlrPagoItRec" ) != null ) {
					deTotalDiaPago += rs.getDouble( "VlrPagoItRec" );
					deTotalPago += rs.getDouble( "VlrPagoItRec" );
				}
				codrec = rs.getInt( "CODREC" );
				nparcitrec = rs.getInt( "NPARCITREC" );

				bFimDia = true;
				sDtTotal = StringFunctions.sqlDateToStrDate( rs.getDate( sCampoTotal ) );
			}

			if ( ( bFimDia ) && ( cbImpTotDia.getVlrString().equals( "S" ) ) ) {
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" );
				imp.say( 41, "Totais do Dia-> | " + sDtTotal + " | " + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( deTotalDiaParc ) ) + " | " + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( deTotalDiaPago ) ) + " | "
						+ Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( deTotalDiaApag ) ) );
				imp.say( 135, "|" );
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" + StringFunctions.replicate( "=", 133 ) + "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|" );
			imp.say( 55, "Totais Geral-> | " + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( deTotParc ) ) + " | " + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( deTotalPago ) ) + " | " + Funcoes.strDecimalToStrCurrency( 13, 2, String.valueOf( deTotalApag ) ) );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	private Vector<String> getObs( String sObs, int iTam ) {

		Vector<String> vRetorno = null;
		boolean bFim = false;
		try {
			sObs = sObs.replaceAll( ( (char) 13 ) + "", " " );
			sObs = sObs.replaceAll( ( (char) 10 ) + "", "" );
			sObs = sObs.trim();
			vRetorno = new Vector<String>();
			if ( !sObs.equals( "" ) ) {
				while ( !bFim ) {
					if ( sObs.length() <= iTam ) {
						vRetorno.addElement( sObs );
						bFim = true;
					}
					else {
						vRetorno.addElement( sObs.substring( 0, iTam ) );
						sObs = sObs.substring( iTam );
					}
				}
			}
		} finally {
			sObs = null;
			bFim = false;
		}
		return vRetorno;
	}

	private boolean getPrefere() {

		boolean retorno = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT SETORVENDA FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno = rs.getString( "SETORVENDA" ) != null && "CA".indexOf( rs.getString( "SETORVENDA" ) ) >= 0;
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao verificar preferências!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

		return retorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
		lcSetor.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcBanco.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcTipoCob.setConexao( cn );
		lcTipoCli.setConexao( cn );
		lcConta.setConexao( cn );
		bPref = getPrefere();
	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if ( evt.getIndice() == 1 ) {
			rgOrdem.setVlrString( "P" );
		}
		else {
			rgOrdem.setVlrString( "V" );
		}
	}
}
