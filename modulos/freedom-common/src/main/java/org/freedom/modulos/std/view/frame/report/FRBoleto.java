/**
 * @version 19/12/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRBoleto.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Empresa;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Extenso;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

import sun.awt.image.ToolkitImage;

public class FRBoleto extends FRelatorio implements CarregaListener {

	private static final long serialVersionUID = 1L;

	public JTextFieldPad txtCodModBol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtPreImpModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtClassModBol = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	public JTextFieldPad txtCodVenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	public JTextFieldPad txtCodVenda2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtTipoVenda = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtTipoVenda2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 1, 0 );

	public JTextFieldFK txtDocVenda = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldFK txtDataVenda = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	public JTextFieldFK txtDocVenda2 = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldFK txtDataVenda2 = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodCli2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldFK txtRazCli2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtParc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldFK txtNomeBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTpCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTpCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodCartCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtDescCartCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtImpInst = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	// private JCheckBoxPad cbTipoImp = new JCheckBoxPad("Impressão gráfica","S","N");

	private ListaCampos lcModBol = new ListaCampos( this );

	private ListaCampos lcVenda = new ListaCampos( this );

	private ListaCampos lcVenda2 = new ListaCampos( this );

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcCli2 = new ListaCampos( this );

	private ListaCampos lcBanco = new ListaCampos( this );

	private ListaCampos lcTipoCob = new ListaCampos( this );

	private final ListaCampos lcCartCob = new ListaCampos( this, "CB" );
	
	private ListaCampos lcTipoMov = new ListaCampos( this );

	private JInternalFrame fExt = null;

//	private String sInfoMoeda[] = new String[ 4 ];

	private List<?> lsParcelas = null;

	private boolean bCodOrc = false;

	private boolean bNomeConv = false;

	private boolean bObsOrc = false;

	private boolean bAltParcela = false;
	
	private String tpnossonumero = "D";
	
	private String impdocbol = "N";

	private JTablePad tbBoletos = new JTablePad();

	private JScrollPane scrol = new JScrollPane( tbBoletos );

	private JPanelPad pnCampos = new JPanelPad();

	private JPanelPad pnTabela = new JPanelPad( new BorderLayout() );

	private JButtonPad btGerar = new JButtonPad( "Montar boletos", Icone.novo( "btGerar.png" ) );

	private Checkbox cbTab = new Checkbox();
	
	private String[] moeda = null;

	public FRBoleto() {

		this( null );
	}

	public FRBoleto( JInternalFrame fExt ) {

		setTitulo( "Impressão de boleto/recibo" );
		setAtribos( 80, 80, 570, 650 );

		this.fExt = fExt;

		montaListaCampos();
		montaTela();

		// txtCodVenda.setRequerido( true );

		Calendar cal = Calendar.getInstance();
		txtDtFim.setVlrDate( cal.getTime() );
		txtDtIni.setVlrDate( cal.getTime() );
		btGerar.addActionListener( this );

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

		/*********
		 * VENDA *
		 *********/

		lcVenda.add( new GuardaCampo( txtCodVenda, "CodVenda", "Cód.venda", ListaCampos.DB_PK, false ) );
		lcVenda.add( new GuardaCampo( txtDocVenda, "DocVenda", "Doc.", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtDataVenda, "DtEmitVenda", "Data", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true ) );
		lcVenda.add( new GuardaCampo( txtTipoVenda, "TipoVenda", "Tipo Venda", ListaCampos.DB_SI, false ) );
		lcVenda.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_FK, txtNomeBanco, false ) );
		lcVenda.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob", ListaCampos.DB_FK, txtDescCartCob, false ) );
		lcVenda.setReadOnly( true );
		lcVenda.montaSql( false, "VENDA", "VD" );
		txtCodVenda.setTabelaExterna( lcVenda, null );
		txtCodVenda.setFK( true );
		txtCodVenda.setNomeCampo( "CodVenda" );

		/***********
		 * CLIENTE *
		 ***********/

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		txtCodCli.setTabelaExterna( lcCli, null );

		/************
		 * VENDA 2 *
		 ************/

		lcVenda2.add( new GuardaCampo( txtCodVenda2, "CodVenda", "Cód.venda", ListaCampos.DB_PK, false ) );
		lcVenda2.add( new GuardaCampo( txtDocVenda2, "DocVenda", "Doc.", ListaCampos.DB_SI, false ) );
		lcVenda2.add( new GuardaCampo( txtDataVenda2, "DtEmitVenda", "Data", ListaCampos.DB_SI, false ) );
		lcVenda2.add( new GuardaCampo( txtCodCli2, "CodCli", "Cód.cli.", ListaCampos.DB_FK, true ) );
		lcVenda2.add( new GuardaCampo( txtTipoVenda2, "TipoVenda", "Tipo Venda", ListaCampos.DB_SI, false ) );
		lcVenda2.setReadOnly( true );
		lcVenda2.montaSql( false, "VENDA", "VD" );
		txtCodVenda2.setTabelaExterna( lcVenda2, null );
		txtCodVenda2.setFK( true );
		txtCodVenda2.setNomeCampo( "CodVenda" );

		/*************
		 * CLIENTE 2 *
		 *************/

		lcCli2.add( new GuardaCampo( txtCodCli2, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli2.add( new GuardaCampo( txtRazCli2, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli2.setReadOnly( true );
		lcCli2.montaSql( false, "CLIENTE", "VD" );
		txtCodCli2.setTabelaExterna( lcCli2, null );

		/*********
		 * BANCO *
		 *********/

		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcBanco.add( new GuardaCampo( txtNomeBanco, "NomeBanco", "Nome do Banco", ListaCampos.DB_SI, false ) );
		lcBanco.setReadOnly( true );
		lcBanco.montaSql( false, "BANCO", "FN" );
		txtCodBanco.setTabelaExterna( lcBanco, null );
		txtCodBanco.setPK( true );
		txtCodBanco.setNomeCampo( "CodBanco" );
		txtCodBanco.setListaCampos( lcBanco );

		/********************
		 * TIPO DE COBRANÇA *
		 ********************/

		lcTipoCob.add( new GuardaCampo( txtCodTpCob, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, false ) );
		lcTipoCob.add( new GuardaCampo( txtDescTpCob, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, false ) );
		lcTipoCob.setReadOnly( true );
		lcTipoCob.montaSql( false, "TIPOCOB", "FN" );
		txtCodTpCob.setTabelaExterna( lcTipoCob, null );
		txtCodTpCob.setPK( true );
		txtCodTpCob.setNomeCampo( "CodTipoCob" );
		txtCodTpCob.setListaCampos( lcTipoCob );

		/************************
		 * CARTEIRA DE COBRANÇA *
		 ************************/

		txtCodCartCob.setNomeCampo( "CodCartCob" );
		lcCartCob.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob", ListaCampos.DB_PK, false ) );
		lcCartCob.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, false ) );
		lcCartCob.add( new GuardaCampo( txtDescCartCob, "DescCartCob", "Desc.Cart.Cob", ListaCampos.DB_SI, false ) );
		lcCartCob.setDinWhereAdic( "CODBANCO = #S", txtCodBanco );
		lcCartCob.montaSql( false, "CARTCOB", "FN" );
		lcCartCob.setQueryCommit( false );
		lcCartCob.setReadOnly( true );
		txtCodCartCob.setTabelaExterna( lcCartCob, null );
		txtCodCartCob.setListaCampos( lcCartCob );
		txtDescCartCob.setListaCampos( lcCartCob );
		txtCodCartCob.setFK( true );

		/************************
		 * TIPO DE MOVIMENTO *
		 ************************/

		txtCodTipoMov.setNomeCampo( "CodTipoMov" );
		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, null );
		txtCodTipoMov.setFK( true );
		
	}

	private void montaTela() {

		pnCampos.setPreferredSize( new Dimension( 200, 350 ) );
		pnCliente.add( pnCampos, BorderLayout.NORTH );
		setPainel( pnCampos );

		adic( new JLabelPad( "Cód.mod." ), 7, 0, 300, 20 );
		adic( txtCodModBol, 7, 20, 80, 20 );
		adic( new JLabelPad( "Descrição do modelo" ), 90, 0, 430, 20 );
		adic( txtDescModBol, 90, 20, 430, 20 );
		adic( new JLabelPad( "Pedido de" ), 7, 40, 80, 20 );
		adic( txtCodVenda, 7, 60, 80, 20 );
		adic( new JLabelPad( "Doc." ), 90, 40, 97, 20 );
		adic( txtDocVenda, 90, 60, 97, 20 );
		adic( new JLabelPad( "Data" ), 190, 40, 97, 20 );
		adic( txtDataVenda, 190, 60, 97, 20 );
		adic( new JLabelPad( "Cliente" ), 290, 40, 230, 20 );
		adic( txtRazCli, 290, 60, 230, 20 );
		adic( new JLabelPad( "Pedido até" ), 7, 80, 80, 20 );
		adic( txtCodVenda2, 7, 100, 80, 20 );
		adic( new JLabelPad( "Doc." ), 90, 80, 97, 20 );
		adic( txtDocVenda2, 90, 100, 97, 20 );
		adic( new JLabelPad( "Data" ), 190, 80, 97, 20 );
		adic( txtDataVenda2, 190, 100, 97, 20 );
		adic( new JLabelPad( "Cliente" ), 290, 80, 230, 20 );
		adic( txtRazCli2, 290, 100, 230, 20 );
		adic( new JLabelPad( "Nro.parcela" ), 7, 120, 80, 20 );
		adic( txtParc, 7, 140, 80, 20 );
		adic( new JLabelPad( "Cód.banco" ), 90, 120, 80, 20 );
		adic( txtCodBanco, 90, 140, 80, 20 );
		adic( new JLabelPad( "Nome do banco" ), 173, 120, 348, 20 );
		adic( txtNomeBanco, 173, 140, 348, 20 );
		adic( new JLabelPad( "Cód.tp.cob." ), 7, 160, 300, 20 );
		adic( txtCodTpCob, 7, 180, 80, 20 );
		adic( new JLabelPad( "Descrição do tipo de cobrança" ), 90, 160, 430, 20 );
		adic( txtDescTpCob, 90, 180, 430, 20 );
		adic( new JLabelPad( "Cód.Tipo.Mov" ), 7, 240, 150, 20 );
		adic( txtCodTipoMov, 7, 263, 80, 20 );
		adic( new JLabelPad( "Descrição do tipo de movimento" ), 90, 240, 430, 20 );
		adic( txtDescTipoMov, 90, 263, 430, 20 );
		adic( new JLabelPad( "Cód.cart.cob." ), 7, 200, 80, 20 );
		adic( txtCodCartCob, 7, 220, 80, 20 );
		adic( new JLabelPad( "Descrição da carteira de cobrança" ), 90, 200, 347, 20 );
		adic( txtDescCartCob, 90, 220, 347, 20 );
		adic( new JLabelPad( "Num. Conta" ), 440, 200, 80, 20 );
		adic( txtNumConta, 440, 220, 80, 20 );

		JLabel periodo = new JLabel( "Período (Emissão)", SwingConstants.CENTER );
		periodo.setOpaque( true );

		adic( periodo, 25, 295, 150, 20 );
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		adic( borda, 7, 300, 300, 45 );
		adic( txtDtIni, 25, 315, 110, 20 );
		adic( new JLabel( "até", SwingConstants.CENTER ), 135, 315, 40, 20 );
		adic( txtDtFim, 175, 315, 110, 20 );

		adic( btGerar, 370, 305, 150, 30 );

		pnCliente.add( pnTabela, BorderLayout.CENTER );
		setPainel( pnTabela );

		pnTabela.add( scrol, BorderLayout.CENTER );

		tbBoletos.adicColuna( " " );
		tbBoletos.adicColuna( "Pedido" );
		tbBoletos.adicColuna( "Dt. emissão" );
		tbBoletos.adicColuna( "Dt. vencimento" );
		tbBoletos.adicColuna( "Razão social do cliente" );
		tbBoletos.adicColuna( "Valor" );

		tbBoletos.setTamColuna( 5, 0 );
		tbBoletos.setTamColuna( 90, 2 );
		tbBoletos.setTamColuna( 90, 3 );
		tbBoletos.setTamColuna( 165, 4 );
		tbBoletos.setTamColuna( 100, 5 );
		
		txtNumConta.setSoLeitura( true );
		lcCartCob.addCarregaListener( this );

	}

	public static String aplicaTxtObs( String sTxa, final String sCampo, String sValor ) {

		String retorno = "";
		String sParam1 = "";
		String sParam2 = "";
		Integer iNumLinObs = new Integer( 0 );
		Integer iNumColObs = new Integer( 0 );
		List<String> lObs = null;

		int iposini = sTxa.indexOf( sCampo );

		if ( iposini > -1 ) {
			sParam1 = sTxa.substring( iposini + 8, iposini + 11 );
			sParam2 = sTxa.substring( iposini + 12, iposini + 15 );

			iNumLinObs = new Integer( sParam1 ).intValue();
			iNumColObs = new Integer( sParam2 ).intValue();
		}

		if ( sValor != null ) {

			try {

				lObs = (List<String>) Funcoes.stringToVector( sValor, "\n" );

				if ( lObs.size() >= iNumLinObs ) {
					try {
						lObs = lObs.subList( iNumLinObs - 1, iNumLinObs );
					} catch ( Exception e ) {
						// VERIFICAR ERRO COM URGENCIA E TRATÁ-LO;
					}
				}
				else {
					while ( lObs.size() < iNumLinObs ) {
						lObs.add( "" );
					}
				}

				String sLinhaObs;
				String sLinhasObs = "";

				for ( int i = 0; i < lObs.size(); i++ ) {
					sLinhaObs = lObs.get( i ).toString();
					sLinhaObs = sLinhaObs.length() > iNumColObs ? sLinhaObs.substring( 0, iNumColObs ) : sLinhaObs;
					if ( i == 0 ) {
						sLinhasObs = sLinhaObs;
					}
					else {
						sLinhasObs = sLinhasObs + "\n" + sLinhaObs;
					}
				}

				retorno = sTxa.replaceAll( "\\" + sCampo + sParam1 + "_" + sParam2 + "]", sLinhasObs );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		else {
			retorno = sTxa.replaceAll( "\\" + sCampo + sParam1 + "_" + sParam2 + "]", "" );
			// retorno = sTxa;
		}
		return retorno;
	}

	public static String aplicCampos( ResultSet rs, String[] sNat , String[] sInfoMoeda) {

		Date dCampo = null;
		String sRet = null;
		String sTxa = null;
		String sCampo = null;
		String sParam1 = null;
		String sParam2 = null;

		try {

			sTxa = rs.getString( "TxaModBol" );
			sCampo = "";
			dCampo = null;
			String sObsOrc;
			String sObsVen;
			List<String> lObsOrc;
			int iNumLinObs = 0;
			int iNumColObs = 0;

			// Aplicando campos de dados:
			// Estes '\\' que aparecem por ai..são para anular caracteres especiais de "expressão regular".

			if ( sTxa != null ) {

				if ( ( sCampo = rs.getString( "CODORC" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_CODORC_]", sCampo );

				if ( ( sCampo = rs.getString( "NOMECONV" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_____________________NOMECONV___________________]", sCampo );

				sParam1 = "";
				sParam2 = "";

				sTxa = aplicaTxtObs( sTxa, "[OBSORC_", rs.getString( "OBSORC" ) );

				while ( sTxa.indexOf( "[OBSVEN_" ) > -1 ) {
					sTxa = aplicaTxtObs( sTxa, "[OBSVEN_", rs.getString( "OBSVENDA" ) );
				}
				if ( ( dCampo = rs.getDate( "DtVencItRec" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[VENCIMEN]", StringFunctions.sqlDateToStrDate( dCampo ) );
				if ( ( dCampo = rs.getDate( "DtEmitVenda" ) ) != null ) {
					sTxa = sTxa.replaceAll( "\\[DATADOC_]", StringFunctions.sqlDateToStrDate( dCampo ) );
					sTxa = sTxa.replaceAll( "\\[DIA_E]", StringFunctions.strZero( String.valueOf( Funcoes.getDiaMes( Funcoes.sqlDateToDate( dCampo ) ) ), 2 ) );
					sTxa = sTxa.replaceAll( "\\[MES_E]", Funcoes.getMesExtenso( Funcoes.sqlDateToDate( dCampo ) ) );
					sTxa = sTxa.replaceAll( "\\[ANO_E]", StringFunctions.strZero( String.valueOf( Funcoes.getAno( Funcoes.sqlDateToDate( dCampo ) ) ), 4 ) );
				}
				if ( ( sCampo = rs.getString( "CodRec" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[CODREC]", Funcoes.alinhaDir( sCampo, 8 ) );
				if ( ( sCampo = rs.getString( "DocVenda" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[__DOCUMENTO__]", Funcoes.copy( StringFunctions.strZero( sCampo.trim(), 8), 15 ) );
				if ( ( sCampo = rs.getString( "ReciboItRec" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[RECIBO]", Funcoes.alinhaDir( sCampo, 8 ) );
				if ( ( sCampo = rs.getString( "NParcItRec" ) ) != null ) {
					sTxa = sTxa.replaceAll( "\\[P]", Funcoes.alinhaDir( sCampo, 2 ) );
					if ( rs.getInt( 1 ) > 1 )
						sTxa = sTxa.replaceAll( "\\[A]", "" + ( (char) ( rs.getInt( "NParcItRec" ) + 64 ) ) );
				}
				if ( ( sCampo = rs.getInt( "PARCS" ) + "" ) != null )
					sTxa = sTxa.replaceAll( "\\[T]", "/" + Funcoes.copy( sCampo, 0, 2 ) );
				if ( ( sCampo = rs.getString( "VlrParcItRec" ) ) != null && rs.getDouble( "VlrParcItRec" ) != 0 ) {
					sTxa = sTxa.replaceAll( "\\[VALOR_DOCUMEN]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
					sTxa = sTxa.replaceAll( "\\[VALOR_EXTENSO]", Extenso.extenso( rs.getDouble( "VlrParcItRec" ), sInfoMoeda[ 0 ], sInfoMoeda[ 1 ], sInfoMoeda[ 2 ], sInfoMoeda[ 3 ] ) ).toUpperCase();
				}
				if ( ( sCampo = rs.getString( "VlrApagItRec" ) ) != null && rs.getDouble( "VlrApagItRec" ) != 0 ) {
					sTxa = sTxa.replaceAll( "\\[VLIQ_DOCUMENT]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
				}
				try {
					if ( ( sCampo = rs.getString( "VlrPagoItRec" ) ) != null && rs.getDouble( "VlrPagoItRec" ) != 0 ) {
						sTxa = sTxa.replaceAll( "\\[VPAGO_DOCUMENT]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
					}
				}
				catch (Exception e) {
					System.out.println("Protegendo, caso não exista a coluna na query");
				}
				
				try {
					if ( ( sCampo = rs.getString( "NumCheq" ) ) != null && rs.getDouble( "VlrParcItRec" ) != 0 ) {
						sTxa = sTxa.replaceAll( "\\[NUM_CHEQ]", Funcoes.copy( StringFunctions.strZero( sCampo.trim(), 8), 15 ) );
					}
				}
				catch (Exception e) {
					System.out.println("Protegendo, caso não exista a coluna na query");
				}
				
				
				if ( ( sCampo = rs.getString( "VlrDescItRec" ) ) != null && rs.getDouble( "VlrDescItRec" ) != 0 ) {
					sTxa = sTxa.replaceAll( "\\[DESC_DOCUMENT]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
					sTxa = sTxa.replaceAll( "\\[VALOR_EXTENSO_DESC]", Extenso.extenso( rs.getDouble( "VlrDescItRec" ), sInfoMoeda[ 0 ], sInfoMoeda[ 1 ], sInfoMoeda[ 2 ], sInfoMoeda[ 3 ] ) ).toUpperCase();
				}
				
				try {
					
					if ( rs.getString( "baseinss") !=null )  {
						
						if(rs.getBigDecimal( "retinss").floatValue()>0) {
							sCampo = Funcoes.bdToStrd( rs.getBigDecimal( "baseinss"), 2 ).toString();
						}
						else {
							sCampo = "0";
						}
						
						sTxa = sTxa.replaceAll( "\\[BASE_INSS]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
					}
					if ( rs.getString( "baseirrf") !=null )  {
						if(rs.getBigDecimal( "retirrf").floatValue()>0) {
							sCampo = Funcoes.bdToStrd( rs.getBigDecimal( "baseirrf"), 2 ).toString();
						}
						else {
							sCampo = "0";
						}
						sTxa = sTxa.replaceAll( "\\[BASE_IRRF]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
					}
					if ( rs.getString( "retirrf") !=null )  {
						sCampo = Funcoes.bdToStrd( rs.getBigDecimal( "retirrf"), 2 ).toString();
						sTxa = sTxa.replaceAll( "\\[VALOR_IRRF]", sCampo );
					}
					if ( rs.getString( "retinss") !=null )  {
						sCampo = Funcoes.bdToStrd( rs.getBigDecimal( "retinss"), 2 ).toString();
						sTxa = sTxa.replaceAll( "\\[VALOR_INSS]", sCampo );
					}
					if ( rs.getString( "aliqinss") !=null )  {
						
						BigDecimal aliqinss = rs.getBigDecimal( "aliqinss");
						
						aliqinss = aliqinss.setScale( 2, BigDecimal.ROUND_UP );
						
						sCampo = Funcoes.bdToStrd( aliqinss, 2 ).toString();
						sTxa = sTxa.replaceAll( "\\[ALIQ_INSS]", sCampo );
					}
					if ( rs.getString( "aliqirrf") !=null )  {
						sCampo = Funcoes.bdToStrd( rs.getBigDecimal( "aliqirrf"), 2 ).toString();
						sTxa = sTxa.replaceAll( "\\[ALIQ_IRRF]", sCampo );
					}
					if ( rs.getString( "vlrbruto") !=null )  {
						sCampo = Funcoes.bdToStrd( rs.getBigDecimal( "vlrbruto"), 2 ).toString();
						sTxa = sTxa.replaceAll( "\\[VALOR_BRUTO]", sCampo ) ;
					}
					
					
				}
				catch (Exception e) {
					System.out.print( "Não possui os campos relativos a retenção de inss e irrf..." );
				}
				
				
				if ( ( sCampo = rs.getString( "CodCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[CODCLI]", Funcoes.copy( sCampo, 0, 8 ) );
				if ( ( sCampo = rs.getString( "RazCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_____________RAZAO____DO____CLIENTE_____________]", Funcoes.copy( sCampo, 0, 50 ) );
				if ( ( sCampo = rs.getString( "NomeCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_____________NOME_____DO____CLIENTE_____________]", Funcoes.copy( sCampo, 0, 50 ) );
				if ( ( sCampo = rs.getString( "CpfCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[CPF/CNPJ_ CLIENT]", Funcoes.setMascara( sCampo, "###.###.###-##" ) );
				else if ( ( sCampo = rs.getString( "CnpjCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[CPF/CNPJ_ CLIENT]", Funcoes.setMascara( sCampo, "##.###.###/####-##" ) );
				if ( ( sCampo = rs.getString( "RgCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[____IE/RG____CLIENTE]", Funcoes.copy( sCampo, 0, 22 ) );
				else if ( ( sCampo = rs.getString( "InscCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[____IE/RG____CLIENTE]", Funcoes.copy( sCampo, 0, 22 ) );
				if ( ( sCampo = rs.getString( "EndCob" ) ) != null || ( sCampo = rs.getString( "EndCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[____________ENDERECO____DO____CLIENTE___________]", Funcoes.copy( sCampo, 0, 31 ) );
				if ( ( sCampo = rs.getString( "NumCob" ) ) != null || ( sCampo = rs.getString( "NumCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[NUMERO]", Funcoes.copy( sCampo, 0, 10 ) );
				// Endereço com número
				if ( ( sCampo = rs.getString( "EndCob" ) ) != null || ( sCampo = rs.getString( "EndCli" ) ) != null ) {
					if (rs.getString( "EndCob" )!=null) {
						sCampo = sCampo.trim()+", "+rs.getString( "numcob" );
					} else {
						sCampo = sCampo.trim()+", "+rs.getString( "numcli" );
					}
					sTxa = sTxa.replaceAll( "\\[_______ENDERECO_COM_NUMERO_DO_CLIENTE___________]", Funcoes.copy( sCampo, 0, 50 ) );
				}
				//
				if ( ( sCampo = rs.getString( "ComplCob" ) ) != null || ( sCampo = rs.getString( "ComplCli" ) ) != null ) {
					sTxa = sTxa.replaceAll( "\\[____COMPLEMENTO___]", Funcoes.copy( sCampo, 0, 12 ) );
				}
				else {
					sTxa = sTxa.replaceAll( "\\[____COMPLEMENTO___]", Funcoes.copy( "", 0, 12 ) );
				}

				if ( ( sCampo = rs.getString( "BairCob" ) ) != null || ( sCampo = rs.getString( "BairCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[___________BAIRRO___________]", Funcoes.copy( sCampo, 0, 12 ) );

				if ( ( sCampo = rs.getString( "CepCob" ) ) != null || ( sCampo = rs.getString( "CepCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[__CEP__]", Funcoes.setMascara( sCampo, "#####-###" ) );
				if ( ( sCampo = rs.getString( "CidCob" ) ) != null || ( sCampo = rs.getString( "CidCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[___________CIDADE___________]", sCampo.trim() );
				if ( ( sCampo = rs.getString( "UfCob" ) ) != null || ( sCampo = rs.getString( "UfCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[UF]", Funcoes.copy( sCampo, 0, 2 ) );
				if ( ( sCampo = rs.getString( "FoneCli" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[__TELEFONE___]", Funcoes.setMascara( sCampo.trim(), "####-####" ) );
				if ( ( sCampo = rs.getString( "DDDCli" ) ) != null || ( sCampo = "(" + rs.getString( "DDDCli" ) ) + ")" != null )
					sTxa = sTxa.replaceAll( "\\[DDD]", Funcoes.copy( sCampo, 0, 5 ) );
				
				if( sNat != null ){
					if ( ( sCampo = sNat[ 0 ] ) != null )
						sTxa = sTxa.replaceAll( "\\[CODNAT]", Funcoes.copy( sCampo, 0, 8 ) );
					if ( ( sCampo = sNat[ 1 ] ) != null )
						sTxa = sTxa.replaceAll( "\\[______________NATUREZA_DA_OPERACAO______________]", Funcoes.copy( sCampo, 0, 50 ) );
				}
				
				if ( ( sCampo = rs.getString( "CODVENDA" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[CODVENDA]", Funcoes.copy( sCampo, 0, 10 ) );
				if ( ( sCampo = rs.getString( "VlrApagRec" ) ) != null && rs.getDouble( "VlrApagRec" ) != 0 )
					sTxa = sTxa.replaceAll( "\\[TOTAL_PARCELAS]", Funcoes.strDecimalToStrCurrency( 15, 2, sCampo ) );
				if ( ( sCampo = rs.getString( "NomeVend" ) ) != null || ( sCampo = rs.getString( "NomeVend" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_______COMISSIONADO1_______]", Funcoes.copy( sCampo, 0, 30 ) );
				if ( ( sCampo = rs.getString( "NomeVend2" ) ) != null || ( sCampo = rs.getString( "NomeVend2" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_______COMISSIONADO2_______]", Funcoes.copy( sCampo, 0, 30 ) );
				if ( ( sCampo = rs.getString( "NomeVend3" ) ) != null || ( sCampo = rs.getString( "NomeVend3" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_______COMISSIONADO3_______]", Funcoes.copy( sCampo, 0, 30 ) );
				if ( ( sCampo = rs.getString( "NomeVend4" ) ) != null || ( sCampo = rs.getString( "NomeVend4" ) ) != null )
					sTxa = sTxa.replaceAll( "\\[_______COMISSIONADO4_______]", Funcoes.copy( sCampo, 0, 30 ) );

				// Aplicar campos especiais de dados:

				int iPos = 0;
				while ( ( iPos = sTxa.indexOf( "%_VAL", iPos + 1 ) ) > 0 ) {
					double dVal = 0;
					String sCaixa = sTxa.substring( iPos - 9, iPos );
					sCaixa += "\\" + sTxa.substring( iPos, iPos + 6 );
					dVal = rs.getDouble( "VlrParcitRec" );
					dVal *= Double.parseDouble( sTxa.substring( iPos - 8, iPos ) ) / 100;
					sTxa = sTxa.replaceAll( "\\" + sCaixa, Funcoes.strDecimalToStrCurrency( 15, 2, new BigDecimal( dVal ).setScale( 2, BigDecimal.ROUND_HALF_UP ).toString() ) );
				}
				iPos = 0;
				while ( ( iPos = sTxa.indexOf( "+_VAL", iPos + 1 ) ) > 0 ) {
					double dVal = 0;
					String sCaixa = sTxa.substring( iPos - 9, iPos );
					sCaixa += "\\" + sTxa.substring( iPos, iPos + 6 );
					dVal = rs.getDouble( "VlrParcitRec" );
					dVal += Double.parseDouble( sTxa.substring( iPos - 8, iPos ) );
					sTxa = sTxa.replaceAll( "\\" + sCaixa, Funcoes.strDecimalToStrCurrency( 15, 2, new BigDecimal( dVal ).setScale( 2, BigDecimal.ROUND_HALF_UP ).toString() ) );
				}
				iPos = 0;
				while ( ( iPos = sTxa.indexOf( "-_VAL", iPos + 1 ) ) > 0 ) {
					double dVal = 0;
					String sCaixa = sTxa.substring( iPos - 9, iPos );
					sCaixa += "\\" + sTxa.substring( iPos, iPos + 6 );
					dVal = rs.getDouble( "VlrParcitRec" );
					dVal -= Double.parseDouble( sTxa.substring( iPos - 8, iPos ) );
					sTxa = sTxa.replaceAll( "\\" + sCaixa, Funcoes.strDecimalToStrCurrency( 15, 2, new BigDecimal( dVal ).setScale( 2, BigDecimal.ROUND_HALF_UP ).toString() ) );
				}
				iPos = 0;
				while ( ( iPos = sTxa.indexOf( "+_VEN", iPos + 1 ) ) > 0 ) {
					GregorianCalendar cVal = new GregorianCalendar();
					String sCaixa = sTxa.substring( iPos - 4, iPos );
					sCaixa += "\\" + sTxa.substring( iPos, iPos + 6 );
					cVal.setTime( rs.getDate( "DtVencItRec" ) );
					cVal.set( Calendar.DATE, cVal.get( Calendar.DATE ) + Integer.parseInt( sTxa.substring( iPos - 3, iPos ) ) );
					sTxa = sTxa.replaceAll( "\\" + sCaixa, Funcoes.dateToStrDate( cVal.getTime() ) );
				}

				sRet = sTxa;
			}

			// Ajustando campos de ação:

			sRet = sRet.replaceAll( "\\<LP\\>.*].*\\<_LP\\>", "" );
			sRet = sRet.replaceAll( "\\<[_]*LP\\>", "" );
			sRet = sRet.replaceAll( "\\<EJECT\\>", "" + ( (char) 12 ) + ( (char) 13 ) );

			// Tirando campos não setados:

			Pattern p = Pattern.compile( "\\[.*\\]" );
			Matcher m = p.matcher( sRet );
			StringBuffer sb = new StringBuffer();

			while ( m.find() ) {
				m.appendReplacement( sb, StringFunctions.replicate( " ", m.end() - m.start() ) );
			}

			m.appendTail( sb );
			sRet = sb.toString();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro na consulta ao modelo de boleto!\n" + err.getMessage(), true, Aplicativo.getInstace().getConexao(), err );
			err.printStackTrace();
		}

		return sRet;
	}

	public static String[] getMoeda() {

		String sRet[] = new String[ 5 ];
		StringBuilder sSQL = new StringBuilder();

		try {

			sSQL.append( "SELECT M.SINGMOEDA,M.PLURMOEDA,M.DECSMOEDA,M.DECPMOEDA,M.CODFBNMOEDA FROM FNMOEDA M, SGPREFERE1 P " );
			sSQL.append( "WHERE M.CODMOEDA=P.CODMOEDA AND M.CODEMP=P.CODEMPMO AND M.CODFILIAL=P.CODFILIALMO " );
			sSQL.append( "AND P.CODEMP=? AND P.CODFILIAL=?" );

			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				sRet[ 0 ] = rs.getString( "SingMoeda" ).trim();
				sRet[ 1 ] = rs.getString( "PlurMoeda" ).trim();
				sRet[ 2 ] = rs.getString( "DecSMoeda" ).trim();
				sRet[ 3 ] = rs.getString( "DecPMoeda" ).trim();
				sRet[ 4 ] = rs.getString( "CodFbnMoeda" ).trim();
			}
			rs.close();
			ps.close();

			Aplicativo.getInstace().getConexao().commit();
			
		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar a moeda padrão!\n" + err.getMessage(), true, Aplicativo.getInstace().getConexao(), err );
			err.printStackTrace();
		}

//		if ( sRet == null ) {
//			Funcoes.mensagemErro( null, "A moeda padrão pode não estar ajustada no preferências!" );
//		}

		return sRet;
	}

	private String getClassModelo( final String preImpModbol, final String classModBol ) {

		String retorno = null;

		if ( "N".equals( preImpModbol ) ) {
			if ( classModBol.indexOf( '/', 0 ) == -1 ) {
				retorno = "layout/bol/" + classModBol;
			}
			else {
				retorno = classModBol;
			}
		}
		return retorno;

	}
	
	private String getNumConta(int codModBol, String codBanco, String CodCartCob){
		
		String numConta = null;
		StringBuilder sql = new StringBuilder();
		try{
	
			sql.append( "select i.numconta from fnitmodboleto i ");
			sql.append( "where i.codemp=? and i.codfilial=? and  i.codmodbol=? and ");
			sql.append( "i.codempbo=? and i.codfilialbo=? and i.codbanco=? and ");
			sql.append( "i.codempcb=? and i.codfilialcb=? and i.codcartcob=?" );
			
			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, codModBol );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5,  ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 6, codBanco );
			ps.setInt( 7, Aplicativo.iCodEmp );
			ps.setInt( 8,  ListaCampos.getMasterFilial( "FNCARTCOB" ) );
			ps.setString( 9,  CodCartCob );
			
			ResultSet rs = ps.executeQuery();
			
			if ( rs.next() ) {
				numConta = rs.getString( "NumConta" );
			}
			
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		return numConta;
	}

	private int getCodrec( final int codvenda, final String tipovenda ) {

		int codrec = 0;

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT CODREC FROM FNRECEBER " );
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND " );
			sql.append( "CODEMPVA=? AND CODFILIALVA=? AND CODVENDA=? AND TIPOVENDA=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( 5, codvenda );
			ps.setString( 6, tipovenda );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				codrec = rs.getInt( "CODREC" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return codrec;
	}

	private void getTpnossonumero() {

		try {

			String sql = "SELECT TPNOSSONUMERO, IMPDOCBOL FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				tpnossonumero = rs.getString( "TPNOSSONUMERO" );
				impdocbol = rs.getString( "IMPDOCBOL" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		if ( tpnossonumero == null ) {
			tpnossonumero = "D";
		}
		if ( impdocbol == null ) {
			impdocbol = "N";
		}
	}
	
	private void getAtualizaParcela() {

		try {

			String sql = "SELECT ATBANCOIMPBOL FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				bAltParcela = "S".equalsIgnoreCase( rs.getString( "ATBANCOIMPBOL" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	private boolean atualizaParcela( final int codrec, final Integer codparc, final String codbanco, final String codcartcob, final String numconta ) {

		boolean ret = true;
		boolean bcart = false;
		int iparam = 1;

		if ( bAltParcela && ( codbanco != null && codbanco.trim().length() > 0 ) && codrec > 0 ) {

			try {

				bcart = ( codcartcob != null && codcartcob.trim().length() > 0 );

				StringBuilder sql = new StringBuilder();

				sql.append( "UPDATE FNRECEBER SET CODEMPBO=?, CODFILIALBO=?, CODBANCO=? " );
				if ( bcart ) {
					sql.append( ", CODEMPCB=?, CODFILIALCB=?,  CODCARTCOB=?, " );
					sql.append( "CODEMPCA=?, CODFILIALCA=?, NUMCONTA=? ");
				}
				sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODREC=?" );

				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( iparam++, codbanco );

				if ( bcart ) {
					ps.setInt( iparam++, Aplicativo.iCodEmp );
					ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCARTCOB" ) );
					ps.setString( iparam++, codcartcob );
					
					ps.setInt( iparam++, Aplicativo.iCodEmp );
					ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCONTA" ) );
					ps.setString( iparam++, numconta );
				}

				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNRECEBER" ) );
				ps.setInt( iparam++, codrec );
				ps.executeUpdate();
				ps.close();

				con.commit();

				sql = new StringBuilder();

				sql.append( "UPDATE FNITRECEBER SET CODEMPBO=?, CODFILIALBO=?, CODBANCO=? " );
				if ( bcart ) {
					sql.append( ", CODEMPCB=?, CODFILIALCB=?,  CODCARTCOB=?, " );
					sql.append( "CODEMPCA=?, CODFILIALCA=?, NUMCONTA=? ");
				}
				sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODREC=? " );
				if ( codparc != null && codparc > 0 ) {
					sql.append( "AND NPARCITREC=? " );
				}

				iparam = 1;

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNBANCO" ) );
				ps.setString( iparam++, codbanco );

				if ( bcart ) {
					ps.setInt( iparam++, Aplicativo.iCodEmp );
					ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCARTCOB" ) );
					ps.setString( iparam++, codcartcob );
					ps.setInt( iparam++, Aplicativo.iCodEmp );
					ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCONTA" ) );
					ps.setString( iparam++, numconta );
				}

				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
				ps.setInt( iparam++, codrec );

				if ( codparc != null && codparc > 0 ) {
					ps.setInt( iparam++, codparc );
				}
				ps.executeUpdate();
				ps.close();

				con.commit();
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao atualizar paracela(s)!\n" + e.getMessage() );
				ret = false;
			}
		}

		return ret;
	}

	private HashMap<String, Object> getParametros() {

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		Empresa empresa = new Empresa( con );
		parametros.put( "CODEMP", Aplicativo.iCodEmp );
		parametros.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		parametros.put( "IMPDOC", txtImpInst.getVlrString() );
		parametros.put( "TPNOSSONUMERO", tpnossonumero );
		parametros.put( "IMPDOCBOL", impdocbol );

		if ( Aplicativo.empresa != null ) {
			parametros.put( "RAZEMP", empresa.getAll().get( "RAZEMP" ) );
			ToolkitImage logoemp = (ToolkitImage) empresa.getAll().get( "LOGOEMP" );
			if ( logoemp != null ) {
				parametros.put( "LOGOEMP", logoemp );
			}
		}
		// parametros.put( "CODVENDA", txtCodVenda.getVlrInteger() );

		return parametros;
	}

	public void setParcelas( final List<?> lsParcParam ) {

		lsParcelas = lsParcParam;
	}

	private ResultSet execQuery( String sWhereGrid ) {

		ResultSet rsRetorno = null;
		StringBuilder sSQL = new StringBuilder();
		StringBuilder sWhere = new StringBuilder();
		final int codvenda = txtCodVenda.getVlrInteger().intValue();
		final int codvenda2 = txtCodVenda2.getVlrInteger().intValue();
		final int nparc = txtParc.getVlrInteger().intValue();
		final String codbanco = txtCodBanco.getVlrString().trim();
		final int codtipocob = txtCodTpCob.getVlrInteger().intValue();
		final int codTipoMov = txtCodTipoMov.getVlrInteger().intValue();
		ImprimeOS imp = null;
		PreparedStatement ps = null;
		int param = 1;

		if ( !sWhereGrid.equals( "" ) ) {

			String sWhereBol = getBoletos();
		}

		sWhere.append( "MB.CODEMP=? AND MB.CODFILIAL=? AND MB.CODMODBOL=? AND " );

		if ( codvenda != 0 && codvenda2 != 0 ) {
			sWhere.append( "R.CODEMPVA=? AND R.CODFILIALVA=? AND R.CODVENDA BETWEEN ? AND ? " );
		}

		if ( codvenda != 0 && codvenda2 == 0 ) {
			sWhere.append( "R.CODEMPVA=? AND R.CODFILIALVA=? AND R.CODVENDA=? " );
		}
		else if ( codvenda == 0 && codvenda2 == 0 ) {
			sWhere.append( "V.DTEMITVENDA BETWEEN ? AND ? " );
		}
		if ( !"".equals( codbanco ) ) {
			sWhere.append( "AND B.CODEMP=? AND B.CODFILIAL=? AND B.CODBANCO=? " );
		}
		if ( codtipocob != 0 ) {
			sWhere.append( "AND ITR.CODEMPTC=? AND ITR.CODFILIALTC=? AND ITR.CODTIPOCOB=? " );
		}
		if ( nparc != 0 ) {
			sWhere.append( "AND ITR.NPARCITREC=? " );
		}
		if ( codTipoMov != 0 ) {
			sWhere.append( "AND V.CODEMPTM=? AND V.CODFILIALTM=? AND V.CODTIPOMOV=? " );
		}

		// sWhere.append( "AND R.CODVENDA IN " + "( " + sWhereBol.toString() + " )" );

		if ( ( lsParcelas != null ) && ( lsParcelas.size() > 0 ) ) {
			sWhere.append( "AND ITR.NPARCITREC IN (" );
			for ( int i = 0; i < lsParcelas.size(); i++ ) {
				if ( i != 0 ) {
					sWhere.append( "," );
				}
				sWhere.append( lsParcelas.get( i ) );
			}
			sWhere.append( ")" );
		}

		imp = new ImprimeOS( "", con );
		imp.verifLinPag();
		imp.setTitulo( "Boleto" );

		sSQL.append( "SELECT V.CODVENDA,V.OBSVENDA,(SELECT COUNT(*) FROM FNITRECEBER ITR2 " );
		sSQL.append( "WHERE ITR2.CODREC=R.CODREC AND ITR2.CODEMP=R.CODEMP AND ITR2.CODFILIAL=R.CODFILIAL) PARCS, " );
		sSQL.append( "ITR.DTVENCITREC,ITR.NPARCITREC,ITR.VLRAPAGITREC,ITR.VLRPARCITREC,ITR.VLRDESCITREC, ITR.DOCLANCAITREC, " );
		sSQL.append( "(ITR.VLRJUROSITREC+ITR.VLRMULTAITREC) VLRMULTA,  " );
		sSQL.append( "R.DOCREC, R.DATAREC, R.VLRREC, ITR.CODBANCO, B.DVBANCO, " );
		sSQL.append( "B.IMGBOLBANCO LOGOBANCO01, COALESCE(B.IMGBOLBANCO2,B.IMGBOLBANCO) LOGOBANCO02, B.IMGBOLBANCO LOGOBANCO03, B.IMGBOLBANCO LOGOBANCO04," );
		sSQL.append( "IM.CODCARTCOB, MB.ESPDOCMODBOL ESPDOC, MB.ACEITEMODBOL ACEITE, MB.MDECOB, " );
		sSQL.append( "MB.PREIMPMODBOL, MB.CLASSMODBOL, MB.TXAMODBOL, V.DTEMITVENDA, V.DOCVENDA, " );
		sSQL.append( "C.CODCLI,C.RAZCLI,C.NOMECLI,C.CPFCLI,C.CNPJCLI,C.RGCLI,C.INSCCLI, " );
		sSQL.append( "C.ENDCLI,C.NUMCLI,C.COMPLCLI,C.CEPCLI,C.BAIRCLI,C.CIDCLI,C.UFCLI, " );
		sSQL.append( "C.ENDCOB,C.NUMCOB,C.COMPLCOB,C.CEPCOB,C.BAIRCOB,C.CIDCOB,C.UFCOB, " );
		sSQL.append( "C.FONECLI,C.DDDCLI,R.CODREC, P.CODMOEDA, C.PESSOACLI, ITR.RECIBOITREC, " );
		sSQL.append( "(ITR.DTVENCITREC-CAST('07.10.1997' AS DATE)) FATVENC, M.CODFBNMOEDA, " );
		sSQL.append( "M.SINGMOEDA,M.PLURMOEDA,M.DECSMOEDA,M.DECPMOEDA, ");
		sSQL.append( " F.RAZFILIAL,  F.ENDFILIAL, F.NUMFILIAL, F.BAIRFILIAL, F.CIDFILIAL, F.CEPFILIAL, F.FONEFILIAL, F.FAXFILIAL, " );
		sSQL.append( "F.CNPJFILIAL, F.INSCFILIAL, F.WWWFILIAL, F.EMAILFILIAL, F.UNIDFRANQUEADA, F.COMPLFILIAL, F.SIGLAUF, " );

		sSQL.append( "IV.CODNAT, N.DESCNAT, F.RAZFILIAL, CT.AGENCIACONTA, IM.NUMCONTA, " );
		sSQL.append( "MB.DESCLPMODBOL, MB.INSTPAGMODBOL, IM.CONVCOB, IM.DVCONVCOB , R.VLRAPAGREC, VD.NOMEVEND, " );

		sSQL.append( "(SELECT FIRST 1 VO.CODORC FROM VDVENDAORC VO " );
		sSQL.append( "WHERE VO.CODEMP=V.CODEMP AND VO.CODFILIAL=VO.CODFILIAL AND " );
		sSQL.append( "VO.CODVENDA = V.CODVENDA AND VO.TIPOVENDA=V.TIPOVENDA) AS CODORC, " );

		sSQL.append( "(SELECT AC.NOMECONV FROM ATCONVENIADO AC,VDORCAMENTO VA " );
		sSQL.append( "WHERE VA.CODEMP=V.CODEMP AND VA.CODFILIAL=V.CODFILIAL AND " );
		sSQL.append( "VA.CODORC =(SELECT FIRST 1 VO.CODORC FROM VDVENDAORC VO " );
		sSQL.append( "WHERE VO.CODEMP=V.CODEMP AND VO.CODFILIAL=VO.CODFILIAL AND " );
		sSQL.append( "VO.CODVENDA = V.CODVENDA AND VO.TIPOVENDA=V.TIPOVENDA) AND " );
		sSQL.append( "AC.CODEMP=VA.CODEMPCV AND AC.CODFILIAL=VA.CODFILIALCV AND " );
		sSQL.append( "AC.CODCONV=VA.CODCONV) AS NOMECONV, " );

		sSQL.append( "(SELECT VA.OBSORC FROM VDORCAMENTO VA " );
		sSQL.append( "WHERE VA.CODEMP=V.CODEMP AND VA.CODFILIAL=V.CODFILIAL AND " );
		sSQL.append( "VA.CODORC =(SELECT FIRST 1 VO.CODORC FROM VDVENDAORC VO " );
		sSQL.append( "WHERE VO.CODEMP=V.CODEMP AND VO.CODFILIAL=VO.CODFILIAL AND " );
		sSQL.append( "VO.CODVENDA = V.CODVENDA AND VO.TIPOVENDA=V.TIPOVENDA)) AS OBSORC, ITR.DESCPONT, " );

		sSQL.append( "(SELECT V1.NOMEVEND FROM VDVENDEDOR V1, VDVENDACOMIS VC WHERE " );
		sSQL.append( "V1.CODEMP=VC.codempvd AND V1.CODFILIAL = VC.codfilialvd AND V1.codvend=VC.codvend AND " );
		sSQL.append( "VC.codemp=V.CODEMP AND VC.codfilial=V.codfilial AND VC.codvenda=V.CODVENDA AND VC.tipovenda=V.TIPOVENDA " );
		sSQL.append( "AND VC.seqvc=1 ) AS NOMEVEND1," );

		sSQL.append( "(SELECT V1.NOMEVEND FROM VDVENDEDOR V1, VDVENDACOMIS VC WHERE " );
		sSQL.append( "V1.CODEMP=VC.codempvd AND V1.CODFILIAL = VC.codfilialvd AND V1.codvend=VC.codvend AND " );
		sSQL.append( "VC.codemp=V.CODEMP AND VC.codfilial=V.codfilial AND VC.codvenda=V.CODVENDA AND VC.tipovenda=V.TIPOVENDA " );
		sSQL.append( "AND VC.seqvc=2 ) AS NOMEVEND2," );

		sSQL.append( "(SELECT V1.NOMEVEND FROM VDVENDEDOR V1, VDVENDACOMIS VC WHERE " );
		sSQL.append( "V1.CODEMP=VC.codempvd AND V1.CODFILIAL = VC.codfilialvd AND V1.codvend=VC.codvend AND " );
		sSQL.append( "VC.codemp=V.CODEMP AND VC.codfilial=V.codfilial AND VC.codvenda=V.CODVENDA AND VC.tipovenda=V.TIPOVENDA " );
		sSQL.append( "AND VC.seqvc=3 ) AS NOMEVEND3," );

		sSQL.append( "(SELECT V1.NOMEVEND FROM VDVENDEDOR V1, VDVENDACOMIS VC WHERE " );
		sSQL.append( "V1.CODEMP=VC.codempvd AND V1.CODFILIAL = VC.codfilialvd AND V1.codvend=VC.codvend AND " );
		sSQL.append( "VC.codemp=V.CODEMP AND VC.codfilial=V.codfilial AND VC.codvenda=V.CODVENDA AND VC.tipovenda=V.TIPOVENDA " );
		sSQL.append( "AND VC.seqvc=4 ) AS NOMEVEND4, " );

		sSQL.append( "ITR.OBSITREC OBS, TCO.VARIACAOCARTCOB, COALESCE(ITR.SEQNOSSONUMERO,0) SEQNOSSONUMERO, F.UFFILIAL, " );
		sSQL.append( "MU.NOMEMUNIC CIDCLI ");
		
		sSQL.append( "FROM FNRECEBER R, SGPREFERE1 P, FNMOEDA M, FNBANCO B, " );
		sSQL.append( "FNMODBOLETO MB, FNITMODBOLETO IM, VDITVENDA IV, LFNATOPER N,  FNITRECEBER ITR, " );
		sSQL.append( "SGFILIAL F, FNCONTA CT, VDVENDEDOR VD, VDVENDA V, FNCARTCOB TCO, VDCLIENTE C " );
		sSQL.append( "LEFT OUTER JOIN SGMUNICIPIO MU ON MU.CODPAIS=C.CODPAIS AND MU.SIGLAUF=C.SIGLAUF AND MU.CODMUNIC=C.CODMUNIC ");

		sSQL.append( "WHERE ITR.CODREC=R.CODREC AND ITR.CODEMP=R.CODEMP AND ITR.CODFILIAL=R.CODFILIAL AND " );
		sSQL.append( "V.CODVENDA=R.CODVENDA AND V.CODEMP=R.CODEMPVA AND V.CODFILIAL=R.CODFILIALVA AND " );
		sSQL.append( "F.CODEMP=R.CODEMP AND F.CODFILIAL=R.CODFILIAL AND " );
		sSQL.append( "C.CODCLI=V.CODCLI AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND " );
		sSQL.append( "P.CODEMP=R.CODEMP AND P.CODFILIAL=R.CODFILIAL AND " );
		sSQL.append( "M.CODEMP=P.CODEMPMO AND M.CODFILIAL=P.CODFILIALMO AND M.CODMOEDA=P.CODMOEDA AND " );
		sSQL.append( "B.CODEMP=ITR.CODEMPBO AND B.CODFILIAL=ITR.CODFILIALBO AND B.CODBANCO=ITR.CODBANCO AND " );
		sSQL.append( "IM.CODEMP=MB.CODEMP AND IM.CODFILIAL=MB.CODFILIAL AND IM.CODMODBOL=MB.CODMODBOL AND " );
		sSQL.append( "IM.CODEMPBO=ITR.CODEMPBO AND IM.CODFILIALBO=ITR.CODFILIALBO AND IM.CODBANCO=ITR.CODBANCO AND " );
		sSQL.append( "IM.CODEMPCB=ITR.CODEMPCB AND IM.CODFILIALCB=ITR.CODFILIALCB AND IM.CODCARTCOB=ITR.CODCARTCOB AND " );
		sSQL.append( "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND IV.TIPOVENDA=V.TIPOVENDA AND " );
		sSQL.append( "IV.CODVENDA=V.CODVENDA AND IV.CODITVENDA=( SELECT MIN(CODITVENDA) FROM VDITVENDA IV2 " );
		sSQL.append( "WHERE IV2.CODEMP=IV.CODEMP AND IV2.CODFILIAL=IV.CODFILIAL AND IV2.TIPOVENDA=IV.TIPOVENDA AND " );
		sSQL.append( "IV2.CODVENDA=IV.CODVENDA AND IV2.CODNAT IS NOT NULL ) AND " );
		sSQL.append( "N.CODEMP=IV.CODEMPNT AND N.CODFILIAL=IV.CODFILIALNT AND N.CODNAT=IV.CODNAT AND  " );
		sSQL.append( "ITR.STATUSITREC IN ('R1','RL', 'RR') " );
		sSQL.append( "AND VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND VD.CODVEND=V.CODVEND AND " );
		sSQL.append( "CT.CODEMP=IM.CODEMPCT AND CT.CODFILIAL=IM.CODFILIALCT AND CT.NUMCONTA=IM.NUMCONTA AND " );
		sSQL.append( "TCO.CODEMP=ITR.CODEMPCB AND TCO.CODFILIAL=ITR.CODFILIALCB AND TCO.CODCARTCOB=ITR.CODCARTCOB and tco.codempbo = b.codemp and tco.codfilialbo=b.codfilial and tco.codbanco=b.codbanco AND ");

		sSQL.append( sWhere );
		sSQL.append( sWhereGrid );
		sSQL.append( " order by v.codvenda" );

		try {

			String strDebug = sSQL.toString();

			System.out.println( "QUERY PARA DEBUG:" + strDebug );

			ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );

			strDebug = strDebug.replaceFirst( "\\?", Aplicativo.iCodEmp + "" );

			ps.setInt( param++, ListaCampos.getMasterFilial( "FNMODBOLETO" ) );

			strDebug = strDebug.replaceFirst( "\\?", ListaCampos.getMasterFilial( "FNMODBOLETO" ) + "" );

			ps.setInt( param++, txtCodModBol.getVlrInteger().intValue() );

			strDebug = strDebug.replaceFirst( "\\?", txtCodModBol.getVlrInteger().intValue() + "" );

			if ( codvenda != 0 && codvenda2 != 0 ) {

				ps.setInt( param++, Aplicativo.iCodEmp );

				strDebug = strDebug.replaceFirst( "\\?", Aplicativo.iCodEmp + "" );

				ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );

				strDebug = strDebug.replaceFirst( "\\?", ListaCampos.getMasterFilial( "VDVENDA" ) + "" );

				ps.setInt( param++, txtCodVenda.getVlrInteger().intValue() );

				strDebug = strDebug.replaceFirst( "\\?", txtCodVenda.getVlrInteger().intValue() + "" );

				ps.setInt( param++, txtCodVenda2.getVlrInteger().intValue() );

				strDebug = strDebug.replaceFirst( "\\?", txtCodVenda2.getVlrInteger().intValue() + "" );
			}

			if ( codvenda != 0 && codvenda2 == 0 ) {
				ps.setInt( param++, Aplicativo.iCodEmp );

				strDebug = strDebug.replaceFirst( "\\?", Aplicativo.iCodEmp + "" );

				ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );

				strDebug = strDebug.replaceFirst( "\\?", ListaCampos.getMasterFilial( "VDVENDA" ) + "" );

				ps.setInt( param++, txtCodVenda.getVlrInteger().intValue() );

				strDebug = strDebug.replaceFirst( "\\?", txtCodVenda.getVlrInteger().intValue() + "" );

			}
			else if ( codvenda == 0 && codvenda2 == 0 ) {
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );

				strDebug = strDebug.replaceFirst( "\\?", Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) + "" );

				ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );

				strDebug = strDebug.replaceFirst( "\\?", Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) + "" );

			}
			if ( !"".equals( codbanco ) ) {
				ps.setInt( param++, Aplicativo.iCodEmp );

				strDebug = strDebug.replaceFirst( "\\?", Aplicativo.iCodEmp + "" );

				ps.setInt( param++, ListaCampos.getMasterFilial( "FNBANCO" ) );

				strDebug = strDebug.replaceFirst( "\\?", ListaCampos.getMasterFilial( "FNBANCO" ) + "" );

				ps.setString( param++, codbanco );

				strDebug = strDebug.replaceFirst( "\\?", codbanco + "" );

			}
			if ( codtipocob != 0 ) {
				ps.setInt( param++, Aplicativo.iCodEmp );

				strDebug = strDebug.replaceFirst( "\\?", Aplicativo.iCodEmp + "" );

				ps.setInt( param++, ListaCampos.getMasterFilial( "FNTIPOCOB" ) );

				strDebug = strDebug.replaceFirst( "\\?", ListaCampos.getMasterFilial( "FNTIPOCOB" ) + "" );

				ps.setInt( param++, codtipocob );

				strDebug = strDebug.replaceFirst( "\\?", codtipocob + "" );

			}
			if ( nparc != 0 ) {
				ps.setInt( param++, nparc );
				strDebug = strDebug.replaceFirst( "\\?", nparc + "" );
			}

			if ( codTipoMov != 0 ) {

				ps.setInt( param++, Aplicativo.iCodEmp );

				strDebug = strDebug.replaceFirst( "\\?", Aplicativo.iCodEmp + "" );

				ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );

				strDebug = strDebug.replaceFirst( "\\?", ListaCampos.getMasterFilial( "VDVENDA" ) + "" );

				ps.setInt( param++, codTipoMov );

				strDebug = strDebug.replaceFirst( "\\?", codTipoMov + "" );

			}

			rsRetorno = ps.executeQuery();
			System.out.println( "SQL: " + strDebug );

		} catch ( SQLException e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados \n" + e.getMessage() );
		}

		return rsRetorno;
	}

	private void montaGrid() {

		if ( "".equals( txtCodModBol.getVlrString() ) ) {

			Funcoes.mensagemInforma( this, "Modelo de boleto não selecionado!" );
			txtCodModBol.requestFocus();
			return;
		}

		atualizaParcela( getCodrec( txtCodVenda.getVlrInteger(), txtTipoVenda.getVlrString() ),
				txtParc.getVlrInteger(), txtCodBanco.getVlrString(), txtCodCartCob.getVlrString(), txtNumConta.getVlrString() );

		ResultSet rs = execQuery( "" );
		tbBoletos.setColunaEditavel( 0, true );

		try {

			for ( int i = 0; rs.next(); i++ ) {

				tbBoletos.adicLinha();
				tbBoletos.setValor( true, i, 0 );
				tbBoletos.setValor( rs.getInt( "CodVenda" ), i, 1 );
				tbBoletos.setValor( Funcoes.dateToSQLDate( rs.getDate( "DtEmitVenda" ) ), i, 2 );
				tbBoletos.setValor( Funcoes.dateToSQLDate( rs.getDate( "DtVencItRec" ) ), i, 3 );
				tbBoletos.setValor( rs.getString( "RazCli" ), i, 4 );
				tbBoletos.setValor( rs.getBigDecimal( "VlrParcItRec" ), i, 5 );

			}
			rs.close();
			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar tabela!\n" + e.getMessage() );
		}
	}

	private String getBoletos() {

		int numLinhas = tbBoletos.getNumLinhas();
		int numLinhasSel = 0;
		String[] sValores = null;
		Vector<String> vValores = new Vector<String>();
		String sRet = "";

		try {

			for ( int i = 0; i < numLinhas; i++ ) {

				if ( tbBoletos.getValor( i, 0 ).equals( true ) ) {

					vValores.add( tbBoletos.getValor( i, 1 ).toString() );

				}
			}
			sRet = Funcoes.vectorToString( vValores, "," );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return sRet;
	}

	private int verificaSel( Component comp ) {

		int count = 0;

		for ( int i = 0; i < tbBoletos.getNumLinhas(); i++ ) {

			if ( tbBoletos.getValor( i, 0 ).equals( true ) ) {

				count++;
			}
		}

		return count;
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		imprimir( bVisualizar, null );
	}

	public void imprimir( TYPE_PRINT bVisualizar, JInternalFrame orig ) {

		if ( verificaSel( this ) == 0 ) {
			Funcoes.mensagemInforma( this, "Não existem boletos selecionados para impressão!" );
			return;
		}

		final int codvenda = txtCodVenda.getVlrInteger().intValue();
		final int codvenda2 = txtCodVenda2.getVlrInteger().intValue();
		final int nparc = txtParc.getVlrInteger().intValue();
		final String codbanco = txtCodBanco.getVlrString().trim();
		final int codtipocob = txtCodTpCob.getVlrInteger().intValue();
		final int codTipoMov = txtCodTipoMov.getVlrInteger().intValue();
		String sBoletos = getBoletos();
		String sWhere = " AND V.CODVENDA IN ( " + sBoletos + " )";

		if ( txtCodModBol.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Modelo de boleto não selecionado!" );
			txtCodModBol.requestFocus();
			return;
		}
		else if ( ( codvenda == 0 ) && ( ( "".equals( txtDtIni.getVlrString() ) ) || ( "".equals( txtDtFim.getVlrString() ) ) ) ) {
			Funcoes.mensagemInforma( this, "Período não selecionado!" );
			txtDtIni.requestFocus();
			return;
		}

		try {
			lcModBol.carregaDados();
			ResultSet rs = execQuery( sWhere );

			String classe = getClassModelo( txtPreImpModBol.getVlrString(), txtClassModBol.getVlrString() );

			if ( classe == null ) {
				imprimeTexto( bVisualizar, rs );
			}
			else {
				imprimeGrafico( bVisualizar, rs, classe, orig );
			}

			rs.close();

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao tentar imprimir!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimeTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) throws Exception {

		String sVal = null;
		ImprimeOS imp = null;
		imp = new ImprimeOS( "", con );
		imp.verifLinPag();
		imp.setTitulo( "Boleto" );
		String[] sNat = null;
		while ( rs.next() ) {
			sNat = new String[ 2 ];
			sNat[ 0 ] = rs.getString( "CODNAT" );
			sNat[ 1 ] = rs.getString( "DESCNAT" );
			sVal = aplicCampos( rs, sNat, moeda );

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

			if ( fExt == null ) {
				imp.preview( this );
			}
			else {
				imp.preview( fExt );
			}
		}
		else {
			imp.print();
		}
	}

	private void imprimeGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String classe, JInternalFrame orig ) {

		FPrinterJob dlGr = new FPrinterJob( classe, "Boleto", null, rs, getParametros(), orig == null ? this : orig );

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

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcModBol.setConexao( cn );
		lcVenda.setConexao( cn );
		lcCli.setConexao( cn );
		lcCli2.setConexao( cn );
		lcBanco.setConexao( cn );
		lcTipoCob.setConexao( cn );
		lcVenda2.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcCartCob.setConexao( cn );
		moeda = getMoeda();

		getAtualizaParcela();
		
		getTpnossonumero();
		
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );

		if ( evt.getSource() == btGerar ) {
			gerar();
		}
	}

	public void gerar() {

		tbBoletos.limpa();
		montaGrid();
	}
	
	public void afterCarrega( CarregaEvent cevt ) {
		if(cevt.getListaCampos() == lcCartCob ){
			//System.out.println(txtCodBanco.getVlrString());
			txtNumConta.setVlrString( getNumConta( txtCodModBol.getVlrInteger()
					, txtCodBanco.getVlrString() , txtCodCartCob.getVlrString()  ) );
		}
	}

	public void afterInsert( InsertEvent ievt ) {
		
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}
}
