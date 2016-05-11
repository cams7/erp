/**
 * @version 20/07/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms.view.frame.report <BR>
 *         Classe:
 * @(#)FRFreteExpedicao.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Tela para filtros do relatório de fretes de expedição de produtos.
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

public class FRFreteExpedicao extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeTran = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private ListaCampos lcTransp = new ListaCampos( this );

	boolean cliente = false;

	boolean diario = false;
	
	private JCheckBoxPad cbPendentes = new JCheckBoxPad( "Pendentes", "S", "N" );

	private JCheckBoxPad cbEmPagamento = new JCheckBoxPad( "Em pagamento", "S", "N" );

	private JCheckBoxPad cbPagos = new JCheckBoxPad( "Pagas", "S", "N" );


	public FRFreteExpedicao() {

		setTitulo( "Fretes de expedição de mercadorias" );

		setAtribos( 80, 80, 380, 280 );

		txtNomeTran.setAtivo( false );

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		lcTransp.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.Tran.", ListaCampos.DB_PK, true ) );
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
		
		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, TitledBorder.LEFT ) );

		adic( pnFiltros, 4, 75, 325, 120 );

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
	
	private HashMap<String,BigDecimal> getCalculaRetencoes() {
		
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
		
		BigDecimal vlrinsspago = new BigDecimal(0);
		BigDecimal vlrirrfpago = new BigDecimal(0);
		
		BigDecimal vlrtotpago = new BigDecimal(0);

		Boolean calcinss = false;
		Boolean calcirrf = false;
		Boolean calcoutros = false;
		
		int codpag = -1;
		
		Integer nrodepend = null;
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			sql.append( "select ");
			sql.append( "'S' pago, fr.codpag, ");
			sql.append( "coalesce(fo.nrodependfor,0) nrodependfor, tf.retencaoinss, tf.retencaoirrf, tf.percbaseinss, ");
			sql.append( "tf.percbaseirrf, tf.percretoutros, tf.retencaooutros, fr.vlrfrete vlrfrete, ip.vlrretinss vlrretinss, ip.vlrretirrf vlrretirrf ");
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
			sql.append( "fr.codemp=? and fr.codfilial=? and coalesce(fr.dtpagfrete,fr.dtemitfrete) between ? and ? ");
			sql.append( "and fr.codemptn=? and fr.codfilialtn=? and fr.codtran=? and fr.ticket is null and fr.codpag is not null ");
			sql.append( "union all ");
			sql.append( "select ");
			sql.append( "'N' pago, fr.codpag, ");
			sql.append( "coalesce(fo.nrodependfor,0) nrodependfor, tf.retencaoinss, tf.retencaoirrf, tf.percbaseinss, ");
			sql.append( "tf.percbaseirrf, tf.percretoutros, tf.retencaooutros, sum(fr.vlrfrete) vlrfrete, cast(0 as decimal(15,5)) vlrretinss, cast(0 as decimal(15,5)) vlrretirrf ");
			sql.append( "from ");
			sql.append( "lffrete fr ");
			sql.append( "left outer join vdtransp tr on ");
			sql.append( "tr.codemp=fr.codemptn and tr.codfilial=fr.codfilialtn and tr.codtran=fr.codtran ");
			sql.append( "left outer join cpforneced fo on ");
			sql.append( "fo.codemp=tr.codempfr and fo.codfilial=tr.codfilialfr and fo.codfor=tr.codfor ");
			sql.append( "left outer join cptipofor tf on ");
			sql.append( "tf.codemp=fo.codemptf and tf.codfilial=fo.codfilialtf and tf.codtipofor=fo.codtipofor ");
			sql.append( "where ");
			sql.append( "fr.codemp=? and fr.codfilial=? and coalesce(fr.dtpagfrete,fr.dtemitfrete) between ? and ? ");
			sql.append( "and fr.codemptn=? and fr.codfilialtn=? and fr.codtran=? and fr.ticket is null and fr.codpag is null ");
			sql.append( "group by 2,3,4,5,6,7,8,9 ");
			sql.append( "order by 1 desc, 2 " );
			
			
			ps = con.prepareStatement( sql.toString() );
			
			System.out.println("query pgto ant:" + sql.toString());
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFFRETE" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( 5, lcTransp.getCodEmp() );
			ps.setInt( 6, lcTransp.getCodFilial() );
			ps.setInt( 7, txtCodTran.getVlrInteger() );
			ps.setInt( 8, Aplicativo.iCodEmp );
			ps.setInt( 9, ListaCampos.getMasterFilial( "LFFRETE" ) );
			ps.setDate( 10, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( 11, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( 12, lcTransp.getCodEmp() );
			ps.setInt( 13, lcTransp.getCodFilial() );
			ps.setInt( 14, txtCodTran.getVlrInteger() );			
			rs = ps.executeQuery();

			while(rs.next()){
			
				// Leitura do registro referente ao inss recolhido
				if( "S".equals(rs.getString( "PAGO" )) ) {
					
					vlrtotpago = vlrtotpago.add( rs.getBigDecimal( "vlrfrete" ));				
					if (codpag!=rs.getInt( "codpag" )) {
						vlrinsspago = vlrinsspago.add( rs.getBigDecimal( "vlrretinss" ) );
						vlrirrfpago = vlrirrfpago.add( rs.getBigDecimal( "vlrretirrf" ) );
					}
					codpag = rs.getInt( "codpag" );
					
				}
				// Leitura do registro referente ao inss a recolher
				else  {
					
				//	vlrtotpago = vlrtotpago.add( rs.getBigDecimal( "vlrfrete" )); 
					
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
			txtDataini.requestFocus();
			return;
		}
		if ( ! (txtCodTran.getVlrInteger()>0) ) {
			Funcoes.mensagemInforma( this, "Código do transportador obrigatório!" );
			txtCodTran.requestFocus();
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		StringBuffer sCab = new StringBuffer();		
		
		BigDecimal vlrinss = null;
		BigDecimal vlrirrf = null;
		BigDecimal vlrinsspago = null;
		BigDecimal vlrirrfpago = null;
		BigDecimal vlrtotpago = null;
		
		try {
		
			/*********CALCULO DE RETENCOES**********/
				
			HashMap<String, BigDecimal> retensoes = getCalculaRetencoes();
				
			vlrinss = retensoes.get( "VLRINSS" );
			vlrirrf = retensoes.get( "VLRIRRF" );
			vlrinsspago = retensoes.get( "VLRINSSPAGO" );
			vlrirrfpago = retensoes.get( "VLRIRRFPAGO" );
			vlrtotpago = retensoes.get( "VLRTOTPAGO" );
				
			/***********FIM**********************/
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		int param = 1;

		sql.append( "select fr.codtran, tr.nometran, tr.placatran placaveiculo, fr.dtemitfrete dtent,cl.cidcli localentrega, " );
		
		sql.append( "docfrete ticket,fr.vlrfrete /  fr.pesoliquido * 1000 preco, fr.pesoliquido, fr.vlrfrete, 0.00 vlrretinss, 0.00 vlrretirrf, ");
		sql.append( "(select p.vlrpagopag from FNPAGAR p where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and ");

		sql.append( "(select sum(ip1.nparcpag) from fnitpagar ip1 "); 
		sql.append( "where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) = ");

		sql.append( "(select sum(ip1.nparcpag) from fnitpagar ip1 ");
		sql.append( "where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag ");
		sql.append( "and ip1.statusitpag='PP')) as pago, ");

		sql.append( "(select p.vlrpagopag from FNPAGAR p where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa ");
		sql.append( "and p.codpag=fr.codpag) as emPagamento ");

		sql.append( "from lffrete fr ");
		sql.append( "left outer join vdtransp tr on tr.codemp=fr.codemptn and tr.codfilial=fr.codfilialtn and tr.codtran=fr.codtran ");
		
		sql.append( "left outer join vdcliente cl on cl.codempuc=fr.codempde and cl.codfilialuc=fr.codfilialde and cl.codunifcod=fr.coddestinat ");
		sql.append( "left outer join sgmunicipio mn on mn.codpais=cl.codpais and mn.siglauf=cl.siglauf and mn.codmunic=cl.codmunic ");
		
		sql.append( "where  fr.codemp=? and fr.codfilial=? and fr.dtemitfrete between ? and ? ");
		sql.append( "and fr.ticket is null ");

		if ( txtCodTran.getVlrInteger() > 0 ) {
			sql.append( "and fr.codemptn=? and fr.codfilialtn=? and fr.codtran=? " );
		}
		
		StringBuilder where = new StringBuilder();
		
		if ( "S".equals( cbPendentes.getVlrString() ) ) {
			where.append( " fr.codpag is null" );
		}
		if ( "S".equals( cbPagos.getVlrString() ) ) {
			if ( where.length() > 0 ) {
				where.append( " and" );
			}
			where.append( " (select p.codpag from FNPAGAR p" );
			where.append( "  where p.codemp=fr.codemppa and p.codfilial=fr.codfilialpa and p.codpag=fr.codpag and" );
			where.append( "  (select count(ip1.nparcpag) from fnitpagar ip1" );
			where.append( "   where ip1.codemp=p.codemp and ip1.codfilial=p.codfilial and ip1.codpag=p.codpag) =" );
			where.append( "  (select count(ip1.nparcpag) from fnitpagar ip1" );
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

		sql.append( " order by fr.dtemitfrete " );

		try {

			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "LFFRETE" ) );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

			if ( txtCodTran.getVlrInteger() > 0 ) {
				
				ps.setInt( param++, lcTransp.getCodEmp() );
				ps.setInt( param++, lcTransp.getCodFilial() );
				ps.setInt( param++, txtCodTran.getVlrInteger() );

				sCab.append( "Transportador: " + txtNomeTran.getVlrString() + "\n" );
				
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

		dlGr = new FPrinterJob( "layout/rel/REL_FRETE_EXPEDICAO.jasper", "Relatório de fretes", sCab, rs, hParam, this );

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
