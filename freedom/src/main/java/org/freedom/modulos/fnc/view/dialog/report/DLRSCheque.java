/**
 * @version 26/05/2005 <BR>
 * @author Setpoint Informática Ltda / Robson Sanchez.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLRSCheque.java <BR>
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
 *                     ? <BR>
 * 
 */

package org.freedom.modulos.fnc.view.dialog.report;

import java.awt.Component;
import java.util.Vector;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRSCheque extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JRadioGroup<?, ?> rgOrdem = null;

	private JRadioGroup<?, ?> rgSaida = null;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtNome = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JComboBoxPad cbTipoSaida = null;

	private JLabelPad lbOrdem = new JLabelPad( "Ordenar por:" );

	private JLabelPad lbSaida = new JLabelPad( "Visualizar saída:" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs2 = new Vector<String>();

	private Vector<String> vVals2 = new Vector<String>();

	private Vector<String> vStrTipo = new Vector<String>();

	public DLRSCheque( Component cOrig ) {

		super( cOrig );
		setTitulo( "Relatório de Saída de Cheque" );
		setAtribos( 291, 292 );

		// / Prepara o combobox ////////////////////
		vStrTipo.addElement( "<Selecione>" );
		vStrTipo.addElement( "Depositado" );
		vStrTipo.addElement( "Perdido" );
		vStrTipo.addElement( "Repassado" );
		vStrTipo.addElement( "Roubado" );
		vStrTipo.addElement( "Todos" );

		cbTipoSaida = new JComboBoxPad( vStrTipo, vStrTipo, JComboBoxPad.TP_STRING, 10, 0 );

		vLabs2.addElement( "A primeira" );
		vLabs2.addElement( "Todas" );
		vVals2.addElement( "P" );
		vVals2.addElement( "T" );
		rgSaida = new JRadioGroup<String, String>( 1, 2, vLabs2, vVals2 );
		rgSaida.setVlrString( "P" );
		adic( lbSaida, 7, 5, 150, 15 );
		adic( rgSaida, 7, 25, 264, 30 );

		adic( new JLabelPad( "Periodo:" ), 7, 55, 125, 20 );
		adic( new JLabelPad( "De:" ), 7, 75, 35, 20 );
		adic( txtDataini, 32, 75, 100, 20 );
		adic( new JLabelPad( "Até:" ), 140, 75, 25, 20 );
		adic( txtDatafim, 170, 75, 100, 20 );

		vLabs.addElement( "Data" );
		vLabs.addElement( "Nome" );
		vVals.addElement( "D" );
		vVals.addElement( "N" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		adic( lbOrdem, 7, 100, 80, 15 );
		adic( rgOrdem, 7, 120, 264, 30 );

		adic( new JLabelPad( "Tipo de Saída" ), 7, 150, 90, 20 );
		adic( cbTipoSaida, 7, 170, 100, 25 );
		adic( new JLabelPad( "Cliente" ), 111, 150, 75, 20 );
		adic( txtNome, 111, 170, 160, 25 );

	}

	public String getTipoS() {

		return cbTipoSaida.getVlrString();
	}

	public int getTipo() {

		int Result = 0;

		if ( cbTipoSaida.getVlrString().equals( "Depositado" ) )
			Result = 1;
		else if ( cbTipoSaida.getVlrString().equals( "Perdido" ) )
			Result = 2;
		else if ( cbTipoSaida.getVlrString().equals( "Repassado" ) )
			Result = 3;
		else if ( cbTipoSaida.getVlrString().equals( "Roubado" ) )
			Result = 4;
		else if ( cbTipoSaida.getVlrString().equals( "Todos" ) )
			Result = 5;

		return Result;
	}

	public String SetTipo( int valor ) {

		String Result = null;

		if ( valor == 1 )
			Result = "Depósito";
		else if ( valor == 2 )
			Result = "Perda";
		else if ( valor == 3 )
			Result = "Repasse";
		else if ( valor == 4 )
			Result = "Roubo";

		return Result;
	}

	public String getCNome() {

		return txtNome.getVlrString();
	}

	public JTextFieldPad GetDataini() {

		return txtDataini;
	}

	public JTextFieldPad GetDatafim() {

		return txtDatafim;
	}

	public boolean CompData() {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return false;
		}
		else
			return true;
	}

	public boolean CompTipo() {

		if ( ( getTipoS().length() < 1 ) | ( getTipoS().equals( "<Selecione>" ) ) ) {
			Funcoes.mensagemInforma( this, "Escolha um tipo de saída para a consulta! !" );
			return false;
		}
		else
			return true;
	}

	public String getValor() {

		String sRetorno = "";
		if ( rgOrdem.getVlrString().compareTo( "D" ) == 0 )
			sRetorno = "sgscheque.DTINS";
		else if ( rgOrdem.getVlrString().compareTo( "N" ) == 0 )
			sRetorno = "vdcliente.NOMECLI";
		return sRetorno;
	}

	public String getSValor() {

		String sRetorno = "";
		if ( rgSaida.getVlrString().compareTo( "P" ) == 0 )
			sRetorno = "P";
		else if ( rgSaida.getVlrString().compareTo( "T" ) == 0 )
			sRetorno = "T";
		return sRetorno;
	}

}
