/**
 * @version 23/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)DLNovoHist.java <BR>
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
 *         Nova chamada de histórico de contatos.
 * 
 */

package org.freedom.modulos.crm.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.agenda.DLNovoAgen;
import org.freedom.modulos.crm.agenda.DLNovoAgen.PARAM_SGSETAAGENDASP;

public class DLNovoHist extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnCab = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JTextFieldPad txtDataCont = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCont = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCont = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextAreaPad txaDescAtend = new JTextAreaPad();

	private ListaCampos lcCont = new ListaCampos( this );

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcAtend = new ListaCampos( this );

	private JScrollPane spnDesc = new JScrollPane( txaDescAtend );

	private JLabelPad lbImg = new JLabelPad( Icone.novo( "bannerTMKhistorico.jpg" ) );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private Vector<String> vLabs1 = new Vector<String>();

	private JComboBoxPad cbSit = null;

	private JComboBoxPad cbTipo = null;

	private Object[] sValsAgen = null;

	public DLNovoHist( int iCod, int index, Component cOrig ) {

		super( cOrig );
		setTitulo( "Nova chamada" );
		setAtribos( 502, 460 );
		setLocationRelativeTo( Aplicativo.telaPrincipal );

		vVals.addElement( "" );
		vVals.addElement( "RJ" );
		vVals.addElement( "AG" );
		vVals.addElement( "EF" );
		vLabs.addElement( "<--Selecione-->" );
		vLabs.addElement( "Rejeitado" );
		vLabs.addElement( "Agendar" );
		vLabs.addElement( "Efetivada" );
		cbSit = new JComboBoxPad( vLabs, vVals, JComboBoxPad.TP_STRING, 3, 0 );

		vVals1.addElement( "" );
		vVals1.addElement( "H" );
		vVals1.addElement( "V" );
		vVals1.addElement( "N" );
		vVals1.addElement( "C" );
		vVals1.addElement( "O" );
		vVals1.addElement( "L" );
		vVals1.addElement( "P" );
		vVals1.addElement( "I" );
		vLabs1.addElement( "<--Selecione-->" );
		vLabs1.addElement( "Historico" );
		vLabs1.addElement( "Visita" );
		vLabs1.addElement( "Visita (cliente não cadastrado)" );
		vLabs1.addElement( "Campanha" );
		vLabs1.addElement( "Cobrança" );
		vLabs1.addElement( "Ligação Pré-Venda" );
		vLabs1.addElement( "Ligação Pós-Venda" );
		vLabs1.addElement( "Indefinida" );

		cbTipo = new JComboBoxPad( vLabs1, vVals1, JComboBoxPad.TP_STRING, 4, 2 );

		lcCont.add( new GuardaCampo( txtCodCont, "CodCto", "Cód.cli", ListaCampos.DB_PK, txtNomeCont, false ) );
		lcCont.add( new GuardaCampo( txtNomeCont, "NomeCto", "Nome do contato", ListaCampos.DB_SI, false ) );
		lcCont.montaSql( false, "CONTATO", "TK" );
		lcCont.setReadOnly( true );
		txtCodCont.setTabelaExterna( lcCont, null );
		txtCodCont.setFK( true );
		txtCodCont.setNomeCampo( "CodCto" );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cont", ListaCampos.DB_PK, txtNomeCli, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		lcAtend.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.Atend", ListaCampos.DB_PK, txtNomeAtend, true ) );
		lcAtend.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ) );
		lcAtend.montaSql( false, "ATENDENTE", "AT" );
		lcAtend.setReadOnly( true );
		txtCodAtend.setTabelaExterna( lcAtend, null );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );

		pnCab.setPreferredSize( new Dimension( 500, 60 ) );
		pnCab.add( lbImg );
		c.add( pnCab, BorderLayout.NORTH );

		if ( index == 0 ) {
			adic( new JLabelPad( "Cód. cont." ), 7, 5, 80, 20 );
			adic( txtCodCont, 7, 25, 80, 20 );
			adic( new JLabelPad( "Nome do contato" ), 90, 5, 197, 20 );
			adic( txtNomeCont, 90, 25, 390, 20 );
			txtCodCont.setVlrInteger( new Integer( iCod ) );
			txtCodCont.setAtivo( false );
		}
		else if ( index == 1 ) {
			adic( new JLabelPad( "Cód. cli." ), 7, 5, 80, 20 );
			adic( txtCodCli, 7, 25, 80, 20 );
			adic( new JLabelPad( "Razão social do cliente" ), 90, 5, 197, 20 );
			adic( txtNomeCli, 90, 25, 390, 20 );
			txtCodCli.setVlrInteger( new Integer( iCod ) );
			txtCodCli.setAtivo( false );
		}

		adic( new JLabelPad( "Situação" ), 7, 45, 150, 20 );
		adic( cbSit, 7, 65, 235, 20 );
		adic( new JLabelPad( "Tipo do contato" ), 245, 45, 150, 20 );
		adic( cbTipo, 245, 65, 235, 20 );
		adic( new JLabelPad( "Cód. atend." ), 7, 85, 80, 20 );
		adic( txtCodAtend, 7, 105, 80, 20 );
		adic( new JLabelPad( "Nome do atendente" ), 90, 85, 197, 20 );
		adic( txtNomeAtend, 90, 105, 287, 20 );
		adic( new JLabelPad( "Data" ), 380, 85, 100, 20 );
		adic( txtDataCont, 380, 105, 100, 20 );

		JLabelPad lbChamada = new JLabelPad( "   Chamada" );
		lbChamada.setOpaque( true );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( lbChamada, 20, 130, 80, 20 );
		adic( lbLinha, 7, 140, 470, 2 );
		adic( spnDesc, 7, 155, 470, 140 );

		txtDataCont.setRequerido( true );
		txtDataCont.setVlrDate( new java.util.Date() );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( txtCodAtend.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Código do atendente inválido!" );
				return;
			}
			else if ( txaDescAtend.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Não foi digitado nenhum procedimento!" );
				return;
			}
			else if ( cbSit.getVlrString().equals( "AG" ) ) {
				DLNovoAgen dl = new DLNovoAgen( this );
				dl.setConexao( con );
				dl.setVisible( true );
				if ( !dl.OK )
					return;
				sValsAgen = dl.getParamSP();
				dl.dispose();
			}
		}
		super.actionPerformed( evt );
	}

	public void buscaAtend() {

		String sSQL = "SELECT CODATEND FROM ATATENDENTE" + " WHERE CODEMPUS=? AND CODFILIALUS=? AND IDUSU=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilialPad );
			ps.setString( 3, Aplicativo.getUsuario().getIdusu() );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() ) {
				txtCodAtend.setVlrInteger( new Integer( rs.getInt( "CodAtend" ) ) );
				lcAtend.carregaDados();
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o atendente atual!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

	}

	public void setValores( Object[] sVal ) {

		txaDescAtend.setVlrString( (String) sVal[ 0 ] );
		txtCodAtend.setVlrString( (String) sVal[ 1 ] );
		cbSit.setVlrString( (String) sVal[ 2 ] );
		cbTipo.setVlrString( (String) sVal[ 3 ] );
		txtDataCont.setVlrDate( (Date) sVal[ 4 ] );
		lcAtend.carregaDados();
	}

	public Object[] getValores() {

		Object[] oVal = new Object[ 26 ];
		oVal[ 0 ] = txaDescAtend.getVlrString();
		oVal[ 1 ] = txtCodAtend.getVlrString();
		oVal[ 2 ] = cbSit.getVlrString();
		oVal[ 3 ] = cbTipo.getVlrString();
		oVal[ 4 ] = Funcoes.dateToSQLDate( txtDataCont.getVlrDate() );
		if ( sValsAgen != null ) {
			oVal[ 5 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODAGD.ordinal() ];
			oVal[ 6 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODEMP.ordinal() ];
			oVal[ 7 ] = sValsAgen[ PARAM_SGSETAAGENDASP.DTAINIAGD.ordinal() ];
			oVal[ 8 ] = sValsAgen[ PARAM_SGSETAAGENDASP.HRINIAGD.ordinal() ];
			oVal[ 9 ] = sValsAgen[ PARAM_SGSETAAGENDASP.DTAFIMAGD.ordinal() ];
			oVal[ 10 ] = sValsAgen[ PARAM_SGSETAAGENDASP.HRFIMAGD.ordinal() ];
			oVal[ 11 ] = sValsAgen[ PARAM_SGSETAAGENDASP.ASSUNTOAGD.ordinal() ];
			oVal[ 12 ] = sValsAgen[ PARAM_SGSETAAGENDASP.DESCAGD.ordinal() ];
			oVal[ 13 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODFILIALTA.ordinal() ];
			oVal[ 14 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODTIPOAGD.ordinal()  ];
			oVal[ 15 ] = sValsAgen[ PARAM_SGSETAAGENDASP.PRIORAGD.ordinal()  ];
			oVal[ 16 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODAGE.ordinal()  ];
			oVal[ 17 ] = sValsAgen[ PARAM_SGSETAAGENDASP.TIPOAGE.ordinal()  ];
			oVal[ 18 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODFILIALAE.ordinal()  ];
			oVal[ 19 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODAGEEMIT.ordinal()  ];
			oVal[ 20 ] = sValsAgen[ PARAM_SGSETAAGENDASP.TIPOAGEEMIT.ordinal()  ];
			oVal[ 21 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CAAGD.ordinal()  ];
			oVal[ 22 ] = sValsAgen[ PARAM_SGSETAAGENDASP.SITAGD.ordinal()  ];
			oVal[ 23 ] = sValsAgen[ PARAM_SGSETAAGENDASP.RESOLUCAOMOTIVO.ordinal()  ];
			oVal[ 24 ] = sValsAgen[ PARAM_SGSETAAGENDASP.CODAGDAR.ordinal()  ];
			oVal[ 25 ] = sValsAgen[ PARAM_SGSETAAGENDASP.DIATODO.ordinal()  ];
			/*
			 * 	CODAGD, CODEMP, DTAINIAGD, HRINIAGD, DTAFIMAGD, HRFIMAGD, ASSUNTOAGD, DESCAGD, CODFILIALTA, CODTIPOAGD, 
		PRIORAGD, CODAGE, CODFILIALAE, CODAGEEMIT, TIPOAGEEMIT, CAAGD, SITAGD, RESOLUCAOMOTIVO, CODAGDAR, DIATODO	
			oVal[ 14 ] = sValsAgen[ 9 ];
			oVal[ 15 ] = sValsAgen[ 10 ];
			oVal[ 16 ] = sValsAgen[ 11 ];
			oVal[ 17 ] = sValsAgen[ 12 ];
			oVal[ 18 ] = sValsAgen[ 13 ];
			oVal[ 19 ] = sValsAgen[ 14 ];
			oVal[ 20 ] = sValsAgen[ 15 ];
			oVal[ 21 ] = sValsAgen[ 16 ];
			*/
		}
		return oVal;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAtend.setConexao( cn );
		lcCont.setConexao( cn );
		lcCont.carregaDados();
		lcCli.setConexao( cn );
		lcCli.carregaDados();
		buscaAtend();
	}
}
