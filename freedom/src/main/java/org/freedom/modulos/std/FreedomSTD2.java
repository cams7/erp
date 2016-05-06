/**
 * @version 02/02/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)Freedomstd.java <BR>
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
 *                     Tela principal do módulo standard.
 * 
 */

package org.freedom.modulos.std;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrincipal2;
import org.freedom.library.swing.frame.LoginPD;
import org.freedom.modulos.std.view.background.Background34;

public class FreedomSTD2 extends FreedomSTD {

	public FreedomSTD2() {

		super( "iconstd.png", "splashSTD.png", 1, "Freedom", 1, "Standard", null, new FPrincipal2( new Background34(), "bgFreedomSTD2.jpg" ), LoginPD.class );
		this.montaMenu();
	}

	public static void main( String sParams[] ) {

		try {
			Aplicativo.setLookAndFeel( "freedom.ini" );
			FreedomSTD2 freedom = new FreedomSTD2();
			FPrincipal2.carregaAgenda();
			freedom.show();
		} catch ( Throwable e ) {
			Funcoes.criaTelaErro( "Erro de execução\n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getLocalizedMessage() );
			e.printStackTrace();
		}
	}
}
