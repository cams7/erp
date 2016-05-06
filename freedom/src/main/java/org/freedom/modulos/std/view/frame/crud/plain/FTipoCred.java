/**
 * @version 21/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FTipoCred.java <BR>
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
 *         Tipos para liberação de crédito...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.report.DLRTipoCred;

public class FTipoCred extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTipoCred = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTipoCred = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtVlrTipoCred = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 3 );

	public FTipoCred() {

		super();
		setTitulo( "Cadastro de tipos de credito" );
		setAtribos( 50, 50, 350, 165 );
		adicCampo( txtCodTipoCred, 7, 20, 80, 20, "CodTpCred", "Cód.tp.cred.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoCred, 90, 20, 240, 20, "DescTpCred", "Descrição do tipo de credito", ListaCampos.DB_SI, true );
		adicCampo( txtVlrTipoCred, 7, 60, 120, 20, "VlrTpCred", "Valor", ListaCampos.DB_SI, true );
		setListaCampos( true, "TIPOCRED", "FN" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		setImprimir( true );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLRTipoCred dl = null;
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = 0;
		int iTot = 0;

		dl = new DLRTipoCred();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Tipos de Crédito" );
			imp.limpaPags();

			sSQL = "SELECT TP.CODTPCRED,TP.DESCTPCRED," + "(SELECT COUNT(CLI.CODCLI) FROM VDCLIENTE CLI " + "WHERE CLI.CODEMP=TP.CODEMP AND CLI.CODFILAL=TP.CODFILIAL " + "AND CLI.CODTPCRED = TP.CODTPCRED) " + "FROM FNTIPOCRED TP " + "WHERE TP.CODEMP=? AND TP.CODFILIAL=? " + "ORDER BY TP."
					+ dl.getValor();
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNTIPOCRED" ) );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Cód.tp.cred." );
					imp.say( imp.pRow(), 20, "Descrição" );
					imp.say( imp.pRow(), 70, "Qtd.cli." );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodTpCred" ) );
				imp.say( imp.pRow(), 20, rs.getString( "DescTpCred" ) );
				imp.say( imp.pRow(), 70, Funcoes.alinhaDir( rs.getInt( 3 ), 8 ) );
				iTot += rs.getInt( 3 );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 50, "Total de clientes:" );
			imp.say( imp.pRow(), 71, Funcoes.alinhaDir( iTot, 8 ) );
			imp.say( imp.pRow(), 80, "|" );
			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();
			imp.fechaGravacao();

			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro na consulta tabela de tipos de tipos de créditos!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			dl = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}
}
