/**
 * @version 27/06/2003 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freeedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)DLAtualiza.java <BR>
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
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.cfg.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLAtualiza extends FFDialogo {

	private static final long serialVersionUID = 1L;

	public DLAtualiza( Component cOrig ) {

		super( cOrig );
		setTitulo( "Atualização do sistema" );
		setAtribos( 450, 300 );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			try {
				String sColumns = "";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery( "SELECT * FROM VDTIPOCLI" );
				ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				for ( int i = 1; i <= numberOfColumns; i++ ) {
					sColumns = sColumns + rsmd.getColumnName( i ) + ", ";
				}
				Funcoes.mensagemErro( this, "Núm. de cols.: " + numberOfColumns );
				Funcoes.mensagemErro( this, "Colunas: \n" + sColumns );
				// boolean b = rsmd.isSearchable(1);
			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "Erro: " + e.getMessage() );
			}

		}
		super.actionPerformed( evt );
	}
}
