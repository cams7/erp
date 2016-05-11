/**
 * @version 12/08/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FTarefa.java <BR>
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

package org.freedom.modulos.cfg.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.dialog.report.DLRTarefa;
import org.freedom.modulos.cfg.view.frame.crud.detail.FObjetoTb;

public class FTarefa extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTarefa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTarefa = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtIDObj = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldFK txtDescObj = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private Vector<String> vValsTipo = new Vector<String>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private JComboBoxPad cbTipo = new JComboBoxPad( vLabsTipo, vValsTipo, JComboBoxPad.TP_STRING, 2, 0 );

	private ListaCampos lcObjeto = new ListaCampos( this, "OB" );

	public FTarefa() {

		super();
		setTitulo( "Cadastro de tarefas" );
		setAtribos( 50, 50, 330, 205 );

		// Montando tipos:

		vValsTipo.add( "" );
		vValsTipo.add( "01" );
		vValsTipo.add( "02" );
		vValsTipo.add( "03" );
		vValsTipo.add( "04" );
		vValsTipo.add( "05" );
		vLabsTipo.add( "<--Selecione-->" );
		vLabsTipo.add( "Consulta" );
		vLabsTipo.add( "Insere" );
		vLabsTipo.add( "Atualiza" );
		vLabsTipo.add( "Exclui" );
		vLabsTipo.add( "Executa" );
		cbTipo.setItensGeneric( vLabsTipo, vValsTipo );

		lcObjeto.setUsaFI( false );
		lcObjeto.add( new GuardaCampo( txtIDObj, "IDObj", "Id.obj.", ListaCampos.DB_PK, true ) );
		lcObjeto.add( new GuardaCampo( txtDescObj, "DescObj", "Descrição", ListaCampos.DB_SI, false ) );
		lcObjeto.montaSql( false, "OBJETO", "SG" );
		lcObjeto.setQueryCommit( false );
		lcObjeto.setReadOnly( true );
		txtIDObj.setTabelaExterna( lcObjeto, FObjetoTb.class.getCanonicalName() );

		adicCampo( txtCodTarefa, 7, 20, 50, 20, "CodTarefa", "Cód.tar.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTarefa, 60, 20, 250, 20, "DescTarefa", "Descrição da tarefa", ListaCampos.DB_FK, true );
		adicCampo( txtIDObj, 7, 65, 50, 20, "IDObj", "Id.obj.", ListaCampos.DB_PK, true );
		adicDescFK( txtDescObj, 60, 65, 250, 20, "DescObj", "Descrição do objeto" );
		adicDB( cbTipo, 7, 105, 230, 25, "TipoTarefa", "Tipo", true );
		setListaCampos( true, "TAREFA", "SG" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		setImprimir( true );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcObjeto.setConexao( cn );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de tarefas" );
		DLRTarefa dl = new DLRTarefa();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String sSQL = "SELECT CODTAREFA,DESCTAREFA FROM SGTAREFA ORDER BY " + dl.getValor();
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
					imp.say( imp.pRow() + 0, 2, "Código" );
					imp.say( imp.pRow() + 0, 25, "Descrição" );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 80 ) );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodTarefa" ) );
				imp.say( imp.pRow() + 0, 25, rs.getString( "DescTarefa" ) );
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
			Funcoes.mensagemErro( this, "Erro consulta tabela de tarefas!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}
}
