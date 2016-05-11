/**
 * @version 12/03/2011 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.pcp <BR>
 * Classe: @(#)DLRetIndustria.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela de validação e geração de entrada de retorno para industrialização.
 */

package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class DLRetIndustria extends FFDialogo implements MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnControl = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCab = new JPanelPad( 400, 45 );

	private JTablePad tabRetorno = new JTablePad();

	private JScrollPane spnTabRet = new JScrollPane( tabRetorno );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JPanelPad pnBot = new JPanelPad( new FlowLayout( FlowLayout.LEFT, 5, 5 ) );

	private JPanelPad pnBotSair = new JPanelPad( new FlowLayout( FlowLayout.LEFT, 5, 5 ) );

	private ImageIcon imgStatus = null;

	private String sEstAnalise = "";
	
	private ListaCampos lcPlanoPag = new ListaCampos( this );
	
	private ListaCampos lcFornecedor = new ListaCampos( this );
	
	private ListaCampos lcTipoMov = new ListaCampos( this ); 

	public JButtonPad btOK = new JButtonPad( "Gerar", Icone.novo( "btOk.png" ) );
	
	public JButtonPad btCancel = new JButtonPad( "Cancelar", Icone.novo( "btCancelar.png" ) );

	private enum enumTabRemessa {

		SEL, SEQ, CODPROD, REFPROD, DESCPROD, QTD, CODCOMPRA, SEQITOP  

	};
	
	private org.freedom.modulos.gms.view.frame.crud.detail.FCompra compraGMS = null;
	
	//comunidade@brasemb.or.jp 81334045211
	
	private Integer codop = null;
	
	private Integer seqop = null;

	public DLRetIndustria( DbConnection con, Integer pcodop, Integer pseqop ) {

		setTitulo( "Retorno de industrialização" );
		
		codop = pcodop;
		seqop = pseqop;
		
		setAtribos( 660, 380 );
		
		montaTela( );
		montaListaCampos();
		
		setConexao( con );

		btOK.addActionListener( this );
		btCancel.addActionListener( this );

	}
	
	private void montaListaCampos() {

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );
		txtCodTipoMov.setFK( true );
		txtCodTipoMov.setNomeCampo( "codtipomov" );
		
		lcFornecedor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFornecedor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão Social do Fornecedor", ListaCampos.DB_SI, false ) );
		lcFornecedor.montaSql( false, "FORNECED", "CP" );
		lcFornecedor.setQueryCommit( false );
		lcFornecedor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFornecedor, FFornecedor.class.getCanonicalName() );
		txtCodFor.setFK( true );
		txtCodFor.setNomeCampo( "codfor" );

		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.P.Pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag.setFK( true );
		txtCodPlanoPag.setNomeCampo( "codplanopag" );
		
	}

	private void montaTela( ) {

		pinCab.setPreferredSize( new Dimension( 400, 100 ) );
		pnControl.add( pinCab, BorderLayout.NORTH );
		pnControl.add( spnTabRet, BorderLayout.CENTER );
		c.add( pnControl, BorderLayout.CENTER );

		setPainel( pinCab );


		adic( txtCodFor, 7, 20, 70, 20, "Cód.For." );
		adic( txtRazFor, 80, 20, 200, 20, "Razão Social do Fornecedor" );

		adic( txtCodTipoMov, 283, 20, 70, 20, "Cód.Tp.Mov." );
		adic( txtDescTipoMov, 356, 20, 200, 20, "Descrição do tipo de movimento" );

		adic( txtCodPlanoPag, 7, 60, 70, 20, "Cód.P.Pag." );
		adic( txtDescPlanoPag, 80, 60, 200, 20, "Descrição do plano de pagamento" );
		
		pnRodape.removeAll();
		pnRodape.add( pnBot, BorderLayout.WEST );
		pnBot.tiraBorda();
	
		btCancel.setPreferredSize( new Dimension( 100, 30 ) );
		btOK.setPreferredSize( new Dimension( 100, 30 ) );
		
		pnBotSair.add( btOK );
		pnBotSair.add( btCancel );
		pnRodape.add( pnBotSair, BorderLayout.EAST );
		
		tabRetorno.adicColuna( "" );
		tabRetorno.adicColuna( "Seq." );
		tabRetorno.adicColuna( "Código" );
		tabRetorno.adicColuna( "Referência" );
		tabRetorno.adicColuna( "Descrição" );
		tabRetorno.adicColuna( "Qtd." );
		tabRetorno.adicColuna( "Cod.Compra" );
		tabRetorno.adicColuna( "seq.it.op" );
		
		tabRetorno.setTamColuna( 10, enumTabRemessa.SEL.ordinal() );
		tabRetorno.setTamColuna( 10, enumTabRemessa.SEQ.ordinal() );
		tabRetorno.setTamColuna( 60, enumTabRemessa.CODPROD.ordinal() );
		tabRetorno.setTamColuna( 110, enumTabRemessa.REFPROD.ordinal() );
		tabRetorno.setTamColuna( 300, enumTabRemessa.DESCPROD.ordinal() );
		tabRetorno.setTamColuna( 80, enumTabRemessa.QTD.ordinal() );
		tabRetorno.setTamColuna( 60, enumTabRemessa.CODCOMPRA.ordinal() );

		tabRetorno.setColunaEditavel( enumTabRemessa.SEL.ordinal(), true );
		tabRetorno.setColunaInvisivel( enumTabRemessa.SEQITOP.ordinal() );
		
		tabRetorno.addMouseListener( this );

	}
	
	public void carregaPreferencias( ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select ");
		sql.append("p5.codtipomovre, tm.codplanopag ");
		sql.append("from sgprefere5 p5, eqtipomov tm ");
		sql.append("where tm.codemp=p5.codempre and tm.codfilial=p5.codfilialre and tm.codtipomov=p5.codtipomovre and p5.codemp=? and p5.codfilial=? ");

		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );

			rs = ps.executeQuery();

			int i = 0;
			if ( rs.next() ) {
				
				txtCodTipoMov.setVlrInteger( rs.getInt( "codtipomovre" ) );
				txtCodPlanoPag.setVlrInteger( rs.getInt( "codplanopag" ) );
				
				lcTipoMov.carregaDados();
				lcPlanoPag.carregaDados();
				
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void carregaTabela( ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		Vector<Object> vLinha = null;
		BigDecimal bVlrAfer = null;
		BigDecimal bVlrMin = null;
		BigDecimal bVlrmax = null;
		boolean ablBt = false;
		boolean ablBtVis = false;

		sql.append("select ");
		
		sql.append("it.refprod, it.codprod, pd.descprod, it.qtditop, pd.codalmox, it.codcompra, it.seqitop ");
		
		sql.append("from ppitop it, ppfase fs, ppop op, eqproduto pd ");
		
		sql.append("where fs.codemp=it.codempfs and fs.codfilial=it.codfilialfs and fs.codfase=it.codfase and ");
		sql.append("it.codemp=op.codemp and it.codfilial=op.codfilial and it.codop=op.codop and it.seqop=op.seqop and ");
		sql.append("pd.codemp=it.codemppd and pd.codfilial=it.codfilialpd and pd.codprod=it.codprod and ");
		sql.append("fs.externafase='S' and it.tipoexterno='R' and ");
		sql.append("op.codemp=? and op.codfilial=? and op.codop=? and op.seqop=? ");

		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( 3, codop );
			ps.setInt( 4, seqop );

			rs = ps.executeQuery();

			int i = 0;
			while ( rs.next() ) {

				tabRetorno.adicLinha();

				BigDecimal qtd = rs.getBigDecimal( "QTDITOP" );
				Integer codalmox = rs.getInt( "CODALMOX" );
				Integer codprod = rs.getInt( "CODPROD" );
				Integer codvenda = rs.getInt( "CODCOMPRA") ;
				Integer seqitop = rs.getInt( "SEQITOP") ;
				
				if(codvenda==null || codvenda<1) {
					tabRetorno.setValor( new Boolean(true), i, enumTabRemessa.SEL.ordinal() );
				}
				else {
					tabRetorno.setValor( new Boolean(false), i, enumTabRemessa.SEL.ordinal() );
				}
				
				tabRetorno.setValor( i+1, i, enumTabRemessa.SEQ.ordinal() );
				tabRetorno.setValor( codprod, i, enumTabRemessa.CODPROD.ordinal() );
				tabRetorno.setValor( rs.getString( "REFPROD" ), i, enumTabRemessa.REFPROD.ordinal() );
				tabRetorno.setValor( rs.getString( "DESCPROD" ), i, enumTabRemessa.DESCPROD.ordinal() );
				tabRetorno.setValor( codvenda, i, enumTabRemessa.CODCOMPRA.ordinal() );
				tabRetorno.setValor( seqitop, i, enumTabRemessa.SEQITOP.ordinal() );
				
				tabRetorno.setValor( qtd, i, enumTabRemessa.QTD.ordinal() );
				
				i++;

			}

			rs.close();
			ps.close();

			con.commit();
			
			
		} catch ( SQLException err ) {
			err.printStackTrace();
		}
	}


	public void carregaCampos( Object[] sValores ) {

	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );

		if ( evt.getSource() == btOK ) {
			if(temPendente()) {
				gerarRetorno();
			}
			else {
				Funcoes.mensagemInforma( this, "Não existem ítem selecionados ou pendentes para gerar retorno!" );
			}
		}
		if ( evt.getSource() == btCancel ) {
			dispose();
		}
	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( mevt.getClickCount() == 2 ) {
			if ( mevt.getSource() == tabRetorno && tabRetorno.getLinhaSel() >= 0 ) {
///				alteraQual( imgPendente.equals( tabRemessa.getValor( tabRemessa.getLinhaSel(), EcolPPOPCQ.STATUS.ordinal() ) ) );
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcTipoMov.setConexao( cn );
		lcFornecedor.setConexao( cn );
		lcPlanoPag.setConexao( cn );
		carregaPreferencias();
		carregaTabela();
	}
	
	private Integer getCodCompra() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		Integer ret = null;
		
		try {
			
//			sql.append("SELECT ISEQ FROM SPGERANUM(? , ?, 'VD') " );
			sql.append("select coalesce(max(codcompra),0) + 1 from cpcompra where codemp=? and codfilial=? " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				ret = rs.getInt( 1 );
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private void gerarRetorno() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
	

		sql.append("insert into cpcompra ( " );
		sql.append("codemp, codfilial, codcompra, ");
		sql.append("CODEMPFR, CODFILIALFR,CODFOR, " );
		sql.append("CODEMPPG, CODFILIALPG,CODPLANOPAG,");
		sql.append("CODEMPSE, CODFILIALSE,SERIE,DOCCOMPRA,");
		sql.append("CODEMPTM, CODFILIALTM, CODTIPOMOV, " );
		sql.append("DTENTCOMPRA, DTEMITCOMPRA, CODEMPOP, CODFILIALOP, CODOP, SEQOP  ) " );
		sql.append("VALUES ( " );
		sql.append("?, ?, ?, "); // Compra
		sql.append("?, ?, ?, " ); // Fornecedor
		sql.append("?, ?, ?, "); // Plano de pagamento
		sql.append("?, ?, ?, ?, " ); //Serie
		sql.append("?, ?, ?, " ); //TipoMov
		
		sql.append("CAST('today' AS DATE),CAST('today' AS DATE), " );

		sql.append("?, ?, ?, ? )" ); //OP

		try {

			Integer codcompra = getCodCompra();
			Integer codfor = txtCodFor.getVlrInteger();
			Integer codplanopag = txtCodPlanoPag.getVlrInteger();
			Integer codtipomov = txtCodTipoMov.getVlrInteger();
			
			
			if( codcompra == null || codcompra < 1) {
				Funcoes.mensagemErro( this, "Não foi possível determinar um novo código para a compra!\n Consulte o suporte técnico." );
				return;
			}

			if( codfor == null || codfor < 1 ) {
				Funcoes.mensagemErro( this, "Fornecedor não foi informado!\n Informe o fornecedor ou consulte o suporte técnico." );
				return;
			}
			
			if( codtipomov == null || codtipomov < 1 ) {
				Funcoes.mensagemErro( this, "Informe um tipo de movimento!\n Verifique se existe um tipo de movimento para reterno definido nas preferências do módulo PCP\n ou consulte o suporte técnico." );
				return;
			}
			
			if( codplanopag == null || codplanopag < 1 ) {
				Funcoes.mensagemErro( this, "Informe um plano de pagamento para a saída!\n Verifique se existe um plano de pagamento vinculado ao tipo de movimento informado\n ou consulte o suporte técnico." );
				return;
			}
			
			String serie = TipoMov.getSerieTipoMov( codtipomov );
			
			if( serie == null || "".equals( serie ) ) {
				Funcoes.mensagemErro( this, "Não foi possível determinar uma série para o retorno!\n Verifique se existe uma série vinculada ao tipo de movimento informado\n ou consulte o suporte técnico." );
				return;
			}
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 3, codcompra );
			
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( 6, codfor );
			
			ps.setInt( 7, Aplicativo.iCodEmp );
			ps.setInt( 8, ListaCampos.getMasterFilial( "FNPLANOPAG" ) );
			ps.setInt( 9, codplanopag );
			
			ps.setInt( 10, Aplicativo.iCodEmp );
			ps.setInt( 11, ListaCampos.getMasterFilial( "EQSERIE" ) );
			ps.setString( 12, serie );
			ps.setInt( 13, TipoMov.getDocSerie( serie ) );
			
			ps.setInt( 14, Aplicativo.iCodEmp );
			ps.setInt( 15, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( 16, codtipomov );
			
			ps.setInt( 17, Aplicativo.iCodEmp );
			ps.setInt( 18, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( 19, codop );
			ps.setInt( 20, seqop );
			
			int inserido = ps.executeUpdate();
			
			if(inserido>0) {
				if ( Funcoes.mensagemConfirma( null, "Compra '" + codcompra + "' gerada com sucesso!!!\n\n" + "Deseja edita-la?" ) == JOptionPane.YES_OPTION ) {
					compraGMS = new FCompra();
					Aplicativo.telaPrincipal.criatela( "Compra", compraGMS, con, false );
					compraGMS.exec( codcompra );
					
					gerarItensRetorno( compraGMS, codcompra );
					
					this.dispose();
					
				}
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean temPendente() {
		boolean ret = false;
		
		try {
			for(int i=0; i<tabRetorno.getNumLinhas(); i++) {
		
				Integer codvenda = (Integer) tabRetorno.getValor( i, enumTabRemessa.CODCOMPRA.ordinal() );
				Boolean selecionado = (Boolean) tabRetorno.getValor( i, enumTabRemessa.SEL.ordinal() );
				
				if(codvenda < 1 && selecionado) {
					return true;
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private void gerarItensRetorno(FCompra fcompra, Integer pcodcompra) {
		
		try {

			for(int i=0; i<tabRetorno.getNumLinhas(); i++) {
				
				Integer codprod = (Integer) tabRetorno.getValor( i, enumTabRemessa.CODPROD.ordinal() );
				String refprod = (String) tabRetorno.getValor( i, enumTabRemessa.REFPROD.ordinal() );
				BigDecimal qtd = (BigDecimal) tabRetorno.getValor( i, enumTabRemessa.QTD.ordinal() );
				
				Boolean selecionado = (Boolean) tabRetorno.getValor( i, enumTabRemessa.SEL.ordinal() );
				Integer codcompra = (Integer) tabRetorno.getValor( i, enumTabRemessa.CODCOMPRA.ordinal() );
				Integer seqitop = (Integer) tabRetorno.getValor( i, enumTabRemessa.SEQITOP.ordinal() );
				
				if(codcompra < 1 && selecionado) {
					fcompra.insertItem(codprod, refprod, qtd);
					
					atualizaItOP( pcodcompra, seqitop );
					
				}
				
				
			}
			
			fcompra.execShow();
			
			try {
				fcompra.setSelected(true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void atualizaItOP(Integer codcompra, Integer seqitop) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
	
		sql.append("update ppitop set codempcp=?, codfilialcp=?, codcompra=? " );
		sql.append("where codemp=? and codfilial=? and codop=? and seqop=? and seqitop=?");

		try {

			ps = con.prepareStatement( sql.toString() );
						
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 3, codcompra );
			
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( 6, codop );
			ps.setInt( 7, seqop );
			ps.setInt( 8, seqitop );
			
			ps.executeUpdate();
			
			con.commit();
			ps.close();
			
						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
