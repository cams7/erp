/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)DLRLancaExp.java <BR>
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

package org.freedom.modulos.std.view.dialog.report;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

import java.util.Calendar;
import java.util.Vector;

public class DLRLancaExp extends FFDialogo implements FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoExp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoExp = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcVend = new ListaCampos( this );

	private ListaCampos lcTipoExp = new ListaCampos( this );

	private Vector<String> vLabsRet = new Vector<String>();

	private Vector<String> vValsRet = new Vector<String>();

	private JRadioGroup<?, ?> rgRet;

	public DLRLancaExp() {

		setTitulo( "Lançamentos de Expositores" );
		setAtribos( 80, 80, 310, 270 );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtDescCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVend, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

		lcTipoExp.add( new GuardaCampo( txtCodTipoExp, "CodTipoExp", "Cód.tp.exp", ListaCampos.DB_PK, false ) );
		lcTipoExp.add( new GuardaCampo( txtDescTipoExp, "DescTipoExp", "Descrição do tipo de expositor", ListaCampos.DB_SI, false ) );
		lcTipoExp.montaSql( false, "TIPOEXP", "EQ" );
		lcTipoExp.setReadOnly( true );
		txtCodTipoExp.setTabelaExterna( lcTipoExp, null );
		txtCodTipoExp.setFK( true );
		txtCodTipoExp.setNomeCampo( "CodTipoExp" );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		vValsRet.addElement( "E" );
		vValsRet.addElement( "R" );
		vValsRet.addElement( "T" );
		vLabsRet.addElement( "Enviados" );
		vLabsRet.addElement( "Retornados" );
		vLabsRet.addElement( "Todos" );
		rgRet = new JRadioGroup<String, String>( 1, 3, vLabsRet, vValsRet );
		rgRet.setVlrString( "T" );

		adic( new JLabelPad( "Periodo:" ), 7, 5, 120, 20 );
		adic( new JLabelPad( "De:" ), 7, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 140, 25, 17, 20 );
		adic( txtDatafim, 160, 25, 100, 20 );
		adic( new JLabelPad( "Cód.cli." ), 7, 45, 280, 20 );
		adic( txtCodCli, 7, 65, 80, 20 );
		adic( new JLabelPad( "Descrição do cliente" ), 90, 45, 280, 20 );
		adic( txtDescCli, 90, 65, 200, 20 );
		adic( new JLabelPad( "Cód.comiss." ), 7, 85, 280, 20 );
		adic( txtCodVend, 7, 105, 80, 20 );
		adic( new JLabelPad( "Nome do comissionado" ), 90, 85, 280, 20 );
		adic( txtDescVend, 90, 105, 200, 20 );
		adic( new JLabelPad( "Cód.tp.exp." ), 7, 125, 280, 20 );
		adic( txtCodTipoExp, 7, 145, 80, 20 );
		adic( new JLabelPad( "Descrição do tipo de expositor" ), 90, 125, 280, 20 );
		adic( txtDescTipoExp, 90, 145, 200, 20 );
		adic( rgRet, 7, 170, 290, 30 );
		txtCodCli.addFocusListener( this );
	}

	public String[] getValores() {

		String sRet[] = new String[ 6 ];
		sRet[ 0 ] = txtDataini.getVlrString();
		sRet[ 1 ] = txtDatafim.getVlrString();
		sRet[ 2 ] = txtCodCli.getVlrString();
		sRet[ 3 ] = txtCodVend.getVlrString();
		sRet[ 4 ] = txtCodTipoExp.getVlrString();
		sRet[ 5 ] = rgRet.getVlrString();
		return sRet;
	}

	public void focusLost( FocusEvent fevt ) {

		if ( ( fevt.getSource() == txtCodCli ) && txtCodCli.getText().trim().length() > 0 ) {
			txtCodVend.setAtivo( false );
		}
	}

	public void focusGained( FocusEvent fevt ) {

	}

	public void setConexao( DbConnection cn ) {

		lcCli.setConexao( cn );
		lcVend.setConexao( cn );
		lcTipoExp.setConexao( cn );
	}
}
