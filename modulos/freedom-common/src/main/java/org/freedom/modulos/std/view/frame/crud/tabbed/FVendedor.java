/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FVendedor.java <BR>
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
 *                    Tela de cadastro de comissionados (vendedores).
 * 
 */

package org.freedom.modulos.std.view.frame.crud.tabbed;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
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
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;
import org.freedom.modulos.gms.view.frame.crud.plain.FSecaoProd;
import org.freedom.modulos.grh.view.frame.crud.plain.FFuncao;
import org.freedom.modulos.std.view.dialog.report.DLRVendedor;
import org.freedom.modulos.std.view.frame.crud.plain.FCLComis;
import org.freedom.modulos.std.view.frame.crud.plain.FSetor;
import org.freedom.modulos.std.view.frame.crud.plain.FTipoVend;

public class FVendedor extends FTabDados implements PostListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinComiss = new JPanelPad( 470, 360 );

	private JPanelPad pinAss = new JPanelPad( 470, 360 );

	private JPanelPad pnObs = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTextAreaPad txaObs = new JTextAreaPad();

	private JScrollPane spnObs = new JScrollPane( txaObs );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtNomeVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCpfVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtRgVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtCnpjVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 18, 0 );

	private JTextFieldPad txtInscVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtEndVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtBairVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtComplVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCidVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtDDDFoneVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFoneVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtDDDCelVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtCelVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtDDDFaxVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFaxVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmailVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtPercComVend = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 7, 3 );

	private JTextFieldPad txtCodFornVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodFunc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFunc = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodClComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescClComis = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSSPVend = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNumConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtVlrAbono = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtVlrDesconto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecFin );

	private PainelImagem imgAssOrc = new PainelImagem( 65000 );

	private JCheckBoxPad cbAtivo = new JCheckBoxPad( "Ativo", "S", "N" );

	private ListaCampos lcPlan = new ListaCampos( this, "PN" );

	private ListaCampos lcSetor = new ListaCampos( this, "SE" );

	private ListaCampos lcClComis = new ListaCampos( this, "CM" );

	private ListaCampos lcFuncao = new ListaCampos( this, "FU" );

	private ListaCampos lcTipoComis = new ListaCampos( this, "TV" );

	private ListaCampos lcConta = new ListaCampos( this, "CA" );

	private JButtonPad btBuscaEnd = new JButtonPad( Icone.novo( "btBuscacep.png" ) );

	private Map<String, Object> bPref = null;
	
	private ListaCampos lcSecao = new ListaCampos( this, "SC" );
	
	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );
	
	private JTextFieldFK txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );


	public FVendedor() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de comissionados" );
		setAtribos( 50, 10, 440, 690 );

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, txtDescPlan, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descriçao do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.setWhereAdic( "TIPOPLAN = 'D'" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setQueryCommit( false );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );

		lcFuncao.add( new GuardaCampo( txtCodFunc, "CodFunc", "Cód.função", ListaCampos.DB_PK, txtDescFunc, false ) );
		lcFuncao.add( new GuardaCampo( txtDescFunc, "DescFunc", "Descriçao da função", ListaCampos.DB_SI, false ) );

		txtCodFunc.setTabelaExterna( lcFuncao, FFuncao.class.getCanonicalName() );
		txtCodFunc.setNomeCampo( "codfunc" );
		txtCodFunc.setFK( true );
		lcFuncao.setQueryCommit( false );
		lcFuncao.setReadOnly( true );
		lcFuncao.montaSql( false, "FUNCAO", "RH" );

		lcClComis.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.cl.comis.", ListaCampos.DB_PK, txtDescClComis, true ) );
		lcClComis.add( new GuardaCampo( txtDescClComis, "DescClComis", "Descriçao da classificação da comissão", ListaCampos.DB_SI, false ) );
		lcClComis.montaSql( false, "CLCOMIS", "VD" );
		lcClComis.setQueryCommit( false );
		lcClComis.setReadOnly( true );
		txtCodClComis.setTabelaExterna( lcClComis, FCLComis.class.getCanonicalName() );

		lcTipoComis.add( new GuardaCampo( txtCodTipoVend, "CodTipoVend", "Cód.tp.comis.", ListaCampos.DB_PK, txtDescTipoVend, true ) );
		lcTipoComis.add( new GuardaCampo( txtDescTipoVend, "DescTipoVend", "Descrição tipo de venda", ListaCampos.DB_SI, false ) );

		txtCodTipoVend.setTabelaExterna( lcTipoComis, FTipoVend.class.getCanonicalName() );
		txtCodTipoVend.setNomeCampo( "CodTipoVend" );
		txtCodTipoVend.setFK( true );
		lcTipoComis.setQueryCommit( false );
		lcTipoComis.setReadOnly( true );
		lcTipoComis.montaSql( false, "TIPOVEND", "VD" );

		lcConta.add( new GuardaCampo( txtNumConta, "NumConta", "num.Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descriçao da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setQueryCommit( false );
		lcConta.setReadOnly( true );
		txtNumConta.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );
		
		//FK com a Secao de produção
		
		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		lcSecao.montaSql( false, "SECAO", "EQ" );
		lcSecao.setReadOnly( true );
		lcSecao.setQueryCommit( false );
		txtCodSecao.setTabelaExterna( lcSecao, FSecaoProd.class.getCanonicalName() );
	}

	private void montaTela() {

		setPainel( pinComiss );
		adicTab( "Comissionado", pinComiss );
		adicCampo( txtCodVend, 7, 20, 100, 20, "CodVend", "Cód.Comiss.", ListaCampos.DB_PK, true );
		adicCampo( txtNomeVend, 110, 20, 192, 20, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, true );
		adicDB( cbAtivo, 305, 20, 70, 20, "AtivoComis", "Ativo", true );
		adicCampo( txtCpfVend, 7, 60, 130, 20, "CpfVend", "CPF", ListaCampos.DB_SI, false );
		adicCampo( txtRgVend, 140, 60, 149, 20, "RgVend", "RG", ListaCampos.DB_SI, false );
		adicCampo( txtSSPVend, 292, 60, 80, 20, "SSPVend", "Orgão exp.", ListaCampos.DB_SI, false );
		adicCampo( txtCnpjVend, 7, 100, 180, 20, "CnpjVend", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtInscVend, 190, 100, 182, 20, "InscVend", "IE", ListaCampos.DB_SI, false );
		adicCampo( txtCepVend, 7, 140, 70, 20, "CepVend", "Cep", ListaCampos.DB_SI, false );
		adic( btBuscaEnd, 80, 140, 20, 20 );
		adicCampo( txtEndVend, 103, 140, 220, 20, "EndVend", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumVend, 326, 140, 47, 20, "NumVend", "Num.", ListaCampos.DB_SI, false );
		adicCampo( txtComplVend, 7, 180, 50, 20, "ComplVend", "Compl.", ListaCampos.DB_SI, false );
		adicCampo( txtBairVend, 60, 180, 120, 20, "BairVend", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCidVend, 183, 180, 150, 20, "CidVend", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtUFVend, 336, 180, 36, 20, "UFVend", "UF", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFoneVend, 7, 220, 40, 20, "DDDFoneVend", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneVend, 50, 220, 77, 20, "FoneVend", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFaxVend, 130, 220, 40, 20, "DDDFaxVend", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFaxVend, 177, 220, 77, 20, "FaxVend", "Fax", ListaCampos.DB_SI, false );
		adicCampo( txtDDDCelVend, 257, 220, 40, 20, "DDDCelVend", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtCelVend, 300, 220, 72, 20, "CelVend", "Cel", ListaCampos.DB_SI, false );
		adicCampo( txtEmailVend, 7, 260, 100, 20, "EmailVend", "E-Mail", ListaCampos.DB_SI, false );
		adicCampo( txtPercComVend, 110, 260, 50, 20, "PercComVend", "%Comis.", ListaCampos.DB_SI, true );
		
		adicCampo( txtVlrAbono, 163, 260, 60, 20, "VlrAbono", "Abono", ListaCampos.DB_SI, false );
		adicCampo( txtVlrDesconto, 226, 260, 60, 20, "VlrDesconto", "Desconto", ListaCampos.DB_SI, false );
		
		adicCampo( txtCodFornVend, 290, 260, 82, 20, "CodFornVend", "Cód.comis.for.", ListaCampos.DB_SI, false );
		adicCampo( txtCodPlan, 7, 300, 100, 20, "CodPlan", "Cód.plan.", ListaCampos.DB_FK, txtDescPlan, false );
		adicDescFK( txtDescPlan, 110, 300, 262, 20, "DescPlan", "Descrição do planejamento" );
		adicCampo( txtCodClComis, 7, 340, 100, 20, "CodClComis", "Cód.cl.comiss.", ListaCampos.DB_FK, txtDescClComis, false );
		adicDescFK( txtDescClComis, 110, 340, 262, 20, "DescClComis", "Descrição da Classificacao da comissão" );
		adicCampo( txtCodFunc, 7, 380, 100, 20, "CodFunc", "Cód.função", ListaCampos.DB_FK, txtDescFunc, false );
		adicDescFK( txtDescFunc, 110, 380, 262, 20, "DescFunc", "Descrição da função" );
		adicCampo( txtCodTipoVend, 7, 420, 100, 20, "CodTipoVend", "Cód.tp.Comiss.", ListaCampos.DB_FK, txtDescTipoVend, false );
		adicDescFK( txtDescTipoVend, 110, 420, 262, 20, "DescTipoVend", "Descrição do tipo de comissionado" );
		adicCampo( txtNumConta, 7, 460, 100, 20, "NumConta", "Cód.conta", ListaCampos.DB_FK, txtDescConta, false );
		adicDescFK( txtDescConta, 110, 460, 262, 20, "DescConta", "Descrição da conta" );
		
		adicCampo( txtCodSecao, 7, 500, 100, 20, "CodSecao", "Cód. seção", ListaCampos.DB_FK, txtDescSecao, false );
		adicDescFK( txtDescSecao, 110, 500, 262, 20, "DescSecao", "Descrição da seção" );


		if ( (Boolean) bPref.get( "BUSCACEP" ) ) {
			btBuscaEnd.setEnabled( true );
		}
		else {
			btBuscaEnd.setEnabled( false );
		}

		btBuscaEnd.addActionListener( this );
		btBuscaEnd.setToolTipText( "Busca Endereço a partir do CEP" );

		txtCpfVend.setMascara( JTextFieldPad.MC_CPF );
		txtCnpjVend.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepVend.setMascara( JTextFieldPad.MC_CEP );
		txtFoneVend.setMascara( JTextFieldPad.MC_FONE );
		txtCelVend.setMascara( JTextFieldPad.MC_CELULAR );
		txtFaxVend.setMascara( JTextFieldPad.MC_FONE );
		lcCampos.addPostListener( this );
		lcCampos.setQueryInsert( false );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		adicTab( "Observações", pnObs );
		adicDBLiv( txaObs, "ObsVend", "Observações", false );
		pnObs.add( spnObs );

		setPainel( pinAss );
		adicTab( "Assinatura", pinAss );
		adicDB( imgAssOrc, 15, 30, 340, 85, "ImgAssVend", "Assinatura ( 340 pixel X 85 pixel )", true );

		//setListaCampos( true, "VENDEDOR", "VD" );
		lcCampos.setQueryInsert( false );

		setImprimir( true );

	}

	private void montaSetor() {

/*		Rectangle rec = getBounds();
		rec.height += 40;
		setBounds( rec );
*/
		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, txtDescSetor, false ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descriçao do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setQueryCommit( false );
		lcSetor.setReadOnly( true );
		txtCodSetor.setTabelaExterna( lcSetor, FSetor.class.getCanonicalName() );

		setPainel( pinComiss );
		Rectangle rec = pinComiss.getBounds();
		rec.height += 40;
		pinComiss.setBounds( rec );

		adicCampo( txtCodSetor, 7, 540, 100, 20, "CodSetor", "Cód.setor", ListaCampos.DB_FK, txtDescSetor, false );
		adicDescFK( txtDescSetor, 110, 540, 262, 20, "DescSetor", "Descrição do setor" );
		lcSetor.setConexao( con );
	}

	private boolean ehSetorVend() {

		boolean bRet = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT SETORVENDA FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				String sVal = null;
				if ( ( sVal = rs.getString( "SetorVenda" ) ) != null ) {
					if ( "VA".indexOf( sVal ) >= 0 ) // Se tiver V ou A no sVal!
						bRet = true;
				}
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao verificar setor!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRet;

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

		if ( !"".equals( txtCepVend.getVlrString() ) ) {

			txtEndVend.setEnabled( false );
			txtComplVend.setEnabled( false );
			txtBairVend.setEnabled( false );
			txtCidVend.setEnabled( false );
			txtUFVend.setEnabled( false );
			// txtCodPais.setEnabled( false );
			// txtSiglaUF.setEnabled( false );
			// txtCodMun.setEnabled( false );
			txtDDDFoneVend.setEnabled( false );
			txtDDDFaxVend.setEnabled( false );
			txtDDDCelVend.setEnabled( false );

			Thread th = new Thread( new Runnable() {

				public void run() {

					try {
						WSCep cep = new WSCep();
						cep.setCon( con );
						cep.setCep( txtCepVend.getVlrString() );
						cep.busca();
						Endereco endereco = cep.getEndereco();

						txtEndVend.setVlrString( endereco.getTipo() + " " + endereco.getLogradouro() );
						txtComplVend.setVlrString( endereco.getComplemento() );
						txtBairVend.setVlrString( endereco.getBairro() );
						txtCidVend.setVlrString( endereco.getCidade() );
						txtUFVend.setVlrString( endereco.getSiglauf() );
						// txtCodPais.setVlrInteger( endereco.getCodpais() );
						// txtSiglaUF.setVlrString( endereco.getSiglauf() );
						// txtCodMun.setVlrString( endereco.getCodmunic() );

						// lcPais.carregaDados();
						// lcUF.carregaDados();
						// lcMunic.carregaDados();

						txtNumVend.requestFocus();
					} catch ( Exception e ) {
						Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
					} finally {
						txtEndVend.setEnabled( true );
						txtComplVend.setEnabled( true );
						txtBairVend.setEnabled( true );
						txtCidVend.setEnabled( true );
						txtUFVend.setEnabled( true );
						// txtCodPais.setEnabled( true );
						// txtSiglaUF.setEnabled( true );
						// txtCodMun.setEnabled( true );
						txtDDDFoneVend.setEnabled( true );
						txtDDDFaxVend.setEnabled( true );
						txtDDDCelVend.setEnabled( true );
					}
				}
			} );
			try {
				th.start();
			} catch ( Exception err ) {
				Funcoes.mensagemInforma( null, "Não foi encontrado o endereço para o CEP informado!" );
				txtCepVend.requestFocus();
			}
		}
		else {
			Funcoes.mensagemInforma( null, "Digite um CEP para busca!" );
			txtCepVend.requestFocus();
		}

	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "";
		String sWhere = "";
		String sValores[] = null;
		int linPag = 0;
		int iContaReg = 0;

		ImprimeOS imp = new ImprimeOS( "", con );
		DLRVendedor dl = new DLRVendedor( this, con );
		Vector<String> vFiltros = new Vector<String>();

		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		sValores = dl.getValores();
		dl.dispose();

		if ( sValores[ 1 ].length() > 0 ) {
			System.out.println( "CIDADE NO FILTRO:" + sValores[ 1 ] );
			sWhere += " AND VD.CIDVEND = '" + sValores[ 1 ] + "'";
			vFiltros.add( "CIDADE = " + sValores[ 1 ].trim() );
		}
		else
			System.out.println( "Cidade nula no filtro" );

		if ( sValores[ 2 ].length() > 0 ) {
			sWhere += " AND VD.CODCLCOMIS=" + sValores[ 2 ];
			vFiltros.add( "CLAS.COMISSÃO = " + sValores[ 2 ] );
		}
		if ( sValores[ 3 ].length() > 0 ) {
			sWhere += " AND VD.CODSETOR = " + sValores[ 3 ];
			vFiltros.add( "SETOR = " + sValores[ 3 ] );
		}
		if ( sValores[ 4 ].length() > 0 ) {
			sWhere += " AND VD.CODFUNC = " + sValores[ 4 ];
			vFiltros.add( "FUNÇÃO = " + sValores[ 4 ] );
		}

		sSQL = "select VD.CODVEND,VD.NOMEVEND,VD.DDDFONEVEND,VD.CIDVEND,VD.FONEVEND,VD.FAXVEND,VD.EMAILVEND,VD.PERCCOMVEND,VD.COMIPIVEND," + "VD.CELVEND,VD.CODSETOR," + "(SELECT SE.DESCSETOR FROM VDSETOR SE WHERE SE.CODEMP=VD.CODEMPSE AND SE.CODFILIAL=VD.CODFILIALSE AND SE.CODSETOR=VD.CODSETOR),"
				+ "VD.CODCLCOMIS," + "(SELECT CM.DESCCLCOMIS FROM VDCLCOMIS CM WHERE CM.CODEMP=VD.codempcm AND CM.CODFILIAL=VD.codfilialcm AND CM.codclcomis=VD.codclcomis)," + "VD.CODFUNC,"
				+ "(SELECT FU.DESCFUNC FROM RHFUNCAO FU WHERE FU.CODEMP=VD.codempfu AND FU.CODFILIAL=VD.codfilialfu AND FU.codfunc=VD.codfunc)" + "FROM vdvendedor VD " + "WHERE VD.CODEMP=? AND VD.CODFILIAL=? " + sWhere + " order by " + sValores[ 0 ];

		System.out.println( "sql é " + sSQL );
		try {

			linPag = imp.verifLinPag() - 1;
			imp.limpaPags();
			imp.setTitulo( "Relatório de Comissionados" );
			imp.addSubTitulo( "RELATÓRIO DE COMISSIONADOS" );
			imp.addSubTitulo( "Filtrado por:" );

			for ( int i = 0; i < vFiltros.size(); i++ ) {
				String sTmp = (String) vFiltros.elementAt( i );
				imp.addSubTitulo( sTmp );
			}

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
			rs = ps.executeQuery();

			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.montaCab();
					imp.impCab( 136, true );
					imp.say( imp.pRow(), 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" );
					imp.say( imp.pRow(), 4, "Código" );
					imp.say( imp.pRow(), 12, "|" );
					imp.say( imp.pRow(), 14, "Nome:" );
					imp.say( imp.pRow(), 46, "|" );
					imp.say( imp.pRow(), 48, "Setor:" );
					imp.say( imp.pRow(), 58, "|" );
					imp.say( imp.pRow(), 60, "Fução:" );
					imp.say( imp.pRow(), 70, "|" );
					imp.say( imp.pRow(), 72, "Cl.comis.:" );
					imp.say( imp.pRow(), 84, "|" );
					imp.say( imp.pRow(), 92, "Fone:" );
					imp.say( imp.pRow(), 106, "|" );
					imp.say( imp.pRow(), 108, "Cidade:" );
					imp.say( imp.pRow(), 135, "|" );
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "|" + StringFunctions.replicate( "-", 133 ) + "|" );
				}

				imp.say( imp.pRow() + 1, 0, imp.comprimido() );
				imp.say( imp.pRow(), 0, "|" );
				imp.say( imp.pRow(), 4, rs.getString( "CodVend" ) );
				imp.say( imp.pRow(), 12, "|" );
				imp.say( imp.pRow(), 14, rs.getString( "NomeVend" ) != null ? rs.getString( "NomeVend" ).substring( 0, 30 ) : "" );
				imp.say( imp.pRow(), 46, "|" );
				imp.say( imp.pRow(), 48, rs.getString( "CodSetor" ) != null ? rs.getString( "CodSetor" ) : "" );
				imp.say( imp.pRow(), 58, "|" );
				imp.say( imp.pRow(), 60, rs.getString( "CodFunc" ) != null ? rs.getString( "CodFunc" ) : "" );
				imp.say( imp.pRow(), 70, "|" );
				imp.say( imp.pRow(), 72, rs.getString( "CodClComis" ) != null ? rs.getString( "CodClComis" ) : "" );
				imp.say( imp.pRow(), 84, "|" );
				imp.say( imp.pRow(), 86, rs.getString( "DDDFoneVend" ) != null ? "(" + rs.getString( "DDDFoneVend" ) + ")" : "" );
				imp.say( imp.pRow(), 92, rs.getString( "FoneVend" ) != null ? Funcoes.setMascara( rs.getString( "FoneVend" ).trim(), "####-####" ) : "" );
				imp.say( imp.pRow(), 106, "|" );
				imp.say( imp.pRow(), 108, rs.getString( "CidVend" ) != null ? rs.getString( "CidVend" ).substring( 0, 19 ) : "" );
				imp.say( imp.pRow(), 135, "|" );

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
					imp.incPags();
					imp.eject();
				}
				iContaReg++;
			}
			imp.say( imp.pRow() + 1, 0, imp.comprimido() );
			imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
			imp.eject();

			imp.fechaGravacao();
			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de clientes!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			sWhere = null;
			sValores = null;
			dl = null;
			vFiltros = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		bPref = getPrefere();

		montaTela();

		if ( ehSetorVend() ) {
			montaSetor();
		}
		
		lcClComis.setConexao( cn );
		lcPlan.setConexao( cn );
		lcFuncao.setConexao( cn );
		lcTipoComis.setConexao( cn );
		lcConta.setConexao( cn );
		lcSecao.setConexao( cn );
		
		setListaCampos( true, "VENDEDOR", "VD" );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		else if ( evt.getSource() == btBuscaEnd ) {
			buscaEndereco();
		}
		super.actionPerformed( evt );
	}

	public void beforePost( PostEvent pevt ) {
		if( pevt.getListaCampos() == lcCampos ) {
			
			if ( !"".equals( txtEmailVend.getVlrString().trim() ) && !Funcoes.validaEmail( txtEmailVend.getVlrString().trim() ) ){
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Endereço de e-mail inválido !\nO registro não foi salvo. ! ! !" );
				return;
			}			
			if ( txtInscVend.getText().trim().length() < 1 ) {
				if ( Funcoes.mensagemConfirma( this, "Inscrição Estadual em branco! Inserir ISENTO?" ) == JOptionPane.OK_OPTION ){
					txtInscVend.setVlrString( "ISENTO" );
				}
				pevt.cancela();
				txtInscVend.requestFocus();
			}
			else if ( txtInscVend.getText().trim().toUpperCase().compareTo( "ISENTO" ) == 0 ){
				return;
			}
			else if ( txtUFVend.getText().trim().length() < 2 ) {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Campo UF é requerido! ! !" );
				txtUFVend.requestFocus();
			}
			else if ( Funcoes.validaIE( txtInscVend.getVlrString(), txtUFVend.getVlrString() ) ) {
				txtInscVend.setVlrString( Funcoes.formataIE( txtInscVend.getText(), txtUFVend.getText() ) );
			} else  {
				pevt.cancela();
				Funcoes.mensagemInforma( this, "Inscrição Estadual Inválida ! ! !" );
				txtInscVend.requestFocus();
			}
		}
	}
}
