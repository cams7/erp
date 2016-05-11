/**
 * @version 8/02/2007 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc <BR>
 *         Classe:
 * @(#)FPrefereFBB.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Tela de preferencias para Febraban
 * 
 */

package org.freedom.modulos.fnc.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.fnc.view.frame.crud.plain.FBanco;

public class FPrefereFBB extends FTabDados implements CarregaListener, JComboBoxListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelGeral = new JPanelPad();

	private final JPanelPad panelSiacc = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private final JPanelPad panelCaminhos = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private final JPanelPad panelCaminhosSiacc = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelTabSiacc = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private final JPanelPad panelSiaccGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private final JPanelPad panelSiaccPref = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );
	
	private final JTabbedPanePad tbSiacc = new JTabbedPanePad();
	
	private final JPanelPad panelCamposSiaccPref = new JPanelPad( 300, 340 );

	private final JPanelPad panelCamposSiacc = new JPanelPad();
	
	private final JPanelPad panelCamposSiaccCaminhos = new JPanelPad();
	
	private final JPanelPad panelCamposCnabCaminhos = new JPanelPad();

	private final JPanelPad panelNavSiacc = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JTabbedPanePad tbCnab = new JTabbedPanePad();

	private final JPanelPad panelCnab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCnabManager = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCnabGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCnabPref = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelTabCnab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelCamposCnab = new JPanelPad();

	private final JPanelPad panelCamposCnabPref = new JPanelPad( 300, 340 );

	private final JPanelPad panelNavCnab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JTextFieldPad txtNomeEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtNomeEmpCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );
	
	private JCheckBoxPad cbSobrescreveHist = new JCheckBoxPad( "Sobrescreve histórico dos títulos na baixa.", "S", "N" );

	private final JTextFieldPad txtTipoSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCodBancoSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldFK txtNomeBancoSiacc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodConvSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtVersaoSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	
	private final JTextFieldPad txtCaminhoRemessa = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );
	
	private final JTextFieldPad txtCaminhoRetorno = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );
	
	private final JTextFieldPad txtBackupRemessa = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );
	
	private final JTextFieldPad txtBackupRetorno = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );

	private final JTextFieldPad txtCaminhoRemessaSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );
	
	private final JTextFieldPad txtCaminhoRetornoSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );
	
	private final JTextFieldPad txtBackupRemessaSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );
	
	private final JTextFieldPad txtBackupRetornoSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 300, 0 );

	private final JTextFieldPad txtIdentServSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 17, 0 );

	private final JTextFieldPad txtContaComprSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 16, 0 );

	private final JTextFieldPad txtNroSeqSiacc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JRadioGroup<?, ?> rgIdentAmbCliSiacc;

	private JRadioGroup<?, ?> rgIdentAmbBcoSiacc;

	private final JTextFieldPad txtTipoCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCodBancoCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldFK txtNomeBancoCnab = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodConvCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtVersaoCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtIdentServCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 17, 0 );

	private final JTextFieldPad txtContaComprCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 16, 0 );

	private final JTextFieldPad txtNroSeqCnab = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtNumContaCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtAgenciaCnab = new JTextFieldFK( JTextFieldFK.TP_STRING, 6, 0 );

	private final JTextFieldFK txtDescContaCnab = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private final JTextFieldPad txtNumContaSiacc = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtAgenciaSiacc = new JTextFieldFK( JTextFieldFK.TP_STRING, 6, 0 );

	private final JTextFieldFK txtDescContaSiacc = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );

	private final JTextFieldPad txtModalidadeCnab = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtConvBol = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JRadioGroup<?, ?> rgIdentAmbCliCnab;

	private JRadioGroup<?, ?> rgIdentAmbBcoCnab;

	private JComboBoxPad cbFormaCadastramento;
	
	private JComboBoxPad cbPadraoSIACC;

	private JComboBoxPad cbPadraoCNAB;

	private JComboBoxPad cbTipoDocumento;

	private JComboBoxPad cbEmissaoBloqueto;

	private JComboBoxPad cbDistribuicao;

	private JComboBoxPad cbEspecieTitulo;

	private JComboBoxPad cbJurosMora;
	
	private JComboBoxPad cbInstrucoes;

	private JComboBoxPad cbOutrasInstrucoes;
	
	private final JTextFieldPad txtVlrJuros = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private final JTextFieldPad txtVlrPercMulta = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JComboBoxPad cbDesconto;

	private final JTextFieldPad txtVlrDesconto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin );

	private JComboBoxPad cbProtesto;

	private final JTextFieldPad txtNumDiasProtesto = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JComboBoxPad cbDevolucao;

	private final JTextFieldPad txtNumDiasDevolucao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 3, 0 );

	private JComboBoxPad cbAceite;

	private final ListaCampos lcSiacc = new ListaCampos( this, "BO" );

	private final ListaCampos lcCnab = new ListaCampos( this, "BO" );

	private final ListaCampos lcBancoSiacc = new ListaCampos( this, "BO" );

	private final ListaCampos lcBancoCnab = new ListaCampos( this, "BO" );

	private final ListaCampos lcContaCnab = new ListaCampos( this, "CA" );

	private final ListaCampos lcContaSiacc = new ListaCampos( this, "CA" );

	private final Navegador nvSiacc = new Navegador( true );

	private final Navegador nvCnab = new Navegador( true );

	private final JTablePad tabSiacc = new JTablePad();

	private final JTablePad tabCnab = new JTablePad();

	public static final String TP_SIACC = "01";

	public static final String TP_CNAB = "02";

	// Espécie do título
	private Vector<String> vLabs5;
	
	private Vector<Integer> vVals5;

	// Instrução de protesto / juros
	private Vector<String> vLabs8 = new Vector<String>();
	
	private Vector<Integer> vVals8 = new Vector<Integer>();
	
	// Banco 748 - Instruções
	private Vector<String> vLabsInstr = new Vector<String>();
	private Vector<Integer> vValsInstr = new Vector<Integer>();
	
	// Banco 748 - Outras Instruções
	private Vector<String> vLabsOutrasInstr = new Vector<String>();
	private Vector<Integer> vValsOutrasInstr = new Vector<Integer>();
	
	//Botões Cnab
	private final JButtonPad btGetCaminhoRemessa = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private final JButtonPad btGetCaminhoRetorno = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private final JButtonPad btGetBackupRemessa = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private final JButtonPad btGetBackupRetorno = new JButtonPad(Icone.novo("btAbrirPeq.png"));

	//Botões Siacc
	private final JButtonPad btGetCaminhoRemessaSiacc = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private final JButtonPad btGetCaminhoRetornoSiacc = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private final JButtonPad btGetBackupRemessaSiacc = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	private final JButtonPad btGetBackupRetornoSiacc = new JButtonPad(Icone.novo("btAbrirPeq.png"));
	
	public FPrefereFBB() {

		setTitulo( "Preferências Febraban" );
		setAtribos( 20, 20, 790, 600 );

		montaRadioGrupos();
		montaComboBoxs();
		montaListaCampos();
		montaTela();

		lcSiacc.montaTab();
		lcCnab.montaTab();

		tabSiacc.setTamColuna( 40, 0 );
		tabSiacc.setTamColuna( 80, 1 );
		tabSiacc.setTamColuna( 150, 2 );
		tabSiacc.setTamColuna( 50, 3 );

		tabCnab.setTamColuna( 40, 0 );
		tabCnab.setTamColuna( 80, 1 );
		tabCnab.setTamColuna( 150, 2 );
		tabCnab.setTamColuna( 50, 3 );

		txtTipoSiacc.setVlrString( TP_SIACC );
		txtTipoCnab.setVlrString( TP_CNAB );

		lcCnab.addPostListener( this );
		lcSiacc.addPostListener( this );

		lcCampos.addCarregaListener( this );
		lcCnab.addCarregaListener( this );
		lcSiacc.addCarregaListener( this );
		
		btGetCaminhoRemessa.setToolTipText("Localizar diretório");
		btGetCaminhoRetorno.setToolTipText("Localizar diretório");
		btGetBackupRemessa.setToolTipText("Localizar diretório");
		btGetBackupRetorno.setToolTipText("Localizar diretório");

		btGetCaminhoRemessa.addActionListener(this);
		btGetCaminhoRetorno.addActionListener(this);
		btGetBackupRemessa.addActionListener(this);
		btGetBackupRetorno.addActionListener(this);

	}

	private void montaRadioGrupos() {

		Vector<String> vLabs0 = new Vector<String>();
		Vector<String> vVals0 = new Vector<String>();
		vLabs0.add( "Produção" );
		vLabs0.add( "Teste" );
		vVals0.add( "P" );
		vVals0.add( "T" );
		rgIdentAmbCliSiacc = new JRadioGroup<String, String>( 2, 1, vLabs0, vVals0 );

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();
		vLabs1.add( "Produção" );
		vLabs1.add( "Teste" );
		vVals1.add( "P" );
		vVals1.add( "T" );
		rgIdentAmbBcoSiacc = new JRadioGroup<String, String>( 2, 1, vLabs1, vVals1 );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();
		vLabs2.add( "Produção" );
		vLabs2.add( "Teste" );
		vVals2.add( "P" );
		vVals2.add( "T" );
		rgIdentAmbCliCnab = new JRadioGroup<String, String>( 2, 1, vLabs2, vVals2 );

		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();
		vLabs3.add( "Produção" );
		vLabs3.add( "Teste" );
		vVals3.add( "P" );
		vVals3.add( "T" );
		rgIdentAmbBcoCnab = new JRadioGroup<String, String>( 2, 1, vLabs3, vVals3 );
	}

	private void montaComboBoxs() {

		Vector<String> vLabs1 = new Vector<String>();
		Vector<Integer> vVals1 = new Vector<Integer>();
		vLabs1.addElement( "com cadastro" );
		vLabs1.addElement( "sem cadastro" );
		vVals1.addElement( 1 );
		vVals1.addElement( 2 );
		cbFormaCadastramento = new JComboBoxPad( vLabs1, vVals1, JComboBoxPad.TP_INTEGER, 1, 0 );

		Vector<String> vLabs2 = new Vector<String>();
		Vector<Integer> vVals2 = new Vector<Integer>();
		vLabs2.addElement( "tradicional" );
		vLabs2.addElement( "escritural" );
		vVals2.addElement( 1 );
		vVals2.addElement( 2 );
		cbTipoDocumento = new JComboBoxPad( vLabs2, vVals2, JComboBoxPad.TP_INTEGER, 1, 0 );

		Vector<String> vLabs3 = new Vector<String>();
		Vector<Integer> vVals3 = new Vector<Integer>();
		vLabs3.addElement( "Banco emite" );
		vLabs3.addElement( "Empresa emite" );
		vLabs3.addElement( "Banco pré-emite e empresa completa" );
		vLabs3.addElement( "Banco reemite" );
		vLabs3.addElement( "Banco não reemite" );
		vLabs3.addElement( "Combraça sem papel" );
		vVals3.addElement( 1 );
		vVals3.addElement( 2 );
		vVals3.addElement( 3 );
		vVals3.addElement( 4 );
		vVals3.addElement( 5 );
		vVals3.addElement( 6 );
		cbEmissaoBloqueto = new JComboBoxPad( vLabs3, vVals3, JComboBoxPad.TP_INTEGER, 1, 0 );

		Vector<String> vLabs4 = new Vector<String>();
		Vector<Integer> vVals4 = new Vector<Integer>();
		vLabs4.addElement( "Banco" );
		vLabs4.addElement( "Empresa" );
		vVals4.addElement( 1 );
		vVals4.addElement( 2 );
		cbDistribuicao = new JComboBoxPad( vLabs4, vVals4, JComboBoxPad.TP_INTEGER, 1, 0 );

		// Espécio do título
        geraEspecieTitulo( "240", false, "" );
		cbEspecieTitulo = new JComboBoxPad( vLabs5, vVals5, JComboBoxPad.TP_INTEGER, 2, 0 );

		// Instrução de protesto
        geraProtesto("240", false);
		cbProtesto = new JComboBoxPad( vLabs8, vVals8, JComboBoxPad.TP_INTEGER, 1, 0 );
		
		// Instruções
		geraInstrucoes();
		cbInstrucoes = new JComboBoxPad( vLabsInstr, vValsInstr , JComboBoxPad.TP_INTEGER, 1, 0 );
		
		// Outras instruções
		geraOutrasInstrucoes();
		cbOutrasInstrucoes = new JComboBoxPad( vLabsOutrasInstr, vValsOutrasInstr, JComboBoxPad.TP_INTEGER, 1, 0 );
		
		Vector<String> vLabs6 = new Vector<String>();
		Vector<Integer> vVals6 = new Vector<Integer>();
		vLabs6.addElement( "Valor por dia" );
		vLabs6.addElement( "Taxa mensal" );
		vLabs6.addElement( "Isento" );
		vVals6.addElement( 1 );
		vVals6.addElement( 2 );
		vVals6.addElement( 3 );
		cbJurosMora = new JComboBoxPad( vLabs6, vVals6, JComboBoxPad.TP_INTEGER, 1, 0 );

		Vector<String> vLabs7 = new Vector<String>();
		Vector<Integer> vVals7 = new Vector<Integer>();
		vLabs7.addElement( "Sem desconto" );
		vLabs7.addElement( "Valor fixo até a data informada" );
		vLabs7.addElement( "Percentual até a data informada" );
		vLabs7.addElement( "Valor por antecipação por dia corrido" );
		vLabs7.addElement( "Valor por antecipação por dia util" );
		vLabs7.addElement( "Percentual sobre o valor nominal dia corrido" );
		vLabs7.addElement( "Percentual sobre o valor nominal dia util" );
		vVals7.addElement( 0 );
		vVals7.addElement( 1 );
		vVals7.addElement( 2 );
		vVals7.addElement( 3 );
		vVals7.addElement( 4 );
		vVals7.addElement( 5 );
		vVals7.addElement( 6 );
		cbDesconto = new JComboBoxPad( vLabs7, vVals7, JComboBoxPad.TP_INTEGER, 1, 0 );

		Vector<String> vLabs9 = new Vector<String>();
		Vector<Integer> vVals9 = new Vector<Integer>();
		vLabs9.addElement( "Baixar / Devolver" );
		vLabs9.addElement( "Não baixar / Não devlover" );
		vVals9.addElement( 1 );
		vVals9.addElement( 2 );
		cbDevolucao = new JComboBoxPad( vLabs9, vVals9, JComboBoxPad.TP_INTEGER, 1, 0 );

		Vector<String> vLabs10 = new Vector<String>();
		Vector<String> vVals10 = new Vector<String>();
		vLabs10.addElement( "Sim" );
		vLabs10.addElement( "Não" );
		vVals10.addElement( "S" );
		vVals10.addElement( "N" );
		cbAceite = new JComboBoxPad( vLabs10, vVals10, JComboBoxPad.TP_STRING, 1, 0 );
		
		Vector<String> vLabsSIACC = new Vector<String>();
		Vector<String> vValsSIACC = new Vector<String>();
		vLabsSIACC.addElement( "150 bytes" );
		vLabsSIACC.addElement( "240 bytes" );
		vValsSIACC.addElement( "150" );
		vValsSIACC.addElement( "240" );
		cbPadraoSIACC = new JComboBoxPad( vLabsSIACC, vValsSIACC, JComboBoxPad.TP_STRING, 1, 0 );
		cbPadraoSIACC.addComboBoxListener( this );		

		Vector<String> vLabs11 = new Vector<String>();
		Vector<String> vVals11 = new Vector<String>();
		vLabs11.addElement( "240 bytes" );
		vLabs11.addElement( "400 bytes" );
		vVals11.addElement( "240" );
		vVals11.addElement( "400" );
		cbPadraoCNAB = new JComboBoxPad( vLabs11, vVals11, JComboBoxPad.TP_STRING, 1, 0 );
		cbPadraoCNAB.addComboBoxListener( this );

	}

	private void geraProtesto( final String cnab, final boolean troca) {
		vLabs8 = new Vector<String>();
		vVals8 = new Vector<Integer>();
		if ("240".equals( cnab )) { 
			vLabs8.addElement( "Dias corridos" );
			vLabs8.addElement( "Dias utéis" );
			vLabs8.addElement( "Não protestar" );
			vVals8.addElement( 1 );
			vVals8.addElement( 2 );
			vVals8.addElement( 3 );
		} else {
			if ("341".equals(txtCodBancoCnab.getVlrString())) { // Itaú
				// Inserir instruções Itaú
				vLabs8.addElement( "00 - Sem instruções" );
				vLabs8.addElement( "02 - Devolver após 05 dias do vencimento" );
				vLabs8.addElement( "03 - Devolver após 30 dias do vencimento" );
				vLabs8.addElement( "05 - Receber conforme instruções no próprio título" );
				vLabs8.addElement( "07 - Devolver após 15 dias do vencimento" );
				vLabs8.addElement( "09 - Protestar " );
				vLabs8.addElement( "10 - Não protestar" );
				vLabs8.addElement( "20 - Não receber após 10 dias do vencimento" );
				vLabs8.addElement( "21 - Não receber após 15 dias do vencimento" );
				vLabs8.addElement( "24 - Não receber após 30 dias do vencimento" );
				vLabs8.addElement( "30 - Importância de desconto por dia" );
				vLabs8.addElement( "43 - Sujeito a protesto se não for pago no vencimento" );
				vLabs8.addElement( "54 - Após o vencimento pagável somente na empresa" );
				vLabs8.addElement( "59 - Cobrança negociada.Pagável somente por este boleto na rede bancária" );
				vVals8.addElement( 0 );
				vVals8.addElement( 2 );
				vVals8.addElement( 3 );
				vVals8.addElement( 5 );
				vVals8.addElement( 7 );
				vVals8.addElement( 9 );
				vVals8.addElement( 10 );
				vVals8.addElement( 20 );
				vVals8.addElement( 21 );
				vVals8.addElement( 24 );
				vVals8.addElement( 30 );
				vVals8.addElement( 43 );
				vVals8.addElement( 54 );
				vVals8.addElement( 59 );				
			} else if ("237".equals(txtCodBancoCnab.getVlrString())) { // Bradesco
				// Inserir instruções Bradesco 400 bytes
				vLabs8.addElement( "00 - Sem instruções" );
				vLabs8.addElement( "06 - Protestar" );
				vLabs8.addElement( "05 - Protesto Falimentar" );
				vLabs8.addElement( "18 - Decurso de Prazo" );
				vLabs8.addElement( "08 - Não cobrar juros de mora" );
				vLabs8.addElement( "09 - Não receber após o vencimento " );
				vLabs8.addElement( "10 - Multa de 10% após o 4 dias do vencimento" );
				vLabs8.addElement( "11 - Não receber após 8 dias do vencimento" );
				vLabs8.addElement( "12 - Cobrar encargos após 5 dias do vencimento" );
				vLabs8.addElement( "13 - Cobrar encargos após 10 dias do vencimento" );
				vLabs8.addElement( "14 - Cobrar encargos após 15 dias do vencimento" );
				vLabs8.addElement( "15 - Conceder desconto mesmo se pago após o vencimento" );
				vVals8.addElement( 0 );
				vVals8.addElement( 6 );
				vVals8.addElement( 5 );
				vVals8.addElement( 18 );
				vVals8.addElement( 8 );
				vVals8.addElement( 9 );
				vVals8.addElement( 10 );
				vVals8.addElement( 11 );
				vVals8.addElement( 12 );
				vVals8.addElement( 13 );
				vVals8.addElement( 14 );
				vVals8.addElement( 15 );
								
			} else if ("748".equals(txtCodBancoCnab.getVlrString())){
				
				vLabs8.addElement( "00 - Não protestar" );
				vLabs8.addElement( "06 - Protestar automaticamente" );
				vVals8.addElement( 0 );
				vVals8.addElement( 6 );
							
			} else {
				vLabs8.addElement( "00 - Sem instruções" );
				vLabs8.addElement( "01 - Cobrar juros (Disp. se inf. vlr. a ser cobr. p/ dia atraso)" );
				vLabs8.addElement( "03 - Protestar no 3o dia útil após vencido" );
				vLabs8.addElement( "04 - Protestar no 4o dia útil após vencido" );
				vLabs8.addElement( "05 - Protestar no 5o dia útil após vencido" );
				vLabs8.addElement( "10 - Protestar no 10o dia corrido após vencido" );
				vLabs8.addElement( "15 - Protestar no 15o dia corrido após vencido" );
				vLabs8.addElement( "20 - Protestar no 20o dia corrido após vencido" );
				vLabs8.addElement( "25 - Protestar no 25o dia corrido após vencido" );
				vLabs8.addElement( "30 - Protestar no 30o dia corrido após vencido" );
				vLabs8.addElement( "45 - Protestar no 45o dia corrido após vencido" );
				vLabs8.addElement( "06 - Indica Protesto em dias corridos, com prazo de 6 a 29, 35 ou 40 dias corridos" );
				vLabs8.addElement( "07 - Não protestar" );
				vLabs8.addElement( "22 - Conceder desconto só até a data estipulada" );
				vVals8.addElement( 0 );
				vVals8.addElement( 1 );
				vVals8.addElement( 3 );
				vVals8.addElement( 4 );
				vVals8.addElement( 5 );
				vVals8.addElement( 10 );
				vVals8.addElement( 15 );
				vVals8.addElement( 20 );
				vVals8.addElement( 25 );
				vVals8.addElement( 30 );
				vVals8.addElement( 45 );
				vVals8.addElement( 06 );
				vVals8.addElement( 07 );
				vVals8.addElement( 22 );
			}
		   /* - 00 - Sem de instruções
			- 01 - Cobrar juros (Dispensável se informado o valor a ser cobrado por dia de
			atraso).
			- 03 - Protestar no 3o dia útil após vencido
			- 04 - Protestar no 4o dia útil após vencido
			- 05 - Protestar no 5o dia útil após vencido
			- 10 - Protestar no 10o dia corrido após vencido
			- 15 - Protestar no 15o dia corrido após vencido
			- 20 - Protestar no 20o dia corrido após vencido
			- 25 - Protestar no 25o dia corrido após vencido
			- 30 - Protestar no 30o dia corrido após vencido
			- 45 - Protestar no 45o dia corrido após vencido
			- 06 - Indica Protesto em dias corridos, com prazo de 6 a 29, 35 ou 40 dias
			Corridos.
			- Obrigatório impostar, nas posições 392 a 393 o prazo de protesto
			desejado: 6 a 29, 35 ou 40 dias.
			- 07 - Não protestar
			- 22 - Conceder desconto só até a data estipulada */	
		}
		if (troca) {
			cbProtesto.setItensGeneric( vLabs8, vVals8 );
		}

	}
	private void geraEspecieTitulo( final String cnab, final boolean troca, final String codbanco ) {
		vLabs5 = new Vector<String>();
		vVals5 = new Vector<Integer>();
		if ( "240".equals( cnab ) ) {
			vLabs5.addElement( "CH- Cheque" );
			vLabs5.addElement( "DM- Duplicata mercantil" );
			vLabs5.addElement( "DMI- Duplic. mercantil p/indic." );
			vLabs5.addElement( "DS- Duplicata de serviço" );
			vLabs5.addElement( "DSI- Duplic. de serviço p/indic." );
			vLabs5.addElement( "DR- Duplicata rural" );
			vLabs5.addElement( "LC- Letra de cambio" );
			vLabs5.addElement( "NCC- Nota de crédito comercial" );
			vLabs5.addElement( "NCE- Nota de crédito a exportação" );
			vLabs5.addElement( "NCI- Nota de crédito indústria" );
			vLabs5.addElement( "NCR- Nota de crédito rural" );
			vLabs5.addElement( "NP- Nota promissória" );
			vLabs5.addElement( "NPR- Nota promissória rural" );
			vLabs5.addElement( "TM- Triplicata mercantíl" );
			vLabs5.addElement( "TS- Triplicata de serviço" );
			vLabs5.addElement( "NS- Nota de seguro" );
			vLabs5.addElement( "RC- Recibo" );
			vLabs5.addElement( "FAT- Fatura" );
			vLabs5.addElement( "ND- Nota de débito" );
			vLabs5.addElement( "AP- Apolice de seguro" );
			vLabs5.addElement( "ME- Mensalidade escolar" );
			vLabs5.addElement( "PC- Parcela de consórcio" );
			vLabs5.addElement( "Outros" );
			vVals5.addElement( 1 );
			vVals5.addElement( 2 );
			vVals5.addElement( 3 );
			vVals5.addElement( 4 );
			vVals5.addElement( 5 );
			vVals5.addElement( 6 );
			vVals5.addElement( 7 );
			vVals5.addElement( 8 );
			vVals5.addElement( 9 );
			vVals5.addElement( 10 );
			vVals5.addElement( 11 );
			vVals5.addElement( 12 );
			vVals5.addElement( 13 );
			vVals5.addElement( 14 );
			vVals5.addElement( 15 );
			vVals5.addElement( 16 );
			vVals5.addElement( 17 );
			vVals5.addElement( 18 );
			vVals5.addElement( 19 );
			vVals5.addElement( 20 );
			vVals5.addElement( 21 );
			vVals5.addElement( 22 );
			vVals5.addElement( 99 );
		} else {
			
			if( "748".equals( codbanco )){
				vLabs5.addElement( "DMI- Duplicata mercantil por Ind." );
				vLabs5.addElement( "DR - Duplicata Rural" );
				vLabs5.addElement( "NP - Nota Promissória" );
				vLabs5.addElement( "NR - Nota Promissória Rural" );
				vLabs5.addElement( "NS - Nota de Seguro" );
				vLabs5.addElement( "RC - Recibo" );
				vLabs5.addElement( "LC - Letra de Câmbio" );
				vLabs5.addElement( "ND - Nota de Débito" );
				vLabs5.addElement( "DSI - Duplicata de Serviço por Ind." );
				vLabs5.addElement( "OS - Outros" );
				vVals5.addElement( 65 );
				vVals5.addElement( 66 );
				vVals5.addElement( 67 );
				vVals5.addElement( 68 );
				vVals5.addElement( 69 );
				vVals5.addElement( 70 );
				vVals5.addElement( 71 );
				vVals5.addElement( 72 );
				vVals5.addElement( 73 );
				vVals5.addElement( 74 );
				
			} else {
	/*			00 - informado nos registros com comando 97-Despesas de Sustação de Protesto
				nas posições 109/110 desde que o titulo não conste mais da existência
				01 -duplicata mercantil
				02 - nota promissória
				03 - nota de seguro
				05 - recibo
				08 - letra de cambio
				09 - warrant
				10 - cheque
				12 - duplicata de serviço
				13 - nota de debito
				15 - apólice de seguro
				25 - divida ativa da União
				26 - divida ativa de Estado
				27 - divida ativa de Município */
	
				vLabs5.addElement( "DM- Duplicata mercantil" );
				vLabs5.addElement( "NP- Nota promissória" );
				vLabs5.addElement( "NS- Nota de seguro" );
				vLabs5.addElement( "RC- Recibo" );
				vLabs5.addElement( "LC- Letra de cambio" );
				vLabs5.addElement( "WT- Warrant" );
				vLabs5.addElement( "CQ- Cheque" );
				vLabs5.addElement( "DS- Duplicata de serviço" );
				vLabs5.addElement( "ND- Nota de débito" );
				vLabs5.addElement( "AP- Apolice de seguro" );
				vLabs5.addElement( "DAU- Divida ativa da União" );
				vLabs5.addElement( "DAE- Divida ativa do Estado" );
				vLabs5.addElement( "DAM- Divida ativa do Município" );
				vVals5.addElement( 1 );
				vVals5.addElement( 2 );
				vVals5.addElement( 3 );
				vVals5.addElement( 5 );
				vVals5.addElement( 8 );
				vVals5.addElement( 9 );
				vVals5.addElement( 10 );
				vVals5.addElement( 12 );
				vVals5.addElement( 13 );
				vVals5.addElement( 15 );
				vVals5.addElement( 25 );
				vVals5.addElement( 26 );
				vVals5.addElement( 27 );
				}
		}

		if ( troca ) {
			cbEspecieTitulo.setItensGeneric( vLabs5, vVals5);
		}
		
	}
	
	private void geraInstrucoes() {
		
		vLabsInstr = new Vector<String>();
		vValsInstr = new Vector<Integer>();
		vLabsInstr.addElement( "Cadastro de título" );
		vLabsInstr.addElement( "Pedido de baixa" );
		vLabsInstr.addElement( "Concessão de abatimento" );
		vLabsInstr.addElement( "Cancelamento de abatimento concedido" );
		vLabsInstr.addElement( "Alteração de vencimento" );
		vLabsInstr.addElement( "Pedido de protesto" );
		vLabsInstr.addElement( "Sustar protesto e baixar titúlo" );
		vLabsInstr.addElement( "Sustar protesto e manter em carteira" );
		vLabsInstr.addElement( "Alteração de outros dados" );
		vValsInstr.addElement( 1 );
		vValsInstr.addElement( 2 );
		vValsInstr.addElement( 4 );
		vValsInstr.addElement( 5 );
		vValsInstr.addElement( 6 );
		vValsInstr.addElement( 9 );
		vValsInstr.addElement( 18 );
		vValsInstr.addElement( 19 );
		vValsInstr.addElement( 31 );

	}
	
	
	private void geraOutrasInstrucoes(  ) {
		
		
		vLabsOutrasInstr = new Vector<String>();
		vValsOutrasInstr = new Vector<Integer>();
		vLabsOutrasInstr.addElement( "Desconto" );
		vLabsOutrasInstr.addElement( "Juros por dia");
		vLabsOutrasInstr.addElement( "Desconto por dia de antecipação");
		vLabsOutrasInstr.addElement( "Data limite para concessão de desconto");
		vLabsOutrasInstr.addElement( "Cancelamento de protesto automático");
		vLabsOutrasInstr.addElement( "Carteira de cobrança - não disponível");
		vValsOutrasInstr.addElement( 65 );
		vValsOutrasInstr.addElement( 66 );
		vValsOutrasInstr.addElement( 67);
		vValsOutrasInstr.addElement( 68 );
		vValsOutrasInstr.addElement( 69 );
		vValsOutrasInstr.addElement( 70 );
		
	}
	
	private void montaListaCampos() {

		/**********************
		 * FNBANCO SIACC *
		 **********************/
		lcBancoSiacc.add( new GuardaCampo( txtCodBancoSiacc, "CodBanco", "Cód.banco", ListaCampos.DB_PK, true ) );
		lcBancoSiacc.add( new GuardaCampo( txtNomeBancoSiacc, "NomeBanco", "Nome do Banco", ListaCampos.DB_SI, false ) );
		//lcBancoSiacc.setDinWhereAdic( " CODBANCO=#N ", txtCodBancoSiacc );
		lcBancoSiacc.montaSql( false, "BANCO", "FN" );
		lcBancoSiacc.setQueryCommit( false );
		lcBancoSiacc.setReadOnly( true );
		txtCodBancoSiacc.setTabelaExterna( lcBancoSiacc, FBanco.class.getCanonicalName() );

		lcSiacc.setMaster( lcCampos );
		lcSiacc.setTabela( tabSiacc );

		lcCampos.adicDetalhe( lcSiacc );

		/**********************
		 * FNBANCO CNAB *
		 **********************/
		lcBancoCnab.add( new GuardaCampo( txtCodBancoCnab, "CodBanco", "Cód.banco", ListaCampos.DB_PK, true ) );
		lcBancoCnab.add( new GuardaCampo( txtNomeBancoCnab, "NomeBanco", "Nome do Banco", ListaCampos.DB_SI, false ) );
		//lcBancoCnab.setDinWhereAdic( " CODBANCO=#N ", txtCodBancoCnab );
		lcBancoCnab.montaSql( false, "BANCO", "FN" );
		lcBancoCnab.setQueryCommit( false );
		lcBancoCnab.setReadOnly( true );
		txtCodBancoCnab.setTabelaExterna( lcBancoCnab, FBanco.class.getCanonicalName() );

		/**********************
		 * FNCONTA CONTA CNAB *
		 **********************/
		lcContaCnab.add( new GuardaCampo( txtNumContaCnab, "NumConta", "N° Conta", ListaCampos.DB_PK, false ) );
		lcContaCnab.add( new GuardaCampo( txtAgenciaCnab, "AgenciaConta", "Agência", ListaCampos.DB_SI, false ) );
		lcContaCnab.add( new GuardaCampo( txtDescContaCnab, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcContaCnab.montaSql( false, "CONTA", "FN" );
		lcContaCnab.setQueryCommit( false );
		lcContaCnab.setReadOnly( true );
		txtNumContaCnab.setTabelaExterna( lcContaCnab, FConta.class.getCanonicalName() );

		/***********************
		 * FNCONTA CONTA SIACC *
		 ***********************/
		lcContaSiacc.add( new GuardaCampo( txtNumContaSiacc, "NumConta", "N° Conta", ListaCampos.DB_PK, false ) );
		lcContaSiacc.add( new GuardaCampo( txtAgenciaSiacc, "AgenciaConta", "Agência", ListaCampos.DB_SI, false ) );
		lcContaSiacc.add( new GuardaCampo( txtDescContaSiacc, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcContaSiacc.montaSql( false, "CONTA", "FN" );
		lcContaSiacc.setQueryCommit( false );
		lcContaSiacc.setReadOnly( true );
		txtNumContaSiacc.setTabelaExterna( lcContaSiacc, FConta.class.getCanonicalName() );

		lcCnab.setMaster( lcCampos );
		lcCnab.setTabela( tabCnab );

		lcCampos.adicDetalhe( lcCnab );
	}

	private void montaTela() {

		/*****************
		 * GERAL *
		 *****************/

		lcCampos.setMensInserir( false );

		setPainel( panelGeral );
		adicTab( "Geral", panelGeral );
		adicCampo( txtNomeEmp, 7, 30, 250, 20, "NomeEmp", "Nome da empresa (siacc)", ListaCampos.DB_SI, true );
		adicCampo( txtNomeEmpCnab, 7, 70, 250, 20, "NomeEmpCnab", "Nome da empresa (cnab)", ListaCampos.DB_SI, true );
		adicDB( cbSobrescreveHist, 7, 110, 300, 20, "SobrescreveHist", "", true );

		nav.setAtivo( 0, false );
		lcCampos.setPodeExc( false );

		setListaCampos( false, "PREFERE6", "SG" );

		/*****************
		 * SIACC *
		 *****************/
		setListaCampos( lcSiacc );
		setNavegador( nvSiacc );

		adicTab( "SIACC", panelSiacc );
		setPainel( panelCamposSiacc, panelSiacc );

		panelSiacc.add( panelTabSiacc, BorderLayout.NORTH );
		panelSiacc.add( tbSiacc, BorderLayout.CENTER );
		panelSiacc.add( panelNavSiacc, BorderLayout.SOUTH );

		panelTabSiacc.setPreferredSize( new Dimension( 300, 100 ) );
		panelTabSiacc.setBorder( BorderFactory.createEtchedBorder() );
		panelTabSiacc.add( new JScrollPane( tabSiacc ), BorderLayout.CENTER );
		
		tbSiacc.setTabPlacement( SwingConstants.BOTTOM );

		tbSiacc.add( "geral", panelSiaccGeral );
		panelSiaccGeral.add( panelCamposSiacc, BorderLayout.CENTER );
		setPainel( panelCamposSiacc);

		//lcSiacc.add( new GuardaCampo( txtTipoSiacc, "TipoFebraban", "Tipo", ListaCampos.DB_PK, true ) );
		adicCampo( txtCodBancoSiacc, 7, 30, 100, 20, "CodBanco", "Cód.banco", ListaCampos.DB_PF, txtNomeBancoSiacc, true );
		adicDescFK( txtNomeBancoSiacc, 110, 30, 260, 20, "NomeBanco", "Nome do banco" );
		adicCampoInvisivel(  txtTipoSiacc, "TipoFebraban",  "Tipo", ListaCampos.DB_PK, true );
		adicCampo( txtCodConvSiacc, 7, 70, 140, 20, "CodConv", "Convênio", ListaCampos.DB_SI, false );
		adicCampo( txtVersaoSiacc, 150, 70, 50, 20, "VerLayout", "Versão", ListaCampos.DB_SI, false );
		adicCampo( txtIdentServSiacc, 203, 70, 100, 20, "IdentServ", "Ident. Serviço", ListaCampos.DB_SI, false );
		adicCampo( txtNroSeqSiacc, 306, 70, 63, 20, "NroSeq", "Sequência", ListaCampos.DB_SI, false );
		adicCampo( txtNumContaSiacc, 7, 110, 80, 20, "NumConta", "Nº da conta", ListaCampos.DB_FK, true );
		adicDescFK( txtAgenciaSiacc, 90, 110, 57, 20, "AgenciaConta", "Agência" );
		adicDescFK( txtDescContaSiacc, 150, 110, 220, 20, "DescConta", "Descrição da conta" );
		adicCampo( txtContaComprSiacc, 7, 150, 140, 20, "ContaCompr", "Conta Compromisso", ListaCampos.DB_SI, false );
		adicDB( rgIdentAmbCliSiacc, 7, 190, 178, 60, "IdentAmbCli", "Ambiente do cliente", false );
		adicDB( rgIdentAmbBcoSiacc, 193, 190, 178, 60, "IdentAmbBco", "Ambiente do banco", false );


		/*** SIACC - ABA PREF ***/
	
		tbSiacc.add( "preferências", panelSiaccPref );
		panelSiaccPref.add( new JScrollPane( panelCamposSiaccPref ), BorderLayout.CENTER );
		setPainel( panelCamposSiaccPref );

		adicDB( cbPadraoSIACC, 7, 20, 117, 20, "PADRAOSIACC", "Padrão SIACC", false );
				
		/*** SIACC - ABA CAMINHOS ***/
		
		tbSiacc.add( "Caminhos", panelCaminhosSiacc );

		setPainel( panelCamposSiaccCaminhos, panelCaminhosSiacc );
		
		adicCampo( txtCaminhoRemessaSiacc, 7, 20, 300, 20, "CaminhoRemessa", "Pasta padrão para arquivo de remessa", ListaCampos.DB_SI, false );
		adicCampo( txtCaminhoRetornoSiacc, 7, 60, 300, 20, "CaminhoRetorno", "Pasta padrão para arquivo de retorno", ListaCampos.DB_SI, false );
		adicCampo( txtBackupRemessaSiacc, 7, 100, 300, 20, "BackupRemessa", "Pasta padrão para backup de arquivo de remessa", ListaCampos.DB_SI, false );
		adicCampo( txtBackupRetornoSiacc, 7, 140, 300, 20, "BackupRetorno", "Pasta padrão para backup de arquivo de retorno", ListaCampos.DB_SI, false );
		
		adic(btGetCaminhoRemessaSiacc, 310, 20, 20, 20);
		adic(btGetCaminhoRetornoSiacc, 310, 60, 20, 20);
		adic(btGetBackupRemessaSiacc, 310, 100, 20, 20);
		adic(btGetBackupRetornoSiacc, 310, 140, 20, 20);
		
		/****************/
		
		setListaCampos( false, "ITPREFERE6", "SG" );
		lcSiacc.setWhereAdic( " TIPOFEBRABAN='01' " );

		panelNavSiacc.setPreferredSize( new Dimension( 300, 30 ) );
		panelNavSiacc.setBorder( BorderFactory.createEtchedBorder() );
		panelNavSiacc.add( nvSiacc, BorderLayout.WEST );

		/****************
		 * CNAB *
		 ****************/
		setListaCampos( lcCnab );
		setNavegador( nvCnab );

		adicTab( "CNAB", panelCnab );

		panelCnab.add( panelCnabManager, BorderLayout.CENTER );

		panelTabCnab.setPreferredSize( new Dimension( 300, 80 ) );
		panelTabCnab.setBorder( BorderFactory.createEtchedBorder() );
		panelTabCnab.add( new JScrollPane( tabCnab ), BorderLayout.CENTER );

		panelCnabManager.add( panelTabCnab, BorderLayout.NORTH );
		panelCnabManager.add( tbCnab, BorderLayout.CENTER );
		panelCnabManager.add( panelNavCnab, BorderLayout.SOUTH );

		tbCnab.setTabPlacement( SwingConstants.BOTTOM );

		/*** ABA GERAL ***/

		tbCnab.add( "geral", panelCnabGeral );
		panelCnabGeral.add( panelCamposCnab, BorderLayout.CENTER );
		setPainel( panelCamposCnab );

		adicCampo( txtCodBancoCnab, 7, 30, 100, 20, "CodBanco", "Cód.banco", ListaCampos.DB_PF, txtNomeBancoCnab, true );
		adicDescFK( txtNomeBancoCnab, 110, 30, 260, 20, "NomeBanco", "Nome do banco" );
		adicCampoInvisivel( txtTipoCnab, "TipoFebraban", "Tipo", ListaCampos.DB_PK, true );
//		lcCnab.add( new GuardaCampo( , , "Tipo", ListaCampos.DB_PK, true ) );
		adicCampo( txtCodConvCnab, 7, 70, 140, 20, "CodConv", "Convênio", ListaCampos.DB_SI, false );
		adicCampo( txtVersaoCnab, 150, 70, 50, 20, "VerLayout", "Versão", ListaCampos.DB_SI, false );
		adicCampo( txtIdentServCnab, 203, 70, 100, 20, "IdentServ", "Ident. Serviço", ListaCampos.DB_SI, false );
		adicCampo( txtNroSeqCnab, 306, 70, 63, 20, "NroSeq", "Sequência", ListaCampos.DB_SI, false );
		adicCampo( txtNumContaCnab, 7, 110, 80, 20, "NumConta", "Nº da conta", ListaCampos.DB_FK, true );
		adicDescFK( txtAgenciaCnab, 90, 110, 57, 20, "AgenciaConta", "Agência" );
		adicDescFK( txtDescContaCnab, 150, 110, 220, 20, "DescConta", "Descrição da conta" );
		adicCampo( txtContaComprCnab, 7, 150, 140, 20, "ContaCompr", "Conta Compromisso", ListaCampos.DB_SI, false );
		adicCampo( txtModalidadeCnab, 150, 150, 100, 20, "MdeCob", "Modalidade", ListaCampos.DB_SI, false );
		adicCampo( txtConvBol, 253, 150, 117, 20, "ConvCob", "Convênio boleto", ListaCampos.DB_SI, false );

		adicDB( rgIdentAmbCliCnab, 7, 190, 178, 60, "IdentAmbCli", "Ambiente do cliente", false );
		adicDB( rgIdentAmbBcoCnab, 193, 190, 178, 60, "IdentAmbBco", "Ambiente do banco", false );

		panelNavCnab.setPreferredSize( new Dimension( 300, 30 ) );
		panelNavCnab.setBorder( BorderFactory.createEtchedBorder() );
		panelNavCnab.add( nvCnab, BorderLayout.WEST );

		/****************/

		/*** ABA PREF ***/

		tbCnab.add( "preferências", panelCnabPref );
		panelCnabPref.add( new JScrollPane( panelCamposCnabPref ), BorderLayout.CENTER );
		setPainel( panelCamposCnabPref );

		adicDB( cbFormaCadastramento, 10, 20, 220, 20, "FORCADTIT", "Cadastramento do titulo no banco", false );
		adicDB( cbPadraoCNAB, 233, 20, 117, 20, "PADRAOCNAB", "Padrão CNAB", false );

		adicDB( cbTipoDocumento, 10, 60, 137, 20, "TIPODOC", "Tipo de documento", false );
		adicDB( cbEmissaoBloqueto, 150, 60, 200, 20, "IDENTEMITBOL", "Emissão do bloqueto", false );

		adicDB( cbDistribuicao, 10, 100, 137, 20, "IDENTDISTBOL", "Distribuição", false );
		adicDB( cbEspecieTitulo, 150, 100, 200, 20, "ESPECTIT", "Espécie do titulo", false );

		adicDB( cbJurosMora, 10, 140, 180, 20, "CODJUROS", "Cobrança de juros", false );
		adicDB( txtVlrJuros, 193, 140, 80, 20, "VLRPERCJUROS", "% Juros", false );
		adicDB( txtVlrPercMulta, 276, 140, 80, 20, "VLRPERCMULTA", "% Multa", false );
		adicDB( cbDesconto, 10, 180, 250, 20, "CODDESC", "Indentificação para consessão de desconto", false );
		adicDB( txtVlrDesconto, 270, 180, 80, 20, "VLRPERCDESC", "% Desc.", false );
		adicDB( cbProtesto, 10, 220, 250, 20, "CODPROT", "Instrução de protesto", false );
		adicDB( txtNumDiasProtesto, 270, 220, 80, 20, "DIASPROT", "Dias", false );
		adicDB( cbDevolucao, 10, 260, 250, 20, "CODBAIXADEV", "Código para devolução", false );
		adicDB( txtNumDiasDevolucao, 270, 260, 80, 20, "DIASBAIXADEV", "Dias", false );
		adicDB( cbAceite, 10, 300, 340, 20, "ACEITE", "Aceite", false );
		adicDB( cbInstrucoes, 10, 340, 160, 20, "CODINSTR", "Instruções", false );
		adicDB( cbOutrasInstrucoes, 173, 340, 160, 20, "CODOUTINSTR", "Alteração de outros dados", false );

		tbCnab.add( "Caminhos", panelCaminhos );

		setPainel( panelCamposCnabCaminhos, panelCaminhos );
		
		adicCampo( txtCaminhoRemessa	, 7		, 20	, 300	, 20	, "CaminhoRemessa"	, "Pasta padrão para arquivo de remessa"			, ListaCampos.DB_SI, false );
		adicCampo( txtCaminhoRetorno	, 7		, 60	, 300	, 20	, "CaminhoRetorno"	, "Pasta padrão para arquivo de retorno"			, ListaCampos.DB_SI, false );
		adicCampo( txtBackupRemessa		, 7		, 100	, 300	, 20	, "BackupRemessa"	, "Pasta padrão para backup de arquivo de remessa"	, ListaCampos.DB_SI, false );
		adicCampo( txtBackupRetorno		, 7		, 140	, 300	, 20	, "BackupRetorno"	, "Pasta padrão para backup de arquivo de retorno"	, ListaCampos.DB_SI, false );
		
		adic(btGetCaminhoRemessa		, 310	, 20	, 20	, 20);
		adic(btGetCaminhoRetorno		, 310	, 60	, 20	, 20);
		adic(btGetBackupRemessa			, 310	, 100	, 20	, 20);
		adic(btGetBackupRetorno			, 310	, 140	, 20	, 20);
		
		
		/****************/

		setListaCampos( false, "ITPREFERE6", "SG" );
		lcCnab.setWhereAdic( " TIPOFEBRABAN='02' " );
	}

	public void afterCarrega( CarregaEvent e ) {

	}

	public void beforeCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {

			txtTipoCnab.setVlrString( TP_CNAB );
			txtTipoSiacc.setVlrString( TP_SIACC );
		}
	}

	public void beforePost( PostEvent e ) {

		if ( e.getListaCampos() == lcSiacc ) {

			txtTipoSiacc.setVlrString( TP_SIACC );
		}
		else if ( e.getListaCampos() == lcCnab ) {

			txtTipoCnab.setVlrString( TP_CNAB );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcSiacc.setConexao( cn );
		lcCnab.setConexao( cn );
		lcBancoSiacc.setConexao( cn );
		lcBancoCnab.setConexao( cn );
		lcContaCnab.setConexao( cn );
		lcContaSiacc.setConexao( cn );

		lcCampos.carregaDados();
	}

	public void valorAlterado( JComboBoxEvent evt ) {
		if ( evt.getComboBoxPad()==cbPadraoCNAB ) {
			geraEspecieTitulo( evt.getComboBoxPad().getVlrString(), true, txtCodBancoCnab.getVlrString() );
			geraProtesto( evt.getComboBoxPad().getVlrString(), true );
		}
		/*
		if ( evt.getComboBoxPad()==cbInstrucoes ) {
			geraOutrasInstrucoes(evt.getComboBoxPad().getVlrInteger());
		}
		*/
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btGetCaminhoRemessa) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					getPasta( txtCaminhoRemessa );
				}
			});
			th.start();
		}

		if (e.getSource() == btGetCaminhoRetorno) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					getPasta( txtCaminhoRetorno );
				}
			});
			th.start();
		}

		if (e.getSource() == btGetBackupRemessa) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					getPasta( txtBackupRemessa );
				}
			});
			th.start();
		}

		if (e.getSource() == btGetBackupRetorno) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					getPasta( txtBackupRetorno );
				}
			});
			th.start();
		}
		
		super.actionPerformed(e);

	}

	
	private void getPasta(JTextFieldPad campo) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			campo.setVlrString(fileChooser.getSelectedFile().getPath());			
		}
	}

	public void beforeInsert( InsertEvent ievt ) {

		
	}

	public void afterInsert( InsertEvent ievt ) {
		if (ievt.getListaCampos()==lcCampos) {
			cbSobrescreveHist.setVlrString( "S" );
		}
	}
	
}
