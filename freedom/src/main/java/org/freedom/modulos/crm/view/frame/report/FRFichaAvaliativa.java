/**
 * @version 28/09/2011 <BR>
 * @author Setpoint Informática Ltda. / Bruno Nascimento<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FRCronograma.java <BR>
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
 *         Relatório Cronograma Sintético.
 * 
 */

package org.freedom.modulos.crm.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.view.frame.crud.plain.FMotivoAval;
import org.freedom.modulos.crm.view.frame.crud.tabbed.FContato;

public class FRFichaAvaliativa extends FRelatorio implements CarregaListener{

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	
	/** FICHA AVALIATIVA
	 * 
	 */
	
	private JTextFieldPad txtCodCto = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCto = new JTextFieldFK( JTextFieldFK.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JCheckBoxPad cbFinaliCriFichaAval = new JCheckBoxPad( "Criança?", "S", "N" );
	
	private JCheckBoxPad cbFinaliAniFichaAval = new JCheckBoxPad( "Animal?", "S", "N" );
	
	private JCheckBoxPad cbFinaliOutFichaAval = new JCheckBoxPad( "Outros?", "S", "N" );
	
	private JCheckBoxPad cbCobertFichaAval = new JCheckBoxPad( " Indica se é cobertura?", "S", "N" );
	
	private JCheckBoxPad cbEstrutFichaAval = new JCheckBoxPad( "Há necessidade de estrutura?", "S", "N" );
	
	private JCheckBoxPad cbOcupadoFichaAval = new JCheckBoxPad( "Imóvel ocupado?", "S", "N" );
	
	private JCheckBoxPad cbSacadaFichaAval = new JCheckBoxPad( "Sacadas?", "S", "N" );
	
	private JCheckBoxPad cbPredentrfichaAval = new JCheckBoxPad( "Prédio/casa está sendo entregue agora?", "S", "N");
	
	private JCheckBoxPad cbJanelaFichaAval =  new JCheckBoxPad( "Janelas?", "S", "N");
	
	private JCheckBoxPad cbOutrosFichaAval = new JCheckBoxPad( "Outros?", "S", "N");
	
	private JCheckBoxPad cbApartamento = new JCheckBoxPad( "Apartameto?", "S", "N");
	
	private JCheckBoxPad cbCasa = new JCheckBoxPad( "Casa?", "S", "N");
	
	private JCheckBoxPad cbEmpresa = new JCheckBoxPad( "Empresa?", "S", "N");
	
	private JCheckBoxPad cbMobiliado = new JCheckBoxPad( "Mobiliado?", "S", "N");
	
	private JCheckBoxPad cbSemi= new JCheckBoxPad( "Semi-Mobiliado?", "S", "N");
	
	private JCheckBoxPad cbVazio= new JCheckBoxPad( "Vazio?", "S", "N");
	
	private JTextFieldPad txtCodMotAval= new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescMotAval = new JTextFieldFK( JTextFieldFK.TP_STRING, 50, 0 );
	
	private ListaCampos lcContato = new ListaCampos( this );
	
	private ListaCampos lcMotAval = new ListaCampos( this );
	
	public FRFichaAvaliativa() {		
		setTitulo( "Ações realizadas" );
		setAtribos( 80, 80, 370	, 430 );
		
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
		adic( txtCodCto, 7, 80, 80, 20, "Cod.Contato" );
		adic( txtRazCto, 90, 80, 225, 20, "Nome do contato" );
		adic( txtCodMotAval, 7, 120, 80, 20, "Cod.Mot.Aval.");
		adic( txtDescMotAval, 90, 120, 225, 20, "Descrição do motivo da avaliação." );
		adic( cbPredentrfichaAval, 7, 150, 265, 20, "");
		adic( cbCobertFichaAval, 7, 175, 225, 20, "" );
		adic( cbEstrutFichaAval, 7, 200, 225, 20, "" );
		adic( cbOcupadoFichaAval, 7, 225, 225, 20, "" );
		adic( cbSacadaFichaAval, 7, 250, 100, 20, "" );
		adic( cbJanelaFichaAval, 120, 250, 100, 20, "");
		adic( cbOutrosFichaAval, 240, 250, 100, 20, "");

		adic( cbFinaliCriFichaAval, 7, 275, 100, 20, "");
		adic( cbFinaliAniFichaAval, 120, 275, 100, 20, "");
		adic( cbFinaliOutFichaAval, 240, 275, 100, 20, "");
		adic( cbCasa, 7, 300, 100, 20, "");
		adic( cbApartamento, 120, 300, 100, 20, "");
		adic( cbEmpresa, 240, 300, 100, 20, "");
		adic( cbMobiliado, 7, 325, 100, 20, "");
		adic( cbSemi, 120, 325, 120, 20, "");
		adic( cbVazio, 240, 325, 100, 20, "");

				
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
	
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
	}
	
	private void montaListaCampos() {
		
		
		/**********************
		 * Contato * *
		 *******************/

		txtCodCto.setTabelaExterna( lcContato, FContato.class.getCanonicalName());
		txtCodCto.setFK( true );
		txtCodCto.setNomeCampo( "Codcto" );
		lcContato.add( new GuardaCampo( txtCodCto, "CodCto", "Cód.Contato", ListaCampos.DB_PK, false ) );
		lcContato.add( new GuardaCampo( txtRazCto, "RazCto", "Razão do contato.", ListaCampos.DB_SI, false ) );
		lcContato.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_SI, false ) );
		lcContato.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.Tipo Cli.", ListaCampos.DB_SI, false ) );
		lcContato.montaSql( false, "CONTATO", "TK" );
		lcContato.setReadOnly( true );
		lcContato.setQueryCommit( false );
		
		
		/**********************
		 * Motivo Aval. * *
		 *******************/

		txtCodMotAval.setTabelaExterna( lcMotAval, FMotivoAval.class.getCanonicalName());
		txtCodMotAval.setFK( true );
		txtCodMotAval.setNomeCampo( "MotAval" );
		lcMotAval.add( new GuardaCampo( txtCodMotAval, "CodMotAval", "Cód.Motivo", ListaCampos.DB_PK, false ) );
		lcMotAval.add( new GuardaCampo( txtDescMotAval, "DescMotAval", "Descrição do motivo da avaliação.", ListaCampos.DB_SI, false ) );
		lcMotAval.montaSql( false, "MotivoAval", "CR" );
		lcMotAval.setReadOnly( true );
		lcMotAval.setQueryCommit( false );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		Blob fotoemp = FPrinterJob.getLogo( con );
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data inicial maior que a data final!" );
			return;
		}
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder(" where fi.codemp=? and fi.codfilial=? and fi.dtfichaaval between ? and ? ");
		
		try{
			
			if(txtCodCto.getVlrInteger() > 0) {
				where.append(" and fi.codcto=? ");
			}
			
			if(txtCodMotAval.getVlrInteger() > 0) {
				where.append(" and fi.codmotaval=? ");
			}
			
			if("S".equals( cbPredentrfichaAval.getVlrString() )) {
				where.append( "and fi.predentrfichaaval='S' " );
			}
			if("S".equals( cbCobertFichaAval.getVlrString() )) {
				where.append( "and fi.cobertfichaaval='S' ");
			}
			if("S".equals( cbEstrutFichaAval.getVlrString() )) {
				where.append( "and fi.estrutfichaaval='S' ");
			}
			if("S".equals( cbOcupadoFichaAval.getVlrString() )) {
				where.append("and fi.ocupadofichaaval='S' ");
			}
			if("S".equals( cbSacadaFichaAval.getVlrString())) {
				where.append("and fi.sacadafichaaval='S' ");
			}
			if("S".equals( cbJanelaFichaAval.getVlrString() )) {
				where.append("and fi.janelafichaaval='S' ");
			}
			if("S".equals( cbOutrosFichaAval.getVlrString() )) {
				where.append("and fi.outrosfichaaval='S' ");
			}
			if("S".equals( cbFinaliCriFichaAval.getVlrString() )) {
				where.append("and fi.finalicrifichaaval='S' ");
			}
			if("S".equals( cbFinaliAniFichaAval.getVlrString() )) {
				where.append("and fi.finalianifichaaval='S' ");
			}
			if("S".equals( cbFinaliOutFichaAval.getVlrString() )) {
				where.append("and fi.finalioutfichaaval='S' ");
			}
			
			if("S".equals( cbCasa.getVlrString() ) || "S".equals( cbApartamento.getVlrString()) || "S".equals( cbEmpresa.getVlrString() )) {
				where.append( "and fi.localfichaaval in (" );
				String sep = "";
				if("S".equals( cbCasa.getVlrString() )) {
					where.append("'C'");
					sep = ",";
				}
				if("S".equals( cbApartamento.getVlrString() )) {
					where.append(sep);
					where.append("'A'");
					sep=",";
				}
				if("S".equals( cbEmpresa.getVlrString() )) {
					where.append(sep);
					where.append("'E'");
				}
				where.append( ") ");
			}
			
			if("S".equals( cbMobiliado.getVlrString() ) || "S".equals( cbSemi.getVlrString() ) || "S".equals( cbVazio.getVlrString() )) {
				where.append( "and fi.mobilfichaaval in (" );
				String sep = "";
				if("S".equals( cbMobiliado.getVlrString() )) {
					where.append("'M'");
					sep = ",";
				}
				if("S".equals( cbSemi.getVlrString() )) {
					where.append(sep);
					where.append("'S'");
					sep=",";
				}
				if("S".equals( cbVazio.getVlrString() )) {
					where.append(sep);
					where.append("'V'");
				}
				where.append( ") ");
			}		
			
			sql.append("select fi.seqfichaaval, fi.codcto, cto.nomecto, cto.edificiocto, fi.dtfichaaval, fi.codmotaval, ma.descmotaval, fi.finalicrifichaaval, fi.finalianifichaaval, ");
			sql.append("fi.finalioutfichaaval, fi.janelafichaaval, fi.qtdjanelafichaaval, fi.sacadafichaaval, fi.qtdsacadafichaaval, fi.outrosfichaaval, fi.descoutrosfichaaval, fi.obsfichaaval, fi.localfichaaval, ");
			sql.append("f.razfilial, f.dddfilial, f.fonefilial, f.endfilial, f.numfilial, f.siglauf siglauff, f.bairfilial, f.cnpjfilial,f.emailfilial,  m.nomemunic nomemunicf ");
			sql.append("from crfichaaval fi ");
			sql.append("left outer join tkcontato cto on cto.codemp = fi.codempco and cto.codfilial = fi.codfilialco and cto.codcto=fi.codcto ");
			sql.append("left outer join crmotivoaval ma on ma.codemp = fi.codempma and ma.codfilial = fi.codfilialma and ma.codmotaval=fi.codmotaval ");
			sql.append("left outer join sgfilial f on f.codemp=? and f.codfilial=? ");
			sql.append("left outer join sgmunicipio m on m.codmunic=f.codmunic and m.codpais=f.codpais and m.siglauf=f.siglauf ");
			sql.append( where.toString() );
			ps = con.prepareStatement(sql.toString());
			int param = 1;
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGFILIAL" ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CRFICHAAVAL" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ));
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ));
			
			if(txtCodCto.getVlrInteger() > 0) {
				ps.setInt( param++, txtCodCto.getVlrInteger() );
			}
			
			if(txtCodMotAval.getVlrInteger() > 0) {
				ps.setInt( param++,txtCodMotAval.getVlrInteger() );
			}
			
			rs = ps.executeQuery();
			
			System.out.println(sql.toString());
			

		} catch (Exception err) {
			Funcoes.mensagemErro( this, "Erro na consulta do relatório de fichas avaliativas \n" + err.getMessage(), true, con, err );
		}

		imprimiGrafico( bVisualizar, rs, fotoemp );

	}

	private void imprimiGrafico( TYPE_PRINT bVisualizar, ResultSet rs, Blob fotoemp) {
		String report = "relatorios/rel_ficha_avaliativa_091.jasper";
		String label = "Lista ficha avaliativa";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();
		hParam.put( "SUBREPORT_DIR", "relatorios/" );
		hParam.put( "CODEMP", new Integer(Aplicativo.iCodEmp) );
		hParam.put( "CODFILIAL", new Integer(ListaCampos.getMasterFilial( "CRFICHAAVAL" )) );
	    try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
		
		FPrinterJob dlGr = new FPrinterJob( report, label, "", rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatório de fichas avaliativas!" + err.getMessage(), true, con, err );
			}
		}
	}
	

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcContato.setConexao( cn );
		lcMotAval.setConexao( cn );
		
	}

	public void afterCarrega( CarregaEvent cevt ) {
		
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

}
