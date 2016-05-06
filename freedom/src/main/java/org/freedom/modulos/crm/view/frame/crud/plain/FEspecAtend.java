/**
 * @version 20/01/2011 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm <BR>
 *         Classe: @(#)FOrigContato.java <BR>
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
 * 	Tela para cadastrameto de tipos de especificação de atendimentos
 * 
 */
package org.freedom.modulos.crm.view.frame.crud.plain;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;

public class FEspecAtend extends FDados {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodEspec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtDescSpec = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private final JCheckBoxPad cbPgComiEspec = new JCheckBoxPad( "Paga comissão ?", "S", "N" );
	
	private final JCheckBoxPad cbCobCliEspec = new JCheckBoxPad( "Cobra do cliente ?", "S", "N" );
	
	private final JCheckBoxPad cbContMetaEspec = new JCheckBoxPad( "Conta para meta do atendente ?", "S", "N" );
	
	private final JCheckBoxPad cbMrelCobEspec = new JCheckBoxPad( "Mostra no relatório de cobrança ?", "S", "N" );
	
	private final JCheckBoxPad cbBhEspec = new JCheckBoxPad( "Conta para banco de horas ?", "S", "N" );
	
	private final JCheckBoxPad cbObrigChamEspec = new JCheckBoxPad( "Chamado Obrigatório no Atendimento ?", "S", "N" );
	
	private final JCheckBoxPad cbObrigProjEspec = new JCheckBoxPad( "Contrato/Projeto Obrigatório no Atendimento ?", "S", "N" );
	
	private final JTextFieldPad txtTempoMinCobEspec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private final JTextFieldPad txtTempoMaxCobEspec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private final JTextFieldPad txtPercComiEspec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 2, Aplicativo.casasDecFin );
	
	public FEspecAtend() {

		super();
		
		setTitulo( "Especificação de Atendimentos" );
		
		setAtribos( 15, 30, 580,330 );
		
		montaTela(); 
		
	}

	private void montaTela() {
		
		nav.setNavigation( true );

		adicCampo( 	txtCodEspec, 			 7, 	 20, 	100, 	20, "CodEspec", 		"Cód.Espec.", 					ListaCampos.DB_PK, true );
		adicCampo( 	txtDescSpec, 		   110, 	 20, 	360, 	20, "DescEspec", 		"Descrição da especificação", 	ListaCampos.DB_SI, true );
		
		adicCampo( 	txtTempoMinCobEspec, 	 7, 	 60, 	100, 	20, "TempoMinCobEspec", "Tempo Min.(m)", 				ListaCampos.DB_SI, true );
		adicCampo( 	txtTempoMaxCobEspec,   110, 	 60, 	100, 	20, "TempoMaxCobEspec", "Tempo Max.(m)", 				ListaCampos.DB_SI, true );
		adicCampo( 	txtPercComiEspec, 	   213, 	 60, 	100, 	20, "PercComiEspec", 	"% Comissão", 					ListaCampos.DB_SI, true );
		
		adicDB( 	cbCobCliEspec, 			 7, 	100, 	300, 	20, "CobCliEspec", 		"", 												true );
		adicDB( 	cbContMetaEspec, 		 7, 	120, 	300, 	20, "ContMetaEspec", 	"", 												true );
		adicDB( 	cbPgComiEspec, 			 7, 	140, 	300, 	20, "PgComiEspec", 		"", 												true );
		adicDB( 	cbMrelCobEspec, 		 7, 	160, 	300, 	20, "MrelCobEspec", 	"", 												true );
		adicDB( 	cbBhEspec, 				 7, 	180, 	300, 	20, "BhEspec", 			"", 												true );
		adicDB( 	cbObrigChamEspec,		 7, 	200, 	300, 	20, "ObrigChamEspec", 	"", 												true );
		adicDB( 	cbObrigProjEspec,		 7, 	220, 	300, 	20, "ObrigProjEspec", 	"", 												true );

		setListaCampos( true, "ESPECATEND", "AT" );
	}

}
