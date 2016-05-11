/**
 * @version 02/08/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms.view.frame.report <BR>
 *         Classe:
 * @(#)FRVendasItem.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela para filtros do relatirio de fretes de recebimento de mercadorias.
 * 
 */

package org.freedom.modulos.gms.view.frame.report;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.border.TitledBorder;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.fnc.view.dialog.utility.DLNovoPag;

public class FRFreteRecMerc extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtPrevrecfim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcTransp = new ListaCampos( this );

	boolean cliente = false;

	boolean diario = false;

	private JCheckBoxPad cbPendentes = new JCheckBoxPad( "Pendentes", "S", "N" );

	private JCheckBoxPad cbEmPagamento = new JCheckBoxPad( "Em pagamento", "S", "N" );

	private JCheckBoxPad cbPagos = new JCheckBoxPad( "Pagas", "S", "N" );


	public FRFreteRecMerc() {

		setTitulo( "Fretes de recebimento de mercadorias" );
		setAtribos( 80, 80, 380, 350 );

		txtNomeTran.setAtivo( false );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );
        
		txtPrevrecfim.setVlrDate( new Date() );
		
		lcTransp.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.Tran.", ListaCampos.DB_PK, false ) );
		lcTransp.add( new GuardaCampo( txtNomeTran, "NomeTran", "Nome do transportador", ListaCampos.DB_SI, false ) );
		txtCodTran.setTabelaExterna( lcTransp, null );
		txtCodTran.setNomeCampo( "CodTran" );
		txtCodTran.setFK( true );
		lcTransp.setReadOnly( true );
		lcTransp.montaSql( false, "TRANSP", "VD" );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnPeriodo, 4, 5, 325, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 05, 30, 20 );
		pnPeriodo.adic( txtDataini, 35, 05, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 05, 30, 20 );
		pnPeriodo.adic( txtDatafim, 170, 05, 90, 20 );

		JPanelPad pnPrevisao = new JPanelPad();
		pnPrevisao.setBorder( SwingParams.getPanelLabel( "Previsão de pagto.", Color.BLACK, TitledBorder.LEFT ) );
	
		adic( pnPrevisao, 4, 75, 325, 60 );

		pnPrevisao.adic( new JLabelPad( "Data:" ), 5, 05, 30, 20 );
		pnPrevisao.adic( txtPrevrecfim, 45, 05, 90, 20 );

		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );
		
		adic( pnFiltros, 4, 145, 325, 120 );

		pnFiltros.adic( new JLabelPad( "Cód.Tran." ), 4, 5, 70, 20 );
		pnFiltros.adic( txtCodTran, 4, 25, 70, 20 );

		pnFiltros.adic( new JLabelPad( "Nome do transportador" ), 77, 5, 230, 20 );
		pnFiltros.adic( txtNomeTran, 77, 25, 230, 20 );

		pnFiltros.adic( cbPendentes, 7, 55, 90, 20 );
		pnFiltros.adic( cbEmPagamento, 100, 55, 120, 20 );
		pnFiltros.adic( cbPagos, 230, 55, 65, 20 );

		cbPendentes.setVlrString( "S" );
		cbEmPagamento.setVlrString( "N" );
		cbPagos.setVlrString( "N" );

	}


	private HashMap<String,BigDecimal> getCalculaRetencoes(Date dtprevrecfim) {

		HashMap<String,BigDecimal> ret = new HashMap<String, BigDecimal>();

		BigDecimal cem = new BigDecimal( 100 );

		BigDecimal percbaseinss = null;
		BigDecimal percbaseirrf = null;
		BigDecimal percoutros = null;
		BigDecimal vlrbaseinss = null;
		BigDecimal vlrbaseirrf = null;

		BigDecimal vlrinss = null;
		BigDecimal vlrirrf = null;
		BigDecimal vlroriginal = null;

		BigDecimal vlrinsspago = null;
		BigDecimal vlrirrfpago = null;
		BigDecimal vlrtotpago = new BigDecimal(0);

		Boolean calcinss = false;
		Boolean calcirrf = false;
		Boolean calcoutros = false;

		Integer nrodepend = null;

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {


			sql.append( "select ");
			sql.append( "case when fr.codpag is null then 'N' else 'S' end as pago, sum(fr.vlrfrete) vlrfrete, coalesce(fo.nrodependfor,0) nrodependfor, tf.retencaoinss, tf.retencaoirrf, tf.percbaseinss, ");
			sql.append( "tf.percbaseirrf, tf.percretoutros, tf.retencaooutros, ip.vlrretinss, ip.vlrretirrf ");

			sql.append( "from ");

			sql.append( "lffrete fr ");

			sql.append( "left outer join vdtransp tr on ");
			sql.append( "tr.codemp=fr.codemptn and tr.codfilial=fr.codfilialtn and tr.codtran=fr.codtran ");

			sql.append( "left outer join cpforneced fo on ");
			sql.append( "fo.codemp=tr.codempfr and fo.codfilial=tr.codfilialfr and fo.codfor=tr.codfor ");

			sql.append( "left outer join cptipofor tf on ");
			sql.append( "tf.codemp=fo.codemptf and tf.codfilial=fo.codfilialtf and tf.codtipofor=fo.codtipofor ");	

			sql.append( "left outer join fnpagar ip on ip.codemp=fr.codemppa and ip.codfilial=fr.codfilialpa and ip.codpag=fr.codpag ");

			sql.append( "where ");
			sql.append( "fr.codemp=? and fr.codfilial=? and coalesce(");
			sql.append( "(case when extract(year from fr.dtpagfrete)=? and extract(month from fr.dtpagfrete)=? ");
			sql.append( "then fr.dtpagfrete else null end) ");
			sql.append( ",fr.dtemitfrete) between ? and ? ");

			sql.append( "and fr.codemptn=? and fr.codfilialtn=? and fr.codtran=? and fr.ticket is not null ");

			sql.append( "group by 1,3,4,5,6,7,8,9,10,11 order by 1 desc " );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, Funcoes.getAno( dtprevrecfim ) );
			ps.setInt( 4, Funcoes.getMes( dtprevrecfim ) );
			ps.setDate( 5, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) ); 
			ps.setDate( 6, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( 7, lcTransp.getCodEmp() );
			ps.setInt( 8, lcTransp.getCodFilial() );
			ps.setInt( 9, txtCodTran.getVlrInteger() );

			rs = ps.executeQuery();

			System.out.println("query ret ant: " + sql.toString());

			while(rs.next()){

				// Leitura do registro referente ao inss recolhido
				if( "S".equals(rs.getString( "PAGO" )) ) {

					vlrtotpago = vlrtotpago.add( rs.getBigDecimal( "vlrfrete" ));				
					vlrinsspago = rs.getBigDecimal( "vlrretinss" );
					vlrirrfpago = rs.getBigDecimal( "vlrretirrf" );

				}
				// Leitura do registro referente ao inss a recolher
				else  {

					calcinss = "S".equals( rs.getString( "RetencaoINSS" ));
					calcirrf = "S".equals( rs.getString( "RetencaoIRRF" ));
					calcoutros = "S".equals( rs.getString( "RetencaoIRRF" ));
					nrodepend = rs.getInt( "nrodependfor" );

					vlroriginal = rs.getBigDecimal( "vlrfrete" );

					if(vlrtotpago!=null) {
						vlroriginal = vlroriginal.add( vlrtotpago );						
					}

					//Se deve calcular a retenção de INSS...
					if( calcinss ) {
						percbaseinss = rs.getBigDecimal( "PercBaseINSS" );					
						vlrbaseinss = (vlroriginal.multiply( percbaseinss )).divide( cem );

						// Se deve calcular a retenção de outros tributos junto com o INSS
						if(calcoutros) {
							percoutros = rs.getBigDecimal( "PercRetOutros" );
						}					
						vlrinss = DLNovoPag.getVlrINSS( vlroriginal, vlrbaseinss, percoutros, nrodepend );					
					}				
					//Se deve calcular a retenção de INSS...
					if( calcirrf ) {
						percbaseirrf = rs.getBigDecimal( "PercBaseIRRF" );
						vlrbaseirrf = (vlroriginal.multiply( percbaseirrf )).divide( cem );

						vlrirrf = DLNovoPag.getVlrIRRF( vlroriginal, vlrbaseirrf, vlrbaseinss, vlrinss, DLNovoPag.getReducaoDependente(), nrodepend );

					}

				}

			}

			ret.put( "VLRINSS", vlrinss );
			ret.put( "VLRIRRF", vlrirrf );

			ret.put( "VLRINSSPAGO", vlrinsspago );
			ret.put( "VLRIRRFPAGO", vlrirrfpago );

			ret.put( "VLRTOTPAGO", vlrtotpago );

			con.commit();
			ps.close();
			rs.close();


		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public void imprimir( TYPE_PRINT visualizar ) {

		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}
		
		if ( (txtPrevrecfim.getText()==null) || ("".equals( txtPrevrecfim.getText().trim())) ) {
			Funcoes.mensagemInforma( this, "Selecione uma data para previsão de pagamento !" );
			return;
		}

		Date dtprevrecfim = txtPrevrecfim.getVlrDate();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuffer sCab = new StringBuffer();		

		BigDecimal vlrinss = null;
		BigDecimal vlrirrf = null;

		BigDecimal vlrinsspago = null;
		BigDecimal vlrirrfpago = null;
		BigDecimal vlrtotpago = null;

		int param = 1;

		if(txtCodTran.getVlrInteger()>0) {

			try {

				/*********CALCULO DE RETENCOES**********/

				/*********CALCULO DE RETENCOES**********/



				HashMap<String, BigDecimal> retensoes = getCalculaRetencoes(dtprevrecfim);

				vlrinss = retensoes.get( "VLRINSS" );
				vlrirrf = retensoes.get( "VLRIRRF" );
				vlrinsspago = retensoes.get( "VLRINSSPAGO" );
				vlrirrfpago = retensoes.get( "VLRIRRFPAGO" );
				vlrtotpago = retensoes.get( "VLRTOTPAGO" );


				/***********FIM**********************/			
				/***********FIM**********************/

			}
			catch (Exception e) {
				e.printStackTrace();
			}

			sql.append( "select ");
			sql.append( "fr.codtran, tr.nometran, rm.placaveiculo, rm.dtent, rm.codbairro, br.nomebairro, rm.ticket, br.vlrfrete preco, ");
			sql.append( "fr.pesoliquido, fr.vlrfrete, 0.00 vlrretinss, 0.00 vlrretirrf, ");

			sql.append( "(select p.vlrpagopag from FNPAGAR p ");
			sql.append( "where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and ");
			sql.append( "(select sum(ip1.nparcpag) from fnitpagar ip1 ");
			sql.append( "where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) = ");
			sql.append( "(select sum(ip1.nparcpag) from fnitpagar ip1 ");
			sql.append( "where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and ");
			sql.append( "ip1.statusitpag='PP')) as pago, ");

			sql.append( "(select p.vlrpagopag from FNPAGAR p ");
			sql.append( "where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag) as emPagamento ");

			sql.append( "from ");
			sql.append( "eqrecmerc rm ");
			sql.append( "right outer join lffrete fr on ");
			sql.append( "fr.codemprm=rm.codemp and fr.codfilialrm=rm.codfilial and fr.ticket=rm.ticket and fr.ticket is not null ");
			sql.append( "left outer join vdtransp tr on ");
			sql.append( "tr.codemp=fr.codemptn and tr.codfilial=fr.codfilialtn and tr.codtran=fr.codtran ");
			sql.append( "left outer join sgbairro br on ");
			sql.append( "br.codpais=rm.codpais and br.siglauf=rm.siglauf and br.codmunic=rm.codmunic and br.codbairro=rm.codbairro ");

			sql.append( "where ");

			//Código mantido para eventual retorno
			//sql.append( " rm.codemp=? and rm.codfilial=? and rm.dtent between ? and ? ");
			sql.append( " fr.codemp=? and fr.codfilial=? and fr.dtemitfrete between ? and ? ");

			if ( txtCodTran.getVlrInteger() > 0 ) {
				//sql.append( "and rm.codemptn=? and rm.codfilialtn=? and rm.codtran=? " );
				sql.append( "and fr.codemptn=? and fr.codfilialtn=? and fr.codtran=? " );
			}

			StringBuilder where = new StringBuilder();

			if ( "S".equals( cbPendentes.getVlrString() ) ) {
				where.append( " fr.codpag is null" );
			}
			if ( "S".equals( cbPagos.getVlrString() ) ) {
				if ( where.length() > 0 ) {
					where.append( " or" );
				}
				where.append( " (select p.codpag from FNPAGAR p" );
				where.append( "  where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
				where.append( "   ip1.statusitpag='PP')) is not null " );
			}
			if ( "S".equals( cbEmPagamento.getVlrString() ) ) {
				if ( where.length() > 0 ) {
					where.append( " or" );
				}
				where.append( " (fr.codpag is not null and" );
				where.append( " (select p.codpag from FNPAGAR p" );
				where.append( "  where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
				where.append( "   ip1.statusitpag='PP')) is null) " );
			}

			sql.append( where.length() > 0 ? ( " and (" + where.toString() + ")" ) : "" );

			sql.append( " order by rm.dtent,rm.ticket " );


		}
		
		// Query para relatório agrupado
		else {

			sql.append( "select ");
			sql.append( "fr.codtran, tn.nometran, sum(fr.vlrfrete) valor, sum(fr.pesoliquido) peso ");
			sql.append( "from lffrete fr, vdtransp tn ");
			sql.append( "where tn.codemp=fr.codemptn and tn.codfilial=fr.codfilialtn and tn.codtran=fr.codtran and ");
			sql.append( "fr.ticket is not null and fr.codemp=? and fr.codfilial=? and fr.dtemitfrete between ? and ? and ");
			
			StringBuilder where = new StringBuilder();

			if ( "S".equals( cbPendentes.getVlrString() ) ) {
				where.append( " fr.codpag is null " );
			}
			if ( "S".equals( cbPagos.getVlrString() ) ) {
				if ( where.length() > 0 ) {
					where.append( " or" );
				}
				where.append( " (select p.codpag from FNPAGAR p" );
				where.append( "  where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
				where.append( "   ip1.statusitpag='PP')) is not null " );
			}
			if ( "S".equals( cbEmPagamento.getVlrString() ) ) {
				if ( where.length() > 0 ) {
					where.append( " or" );
				}
				where.append( " (fr.codpag is not null and" );
				where.append( " (select p.codpag from FNPAGAR p" );
				where.append( "  where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
				where.append( "  (select sum(ip1.nparcpag) from fnitpagar ip1" );
				where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag and" );
				where.append( "   ip1.statusitpag='PP')) is null) " );
			}


			sql.append( where );
			sql.append( "group by 1,2 " );
			

		}


		try {

			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			if ( txtCodTran.getVlrInteger() > 0 ) {
			
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "EQRECMERC" ) );
	
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

				ps.setInt( param++, lcTransp.getCodEmp() );
				ps.setInt( param++, lcTransp.getCodFilial() );
				ps.setInt( param++, txtCodTran.getVlrInteger() );

				sCab.append( "Transportador: " + txtNomeTran.getVlrString() + "\n" );

			}
			else {
				
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "LFFRETE" ) );
	
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				
			}

			System.out.println( "SQL:" + sql.toString() );

			rs = ps.executeQuery();

		} 
		catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao buscar dados dos fretes." );

		}

		imprimirGrafico( visualizar, rs, sCab.toString(), vlrinss, vlrirrf, vlrinsspago, vlrirrfpago, vlrtotpago );

	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, final String sCab, BigDecimal vlrinss, BigDecimal vlrirrf,BigDecimal vlrinsspago,BigDecimal vlrirrfpago, BigDecimal vlrtotpago ) {

		HashMap<String, Object> hParam = new HashMap<String, Object>();

		hParam.put( "VLRINSS"		, vlrinss 		!= null ? vlrinss 		: new BigDecimal(0));
		hParam.put( "VLRIRRF"		, vlrirrf 		!= null ? vlrirrf 		: new BigDecimal(0) );
		hParam.put( "VLRINSSPAGO"	, vlrinsspago 	!= null ? vlrinsspago 	: new BigDecimal(0) );
		hParam.put( "VLRIRRFPAGO"	, vlrirrfpago 	!= null ? vlrirrfpago 	: new BigDecimal(0) );
		hParam.put( "VLRTOTPAGO"	, vlrtotpago 	!= null ? vlrtotpago 	: new BigDecimal(0) );

		FPrinterJob dlGr = null;

		dlGr = new FPrinterJob( txtCodTran.getVlrInteger() > 0 ? "layout/rel/REL_FRETE_RECMERC.jasper" : "layout/rel/REL_FRETE_RECMERC_AGRUPADO.jasper" , "Relatório de fretes", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcTransp.setConexao( cn );

	}
}
