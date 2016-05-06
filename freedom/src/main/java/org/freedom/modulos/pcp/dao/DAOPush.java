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
import java.util.Date;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.pcp.business.object.PPGeraOP;

public class DAOPush extends AbstractDAO {

	public DAOPush( DbConnection cn) {
		super( cn );
	}

	public ResultSet carregaItens(Integer codemp, Integer codfilial, Integer codprod, 
			Integer codempsc, Integer codfilialsc, String codsecao) throws SQLException{

		StringBuilder sql = new StringBuilder();

		sql.append( "select pd.codemp codemppd, pd.codfilial codfilialpd, pd.codprod, pd.refprod, es.seqest, pd.descprod, pd.qtdminprod, pd.sldprod qtdestoque, ");

		// RMA não atendidas
		StringBuilder qtdrma = new StringBuilder();
		qtdrma.append( "coalesce( ");			
		qtdrma.append( "(select sum(ir.qtdaprovitrma - ir.qtdexpitrma) from eqitrma ir ");
		qtdrma.append( "where ir.codemppd=pd.codemp and ir.codfilial=pd.codfilial and ir.codprod=pd.codprod ");
		qtdrma.append( "and ir.qtdaprovitrma > 0 and ir.sitaprovitrma in ('AP','AT') ");
		qtdrma.append( "and ir.qtdexpitrma < ir.qtdaprovitrma) ");
		qtdrma.append( ",0)") ;

		sql.append( qtdrma );
		sql.append( " qtdreq, ");

		// Ops não finalizadas
		sql.append( "coalesce( ");
		sql.append( "(select sum(op.qtdprevprodop) from ppop op ");
		sql.append( "where op.codemppd=pd.codemp and op.codfilial=pd.codfilial and op.codprod=pd.codprod ");
		sql.append( "and op.sitop='PE'), 0 ) qtdemprod ");

		sql.append( "from eqproduto pd, ppestrutura es ");
		sql.append( "where ");
		sql.append( "pd.ativoprod='S' and ((pd.sldprod < pd.qtdminprod) or (pd.sldprod < ");
		sql.append( qtdrma );
		sql.append( ")) and pd.tipoprod in ('F','06') and pd.codemp=? and pd.codfilial=? and ");

		sql.append( "es.codemp=pd.codemp and es.codfilial=pd.codfilial and es.codprod = pd.codprod ");


		if (codprod > 0) {
			sql.append( " and pd.codprod=? ");
		}

		if ((null != codsecao) && (! "".equals(codsecao))) {
			sql.append( " and pd.codempsc=? and pd.codfilialsc=? and pd.codsecao=? ");
		}

		StringBuffer status = new StringBuffer( "" );

		System.out.println( "SQL:" + sql.toString() );

		PreparedStatement ps = getConn().prepareStatement( sql.toString() );

		int iparam = 1;

		ps.setInt( iparam++, codemp );
		ps.setInt( iparam++, codfilial );

		if (codprod > 0) {
			ps.setInt(iparam++, codprod);
		}
		if ((null != codsecao) && (! "".equals(codsecao))) {
			ps.setInt( iparam++, codempsc );
			ps.setInt( iparam++, codfilialsc );
			ps.setString( iparam++, codsecao );
		}

		ResultSet rs = ps.executeQuery();

		return rs;
	}


	public ResultSet geraOP(PPGeraOP gerarOp) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "select codopret,seqopret " );
		sql.append( "from ppgeraop(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );
		PreparedStatement ps = null;
		ResultSet rs = null;

		ps = getConn().prepareStatement( sql.toString() );

		ps.setString( PPGeraOP.PROCEDUREOP.TIPOPROCESS.ordinal(), "C" );//x
		ps.setInt( PPGeraOP.PROCEDUREOP.CODEMPOP.ordinal(), gerarOp.getCodempop());//x
		ps.setInt( PPGeraOP.PROCEDUREOP.CODFILIALOP.ordinal(), gerarOp.getCodfilialop());//x
		ps.setNull( PPGeraOP.PROCEDUREOP.CODOP.ordinal(), Types.INTEGER );//x
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
		ps.setDate( PPGeraOP.PROCEDUREOP.DTFABROP.ordinal(), Funcoes.dateToSQLDate(gerarOp.getDtFabOp()));
		ps.setInt( PPGeraOP.PROCEDUREOP.SEQEST.ordinal(), gerarOp.getSeqest());
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

		rs = ps.executeQuery();

		return rs;
	}

	public void insertPPProcessaOpTmp(Date dtfabrop, BigDecimal qtdaprod, Integer codempet, Integer codfilialet, Integer codest) throws SQLException {

		StringBuilder sql = new StringBuilder( "" );
		PreparedStatement ps = null;

		sql.append( "insert into ppprocessaoptmp (codemp, codfilial, codorc, coditorc, tipoorc, dtfabrop, qtdaprod, codempet, codfilialet, codest) " );
		sql.append( "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" );
		ps = getConn().prepareStatement( sql.toString() );

		ps.setDate( 6, Funcoes.dateToSQLDate( dtfabrop));
		ps.setBigDecimal( 7, qtdaprod);
		ps.setInt( 8, codempet);
		ps.setInt( 9, codfilialet);
		ps.setInt( 10, codest);

		ps.execute();
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

	public boolean comRef() throws SQLException {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;


		ps = Aplicativo.getInstace().getConexao().prepareStatement( sSQL );

		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

		rs = ps.executeQuery();

		if ( rs.next() )
			if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
				bRetorno = true;

		ps.close();
		rs.close();
		return bRetorno;
	}

}


