/**
 * @version 22/05/2013 <BR>
 * @author Setpoint Informática Ltda./Bruno Nascimento<BR>
 *  
 *          Projeto: Freedom <BR>
 * 
 *          Pacote: org.freedom.modulos.pcp.dao <BR>
 *          Classe: @(#)DAOEstrutura.java <BR>
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
 *                  
 * 
 */


package org.freedom.modulos.pcp.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;

public class DAOEstrutura extends AbstractDAO {

	public DAOEstrutura( DbConnection cn) {
		super( cn );
	}


	public void copiarEstrutura(Integer codemp, Integer codfilial, Integer seqest, Integer codprod, Integer codprodcopiado) throws SQLException{

		StringBuilder sql = new StringBuilder();
		int param = 1;
		
		sql.append( "insert into ppestrutura (codemp, codfilial, codprod, seqest, descest, qtdest, refprod, ativoest, codempml, codfilialml, ");
		sql.append( "codmodlote, nrodiasvalid, gloteopp, usadensidadeop, observacao, estdinamica, despauto, bloqqtdprod, expedirrma, gerarop) select codemp, codfilial, ");
		sql.append( codprod);
		sql.append( ", seqest, descest, qtdest, refprod, 'N', codempml, codfilialml, ");
		sql.append( "codmodlote, nrodiasvalid, gloteopp, usadensidadeop, observacao, estdinamica, despauto, bloqqtdprod, expedirrma, gerarop from ppestrutura ");
		sql.append( "where codemp=? and codfilial=? and seqest=? and codprod=? ");
		
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setInt( param++, seqest );
		ps.setInt( param++, codprodcopiado );
		
		ps.execute();
		param = 1;
		
		// INSERT NA TABELA PPESTRUTUFASE
		StringBuilder sqlFase = new StringBuilder();
		sqlFase.append( "insert into ppestrufase (codemp, codfilial, codprod, seqest, seqef, codempfs, codfilialfs, codfase, codemptr, ");
		sqlFase.append( "codfilialtr, codtprec, tempoef, finalizaop, instrucoes) ");
		sqlFase.append( "select codemp, codfilial,");
		sqlFase.append( codprod);
		sqlFase.append( ",seqest, seqef, codempfs, codfilialfs, codfase, codemptr, ");
		sqlFase.append( "codfilialtr, codtprec, tempoef, finalizaop, instrucoes from ppestrufase ");
		sqlFase.append( "where codemp=? and codfilial=? and seqest=? and codprod=? ");
		
		PreparedStatement psFase = getConn().prepareStatement( sqlFase.toString() );
		psFase.setInt( param++, codemp );
		psFase.setInt( param++, codfilial );
		psFase.setInt( param++, seqest );
		psFase.setInt( param++, codprodcopiado );
		
		psFase.execute();
		param = 1;
		
		// INSERT NA TABELA PPITESTRUTURA
		StringBuilder sqlItEstrutura = new StringBuilder();
		sqlItEstrutura.append( "insert into ppitestrutura (codemp, codfilial, codprod, seqest, seqitest, refprod, codemppd, codfilialpd, codprodpd, ");
		sqlItEstrutura.append( "refprodpd, codempfs, codfilialfs, codfase, seqef, qtditest, rmaautoitest, cprova, qtdvariavel, qtdfixa, permiteajusteitest, ");
		sqlItEstrutura.append( "tipoexterno) ");
		sqlItEstrutura.append( "select codemp, codfilial,");
		sqlItEstrutura.append( codprod);
		sqlItEstrutura.append( ", seqest, seqitest, refprod, codemppd, codfilialpd, codprodpd, ");
		
		sqlItEstrutura.append( "refprodpd, codempfs, codfilialfs, codfase, seqef, qtditest, rmaautoitest, cprova, qtdvariavel, qtdfixa, permiteajusteitest, ");
		sqlItEstrutura.append( "tipoexterno from ppitestrutura where  codemp=? and codfilial=? and seqest=? and codprod=? ");
		
		PreparedStatement psItEstrutura = getConn().prepareStatement( sqlItEstrutura.toString() );
		psItEstrutura.setInt( param++, codemp );
		psItEstrutura.setInt( param++, codfilial );
		psItEstrutura.setInt( param++, seqest );
		psItEstrutura.setInt( param++, codprodcopiado );

		psItEstrutura.execute();
		param = 1;

		// INSERT NA TABELA PPDISTRIB
		StringBuilder sqlDistrib = new StringBuilder();
		sqlDistrib.append( "insert into ppdistrib (codemp, codfilial, codprod, seqest, seqef, codempfs, codfilialfs, codfase, seqde, codempde, codfilialde, ");
		sqlDistrib.append( "codprodde, seqestde) ");
		sqlDistrib.append( "select codemp, codfilial,  ");
		sqlDistrib.append( codprod);
		sqlDistrib.append( ", seqest, seqef, codempfs, codfilialfs, codfase, seqde, codempde, codfilialde, codprodde, seqestde from ppdistrib ");
		sqlDistrib.append( "where  codemp=? and codfilial=? and seqest=? and codprod=? ");
		
		PreparedStatement psDistrib = getConn().prepareStatement( sqlDistrib.toString() );
		psDistrib.setInt( param++, codemp );
		psDistrib.setInt( param++, codfilial );
		psDistrib.setInt( param++, seqest );
		psDistrib.setInt( param++, codprodcopiado );
		
		psDistrib.execute();
		param = 1;

		// INSERT NA TABELA PPESTRUTURASUBPROD
		StringBuilder sqlSubProd = new StringBuilder();
		sqlSubProd.append( "insert into ppitestruturasubprod (codemp, codfilial, codprod, seqest, seqitestsp, refprod, codemppd, codfilialpd, codprodpd, ");
		sqlSubProd.append( "refprodpd, codempfs, codfilialfs, codfase, seqef, qtditestsp, qtdvariavel, qtdfixa) ");
		sqlSubProd.append( "select codemp, codfilial, ");
		
		sqlSubProd.append( codprod);
		
		sqlSubProd.append(", seqest, seqitestsp, refprod, codemppd, codfilialpd, codprodpd, ");
		sqlSubProd.append( "refprodpd, codempfs, codfilialfs, codfase, seqef, qtditestsp, qtdvariavel, qtdfixa from ppitestruturasubprod ");
		sqlSubProd.append( "where codemp=? and codfilial=? and seqest=? and codprod=? ");

		PreparedStatement psSubProd = getConn().prepareStatement( sqlSubProd.toString() );
		psSubProd.setInt( param++, codemp );
		psSubProd.setInt( param++, codfilial );
		psSubProd.setInt( param++, seqest );
		psSubProd.setInt( param++, codprodcopiado );
		
		psSubProd.execute();
		
		getConn().commit();
		
		ps.close();
		psFase.close();
		psItEstrutura.close();
		psDistrib.close();
		psSubProd.close();
	}

}


