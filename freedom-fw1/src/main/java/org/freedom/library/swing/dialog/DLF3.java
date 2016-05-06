/**
 * @version 23/02/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)DLF3.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Comentários da classe.....
 */

package org.freedom.library.swing.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import org.freedom.library.swing.component.JTablePad;

public abstract class DLF3 extends FFDialogo implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTablePad tab = new JTablePad();
	private JScrollPane spnCentro = new JScrollPane(tab);
	public Object oRetVal = null;

	/**
	 * 
	 * Classe mãe para dialogos auxiliares. Construtor da classe...criado grid
	 * com <BR>
	 * 2 colunas "origem e código aux", <BR>
	 * origem: origem da chave, ex: tabela de preços. <BR>
	 * código aux.: código auxilial para busca. <BR>
	 * 
	 * @param cOrig
	 *            - Janela mãe do dialogo.
	 */
	public DLF3() {
		// super(cOrig);
		setAlwaysOnTop(true);
		setTitulo("Pesquisa auxiliar");
		setAtribos(550, 260);
		setResizable(true);

		c.add(spnCentro, BorderLayout.CENTER);

		addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				if (tab.getNumLinhas() > 0) {
					tab.requestFocus();
					// tab.setLinhaSel(0);
				}
				else
					btCancel.requestFocus();
			}
		});
		
		
		// Evitando que o ENTER no grid simule o duplo click
		InputMap im =  tab.getInputMap();
		ActionMap am =  tab.getActionMap();
	
		im.clear();
		am.clear();
		
		Action enterKey = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				try	{					
					acaoOk();
				}
				catch (Exception ex){
					ex.printStackTrace();
				}					
			}
		};

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "acaoOk");
		am.put("acaoOk", enterKey);
		
		
		
	}

	public abstract boolean setValor(Object oVal, String sTipo);

	public Object getValor() {
		return oRetVal;
	}

	public Object getValorGrid() {
		Object oRet = null;
		if (tab.getNumLinhas() > 0 && tab.getLinhaSel() >= 0)
			oRet = tab.getValor(tab.getLinhaSel(), 1);
		return oRet;
	}

	private void acaoOk() {
		
		btOK.doClick();
		
	}
	
	public void keyPressed(KeyEvent kevt) {
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
				
				acaoOk();
				
		}
		else
			super.keyPressed(kevt);

	}

	public void keyReleased(KeyEvent kevt) {
	}

	public void keyTyped(KeyEvent kevt) {
	}

}
