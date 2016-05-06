/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FTurnos.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  Tela de cadastro de turnos
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;

public class FTurnos extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodTurno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtDescTurno = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtNhsTurno = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private final JTextFieldPad txtHIniTurno = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );

	private final JTextFieldPad txtHIniIntTurno = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );

	private final JTextFieldPad txtHFimIntTurno = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );

	private final JTextFieldPad txtHFimTurno = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );
	
	private final JCheckBoxPad cbTrabSabTurno = new JCheckBoxPad( "Trabalha nos sábados", "S", "N" );

	private final JCheckBoxPad cbTrabDomTurno = new JCheckBoxPad( "Trabalha nos domingos", "S", "N" );

	private final JTextFieldPad txtPercBHTBSabTurno = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtPercBHTBDomTurno = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );

	private final JTextFieldPad txtPercBHTBFerTurno = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDec );
	
	private final Vector<String> vTipoTurnoLab = new Vector<String>();

	private final Vector<String> vTipoTurnoVal = new Vector<String>();

	private JRadioGroup<String, String> rgTipoTurno = null;

	public FTurnos() {
 
		super();

		nav.setNavigation( true );
		setTitulo( "Cadastro de Turnos" );
		setAtribos( 50, 50, 550, 450 );

		vTipoTurnoLab.addElement( "Normal ( manhã e tarde )" );
		vTipoTurnoLab.addElement( "Manhã" );
		vTipoTurnoLab.addElement( "Tarde" );
		vTipoTurnoLab.addElement( "Noite" );
		vTipoTurnoLab.addElement( "Especial" );
		vTipoTurnoVal.addElement( "N" );
		vTipoTurnoVal.addElement( "M" );
		vTipoTurnoVal.addElement( "T" );
		vTipoTurnoVal.addElement( "O" );
		vTipoTurnoVal.addElement( "E" );
		rgTipoTurno = new JRadioGroup<String, String>( 3, 2, vTipoTurnoLab, vTipoTurnoVal );
		rgTipoTurno.setVlrString( "N" );

		montaTela();

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		setImprimir( true );
	}

	private void montaTela() {

		adicCampo( txtCodTurno, 7, 20, 80, 20, "CodTurno", "Cód.turno", ListaCampos.DB_PK, true );
		adicCampo( txtDescTurno, 90, 20, 223, 20, "DescTurno", "Descrição do turno", ListaCampos.DB_SI, true );
		adicCampo( txtNhsTurno, 316, 20, 100, 20, "NhsTurno", "Nº H.semanais", ListaCampos.DB_SI, true );
		adicCampo( txtHIniTurno, 7, 60, 100, 20, "HIniTurno", "Inicío do turno", ListaCampos.DB_SI, true );
		adicCampo( txtHIniIntTurno, 110, 60, 100, 20, "HIniIntTurno", "Inicío do intervalo", ListaCampos.DB_SI, true );
		adicCampo( txtHFimIntTurno, 213, 60, 100, 20, "HFimIntTurno", "Fim do intervalo", ListaCampos.DB_SI, true );
		adicCampo( txtHFimTurno, 316, 60, 100, 20, "HFimTurno", "fim do turno", ListaCampos.DB_SI, true );
		adicDB( rgTipoTurno, 7, 100, 409, 70, "TipoTurno", "Tipo de Turno:", true );
		adicDB( cbTrabSabTurno, 7, 180, 200, 20,"TrabSabTurno", "", false );
		adicDB( cbTrabDomTurno, 210, 180, 200, 20,"TrabDomTurno", "", false );
		adicCampo( txtPercBHTBSabTurno, 7,230, 130, 20, "PercBHTBSabTurno", "B.H.% Adic.Sábados", ListaCampos.DB_SI, true);
		adicCampo( txtPercBHTBDomTurno, 140,230, 130, 20, "PercBHTBDomTurno", "B.H.% Adic.Domingos", ListaCampos.DB_SI, true);
		adicCampo( txtPercBHTBFerTurno, 273,230, 130, 20, "PercBHTBFerTurno", "B.H.% Adic.Feriados", ListaCampos.DB_SI, true);
		setListaCampos( true, "TURNO", "RH" );
		lcCampos.setQueryInsert( false );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "RHDEPTO" ) );

		dlGr = new FPrinterJob( "relatorios/grhTurnos.jasper", "Lista de Turnos", "", this, hParam, con, null, false );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro na geração do relátorio!" + e.getMessage(), true, con, e );
			}
		}
	}
}
