/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLAltComisVenda.java <BR>
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
 */

package org.freedom.modulos.std.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLAltComisVend extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtPercComis = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private int iCodVenda = 0;

	public DLAltComisVend( Component cOrig, int iCodVenda, BigDecimal bigComis, DbConnection con ) {

		super( cOrig );

		setTitulo( "Alteração de comissão" );
		setAtribos( 250, 120 );
		setConexao( con );
		this.iCodVenda = iCodVenda;

		setToFrameLayout();

		txtPercComis.setVlrBigDecimal( bigComis );

		adic( new JLabelPad( "% Comissão" ), 7, 0, 133, 20 );
		adic( txtPercComis, 7, 20, 140, 20 );
		adic( btExec, 160, 10, 30, 30 );

		btExec.addActionListener( this );
	}

	private void alterar() {

		if ( txtPercComis.getVlrDouble().doubleValue() < 0 ) {
			Funcoes.mensagemInforma( this, "Percentual inválido!" );
			return;
		}
		String sSQL = "UPDATE VDVENDA SET PERCMCOMISVENDA=? WHERE CODVENDA=? AND CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setBigDecimal( 1, txtPercComis.getVlrBigDecimal() );
			ps.setInt( 2, iCodVenda );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.executeUpdate();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao alterar a venda!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btExec ) {
			alterar();
		}
	}

	public BigDecimal getValore() {

		return txtPercComis.getVlrBigDecimal();
	}
}
