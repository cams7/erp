/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLAdicOrc.java <BR>
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
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.event.ActionListener;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;

public class DLContingencia extends FDialogo implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtDtEntrada = new JTextFieldPad( JTextFieldPad.TP_DATE, 8, 0 );
	
	private JTextFieldPad txtHEntrada = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );
	
	private JTextFieldPad txtDtSaida = new JTextFieldPad( JTextFieldPad.TP_DATE, 8, 0 );
	
	private JTextFieldPad txtHSaida = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );
	
	private JTextFieldPad txtTipoEmissaoNFe = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextAreaPad txaJustificativa = new JTextAreaPad( 256 );
	
	public ListaCampos lcCampos = new ListaCampos(this);
	
	
	public DLContingencia() {
		super();
		setTitulo( "Contingência NFE" );
		setAtribos( 120, 140, 750, 300 );
		montaTela();
	}
	
	public void montaTela(){
		
		adic( new JLabelPad( "Data Entrada" ), 7, 0, 100, 20 );
		adic( txtDtEntrada, 7, 20, 130, 20 );
		
		adic( new JLabelPad( "Hora Entrada" ), 7, 0, 100, 20 );
		adic( txtHEntrada, 140, 20, 130, 20 );
		
		adic( new JLabelPad( "Data Saída" ), 7, 40, 100, 20 );
		adic( txtDtSaida, 7, 60, 130, 20 );
	
		adic( new JLabelPad( "Hora Saída" ), 7, 40, 100, 20 );
		adic( txtHEntrada, 140, 60, 130, 20 );
	
		adic( new JLabelPad( "Justificativa" ), 7, 10, 200, 20 );
		adic( txaJustificativa, 7, 100, 400, 20 );
		
	}
	

	public void beforeCarrega( CarregaEvent cevt ) {
	}

	public void afterCarrega( CarregaEvent cevt ) {
	}
	
	@ Override
	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		
		lcCampos.setConexao( cn );
	}

	
}
