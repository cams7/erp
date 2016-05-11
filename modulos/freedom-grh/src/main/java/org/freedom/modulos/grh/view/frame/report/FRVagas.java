/**
 * @version 12/08/2008 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.grh <BR>
 *         Classe:
 * @(#)FRVagas.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  Tela para cadastro de estruturas de produtos.
 * 
 */
package org.freedom.modulos.grh.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRVagas extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodEmpr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeEmpr = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodFuncao = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescFuncao = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final ListaCampos lcFuncao = new ListaCampos( this );

	private final ListaCampos lcEmpr = new ListaCampos( this );

	public FRVagas() {

		super( false );
		setTitulo( "Lista de Vagas" );
		setAtribos( 50, 50, 350, 220 );

		montaListaCampos();
		montaTela();

	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );

		adic( new JLabelPad( "De:" ), 10, 25, 30, 20 );
		adic( txtDataini, 40, 25, 97, 20 );
		adic( new JLabelPad( "Até:" ), 152, 25, 37, 20 );
		adic( txtDatafim, 190, 25, 100, 20 );
		adic( new JLabelPad( "Cód.Empr." ), 7, 55, 70, 20 );
		adic( txtCodEmpr, 7, 75, 70, 20 );
		adic( new JLabelPad( "Descrição do Empregador" ), 80, 55, 250, 20 );
		adic( txtNomeEmpr, 80, 75, 250, 20 );
		adic( new JLabelPad( "Cód.Func" ), 7, 95, 70, 20 );
		adic( txtCodFuncao, 7, 115, 70, 20 );
		adic( new JLabelPad( "Descrição da Função" ), 80, 95, 250, 20 );
		adic( txtDescFuncao, 80, 115, 250, 20 );
	}

	private void montaListaCampos() {

		/******************
		 * Função *
		 ******************/
		lcFuncao.add( new GuardaCampo( txtCodFuncao, "CodFunc", "Cód.Func.", ListaCampos.DB_PK, false ) );
		lcFuncao.add( new GuardaCampo( txtDescFuncao, "DescFunc", "Descrição da função", ListaCampos.DB_SI, false ) );
		txtCodFuncao.setTabelaExterna( lcFuncao, null );
		txtCodFuncao.setNomeCampo( "CodFunc" );
		txtCodFuncao.setFK( true );
		lcFuncao.setReadOnly( true );
		lcFuncao.montaSql( false, "FUNCAO", "RH" );

		/******************
		 * Empregador *
		 ******************/
		lcEmpr.add( new GuardaCampo( txtCodEmpr, "CodEmpr", "Cód.Empr..", ListaCampos.DB_PK, false ) );
		lcEmpr.add( new GuardaCampo( txtNomeEmpr, "NomeEmpr", "Descrição do Empregador", ListaCampos.DB_SI, false ) );
		txtCodEmpr.setTabelaExterna( lcEmpr, null );
		txtCodEmpr.setNomeCampo( "CodEmpr" );
		txtCodEmpr.setFK( true );
		lcEmpr.setReadOnly( true );
		lcEmpr.montaSql( false, "EMPREGADOR", "RH" );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();

		try {

			if ( txtCodFuncao.getVlrInteger() > 0 ) {

				sWhere.append( "AND V.CODFUNC = " + txtCodFuncao.getVlrInteger() );
			}
			if ( txtCodEmpr.getVlrInteger() > 0 ) {

				sWhere.append( "AND V.CODEMPR = " + txtCodEmpr.getVlrInteger() );
			}
			if ( txtDataini.getVlrDate() != null && txtDatafim.getVlrDate() != null ) {

				sWhere.append( "AND V.DTINS BETWEEN ? AND ?" );

			}

			sql.append( "SELECT E.NOMEEMPR, F.DESCFUNC, T.DESCTURNO, V.FAIXASALINI, V.FAIXASALFIM, V.CODVAGA, E.CODEMPR, F.CODFUNC " );
			sql.append( "FROM RHVAGA V, RHEMPREGADOR E, RHFUNCAO F, RHTURNO T " );
			sql.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? AND " );
			sql.append( "V.CODEMPEM=E.CODEMP AND V.CODFILIALEM=E.CODFILIAL AND V.CODEMPR=E.CODEMPR AND " );
			sql.append( "V.CODEMPFC=F.CODEMP AND V.CODFILIALFC=F.CODFILIAL AND V.CODFUNC=F.CODFUNC AND  " );
			sql.append( "V.CODEMPTN=T.CODEMP AND V.CODFILIALTN=T.CODFILIAL AND V.CODTURNO=T.CODTURNO " );
			sql.append( sWhere.toString() );
			sql.append( "ORDER BY V.CODVAGA, E.NOMEEMPR, F.DESCFUNC, T.DESCTURNO, V.FAIXASALINI, V.FAIXASALFIM " );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RHVAGA" ) );

			if ( txtDataini.getVlrDate() != null && txtDatafim.getVlrDate() != null ) {

				ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			}
			rs = ps.executeQuery();

		} catch ( SQLException e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados! " + e.getMessage() );
		}

		dlGr = new FPrinterJob( "relatorios/grhVagas.jasper", "Lista de Vagas", "", rs, null, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro na geração do relátorio!" + e.getMessage(), true, con, e );
			}
		}
	}

	public void setConexao( DbConnection con ) {

		super.setConexao( con );
		lcEmpr.setConexao( con );
		lcFuncao.setConexao( con );
	}
}
