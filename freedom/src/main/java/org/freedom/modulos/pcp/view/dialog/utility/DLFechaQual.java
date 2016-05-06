/**
 * @version 17/07/2008 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe:
 * @(#)DLFechaQual.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 */
package org.freedom.modulos.pcp.view.dialog.utility;

import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

import java.util.HashMap;
import java.util.Vector;

public class DLFechaQual extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDescEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDescAfer = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtVlrAfer = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtVlrMin = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private JTextFieldPad txtVlrMax = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	String tipo = "";

	private JTextFieldPad txtCodUnid = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtCasasDec = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 5, 0 );

	public DLFechaQual( String sDescAnalise, String sTipo, BigDecimal bVlrMin, BigDecimal bVlrMax, BigDecimal vlrAfer, String sAfer, String status, boolean editable, String codUnid, DbConnection con ) {

		setTitulo( "Qualidade" );
		setAtribos( 390, 220 );

		adic( new JLabelPad( "Descrição da analise" ), 7, 5, 360, 20 );
		adic( txtDescEst, 7, 25, 360, 20 );

		txtVlrMin.setDecimal( Integer.valueOf( Funcoes.getCasasDecUnid( codUnid, con ) ) );
		txtVlrMax.setDecimal( Integer.valueOf( Funcoes.getCasasDecUnid( codUnid, con ) ) );
		txtVlrAfer.setDecimal( Integer.valueOf( Funcoes.getCasasDecUnid( codUnid, con ) ) );

		tipo = sTipo;

		vLabs1.addElement( "Pendente" );
		vLabs1.addElement( "Recusada" );
		vLabs1.addElement( "Aprovada" );
		vLabs1.addElement( "Corrigida" );
		vVals1.addElement( "PE" );
		vVals1.addElement( "RC" );
		vVals1.addElement( "AP" );
		vVals1.addElement( "CO" );

		rgTipo = new JRadioGroup<String, String>( 1, 3, vLabs1, vVals1 );

		if ( "DT".equals( sTipo ) ) {

			adic( new JLabelPad( "Aferição" ), 7, 45, 360, 20 );
			adic( txtDescAfer, 7, 65, 360, 20 );
			adic( rgTipo, 7, 95, 360, 30 );
			rgTipo.setVlrString( status );

			txtDescAfer.setRequerido( true );
			txtDescAfer.setVlrString( sAfer );
		}
		else if ( "MM".equals( sTipo ) ) {

			adic( new JLabelPad( "Vlr.Mín." ), 7, 45, 70, 20 );
			adic( txtVlrMin, 7, 65, 70, 20 );
			adic( new JLabelPad( "Vlr.Máx." ), 80, 45, 70, 20 );
			adic( txtVlrMax, 80, 65, 70, 20 );
			adic( new JLabelPad( "Aferição" ), 7, 85, 210, 20 );
			adic( txtVlrAfer, 7, 105, 70, 20 );
			adic( txtCodUnid, 7, 155, 70, 20 );
			adic( txtCasasDec, 80, 155, 70, 20 );

			txtVlrMin.setSoLeitura( true );
			txtVlrMax.setSoLeitura( true );
			txtVlrAfer.setRequerido( true );

			txtVlrMin.setVlrBigDecimal( bVlrMin );
			txtVlrMax.setVlrBigDecimal( bVlrMax );
			txtVlrAfer.setVlrBigDecimal( vlrAfer == null ? new BigDecimal( 0 ) : vlrAfer );
		}

		txtDescEst.setVlrString( sDescAnalise );
		txtDescEst.setAtivo( false );

		if ( !editable ) {
			rgTipo.setAtivo( false );
			txtVlrAfer.setAtivo( false );
			txtDescAfer.setAtivo( false );
		}

	}

	public String getStatus() {

		return rgTipo.getVlrString();
	}

	public HashMap<String, Object> getValor() {

		HashMap<String, Object> hRet = new HashMap<String, Object>();

		try {
			hRet.put( "DESCAFER", txtDescAfer.getVlrString() );
			hRet.put( "VLRAFER", txtVlrAfer.getVlrBigDecimal() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return hRet;
	}

	private int casasDec() {

		int ret = 0;

		return ret;

	}

	public void ok() {

		if ( "DT".equals( tipo ) ) {
			if ( ( txtDescAfer.getVlrString().equals( "" ) ) ) {
				Funcoes.mensagemInforma( this, "Informe a descrição!" );
				return;
			}
			else {
				super.ok();
			}
		}
		else if ( "MM".equals( tipo ) ) {
			if ( ( txtVlrAfer.getVlrString().equals( "" ) ) ) {
				Funcoes.mensagemInforma( this, "Informe o valor!" );
				return;
			}
			else {
				super.ok();
			}
		}
	}
}
