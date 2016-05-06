/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FGrade.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.ProcessoSec;
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
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.dao.DAOGrade;

public class FGrade extends FFilho implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;
	
	public static enum TAB_GRADE { ADICPROD, DESCPROD, REFPROD, CODFABPROD, CODBARPROD, DESCCOMPL, PRECOBASE, PRECOBASEANT, CADASTRADO, ATUALIZAPROD }
	
	public static enum TAB_MOD { SN, TIPOVAR, DESCVAR, REFPROD, CODFABPROD, CODBARPROD, DESCCOMPPRODMODG, DESCCOMPITMODG, PRECOBASE }

	private JPanelPad pinCab = new JPanelPad( 700, 95 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnSubRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinRod = new JPanelPad( 480, 55 );

	private JPanelPad pinSair = new JPanelPad( 120, 45 );

	private JPanelPad pinBtSel = new JPanelPad( 40, 110 );

	private JPanelPad pinBtSelMod = new JPanelPad( 40, 110 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnTabMod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCliTab = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JLabelPad lbCodModG = new JLabelPad( "Cód.mod.gp" );

	private JLabelPad lbDescModG = new JLabelPad( "Descrição do modelo de grupo" );

	private JLabelPad lbDescINIModG = new JLabelPad( "Descrição ini." );

	private JLabelPad lbRefINIModG = new JLabelPad( "Ref.ini." );

	private JLabelPad lbCodFabINIModG = new JLabelPad( "Cod.fab.ini." );

	private JLabelPad lbCodBarINIModG = new JLabelPad( "Cod.bar.ini." );

	private JTextFieldPad txtCodModG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescModG = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescINIModG = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtDescCompProdModG = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtRefINIModG = new JTextFieldFK( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtCodFabINIModG = new JTextFieldFK( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtCodBarINIModG = new JTextFieldFK( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtPrecoBaseProd = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );
	
	private JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JTablePad tabMod = new JTablePad();

	private JScrollPane spnTabMod = new JScrollPane( tabMod );

	private JButtonPad btAtualizaPreco = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	private JButtonPad btTudo = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btNada = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btTudoMod = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btNadaMod = new JButtonPad( Icone.novo( "btNada.png" ) );

	private JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JProgressBar pbGrade = new JProgressBar();

	private JLabelPad lbAnd = new JLabelPad( "Andamento:" );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcModG = new ListaCampos( this );
	
	private ListaCampos lcProd = new ListaCampos( this );

	int iCodProd = 0;
	
	DAOGrade daoGrade = null;
	
	Map<String , Object> preferencia = null;

	public FGrade() {

		// Monta a tela
		super( false );
		setTitulo( "Grade" );
		setAtribos( 25, 10, 730, 450 );
		Container c = getTela();
		c.setLayout( new BorderLayout() );
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pnCli, BorderLayout.CENTER );
		c.add( pinCab, BorderLayout.NORTH );
		
		btAtualizaPreco.setToolTipText( "Atualiza preço na grade" );

		pinCab.adic( lbCodModG, 7, 5, 75, 20 );
		pinCab.adic( txtCodModG, 7, 25, 75, 20 );
		pinCab.adic( lbDescModG, 85, 5, 192, 20 );
		pinCab.adic( txtDescModG, 85, 25, 192, 20 );
		pinCab.adic( lbDescINIModG, 280, 5, 167, 20 );
		pinCab.adic( txtDescINIModG, 280, 25, 167, 20 );
		pinCab.adic( lbRefINIModG, 450, 5, 67, 20 );
		pinCab.adic( txtRefINIModG, 450, 25, 67, 20 );
		pinCab.adic( lbCodFabINIModG, 520, 5, 77, 20 );
		pinCab.adic( txtCodFabINIModG, 520, 25, 77, 20 );
		pinCab.adic( lbCodBarINIModG, 600, 5, 77, 20 );
		pinCab.adic( txtCodBarINIModG, 600, 25, 77, 20 );
		pinCab.adic( txtCodProd, 7, 65, 77, 20, "Cód.prod" );
		pinCab.adic( txtDescProd, 85, 65, 192, 20, "Descrição do Produto" );
		pinCab.adic( txtPrecoBaseProd, 280, 65, 75, 20, "Preço base" );
		pinCab.adic( btAtualizaPreco, 358, 55, 30, 30, "");
		
		
		pnRod.setPreferredSize( new Dimension( 600, 50 ) );

		pnSubRod.setPreferredSize( new Dimension( 600, 50 ) );
		pnRod.add( pnSubRod, BorderLayout.SOUTH );

		pinSair.tiraBorda();
		pinSair.adic( btSair, 10, 10, 100, 30 );
		btSair.setPreferredSize( new Dimension( 120, 30 ) );

		pnSubRod.add( pinSair, BorderLayout.EAST );
		pnSubRod.add( pinRod, BorderLayout.CENTER );

		pinRod.tiraBorda();
		pinRod.adic( lbAnd, 10, 5, 400, 15 );
		pinRod.adic( pbGrade, 10, 20, 300, 20 );

		pinBtSel.adic( btTudo, 5, 5, 30, 30 );
		pinBtSel.adic( btNada, 5, 38, 30, 30 );
		pinBtSel.adic( btGerar, 5, 71, 30, 30 );

		pnTabMod.setPreferredSize( new Dimension( 600, 130 ) );

		pnTabMod.add( spnTabMod, BorderLayout.CENTER );
		pnTabMod.add( pinBtSelMod, BorderLayout.EAST );

		pinBtSelMod.adic( btTudoMod, 5, 5, 30, 30 );
		pinBtSelMod.adic( btNadaMod, 5, 38, 30, 30 );
		pinBtSelMod.adic( btExec, 5, 71, 30, 30 );

		pnCliTab.add( spnTab, BorderLayout.CENTER );
		pnCliTab.add( pinBtSel, BorderLayout.EAST );

		pnCli.add( pnTabMod, BorderLayout.NORTH );
		pnCli.add( pnCliTab, BorderLayout.CENTER );

		// Seta os comentários

		btExec.setToolTipText( "Executar montagem" );
		btTudo.setToolTipText( "Selecionar tudo" );
		btNada.setToolTipText( "Limpar seleção" );
		btTudoMod.setToolTipText( "Selecionar tudo" );
		btNadaMod.setToolTipText( "Limpar seleção" );
		btGerar.setToolTipText( "Gerar no banco" );

		// Monta as tabelas

		tab.adicColuna( "Adic." );
		tab.adicColuna( "Descrição do produto" );
		tab.adicColuna( "Referência" );
		tab.adicColuna( "Cód.fab." );
		tab.adicColuna( "Cód.bar." );
		tab.adicColuna( "Descrição Completa." );
		tab.adicColuna( "Preço base" );
		tab.adicColuna( "Preço base Ant." );
		tab.adicColuna( "Já Cadastado." );
		tab.adicColuna( "Atualiza" );
		
		
		tab.setTamColuna( 40, TAB_GRADE.ADICPROD.ordinal() );
		tab.setTamColuna( 280, TAB_GRADE.DESCPROD.ordinal() );
		tab.setTamColuna( 100, TAB_GRADE.REFPROD.ordinal() );
		tab.setTamColuna( 80, TAB_GRADE.CODFABPROD.ordinal() );
		tab.setTamColuna( 80, TAB_GRADE.CODBARPROD.ordinal() );
		tab.setTamColuna( 250, TAB_GRADE.DESCCOMPL.ordinal() );
		tab.setTamColuna( 80, TAB_GRADE.PRECOBASE.ordinal() );
		tab.setTamColuna( 80, TAB_GRADE.PRECOBASEANT.ordinal() );
		tab.setTamColuna( 80, TAB_GRADE.CADASTRADO.ordinal() );
		tab.setTamColuna( 80, TAB_GRADE.ATUALIZAPROD.ordinal() );
		
		
		tab.setColunaEditavel( TAB_GRADE.ADICPROD.ordinal(), true );
		tab.setColunaEditavel( TAB_GRADE.PRECOBASE.ordinal(), true );
		tab.setColunaEditavel( TAB_GRADE.ATUALIZAPROD.ordinal(), true );
		
		tabMod.adicColuna( "S/N" );
		tabMod.adicColuna( "Tipo de variante" );
		tabMod.adicColuna( "Descrição da variante" );
		tabMod.adicColuna( "Referência" );
		tabMod.adicColuna( "Cód.fab." );
		tabMod.adicColuna( "Cód.bar." );
		tabMod.adicColuna( "Desc.compl.inicial." );
		tabMod.adicColuna( "Desc.compl.item." );
		tabMod.adicColuna( "Preço base" );
		
		tabMod.setTamColuna( 40, TAB_MOD.SN.ordinal());
		tabMod.setTamColuna( 160, TAB_MOD.TIPOVAR.ordinal() );
		tabMod.setTamColuna( 160, TAB_MOD.DESCVAR.ordinal() );
		tabMod.setTamColuna( 100, TAB_MOD.REFPROD.ordinal() );
		tabMod.setTamColuna( 80, TAB_MOD.CODFABPROD.ordinal() );
		tabMod.setTamColuna( 80, TAB_MOD.CODBARPROD.ordinal() );
		tabMod.setTamColuna( 100, TAB_MOD.DESCCOMPPRODMODG.ordinal() );
		tabMod.setTamColuna( 100, TAB_MOD.DESCCOMPITMODG.ordinal() );
		tabMod.setTamColuna( 80, TAB_MOD.PRECOBASE.ordinal() );
		
		tabMod.setColunaEditavel( TAB_MOD.SN.ordinal(), true );
		//tabMod.setColunaEditavel( TAB_MOD.TIPOVAR.ordinal(), true );
		//tabMod.setColunaEditavel( TAB_MOD.DESCVAR.ordinal(), true );
		//tabMod.setColunaEditavel( TAB_MOD.DESCCOMPPRODMODG.ordinal(), true );
		//tabMod.setColunaEditavel( TAB_MOD.DESCCOMPITMODG.ordinal(), true );
		tabMod.setColunaEditavel( TAB_MOD.PRECOBASE.ordinal(), true );
		
		txtCodProd.setPKFK( false, true );
		lcProd.add (new GuardaCampo( txtCodProd, "CodProd", "Cód.Prod.", ListaCampos.DB_PK, txtDescProd, false ) );
		lcProd.add (new GuardaCampo( txtDescProd, "DescProd", "DescProd.", ListaCampos.DB_SI, false ) );
		lcProd.add (new GuardaCampo( txtPrecoBaseProd, "PrecoBaseProd", "Preço base.", ListaCampos.DB_SI, false ) );
		
		
		lcProd.montaSql( false, "PRODUTO", "EQ" );	
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setListaCampos( lcProd );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );
		
		// Seta a FK do Modelo

		txtCodModG.setPKFK( true, false );
		lcModG.add( new GuardaCampo( txtCodModG, "CodModG", "Cód.mod.gp.", ListaCampos.DB_PK, true ) );
		lcModG.add( new GuardaCampo( txtDescModG, "DescModG", "Descrição do modelo de grupo", ListaCampos.DB_SI, false ) );
		lcModG.add( new GuardaCampo( txtDescINIModG, "DescProdModG", "Descrição ini.", ListaCampos.DB_SI, false ) );
		lcModG.add( new GuardaCampo( txtRefINIModG, "RefModG", "Ref. ini.", ListaCampos.DB_SI, false ) );
		lcModG.add( new GuardaCampo( txtCodFabINIModG, "CodFabModG", "Cód.fab.ini.", ListaCampos.DB_SI, false ) );
		lcModG.add( new GuardaCampo( txtCodBarINIModG, "CodBarModG", "Cód.bar.ini.", ListaCampos.DB_SI, false ) );
		lcModG.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_FK, false ) );
		lcModG.add( new GuardaCampo( txtDescCompProdModG, "DescCompProdModG", "Desc.compl.inicial.", ListaCampos.DB_SI, false ) );
		lcModG.montaSql( false, "MODGRADE", "EQ" );
		lcModG.setReadOnly( true );
		txtCodModG.setNomeCampo( "CodModG" );
		txtCodModG.setListaCampos( lcModG );
		txtDescModG.setListaCampos( lcModG );
		txtDescINIModG.setListaCampos( lcModG );
		txtRefINIModG.setListaCampos( lcModG );
		txtCodFabINIModG.setListaCampos( lcModG );
		txtCodBarINIModG.setListaCampos( lcModG );

		// Adiciona os Listeners
		lcModG.addCarregaListener( this );
		btSair.addActionListener( this );
		btExec.addActionListener( this );
		btGerar.addActionListener( this );
		btTudo.addActionListener( this );
		btNada.addActionListener( this );
		btTudoMod.addActionListener( this );
		btNadaMod.addActionListener( this );
		btAtualizaPreco.addActionListener( this );

		txtCodModG.requestFocus();
		
		habilitaCampos(false);
	}

	private void carregar() {

		if ( txtCodModG.getText().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Selecione o modelo!" );
			txtCodModG.requestFocus();
			return;
		}

		tab.limpa();

		String sTmp = "";
		Vector<String> vModelos = new Vector<String>();
		Vector<Vector<Object[]>> vItens = new Vector<Vector<Object[]>>();
		boolean bAchou = false;

		if ( tabMod.getNumLinhas() > 0 ) {
			for (int i = 0; i < tabMod.getNumLinhas(); i++) {
				if (((Boolean) tabMod.getValor( i, 0 ) ).booleanValue()) {
					sTmp = "" + tabMod.getValor( i, 1 );
					if (vModelos.size() == 0) {
						vModelos.addElement( sTmp );
					}
					else {
						bAchou = false;
						for (int i2 = 0; i2 < vModelos.size(); i2++) {
							if (sTmp.equals(vModelos.elementAt( i2 ))) {
								bAchou = true;
								break;
							}
						}
						if (!bAchou) {
							vModelos.addElement( sTmp );
						}
					}
				} // fim do if do boolean
			} // Fim do for

			for (int i = 0; i < vModelos.size(); i++) {
				vItens.addElement( getItens( "" + vModelos.elementAt( i ) ) );
			}
			tab.limpa();
			geraItens( txtDescINIModG.getText(), txtRefINIModG.getText(), txtCodFabINIModG.getText(), txtCodBarINIModG.getText(), txtDescCompProdModG.getText(), txtPrecoBaseProd.getVlrBigDecimal(), new BigDecimal(0), 0, vItens );

		}
	}

	private void geraItens( String sDesc, String sRef, String sCodfab, String sCodbar, String sDescComp, BigDecimal precoBase, BigDecimal precoitvarg, int iItem, Vector<Vector<Object[]>> itens ) {

		String sDescAnt = sDesc;
		String sRefAnt = sRef;
		String sCodfabAnt = sCodfab;
		String sCodbarAnt = sCodbar;
		String sDescComplAnt = sDescComp;
		BigDecimal valorAnt;
		
		if ((Boolean) preferencia.get( "calcprecog" ))  
			valorAnt = precoitvarg;
		else 
			valorAnt = new BigDecimal( 0 );
		
		
		Map<String,Object> infProd = new HashMap<String, Object>();
	
		
		if (iItem < itens.size()) {
			for (int i = 0; i < itens.elementAt( iItem ).size(); i++) {
				sDesc = sDescAnt.trim() + " " + ( (Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 0 ];
				sRef = sRefAnt.trim() + ( (Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 1 ];
				sCodfab = sCodfabAnt.trim() + ( (Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 2 ];
				sCodbar = sCodbarAnt.trim() + ( (Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 3 ];
				sDescComp = sDescComplAnt.trim()  + " " + ( (Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 4 ];
				if ((Boolean) preferencia.get( "calcprecog" )) { 
					precoitvarg = valorAnt.add( (BigDecimal) ( (Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 5 ] );
				} else {
					valorAnt = (BigDecimal) ((Object[]) itens.elementAt( iItem ).elementAt( i ) )[ 5 ] ;
				}
				
				geraItens( sDesc, sRef, sCodfab, sCodbar, sDescComp, precoBase, precoitvarg, iItem + 1, itens );
				if ( iItem == itens.size() - 1 ) {
					if ( !sDesc.equals( "" ) ) {
						try {
							infProd = daoGrade.getPrecoBaseAnt( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQITGRADE" ), sRef );
						} catch ( SQLException e ) {
							e.printStackTrace();
						}
						
						tab.adicLinha();
						tab.setValor( new Boolean( true ), tab.getNumLinhas() - 1, TAB_GRADE.ADICPROD.ordinal() );
						tab.setValor( sDesc, tab.getNumLinhas() - 1, TAB_GRADE.DESCPROD.ordinal() );
						tab.setValor( sRef, tab.getNumLinhas() - 1, TAB_GRADE.REFPROD.ordinal() );
						tab.setValor( sCodfab, tab.getNumLinhas() - 1, TAB_GRADE.CODFABPROD.ordinal() );
						tab.setValor( sCodbar, tab.getNumLinhas() - 1, TAB_GRADE.CODBARPROD.ordinal());
						tab.setValor( sDescComp, tab.getNumLinhas() - 1, TAB_GRADE.DESCCOMPL.ordinal());
						if ((Boolean) preferencia.get( "calcprecog" )) {
							tab.setValor( precoitvarg, tab.getNumLinhas() - 1, TAB_GRADE.PRECOBASE.ordinal());
						} else {
							if (valorAnt.compareTo(precoBase) > 0) {
								tab.setValor( valorAnt, tab.getNumLinhas() - 1, TAB_GRADE.PRECOBASE.ordinal());
							} else {
								tab.setValor( precoBase, tab.getNumLinhas() - 1, TAB_GRADE.PRECOBASE.ordinal());
							}
						}
						boolean cadastrado = (Boolean) infProd.get( "CADASTRADO" );
						tab.setValor( infProd.get( "PRECOBASEPROD" ), tab.getNumLinhas() - 1, TAB_GRADE.PRECOBASEANT.ordinal());
						tab.setValor( cadastrado, tab.getNumLinhas() - 1, TAB_GRADE.CADASTRADO.ordinal());
						tab.setValor( cadastrado, tab.getNumLinhas() - 1, TAB_GRADE.ATUALIZAPROD.ordinal() );
					}
				}
			}
		}

	}

	private Object[] getMatrizTab( int i ) {

		Object[] aItem = new Object[ 6 ];
		aItem[ 0 ] = (String) tabMod.getValor( i, TAB_MOD.DESCVAR.ordinal() );
		aItem[ 1 ] = (String) tabMod.getValor( i, TAB_MOD.REFPROD.ordinal() );
		aItem[ 2 ] = (String) tabMod.getValor( i, TAB_MOD.CODFABPROD.ordinal() );
		aItem[ 3 ] = (String) tabMod.getValor( i, TAB_MOD.CODBARPROD.ordinal() );
		aItem[ 4 ] = (String) tabMod.getValor( i, TAB_MOD.DESCCOMPITMODG.ordinal() );
		aItem[ 5 ] = (BigDecimal) tabMod.getValor( i, TAB_MOD.PRECOBASE.ordinal() );
		return aItem;
	}

	private Vector<Object[]> getItens( String sTipo ) {

		Vector<Object[]> vTmp = new Vector<Object[]>();
		for (int i = 0; i < tabMod.getNumLinhas(); i++) {
			if (((Boolean) tabMod.getValor( i, 0 ) ).booleanValue() & tabMod.getValor( i, 1 ).equals( sTipo )) {
				vTmp.addElement( getMatrizTab( i ) );
			}
		}
		return vTmp;
	}

	private void gerar() {

		int iContaItens = 0;
		String erros = "";
		for (int i = 0; i < tab.getNumLinhas(); i++) {
			if (((Boolean) tab.getValor( i, 0 ) ).booleanValue())
				iContaItens++;
		}

		pbGrade.setMinimum( 0 );
		pbGrade.setMaximum( iContaItens );
		pbGrade.setStringPainted( true );
		pbGrade.setValue( 0 );
	
		try {
			erros = daoGrade.executeProcedure( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQPRODUTO" ), txtCodProd.getVlrInteger()
					, Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQMODGRADE" ), txtCodModG.getVlrInteger()
					, tab, pbGrade );
			
			if (!erros.trim().equals( "" )) {
				Funcoes.criaTelaErro( "Alguns erros foram reportados:\n" + erros );
			}
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao executar um procedimento na tabela PRODUTO!\n" + err.getMessage(), true, con, err );
		}
	}

	private void carregaTabMod() {

		tabMod.limpa();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Object[] sVals = new Object[8];
		int iContaLinha = 0;
		try {
			rs = daoGrade.getMontaTab( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQMODGRADE" ) , txtCodModG.getVlrInteger());
			
			while (rs.next()) {
				sVals[ 0 ] = rs.getString( "DescVarG" ) != null ? rs.getString( "DescVarG" ) : "";
				sVals[ 1 ] = rs.getString( "DescItModG" ) != null ? rs.getString( "DescItModG" ) : "";
				sVals[ 2 ] = rs.getString( "RefItModG" ) != null ? rs.getString( "RefItModG" ) : "";
				sVals[ 3 ] = rs.getString( "CodFabItModG" ) != null ? rs.getString( "CodFabItModG" ) : "";
				sVals[ 4 ] = rs.getString( "CodBarItModG" ) != null ? rs.getString( "CodBarItModG" ) : "";
				sVals[ 5 ] = rs.getString( "DESCCOMPPRODMODG" ) != null ? rs.getString( "DescCompProdModG" ) : "";
				sVals[ 6 ] = rs.getString( "DESCCOMPITMODG" ) != null ? rs.getString( "DescCompItModG" ) : "";
				sVals[ 7 ] = rs.getBigDecimal( "PRECOITVARG" ) != null ? rs.getBigDecimal( "PRECOITVARG" ) : "";
				tabMod.adicLinha();
				tabMod.setColColor( -1, TAB_MOD.PRECOBASE.ordinal(), Color.WHITE, Color.RED );
				tabMod.setValor( new Boolean( true ), iContaLinha, 0 );
		
				if ((Boolean) preferencia.get( "calcprecog" )) { 
					for (int i = 0; i < 8; i++) {
						tabMod.setValor( sVals[ i ], iContaLinha, i + 1 );
					}
				} else {
					for (int i = 0; i < 7; i++) {
						tabMod.setValor( sVals[ i ], iContaLinha, i + 1 );
					}
					tabMod.setValor( txtPrecoBaseProd.getVlrBigDecimal(), iContaLinha, TAB_MOD.PRECOBASE.ordinal() );
				}
				iCodProd = rs.getInt( "CodProd" );
				iContaLinha++;
			}

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro a carregar a tabela ITMODGRADE!\n" + err.getMessage(), true, con, err );
		}
	}
	
	//String sSQL = "SELECT M.CODPROD,I.CODMODG,I.CODITMODG,I.CODVARG,V.DESCVARG," + "I.DESCITMODG,I.REFITMODG,I.CODFABITMODG,I.CODBARITMODG " + "FROM EQITMODGRADE I, EQVARGRADE V, EQMODGRADE M WHERE " + "M.CODEMP = ? AND M.CODFILIAL = ? AND I.CODMODG=" + txtCodModG.getText().trim()
	//		+ " AND V.CODVARG = I.CODVARG AND M.CODMODG=I.CODMODG " + "ORDER BY I.CODMODG,I.CODITMODG,I.CODVARG ";
	//ps = con.prepareStatement( sSQL );
	//	ps.setInt( 1, Aplicativo.iCodEmp );
	//	ps.setInt( 2, ListaCampos.getMasterFilial( "EQMODGRADE" ) );
	//	rs = ps.executeQuery();
	// rs.close();
	// ps.close();
	
	private void carregaTudo( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( true ), i, 0 );
		}
	}

	private void carregaNada( JTablePad tb ) {

		for ( int i = 0; i < tb.getNumLinhas(); i++ ) {
			tb.setValor( new Boolean( false ), i, 0 );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btExec ) {
			carregar();
		}
		else if ( evt.getSource() == btGerar ) {
			ProcessoSec pSec = new ProcessoSec( 500, new Processo() {

				public void run() {

					pbGrade.updateUI();
				}
			}, new Processo() {

				public void run() {

					gerar();
				}
			} );
			pSec.iniciar();
		}
		else if ( evt.getSource() == btTudo ) {
			carregaTudo( tab );
		}
		else if ( evt.getSource() == btNada ) {
			carregaNada( tab );
		}
		else if ( evt.getSource() == btTudoMod ) {
			carregaTudo( tabMod );
		}
		else if ( evt.getSource() == btNadaMod ) {
			carregaNada( tabMod );
		}
		else if (evt.getSource() == btAtualizaPreco) {
			carregaTabMod();
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {
		if ( cevt.ok ) {
			carregaTabMod();
		}
		if (cevt.getListaCampos() == lcModG) {
			habilitaCampos(!(Boolean) preferencia.get( "calcprecog" ) );
		}
	}
	
	public void habilitaCampos(boolean habilita) {
		
		txtPrecoBaseProd.setAtivo( habilita );
		btAtualizaPreco.setEnabled( habilita );
		
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcModG.setConexao( cn );
		lcProd.setConexao( cn );
		
		daoGrade = new DAOGrade( cn );
		preferencia = daoGrade.getPref();
		
	}


}
