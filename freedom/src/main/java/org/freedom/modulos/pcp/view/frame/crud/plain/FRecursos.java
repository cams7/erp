/**
 * @version 25/03/2004 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pcp <BR>
 *         Classe: @(#)FBanco.java <BR>
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
 *         Tela de cadastro de tipos de recursos de produção.
 */

package org.freedom.modulos.pcp.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.pcp.view.dialog.report.DLRRecursos;

public class FRecursos extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodRecp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescRecp = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTpRecp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTpRecp = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcTpRecp = new ListaCampos( this, "TR" );

	public FRecursos() {

		super();
		setTitulo( "Cadastro de recursos de producão." );
		setAtribos( 50, 180, 355, 165 );

		lcTpRecp.add( new GuardaCampo( txtCodTpRecp, "Codtprec", "Cód.rec.", ListaCampos.DB_PK, txtDescTpRecp, true ) );
		lcTpRecp.add( new GuardaCampo( txtDescTpRecp, "Desctprec", "Descriçao do recurso", ListaCampos.DB_SI, false ) );
		lcTpRecp.montaSql( false, "TIPOREC", "PP" );
		lcTpRecp.setQueryCommit( false );
		lcTpRecp.setReadOnly( true );
		txtCodTpRecp.setTabelaExterna( lcTpRecp, FTipoRec.class.getCanonicalName() );

		adicCampo( txtCodRecp, 7, 20, 70, 20, "Codrecp", "Cód.rec.", ListaCampos.DB_PK, true );
		adicCampo( txtDescRecp, 80, 20, 240, 20, "descrecp", "Descrição do recurso", ListaCampos.DB_SI, true );
		adicCampo( txtCodTpRecp, 7, 60, 70, 20, "Codtprec", "Cód.tp.rec.", ListaCampos.DB_FK, txtDescTpRecp, false );
		adicDescFK( txtDescTpRecp, 80, 60, 240, 20, "desctprec", "Descrição do tipo de recurso" );
		setListaCampos( true, "RECURSO", "PP" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		// lcCampos.setQueryInsert(false);
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

	private void imprimir( TYPE_PRINT bVisualizar ) {

		DLRRecursos dl = new DLRRecursos( this );
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		String sSQL = "SELECT CODRECP,DESCRECP FROM PPRECURSO ORDER BY " + dl.getValores().elementAt( 0 ).toString();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de recursos de produção!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

		if ( dl.getValores().elementAt( 1 ).equals( "T" ) ) {
			ImprimeOS imp = new ImprimeOS( "", con );
			int linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Recursos de produção" );

			imp.limpaPags();
			try {
				while ( rs.next() ) {
					if ( imp.pRow() == 0 ) {
						imp.impCab( 80, false );
						imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
						imp.say( imp.pRow() + 0, 0, "" );
						imp.say( imp.pRow() + 0, 2, "Código" );
						imp.say( imp.pRow() + 0, 30, "Descrição" );
						imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 79 ) );
					}
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 2, rs.getString( "Codrecp" ) );
					imp.say( imp.pRow() + 0, 30, rs.getString( "descrecp" ) );
					if ( imp.pRow() >= linPag ) {
						imp.incPags();
						imp.eject();
					}
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 79 ) );
				imp.eject();

				imp.fechaGravacao();
				con.commit();
				dl.dispose();
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de recursos de produção!" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}
		}
		else {
			FPrinterJob dlGr = null;
			dlGr = new FPrinterJob( "relatorios/recursos.jasper", "RELATORIO DE RECURSOS DE PRODUÇÃO", "Código\nDescrição", rs, null, this );

			if ( bVisualizar==TYPE_PRINT.VIEW )
				dlGr.setVisible( true );
			else {
				try {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro na impressão de recursos de produção!" + err.getMessage(), true, con, err );
				}
			}
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTpRecp.setConexao( cn );
	}
}
