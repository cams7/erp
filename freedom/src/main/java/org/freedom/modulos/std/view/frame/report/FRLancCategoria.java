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

public class FRLancCategoria extends FRelatorio implements ActionListener, CarregaListener {
	
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

	public FRLancCategoria() {
		super(false);

		setTitulo( "Lançamentos por categoria" );
		setAtribos( 80, 80, 460, 525 );

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

		JLabel periodo = new JLabel( "Período", SwingConstants.CENTER );
		periodo.setOpaque( true );
		adic( periodo, 25, 10, 80, 20 );
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		adic( borda, 7, 20, 420, 45 );
		adic( txtDataini, 85, 35, 110, 20 );
		adic( new JLabel( "até", SwingConstants.CENTER ), 195, 35, 40, 20 );
		adic( txtDatafim, 235, 35, 110, 20 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

	
		adic( new JLabelPad( "Cód.Conta" ), 7, 65, 80, 20 );
		adic( txtCodConta, 7, 85, 80, 20 );		
		adic( new JLabelPad( "Descrição da  Conta" ), 90, 65, 230, 20 );
		adic( txtDescConta, 90, 85, 337, 20 );
		
		
		adic( new JLabelPad( "Cód. CC" ), 7, 105, 80, 20 );
		adic( txtCodCC, 7, 125, 80, 20 );
		adic( new JLabelPad( "Descrição do Centro de custo" ), 90, 105, 230, 20 );
		adic( txtDescCC, 90, 125, 307, 20 );
		adic( btAdicCC, 400, 125, 30, 30 );
		adic( btDeletaSelecionadoCC, 400, 165, 30, 30 );
		adic( btLimpaCC, 400, 198, 30, 30 );
		adic( pnTabelaCC, 7, 165, 390, 100  );
				
		
		adic( new JLabelPad( "Cód.Plan." ), 7, 280, 80, 20 );
		adic( txtCodPlan, 7, 300, 80, 20 );
		adic( new JLabelPad( "Descrição do planejamento" ), 90, 280, 230, 20 );
		adic( txtDescPlan, 90, 300, 307, 20 );
		adic( btAdic, 400, 300, 20, 20 );
		adic( btDeletaSelecionado, 400, 340, 30, 30 );
		adic( btLimpa, 400, 373, 30, 30 );
		adic( pnTabela, 7, 340, 390, 100  );

		pnTabelaCC.add( scrol2, BorderLayout.CENTER );

		tbCentroCusto.adicColuna( "Cód. CC" );
		tbCentroCusto.adicColuna( "Singla CC" );
		tbCentroCusto.adicColuna( "Descrição do Centro de custo" );

		tbCentroCusto.setTamColuna( 90, 0 );
		tbCentroCusto.setTamColuna( 90, 1 );
		tbCentroCusto.setTamColuna( 205, 2 );
		
		
		pnTabela.add( scrol, BorderLayout.CENTER );

		tbPlanoPag.adicColuna( "Cód.Plan" );
		tbPlanoPag.adicColuna( "Descrição do Planejamento" );
		tbPlanoPag.adicColuna( "Nivel" );
		
		tbPlanoPag.setTamColuna( 90, 0 );
		tbPlanoPag.setTamColuna( 205, 1 );
		tbPlanoPag.setTamColuna( 90, 2 );
		

	}

	private void montaListaCampos() {

		/*******************
		 * Centro de custo *
		 *******************/

		lcCC.add( new GuardaCampo( txtCodCC, "CodCC", "Cód.cc.", ListaCampos.DB_PK, false ) );
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

		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Cód.conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

		/****************
		 * Planejamento * *
		 ************/

		lcPlan.add( new GuardaCampo( txtCodPlan, "CodPlan", "Cód.plan", ListaCampos.DB_PK, false ) );
		lcPlan.add( new GuardaCampo( txtDescPlan, "DescPlan", "Descrição do planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.add( new GuardaCampo( txtNivelPlan, "NivelPlan", "Nível de planejamento", ListaCampos.DB_SI, false ) );
		lcPlan.montaSql( false, "PLANEJAMENTO", "FN" );
		lcPlan.setReadOnly( true );
		txtCodPlan.setTabelaExterna( lcPlan, null );
		txtCodPlan.setFK( true );
		txtCodPlan.setNomeCampo( "CodPlan" );
		lcPlan.setMuiltiselecaoF2( true );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuilder sSQL = new StringBuilder();
		ResultSet rs = null;
		StringBuilder sCab = new StringBuilder();
		StringBuilder sWhere = new StringBuilder();
		int iParam = 1;
		String sCodPlan = txtCodPlan.getVlrString().trim();
		String sCodCC = txtCodCC.getVlrString().trim();

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {

			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}
		
		
		if(tbCentroCusto.getRowCount() > 0	){
			sWhere.append( "AND SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODCC in (" );
			
			
			int numLinhas = tbCentroCusto.getNumLinhas();
			int numLinhasSel = 0;
			String[] sValores = null;
			Vector<String> vValores = new Vector<String>();
			String sRet = "";

			try {

				for ( int i = 0; i < numLinhas; i++ ) {
						vValores.add( "'" + tbCentroCusto.getValor( i, SEQUENCIA.CODIGO.ordinal() ).toString() + "'" );
					}
		
				sRet = Funcoes.vectorToString( vValores, "," );

			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
			sWhere.append( sRet +  ") ");
			
			
			
			sCab.append( " Planejamento:  " + txtCodPlan.getVlrString() + " " + txtDescPlan.getVlrString() );
		} else  {

			if ( !"".equals( txtCodCC.getVlrString().trim() ) ) {
	
				sWhere.append( "AND SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODCC LIKE? " );
				sCab.append( " Centro de custo:  " + txtCodCC.getVlrString() + "  " + txtDescCC.getVlrString() );
			}
		}

		if ( !"".equals( txtCodConta.getVlrString() ) ) {

			sWhere.append( "AND C.CODEMP=? AND C.CODFILIAL=? AND C.NUMCONTA=? " );
			sCab.append( " Conta:  " + txtCodConta.getVlrString() + " " + txtDescConta.getVlrString() );
		}
		
		if(tbPlanoPag.getRowCount() > 0	){
			sWhere.append( "AND SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODPLAN in (" );
			
			
			int numLinhas = tbPlanoPag.getNumLinhas();
			int numLinhasSel = 0;
			String[] sValores = null;
			Vector<String> vValores = new Vector<String>();
			String sRet = "";

			try {

				for ( int i = 0; i < numLinhas; i++ ) {
						vValores.add( "'" + tbPlanoPag.getValor( i,  SEQUENCIA.CODIGO.ordinal() ).toString() + "'" );
					}
		
				sRet = Funcoes.vectorToString( vValores, "," );

			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
			sWhere.append( sRet +  ") ");
			
			
			
			sCab.append( " Planejamento:  " + txtCodPlan.getVlrString() + " " + txtDescPlan.getVlrString() );
		} else  {
	
			if ( !"".equals( txtCodPlan.getVlrString() ) ) {
	
				sWhere.append( "AND SL.CODEMP=? AND SL.CODFILIAL=? AND SL.CODPLAN LIKE ?" );
				sCab.append( " Planejamento:  " + txtCodPlan.getVlrString() + " " + txtDescPlan.getVlrString() );
			}
		}

		sCab.append( "  Periodo: " + txtDataini.getVlrString() + " " + " Até " + " " + txtDatafim.getVlrString() );

		sSQL.append( " SELECT SL.CODPLAN, PL.DESCPLAN, SL.DATASUBLANCA, SL.HISTSUBLANCA, L.DOCLANCA, SL.VLRSUBLANCA,CC.DESCCC, C.DESCCONTA " );
		sSQL.append( "FROM FNSUBLANCA SL " );
		sSQL.append( "LEFT OUTER JOIN FNPLANEJAMENTO PL ON PL.CODEMP=SL.CODEMPPN AND PL.CODFILIAL=SL.CODFILIALPN AND PL.CODPLAN=SL.CODPLAN " );
		sSQL.append( "LEFT OUTER JOIN FNLANCA L ON L.CODEMP=SL.CODEMP AND L.CODFILIAL=SL.CODFILIAL AND L.CODLANCA=SL.CODLANCA " );
		sSQL.append( "LEFT OUTER JOIN FNCC CC ON CC.CODEMP=SL.CODEMPCC AND CC.CODFILIAL=SL.CODFILIALCC AND CC.CODCC=SL.CODCC AND CC.ANOCC=SL.ANOCC " );
		sSQL.append( "LEFT OUTER JOIN FNPLANEJAMENTO PL2 ON PL2.CODEMP=L.CODEMPPN AND PL2.CODFILIAL=L.CODFILIALPN AND PL2.CODPLAN=L.CODPLAN " );
		sSQL.append( "LEFT OUTER JOIN FNCONTA C ON C.CODEMPPN=PL2.CODEMP AND C.CODFILIALPN=PL2.CODFILIAL AND C.CODPLAN=PL2.CODPLAN " );
		sSQL.append( "WHERE SL.CODEMP=? AND SL.CODFILIAL=? AND SL.DATASUBLANCA BETWEEN ? AND ? " );
		sSQL.append( sWhere.toString() );
		sSQL.append( "ORDER BY SL.CODPLAN, SL.DATASUBLANCA" );

		try {

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );

			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if(tbCentroCusto.getRowCount() == 0){
					
				if ( !"".equals( txtCodCC.getVlrString() ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
					
			
						if ( sCodCC.indexOf( "%" ) == -1 ) {
							if ( sCodCC.length() < 13 ) {
								sCodCC += "%";
							}
						}
						ps.setString( iParam++, sCodCC );
					}
		
			} else {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			}
			


			if ( !"".equals( txtCodConta.getVlrString() ) ) {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNCONTA" ) );
				ps.setString( iParam++, txtCodConta.getVlrString() );
			}

			if(tbPlanoPag.getRowCount() == 0){
				
				if ( !"".equals( txtCodPlan.getVlrString() ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
					if ( sCodPlan.indexOf( "%" ) == -1 ) {
						if ( sCodPlan.length() < 13 ) {
							sCodPlan += "%";
						}
					}
						
					ps.setString( iParam++, sCodPlan );
				}
	
			} else {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			}

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

		dlGr = new FPrinterJob( "relatorios/FRLancamentos.jasper", "Lançamentos por categoria", sCab, rs, hParam, this );

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
		lcPlan.setConexao( cn );
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
		
		if ( evt.getSource() == btAdicCC ) {
			adicionaGridCentroCusto();
		} else if( evt.getSource() == btLimpaCC) {
			limpaGridCC();
		} else if( evt.getSource() == btDeletaSelecionadoCC) {
			deletaLinhaSelecionadaCC();
		} else if ( evt.getSource() == btAdic ) {
			adicionaGridPlanPag();
		} else if( evt.getSource() == btLimpa) {
			limpaGridPlanPag();
		} else if( evt.getSource() == btDeletaSelecionado) {
			deletaLinhaSelecionada();
		} else if( evt.getSource() == btExportXLS) {
			
		}
		
		super.actionPerformed( evt );
	}
	
	private void deletaLinhaSelecionadaCC() {
		if(tbCentroCusto.getSelectedRow() != -1){
			int linha = tbCentroCusto.getSelectedRow();
			tbCentroCusto.delLinha( linha );
		} else {
			Funcoes.mensagemInforma( this, "Centro de custo não selecionado!" );
			txtCodCC.requestFocus();
			return;
		}		

	}
	
	private void deletaLinhaSelecionada() {
		if(tbPlanoPag.getSelectedRow() != -1){
			int linha = tbPlanoPag.getSelectedRow();
			tbPlanoPag.delLinha( linha );
		} else {
			Funcoes.mensagemInforma( this, "Plano de pagamento não selecionado!" );
			txtCodPlan.requestFocus();
			return;
		}		
		
	}
	
	

	private void limpaGridCC() {

		tbCentroCusto.limpa();
		
	}
	

	private void limpaGridPlanPag() {

		tbPlanoPag.limpa();
		
	}
	
	private void adicionaGridCentroCusto() { 
		int colCodCC = 0;
		int colSiglaCC = 1;
		int colDescCC = 2;
		
		int qtdLinhas = tbCentroCusto.getNumLinhas();
		
		if ( "".equals( txtCodCC.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Plano de Pagamento não selecionado!" );
			txtCodCC.requestFocus();
			return;
		}		
		
		if(qtdLinhas > 0){
			String compare = null;
			boolean cadastrado = false;
			for(int i = 0; i < qtdLinhas; i++) {
				compare = tbCentroCusto.getValor( i, 0 ).toString();
				
				if(compare.equals( txtCodCC.getVlrString() )) {
					cadastrado = true;
					break;
				}
				
			}
			
			if(cadastrado){
				Funcoes.mensagemInforma( this, "Código Centro de custo já adicionado!" );
				txtCodCC.requestFocus();
				return;
			}
		}
		
		tbCentroCusto.adicLinha();
		
			tbCentroCusto.setValor( txtCodCC.getVlrString(), qtdLinhas , colCodCC );
			tbCentroCusto.setValor( txtSiglaCC.getVlrString(), qtdLinhas , colSiglaCC );
			tbCentroCusto.setValor( txtDescCC.getVlrString(), qtdLinhas , colDescCC );
	}

	private void adicionaGridPlanPag() { 

	
		int qtdLinhas = tbPlanoPag.getNumLinhas();
		
		if ( "".equals( txtCodPlan.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Plano de Pagamento não selecionado!" );
			txtCodPlan.requestFocus();
			return;
		}
		
		if( txtNivelPlan.getVlrInteger() != 6){
			Funcoes.mensagemInforma( this, "Apenas planejamentos analíticos são permitidos nessa opção!" );
			txtCodPlan.requestFocus();
			return;
		}
		
		
		if(qtdLinhas > 0){
			String compare = null;
			boolean cadastrado = false;
			for(int i = 0; i < qtdLinhas; i++) {
				compare = tbPlanoPag.getValor( i, 0 ).toString();
				
				if(compare.equals( txtCodPlan.getVlrString() )) {
					cadastrado = true;
					break;
				}
				
			}
			
			if(cadastrado){
				Funcoes.mensagemInforma( this, "Código de planejamento já adicionado!" );
				txtCodPlan.requestFocus();
				return;
			}
		}
		
		tbPlanoPag.adicLinha();
		
			tbPlanoPag.setValor( txtCodPlan.getVlrString(), qtdLinhas , SEQUENCIA.CODIGO.ordinal() );
			tbPlanoPag.setValor( txtDescPlan.getVlrString(), qtdLinhas , SEQUENCIA.DESCRICAO.ordinal() );
			tbPlanoPag.setValor( txtNivelPlan.getVlrInteger(), qtdLinhas , SEQUENCIA.NIVEL.ordinal() );
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}

	public void afterCarrega( CarregaEvent cevt ) {
		
		if(cevt.getListaCampos() == lcPlan){
			Vector<Object> lista = 	 txtCodPlan.getResultF2();
			
			if(lista != null && lista.size() > 0){
				String anatiticos = "6";
				tbPlanoPag.limpa();
				
				
				for ( Object row : lista ) {
					Vector<Object> rowVect = (Vector<Object>) row;
					String nivel = (String) rowVect.elementAt(SEQUENCIA.NIVEL.ordinal());
					if( anatiticos.equals( nivel ) ){
						tbPlanoPag.adicLinha(rowVect);
					}
					
				}
				txtCodPlan.setResultF2( null );
				txtCodPlan.setVlrString( "" );
				txtDescPlan.setVlrString( "" );
			}
		}
		
		
		if(cevt.getListaCampos() == lcCC){
			Vector<Object> lista = 	 txtCodCC.getResultF2();
			
			if(lista != null && lista.size() > 0){
				tbCentroCusto.limpa();
				
				
				for ( Object row : lista ) {
					Vector<Object> rowVect = (Vector<Object>) row;
					tbCentroCusto.adicLinha(rowVect);	
				}
				txtCodCC.setResultF2( null );
				txtCodCC.setVlrString( "" );
				txtDescCC.setVlrString( "" );
			}
		}
		
	}

	
}
