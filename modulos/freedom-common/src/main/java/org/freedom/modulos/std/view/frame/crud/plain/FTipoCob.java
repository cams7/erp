/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FTipoCob.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.frame.crud.plain.FTalaoCheq;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;
import org.freedom.modulos.std.view.dialog.report.DLRTipoCob;

public class FTipoCob extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodTipoCob = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtDescTipoCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldPad txtDuplCob = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private final JTextFieldPad txtDiasProv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTextFieldPad txtNumconta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescconta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSeqtalao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );

	private ListaCampos lcConta = new ListaCampos( this, "CT" );

	private ListaCampos lcTalaoCheq = new ListaCampos( this, "" );
	
	private JRadioGroup<?, ?> rgTipoFebraban = null;
	
	private JRadioGroup<?, ?> rgTipoSPED = null;

	private final JCheckBoxPad cbObriCartCob = new JCheckBoxPad( "Obrigar carteira de cobraça?", "S", "N" );

	public FTipoCob() {

		super();
		setTitulo( "Cadastro de Tipo de Cobrança" );
		setAtribos( 50, 50, 480, 360 );
		
		lcConta.add( new GuardaCampo( txtNumconta, "Numconta", "Número conta", ListaCampos.DB_PK, txtDescconta, false ) );
		lcConta.add( new GuardaCampo( txtDescconta, "Descconta", "Descriçao do conta", ListaCampos.DB_SI, null, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setQueryCommit( false );
		lcConta.setReadOnly( true );
		txtNumconta.setTabelaExterna( lcConta, FConta.class.getCanonicalName() );

		lcTalaoCheq.add( new GuardaCampo( txtNumconta, "Numconta", "Número conta", ListaCampos.DB_PF, txtDescconta, false ) );
		lcTalaoCheq.add( new GuardaCampo( txtSeqtalao, "Seqtalao", "Seq.", ListaCampos.DB_PK, txtDescconta, false ) );
		lcTalaoCheq.montaSql( false, "TALAOCHEQ", "FN" );
		lcTalaoCheq.setQueryCommit( false );
		lcTalaoCheq.setReadOnly( true );
		txtSeqtalao.setTabelaExterna( lcTalaoCheq, FTalaoCheq.class.getCanonicalName() );
		
		montaRadioGrupos();

		montaTela();

		setListaCampos( true, "TIPOCOB", "FN" );
		lcCampos.setQueryInsert( false );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );

		rgTipoFebraban.setVlrString( "00" );
	}

	private void montaRadioGrupos() {

		/*********************
		 * TIPO FEBRABAN *
		 *********************/

		Vector<String> vLabsTipoFebraban = new Vector<String>();
		Vector<String> vValsTipoFebraban = new Vector<String>();

		vLabsTipoFebraban.addElement( "Carteira" );
		vLabsTipoFebraban.addElement( "Cheque" );
		vLabsTipoFebraban.addElement( "SIACC" );
		vLabsTipoFebraban.addElement( "CNAB" );
		vValsTipoFebraban.addElement( "00" );
		vValsTipoFebraban.addElement( "CQ" );
		vValsTipoFebraban.addElement( "01" );
		vValsTipoFebraban.addElement( "02" );

		rgTipoFebraban = new JRadioGroup<String, String>( 1, 4, vLabsTipoFebraban, vValsTipoFebraban );
		
		
		/*********************
		 * TIPO SPED		 *
		 *********************/

		Vector<String> vLabsTipoSPED = new Vector<String>();
		Vector<String> vValsTipoSPED = new Vector<String>();

		vLabsTipoSPED.addElement( "Duplicata" );
		vLabsTipoSPED.addElement( "Cheque" );
		vLabsTipoSPED.addElement( "Promissória" );
		vLabsTipoSPED.addElement( "Recibo" );
		vValsTipoSPED.addElement( "00" );
		vValsTipoSPED.addElement( "01" );
		vValsTipoSPED.addElement( "02" );
		vValsTipoSPED.addElement( "03" );

		rgTipoSPED = new JRadioGroup<String, String>( 1, 4, vLabsTipoSPED, vValsTipoSPED );
		
	}

	private void montaTela() {

		adicCampo( txtCodTipoCob, 7, 20, 75, 20, "CodTipoCob", "Cód.tp.cob.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoCob, 85, 20, 190, 20, "DescTipoCob", "Descrição do tipo de cobrança", ListaCampos.DB_SI, true );
		adicCampo( txtDuplCob, 278, 20, 70, 20, "DuplCob", "Duplicata", ListaCampos.DB_SI, false );
		adicCampo( txtDiasProv, 351, 20, 75, 20, "NroDiasComp", "Dias Comp.", ListaCampos.DB_SI, false );
		adicDB( rgTipoFebraban, 7, 70, 416, 30, "TipoFebraban", "Tipo de cob. FEBRABAN ou forma de pagto.", false );
		adicDB( rgTipoSPED, 7, 125, 416, 30, "TipoSPED", "Tipo de cob. SPED", false );
		
		adicDB( cbObriCartCob, 7, 180, 416, 20, "ObrigCartCob", "", true );
		adic( new JLabelPad( "Informações para impressão de cheques" ), 7, 165, 420, 20 );
		
		JLabelPad borda = new JLabelPad();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		adic( borda, 7, 215, 420, 4 );
		adicCampo( txtNumconta, 7, 245, 80, 20, "Numconta", "Número conta", ListaCampos.DB_FK, false );
		adicDescFK( txtDescconta, 90, 245, 230, 20, "Descconta", "Descrição da conta" );
		adicCampo( txtSeqtalao, 330, 245, 90, 20, "Seqtalao", "Seq. talonário", ListaCampos.DB_SI, false);
		
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLRTipoCob dl = null;
		ImprimeOS imp = null;
		int linPag = 0;

		dl = new DLRTipoCob();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Tipos de Cobrança" );
			imp.limpaPags();

			sSQL = "SELECT CODTIPOCOB,DESCTIPOCOB,DUPLCOB " + "FROM FNTIPOCOB " + "WHERE CODEMP=? AND CODFILIAL=? " + "ORDER BY " + dl.getValor();

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNTIPOCOB" ) );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Código" );
					imp.say( imp.pRow(), 20, "Descrição" );
					imp.say( imp.pRow(), 60, "Duplicata" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodTipoCob" ) );
				imp.say( imp.pRow(), 20, rs.getString( "DescTipoCob" ) );
				imp.say( imp.pRow(), 60, rs.getString( "DuplCob" ) != null ? rs.getString( "DuplCob" ) : "" );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.normal() );
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
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de cobrança!" + err.getMessage(), true, con, err );
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
	
	public void setConexao(DbConnection cn) {
		super.setConexao( cn );
		lcConta.setConexao( cn );
		lcTalaoCheq.setConexao( cn );
	}
	
}
