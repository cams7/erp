/**
 * @version 05/12/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FSVV.java <BR>
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

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;

public class FSVV extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCliente = new JPanelPad( 0, 130 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatainiLote = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JProgressBar pbAnd = new JProgressBar();

	private JButtonPad btVisual = new JButtonPad( Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JCheckBoxPad cbEstoque = new JCheckBoxPad( "Estoque", "S", "N" );

	private int iAnd = 0;

	private JLabelPad lbAnd = new JLabelPad( "Aguardando." );

	private final String CRLF = "" + ( (char) 13 ) + ( (char) 10 );

	private final int TAM_GRUPO = 14;

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, TAM_GRUPO, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcGrup = new ListaCampos( this );

	ProcessoSec pSec = null;

	String sRelErros = "";

	String[] sInfoEmp = null;

	public FSVV() {

		super( false );
		setTitulo( "Exportar vendas" );
		setAtribos( 50, 50, 610, 450 );

		btVisual.setToolTipText( "Visualizar" );
		btGerar.setToolTipText( "Gerar" );

		btGerar.setEnabled( false );
		cbEstoque.setEnabled( false );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		adicBotaoSair();

		c.add( pinCliente, BorderLayout.NORTH );
		c.add( spnTab, BorderLayout.CENTER );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		pinCliente.adic( new JLabelPad( "Início" ), 7, 0, 100, 25 );
		pinCliente.adic( txtDataini, 7, 20, 100, 20 );
		pinCliente.adic( new JLabelPad( "Fim" ), 110, 0, 97, 25 );
		pinCliente.adic( txtDatafim, 110, 20, 100, 20 );
		pinCliente.adic( new JLabelPad( "Início sufixo" ), 223, 0, 97, 25 );
		pinCliente.adic( txtDatainiLote, 223, 20, 100, 20 );
		pinCliente.adic( btVisual, 333, 15, 30, 30 );
		pinCliente.adic( btGerar, 373, 15, 30, 30 );
		pinCliente.adic( cbEstoque, 413, 20, 100, 20 );

		pinCliente.adic( new JLabelPad( "Cód.Grup." ), 7, 40, 90, 20 );
		pinCliente.adic( txtCodGrup, 7, 60, 90, 20 );
		pinCliente.adic( new JLabelPad( "Descrição do grupo" ), 100, 40, 190, 20 );
		pinCliente.adic( txtDescGrup, 100, 60, 190, 20 );

		pinCliente.adic( lbAnd, 7, 100, 150, 20 );
		pinCliente.adic( pbAnd, 160, 100, 200, 20 );

		tab.adicColuna( "Tipo mov." ); // 0
		tab.adicColuna( "Série" ); // 1
		tab.adicColuna( "Doc" ); // 2
		tab.adicColuna( "Emissao" ); // 3
		tab.adicColuna( "Cód.vend." ); // 4
		tab.adicColuna( "Cód.ítem." ); // 5
		tab.adicColuna( "Unid." ); // 6
		tab.adicColuna( "Lote." ); // 7
		tab.adicColuna( "Quant." ); // 8
		tab.adicColuna( "V.ítem." ); // 9
		tab.adicColuna( "Prazo médio." ); // 10
		tab.adicColuna( "Cat.cliente" ); // 11
		tab.adicColuna( "Cidade" ); // 12
		tab.adicColuna( "UF" ); // 13
		tab.adicColuna( "CEP" ); // 14
		tab.adicColuna( "Campanha" ); // 15
		tab.adicColuna( "Partic." ); // 16
		tab.adicColuna( "CNPJ/CPF" ); // 17
		tab.adicColuna( "Cod.cli." ); // 18
		tab.adicColuna( "Pessoa" ); // 19
		tab.adicColuna( "Razão social" ); // 20
		tab.adicColuna( "Endereço de entrega" ); // 21
		tab.adicColuna( "Bairro de entrega" ); // 22
		tab.adicColuna( "Endereço de cobrança" ); // 23
		tab.adicColuna( "Bairro de cobrança" ); // 24
		tab.adicColuna( "Cidade de cobrança" ); // 25
		tab.adicColuna( "Estado de cobrança" ); // 26
		tab.adicColuna( "CEP de cobrança" ); // 27
		tab.adicColuna( "E-mail" ); // 28
		tab.adicColuna( "Contato" ); // 29
		tab.adicColuna( "Telefone" ); // 30

		tab.setTamColuna( 70, 0 );
		tab.setTamColuna( 50, 1 );
		tab.setTamColuna( 50, 2 );
		tab.setTamColuna( 100, 3 );
		tab.setTamColuna( 80, 4 );
		tab.setTamColuna( 80, 5 );
		tab.setTamColuna( 50, 6 );
		tab.setTamColuna( 80, 7 );
		tab.setTamColuna( 60, 8 );
		tab.setTamColuna( 100, 9 );
		tab.setTamColuna( 100, 10 );
		tab.setTamColuna( 100, 11 );
		tab.setTamColuna( 200, 12 );
		tab.setTamColuna( 50, 13 );
		tab.setTamColuna( 90, 14 );
		tab.setTamColuna( 120, 15 );
		tab.setTamColuna( 70, 16 );
		tab.setTamColuna( 110, 17 );
		tab.setTamColuna( 80, 18 );
		tab.setTamColuna( 100, 19 );
		tab.setTamColuna( 200, 20 );
		tab.setTamColuna( 200, 21 );
		tab.setTamColuna( 150, 22 );
		tab.setTamColuna( 200, 23 );
		tab.setTamColuna( 150, 24 );
		tab.setTamColuna( 200, 25 );
		tab.setTamColuna( 200, 26 );
		tab.setTamColuna( 120, 27 );
		tab.setTamColuna( 130, 28 );
		tab.setTamColuna( 150, 29 );
		tab.setTamColuna( 100, 30 );

		btVisual.addActionListener( this );
		btGerar.addActionListener( this );

		pbAnd.setStringPainted( true );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do gurpo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		lcGrup.setConexao( Aplicativo.telaPrincipal.getConexao() );

	}

	public void iniGerar() {

		pSec = new ProcessoSec( 500, new Processo() {

			public void run() {

				lbAnd.updateUI();
				pbAnd.updateUI();
			}
		}, new Processo() {

			public void run() {

				gerar();
				pSec.parar();
			}
		} );
		pSec.iniciar();
	}

	public void iniVisualiza() {

		pSec = new ProcessoSec( 500, new Processo() {

			public void run() {

				lbAnd.updateUI();
				pbAnd.updateUI();
			}
		}, new Processo() {

			public void run() {

				visualizar();
				pSec.parar();
			}
		} );
		pSec.iniciar();
	}

	private void visualizar() {

		String sTmp = "";
		Object[] oFatConv = null;
		String sCodUnid = "";
		float ftFatConv = 1;
		sRelErros = "";

		sInfoEmp = buscaInfoEmp();
		btGerar.setEnabled( false );
		cbEstoque.setEnabled( false );
		Date dtIniLoteDig = null;
		Date dtIniLote = null;
		String sSufixo = "";
		lbAnd.setText( "Buscando dados..." );
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}
		if ( !txtDatainiLote.getVlrString().trim().equals( "" ) ) {
			dtIniLoteDig = txtDatainiLote.getVlrDate();
		}
		try {
			String sSQL = "SELECT T.TIPOMOV, V.SERIE, V.DOCVENDA, V.DTEMITVENDA," + "VE.CODFORNVEND,P.CODFABPROD,IT.CODLOTE,SUM(IT.QTDITVENDA)," + "SUM(IT.VLRLIQITVENDA),SUM(" + "(SELECT SUM(IT2.VLRLIQITVENDA*(PARC.PERCPAG/100)*PARC.DIASPAG)"
					+ " FROM VDVENDA V2, VDITVENDA IT2, FNPARCPAG PARC WHERE " + " V2.TIPOVENDA=V.TIPOVENDA AND V2.CODVENDA=V.CODVENDA" + " AND V2.CODEMP=V.CODEMP AND V2.CODFILIAL=V.CODFILIAL" + " AND IT2.TIPOVENDA=IT.TIPOVENDA AND IT2.CODVENDA=IT.CODVENDA"
					+ " AND IT2.CODITVENDA = IT.CODITVENDA AND IT2.CODEMP=V2.CODEMP" + " AND IT2.CODFILIAL=V2.CODFILIAL AND PARC.CODPLANOPAG=V2.CODPLANOPAG" + " AND PARC.CODEMP=V2.CODEMPPG AND PARC.CODFILIAL=V2.CODFILIALPG))," 
					+ "C.CODTIPOCLI,cast(coalesce(mn.nomemunic,c.CIDCLI) as char(80)) cidcli,coalesce(c.siglauf, C.UFCLI) ufcli,"
					+ "C.CEPCLI,C.CNPJCLI,C.CPFCLI,C.CODCLI,C.PESSOACLI,C.RAZCLI,C.ENDCLI," + "C.NUMCLI,C.COMPLCLI,C.BAIRCLI,C.ENDENT,C.NUMENT,C.COMPLENT," + "C.BAIRENT,C.ENDCOB,C.NUMCOB,C.COMPLCOB,C.BAIRCOB," + "C.CIDCOB,C.UFCOB,C.CEPCOB,C.EMAILCLI,C.CONTCLI,C.FONECLI,"
					+ "T.ESTIPOMOV,V.CODVENDA,IT.CODITVENDA,IT.CODPROD," + " MIN((SELECT DINILOTE FROM EQLOTE L" + " WHERE L.CODPROD=IT.CODPROD AND L.CODLOTE=IT.CODLOTE AND" + " L.CODEMP=IT.CODEMPLE AND L.CODFILIAL=IT.CODFILIALLE))" +

					" FROM VDVENDA V, VDITVENDA IT, VDCLIENTE C2,EQPRODUTO P, " 

					+ " EQTIPOMOV T, SGPREFERE1 PF, VDVENDEDOR VE, VDCLIENTE C " 
					
					+ " LEFT OUTER JOIN SGMUNICIPIO MN ON MN.CODPAIS=C.CODPAIS AND MN.SIGLAUF=C.SIGLAUF AND MN.CODMUNIC=C.CODMUNIC "

					+ " WHERE V.CODEMP=? AND V.CODFILIAL=? AND V.DTEMITVENDA BETWEEN ? AND ? AND " 

					+ " SUBSTRING(V.STATUSVENDA FROM 1 FOR 1) != 'C' AND " 

					+ " IT.CODVENDA=V.CODVENDA AND IT.CODEMP=V.CODEMP AND IT.CODFILIAL=V.CODFILIAL AND C2.CODCLI=V.CODCLI" + " AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND" + " C2.CODPESQ=C.CODCLI AND C2.CODFILIAL=C.CODFILIAL AND" + " C2.CODEMP=C.CODEMP AND VE.CODVEND=V.CODVEND AND"
					+ " VE.CODEMP=V.CODEMPVD AND VE.CODFILIAL=V.CODFILIALVD AND" + " P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND" + " P.CVPROD IN ('V','A') AND P.TIPOPROD='P' AND" + " P.CODFILIAL=IT.CODFILIALPD AND T.CODTIPOMOV=V.CODTIPOMOV AND"
					+ " T.CODEMP=V.CODEMPTM AND T.CODFILIAL=V.CODFILIALTM AND T.ESTOQTIPOMOV='S' AND " + " ((P.CODMARCA=PF.CODMARCA AND P.CODEMPMC=PF.CODEMPMC AND" + " P.CODFILIALMC=PF.CODFILIALMC) OR PF.CODMARCA IS NULL) AND" + " ((P.CODGRUP=PF.CODGRUP AND P.CODEMPGP=PF.CODEMPGP AND"
					+ " P.CODFILIALGP=PF.CODFILIALGP) OR PF.CODGRUP IS NULL)" + " AND PF.CODEMP=? AND PF.CODFILIAL=? ";

			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				sSQL = sSQL + " AND P.CODEMPGP=? AND P.CODFILIALGP=? AND P.CODGRUP=? ";
			}

			sSQL = sSQL + " GROUP BY " 
						+ "T.TIPOMOV, V.SERIE, V.DOCVENDA, V.DTEMITVENDA,VE.CODFORNVEND, P.CODFABPROD,IT.CODLOTE,C.CODTIPOCLI,C.CIDCLI, mn.nomemunic, C.UFCLI, c.siglauf, " 
						+ "C.CEPCLI,C.CNPJCLI,C.CPFCLI,C.CODCLI,C.PESSOACLI,C.RAZCLI,C.ENDCLI,C.NUMCLI,C.COMPLCLI,C.BAIRCLI,C.ENDENT,C.NUMENT,"
						+ "C.COMPLENT,C.BAIRENT,C.ENDCOB,C.NUMCOB,C.COMPLCOB,C.BAIRCOB," 
						+ "C.CIDCOB,C.UFCOB,C.CEPCOB,C.EMAILCLI,C.CONTCLI,C.FONECLI,T.ESTIPOMOV,V.CODVENDA,IT.CODITVENDA,IT.CODPROD ";

			sSQL = sSQL + "UNION ALL " 
					
					+ "SELECT T.TIPOMOV, C.SERIE, C.DOCCOMPRA, C.DTEMITCOMPRA," + "CAST(NULL AS CHAR(13)),P.CODFABPROD,IT.CODLOTE,SUM(IT.QTDITCOMPRA)," 
					
					+ "SUM(IT.VLRLIQITCOMPRA),SUM(" 
					
					+ "(SELECT SUM(IT2.VLRLIQITCOMPRA*(PARC.PERCPAG/100)*PARC.DIASPAG) FROM CPCOMPRA C2, CPITCOMPRA IT2, FNPARCPAG PARC WHERE C2.CODCOMPRA=C.CODCOMPRA" + " AND C2.CODEMP=C.CODEMP AND C2.CODFILIAL=C.CODFILIAL" 
					+ " AND IT2.CODCOMPRA=IT.CODCOMPRA" + " AND IT2.CODITCOMPRA = IT.CODITCOMPRA AND IT2.CODEMP=C2.CODEMP AND IT2.CODFILIAL=C2.CODFILIAL AND PARC.CODPLANOPAG=C2.CODPLANOPAG" 					
					+ " AND PARC.CODEMP=C2.CODEMPPG AND PARC.CODFILIAL=C2.CODFILIALPG))," 
					
					+ "4,cast(coalesce(mn.nomemunic,F.CIDFOR) as char(80)) cidfor,coalesce(f.siglauf,F.UFFOR) uffor, F.CEPFOR,F.CNPJFOR,F.CPFFOR,F.CODFOR,F.PESSOAFOR,F.RAZFOR,F.ENDFOR,F.NUMFOR,F.COMPLFOR,F.BAIRFOR,F.ENDFOR,F.NUMFOR,F.COMPLFOR," 
					+ "F.BAIRFOR,F.ENDFOR,F.NUMFOR,F.COMPLFOR,F.BAIRFOR,F.CIDFOR,F.UFFOR,F.CEPFOR,F.EMAILFOR,F.CONTFOR,F.FONEFOR,T.ESTIPOMOV,C.CODCOMPRA,IT.CODITCOMPRA,IT.CODPROD," 
					
					+ " MIN((SELECT DINILOTE FROM EQLOTE L WHERE L.CODPROD=IT.CODPROD AND L.CODLOTE=IT.CODLOTE AND L.CODEMP=IT.CODEMPLE AND L.CODFILIAL=IT.CODFILIALLE))" 
					
					+ " FROM CPCOMPRA C, CPITCOMPRA IT, EQPRODUTO P, EQTIPOMOV T, SGPREFERE1 PF, CPFORNECED F "
					
					+ " LEFT OUTER JOIN SGMUNICIPIO MN ON MN.CODPAIS=F.CODPAIS AND MN.SIGLAUF=F.SIGLAUF AND MN.CODMUNIC=F.CODMUNIC "
					
					+ "WHERE C.CODEMP=? AND C.CODFILIAL=? AND"					
					
					+ " C.DTEMITCOMPRA BETWEEN ? AND ? AND IT.CODCOMPRA=C.CODCOMPRA AND IT.CODEMP=C.CODEMP AND IT.CODFILIAL=C.CODFILIAL AND F.CODFOR=C.CODFOR AND F.CODFILIAL=C.CODFILIALFR AND" 
					+ " F.CODEMP=C.CODEMPFR AND P.CODPROD=IT.CODPROD AND P.CODEMP=IT.CODEMPPD AND P.CVPROD IN ('V','A') AND P.TIPOPROD='P' AND P.CODFILIAL=IT.CODFILIALPD AND T.CODTIPOMOV=c.CODTIPOMOV AND" 
					+ " T.CODEMP=C.CODEMPTM AND T.CODFILIAL=C.CODFILIALTM AND T.ESTOQTIPOMOV='S' AND  ((P.CODMARCA=PF.CODMARCA AND P.CODEMPMC=PF.CODEMPMC AND "
					+ " P.CODFILIALMC=PF.CODFILIALMC) OR PF.CODMARCA IS NULL) AND ((P.CODGRUP=PF.CODGRUP AND P.CODEMPGP=PF.CODEMPGP AND P.CODFILIALGP=PF.CODFILIALGP) OR PF.CODGRUP IS NULL) AND" 
					+ " NOT (C.CODFOR=PF.CODFOR AND C.CODEMPFR=PF.CODEMPFR AND  C.CODFILIALFR=PF.CODFILIALFR)"
					+ " AND PF.CODEMP=? AND PF.CODFILIAL=? ";

			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				sSQL = sSQL + " AND P.CODEMPGP=? AND P.CODFILIALGP=? AND P.CODGRUP=? ";
			}

			sSQL = sSQL + " GROUP BY " 
						+ " T.TIPOMOV, C.SERIE, C.DOCCOMPRA, C.DTEMITCOMPRA, P.CODFABPROD,IT.CODLOTE,F.CIDFOR,F.UFFOR," 
						+ "F.CEPFOR,F.CNPJFOR,F.CPFFOR,F.CODFOR,F.PESSOAFOR,F.RAZFOR,F.ENDFOR,F.NUMFOR,F.COMPLFOR,F.BAIRFOR,F.ENDFOR,F.NUMFOR,"
						+ "F.COMPLFOR,F.BAIRFOR,F.ENDFOR,F.NUMFOR,F.COMPLFOR,F.BAIRFOR,F.CIDFOR,F.UFFOR,mn.nomemunic, f.siglauf, F.CEPFOR,F.EMAILFOR,F.CONTFOR,F.FONEFOR," 
						+ "T.ESTIPOMOV,C.CODCOMPRA,IT.CODITCOMPRA,IT.CODPROD ";

			sSQL = sSQL + " ORDER BY 4,3";

			// System.out.println(sSQL);
			PreparedStatement ps = con.prepareStatement( sSQL );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );

			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				ps.setInt( param++, lcGrup.getCodEmp() );
				ps.setInt( param++, lcGrup.getCodFilial() );
				ps.setString( param++, txtCodGrup.getVlrString() );
			}

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );

			if ( !"".equals( txtCodGrup.getVlrString() ) ) {
				ps.setInt( param++, lcGrup.getCodEmp() );
				ps.setInt( param++, lcGrup.getCodFilial() );
				ps.setString( param++, txtCodGrup.getVlrString() );
			}

			ResultSet rs = ps.executeQuery();
			tab.limpa();
			for ( int i = 0; rs.next(); i++ ) {
				tab.adicLinha();
				String sIdent = "[Doc: " + rs.getInt( "DocVenda" ) + "]" + "[Item: " + rs.getInt( "CodItVenda" ) + "]";
				String sIdentP = sIdent + "[C.Produto: " + rs.getInt( "CodProd" ) + "]";
				String sIdentC = sIdent + "[C.Cliente/For: " + rs.getInt( "CodCli" ) + "]";
				lbAnd.setText( sIdent );
				String sESTipoMov = rs.getString( "ESTipoMov" );
				String sTipoMov = trataTipoMov( rs.getString( "TipoMov" ), sESTipoMov );
				tab.setValor( verifErro( 'A', sTipoMov, sIdent + "[Tipo Movimento]", null ), i, 0 );
				tab.setValor( Funcoes.copy( rs.getString( "Serie" ), 3 ).trim(), i, 1 );
				tab.setValor( Funcoes.copy( rs.getString( "DocVenda" ), 8 ).trim(), i, 2 );
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DtEmitVenda" ) ).replaceAll( "/", "" ), i, 3 );
				tab.setValor( verifErro( 'A', rs.getString( "CodFornVend" ), sIdent + "[C. Vendedor]", sTipoMov ), i, 4 );
				sSufixo = "";
				dtIniLote = null;
				if ( dtIniLoteDig != null ) {
					if ( rs.getDate( 42 ) != null )
						dtIniLote = new Date( rs.getDate( 42 ).getTime() );
					if ( dtIniLote != null ) {
						if ( dtIniLote.compareTo( dtIniLoteDig ) >= 0 ) {
							sSufixo = "P";
						}
					}
				}
				oFatConv = buscaUnid( rs.getInt( "CodProd" ) );
				sCodUnid = oFatConv[ 0 ].toString();
				ftFatConv = ( (Float) oFatConv[ 1 ] ).floatValue();
				tab.setValor( verifErro( 'B', rs.getString( "CodFabProd" ), sIdentP + "[C. Fabricante]", sTipoMov ) + sSufixo, i, 5 );
				tab.setValor( verifErro( 'B', Funcoes.copy( sCodUnid, 2 ).trim(), sIdentP + "[Unidade Conv]", "" ), i, 6 );
				tab.setValor( rs.getString( "CodLote" ) != null ? rs.getString( "CodLote" ).trim() : "", i, 7 );
				tab.setValor( Funcoes.strDecimalToStrCurrency( 14, 2, ( rs.getFloat( 8 ) * ftFatConv ) + "" ).trim(), i, 8 ); // A quantidade também precisa de ","
				tab.setValor( Funcoes.strDecimalToStrCurrency( 18, 2, rs.getString( 9 ) ).trim(), i, 9 );
				tab.setValor( "" + ( new BigDecimal( rs.getDouble( 10 ) / ( rs.getDouble( 9 ) != 0 ? rs.getDouble( 9 ) : 1 ) ) ).setScale( 0, BigDecimal.ROUND_HALF_UP ).intValue(), i, 10 );
				tab.setValor( verifErro( 'B', StringFunctions.strZero( rs.getString( "CodTipoCli" ), 2 ), sIdentC + "[Cat. Cliente]", null ), i, 11 );
				tab.setValor( verifErro( 'C', rs.getString( "CidCli" ), sIdentC + "[Cidade]", sTipoMov ), i, 12 );
				tab.setValor( verifErro( 'D', rs.getString( "UfCli" ), sIdentC + "[UF]", sTipoMov ), i, 13 );
				tab.setValor( verifErro( 'E', rs.getString( "CepCli" ), sIdentC + "[CEP]", sTipoMov ), i, 14 );
				String[] sCodigosCamp = buscaCliFor( rs.getInt( "CodCli" ) );
				String sCliFor = sCodigosCamp[ 0 ];
				String sCpCliFor = sCodigosCamp[ 1 ];
				// Campanha alterar aqui para 02
				tab.setValor( sCpCliFor, i, 15 );
				tab.setValor( sCliFor, i, 16 );
				tab.setValor( verifErro( 'B', ( rs.getString( "CnpjCli" ) != null ? rs.getString( "CnpjCli" ) : rs.getString( "CpfCli" ) ), sIdentC + "[CNPJ/CPF Cliente]", "" ), i, 17 );
				tab.setValor( rs.getString( "CodCli" ), i, 18 );
				tab.setValor( rs.getString( "PessoaCli" ), i, 19 );
				tab.setValor( Funcoes.copy( rs.getString( "RazCli" ), 40 ).trim(), i, 20 );
				// Impressao do end. ent.
				sTmp = rs.getString( "EndEnt" ) != null ? rs.getString( "EndEnt" ).trim() : ( rs.getString( "EndCli" ) != null ? rs.getString( "EndCli" ).trim() : "" );
				sTmp = sTmp.trim() + ( rs.getString( "NumEnt" ) != null ? ", " + rs.getString( "NumEnt" ) : ( rs.getString( "NumCli" ) != null ? ", " + rs.getString( "NumCli" ) : "" ) );
				sTmp = sTmp.trim() + ( rs.getString( "ComplEnt" ) != null ? " - " + rs.getString( "ComplEnt" ).trim() : ( rs.getString( "ComplCli" ) != null ? " - " + rs.getString( "ComplCli" ).trim() : "" ) );
				tab.setValor( verifErro( 'B', Funcoes.copy( sTmp.trim(), 40 ), sIdentC + "[Endereço]", "" ), i, 21 );
				sTmp = rs.getString( "BairEnt" ) != null ? rs.getString( "BairEnt" ).trim() : ( rs.getString( "BairCli" ) != null ? rs.getString( "BairCli" ).trim() : "" );
				tab.setValor( verifErro( 'B', sTmp, sIdentC + "[Bairro]", "" ), i, 22 );
				// Impressao do end. cob.
				sTmp = rs.getString( "EndCob" ) != null ? rs.getString( "EndCob" ) : ( rs.getString( "EndCli" ) != null ? rs.getString( "EndCli" ).trim() : "" );
				sTmp = sTmp.trim() + ( rs.getString( "NumCob" ) != null ? ", " + rs.getString( "NumCob" ) : ( rs.getString( "NumCli" ) != null ? ", " + rs.getString( "NumCli" ) : "" ) );
				sTmp = sTmp.trim() + ( rs.getString( "ComplCob" ) != null ? " - " + rs.getString( "ComplCob" ) : ( rs.getString( "ComplCli" ) != null ? " - " + rs.getString( "ComplCli" ) : "" ) );
				tab.setValor( Funcoes.copy( sTmp.trim(), 40 ), i, 23 );
				sTmp = rs.getString( "BairCob" ) != null ? rs.getString( "BairCob" ).trim() : ( rs.getString( "BairCli" ) != null ? rs.getString( "BairCli" ).trim() : "" );
				tab.setValor( sTmp, i, 24 );
				sTmp = rs.getString( "CidCob" ) != null ? rs.getString( "CidCob" ).trim() : ( rs.getString( "CidCli" ) != null ? rs.getString( "CidCli" ).trim() : "" );
				tab.setValor( sTmp, i, 25 );
				sTmp = rs.getString( "UfCob" ) != null ? rs.getString( "UfCob" ).trim() : ( rs.getString( "UfCli" ) != null ? rs.getString( "UfCli" ).trim() : "" );
				tab.setValor( sTmp, i, 26 );
				sTmp = rs.getString( "CepCob" ) != null ? rs.getString( "CepCob" ).trim() : ( rs.getString( "CepCli" ) != null ? rs.getString( "CepCli" ).trim() : "" );
				tab.setValor( sTmp, i, 27 );
				tab.setValor( rs.getString( "EmailCli" ) != null ? rs.getString( "EmailCli" ).trim() : "", i, 28 );

				String contcli = rs.getString( "ContCli" );

				if ( contcli != null ) {

					contcli = contcli.trim();

					Integer[] numeros = { contcli.indexOf( " " ), contcli.indexOf( "/" ), contcli.indexOf( "-" ) };

					Integer pos = Funcoes.getMenor( numeros, 1 );

					if ( pos == null || ( pos > 10 && contcli.length() > 10 ) ) {

						pos = 10;

					}
					else if ( pos != null && contcli.length() > pos && pos > 0 ) {

						contcli = contcli.substring( 0, pos );

					}

				}

				tab.setValor( contcli != null ? contcli.trim() : "", i, 29 );
				tab.setValor( rs.getString( "FoneCli" ) != null ? rs.getString( "FoneCli" ).trim() : "", i, 30 );
			}
			lbAnd.setText( "Pronto." );
			rs.close();
			ps.close();
			if ( sRelErros.equals( "" ) ) {
				btGerar.setEnabled( true );
				cbEstoque.setEnabled( true );
			}
			else
				Funcoes.criaTelaErro( sRelErros, this );
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar dados!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void gerar() {

		Object[] oFatConv = null;
		float ftFatConv = 1;
		String sCodUnid = "";
		iAnd = -1; // Para poder controlar o estoque tb.
		pbAnd.setMaximum( tab.getNumLinhas() );
		pbAnd.setValue( iAnd );
		lbAnd.setText( "Iniciando..." );
		File fArq = new File( "m" + StringFunctions.strZero( sInfoEmp[ 3 ], 6 ) + ".txt" );
		if ( fArq.exists() )
			fArq.delete();
		try {
			if ( fArq.createNewFile() ) {
				PrintStream pst = new PrintStream( new FileOutputStream( fArq ) );
				lbAnd.setText( "Gravando vendas..." );
				pst.print( "60;" + StringFunctions.strZero( sInfoEmp[ 3 ], 6 ) + ";" + Funcoes.dateToStrDate( new Date() ).replaceAll( "/", "" ) + ";" + txtDataini.getVlrString().replaceAll( "/", "" ) + ";" + txtDatafim.getVlrString().replaceAll( "/", "" ) + CRLF );
				for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
					Object[] oVals = tab.getLinha( i ).toArray();
					pst.print( "70" );
					for ( int j = 0; j <= 30; j++ ) {
						pst.print( ";" + oVals[ j ] );
					}
					pst.print( CRLF );
					pbAnd.setValue( ++iAnd );
				}
				if ( cbEstoque.getVlrString().equals( "S" ) ) {
					lbAnd.setText( "Buscando estoque..." );
					/*
					 * String sSQL = "SELECT P.CODFABPROD,P.CODPROD,MAX(MP.CODMOVPROD),"+ "(SELECT MP1.SLDMOVPROD FROM EQMOVPROD MP1 "+ "WHERE MP1.CODEMP=? AND MP1.CODFILIAL=? AND "+ "MP1.CODMOVPROD=MAX(MP.CODMOVPROD)) "+ "FROM EQMOVPROD MP, EQPRODUTO P,SGPREFERE1 PF "+
					 * "WHERE MP.CODEMP=? AND MP.CODFILIAL=? AND "+ "P.CVPROD IN ('V','A') AND P.TIPOPROD='P' AND " + "MP.DTMOVPROD<=? AND P.CODPROD=MP.CODPROD AND "+ "P.CODEMP=MP.CODEMPPD AND P.CODFILIAL=MP.CODFILIALPD AND "+ "((P.CODMARCA=PF.CODMARCA AND P.CODEMPMC=PF.CODEMPMC AND "+
					 * "P.CODFILIALMC=PF.CODFILIALMC) OR PF.CODMARCA IS NULL) AND "+ "((P.CODGRUP=PF.CODGRUP AND P.CODEMPGP=PF.CODEMPGP AND "+ "P.CODFILIALGP=PF.CODFILIALGP) OR PF.CODGRUP IS NULL) AND "+ "PF.CODEMP=? AND PF.CODFILIAL=? "+ "GROUP BY P.CODFABPROD,P.CODPROD";
					 */

					String sSQL = "SELECT P.CODFABPROD,P.CODPROD, E.SALDO " + "FROM EQRELINVPRODSP(?,?,'M',null,null,null,?,null,null,null) E, EQPRODUTO P, SGPREFERE1 PF " + "WHERE " + "P.CODEMP=? AND P.CODFILIAL=? AND P.CODPROD=E.CODPROD AND " + "P.CVPROD IN ('V','A') AND P.TIPOPROD='P' AND "
							+ "((P.CODMARCA=PF.CODMARCA AND P.CODEMPMC=PF.CODEMPMC AND " + "P.CODFILIALMC=PF.CODFILIALMC) OR PF.CODMARCA IS NULL) AND " + "((P.CODGRUP=PF.CODGRUP AND P.CODEMPGP=PF.CODEMPGP AND " + "P.CODFILIALGP=PF.CODFILIALGP) OR PF.CODGRUP IS NULL) "
							+ "and PF.CODEMP=? AND PF.CODFILIAL=? " + "and e.saldo>0 " + "ORDER BY 1";

					try {

						PreparedStatement ps = con.prepareStatement( sSQL );
						ps.setInt( 1, Aplicativo.iCodEmp );
						ps.setInt( 2, ListaCampos.getMasterFilial( "EQMOVPROD" ) );
						ps.setDate( 3, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

						ps.setInt( 4, Aplicativo.iCodEmp );
						ps.setInt( 5, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
						ps.setInt( 6, Aplicativo.iCodEmp );
						ps.setInt( 7, Aplicativo.iCodFilial );

						ResultSet rs = ps.executeQuery();
						lbAnd.setText( "Gravando estoque..." );
						String sIdent = "";
						sRelErros = "";
						while ( rs.next() ) {
							oFatConv = buscaUnid( rs.getInt( "CodProd" ) );
							sCodUnid = oFatConv[ 0 ].toString();
							ftFatConv = ( (Float) oFatConv[ 1 ] ).floatValue();
							pst.print( "80" );
							sIdent = "[Estoque][C.Produto: " + rs.getInt( "CodProd" ) + "]";
							pst.print( ";" + verifErro( 'B', rs.getString( "CodFabProd" ), sIdent + "[C. Fabricante]", "" ) );
							pst.print( ";" + verifErro( 'B', Funcoes.copy( sCodUnid, 2 ).trim(), sIdent + "[Unidade Conv]", "" ) );
							pst.print( ";" + Funcoes.strDecimalToStrCurrency( 14, 2, "" + ( rs.getFloat( 3 ) * ftFatConv ) ).trim() );
							pst.print( CRLF );
						}
						if ( !sRelErros.equals( "" ) )
							Funcoes.criaTelaErro( sRelErros, this );
						rs.close();
						ps.close();
						con.commit();
					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Erro ao buscar o estoque!\n" + err.getMessage(), true, con, err );
					}
				}
				pbAnd.setValue( ++iAnd );
				pst.flush();
				pst.close();
			}
		} catch ( IOException err ) {
			Funcoes.mensagemErro( this, "Erro ao gravar arquivo!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		lbAnd.setText( "Pronto." );
	}

	private String trataTipoMov( String sVal, String sES ) {

		String sRet = "";
		if ( sVal.equals( "VD" ) )
			sRet = "01";
		else if ( sVal.equals( "VE" ) )
			sRet = "02";
		else if ( sVal.equals( "VT" ) )
			sRet = "03";
		/*
		 * else if (sVal.equals("DV")) sRet = "04";
		 */
		else if ( sVal.equals( "CC" ) )
			sRet = "05";
		else if ( sVal.equals( "BN" ) && sES.equals( "S" ) )
			sRet = "06";
		else if ( sVal.equals( "PE" ) )
			sRet = "08";
		else if ( sVal.equals( "CS" ) )
			sRet = "09";
		else if ( sVal.equals( "CE" ) )
			sRet = "10";
		else {
			if ( sES.equals( "S" ) )
				sRet = "07";
			if ( sES.equals( "E" ) )
				sRet = "11";
		}
		return sRet;
	}

	public String verifErro( char cTipo, String sVal, String sIdent, String sChave ) {

		if ( sChave == null ) {
			sVal = sVal != null ? sVal.trim() : "";
			switch ( cTipo ) {
				case 'A' :
					if ( "01,02,03,04,05,06,07,08,09,10,11".indexOf( sVal ) < 0 )
						sRelErros += sIdent + " : tipo de movimento inválido!\n";
					break;
				case 'B' :
					if ( "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,99".indexOf( sVal ) < 0 )
						sRelErros += sIdent + " : Categoria do cliente inválida!\n";
					break;
			}
		}
		else {
			switch ( cTipo ) {
				case 'C' :
					if ( "07,08".indexOf( sChave ) >= 0 )
						sVal = sInfoEmp[ 0 ];
					else
						cTipo = 'B';
					break;
				case 'D' :
					if ( "07,08".indexOf( sChave ) >= 0 )
						sVal = sInfoEmp[ 1 ];
					else
						cTipo = 'B';
					break;
				case 'E' :
					if ( "07,08".indexOf( sChave ) >= 0 )
						sVal = sInfoEmp[ 2 ];
					else
						cTipo = 'B';
					break;
			}
			switch ( cTipo ) {
				case 'A' :
					if ( "01,02,03,04,06,09".indexOf( sChave ) >= 0 && sVal == null )
						sRelErros += sIdent + " : campo requerido!\n";
					break;
				case 'B' :
					if ( sVal == null || sVal.trim().equals( "" ) )
						sRelErros += sIdent + " : campo requerido!\n";
					break;
			}
			sVal = sVal != null ? sVal.trim() : "";
		}
		return sVal;
	}

	private String[] buscaInfoEmp() {

		String[] sRet = new String[ 4 ];
		String sSQL = "SELECT M.NOMEMUNIC,M.SIGLAUF,F.CEPFILIAL,F.CODDISTFILIAL " + "FROM SGFILIAL F, SGMUNICIPIO M " + "WHERE F.CODMUNIC=M.CODMUNIC AND F.CODPAIS=M.CODPAIS AND F.SIGLAUF=M.SIGLAUF ";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				sRet[ 0 ] = rs.getString( "NOMEMUNIC" );
				sRet[ 1 ] = rs.getString( "SIGLAUF" );
				sRet[ 2 ] = rs.getString( "CepFilial" );
				sRet[ 3 ] = rs.getString( "CodDistFilial" );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar dados da filial!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		return sRet;
	}

	public String[] buscaCliFor( int iCodCli ) {

		String sRet[] = { "", "" };
		String sSQL = "SELECT CF.CODCLIFOR, CF.CODCPCLIFOR FROM VDCLIENTEFOR CF, SGPREFERE1 P WHERE " + "CF.CODFOR=P.CODFOR AND CF.CODFILIALFR=P.CODFILIALFR AND " + "CF.CODEMPFR=P.CODEMPFR AND CF.CODCLI=? AND CF.CODEMP=? AND " + "CF.CODFILIAL=? AND P.CODEMP=? AND P.CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodCli );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				sRet[ 0 ] = rs.getString( "CodCliFor" ) != null ? rs.getString( "CodCliFor" ).trim() : "";
				sRet[ 1 ] = rs.getString( "CodCpCliFor" ) != null ? rs.getString( "CodCpCliFor" ).trim() : "";
			}
			if ( sRet[ 0 ].trim().equals( "0" ) )
				sRet[ 0 ] = "";
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o código do cliente no fornecedor!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		return sRet;
	}

	public Object[] buscaUnid( int iCodProd ) {

		Object[] oRet = { "", new Float( 1 ) };
		String sSQL = "SELECT P.CODUNID," + "(SELECT FIRST 1 F.CODUNID FROM EQFATCONV F" + " WHERE F.CODPROD=P.CODPROD AND F.CODEMP=P.CODEMP AND F.CODFILIAL=P.CODFILIAL AND " + "F.CODUNID!=P.CODUNID AND F.CPFATCONV='S' ORDER BY F.CODUNID), " + "(SELECT FIRST 1 F.FATCONV FROM EQFATCONV F"
				+ " WHERE F.CODPROD=P.CODPROD AND F.CODEMP=P.CODEMP AND F.CODFILIAL=P.CODFILIAL AND " + "F.CODUNID!=P.CODUNID AND F.CPFATCONV='S' ORDER BY F.CODUNID) " + " FROM EQPRODUTO P  " + "WHERE P.CODPROD=? AND P.CODEMP=? AND P.CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, iCodProd );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( 2 ) == null ) {
					oRet[ 0 ] = rs.getString( "CodUnid" );
					oRet[ 1 ] = new Float( 1 );
				}
				else {
					oRet[ 0 ] = rs.getString( 2 ).trim();
					oRet[ 1 ] = new Float( rs.getFloat( 3 ) );
				}
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar a unidade!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		return oRet;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btGerar ) {
			iniGerar();
		}
		else if ( evt.getSource() == btVisual ) {
			iniVisualiza();
		}
	}
}
