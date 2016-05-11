/**
 * @version 28/09/2011 <BR>
 * @author Setpoint Informática Ltda./ Bruno Nascimento<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FRSitContr.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *         Relatório Situação Projetos/Contratos.
 * 
 */

package org.freedom.modulos.crm.view.frame.report;

import org.freedom.library.type.TYPE_PRINT;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;

public class FRAnaliseAcoes extends FRelatorio  {

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private ListaCampos lcAtendente = new ListaCampos( this );
	
	private ListaCampos lcUsuario = new ListaCampos( this );
	
	private JTextFieldPad txtCodAtend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0);
	
	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );
		
	private JTextFieldPad txtIntervaloDiaIni = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtIntervaloDiaFim = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtIntervaloHoraIni = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2);
	
	private JTextFieldPad txtIntervaloHoraFim = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2);
	
	private JComboBoxPad cbOrdem = null;
	
	private JComboBoxPad cbTipo = null;
	
	private JRadioGroup< ? , ?> rgTipo = null;
		
	public FRAnaliseAcoes() {		
		setTitulo( "Análise de ações" );
		setAtribos( 80, 80, 410	, 400 );
		montaComboBox();
		montaListaCampos();
		montaTela();
		
	}
	
	private void montaTela(){
				
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Período:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 7, 1, 80, 20 );
		adic( lbLinha, 5, 10, 300, 45 );
		
		adic( new JLabelPad( "De:" ), 15, 25, 25, 20 );
		adic( txtDataini, 42, 25, 95, 20 );
		adic( new JLabelPad( "Até:" ), 148, 25, 25, 20 );
		adic( txtDatafim, 178, 25, 95, 20 );
		
		adic( txtCodAtend, 7, 80, 80, 20, "Cod.Atend" );
		adic( txtNomeAtend, 90, 80, 215, 20, "Nome do atendente");
		
		adic( txtCodUsu, 7, 120, 80, 20, "Cod.Usu" );
		adic( txtNomeUsu, 90, 120, 215, 20, "Nome do usuário");
		txtCodUsu.setNomeCampo( "IDUSU" );
		
		adic( new JLabelPad( "Intervalo em dias" ), 7, 140, 200, 20 );
		adic( txtIntervaloDiaIni, 7, 160, 80, 20 );
		adic( txtIntervaloDiaFim, 90, 160, 80, 20);
		
		adic( new JLabelPad( "Intervalo em horas" ), 7, 180, 200, 20 );
		adic( txtIntervaloHoraIni, 7, 200, 80, 20 );
		adic( txtIntervaloHoraFim, 90, 200, 80, 20);
			
		//adic( rgTipo, 6, 225, 300, 30 );
		
		adic( cbOrdem, 6, 240, 300, 20, "Ordem");
	
		adic( cbTipo,  6, 280, 300, 20, "Tipo de relatório");
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
	
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );
		
		setIntervalos();
		
	}
	
	private void setIntervalos(){
		
		txtIntervaloDiaIni.setVlrInteger( 0 );
		txtIntervaloDiaFim.setVlrInteger( 99 );
		txtIntervaloHoraIni.setVlrInteger( 0 );
		txtIntervaloHoraFim.setVlrInteger( 1000 );
	}
	
	private void montaComboBox(){
	
		Vector<String> lOrdem = new Vector<String>();
		Vector<String> vOrdem = new Vector<String>();
		
		lOrdem.addElement( "Dia e hora " );
		lOrdem.addElement( "Data e horário" );
		lOrdem.addElement( "Atendente, data e horário" );
		lOrdem.addElement( "Atendente, data (Decrescente) e horário (Decrescente)" );
		vOrdem.addElement( " 8 desc, 9 desc" );
		vOrdem.addElement( " a.dataatendo, a.horaatendofin" );
		vOrdem.addElement( " e.nomeatend, a.dataatendo, a.horaatendofin" );
		vOrdem.addElement( " e.nomeatend, a.dataatendo desc, a.horaatendofin desc" );

		cbOrdem = new JComboBoxPad( lOrdem, vOrdem, JComboBoxPad.TP_INTEGER, 5, 0 );
	
		Vector<String> lTipo = new Vector<String>();
		Vector<String> vTipo = new Vector<String>();
		
		lTipo.addElement( " Detalhado" );
		lTipo.addElement( " Resumido" );
		lTipo.addElement( " Média" );
		vTipo.addElement( "REL_ANALISE_ACOES.jasper" );
		vTipo.addElement( "REL_ANALISE_ACOES.jasper" );
		vTipo.addElement( "REL_ANALISE_ACOES.jasper" );
		
		cbTipo = new JComboBoxPad( lTipo, vTipo, JComboBoxPad.TP_INTEGER, 8, 0 );
	
	}

	private void montaListaCampos() {
		 
		//Atendente
		lcAtendente.add( new GuardaCampo( txtCodAtend, "CodAtend", "Cód.atend.", ListaCampos.DB_PK, false ), "txtCodVendx" );
		lcAtendente.add( new GuardaCampo( txtNomeAtend, "NomeAtend", "Nome", ListaCampos.DB_SI, false ), "txtCodVendx" );
		lcAtendente.montaSql( false, "ATENDENTE", "AT" );
		lcAtendente.setReadOnly( true );
		
		txtCodAtend.setTabelaExterna( lcAtendente, null );
		txtCodAtend.setFK( true );
		txtCodAtend.setNomeCampo( "CodAtend" );
		
		lcUsuario.add( new GuardaCampo( txtCodUsu, "IDUSU", "ID usuario", ListaCampos.DB_PK,txtNomeUsu, false ) );
		lcUsuario.add( new GuardaCampo( txtNomeUsu, "NOMEUSU", "Nome do usuario", ListaCampos.DB_SI, false ) );
		lcUsuario.montaSql( false, "USUARIO", "SG" );
		lcUsuario.setReadOnly( true );

		txtCodUsu.setTabelaExterna( lcUsuario, null );
		txtCodUsu.setFK( true );
	

	}
	
	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sCab = new StringBuilder();
		StringBuilder sql = new StringBuilder();
		Blob fotoemp = FPrinterJob.getLogo( con );
		
		
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			return;
		}
		
		sCab.append( "Período de " + txtDataini.getVlrString() + " a " +  txtDatafim.getVlrString()  );
		
		if(txtCodAtend.getVlrInteger().intValue() > 0){
			sCab.append( txtCodAtend.getVlrInteger().toString() + " - " + txtNomeAtend.getVlrString()  );
		}
		if(!"".equals( txtCodUsu.getVlrString() ) ) {
			sCab.append( " - Usuário: " + txtCodUsu.getVlrString() );	
		}

		sql.append( "select e.nomeatend, a.idusuins, a.codatend, a.dataatendo, a.dtins , a.horaatendofin, a.hins, cast ( ( ( ( case when a.hins-a.horaatendofin>0 then ");
		sql.append( "		a.hins-a.horaatendofin else 0 end) / 60 / 60 ) + ( ");
		sql.append( "(a.dtins-a.dataatendo) * 24) / 24 ) as decimal(15,2) ) numdias , ");
		sql.append( "cast ( ( (case when a.hins-a.horaatendofin>0 then ");
		sql.append( "a.hins-a.horaatendofin else 0 end) / 60 / 60 ) + ( ");
		sql.append( "(a.dtins-a.dataatendo) * 24) as decimal(15,2) ) qtdhorasint , ");
		sql.append( "cast( ( a.horaatendofin- a.horaatendo) / 60 / 60 as decimal(15,2) ) qtdhoras , ea.descespec, ea.codespec ");
		sql.append( "from atatendimento a, atatendente e, atespecatend ea where a.codemp=? and a.codfilial=? and a.dataatendo between ? and ? ");
		sql.append( "and e.codemp=a.codempae  and e.codfilial=a.codfilialae" );
		if(txtCodAtend.getVlrInteger() > 0){
			sql.append( " and  a.codatend=? ");	
		}
		sql.append( " and e.codatend=a.codatend ");
		if(!"".equals( txtCodUsu.getVlrString() ) ) {
			sql.append(  " and a.idusuins=? ");
		}
		sql.append( " and ea.codemp=a.codempea and ea.codfilial=a.codfilialea and ea.codespec=a.codespec ");
		sql.append( "order by ");
		sql.append( cbOrdem.getVlrString() );

		try{

			ps = con.prepareStatement( sql.toString() );
			int param = 1;
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			if(txtCodAtend.getVlrInteger() > 0){
				ps.setInt( param++, txtCodAtend.getVlrInteger() );
			}
			if(!"".equals( txtCodUsu.getVlrString() ) ) {
				ps.setString( param++, txtCodUsu.getVlrString().toUpperCase() );
			}
			rs = ps.executeQuery();

		} catch (Exception err) {
			Funcoes.mensagemErro( this, "Erro consulta Relatório de premiação!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
		
		imprimiGrafico( bVisualizar, rs,  sCab.toString(), fotoemp );

	}

	private void imprimiGrafico( TYPE_PRINT bVisualizar, ResultSet rs, String sCab, Blob fotoemp) {
		String report = "layout/rel/" + cbTipo.getVlrString();
		String label = "Relatório de análise de acoes";
		
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		hParam.put( "TITULO", "Relatório de Análise de ações" );
		
		try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		
		FPrinterJob  dlGr = new FPrinterJob( report, label, sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Premiação!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcAtendente.setConexao( cn );
		lcUsuario.setConexao( cn );
	}
}
