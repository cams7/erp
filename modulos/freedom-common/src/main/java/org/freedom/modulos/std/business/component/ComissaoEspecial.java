/**
 * @version 31/08/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)ComissaoEspecial.java <BR>
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
 * Classe para calculo de comissionamento especial por seção de produção.                    
 * 
 */

package org.freedom.modulos.std.business.component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.std.business.object.ComissionadoComissao;
import org.freedom.modulos.std.business.object.SecaoComissao;

public class ComissaoEspecial {
	
	private final Constant TIPO_COMISSAO_FAT = new Constant( "Valor gerado no faturamento", "F" );
	
	private final Constant TIPO_COMISSAO_REC = new Constant( "Valor gerado no recebimento", "R" );
	
	private final Constant TIPO_COMISSAO_ESP = new Constant( "Comissionamento especial", "E" );
	
	private final Constant STATUS_COMISSAO_ABERTO = new Constant( "Em Aberto", "C1" );
	
	private final Constant STATUS_COMISSAO_LIBERADA = new Constant( "Liberada", "C2" );
	
	private final Constant STATUS_COMISSAO_PAGA = new Constant( "Paga", "CP" );
	
	/** Coleção contendo os valores de comissão por seção **/
	
	private Vector<SecaoComissao> lista_secao_comissao;

	/** Coleção contendo os valores de comissão por vendedor **/
	
	private Vector<ComissionadoComissao> lista_comissionado_valor_comissao;
	
	private DbConnection con = null;

	private Integer codvenda;
	
	private String tipovenda;
	
	private BigDecimal vlrvendacomi;
	
	private Date datacomi;
	
	private Date dtvenccomi;

	public ComissaoEspecial( Integer codvendap, String tipovendap, BigDecimal vlrvendacomip, Date datacomip, Date dtvenccomip ) {

		setCon( Aplicativo.getInstace().getConexao() );
		setCodvenda( codvendap );
		setTipovenda( tipovendap );
		setVlrvendacomi( vlrvendacomip );
		
		setDatacomi( datacomip );
		setDtvenccomi( dtvenccomip );
		
		carregaSecoesComissao();
		carregaCommisionadosComissao();

	}
	 
	public void processaComissao() {
		
		limpaTabelaComissoes();
		geraTabelaComissoes();
		
	}
	
	private void carregaSecoesComissao() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<SecaoComissao> lista_secao_comissaop = new Vector<SecaoComissao>();
		
		try {
			
			sql.append( "select pd.codsecao,rc.perccomisgeral," );
			sql.append( "sum( (iv.vlrbasecomisitvenda * rc.perccomisgeral) / 100 ) vlrcomissaogeral ");
			sql.append( "from vditvenda iv, eqproduto pd " );
			sql.append( "left outer join vdregracomis rc on " );	
			sql.append( "rc.codempsc=pd.codempsc and rc.codfilial=pd.codfilialsc and rc.codsecao=pd.codsecao " );
			sql.append( "where " );
			sql.append( "pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd and  pd.codprod=iv.codprod and " );
			sql.append( "iv.codemp=? and iv.codfilial=? and iv.codvenda=? and iv.tipovenda=? " );
			sql.append( "group by pd.codsecao,rc.perccomisgeral " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDREGRACOMIS" ) );
			ps.setInt( 3, getCodvenda() );
			ps.setString( 4, getTipovenda() );
			
			rs = ps.executeQuery();
			
			while( rs.next() ) {
		
				SecaoComissao sessaocomissao = new SecaoComissao();
				
				sessaocomissao.setCodsecao( rs.getString( "codsecao" ) );
				sessaocomissao.setPerccomisgeral( rs.getBigDecimal( "perccomisgeral" ) );
				sessaocomissao.setValorcomisgeral( rs.getBigDecimal( "vlrcomissaogeral" ) );
				
				lista_secao_comissaop.addElement( sessaocomissao );
				
			}
			
			setListaSecaocomissao( lista_secao_comissaop );
			
			con.commit();
			rs.close();
			ps.close();
					
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void carregaCommisionadosComissao() {
		
 		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<ComissionadoComissao> lista_comissionados_comissaop = new Vector<ComissionadoComissao>();
		
		try {
			
			sql.append( "select irc.codvend, irc.perccomisitrc/100 perccomisitrc ");
			sql.append( "from vditregracomis irc, vdregracomis rc ");
			sql.append( "where irc.codemp=rc.codemp and irc.codfilial=rc.codfilial and irc.codregrcomis=rc.codregrcomis ");
			sql.append( "and rc.codempsc=? and rc.codfilialsc=? and rc.codsecao=? and irc.perccomisitrc > 0 " );
			
			ps = con.prepareStatement( sql.toString() );
			
			Vector<SecaoComissao> lista_secoescomissao = getListaSecaoComissao();
			
			for ( int i = 0; lista_secoescomissao.size()>i ; i++) {

				SecaoComissao secaoComissao = (SecaoComissao) lista_secoescomissao.elementAt( i );
				
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "EQSECAO" ) );
				ps.setString( 3, secaoComissao.getCodsecao() );
				
				System.out.println("Sessao " + secaoComissao.getCodsecao() + " localizada...");
				System.out.println("% Geral" + secaoComissao.getPerccomisgeral()) ;
				System.out.println("Vlr Geral" + secaoComissao.getValorcomisgeral()) ;
				
				rs = ps.executeQuery();

				while( rs.next() ) {
					
					
					Integer codvend = rs.getInt( "codvend" ); 
					
					String codsecao = secaoComissao.getCodsecao(); 
					
					ComissionadoComissao comissionadocomissaop = new ComissionadoComissao();
					
					/** Calculando o valor da comissao **/
					
					BigDecimal valorcomissaosecao = secaoComissao.getValorcomisgeral();
					
					BigDecimal perccomis = rs.getBigDecimal( "perccomisitrc" );
					
					BigDecimal vlrcomis = valorcomissaosecao.multiply( perccomis );
										
					/****************************/
					
					comissionadocomissaop.setCodvend( codvend );
					
					comissionadocomissaop.setCodsecao( codsecao );
					
					comissionadocomissaop.setPerccomis( perccomis );
					
					comissionadocomissaop.setVlrcomis( vlrcomis );
					
					lista_comissionados_comissaop.addElement( comissionadocomissaop );
					
					System.out.println("Vendedor " + codvend + " adicionado");
					System.out.println("% Comissao " + perccomis );
					System.out.println("Vlr Comissao " + vlrcomis );
				
				}

			}
			
			con.commit();
			rs.close();
			ps.close();
	
			setListaComissionadoValorComissao( lista_comissionados_comissaop );
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void limpaTabelaComissoes() {
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		
		try {
			
			sql.append( "delete from vdcomissao ");
			sql.append( "where codempve=? and codfilialve=? and codvenda=? and tipovenda=? and tipocomi=? ");
		
			int iparam = 1;
				
			ps = con.prepareStatement( sql.toString() );
				
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setInt( iparam++, getCodvenda() ); 
			ps.setString( iparam++, getTipovenda() );
			ps.setString( iparam++, TIPO_COMISSAO_ESP.getValue().toString() );
				
			ps.execute();
				
			con.commit();
			ps.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void geraTabelaComissoes() {
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		
		try {
			
			sql.append( "insert into vdcomissao ( " );
			sql.append( "codemp, codfilial, codcomi, tipocomi, ");
			sql.append( "vlrvendacomi, vlrcomi, datacomi, dtvenccomi, statuscomi, ");
			sql.append( "codempvd, codfilialvd, codvend, ");
			sql.append( "codempve, codfilialve, codvenda, tipovenda ) ");
			sql.append( "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ) ");
			
			Vector<ComissionadoComissao> lista_comissionados_valor_comissaop = getListaComissionadoValorComissao();
			
			int iparam;
			
			for( int i=0; lista_comissionados_valor_comissaop.size() > i; i++ ) {
				
				ComissionadoComissao comissionado_comissaop = lista_comissionados_valor_comissaop.elementAt( i );
				
				iparam = 1;
				
				ps = con.prepareStatement( sql.toString() );
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
				ps.setInt( iparam++, geraNovoCodComi() );
				ps.setString( iparam++, TIPO_COMISSAO_ESP.getValue().toString() );
				
				ps.setBigDecimal( iparam++, getVlrvendacomi() );
				ps.setBigDecimal( iparam++, comissionado_comissaop.getVlrcomis() );
				
				ps.setDate( iparam++, Funcoes.dateToSQLDate( getDatacomi() ) );
				ps.setDate( iparam++, Funcoes.dateToSQLDate( getDtvenccomi() ) );
				ps.setString( iparam++, STATUS_COMISSAO_ABERTO.getValue().toString() );
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
				ps.setInt( iparam++, comissionado_comissaop.getCodvend() );
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setInt( iparam++, getCodvenda() );
				ps.setString( iparam++, getTipovenda() );
				
				ps.execute();
				
				con.commit();
				ps.close();
				
			}
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Integer geraNovoCodComi() {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer ret = null;
		
		try {
			
			sql.append( "select coalesce(max(codcomi),0) + 1 codcomi from vdcomissao where codemp=? and codfilial=? " );
			
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
			
			rs = ps.executeQuery();

			if( rs.next() ) {
					
				ret = rs.getInt( "codcomi" ); 

			}
			else {
				ret = 1;
			}
			
			con.commit();
			rs.close();
			ps.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return ret;
	}

		
	public DbConnection getCon() {
	
		return con;
	}

	
	public void setCon( DbConnection con ) {
	
		this.con = con;
	}

	
	public Integer getCodvenda() {
	
		return codvenda;
	}

	
	public void setCodvenda( Integer codvenda ) {
	
		this.codvenda = codvenda;
	}

	
	public String getTipovenda() {
	
		return tipovenda;
	}

	
	public void setTipovenda( String tipovenda ) {
	
		this.tipovenda = tipovenda;
	}
	
	public Vector<ComissionadoComissao> getListaComissionadoValorComissao() {
	
		return lista_comissionado_valor_comissao;
	}

	
	public void setListaComissionadoValorComissao( Vector<ComissionadoComissao> lista_comissionado_valor_comissaop ) {
	
		this.lista_comissionado_valor_comissao = lista_comissionado_valor_comissaop;
	}

	public Vector<SecaoComissao> getListaSecaoComissao() {
	
		return lista_secao_comissao;
	}

	
	public void setListaSecaocomissao( Vector<SecaoComissao> listaSecaocomissaop ) {
	
		lista_secao_comissao = listaSecaocomissaop;
	}

	
	public BigDecimal getVlrvendacomi() {
	
		return vlrvendacomi;
	}

	
	public void setVlrvendacomi( BigDecimal vlrvendacomi ) {
	
		this.vlrvendacomi = vlrvendacomi;
	}

	
	public Date getDatacomi() {
	
		return datacomi;
	}

	
	public void setDatacomi( Date datacomi ) {
	
		this.datacomi = datacomi;
	}

	
	public Date getDtvenccomi() {
	
		return dtvenccomi;
	}

	
	public void setDtvenccomi( Date dtvenccomi ) {
	
		this.dtvenccomi = dtvenccomi;
	}

}
