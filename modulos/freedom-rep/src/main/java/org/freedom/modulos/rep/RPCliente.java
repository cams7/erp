/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)RPCliente.java <BR>
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
 *                    Tela para cadastro de clientes.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;

public class RPCliente extends FTabDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelCliente = new JPanelPad();

	private final JPanelPad panelEntrega = new JPanelPad();

	private final JPanelPad panelCobranca = new JPanelPad();

	private final JPanelPad panelVenda = new JPanelPad();

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private final JTextFieldPad txtCodCliCP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtRazCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldFK txtRazCliCP = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private final JTextFieldPad txtNomeCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextFieldPad txtCnpjCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private final JTextFieldPad txtInscCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldPad txtEndCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	// private final JTextFieldPad txtNumCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtCidCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtUFCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtDDDCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtFoneCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtFaxCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtEmailCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtEndEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	// private final JTextFieldPad txtNumEnt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtCidEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtUFEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCnpjEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private final JTextFieldPad txtInscEnt = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldPad txtEndCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	// private final JTextFieldPad txtNumCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtCidCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtUFCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 45, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 45, 0 );

	private final JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 45, 0 );

	private final JTextFieldPad txtFatLucr = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 2 );

	private final JTextFieldPad txtContatoCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private final JTextFieldPad txtCelContatoCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private final JCheckBoxPad cbAtivo = new JCheckBoxPad( "Cliente Ativo", "S", "N" );

	private final JButtonPad btCopiarEnt = new JButtonPad( "copiar endereço", Icone.novo( "btReset.png" ) );

	private final JButtonPad btCopiarCob = new JButtonPad( "copiar endereço", Icone.novo( "btReset.png" ) );

	private final ListaCampos lcTipoCli = new ListaCampos( this, "TC" );
	
	private final ListaCampos lcClienteCP = new ListaCampos( this, "CP" );
	
	private final ListaCampos lcVend = new ListaCampos( this, "VO" );

	private final ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	public RPCliente() {

		super( false );
		setTitulo( "Cadastro de tipos de clientes" );
		setAtribos( 50, 50, 450, 530 );

		montaListaCampos();

		montaTela();
		setListaCampos( true, "CLIENTE", "RP" );

		txtCnpjCli.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepCli.setMascara( JTextFieldPad.MC_CEP );
		txtFoneCli.setMascara( JTextFieldPad.MC_FONE );
		txtCelContatoCli.setMascara( JTextFieldPad.MC_CELULAR );
		txtFaxCli.setMascara( JTextFieldPad.MC_FONE );
		txtCepEnt.setMascara( JTextFieldPad.MC_CEP );
		txtCepCob.setMascara( JTextFieldPad.MC_CEP );
		txtCnpjEnt.setMascara( JTextFieldPad.MC_CNPJ );

		btCopiarEnt.addActionListener( this );
		btCopiarCob.addActionListener( this );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		setImprimir( true );
		
		nav.setNavigation( true );
	}

	private void montaListaCampos() {

		/********************
		 * TIPO CLIENTE *
		 ********************/

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, true ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		lcTipoCli.montaSql( false, "TIPOCLI", "RP" );
		lcTipoCli.setQueryCommit( false );
		lcTipoCli.setReadOnly( true );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, null );
		

		/********************
		 * TIPO PRINCIPAL   *
		 ********************/

		lcClienteCP.add( new GuardaCampo( txtCodCliCP, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcClienteCP.add( new GuardaCampo( txtRazCliCP, "RazCli", "Razão do cliente", ListaCampos.DB_SI, false ) );
		lcClienteCP.montaSql( false, "CLIENTE", "RP" );
		lcClienteCP.setQueryCommit( false );
		lcClienteCP.setReadOnly( true );
		txtCodCliCP.setTabelaExterna( lcClienteCP, null );
		txtCodCliCP.setNomeCampo( "codcli" );

		/********************
		 * VENDEDOR *
		 ********************/

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.vend.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "RP" );
		lcVend.setQueryCommit( false );
		lcVend.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVend, null );

		/**********************
		 * PLANO DE PAGAMENTO *
		 **********************/

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "RP" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
	}

	private void montaTela() {

		/*******************
		 * CLIENTE *
		 *******************/

		adicTab( "Cliente", panelCliente );
		setPainel( panelCliente );

		adicCampo( txtCodCli, 7, 30, 100, 20, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true );
		adicCampo( txtRazCli, 110, 30, 300, 20, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, true );

		adicCampo( txtNomeCli, 7, 70, 403, 20, "NomeCli", "Nome do fantazia", ListaCampos.DB_SI, true );

		adicCampo( txtCnpjCli, 7, 110, 200, 20, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtInscCli, 210, 110, 200, 20, "InscCli", "Inscrição", ListaCampos.DB_SI, false );

		adicCampo( txtEndCli, 7, 150, 403, 20, "EndCli", "Endereço", ListaCampos.DB_SI, false );

		adicCampo( txtCidCli, 7, 190, 132, 20, "CidCli", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairCli, 142, 190, 132, 20, "BairCli", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepCli, 277, 190, 80, 20, "CepCli", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFCli, 360, 190, 50, 20, "EstCli", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtDDDCli, 7, 230, 52, 20, "DDDCli", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneCli, 62, 230, 172, 20, "FoneCli", "Fone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxCli, 237, 230, 172, 20, "FaxCli", "Fax", ListaCampos.DB_SI, false );

		adicCampo( txtEmailCli, 7, 270, 403, 20, "EmailCli", "E-mail", ListaCampos.DB_SI, false );

		adicCampo( txtContatoCli, 7, 310, 403, 20, "ContatoCli", "Contato", ListaCampos.DB_SI, false );

		adicCampo( txtCelContatoCli, 7, 350, 172, 20, "CelContatoCli", "Celular", ListaCampos.DB_SI, false );

		
		/*******************
		 * ENTREGA *
		 *******************/

		adicTab( "Entrega", panelEntrega );
		setPainel( panelEntrega );

		adicCampo( txtEndEnt, 7, 30, 403, 20, "EndEntCli", "Endereço", ListaCampos.DB_SI, false );

		adicCampo( txtCidEnt, 7, 70, 132, 20, "CidEntCli", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairEnt, 142, 70, 132, 20, "BairEntCli", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepEnt, 277, 70, 80, 20, "CepEntCli", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFEnt, 360, 70, 50, 20, "EstEntCli", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtCnpjEnt, 7, 110, 200, 20, "CnpjEntCli", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtInscEnt, 210, 110, 200, 20, "InscEntCli", "Inscrição", ListaCampos.DB_SI, false );

		adic( btCopiarEnt, 7, 150, 200, 30 );

		/*******************
		 * COBRANÇA *
		 *******************/

		adicTab( "Cobrança", panelCobranca );
		setPainel( panelCobranca );

		adicCampo( txtEndCob, 7, 30, 403, 20, "EndCobCli", "Endereço", ListaCampos.DB_SI, false );

		adicCampo( txtCidCob, 7, 70, 132, 20, "CidCobCli", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairCob, 142, 70, 132, 20, "BairCobCli", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepCob, 277, 70, 80, 20, "CepCobCli", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFCob, 360, 70, 50, 20, "EstCobCli", "UF", ListaCampos.DB_SI, false );

		adic( btCopiarCob, 7, 150, 200, 30 );

		/*******************
		 * VENDAS *
		 *******************/

		adicTab( "Vendas", panelVenda );
		setPainel( panelVenda );

		JPanelPad pnComercial = new JPanelPad();
		pnComercial.setBorder( SwingParams.getPanelLabel( "Comercial", Color.BLUE ) );

		JPanelPad pnAtividade = new JPanelPad();
		pnAtividade.setBorder( SwingParams.getPanelLabel( "Atividade", Color.BLUE ) );

		adic( pnComercial, 0, 0, 403, 200 );
	
		setPainel( pnComercial );

		adicCampo( txtCodTipoCli, 	7, 		20, 	90, 	20, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_FK, txtDescTipoCli, true );
		adicDescFK( txtDescTipoCli, 100,	20, 	290, 	20, "DescTipoCli", "Descrição do tipo de cliente" );

		adicCampo( txtCodVend, 		7, 		60, 	90, 	20, "CodVend", "Cód.vend.", ListaCampos.DB_FK, txtNomeVend, true );
		adicDescFK( txtNomeVend, 	100, 	60, 	290, 	20, "NomeVend", "Nome do vendedor" );

		adicCampo( txtCodPlanoPag, 	7, 		100, 	90, 	20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag, false );
		adicDescFK( txtDescPlanoPag, 100, 	100, 	290, 	20, "DescPlanoPag", "Descrição do plano de pagamento" );
		
		adicCampo( txtCodCliCP, 	 7, 	140, 	90, 	20, "CodCliCP", "Cód.Cli.", ListaCampos.DB_FK, txtRazCliCP, false );
		adicDescFK( txtRazCliCP, 	100, 	140, 	290, 	20, "RazCli", "Razão do cliente principal" );
		
		txtCodCliCP.setNomeCampo( "codcli" ); 
		
		setPainel( panelVenda );
		adic( pnAtividade, 0, 210, 403, 100 );

		setPainel( pnAtividade );
		
		adicDB( cbAtivo, 0, 0, 300, 20, "AtivCli", "", true );

		adicCampo( txtFatLucr, 0, 40, 200, 20, "FatLucr", "Fator de lucratividade", ListaCampos.DB_SI, false );
	}

	private void copiarEndereco( final String arg ) {

		String endereco = txtEndCli.getVlrString();
		String cidade = txtCidCli.getVlrString();
		String bairro = txtBairCli.getVlrString();
		String cep = txtCepCli.getVlrString();
		String estado = txtUFCli.getVlrString();
		String cnpj = txtCnpjCli.getVlrString();
		String insc = txtInscCli.getVlrString();

		if ( "entrega".equalsIgnoreCase( arg ) ) {

			txtEndEnt.setVlrString( endereco );
			txtCidEnt.setVlrString( cidade );
			txtBairEnt.setVlrString( bairro );
			txtCepEnt.setVlrString( cep );
			txtUFEnt.setVlrString( estado );
			txtCnpjEnt.setVlrString( cnpj );
			txtInscEnt.setVlrString( insc );
		}
		else if ( "cobrança".equalsIgnoreCase( arg ) ) {

			txtEndCob.setVlrString( endereco );
			txtCidCob.setVlrString( cidade );
			txtBairCob.setVlrString( bairro );
			txtCepCob.setVlrString( cep );
			txtUFCob.setVlrString( estado );
		}
	}

	private void imprimir( TYPE_PRINT view ) {

		if ( txtCodCli.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione o cliente." );
			return;
		}

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT C.CODEMP,C.CODFILIAL,C.CODCLI," );
			sql.append( "C.RAZCLI,C.NOMECLI,C.CNPJCLI,C.INSCCLI," );
			sql.append( "C.ENDCLI,C.CIDCLI,C.ESTCLI,C.CEPCLI,C.BAIRCLI," );
			sql.append( "C.DDDCLI,C.FONECLI,C.FAXCLI,C.EMAILCLI," );
			sql.append( "C.ENDCOBCLI,C.BAIRCOBCLI,C.CIDCOBCLI,C.ESTCOBCLI,C.CEPCOBCLI," );
			sql.append( "C.ENDENTCLI,C.BAIRENTCLI,C.CIDENTCLI,C.CEPENTCLI,C.ESTENTCLI," );
			sql.append( "C.INSCENTCLI,C.CNPJENTCLI,C.ATIVCLI," );
			sql.append( "T.DESCTIPOCLI, V.NOMEVEND, P.DESCPLANOPAG, C.CONTATOCLI " );
			sql.append( "FROM RPCLIENTE C " );
			sql.append( "LEFT OUTER JOIN RPTIPOCLI T ON T.CODEMP=C.CODEMPTC AND T.CODFILIAL=C.CODFILIALTC AND T.CODTIPOCLI=C.CODTIPOCLI " );
			sql.append( "LEFT OUTER JOIN RPVENDEDOR V ON V.CODEMP=C.CODEMPVO AND V.CODFILIAL=C.CODFILIALVO AND V.CODVEND=C.CODVEND " );
			sql.append( "LEFT OUTER JOIN RPPLANOPAG P ON P.CODEMP=C.CODEMPPG AND P.CODFILIAL=C.CODFILIALPG AND P.CODPLANOPAG=C.CODPLANOPAG " );
			sql.append( "WHERE C.CODEMP=? AND C.CODFILIAL=? AND C.CODCLI=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPCLIENTE" ) );
			ps.setInt( 3, txtCodCli.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/rpcliente.jasper", "CLIENTE - " + txtCodCli.getVlrInteger() + " - " + txtNomeCli.getVlrString(), null, rs, hParam, this );

			if ( view==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btCopiarEnt ) {
			copiarEndereco( "entrega" );
		}
		else if ( e.getSource() == btCopiarCob ) {
			copiarEndereco( "cobrança" );
		}
		else if ( e.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( e.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}

		super.actionPerformed( e );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcTipoCli.setConexao( cn );
		lcVend.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		lcClienteCP.setConexao( cn );
		
	}
}
