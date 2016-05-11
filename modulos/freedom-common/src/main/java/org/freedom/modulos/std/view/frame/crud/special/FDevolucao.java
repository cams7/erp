/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FDevolucao.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrincipal;
import org.freedom.library.swing.frame.FWizard;
import org.freedom.modulos.gms.view.frame.crud.detail.FCompra;
import org.freedom.modulos.pdv.FVenda;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaItVenda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.modulos.std.view.frame.utility.FCancVenda;

public class FDevolucao extends FWizard implements CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pin1 = null;

	private JPanelPad pin1a = null;

	private JPanelPad pin1a1 = null;

	private JPanelPad pin1a2 = null;

	private JPanelPad pin1b = null;

	private JPanelPad pin1b1 = null;

	private JPanelPad pin1c = null;

	private JPanelPad pin1c1 = null;

	private JRadioGroup<?, ?> rg1 = null;

	private JRadioGroup<?, ?> rg1a = null;

	private JRadioGroup<?, ?> rg1b = null;

	private JRadioGroup<?, ?> rg1c = null;

	private JTextFieldPad txtCodVenda1a = null;

	private JTextFieldFK txtDocVenda1a = null;

	private JTextFieldPad txtTipoVenda1a = null;

	private JTextFieldFK txtDataVenda1a = null;

	private JTextFieldFK txtStatusVenda1a = null;

	private JTextFieldPad txtCodCli1c = null;

	private JTextFieldFK txtRazCli1c = null;

	private ListaCampos lcVenda1a = null;

	private ListaCampos lcCli1c = null;

	private JButtonPad btBuscaItVenda1b = new JButtonPad( "Buscar itens ->" );

	private JLabelPad lbBuscaItVenda1b = new JLabelPad( "0 iten(s) a ser(em) adicionado(s)" );

	private Vector<?> vItVenda1b = null;

	public FDevolucao() {

		super( Aplicativo.telaPrincipal );
		setAtribos( 500, 310 );
		setTitulo( "Devolução" );
		setCabecalho( "Devolução de venda" );

		mostraPainel1();
	}

	private void mostraPainel1() {

		if ( pin1 != null ) {
			setPainel( pin1 );
			return;
		}

		pin1 = new JPanelPad();
		setPainel( pin1 );

		rg1 = new JRadioGroup<String, String>( 3, 1, new Object[] { "Devolução de saída completa.", "Devolução de saída parcial.", "Devolução de saída sem venda agregada.", }, new Object[] { "SC", "SP", "SS" } );
		rg1.setBorder( BorderFactory.createEmptyBorder() );

		adic( new JLabelPad( "Documento de entrada:" ), 20, 10, 250, 20 );
		adic( rg1, 20, 40, 300, 80 );

	}

	private void mostraPainel1a() {

		if ( pin1a != null ) {
			setPainel( pin1a );
			return;
		}

		pin1a = new JPanelPad();
		setPainel( pin1a );

		txtCodVenda1a = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );
		txtDocVenda1a = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 9, 0 );
		txtDataVenda1a = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );
		txtStatusVenda1a = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );
		JTextFieldFK txtSerie = new JTextFieldFK( JTextFieldPad.TP_STRING, 4, 0 );
		JTextFieldFK txtVlrLiqVenda = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 15, 2 );
		txtTipoVenda1a = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );

		rg1a = new JRadioGroup<String, String>( 2, 1, new Object[] { "Cancelar venda", "Emitir nota de entrada", }, new Object[] { "CV", "NE" } );
		rg1a.setBorder( BorderFactory.createEmptyBorder() );

		lcVenda1a = new ListaCampos( this );

		rg1a.setAtivo( false );

		lcVenda1a.add( new GuardaCampo( txtCodVenda1a, "CodVenda", "Cód.vend.", ListaCampos.DB_PK, true ) );
		lcVenda1a.add( new GuardaCampo( txtDocVenda1a, "DocVenda", "Doc.", ListaCampos.DB_SI, false ) );
		lcVenda1a.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_SI, false ) );
		lcVenda1a.add( new GuardaCampo( txtVlrLiqVenda, "VlrLiqVenda", "Valor.liq.", ListaCampos.DB_SI, false ) );
		lcVenda1a.add( new GuardaCampo( txtDataVenda1a, "DtEmitVenda", "Data", ListaCampos.DB_SI, false ) );
		lcVenda1a.add( new GuardaCampo( txtStatusVenda1a, "StatusVenda", "Status", ListaCampos.DB_SI, false ) );
		lcVenda1a.add( new GuardaCampo( txtTipoVenda1a, "TipoVenda", "Tp.venda", ListaCampos.DB_SI, false ) );
		lcVenda1a.montaSql( false, "VENDA", "VD" );
		lcVenda1a.setReadOnly( true );
		lcVenda1a.setConexao( con );
		txtCodVenda1a.setTabelaExterna( lcVenda1a, FVenda.class.getCanonicalName() );
		txtCodVenda1a.setFK( true );
		txtCodVenda1a.setNomeCampo( "CodVenda" );

		adic( new JLabelPad( "Informações da venda" ), 20, 10, 250, 20 );
		adic( new JLabelPad( "Nº pedido" ), 37, 40, 80, 20 );
		adic( txtCodVenda1a, 37, 60, 80, 20 );
		adic( new JLabelPad( "Doc." ), 120, 40, 77, 20 );
		adic( txtDocVenda1a, 120, 60, 77, 20 );
		adic( new JLabelPad( "Série" ), 200, 40, 67, 20 );
		adic( txtSerie, 200, 60, 67, 20 );
		adic( new JLabelPad( "Valor" ), 270, 40, 97, 20 );
		adic( txtVlrLiqVenda, 270, 60, 97, 20 );
		adic( new JLabelPad( "Emissão" ), 370, 40, 100, 20 );
		adic( txtDataVenda1a, 370, 60, 100, 20 );
		adic( rg1a, 40, 100, 250, 60 );

		lcVenda1a.addCarregaListener( this );

	}

	private void mostraPainel1b() {

		if ( pin1b != null ) {
			setPainel( pin1b );
			return;
		}

		pin1b = new JPanelPad();
		setPainel( pin1b );

		rg1b = new JRadioGroup<String, String>( 1, 1, new Object[] { "Gerar entrada agora.", }, new Object[] { "GE" } );
		rg1b.setBorder( BorderFactory.createEmptyBorder() );

		rg1b.setAtivo( false );

		adic( btBuscaItVenda1b, 40, 20, 180, 30 );
		adic( lbBuscaItVenda1b, 40, 60, 250, 20 );
		adic( rg1b, 40, 100, 250, 60 );

		btBuscaItVenda1b.addActionListener( this );

	}

	private void mostraPainel1c() {

		if ( pin1c != null ) {
			setPainel( pin1c );
			return;
		}

		pin1c = new JPanelPad();
		setPainel( pin1c );

		txtCodCli1c = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
		txtRazCli1c = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

		rg1c = new JRadioGroup<String, String>( 1, 1, new Object[] { "Gerar fornecedor agora.", }, new Object[] { "GC" } );
		rg1c.setBorder( BorderFactory.createEmptyBorder() );

		lcCli1c = new ListaCampos( this );

		rg1c.setAtivo( false );

		lcCli1c.add( new GuardaCampo( txtCodCli1c, "CodCli", "Cód.cli", ListaCampos.DB_PK, true ) );
		lcCli1c.add( new GuardaCampo( txtRazCli1c, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli1c.montaSql( false, "CLIENTE", "VD" );
		lcCli1c.setReadOnly( true );
		lcCli1c.setConexao( con );
		txtCodCli1c.setTabelaExterna( lcCli1c, FCliente.class.getCanonicalName() );
		txtCodCli1c.setFK( true );
		txtCodCli1c.setNomeCampo( "CodCli" );

		adic( new JLabelPad( "Informações do cliente" ), 20, 10, 250, 20 );
		adic( new JLabelPad( "Cód.cli." ), 37, 40, 250, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 120, 40, 300, 20 );
		adic( txtCodCli1c, 37, 60, 80, 20 );
		adic( txtRazCli1c, 120, 60, 300, 20 );
		adic( rg1c, 40, 100, 250, 60 );

		lcCli1c.addCarregaListener( this );

	}

	private void mostraPainel1a1() {

		pin1a1 = new JPanelPad();
		setPainel( pin1a1 );

		if ( cancVenda() ) {
			adic( new JLabelPad( "A venda foi cancelada com sucesso." ), 20, 10, 350, 20 );
			podeCancelar = false;
			podeFinalizar = true;
			podeVoltar = false;
		}
		else
			adic( new JLabelPad( "Não foi possível cancelar a venda." ), 20, 10, 350, 20 );
	}

	private void mostraPainel1a2() {

		pin1a2 = new JPanelPad();
		setPainel( pin1a2 );

		int iCodCompra = 0;
		if ( ( iCodCompra = entrada( txtCodVenda1a.getVlrInteger().intValue(), txtTipoVenda1a.getVlrString() ) ) > 0 ) {
			adic( new JLabelPad( "Entrada '" + iCodCompra + "' gerada com sucesso!" ), 20, 10, 350, 20 );
			abreCompra( iCodCompra );
			podeCancelar = false;
			podeFinalizar = true;
			podeVoltar = false;
		}
		else
			adic( new JLabelPad( "Não foi possível gerar entrada." ), 20, 10, 350, 20 );
	}

	private void mostraPainel1b1() {

		pin1b1 = new JPanelPad();
		setPainel( pin1b1 );

		int iCodCompra = 0;
		if ( ( iCodCompra = entradaParc( vItVenda1b ) ) > 0 ) {
			adic( new JLabelPad( "Entrada '" + iCodCompra + "' gerada com sucesso!" ), 20, 10, 350, 20 );
			abreCompra( iCodCompra );
			podeCancelar = false;
			podeFinalizar = true;
			podeVoltar = false;
		}
		else
			adic( new JLabelPad( "Não foi possível gerar entrada." ), 20, 10, 350, 20 );
	}

	private void mostraPainel1c1() {

		pin1c1 = new JPanelPad();
		setPainel( pin1c1 );

		int iCodFor = 0;
		if ( ( iCodFor = geraFor( txtCodCli1c.getVlrInteger().intValue() ) ) > 0 ) {
			adic( new JLabelPad( "Entrada iniciada com sucesso!" ), 20, 10, 350, 20 );
			abreFor( iCodFor );
			podeCancelar = false;
			podeFinalizar = true;
			podeVoltar = false;
		}
		else
			adic( new JLabelPad( "Não foi possível criar o fornecedor." ), 20, 10, 350, 20 );
	}

	private int entrada( int iCodVenda, String sTipoVenda ) {

		int iRet = 0;
		String sSQL = "SELECT CODCOMPRA FROM CPGERAENTRADASP(?,?,?,?,?,'S')";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setString( 4, sTipoVenda );
			ps.setInt( 5, iCodVenda );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "CodCompra" );
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao gerar entrada!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		return iRet;
	}

	private int entradaParc( Vector<?> vItens ) {

		int iRet = 0;
		String sSQL = "SELECT CODCOMPRA FROM CPGERAENTRADASP(?,?,?,?,?,'N')";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setString( 4, ( (Object[]) vItens.elementAt( 0 ) )[ 3 ].toString() );
			ps.setString( 5, ( (Object[]) vItens.elementAt( 0 ) )[ 1 ].toString() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "CodCompra" );
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao gerar entrada!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
			return iRet;
		}
		for ( int i = 0; i < vItens.size(); i++ ) {
			String sSQLIt = "EXECUTE PROCEDURE CPGERAITENTRADASP(?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement ps = con.prepareStatement( sSQLIt );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );
				ps.setInt( 3, iRet );
				ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setString( 5, ( (Object[]) vItens.elementAt( i ) )[ 3 ].toString() );
				ps.setString( 6, ( (Object[]) vItens.elementAt( i ) )[ 1 ].toString() );
				ps.setString( 7, ( (Object[]) vItens.elementAt( i ) )[ 2 ].toString() );
				ps.setString( 8, ( (Object[]) vItens.elementAt( i ) )[ 0 ].toString() );
				ps.execute();
				ps.close();
				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao gerar item da entrada!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
		}
		return iRet;
	}

	private void buscaItVenda() {

		DLBuscaItVenda dl = new DLBuscaItVenda( this );
		dl.setConexao( con );
		dl.setVisible( true );
		if ( dl.OK ) {
			vItVenda1b = dl.getValores();
			lbBuscaItVenda1b.setText( vItVenda1b.size() + " item(s) a serem adicionados" );
			if ( vItVenda1b.size() > 0 )
				podeProximo = true;
		}
		else
			podeProximo = false;
		dl.dispose();
		upBotoes();
	}

	private int geraFor( int iCodCli ) {

		int iRet = 0;
		String sSQL = "SELECT CODFOR FROM CPADICFORSP(?,?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( 4, iCodCli );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "CodFor" );
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao gerar fornecedor!\n" + err.getMessage() );
			err.printStackTrace();
		}
		return iRet;
	}

	private boolean cancVenda() {

		boolean bRet = false;
		FCancVenda cVenda = new FCancVenda();
		cVenda.setConexao( con );
		bRet = cVenda.cancelar( txtCodVenda1a.getVlrInteger().intValue(), txtStatusVenda1a.getVlrString(), "Devolução de mercadoria."  );
		return bRet;
	}

	private void trataVenda1a() {

		if ( txtCodVenda1a.getVlrInteger().intValue() > 0 ) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime( txtDataVenda1a.getVlrDate() );
			if ( cal.get( Calendar.MONTH ) == ( new GregorianCalendar() ).get( Calendar.MONTH ) )
				rg1a.setAtivo( 0, true );
			else {
				rg1a.setAtivo( 0, false );
				rg1a.setVlrString( "NE" );
			}
			rg1a.setAtivo( 1, true );
			podeProximo = true;
		}
		else {
			rg1a.setAtivo( 0, false );
			rg1a.setAtivo( 1, false );
			podeProximo = false;
		}
	}

	private void abreCompra( int iCodCompra ) {

		if ( cPai instanceof FPrincipal )
			if ( ( (FPrincipal) cPai ).temTela( "Compra" ) == false ) {
				FCompra tela = new FCompra();
				( (FPrincipal) cPai ).criatela( "Compra", tela, con );
				tela.exec( iCodCompra );
			}
	}

	private void abreFor( int iCodFor ) {

		int iCodTipoMov = 0;
		String sSerie = "";
		int iDoc = 0;
		String sSQL = "SELECT P.CODTIPOMOV5,S.SERIE," + "(SELECT DOC FROM LFNOVODOCSP(T.SERIE,T.CODEMP,T.CODFILIALSE)) " + "FROM SGPREFERE1 P, EQTIPOMOV T, LFSERIE S WHERE " + "T.CODTIPOMOV =P.CODTIPOMOV AND T.CODEMP=P.CODEMPTM AND " + "T.CODFILIAL=P.CODFILIALTM AND S.SERIE=T.SERIE AND "
				+ "S.CODEMP=T.CODEMPSE AND S.CODFILIAL=T.CODFILIALSE AND " + "P.CODEMP=? AND P.CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				iCodTipoMov = rs.getInt( "CodTipoMov5" );
				sSerie = rs.getString( "Serie" );
				iDoc = rs.getInt( 3 );
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o tipo de movimento para devolução!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		if ( cPai instanceof FPrincipal )
			if ( ( (FPrincipal) cPai ).temTela( "Fornecedor" ) == false ) {
				FCompra tela = new FCompra();
				( (FPrincipal) cPai ).criatela( "Fornecedor", tela, con );
				tela.execDev( iCodFor, iCodTipoMov, sSerie, iDoc );
			}
	}

	public String proximo() {

		String sRet = "";
		if ( getNivel().equals( "1" ) ) {
			if ( rg1.getVlrString().equals( "SC" ) ) {
				mostraPainel1a();
				sRet = "a";
			}
			else if ( rg1.getVlrString().equals( "SP" ) ) {
				mostraPainel1b();
				sRet = "b";
			}
			else if ( rg1.getVlrString().equals( "SS" ) ) {
				mostraPainel1c();
				sRet = "c";
			}
			podeVoltar = true;
			podeProximo = false;
		}
		if ( getNivel().equals( "1a" ) ) {
			if ( rg1a.getVlrString().equals( "CV" ) ) {
				mostraPainel1a1();
				podeProximo = false;
				sRet = "1";
			}
			else if ( rg1a.getVlrString().equals( "NE" ) ) {
				mostraPainel1a2();
				podeProximo = false;
				sRet = "2";
			}
		}
		if ( getNivel().equals( "1b" ) ) {
			mostraPainel1b1();
			podeProximo = false;
			sRet = "1";
		}
		if ( getNivel().equals( "1c" ) ) {
			mostraPainel1c1();
			podeProximo = false;
			sRet = "1";
		}
		return sRet;
	}

	public void voltar() {

		if ( getNivel().equals( "1" ) ) {
			mostraPainel1();
			podeVoltar = false;
			podeProximo = true;
		}
		else if ( getNivel().equals( "1a" ) ) {
			mostraPainel1a();
			podeProximo = true;
		}
		else if ( getNivel().equals( "1b" ) ) {
			mostraPainel1b();
			podeProximo = true;
		}
		else if ( getNivel().equals( "1c" ) ) {
			mostraPainel1c();
			podeProximo = true;
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btBuscaItVenda1b ) {
			buscaItVenda();
		}
		super.actionPerformed( evt );
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcVenda1a ) {
			trataVenda1a();
		}
		else if ( cevt.getListaCampos() == lcCli1c ) {
			if ( txtCodCli1c.getVlrInteger().intValue() > 0 )
				podeProximo = true;
			else
				podeProximo = false;
		}
		upBotoes();
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}
}
