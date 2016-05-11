/**
 * @version 03/01/2005 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe:
 * @(#)FConveniado.java <BR>
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
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.atd.view.frame.crud.tabbed;

import java.awt.BorderLayout;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import javax.swing.JScrollPane;

import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JGroupField;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.atd.view.frame.crud.plain.FAtribuicao;
import org.freedom.modulos.atd.view.frame.crud.plain.FEncaminhador;
import org.freedom.modulos.atd.view.frame.crud.plain.FTipoConv;
import org.freedom.modulos.cfg.view.frame.crud.plain.FGrauInst;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FConveniado extends FTabDados implements PostListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinGeral = new JPanelPad( 650, 520 );

	private JPanelPad pinInfo = new JPanelPad( 650, 520 );

	private JPanelPad pnAtrib = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtNomeConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRgConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCPFConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtIdentificConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDtNascConv = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtEndConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtComplConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtBairConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDddConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtFoneConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtCelConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFaxConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmailConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCNSConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtCodAtrib = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldFK txtDescAtrib = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodConvAtrib = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodEnc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeEnc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodFunc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFunc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodGrauInst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescGrauInst = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtPaiConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtMaeConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRGPaiConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtRGMaeConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtICard = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtNVia = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTablePad tabAtrib = new JTablePad();

	private JScrollPane spnAtrib = new JScrollPane( tabAtrib );

	private JTextAreaPad txaAtrib = new JTextAreaPad();

	private JPanelPad pinRodAtrib = new JPanelPad( 650, 250 );

	private Navegador navAtrib = new Navegador( true );

	private ListaCampos lcTipoConv = new ListaCampos( this, "TC" );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );

	private ListaCampos lcAtrib = new ListaCampos( this, "AB" );

	private ListaCampos lcConvAtrib = new ListaCampos( this, "" );

	private ListaCampos lcGrauInst = new ListaCampos( this, "GI" );

	private ListaCampos lcAtend = new ListaCampos( this, "AE" );

	private ListaCampos lcEnc = new ListaCampos( this, "EC" );

	private ListaCampos lcFunc = new ListaCampos( this, "FC" );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	private JComboBoxPad cbSexo = null;

	private JGroupField gfCamp = new JGroupField();

	private Object[] oPrefs;

	public FConveniado() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Conveniados" );
		setAtribos( 50, 10, 530, 450 );

		lcCampos.addPostListener( this );

		lcConvAtrib.setMaster( lcCampos );
		lcCampos.adicDetalhe( lcConvAtrib );
		lcConvAtrib.setTabela( tabAtrib );

		lcTipoConv.add( new GuardaCampo( txtCodTipoConv, "CodTpConv", "Cód.tp.conv.", ListaCampos.DB_PK, false ) );
		lcTipoConv.add( new GuardaCampo( txtDescTipoConv, "DescTpConv", "Descriçao do tipo de conveniado", ListaCampos.DB_SI, false ) );
		lcTipoConv.montaSql( false, "TIPOCONV", "AT" );
		lcTipoConv.setQueryCommit( false );
		lcTipoConv.setReadOnly( true );
		txtCodTipoConv.setTabelaExterna( lcTipoConv, FTipoConv.class.getCanonicalName() );

		lcGrauInst.add( new GuardaCampo( txtCodGrauInst, "CodGri", "Cód.gri.", ListaCampos.DB_PK, false ) );
		lcGrauInst.add( new GuardaCampo( txtDescGrauInst, "DescGri", "Descriçao do grau de instrução", ListaCampos.DB_SI, false ) );
		lcGrauInst.montaSql( false, "GRAUINST", "SG" );
		lcGrauInst.setQueryCommit( false );
		lcGrauInst.setReadOnly( true );
		txtCodGrauInst.setTabelaExterna( lcGrauInst, FGrauInst.class.getCanonicalName() );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtDescCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setQueryCommit( false );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );

		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ) );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome do atendente", ListaCampos.DB_SI, false ) );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setQueryCommit( false );
		lcAtend.setReadOnly( true );
		txtCodAtend.setTabelaExterna( lcAtend, FAtendente.class.getCanonicalName() );

		lcEnc.add( new GuardaCampo( txtCodEnc, "CodEnc", "Cód.enc.", ListaCampos.DB_PK, false ) );
		lcEnc.add( new GuardaCampo( txtNomeEnc, "NomeEnc", "Descrição do encaminhador", ListaCampos.DB_SI, false ) );
		lcEnc.montaSql( false, "ENCAMINHADOR", "AT" );
		lcEnc.setQueryCommit( false );
		lcEnc.setReadOnly( true );
		txtCodEnc.setTabelaExterna( lcEnc, FEncaminhador.class.getCanonicalName() );

		lcFunc.add( new GuardaCampo( txtCodFunc, "CodFunc", "Cód.funç.", ListaCampos.DB_PK, false ) );
		lcFunc.add( new GuardaCampo( txtDescFunc, "DescFunc", "Descrição da função", ListaCampos.DB_SI, false ) );
		lcFunc.montaSql( false, "FUNCAO", "RH" );
		lcFunc.setQueryCommit( false );
		lcFunc.setReadOnly( true );
		txtCodFunc.setTabelaExterna( lcFunc, FFuncao.class.getCanonicalName() );

		vVals.add( "" );
		vVals.add( "M" );
		vVals.add( "F" );
		vLabs.add( "<--Selecione-->" );
		vLabs.add( "Masculino" );
		vLabs.add( "Feminino" );
		cbSexo = new JComboBoxPad( vLabs, vVals, JTextFieldPad.TP_STRING, 1, 0 );

		setPainel( pinGeral );
		adicTab( "Geral", pinGeral );

		adicCampo( txtCodConv, 7, 20, 70, 20, "CodConv", "Cód.conv.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeConv, 80, 20, 277, 20, "NomeConv", "Nome do conveniado", ListaCampos.DB_SI, true );
		adicDB( cbSexo, 360, 18, 140, 24, "SexoConv", "Sexo", true );
		adicCampo( txtDtNascConv, 7, 60, 120, 20, "DtNascConv", "Data de nascimento", ListaCampos.DB_SI, true );
		adicCampo( txtRgConv, 130, 60, 117, 20, "RgConv", "RG", ListaCampos.DB_SI, true );
		adicCampo( txtCPFConv, 250, 60, 117, 20, "CPFConv", "CPF", ListaCampos.DB_SI, true );
		adicCampo( txtIdentificConv, 370, 60, 130, 20, "IdentificConv", " Nº.Identificação", ListaCampos.DB_SI, false );
		adicCampo( txtEndConv, 7, 100, 250, 20, "EndConv", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumConv, 260, 100, 57, 20, "NumConv", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplConv, 320, 100, 47, 20, "ComplConv", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtCepConv, 370, 100, 130, 20, "CepConv", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtBairConv, 7, 140, 120, 20, "BairConv", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidConv, 130, 140, 117, 20, "CidConv", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUFConv, 250, 140, 37, 20, "UFConv", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtDddConv, 290, 140, 25, 20, "DddConv", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneConv, 318, 140, 90, 20, "FoneConv", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxConv, 410, 140, 90, 20, "Faxconv", "Fax", ListaCampos.DB_SI, false );
		adicCampo( txtCelConv, 7, 180, 90, 20, "CelConv", "Cel", ListaCampos.DB_SI, false );
		adicCampo( txtCodGrauInst, 100, 180, 97, 20, "CodGri", "Cód.gr.inst.", ListaCampos.DB_FK, txtDescGrauInst, false );
		adicDescFK( txtDescGrauInst, 200, 180, 300, 20, "DescGri", "Descrição do grau de instrução" );
		adicCampo( txtCodTipoConv, 7, 220, 100, 20, "CodTpConv", "Códtp.conv.", ListaCampos.DB_FK, txtDescTipoConv, true );
		adicDescFK( txtDescTipoConv, 110, 220, 390, 20, "DescTpConv", "Descrição do tipo de conveniado" );
		adicCampo( txtCodCli, 7, 260, 100, 20, "CodCli", "Cód.cli.", ListaCampos.DB_FK, txtDescCli, true );
		adicDescFK( txtDescCli, 110, 260, 390, 20, "DescCli", "Razão social do cliente" );
		adicCampo( txtEmailConv, 7, 300, 180, 20, "EmailConv", "E-Mail", ListaCampos.DB_SI, false );
		adicCampo( txtCNSConv, 190, 300, 137, 20, "CnsConv", "CNS-Sus", ListaCampos.DB_SI, false );
		adicCampo( txtNVia, 330, 300, 47, 20, "NRVIACONV", "Nº Via", ListaCampos.DB_SI, false );
		adicCampo( txtICard, 380, 300, 120, 20, "ICARDCONV", "Matricula", ListaCampos.DB_SI, false );

		// txtRgConv.setMascara( JTextFieldPad.MC_RG );
		txtCPFConv.setMascara( JTextFieldPad.MC_CPF );
		txtCepConv.setMascara( JTextFieldPad.MC_CEP );
		txtFoneConv.setMascara( JTextFieldPad.MC_FONE );
		txtCelConv.setMascara( JTextFieldPad.MC_CELULAR );
		txtFaxConv.setMascara( JTextFieldPad.MC_FONE );

		// Informações complementares

		setPainel( pinInfo );
		adicTab( "Inf. complementares", pinInfo );
		adicCampo( txtCodAtend, 7, 20, 100, 20, "CodAtend", "Cód.atend.", ListaCampos.DB_FK, txtNomeAtend, true );
		adicDescFK( txtNomeAtend, 110, 20, 390, 20, "NomeAtend", "Descrição do atendente" );
		adicCampo( txtCodEnc, 7, 60, 100, 20, "CodEnc", "Cód.enc.", ListaCampos.DB_FK, false );
		adicDescFK( txtNomeEnc, 110, 60, 390, 20, "NomeEnc", "Descrição do encaminhador" );
		adicCampo( txtPaiConv, 7, 100, 240, 20, "PaiConv", "Pai", ListaCampos.DB_SI, false );
		adicCampo( txtMaeConv, 250, 100, 250, 20, "MaeConv", "Mãe", ListaCampos.DB_SI, false );
		adicCampo( txtRGPaiConv, 7, 140, 150, 20, "RGPaiConv", "RG Pai", ListaCampos.DB_SI, false );
		adicCampo( txtRGMaeConv, 160, 140, 147, 20, "RGMaeConv", "RG Mãe", ListaCampos.DB_SI, false );
		adicCampo( txtCodFunc, 7, 180, 100, 20, "CodFunc", "Cód.função", ListaCampos.DB_FK, false );
		adicDescFK( txtDescFunc, 110, 180, 390, 20, "DescFunc", "Descrição da função" );

		setListaCampos( true, "CONVENIADO", "AT" );

		// Atribuições

		setPainel( pinRodAtrib, pnAtrib );
		adicTab( "Atribuições", pnAtrib );
		setListaCampos( lcConvAtrib );
		setNavegador( navAtrib );
		pnAtrib.add( pinRodAtrib, BorderLayout.SOUTH );
		pnAtrib.add( spnAtrib, BorderLayout.CENTER );

		pinRodAtrib.adic( navAtrib, 0, 222, 270, 25 );

		txtCodAtrib.setTipo( JTextFieldPad.TP_STRING, 15, 0 );
		lcAtrib.add( new GuardaCampo( txtCodAtrib, "CodAtrib", "Cód.atrib.", ListaCampos.DB_PK, false ) );
		lcAtrib.add( new GuardaCampo( txtDescAtrib, "DescAtrib", "Descrição das atribuições", ListaCampos.DB_SI, false ) );
		lcAtrib.montaSql( false, "ATRIBUICAO", "AT" );
		lcAtrib.setQueryCommit( false );
		lcAtrib.setReadOnly( true );
		txtDescAtrib.setListaCampos( lcAtrib );
		txtCodAtrib.setTabelaExterna( lcAtrib, FAtribuicao.class.getCanonicalName() );

		adicCampo( txtCodConvAtrib, 7, 20, 80, 20, "SeqConvAtrib", "Nºseq.", ListaCampos.DB_PK, true );
		adicCampo( txtCodAtrib, 90, 20, 77, 20, "CodAtrib", "Cód.atrib.", ListaCampos.DB_FK, txtDescAtrib, false );
		adicDescFK( txtDescAtrib, 170, 20, 197, 20, "DescAtrib", "Descrição da atribuição." );
		adicDB( txaAtrib, 7, 60, 360, 60, "ObsConvAtrib", "Obs.", false );
		adic( gfCamp, 7, 130, 360, 80 );
		setListaCampos( true, "CONVATRIB", "AT" );
		lcConvAtrib.setQueryInsert( false );
		lcConvAtrib.setQueryCommit( false );
		lcConvAtrib.montaTab();
		tabAtrib.setTamColuna( 65, 0 );
		tabAtrib.setTamColuna( 80, 1 );
		tabAtrib.setTamColuna( 150, 2 );

		lcCampos.setQueryInsert( false );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTipoConv.setConexao( cn );
		lcAtrib.setConexao( cn );
		lcCli.setConexao( cn );
		lcConvAtrib.setConexao( cn );
		lcGrauInst.setConexao( cn );
		lcAtend.setConexao( cn );
		lcEnc.setConexao( cn );
		lcFunc.setConexao( cn );

		gfCamp.setCampos( "ATCONVATRIBTB", "CodConv", "SeqConvAtrib", lcConvAtrib, cn );

		oPrefs = prefs();
	}

	public void setCodcli( String sCodcli, String sRazCli ) {

		txtCodCli.setVlrString( sCodcli );
		txtDescCli.setVlrString( sRazCli );
		lcCampos.post();
	}

	public void beforePost( PostEvent pevt ) {

		Integer iCodTipoCli = null;
		Integer iCodClasCli = null;
		if ( txtCodCli.getText().equals( "" ) ) {
			if ( Funcoes.mensagemConfirma( this, "Cliente não selecionado!\nDeseja cadastrar?" ) == JOptionPane.YES_OPTION ) {
				if ( oPrefs != null ) {
					iCodTipoCli = (Integer) oPrefs[ 0 ];
					iCodClasCli = (Integer) oPrefs[ 1 ];
				}
				pevt.cancela();
				FCliente tela;
				if ( Aplicativo.telaPrincipal.temTela( "Clientes" ) == false ) {
					tela = new FCliente();
					Aplicativo.telaPrincipal.criatela( "Clientes", tela, con );
				}
				else {
					tela = (FCliente) Aplicativo.telaPrincipal.getTela( "Clientes" );
				}
				tela.setConveniado( this );
				tela.lcCampos.cancel( false );
				tela.lcCampos.insert( true );
				tela.setVlrConveniado( txtNomeConv.getVlrString(), txtNomeConv.getVlrString(), txtEndConv.getVlrString(), txtNumConv.getVlrInteger(), txtComplConv.getVlrString(), txtBairConv.getVlrString(), txtCidConv.getVlrString(), txtCepConv.getVlrString(), txtUFConv.getVlrString(), txtRgConv
						.getVlrString(), txtCPFConv.getVlrString(), txtFoneConv.getVlrString(), txtFaxConv.getVlrString(), txtEmailConv.getVlrString(), iCodTipoCli, iCodClasCli );

			}
		}
	}

	private Object[] prefs() {

		Object[] bRetorno = new Object[ 2 ];
		String sSQL = "SELECT CODTIPOCLI,CODCLASCLI FROM SGPREFERE2 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE2" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				bRetorno[ 0 ] = new Integer( rs.getInt( "CODTIPOCLI" ) );
				bRetorno[ 1 ] = new Integer( rs.getInt( "CODCLASCLI" ) );
			}
			// rs.close();
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela SGPREFERE2!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

}
