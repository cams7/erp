/*
 * Projeto: Freedom Pacote: org.freedom.modules.crm Classe: @(#)FContato.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */

package org.freedom.modulos.crm.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.business.webservice.WSCep;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Endereco;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FAndamento;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.crm.dao.DAOContato;
import org.freedom.modulos.crm.dao.DAOContato.CONT_PREFS;
import org.freedom.modulos.crm.view.dialog.report.DLRCont;
import org.freedom.modulos.crm.view.dialog.utility.DLContToCli;
import org.freedom.modulos.crm.view.frame.crud.detail.FFichaAval;
import org.freedom.modulos.crm.view.frame.crud.plain.FAtividade;
import org.freedom.modulos.crm.view.frame.crud.plain.FOrigContato;
import org.freedom.modulos.crm.view.frame.crud.plain.FTipoCont;
import org.freedom.modulos.std.view.frame.crud.plain.FSetor;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoCli;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;

/**
 * Cadastro de contatos
 * 
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva
 * @version 09/09/2009 - Alex Rodrigues
 */
public class FContato extends FTabDados implements RadioGroupListener, PostListener, ActionListener, ChangeListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCont = new JPanelPad( 500, 330 );

	private JPanelPad pinVend = new JPanelPad( 0, 80 );

	private JPanelPad pinRodAtiv = new JPanelPad( 0, 80 );

	private JPanelPad pinRodGrupos = new JPanelPad( 0, 80 );

	private JPanelPad pinGrupos = new JPanelPad( 0, 80 );

	private JPanelPad pnAtiv = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnGrupos = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCompl = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtRazCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtNomeCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCargoCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtContCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCnpjCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtInscCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtCpfCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtRgCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtEndCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtUFCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtCepCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDDDCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFaxCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtCelCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtEmailCont = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 50, 0 );

	private JTextFieldPad txtEdificioCto = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDataCont = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAtiv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAtiv = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodMun = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtDDDMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodTipoCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodOrigCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescOrigCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTipoCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JCheckBoxPad cbAtivo = new JCheckBoxPad( "Ativo", "S", "N" );

	private JRadioGroup<String, String> rgPessoa = null;

	private JTextAreaPad txaObs = new JTextAreaPad();

	private JButtonPad btExportCli = new JButtonPad( Icone.novo( "btExportaCli.png" ) );

	private JButtonPad btBuscaEnd = new JButtonPad( Icone.novo( "btBuscacep.png" ) );
	
	private JButtonPad btFichaAval = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JTablePad tabAtiv = new JTablePad();

	private JTablePad tabGrupos = new JTablePad();

	private ListaCampos lcVend = new ListaCampos( this, "VD" );

	private ListaCampos lcSetor = new ListaCampos( this, "SR" );

	private ListaCampos lcAtiv = new ListaCampos( this, "AV" );

	private ListaCampos lcGrupo = new ListaCampos( this, "GP" );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcOrigCont = new ListaCampos( this, "OC" );

	private ListaCampos lcTipoCli = new ListaCampos( this, "TC" );

	private ListaCampos lcTipoCont = new ListaCampos( this, "TO" );

	private ListaCampos lcUF = new ListaCampos( this );

	private ListaCampos lcPais = new ListaCampos( this );

	private ListaCampos lcAtivFK = new ListaCampos( this, "AV" );

	private ListaCampos lcGrupFK = new ListaCampos( this, "GP" );

	private Navegador navAtiv = new Navegador( true );

	private Navegador navGrupos = new Navegador( true );
	
	private DAOContato daocontato = null;
	
	public FContato() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Contatos" );
		setAtribos( 50, 10, 530, 640 );

		lcAtiv.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcAtiv );
		lcAtiv.setTabela( tabAtiv );

		lcGrupo.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcGrupo );
		lcGrupo.setTabela( tabGrupos );

		Vector<String> vPessoaLab = new Vector<String>();
		Vector<String> vPessoaVal = new Vector<String>();
		vPessoaLab.addElement( "Jurídica" );
		vPessoaLab.addElement( "Física" );
		vPessoaVal.addElement( "J" );
		vPessoaVal.addElement( "F" );

		rgPessoa = new JRadioGroup<String, String>( 2, 1, vPessoaLab, vPessoaVal );
		rgPessoa.addRadioGroupListener( this );
		rgPessoa.setVlrString( "F" );

		montaListaCampos();
		montaTela();

		txtCpfCont.setMascara( JTextFieldPad.MC_CPF );
		txtCnpjCont.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepCont.setMascara( JTextFieldPad.MC_CEP );
		txtFoneCont.setMascara( JTextFieldPad.MC_FONE );
		txtFaxCont.setMascara( JTextFieldPad.MC_FONE );
		txtCelCont.setMascara( JTextFieldPad.MC_CELULAR );

		btBuscaEnd.setToolTipText( "Busca Endereço a partir do CEP" );
		btExportCli.setToolTipText( "Transforma contato em cliente" );
		
		lcCampos.addPostListener( this );
		
		lcCampos.addInsertListener( this );

		btPrevimp.setToolTipText( "Previsão do relatório de contatos" );
		btImp.setToolTipText( "Impressão de relatório de contatos" );
		btExportCli.setToolTipText( "Transforma em contato em cliente" );
		btBuscaEnd.setToolTipText( "Busca endereço pelo CEP" );
		btFichaAval.setToolTipText( "Ficha avaliativa" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btExportCli.addActionListener( this );
		btBuscaEnd.addActionListener( this );
		btFichaAval.addActionListener( this );

	

		tpn.addChangeListener( this );

		setImprimir( true );
	}

	private void montaListaCampos() {

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVend, FVendedor.class.getCanonicalName() );

		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, true ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setQueryCommit( false );
		lcSetor.setReadOnly( true );
		txtCodSetor.setTabelaExterna( lcSetor, FSetor.class.getCanonicalName() );

		lcAtivFK.add( new GuardaCampo( txtCodAtiv, "CodAtiv", "Cód.ativ.", ListaCampos.DB_PK, true ) );
		lcAtivFK.add( new GuardaCampo( txtDescAtiv, "DescAtiv", "Descrição da atividade", ListaCampos.DB_SI, false ) );
		lcAtivFK.montaSql( false, "ATIVIDADE", "TK" );
		lcAtivFK.setReadOnly( true );
		lcAtivFK.setQueryCommit( false );
		txtCodAtiv.setTabelaExterna( lcAtivFK, FAtividade.class.getCanonicalName() );

		lcGrupFK.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grup.", ListaCampos.DB_PK, true ) );
		lcGrupFK.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrupFK.montaSql( false, "GRUPO", "EQ" );
		lcGrupFK.setReadOnly( true );
		lcGrupFK.setQueryCommit( false );
		txtCodGrup.setTabelaExterna( lcGrupFK, null );

		/** TIPO CLI */
		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, FTipoCli.class.getCanonicalName() );

		lcOrigCont.add( new GuardaCampo( txtCodOrigCont, "CodOrigCont", "Cód.origem", ListaCampos.DB_PK, true ) );
		lcOrigCont.add( new GuardaCampo( txtDescOrigCont, "DescOrigCont", "descrição da origem", ListaCampos.DB_SI, false ) );
		lcOrigCont.montaSql( false, "ORIGCONT", "TK" );
		lcOrigCont.setQueryCommit( false );
		lcOrigCont.setReadOnly( true );
		txtCodOrigCont.setTabelaExterna( lcOrigCont, FOrigContato.class.getCanonicalName() );

		/** MUNICIPIO */
		lcMunic.setUsaME( false );
		lcMunic.add( new GuardaCampo( txtCodMun, "CodMunic", "Cód.Munic.", ListaCampos.DB_PK, false ) );
		lcMunic.add( new GuardaCampo( txtDescMun, "NomeMunic", "Nome Munic.", ListaCampos.DB_SI, false ) );
		lcMunic.add( new GuardaCampo( txtDDDMun, "DDDMunic", "DDD Munic.", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcMunic.montaSql( false, "MUNICIPIO", "SG" );
		lcMunic.setQueryCommit( false );
		lcMunic.setReadOnly( true );
		txtCodMun.setTabelaExterna( lcMunic, FMunicipio.class.getCanonicalName() );

		/** UF */
		lcUF.setUsaME( false );
		lcUF.add( new GuardaCampo( txtSiglaUF, "SiglaUf", "Sigla", ListaCampos.DB_PK, false ) );
		lcUF.add( new GuardaCampo( txtNomeUF, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "CODPAIS = #S", txtCodPais );
		lcUF.montaSql( false, "UF", "SG" );
		lcUF.setQueryCommit( false );
		lcUF.setReadOnly( true );
		txtSiglaUF.setTabelaExterna( lcUF, FUF.class.getCanonicalName() );

		/** PAIS */
		lcPais.setUsaME( false );
		lcPais.add( new GuardaCampo( txtCodPais, "CodPais", "Cod.país.", ListaCampos.DB_PK, false ) );
		lcPais.add( new GuardaCampo( txtDescPais, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPais.montaSql( false, "PAIS", "SG" );
		lcPais.setQueryCommit( false );
		lcPais.setReadOnly( true );
		txtCodPais.setTabelaExterna( lcPais, FPais.class.getCanonicalName() );

		/** TIPO CONT */
		lcTipoCont.add( new GuardaCampo( txtCodTipoCont, "CodTipoCont", "Cód.tp.cli.", ListaCampos.DB_PK, true ) );
		lcTipoCont.add( new GuardaCampo( txtDescTipoCont, "DescTipoCont", "Descrição do tipo de contato", ListaCampos.DB_SI, false ) );
		lcTipoCont.montaSql( false, "TIPOCONT", "TK" );
		lcTipoCont.setQueryCommit( false );
		lcTipoCont.setReadOnly( true );
		txtCodTipoCont.setTabelaExterna( lcTipoCont, FTipoCont.class.getCanonicalName() );

	}

	private void montaTela() {

		setPainel( pinCont );
		adicTab( "Contato", pinCont );

		adicCampo( txtCodCont, 7, 20, 80, 20, "CodCto", "Cód.cto.", ListaCampos.DB_PK, true );
		adicCampo( txtRazCont, 90, 20, 307, 20, "RazCto", "Razão social do contato", ListaCampos.DB_SI, true );
		adicDB( rgPessoa, 400, 20, 100, 60, "PessoaCto", "Pessoa", true );
		adicDB( cbAtivo, 19, 60, 70, 20, "AtivoCto", "Ativo", true );
		adicCampo( txtNomeCont, 90, 60, 307, 20, "NomeCto", "Nome do contato", ListaCampos.DB_SI, true );
		adicCampo( txtCodSetor, 7, 110, 100, 20, "CodSetor", "Cód.setor", ListaCampos.DB_FK, txtDescSetor, true );
		adicDescFK( txtDescSetor, 110, 110, 390, 20, "DescSetor", "Descrição do setor do contato" );
		adicCampo( txtRgCont, 7, 150, 120, 20, "RgCto", "RG", ListaCampos.DB_SI, false );
		adicCampo( txtCpfCont, 130, 150, 120, 20, "CpfCto", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtCnpjCont, 253, 150, 120, 20, "CnpjCto", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtInscCont, 376, 150, 123, 20, "InscCto", "Inscrição Estadual", ListaCampos.DB_SI, false );
		adicCampo( txtContCont, 7, 190, 120, 20, "ContCto", "Falar com:", ListaCampos.DB_SI, false );
		adicCampo( txtCargoCont, 130, 190, 120, 20, "CargoContCto", "Cargo", ListaCampos.DB_SI, false );
		adicCampo( txtEmailCont, 253, 190, 246, 20, "EmailCto", "E-Mail", ListaCampos.DB_SI, false );
		adicCampo( txtCepCont, 7, 230, 95, 20, "CepCto", "Cep", ListaCampos.DB_SI, false );
		adic( btBuscaEnd, 105, 229, 20, 20 );
		adicCampo( txtEndCont, 130, 230, 215, 20, "EndCto", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumCont, 348, 230, 67, 20, "NumCto", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplCont, 418, 230, 80, 20, "ComplCto", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtBairCont, 7, 270, 141, 20, "BairCto", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtDDDCont, 151, 270, 30, 20, "DDDCto", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneCont, 184, 270, 80, 20, "FoneCto", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxCont, 267, 270, 80, 20, "FaxCto", "Fax", ListaCampos.DB_SI, false );
		adicCampo( txtCelCont, 350, 270, 80, 20, "CelCto", "Celular", ListaCampos.DB_SI, false );
		adicCampo( txtNumEmp, 433, 270, 67, 20, "numempcto", "Nº Func.", ListaCampos.DB_SI, false );


		adicCampo( txtEdificioCto, 7, 310, 141, 20, "EdificioCto", "Edifício", ListaCampos.DB_SI, false);

		adicCampo( txtCodTipoCont, 151, 310, 80, 20, "CodTipoCont", "Cód.Tipo Cont.", ListaCampos.DB_FK, txtDescTipoCont, true );
		adicDescFK( txtDescTipoCont, 234, 310, 265, 20, "DescTipoCont", "Descrição do tipo de contato" );

		adicCampo( txtCodOrigCont, 7, 350, 100, 20, "CodOrigCont", "Cod.orig.", ListaCampos.DB_FK, txtDescOrigCont, true );
		adicDescFK( txtDescOrigCont, 110, 350, 390, 20, "DescOrigCont", "Descrição da origem" );

		adicCampo( txtCodTipoCli, 7, 390, 100, 20, "CodTipoCli", "Cód.Tipo Cli.", ListaCampos.DB_FK, txtDescTipoCli, false );
		adicDescFK( txtDescTipoCli, 110, 390, 390, 20, "DescTipoCli", "Descrição do tipo de cliente" );

		adicCampo( txtCodPais, 7, 430, 100, 20, "CodPais", "Cod.país", ListaCampos.DB_FK, txtDescPais, false );
		adicDescFK( txtDescPais, 110, 430, 390, 20, "NomePais", "Nome do país" );
		adicCampo( txtSiglaUF, 7, 470, 100, 20, "SiglaUf", "Sigla UF", ListaCampos.DB_FK, txtNomeUF, false );
		adicDescFK( txtNomeUF, 110, 470, 390, 20, "NomeUF", "Nome UF" );
		adicCampo( txtCodMun, 7, 510, 100, 20, "CodMunic", "Cod.munic.", ListaCampos.DB_FK, txtDescMun, false );
		adicDescFK( txtDescMun, 110, 510, 390, 20, "NomeMunic", "Nome do municipio" );

		adicTab( "Informações complementares", pnCompl );

		pnCompl.add( pinVend, BorderLayout.NORTH );
		pnCompl.add( new JScrollPane( txaObs ), BorderLayout.CENTER );

		adicDBLiv( txaObs, "ObsCto", "Observações", false );

		setPainel( pinVend );

		adicCampo( txtCodVend, 7, 25, 100, 20, "CodVend", "Cód.comiss.", ListaCampos.DB_FK, txtDescVend, false );
		adicDescFK( txtDescVend, 110, 25, 300, 20, "NomeVend", "Nome do comissionado" );
		adicCampo( txtDataCont, 413, 25, 87, 20, "DataCto", "Data", ListaCampos.DB_SI, false );
		adic( new JLabelPad( "Observações:" ), 7, 55, 150, 20 );

		setListaCampos( true, "CONTATO", "TK" );
		lcCampos.setQueryInsert( false );

		// Atividade

		setPainel( pinRodAtiv, pnAtiv );
		adicTab( "Atividade", pnAtiv );
		setListaCampos( lcAtiv );
		setNavegador( navAtiv );
		pnAtiv.add( pinRodAtiv, BorderLayout.SOUTH );
		pnAtiv.add( new JScrollPane( tabAtiv ), BorderLayout.CENTER );

		pinRodAtiv.adic( navAtiv, 0, 50, 270, 25 );

		adicCampo( txtCodAtiv, 7, 20, 100, 20, "CodAtiv", "Cód.ativ.", ListaCampos.DB_PF, txtDescAtiv, true );
		adicDescFK( txtDescAtiv, 110, 20, 390, 20, "DescAtiv", "Descrição da atividade" );
		setListaCampos( false, "CTOATIV", "TK" );
		lcAtiv.montaTab();
		lcAtiv.setQueryInsert( false );
		lcAtiv.setQueryCommit( false );

		tabAtiv.setTamColuna( 220, 1 );

		setPainel( pinRodGrupos, pnGrupos );
		adicTab( "Interesses", pnGrupos );
		setListaCampos( lcGrupo );
		setNavegador( navGrupos );
		pnGrupos.add( pinRodGrupos, BorderLayout.SOUTH );
		pnGrupos.add( new JScrollPane( tabGrupos ), BorderLayout.CENTER );

		pinRodGrupos.adic( navGrupos, 0, 50, 270, 25 );

		adicCampo( txtCodGrup, 7, 20, 100, 20, "CodGrup", "Cód.Grupo.", ListaCampos.DB_PF, txtDescGrup, true );
		adicDescFK( txtDescGrup, 110, 20, 390, 20, "DescGrup", "Descrição do grupo" );
		setListaCampos( false, "CTOGRPINT", "TK" );
		lcGrupo.montaTab();
		lcGrupo.setQueryInsert( false );
		lcGrupo.setQueryCommit( false );

		tabGrupos.setTamColuna( 80, 0 );
		tabGrupos.setTamColuna( 250, 1 );

		pnGImp.removeAll();
		pnGImp.setLayout( new GridLayout( 1, 4 ) );
		pnGImp.setPreferredSize( new Dimension( 110, 26 ) );
		pnGImp.add( btExportCli );
		pnGImp.add( btImp );
		pnGImp.add( btPrevimp );
		pnGImp.add( btFichaAval );
		
		
	}

	private void buscaEndereco() {

		if ( !"".equals( txtCepCont.getVlrString() ) ) {

			txtEndCont.setEnabled( false );
			txtComplCont.setEnabled( false );
			txtBairCont.setEnabled( false );
			txtCodPais.setEnabled( false );
			txtSiglaUF.setEnabled( false );
			txtCodMun.setEnabled( false );
			txtDDDMun.setEnabled( false );

			Thread th = new Thread( new Runnable() {

				public void run() {

					try {
						WSCep cep = new WSCep();
						cep.setCon( con );
						cep.setCep( txtCepCont.getVlrString() );
						cep.busca();
						Endereco endereco = cep.getEndereco();

						txtEndCont.setVlrString( endereco.getTipo() + " " + endereco.getLogradouro() );
						txtComplCont.setVlrString( endereco.getComplemento() );
						txtBairCont.setVlrString( endereco.getBairro() );
						txtCidCont.setVlrString( endereco.getCidade() );
						txtUFCont.setVlrString( endereco.getSiglauf() );
						txtCodPais.setVlrInteger( endereco.getCodpais() );
						txtSiglaUF.setVlrString( endereco.getSiglauf() );
						txtCodMun.setVlrString( endereco.getCodmunic() );

						lcPais.carregaDados();
						lcUF.carregaDados();
						lcMunic.carregaDados();

						txtNumCont.requestFocus();
					} catch ( Exception e ) {
						Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
					} finally {
						txtEndCont.setEnabled( true );
						txtComplCont.setEnabled( true );
						txtBairCont.setEnabled( true );
						txtCodPais.setEnabled( true );
						txtSiglaUF.setEnabled( true );
						txtCodMun.setEnabled( true );
						txtDDDMun.setEnabled( true );
					}
				}
			} );
			try {
				th.start();
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
				txtCepCont.requestFocus();
			}
		}
		else {
			Funcoes.mensagemInforma( null, "Digite um CEP para busca!" );
			txtCepCont.requestFocus();
		}
	}

	private boolean duploCNPJ() {

		boolean bRetorno = false;

		try {
			PreparedStatement ps = con.prepareStatement( "SELECT CNPJCTO FROM TKCONTATO WHERE CNPJCTO=? AND NOT CODCTO=? " );
			ps.setString( 1, txtCnpjCont.getVlrString() );
			ps.setInt( 2, txtCodCont.getVlrInteger() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				bRetorno = true;
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao checar CNPJ.\n" + err.getMessage(), true, con, err );
		}

		return bRetorno;
	}

	private void exportaCli() {

		if ( txtCodCont.getVlrInteger().intValue() == 0 || lcCampos.getStatus() != ListaCampos.LCS_SELECT ) {
			Funcoes.mensagemInforma( this, "Selecione um contato cadastrado antes!" );
			return;
		}

		DLContToCli dl = new DLContToCli( this, txtCodSetor.getVlrInteger() , txtCodTipoCli.getVlrInteger() );
		dl.setConexao( con );
		dl.setVisible( true );

		if ( !dl.OK ) {
			dl.dispose();
			return;
		}

		DLContToCli.ContatoClienteBean contatoClienteBean = dl.getValores();

		dl.dispose();

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT IRET FROM TKCONTCLISP(?,?,?,?,?,?,?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, lcCampos.getCodFilial() );
			ps.setInt( 3, txtCodCont.getVlrInteger().intValue() );
			ps.setInt( 4, lcCampos.getCodFilial() );
			ps.setInt( 5, contatoClienteBean.getTipo() );
			ps.setInt( 6, lcCampos.getCodFilial() );
			ps.setInt( 7, contatoClienteBean.getClassificacao() );
			ps.setInt( 8, lcCampos.getCodFilial() );
			ps.setInt( 9, contatoClienteBean.getSetor() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( Funcoes.mensagemConfirma( this, "Cliente '" + rs.getInt( 1 ) + "' criado com sucesso!\nGostaria de edita-lo agora?" ) == JOptionPane.OK_OPTION ) {
					abreCli( rs.getInt( 1 ) );
				}
			}

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao criar cliente!\n" + err.getMessage(), true, con, err );
		}
	}

	private void abreCli( int codigoCliente ) {

		FCliente cliente = null;
		if ( Aplicativo.telaPrincipal.temTela( FCliente.class.getName() ) ) {
			cliente = (FCliente) Aplicativo.telaPrincipal.getTela( FCliente.class.getName() );
		}
		else {
			cliente = new FCliente();
			Aplicativo.telaPrincipal.criatela( "Cliente", cliente, con );
		}

		cliente.exec( codigoCliente );
	}

	private void imprimir(TYPE_PRINT bVisualizar) {
		
		String where = "";
		String filtros = "";
		String[] sValores;
		DLRCont dl = new DLRCont( this, con );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		sValores = dl.getValores();
		dl.dispose();
		
		Blob fotoemp = FPrinterJob.getLogo( con );

		StringBuilder sql = new StringBuilder();
		
		if ( sValores[ DLRCont.VALORES.DE.ordinal() ].trim().length() > 0 ) {
			where = " AND C.RAZCTO >= '" + sValores[ DLRCont.VALORES.DE.ordinal() ] + "'";
			
		}
		if ( sValores[ DLRCont.VALORES.A.ordinal() ].trim().length() > 0 ) {
			where = where  + " AND C.RAZCTO <= '" + sValores[ DLRCont.VALORES.A.ordinal() ] + "'";
			
		}
		//{ ORDEM, OBSERVACAO, DE, A, FISICA, CIDADE, JURIDICA, MODO, SETOR, DESCSETOR, TIPOIMP, CODTIPOCLI, SIGLAUF, CODMUNIC, CODGRUP, CODATIV, CODORIGCONT, EDIFICIO   } 
		if ( sValores[ DLRCont.VALORES.FISICA.ordinal() ].equals( "N" ) ) {
			where = where + " AND C.PESSOACTO <> 'F'";
	
		}
		if ( sValores[ DLRCont.VALORES.CIDADE.ordinal() ].length() > 0 ) {
			where = where + " AND C.CIDCTO = '" + sValores[DLRCont.VALORES.CIDADE.ordinal() ] + "'";
	
		}
		if ( sValores[ DLRCont.VALORES.JURIDICA.ordinal() ].equals( "N" ) ) {
			where = where + " AND C.PESSOACTO <> 'J'";
		}
		if ( sValores[ DLRCont.VALORES.SETOR.ordinal() ].length() > 0 ) {
			where = where +  " AND C.CODSETOR = " + sValores[ DLRCont.VALORES.SETOR.ordinal() ];
		}
		if ( sValores[ DLRCont.VALORES.EDIFICIO.ordinal() ].length() > 0 ) {
			where = where +  " AND C.EDIFICIOCTO like '%" + sValores[ DLRCont.VALORES.EDIFICIO.ordinal() ] + "%' ";
		}
		
		if ( sValores[ DLRCont.VALORES.SIGLAUF.ordinal() ].length() > 0 ) {
			where = where +  " AND C.SIGLAUF = '" + sValores[ DLRCont.VALORES.SIGLAUF.ordinal() ] + "' ";
		}
		
		if ( sValores[ DLRCont.VALORES.CODMUNIC.ordinal() ].length() > 0 ) {
			where = where +  " AND C.CODMUNIC = '" + sValores[ DLRCont.VALORES.CODMUNIC.ordinal() ] + "'";
		}
		/*
		if ( sValores[ DLRCont.VALORES.CODORIGCONT.ordinal() ].length() > 0 ) {
			where = where +  " AND C.CODORIGCONT = " + sValores[ DLRCont.VALORES.CODORIGCONT.ordinal() ];
		}
		*/
		sql.append("select f.razfilial, f.dddfilial, f.fonefilial ");
		sql.append(", f.endfilial, f.numfilial, f.siglauf siglauff ");
		sql.append(", f.bairfilial, f.cnpjfilial,f.emailfilial ");
		sql.append(" , m.nomemunic nomemunicf ");
		sql.append(", c.codemp, c.codfilial , c.codcto, c.razcto, c.endcto, c.numcto, c.baircto ");
		sql.append(", c.siglauf siglaufc, c.cpfcto, c.dddcto ");
		sql.append(", c.fonecto, c.cnpjcto, c.celcto ");
		sql.append(", c.contcto, mc.nomemunic nomemunicc, c.pessoacto, c.emailcto ");
		sql.append(", c.codtipocli, c.edificiocto,c.complcto ");
		sql.append("from sgfilial f ");
		sql.append("left outer join sgmunicipio m on ");
		sql.append("m.codmunic=f.codmunic and m.codpais=f.codpais ");
		sql.append("and m.siglauf=f.siglauf ");
		sql.append("inner join tkcontato c on ");
		sql.append("c.codemp=? and c.codfilial=? ");
		sql.append("left outer join sgmunicipio mc on ");
		sql.append("mc.codmunic=c.codmunic and mc.codpais=c.codpais ");
		sql.append("and mc.siglauf=c.siglauf ");
		sql.append("where f.codemp=? and f.codfilial=? " + where);
		
		
		if ("G".equals( sValores[DLRCont.VALORES.TIPOIMP.ordinal()]) ) {
			this.imprimirGrafico(bVisualizar, sql, fotoemp);
		} else {
			this.imprimirTexto( bVisualizar, sValores );
		}
		
	}
	
	private void imprimirGrafico( TYPE_PRINT bVisualizar , StringBuilder sql, Blob fotoemp ) { 
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			System.out.println("QUERY: " + sql.toString());
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "TKCONTATO" ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CRFICHAAVAL" ) );
			rs = ps.executeQuery();
	
		}catch (Exception e) {
			Funcoes.mensagemErro( this, "Erro executando consulta dos contatos: \n" + e.getMessage() );
			e.printStackTrace();
		}
		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		
		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "TKCONTATO" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		try {
			hParam.put( "LOGOEMP", new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
	
		dlGr = new FPrinterJob( "relatorios/contato.jasper", "Listagem de contatos", "", rs, hParam, this );
		if (bVisualizar==TYPE_PRINT.VIEW) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão da listagem de contatos !" + err.getMessage(), true, con, err );
			}
		}
		/*		FPrinterJob dlGr = null;

		if ( "D".equals( rgFormato.getVlrString() ) ) {
			String fileRelDetalhado = "relatorios/ResumoDiarioDetalhado.jasper";
			if ("S".equals( cbAgruparVendedor.getVlrString())) {
				fileRelDetalhado = "relatorios/ResumoDiarioDetalhado_02.jasper";
			}
			dlGr = new FPrinterJob( fileRelDetalhado, "Resumo de Vendas diario - detalhado", sCab, rs, null, this );
		}
		else if ( "R".equals( rgFormato.getVlrString() ) ) {
			dlGr = new FPrinterJob( "relatorios/ResumoDiarioResumido.jasper", "Resumo de Vendas diario - resumido", sCab, rs, null, this );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de resumo diario!" + err.getMessage(), true, con, err );
			}
		}
*/
	}
	
	private void imprimirTexto( TYPE_PRINT bVisualizar, String[] sValores ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sObs = "";
		String sWhere = "";
		String sAnd = " WHERE ";
		String sTmp = null;
		Vector<String> vFiltros = new Vector<String>();
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		int iContaReg = 0;
		FAndamento And = null;

		if ( sValores[ DLRCont.VALORES.OBSERVACAO.ordinal() ].equals( "S" ) ) {
			sObs = ",OBSCTO";
		}
		if ( sValores[ DLRCont.VALORES.DE.ordinal() ].trim().length() > 0 ) {
			sWhere = sWhere + sAnd + "RAZCTO >= '" + sValores[ 2 ] + "'";
			vFiltros.add( "RAZAO MAIORES QUE " + sValores[ 2 ].trim() );
			sAnd = " AND ";
		}
		if ( sValores[ DLRCont.VALORES.A.ordinal() ].trim().length() > 0 ) {
			sWhere = sWhere + sAnd + "RAZCTO <= '" + sValores[ 3 ] + "'";
			vFiltros.add( "RAZAO MENORES QUE " + sValores[ 3 ].trim() );
			sAnd = " AND ";
		}
		//{ ORDEM, OBSERVACAO, DE, A, FISICA, CIDADE, JURIDICA, MODO, SETOR, DESCSETOR, TIPOIMP, CODTIPOCLI, SIGLAUF, CODMUNIC, CODGRUP, CODATIV, CODORIGCONT, EDIFICIO   } 
		if ( sValores[ DLRCont.VALORES.FISICA.ordinal() ].equals( "N" ) ) {
			sWhere = sWhere + sAnd + "PESSOACTO <> 'F'";
			vFiltros.add( "PESSOAS JURIDICAS" );
			sAnd = " AND ";
		}
		if ( sValores[ DLRCont.VALORES.CIDADE.ordinal() ].length() > 0 ) {
			sWhere = sWhere + sAnd + "CIDCTO = '" + sValores[ 5 ] + "'";
			vFiltros.add( "CIDADE = " + sValores[ 5 ].trim() );
			sAnd = " AND ";
		}
		if ( sValores[ DLRCont.VALORES.JURIDICA.ordinal() ].equals( "N" ) ) {
			sWhere = sWhere + sAnd + "PESSOACTO <> 'J'";
			vFiltros.add( "PESSOAS FISICA" );
			sAnd = " AND ";
		}
		if ( sValores[ DLRCont.VALORES.SETOR.ordinal() ].length() > 0 ) {
			sWhere = sWhere + sAnd + "CODSETOR = " + sValores[ 8 ];
			vFiltros.add( "SETOR = " + sValores[ 9 ] );
			sAnd = " AND ";
		}
		if ( sValores[ DLRCont.VALORES.MODO.ordinal() ].equals( "C" ) ) {
			sSQL = "SELECT CODCTO,RAZCTO,PESSOACTO,NOMECTO,CONTCTO,ENDCTO,NUMCTO," + "BAIRCTO,CIDCTO,COMPLCTO,UFCTO,CEPCTO,CNPJCTO,INSCCTO,CPFCTO,RGCTO," + "DDDCTO, FONECTO,FAXCTO,EMAILCTO" + sObs + " FROM TKCONTATO" + sWhere + " ORDER BY " + sValores[ DLRCont.VALORES.ORDEM.ordinal() ];
			try {
				ps = con.prepareStatement( "SELECT COUNT(*) FROM TKCONTATO" + sWhere );
				rs = ps.executeQuery();
				rs.next();
				And = new FAndamento( "Montando relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
				con.commit();

				ps = con.prepareStatement( sSQL );
				rs = ps.executeQuery();

				imp.montaCab();
				imp.setTitulo( "Relatório de Contatos" );
				imp.addSubTitulo( "Relatório de Contatos" );
				imp.addSubTitulo( "Filtrado por:" );
				for ( int i = 0; i < vFiltros.size(); i++ ) {
					sTmp = vFiltros.elementAt( i );
					imp.addSubTitulo( sTmp );
				}
				imp.limpaPags();
				while ( rs.next() ) {
					if ( imp.pRow() >= linPag ) {
						imp.incPags();
						imp.eject();
					}
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					}
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" );
					imp.say( imp.pRow(), 2, "Cód.cto.:" );
					imp.say( imp.pRow(), 10, rs.getString( "CodCto" ) );
					imp.say( imp.pRow(), 20, "Razão:" );
					imp.say( imp.pRow(), 27, rs.getString( "RazCto" ) );
					imp.say( imp.pRow(), 127, "Setor:" );
					imp.say( imp.pRow(), 133, rs.getString( "PessoaCto" ) );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" );
					imp.say( imp.pRow(), 1, "Nome:" );
					imp.say( imp.pRow(), 7, rs.getString( "NomeCto" ) );
					imp.say( imp.pRow(), 60, "Contato:" );
					imp.say( imp.pRow(), 69, rs.getString( "ContCto" ) );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" );
					imp.say( imp.pRow(), 1, "Endereço:" );
					imp.say( imp.pRow(), 11, rs.getString( "EndCto" ) );
					imp.say( imp.pRow(), 62, "N.:" );
					imp.say( imp.pRow(), 67, "" + rs.getInt( "NumCto" ) );
					imp.say( imp.pRow(), 76, "Compl.:" );
					imp.say( imp.pRow(), 85, rs.getString( "ComplCto" ) != null ? rs.getString( "ComplCto" ).trim() : "" );
					imp.say( imp.pRow(), 94, "Bairro:" );
					imp.say( imp.pRow(), 103, rs.getString( "BairCto" ) != null ? rs.getString( "BairCto" ).trim() : "" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|Cidade:" );
					imp.say( imp.pRow(), 8, rs.getString( "CidCto" ) );
					imp.say( imp.pRow(), 88, "UF:" );
					imp.say( imp.pRow(), 93, rs.getString( "UfCto" ) );
					imp.say( imp.pRow(), 120, "CEP:" );
					imp.say( imp.pRow(), 126, rs.getString( "CepCto" ) != null ? Funcoes.setMascara( rs.getString( "CepCto" ), "#####-###" ) : "" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					if ( ( rs.getString( "CnpjCto" ) ) != null && ( rs.getString( "InscCto" ) != null ) ) {
						imp.say( imp.pRow(), 0, "|CNPJ:" );
						imp.say( imp.pRow(), 7, Funcoes.setMascara( rs.getString( "CnpjCto" ), "##.###.###/####-##" ) );
						imp.say( imp.pRow(), 50, "IE:" );
						if ( !rs.getString( "InscCto" ).trim().toUpperCase().equals( "ISENTO" ) && rs.getString( "UFCto" ) != null ) {
							// Funcoes.validaIE( rs.getString( "InscCto" ), rs.getString( "UFCto" ) );
							imp.say( imp.pRow(), 55, Funcoes.formataIE( rs.getString( "InscCto" ), rs.getString( "UFCto" ) ) );
						}
						imp.say( imp.pRow(), 135, "|" );
					}
					else {
						imp.say( imp.pRow(), 0, "|CPF:" );
						imp.say( imp.pRow(), 6, Funcoes.setMascara( rs.getString( "CPFCto" ), "###.###.###-##" ) );
						imp.say( imp.pRow(), 50, "RG:" );
						imp.say( imp.pRow(), 55, rs.getString( "RgCto" ) );
					}
					imp.say( imp.pRow(), 80, "Tel:" );
					imp.say( imp.pRow(), 86, "(" + ( rs.getString( "DddCto" ) != null ? rs.getString( "DddCto" ) : "    " ) + ")" + ( rs.getString( "FoneCto" ) != null ? Funcoes.setMascara( rs.getString( "FoneCto" ), "####-####" ) : "" ) );
					imp.say( imp.pRow(), 120, "Fax:" );
					imp.say( imp.pRow(), 126, rs.getString( "FaxCto" ) != null ? Funcoes.setMascara( rs.getString( "FaxCto" ), "####-####" ) : "" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|Contato:" );
					imp.say( imp.pRow(), 9, rs.getString( "ContCto" ) );
					imp.say( imp.pRow(), 70, "E-mail:" );
					imp.say( imp.pRow(), 79, Funcoes.copy( rs.getString( "EmailCto" ), 54 ) );
					imp.say( imp.pRow(), 135, "|" );
					if ( sObs.length() > 0 ) {
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|Obs:" );
						imp.say( imp.pRow(), 6, rs.getString( "ObsCto" ) );
						imp.say( imp.pRow(), 135, "|" );
					}
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );

					And.atualiza( iContaReg );
					iContaReg++;
				}
				imp.eject();
				imp.fechaGravacao();

				con.commit();
				And.dispose();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro consulta tabela de contatos!" + err.getMessage(), true, con, err );
			}
		}
		else if ( sValores[ DLRCont.VALORES.MODO.ordinal() ].equals( "R" ) ) {
			sSQL = "SELECT CODCTO,NOMECTO,ENDCTO,CIDCTO,DDDCTO, FONECTO " + "FROM TKCONTATO" + sWhere + " ORDER BY " + sValores[ DLRCont.VALORES.ORDEM.ordinal() ];

			try {
				ps = con.prepareStatement( "SELECT COUNT(*) FROM TKCONTATO" + sWhere );
				rs = ps.executeQuery();
				rs.next();
				And = new FAndamento( "Montando Relatório, Aguarde!", 0, rs.getInt( 1 ) - 1 );
				con.commit();

				ps = con.prepareStatement( sSQL );
				rs = ps.executeQuery();

				imp.montaCab();
				imp.setTitulo( "Relatório de Contatos" );
				imp.addSubTitulo( "Relatório de Contatos" );
				imp.limpaPags();

				while ( rs.next() ) {
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, true );
						imp.say( imp.pRow() + 0, 2, "|" + StringFunctions.replicate( " ", 60 ) + "Filtrado por:" + StringFunctions.replicate( " ", 60 ) + "|" );
						for ( int i = 0; i < vFiltros.size(); i++ ) {
							sTmp = vFiltros.elementAt( i );
							sTmp = "|" + StringFunctions.replicate( " ", ( ( ( 135 - sTmp.length() ) / 2 ) - 1 ) ) + sTmp;
							sTmp += StringFunctions.replicate( " ", 134 - sTmp.length() ) + "|";
							imp.say( imp.pRow() + 1, 0, imp.comprimido() );
							imp.say( imp.pRow(), 2, sTmp );
						}
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "| Código" );
						imp.say( imp.pRow(), 10, "Nome:" );
						imp.say( imp.pRow(), 50, "Endereço:" );
						imp.say( imp.pRow(), 90, "Cidade:" );
						imp.say( imp.pRow(), 120, "Tel:" );
						imp.say( imp.pRow(), 135, "|" );
						imp.say( imp.pRow() + 1, 0, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					}
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" );
					imp.say( imp.pRow(), 2, rs.getString( "CodCto" ) );
					imp.say( imp.pRow(), 10, rs.getString( "NomeCto" ) != null ? rs.getString( "NomeCto" ).substring( 0, 39 ) : "" );
					imp.say( imp.pRow(), 50, rs.getString( "EndCto" ) != null ? rs.getString( "EndCto" ).substring( 0, 39 ) : "" );
					imp.say( imp.pRow(), 90, rs.getString( "CidCto" ) != null ? rs.getString( "CidCto" ).substring( 0, 29 ) : "" );
					imp.say( imp.pRow(), 120, rs.getString( "FoneCto" ) != null ? Funcoes.setMascara( rs.getString( "FoneCto" ), "####-####" ) : "" );
					imp.say( imp.pRow(), 135, "|" );
					if ( imp.pRow() >= linPag ) {
						imp.incPags();
						imp.eject();
					}
					And.atualiza( iContaReg );
					iContaReg++;
				}
				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
				imp.eject();
				imp.fechaGravacao();
				con.commit();

				And.dispose();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro consulta tabela de contatos!" + err.getMessage(), true, con, err );
			}
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btExportCli ) {
			exportaCli();
		}
		else if ( evt.getSource() == btBuscaEnd ) {
			buscaEndereco();
		}
		else if (evt.getSource() == btFichaAval ) {
			abreFichaAval();
		}

		super.actionPerformed( evt );
	}

	private void impFichaAval() {
		if (txtCodCont.getVlrInteger().intValue()==0) {
			Funcoes.mensagemInforma( this, "Selecione um contato !" );
		} else if (lcCampos.getStatus()==ListaCampos.LCS_INSERT) {
			Funcoes.mensagemInforma( this, "Registro não foi salvo !" );
		} else {
			impFichaAval(txtCodCont.getVlrInteger().intValue());
		}
	}
	
	public void impFichaAval(final int codcont){
		/*
		Blob fotoemp = FPrinterJob.getLogo( con );
		
		StringBuilder sql = daocontato.getSqlFichaAval();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "TKCONTATO" ) );
			ps.setInt( param++, codcont );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGFILIAL" ) );
			
			rs = ps.executeQuery();
		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro executando consulta: \n" + e.getMessage() );
			e.printStackTrace();
		}
		

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "TKCONTATO" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/ficha_avaliativa_091_sub.jasper");
		try {
			hParam.put( "LOGOEMP", new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		dlGr = new FPrinterJob( daocontato.getPrefs()[CONT_PREFS.LAYOUTFICHAAVAL.ordinal()].toString(), "Ficha avaliativa", "", rs, hParam, this );
		dlGr.setVisible( true );
		*/
		
	}
	
	private void abreFichaAval(){
		Integer codcto = txtCodCont.getVlrInteger();		
		FFichaAval ficha = new FFichaAval(con, codcto);
		fPrim.criatela( "Ficha Avaliativa", ficha , con );
	}

	
	public void beforePost( PostEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {

			if ( "J".equals( rgPessoa.getVlrString() ) ) {
				if ( duploCNPJ() ) {
					e.cancela();
					Funcoes.mensagemInforma( this, "Este CNPJ ja está cadastrado!" );
					txtCnpjCont.requestFocus();
				}
				else if ( !Funcoes.validaIE( txtInscCont.getVlrString(), txtUFCont.getVlrString() ) ) {
					e.cancela();
					Funcoes.mensagemInforma( this, "Inscrição Estadual Inválida ! ! !" );
					txtInscCont.requestFocus();
				}
			}

			txtDataCont.setVlrDate( new Date() );
		}
	}

	public void valorAlterado( RadioGroupEvent rgevt ) {

		if ( "J".equals( rgPessoa.getVlrString() ) ) {
			txtCnpjCont.setEnabled( true );
			txtInscCont.setEnabled( true );
			txtCpfCont.setEnabled( false );
			txtRgCont.setEnabled( false );
		}
		else if ( "F".equals( rgPessoa.getVlrString() ) ) {
			txtCnpjCont.setEnabled( false );
			txtInscCont.setEnabled( false );
			txtCpfCont.setEnabled( true );
			txtRgCont.setEnabled( true );
		}
	}

	public void stateChanged( ChangeEvent cevt ) {

		if ( cevt.getSource() == tpn ) {
			if ( tpn.getSelectedIndex() == 0 ) {
				txtCodCont.requestFocus();
			}
			else if ( tpn.getSelectedIndex() == 4 ) {
				txaObs.requestFocus();
			}
		}
	}
	
	public void beforeInsert( InsertEvent ievt ) {

	}
	
	public void afterInsert( InsertEvent ievt ) {
	
		if ( ievt.getListaCampos() == lcCampos ) {

			if ( "S".equals( daocontato.getPrefs()[ CONT_PREFS.USACTOSEQ.ordinal() ] ) ) {
				txtCodCont.setVlrInteger( daocontato.testaCodPK( "TKCONTATO" ) );
			}
		}
	}


	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAtiv.setConexao( cn );
		lcAtivFK.setConexao( cn );
		lcGrupFK.setConexao( cn );
		lcVend.setConexao( cn );
		lcSetor.setConexao( cn );
		lcUF.setConexao( cn );
		lcMunic.setConexao( cn );
		lcPais.setConexao( cn );
		lcTipoCli.setConexao( cn );
		lcOrigCont.setConexao( cn );
		lcGrupo.setConexao( cn );
		lcTipoCont.setConexao( cn );
		
		daocontato = new DAOContato( cn );
		try {
			daocontato.setPrefs( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "SGPREFERE3" ) );
			if (daocontato.getPrefs()[CONT_PREFS.LAYOUTFICHAAVAL.ordinal()]==null || "".equals( daocontato.getPrefs()[CONT_PREFS.LAYOUTFICHAAVAL.ordinal()].toString().trim() ) ) {
				btFichaAval.setEnabled( false );
			} else {
				btFichaAval.setEnabled( true );
			}

		} catch (SQLException e) {
			Funcoes.mensagemErro( this, "Erro carregando preferências !\b" + e.getMessage() );
		}

	}


	public void exec( int codCto ) {

		txtCodCont.setVlrInteger( codCto );
		lcCampos.carregaDados();
	}

}
