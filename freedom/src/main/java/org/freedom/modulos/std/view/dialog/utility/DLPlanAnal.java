/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freeedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLPlanAnal.java <BR>
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
 *                     Tela de cadastro de planejamento financeiro (Contas analíticas).
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Vector;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLPlanAnal extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTabbedPanePad tabbedpanel = new JTabbedPanePad();

	private final JPanelPad panelgeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelfields = new JPanelPad();

	private final JPanelPad panelcontabil = new JPanelPad();

	private final JTextFieldPad txtCodPai = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtDescPai = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodAnal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtDescAnal = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private final JTextFieldPad txtCodRedAnal = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodContDeb = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtCodContCred = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtCodForContab = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtCodHistPad = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtDescHistPad = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private Vector<String> vValsTipoPlan = new Vector<String>();

	private Vector<String> vLabsTipoPlan = new Vector<String>();

	private Vector<String> vValsFinPlan = new Vector<String>();

	private Vector<String> vLabsFinPlan = new Vector<String>();

	private Vector<String> vValsEsFinPlan = new Vector<String>();

	private Vector<String> vLabsEsFinPlan = new Vector<String>();

	private Vector<String> vValsClasFinPlan = new Vector<String>();

	private Vector<String> vLabsClasFinPlan = new Vector<String>();

	private JRadioGroup<String, String> rgTipoPlan = null;

	private JRadioGroup<String, String> rgFinPlan = null;

	private JRadioGroup<String, String> rgEsFinPlan = null;

	private JRadioGroup<String, String> rgClasFinPlan = null;

	private ListaCampos lcHist = new ListaCampos( this, "HP" );

	public DLPlanAnal( Component cOrig, String sCodPai, String sDescPai, String sCod, String sDesc, String sTipo, String sFin, String sCodContCred, String sCodContDeb, int iCodHist, String sFinalidade, String sESFin, String sClasFin, Integer codredplan ) {

		super( cOrig );
		
		setTitulo( "Planejamento financeiro (Conta Analítica)" );
		setAtribos( 430, 570 );

		montaListaCampos();
		montaRadioGroup();
		montaTela();

		cancText( txtCodPai );
		cancText( txtDescPai );
		cancText( txtCodAnal );
		Funcoes.setBordReq( txtDescAnal );

		txtCodPai.setVlrString( sCodPai );
		txtDescPai.setVlrString( sDescPai );
		txtCodAnal.setVlrString( sCod );
		txtCodContCred.setVlrString( sCodContCred );
		txtCodContDeb.setVlrString( sCodContDeb );
		txtCodHistPad.setVlrInteger( iCodHist );
		rgFinPlan.setVlrString( sFinalidade );
		rgTipoPlan.setVlrString( sTipo );
		
		if(codredplan!=null && codredplan > 0) {
			txtCodRedAnal.setVlrInteger( codredplan );
		}
		
		if ( "".equals( sFin.trim() ) ) {
			rgFinPlan.setVlrString( sFin );
		}
		if ( sDesc != null ) {
			setTitulo( "Edição de Conta Analítica" );
			txtDescAnal.setVlrString( sDesc );
			txtDescAnal.selectAll();
		}
		if ( sESFin == null || "".equals( sESFin ) ) {
			if ( "D".equals( sTipo ) ) {
				sESFin = "S";
			}
			else {
				sESFin = "E";
			}
		}
		if ( sClasFin == null || "".equals( sESFin ) ) {
			sClasFin = "O";
		}
		rgEsFinPlan.setVlrString( sESFin );
		rgClasFinPlan.setVlrString( sClasFin );
		txtDescAnal.requestFocus();
	}

	private void montaListaCampos() {

		lcHist.add( new GuardaCampo( txtCodHistPad, "CodHist", "Cód.hist.", ListaCampos.DB_PK, false ) );
		lcHist.add( new GuardaCampo( txtDescHistPad, "DescHist", "Descrição do historico padrão", ListaCampos.DB_SI, false ) );
		lcHist.montaSql( false, "HISTPAD", "FN" );
		lcHist.setQueryCommit( false );
		lcHist.setReadOnly( true );
		txtCodHistPad.setTabelaExterna( lcHist, null );
		txtCodHistPad.setListaCampos( lcHist );
		txtCodHistPad.setNomeCampo( "CodHist" );
		txtCodHistPad.setChave( ListaCampos.DB_PK );
		txtDescHistPad.setListaCampos( lcHist );
		txtDescHistPad.setNomeCampo( "DescHist" );
		txtDescHistPad.setLabel( "Descrição do historico padrão" );
	}

	private void montaRadioGroup() {

		vValsTipoPlan.addElement( "B" );
		vValsTipoPlan.addElement( "C" );
		vValsTipoPlan.addElement( "D" );
		vValsTipoPlan.addElement( "R" );
		vLabsTipoPlan.addElement( "Bancos" );
		vLabsTipoPlan.addElement( "Caixa" );
		vLabsTipoPlan.addElement( "Despesas" );
		vLabsTipoPlan.addElement( "Receitas" );
		
		rgTipoPlan = new JRadioGroup<String, String>( 2, 2, vLabsTipoPlan, vValsTipoPlan );
		rgTipoPlan.setAtivo( 0, false );
		rgTipoPlan.setAtivo( 1, false );
		rgTipoPlan.setAtivo( 2, false );
		rgTipoPlan.setAtivo( 3, false );

		vValsFinPlan.addElement( "RV" );
		vValsFinPlan.addElement( "OR" );
		vValsFinPlan.addElement( "ER" );
		vValsFinPlan.addElement( "CF" );
		vValsFinPlan.addElement( "CV" );
		vValsFinPlan.addElement( "II" );
		vValsFinPlan.addElement( "RF" );
		vValsFinPlan.addElement( "DF" );
		vValsFinPlan.addElement( "CS" );
		vValsFinPlan.addElement( "IR" );
		vValsFinPlan.addElement( "OO" );
		vLabsFinPlan.addElement( "RV - Receitas sobre vendas" );
		vLabsFinPlan.addElement( "OR - Outras receitas" );
		vLabsFinPlan.addElement( "ER - Estorno de receitas" );
		vLabsFinPlan.addElement( "CF - Custo fixo" );
		vLabsFinPlan.addElement( "CV - Custo variável" );
		vLabsFinPlan.addElement( "II - Investimentos" );
		vLabsFinPlan.addElement( "RF - Receitas financeiras" );
		vLabsFinPlan.addElement( "DF - Despesas financeiras" );
		vLabsFinPlan.addElement( "CS - Contribuição social" );
		vLabsFinPlan.addElement( "IR - Imposto de renda" );
		vLabsFinPlan.addElement( "OO - Outros" );
		
		rgFinPlan = new JRadioGroup<String, String>( 6, 2, vLabsFinPlan, vValsFinPlan );

		vValsEsFinPlan.addElement( "E" );
		vValsEsFinPlan.addElement( "S" );
		vLabsEsFinPlan.addElement( "Entrada" );
		vLabsEsFinPlan.addElement( "Saída" );
		
		rgEsFinPlan = new JRadioGroup<String, String>( 1, 2, vLabsEsFinPlan, vValsEsFinPlan );

		vValsClasFinPlan.addElement( "O" );
		vValsClasFinPlan.addElement( "N" );
		vLabsClasFinPlan.addElement( "Operacional" );
		vLabsClasFinPlan.addElement( "Não Operacional" );
		
		rgClasFinPlan = new JRadioGroup<String, String>( 1, 2, vLabsClasFinPlan, vValsClasFinPlan );

	}

	private void montaTela() {

		panelfields.adic( txtCodPai			, 7		, 20	, 80	, 20	, "Cód.origem"			 			);
		panelfields.adic( txtDescPai		, 90	, 20	, 300	, 20	, "Descrição da origem" 			);
		panelfields.adic( txtCodAnal		, 7		, 60	, 110	, 20	, "Código" 							);
		panelfields.adic( txtDescAnal		, 120	, 60	, 270	, 20	, "Descrição" 						);

		panelfields.adic( txtCodRedAnal 	, 7		, 100	, 110	, 20	, "Código reduzido" 				);
		
		panelfields.adic( rgTipoPlan		, 7		, 130	, 383	, 60 										);
		panelfields.adic( rgFinPlan			, 7		, 215	, 383	, 130	, "Finalidade" 						);
		panelfields.adic( rgEsFinPlan		, 7		, 370	, 383	, 30	, "Origem" 							);
		panelfields.adic( rgClasFinPlan		, 7		, 420	, 383	, 30	, "Classificação" 					);

		panelcontabil.adic( txtCodContDeb	, 7		, 20	, 150	, 20	, "Cód.cont.débito" 				);
		panelcontabil.adic( txtCodContCred	, 160	, 20	, 150	, 20	, "Cód.cont.crédito" 				);
		panelcontabil.adic( txtCodHistPad	, 7		, 60	, 80	, 20	, "Cód.hist." 						);
		panelcontabil.adic( txtDescHistPad	, 90	, 60	, 300	, 20	, "Descrição do historico padrão" 	);

		tabbedpanel.add( "Geral"			, panelfields 	);
		tabbedpanel.add( "Contábil"			, panelcontabil );

		panelgeral.add( tabbedpanel, BorderLayout.CENTER );
		
		setPanel( panelgeral );
		
	}

	private void cancText( JTextFieldPad txt ) {

		txt.setBackground( Color.lightGray );
		txt.setFont( new Font( "Dialog", Font.BOLD, 12 ) );
		txt.setEditable( false );
		txt.setForeground( new Color( 118, 89, 170 ) );
		
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtDescAnal.getText().trim().length() == 0 ) {
				Funcoes.mensagemErro( this, "O campo descrição está em branco!" );
				txtDescAnal.requestFocus();
				return;
			}
		}
		super.actionPerformed( evt );
	}

	public Object[] getValores() {

		Object[] ret = new Object[ 8 ];
		
		ret[ 0 ] = txtDescAnal.getVlrString();
		ret[ 1 ] = rgFinPlan.getVlrString();
		ret[ 2 ] = txtCodContCred.getVlrString();
		ret[ 3 ] = txtCodContDeb.getVlrString();
		ret[ 4 ] = txtCodHistPad.getVlrInteger();
		ret[ 5 ] = rgEsFinPlan.getVlrString();
		ret[ 6 ] = rgClasFinPlan.getVlrString();
		ret[ 7 ] = txtCodRedAnal.getVlrInteger();

		return ret;
	}

	@ Override
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcHist.setConexao( cn );
		lcHist.carregaDados();
	}
}
