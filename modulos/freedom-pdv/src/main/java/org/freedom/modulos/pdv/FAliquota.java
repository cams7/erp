/**
 * @version 17/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FAliquota.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.ecf.app.ControllerECF;
import org.freedom.ecf.driver.AbstractECFDriver;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.AplicativoPDV;

public class FAliquota extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JPanelPad pinCab = new JPanelPad( 400, 60 );

	private final JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JTextFieldPad txtAliquota = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 4, 2 );

	private final JButtonPad btInsere = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private final JTablePad tab = new JTablePad();

	private final JScrollPane spnTab = new JScrollPane( tab );

	private final ControllerECF ecf;

	private List<String> aliquotas;

	private int sizeAliquotas;

	public FAliquota() {

		setTitulo( "Ajusta aliquotas" );
		setAtribos( 100, 150, 395, 195 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		montaTela();

		setToFrameLayout();

		carregaTabela();
	}

	private void montaTela() {

		btInsere.setPreferredSize( new Dimension( 30, 30 ) );
		btInsere.setToolTipText( "Insere alíquota" );
		btInsere.addActionListener( this );

		setPanel( pnCli );

		pnCli.add( spnTab, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );

		pinCab.adic( new JLabelPad( "Inserir aliquota" ), 7, 5, 87, 20 );
		pinCab.adic( txtAliquota, 7, 25, 87, 20 );
		pinCab.adic( btInsere, 120, 15, 30, 30 );

		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "" );
		tab.adicColuna( "" );

		tab.setTamColuna( 95, 0 );
		tab.setTamColuna( 95, 1 );
		tab.setTamColuna( 95, 2 );
		tab.setTamColuna( 95, 3 );

		tab.setFont( new Font( "Arial", Font.PLAIN, 12 ) );
	}

	private void insereAliquota() {

		DecimalFormat df = new DecimalFormat( "00.00" );
		String strAliquota = df.format( txtAliquota.getVlrBigDecimal().doubleValue() ).replaceAll( ",", "" );

		if ( aliquotas.contains( strAliquota ) ) {
			Funcoes.mensagemErro( this, "Alíquota já foi cadastrada!" );
		}
		else {
			if ( sizeAliquotas > 15 ) {
				Funcoes.mensagemErro( this, "Quantidade maxima de aliquotas já foi atingida!" );
			}
			else {
				if ( !ecf.programaAliquota( txtAliquota.getVlrBigDecimal(), AbstractECFDriver.ICMS ) ) {
					Funcoes.mensagemErro( this, ecf.getMessageLog() );
				}
				carregaTabela();
			}
		}
	}

	private void carregaTabela() {

		aliquotas = ecf.getAliquotas();

		tab.limpa();
		sizeAliquotas = 0;

		String aliq = null;
		DecimalFormat df = new DecimalFormat( "00.00" );

		int row = 0;
		int col = 0;
		int size = aliquotas.size();
		int oldRow = -1;

		float aliquota = 0.0f;

		for ( int i = 0; i < size; i++ ) {

			aliquota = new Float( aliquotas.get( i ) ).floatValue();

			if ( aliquota > 0.0f ) {

				if ( row != oldRow ) {
					tab.adicLinha();
					oldRow = row;
				}

				aliq = "T" + StringFunctions.strZero( String.valueOf( i + 1 ), 2 ) + " = " + df.format( aliquota / 100 ) + " %";

				tab.setValor( aliq, row, col++ );

				if ( col == 4 ) {
					col = 0;
					row++;
				}

				sizeAliquotas++;
			}
		}
	}

	@ Override
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btInsere ) {
			insereAliquota();
		}
	}

}
