/**
 * @version 02/05/2011S <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.modulos.fnc.library.swing.component <BR>
 * Classe: @(#)JTextFieldPlan.java <BR>
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
 * Classe padrão para entrada de dados de códigos de planejamento financeiro.
 */

package org.freedom.modulos.fnc.library.swing.component;

import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;

public class JTextFieldPlan extends JTextFieldPad {

	private static final long serialVersionUID = 1L;
	
	public JTextFieldPlan(int tipo, int tam, int dec) {
		super(tipo, tam, dec);
	}
	
	public void keyPressed(KeyEvent kevt) {
		
		if (kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			
			String codred = this.getVlrString();
			
			// Verifica se o valor digitado é um código reduzido.
			if(codred !=null && codred.length() <13) {

				try {
				
					String codplan = getCodPlan( Integer.parseInt( codred ) );
					
					if(codplan!=null) {
						
						this.setVlrString( codplan );
						
					}
				}
				catch (NumberFormatException e) {
					System.out.println("Valor digitado inválido (não é um número)");
				}
				
			}
		
		}
		
		super.keyPressed( kevt );
		
	}
	
	private String getCodPlan(Integer codredplan) {
		
		String ret = null;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		DbConnection con = null; 
		ResultSet rs = null;
		
		try {   
			 
			con = Aplicativo.getInstace().getConexao();
			
			sql.append("select codplan from fnplanejamento where codemp=? and codfilial=? and codredplan=? ");
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setInt( 3, codredplan );
			
			rs = ps.executeQuery();
			
			System.out.println("Executando busca pelo código reduzido: " + codredplan );
			
			if(rs.next()) {
				ret = rs.getString( "codplan" );
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}

}
