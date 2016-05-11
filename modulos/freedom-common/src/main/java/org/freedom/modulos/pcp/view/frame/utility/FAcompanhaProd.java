/**
 * @version 07/07/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe: @(#)FAgendProd.java <BR>
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
 */
package org.freedom.modulos.pcp.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.gms.business.object.StatusOS;
import org.freedom.modulos.gms.view.frame.crud.plain.FSecaoProd;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.pcp.business.object.StatusOP;
import org.freedom.modulos.pcp.view.frame.crud.detail.FOP;

public class FAcompanhaProd extends FFilho implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private enum ecolAcompanhamentos {

		SITOP, SITATRASO, DTEMITOP, DTFABROP, CODOP, SEQOP, REFPROD, DESCEST, QTDSUG, QTDPREV, QTDTOTAL, TEMPOTOTAL, TOTALFASE

	};

	private JPanelPad pinCab = new JPanelPad( 0, 145 );

	private JPanelPad pnCab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinRod = new JPanelPad( 685, 39 );

	private JPanelPad pnBotoes = new JPanelPad( 200, 200 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JButtonPad btFiltrar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btNovaOp = new JButtonPad( Icone.novo( "btNovo.png" ) );
	
	private JButtonPad btImprimir = new JButtonPad( Icone.novo( "btPrevimp.png" ) );

	private JRadioGroup<?, ?> rgFiltro = null;

	private Vector<String> vValsData = new Vector<String>();

	private Vector<String> vLabsData = new Vector<String>();

	private JCheckBoxPad cbCancelada = new JCheckBoxPad( "Cancelada", "S", "N" );

	private JCheckBoxPad cbPendente = new JCheckBoxPad( "Pendente", "S", "N" );

	private JCheckBoxPad cbFinalizada = new JCheckBoxPad( "Finalizada", "S", "N" );

	private JCheckBoxPad cbAtrasadas = new JCheckBoxPad( "Atrasadas", "S", "N" );

	private JCheckBoxPad cbPrincipal = new JCheckBoxPad( "Principal", "S", "N" );

	private JCheckBoxPad cbRelacionada = new JCheckBoxPad( "Relacionadas", "S", "N" );

	private JCheckBoxPad cbBloqueada = new JCheckBoxPad( "Bloqueada", "S", "N" );

	private Vector<String> vValsStatus = new Vector<String>();

	private Vector<String> vLabsStatus = new Vector<String>();

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JLabelPad lbTxtPendente = new JLabelPad( "Pendente" );

	private JLabelPad lbTxtFinalizada = new JLabelPad( "Finalizada" );

//	private JLabelPad lbTxtAtrasado = new JLabelPad( "Atrasada" );

	private JLabelPad lbTxtCancelado = new JLabelPad( "Cancelada" );

	private JLabelPad lbTxtBloqueado = new JLabelPad( "Bloqueada" );

	private ImageIcon imgPendente = Icone.novo( "clIndisponivelParc.gif" );

	private ImageIcon imgFinalizada = Icone.novo( "clEfetivado.gif" );

	private ImageIcon imgAtrasada = Icone.novo( "op_atrasada_10x10.png" );

	private ImageIcon imgCancelado = Icone.novo( "clCancelado.gif" );

	private ImageIcon imgBloqueado = Icone.novo( "clOpBloqueada.gif" );

	private JLabelPad lbImgPendente = new JLabelPad( imgPendente );

	private JLabelPad lbImgFinalizada = new JLabelPad( imgFinalizada );

	private JLabelPad lbImgAtrasado = new JLabelPad( imgAtrasada );

	private JLabelPad lbImgCancelado = new JLabelPad( imgCancelado );

	private JLabelPad lbImgBloqueada = new JLabelPad( imgBloqueado );

	private ImageIcon imgStatus = null;
	
	private ImageIcon imgStatus_atraso = null;
	
	private ImageIcon imgBranco = Icone.novo( "clBranco.gif" );
	
	private Font fontLegenda = new Font( "Arial", Font.PLAIN, 9 );

	private Color corLegenda = new Color( 120, 120, 120 );
	
	private ListaCampos lcSecao = new ListaCampos( this );
	
	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );
	
	private JTextFieldFK txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTablePad tabstatus = new JTablePad();
	
	private JScrollPane scpStatus = new JScrollPane( tabstatus );
	
	private ListaCampos lcProd = new ListaCampos( this );
	
	private ListaCampos lcProd2 = new ListaCampos( this );
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	FOP f;

	public FAcompanhaProd() {

		super( true );
		setAtribos( 50, 50, 900, 470 );

		montaTela();

	}

	private void montaTela() {

		adicBotaoSair();

		getTela().add( pnCab, BorderLayout.CENTER );
		pnCab.add( pinCab, BorderLayout.NORTH );
		pnCab.add( spnTab, BorderLayout.CENTER );

		pnRod.add( pnBotoes, BorderLayout.CENTER );

		vValsData.addElement( "F" );
		vValsData.addElement( "E" );
		vLabsData.addElement( "Fabricação" );
		vLabsData.addElement( "Emissão" );

		rgFiltro = new JRadioGroup<String, String>( 2, 1, vLabsData, vValsData );
		rgFiltro.setVlrString( "F" );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK ) );		
		
		pinCab.adic(pnPeriodo, 5, 5, 280, 80 );
		
		pnPeriodo.adic( new JLabelPad( "De:" ), 10, 10, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 10, 87, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 130, 10, 37, 20 );
		pnPeriodo.adic( txtDatafim, 155, 10, 87, 20 );
		
		JPanelPad pnTipoOP = new JPanelPad();
		pnTipoOP.setBorder( SwingParams.getPanelLabel( "Tipo", Color.BLACK ) );
		
		pinCab.adic( pnTipoOP, 445, 5, 150, 90 );

		pnTipoOP.adic( new JLabelPad(imgAtrasada), 3, 3, 12, 12 );
		pnTipoOP.adic( cbAtrasadas, 15, 0, 90, 20 );

		pnTipoOP.adic( cbPrincipal, 15, 20, 90, 20 );
		pnTipoOP.adic( cbRelacionada, 15, 40, 105, 20 );

		
		pinCab.adic( scpStatus, 605, 13, 150, 80 );
		
		pinCab.adic( txtCodSecao, 7, 110, 80, 20, "Cód.Seção" );
		pinCab.adic( txtDescSecao, 90, 110, 340, 20, "Descrição da seção" );

		
		if(comRef()) {
			pinCab.adic( txtRefProd, 433, 110, 80, 20, "Referência" );
		}
		else {
			pinCab.adic( txtCodProd, 433, 110, 80, 20, "Cód.Prod." );		
		}

		pinCab.adic( txtDescProd, 516, 110, 340, 20, "Descrição do produto" );

		JPanelPad pnTipoData = new JPanelPad();
		pnTipoData.setBorder( SwingParams.getPanelLabel( "Filtrar por:", Color.BLACK ) );
		
		pinCab.adic(pnTipoData, 290,5,150,80);
		
		pnTipoData.adic( rgFiltro, 5, 0, 130, 50 );
		
		pinCab.adic( btFiltrar, 760, 13, 30, 79 );

	
		pnBotoes.adic( btNovaOp, 10, 0, 30, 25 );
		pnBotoes.adic( btImprimir, 41, 0, 30, 25 );

		btNovaOp.setToolTipText( "Nova ordem de produção" );
		btImprimir.setToolTipText( "Imprimir relação" );
		
		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "Emissão" );
		tab.adicColuna( "Fabricação" );
		tab.adicColuna( "O.P." );
		tab.adicColuna( "Seq." );
		tab.adicColuna( "Cod.Pd." );
		tab.adicColuna( "Descrição do produto" );
		tab.adicColuna( "Qtd sug." );
		tab.adicColuna( "Qtd prev." );
		tab.adicColuna( "Qtd tot." );
		tab.adicColuna( "Concl." );
		tab.adicColuna( "Fase" );

		tab.setTamColuna( 10, ecolAcompanhamentos.SITOP.ordinal() );
		tab.setTamColuna( 10, ecolAcompanhamentos.SITATRASO.ordinal() );
		tab.setTamColuna( 68, ecolAcompanhamentos.DTEMITOP.ordinal() );
		tab.setTamColuna( 68, ecolAcompanhamentos.DTFABROP.ordinal() );
		tab.setTamColuna( 45, ecolAcompanhamentos.CODOP.ordinal() );
		tab.setTamColuna( 28, ecolAcompanhamentos.SEQOP.ordinal() );
		tab.setTamColuna( 50, ecolAcompanhamentos.REFPROD.ordinal() );
		tab.setTamColuna( 300, ecolAcompanhamentos.DESCEST.ordinal() );
		tab.setTamColuna( 62, ecolAcompanhamentos.QTDSUG.ordinal() );
		tab.setTamColuna( 62, ecolAcompanhamentos.QTDPREV.ordinal() );
		tab.setTamColuna( 62, ecolAcompanhamentos.QTDTOTAL.ordinal() );
		tab.setTamColuna( 40, ecolAcompanhamentos.TOTALFASE.ordinal() );
		tab.setTamColuna( 55, ecolAcompanhamentos.TEMPOTOTAL.ordinal() );
		
		tab.setRowHeight( 20 );

		Calendar cPeriodo = Calendar.getInstance();

		txtDataini.setVlrDate( cPeriodo.getTime() );

		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) + 30 );

		txtDatafim.setVlrDate( cPeriodo.getTime() );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		btFiltrar.addActionListener( this );
		btNovaOp.addActionListener( this );
		btImprimir.addActionListener( this );
		tab.addMouseListener( this );
		
		cbAtrasadas.setVlrString( "S" );
		cbPrincipal.setVlrString( "S" );
		cbRelacionada.setVlrString( "S" );
		
		montaListaCampos();
		
		montaGridStatus();
		carregaStatus();
		
	}

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = Aplicativo.getInstace().getConexao().prepareStatement( sSQL );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			
			rs = ps.executeQuery();
			
			if ( rs.next() )
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			
			
		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} 
		finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
		return bRetorno;
	}

	
	private void montaListaCampos() {
		
		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da seção", ListaCampos.DB_SI, false ) );
		lcSecao.montaSql( false, "SECAO", "EQ" );
		txtCodSecao.setNomeCampo( "CodSecao" );
		txtCodSecao.setFK( true );
		lcSecao.setReadOnly( true );
		lcSecao.setQueryCommit( false );
		txtCodSecao.setTabelaExterna( lcSecao, FSecaoProd.class.getCanonicalName() );
		
		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.setWhereAdic( "TIPOPROD='F'" );
		txtCodProd.setTabelaExterna( lcProd, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_PK, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "codprod", "Cód.prod.", ListaCampos.DB_SI, false ) );

		txtRefProd.setNomeCampo( "RefProd" );

		lcProd2.setWhereAdic( "ATIVOPROD='S'" );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		lcProd2.setQueryCommit( false );
		lcProd2.setReadOnly( true );
		txtRefProd.setTabelaExterna( lcProd2, FProduto.class.getCanonicalName() );


		
	}
	
	private void montaGridStatus() {

		tabstatus.adicColuna( "" ); // Selecao
		tabstatus.adicColuna( "Cod." ); // Codigo
		tabstatus.adicColuna( "" ); // Imagem
		tabstatus.adicColuna( "Status" ); // Descrição

		tabstatus.setTamColuna( 10, 0 );

		tabstatus.setColunaInvisivel( 1 );

		tabstatus.setTamColuna( 10, 2 );
		tabstatus.setTamColuna( 100, 3 );

		tabstatus.setRowHeight( 15 );

		tabstatus.setColunaEditavel( 0, new Boolean( true ) );

	}
	
	private void carregaStatus() {

		Vector<Object> valores = StatusOP.getValores();
		Vector<String> labels = StatusOP.getLabels();
		Vector<ImageIcon> icones = new Vector<ImageIcon>();

		Vector<Object> item = null;

		for ( int i = 0; i < valores.size(); i++ ) {

			item = new Vector<Object>();

			String valor = valores.elementAt( i ).toString();
			String label = labels.elementAt( i );
			ImageIcon icon = StatusOP.getImagem( valor, StatusOP.IMG_TAMANHO_P );

			if ( StatusOP.OP_FINALIZADA.getValue().equals( valor ) ) {
				item.addElement( new Boolean( false ) );
			}
			else {
				item.addElement( new Boolean( true ) );
			}

			item.addElement( valor );
			item.addElement( icon );
			item.addElement( label );

			tabstatus.adicLinha( item );

		}

	}

	
	private ResultSet execConsulta() {
		ResultSet ret = null;
		
		try {

			if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
	
				Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
				return null;
			}
	
			StringBuffer sql = new StringBuffer();
			StringBuffer sWhere = new StringBuffer();
			StringBuffer sWhere2 = new StringBuffer();
			StringBuilder sOrderBy = new StringBuilder();
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean or = false;
			boolean order = false;
			String sData = "";	
		
			sql.append( "SELECT SITOP,DTEMITOP,DTFABROP,CODOP,SEQOP,DESCEST,CODPROD,REFPROD,CODSECAO, REFPROD, " );
			sql.append( "CAST( QTDSUG AS DECIMAL(15,2)) QTDSUG," );
			sql.append( "CAST( QTDPREV AS DECIMAL(15,2)) QTDPREV," );
			sql.append( "CAST( QTDFINAL AS DECIMAL(15,2)) QTDFINAL," );
			sql.append( "CAST((TEMPOFIN*100/( CASE WHEN COALESCE(TEMPOTOT,0)=0 THEN 1 ELSE TEMPOTOT END  )) AS DECIMAL (15,2)) TEMPO, " );
			sql.append( "FASEATUAL,TOTFASES FROM PPLISTAOPVW01 " );
			
			if ( "F".equals( rgFiltro.getVlrString() ) ) {
				sData = "DTFABROP";
			}
			else if ( "E".equals( rgFiltro.getVlrString() ) ) {
				sData = "DTEMITOP";
			}
			
			sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND " + sData + " BETWEEN ? AND ? " );
			
		
			
			if(txtCodSecao.getVlrString()!=null && !"".equals( txtCodSecao.getVlrString()) ) {
				
				sql.append( " AND CODSECAO = ? " );
				
			}
			
			if( txtCodProd.getVlrInteger()>0  ) {
				
				sql.append( " AND  CODPROD=? " );
				
			}
			
			StringBuffer status = new StringBuffer( "" );
	
			boolean primeiro = true;
			
			for ( int i = 0; i < tabstatus.getNumLinhas(); i++ ) {
	
				if ( (Boolean) tabstatus.getValor( i, 0 ) ) {
	
					if ( primeiro ) {
						sql.append( " and sitop in (" );
					}
					else {
						sql.append( "," );
					}
	
					sql.append( "'" + tabstatus.getValor( i, 1 ) + "'" );
	
					primeiro = false;
				}
	
				if ( i == tabstatus.getNumLinhas() - 1 && !primeiro ) {
					sql.append( " ) " );
				}
	
			}
			
			
			if ( "S".equals( cbAtrasadas.getVlrString() ) ) {
	
				sql.append( " and (SITOP='PE' AND DTFABROP < CAST( 'NOW' AS DATE )) " );
				
			}
			
			if (!( "S".equals( cbPrincipal.getVlrString() ) && "S".equals( cbRelacionada.getVlrString() )) ) {
	
				if ( "S".equals( cbRelacionada.getVlrString() ) ) {
	
					sql.append( "AND SEQOP<>0 " );			
	
				}
				else {
					sql.append( "AND SEQOP=0 " );
				}
		
				
			}
			
			sql.append( sOrderBy.toString() );
			System.out.println( sql.toString() );

			ps = con.prepareStatement( sql.toString() );
			
			int iparam = 1;
			
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			
			if(txtCodSecao.getVlrString()!=null && !"".equals( txtCodSecao.getVlrString()) ) {
				
				ps.setString( iparam++, txtCodSecao.getVlrString() );
				
			}
			
			if( txtCodProd.getVlrInteger()>0  ) {
				
				ps.setInt( iparam++, txtCodProd.getVlrInteger() );
				
			}
			
			ret = ps.executeQuery();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	private void montaGrid() {

		try {

			ResultSet rs = execConsulta();
			
			tab.limpa();

			boolean atrasado = false;
			
			for ( int i = 0; rs.next(); i++ ) {

				Date dtfab = Funcoes.getDataPura( Funcoes.sqlDateToDate( rs.getDate( "DTFABROP" ) ) );
				Date dtatual = Funcoes.getDataPura( new Date() );

				atrasado = dtfab.before( dtatual );
				
				
				
				if(StatusOP.OP_PENDENTE.getValue().equals( rs.getString( "SITOP" ) )) { 
				
					if(atrasado) {
						imgStatus_atraso = imgAtrasada;
					}
					else {
						imgStatus_atraso = imgBranco;
					}
				}
				else {
					imgStatus_atraso = imgBranco;
				}

				imgStatus = StatusOP.getImagem( rs.getString( "SITOP" ), StatusOS.IMG_TAMANHO_P );

				tab.adicLinha();
				tab.setValor( imgStatus, i, ecolAcompanhamentos.SITOP.ordinal() );
				tab.setValor( imgStatus_atraso, i, ecolAcompanhamentos.SITATRASO.ordinal() );
				tab.setValor( rs.getDate( "DTEMITOP" ), i, ecolAcompanhamentos.DTEMITOP.ordinal() );
				tab.setValor( rs.getDate( "DTFABROP" ), i, ecolAcompanhamentos.DTFABROP.ordinal() );
				tab.setValor( rs.getInt( "CODOP" ), i, ecolAcompanhamentos.CODOP.ordinal() );
				tab.setValor( rs.getInt( "SEQOP" ), i, ecolAcompanhamentos.SEQOP.ordinal() );
				tab.setValor( rs.getString( "REFPROD" ), i, ecolAcompanhamentos.REFPROD.ordinal() );
				tab.setValor( rs.getString( "DESCEST" ), i, ecolAcompanhamentos.DESCEST.ordinal() );
				tab.setValor( rs.getBigDecimal( "QTDSUG" ), i, ecolAcompanhamentos.QTDSUG.ordinal() );
				tab.setValor( rs.getBigDecimal( "QTDPREV" ), i, ecolAcompanhamentos.QTDPREV.ordinal() );
				tab.setValor( rs.getBigDecimal( "QTDFINAL" ), i, ecolAcompanhamentos.QTDTOTAL.ordinal() );
				tab.setValor( rs.getBigDecimal( "TEMPO" ) + "%", i, ecolAcompanhamentos.TEMPOTOTAL.ordinal() );
				tab.setValor( rs.getBigDecimal( "FASEATUAL" ) + "/" + rs.getBigDecimal( "TOTFASES" ), i, ecolAcompanhamentos.TOTALFASE.ordinal() );

			}

			con.commit();

		} catch ( Exception e ) {

			Funcoes.mensagemInforma( this, "Erro ao montar grid " + e.getMessage() );

			e.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent e ) {

		try {
			if ( e.getSource() == btFiltrar ) {
				montaGrid();
			}
			else if ( e.getSource() == btNovaOp ) {

				if ( Aplicativo.telaPrincipal.temTela( "Ordens de produção" ) == false ) {
					f = new FOP( true );
					f.setTelaPrim( Aplicativo.telaPrincipal );
					Aplicativo.telaPrincipal.criatela( "Ordens de produção", f, con );
				}
			}
			else if ( e.getSource() == btImprimir ) {
				
				FPrinterJob dlGr = null;
				HashMap<String, Object> hParam = new HashMap<String, Object>();

				hParam.put( "CODEMP", Aplicativo.iCodEmp );
				hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "PPOP" ) );
				hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
				hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );

				dlGr = new FPrinterJob( "layout/rel/REL_ACOMP_PROD_01.jasper", "Relatório de acompanhamento da produção", "", execConsulta(), hParam, this );

				dlGr.setVisible( true );
				
			}
			
		} 
		catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao abrir OP!\n", true, con, err );
		}
	}

	public void mouseClicked( MouseEvent mevt ) {

		JTablePad tabEv = (JTablePad) mevt.getSource();

		if ( mevt.getClickCount() == 2 ) {

			Integer codOp = (Integer) tab.getValor( tab.getLinhaSel(), ecolAcompanhamentos.CODOP.ordinal() );
			Integer seqOp = (Integer) tab.getValor( tab.getLinhaSel(), ecolAcompanhamentos.SEQOP.ordinal() );

			if ( tabEv == tab && tabEv.getLinhaSel() >= 0 ) {

				if ( Aplicativo.telaPrincipal.temTela( "Ordens de produção" ) == false ) {

					f = new FOP( codOp, seqOp );
					Aplicativo.telaPrincipal.criatela( "Ordens de produção", f, con );
				}
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
		
		lcSecao.setConexao( con );
		lcProd.setConexao( con );
		lcProd2.setConexao( con );

	}


}


