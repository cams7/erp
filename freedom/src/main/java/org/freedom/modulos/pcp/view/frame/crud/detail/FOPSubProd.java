/**
 * @version 06/05/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe: @(#)FOPFase.java <BR>
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

package org.freedom.modulos.pcp.view.frame.crud.detail;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.freedom.acao.CancelEvent;
import org.freedom.acao.CancelListener;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.pcp.Interface.Recarrega;
import org.freedom.modulos.pcp.view.frame.crud.plain.FFase;

public class FOPSubProd extends FDetalhe implements PostListener, CancelListener, InsertListener, ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad( new GridLayout( 1, 1 ) );

	private JPanelPad pinDetFasesCampos = new JPanelPad();

	private JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCodProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtCodProdSubProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtRefProdSubProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldFK txtDescProdSubProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDtEmit = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtValid = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFabProd = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtQtdPrevOP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 3 );

	private JTextFieldPad txtQtdFinalOP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 3 );
	
	private JTextFieldPad txtQtdItSp= new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtCodFase = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFase = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSeqSubProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSitFS = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtTpFase = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );
	
	private JTextFieldFK txtCodUnid = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );
	
	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );
	
	private ListaCampos lcProdSubProd = new ListaCampos( this, "PD" );
	
	private ListaCampos lcProdSubProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcFase = new ListaCampos( this, "FS" );

	private int iCodOP;

	private int iSeqOP;

	private int iSeqEst;

	private Recarrega telaant = null;
	
	private boolean usaRef =  false;

	public FOPSubProd( int iCodOP, int iSeqOP, int iSeqEst, Recarrega telaOP, boolean usarefprod ) { // ,boolean bExecuta

		setTitulo( "SubProdutos" );
		setName( "SupProdutos" );

		setAtribos( 70, 40, 675, 470 );
		setAltCab( 130 );
		setAltDet( 60 );

		this.iCodOP = iCodOP;
		this.iSeqOP = iSeqOP;
		this.iSeqEst = iSeqEst;
		this.telaant = telaOP;
		this.usaRef = usarefprod;

		txtCodOP.setAtivo( false );
		txtCodProd.setAtivo( false );
		txtRefProd.setAtivo( false );
		txtDtEmit.setAtivo( false );
		txtDtValid.setAtivo( false );
		txtQtdPrevOP.setAtivo( false );
		txtQtdFinalOP.setAtivo( false );
		txtSeqOP.setAtivo( false );

		txtCodFase.setAtivo( false );
		txtSeqSubProd.setAtivo( false );

		pinCab = new JPanelPad( 500, 90 );
		
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

//		lcProd.setUsaME( false );
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, true ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		//lcProd.setWhereAdic( "TIPOPROD='F'" );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );
		txtDescProd.setListaCampos( lcProd );
		
//		lcProd2.setUsaME( false );
		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Ref.Prod.", ListaCampos.DB_PK, true ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		//lcProd2.setWhereAdic( "TIPOPROD='F'" );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );
		txtDescProd.setListaCampos( lcProd2 );
		
//		lcProdSubProd.setUsaME( false );
		lcProdSubProd.add( new GuardaCampo( txtCodProdSubProd, "CodProd", "Cód.Prod.", ListaCampos.DB_PK, true ) );
		lcProdSubProd.add( new GuardaCampo( txtRefProdSubProd, "RefProd", "Ref.Prod.", ListaCampos.DB_SI, false ) );
		lcProdSubProd.add( new GuardaCampo( txtDescProdSubProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdSubProd.add( new GuardaCampo( txtCodUnid, "CodUnid", "Código da Unidade", ListaCampos.DB_SI, false ) );
//		lcProdSubProd.setWhereAdic( "TIPOPROD='F'" );
		lcProdSubProd.montaSql( false, "PRODUTO", "EQ" );
		lcProdSubProd.setQueryCommit( false );
		lcProdSubProd.setReadOnly( true );
		txtCodProdSubProd.setTabelaExterna( lcProdSubProd, FProduto.class.getCanonicalName() );
		txtDescProdSubProd.setListaCampos( lcProdSubProd );
		
//		lcProdSubProd2.setUsaME( false );
		lcProdSubProd2.add( new GuardaCampo( txtRefProdSubProd, "RefProd", "Cód.prod.", ListaCampos.DB_PK, true ) );
		lcProdSubProd2.add( new GuardaCampo( txtCodProdSubProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, true ) );
		lcProdSubProd2.add( new GuardaCampo( txtDescProdSubProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProdSubProd2.add( new GuardaCampo( txtCodUnid, "CodUnid", "Código da Unidade", ListaCampos.DB_SI, false ) );
		//		lcProdSubProd.setWhereAdic( "TIPOPROD='F'" );
		lcProdSubProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProdSubProd2.setQueryCommit( false );
		lcProdSubProd2.setReadOnly( true );
		txtCodProdSubProd.setTabelaExterna( lcProdSubProd2, FProduto.class.getCanonicalName() );
		txtDescProdSubProd.setListaCampos( lcProdSubProd2 );

		adicCampo( txtCodOP, 7, 20, 80, 20, "CodOP", "Nº.OP", ListaCampos.DB_PK, true );
		adicCampo( txtSeqOP, 90, 20, 60, 20, "SeqOP", "Seq.OP", ListaCampos.DB_PK, true );
		
		if ( usarefprod ) {
			adicCampo( txtRefProd, 153, 20, 120, 20, "refprod", "Referência", ListaCampos.DB_FK, true );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, txtDescProd, false );
			txtRefProdSubProd.setFK( true );
		}
		else {
			adicCampo( txtCodProd, 153, 20, 120, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, true );
			adicCampoInvisivel( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false );
		}
		
		adicDescFK( txtDescProd, 276, 20, 350, 20, "DescProd", "Descrição do produto" );
		
		adicCampo( txtDtEmit, 7, 60, 110, 20, "DtEmitOP", "Emissão", ListaCampos.DB_SI, true );
		adicCampo( txtDtValid, 120, 60, 110, 20, "DtValidPDOP", "Valid.prod.", ListaCampos.DB_SI, true );
		adicCampo( txtQtdPrevOP, 233, 60, 87, 20, "QtdPrevProdOP", "Qtd.prevista", ListaCampos.DB_SI, true );
		adicCampo( txtQtdFinalOP, 323, 60, 100, 20, "QtdFinalProdOP", "Qtd.produzida", ListaCampos.DB_SI, true );
		adicCampoInvisivel( txtDtFabProd, "dtfabrop", "Dt.Fabric.", ListaCampos.DB_SI, true );

		setListaCampos( false, "OP", "PP" );
		lcCampos.setQueryInsert( false );

		lcFase.add( new GuardaCampo( txtCodFase, "CodFase", "Cód.fase", ListaCampos.DB_PK, true ) );
		lcFase.add( new GuardaCampo( txtDescFase, "DescFase", "Descrição da fase", ListaCampos.DB_SI, false ) );
		lcFase.add( new GuardaCampo( txtTpFase, "TipoFase", "Tipo", ListaCampos.DB_SI, false ) );
		lcFase.montaSql( false, "FASE", "PP" );
		lcFase.setQueryCommit( false );
		lcFase.setReadOnly( true );
		txtCodFase.setTabelaExterna( lcFase, FFase.class.getCanonicalName() );
		txtDescFase.setListaCampos( lcFase );

		setPainel( pinDet, pnDet );
		pinDet.add( pinDetFasesCampos );
		setPainel( pinDetFasesCampos );
		setListaCampos( lcDet ); 
		setNavegador( navRod );

		adicCampo( txtSeqSubProd, 5, 20, 40, 20, "SeqSubProd", "Seq.", ListaCampos.DB_PK, true );
		adicCampo( txtCodFase, 48, 20, 77, 20, "CodFase", "Cd.fase", ListaCampos.DB_FK, txtDescFase, true );
		adicDescFK( txtDescFase, 128, 20, 227, 20, "DescFase", "Descrição da fase" );
		adicDescFKInvisivel( txtTpFase, "DescFase", "Descrição da fase" );
		
		if ( usarefprod ) {
			adicCampo( txtRefProdSubProd, 358, 20, 133, 20, "refprod", "Referência", ListaCampos.DB_FK, txtDescProd,  true );
			adicCampoInvisivel( txtCodProdSubProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI,  true );
			adicDescFK( txtCodUnid,  572, 20, 75, 20, "CodUnid", "Cód.Unid");
			txtCodProdSubProd.setFK( true );
		}
		else {
			adicCampo( txtCodProdSubProd, 358, 20, 133, 20, "codprod", "Cód.prod.", ListaCampos.DB_FK, txtDescProdSubProd, true );
			adicCampoInvisivel( txtRefProdSubProd, "RefProd", "Ref.prod.", ListaCampos.DB_SI,  true );
			adicDescFK( txtCodUnid,  572, 20, 75, 20, "CodUnid", "Cód.Unid");
			txtRefProdSubProd.setFK( true );
		}
	
		adicCampo( txtQtdItSp, 494, 20, 75, 20, "QtdItSp", "Qtd.(Planos)", ListaCampos.DB_SI, true );
		
		
		
		setPainel( pinDetFasesCampos );

		setListaCampos( true, "OPSUBPROD", "PP" );
		lcDet.setQueryInsert( true );
		montaTab();

		lcCampos.setReadOnly( true );

		tab.setTamColuna( 30, 0 ); // Item
		tab.setTamColuna( 50, 1 ); // CodFase
		tab.setTamColuna( 230, 2 ); // Desc.Fase


		lcCampos.addCarregaListener( this );
		lcDet.addCarregaListener( this );
		lcFase.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcDet.addPostListener( this );

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {
		if ( cevt.getListaCampos() == lcDet ) {
			txtCodUnid.setVlrString( getCodUnid() );
		}
	}

	public void afterInsert( InsertEvent ievt ) {
		
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void beforePost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcDet ) {

		
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );

	}

	public void afterPost( PostEvent pevt ) {

		if ( pevt.getListaCampos() == lcDet ) {
			lcCampos.carregaDados();
		}

	}

	public void beforeCancel( CancelEvent cevt ) {

	}

	public void afterCancel( CancelEvent cevt ) {

	}
	
	public BigDecimal getQtdItSP(Integer codemp, Integer codfilial, Integer codop, Integer seqop) throws SQLException{
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal qtditsp = null;
		try {
			sql.append( "select first 1 qtditsp from ppopsubprod where codemp=? and codfilial=? and codop=? and seqop=? order by codfase desc" );
		
			ps  = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codop );
			ps.setInt( param++, seqop );
			rs = ps.executeQuery();
			
			if(rs.next()){
				qtditsp = rs.getBigDecimal( "qtditsp" );
			}
			
		} finally {
			con.commit();
			ps.close();
			rs.close();
		}
		
		return  qtditsp;
	}
	
	
	public void atualizaSubProd( Integer codemp, Integer codfilial, Integer codop, Integer seqop, Integer codempfs, Integer codfilialfs, Integer codfase, BigDecimal qtdItSP ) throws SQLException{
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		PreparedStatement psSelect = null;
		Integer novaop = null;
		
		try {

			sql.append( "update ppopsubprod p set p.qtditsp=? where  p.codemp =? and p.codfilial =? and p.codop =? and p.seqop =? ");
			sql.append( " and p.seqsubprod=(select first 1 s.seqsubprod from ppopsubprod s where s.codemp=? ");
			sql.append( "and s.codfilial=? and s.codop=? and s.seqop=? order by s.codfase desc) ");
			

			ps = con.prepareStatement( sql.toString() );

			int param = 1;
			
			ps.setBigDecimal( param++, qtdItSP );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codop );
			ps.setInt( param++, seqop );
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codop );
			ps.setInt( param++, seqop );
	/*		ps.setInt( param++, codempfs );
			ps.setInt( param++, codfilialfs );
			ps.setInt( param++, codfase );
	*/		
			ps.execute();			
			
		} finally {
			
			ps.close();
			con.commit();
		}
	}
	
	private String getCodUnid() {
		String result = null;
		StringBuilder sql = new StringBuilder("select pd.codunid from eqproduto pd ");
		sql.append( "where pd.codemp=? and pd.codfilial=? ");
		if(usaRef){
			sql.append( "and pd.refprod=? ");
		} else {
			sql.append( "and pd.codprod=? ");
		}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			if(usaRef){
				ps.setString( param++, txtRefProdSubProd.getVlrString() );
			} else {
				ps.setInt( param++, txtCodProdSubProd.getVlrInteger() );
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString( "codunid" );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException e ) {
			Funcoes.mensagemInforma( this, "Não foi possível carregar unidade !\n" + e.getMessage() );
			e.printStackTrace();
		}
		return result;
	}
	
	

	public void dispose() {

		telaant.recarrega();
		super.dispose();
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcProdSubProd.setConexao( cn );
		lcProdSubProd2.setConexao( cn );
		lcFase.setConexao( cn );
		
		txtCodOP.setVlrInteger( new Integer( iCodOP ) );
		txtSeqOP.setVlrInteger( new Integer( iSeqOP ) );
		
		lcCampos.carregaDados();

	}
}
