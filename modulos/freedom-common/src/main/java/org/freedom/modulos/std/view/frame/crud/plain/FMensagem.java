/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FMensagem.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.report.DLRMensagem;

public class FMensagem extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCod = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextAreaPad txaMens = new JTextAreaPad( 10000 );

	private JScrollPane spnMen = new JScrollPane( txaMens );
	
	private JComboBoxPad cbCamposEspec = null;
	
	private final JButtonPad btAdic = new JButtonPad( Icone.novo( "btAdic2.gif" ) );

	public FMensagem() {

		super();
		
		setTitulo( "Cadastro de mensagens" );
		setAtribos( 50, 50, 450, 300 );
		
		Vector<String> vLabs2 = new Vector<String>();

		vLabs2.addElement( "<--Selecione-->" );
		vLabs2.addElement( "Base do ICMS" );
		
		vLabs2.addElement( "Valor ICMS" );
		vLabs2.addElement( "Valor ICMS Diferido" );
		vLabs2.addElement( "Valor ICMS Devido" );
		vLabs2.addElement( "Valor ICMS Créd. Presumido" );
		
		vLabs2.addElement( "Valor ICMS Simples" );
		vLabs2.addElement( "Aliq. ICMS Simples" );
		
		vLabs2.addElement( "Valor PIS" );
		vLabs2.addElement( "Valor COFINS" );
		vLabs2.addElement( "Taxa Siscomex" );
		vLabs2.addElement( "Número DI" );
		
		
		Vector<String> vVals2 = new Vector<String>();
		
		vVals2.addElement( "" );
		vVals2.addElement( "#BICMS#" );
		
		vVals2.addElement( "#VLRICMS#" );
		vVals2.addElement( "#VLRICMSDIF#" );
		vVals2.addElement( "#VLRICMSDEV#" );
		vVals2.addElement( "#VLRICMSCPRES#" );
		
		vVals2.addElement( "#VLRICMSSIMPLES#" );
		vVals2.addElement( "#ALIQICMSSIMPLES#" );
		
		vVals2.addElement( "#VALORPIS#" );
		vVals2.addElement( "#VALORCOFINS#" );
		vVals2.addElement( "#TAXASISCOMEX#" );
		vVals2.addElement( "#NUMERODI#" );

		
		cbCamposEspec = new JComboBoxPad( vLabs2, vVals2, JComboBoxPad.TP_STRING, 20, 0 );
		
		adicCampo( txtCod, 7, 20, 80, 20, "CodMens", "Cód.mens.", ListaCampos.DB_PK, true );
		
		adic( cbCamposEspec, 90, 20, 305, 20, "Campos especiais" );
		adic( btAdic, 398, 20, 22, 20 );
		
		
		adicDBLiv( txaMens, "Mens", "Mensagem", true );
		adic( spnMen, 7, 50, 414, 175 );
		setListaCampos( true, "MENSAGEM", "LF" );
		
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btAdic.addActionListener( this );
		lcCampos.setQueryInsert( false );
		
		nav.setNavigation( true );
				
		setImprimir( true );
		
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( evt.getSource() == btAdic ) {
			
			txaMens.insert( cbCamposEspec.getVlrString(), txaMens.getCaretPosition() );
			
		}
			
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de mensagens" );
		DLRMensagem dl = new DLRMensagem();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String sSQL = "SELECT CODMENS,MENS " + "FROM LFMENSAGEM ORDER BY " + dl.getValor();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();
			imp.limpaPags();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 0, 2, "Cód.mens." );
					imp.say( imp.pRow() + 0, 20, "Mensagem" );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 80 ) );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodMens" ) );
				imp.say( imp.pRow() + 0, 20, rs.getString( "Mens" ) );
				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 80 ) );
			imp.eject();

			imp.fechaGravacao();

			// rs.close();
			// ps.close();
			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela mensagens!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}
}
