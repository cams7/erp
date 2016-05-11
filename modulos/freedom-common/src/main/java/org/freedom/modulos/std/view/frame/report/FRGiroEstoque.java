/**
 * @version 19/11/2009 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRGiroEstoque.java <BR>
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
 *         Classe para filtros e carregamento de relatório de giro de estoque.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRGiroEstoque extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JLabelPad lbCodGrup = new JLabelPad( "Cód.grupo" );

	private JLabelPad lbDescGrup = new JLabelPad( "Descrição do grupo" );

	private JCheckBoxPad cbSemEstoq = new JCheckBoxPad( "Imprimir produtos sem estoque?", "S", "N" );

	private JRadioGroup<?, ?> rgOrdem = null;

	private Vector<String> vLabs = new Vector<String>( 2 );

	private Vector<String> vVals = new Vector<String>( 2 );

	private ListaCampos lcGrup = new ListaCampos( this );

	public FRGiroEstoque() {

		setTitulo( "Relatório de Giro de estoque" );

		setAtribos( 140, 40, 340, 290 );

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vLabs.addElement( "+ Vendido" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		vVals.addElement( "M" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );

		JPanelPad pnLinha = new JPanelPad( "Posição do dia:", Color.BLUE );

		adic( pnLinha, 7, 0, 300, 65 );
		pnLinha.adic( txtDataini, 7, 7, 75, 20 );

		txtDataini.setVlrDate( new Date() );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		adic( lbCodGrup, 7, 75, 250, 20 );
		adic( txtCodGrup, 7, 95, 80, 20 );
		adic( lbDescGrup, 90, 75, 250, 20 );
		adic( txtDescGrup, 90, 95, 216, 20 );
		adic( rgOrdem, 7, 130, 300, 30 );

		cbSemEstoq.setVlrString( "S" );

		adic( cbSemEstoq, 7, 165, 300, 30 );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sql = new StringBuilder();
		StringBuilder status = new StringBuilder();
		StringBuilder filtros = new StringBuilder();

		try {

			sql.append( "select codprod,refprod,codfabprod,codbarprod,descprod," );
			sql.append( "dtultcp,doccompra,identcontainer,qtdultcp,qtdvendida,saldoatu,saldoant " );
			sql.append( "from eqrelgiroprod(?,?,?) " );

			if ( "N".equals( cbSemEstoq.getVlrString() ) ) {
				sql.append( " where saldoatu>0 " );
			}

			if ( txtCodGrup.getVlrInteger() > 0 ) {
				sql.append( " where codempgp=? and codfilialgp=? and codgrup=? " );
			}

			if ( "C".equals( rgOrdem.getVlrString() ) ) {
				sql.append( "order by codprod,descprod,qtdvendida desc " );
			}
			if ( "D".equals( rgOrdem.getVlrString() ) ) {
				sql.append( "order by descprod, qtdvendida desc " );
			}
			if ( "M".equals( rgOrdem.getVlrString() ) ) {
				sql.append( "order by qtdvendida desc " );
			}

			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, Aplicativo.iCodFilial );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );

			if ( txtCodGrup.getVlrInteger() > 0 ) {
				ps.setInt( iparam++, lcGrup.getCodEmp() );
				ps.setInt( iparam++, lcGrup.getCodFilial() );
				ps.setString( iparam++, txtCodGrup.getVlrString() );
			}

			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();
			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "CODFILIAL", Aplicativo.iCodFilial );
			hParam.put( "DATA", txtDataini.getVlrDate() );
			hParam.put( "SUBREPORT_DIR", "org/freedom/layout/rel" );

			FPrinterJob dlGr = new FPrinterJob( "layout/rel/REL_GIRO_ESTOQUE.jasper", "Relatório de Giro de estoque", "", rs, hParam, this );

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro consultar giro do estoque!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void setConexao( DbConnection cn ) {

		lcGrup.setConexao( cn );
		super.setConexao( cn );

	}

}
