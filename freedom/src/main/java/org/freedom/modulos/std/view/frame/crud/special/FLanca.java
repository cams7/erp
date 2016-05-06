/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FLanca.java <BR>
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
 *         Tela de visualização de lançamentos financeiros.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.freedom.acao.TabelaEditEvent;
import org.freedom.acao.TabelaEditListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrincipal;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modulos.fnc.business.object.Cheque;
import org.freedom.modulos.fnc.view.dialog.utility.DLBuscaLancaValor;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaPag;
import org.freedom.modulos.fnc.view.frame.crud.detail.FCheque;
import org.freedom.modulos.fnc.view.frame.crud.plain.FSinalizadores;
import org.freedom.modulos.fnc.view.frame.utility.FManutPag.enum_tab_manut;
import org.freedom.modulos.std.view.frame.crud.special.FSubLanca.COL_VALS;

public class FLanca extends FFilho implements ActionListener, ChangeListener, MouseListener, TabelaEditListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 600, 64 );

	private JPanelPad pnNav = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 7 ) );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCentro = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );;

	private JPanelPad pinPeriodo = new JPanelPad( 260, 50 );

	private JLabelPad lbPeriodo = new JLabelPad( " Período" );

	private JPanelPad pinData = new JPanelPad( 100, 54 );

	private JPanelPad pinSaldo = new JPanelPad( 100, 54 );

	private JPanelPad pinSaldoComposto = new JPanelPad( 100, 54 );

	private JPanelPad pinAtualiza = new JPanelPad( 100, 54 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btCalcSaldo = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btPrim = new JButtonPad( Icone.novo( "btPrim.png" ) );

	private JButtonPad btAnt = new JButtonPad( Icone.novo( "btAnt.png" ) );

	private JButtonPad btProx = new JButtonPad( Icone.novo( "btProx.png" ) );

	private JButtonPad btUlt = new JButtonPad( Icone.novo( "btUlt.png" ) );

	private JButtonPad btNovo = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluir = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private JButtonPad btEditar = new JButtonPad( Icone.novo( "btEditar.gif" ) );

	private JLabelPad lbA = new JLabelPad( "à" );

	private JPanelPad pinLbPeriodo = new JPanelPad( 53, 15 );

	private JLabelPad lbDataSaldo = new JLabelPad( "Data" );

	private JLabelPad lbSaldo = new JLabelPad( "Saldo" );

	private JLabelPad lbSaldoComposto = new JLabelPad( "Saldo Composto" );

	private JLabelPad lbAtualSaldo = new JLabelPad( "Atualiza" );

	private JLabelPad lbDataSaldoVal = new JLabelPad( "" );

	private JTextFieldPad txtVlrSaldo = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 ) ;

	private JTextFieldPad txtVlrSaldoComposto = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 ) ;

	private JLabelPad lbAtualSaldoVal = new JLabelPad( "NÃO" );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JTabbedPanePad tpn = new JTabbedPanePad();

	private String[] sPlanos = null;

	private String sCodPlan = "";

	private String[] sContas = null;

	private String sConta = "";

	private Date dIniLanca = null;

	private Date dFimLanca = null;
	
	private JPopupMenu menuCores = new JPopupMenu();

	private JButtonPad btAbreCheque = new JButtonPad( Icone.novo( "btCheque.png" ) );

	private JButtonPad btBuscaLancaValor = new JButtonPad( Icone.novo( "btPdvGravaMoeda.png" ) );
	
	private JMenuItem menu_limpa_cor_linha = new JMenuItem();
	
	private JMenuItem menu_limpa_cor_tudo = new JMenuItem();
	
	private JMenuItem menu_cadastra_cor = new JMenuItem();
	
	private static int X = 20;
	
	private static int Y = 25;
	
	private static int L = 920;
	
	private static int A = 750;
	
	boolean Ctrl = false;
	
	private enum enum_tab_lanca {  
		CODLANCA, DATASUBLANCA, TRANSFLANCA, ORIGSUBLANCA, NUMCONTA, VLRSUBLANCA, HISTBLANCA, CHEQUES, DOCLANCA, CODPAG, CODREC, NPARCPAG, SEQCHEQ, COR  };

		public FLanca() {

			super( false );

			setTitulo( "Lançamentos Financeiros" );
			
			Container c = getContentPane();

			c.setLayout( new BorderLayout() );

			pnRod.setPreferredSize( new Dimension( 600, 32 ) );

			c.add( pnRod, BorderLayout.SOUTH );
			c.add( pnCentro, BorderLayout.CENTER );
			c.add( pinCab, BorderLayout.NORTH );

			tab.setiDecimal( Aplicativo.casasDecFin );
			
			tpn.setTabLayoutPolicy( JTabbedPanePad.SCROLL_TAB_LAYOUT );
			tpn.setPreferredSize( new Dimension( 600, 30 ) );

			pnCentro.add( tpn, BorderLayout.SOUTH );
			pnCentro.add( spnTab, BorderLayout.CENTER );


			pinPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK ) );

			pinCab.adic( pinPeriodo, 7, 0, 270, 54 );

			pinPeriodo.adic( txtDataini, 7, 0, 100, 20 );
			pinPeriodo.adic( lbA, 110, 0, 7, 20 );
			pinPeriodo.adic( txtDatafim, 120, 0, 97, 20 );
			pinPeriodo.adic( btExec, 220, 0, 30, 20 );

			btExec.setContentAreaFilled(false);
			btExec.setBorderPainted(false);

			pinCab.adic( pinData, 280, 0, 125, 54 );
			pinData.setBorder( SwingParams.getPanelLabel( "Data", Color.BLACK ) );

			lbDataSaldoVal.setForeground( new Color( 0, 140, 0 ) );
			lbDataSaldoVal.setFont( SwingParams.getFontboldmax() );
			pinData.adic( lbDataSaldoVal, 7, 3, 110, 15 );

			pinCab.adic( pinSaldo, 405, 0, 135, 54 );
			pinSaldo.setBorder( SwingParams.getPanelLabel( "Saldo", Color.BLACK ) );

			pinSaldo.adic( txtVlrSaldo, 10, 3, 120, 20 );
			txtVlrSaldo.setForeground( new Color( 0, 140, 0 ) );

			txtVlrSaldo.setBorder( null );
			txtVlrSaldo.setBackground( null );

			txtVlrSaldo.setFont( SwingParams.getFontboldmax() );

			pinCab.adic( pinSaldoComposto, 540, 0, 135, 54 );
			pinSaldoComposto.setBorder( SwingParams.getPanelLabel( "Saldo composto", Color.BLACK ) );

			pinSaldoComposto.adic( txtVlrSaldoComposto, 10, 3, 120, 20 );
			txtVlrSaldoComposto.setForeground( new Color( 0, 140, 0 ) );

			txtVlrSaldoComposto.setBorder( null );
			txtVlrSaldoComposto.setBackground( null );

			txtVlrSaldoComposto.setFont( SwingParams.getFontboldmax() );

			txtVlrSaldo.setEditable( false );
			txtVlrSaldoComposto.setEditable( false );

			pinCab.adic( pinAtualiza, 675, 0, 120, 54 );
			pinAtualiza.setBorder( SwingParams.getPanelLabel( "Atualizado", Color.BLACK ) );
			pinAtualiza.adic( lbAtualSaldoVal, 10, 2, 57, 15 );
			lbAtualSaldoVal.setFont( SwingParams.getFontboldmax() );

			pinAtualiza.adic( btCalcSaldo, 70, 0, 30, 20 );

			btCalcSaldo.setContentAreaFilled(false);
			btCalcSaldo.setBorderPainted(false);

			btSair.setPreferredSize( new Dimension( 100, 31 ) );

			pnNav.setPreferredSize( new Dimension( 240, 30 ) );

			pnRod.setBorder( BorderFactory.createEtchedBorder() );
			pnRod.add( btSair, BorderLayout.EAST );
			pnRod.add( pnNav, BorderLayout.WEST );

			pnNav.add( btPrim );
			pnNav.add( btAnt );
			pnNav.add( btProx );
			pnNav.add( btUlt );
			pnNav.add( btNovo );
			pnNav.add( btExcluir );
			pnNav.add( btEditar );
			pnNav.add( btAbreCheque );
			pnNav.add( btBuscaLancaValor );
			
			btAbreCheque.setEnabled( false );
			//CODLANCA, DATASUBLANCA, TRANSFLANCA, ORIGSUBLANCA, NUMCONTA, VLRSUBLANCA, CHEQUES, DOCLANCA, HISTBLANCA, CODPAG, NPARCPAG, SEQCHEQ, COR  };
			tab.adicColuna( "NºLanç." );
			tab.adicColuna( "Data" );
			tab.adicColuna( "Tsf." );
			tab.adicColuna( "Orig." );
			tab.adicColuna( "Conta tsf." );

			tab.adicColuna( "Valor" );
			tab.adicColuna( "Histórico" );

			tab.adicColuna( "Cheques" );
			tab.adicColuna( "Nº doc." );
			
			tab.adicColuna( "Cod.Pag." );
			tab.adicColuna( "Cod.Rec." );
			tab.adicColuna( "N.Parc.Pag." );
			tab.adicColuna( "Seq.Cheque" );
			tab.adicColuna( "Cor" );

			tab.setTamColuna( 60, enum_tab_lanca.CODLANCA.ordinal() );
			tab.setTamColuna( 90, enum_tab_lanca.DATASUBLANCA.ordinal() );
			tab.setTamColuna( 90, enum_tab_lanca.DOCLANCA.ordinal() );
			tab.setTamColuna( 100, enum_tab_lanca.VLRSUBLANCA.ordinal() );
			tab.setTamColuna( 400, enum_tab_lanca.HISTBLANCA.ordinal() );		
			tab.setTamColuna( 72, enum_tab_lanca.NUMCONTA.ordinal() );
			tab.setTamColuna( 80, enum_tab_lanca.CHEQUES.ordinal() );

			tab.setColunaInvisivel( enum_tab_lanca.TRANSFLANCA.ordinal() );
			tab.setColunaInvisivel( enum_tab_lanca.ORIGSUBLANCA.ordinal() );
			tab.setColunaInvisivel( enum_tab_lanca.NUMCONTA.ordinal() );

			tab.setColunaInvisivel( enum_tab_lanca.CODPAG.ordinal() );
			tab.setColunaInvisivel( enum_tab_lanca.CODREC.ordinal() );
			tab.setColunaInvisivel( enum_tab_lanca.NPARCPAG.ordinal() );
			tab.setColunaInvisivel( enum_tab_lanca.SEQCHEQ.ordinal() );

			tab.setTamColuna( 65, enum_tab_lanca.COR.ordinal() );
			tab.setColunaInvisivel( enum_tab_lanca.COR.ordinal() );

			tab.addMouseListener( this );
			tab.addTabelaEditListener( this );

			tab.setRowHeight( 22 );
			tab.setFont( SwingParams.getFontboldmedmax() );

			btSair.addActionListener( this );
			btPrim.addActionListener( this );
			btAnt.addActionListener( this );
			btProx.addActionListener( this );
			btUlt.addActionListener( this );
			btNovo.addActionListener( this );
			btEditar.addActionListener( this );
			btExcluir.addActionListener( this );
			btExec.addActionListener( this );
			btCalcSaldo.addActionListener( this );
			btAbreCheque.addActionListener( this );
			btBuscaLancaValor.addActionListener( this );
			
			tpn.addChangeListener( this );

			Calendar cPeriodo = Calendar.getInstance();

			txtDatafim.setVlrDate( cPeriodo.getTime() );
			cPeriodo.set( Calendar.DAY_OF_YEAR, cPeriodo.get( Calendar.DAY_OF_YEAR ) - 7 );
			txtDataini.setVlrDate( cPeriodo.getTime() );
			
			addKeyListener(this);
			addInternalFrameListener(this);
			c.addKeyListener( this );
			txtDataini.addKeyListener( this );
			txtDatafim.addKeyListener( this );
			
			btBuscaLancaValor.setToolTipText( "Localiza valor - (Ctrl + N)" );

		}

		private void mostraSaldoComposto(boolean mostra) {

			txtVlrSaldoComposto.setVisible( mostra );
			lbSaldoComposto.setVisible( mostra );

		}

		private void montaTabela( Date dini, Date dfim, BigDecimal valor1, BigDecimal valor2, boolean filtraperiodo ) {

			/*sql.append( "SELECT S.CODLANCA, S.DATASUBLANCA, COALESCE(L.TRANSFLANCA,'') TRANSFLANCA, COALESCE(S.ORIGSUBLANCA,'') ORIGSUBLANCA," ); 
			sql.append( "COALESCE(L.DOCLANCA,'') DOCLANCA,S.VLRSUBLANCA,COALESCE(L.HISTBLANCA,'') HISTBLANCA," );
			
			sql.append( "COALESCE((SELECT C.NUMCONTA FROM FNSUBLANCA S1,FNCONTA C " );				
			sql.append( "WHERE S1.CODSUBLANCA=0 AND S1.CODLANCA=S.CODLANCA AND ");
			sql.append( "S1.CODEMP=S.CODEMP AND S1.CODFILIAL=S.CODFILIAL AND " );
			sql.append( "C.CODPLAN=S1.CODPLAN AND C.CODEMP=S1.CODEMPPN AND " );
			sql.append( "C.CODFILIAL=S1.CODFILIALPN ),'') NUMCONTA, ");
			
			sql.append( "L.CODPAG, L.CODREC, L.NPARCPAG, SN.CORSINAL, L.CODSINAL, ");
				
			// Verifica se existe cheque para buscar...
			sql.append( "coalesce((select count(*) from fnpagcheq pc where pc.codemp=l.codemp and pc.codfilial=l.codfilial and pc.codpag=l.codpag and pc.nparcpag=l.nparcpag),0) temcheque " );
			
			sql.append( " FROM FNSUBLANCA S, FNLANCA L ");
			sql.append( " LEFT OUTER JOIN FNSINAL SN ON SN.CODEMP=L.CODEMPSN AND SN.CODFILIAL=L.CODFILIALSN AND SN.CODSINAL=L.CODSINAL ");
				
			sql.append( " WHERE ");

			sql.append( " S.CODLANCA = L.CODLANCA AND S.CODEMP=L.CODEMP AND S.CODFILIAL=L.CODFILIAL"); 
			sql.append( " AND S.CODPLAN = ? AND L.CODEMP=? AND L.CODFILIAL=? " );
			*/

			tab.setRowSorter( null );
			tab.limpa();

			StringBuilder sql = new StringBuilder();
			
			sql.append("select s.codlanca, s.datasublanca, coalesce(l.transflanca,'') transflanca ");
			sql.append(", coalesce(s.origsublanca,'') origsublanca, coalesce(l.doclanca,'') doclanca ");
			sql.append(", s.vlrsublanca, coalesce(l.histblanca,'') histblanca ");
			sql.append(", coalesce(c.numconta,'') numconta ");
			sql.append(", l.codpag, l.codrec, l.nparcpag, sn.corsinal, l.codsinal ");
			sql.append(", coalesce((select count(*) from fnpagcheq pc ");
			sql.append("where pc.codemp=l.codemp and pc.codfilial=l.codfilial ");
			sql.append("and pc.codpag=l.codpag and pc.nparcpag=l.nparcpag),0) temcheque ");
			sql.append("from fnsublanca s ");
			sql.append("inner join fnlanca l on ");
			sql.append("l.codemp=s.codemp and l.codfilial=s.codfilial and l.codlanca=s.codlanca ");
			sql.append("left outer join fnsublanca s1 on ");
			sql.append("s1.codlanca=s.codlanca and s1.codemp=s.codemp and s1.codfilial=s.codfilial ");
			sql.append("and s1.codsublanca=0 ");
			sql.append("left outer join fnconta c on ");
			sql.append("c.codemp=s1.codemppn and c.codfilial=s1.codfilialpn and c.codplan=s1.codplan ");
			sql.append("left outer join fnsinal sn on ");
			sql.append("sn.codemp=l.codempsn and sn.codfilial=l.codfilialsn and sn.codsinal=l.codsinal ");
			sql.append("where s.codplan=? and s.codemp=? and s.codfilial=? ");
			 
			if(filtraperiodo) {
				sql.append( " and s.datasublanca between ? and ? ");
			}
			if( (valor1!=null && valor2==null) || (valor1==null && valor2!=null) ) {
				sql.append( " and abs(l.vlrlanca) = ? ");
			}
			else if( valor1!=null && valor2!=null ) {
				sql.append( " and abs(l.vlrlanca) between ? and ? ");
			}

			sql.append("order by s.codplan, s.datasublanca, s.codlanca ");
			
			try {

				PreparedStatement ps = con.prepareStatement( sql.toString() );

				int iparam = 1;
				
				ps.setString( iparam++, sCodPlan );
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
				if(filtraperiodo) {
					ps.setDate( iparam++, Funcoes.dateToSQLDate( dini ) );
					ps.setDate( iparam++, Funcoes.dateToSQLDate( dfim ) );
				}
				if( (valor1!=null && valor2==null) || (valor1==null && valor2!=null) ) {
					ps.setBigDecimal( iparam++, valor1==null ? valor2 : valor1 );
				}
				else if( valor1!=null && valor2!=null ) {
					ps.setBigDecimal( iparam++, valor1 );
					ps.setBigDecimal( iparam++, valor2 );
				}

				ResultSet rs = ps.executeQuery();

				int row = 0;
				
				Vector<Cheque> cheques = null;
				
				for ( int i = 0; rs.next(); i++ ) {

					tab.adicLinha();
					
					Color corsinal = rs.getString( "corsinal" ) == null ? null : new Color(rs.getInt( "corsinal" ));
					
					tab.setValor( rs.getString( enum_tab_lanca.CODLANCA.name() ), i, enum_tab_lanca.CODLANCA.ordinal(), corsinal );
					tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( enum_tab_lanca.DATASUBLANCA.name() ) ), i, enum_tab_lanca.DATASUBLANCA.ordinal(), corsinal );
					tab.setValor( rs.getString( enum_tab_lanca.TRANSFLANCA.name() ), i, enum_tab_lanca.TRANSFLANCA.ordinal(), corsinal );

					if ( "S".equals( rs.getString( enum_tab_lanca.TRANSFLANCA.name() ) ) ) {
						tab.setValor( rs.getString(enum_tab_lanca.NUMCONTA.name()), i, enum_tab_lanca.NUMCONTA.ordinal(), corsinal );
					} else {
						tab.setValor( "", i, enum_tab_lanca.NUMCONTA.ordinal(), corsinal );
					}

					tab.setValor( rs.getString( enum_tab_lanca.ORIGSUBLANCA.name()) , i, enum_tab_lanca.ORIGSUBLANCA.ordinal(), corsinal );
					tab.setValor( " " + rs.getString( enum_tab_lanca.DOCLANCA.name()).trim(), i, enum_tab_lanca.DOCLANCA.ordinal(), corsinal );
					
					BigDecimal valor = rs.getBigDecimal(enum_tab_lanca.VLRSUBLANCA.name() ).setScale( 2, BigDecimal.ROUND_CEILING );
					tab.setValor( valor, i, enum_tab_lanca.VLRSUBLANCA.ordinal(), corsinal );
					
//					tab.setValor( Funcoes.bdToStr( rs.getBigDecimal( enum_tab_lanca.VLRSUBLANCA.name() ) ), i, enum_tab_lanca.VLRSUBLANCA.ordinal(), corsinal );
					tab.setValor( " " + rs.getString( enum_tab_lanca.HISTBLANCA.name()).trim(), i, enum_tab_lanca.HISTBLANCA.ordinal(), corsinal );				
					tab.setValor( rs.getString( enum_tab_lanca.CODPAG.name()), i, enum_tab_lanca.CODPAG.ordinal(), corsinal );
					tab.setValor( rs.getString( enum_tab_lanca.CODREC.name()), i, enum_tab_lanca.CODREC.ordinal(), corsinal );
					tab.setValor( rs.getString( enum_tab_lanca.NPARCPAG.name()), i, enum_tab_lanca.NPARCPAG.ordinal(), corsinal );

					if( rs.getInt( "temcheque" )>0) {
						
						cheques = DLEditaPag.buscaCheques( rs.getInt( enum_tab_manut.CODPAG.name()), rs.getInt( enum_tab_manut.NPARCPAG.name() ), con);
						
						Vector<String> numcheques = new Vector<String>();
						Vector<String> seqcheques = new Vector<String>();
							
						for ( int ic = 0; cheques.size() > ic; ic++ ) {
							Cheque cheque = (Cheque) cheques.get( ic );
							numcheques.add( cheque.getNumcheq().toString() );
							seqcheques.add( cheque.getSeqcheq().toString() );
						}
							
						tab.setValor( " " + numcheques, i, enum_tab_lanca.CHEQUES.ordinal(), corsinal );
						tab.setValor( seqcheques, i, enum_tab_lanca.SEQCHEQ.ordinal(), corsinal );
						
					}
					else {
						tab.setValor( "", i, enum_tab_lanca.CHEQUES.ordinal(), corsinal );
						tab.setValor( "", i, enum_tab_lanca.SEQCHEQ.ordinal(), corsinal );
					}
					
					tab.setValor( rs.getString( "codsinal" ) == null ? 0 : rs.getInt( "codsinal" ), i, enum_tab_lanca.COR.ordinal(), corsinal );
					
					row++;
				}
				
				// Permitindo reordenação
				if ( row > 0 ) {
					RowSorter<TableModel> sorter = new TableRowSorter<TableModel>( tab.getModel() );
					tab.setRowSorter( sorter );
				} else {
					tab.setRowSorter( null );
				}

				rs.close();
				ps.close();

				atualizaSaldo();
				atualizaSaldoComposto();

			} 
			catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao montar a tabela!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
		}
		
		private void buscaValorLanca() {
		
			DLBuscaLancaValor dl = new DLBuscaLancaValor( this );
			
			dl.setVisible( true );
	
			if ( dl.OK ) {
				
				montaTabela( txtDataini.getVlrDate(), txtDatafim.getVlrDate(), dl.getValor1(), dl.getValor2(), dl.getFiltroData() );
				
				dl.dispose();
			}
			else {
				dl.dispose();
			}
		}
		
		public void valorAlterado( TabelaEditEvent evt ) {}
		
		/*

		class ComboBoxRenderer extends JComboBoxPad implements TableCellRenderer {

			private static final long serialVersionUID = 1L;

			public ComboBoxRenderer(Vector<?> labels, Vector<?> valores, int tipo, int tamanho, int dec) {
				super(labels, valores, tipo, tamanho, dec);
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (isSelected) {
					setForeground(table.getSelectionForeground());
					super.setBackground(table.getSelectionBackground());
				} 
				else {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				}
				setSelectedItem(value);
				return this;
			}
		}
		


		class ComboBoxEditor extends DefaultCellEditor {
			private static final long serialVersionUID = 1L;

			public ComboBoxEditor(Vector<?> labels, Vector<?> valores, int tipo, int tamanho, int dec) {
				super(new JComboBoxPad(labels, valores, tipo, tamanho, dec));
			}
		}
		
		*/

		@ SuppressWarnings ( "unchecked" )
		private void abreCheque() {

			if ( tab.getLinhaSel() > -1 ) {

				Integer seqcheque = null;
				Vector<String> seqcheq = (Vector<String>) tab.getValor( tab.getLinhaSel(), enum_tab_lanca.SEQCHEQ.ordinal()); 

				if(seqcheq!=null) {
					seqcheque = Integer.parseInt( (String) seqcheq.elementAt( 0 ) );
				}

				if(seqcheque!=null) {

					FCheque tela = null;

					if ( Aplicativo.telaPrincipal.temTela( FCheque.class.getName() ) ) {
						tela = (FCheque) Aplicativo.telaPrincipal.getTela( FCheque.class.getName() );
					}
					else {
						tela = new FCheque();
						Aplicativo.telaPrincipal.criatela( "Cheque", tela, con );
					}

					if(seqcheque>0) {
						tela.exec( seqcheque );
					}
				}

			}

		}

		private void montaTabs() {

			tpn.setTabPlacement( SwingConstants.BOTTOM );
			/*
			 * String sSQL = "SELECT (SELECT COUNT(C1.NUMCONTA) FROM FNCONTA C1,FNPLANEJAMENTO P1 "+ "WHERE C1.ATIVACONTA='S' AND P1.NIVELPLAN = 6 AND P1.TIPOPLAN IN ('B','C') AND C1.CODPLAN=P1.CODPLAN" + " AND C1.CODEMP=P1.CODEMP AND C1.CODFILIAL=P1.CODFILIAL AND P1.CODEMP=P.CODEMP" +
			 * " AND P1.CODFILIAL=P.CODFILIAL),P.CODPLAN,C.NUMCONTA,C.DESCCONTA"+ " FROM FNPLANEJAMENTO P,FNCONTA C WHERE C.ATIVACONTA='S' AND P.NIVELPLAN = 6"+ " AND P.TIPOPLAN IN ('B','C') AND C.CODPLAN = P.CODPLAN" +
			 * " AND C.CODEMP = P.CODEMP AND C.CODFILIAL=P.CODFILIAL AND P.CODEMP=? AND P.CODFILIAL=? ORDER BY 4";
			 */

			String sSQL = "SELECT (SELECT COUNT(C1.NUMCONTA) FROM FNCONTA C1,FNPLANEJAMENTO P1 " 
				+ "WHERE C1.ATIVACONTA='S' AND P1.NIVELPLAN = 6 AND P1.TIPOPLAN IN ('B','C') AND C1.CODPLAN=P1.CODPLAN " 
				+ "AND C1.CODEMP=P1.CODEMP AND C1.CODFILIAL=P1.CODFILIAL AND P1.CODEMP=P.CODEMP "
				+ "AND P1.CODFILIAL=P.CODFILIAL),P.CODPLAN,C.NUMCONTA,C.DESCCONTA " 
				+ "FROM FNPLANEJAMENTO P,FNCONTA C WHERE C.ATIVACONTA='S' AND P.NIVELPLAN = 6 " 
				+ "AND P.TIPOPLAN IN ('B','C') AND C.CODPLAN = P.CODPLAN "
				+ "AND C.CODEMP = P.CODEMP AND C.CODFILIAL=P.CODFILIAL AND P.CODEMP=? AND P.CODFILIAL=? " 
				+ "AND ( TUSUCONTA='S' OR EXISTS (SELECT * FROM FNCONTAUSU CU " 
				+ "WHERE CU.CODEMP=C.CODEMP AND CU.CODFILIAL=C.CODFILIAL AND " 
				+ "CU.NUMCONTA=C.NUMCONTA AND CU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND CU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) 
				+ "AND CU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) " + "ORDER BY 4";

			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				ResultSet rs = ps.executeQuery();
				for ( int i = 0; rs.next(); i++ ) {
					if ( i == 0 ) {
						sPlanos = new String[ rs.getInt( 1 ) ];
						sContas = new String[ rs.getInt( 1 ) ];
					}
					sContas[ i ] = rs.getString( "NumConta" );
					sPlanos[ i ] = rs.getString( "CodPlan" );
					tpn.addTab( rs.getString( "DescConta" ).trim(), new JPanelPad( JPanelPad.TP_JPANEL ) );
				}
				rs.close();
				ps.close();
				con.commit();

			} catch ( SQLException err ) {
				System.out.println( "Erro ao montar as tabs!\n" + err.getMessage() );
				err.printStackTrace();
			}
		}

		private void atualizaSaldoComposto() {

			int iCodEmp = 0;
			int iCodFilial = 0;
			StringBuilder sql = new StringBuilder();

			sql.append( "select	cv.numconta, "); 

			sql.append( "coalesce(sum(( ");

			sql.append( "	select s.saldosl from fnsaldolanca s ");
			sql.append( "	where s.codplan=ct2.codplan and s.codemp=ct2.codemppn and s.codfilial=ct2.codfilialpn and s.codemppn=ct2.codemppn and ");
			sql.append( "	s.codfilialpn=ct2.codfilialpn and s.datasl=");
			sql.append( "	(select max(s1.datasl) ");		
			sql.append( "		from fnsaldolanca s1 where s1.datasl <= ? and s1.codplan=s.codplan ");
			sql.append( "		and s1.codemp=s.codemp and s1.codfilial=s.codfilial and s1.codemppn=s.codemppn and s1.codfilialpn=s.codfilialpn ");
			sql.append( "	)" );

			sql.append( ")),0) saldovinculado ");

			sql.append( "from fncontavinculada cv, fnconta ct2, fnconta ct1 ");
			sql.append( "where ct2.codemp=cv.codempcv and ct2.codfilial=cv.codfilialcv and ct2.numconta=cv.numcontacv ");
			sql.append( "and  ct1.codemppn=? and ct1.codfilialpn=? and ct1.codplan=? ");
			sql.append( "and ct1.numconta=cv.numconta and ct1.codemp=cv.codemp and ct1.codfilial=cv.codfilial ");
			sql.append( "group by 1 ");  

			try {
				iCodEmp = Aplicativo.iCodEmp;
				iCodFilial = ListaCampos.getMasterFilial( "FNSALDOLANCA" );

				PreparedStatement ps = con.prepareStatement( sql.toString() );

				ps.setDate( 1, Funcoes.dateToSQLDate( dFimLanca ) );
				ps.setInt( 2, iCodEmp );
				ps.setInt( 3, iCodFilial );
				ps.setString( 4, sCodPlan );	

				System.out.println("Query do saldo composto:" + sql.toString());

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {

					BigDecimal SaldoComposto = txtVlrSaldo.getVlrBigDecimal();

					SaldoComposto = SaldoComposto.add( rs.getBigDecimal( "SaldoVinculado" ) );

					txtVlrSaldoComposto.setVlrString( Funcoes.bdToStr( SaldoComposto, Aplicativo.casasDecFin ).toString());
					mostraSaldoComposto( true );
				}
				else {
					txtVlrSaldoComposto.setVlrString( "0,00" );
					mostraSaldoComposto( false );
				}

				rs.close();
				ps.close();
				// con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar o saldo composto!\n" + err.getMessage(), true, con, err );
			}
		}

		private void atualizaSaldo() {

			int iCodEmp = 0;
			int iCodFilial = 0;

			String sSQL = "SELECT S.DATASL,S.SALDOSL FROM FNSALDOLANCA S WHERE S.CODPLAN=?" + " AND S.CODEMP=? AND S.CODFILIAL=? AND S.CODEMPPN=? AND S.CODFILIALPN=?" + " AND S.DATASL=(SELECT MAX(S1.DATASL)" + " FROM FNSALDOLANCA S1 WHERE S1.DATASL <= ? AND S1.CODPLAN=S.CODPLAN"
			+ " AND S1.CODEMP=S.CODEMP AND S1.CODFILIAL=S.CODFILIAL" + " AND S1.CODEMPPN=S.CODEMPPN AND S1.CODFILIALPN=S.CODFILIALPN)";
			try {
				iCodEmp = Aplicativo.iCodEmp;
				iCodFilial = ListaCampos.getMasterFilial( "FNSALDOLANCA" );
				PreparedStatement ps = con.prepareStatement( sSQL );

				ps.setString( 1, sCodPlan );
				ps.setInt( 2, iCodEmp );
				ps.setInt( 3, iCodFilial );
				ps.setInt( 4, iCodEmp );
				ps.setInt( 5, iCodFilial );
				ps.setDate( 6, Funcoes.dateToSQLDate( dFimLanca ) );

				ResultSet rs = ps.executeQuery();
				if ( rs.next() ) {
					lbDataSaldoVal.setText( StringFunctions.sqlDateToStrDate( rs.getDate( "DataSl" ) ) );
					//				lbVlrSaldo.setText( Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "SaldoSl" ) ) );
					txtVlrSaldo.setVlrString( Funcoes.bdToStr( rs.getBigDecimal( "SaldoSl" ), Aplicativo.casasDecFin ).toString());
					lbAtualSaldoVal.setText( "SIM" );
				}
				else {
					lbDataSaldoVal.setText( "" );
					txtVlrSaldo.setVlrString( "0,00" );
					lbAtualSaldoVal.setText( "SEM" );
				}
				rs.close();
				ps.close();
				// con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar o saldo!\n" + err.getMessage(), true, con, err );
			}
		}

		private void prim() {

			if ( ( tab != null ) & ( tab.getNumLinhas() > 0 ) )
				tab.setLinhaSel( 0 );
		}

		private void ant() {

			int iLin = 0;
			if ( ( tab != null ) & ( tab.getNumLinhas() > 0 ) ) {
				iLin = tab.getLinhaSel();
				if ( iLin > 0 )
					tab.setLinhaSel( iLin - 1 );
			}
		}

		private void prox() {

			int iLin = 0;
			if ( ( tab != null ) & ( tab.getNumLinhas() > 0 ) ) {
				iLin = tab.getLinhaSel();
				if ( iLin < ( tab.getNumLinhas() - 1 ) )
					tab.setLinhaSel( iLin + 1 );
			}
		}

		private void ult() {

			if ( ( tab != null ) & ( tab.getNumLinhas() > 0 ) )
				tab.setLinhaSel( tab.getNumLinhas() - 1 );
		}

		private boolean validaPeriodo() {

			boolean bRetorno = false;
			if ( txtDataini.getText().trim().length() == 0 ) {
			}
			else if ( txtDataini.getText().trim().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data inicial inválida!" );
			}
			else if ( txtDatafim.getText().trim().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Data final inválida!" );
			}
			else if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			}
			else {
				dIniLanca = txtDataini.getVlrDate();
				dFimLanca = txtDatafim.getVlrDate();
				bRetorno = true;
			}
			return bRetorno;
		}

		private void excluir() {
			String codpag = null;
			String codrec = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			if ( ( tab.getLinhaSel() >= 0 ) & ( Funcoes.mensagemConfirma( this, "Deseja realmente excluir este lancamento?" ) == 0 ) ) {
				try {
					ps = con.prepareStatement( "SELECT FIRST 1 CODPAG, CODREC FROM FNSUBLANCA WHERE CODLANCA=? AND CODEMP=? AND CODFILIAL=? AND " +
							"(CODPAG IS NOT NULL OR CODREC IS NOT NULL)" ); 
					ps.setString( 1, (String) tab.getValor( tab.getLinhaSel(), enum_tab_lanca.CODLANCA.ordinal() ) );
					ps.setInt( 2, Aplicativo.iCodEmp );
					ps.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
					rs = ps.executeQuery();
					if (rs.next()) {
						codpag = rs.getString( "CODPAG" );
						codrec = rs.getString( "CODREC" );
					}
					rs.close();
					ps.close();
					con.commit();
					if (  ( codrec == null || "".equals( codrec ) )  && ( codpag == null || "".equals( codpag )  ) ) {
							ps = con.prepareStatement( "DELETE FROM FNLANCA WHERE CODLANCA=? AND CODEMP=? AND CODFILIAL=?" );
							ps.setString( 1, (String) tab.getValor( tab.getLinhaSel(), enum_tab_lanca.CODLANCA.ordinal() ) );
							ps.setInt( 2, Aplicativo.iCodEmp );
							ps.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
							ps.executeUpdate();
							ps.close();
							con.commit();
							montaTabela( dIniLanca, dFimLanca, null, null, true );
					} else {
						Funcoes.mensagemInforma( this,
								"Operação não permitida!\n" +
								"Esta operação deve ser efetuada pela tela de manutenção de contas a "+
								(codrec==null ? "pagar":"receber")+"!" );
					}
				} catch ( SQLException err ) {
					Funcoes.mensagemErro( this, "Erro ao excluir o lançamento!\n" + err.getMessage(), true, con, err );
				}
			}
		}

		private void novo() {

			if ( validaPeriodo() ) {
				Container cont = getContentPane();
				while ( true ) {
					if ( cont instanceof FPrincipal )
						break;
					cont = cont.getParent();
				}
				if ( ! ( (FPrincipal) cont ).temTela( "FSubLanca" ) ) {
					FSubLanca form = new FSubLanca( null, sCodPlan, dIniLanca, dFimLanca );
					( (FPrincipal) cont ).criatela( "FSubLanca", form, con );
					form.addInternalFrameListener( new InternalFrameAdapter() {

						public void internalFrameClosed( InternalFrameEvent ievt ) {

							adicLanca( ( (FSubLanca) ievt.getSource() ).getValores() );
						}
					} );
				}
			}
		}

		/*
		 * Se o lancamento nao for uma transferencia e nao ter origem nesta conta ele nao pode ser editado
		 */
		private void editar() {

			if ( ( tab.getLinhaSel() >= 0 ) & ( validaPeriodo() ) ) {
				if ( ( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.ORIGSUBLANCA.ordinal() ).equals( "N" ) ) 
				  && ( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.TRANSFLANCA.ordinal() ).equals( "S" ) ) ) {
					
					Funcoes.mensagemInforma( this, "Este lançamento é uma transferência!\nDeve ser alterado na conta de origem: \"" + tab.getValor( tab.getLinhaSel(), enum_tab_lanca.NUMCONTA.ordinal() ).toString().trim() + "\"" );
				
				}
				/*else if ( ( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.ORIGSUBLANCA.ordinal() ).equals( "S" ) ) 
					   && ( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.TRANSFLANCA.ordinal() ).equals( "S" ) ) ) {
					
					DLDataTransf dl = new DLDataTransf( this );
					
					dl.setVisible( true );
					
					if ( !dl.OK ) {
						dl.dispose();
						return;
					}
					
					Date dDtNova = dl.getValor();
					dl.dispose();
					String sSQL = "UPDATE FNSUBLANCA SET DATASUBLANCA=? WHERE CODLANCA = ? AND CODEMP=? AND CODFILIAL=? AND CODSUBLANCA > 0";
					
					try {
						PreparedStatement ps = con.prepareStatement( sSQL );
						ps.setDate( 1, Funcoes.dateToSQLDate( dDtNova ) );
						ps.setInt( 2, Integer.parseInt( (String) tab.getValor( tab.getLinhaSel(), enum_tab_lanca.CODLANCA.ordinal() ) ) );
						ps.setInt( 3, Aplicativo.iCodEmp );
						ps.setInt( 4, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
						ps.executeUpdate();
						ps.close();
						con.commit();
					} catch ( SQLException err ) {
						Funcoes.mensagemErro( this, "Erro ao atualizar a data da transferência!\n" + err.getMessage(), true, con, err );
					}
					tab.setValor( Funcoes.dateToStrDate( dDtNova ), tab.getLinhaSel(), enum_tab_lanca.DATASUBLANCA.ordinal() );

				}*/
				
				else {
					Container cont = getContentPane();
					while ( true ) {
						if ( cont instanceof FPrincipal )
							break;
						cont = cont.getParent();
					}
					if ( ! ( (FPrincipal) cont ).temTela( "FSubLanca" ) ) {
						FSubLanca form = new FSubLanca( (String) tab.getValor( tab.getLinhaSel(), enum_tab_lanca.CODLANCA.ordinal() ), sCodPlan, dIniLanca, dFimLanca );
						( (FPrincipal) cont ).criatela( "FSubLanca", form, con );
						form.addInternalFrameListener( new InternalFrameAdapter() {

							public void internalFrameClosed( InternalFrameEvent ievt ) {

								altLanca( ( (FSubLanca) ievt.getSource() ).getValores() );
							}
						} );
					}
				}
			}
		}

		private void adicLanca( Object[] vals ) {

			/*	public enum COL_VALS{ CODLANCA, DATALANCA, TRANSF, DOCLANCA, VLRATUALLANCA, HISTLANCA, CODPLAN }
*/
			int iLin = -1;
			if ( ( vals[ COL_VALS.CODLANCA.ordinal() ].toString().length() > 0 ) 
					&&	( testaCodLanca( Integer.parseInt( vals[ COL_VALS.CODLANCA.ordinal() ].toString() ) ) ) 
					&& ( sCodPlan.equals( vals[ COL_VALS.CODPLAN.ordinal() ] ) ) 
					&& ( !dIniLanca.after( Funcoes.strDateToDate( vals[ COL_VALS.DATALANCA.ordinal() ].toString() ) ) ) 
					&& ( !dFimLanca.before( Funcoes.strDateToDate( vals[ COL_VALS.DATALANCA.ordinal() ].toString() ) ) ) ) {
				for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
					if ( ( (String) tab.getValor( i, enum_tab_lanca.CODLANCA.ordinal() ) ).trim().equals( vals[ COL_VALS.CODLANCA.ordinal() ] ) ) {
						tab.tiraLinha( i );
						break;
					}
				}
				
				Vector<Object> row = new Vector<Object>();
				
				for (int i=0; i<enum_tab_lanca.values().length; i++) {
					row.addElement( null );
				}
				
				//tab.adicLinha();
				//iLin = tab.getNumLinhas() - 1;
				
				row.setElementAt( vals[ COL_VALS.CODLANCA.ordinal() ], enum_tab_lanca.CODLANCA.ordinal() );
				row.setElementAt( vals[ COL_VALS.DATALANCA.ordinal() ], enum_tab_lanca.DATASUBLANCA.ordinal() );
				row.setElementAt( vals[ COL_VALS.TRANSF.ordinal() ], enum_tab_lanca.TRANSFLANCA.ordinal() );
				
				if ( vals[ COL_VALS.TRANSF.ordinal() ].equals( "S" ) ) {
					row.setElementAt( sConta, enum_tab_lanca.NUMCONTA.ordinal() );
				}
				else {
					row.setElementAt( "", enum_tab_lanca.NUMCONTA.ordinal() );
				}
				
				row.setElementAt( " " + vals[ COL_VALS.DOCLANCA.ordinal() ], enum_tab_lanca.DOCLANCA.ordinal() );
				
				row.setElementAt( ConversionFunctions.stringCurrencyToBigDecimal( vals[ COL_VALS.VLRATUALLANCA.ordinal() ].toString() ), enum_tab_lanca.VLRSUBLANCA.ordinal() );
				
				row.setElementAt( " " + vals[ COL_VALS.HISTLANCA.ordinal() ], enum_tab_lanca.HISTBLANCA.ordinal() );
				
				for (int i=0; i<row.size(); i++) {
					if (row.elementAt( i )==null) {
						row.setElementAt( "", i );
					}
				}
				
				tab.adicLinha(row);
			}
			lbAtualSaldoVal.setText( "NÃO" );
		}

		private void altLanca( Object[] vals ) {
			
			/*public enum COL_VALS{ CODLANCA, DATALANCA, TRANSF, DOCLANCA, VLRATUALLANCA, HISTLANCA, CODPLAN }
*/

			int iLin = -1;
			if ( ( sCodPlan.equals( vals[ COL_VALS.CODPLAN.ordinal() ] ) ) 
					&& ( !dIniLanca.after( Funcoes.strDateToDate( vals[ COL_VALS.DATALANCA.ordinal() ].toString() ) ) ) 
					&& ( !dFimLanca.before( Funcoes.strDateToDate( vals[ COL_VALS.DATALANCA.ordinal() ].toString() ) ) ) ) {
				for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
					if ( ( (String) tab.getValor( i, enum_tab_lanca.CODLANCA.ordinal() ) ).trim().equals( vals[ COL_VALS.CODLANCA.ordinal() ] ) ) {
						iLin = i;
						break;
					}
				}
				
				tab.setValor( vals[ COL_VALS.CODLANCA.ordinal() ], iLin, enum_tab_lanca.CODLANCA.ordinal() );
				tab.setValor( vals[ COL_VALS.DATALANCA.ordinal() ], iLin, enum_tab_lanca.DATASUBLANCA.ordinal() );
				tab.setValor( vals[ COL_VALS.TRANSF.ordinal() ], iLin, enum_tab_lanca.TRANSFLANCA.ordinal() );
				tab.setValor( "S", iLin, enum_tab_lanca.ORIGSUBLANCA.ordinal() );
			
				if ( vals[ COL_VALS.TRANSF.ordinal() ].equals( "S" ) ) { 
					tab.setValor( sConta, iLin, enum_tab_lanca.NUMCONTA.ordinal() );
				}
				else {
					tab.setValor( "", iLin, enum_tab_lanca.NUMCONTA.ordinal() );
				}
				
				tab.setValor( " " + vals[ COL_VALS.DOCLANCA.ordinal() ], iLin, enum_tab_lanca.DOCLANCA.ordinal() );
				tab.setValor( ConversionFunctions.stringCurrencyToBigDecimal( vals[ COL_VALS.VLRATUALLANCA.ordinal() ].toString() ), iLin, enum_tab_lanca.VLRSUBLANCA.ordinal() );				
				tab.setValor( " " + vals[ COL_VALS.HISTLANCA.ordinal() ], iLin, enum_tab_lanca.HISTBLANCA.ordinal() );
				
				
			}
			lbAtualSaldoVal.setText( "NÃO" );
		}

		private boolean testaCodLanca( int iCodLanca ) {

			boolean bRetorno = false;
			try {
				PreparedStatement ps = con.prepareStatement( "SELECT CODLANCA FROM FNLANCA WHERE CODLANCA=? AND CODEMP=? AND CODFILIAL=?" );
				ps.setInt( 1, iCodLanca );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "FNLANCA" ) );
				if ( ( ps.executeQuery() ).next() )
					bRetorno = true;

				// con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao testar o código do lnaçamento!\n" + err.getMessage(), true, con, err );
			}
			return bRetorno;
		}

		public void actionPerformed( ActionEvent evt ) {

			if ( evt.getSource() == btSair )
				dispose();
			else if ( evt.getSource() == btPrim )
				prim();
			else if ( evt.getSource() == btAnt )
				ant();
			else if ( evt.getSource() == btProx )
				prox();
			else if ( evt.getSource() == btUlt )
				ult();
			else if ( evt.getSource() == btNovo )
				novo();
			else if ( evt.getSource() == btEditar )
				editar();
			else if ( evt.getSource() == btExcluir )
				excluir();
			else if ( evt.getSource() == btExec ) {
				if ( validaPeriodo() ) {
					montaTabela( dIniLanca, dFimLanca, null, null, true );
				}
			}
			else if ( evt.getSource() == btCalcSaldo ) {
				if ( validaPeriodo() ) {
					atualizaSaldo();
					atualizaSaldoComposto();
				}
			}
			else if(evt.getSource() == btAbreCheque ) {
				abreCheque( );
			}
			else if(evt.getSource() == btBuscaLancaValor ) {
				buscaValorLanca();
			}
			else if(evt.getSource() instanceof JMenuItem) {
				
				JMenuItem menu = (JMenuItem) evt.getSource();
				
				String opcao  = menu.getText();
				
				Integer codsinal = null;
				
				if(menu != menu_limpa_cor_linha && menu != menu_cadastra_cor && menu != menu_limpa_cor_tudo) {
					
					codsinal = Integer.parseInt( opcao.substring( 0, opcao.indexOf( "-" ) ));
					
				} 
				else if (evt.getSource() == menu_cadastra_cor){ 
					
					if (Funcoes.verificaAcessoClasse(FSinalizadores.class.getCanonicalName())) {
						FSinalizadores sinal = (FSinalizadores) Aplicativo.getInstace().abreTela("Sinalizadores", FSinalizadores.class);
						if (sinal!=null) {
							sinal.setOwner(this);
						}
						
					} else {
						Funcoes.mensagemInforma(null, "O usuário " + Aplicativo.getUsuario().getIdusu() + " não possui acesso a tela solicitada (" + FSinalizadores.class.getName()
								+ ").\nSolicite a liberação do acesso ao administrador do sistema.");
					}
					
					return;
				}
				if(menu == menu_limpa_cor_tudo) {
					atualizaCor( null, Integer.parseInt( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.CODLANCA.ordinal() ).toString() ), true );
				}
				else {
					atualizaCor( codsinal, Integer.parseInt( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.CODLANCA.ordinal() ).toString() ), false );
				}
				montaTabela( dIniLanca, dFimLanca, null, null, true );
								
			}
		}

		public void stateChanged( ChangeEvent cevt ) {
			sCodPlan = sPlanos[ tpn.getSelectedIndex() ];
			sConta = sContas[ tpn.getSelectedIndex() ];
			if ( validaPeriodo() ) {
				montaTabela( dIniLanca, dFimLanca, null, null, true );
			}
		}

		public void setConexao( DbConnection cn ) {

			super.setConexao( cn );
			
			montaTabs();
			
			montaMenuCores();
			
			alinha_tela();
			
		}
		
		private void alinha_tela() {
			
			StringBuilder sql = new StringBuilder();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			boolean reposiciona = false;
			
			try {
			
				sql.append("select coalesce(alinhatelalanca,'N') alinha from sgprefere1 p1 where p1.codemp=? and p1.codfilial=? ");
				
				ps = con.prepareStatement( sql.toString() );
				
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, Aplicativo.iCodFilial );
				
				rs = ps.executeQuery();
				
				if (rs.next()) {
					reposiciona = "S".equals( rs.getString( "alinha" ) );
				}
				
				if(reposiciona) {
					
					int largPrinc = ( int ) Aplicativo.telaPrincipal.dpArea.getSize().getWidth();
					
					int posicao = largPrinc - L;
					
					setAtribos( posicao, Y, L, A );
					
				}
				else {
					setAtribos( X, Y, L, A );
				}
				
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		@ SuppressWarnings ( "unchecked" )
		public void mouseClicked( MouseEvent mevt ) {

			if ( mevt.getSource() == tab && mevt.getClickCount() == 2 ) {
				editar();
			}
			else if ( mevt.getSource() == tab && mevt.getClickCount() == 1 ) {

				btAbreCheque.setEnabled( false );

				if( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.SEQCHEQ.ordinal())!=null && !"".equals( tab.getValor( tab.getLinhaSel(), enum_tab_lanca.SEQCHEQ.ordinal())) ) {

					Vector<String> seqcheq = (Vector<String>) tab.getValor( tab.getLinhaSel(), enum_tab_lanca.SEQCHEQ.ordinal()); 

					if(seqcheq!=null && seqcheq.size()>0) {
						Integer seqcheque = Integer.parseInt( (String) seqcheq.elementAt( 0 ) );

						if(seqcheque>0) {
							btAbreCheque.setEnabled( true );
						}
					}
				}

			}

		}

		public void mouseEntered( MouseEvent arg0 ) { }

		public void mouseExited( MouseEvent arg0 ) { }

		public void mousePressed( MouseEvent mevt ) {
			if (mevt.getModifiers() == InputEvent.BUTTON3_MASK && mevt.getSource()==tab) {
				menuCores.show(this, mevt.getXOnScreen(), mevt.getYOnScreen());
			}
		}

		public void mouseReleased( MouseEvent arg0 ) { }
		
		public HashMap<String, Vector<?>> montaListaCores() {

			Vector<HashMap<String, Object>> vVals = new Vector<HashMap<String, Object>>();
			Vector<Color> vLabs = new Vector<Color>();
		
			HashMap<String, Vector<?>> ret = new HashMap<String, Vector<?>>();

			PreparedStatement ps = null;
			ResultSet rs = null;
			StringBuffer sql = new StringBuffer();

			sql.append("select s.codsinal, s.descsinal, s.corsinal ");
			sql.append("from fnsinal s ");
			sql.append("where s.codemp=? and s.codfilial=? ");

			try {
				
				ps = con.prepareStatement(sql.toString());
				ps.setInt(1, Aplicativo.iCodEmp);
				ps.setInt(2, ListaCampos.getMasterFilial("FNSINAL"));
				
				rs = ps.executeQuery();

				while (rs.next()) {
					
					HashMap<String, Object> hvalores = new HashMap<String, Object>();
					
					hvalores.put( "CODSINAL", rs.getInt( "CODSINAL" ) );
					hvalores.put( "DESCSINAL", rs.getString( "DESCSINAL" ) );
					
					vVals.addElement( hvalores );
					
					Color cor = new Color(rs.getInt( "corsinal" ));
					
					vLabs.addElement( cor );
					
				}

				ret.put("VAL",  vVals);
				ret.put("LAB",  vLabs);


			}
			catch (SQLException err) {
				err.printStackTrace();
				Funcoes.mensagemErro(null, "Erro ao buscar sinais");
			}
			return ret;
		}
		
		private void atualizaCor(Integer codsinal, Integer codlanca, boolean tudo ) {
			
			StringBuilder sql = new StringBuilder();
			PreparedStatement ps = null;
			
			try {
				
				sql.append( "update fnlanca set codempsn=?, codfilialsn=?, codsinal=? " );
				sql.append( "where codemp=? and codfilial=? ");
				
				if(!tudo) {
					sql.append( "and codlanca=? " );
				}
				else {
					sql.append( "and codsinal is not null " );
				}
				
				ps = con.prepareStatement( sql.toString() ); 

				if(codsinal!=null) {
				
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "FNSINAL" ) );
					ps.setInt( 3, codsinal );
			
				}
				else {

					ps.setNull( 1, Types.INTEGER );
					ps.setNull( 2, Types.INTEGER );
					ps.setNull( 3, Types.INTEGER );
					
				}
					
				ps.setInt( 4, Aplicativo.iCodEmp );
				ps.setInt( 5, ListaCampos.getMasterFilial( "FNLANCA" ) );
				
				if(!tudo) {
					ps.setInt( 6, codlanca );
				}
				
				ps.execute();
				
				con.commit();
				
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private	void montaMenuCores() {
			this.montaMenuCores(true);
		}
		
		public void montaMenuCores(boolean primeiravez) {

			try {
				if (!primeiravez) {
					menuCores = new JPopupMenu();
				}
				HashMap<String, Vector<?>> cores = montaListaCores();
				
				Vector<Color> labels = (Vector<Color>) cores.get( "LAB" );
				Vector<HashMap<String, Object>> valores = (Vector<HashMap<String, Object>>) cores.get("VAL");
		
				for( int i =0; i < valores.size(); i++ ) {
					
					JMenuItem menucor = new JMenuItem();
					
					menucor.addActionListener(this);
					
					menucor.setBackground( labels.elementAt( i ) );
					
					HashMap<String, Object> hvalores = valores.elementAt( i );
					
					menucor.setText( (Integer) hvalores.get( "CODSINAL" ) + "-" + (String) hvalores.get( "DESCSINAL" ) );
					
					menuCores.add(menucor);
					
				}
				
				menuCores.addSeparator();
				
				menu_limpa_cor_linha.setText( "Limpa cor" );
				menu_cadastra_cor.setText( "Cadastro nova cor" );
				menu_limpa_cor_tudo.setText( "Limpa tudo" );
				
				menu_limpa_cor_linha.addActionListener( this );
				menu_cadastra_cor.addActionListener( this );
				menu_limpa_cor_tudo.addActionListener( this );
				
				menuCores.add( menu_limpa_cor_linha );
				menuCores.add( menu_cadastra_cor );
				menuCores.add( menu_limpa_cor_tudo );
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void keyPressed(KeyEvent kevt) {
			
			if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
				Ctrl = true;
			
			if (Ctrl) {
				if (kevt.getKeyCode() == KeyEvent.VK_F) {
					btBuscaLancaValor.doClick();
				}
			}
		}

		public void keyReleased(KeyEvent kevt) {
			
			if ( kevt.getKeyCode() == KeyEvent.VK_CONTROL ) {
				Ctrl = false;
			}
			
		}

		public void keyTyped( KeyEvent arg0 ) {

			// TODO Auto-generated method stub
			
		}
		
}
