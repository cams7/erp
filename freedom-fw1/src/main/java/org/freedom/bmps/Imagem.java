/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *         Projeto: Freedom <BR>
 *         Pacote: bmps <BR>
 *         Classe:
 * @(#)Imagem.java <BR>
 *                 Este programa é licenciado de acordo com a LPG-PC (Licença
 *                 Pública Geral para Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e
 *                 REPRODUÇÕES deste Programa. <BR>
 *                 Caso uma cópia da LPG-PC não esteja disponível junto com este
 *                 Programa, você pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR
 *                 este Programa é preciso estar <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 *                 <BR>
 *                 Comentários para a classe...
 */

package org.freedom.bmps;

import java.awt.Image;
import java.net.URL;

public class Imagem { // extends java.applet.Applet {

	public static String dirImages = "/org/freedom/images/";

	public Imagem() {
	}

	public static Image novo(String nome) {
		URL url = Imagem.class.getResource(dirImages + nome);

		Image img = null;
		if (url != null)
			img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
		return img;
	}
}