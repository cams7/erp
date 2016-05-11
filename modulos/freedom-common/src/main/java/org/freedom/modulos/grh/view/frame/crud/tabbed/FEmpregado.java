/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FEmpregado.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Tela de cadastro de empregados.
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.grh.view.frame.crud.plain.FBeneficio;
import org.freedom.modulos.grh.view.frame.crud.plain.FDepto;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;
import org.freedom.modulos.grh.view.frame.crud.plain.FTurnos;

public class FEmpregado extends FTabDados implements KeyListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCod = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );

	private final JTextFieldPad txtCodFuncao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtCodTurno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtCodDepto = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldFK txtDescFuncao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtDescTurno = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtDescDepto = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtDesc = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtApelido = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtEndEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtDataNasc = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldFK txtIdade = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtNumEmpr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );

	private final JTextFieldPad txtCidEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtFoneEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private final JTextFieldPad txtDtAdmissao = new JTextFieldPad( JTextFieldPad.TP_DATE, 12, 0 );

	private final JTextFieldPad txtCtps = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private final JTextFieldPad txtUfCtps = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCertifExercito = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtPisPasep = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtRg = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtOrgEmiss = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtUfExpedRg = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtDtExpRg = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtCpfEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private final JTextFieldPad txtTituloEleit = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldPad txtZonaEleit = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtSecaoEleit = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtCnh = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtNomeMae = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextFieldPad txtNomePai = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextFieldPad txtComplemento = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtUfEmpregado = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtDtDemissao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtEmail = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtDddEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtFoneEmpr2 = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private final JTextFieldPad txtCelEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private final JTextFieldPad txtVlrSalario = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtCustoHoraTrab = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtDtVigor = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	// private final JTextFieldPad txtObsSal = new JTextFieldPad( JTextFieldPad.TP_STRING, 1000, 0 );

	private JTextAreaPad txaObsSal = new JTextAreaPad();

	private JScrollPane spnTxa = new JScrollPane( txaObsSal );

	private final JTextFieldPad txtSeqSal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtCodBenef = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescBenef = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtCbo = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldFK txtVlrBenef = new JTextFieldFK( JTextFieldPad.TP_STRING, 15, 2 );

	private final JPanelPad panelEmpregados = new JPanelPad();

	private final JPanelPad panelSalario = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelBeneficios = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private PainelImagem fotoEmpr = new PainelImagem( 65000 );

	private final ListaCampos lcFuncao = new ListaCampos( this, "FO" );

	private final ListaCampos lcTurno = new ListaCampos( this, "TO" );

	private final ListaCampos lcDepto = new ListaCampos( this, "DP" );

	private ListaCampos lcBenef = new ListaCampos( this, "BN" );

	private ListaCampos lcEmpSal = new ListaCampos( this );

	private ListaCampos lcEmpBenef = new ListaCampos( this );

	private JTablePad tabSal = new JTablePad();

	private JTablePad tabBenef = new JTablePad();

	private JScrollPane spnTabSal = new JScrollPane( tabSal );

	private Navegador navSal = new Navegador( true );

	private JPanelPad pinSal = new JPanelPad( 0, 100 );

	private JScrollPane spnTabBenef = new JScrollPane( tabBenef );

	private Navegador navBenef = new Navegador( true );

	private JPanelPad pinBen = new JPanelPad( 0, 80 );

	private JComboBoxPad cbStatus = null;

	private JComboBoxPad cbSexo = null;

	public static Constant STATUS_ADMITIDO = new Constant( "Admitido" , "AD"); 

	public static Constant STATUS_DEMITIDO = new Constant( "Demitido", "DE" );
	
	public static Constant STATUS_FERIAS = new Constant( "Férias", "EF" );
	
	public static Constant STATUS_MATERNIDADE = new Constant( "Licença maternidade", "LM" );
	
	public static Constant STATUS_AFASTADO = new Constant( "Afastamento INSS", "AI" );
	
	public static Constant STATUS_APOSENTADO = new Constant( "Aposentado", "AP" );

	public FEmpregado() {

		super();
		setTitulo( "Cadastro de Empregados" );
		setAtribos( 50, 50, 510, 630 );

		lcEmpSal.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcEmpSal );
		lcEmpSal.setTabela( tabSal );

		lcEmpBenef.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcEmpBenef );
		lcEmpBenef.setTabela( tabBenef );

		montaListaCampos();

		montaTela();

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		txtDataNasc.addKeyListener( this );
		lcCampos.addCarregaListener( this );

		setImprimir( true );

	}

	private int calcIdade() {

		int ano = 0;
		int idade = 0;

		ano = Funcoes.getAno( txtDataNasc.getVlrDate() );
		idade = ( Funcoes.getAno( new Date() ) - ano );

		return idade;

	}

	private void montaListaCampos() {

		lcFuncao.add( new GuardaCampo( txtCodFuncao, "CodFunc", "Cód.Func.", ListaCampos.DB_PK, true ) );
		lcFuncao.add( new GuardaCampo( txtDescFuncao, "DescFunc", "Descrição da função", ListaCampos.DB_SI, false ) );
		lcFuncao.add( new GuardaCampo( txtCbo, "CboFunc", "CBO", ListaCampos.DB_SI, false ) );
		lcFuncao.montaSql( false, "FUNCAO", "RH" );
		lcFuncao.setQueryCommit( false );
		lcFuncao.setReadOnly( true );
		txtCodFuncao.setTabelaExterna( lcFuncao, FFuncao.class.getCanonicalName() );

		lcTurno.add( new GuardaCampo( txtCodTurno, "CodTurno", "Cód. turno", ListaCampos.DB_PK, true ) );
		lcTurno.add( new GuardaCampo( txtDescTurno, "DescTurno", "Descrição do turno", ListaCampos.DB_SI, false ) );
		lcTurno.montaSql( false, "TURNO", "RH" );
		lcTurno.setQueryCommit( false );
		lcTurno.setReadOnly( true );
		txtCodTurno.setTabelaExterna( lcTurno, FTurnos.class.getCanonicalName() );

		lcDepto.add( new GuardaCampo( txtCodDepto, "CodDep", "Cód.Depto.", ListaCampos.DB_PK, true ) );
		lcDepto.add( new GuardaCampo( txtDescDepto, "DescDep", "Descrição do departamento", ListaCampos.DB_SI, false ) );
		lcDepto.montaSql( false, "DEPTO", "RH" );
		lcDepto.setQueryCommit( false );
		lcDepto.setReadOnly( true );
		txtCodDepto.setTabelaExterna( lcDepto, FDepto.class.getCanonicalName() );

		lcBenef.add( new GuardaCampo( txtCodBenef, "CodBenef", "Cód.benef", ListaCampos.DB_PK, txtDescBenef, false ) );
		lcBenef.add( new GuardaCampo( txtDescBenef, "DescBenef", "Descrição do benefício", ListaCampos.DB_SI, false ) );
		lcBenef.add( new GuardaCampo( txtVlrBenef, "ValorBenef", "Valor do benefício", ListaCampos.DB_SI, false ) );
		lcBenef.montaSql( false, "BENEFICIO", "RH" );
		lcBenef.setReadOnly( true );
		lcBenef.setQueryCommit( false );
		txtCodBenef.setTabelaExterna( lcBenef, FBeneficio.class.getCanonicalName() );
		txtCodBenef.setFK( true );
		txtCodBenef.setListaCampos( lcBenef );
		txtDescBenef.setListaCampos( lcBenef );
		txtVlrBenef.setListaCampos( lcBenef );

	}

	private void montaTela() {

		Vector<String> vVals = new Vector<String>();
		vVals.addElement( "AD" );
		vVals.addElement( "DE" );
		vVals.addElement( "EF" );
		vVals.addElement( "LM" );
		vVals.addElement( "AI" );
		vVals.addElement( "AP" );
		Vector<String> vLabs = new Vector<String>();
		vLabs.addElement( "Admitido" );
		vLabs.addElement( "Demitido" );
		vLabs.addElement( "Em férias" );
		vLabs.addElement( "Licença maternidade" );
		vLabs.addElement( "Afastamento INSS" );
		vLabs.addElement( "Aposentado" );
		Vector<String> vLabs2 = new Vector<String>();
		vLabs2.addElement( "Masculino" );
		vLabs2.addElement( "Feminino" );
		Vector<String> vVals2 = new Vector<String>();
		vVals2.addElement( "M" );
		vVals2.addElement( "F" );

		cbStatus = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_STRING, 2, 0 );
		cbSexo = new JComboBoxPad( vLabs2, vVals2, JComboBoxPad.TP_STRING, 2, 0 );

		/***********
		 * Geral *
		 ***********/

		adicTab( "Geral", panelEmpregados );
		setPainel( panelEmpregados );

		adicCampo( txtCod, 7, 20, 42, 20, "MatEmpr", "Mat.", ListaCampos.DB_PK, true );
		adicCampo( txtDesc, 52, 20, 242, 20, "NomeEmpr", "Nome do empregado", ListaCampos.DB_SI, true );
		adicCampo( txtDtAdmissao, 297, 20, 71, 20, "DtAdmissao", "Data admis.", ListaCampos.DB_SI, true );
		adicDB( fotoEmpr, 380, 20, 100, 133, "FotoEmpr", "Foto ( 3 x 4 )", false );
		adicCampo( txtApelido, 7, 60, 110, 20, "ApelidoEmpr", "Apelido", ListaCampos.DB_SI, true );
		adicDB( cbSexo, 120, 60, 120, 20, "SexoEmpr", "Sexo", false );
		adicCampo( txtDataNasc, 243, 60, 85, 20, "DtNascEmpr", "Data de nasc.", ListaCampos.DB_SI, false );
		adic( new JLabelPad( "Idade" ), 331, 40, 45, 20 );
		adic( txtIdade, 331, 60, 40, 20 );
		adicCampo( txtCtps, 7, 100, 120, 20, "CtpsEmpr", "CTPS", ListaCampos.DB_SI, false );
		adicCampo( txtSerie, 130, 100, 80, 20, "SerieCtpsEmpr", "Série", ListaCampos.DB_SI, false );
		adicCampo( txtUfCtps, 213, 100, 30, 20, "UfCtpsEmpr", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtCertifExercito, 245, 100, 125, 20, "CertReservEmpr", "Cert. exército", ListaCampos.DB_SI, false );
		adicCampo( txtPisPasep, 7, 140, 110, 20, "PisPasepEmpr", "PIS/PASEP", ListaCampos.DB_SI, false );
		adicCampo( txtRg, 120, 140, 85, 20, "RgEmpr", "RG", ListaCampos.DB_SI, false );
		adicCampo( txtOrgEmiss, 208, 140, 62, 20, "OrgExpRhEmpr", "Org.Emis.", ListaCampos.DB_SI, false );
		adicCampo( txtUfExpedRg, 272, 140, 30, 20, "UfRgEmpr", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtDtExpRg, 305, 140, 65, 20, "DtExpRgEmpr", "Emissão", ListaCampos.DB_SI, false );
		adicCampo( txtCpfEmpr, 7, 180, 92, 20, "CpfEmpr", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtTituloEleit, 102, 180, 110, 20, "TitEleitEmpr", "Titulo Eleitoral", ListaCampos.DB_SI, false );
		adicCampo( txtZonaEleit, 215, 180, 60, 20, "ZonaEleitEmpr", "Zona", ListaCampos.DB_SI, false );
		adicCampo( txtSecaoEleit, 278, 180, 60, 20, "SecaoEleitEmpr", "Seção", ListaCampos.DB_SI, false );
		adicCampo( txtCnh, 341, 180, 140, 20, "CnhEmpr", "CNH", ListaCampos.DB_SI, false );
		adicCampo( txtNomeMae, 7, 220, 230, 20, "MaeEmpr", "Nome da mãe", ListaCampos.DB_SI, false );
		adicCampo( txtNomePai, 240, 220, 241, 20, "PaiEmpr", "Nome do pai", ListaCampos.DB_SI, false );
		adicCampo( txtEndEmpr, 7, 260, 280, 20, "EndEmpr", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumEmpr, 290, 260, 80, 20, "NumEmpr", "Número", ListaCampos.DB_SI, false );
		adicCampo( txtComplemento, 373, 260, 110, 20, "ComplEndEmpr", "Complemento", ListaCampos.DB_SI, false );
		adicCampo( txtBairEmpr, 7, 300, 190, 20, "BairEmpr", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidEmpr, 200, 300, 160, 20, "CidEmpr", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUfEmpregado, 363, 300, 30, 20, "UfEmpr", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtCepEmpr, 396, 300, 87, 20, "CepEmpr", "CEP", ListaCampos.DB_SI, false );
		adicCampo( txtDddEmpr, 7, 340, 35, 20, "DddEmpr", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneEmpr, 45, 340, 68, 20, "FoneEmpr", "Telefone 1", ListaCampos.DB_SI, false );
		adicCampo( txtFoneEmpr2, 116, 340, 68, 20, "Fone2Empr", "Telefone 2", ListaCampos.DB_SI, false );
		adicCampo( txtCelEmpr, 187, 340, 68, 20, "CelEmpr", "Celular", ListaCampos.DB_SI, false );
		adicCampo( txtEmail, 258, 340, 225, 20, "EmailEmpr", "E-mail", ListaCampos.DB_SI, false );
		adicCampo( txtCodTurno, 7, 380, 60, 20, "CodTurno", "Cód.Turn.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescTurno, 71, 380, 410, 20, "DescTurno", "Descrição do turno" );
		adicCampo( txtCodFuncao, 7, 420, 60, 20, "CodFunc", "Cód.Func.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescFuncao, 71, 420, 340, 20, "DescFunc", "Descrição da função" );
		adicDescFK( txtCbo, 415, 420, 68, 20, "CboFunc", "CBO" );
		adicCampo( txtCodDepto, 7, 460, 60, 20, "CodDep", "Cód.Dept.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescDepto, 71, 460, 412, 20, "DescDepto", "Descrição do departamento" );
		adicDB( cbStatus, 7, 500, 100, 20, "StatusEmpr", "Status", false );
		adicCampo( txtDtDemissao, 110, 500, 90, 20, "DtDemissaoEmpr", "Data Demissão", ListaCampos.DB_SI, false );

		txtCepEmpr.setMascara( JTextFieldPad.MC_CEP );
		txtFoneEmpr.setMascara( JTextFieldPad.MC_FONE );
		txtFoneEmpr2.setMascara( JTextFieldPad.MC_FONE );
		txtCelEmpr.setMascara( JTextFieldPad.MC_CELULAR );
		txtCpfEmpr.setMascara( JTextFieldPad.MC_CPF );
		txtRg.setMascara( JTextFieldPad.MC_RG );

		setListaCampos( true, "EMPREGADO", "RH" );
		lcCampos.setQueryInsert( false );

		/***********
		 * Salário *
		 ***********/

		setPainel( pinSal, panelSalario );
		adicTab( "Salário", panelSalario );
		setListaCampos( lcEmpSal );
		navSal.setAtivo( 4, true );
		setNavegador( navSal );

		panelSalario.add( pinSal, BorderLayout.SOUTH );
		panelSalario.add( spnTabSal, BorderLayout.CENTER );

		adicCampoInvisivel( txtSeqSal, "SeqSal", "Seq.", ListaCampos.DB_PK, false );
		adicCampo( txtVlrSalario, 7, 20, 90, 20, "ValorSal", "Salário", ListaCampos.DB_SI, false );
		adicCampo( txtCustoHoraTrab, 100, 20, 90, 20, "CustoHoraTrab", "Custo h.trab.", ListaCampos.DB_SI, false );
		adicCampo( txtDtVigor, 193, 20, 77, 20, "DtVigor", "Data.vigor", ListaCampos.DB_SI, true );

		// adicCampo( txtObsSal, 193, 20, 280, 20, "ObsSal", "Observação", ListaCampos.DB_SI, false );
		adicDB( txaObsSal, 276, 20, 210, 73, "ObsSal", "Observação", false );
		pinSal.adic( navSal, 0, 70, 270, 25 );
		setListaCampos( true, "EMPREGADOSAL", "RH" );
		lcEmpSal.setQueryInsert( false );
		lcEmpSal.setQueryCommit( false );
		lcEmpSal.montaTab();

		tabSal.setTamColuna( 30, 0 );
		tabSal.setTamColuna( 70, 1 );
		tabSal.setTamColuna( 70, 2 );
		tabSal.setTamColuna( 70, 3 );
		tabSal.setTamColuna( 245, 4 );

		/**************
		 * Benefícios *
		 **************/

		setPainel( pinBen, panelBeneficios );
		adicTab( "Benefícios", panelBeneficios );
		setListaCampos( lcEmpBenef );
		navSal.setAtivo( 4, true );
		setNavegador( navBenef );

		panelBeneficios.add( spnTabBenef, BorderLayout.CENTER );
		panelBeneficios.add( pinBen, BorderLayout.SOUTH );

		adicCampo( txtCodBenef, 7, 20, 60, 20, "CodBenef", "Cód.Benef", ListaCampos.DB_PF, txtDescBenef, true );
		adicDescFK( txtDescBenef, 70, 20, 250, 20, "DescBenef", "Descrição do benefício" );
		adicDescFK( txtVlrBenef, 325, 20, 80, 20, "ValorBenef", "Valor( R$ )" );
		pinBen.adic( navBenef, 0, 50, 270, 25 );
		setListaCampos( false, "EMPREGADOBENEF", "RH" );
		lcEmpBenef.setQueryInsert( false );
		lcEmpBenef.setQueryCommit( false );
		lcEmpBenef.montaTab();

		tabBenef.setTamColuna( 335, 1 );
		tabBenef.setTamColuna( 100, 2 );

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcFuncao.setConexao( cn );
		lcTurno.setConexao( cn );
		lcDepto.setConexao( cn );
		lcEmpSal.setConexao( cn );
		lcBenef.setConexao( cn );
		lcEmpBenef.setConexao( cn );
	}

	public void keyPressed( KeyEvent kevt ) {

		super.keyPressed( kevt );

		if ( kevt.getSource() == txtDataNasc ) {
			if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
				txtIdade.setVlrInteger( calcIdade() );
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sql = new StringBuilder();
		StringBuilder where_status = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			DLREmpregado dl = new DLREmpregado( this, con );
			dl.setVisible( true );


			if ( dl.OK ) {


				sql.append( "select rp.matempr, rp.nomeempr, rp.foneempr, rp.dtadmissao, rp.emailempr, rp.codfunc, dp.coddep, " );
				sql.append( "fu.descfunc, dp.descdep " );
				sql.append( "from rhempregado rp, rhfuncao fu, rhdepto dp " );
				sql.append( "where   " );
				sql.append( "fu.codemp=rp.codempfo and fu.codfilial=rp.codfilialfo and fu.codfunc=rp.codfunc " );
				sql.append( "and dp.codemp=rp.codempdp and dp.codfilial=rp.codfilialdp and dp.coddep=rp.coddep and " );
				sql.append( "rp.codemp=? and rp.codfilial=? " );

				Vector<Object> vstatus = new Vector<Object>();

				if( dl.isAdmitido() ) 		vstatus.addElement( STATUS_ADMITIDO.getValue() ) ;
				if( dl.isDemitido() ) 		vstatus.addElement( STATUS_DEMITIDO.getValue() ) ;
				if( dl.isFerias() ) 		vstatus.addElement( STATUS_FERIAS.getValue() ) ;
				if( dl.isAfastado() ) 		vstatus.addElement( STATUS_AFASTADO.getValue() ) ;
				if( dl.isMaternidade() )	vstatus.addElement( STATUS_MATERNIDADE.getValue() ) ;
				if( dl.isAposentado() )	 	vstatus.addElement( STATUS_APOSENTADO.getValue() ) ;
				
				sql.append( " and rp.statusempr in ( '" + Funcoes.vectorToString( vstatus, "','" ) + "' ) " );
				
				sql.append( "order by + " + dl.getOrdem() );

				ps = con.prepareStatement( sql.toString() );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );

				rs = ps.executeQuery();

				FPrinterJob dlGr = null;
				HashMap<String, Object> hParam = new HashMap<String, Object>();

				hParam.put( "CODEMP", Aplicativo.iCodEmp );
				hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "RHEMPREGADO" ) );

				dlGr = new FPrinterJob( "relatorios/EmprResumido.jasper","Relação de Empregados","",rs,hParam,this,null); 

				if ( bVisualizar==TYPE_PRINT.VIEW ) {
					dlGr.setVisible( true );
				}
				else {
					try {
						JasperPrintManager.printReport( dlGr.getRelatorio(), true );
					} catch ( Exception e ) {
						e.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao abrir relátorio!" + e.getMessage(), true, con, e );
					}
				}

			}	 

		}
		catch (Exception e) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro na geração do relátorio!" + e.getMessage(), true, con, e );
		}

	}

	public void afterCarrega( CarregaEvent cevt ) {


		if ( cevt.getListaCampos() == lcCampos ) {

			txtIdade.setVlrInteger( calcIdade() );
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	private class DLREmpregado extends FFDialogo {

		private static final long serialVersionUID = 1l;

		private final JTextFieldPad txtCodCand = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

		private final JTextFieldPad txtNomeEmpr = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

		private final JTextFieldPad txtNomeDe = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

		private final JTextFieldPad txtNomeAte = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
		
		private final JCheckBoxPad cbAdmitido = new JCheckBoxPad( STATUS_ADMITIDO.getName(), "S", "N" );//AD
		private final JCheckBoxPad cbDemitido = new JCheckBoxPad( STATUS_DEMITIDO.getName(), "S", "N" );//AD
		private final JCheckBoxPad cbFerias = new JCheckBoxPad( STATUS_FERIAS.getName(), "S", "N" );//AD
		private final JCheckBoxPad cbMaternidade = new JCheckBoxPad( STATUS_MATERNIDADE.getName(), "S", "N" );//AD
		private final JCheckBoxPad cbAfastado = new JCheckBoxPad( STATUS_AFASTADO.getName(), "S", "N" );//AD
		private final JCheckBoxPad cbAposentado = new JCheckBoxPad( STATUS_APOSENTADO.getName(), "S", "N" );//AD
		
		private JRadioGroup<String, String> rgOrdem = null;

		DLREmpregado( Component cOrig, DbConnection cn ) {

			super( cOrig ); 
			
			setTitulo( "Relatório de Empregados" );
			setAtribos( 355, 260 );
			setConexao( cn );

			adic( new JLabelPad( "Ordem :" ), 10, 10, 320, 20 );

			Vector<String> vLabs = new Vector<String>();
			Vector<String> vVals = new Vector<String>();

			vLabs.addElement( "Nome" );
			vLabs.addElement( "Matrícula" );
			vLabs.addElement( "Admissão" );

			vVals.addElement( "RP.NOMEEMPR" );
			vVals.addElement( "RP.matempr" );
			vVals.addElement( "RP.DTADMISSAO" );
			
			rgOrdem = new JRadioGroup<String, String>( 1, 3, vLabs, vVals );
			rgOrdem.setVlrString( "N" );
			
			adic( rgOrdem, 10, 30, 320, 30 );

			JPanelPad pnStatus = new JPanelPad();
			pnStatus.setBorder( SwingParams.getPanelLabel( "Status", Color.blue ) );
			
			adic(pnStatus, 7, 70, 325, 100);
			
			pnStatus.adic( cbAdmitido, 10, 5, 150, 20 );
			pnStatus.adic( cbDemitido, 165, 5, 150, 20 );
			pnStatus.adic( cbFerias, 10, 25, 150, 20 );
			pnStatus.adic( cbMaternidade, 165, 25, 150, 20 );
			pnStatus.adic( cbAfastado, 10, 45, 150, 20 );
			pnStatus.adic( cbAposentado, 165, 45, 150, 20 );
			
			cbAdmitido.setVlrString( "S" );

		}
		
		public Boolean isAdmitido() {
			return "S".equals(cbAdmitido.getVlrString());
		}
		public Boolean isDemitido() {
			return "S".equals(cbDemitido.getVlrString());
		}
		public Boolean isFerias() {
			return "S".equals(cbFerias.getVlrString());
		}
		public Boolean isMaternidade() {
			return "S".equals(cbMaternidade.getVlrString());
		}
		public Boolean isAfastado() {
			return "S".equals(cbAfastado.getVlrString());
		}
		public Boolean isAposentado() {
			return "S".equals(cbAposentado.getVlrString());
		}
		public String getNomeDe() {
			return txtNomeDe.getVlrString();
		}
		public String getNomeAte() {
			return txtNomeAte.getVlrString();
		}
		public String getOrdem() {
			return rgOrdem.getVlrString();
		}
	}
}
