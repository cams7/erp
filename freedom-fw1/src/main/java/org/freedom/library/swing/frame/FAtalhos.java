/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)FLogin.java <BR>
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
 * Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.util.SwingParams;

public class FAtalhos extends FFDialogo {
	private static final long serialVersionUID = 1L;

	private JPanelPad pnAtalhos = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

	public FAtalhos() {

		super(Aplicativo.telaPrincipal);
		setTitulo("Atalhos");
		setAtribos(380, 550);

		setToFrameLayout();

		c.add(pnAtalhos);

		JLabelPad lbAtalhos = new JLabelPad();
		StringBuilder atalhos = new StringBuilder();

		atalhos.append("<HTML><BODY>");
		atalhos.append("<UL>");
		atalhos.append("<LI><STRONG>CTRL + N</STRONG> - <EM>Novo Registro</EM>");
		atalhos.append("<LI><STRONG>CTRL + S</STRONG> - <EM>Gravar Alterações</EM>");
		atalhos.append("<LI><STRONG>CTRL + D</STRONG> - <EM>Apagar Registro</EM>");
		atalhos.append("<LI><STRONG>CTRL + E</STRONG> - <EM>Editar</EM>");
		atalhos.append("<LI><STRONG>CTRL + W</STRONG> - <EM>Cancelar Alterações</EM>");
		atalhos.append("<LI><STRONG>CTRL + P</STRONG> - <EM>Imprimir Registro</EM>");
		atalhos.append("<LI><STRONG>CTRL + R</STRONG> - <EM>Visualizar Impressão</EM>");
		atalhos.append("<LI><STRONG>CTRL + O</STRONG> - <EM>Observações</EM>");
		atalhos.append("<LI><STRONG>CTRL + I</STRONG> - <EM>Imprimir</EM>");
		atalhos.append("<LI><STRONG>CTRL + P</STRONG> - <EM>Previsão de Impressão</EM>");
		atalhos.append("<LI><STRONG>TAB</STRONG> - <EM>Vai para o próximo campo</EM>");
		atalhos.append("<LI><STRONG>SHIFT + TAB</STRONG> - <EM>Volta para o campo anterior</EM>");
		atalhos.append("<LI><STRONG>SHIFT + F4</STRONG> - <EM>Fecha a Tela</EM>");
		atalhos.append("<LI><STRONG>BARRA DE ESPAÇOS</STRONG> - <EM>Aperta um botão</EM>");
		atalhos.append("<LI><STRONG>CTRL + PAGE UP</STRONG> - <EM>Vai para o Primeiro Registro</EM>");
		atalhos.append("<LI><STRONG>PAGE UP</STRONG> - <EM>Vai para o Registro Anterior</EM>");
		atalhos.append("<LI><STRONG>PAGE DOWN</STRONG> - <EM>Vai para o Próximo Registro</EM>");
		atalhos.append("<LI><STRONG>CTRL + PAGE DOWN</STRONG> - <EM>vai para o Último Registro</EM>");
		atalhos.append("<LI><STRONG>F1</STRONG> - <EM>Atalhos</EM>");
		atalhos.append("<LI><STRONG>F2</STRONG> - <EM>Procurar</EM>");
		atalhos.append("<LI><STRONG>F3</STRONG> - <EM>Procurar similar</EM>");
		atalhos.append("<LI><STRONG>F4</STRONG> - <EM>Completar o Orçamento</EM>");
		atalhos.append("<LI><STRONG>F4</STRONG> - <EM>Fechar a Compra</EM>");
		atalhos.append("<LI><STRONG>F4</STRONG> - <EM>Fechar a Venda</EM>");
		atalhos.append("<LI><STRONG>F5</STRONG> - <EM>Consulta pagamentos</EM>");
		atalhos.append("<LI><STRONG>F6</STRONG> - <EM>Abre tela de cadastro</EM>");
		atalhos.append("<LI><STRONG>ESC</STRONG> - <EM>Sair da Tela</EM>");
		atalhos.append("</UL>");
		atalhos.append("</BODY>");
		atalhos.append("</HTML>");

		lbAtalhos.setText(atalhos.toString());
		lbAtalhos.setFont(SwingParams.getFontbold());

		pnAtalhos.add(lbAtalhos);

	}
}
