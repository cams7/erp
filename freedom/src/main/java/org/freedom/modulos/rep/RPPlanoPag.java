/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPPlanoPag.java <BR>
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
 *                     Tela de cadastro de planos de pagamento.
 * 
 */

package org.freedom.modulos.rep;

import java.math.BigDecimal;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;

public class RPPlanoPag extends FDetalhe implements CarregaListener, InsertListener, DeleteListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelPlanoPag = new JPanelPad();

	private final JPanelPad panelParcelas = new JPanelPad();

	private final JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtDescPlanoPag = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private final JTextFieldPad txtNumParc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtNumItemPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private final JTextFieldPad txtDiasItemPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtPercItemPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, AplicativoRep.casasDec );

	private final JTextFieldPad txtJurosParcPag = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 6, AplicativoRep.casasDec );

	private final JTextFieldPad txtDescItemPag = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	public RPPlanoPag() {

		super( false );
		setTitulo( "Cadastro de Planos de Pagamento" );
		setAtribos( 50, 50, 405, 350 );

		montaMaster();

		setListaCampos( true, "PLANOPAG", "RP" );
		lcCampos.setQueryInsert( true );

		setListaCampos( lcDet );
		setNavegador( navRod );

		montaDetale();

		setListaCampos( true, "PARCPLANOPAG", "RP" );
		lcDet.setQueryInsert( false );

		navRod.setAtivo( 4, false );
		navRod.setAtivo( 5, false );

		montaTab();

		tab.setTamColuna( 40, 0 );
		tab.setTamColuna( 40, 1 );
		tab.setTamColuna( 60, 2 );
		tab.setTamColuna( 60, 3 );
		tab.setTamColuna( 189, 4 );

		lcCampos.addCarregaListener( this );
		lcCampos.addInsertListener( this );
		lcCampos.addDeleteListener( this );

		lcDet.addPostListener( this );
	}

	private void montaMaster() {

		/**********************
		 * PLANO DE PAGAMENTO *
		 **********************/

		setAltCab( 100 );
		setPainel( panelPlanoPag, pnCliCab );

		adicCampo( txtCodPlanoPag, 7, 20, 70, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, null, true );
		adicCampo( txtDescPlanoPag, 80, 20, 217, 20, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, null, true );
		adicCampo( txtNumParc, 300, 20, 67, 20, "ParcPlanoPag", "N° Parcs.", ListaCampos.DB_SI, null, true );

	}

	private void montaDetale() {

		/**********************
		 * PARCELAMENTO *
		 **********************/

		setAltDet( 100 );
		setPainel( panelParcelas, pnDet );

		adicCampo( txtNumItemPag, 7, 20, 60, 20, "NroParcPag", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtDiasItemPag, 70, 20, 100, 20, "DiasPag", "Dias", ListaCampos.DB_SI, true );
		adicCampo( txtPercItemPag, 173, 20, 100, 20, "PercPag", "Percento", ListaCampos.DB_SI, true );
		adicCampo( txtJurosParcPag, 276, 20, 100, 20, "JurosParcPag", "Juros", ListaCampos.DB_SI, false );
		adicCampo( txtDescItemPag, 7, 60, 369, 20, "DescParcPag", "Descrição", ListaCampos.DB_SI, false );

	}

	private boolean verificaPercParcelas() {

		boolean retorno = false;

		float total = 0f;
		BigDecimal cem = new BigDecimal( "100" ).setScale( AplicativoRep.casasDec, BigDecimal.ROUND_HALF_UP );

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {

			if ( txtNumItemPag.getVlrInteger().intValue() != (Integer) tab.getValor( i, 0 ) ) {

				total += new BigDecimal( ( (String) tab.getValor( i, 2 ) ).replace( ',', '.' ) ).floatValue();
			}
			else {

				total += txtPercItemPag.getVlrBigDecimal().floatValue();
			}
		}

		if ( total == cem.floatValue() ) {
			retorno = true;
		}

		return retorno;
	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcCampos && e.ok ) {
			txtNumParc.setEditable( false );
		}
	}

	public void afterInsert( InsertEvent e ) {

		txtNumParc.setEditable( true );
	}

	public void beforeInsert( InsertEvent e ) {

	}

	@ Override
	public void beforePost( PostEvent e ) {

		if ( e.getListaCampos() == lcDet ) {
			/*
			 * if ( ! verificaPercParcelas() ) { lcDet.cancelPost(); Funcoes.mensagemErro( this, "Porcentagem das parcelas devem totalizar 100%!" ); }
			 */
		}

		super.beforePost( e );
	}

	public void afterDelete( DeleteEvent e ) {

		lcDet.getTab().limpa();
		lcDet.limpaCampos( true );
	}

	public void beforeDelete( DeleteEvent e ) {

	}

}
