/**
 * @version 04/06/2002 <BR>
 * @author Setpoint Informática Ltda. <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRLancCategoria.java <BR>
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;

public class FRCentroConta extends FRelatorio implements ActionListener, CarregaListener {
	
	enum SEQUENCIA{ CODIGO, DESCRICAO, NIVEL}

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodCC = new JTextFieldPad( JTextFieldPad.TP_STRING, 19, 0 );

	private JTextFieldFK txtDescCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtSiglaCC = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPlan txtCodPlan = new JTextFieldPlan( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldFK txtDescPlan = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtNivelPlan = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private ListaCampos lcCC = new ListaCampos( this );

	private ListaCampos lcConta = new ListaCampos( this );

	private ListaCampos lcPlan = new ListaCampos( this );
	
	private JButtonPad btAdic = new JButtonPad(Icone.novo( "btAdic.gif" ) );
	
	private JButtonPad btLimpa = new JButtonPad(Icone.novo( "btNada.png" ) );
	
	private JButtonPad btDeletaSelecionado = new JButtonPad(Icone.novo( "btExcluir.png" ) );
	
	private JButtonPad btAdicCC = new JButtonPad(Icone.novo( "btAdic.gif" ) );
	
	private JButtonPad btLimpaCC = new JButtonPad(Icone.novo( "btNada.png" ) );
	
	private JButtonPad btDeletaSelecionadoCC = new JButtonPad(Icone.novo( "btExcluir.png" ) );

	private JTablePad tbPlanoPag = new JTablePad();
	
	private JTablePad tbCentroCusto = new JTablePad();

	private JScrollPane scrol = new JScrollPane( tbPlanoPag );
	
	private JScrollPane scrol2 = new JScrollPane( tbCentroCusto);

	private JPanelPad pnCampos = new JPanelPad();

	private JPanelPad pnTabela = new JPanelPad( new BorderLayout() );
	
	private JPanelPad pnTabelaCC = new JPanelPad( new BorderLayout() );
	
	int iAnoBase = 0;
	
	int linha = 0;

	public FRCentroConta() {
		super(false);

		setTitulo( "Centro de custo/Conta" );
		setAtribos( 80, 80, 460, 180 );

		montaTela();
		montaListaCampos();

		btAdic.addActionListener( this );
		btLimpa.addActionListener( this );
		btDeletaSelecionado.setToolTipText( "Excluir" );
		btDeletaSelecionado.addActionListener( this );
		btLimpa.setToolTipText( "Exclui todos" );
		
		btAdicCC.addActionListener( this );
		btLimpaCC.addActionListener( this );
		btDeletaSelecionadoCC.setToolTipText( "Excluir" );
		btDeletaSelecionadoCC.addActionListener( this );
		btLimpaCC.setToolTipText( "Exclui todos" );
		btExportXLS.setEnabled( true );
		//btExportXLS.addActionListener( this );
		lcPlan.addCarregaListener( this );
		lcCC.addCarregaListener( this );
	}

	private void montaTela() {

		adic( new JLabelPad( "Cód.Conta" ), 7, 05, 110, 20 );
		adic( txtCodConta, 7, 25, 110, 20 );		
		adic( new JLabelPad( "Descrição da  Conta" ), 120, 05, 307, 20 );
		adic( txtDescConta, 120, 25, 300, 20 );
		
		adic( new JLabelPad( "Cód. CC" ), 7, 45, 110, 20 );
		adic( txtCodCC, 7, 65, 110, 20 );
		adic( new JLabelPad( "Descrição do Centro de custo" ), 120, 45, 307, 20 );
		adic( txtDescCC, 120, 65, 300, 20 );		

	}

	private void montaListaCampos() {

		/*******************
		 * Centro de custo *
		 *******************/

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.cc.", ListaCampos.DB_PK, true ) );
		lcCC.add( new GuardaCampo( txtSiglaCC, "SiglaCC", "Sigla", ListaCampos.DB_SI, false ) );
		lcCC.add( new GuardaCampo( txtDescCC, "DescCC", "Descrição do centro de custo", ListaCampos.DB_SI, false ) );
		lcCC.setReadOnly( true );
		lcCC.montaSql( false, "CC", "FN" );
		txtCodCC.setTabelaExterna( lcCC, null );
		txtCodCC.setFK( true );
		txtCodCC.setNomeCampo( "CodCC" );
		txtSiglaCC.setListaCampos( lcCC );
		lcCC.setMuiltiselecaoF2( true );
		
		/*********
		 * Conta *
		 *********/

		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Cód.conta", ListaCampos.DB_PK, true ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sSQL = new StringBuilder();
		ResultSet rs = null;
		StringBuilder sCab = new StringBuilder();
		StringBuilder sWhere = new StringBuilder();
		int iParam = 1;
		String sCodPlan = txtCodPlan.getVlrString().trim();
		String sCodCC = txtCodCC.getVlrString().trim();
		
		sCab.append( " Conta:  " + txtCodConta.getVlrString() + " " + txtDescConta.getVlrString() );
		sCab.append( "\nCentro de custo:  " + txtCodCC.getVlrString() + "  " + txtDescCC.getVlrString() );
	
		sSQL.append( " SELECT SL.CODPLAN, PL.DESCPLAN, SL.DATASUBLANCA, SL.HISTSUBLANCA, L.DOCLANCA, -1 * SL.VLRSUBLANCA VLRSUBLANCA,CC.DESCCC, C.DESCCONTA " );
		sSQL.append( "FROM FNSUBLANCA SL " );
		sSQL.append( "LEFT OUTER JOIN FNPLANEJAMENTO PL ON PL.CODEMP=SL.CODEMPPN AND PL.CODFILIAL=SL.CODFILIALPN AND PL.CODPLAN=SL.CODPLAN " );
		sSQL.append( "LEFT OUTER JOIN FNLANCA L ON L.CODEMP=SL.CODEMP AND L.CODFILIAL=SL.CODFILIAL AND L.CODLANCA=SL.CODLANCA " );
		sSQL.append( "LEFT OUTER JOIN FNCC CC ON CC.CODEMP=SL.CODEMPCC AND CC.CODFILIAL=SL.CODFILIALCC AND CC.CODCC=SL.CODCC AND CC.ANOCC=SL.ANOCC " );
		sSQL.append( "LEFT OUTER JOIN FNPLANEJAMENTO PL2 ON PL2.CODEMP=L.CODEMPPN AND PL2.CODFILIAL=L.CODFILIALPN AND PL2.CODPLAN=L.CODPLAN " );
		sSQL.append( "LEFT OUTER JOIN FNCONTA C ON C.CODEMPPN=PL2.CODEMP AND C.CODFILIALPN=PL2.CODFILIAL AND C.CODPLAN=PL2.CODPLAN " );
		sSQL.append( "WHERE SL.CODEMP=? AND SL.CODFILIAL=? " );
		sWhere.append( "AND SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODCC LIKE ? " );
		sWhere.append( "AND C.CODEMP=? AND C.CODFILIAL=? AND C.NUMCONTA=? " );
		sSQL.append( sWhere.toString() );
		sSQL.append( "ORDER BY SL.DATASUBLANCA, SL.CODSUBLANCA" );

		try {

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );

			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			
			if ( sCodCC.indexOf( "%" ) == -1 ) {
				if ( sCodCC.length() < 13 ) {
					sCodCC += "%";
				}
			}
			
			ps.setString( iParam++, sCodCC );
			
			
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNCONTA" ) );
			ps.setString( iParam++, txtCodConta.getVlrString() );

			rs = ps.executeQuery();

		} catch ( Exception e ) {

			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados " + e.getMessage() );
		}

		if (bVisualizar==TYPE_PRINT.EXPORT) {
			if (btExportXLS.execute(rs, getTitle())) {
				Funcoes.mensagemInforma( this, "Arquivo exportado com sucesso !" );
			}
			try {
				rs.close();
				con.commit();
			} catch ( SQLException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			imprimiGrafico( rs, bVisualizar, sCab.toString() );
		}
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar, final String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "FNCONTA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );

		dlGr = new FPrinterJob( "relatorios/FRCentroConta.jasper", "Centro de custo/Conta", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}

		else {

			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "erro na impressão do relatório!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		iAnoBase = buscaAnoBaseCC();
		lcConta.setConexao( cn );
		lcCC.setConexao( cn );
		lcCC.setWhereAdic( "ANOCC=" + iAnoBase );
	}

	private int buscaAnoBaseCC() {

		int iRet = 0;
		String sSQL = "SELECT ANOCENTROCUSTO FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				iRet = rs.getInt( "ANOCENTROCUSTO" );
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao buscar o ano-base para o centro de custo.\n" + err.getMessage(), true, con, err );
		}
		return iRet;
	}
	
	@Override
	public void actionPerformed( ActionEvent evt ) {
		if (evt.getSource() == btImp || evt.getSource() == btPrevimp || evt.getSource() == btExportXLS) {
			if ("".equals( txtCodCC.getVlrString() ) ) {
				Funcoes.mensagemInforma( this, "Campo Centro de custo é Obrigatório!!!" );
				return;
			} else if ("".equals( txtCodConta.getVlrString() ) ) {
				Funcoes.mensagemInforma( this, "Campo Conta é Obrigatório!!!" );
				return;
			}
		}
		super.actionPerformed( evt );
	}
	

	
	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {
		
	}

	
}
