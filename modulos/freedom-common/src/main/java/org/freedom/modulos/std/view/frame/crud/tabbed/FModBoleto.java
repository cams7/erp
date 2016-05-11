/**
 * @version 18/12/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FModBoleto.java <BR>
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
 *         Monta o org.freedom.layout para o boleto bancário.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;
import org.freedom.modulos.fnc.view.frame.crud.plain.FCartCob;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;

public class FModBoleto extends FTabDados implements ActionListener, JComboBoxListener, CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private final CardLayout cardlayout = new CardLayout();

	private final JPanelPad pnGeral = new JPanelPad( new BorderLayout() );

	private final JPanelPad pinCab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCampos = new JPanelPad();

	private final JPanelPad panelBoleto = new JPanelPad( JPanelPad.TP_JPANEL, cardlayout );

	private final JPanelPad pinPreImp = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelPreImp = new JPanelPad();

	private final JPanelPad panelBolElect = new JPanelPad();

	private final JPanelPad panelBancos = new JPanelPad( new BorderLayout() );

	private final JPanelPad panelCamposBancos = new JPanelPad();

	private final JTextFieldPad txtCodModBol = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private final JTextFieldFK txtSeqNossoNumero = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtDescModBol = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodCartCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtDescCartCob = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtConvCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldPad txtDvConvCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JCheckBoxPad ckPreImp = new JCheckBoxPad( "Usa boleto pré-impresso ?", "S", "N" );

	private final JTextAreaPad txaBoleto = new JTextAreaPad( 10000 );

	private final JScrollPane spnCli = new JScrollPane( txaBoleto );

	private final JTextFieldPad txtAdic = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 8, 2 );

	private final JButtonPad btAdic = new JButtonPad( Icone.novo( "btOk.png" ) );

	private JComboBoxPad cbCamposDin = null;

	private JComboBoxPad cbCamposEspec = null;

	private final JTextFieldPad txtClassModBol = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextFieldPad txtEspecie = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldPad txtMdeCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JCheckBoxPad ckAceite = new JCheckBoxPad( "Aceite ?", "S", "N" );

	private final JTextFieldPad txtDescLocaPag = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextAreaPad txaInstrucao = new JTextAreaPad( 500 );

	private final JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtNomeBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	

	// private final JButtonPad btPath = new JButtonPad( "..." );

	private JComboBoxPad cbAcao = null;

	private JCheckBoxPad cbImpDoc = new JCheckBoxPad( "Imprimi doc. nas instruções?", "S", "N" );

	private final JTablePad tabBancos = new JTablePad();

	private final Navegador navBancos = new Navegador( true );

	private final ListaCampos lcConta = new ListaCampos( this, "CT" );

	private final ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private final ListaCampos lcCartCob = new ListaCampos( this, "CB" );

	private final ListaCampos lcItModBol = new ListaCampos( this );
	
	private JButtonPad btAjustaSeqNossoNumero = new JButtonPad( Icone.novo( "btReset.png" ) );

	public FModBoleto() {

		super();
		setTitulo( "Modelo de boleto/Recibo" );
		setAtribos( 30, 30, 730, 500 );

		lcItModBol.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcItModBol );
		lcItModBol.setTabela( tabBancos );
		lcItModBol.setNavegador( navBancos );
		navBancos.setListaCampos( lcItModBol );

		montaCombos();
		montaListaCampos();
		montaTela();

		lcItModBol.montaTab();
		tabBancos.setTamColuna( 70, 0 );
		tabBancos.setTamColuna( 200, 1 );
		tabBancos.setTamColuna( 70, 2 );
		tabBancos.setTamColuna( 200, 3 );
		tabBancos.setTamColuna( 70, 4 );

		// lcItModBol.setState( ListaCampos.LCS_NONE );

		txaBoleto.setFont( new Font( "Courier", Font.PLAIN, 11 ) );
		txaBoleto.setTabSize( 0 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAdic.addActionListener( this );
		// btPath.addActionListener( this );

		cbCamposDin.addComboBoxListener( this );
		cbAcao.addComboBoxListener( this );

		ckPreImp.addCheckBoxListener( this );

		ckAceite.setVlrString( "N" );

		ckPreImp.setVlrString( "S" );
		
		btAjustaSeqNossoNumero.setToolTipText( "Ajustar sequencia." );
		
		btAjustaSeqNossoNumero.addActionListener( this );

		setImprimir( true );

	}

	private void montaListaCampos() {

		/*************
		 * FNCONTA *
		 *************/
		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Cód.conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

		/*************
		 * FNBANCO *
		 *************/
		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, txtNomeBanco, false ) );
		lcBanco.add( new GuardaCampo( txtNomeBanco, "NomeBanco", "Nome do banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setReadOnly( true );
		txtCodBanco.setListaCampos( lcBanco );
		txtCodBanco.setTabelaExterna( lcBanco, FBanco.class.getCanonicalName() );
		txtCodBanco.setNomeCampo( "CodBanco" );
		txtCodBanco.setFK( true );
		txtNomeBanco.setLabel( "Nome do banco" );

		/***************
		 * FNCARTCOB *
		 ***************/
		lcCartCob.add( new GuardaCampo( txtCodCartCob, "CodCartCob", "Cód.cart.cob.", ListaCampos.DB_PK, txtDescCartCob, false ) );
		lcCartCob.add( new GuardaCampo( txtDescCartCob, "DescCartCob", "Descrição da carteira de cobrança", ListaCampos.DB_SI, false ) );
		lcCartCob.setWhereAdicSubSel( "CODBANCO=master.CODBANCO" );
		lcCartCob.montaSql( false, "CARTCOB", "FN" );
		lcCartCob.setReadOnly( true );
		txtCodCartCob.setListaCampos( lcCartCob );
		txtCodCartCob.setTabelaExterna( lcCartCob, FCartCob.class.getCanonicalName() );
		txtCodCartCob.setNomeCampo( "CodCartCob" );
		txtCodCartCob.setFK( true );
		txtDescCartCob.setLabel( "Descrição da carteira de cobrança" );
	}

	private void montaCombos() {

		Vector<String> vLabs = new Vector<String>();
		vLabs.addElement( "<--Selecione-->" );
		vLabs.addElement( "Vencimento" );
		vLabs.addElement( "Data documento" );
		vLabs.addElement( "Nº documento" );
		vLabs.addElement( "Nº recibo" );
		vLabs.addElement( "Nº baixa" );
		vLabs.addElement( "Parcela (No.)" );
		vLabs.addElement( "Parcela (A,B...)" );
		vLabs.addElement( "Total de parcelas" );
		vLabs.addElement( "Valor do documento" );
		vLabs.addElement( "Valor liquido" );
		vLabs.addElement( "Valor extenso" );
		vLabs.addElement( "Desconto na parcela" );
		vLabs.addElement( "Código do cliente/fornecedor" );
		vLabs.addElement( "Razão do cliente/fornecedor" );
		vLabs.addElement( "Nome do cliente/fornecedor" );
		vLabs.addElement( "CPF ou CNPJ do cliente/fornecedor" );
		vLabs.addElement( "IE ou RG do cliente/fornecedor" );
		vLabs.addElement( "Endereço do cliente/fornecedor" );
		vLabs.addElement( "Número" );
		vLabs.addElement( "Endereço com número cliente/fornecedor" );
		vLabs.addElement( "Complemento" );
		vLabs.addElement( "CEP" );
		vLabs.addElement( "Bairro do cliente/fornecedor" );
		vLabs.addElement( "Cidade do cliente/fornecedor" );
		vLabs.addElement( "UF do cliente/fornecedor" );
		vLabs.addElement( "Telefone do cliente/fornecedor" );
		vLabs.addElement( "DDD do cliente/fornecedor" );
		vLabs.addElement( "CFOP" );
		vLabs.addElement( "Descrição da natureza" );
		vLabs.addElement( "Codigo do orçamento" );
		// vLabs.addElement( "Código do conveniado" );
		vLabs.addElement( "Nome do conveniado" );
		vLabs.addElement( "Observações do orçamento/pagar" );
		vLabs.addElement( "Observações da venda" );
		vLabs.addElement( "Código da venda" );
		vLabs.addElement( "Dia de emissão" );
		vLabs.addElement( "Mês de emissão" );
		vLabs.addElement( "Ano de emissão" );
		vLabs.addElement( "Total das parcelas" );
		vLabs.addElement( "Comissionado 1" );
		vLabs.addElement( "Comissionado 2" );
		vLabs.addElement( "Valor Pago" );		
		vLabs.addElement( "Extenso desconto" ); 
		vLabs.addElement( "Nro.Cheque" );
		
		vLabs.addElement( "Valor Bruto" );
		
		vLabs.addElement( "Base do INSS Retido" );
		vLabs.addElement( "Valor do INNS Retido" );
		vLabs.addElement( "Aliq do INSS Retido" );
		
		vLabs.addElement( "Base do IRRF Retido" );
		vLabs.addElement( "Valor do IRRF Retido" );
		vLabs.addElement( "Aliquota do IRRF Retido" );

		Vector<String> vVals = new Vector<String>();
		vVals.addElement( "" ); // larg: 10
		vVals.addElement( "[VENCIMEN]" ); // larg: 10
		vVals.addElement( "[DATADOC_]" ); // larg: 10
		vVals.addElement( "[__DOCUMENTO__]" ); // larg: 15
		vVals.addElement( "[RECIBO]" ); // larg: 8
		vVals.addElement( "[CODREC]" ); // larg: 8
		vVals.addElement( "[P]" ); // larg: 3
		vVals.addElement( "[A]" ); // larg: 3
		vVals.addElement( "[T]" ); // larg: 3
		vVals.addElement( "[VALOR_DOCUMEN]" ); // larg: 15
		vVals.addElement( "[VLIQ_DOCUMENT]" ); // larg: 15
		vVals.addElement( "[VALOR_EXTENSO]" ); // larg: 15
		vVals.addElement( "[DESC_DOCUMENT]" ); // larg: 15
		vVals.addElement( "[CODCLI]" ); // larg: 8
		vVals.addElement( "[_____________RAZAO____DO____CLIENTE_____________]" ); // larg: 50
		vVals.addElement( "[_____________NOME_____DO____CLIENTE_____________]" ); // larg: 50
		vVals.addElement( "[CPF/CNPJ_ CLIENT]" ); // larg: 18
		vVals.addElement( "[____IE/RG____CLIENTE]" ); // larg: 22
		vVals.addElement( "[____________ENDERECO____DO____CLIENTE___________]" ); // larg: 50
		vVals.addElement( "[NUMERO]" ); // larg: 8
		vVals.addElement( "[_______ENDERECO_COM_NUMERO_DO_CLIENTE___________]" ); // larg: 50
		vVals.addElement( "[____COMPLEMENTO___]" ); // larg: 20
		vVals.addElement( "[__CEP__]" ); // larg: 9
		vVals.addElement( "[___________BAIRRO___________]" ); // larg: 30
		vVals.addElement( "[___________CIDADE___________]" ); // larg: 30
		vVals.addElement( "[UF]" ); // larg: 2h
		vVals.addElement( "[__TELEFONE___]" ); // larg: 15
		vVals.addElement( "[DDD]" );// larg: 4
		vVals.addElement( "[CODNAT]" ); // larg: 8
		vVals.addElement( "[______________NATUREZA_DA_OPERACAO______________]" ); // larg: 50
		vVals.addElement( "[_CODORC_]" ); // larg: 10
		// vVals.addElement( "[_CODCONV]" ); // larg: 10
		vVals.addElement( "[_____________________NOMECONV___________________]" ); // larg: 50
		vVals.addElement( "[OBSORC_LLL_CCC]" ); // larg: 50
		vVals.addElement( "[OBSVEN_LLL_CCC]" ); // larg: 50
		vVals.addElement( "[CODVENDA]" ); // larg: 10
		vVals.addElement( "[DIA_E]" ); //
		vVals.addElement( "[MES_E]" );
		vVals.addElement( "[ANO_E]" );
		vVals.addElement( "[TOTAL_PARCELAS]" );
		vVals.addElement( "[_______COMISSIONADO1_______]" );
		vVals.addElement( "[_______COMISSIONADO2_______]" );
		vVals.addElement( "[VPAGO_DOCUMENT]" ); // larg: 15
		vVals.addElement( "[VALOR_EXTENSO_DESC]" ); 
		vVals.addElement( "[NUM_CHEQ]" ); 
		
		vVals.addElement( "[VALOR_BRUTO]" );
		
		vVals.addElement( "[BASE_INSS]" );
		vVals.addElement( "[VALOR_INSS]" );
		vVals.addElement( "[ALIQ_INSS]" );

		vVals.addElement( "[BASE_IRRF]" );
		vVals.addElement( "[VALOR_IRRF]" );
		vVals.addElement( "[ALIQ_IRRF]" );
		
		cbCamposDin = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_STRING, 50, 0 );

		Vector<String> vLabs2 = new Vector<String>();
		vLabs2.addElement( "<--Selecione-->" );
		vLabs2.addElement( "'%' Valor" );
		vLabs2.addElement( "Valor '+'" );
		vLabs2.addElement( "Valor '-'" );
		vLabs2.addElement( "Vencimento '+'" );

		Vector<String> vVals2 = new Vector<String>();
		vVals2.addElement( "" );
		vVals2.addElement( "[#####.##%_VAL]" );
		vVals2.addElement( "[#####.##+_VAL]" );
		vVals2.addElement( "[#####.##-_VAL]" );
		vVals2.addElement( "[###+_VEN]" );

		cbCamposEspec = new JComboBoxPad( vLabs2, vVals2, JComboBoxPad.TP_STRING, 20, 0 );

		Vector<String> vLabs3 = new Vector<String>();
		vLabs3.addElement( "<--Selecione-->" );
		vLabs3.addElement( "Limpa se campo vazio" );
		vLabs3.addElement( "Eject" );

		Vector<String> vVals3 = new Vector<String>();
		vVals3.addElement( "" );
		vVals3.addElement( "<LP><_LP>" );
		vVals3.addElement( "<EJECT>" );

		cbAcao = new JComboBoxPad( vLabs3, vVals3, JComboBoxPad.TP_STRING, 10, 0 );
	}

	private void montaTela() {

		/****************
		 * Aba Geral *
		 ****************/

		adicTab( "Geral", pnGeral );

		panelCampos.setPreferredSize( new Dimension( 750, 110 ) );
		pinCab.add( panelCampos, BorderLayout.NORTH );

		setPainel( panelCampos );

		adicCampo( txtCodModBol, 7, 30, 90, 20, "CodModBol", "Cód.mod.bol.", ListaCampos.DB_PK, true );
		adicCampo( txtDescModBol, 100, 30, 250, 20, "DescModBol", "Descrição do modelo de boleto", ListaCampos.DB_SI, true );

		adicCampo( txtMdeCob, 353, 30, 97, 20, "mdeCob", "Modalidade", ListaCampos.DB_SI, true );
		adicDB( ckPreImp, 460, 30, 200, 20, "PreImpModBol", "", false );
		adicDB( cbImpDoc, 460, 60, 200, 20, "ImpInfoParc", "", false );
		// adicCampo( txtCodConta, 7, 70, 90, 20, "NumConta", "Nº da conta", ListaCampos.DB_FK, txtDescConta, false );
		// adicDescFK( txtDescConta, 100, 70, 350, 20, "DescConta", "Descrição da conta" );
		adicDBLiv( txaBoleto, "TxaModBol", "Corpo", false );
		adicDBLiv( txtClassModBol, "ClassModBol", "Classe modelo", false );
		adicDBLiv( txtEspecie, "EspDocModBol", "Espécie Doc.", false );
		adicDBLiv( ckAceite, "AceiteModBol", "Aceite", false );
		adicDBLiv( txtDescLocaPag, "DescLPModBol", "Espécie Doc.", false );
		adicDBLiv( txaInstrucao, "InstPagModBol", "Instruçâo", false );

		setListaCampos( true, "MODBOLETO", "FN" );

		/***********************************
		 * painel de boleto pre-impresso *
		 ***********************************/
		panelPreImp.adic( new JLabelPad( "Campos de dados" ), 7, 10, 223, 20 );
		panelPreImp.adic( cbCamposDin, 7, 30, 223, 20 );
		panelPreImp.adic( new JLabelPad( "Campos especiais de dados" ), 240, 10, 220, 20 );
		panelPreImp.adic( cbCamposEspec, 240, 30, 115, 20 );
		panelPreImp.adic( txtAdic, 360, 30, 100, 20 );
		panelPreImp.adic( btAdic, 470, 20, 30, 30 );
		panelPreImp.adic( new JLabelPad( "Ações" ), 7, 50, 223, 20 );
		panelPreImp.adic( cbAcao, 7, 70, 223, 20 );
		panelPreImp.setPreferredSize( new Dimension( 750, 110 ) );

		pinPreImp.add( panelPreImp, BorderLayout.NORTH );
		pinPreImp.add( spnCli, BorderLayout.CENTER );

		/*********************************
		 * painel de boleto eletronico *
		 *********************************/
		panelBolElect.adic( new JLabelPad( "Class do modelo de boleto" ), 7, 10, 350, 20 );
		panelBolElect.adic( txtClassModBol, 7, 30, 350, 20 );
		panelBolElect.adic( new JLabelPad( "Espécie doc." ), 360, 10, 100, 20 );
		panelBolElect.adic( txtEspecie, 360, 30, 100, 20 );
		panelBolElect.adic( ckAceite, 470, 30, 100, 20 );
		panelBolElect.adic( new JLabelPad( "Descrição do local de pagamento" ), 7, 50, 450, 20 );
		panelBolElect.adic( txtDescLocaPag, 7, 70, 453, 20 );
		panelBolElect.adic( new JLabelPad( "Instruções de cobrança" ), 7, 90, 450, 20 );
		panelBolElect.adic( new JScrollPane( txaInstrucao ), 7, 110, 453, 130 );
		// panelBolElect.adic( btPath, 370, 27, 26, 26 );

		panelBoleto.add( "preimp", pinPreImp );
		panelBoleto.add( "bolelect", panelBolElect );
		pinCab.add( panelBoleto, BorderLayout.CENTER );

		/****************
		 * Aba Bancos *
		 ****************/

		adicTab( "Bancos", panelBancos );

		panelBancos.add( new JScrollPane( tabBancos ), BorderLayout.CENTER );

		panelCamposBancos.setPreferredSize( new Dimension( 600, 140 ) );

		setPainel( panelCamposBancos );

		setListaCampos( lcItModBol );
		setNavegador( navBancos );

		adicCampo( txtCodBanco, 7, 30, 80, 20, "CodBanco", "Cód.banco", ListaCampos.DB_PF, txtNomeBanco, true );
		adicDescFK( txtNomeBanco, 90, 30, 200, 20, "NomeBanco", "Nome do banco" );
		adicCampo( txtCodCartCob, 293, 30, 80, 20, "CodCartCob", "Cart.cob.", ListaCampos.DB_PF, txtDescCartCob, true );
		adicDescFK( txtDescCartCob, 376, 30, 200, 20, "DescCartCob", "Descrição da carteira de cobrança" );
		adicCampo( txtConvCob, 579, 30, 70, 20, "ConvCob", "Convênio cob.", ListaCampos.DB_SI, true );
		adicCampo( txtDvConvCob, 652, 30, 50, 20, "DvConvCob", "Dig.", ListaCampos.DB_SI, false );

		adicCampo( txtCodConta, 7, 70, 80, 20, "NumConta", "Nº da conta", ListaCampos.DB_FK, txtDescConta, false );
		adicDescFK( txtDescConta, 90, 70, 283, 20, "DescConta", "Descrição da conta" );

		txtSeqNossoNumero.setSoLeitura( true );
		
		adicCampo( txtSeqNossoNumero, 376, 70, 120, 20, "SeqNossoNumero", "Seq.(Nosso Número)", ListaCampos.DB_SI , false );

		adic( btAjustaSeqNossoNumero, 499, 65, 26, 26 );
		
		adic( navBancos, 0, 105, 270, 30 );

		setListaCampos( false, "ITMODBOLETO", "FN" );

		panelBancos.add( panelCamposBancos, BorderLayout.SOUTH );

		pnGeral.add( pinCab );

	}
	
	private void ajustaSeqNossoNumero() {
		
		StringBuilder sql = new StringBuilder();
		JTextFieldPad txtReset = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
		int alterado = 0;
		
		PreparedStatement ps = null;
		
		try {

			// Mondando sql para atualização da sequencia.
			sql.append("update fnitmodboleto set seqnossonumero=? ");
			sql.append("where codemp=? and codfilial=? and codmodbol=? and ");
			sql.append("codempbo=? and codfilialbo=? and codbanco=? and ");
			sql.append("codempcb=? and codfilialcb=? and codcartcob=? ");

			// Gerando dialog para captura do novo numero
			FFDialogo dlReset = new FFDialogo( this );
			dlReset.setTitulo( "Ajustar Numeração" );
			dlReset.setAtribos( 280, 140 );
			dlReset.adic( new JLabelPad("Seq:"), 7, 5, 100, 20 );
			dlReset.adic( txtReset, 7, 25, 100, 20 );
			
			dlReset.setVisible( true );
			
			if ( dlReset.OK ) {
				
				if(Funcoes.mensagemConfirma( this, "Confirma o ajuste na sequência?" ) == JOptionPane.YES_OPTION ) {
				
					// Setando parametros para query de atualização da sequencia.
					ps = con.prepareStatement( sql.toString() );
					
					ps.setInt( 1, txtReset.getVlrInteger() );
					
					ps.setInt( 2, lcSeq.getCodEmp() );
					ps.setInt( 3, lcSeq.getCodFilial() );				
					ps.setInt( 4, txtCodModBol.getVlrInteger() );
					
					ps.setInt( 5, lcBanco.getCodEmp() );
					ps.setInt( 6, lcBanco.getCodFilial() );
					ps.setString( 7, txtCodBanco.getVlrString() );
					
					ps.setInt( 8, lcCartCob.getCodEmp() );
					ps.setInt( 9, lcCartCob.getCodFilial() );
					ps.setString( 10, txtCodCartCob.getVlrString() );
					
					alterado = ps.executeUpdate();
				
				}
				
				
			}
			
			dlReset.dispose();
			
			if(alterado>0) {
				Funcoes.mensagemInforma( this, "Sequencia alterada com sucesso!" );
			}
			
			lcSeq.carregaDados();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@ Override
	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcCampos ) {

			if ( txaBoleto.getVlrString() == null || txaBoleto.getVlrString().trim().length() == 0 ) {

				txaBoleto.setVlrString( "Modelo em branco" );
			}
		}

		super.beforePost( pevt );
	}

	public void valorAlterado( CheckBoxEvent evt ) {

		if ( evt.getCheckBox() == ckPreImp ) {

			if ( "S".equals( ckPreImp.getVlrString() ) ) {
				cardlayout.first( panelBoleto );
			}
			else if ( "N".equals( ckPreImp.getVlrString() ) ) {
				cardlayout.last( panelBoleto );
			}
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbCamposDin ) {

			txaBoleto.insert( cbCamposDin.getVlrString(), txaBoleto.getCaretPosition() );
		}
		else if ( evt.getComboBoxPad() == cbAcao ) {

			txaBoleto.insert( cbAcao.getVlrString(), txaBoleto.getCaretPosition() );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btAdic ) {

			BigDecimal bigVal = txtAdic.getVlrBigDecimal().setScale( 2, BigDecimal.ROUND_HALF_UP );
			String sVal = bigVal.toString();

			if ( cbCamposEspec.getSelectedIndex() < 3 ) {
				// Campos de valores
				sVal = cbCamposEspec.getVlrString().replaceAll( "#####.##", StringFunctions.strZero( bigVal.setScale( 2, BigDecimal.ROUND_HALF_UP ).toString(), 8 ) );
			}
			else if ( cbCamposEspec.getSelectedIndex() == 3 ) {
				// Campos de datas
				sVal = cbCamposEspec.getVlrString().replaceAll( "###", StringFunctions.strZero( bigVal.intValue() + "", 3 ) );
			}

			txaBoleto.insert( sVal, txaBoleto.getCaretPosition() );
		}
		else if ( evt.getSource() == btImp ) {

			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btPrevimp ) {

			imprimir( TYPE_PRINT.VIEW );
		}
		else if(evt.getSource()==btAjustaSeqNossoNumero) {
			ajustaSeqNossoNumero();
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		if ( "S".equals( ckPreImp.getVlrString() ) ) {
			imprimirTexto( bVisualizar );
		}
		else {
			imprimeGrafico( bVisualizar );
		}
	}

	private void imprimirTexto( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		imp.verifLinPag();
		imp.setTitulo( "Teste de boleto" );
		imp.limpaPags();

		String[] sLinhas = txaBoleto.getText().split( "\n" );

		for ( int i = 0; i < sLinhas.length; i++ ) {

			imp.say( imp.pRow() + 1, 0, sLinhas[ i ] );
		}

		imp.eject();
		imp.fechaGravacao();

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	private void imprimeGrafico( TYPE_PRINT bVisualizar ) {

		/*
		 * FPrinterJob dlGr = new FPrinterJob( "relatorios/TipoCli.jasper", "Vendas por Cliente", null, rs, null, this );
		 * 
		 * if ( bVisualizar==TYPE_PRINT.VIEW ) { dlGr.setVisible( true ); } else { try { JasperPrintManager.printReport( dlGr.getRelatorio(), true ); } catch ( Exception err ) { Funcoes.mensagemErro( this, "Erro na impressão de relatório de vendas por cliente!" + err.getMessage(), true, con, err ); } }
		 */
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcConta.setConexao( cn );
		lcBanco.setConexao( cn );
		lcCartCob.setConexao( cn );
		lcItModBol.setConexao( cn );
	}

}
