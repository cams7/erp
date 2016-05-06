/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez e Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FInventario.java <BR>
 * 
 *                      Este programa é licenciado de acordo c>om a LPG-PC (Licença Pública Geral para Programas de Computador), <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa. <BR>
 *                      Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você pode contatar <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é preciso estar <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.dialog.utility.DLLote;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.std.view.dialog.report.DLRInventario;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;

public class FInventario extends FDados implements CarregaListener, InsertListener, PostListener, CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private int casasDec = Aplicativo.casasDec;

	private int casasDecFin = Aplicativo.casasDecFin;

	private int casasDecPre = Aplicativo.casasDecPre;
	
	private JTextFieldPad txtCodInv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtRefProd2 = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodLote = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDataInvP = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtQtdInvP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtPrecoInvP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );
	
	private JTextFieldPad txtCustoInfoProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDecPre );

	private JTextFieldPad txtSldAtualInvP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtSldNovoInvP = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, casasDec );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescLote = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextAreaPad txtObsInv = new JTextAreaPad( 500 );

	private JScrollPane spnObs = new JScrollPane( txtObsInv );

	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcProd2 = new ListaCampos( this, "PD" );

	private ListaCampos lcLote = new ListaCampos( this, "LE" );

	private ListaCampos lcAlmox = new ListaCampos( this, "AX" );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private JCheckBoxPad cbLote = new JCheckBoxPad( "Lote", "S", "N" );
	
	private JTextFieldPad txtLocalProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private int iPrefs[] = { 0, 0, 0 };

	boolean bLote = false;

	public FInventario() {

		super();
		setTitulo( "Inventário" );
		setAtribos( 50, 50, 490, 470 );

		nav.setNavigation( true );
		
		cbLote.addCheckBoxListener( this );

		txtQtdInvP.setAtivo( false );
		txtSldAtualInvP.setAtivo( false );

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, txtDescProd, true ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( cbLote, "CLoteProd", "Classifica por lote?", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cod.Almox.", ListaCampos.DB_FK, txtDescAlmox, false ) );
		lcProd.add( new GuardaCampo( txtCustoInfoProd, "CustoInfoProd", "Custo.Informado", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtLocalProd, "LocalProd", "Local armz.", ListaCampos.DB_SI, false ) );
		
		lcProd.setWhereAdic( "NOT TIPOPROD = 'S' AND ATIVOPROD='S'" );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_FK, false ) );
		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_PK, txtDescProd, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( cbLote, "CLoteProd", "Classifica por lote?", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCustoInfoProd, "CustoInfoProd", "Custo.Informado", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtLocalProd, "LocalProd", "Local armz.", ListaCampos.DB_SI, false ) );
		
		txtRefProd.setChave( ListaCampos.DB_PK );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setListaCampos( lcProd2 );
		lcProd2.setWhereAdic( "NOT TIPOPROD = 'S' AND ATIVOPROD='S'" );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.setWhereAdic( "( ( ESTIPOMOV = 'I' )  AND " + " ( TUSUTIPOMOV='S' OR EXISTS (SELECT * FROM EQTIPOMOVUSU TU " 
				+ "WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND " 
				+ "TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp + " AND "
				+ "TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) " + ")" );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );

		txtCodLote.setAtivo( false );
		lcLote.add( new GuardaCampo( txtCodLote, "CodLote", "Cód.lote", ListaCampos.DB_PK, false ) );
		lcLote.add( new GuardaCampo( txtDescLote, "VenctoLote", "Vencimento do lote", ListaCampos.DB_SI, false ) );
		// lcLote.add(new GuardaCampo( txtSldAtualInvP, 90, 100, 207, 20,
		// "SldLiqLote", "Saldo", false, false, null,
		// JTextFieldPad.TP_DECIMAL,false),"txtDescLotex");
		lcLote.setDinWhereAdic( "CODPROD=#N", txtCodProd );
		lcLote.montaSql( false, "LOTE", "EQ" );
		lcLote.setQueryCommit( false );
		lcLote.setReadOnly( true );
		lcLote.setAutoLimpaPK( false );
		txtCodLote.setTabelaExterna( lcLote, null );

		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, false ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setQueryCommit( false );
		lcAlmox.setReadOnly( true );
		txtCodAlmox.setTabelaExterna( lcAlmox, FAlmox.class.getCanonicalName() );

		lcCampos.addInsertListener( this );
		lcCampos.addCarregaListener( this );
		lcProd.addCarregaListener( this );
		lcProd2.addCarregaListener( this );
		lcLote.addCarregaListener( this );
		lcCampos.addPostListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		txtRefProd.addKeyListener( new KeyAdapter() {

			public void keyPressed( KeyEvent kevt ) {

				lcCampos.edit();
			}
		} );
		txtCodAlmox.addKeyListener( this );
		txtCodLote.addKeyListener( this );

		setImprimir( true );
	}

	private void montaTela() {

		adicCampo( txtCodInv, 7, 20, 90, 20, "CodInvProd", "Cód.inv.prod.", ListaCampos.DB_PK, true );
		adicCampo( txtDataInvP, 100, 20, 100, 20, "DataInvP", "Data", ListaCampos.DB_SI, true );
		adicCampo( txtCodTipoMov, 7, 60, 90, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, true );
		adicDescFK( txtDescTipoMov, 100, 60, 207, 20, "DescTipoMov", "Descrição do tipo de movimento" );

		if ( comRef() ) {
			txtRefProd.setBuscaAdic( new DLBuscaProd( con, "REFPROD", lcProd2.getWhereAdic() ) );
			adicCampoInvisivel( txtRefProd2, "RefProd", "Referência do produto", ListaCampos.DB_SI, false );
			adicCampoInvisivel( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_FK, txtDescProd, false );
			txtRefProd.setRequerido( true );
			adic( new JLabelPad( "Referência" ), 7, 80, 80, 20 );
			adic( txtRefProd, 7, 100, 90, 20 );
		}
		else {
			txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
			adicCampo( txtCodProd, 7, 100, 90, 20, "CodProd", "Cód.prod", ListaCampos.DB_FK, txtDescProd, true );
		}
		adicDescFK( txtDescProd, 100, 100, 207, 20, "DescProd", "Descrição do produto" );
		adicCampo( txtCodLote, 7, 140, 90, 20, "CodLote", "Cód.lote", ListaCampos.DB_FK, txtDescLote, false );
		adicDescFK( txtDescLote, 100, 140, 207, 20, "VenctoLote", "Vencimento do lote" );
		adicCampo( txtCodAlmox, 7, 180, 90, 20, "CodAlmox", "Cód.amox.", ListaCampos.DB_FK, txtDescAlmox, true );
		adicDescFK( txtDescAlmox, 100, 180, 207, 20, "DescAlmox", "Descrição do almoxarifado" );
		adicCampo( txtSldAtualInvP, 7, 220, 140, 20, "SldAtualInvP", "Estoque atual", ListaCampos.DB_SI, false );
		adicCampo( txtSldNovoInvP, 150, 220, 137, 20, "SldDigInvP", "Estoque novo", ListaCampos.DB_SI, false );
		adicCampo( txtPrecoInvP, 90, 260, 97, 20, "PrecoInvP", "Custo unitário", ListaCampos.DB_SI, true );
		adicCampo( txtQtdInvP, 7, 260, 80, 20, "QtdInvP", "Quantidade", ListaCampos.DB_SI, true );

		adicDB( txtObsInv, 7, 300, 300, 80, "ObsInvP", "Observação", false );
		lcCampos.setQueryInsert( false );
		setListaCampos( true, "INVPROD", "EQ" );
		
		adic( txtLocalProd, 194, 260, 100, 20, "Local.armz." , false );
		

	}

	public boolean testaCodLote() {

		boolean bRetorno = false;
		boolean bValido = true;
		if ( txtCodLote.getText().trim().length() > 0 ) {
			bValido = false;
			String sSQL = "SELECT COUNT(*) FROM EQLOTE WHERE CODLOTE=? AND CODPROD=?" + " AND CODEMP=? AND CODFILIAL=?";
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = con.prepareStatement( sSQL );
				ps.setString( 1, txtCodLote.getText().trim() );
				ps.setInt( 2, txtCodProd.getVlrInteger().intValue() );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, lcLote.getCodFilial() );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					if ( rs.getInt( 1 ) > 0 ) {
						bValido = true;
					}
				}
				rs.close();
				ps.close();
				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao consultar a tabela EQLOTE!\n" + err.getMessage(), true, con, err );
			}
		}
		if ( !bValido ) {
			DLLote dl = new DLLote( this, txtCodLote.getText(), txtCodProd.getText(), txtDescProd.getText(), con );
			dl.setVisible( true );
			if ( dl.OK ) {
				bRetorno = true;
				dl.dispose();
			}
			else {
				dl.dispose();
			}
		}
		else {
			bRetorno = true;
		}
		return bRetorno;
	}

	private void testaCodInvProd() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( "SELECT * FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQINVPROD" ) );
			ps.setString( 3, "IV" );
			rs = ps.executeQuery();
			rs.next();
			txtCodInv.setVlrString( rs.getString( 1 ) );
			// rs.close();
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao confirmar código do inventário!\n" + err.getMessage(), true, con, err );
		}
	}

	private int[] prefs() {

		int iRetorno[] = { 0, 0, 0 };
		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sSQL = "SELECT USAREFPROD, CODTIPOMOV6, TIPOPRECOCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ) != null ) {
					if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
						iRetorno[ 0 ] = 1;
				}
				if ( rs.getString( "CODTIPOMOV6" ) != null )
					iRetorno[ 1 ] = rs.getInt( "CODTIPOMOV6" );
				if ( rs.getString( "TIPOPRECOCUSTO" ) != null ) {
					if ( !rs.getString( "TIPOPRECOCUSTO" ).equals( "M" ) )
						iRetorno[ 2 ] = 1;
				}
			}
			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro carregando preferências!\n" + err.getMessage(), true, con, err );
		} finally {
			rs = null;
			ps = null;
			sSQL = null;
		}
		return iRetorno;
	}

	private boolean comRef() {

		return ( iPrefs[ 0 ] == 1 );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcLote.setConexao( cn );
		lcAlmox.setConexao( cn );
		lcTipoMov.setConexao( cn );
		iPrefs = prefs();
		montaTela();
		// tipoCusto();
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) {
			if ( kevt.getSource() == txtObsInv ) {
				if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT ) {
					lcCampos.post();
					lcCampos.limpaCampos( true );
					lcCampos.setState( ListaCampos.LCS_NONE );
					lcCampos.insert( true );
					txtRefProd.requestFocus();
				}
			}
			else if ( kevt.getSource() == txtCodAlmox || kevt.getSource() == txtCodLote ) {
				txtSldAtualInvP.setVlrBigDecimal( buscaSaldo( txtCodProd.getVlrInteger().intValue(), txtDataInvP.getVlrDate(), txtCodLote.getVlrString() )[ 0 ] );
			}
			else if ( kevt.getSource() == txtSldNovoInvP ) {
				BigDecimal bSAtual = txtSldAtualInvP.getVlrBigDecimal();
				BigDecimal bSNovo = txtSldNovoInvP.getVlrBigDecimal();
				txtSldAtualInvP.setVlrBigDecimal( bSAtual ); // Soh para zerar o
				// campo caso ele
				// estaja vazio.
				txtSldNovoInvP.setVlrBigDecimal( bSNovo ); // Soh para zerar o
				// campo caso ele
				// estaja vazio.
				txtQtdInvP.setVlrBigDecimal( bSNovo.subtract( bSAtual ) );
			}
		}
		super.keyPressed( kevt );
	}

	public void valorAlterado( CheckBoxEvent cbevt ) {

		if ( cbLote.getStatus() ) {
			txtCodLote.setAtivo( true );
			txtCodLote.setRequerido( true );
			bLote = true;
		}
		else {
			txtCodLote.setAtivo( false );
			txtCodLote.setRequerido( false );
			bLote = false;
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {
		try {
			if ( cevt.getListaCampos() == lcCampos ) {
				if ( cevt.ok ) {
					txtCodProd.setAtivo( false );
					txtRefProd.setAtivo( false );
					txtDataInvP.setAtivo( false );
					txtCodLote.setAtivo( false );
				}
			}
			else if ( cevt.getListaCampos() == lcLote ) {
				if ( !bLote )
					setSaldo();
			}
			else if ( ( cevt.getListaCampos() == lcProd ) || ( cevt.getListaCampos() == lcAlmox ) ) {
				setSaldo();
					 
				if ( ( ! (txtPrecoInvP.getVlrBigDecimal().floatValue() > 0) ) && ( txtCustoInfoProd.getVlrBigDecimal().floatValue()>0 ) ){ 
					txtPrecoInvP.setVlrBigDecimal( txtCustoInfoProd.getVlrBigDecimal() );
				}
					
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSaldo() {

		try {
			BigDecimal saldo[] = { new BigDecimal(0), new BigDecimal(0) };
			saldo = buscaSaldo( txtCodProd.getVlrInteger().intValue(), txtDataInvP.getVlrDate(), txtCodLote.getVlrString() );
			txtSldAtualInvP.setVlrBigDecimal( saldo[ 0 ] );
			txtPrecoInvP.setVlrBigDecimal( saldo[ 1 ] );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void beforeInsert( InsertEvent ievt ) {

		txtCodProd.setAtivo( true );
		txtRefProd.setAtivo( true );
		txtDataInvP.setAtivo( true );
		txtCodLote.setAtivo( true );
	}

	public void beforePost( PostEvent pevt ) {

		BigDecimal bSAtual = txtSldAtualInvP.getVlrBigDecimal();
		BigDecimal bSNovo = txtSldNovoInvP.getVlrBigDecimal();
		txtSldAtualInvP.setVlrBigDecimal( bSAtual ); // Soh para zerar o campo
		// caso ele estaja vazio.
		txtSldNovoInvP.setVlrBigDecimal( bSNovo ); // Soh para zerar o campo caso
		// ele estaja vazio.
		txtQtdInvP.setVlrBigDecimal( bSNovo.subtract( bSAtual ) );
		if ( bLote ) {
			if ( !testaCodLote() ) {
				pevt.cancela();
			}
		}
		if ( lcCampos.getStatus() == ListaCampos.LCS_INSERT )
			testaCodInvProd();
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	private BigDecimal[] buscaSaldo( int codprod, Date deFim, String codlote ) {

		// Método que busca saldo através da stored procedure EQMOVPRODSLDSP

		BigDecimal result[] = { new BigDecimal(0), new BigDecimal(0) };
		int param = 1;
		if (!"".equals( codlote ) && (!Funcoes.dateToStrDate( deFim ).equals( Funcoes.dateToStrDate( new Date() ) )) ) {
			Funcoes.mensagemInforma( this, "Não é possível buscar saldo de lote anterior a data atual !" );
		} else {
			String sSQL = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			PreparedStatement ps2 = null;
			ResultSet rs2 = null;
			
			try {
				sSQL = "SELECT NSALDOAX,NCUSTOMPMAX FROM EQMOVPRODSLDSP(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				/*
				 * Parâmetros ICODEMP, SCODFILIAL, ICODMOVPROD, ICODEMPPD, SCODFILIALPD, ICODPROD, DDTMOVPROD, NCUSTOMPMMOVPROD
				 */
				ps = con.prepareStatement( sSQL );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setNull( param++, Types.INTEGER );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				ps.setInt( param++, codprod );
				ps.setDate( param++, Funcoes.dateToSQLDate( deFim ) );
				ps.setDouble( param++, 0 );
				ps.setDouble( param++, 0 );
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "EQALMOX" ) );
				ps.setInt( param++, txtCodAlmox.getVlrInteger().intValue() );
				ps.setString( param++, Aplicativo.sMultiAlmoxEmp );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					result[ 1 ] = rs.getBigDecimal( "NCUSTOMPMAX" );
					if (codlote==null || "".equals( codlote )) {
						result[ 0 ] = rs.getBigDecimal( "NSALDOAX" );
					} else {
						param = 1;
						ps2 = con.prepareStatement( "SELECT SLDLIQLOTE FROM EQSALDOLOTE WHERE CODEMP=? AND CODFILIAL=? AND CODPROD=? AND CODLOTE=? " );
						ps2.setInt( param++, Aplicativo.iCodEmp );
						ps2.setInt( param++, ListaCampos.getMasterFilial( "EQSALDOLOTE" ) );
						ps2.setInt( param++, codprod );
						ps2.setString( param++, codlote );
						rs2 = ps2.executeQuery();
						if (rs2.next()) {
							result[ 0 ] = rs2.getBigDecimal( "SLDLIQLOTE" );
						}
						rs2.close();
						ps2.close();
					}
				}
				rs.close();
				ps.close();
				con.commit();
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar o saldo!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			} finally {
				sSQL = null;
				rs = null;
				ps = null;
			}
		}
		return result;
	}

	public void afterInsert( InsertEvent ievt ) {

		txtDataInvP.setVlrDate( new Date() );
		txtCodTipoMov.setVlrInteger( new Integer( iPrefs[ 1 ] ) );
		lcTipoMov.carregaDados();
	}

	public void afterPost( PostEvent pevt ) {

		if(pevt.getListaCampos()==lcCampos) {
			
			if( ! "".equals(txtLocalProd.getVlrString() )) {
				
					
					atualizaLocal();
					
				
			}
			
		}
		
	}

	private void atualizaLocal() {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		try {
			sql.append("update eqproduto set localprod=? ");
			sql.append("where codemp=? and codfilial=? and codprod=? ");
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setString( 1, txtLocalProd.getVlrString() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( 4, txtCodProd.getVlrInteger() );
			
			ps.execute();
			
			con.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void beforeCarrega( CarregaEvent cevt ) {

	}

	private String tipoCusto() {

		return ( iPrefs[ 2 ] == 0 ? "M" : "P" );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sOrdem = null;
		String sOrdenado = null;
		String sRefCod = null;
		String sOrdemGrupo = null;
		String sDivGrupo = null;
		String sCodgrup = "";
		String sRef = null;
		String sCodgrupFiltro = null;
		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		String sLinhaLarga = StringFunctions.replicate( "=", 133 );
		Integer iCodAlmox = null;
		double deTotal = 0;
		Date dtEstoq = null;
		Object[] oVals = null;
		DLRInventario dl = null;
		ImprimeOS imp = null;

		try {

			dl = new DLRInventario( con, this );
			dl.setVisible( true );
			if ( dl.OK == false ) {
				dl.dispose();
				return;
			}

			imp = new ImprimeOS( "", con );
			int linPag = imp.verifLinPag() - 1;

			oVals = dl.getValores();
			dl.dispose();

			sCodgrupFiltro = (String) oVals[ 1 ];
			sDivGrupo = (String) oVals[ 2 ];
			dtEstoq = (Date) oVals[ 3 ];
			iCodAlmox = (Integer) oVals[ 4 ];

			if ( sDivGrupo.equals( "S" ) )
				sOrdemGrupo = "P.CODGRUP,";
			else
				sOrdemGrupo = "";

			if ( comRef() ) {
				sRefCod = "Ref";
				if ( oVals[ 0 ].equals( "CODPROD" ) ) {
					sOrdem = sOrdemGrupo + "P.REFPROD";
					sOrdenado = "Referência";
				}
				else {
					sOrdenado = "Descrição";
					sOrdem = sOrdemGrupo + "P.DESCPROD";
				}
			}
			else {
				sRefCod = "Cod";
				if ( oVals[ 0 ].equals( "CODPROD" ) ) {
					sOrdem = sOrdemGrupo + "P.CODPROD";
					sOrdenado = "Código";
				}
				else {
					sOrdenado = "Descrição";
					sOrdem = sOrdemGrupo + "P.DESCPROD";
				}
			}

			sSQL = "SELECT P.CODPROD,P.REFPROD,P.DESCPROD,P.CODGRUP, P.SALDO, P.CUSTO, P.VLRESTOQ " + "FROM EQRELINVPRODSP(?,?,?,?,?,?,?,?,?,?) P " + "ORDER BY " + sOrdem;

			/*
			 * Parâmetros da procedure ICODEMP, ICODFILIAL, CTIPOCUSTO, ICODEMPGP, SCODFILIALGP, CCODGRUP, DDTESTOQ
			 */
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setString( 3, tipoCusto() );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "EQGRUPO" ) );
			if ( sCodgrupFiltro.trim().equals( "" ) )
				ps.setNull( 6, Types.CHAR );
			else
				ps.setString( 6, sCodgrupFiltro );
			ps.setDate( 7, Funcoes.dateToSQLDate( Funcoes.dateToSQLDate( dtEstoq ) ) );

			ps.setInt( 8, Aplicativo.iCodEmp );
			ps.setInt( 9, ListaCampos.getMasterFilial( "EQALMOX" ) );
			ps.setInt( 10, iCodAlmox );

			rs = ps.executeQuery();

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Inventário" );
			imp.addSubTitulo( "POSIÇÃO DO ESTOQUE EM " + Funcoes.dateToStrDate( dtEstoq ) );
			imp.addSubTitulo( sOrdenado );

			while ( rs.next() ) {

				if ( imp.pRow() >= ( linPag - 1 ) ) {

					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.incPags();
					imp.eject();

				}
				else if ( "S".equals( sDivGrupo ) ) {

					if ( !sCodgrup.equals( rs.getString( "Codgrup" ) ) ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.incPags();
						imp.eject();

					}

				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );

					if ( "S".equals( sDivGrupo ) ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + sLinhaFina + "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" );
						imp.say( ( 136 - rs.getString( "CodGrup" ).length() ) / 2, rs.getString( "CodGrup" ) );
						imp.say( 135, "|" );

					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Grupo/" + sRefCod );
					imp.say( 26, "| Descriçao" );
					imp.say( 69, "| Quant." );
					imp.say( 80, "| " + ( tipoCusto().equals( "M" ) ? "Custo MPM" : "Custo PEPS" ) );
					imp.say( 103, "| Total" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + sLinhaFina + "|" );

				}

				if ( "Ref".equals( sRefCod ) )
					sRef = rs.getString( "RefProd" );
				else
					sRef = rs.getString( "CodProd" );

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "| " + Funcoes.copy( rs.getString( "CODGRUP" ) + "/" + sRef, 0, 23 ) );
				imp.say( 26, Funcoes.copy( "| " + rs.getString( "DESCPROD" ), 0, 43 ) );
				imp.say( 69, Funcoes.copy( "| " + rs.getString( "SALDO" ), 0, 11 ) );
				imp.say( 80, "| " + Funcoes.strDecimalToStrCurrency( 20, 2, rs.getString( "CUSTO" ) ) );
				imp.say( 103, "| " + Funcoes.strDecimalToStrCurrency( 28, 2, rs.getString( "VLRESTOQ" ) ) + "  |" );

				deTotal += rs.getDouble( "VLRESTOQ" );
				sCodgrup = rs.getString( "CODGRUP" );

			}

			rs.close();
			ps.close();
			con.commit();

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaLarga + "+" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "|   VALOR TOTAL DO ESTOQUE EM " + Funcoes.dateToStrDate( dtEstoq ) + ": " + Funcoes.strDecimalToStrCurrency( 15, 2, "" + deTotal ).trim() );
			imp.say( 135, "|" );
			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + sLinhaLarga + "+" );
			imp.incPags();

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW )
				imp.preview( this );
			else
				imp.print();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro na consulta à tabela de setores!\n" + err.getMessage(), true, con, err );
		} finally {
			imp = null;
			sOrdem = null;
			sOrdenado = null;
			sRefCod = null;
			sOrdemGrupo = null;
			sDivGrupo = null;
			sCodgrup = null;
			sCodgrupFiltro = null;
			sSQL = null;
			dtEstoq = null;
			oVals = null;
			dl = null;
			ps = null;
			rs = null;
		}

	}

}
