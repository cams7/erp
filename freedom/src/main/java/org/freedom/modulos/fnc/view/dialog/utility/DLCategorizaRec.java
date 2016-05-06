/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLEditaRec.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Comentários sobre a classe...
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;

public class DLCategorizaRec extends FFDialogo implements CarregaListener, FocusListener {

	private static final long serialVersionUID = 1L;


	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldPad txtAnoCC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );

	private JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcPlan = new ListaCampos( this );

	private ListaCampos lcCC = new ListaCampos( this );

	public enum EColRet {
		CODPLAN, CODCC, ANOCC
	};

	public DLCategorizaRec( Component cOrig ) {

		super( cOrig );
		setTitulo( "Categorizar títulos" );
		setAtribos( 365, 180 );

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan.", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.setWhereAdic( "TIPOPLAN = 'R' AND NIVELPLAN = 6" );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.c.c.", ListaCampos.DB_PK, false ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla c.c.", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custos", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtAnoCC, "AnoCC", "Ano-Base", ListaCampos.DB_PK, false ) );
		lcCC.setReadOnly( true );
		lcCC.setQueryCommit( false );
		lcCC.setWhereAdic( "NIVELCC=10" );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
		txtAnoCC.setTabelaExterna( lcCC, null );
		txtAnoCC.setFK( true );
		txtAnoCC.setNomeCampo( "AnoCC" );

		adic( txtCodPlan, 7, 20, 110, 20, "Cód.categoria" );
		adic( txtDescPlan, 120, 20, 220, 20, "Descrição da categoria" );

		adic( txtCodCC, 7, 60, 110, 20, "Cód.c.c." );
		adic( txtDescCC, 120, 60, 220, 20, "Descrição do centro de custo" );


		lcCC.addCarregaListener( this );


	}


	public Object[] getValores() {

		Object[] oRetorno = new Object[ EColRet.values().length ];

		oRetorno[ EColRet.CODPLAN.ordinal() ] = txtCodPlan.getVlrString();
		oRetorno[ EColRet.CODCC.ordinal() ] = txtCodCC.getVlrString();
		oRetorno[ EColRet.ANOCC.ordinal() ] = txtAnoCC.getVlrInteger();
		
		return oRetorno;
	}
	
/*
	public void actionPerformed( ActionEvent evt ) {
		super
	}
*/
	private int buscaAnoBaseCC() {

		int iRet = 0;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		}

		return iRet;
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCC && txtAnoCC.getVlrInteger().intValue() == 0 ) {
			txtAnoCC.setVlrInteger( new Integer( buscaAnoBaseCC() ) );
		}

	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcPlan.setConexao( cn );
		lcPlan.carregaDados();
		lcCC.setConexao( cn );
		lcCC.carregaDados();

	}

	public void focusGained( FocusEvent arg0 ) {

		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent e ) {


	}
}
