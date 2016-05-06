/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FCaixa.java <BR>
 * 
 *                 Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                 modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                 na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                 Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                 sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                 Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                 Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                 de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                 Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;

public class FCaixa extends FDados implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private ListaCampos lcEst = new ListaCampos( this, "ET" );

	private JTextFieldPad txtCodCaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescCaixa = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSeqIni = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldPad txtSeqMax = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 9, 0 );

	private JTextFieldFK txtDescEst = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbECF = new JCheckBoxPad( "Emissão de Cupom Fiscal ?", "S", "N" );

	private JCheckBoxPad cbTEF = new JCheckBoxPad( "Possui Gerenciador Padrão de TEF ?", "S", "N" );

	private JCheckBoxPad cbOrcamento = new JCheckBoxPad( "Habilitar venda somente com orçamento ?", "S", "N" );

	public FCaixa() {

		super( false );
		setTitulo( "Cadastro de caixa PDV" );
		setAtribos( 50, 50, 415, 270 );

		lcEst.add( new GuardaCampo( txtCodEst, "CodEst", "Cód.est.", ListaCampos.DB_PK, false ) );
		lcEst.add( new GuardaCampo( txtDescEst, "DescEst", "Descrição da estação de trabalho", ListaCampos.DB_SI, false ) );
		lcEst.montaSql( false, "ESTACAO", "SG" );
		lcEst.setQueryCommit( false );
		lcEst.setReadOnly( true );
		txtCodEst.setTabelaExterna( lcEst, FEstacao.class.getCanonicalName() );

		cbECF.setVlrString( "N" );
		cbTEF.setVlrString( "N" );
		cbOrcamento.setVlrString( "N" );

		adicCampo( txtCodCaixa, 7, 20, 80, 20, "CodCaixa", "Cód.caixa", ListaCampos.DB_PK, true );
		adicCampo( txtDescCaixa, 90, 20, 300, 20, "DescCaixa", "Descrição do caixa", ListaCampos.DB_SI, true );
		adicCampo( txtCodEst, 7, 60, 80, 20, "CodEst", "Cód.est.", ListaCampos.DB_FK, txtDescEst, true );
		adicDescFK( txtDescEst, 90, 60, 300, 20, "DescEst", "Descrição da estação de trabalho" );
		adicCampo( txtSeqIni, 7, 100, 190, 20, "SeqIni", "Seq. ínicial", ListaCampos.DB_SI, false );
		adicCampo( txtSeqMax, 200, 100, 190, 20, "SeqMax", "Seq. máxima", ListaCampos.DB_SI, false );
		adicDB( cbECF, 10, 130, 380, 20, "ECFCaixa", "", true );
		adicDB( cbTEF, 10, 150, 380, 20, "TEFCaixa", "", true );
		adicDB( cbOrcamento, 10, 170, 380, 20, "ORCCaixa", "", true );
		setListaCampos( true, "CAIXA", "PV" );

		lcCampos.addCarregaListener( this );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcEst.setConexao( cn );
	}

	private boolean getTemVenda() {

		boolean retorno = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT COUNT(SEQCAIXA) FROM PVSEQUENCIA WHERE CODEMP=? AND CODFILIAL=? AND CODCAIXA=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PVSEQUENCIA" ) );
			ps.setInt( 3, txtCodCaixa.getVlrInteger().intValue() );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno = ( rs.getInt( 1 ) > 0 );
			}

			con.commit();

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro ao verificar sequencia.\n" + e.getMessage() );
			e.printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return retorno;

	}

	public void beforeCarrega( CarregaEvent e ) {

		txtSeqIni.setAtivo( true );
		txtSeqMax.setAtivo( true );
	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			if ( getTemVenda() )
				if ( txtSeqIni.getVlrInteger().intValue() > 0 && txtSeqMax.getVlrInteger().intValue() > 0 ) {
					txtSeqIni.setAtivo( false );
					txtSeqMax.setAtivo( false );
				}
		}
	}

}
