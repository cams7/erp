/**
 * @version 11/08/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe: @(#)FProcesso.java <BR>
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

package org.freedom.modulos.cfg.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.dialog.report.DLRProcesso;
import org.freedom.modulos.cfg.view.frame.crud.plain.FTarefa;

public class FProcesso extends FDetalhe implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodProc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescProc = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTar = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTar = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodItem = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private ListaCampos lcTarefa = new ListaCampos( this, "TA" );

	private JButtonPad btTrat = new JButtonPad( Icone.novo( "btRetorno.png" ) );

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	public FProcesso() {

		btTrat.setToolTipText( "Tratamento de Retorno" );

		setTitulo( "Cadastro Processos" );
		setAtribos( 50, 50, 450, 350 );

		setAltCab( 90 );
		pinCab = new JPanelPad( 420, 90 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		lcTarefa.add( new GuardaCampo( txtCodTar, "CodTarefa", "Cód.tarfa", ListaCampos.DB_PK, true ) );
		lcTarefa.add( new GuardaCampo( txtDescTar, "DescTarefa", "Descrição da tarefa", ListaCampos.DB_SI, false ) );
		lcTarefa.montaSql( false, "TAREFA", "SG" );
		lcTarefa.setQueryCommit( false );
		lcTarefa.setReadOnly( true );
		txtCodTar.setTabelaExterna( lcTarefa, FTarefa.class.getCanonicalName() );

		adicCampo( txtCodProc, 7, 20, 70, 20, "CodProc", "Cód.proc.", ListaCampos.DB_PK, true );
		adicCampo( txtDescProc, 80, 20, 230, 20, "DescProc", "Descrição do precesso", ListaCampos.DB_SI, true );
		setListaCampos( true, "PROCESSO", "SG" );

		setAltDet( 60 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodItem, 7, 20, 40, 20, "SeqItProc", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtCodTar, 50, 20, 77, 20, "CodTarefa", "Cód.tarefa", ListaCampos.DB_FK, txtDescTar, true );
		adicDescFK( txtDescTar, 130, 20, 200, 20, "DescTar", "Descrição da tarefa" );
		setListaCampos( true, "ITPROCESSO", "SG" );

		adic( btTrat, 340, 15, 60, 30 );

		montaTab();
		tab.setTamColuna( 40, 0 );
		tab.setTamColuna( 80, 1 );
		tab.setTamColuna( 200, 2 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		btTrat.addActionListener( this );
		setImprimir( true );
	}

	private void abreTrat() {

		if ( fPrim.temTela( "Orcamento" ) == false ) {
			FTratRet tela = new FTratRet( txtCodProc.getVlrInteger().intValue(), txtCodItem.getVlrInteger().intValue() );
			fPrim.criatela( "Orcamento", tela, con );
			tela.setConexao( con );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		else if ( evt.getSource() == btTrat )
			abreTrat();
		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		lcTarefa.setConexao( cn );
		super.setConexao( cn );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de Processos" );
		DLRProcesso dl = new DLRProcesso();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String sSQL = "SELECT CODPROC,DESCPROC FROM ATPROCESSO ORDER BY " + dl.getValor();
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
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodProc" ) );
				imp.say( imp.pRow() + 0, 25, rs.getString( "DescProc" ) );
				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();

			// rs.close();
			// ps.close();
			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro na consulta na tabela de Processos!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}
}
