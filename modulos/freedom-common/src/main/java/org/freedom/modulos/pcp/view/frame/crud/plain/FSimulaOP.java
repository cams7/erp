/**
 * @version 26/04/2006 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe: @(#)FSimulaOP.java <BR>
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
 *         Simulação do consumo de insumos para Ordem de produção.
 * 
 */

package org.freedom.modulos.pcp.view.frame.crud.plain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;

public class FSimulaOP extends FDados implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnEst = new JPanelPad( 540, 110 );

	private JPanelPad pnTab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRefProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtSeqEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescEst = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtQtd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtSaldoProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JButtonPad btGerar = new JButtonPad( "Simular", Icone.novo( "btExecuta.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JTablePad tab = new JTablePad();

	private JScrollPane scroll = new JScrollPane( tab );

	private ListaCampos lcProdEst = new ListaCampos( this );

	public FSimulaOP() {

		super( true );
		setTitulo( "Simulação de OP" );
		setAtribos( 50, 50, 617, 400 );

		lcProdEst.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, true ) );
		lcProdEst.add( new GuardaCampo( txtDescEst, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdEst.add( new GuardaCampo( txtRefProd, "RefProd", "Referencia", ListaCampos.DB_SI, false ) );
		lcProdEst.add( new GuardaCampo( txtSaldoProd, "SldLiqProd", "Saldo", ListaCampos.DB_SI, false ) );
		//lcProdEst.setWhereAdic( "TIPOPROD='F'" );
		lcProdEst.montaSql( false, "PRODUTO", "EQ" );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK(true);
		lcProdEst.setQueryCommit( false );
		lcProdEst.setReadOnly( true );
	
		txtRefProd.setTabelaExterna( lcProdEst, FProduto.class.getCanonicalName() );
		txtCodProd.setTabelaExterna( lcProdEst, FProduto.class.getCanonicalName() );
		txtSeqEst.setTabelaExterna( lcProdEst, FProduto.class.getCanonicalName() );
		txtDescEst.setListaCampos( lcProdEst );
		


		txtSeqEst.setRequerido( true );
		txtQtd.setRequerido( true );

		txtSaldoProd.setAtivo( false );
		txtCodAlmox.setAtivo( false );

		btSair.addActionListener( this );
		btGerar.addActionListener( this );

		lcProdEst.addCarregaListener( this );

		montaTela();

	}

	private void montaTela() {

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );

		pnEst.adic( new JLabelPad( "Cód.prod." ), 7, 10, 80, 20 );
		pnEst.adic( txtCodProd, 7, 30, 80, 20 );
		pnEst.adic( new JLabelPad( "Descrição da estrutura" ), 90, 10, 200, 20 );
		pnEst.adic( txtDescEst, 90, 30, 227, 20 );
		pnEst.adic( new JLabelPad( "Seq.est." ), 320, 10, 80, 20 );
		pnEst.adic( txtSeqEst, 320, 30, 80, 20 );
		pnEst.adic( new JLabelPad( "Quantidade" ), 403, 10, 120, 20 );
		pnEst.adic( txtQtd, 403, 30, 120, 20 );

		pnEst.adic( new JLabelPad( "Cód.Almox." ), 7, 50, 80, 20 );
		pnEst.adic( txtCodAlmox, 7, 70, 80, 20 );
		pnEst.adic( new JLabelPad( "Descrição do almoxarifado" ), 90, 50, 200, 20 );
		pnEst.adic( txtDescAlmox, 90, 70, 227, 20 );
		pnEst.adic( new JLabelPad( "Saldo" ), 320, 50, 80, 20 );
		pnEst.adic( txtSaldoProd, 320, 70, 80, 20 );

		pnEst.adic( btGerar, 405, 65, 120, 30 );

		tab.adicColuna( "Fase" );// 0
		tab.adicColuna( "Cód.prod" );// 1
		tab.adicColuna( "Descrição do produto" );// 2
		tab.adicColuna( "und." );// 3
		tab.adicColuna( "Compsição" );// 4
		tab.adicColuna( "Saldo" );// 5
		tab.adicColuna( "Utilizado" );// 6
		tab.adicColuna( "Rma" );// 7

		tab.setTamColuna( 40, 0 );
		tab.setTamColuna( 70, 1 );
		tab.setTamColuna( 185, 2 );
		tab.setTamColuna( 40, 3 );
		tab.setTamColuna( 80, 4 );
		tab.setTamColuna( 80, 5 );
		tab.setTamColuna( 80, 6 );
		tab.setTamColuna( 30, 7 );

		pnTab.add( scroll );

		c.add( pnEst, BorderLayout.NORTH );
		c.add( pnTab, BorderLayout.CENTER );

		adicBotaoSair();

	}

	private void simularOP() {

		if ( txtCodProd.getVlrString().trim().length() == 0 ) {
			Funcoes.mensagemErro( this, "Código do produto é requerido!" );
			return;
		}

		if ( !estruturaValida() ) {
			Funcoes.mensagemErro( this, "ESTRUTURA INVALIDA!" );
			return;
		}

		if ( txtQtd.getVlrString().trim().length() == 0 ) {
			Funcoes.mensagemInforma( this, "Quantidade é requerida!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		Object[] linha = new Object[ 8 ];

		try {

			tab.limpa();

			// é selecionada o sequencia do item apenas para melhorar a ordenaçao.
			sSQL = "SELECT IT.CODFASE, IT.SEQITEST, IT.CODPRODPD, P.DESCPROD, " + "P.CODUNID, IT.QTDITEST, P.SLDLIQPROD, IT.RMAAUTOITEST " + "FROM PPESTRUTURA E, PPITESTRUTURA IT, EQPRODUTO P " + "WHERE E.CODEMP=? AND E.CODFILIAL=? AND E.CODPROD=? AND E.SEQEST=? "
					+ "AND E.CODEMP=IT.CODEMP AND E.CODFILIAL=IT.CODFILIAL " + "AND E.CODPROD=IT.CODPROD AND E.SEQEST=IT.SEQEST " + "AND P.CODEMP=IT.CODEMPPD AND P.CODFILIAL=IT.CODFILIALPD AND P.CODPROD=IT.CODPRODPD " + "ORDER BY IT.CODFASE, IT.SEQITEST";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPITESTRUTURA" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqEst.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				linha[ 0 ] = new Integer( rs.getInt( "CODFASE" ) );
				linha[ 1 ] = new Integer( rs.getInt( "CODPRODPD" ) );
				linha[ 2 ] = rs.getString( "DESCPROD" ) != null ? rs.getString( "DESCPROD" ).trim() : "";
				linha[ 3 ] = rs.getString( "CODUNID" ) != null ? rs.getString( "CODUNID" ) : "";
				linha[ 4 ] = ( rs.getBigDecimal( "QTDITEST" ) != null ? rs.getBigDecimal( "QTDITEST" ) : new BigDecimal( 0 ) );
				linha[ 4 ] = ( (BigDecimal) linha[ 4 ] ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
				linha[ 5 ] = ( rs.getBigDecimal( "SLDLIQPROD" ) != null ? rs.getBigDecimal( "SLDLIQPROD" ) : new BigDecimal( 0 ) );
				linha[ 5 ] = ( (BigDecimal) linha[ 5 ] ).setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
				linha[ 6 ] = getQtdTotal( ( rs.getBigDecimal( "QTDITEST" ) != null ? rs.getBigDecimal( "QTDITEST" ) : new BigDecimal( 0 ) ) );
				linha[ 7 ] = rs.getString( "RMAAUTOITEST" ) != null ? rs.getString( "RMAAUTOITEST" ) : "";

				tab.adicLinha( linha );

			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consulrar tabela de PPITESTRUTURA.\n" + e.getMessage() );
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao simular OP.\n" + e.getMessage() );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			linha = null;
		}

	}

	private void getInfoProd() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;

		try {

			sSQL = "SELECT P.CODALMOX, A.DESCALMOX, P.SLDPROD " + "FROM EQPRODUTO P, EQALMOX A " + "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.CODPROD=? " + "AND A.CODEMP=P.CODEMPAX AND A.CODFILIAL=P.CODFILIALAX AND A.CODALMOX=P.CODALMOX ";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				txtCodAlmox.setVlrInteger( new Integer( rs.getInt( "CODALMOX" ) ) );
				txtDescAlmox.setVlrString( rs.getString( "DESCALMOX" ) );
				txtSaldoProd.setVlrInteger( new Integer( rs.getInt( "SLDPROD" ) ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar informações do produto.\n" + e.getMessage() );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

	}

	private BigDecimal getQtdTotal( BigDecimal arg ) {

		BigDecimal ret = null;

		try {
			ret = arg.multiply( txtQtd.getVlrBigDecimal() );
		} catch ( Exception e ) {
			ret = new BigDecimal( 0 );
		}

		ret = ret.setScale( Aplicativo.casasDec, BigDecimal.ROUND_HALF_UP );
		return ret;

	}

	private boolean estruturaValida() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		boolean retorno = false;

		try {

			sSQL = "SELECT CODPROD, SEQEST, DESCEST " + "FROM PPESTRUTURA " + "WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND SEQEST=?";

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPESTRUTURA" ) );
			ps.setInt( 3, txtCodProd.getVlrInteger().intValue() );
			ps.setInt( 4, txtSeqEst.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			if ( rs.next() )
				retorno = true;

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar informações do produto.\n" + e.getMessage() );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		return retorno;

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair )
			dispose();
		else if ( evt.getSource() == btGerar )
			simularOP();

	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcProdEst )
			getInfoProd();

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProdEst.setConexao( cn );

	}

}
