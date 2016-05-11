/**
 * @version 28/10/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez e Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FConsulta.java <BR>
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
 *         Formulário de consulta de produtos.
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.StringDireita;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JScrollPane;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;

public class FConsEstoque extends FFilho implements CarregaListener {

	private static final long serialVersionUID = 1L;

	private final int TAM_GRUPO = 14;

	private JPanelPad pinCabProd = new JPanelPad( 700, 100 );

	private JPanelPad pnCliProd = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtSld = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldFK txtSldR = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldFK txtSldC = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldFK txtSldL = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, TAM_GRUPO, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodAlmoxGrup = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmoxGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodAlmoxProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmoxProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTablePad tabProd = new JTablePad();

	private JScrollPane spnTabProd = new JScrollPane( tabProd );

	private ListaCampos lcProd = new ListaCampos( this, "PR" );

	private ListaCampos lcProd2 = new ListaCampos( this );

	private ListaCampos lcAlmoxGrup = new ListaCampos( this, "AX" );

	private ListaCampos lcAlmoxProd = new ListaCampos( this, "AX" );

	private JPanelPad pinCabGrup = new JPanelPad( 700, 60 );

	private JPanelPad pnCliGrup = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tabGrup = new JTablePad();

	private JScrollPane spnTabGrup = new JScrollPane( tabGrup );

	private ListaCampos lcGrup = new ListaCampos( this );

	private JTabbedPanePad tpnConsulta = new JTabbedPanePad();

	public FConsEstoque() {

		super( false );
		setTitulo( "Consulta" );
		setAtribos( 10, 20, 790, 460 );

		getTela().add( tpnConsulta, BorderLayout.CENTER );
		tpnConsulta.addTab( "Grupo", pnCliGrup );
		tpnConsulta.addTab( "Produto", pnCliProd );

		// Início da aba de consulta de saldo por grupo

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setNomeCampo( "CodGrup" );
		txtCodGrup.setFK( true );
		lcGrup.setReadOnly( true );
		lcGrup.montaSql( false, "GRUPO", "EQ" );

		pnCliGrup.add( pinCabGrup, BorderLayout.NORTH );
		pnCliGrup.add( spnTabGrup, BorderLayout.CENTER );

		tabGrup.adicColuna( "Cód.prod." );
		tabGrup.adicColuna( "Descrição do produto" );
		tabGrup.adicColuna( "Saldo prod." );
		tabGrup.adicColuna( "Saldo res." );
		tabGrup.adicColuna( "Saldo consig." );
		tabGrup.adicColuna( "Saldo líquido" );

		tabGrup.setTamColuna( 80, 0 );
		tabGrup.setTamColuna( 250, 1 );
		tabGrup.setTamColuna( 80, 2 );
		tabGrup.setTamColuna( 80, 3 );
		tabGrup.setTamColuna( 90, 4 );
		tabGrup.setTamColuna( 90, 5 );

		pinCabGrup.adic( new JLabelPad( "Cód.grupo" ), 7, 0, 100, 20 );
		pinCabGrup.adic( new JLabelPad( "Descrição do grupo" ), 110, 0, 300, 20 );
		pinCabGrup.adic( new JLabelPad( "Cód.Almox." ), 413, 0, 70, 20 );
		pinCabGrup.adic( new JLabelPad( "Descrição do almoxarifado" ), 486, 0, 200, 20 );
		pinCabGrup.adic( txtCodGrup, 7, 20, 100, 20 );
		pinCabGrup.adic( txtDescGrup, 110, 20, 300, 20 );
		pinCabGrup.adic( txtCodAlmoxGrup, 413, 20, 70, 20 );
		pinCabGrup.adic( txtDescAlmoxGrup, 486, 20, 200, 20 );

		lcAlmoxGrup.add( new GuardaCampo( txtCodAlmoxGrup, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, txtDescAlmoxGrup, false ) );
		lcAlmoxGrup.add( new GuardaCampo( txtDescAlmoxGrup, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmoxGrup.montaSql( false, "ALMOX", "EQ" );
		lcAlmoxGrup.setQueryCommit( false );
		lcAlmoxGrup.setReadOnly( true );
		txtCodAlmoxGrup.setTabelaExterna( lcAlmoxGrup, null );
		txtCodAlmoxGrup.setNomeCampo( "CodAlmox" );

		lcAlmoxProd.add( new GuardaCampo( txtCodAlmoxProd, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, txtDescAlmoxProd, false ) );
		lcAlmoxProd.add( new GuardaCampo( txtDescAlmoxProd, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmoxProd.montaSql( false, "ALMOX", "EQ" );
		lcAlmoxProd.setQueryCommit( false );
		lcAlmoxProd.setReadOnly( true );
		txtCodAlmoxProd.setTabelaExterna( lcAlmoxProd, null );
		txtCodAlmoxProd.setNomeCampo( "CodAlmox" );

		// Fim da aba de consulta de saldo por grupo

		// Aba de consulta de saldo por produto

		txtCodProd.setRequerido( true );
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		// lcProd.add(new GuardaCampo( txtSldProd, "SldProd", "Saldo", ListaCampos.DB_SI, false));
		// lcProd.add(new GuardaCampo( txtSldRProd, "SldResProd", "Saldo res.", ListaCampos.DB_SI, false));
		// lcProd.add(new GuardaCampo( txtSldCProd, "SldConsigProd", "Saldo cons.", ListaCampos.DB_SI, false));
		// lcProd.add(new GuardaCampo( txtSldLProd, "SldLiqProd", "Saldo liq.", ListaCampos.DB_SI, false));
		lcProd.add( new GuardaCampo( txtCodAlmoxProd, "CodAlmox", "Cód.Almox.", ListaCampos.DB_FK, txtDescAlmoxProd, false ) );

		txtCodProd.setTabelaExterna( lcProd, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		pnCliProd.add( pinCabProd, BorderLayout.NORTH );
		pnCliProd.add( spnTabProd, BorderLayout.CENTER );

		pinCabProd.adic( txtDescProd, 100, 20, 257, 20 );

		pinCabProd.adic( txtCodAlmoxProd, 360, 20, 70, 20 );
		pinCabProd.adic( new JLabelPad( "Cód.Almox." ), 360, 0, 70, 20 );

		pinCabProd.adic( txtDescAlmoxProd, 433, 20, 197, 20 );
		pinCabProd.adic( new JLabelPad( "Descrição do Almoxarifado" ), 433, 0, 200, 20 );

		pinCabProd.adic( new JLabelPad( "Saldo" ), 7, 40, 87, 20 );
		// pinCabProd.adic(txtSldProd,7,60,87,20); x
		pinCabProd.adic( txtSld, 7, 60, 87, 20 );
		pinCabProd.adic( new JLabelPad( "Saldo cons." ), 97, 40, 87, 20 );
		// pinCabProd.adic(txtSldCProd,97,60,87,20);
		pinCabProd.adic( txtSldC, 97, 60, 87, 20 );
		pinCabProd.adic( new JLabelPad( "Saldo rest." ), 187, 40, 87, 20 );
		// pinCabProd.adic(txtSldRProd,187,60,87,20);
		pinCabProd.adic( txtSldR, 187, 60, 87, 20 );
		pinCabProd.adic( new JLabelPad( "Saldo liq." ), 277, 40, 87, 20 );
		// pinCabProd.adic(txtSldLProd,277,60,87,20);
		pinCabProd.adic( txtSldL, 277, 60, 87, 20 );

		tabProd.adicColuna( "Cód.c.cli." );
		tabProd.adicColuna( "Descrição da classificação do cliente" );
		tabProd.adicColuna( "Cód.t.pr." );
		tabProd.adicColuna( "Descrição da tab. de preços" );
		tabProd.adicColuna( "Cód.p.pg." );
		tabProd.adicColuna( "Descrição do plano de pagamento." );
		tabProd.adicColuna( "Preço" );

		tabProd.setTamColuna( 70, 0 );
		tabProd.setTamColuna( 250, 1 );
		tabProd.setTamColuna( 70, 2 );
		tabProd.setTamColuna( 210, 3 );
		tabProd.setTamColuna( 70, 4 );
		tabProd.setTamColuna( 240, 5 );
		tabProd.setTamColuna( 90, 6 );

		lcProd.addCarregaListener( this );
		lcAlmoxGrup.addCarregaListener( this );
		lcAlmoxProd.addCarregaListener( this );
		lcGrup.addCarregaListener( this );

		// Fim da aba de consulta de saldo por produto
		adicBotaoSair();

		txtCodAlmoxGrup.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				if ( ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) && ( txtCodAlmoxGrup.getVlrInteger().intValue() == 0 ) ) {
					buscaEstoque( "grupo" );
				}
			}
		} );

		txtCodAlmoxProd.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				if ( ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) && ( txtCodAlmoxProd.getVlrInteger().intValue() == 0 ) ) {
					buscaEstoque( "produto" );
				}
			}
		} );

	}

	private void montaTela() {

		// Aba de saldos por produto
		if ( comRef() ) {
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
			txtRefProd.setRequerido( true );
			lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Ref.prod", ListaCampos.DB_PK, false ) );
			lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
			lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod", ListaCampos.DB_FK, false ) );
			txtRefProd.setTabelaExterna( lcProd2, null );
			txtRefProd.setNomeCampo( "Refprod" );
			txtRefProd.setFK( true );
			lcProd2.setReadOnly( true );
			lcProd2.montaSql( false, "PRODUTO", "EQ" );
			pinCabProd.adic( new JLabelPad( "Ref.prod." ), 7, 0, 90, 20 );
			pinCabProd.adic( new JLabelPad( "Descrição do produto" ), 100, 0, 200, 20 );
			pinCabProd.adic( txtRefProd, 7, 20, 90, 20 );
			lcProd2.setConexao( con );
			lcAlmoxGrup.setConexao( con );
			lcAlmoxProd.setConexao( con );
		}
		else {
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
			pinCabProd.adic( new JLabelPad( "Cód.prod." ), 7, 0, 90, 20 );
			pinCabProd.adic( new JLabelPad( "Descrição do produto" ), 100, 0, 200, 20 );
			pinCabProd.adic( txtCodProd, 7, 20, 90, 20 );
		}
	}

	/**
	 * 
	 * Carrega os valores para a tabela de consulta. Este método é executado após carregar o ListaCampos da tabela.
	 * 
	 */

	private void buscaEstoque( String sTipo ) {

		ResultSet rs = null;
		String sWhere = "";
		String sSQL = "";
		int iCodAlmox = 0;
		int iParam = 0;
		String sCodGrup = null;
		String sCodProd = null;
		String sFiltro = "";

		try {
			if ( sTipo.equals( "grupo" ) ) {
				iCodAlmox = txtCodAlmoxGrup.getVlrInteger().intValue();
				sCodGrup = txtCodGrup.getVlrString().trim();
				if ( sCodGrup.equals( "" ) ) {
					Funcoes.mensagemInforma( this, "Selecione um grupo!" );
					txtCodGrup.requestFocus();
					return;
				}
				if ( sCodGrup.length() < TAM_GRUPO )
					sCodGrup = " LIKE '" + sCodGrup + "%'";
				else
					sCodGrup = "='" + sCodGrup + "'";

				sFiltro = "P.CODGRUP" + sCodGrup;

			}
			else {
				sCodProd = txtCodProd.getVlrString().trim();
				iCodAlmox = txtCodAlmoxProd.getVlrInteger().intValue();

				if ( sCodProd.equals( "" ) ) {
					Funcoes.mensagemInforma( this, "Selecione um produto!" );
					txtCodProd.requestFocus();
					return;
				}

				sFiltro = "P.CODPROD=" + sCodProd;

			}

			if ( iCodAlmox == 0 ) {
				sWhere = "SP.CODEMPAX = P.CODEMPAX AND SP.CODFILIALAX=P.CODFILIALAX AND " + "SP.CODALMOX = P.CODALMOX";
			}
			else {
				sWhere = "SP.CODEMPAX = ? AND SP.CODFILIALAX=? AND SP.CODALMOX = ?";
			}

			sSQL = "SELECT P.CODPROD,P.DESCPROD,P.SLDPROD, P.SLDRESPROD, " + "P.SLDCONSIGPROD,P.SLDLIQPROD,SP.SLDPROD SLDPRODAX, SP.SLDRESPROD SLDRESPRODAX, " + "SP.SLDCONSIGPROD SLDCONSIGPRODAX,SP.SLDLIQPROD SLDLIQPRODAX " + "FROM EQPRODUTO P, EQSALDOPROD SP "
					+ "WHERE SP.CODEMP=P.CODEMP AND SP.CODFILIAL=P.CODFILIAL AND SP.CODPROD = P.CODPROD AND " + "P.ATIVOPROD='S' AND P.CODEMPGP=? AND P.CODFILIALGP=? AND " + sFiltro + " AND " + sWhere + " ORDER BY P.DESCPROD ";

			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, sTipo.equals( "grupo" ) ? ListaCampos.getMasterFilial( "EQGRUPO" ) : ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			iParam = 3;
			if ( iCodAlmox != 0 ) {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQALMOX" ) );
				ps.setInt( iParam++, iCodAlmox );
			}
			rs = ps.executeQuery();
			int iLin = 0;

			if ( sTipo.equals( "grupo" ) ) {
				tabGrup.limpa();
				while ( rs.next() ) {
					tabGrup.adicLinha();
					tabGrup.setValor( new StringDireita( rs.getString( "CODPROD" ) ), iLin, 0 );
					tabGrup.setValor( rs.getString( "DESCPROD" ), iLin, 1 );
					tabGrup.setValor( new StringDireita( rs.getDouble( iCodAlmox != 0 ? "SLDPRODAX" : "SLDPROD" ) + "" ), iLin, 2 );
					tabGrup.setValor( new StringDireita( rs.getDouble( iCodAlmox != 0 ? "SLDRESPRODAX" : "SLDRESPROD" ) + "" ), iLin, 3 );
					tabGrup.setValor( new StringDireita( rs.getDouble( iCodAlmox != 0 ? "SLDCONSIGPRODAX" : "SLDCONSIGPROD" ) + "" ), iLin, 4 );
					tabGrup.setValor( new StringDireita( rs.getDouble( iCodAlmox != 0 ? "SLDLIQPRODAX" : "SLDLIQPROD" ) + "" ), iLin, 5 );
					iLin++;
				}
			}
			else {
				if ( rs.next() ) {
					txtSld.setVlrBigDecimal( new BigDecimal( rs.getDouble( iCodAlmox != 0 ? "SLDPRODAX" : "SLDPROD" ) + "" ) );
					txtSldR.setVlrBigDecimal( new BigDecimal( rs.getDouble( iCodAlmox != 0 ? "SLDRESPRODAX" : "SLDRESPROD" ) + "" ) );
					txtSldC.setVlrBigDecimal( new BigDecimal( rs.getDouble( iCodAlmox != 0 ? "SLDCONSIGPRODAX" : "SLDCONSIGPROD" ) + "" ) );
					txtSldL.setVlrBigDecimal( new BigDecimal( rs.getDouble( iCodAlmox != 0 ? "SLDLIQPRODAX" : "SLDLIQPROD" ) + "" ) );
				}
				else {
					txtSld.setVlrBigDecimal( new BigDecimal( 0 ) );
					txtSldR.setVlrBigDecimal( new BigDecimal( 0 ) );
					txtSldC.setVlrBigDecimal( new BigDecimal( 0 ) );
					txtSldL.setVlrBigDecimal( new BigDecimal( 0 ) );
				}
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar saldos por grupo!\n" + err.getMessage(), true, con, err );
		} finally {
			sSQL = null;
			sCodGrup = null;
		}

	}

	private void carregaTabGrup() {

		buscaEstoque( "grupo" );
	}

	/**
	 * 
	 * Carrega os valores para a tabela de consulta. Este método é executado após carregar o ListaCampos da tabela.
	 * 
	 */

	private void carregaTabProd() {

		int iLin = 0;
		tabProd.limpa();
		String sSQL = "SELECT P.CODCLASCLI,(SELECT CL.DESCCLASCLI" + " FROM VDCLASCLI CL WHERE " + "CL.CODCLASCLI=P.CODCLASCLI),P.CODTAB,T.DESCTAB," + "P.CODPLANOPAG,PP.DESCPLANOPAG,P.PRECOPROD FROM VDPRECOPROD P," + "VDTABPRECO T, FNPLANOPAG PP WHERE "
				+ "T.CODTAB=P.CODTAB AND PP.CODPLANOPAG=P.CODPLANOPAG AND " + "P.CODPROD = ? ORDER BY P.CODPRECOPROD";

		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtCodProd.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				tabProd.adicLinha();
				tabProd.setValor( rs.getString( 1 ) != null ? rs.getString( 1 ) : "", iLin, 0 );
				tabProd.setValor( rs.getString( 2 ) != null ? rs.getString( 2 ) : "", iLin, 1 );
				tabProd.setValor( rs.getString( "CodTab" ), iLin, 2 );
				tabProd.setValor( rs.getString( "DescTab" ) != null ? rs.getString( "DescTab" ) : "", iLin, 3 );
				tabProd.setValor( rs.getString( "CodPlanoPag" ), iLin, 4 );
				tabProd.setValor( rs.getString( "DescPlanoPag" ) != null ? rs.getString( "DescPlanoPag" ) : "", iLin, 5 );
				tabProd.setValor( rs.getString( "PrecoProd" ) != null ? Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( "PrecoProd" ) ) : "", iLin, 6 );
				iLin++;
			}
			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PRECOPROD!\n" + err.getMessage(), true, con, err );
		}

		buscaEstoque( "produto" );

	}

	public void beforeCarrega( CarregaEvent pevt ) {

		/*
		 * if (pevt.getListaCampos() == lcAlmoxProd){ carregaTabProd(); } if (pevt.getListaCampos() == lcAlmoxGrup) { carregaTabGrup(); }
		 */
	}

	public void afterCarrega( CarregaEvent pevt ) {

		if ( ( pevt.getListaCampos() == lcProd ) || ( pevt.getListaCampos() == lcAlmoxProd ) ) {
			carregaTabProd();
		}
		if ( ( pevt.getListaCampos() == lcGrup ) || ( pevt.getListaCampos() == lcAlmoxGrup ) ) {
			carregaTabGrup();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProd.setConexao( con );
		lcGrup.setConexao( con );
		lcAlmoxGrup.setConexao( con );
		lcAlmoxProd.setConexao( con );
		montaTela();
	}

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}
}
