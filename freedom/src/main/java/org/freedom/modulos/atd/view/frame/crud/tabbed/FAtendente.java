/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FAtendente.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.atd.view.frame.crud.tabbed;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.business.webservice.WSCep;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.Endereco;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.modulos.atd.view.frame.crud.plain.FTipoAtend;
import org.freedom.modulos.cfg.view.frame.crud.tabbed.FUsuario;
import org.freedom.modulos.grh.view.frame.crud.tabbed.FEmpregado;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;

public class FAtendente extends FTabDados implements CarregaListener, PostListener, InsertListener, CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinGeral = new JPanelPad( 650, 520 );
	
	private JPanelPad pinAcesso = new JPanelPad( 650, 520 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNomeAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRgAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCpfAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldPad txtIdentificAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 18, 0 );

	private JTextFieldPad txtEndAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtBairAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCidAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFoneAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtCelAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFaxAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmailAtend = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodTipoAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtIDUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcTipoAtend = new ListaCampos( this, "TA" );

	private JTextFieldPad txtMatEmpr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeEmpr = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtMetaAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 15, 5 );
	
	private JCheckBoxPad cbPartPremiAtend = new JCheckBoxPad( "Premiações", "S", "N" );

	private JCheckBoxPad cbAcesAtdoLerOut = new JCheckBoxPad( "Ler lançamentos de outros atendentes.", "S", "N" );
	
	private JCheckBoxPad cbAcesAtdoAltOut = new JCheckBoxPad( "Alterar lançamentos de outros atendentes.", "S", "N" );

	private JCheckBoxPad cbAcesAtdoDelLan = new JCheckBoxPad( "Excluir lançamentos próprios.", "S", "N" );
	
	private JCheckBoxPad cbAcesAtdoDelOut =  new JCheckBoxPad( "Excluir lançamentos de outros atendentes.", "S", "N" );

	private JCheckBoxPad cbAcesRelEstOut = new JCheckBoxPad( "Emitir relatório de outros atendentes.", "S", "N" );
	
	private JCheckBoxPad cbAcesTroComis = new JCheckBoxPad( "Trocar próprio comissionado.", "S", "N" );
	
	private JCheckBoxPad cbAcesTroComisOut = new JCheckBoxPad( "Trocar comissionado de outros atendentes.", "S", "N" );

	private ListaCampos lcUsu = new ListaCampos( this, "US" );

	private ListaCampos lcVend = new ListaCampos( this, "VE" );

	private ListaCampos lcEmpregado = new ListaCampos( this, "EP" );

	private JButtonPad btBuscaEnd = new JButtonPad( Icone.novo( "btBuscacep.png" ) );

	private Map<String, Object> bPref = null;

	public FAtendente() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Atendentes" );
		setAtribos( 20, 20, 530, 560 );

		lcTipoAtend.add( new GuardaCampo( txtCodTipoAtend, "CodTpAtend", "Cód.tp.atend.", ListaCampos.DB_PK, false ), "txtCodVendx" );
		lcTipoAtend.add( new GuardaCampo( txtDescTipoAtend, "DescTpAtend", "Descriçao do tipo de atendente", ListaCampos.DB_SI, false ), "txtCodVendx" );
		lcTipoAtend.montaSql( false, "TIPOATEND", "AT" );
		lcTipoAtend.setQueryCommit( false );
		lcTipoAtend.setReadOnly( true );
		txtCodTipoAtend.setTabelaExterna( lcTipoAtend, FTipoAtend.class.getCanonicalName() );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comis.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVend, FVendedor.class.getCanonicalName() );

		lcUsu.add( new GuardaCampo( txtIDUsu, "IdUsu", "ID", ListaCampos.DB_PK, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "NomeUsu", "Nome do usuário", ListaCampos.DB_SI, false ) );
		lcUsu.montaSql( false, "USUARIO", "SG" );
		lcUsu.setQueryCommit( false );
		lcUsu.setReadOnly( true );
		txtIDUsu.setTabelaExterna( lcUsu, FUsuario.class.getCanonicalName() );

		lcEmpregado.add( new GuardaCampo( txtMatEmpr, "MatEmpr", "Matrícula.", ListaCampos.DB_PK, false ) );
		lcEmpregado.add( new GuardaCampo( txtNomeEmpr, "NomeEmpr", "Nome do empregado", ListaCampos.DB_SI, false ) );
		lcEmpregado.montaSql( false, "EMPREGADO", "RH" );
		lcEmpregado.setQueryCommit( false );
		lcEmpregado.setReadOnly( true );
		txtMatEmpr.setTabelaExterna( lcEmpregado, FEmpregado.class.getCanonicalName() );

	}

	private void montaTela() {

		setPainel( pinGeral );
		adicTab( "Geral", pinGeral );

		adicCampo( txtCodAtend, 7, 20, 80, 20, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeAtend, 90, 20, 370, 20, "NomeAtend", "Nome do atendente", ListaCampos.DB_SI, true );
		adicCampo( txtCpfAtend, 7, 60, 150, 20, "CpfAtend", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtIdentificAtend, 160, 60, 150, 20, "IdentificAtend", "Identificação", ListaCampos.DB_SI, false );
		adicCampo( txtRgAtend, 313, 60, 149, 20, "RgAtend", "RG", ListaCampos.DB_SI, false );
		adicCampo( txtCepAtend, 7, 100, 68, 20, "CepAtend", "Cep", ListaCampos.DB_SI, false );
		adic( btBuscaEnd, 78, 100, 20, 20 );
		adicCampo( txtEndAtend, 101, 100, 295, 20, "EndAtend", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumAtend, 398, 100, 65, 20, "NumAtend", "Número", ListaCampos.DB_SI, false );
		adicCampo( txtBairAtend, 7, 140, 195, 20, "BairAtend", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidAtend, 205, 140, 195, 20, "CidAtend", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUFAtend, 403, 140, 59, 20, "UFAtend", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtFoneAtend, 7, 180, 150, 20, "FoneAtend", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxAtend, 160, 180, 150, 20, "FaxAtend", "Fax", ListaCampos.DB_SI, false );
		adicCampo( txtCelAtend, 313, 180, 149, 20, "CelAtend", "Cel", ListaCampos.DB_SI, false );
		adicCampo( txtCodTipoAtend, 7, 220, 100, 20, "CodTpAtend", "Cód.tp.atend.", ListaCampos.DB_FK, txtDescTipoAtend, true );
		adicDescFK( txtDescTipoAtend, 110, 220, 352, 20, "DescTpAtend", "Descrição do tipo de atendente" );
		adicCampo( txtIDUsu, 7, 260, 100, 20, "IdUsu", "ID", ListaCampos.DB_FK, txtNomeUsu, false );
		adicDescFK( txtNomeUsu, 110, 260, 352, 20, "NomeUsu", "Nome do usuário" );
		adicCampo( txtCodVend, 7, 300, 100, 20, "CodVend", "Cód.comis.", ListaCampos.DB_FK, txtDescVend, false );
		adicDescFK( txtDescVend, 110, 300, 352, 20, "NomeVend", "Nome do comissionado" );
		adicCampo( txtMatEmpr, 7, 340, 100, 20, "MatEmpr", "Matricula", ListaCampos.DB_FK, txtNomeEmpr, false );
		adicDescFK( txtNomeEmpr, 110, 340, 352, 20, "NomeEmpr", "Nome do empregado" );
		adicCampo( txtEmailAtend, 7, 380, 455, 20, "EmailAtend", "E-Mail", ListaCampos.DB_SI, false );
		adicCampo( txtMetaAtend, 7, 420, 100, 20, "METAATEND", "Meta", ListaCampos.DB_SI, true );
		adicDB( cbPartPremiAtend, 110, 420, 150, 20, "PartPremiAtend", "Participa", true );

		setPainel( pinAcesso );
		adicTab( "Acesso", pinAcesso );
		
		//pinAcesso.add( new BevelBorder( BevelBorder.LOWERED ), 7,3,400,5);
		adic(new JLabelPad("Acesso a tela de gestão de relacionamento com o cliente:"), 7, 10, 400, 20);
		adicDB( cbAcesAtdoLerOut, 7, 30, 400, 20, "acesatdolerout", "", true );
		adicDB( cbAcesAtdoAltOut, 7, 50, 400, 20, "acesatdoaltout", "", true );
		adicDB( cbAcesAtdoDelOut, 7, 70, 400, 20, "acesatdodelout", "", true );
		adicDB( cbAcesAtdoDelLan, 7, 90, 400, 20, "acesatdodellan", "", true );
		
		
		adic(new JLabelPad("Acesso aos relatórios estatísticos:"), 7, 110, 400, 20);
		adicDB( cbAcesRelEstOut, 7, 130, 400, 20, "acesrelestout", "", true );
		
		adic(new JLabelPad("Acesso a tela de cadastro de cliente:"), 7, 150, 400, 20);
		adicDB( cbAcesTroComis, 7, 170, 400, 20, "acestrocomis", "", true );
		adicDB( cbAcesTroComisOut, 7, 190, 400, 20, "acestrocomisout", "", true );

		txtRgAtend.setMascara( JTextFieldPad.MC_RG );
		txtCepAtend.setMascara( JTextFieldPad.MC_CEP );
		txtFoneAtend.setMascara( JTextFieldPad.MC_FONEDDD );
		txtCelAtend.setMascara( JTextFieldPad.MC_CELULAR );
		txtFaxAtend.setMascara( JTextFieldPad.MC_FONE );
		
		setListaCampos( true, "ATENDENTE", "AT" );
		lcCampos.setQueryInsert( false );

		if ( (Boolean) bPref.get( "BUSCACEP" ) ) {
			btBuscaEnd.setEnabled( true );
		}
		else {
			btBuscaEnd.setEnabled( false );
		}

		btBuscaEnd.addActionListener( this );
		btBuscaEnd.setToolTipText( "Busca Endereço a partir do CEP" );
		lcCampos.addPostListener( this );
		cbAcesAtdoLerOut.addCheckBoxListener( this );
		cbAcesAtdoDelLan.addCheckBoxListener( this );
		
	}

	private Map<String, Object> getPrefere() {

		Map<String, Object> retorno = new HashMap<String, Object>();
		StringBuilder sSQL = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sSQL.append( "SELECT BUSCACEP " );
			sSQL.append( "FROM SGPREFERE1 P  " );
			sSQL.append( "WHERE P.CODEMP=? AND P.CODFILIAL=?" );

			try {

				ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

				rs = ps.executeQuery();

				if ( rs.next() ) {

					retorno.put( "BUSCACEP", new Boolean( "S".equals( rs.getString( "BUSCACEP" ) ) ) );
				}

				rs.close();
				ps.close();
				con.commit();
			} catch ( SQLException err ) {

				Funcoes.mensagemErro( this, "Erro ao verificar preferências!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
		} finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
		return retorno;
	}

	private void buscaEndereco() {

		if ( !"".equals( txtCepAtend.getVlrString() ) ) {

			txtEndAtend.setEnabled( false );
			// txtComplVend.setEnabled( false );
			txtBairAtend.setEnabled( false );
			txtCidAtend.setEnabled( false );
			txtUFAtend.setEnabled( false );
			// txtCodPais.setEnabled( false );
			// txtSiglaUF.setEnabled( false );
			// txtCodMun.setEnabled( false );
			// txtDDDFoneVend.setEnabled( false );
			// txtDDDFaxVend.setEnabled( false );
			// txtDDDCelVend.setEnabled( false );

			Thread th = new Thread( new Runnable() {

				public void run() {

					try {
						WSCep cep = new WSCep();
						cep.setCon( con );
						cep.setCep( txtCepAtend.getVlrString() );
						cep.busca();
						Endereco endereco = cep.getEndereco();

						txtEndAtend.setVlrString( endereco.getTipo() + " " + endereco.getLogradouro() );
						// txtComplVend.setVlrString( endereco.getComplemento() );
						txtBairAtend.setVlrString( endereco.getBairro() );
						txtCidAtend.setVlrString( endereco.getCidade() );
						txtUFAtend.setVlrString( endereco.getSiglauf() );
						// txtCodPais.setVlrInteger( endereco.getCodpais() );
						// txtSiglaUF.setVlrString( endereco.getSiglauf() );
						// txtCodMun.setVlrString( endereco.getCodmunic() );

						// lcPais.carregaDados();
						// lcUF.carregaDados();
						// lcMunic.carregaDados();

						txtNumAtend.requestFocus();
					} catch ( Exception e ) {
						Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
					} finally {
						txtEndAtend.setEnabled( true );
						// txtComplAtend.setEnabled( true );
						txtBairAtend.setEnabled( true );
						txtCidAtend.setEnabled( true );
						txtUFAtend.setEnabled( true );
						// txtCodPais.setEnabled( true );
						// txtSiglaUF.setEnabled( true );
						// txtCodMun.setEnabled( true );
						// txtDDDFoneVend.setEnabled( true );
						// txtDDDFaxVend.setEnabled( true );
						// txtDDDCelVend.setEnabled( true );
					}
				}
			} );
			try {
				th.start();
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
				txtCepAtend.requestFocus();
			}
		}
		else {
			Funcoes.mensagemInforma( null, "Digite um CEP para busca!" );
			txtCepAtend.requestFocus();
		}

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btBuscaEnd ) {

			buscaEndereco();
		}
		super.actionPerformed( evt );
	}
	
	public void beforePost( PostEvent pevt ){ 
		if( pevt.getListaCampos() == lcCampos ) {
			
			if ( !"".equals( txtEmailAtend.getVlrString().trim() ) && !Funcoes.validaEmail( txtEmailAtend.getVlrString().trim() ) ){
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Endereço de e-mail inválido !\nO registro não foi salvo. ! ! !" );
				return;
			}
			
		}
	}

	public void afterPost(PostEvent pevt) { }

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		bPref = getPrefere();

		montaTela();

		lcTipoAtend.setConexao( cn );
		lcUsu.setConexao( cn );
		lcVend.setConexao( cn );
		lcEmpregado.setConexao( cn );
	}

	public void beforeInsert( InsertEvent ievt ) {

		
	}

	public void afterInsert( InsertEvent ievt ) {

		if (ievt.getListaCampos()==lcCampos) {
			cbAcesAtdoAltOut.setVlrString( "S" );
			cbAcesAtdoDelLan.setVlrString( "S" );
			cbAcesAtdoDelOut.setVlrString( "S" );
			cbAcesAtdoLerOut.setVlrString( "S" );
		}
		
	}

	public void valorAlterado( CheckBoxEvent evt ) {
		if (evt.getCheckBox() == cbAcesAtdoLerOut) {
			habilitaAcessoAltOut();
		} 
		
	}
	
	public void habilitaAcessoAltOut() {
		cbAcesAtdoAltOut.setEnabled("S".equals( cbAcesAtdoLerOut.getVlrString()));
		cbAcesAtdoDelOut.setEnabled("S".equals( cbAcesAtdoLerOut.getVlrString()));
		if ("S".equals(cbAcesAtdoAltOut.getVlrString()) && "N".equals( cbAcesAtdoLerOut.getVlrString()))  {
			cbAcesAtdoAltOut.setVlrString("N");	
		}
		
		if ("S".equals(cbAcesAtdoDelOut.getVlrString()) && "N".equals( cbAcesAtdoLerOut.getVlrString()))  {
			cbAcesAtdoDelOut.setVlrString("N");
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {
		if (cevt.getListaCampos() == lcCampos) {
			habilitaAcessoAltOut();
		}
	}
}
