/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FTipoFor.java <BR>
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

import javax.swing.BorderFactory;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.report.DLRTipoFor;

public class FTipoFor extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTipoFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTipoFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );
	
	private final JCheckBoxPad cbRetencaoIR = new JCheckBoxPad( "Retém IR", "S", "N" );
	
	private final JCheckBoxPad cbRetencaoINSS = new JCheckBoxPad( "Retém INSS", "S", "N" );
	
	private final JCheckBoxPad cbRetencaoOutros = new JCheckBoxPad( "Retém Outros", "S", "N" );
	
	private JTextFieldPad txtPercBaseINSS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 3, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtPercBaseIRRF = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 3, Aplicativo.casasDecFin );
	
	private JTextFieldPad txtPercRetOutros = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 3, Aplicativo.casasDecFin );
	
	private JLabelPad sepdet = new JLabelPad();
	
	private JLabelPad sepdet2 = new JLabelPad();

	public FTipoFor() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de Tipo de Fornecedor" );
		
		setAtribos( 50, 50, 405, 200 );
		
		sepdet.setBorder( BorderFactory.createEtchedBorder() );		
		sepdet2.setBorder( BorderFactory.createEtchedBorder() );
		
		adicCampo( txtCodTipoFor, 7, 20, 100, 20, "CodTipoFor", "Cód.Tipo fornec.", ListaCampos.DB_PK, true );		
		adicCampo( txtDescTipoFor, 110, 20, 270, 20, "DescTipoFor", "Descrição do tipo de fornecedor", ListaCampos.DB_SI, true );
		
		adicDB( cbRetencaoINSS, 5, 55,  100, 20, "retencaoinss", "", false );				
		adicCampo( txtPercBaseINSS, 7, 100, 100, 20, "PercBaseINSS", "% Base do INSS", ListaCampos.DB_SI, false );
		

		adic( sepdet, 113, 52, 2, 75 );
		
		adicDB( cbRetencaoOutros, 116, 55, 105, 20, "retencaooutros", "", false );
		adicCampo( txtPercRetOutros, 119, 100, 100, 20, "Percretoutros", "% Ret. Outros", ListaCampos.DB_SI, false );

		adic( sepdet2, 225, 52, 2, 75 );
		
		adicDB( cbRetencaoIR, 228, 55, 100, 20, "retencaoirrf", "", false );
		adicCampo( txtPercBaseIRRF, 231, 100, 100, 20, "PercBaseIRRF", "% Base do IRRF", ListaCampos.DB_SI, false );

		setListaCampos( true, "TIPOFOR", "CP" );
		
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
		DLRTipoFor dl = null;
		ImprimeOS imp = null;
		int linPag = 0;

		dl = new DLRTipoFor();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Tipos de Fornecedor" );
			imp.limpaPags();

			sSQL = "SELECT CODTIPOFOR,DESCTIPOFOR " + "FROM CPTIPOFOR " + "WHERE CODEMP=? AND CODFILIAL=? " + "ORDER BY " + dl.getValor();

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPTIPOFOR" ) );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Cód.tp.for." );
					imp.say( imp.pRow(), 30, "Descrição" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodTipoFor" ) );
				imp.say( imp.pRow(), 30, rs.getString( "DescTipoFor" ) );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();
			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de fornecedor!" + err.getMessage(), true, con, err );
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
