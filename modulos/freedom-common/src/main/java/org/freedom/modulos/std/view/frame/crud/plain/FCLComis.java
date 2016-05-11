/**
 * @version 17/05/2004 <BR>
 * @author Setpoint Informática Ltda./Marco Antonio Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FCLComis.java <BR>
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.PostListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.report.DLRClComis;

public class FCLComis extends FDados implements PostListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodClComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescClComis = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtPercFatClComis = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtPercPgtoComis = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private ListaCampos lcClComis = new ListaCampos( this, "CL" );

	public FCLComis() {

		super();
		setTitulo( "Cadastro de Classificação de Comissões" );
		setAtribos( 50, 50, 400, 205 );

		//setListaCampos( true, "CLCOMIS", "VD" );

		lcClComis.add( new GuardaCampo( txtCodClComis, "CodClComis", "Cód.c.comis.", ListaCampos.DB_PK, null, false ) );
		lcClComis.add( new GuardaCampo( txtDescClComis, "DescClComis", "Descriçao da classificação da comissão", ListaCampos.DB_SI, null, false ) );
		lcClComis.montaSql( false, "VDCLCOMIS", "CL" );
		lcClComis.setQueryCommit( false );
		lcClComis.setReadOnly( true );

		JLabelPad lbDistriCom = new JLabelPad( " Distribuição de Comissões " );
		lbDistriCom.setOpaque( true );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adicCampo( txtCodClComis, 7, 20, 80, 20, "CodClComis", "Cód.c.comis.", ListaCampos.DB_PK, null, true );
		adicCampo( txtDescClComis, 90, 20, 250, 20, "DescClComis", "Descrição da classificação da comissão", ListaCampos.DB_SI, null, true );

		adic( lbDistriCom, 7, 50, 250, 20 );
		adic( lbLinha, 7, 72, 250, 2 );

		adicCampo( txtPercFatClComis, 7, 100, 120, 20, "PercFatClComis", "% S/Faturamento", ListaCampos.DB_SI, null, true );
		adicCampo( txtPercPgtoComis, 140, 100, 120, 20, "PercPgtoClComis", "% S/Recebimento.", ListaCampos.DB_SI, null, true );

		setListaCampos( true, "CLCOMIS", "VD" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
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
		StringBuffer sSQL = new StringBuffer();
		DLRClComis dl = new DLRClComis();

		try {

			dl.setVisible( true );
			if ( dl.OK == false ) {
				dl.dispose();
				return;
			}

			sSQL.append( "SELECT CODCLCOMIS,DESCCLCOMIS,PERCFATCLCOMIS, PERCPGTOCLCOMIS FROM VDCLCOMIS " );
			sSQL.append( "ORDER BY " + dl.getValor() );

			ps = con.prepareStatement( sSQL.toString() );
			rs = ps.executeQuery();

			if ( "T".equals( dl.getTipo() ) ) {
				imprimirTexto( bVisualizar, rs );
			}
			else if ( "G".equals( dl.getTipo() ) ) {
				imprimirGrafico( bVisualizar, rs );
			}

			rs.close();
			ps.close();

			con.commit();
			dl.dispose();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório de classificação de cliente!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		String sLinhaFina = StringFunctions.replicate( "-", 133 );
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();

		try {
			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Classificação de Comissões" );

			while ( rs.next() ) {

				if ( imp.pRow() == linPag ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "+" + sLinhaFina + "+" );
					imp.eject();
					imp.incPags();
				}

				if ( imp.pRow() == 0 ) {
					imp.montaCab();
					imp.setTitulo( "Relatório de Classificação de Comissões" );
					imp.addSubTitulo( "Relatório de Classificação de Comissões" );
					imp.impCab( 136, true );

					imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 0, 0, "|" );
					imp.say( imp.pRow() + 0, 2, "Código" );
					imp.say( imp.pRow() + 0, 12, "|" );
					imp.say( imp.pRow() + 0, 14, "Descrição" );
					imp.say( imp.pRow() + 0, 76, "|" );
					imp.say( imp.pRow() + 0, 80, "Perc.Fat." );
					imp.say( imp.pRow() + 0, 100, "|" );
					imp.say( imp.pRow() + 0, 108, "Perc.Receb." );
					imp.say( imp.pRow() + 0, 135, "|" );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
				}

				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 0, "|" );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodClComis" ) );
				imp.say( imp.pRow() + 0, 12, "|" );
				imp.say( imp.pRow() + 0, 14, rs.getString( "DescClComis" ) );
				imp.say( imp.pRow() + 0, 76, "|" );
				imp.say( imp.pRow() + 0, 80, rs.getString( "percFatClComis" ) );
				imp.say( imp.pRow() + 0, 100, "|" );
				imp.say( imp.pRow() + 0, 108, rs.getString( "PercPgtoClComis" ) );
				imp.say( imp.pRow() + 0, 135, "|" );

				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
			imp.eject();

			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}

			else {
				imp.print();
			}
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela de class.comissão!\n" + err.getMessage(), true, con, err );
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		FPrinterJob dlGr = new FPrinterJob( "relatorios/CLComis.jasper", "Classificação de Comissões", null, rs, null, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Classificação de Clientes!\n" + err.getMessage(), true, con, err );
			}
		}
	}
}
