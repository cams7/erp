/**
 * @version 22/04/2013 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento<BR>
 *  
 *          Projeto: Freedom <BR>
 * 
 *          Pacote: org.freedom.modulos.pcp.dao <BR>
 *          Classe: @(#)DAOPull.java <BR>
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
 *                  DAO que possui a responsabilidade sobre o Sistema de produção Puxada ( Pull System ).
 * 
 */


package org.freedom.modulos.pcp.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.pcp.business.object.PPGeraOP;

public class DAOPull extends AbstractDAO {

	public DAOPull( DbConnection cn) {
		super( cn );
	}

	/*private void montaGridAgrup(String agrupProd, String agrupCli, String agrupDataAprov, String agrupDataProd,
			int codempcl, int codfilialcl, int codcli, int codemppd, int codfilialpd, int codprod) {

		try {

			if ( "N".equals( agrupProd ) && "N".equals( agrupCli ) && "N".equals( agrupDataAprov ) && "N".equals( agrupDataProd ) ) {

			//	Funcoes.mensagemInforma( this, "Deve haver ao menos um critério de agrupamento!" );
				return;
			}

			StringBuilder sql = new StringBuilder();

			sql.append( "select " );

			sql.append( "pd.codemp codemppd, pd.codfilial codfilialpd, pd.codprod, pd.descprod, coalesce(sp.sldliqprod,0) qtdestoque, " );
			sql.append( "sum(coalesce(po.qtdaprod,0)) qtdaprod, sum(coalesce(op.qtdprevprodop,0)) qtdemprod, " );
			sql.append( "(select first 1 pe.seqest from ppestrutura pe where pe.codemp=pd.codemp and pe.codfilial=pd.codfilial and pe.codprod=pd.codprod) seqest " );

			int igroup = 8; // Numero padrão de campos

			if ("S".equals(agrupDataProd)) {
				sql.append( ",io.dtaprovitorc dataaprov " );
				igroup++; // Indica que foi adicionado um novo campo que deve fazer parte do groupby
			}

			if ("S".equals(agrupDataProd)) {
				sql.append( ",po.dtfabrop " );
				igroup++;// Indica que foi adicionado um novo campo que deve fazer parte do groupby
			}

			if ("S".equals(agrupCli ) ) {
				sql.append( ",cl.codemp codempcl, cl.codfilial codfilialcl, cl.codcli, cl.razcli " );
				igroup+=4;// Indica que foi adicionado um novo campo que deve fazer parte do group by
			}

			sql.append( "from vdorcamento oc left outer join vditorcamento io on " );
			sql.append( "io.codemp=oc.codemp and io.codfilial=oc.codfilial and io.codorc=oc.codorc and io.tipoorc=oc.tipoorc " );
			sql.append( "left outer join vdcliente cl on cl.codemp=oc.codempcl and cl.codfilial=oc.codfilialcl and cl.codcli=oc.codcli " );
			sql.append( "left outer join eqproduto pd on pd.codemp=io.codemppd and pd.codfilial=io.codfilialpd and pd.codprod=io.codprod " );
			sql.append( "left outer join ppop op on op.codemppd=pd.codemp and op.codfilial=pd.codfilial and op.codprod=pd.codprod and op.sitop in ('PE','BL') " );
			sql.append( "left outer join eqsaldoprod sp on sp.codemp=pd.codemp and sp.codfilial=pd.codfilial and sp.codprod=pd.codprod " );
			sql.append( "inner join ppprocessaoptmp po on " );
			sql.append( "po.codemp=io.codemp and po.codfilial=io.codfilial and po.codorc=io.codorc and po.tipoorc=io.tipoorc and po.coditorc=io.coditorc " );

			sql.append( "where oc.codemp=? and oc.codfilial=? and io.aprovitorc='S' and io.sitproditorc='PE' and pd.tipoprod='F' " );

			if (codprod > 0) {
				sql.append( " and io.codemppd=? and io.codfilialpd=? and io.codprod=? " );
			}
			if (codcli > 0) {
				sql.append( " and oc.codempcl=? and oc.codfilialcl=? and oc.codcli=? " );
			}

			if ("S".equals(agrupProd) || "S".equals(agrupCli) || "S".equals(agrupDataAprov) || "S".equals(agrupDataProd) ) {

				sql.append( "group by " );

				if ("S".equals(agrupProd)) {
					sql.append( "1, 2, 3, 4, 5 " );
				}

				if (igroup > 8) {
					for (int i = 8; i < igroup; i++) {
						sql.append( "," + ( i + 1 ) );
					}
				}

			}

			System.out.println( "SQL:" + sql.toString() );

			PreparedStatement ps = getConn().prepareStatement( sql.toString() );

			int iparam = 1;

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );

			if ( codprod > 0 ) {
				ps.setInt( iparam++, codemppd);
				ps.setInt( iparam++, codfilialpd );
				ps.setInt( iparam++, codprod);
			}
			if ( codcli > 0 ) {
				ps.setInt( iparam++, codempcl );
				ps.setInt( iparam++, codfilialcl );
				ps.setInt( iparam++, codcli );
			}

			ResultSet rs = ps.executeQuery();

			//tabAgrup.limpa();

			//int row = 0;

			// BigDecimal totqtdaprov = new BigDecimal(0);
			BigDecimal totqtdestoq = new BigDecimal( 0 );
			BigDecimal totqtdemprod = new BigDecimal( 0 );
			BigDecimal totqtdaprod = new BigDecimal( 0 );

			sql = new StringBuilder();
			sql.append( "select coalesce(sum(io2.qtdaprovitorc),0) qtdreservado from ppopitorc oo, vditorcamento io2 where " );
			sql.append( "oo.codempoc=io2.codemp and oo.codfilialoc=io2.codfilial " );
			sql.append( "and oo.codorc=io2.codorc and oo.coditorc=io2.coditorc and oo.tipoorc=io2.tipoorc " );
			sql.append( "and io2.codemp=? and io2.codfilial=? " );
			sql.append( "and io2.codemppd=? and io2.codfilialpd=? and io2.codprod=? " );
			sql.append( "and io2.sitproditorc='PD' and coalesce(io2.statusitorc,'PE')!='OV' " );

			ResultSet rs2 = null;

			PreparedStatement ps2 = null;

			ArrayList<Agrupamento> agrupamentos = new ArrayList<Agrupamento>();;

			while ( rs.next() ) {
				Agrupamento agrupamento = new Agrupamento();
				ps2 = getConn().prepareStatement( sql.toString() );

				ps2.setInt( 1, Aplicativo.iCodEmp );
				ps2.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
				ps2.setInt( 3, Aplicativo.iCodEmp );
				ps2.setInt( 4, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
				ps2.setInt( 5, rs.getInt( Agrupamento.AGRUPAMENTO.CODPROD.toString() ) );

				rs2 = ps2.executeQuery();

				BigDecimal qtdreservado = new BigDecimal( 0 );

				if ( rs2.next() ) {
					qtdreservado = rs2.getBigDecimal( Agrupamento.AGRUPAMENTO.QTDRESERVADO.toString() ).setScale( Aplicativo.casasDec );
				}

				agrupamento.setMarcacao( new Boolean( true ) );
				agrupamento.setCodemppd(rs.getInt(Agrupamento.AGRUPAMENTO.CODEMPPD.toString()));
				agrupamento.setCodfilialpd(rs.getInt(Agrupamento.AGRUPAMENTO.CODFILIALPD.toString()));
				agrupamento.setCodprod(rs.getInt(Agrupamento.AGRUPAMENTO.CODPROD.toString()));
				agrupamento.setSeqest(rs.getInt(Agrupamento.AGRUPAMENTO.SEQEST.toString()));
				agrupamento.setDescprod(rs.getString( Agrupamento.AGRUPAMENTO.DESCPROD.toString().trim()));

				if ( "S".equals( agrupDataAprov ) ) {
				//	tabAgrup.setColunaVisivel( 60, Agrupamento.AGRUPAMENTO.DATAAPROV.ordinal() );
				//	tabAgrup.setValor(  , row, Agrupamento.AGRUPAMENTO.DATAAPROV.ordinal() );
					agrupamento.setDataaprov( Funcoes.sqlDateToDate(rs.getDate( Agrupamento.AGRUPAMENTO.DATAAPROV.toString() ) ) );
				}
				else {
					//tabAgrup.setColunaInvisivel( Agrupamento.AGRUPAMENTO.DATAAPROV.ordinal() );
				}

				if ( "S".equals( agrupDataProd ) ) {
					//tabAgrup.setValor( Funcoes.dateToStrDate( rs.getDate( Agrupamento.AGRUPAMENTO.DTFABROP.toString() ) ), row, Agrupamento.AGRUPAMENTO.DTFABROP.ordinal() );
					agrupamento.setDtfabrop(Funcoes.sqlDateToDate(rs.getDate(Agrupamento.AGRUPAMENTO.DTFABROP.toString()))); 
				}
				else {
					//tabAgrup.setValor( Funcoes.dateToStrDate( new Date() ), row, AGRUPAMENTO.DTFABROP.ordinal() );
					agrupamento.setDtfabrop(new Date());
				}

				if ( "S".equals( agrupCli ) ) {
					agrupamento.setCodempcl(rs.getInt( Agrupamento.AGRUPAMENTO.CODEMPCL.toString()));
					agrupamento.setCodfilialcl(rs.getInt( Agrupamento.AGRUPAMENTO.CODFILIALCL.toString()));
					agrupamento.setCodcli(rs.getInt(Agrupamento.AGRUPAMENTO.CODCLI.toString()));
					agrupamento.setRazcli(rs.getString(Agrupamento.AGRUPAMENTO.RAZCLI.toString().trim()));
				}

				//tabAgrup.setValor( imgColuna, row, Agrupamento.AGRUPAMENTO.STATUS.ordinal() );
				agrupamento.setQtdestoque(rs.getBigDecimal(Agrupamento.AGRUPAMENTO.QTDESTOQUE.toString()).setScale(Aplicativo.casasDec));
				agrupamento.setQtdemprod(rs.getBigDecimal(Agrupamento.AGRUPAMENTO.QTDEMPROD.toString()).setScale(Aplicativo.casasDec));
				agrupamento.setQtdaprod( rs.getBigDecimal( Agrupamento.AGRUPAMENTO.QTDAPROD.toString() ).setScale( Aplicativo.casasDec ) );
				agrupamento.setQtdreservado(  rs.getBigDecimal( Agrupamento.AGRUPAMENTO.QTDRESERVADO.toString() ).setScale( Aplicativo.casasDec ) );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}*/
	public StringBuilder montaGrid(String pend, String emproducao, String produto, int codcli, int codprod) {

		StringBuilder sql = new StringBuilder();
		sql.append( "select " );

		sql.append( "oc.statusorc status, io.sitproditorc, io.dtaprovitorc dataaprov, " );
		sql.append( "cast('today' as date) dtfabrop, " );
		sql.append( "io.dtaprovitorc + coalesce(oc.prazoentorc,0) dataentrega, oc.codemp codempoc, " );
		sql.append( "oc.codfilial codfilialoc, oc.codorc, " );
		sql.append( "io.coditorc, io.tipoorc ,cl.codcli, " );
		sql.append( "cl.razcli, io.codemppd, io.codfilialpd, pd.codprod, pe.seqest, " );
		sql.append( "pd.descprod, coalesce(io.qtdaprovitorc,0) qtdaprov, pi.codop, pi.seqop, " );

		sql.append( "sum(coalesce(sp.sldliqprod,0)) qtdestoque , " );

		sql.append( "sum(coalesce(op.qtdprevprodop,0)) qtdemprod " );

		sql.append( "from vdorcamento oc " );

		sql.append( "left outer join vditorcamento io on " );
		sql.append( "io.codemp=oc.codemp and io.codfilial=oc.codfilial and io.codorc=oc.codorc and io.tipoorc=oc.tipoorc " );

		sql.append( "left outer join vdcliente cl on " );
		sql.append( "cl.codemp=oc.codempcl and cl.codfilial=oc.codfilialcl and cl.codcli=oc.codcli " );

		sql.append( "left outer join eqproduto pd on " );
		sql.append( "pd.codemp=io.codemppd and pd.codfilial=io.codfilialpd and pd.codprod=io.codprod " );

		sql.append( "left outer join ppop op on " );
		sql.append( "op.codemppd=pd.codemp and op.codfilial=pd.codfilial and op.codprod=pd.codprod and op.sitop in ('PE','BL') " );

		sql.append( "left outer join eqsaldoprod sp on " );
		sql.append( "sp.codemp=pd.codemp and sp.codfilial=pd.codfilial and sp.codprod=pd.codprod " );

		sql.append( "left outer join ppestrutura pe on " );
		sql.append( "pe.codemp=pd.codemp and pe.codfilial=pd.codfilial and pe.codprod=pd.codprod " );

		sql.append( "left outer join ppopitorc pi on " );
		sql.append( "pi.codempoc=io.codemp and pi.codfilialoc=io.codfilial and pi.codorc=io.codorc and pi.coditorc=io.coditorc and pi.tipoorc=io.tipoorc " );

		sql.append( "where oc.codemp=? and oc.codfilial=? and io.aprovitorc='S' and pd.tipoprod='F' " );

		StringBuffer status = new StringBuffer( "" );

		if ( "S".equals(pend) ) {
			status.append( " 'PE' " );
		}
		if ( "S".equals(emproducao) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'EP'" );
		}
		if ( "S".equals(produto)) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'PD'" );
		}

		if ( status.length() > 0 ) {
			sql.append( " and io.sitproditorc in (" );
			sql.append( status );
			sql.append( ") " );
		}
		else {
			sql.append( " and io.sitproditorc not in('PE','EP','PD') " );
		}

		if ( codprod > 0 ) {
			sql.append( " and io.codemppd=? and io.codfilialpd=? and io.codprod=? " );
		}
		if ( codcli > 0 ) {
			sql.append( " and oc.codempcl=? and oc.codfilialcl=? and oc.codcli=? " );
		}

		sql.append( " and oc.dtorc between ? and ? group by 1,2,3,4,5,6,7,16,8,9,10,11,12,13,14,15,17,18,19,20 " );


		System.out.println( "SQL:" + sql.toString() );

		return sql;
	}


	public StringBuilder montaGridAgrup(String agrupProd, String agrupCli, String agrupDataAprov, String agrupDataProd,
			int codempcl, int codfilialcl, int codcli, int codemppd, int codfilialpd, int codprod) {

		StringBuilder sql = new StringBuilder();

		sql.append( "select " );

		sql.append( "pd.codemp codemppd, pd.codfilial codfilialpd, pd.codprod, pd.descprod, coalesce(sp.sldliqprod,0) qtdestoque, " );
		sql.append( "sum(coalesce(po.qtdaprod,0)) qtdaprod, sum(coalesce(op.qtdprevprodop,0)) qtdemprod, " );
		sql.append( "(select first 1 pe.seqest from ppestrutura pe where pe.codemp=pd.codemp and pe.codfilial=pd.codfilial and pe.codprod=pd.codprod) seqest " );

		int igroup = 8; // Numero padrão de campos

		if ("S".equals(agrupDataAprov)) {
			sql.append( ",io.dtaprovitorc dataaprov " );
			igroup++; // Indica que foi adicionado um novo campo que deve fazer parte do groupby
		}

		if ("S".equals(agrupDataProd)) {
			sql.append( ",po.dtfabrop " );
			igroup++;// Indica que foi adicionado um novo campo que deve fazer parte do groupby
		}

		if ("S".equals(agrupCli)) {
			sql.append( ",cl.codemp codempcl, cl.codfilial codfilialcl, cl.codcli, cl.razcli " );
			igroup++;// Indica que foi adicionado um novo campo que deve fazer parte do group by
			igroup++;// Indica que foi adicionado um novo campo que deve fazer parte do group by
			igroup++;// Indica que foi adicionado um novo campo que deve fazer parte do group by
			igroup++;// Indica que foi adicionado um novo campo que deve fazer parte do group by
		}

		sql.append( "from vdorcamento oc left outer join vditorcamento io on " );
		sql.append( "io.codemp=oc.codemp and io.codfilial=oc.codfilial and io.codorc=oc.codorc and io.tipoorc=oc.tipoorc " );
		sql.append( "left outer join vdcliente cl on cl.codemp=oc.codempcl and cl.codfilial=oc.codfilialcl and cl.codcli=oc.codcli " );
		sql.append( "left outer join eqproduto pd on pd.codemp=io.codemppd and pd.codfilial=io.codfilialpd and pd.codprod=io.codprod " );
		sql.append( "left outer join ppop op on op.codemppd=pd.codemp and op.codfilial=pd.codfilial and op.codprod=pd.codprod and op.sitop in ('PE','BL') " );
		sql.append( "left outer join eqsaldoprod sp on sp.codemp=pd.codemp and sp.codfilial=pd.codfilial and sp.codprod=pd.codprod " );
		sql.append( "inner join ppprocessaoptmp po on " );
		sql.append( "po.codemp=io.codemp and po.codfilial=io.codfilial and po.codorc=io.codorc and po.tipoorc=io.tipoorc and po.coditorc=io.coditorc " );

		sql.append( "where oc.codemp=? and oc.codfilial=? and io.aprovitorc='S' and io.sitproditorc='PE' and pd.tipoprod='F' " );

		if ( codprod > 0 ) {
			sql.append( " and io.codemppd=? and io.codfilialpd=? and io.codprod=? " );
		}
		if ( codcli > 0 ) {
			sql.append( " and oc.codempcl=? and oc.codfilialcl=? and oc.codcli=? " );
		}

		if ("S".equals(agrupProd) || "S".equals(agrupCli) || "S".equals(agrupDataAprov) || "S".equals(agrupDataProd)) {

			sql.append( "group by " );

			if ( "S".equals(agrupProd) ) {
				sql.append( "1, 2, 3, 4, 5 " );
			}

			if ( igroup > 8 ) {
				for ( int i = 8; i < igroup; i++ ) {
					sql.append( "," + ( i + 1 ) );
				}
			}

		}

		System.out.println( "SQL:" + sql.toString() );
		return sql;
	}


	public ResultSet geraOP(PPGeraOP geraOp) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append( "select codopret,seqopret " );
		sql.append( "from ppgeraop(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );
		PreparedStatement ps = null;
		ps = getConn().prepareStatement( sql.toString() );

		ps.setString( PPGeraOP.PROCEDUREOP.TIPOPROCESS.ordinal(), geraOp.getTipoprocess());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPOP.ordinal(), geraOp.getCodempop());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALOP.ordinal(), geraOp.getCodfilialop());
		ps.setNull( PPGeraOP.PROCEDUREOP.CODOP.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.SEQOP.ordinal(), Types.INTEGER );
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPPD.ordinal(), geraOp.getCodemppd());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALPD.ordinal(), geraOp.getCodfilialpd());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODPROD.ordinal(), geraOp.getCodprod());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPOC.ordinal(), geraOp.getCodempoc());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALOC.ordinal(), geraOp.getCodfilialoc());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODORC.ordinal(), geraOp.getCodorc());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODITORC.ordinal(), geraOp.getCoditorc());
		ps.setString( PPGeraOP.PROCEDUREOP.TIPOORC.ordinal(), geraOp.getTipoorc());
		ps.setBigDecimal( PPGeraOP.PROCEDUREOP.QTDSUGPRODOP.ordinal(), geraOp.getQtdSugProdOp());
		ps.setDate( PPGeraOP.PROCEDUREOP.DTFABROP.ordinal(), Funcoes.dateToSQLDate(geraOp.getDtFabOp()));
		ps.setInt( PPGeraOP.PROCEDUREOP.SEQEST.ordinal(), geraOp.getSeqest());
		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPET.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALET.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODEST.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.AGRUPDATAAPROV.ordinal(), Types.CHAR );
		ps.setNull( PPGeraOP.PROCEDUREOP.AGRUPDTFABROP.ordinal(), Types.CHAR );
		ps.setNull( PPGeraOP.PROCEDUREOP.AGRUPCODCLI.ordinal(), Types.CHAR );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPCL.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALCL.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODCLI.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.DATAAPROV.ordinal(), Types.DATE );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPCP.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALCP.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODCOMPRA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODITCOMPRA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.JUSTFICQTDPROD.ordinal(), Types.CHAR );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPPDENTRADA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALPDENTRADA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODPRODENTRADA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.QTDENTRADA.ordinal(), Types.DECIMAL );

		ResultSet rs = ps.executeQuery();

		return rs;
	}


	public ResultSet geraOPSAgrup(PPGeraOP gerarOp) throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append( "select codopret,seqopret " );
		sql.append( "from ppgeraop(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );

		PreparedStatement ps = null;
		ResultSet rs = null;

		ps = getConn().prepareStatement( sql.toString() );

		ps.setString( PPGeraOP.PROCEDUREOP.TIPOPROCESS.ordinal(), gerarOp.getTipoprocess() );
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPOP.ordinal(), gerarOp.getCodempop());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALOP.ordinal(), gerarOp.getCodfilialop() );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODOP.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.SEQOP.ordinal(), Types.INTEGER );
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPPD.ordinal(), gerarOp.getCodemppd());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALPD.ordinal(), gerarOp.getCodfilialpd());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODPROD.ordinal(), gerarOp.getCodprod());
		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPOC.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALOC.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODORC.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.TIPOORC.ordinal(), Types.CHAR );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODITORC.ordinal(), Types.INTEGER );
		ps.setBigDecimal( PPGeraOP.PROCEDUREOP.QTDSUGPRODOP.ordinal(), gerarOp.getQtdSugProdOp());
		ps.setDate( PPGeraOP.PROCEDUREOP.DTFABROP.ordinal(), Funcoes.dateToSQLDate( gerarOp.getDtFabOp()));
		ps.setInt( PPGeraOP.PROCEDUREOP.SEQEST.ordinal(), gerarOp.getSeqest());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPET.ordinal(), gerarOp.getCodempet());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALET.ordinal(), gerarOp.getCodfilialet());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEST.ordinal(), gerarOp.getCodest());

		ps.setString( PPGeraOP.PROCEDUREOP.AGRUPDATAAPROV.ordinal(), gerarOp.getAgrupdataaprov());
		ps.setDate( PPGeraOP.PROCEDUREOP.DATAAPROV.ordinal(), Funcoes.dateToSQLDate( gerarOp.getDataaprov()));
		ps.setString( PPGeraOP.PROCEDUREOP.AGRUPDTFABROP.ordinal(), gerarOp.getAgrupdtfabrop());
		ps.setString( PPGeraOP.PROCEDUREOP.AGRUPCODCLI.ordinal(), gerarOp.getAgrupcodcli());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPCL.ordinal(), gerarOp.getCodempcl());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALCL.ordinal(), gerarOp.getCodfilialcl());
		ps.setInt( PPGeraOP.PROCEDUREOP.CODCLI.ordinal(), gerarOp.getCodcli() );

		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPCP.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALCP.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODCOMPRA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODITCOMPRA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.JUSTFICQTDPROD.ordinal(), Types.CHAR );

		ps.setNull( PPGeraOP.PROCEDUREOP.CODEMPPDENTRADA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODFILIALPDENTRADA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.CODPRODENTRADA.ordinal(), Types.INTEGER );
		ps.setNull( PPGeraOP.PROCEDUREOP.QTDENTRADA.ordinal(), Types.DECIMAL );
		rs = ps.executeQuery();

		return rs;

	}

	public void deletaTabTemp() {

		StringBuilder sql = new StringBuilder( "" );
		PreparedStatement ps = null;

		try {

			sql.append( "delete from ppprocessaoptmp pt where pt.codempet=? and pt.codfilialet=? and pt.codest=?" );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, Aplicativo.iNumEst );

			ps.execute();
			ps.close();

			getConn().commit();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public StringBuilder queryPPProcessaOpTmp()	{
		StringBuilder sql = new StringBuilder();
		sql.append( "insert into ppprocessaoptmp (codemp, codfilial, codorc, coditorc, tipoorc, dtfabrop, qtdaprod, codempet, codfilialet, codest) " );
		sql.append( "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" );

		return sql;
	}

}


