/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 *         Pacote: org.freedom.telas <BR>
 *         Classe:
 * @(#)FTabDados.java <BR>
 * 
 *                    Este programa é licenciado de acordo com a LPG-PC (Licença
 *                    Pública Geral para Programas de Computador), <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU
 *                    como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES
 *                    e REPRODUÇÕES deste Programa. <BR>
 *                    Caso uma cópia da LPG-PC não esteja disponível junto com
 *                    este Programa, você pode contatar <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO
 *                    ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou
 *                    ALTERAR este Programa é preciso estar <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;

public class FTabDados extends FDados implements KeyListener {

    private static final long serialVersionUID = 1L;

    public static boolean     comScroll        = false;

    private boolean           Ctrl             = false;

    public JTabbedPanePad     tpn              = new JTabbedPanePad();

    boolean                   ind              = true;

    public FTabDados() {

        this(true);

        setInitFirstFocus(false);
        setTitulo("Formulário de tabulação", this.getClass().getName());
        setAtribos(50, 50, 350, 300);

        pnCliente.remove(pinDados);
        tpn.addKeyListener(this);
        addKeyListener(this);

    }

    public FTabDados(boolean comScroll) {

        super(comScroll);

    }

    public JTabbedPanePad adicTab(String tit, JComponent pn) {
        tpn.add(tit, pn);
        pn.addKeyListener(this);
        pnCliente.add(tpn, BorderLayout.CENTER);
        return tpn;

    }

    public void removeTab(String tit, JPanelPad pn) {
        int i = -1;
        if ((i = tpn.indexOfTab(tit)) > -1) {
            tpn.removeTabAt(i);
            pn.removeKeyListener(this);
            pnCliente.add(tpn, BorderLayout.CENTER);
        }
    }

    public void keyPressed(KeyEvent kevt) {
        if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
            Ctrl = true;
        if ((Ctrl) & (kevt.getKeyCode() == KeyEvent.VK_TAB)) {
            if (tpn.getSelectedIndex() < tpn.getTabCount() - 1)
                tpn.setSelectedIndex(tpn.getSelectedIndex() + 1);
            else
                tpn.setSelectedIndex(0);
        }
        super.keyPressed(kevt);
    }

    public void keyReleased(KeyEvent kevt) {
        if (kevt.getKeyCode() == KeyEvent.VK_CONTROL)
            Ctrl = false;
        super.keyReleased(kevt);
    }

    public void keyTyped(KeyEvent kevt) {
        if ((Ctrl) & (kevt.getKeyChar() == '\t')) {
            if (tpn.getSelectedIndex() < tpn.getTabCount() - 1)
                tpn.setSelectedIndex(tpn.getSelectedIndex() + 1);
            else
                tpn.setSelectedIndex(0);
        }
        super.keyTyped(kevt);
    }

    public void show() {
        primShow = false;
        super.show();
        ajustaScroll();
        // this.pnCliente.setBounds(0,0,100,100);
    }
}