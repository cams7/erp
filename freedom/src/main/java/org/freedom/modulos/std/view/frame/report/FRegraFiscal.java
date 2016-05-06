/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRegraFiscal.java <BR>
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

package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;

import java.util.Vector;

public class FRegraFiscal extends FDetalhe {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodRegraFiscal = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtDescRegraFiscal = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodNat = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldFK txtDescNat = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbNoUF = new JCheckBoxPad( "Sim", "S", "N" );

	private Vector<String> vDescCV = new Vector<String>();

	private Vector<String> vValCV = new Vector<String>();

	private ListaCampos lcNat = new ListaCampos( this );

	private ListaCampos lcMov = new ListaCampos( this, "TM" );

	public FRegraFiscal() {

		setTitulo( "Regras CFOP" );
		setAtribos( 50, 50, 600, 450 );
		pinCab = new JPanelPad( 440, 70 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		adicCampo( txtCodRegraFiscal, 7, 20, 80, 20, "CodRegra", "Cód.reg.CFOP", ListaCampos.DB_PK, true );
		adicCampo( txtDescRegraFiscal, 90, 20, 220, 20, "DescRegra", "Descrição da regra fiscal", ListaCampos.DB_SI, true );
		setListaCampos( true, "REGRAFISCAL", "LF" );
		lcCampos.setQueryInsert( false );

		lcNat.setUsaME( false );
		lcNat.add( new GuardaCampo( txtCodNat, "CodNat", "CFOP", ListaCampos.DB_PK, false ) );
		lcNat.add( new GuardaCampo( txtDescNat, "DescNat", "Descrição da CFOP", ListaCampos.DB_SI, false ) );
		lcNat.montaSql( false, "NATOPER", "LF" );
		lcNat.setQueryCommit( false );
		lcNat.setReadOnly( true );
		txtCodNat.setTabelaExterna( lcNat, null );

		lcMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcMov.montaSql( false, "TIPOMOV", "EQ" );
		lcMov.setQueryCommit( false );
		lcMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcMov, null );

		vDescCV.addElement( "Venda" );
		vDescCV.addElement( "Compra" );
		vValCV.addElement( "V" );
		vValCV.addElement( "C" );

		JRadioGroup<?, ?> rgCV = new JRadioGroup<String, String>( 1, 2, vDescCV, vValCV );
		rgCV.setVlrString( "V" );

		setAltDet( 120 );
		pinDet = new JPanelPad( 600, 120 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		adicCampo( txtCodNat, 7, 25, 70, 20, "CodNat", "CFOP", ListaCampos.DB_PF, true );
		adicDescFK( txtDescNat, 80, 25, 197, 20, "DescNat", "Descrição da CFOP" );
		adicCampo( txtCodTipoMov, 7, 75, 70, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescTipoMov, 80, 75, 197, 20, "DescTipoMov", "Descrição do tipo de movimento" );
		adicDB( cbNoUF, 280, 25, 87, 20, "NoUFItRF", "No Estado", true );
		adicDB( rgCV, 370, 20, 200, 30, "CVItRf", "Compra/venda", true );

		setListaCampos( false, "ITREGRAFISCAL", "LF" );
		txtCodNat.setStrMascara( "#.###" );
		lcDet.setQueryInsert( false );
		montaTab();
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );
		lcNat.setConexao( con );
		lcMov.setConexao( con );
	}
}
