/**
 * @author Setpoint Informática Ltda./Bruno Nascimento <BR>
 *  
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.dao <BR>
 *         Classe: @(#)DAOMovimento.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Classe com o objetivo de persistir os dados das classes FManutRec e FManutPag
 * 
 */

package org.freedom.modulos.std.dao;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.type.StringDireita;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaRec;
import org.freedom.modulos.fnc.view.dialog.utility.DLBaixaRec.BaixaRecBean;
import org.freedom.modulos.fnc.view.dialog.utility.DLEditaRec.EColRet;
import org.freedom.modulos.fnc.view.frame.utility.FManutRec;
import org.freedom.modulos.std.business.object.ConsultaReceber;

public class DAOMovimento extends AbstractDAO {

	private enum PARAM_INSERT_SL { NONE, CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA,CODEMPCL,CODFILIALCL,CODCLI,
		CODEMPPN, CODFILIALPN, CODPLAN, CODEMPRC, CODFILIALRC, CODREC, NPARCITREC, CODEMPCC, CODFILIALCC, 
		ANOCC, CODCC, ORIGSUBLANCA, DTCOMPSUBLANCA, DATASUBLANCA, DTPREVSUBLANCA, VLRSUBLANCA, TIPOSUBLANCA
	}

	private enum PARAM_UPDATE_IR { NONE, NUMCONTA, CODEMPCA, CODFILIALCA, CODPLAN, CODEMPPN, CODFILIALPN, 
		DOCLANCAITREC, DTPAGOITREC, VLRPAGOITREC, VLRDESCITREC, VLRJUROSITREC, ANOCC, CODCC, CODEMPCC, 
		CODFILIALCC, OBSITREC, STATUSITREC, DTLIGITREC, MULTIBAIXA, ALTUSUITREC, CODREC, NPARCITREC, CODEMP, CODFILIAL
	};

	public DAOMovimento (DbConnection cn) {
		super(cn);
	}

	public Integer pesquisaDocRec(Integer docrec) throws SQLException {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer ret = null;

		sql.append( "select codrec from fnreceber where codemp=? and codfilial=? and docrec=?" );

		ps = getConn().prepareStatement( sql.toString() );

		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		ps.setInt( 3, docrec );

		rs = ps.executeQuery();

		if (rs.next()) {
			ret = rs.getInt( "codrec" );
		}

		rs.close();
		ps.close();
		return ret;
	}

	public Integer pesquisaPedidoRec(Integer codvenda) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer ret = null;

		try {
			sql.append( "select codrec from fnreceber where codemp=? and codfilial=? and codvenda=? and tipovenda='V'" );
			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 3, codvenda );

			rs = ps.executeQuery();

			if (rs.next()) {
				ret = rs.getInt( "codrec" );
			}
			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ps = null;
			rs = null;
		}
		return ret;
	}

	public ResultSet carregaGridConsulta(Integer codemp, Integer codfilial, Integer codempcl, Integer codfilialcl, Integer codcli) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		sql.append( "SELECT IT.DTVENCITREC,IT.STATUSITREC," );
		sql.append( "(SELECT SERIE FROM VDVENDA V " );
		sql.append( "WHERE V.CODEMP=R.CODEMPVA AND V.CODFILIAL=R.CODFILIALVA " );
		sql.append( "AND V.TIPOVENDA=R.TIPOVENDA AND V.CODVENDA=R.CODVENDA) SERIE," );
		sql.append( "R.DOCREC,R.CODVENDA,R.DATAREC,IT.VLRPARCITREC,IT.DTLIQITREC, IT.DTPAGOITREC,IT.VLRPAGOITREC," );
		sql.append( "(CASE WHEN IT.DTLIQITREC IS NULL THEN CAST('today' AS DATE)-IT.DTVENCITREC " );
		sql.append( "ELSE IT.DTLIQITREC - IT.DTVENCITREC END ) DIASATRASO, R.OBSREC," );
		sql.append( "IT.CODBANCO, (SELECT B.NOMEBANCO FROM FNBANCO B " );
		sql.append( "WHERE B.CODBANCO=IT.CODBANCO AND B.CODEMP=IT.CODEMPBO AND B.CODFILIAL=IT.CODFILIALBO) NOMEBANCO," );
		sql.append( "R.CODREC,IT.NPARCITREC,IT.VLRDESCITREC,IT.VLRJUROSITREC,R.TIPOVENDA,IT.VLRAPAGITREC, IT.VLRCANCITREC " );
		sql.append( "FROM FNRECEBER R,FNITRECEBER IT " );
		sql.append( "WHERE R.CODEMPCL=? AND R.CODFILIALCL=? AND R.CODCLI=? AND R.CODEMP=? AND R.CODFILIAL=? AND IT.CODREC=R.CODREC " );
		sql.append( "AND IT.CODEMP=R.CODEMP AND IT.CODFILIAL=R.CODFILIAL " );
		sql.append( "ORDER BY R.CODREC DESC,IT.NPARCITREC DESC" );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codempcl );
		ps.setInt( 2, codfilialcl );
		ps.setInt( 3, codcli );
		ps.setInt( 4, codemp );
		ps.setInt( 5, codfilial );

		return ps.executeQuery();
	}


	public ResultSet carregaGridBaixa(Integer codemp, Integer codfilial, Integer codrecbaixa) throws SQLException {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		sql.append( "SELECT IR.DTVENCITREC,IR.STATUSITREC,R.CODREC,IR.DOCLANCAITREC,R.DOCREC," );
		sql.append( "R.CODVENDA,IR.VLRPARCITREC, IR.DTLIQITREC, IR.DTPAGOITREC,IR.VLRPAGOITREC," );
		sql.append( "IR.VLRAPAGITREC,IR.NUMCONTA,IR.VLRDESCITREC," );
		sql.append( "(SELECT C.DESCCONTA FROM FNCONTA C " );
		sql.append( "WHERE C.NUMCONTA=IR.NUMCONTA " );
		sql.append( "AND C.CODEMP=IR.CODEMPCA AND C.CODFILIAL=IR.CODFILIALCA) DESCCONTA,IR.CODPLAN," );
		sql.append( "(SELECT P.DESCPLAN FROM FNPLANEJAMENTO P " );
		sql.append( "WHERE P.CODPLAN=IR.CODPLAN AND P.CODEMP=IR.CODEMPPN AND P.CODFILIAL=IR.CODFILIALPN) DESCPLAN," );
		sql.append( "IR.CODCC," );
		sql.append( "(SELECT CC.DESCCC FROM FNCC CC " );
		sql.append( "WHERE CC.CODCC=IR.CODCC AND CC.CODEMP=IR.CODEMPCC AND CC.CODFILIAL=IR.CODFILIALCC AND CC.ANOCC=IR.ANOCC) DESCCC," );
		sql.append( "IR.OBSITREC,IR.NPARCITREC,IR.VLRJUROSITREC,IR.DTITREC," );
		sql.append( "(SELECT V.DOCVENDA FROM VDVENDA V " );
		sql.append( "WHERE V.CODEMP=R.CODEMPVA " );
		sql.append( "AND V.CODFILIAL=R.CODFILIALVA AND V.TIPOVENDA=R.TIPOVENDA AND V.CODVENDA=R.CODVENDA) DOCVENDA," );
		sql.append( "IR.CODBANCO, IR.VLRCANCITREC " );
		sql.append( "FROM FNITRECEBER IR,FNRECEBER R  " );
		sql.append( "WHERE IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND IR.CODREC=R.CODREC AND R.CODREC=? AND R.CODEMP=? AND R.CODFILIAL=? " );
		sql.append( "ORDER BY IR.DTVENCITREC,IR.STATUSITREC" );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codrecbaixa );
		ps.setInt( 2, codemp );
		ps.setInt( 3, codfilial );

		return ps.executeQuery();

	}

	public ResultSet getResultSetManut( boolean bAplicFiltros, boolean bordero, boolean renegociveis, boolean validaPeriodo, String rgData, String rgVenc
			, String cbRecebidas, String cbCanceladas, String cbEmBordero, String cbRenegociado, String cbAReceber, String cbEmRenegociacao,
			String cbRecParcial, Integer codCliFiltro, Integer codRecManut, Integer seqNossoNumero, Date dIniManut, Date dFimManut) throws SQLException {

		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		StringBuffer sWhereManut = new StringBuffer();
		StringBuffer sWhereStatus = new StringBuffer();

		if (!validaPeriodo) {
			return null;
		}

		if (bAplicFiltros) {

			sWhereManut.append( " AND " );

			if ("V".equals(rgData)) {
				sWhereManut.append( "IR.DTVENCITREC" );
			} else if ("E".equals(rgData)) {
				sWhereManut.append( "IR.DTITREC" );
			} else {
				sWhereManut.append( "COALESCE(IR.DTPREVITREC,IR.DTVENCITREC)" );
			}

			// sWhereManut.append( rgData.getVlrString().equals( "V" ) ? "IR.DTVENCITREC" : "IR.DTITREC" );
			sWhereManut.append( " BETWEEN ? AND ? AND R.CODEMP=? AND R.CODFILIAL=?" );

			if ("S".equals( cbRecebidas ) || "S".equals( cbAReceber) || "S".equals( cbRecParcial) || 
					"S".equals( cbCanceladas) || "S".equals( cbEmBordero) || "S".equals( cbRenegociado ) || 
					"S".equals( cbEmRenegociacao)) {

				boolean bStatus = false;

				if ("S".equals(cbRecebidas) && !renegociveis) {
					sWhereStatus.append( "IR.STATUSITREC='RP'" );
					bStatus = true;
				}
				if ("S".equals(cbAReceber)) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='R1' " : " IR.STATUSITREC='R1' " );
					bStatus = true;
				}
				if ("S".equals(cbRecParcial)) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RL' " : " IR.STATUSITREC='RL' " );
					bStatus = true;
				}
				if ("S".equals(cbCanceladas) && !renegociveis) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='CR'" : " IR.STATUSITREC='CR' " );
					bStatus = true;
				}
				if ("S".equals(cbEmBordero)) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RB'" : " IR.STATUSITREC='RB' " );
					bStatus = true;
				}
				if ("S".equals(cbRenegociado) && !renegociveis) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RN'" : " IR.STATUSITREC='RN' " );
					bStatus = true;
				}
				if ("S".equals( cbEmRenegociacao) && !renegociveis) {
					sWhereStatus.append( bStatus ? " OR IR.STATUSITREC='RR'" : " IR.STATUSITREC='RR' " );
					bStatus = true;
				}

				sWhereManut.append( " AND (" );
				sWhereManut.append( sWhereStatus );
				sWhereManut.append( ")" );
			} else {
				Funcoes.mensagemInforma( null, "Você deve selecionar ao menos um filtro de status!" );
				return null;
			}

			if (!"TT".equals( rgVenc)) {

				sWhereManut.append( " AND IR.DTVENCITREC" );

				if (rgVenc.equals("VE")) {
					sWhereManut.append( " <'" );
					sWhereManut.append( Funcoes.dateToStrDB( Calendar.getInstance().getTime() ) );
					sWhereManut.append( "'" );
				} else {
					sWhereManut.append( " >='" );
					sWhereManut.append( Funcoes.dateToStrDB( Calendar.getInstance().getTime() ) );
					sWhereManut.append( "'" );
				}
			}
			if (codCliFiltro > 0) {
				sWhereManut.append( " AND R.CODCLI=" );
				sWhereManut.append( codCliFiltro );
			}

			if (bordero) {
				sWhereManut.append( " AND NOT EXISTS (SELECT B.NPARCITREC FROM FNITBORDERO B " );
				sWhereManut.append( "WHERE B.CODEMPRC=IR.CODEMP AND B.CODFILIALRC=IR.CODFILIAL AND " );
				sWhereManut.append( "B.CODREC=IR.CODREC AND B.NPARCITREC=IR.NPARCITREC) " );
			}

			if (renegociveis) {
				sWhereManut.append( " AND NOT EXISTS (SELECT B.NPARCITREC FROM FNITRENEGREC B " );
				sWhereManut.append( "WHERE B.CODEMPIR=IR.CODEMP AND B.CODFILIALIR=IR.CODFILIAL AND " );
				sWhereManut.append( "B.CODREC=IR.CODREC AND B.NPARCITREC=IR.NPARCITREC) " );
			}
		}
		else {
			if (codRecManut > 0) {
				sWhereManut.append( " AND R.CODREC=? ");
			} else {
				sWhereManut.append( " AND " );

				if ("V".equals(rgData)) {
					sWhereManut.append( "IR.DTVENCITREC" );
				} else if ("E".equals(rgData)) {
					sWhereManut.append( "IR.DTITREC" );
				} else {
					sWhereManut.append( "COALESCE(IR.DTPREVITREC,IR.DTVENCITREC)" );
				}

				sWhereManut.append( " BETWEEN ? AND ? " );

			}
			sWhereManut.append( " AND R.CODEMP=? AND R.CODFILIAL=? " );
		}

		sql.append( "SELECT IR.DTVENCITREC,IR.DTPREVITREC,IR.STATUSITREC,R.CODCLI,C.RAZCLI,R.CODREC,IR.DOCLANCAITREC," );
		sql.append( "R.CODVENDA,IR.VLRPARCITREC, IR.DTLIQITREC, IR.DTPAGOITREC,IR.VLRPAGOITREC,IR.VLRAPAGITREC,IR.NUMCONTA," );
		sql.append( "IR.VLRDESCITREC,IR.CODPLAN,IR.CODCC,IR.OBSITREC,IR.NPARCITREC,IR.VLRJUROSITREC," );
		sql.append( "IR.DTITREC,IR.CODBANCO,IR.CODCARTCOB, " );
		sql.append( "(SELECT C.DESCCONTA FROM FNCONTA C " );
		sql.append( "WHERE C.NUMCONTA=IR.NUMCONTA " );
		sql.append( "AND C.CODEMP=IR.CODEMPCA AND C.CODFILIAL=IR.CODFILIALCA) DESCCONTA," );
		sql.append( "(SELECT P.DESCPLAN FROM FNPLANEJAMENTO P " );
		sql.append( "WHERE P.CODPLAN=IR.CODPLAN " );
		sql.append( "AND P.CODEMP=IR.CODEMPPN AND P.CODFILIAL=IR.CODFILIALPN) DESCPLAN," );
		sql.append( "(SELECT CC.DESCCC FROM FNCC CC " );
		sql.append( "WHERE CC.CODCC=IR.CODCC " );
		sql.append( "AND CC.CODEMP=IR.CODEMPCC AND CC.CODFILIAL=IR.CODFILIALCC AND CC.ANOCC=IR.ANOCC) DESCCC," );
		sql.append( "(SELECT VD.DOCVENDA FROM VDVENDA VD " );
		sql.append( "WHERE VD.TIPOVENDA=R.TIPOVENDA AND VD.CODVENDA=R.CODVENDA AND " );
		sql.append( " VD.CODEMP=R.CODEMPVA AND VD.CODFILIAL=R.CODFILIALVA) DOCVENDA," );
		sql.append( "IR.CODTIPOCOB, " );
		sql.append( "(SELECT TP.DESCTIPOCOB FROM FNTIPOCOB TP " );
		sql.append( "WHERE TP.CODEMP=IR.CODEMPTC " );
		sql.append( "AND TP.CODFILIAL=IR.CODFILIALTC AND TP.CODTIPOCOB=IR.CODTIPOCOB) DESCTIPOCOB, " );
		sql.append( "(SELECT BO.NOMEBANCO FROM FNBANCO BO WHERE BO.CODBANCO=IR.CODBANCO " );
		sql.append( "AND BO.CODEMP=IR.CODEMPBO AND BO.CODFILIAL=IR.CODFILIALBO) NOMEBANCO," );
		sql.append( "(SELECT CB.DESCCARTCOB FROM FNCARTCOB CB WHERE CB.CODBANCO=IR.CODBANCO " );
		sql.append( "AND CB.CODEMP=IR.CODEMPBO AND CB.CODFILIAL=IR.CODFILIALBO AND CB.CODCARTCOB=IR.CODCARTCOB) DESCCARTCOB, " );
		sql.append( "R.DOCREC, IR.VLRDEVITREC, IR.DESCPONT, IR.VLRCANCITREC, IR.SEQNOSSONUMERO, " );

		sql.append( "(SELECT FIRST 1 ITR.CODATENDO FROM ATATENDIMENTOITREC ITR " );
		sql.append( "WHERE ITR.CODEMPIR=IR.CODEMP AND ITR.CODFILIALIR=IR.CODFILIAL " );
		sql.append( "AND ITR.CODREC=IR.CODREC AND ITR.NPARCITREC=IR.NPARCITREC ) AS ATEND, " );

		sql.append( "SN.CORSINAL, IR.MULTIBAIXA ");

		sql.append( "FROM FNRECEBER R, VDCLIENTE C, FNITRECEBER IR " );

		sql.append( "LEFT OUTER JOIN FNSINAL SN ON SN.CODEMP=IR.CODEMPSN AND SN.CODFILIAL=IR.CODFILIALSN AND SN.CODSINAL=IR.CODSINAL ");

		sql.append( "WHERE IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND R.CODREC=IR.CODREC AND " );
		sql.append( "C.CODCLI=R.CODCLI AND C.CODEMP=R.CODEMPCL AND C.CODFILIAL=R.CODFILIALCL " );
		sql.append( sWhereManut );

		if (seqNossoNumero>0) {
			sql.append( "and ir.seqnossonumero="  + seqNossoNumero );
		}

		sql.append( " ORDER BY IR.DTVENCITREC,IR.STATUSITREC,IR.CODREC,IR.NPARCITREC" );

		PreparedStatement ps = getConn().prepareStatement( sql.toString() );

		if (bAplicFiltros) {
			ps.setDate( 1, Funcoes.dateToSQLDate( dIniManut ) );
			ps.setDate( 2, Funcoes.dateToSQLDate( dFimManut ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		}
		else {
			int iparam = 1;

			if (codRecManut>0) {
				ps.setInt( iparam++, codRecManut );
			} else {
				ps.setDate( iparam++, Funcoes.dateToSQLDate( dIniManut ) );
				ps.setDate( iparam++, Funcoes.dateToSQLDate( dFimManut ) );
			}

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		}

		rs = ps.executeQuery();

		return rs;
	}

	public ConsultaReceber buscaConsultaReceber(Integer codemp, Integer codfilial, Integer codempcl, Integer codfilialcl, Integer codcli) throws SQLException {
		ConsultaReceber consulta = new ConsultaReceber();
		StringBuilder sql = new StringBuilder();

		//PreparedStatement para a primeira query
		PreparedStatement ps = null;
		ResultSet rs = null;
		//PreparedStatement para a segunda query
		PreparedStatement psmax = null;
		ResultSet rsmax = null;
		//PreparedStatement para a terceira query
		PreparedStatement pssum = null;
		ResultSet rssum = null;

		// Busca totais ...
		sql.append( "select coalesce(sum(ir.vlritrec),0) vlritrec, coalesce(sum(ir.vlrpagoitrec),0) vlrpagoitrec, coalesce(sum(ir.vlrparcitrec),0) vlrparcitrec, ");
		sql.append( "coalesce(sum(ir.vlrapagitrec),0) vlrapagitrec, min(datarec) dataprim, max(datarec) datault " );
		sql.append( "from fnreceber rc, fnitreceber ir " );
		sql.append( "where rc.codemp=ir.codemp and rc.codfilial=ir.codfilial and rc.codrec=ir.codrec and " );
		sql.append( "ir.CODEMP=? AND ir.CODFILIAL=? AND rc.CODEMPCL=? and rc.codfilialcl=? and CODCLI=? " );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codemp );
		ps.setInt( 2, codfilial );
		ps.setInt( 3, codempcl );
		ps.setInt( 4, codfilialcl );
		ps.setInt( 5, codcli );

		rs = ps.executeQuery();

		if (rs.next()) {

			/*		
			txtVlrTotVendLiq.setVlrBigDecimal( rs.getBigDecimal( "vlritrec" ) );
			txtVlrTotPago.setVlrBigDecimal( rs.getBigDecimal( "vlrpagoitrec" ) );
			txtVlrTotVendBrut.setVlrBigDecimal( rs.getBigDecimal( "vlrparcitrec" ) );
			txtVlrTotAberto.setVlrBigDecimal( rs.getBigDecimal( "vlrapagitrec" ) );
			txtPrimCompr.setVlrString( rs.getDate( "dataprim" ) );
			txtUltCompr.setVlrString( rs.getDate( "datault" ) != null ? StringFunctions.sqlDateToStrDate( rs.getDate( "datault" ) ) : "" );
			 */

			consulta.setVlrtotvendliq( rs.getBigDecimal( "vlritrec" ) );
			consulta.setVlrtotpago( rs.getBigDecimal( "vlrpagoitrec" ) );
			consulta.setVlrtotvendbrut( rs.getBigDecimal( "vlrparcitrec" ) );
			consulta.setVlrtotaberto( rs.getBigDecimal( "vlrapagitrec" ) );
			consulta.setPrimcompra( rs.getDate( "dataprim" )  );
			consulta.setUltcompra( rs.getDate( "datault" )  );
		}

		rs.close();
		ps.close();

		getConn().commit();

		// Busca a maior fatura ...
		sql.delete( 0, sql.length() );
		sql.append( "SELECT MAX(VLRREC) VLRREC,DATAREC " );
		sql.append( "FROM FNRECEBER " );
		sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? " );
		sql.append( "GROUP BY DATAREC " );
		sql.append( "ORDER BY 1 DESC" );

		psmax = getConn().prepareStatement( sql.toString() );
		psmax.setInt( 1, codemp );
		psmax.setInt( 2, codfilial );
		psmax.setInt( 3, codcli );

		rsmax = psmax.executeQuery();

		if (rsmax.next()) {
			/*
			txtVlrMaxFat.setVlrString( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs1.getString( 1 ) ) );
			txtDataMaxFat.setVlrString( StringFunctions.sqlDateToStrDate( rs1.getDate( "DATAREC" ) ) );
			 */
			consulta.setVlrmaxfat( rsmax.getBigDecimal( "VLRREC" ) );
			consulta.setDatamaxfat( rsmax.getDate( "DATAREC" ) );
		}


		rsmax.close();
		psmax.close();

		getConn().commit();

		// Busca o maior acumulo ...
		sql.delete( 0, sql.length() );
		sql.append( "SELECT EXTRACT(MONTH FROM DATAREC), SUM(VLRREC), EXTRACT(YEAR FROM DATAREC) " );
		sql.append( "FROM FNRECEBER " );
		sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? " );
		sql.append( "GROUP BY 1, 3 " );
		sql.append( "ORDER BY 2 DESC" );

		pssum = getConn().prepareStatement( sql.toString() );
		pssum.setInt( 1, codemp );
		pssum.setInt( 2, codfilial );
		pssum.setInt( 3, codcli );

		rssum = pssum.executeQuery();

		if (rssum.next()) {
			/*
			txtDataMaxAcum.setVlrString( Funcoes.strMes( rs2.getInt( 1 ) ) + " de " + rs2.getInt( 3 ) );
			txtVlrMaxAcum.setVlrString( Funcoes.strDecimalToStrCurrency( 15, Aplicativo.casasDecFin, rs2.getString( 2 ) ) );
			 */

			consulta.setDatamaxacum( Funcoes.strMes( rssum.getInt( 1 ) ) + " de " + rssum.getInt( 3 ) );
			consulta.setVlrmaxacum( new BigDecimal( rssum.getString( 2 ) ) );
		}

		rssum.close();
		pssum.close();

		getConn().commit();

		return consulta;
	}

	public void execCancItemRec( int codrec, int nparcitrec, String obs ) throws SQLException {
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder( "UPDATE FNITRECEBER SET STATUSITREC='CR', OBSITREC=? " );
		sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODREC=? AND NPARCITREC=? " );
		int param = 1;
		ps = getConn().prepareStatement( sql.toString() );
		ps.setString( param++, obs );
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setInt( param++, codrec );
		ps.setInt( param++, nparcitrec );
		ps.executeUpdate();
		ps.close();
		getConn().commit();
	}

	public void setAltUsuItRec(Integer codrec, Integer nparcitrec, String altusuitrec ) throws SQLException{

		PreparedStatement ps = getConn().prepareStatement( 
				"update fnitreceber set altusuitrec=? , emmanut=? where codemp=? and codfilial=? and codrec=? and nparcitrec=?" );
		ps.setString( 1, altusuitrec );
		ps.setString( 2, "S" );
		ps.setInt( 3, Aplicativo.iCodEmp );
		ps.setInt( 4, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setInt( 5, codrec);
		ps.setInt( 6, nparcitrec );
		ps.executeUpdate();
		ps.close();

		ps = getConn().prepareStatement( 
				"update fnitreceber set emmanut=? where codemp=? and codfilial=? and codrec=? and nparcitrec=?" );
		ps.setString( 1, "N" );
		ps.setInt( 2, Aplicativo.iCodEmp );
		ps.setInt( 3, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setInt( 4, codrec);
		ps.setInt( 5, nparcitrec );
		ps.executeUpdate();
		ps.close();
	}

	public String[] getPlanejamentoContaRec( int iCodRec ) throws SQLException {

		String[] retorno = new String[ 4 ];


		StringBuffer sSQL = new StringBuffer();
		sSQL.append( " SELECT V.CODPLANOPAG, P.CODPLAN, P.NUMCONTA, P.CODCC" );
		sSQL.append( " FROM VDVENDA V, FNPLANOPAG P, FNRECEBER R" );
		sSQL.append( " WHERE V.CODEMPPG=P.CODEMP AND V.CODFILIALPG=P.CODFILIAL AND V.CODPLANOPAG=P.CODPLANOPAG" );
		sSQL.append( " AND V.CODEMP=R.CODEMPVD AND V.CODFILIAL=R.CODFILIALVD AND V.CODVENDA=R.CODVENDA AND V.TIPOVENDA=R.TIPOVENDA" );
		sSQL.append( " AND R.CODEMP=? AND R.CODFILIAL=? AND R.CODREC=?" );

		PreparedStatement ps = getConn().prepareStatement( sSQL.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		ps.setInt( 3, iCodRec );

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			for (int i = 0; i < retorno.length; i++) {
				retorno[ i ] = rs.getString( i + 1 ) == null ? "" : rs.getString( i + 1 );
			}
		}
		ps.close();
		getConn().commit();

		return retorno;
	}

	public void editarTitulo(Object[] oRets, Integer icodrec, Integer inparcitrec, Integer ianocc) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		sql.append( "UPDATE FNITRECEBER SET " );
		sql.append( "NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?," );
		sql.append( "ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=?,DOCLANCAITREC=?,VLRJUROSITREC=?,VLRDEVITREC=?," );
		sql.append( "VLRDESCITREC=?,DTVENCITREC=?,OBSITREC=?,CODEMPBO=?,CODFILIALBO=?,CODBANCO=?," );
		sql.append( "CODEMPTC=?,CODFILIALTC=?,CODTIPOCOB=?," );
		sql.append( "CODEMPCB=?,CODFILIALCB=?,CODCARTCOB=?, DESCPONT=?, DTPREVITREC=?, VLRPARCITREC=?, " );
		sql.append( "DTLIQITREC=?, ALTUSUITREC=? " );
		sql.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=?" );


		ps = getConn().prepareStatement( sql.toString() );

		if ("".equals(oRets[EColRet.NUMCONTA.ordinal()])) {
			ps.setNull( 1, Types.CHAR );
			ps.setNull( 2, Types.INTEGER );
			ps.setNull( 3, Types.INTEGER );
		} else {
			ps.setString( 1, (String) oRets[ EColRet.NUMCONTA.ordinal() ] );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNCONTA" ) );
		}

		if ("".equals(String.valueOf(oRets[EColRet.CODPLAN.ordinal()]).trim())) {
			ps.setNull( 4, Types.CHAR );
			ps.setNull( 5, Types.INTEGER );
			ps.setNull( 6, Types.INTEGER );
		} else {
			ps.setString( 4, (String) oRets[ EColRet.CODPLAN.ordinal() ] );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
		}

		if ("".equals(String.valueOf(oRets[EColRet.CODCC.ordinal()]).trim())) {
			ps.setNull( 7, Types.INTEGER );
			ps.setNull( 8, Types.CHAR );
			ps.setNull( 9, Types.INTEGER );
			ps.setNull( 10, Types.INTEGER );
		} else {
			ps.setInt( 7, ianocc );
			ps.setString( 8, (String) oRets[ EColRet.CODCC.ordinal() ] );
			ps.setInt( 9, Aplicativo.iCodEmp );
			ps.setInt( 10, ListaCampos.getMasterFilial( "FNCC" ) );
		}

		if ("".equals(String.valueOf(oRets[EColRet.DOC.ordinal()]).trim())) {
			ps.setNull( 11, Types.CHAR );
		} else {
			ps.setString( 11, (String) oRets[ EColRet.DOC.ordinal() ] );
		}

		if ("".equals(oRets[EColRet.VLRJUROS.ordinal()])) {
			ps.setNull( 12, Types.DECIMAL );
		} else {
			ps.setBigDecimal( 12, (BigDecimal) oRets[ EColRet.VLRJUROS.ordinal() ] );
		}

		if ("".equals(oRets[EColRet.VLRDEVOLUCAO.ordinal()])) {
			ps.setNull( 13, Types.DECIMAL );
		} else {
			ps.setBigDecimal( 13, (BigDecimal) oRets[ EColRet.VLRDEVOLUCAO.ordinal() ] );
		}

		if ("".equals( oRets[EColRet.VLRDESC.ordinal()])) {
			ps.setNull( 14, Types.DECIMAL );
		} else {
			ps.setBigDecimal( 14, (BigDecimal) ( oRets[ EColRet.VLRDESC.ordinal() ] ) );
		}

		if ("".equals( oRets[ EColRet.DTVENC.ordinal()])) {
			ps.setNull( 15, Types.DECIMAL );
		} else {
			ps.setDate( 15, Funcoes.dateToSQLDate( (java.util.Date) oRets[ EColRet.DTVENC.ordinal() ] ) );
		}

		if ("".equals(oRets[EColRet.OBS.ordinal()])) {
			ps.setNull( 16, Types.CHAR );
		} else {
			ps.setString( 16, (String) oRets[ EColRet.OBS.ordinal() ] );
		}

		if ("".equals( oRets[ EColRet.CODBANCO.ordinal()])) {
			ps.setNull( 17, Types.INTEGER );
			ps.setNull( 18, Types.INTEGER );
			ps.setNull( 19, Types.CHAR );
		} else {
			ps.setInt( 17, Aplicativo.iCodEmp );
			ps.setInt( 18, ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 19, (String) oRets[ EColRet.CODBANCO.ordinal() ] );
		}

		if ("".equals(oRets[ EColRet.CODTPCOB.ordinal()])) {
			ps.setNull( 20, Types.INTEGER );
			ps.setNull( 21, Types.INTEGER );
			ps.setNull( 22, Types.INTEGER );
		} else {
			ps.setInt( 20, Aplicativo.iCodEmp );
			ps.setInt( 21, ListaCampos.getMasterFilial( "FNTIPOCOB" ) );
			ps.setInt( 22, Integer.parseInt( (String) oRets[ EColRet.CODTPCOB.ordinal() ] ) );
		}

		if ("".equals( oRets[ EColRet.CODCARTCOB.ordinal()])) {
			ps.setNull( 23, Types.INTEGER );
			ps.setNull( 24, Types.INTEGER );
			ps.setNull( 25, Types.CHAR );
		} else {
			ps.setInt( 23, Aplicativo.iCodEmp );
			ps.setInt( 24, ListaCampos.getMasterFilial( "FNCARTCOB" ) );
			ps.setString( 25, ( (String) oRets[ EColRet.CODCARTCOB.ordinal() ] ) );
		}
		if ("".equals( oRets[ EColRet.DESCPONT.ordinal()])) {
			ps.setNull( 26, Types.CHAR );
		} else {
			ps.setString( 26, ( (String) oRets[ EColRet.DESCPONT.ordinal() ] ) );
		}
		if (oRets[EColRet.DTPREV.ordinal()] == null || "".equals(oRets[EColRet.DTPREV.ordinal()])) {
			ps.setNull( 27, Types.DECIMAL );
		} else {
			ps.setDate( 27, Funcoes.dateToSQLDate( (java.util.Date) oRets[ EColRet.DTPREV.ordinal() ] ) );
		}

		ps.setBigDecimal( 28, (BigDecimal) ( oRets[ EColRet.VLRPARC.ordinal() ] ) );

		if (oRets[ EColRet.DTLIQITREC.ordinal() ] == null || "".equals( oRets[ EColRet.DTLIQITREC.ordinal()])) {
			ps.setNull( 29, Types.DECIMAL );
		} else {
			ps.setDate( 29, Funcoes.dateToSQLDate( (java.util.Date) oRets[ EColRet.DTLIQITREC.ordinal() ] ) );
		}
		ps.setString( 30, "S"); //indica que o usuario está alterando valores no titulo.
		ps.setInt( 31, icodrec );
		ps.setInt( 32, inparcitrec );
		ps.setInt( 33, Aplicativo.iCodEmp );
		ps.setInt( 34, ListaCampos.getMasterFilial( "FNRECEBER" ) );


		ps.executeUpdate();
		ps.close();

		setAltUsuItRec( icodrec, inparcitrec, "N" );
		getConn().commit();
	}


	public void updateItReceber(BaixaRecBean baixaRecBean, int ianocc, int icodrec, int inparcitrec) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		sql.append( "UPDATE FNITRECEBER SET NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?," );
		sql.append( "ANOCC=?,CODCC=?,CODEMPCC=?,CODFILIALCC=?,DOCLANCAITREC=?,DTPAGOITREC=?,VLRPAGOITREC=VLRPAGOITREC+?," );
		sql.append( "VLRDESCITREC=?,VLRJUROSITREC=?,OBSITREC=?,STATUSITREC='RP', ALTUSUITREC=? " );
		sql.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=?" );
		int param = 1;
		ps = getConn().prepareStatement( sql.toString() );
		ps.setString( param++, baixaRecBean.getConta() );
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNCONTA" ) );
		ps.setString( param++, baixaRecBean.getPlanejamento() );
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );

		if (baixaRecBean.getCentroCusto() == null || "".equals( baixaRecBean.getCentroCusto().trim())) {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.CHAR );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
		}
		else {
			ps.setInt( param++, ianocc );
			ps.setString( param++, baixaRecBean.getCentroCusto() );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNCC" ) );
		}

		ps.setString( param++, baixaRecBean.getDocumento() );
		ps.setDate( param++, Funcoes.dateToSQLDate( baixaRecBean.getDataPagamento() ) );
		ps.setBigDecimal( param++, baixaRecBean.getValorPago() );
		ps.setBigDecimal( param++, baixaRecBean.getValorDesconto() );
		ps.setBigDecimal( param++, baixaRecBean.getValorJuros() );
		ps.setString( param++, baixaRecBean.getObservacao() );
		ps.setString( param++, "S" );
		ps.setInt( param++, icodrec );
		ps.setInt( param++, inparcitrec );
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		ps.executeUpdate();
		ps.close();

		setAltUsuItRec( icodrec, inparcitrec, "N" );
		getConn().commit();
	}

	public void geraSublanca(Integer codrec, Integer nparcrec, Integer codlanca, Integer codsublanca, String codplan, Integer codcli, 
			String codcc, String dtitrec, Date datasublanca, Date dtprevsublanca, BigDecimal vlrsublanca, String tiposublanca, Integer iAnoCC ) throws SQLException{
		PreparedStatement ps = null;
		StringBuilder sqlSubLanca = new StringBuilder();
		sqlSubLanca.append( "INSERT INTO FNSUBLANCA (CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA,CODEMPCL,CODFILIALCL,CODCLI,CODEMPPN,CODFILIALPN,CODPLAN,");
		sqlSubLanca.append( "CODEMPRC, CODFILIALRC, CODREC, NPARCITREC, ");
		sqlSubLanca.append( "CODEMPCC, CODFILIALCC,ANOCC, CODCC, ORIGSUBLANCA, DTCOMPSUBLANCA, DATASUBLANCA,DTPREVSUBLANCA,VLRSUBLANCA, TIPOSUBLANCA) ");
		sqlSubLanca.append( "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");


		ps = getConn().prepareStatement( sqlSubLanca.toString() );

		ps.setInt( PARAM_INSERT_SL.CODEMP.ordinal(), Aplicativo.iCodEmp );
		ps.setInt( PARAM_INSERT_SL.CODFILIAL.ordinal(), ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
		ps.setInt( PARAM_INSERT_SL.CODLANCA.ordinal(), codlanca );
		ps.setInt( PARAM_INSERT_SL.CODSUBLANCA.ordinal(), codsublanca );

		ps.setInt( PARAM_INSERT_SL.CODEMPCL.ordinal(), Aplicativo.iCodEmp );
		ps.setInt( PARAM_INSERT_SL.CODFILIALCL.ordinal(),  ListaCampos.getMasterFilial( "VDCLIENTE" ));
		ps.setInt( PARAM_INSERT_SL.CODCLI.ordinal(), codcli );

		ps.setInt( PARAM_INSERT_SL.CODEMPPN.ordinal(), Aplicativo.iCodEmp );
		ps.setInt( PARAM_INSERT_SL.CODFILIALPN.ordinal(), ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
		ps.setString( PARAM_INSERT_SL.CODPLAN.ordinal(), codplan );
		ps.setInt( PARAM_INSERT_SL.CODEMPRC.ordinal(), Aplicativo.iCodEmp );
		ps.setInt( PARAM_INSERT_SL.CODFILIALRC.ordinal(), ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setInt( PARAM_INSERT_SL.CODREC.ordinal(), codrec );
		ps.setInt( PARAM_INSERT_SL.NPARCITREC.ordinal(), nparcrec );


		if ("".equals(codcc)) {
			ps.setNull( PARAM_INSERT_SL.CODEMPCC.ordinal(), Types.INTEGER );
			ps.setNull( PARAM_INSERT_SL.CODFILIALCC.ordinal(), Types.INTEGER );
			ps.setNull( PARAM_INSERT_SL.ANOCC.ordinal(), Types.CHAR );
			ps.setNull( PARAM_INSERT_SL.CODCC.ordinal(), Types.INTEGER );
		} else {
			ps.setInt( PARAM_INSERT_SL.CODEMPCC.ordinal(), Aplicativo.iCodEmp );
			ps.setInt( PARAM_INSERT_SL.CODFILIALCC.ordinal(), ListaCampos.getMasterFilial( "FNCC" ) );
			ps.setInt( PARAM_INSERT_SL.ANOCC.ordinal(), iAnoCC );
			ps.setString( PARAM_INSERT_SL.CODCC.ordinal(), codcc );
		}
		ps.setString( PARAM_INSERT_SL.ORIGSUBLANCA.ordinal(), "S" );

		ps.setDate( PARAM_INSERT_SL.DTCOMPSUBLANCA.ordinal(), Funcoes.dateToSQLDate( 
				ConversionFunctions.strDateToDate( dtitrec ) )  ) ;

		ps.setDate( PARAM_INSERT_SL.DATASUBLANCA.ordinal(), Funcoes.dateToSQLDate( datasublanca ) );
		ps.setDate( PARAM_INSERT_SL.DTPREVSUBLANCA.ordinal(), Funcoes.dateToSQLDate( datasublanca ) );
		ps.setBigDecimal( PARAM_INSERT_SL.VLRSUBLANCA.ordinal(), vlrsublanca );
		ps.setString( PARAM_INSERT_SL.TIPOSUBLANCA.ordinal(), tiposublanca );

		ps.executeUpdate();
	}

	public void geraFNLanca(Integer codrecsel, DLBaixaRec.BaixaRecBean baxaRec, Integer codlanca)  throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sqlLanca = new StringBuilder();
		StringBuilder sqlSubLanca = new StringBuilder();

		//Recupera DataCompPag
		ps = getConn().prepareStatement( "SELECT dtCompItRec FROM FNITRECEBER WHERE CODEMP = ? AND CODFILIAL = ? AND CODREC = ?");
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setInt( 3, codrecsel) ;

		rs = ps.executeQuery();
		rs.next();
		Date dtCompLanca = rs.getDate( 1 );

		ps.close();

		//Recupera Plano De Contas
		ps = getConn().prepareStatement( "SELECT CODPLAN,CODEMP,CODFILIAL FROM FNCONTA WHERE NUMCONTA= ? AND CODEMP = ? AND CODFILIAL = ?" );
		ps.setString( 1, baxaRec.getConta() );
		ps.setInt( 2, Aplicativo.iCodEmp );
		ps.setInt( 3, ListaCampos.getMasterFilial( "FNCONTA" ) );
		rs = ps.executeQuery();
		rs.next();
		String codPlan = rs.getString( 1 );
		int	codEmpPlan = rs.getInt( 2 );
		int	codFilialPlan = rs.getInt( 3 );

		ps.close();

		sqlLanca.append("INSERT INTO FNLANCA (TIPOLANCA,CODEMP,CODFILIAL,CODLANCA, ");
		sqlLanca.append("CODEMPPN,CODFILIALPN, CODPLAN, DTCOMPLANCA, DATALANCA, DOCLANCA, HISTBLANCA,DTPREVLANCA, ");
		sqlLanca.append("VLRLANCA ) ");
		sqlLanca.append("VALUES ('C', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0 )");

		ps = getConn().prepareStatement( sqlLanca.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
		ps.setInt( 3, codlanca );

		ps.setInt( 4, codEmpPlan);
		ps.setInt( 5, codFilialPlan );
		ps.setString( 6, codPlan );

		ps.setDate( 7, Funcoes.dateToSQLDate( dtCompLanca ) );
		ps.setDate( 8, Funcoes.dateToSQLDate( baxaRec.getDataLiquidacao() ) );

		ps.setString( 9, baxaRec.getDocumento() );
		ps.setString( 10, baxaRec.getObservacao() );

		ps.setDate( 11, Funcoes.dateToSQLDate( baxaRec.getDataPagamento() ) );

		ps.executeUpdate();

	}

	public void excluirReceber(Integer codemp, Integer codfilial, Integer codrec) throws SQLException {
		StringBuilder sqlDelete = new StringBuilder();

		sqlDelete.append( "delete from fnreceber ");
		sqlDelete.append( "where codemp = ? and codfilial = ? " );
		sqlDelete.append( "and codrec = ?" );

		PreparedStatement ps = getConn().prepareStatement(sqlDelete.toString());
		ps.setInt(1, codemp);
		ps.setInt(2, codfilial);
		ps.setInt(3, codrec);

		ps.executeUpdate();
		getConn().commit();
	}

	public List<Integer> getListaLanca(Integer codrec, Integer nparcitrec) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ImageIcon imgStatusAt = null;

		List<Integer> lanctos = new ArrayList<Integer>();

		StringBuilder sqlLanca = new StringBuilder();
		sqlLanca.append( "SELECT CODLANCA FROM FNLANCA L ");
		sqlLanca.append( "WHERE EXISTS( SELECT * FROM FNSUBLANCA SL ");
		sqlLanca.append( "WHERE SL.CODREC = ? AND SL.NPARCITREC = ? ");
		sqlLanca.append( "AND SL.CODEMPRC = ? AND SL.CODFILIALRC = ? ");
		sqlLanca.append( "AND SL.CODEMP=L.CODEMP AND SL.CODFILIAL=L.CODFILIAL ");
		sqlLanca.append( "AND SL.CODLANCA=L.CODLANCA ) ");
		sqlLanca.append( "AND L.CODEMP = ? AND L.CODFILIAL = ? ");

		ps = getConn().prepareStatement( sqlLanca.toString() );
		ps.setInt( 1, codrec );
		ps.setInt( 2, nparcitrec );
		ps.setInt( 3, Aplicativo.iCodEmp );
		ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
		ps.setInt( 5, Aplicativo.iCodEmp );
		ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );

		rs = ps.executeQuery();
		int countLanca = 0;
		while (rs.next()) {
			lanctos.add(new Integer(rs.getInt("CODLANCA")));  
		}

		return lanctos;
	}


	public void baixaTabManut(List<Integer> selecionados, BaixaRecBean baixaRecBean, JTablePad tabManut, 
			boolean manterDados, BigDecimal valorpagto, String statusitrec, Integer ianocc) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		sql.append( "UPDATE FNITRECEBER SET NUMCONTA=?,CODEMPCA=?,CODFILIALCA=?,CODPLAN=?,CODEMPPN=?,CODFILIALPN=?," );
		sql.append( "DOCLANCAITREC=?,DTPAGOITREC=?,VLRPAGOITREC=VLRPAGOITREC+?,VLRDESCITREC=?,VLRJUROSITREC=?,ANOCC=?," );
		sql.append( "CODCC=?,CODEMPCC=?,CODFILIALCC=?,OBSITREC=? ");
		sql.append( ", STATUSITREC=? " );
		sql.append( ", DTLIQITREC=?, MULTIBAIXA=? , ALTUSUITREC=? ");
		sql.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=?" );


		BigDecimal valorapagitrec = null;
		BigDecimal valorpagoitrec = null;

		for (Integer row : selecionados) {
			if (selecionados.size() > 1) {
				baixaRecBean.setValorDesconto( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabManut.getValor( row, FManutRec.EColTabManut.VLRDESCITREC.ordinal() ) ).toString() ) ) ;
				baixaRecBean.setValorJuros( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabManut.getValor( row, FManutRec.EColTabManut.VLRJUROSITREC.ordinal() ) ).toString() ) ) ;
				baixaRecBean.setValorPagoParc( ConversionFunctions.stringCurrencyToBigDecimal(  
						( (StringDireita) tabManut.getValor( row, FManutRec.EColTabManut.VLRPAGOITREC.ordinal() ) ).toString() ) ) ;
			}


			ps = getConn().prepareStatement( sql.toString() );
			ps.setString( PARAM_UPDATE_IR.NUMCONTA.ordinal() , baixaRecBean.getConta() );
			ps.setInt( PARAM_UPDATE_IR.CODEMPCA.ordinal(), Aplicativo.iCodEmp );
			ps.setInt( PARAM_UPDATE_IR.CODFILIALCA.ordinal(), ListaCampos.getMasterFilial( "FNCONTA" ) );

			if(manterDados &&  
					(((String) tabManut.getValor(row, FManutRec.EColTabManut.CODPLAN.ordinal()) ).trim().length() > 0 )) {
				ps.setString( PARAM_UPDATE_IR.CODPLAN.ordinal(), (String) tabManut.getValor( row, FManutRec.EColTabManut.CODPLAN.ordinal() ) );
			}else{
				ps.setString( PARAM_UPDATE_IR.CODPLAN.ordinal(), baixaRecBean.getPlanejamento() );
			}

			ps.setInt( PARAM_UPDATE_IR.CODEMPPN.ordinal(), Aplicativo.iCodEmp );
			ps.setInt( PARAM_UPDATE_IR.CODFILIALPN.ordinal(), ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setDate( PARAM_UPDATE_IR.DTPAGOITREC.ordinal(), Funcoes.dateToSQLDate( baixaRecBean.getDataPagamento() ) );

			if (selecionados.size() == 1) {
				ps.setString( PARAM_UPDATE_IR.DOCLANCAITREC.ordinal(), baixaRecBean.getDocumento() );						
				ps.setBigDecimal( PARAM_UPDATE_IR.VLRPAGOITREC.ordinal(), baixaRecBean.getValorPago() );
			} else {
				valorapagitrec = ConversionFunctions.stringCurrencyToBigDecimal( 
						((StringDireita) tabManut.getValor( row, FManutRec.EColTabManut.VLRAPAGITREC.ordinal()) ).toString() );
				valorpagoitrec = valorapagitrec;

				// Se o valor digitado na dialog de baixa for maior que o valor a pagar da parcela e
				// o item não for o último, então, o valor pago será o total a pagar 
				if ((valorpagto.compareTo(valorapagitrec )>0) && (row.equals((Integer) selecionados.get((selecionados.size()-1))))) {
					valorpagoitrec = valorapagitrec;
					statusitrec = "RP";
				} else if ((valorpagto.compareTo( valorapagitrec )>0) && (row.equals((Integer) selecionados.get((selecionados.size()-1))))) {
					valorpagoitrec = valorpagto;
					baixaRecBean.setValorPago( valorpagoitrec ) ;// Setar o valor do pagamento
					baixaRecBean.setValorJuros( valorpagoitrec.subtract( valorapagitrec ));  // Setar o valor de juros
					tabManut.setValor( Funcoes.bdToStr( baixaRecBean.getValorJuros() ), row, FManutRec.EColTabManut.VLRPAGOITREC.ordinal() );
					statusitrec = "RP";
				} else if ((valorpagto.compareTo( valorapagitrec )<0)) {
					valorpagoitrec = valorpagto;
					statusitrec = "RL";
				} else {
					statusitrec = "RP";
				}
				// Setando o valor pago no Bean
				baixaRecBean.setValorPago( valorpagoitrec );
				// Removendo o valor pago do totalizador de saldo
				valorpagto = valorpagto.subtract( valorpagoitrec );
				// Ajustar o tabManut para evitar problemas na geração dos lançamentos,
				// Pois o método para gerar lançamentos financeiros busca da tabManut

				tabManut.setValor( Funcoes.bdToStr( valorpagoitrec ), row, FManutRec.EColTabManut.VLRAPAGITREC.ordinal() );

				// Passando os parâmetro de documento e valor de pagamento
				ps.setString( PARAM_UPDATE_IR.DOCLANCAITREC.ordinal(), "".equals( tabManut.getValor( row, FManutRec.EColTabManut.DOCLANCA.ordinal() ) ) ? 
						String.valueOf( tabManut.getValor( row, FManutRec.EColTabManut.DOCVENDA.ordinal() ) ) : 
							String.valueOf( tabManut.getValor( row, FManutRec.EColTabManut.DOCLANCA.ordinal() ) ) );
				ps.setBigDecimal( PARAM_UPDATE_IR.VLRPAGOITREC.ordinal(), baixaRecBean.getValorPago() );
			}
			/*NONE, NUMCONTA, CODEMPCA, CODFILIALCA, CODPLAN, CODEMPPN, CODFILIALPN, 
				DOCLANCAITREC, DTPAGOITREC, VLRPAGOITREC, VLRDESCITREC, VLRJUROSITREC, ANOCC, CODCC, CODEMPCC, 
				CODFILIALCC, OBSITREC, DTLIGITREC, MULTIBAIXA, ALTUSUITREC, CODREC, NPARCITREC, CODEMP, CODFILAIL*/

			ps.setBigDecimal( PARAM_UPDATE_IR.VLRDESCITREC.ordinal(), baixaRecBean.getValorDesconto() );
			ps.setBigDecimal( PARAM_UPDATE_IR.VLRJUROSITREC.ordinal(), baixaRecBean.getValorJuros() );

			if (manterDados) {
				if (!"".equals( (String ) tabManut.getValor( row, FManutRec.EColTabManut.CODCC.ordinal() ) ) ) {
					baixaRecBean.setCentroCusto( (String ) tabManut.getValor( row, FManutRec.EColTabManut.CODCC.ordinal() ) );
				}
			}

			if (baixaRecBean.getCentroCusto() == null || "".equals(baixaRecBean.getCentroCusto().trim())) {
				ps.setNull( PARAM_UPDATE_IR.ANOCC.ordinal(), Types.INTEGER );
				ps.setNull( PARAM_UPDATE_IR.CODCC.ordinal(), Types.CHAR );
				ps.setNull( PARAM_UPDATE_IR.CODEMPCC.ordinal(), Types.INTEGER );
				ps.setNull( PARAM_UPDATE_IR.CODFILIALCC.ordinal(), Types.INTEGER );
			} else {
				ps.setInt( PARAM_UPDATE_IR.ANOCC.ordinal(), ianocc );
				ps.setString( PARAM_UPDATE_IR.CODCC.ordinal(), baixaRecBean.getCentroCusto() );
				ps.setInt( PARAM_UPDATE_IR.CODEMPCC.ordinal(), Aplicativo.iCodEmp );
				ps.setInt( PARAM_UPDATE_IR.CODFILIALCC.ordinal(), ListaCampos.getMasterFilial( "FNCC" ) );
				tabManut.setValor( baixaRecBean.getCentroCusto() ,row, FManutRec.EColTabManut.CODCC.ordinal() );
			}

			ps.setString( PARAM_UPDATE_IR.OBSITREC.ordinal(), baixaRecBean.getObservacao() );

			ps.setString( PARAM_UPDATE_IR.STATUSITREC.ordinal(), statusitrec );

			ps.setDate( PARAM_UPDATE_IR.DTLIGITREC.ordinal(), Funcoes.dateToSQLDate( baixaRecBean.getDataLiquidacao() ) );

			ps.setString( PARAM_UPDATE_IR.MULTIBAIXA.ordinal(), (selecionados.size() > 1 ? "S" : "N") );
			ps.setString( PARAM_UPDATE_IR.ALTUSUITREC.ordinal(), "S" );
			ps.setInt( PARAM_UPDATE_IR.CODREC.ordinal(), (Integer) tabManut.getValor( row, FManutRec.EColTabManut.CODREC.ordinal() ) );
			ps.setInt( PARAM_UPDATE_IR.NPARCITREC.ordinal(), (Integer) tabManut.getValor( row, FManutRec.EColTabManut.NPARCITREC.ordinal() ) );
			ps.setInt( PARAM_UPDATE_IR.CODEMP.ordinal(), Aplicativo.iCodEmp );
			ps.setInt( PARAM_UPDATE_IR.CODFILIAL.ordinal(), ListaCampos.getMasterFilial( "FNRECEBER" ) );

			ps.executeUpdate();

			if (valorpagto.compareTo( BigDecimal.ZERO )<=0) 
				break;
		}
	}

	public void estorno(Integer codrec, Integer nparcitrec, List<Integer> lanctos, List<Integer> selecionados,  String statusitrec) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ImageIcon imgStatusAt = null;

		if ("R1".equals( statusitrec )) {
			StringBuilder sql = new StringBuilder();
			sql.append( " select codrenegrec from fnreceber where codemp = ? and codfilial = ? and codrec = ? " );
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 3, codrec );

			rs = ps.executeQuery();
			if (rs.next()) {
				Integer codRenegRec = rs.getInt( "codrenegrec" );
				if (codRenegRec != null && codRenegRec > 0) {
					statusitrec = "RR";
				}
			}

			setAltUsuItRec( codrec, nparcitrec, "S" );

			StringBuilder sqlDelete = new StringBuilder();
			sqlDelete.append( "DELETE FROM FNSUBLANCA WHERE CODREC = ? AND NPARCITREC = ? ");
			sqlDelete.append( "AND CODEMPRC= ? AND CODFILIALRC = ? ");
			sqlDelete.append( "AND CODEMP = ? AND CODFILIAL = ? ");

			ps = getConn().prepareStatement( sqlDelete.toString() );
			ps.setInt( 1, codrec );
			ps.setInt( 2, nparcitrec );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
			ps.executeUpdate();
			ps.close();

			StringBuilder sqlDeleteLanca = new StringBuilder();
			sqlDeleteLanca.append( "DELETE FROM FNLANCA ");
			sqlDeleteLanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
			sqlDeleteLanca.append( "AND VLRLANCA=0 ");

			StringBuilder sqlDeleteSublanca = new StringBuilder();
			sqlDeleteSublanca.append( "DELETE FROM FNSUBLANCA ");
			sqlDeleteSublanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
			sqlDeleteSublanca.append( "AND VLRSUBLANCA=0 ");


			for (Integer codlanca: lanctos) {
				ps = getConn().prepareStatement( sqlDeleteSublanca.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
				ps.setInt( 3, codlanca );
				ps.executeUpdate();	
				ps.close();
				ps = getConn().prepareStatement( sqlDeleteLanca.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
				ps.setInt( 3, codlanca );
				ps.executeUpdate();	
				ps.close();
			}

			StringBuilder sqlUpdate = new StringBuilder();
			sqlUpdate.append( "UPDATE FNITRECEBER SET STATUSITREC='" );
			sqlUpdate.append( statusitrec );
			sqlUpdate.append( "', DTPAGOITREC = null, DTLIQITREC = null " );
			sqlUpdate.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=? ");

			ps = getConn().prepareStatement( sqlUpdate.toString() );
			ps.setInt( 1, codrec );
			ps.setInt( 2, nparcitrec );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.executeUpdate();
			ps.close();

			setAltUsuItRec( codrec, nparcitrec, "N" );

		} else if( "RL".equals( statusitrec )){

			setAltUsuItRec( codrec, nparcitrec, "S" );

			StringBuilder sqlDelete = new StringBuilder();
			sqlDelete.append( "DELETE FROM FNSUBLANCA WHERE CODREC = ? AND NPARCITREC = ? ");
			sqlDelete.append( "AND CODEMPRC= ? AND CODFILIALRC = ? ");
			sqlDelete.append( "AND CODEMP = ? AND CODFILIAL = ? AND CODLANCA=?");

			StringBuilder sqlDeleteLanca = new StringBuilder();
			sqlDeleteLanca.append( "DELETE FROM FNLANCA ");
			sqlDeleteLanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
			sqlDeleteLanca.append( "AND VLRLANCA=0 ");

			StringBuilder sqlDeleteSublanca = new StringBuilder();
			sqlDeleteSublanca.append( "DELETE FROM FNSUBLANCA ");
			sqlDeleteSublanca.append( "WHERE CODEMP = ? AND CODFILIAL=? AND CODLANCA=? ");
			sqlDeleteSublanca.append( "AND VLRSUBLANCA=0 ");

			for (Integer codlanca : selecionados) {
				ps = getConn().prepareStatement( sqlDelete.toString() );
				ps.setInt( 1, codrec );
				ps.setInt( 2, nparcitrec );
				ps.setInt( 3, Aplicativo.iCodEmp );
				ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, ListaCampos.getMasterFilial( "FNLANCA" ) );
				ps.setInt( 7, codlanca );
				ps.executeUpdate();
				ps.close();
				ps = getConn().prepareStatement( sqlDeleteSublanca.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
				ps.setInt( 3, codlanca );
				ps.executeUpdate();	
				ps.close();
				ps = getConn().prepareStatement( sqlDeleteLanca.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNLANCA" ) );
				ps.setInt( 3, codlanca );
				ps.executeUpdate();	
				ps.close();
			}

			BigDecimal saldo = new BigDecimal( 0 );

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT SUM(SL.VLRSUBLANCA)*-1 SALDO FROM FNITRECEBER IR ");
			sql.append( "INNER JOIN FNSUBLANCA SL ON (SL.CODEMPRC = IR.CODEMP AND SL.CODFILIALRC = IR.CODFILIAL AND ");
			sql.append( "SL.CODREC = IR.CODREC AND SL.NPARCITREC = IR.NPARCITREC) ");
			sql.append( "WHERE IR.CODREC = ? AND IR.NPARCITREC = ? ");
			sql.append( "AND IR.CODEMP = ? AND IR.CODFILIAL = ? ");
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, codrec );
			ps.setInt( 2, nparcitrec );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			rs = ps.executeQuery();

			if (rs.next()) {
				saldo = rs.getBigDecimal( "SALDO" );
			}

			StringBuilder sqlUpdate = new StringBuilder();
			sqlUpdate.append( "UPDATE FNITRECEBER SET STATUSITREC = 'RL', VLRPAGOITREC= ? " );
			sqlUpdate.append( "WHERE CODREC=? AND NPARCITREC=? AND CODEMP=? AND CODFILIAL=? ");
			ps = getConn().prepareStatement( sqlUpdate.toString() );
			ps.setBigDecimal( 1, saldo );
			ps.setInt( 2, codrec );
			ps.setInt( 3, nparcitrec );
			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.executeUpdate();

			setAltUsuItRec( codrec, nparcitrec, "N" );

		}

		getConn().commit();
	}


	public Integer gerarSeqLanca(Integer codemp, Integer codfilial) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int codlanca = 0;

		//Recupera o Próximo Sequencial 
		ps = getConn().prepareStatement( "SELECT ISEQ FROM SPGERANUM(?, ?, 'LA') " );
		ps.setInt( 1, codemp );
		ps.setInt( 2, codfilial );

		rs = ps.executeQuery();
		if (rs.next()) {
			codlanca = rs.getInt( "ISEQ" );
		}

		return codlanca;
	}


	public Integer gerarSeqId (String tabela) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null; 
		Integer id = 0;
		ps = getConn().prepareStatement( "select biseq from sgsequence_idsp(?)" );
		ps.setString( 1, tabela );

		rs = ps.executeQuery();
		if (rs.next()) {
			id = rs.getInt( "biseq" ); 
		}
		return id;
	}


	public HashMap<String, Vector<?>> montaListaCores() throws SQLException {
		Vector<HashMap<String, Object>> vVals = new Vector<HashMap<String, Object>>();
		Vector<Color> vLabs = new Vector<Color>();

		HashMap<String, Vector<?>> ret = new HashMap<String, Vector<?>>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append("select s.codsinal, s.descsinal, s.corsinal ");
		sql.append("from fnsinal s ");
		sql.append("where s.codemp=? and s.codfilial=? ");


		ps = getConn().prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("FNSINAL"));

		rs = ps.executeQuery();

		while (rs.next()) {

			HashMap<String, Object> hvalores = new HashMap<String, Object>();

			hvalores.put( "CODSINAL", rs.getInt( "CODSINAL" ) );
			hvalores.put( "DESCSINAL", rs.getString( "DESCSINAL" ) );

			vVals.addElement( hvalores );

			Color cor = new Color(rs.getInt( "corsinal" ));

			vLabs.addElement( cor );
		}

		ret.put("VAL",  vVals);
		ret.put("LAB",  vLabs);

		return ret;
	}


	public void atualizaCor(Integer codsinal, Integer codrec, Integer coditrec ) throws SQLException {
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append( "update fnitreceber set codempsn=?, codfilialsn=?, codsinal=? " );
		sql.append( "where codemp=? and codfilial=? and codrec=? and nparcitrec=? " );

		ps = getConn().prepareStatement( sql.toString() );

		if (codsinal!=null) {
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNSINAL" ) );
			ps.setInt( 3, codsinal );
		} else {
			ps.setNull( 1, Types.INTEGER );
			ps.setNull( 2, Types.INTEGER );
			ps.setNull( 3, Types.INTEGER );
		}

		ps.setInt( 4, Aplicativo.iCodEmp );
		ps.setInt( 5, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
		ps.setInt( 6, codrec );
		ps.setInt( 7, coditrec );
		ps.execute();

		getConn().commit();
	}


	public void atualizaCor(Integer codsinal, Integer codrec, Integer coditpagar, boolean tudo ) throws SQLException {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;

		sql.append( "update fnitpagar set codempsn=?, codfilialsn=?, codsinal=? " );
		sql.append( "where codemp=? and codfilial=?  " );

		if (!tudo) {
			sql.append( "and codpag=? and nparcpag=? " );
		} else {
			sql.append( "and codsinal is not null " );
		}

		ps = getConn().prepareStatement( sql.toString() );

		if (codsinal!=null) {
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNSINAL" ) );
			ps.setInt( 3, codsinal );
		} else {
			ps.setNull( 1, Types.INTEGER );
			ps.setNull( 2, Types.INTEGER );
			ps.setNull( 3, Types.INTEGER );
		}

		ps.setInt( 4, Aplicativo.iCodEmp );
		ps.setInt( 5, ListaCampos.getMasterFilial( "FNITPAGAR" ) );

		if (!tudo) {
			ps.setInt( 6, codrec );
			ps.setInt( 7, coditpagar );
		}
		ps.execute();

		getConn().commit();

		sql.append( "update fnitpagar set codempsn=?, codfilialsn=?, codsinal=? " );
		sql.append( "where codemp=? and codfilial=?, emmanut='S'  " );
	}


	public String[] buscaRelPlanPag( int iCodPag ) throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String[] retorno = new String[ 4 ];

		sql.append( " SELECT C.CODPLANOPAG, PP.CODPLAN, PP.NUMCONTA, PP.CODCC " );
		sql.append( "FROM CPCOMPRA C, FNPLANOPAG PP, FNPAGAR P " );
		sql.append( "WHERE C.CODEMPPG=PP.CODEMP AND C.CODFILIALPG=PP.CODFILIAL AND C.CODPLANOPAG=PP.CODPLANOPAG " );
		sql.append( "AND C.CODEMP=P.CODEMPCP AND C.CODFILIAL=P.CODFILIALCP AND C.CODCOMPRA=P.CODCOMPRA " );
		sql.append( "AND P.CODEMP=? AND P.CODFILIAL=? AND P.CODPAG=?" );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
		ps.setInt( 3, iCodPag );

		rs = ps.executeQuery();

		if (rs.next()) {
			for (int i = 0; i < retorno.length; i++) {
				retorno[ i ] = rs.getString( i + 1 ) == null ? "" : rs.getString( i + 1 );
			}
		}

		rs.close();
		ps.close();

		getConn().commit();
		return retorno;
	}
	
	public void gerarSublanca(Integer codpag, Integer nparcpag, Integer codlanca, Integer codsublanca, String codplan, Integer codfor, 
			String codcc, String dtitpag, String datasublanca, String dtprevsublanca, BigDecimal vlrsublanca, String tiposublanca
			, Integer codcontr, Integer coditcontr, Integer anocc) throws SQLException {
		PreparedStatement ps = null;
		StringBuilder sqlSubLanca = new StringBuilder();
		sqlSubLanca.append( "INSERT INTO FNSUBLANCA (CODEMP,CODFILIAL,CODLANCA,CODSUBLANCA,CODEMPFR,CODFILIALFR,CODFOR,CODEMPPN,CODFILIALPN, CODPLAN, ");
		sqlSubLanca.append( "CODEMPPG, CODFILIALPG, CODPAG, NPARCPAG," );
		sqlSubLanca.append( "CODEMPCC,CODFILIALCC,ANOCC,CODCC,ORIGSUBLANCA,DTCOMPSUBLANCA,DATASUBLANCA,DTPREVSUBLANCA,VLRSUBLANCA, TIPOSUBLANCA");
		sqlSubLanca.append( ", CODEMPCT, CODFILIALCT, CODCONTR, CODITCONTR) ");
		sqlSubLanca.append( "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,'E', ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		ps = getConn().prepareStatement( sqlSubLanca.toString() );

		int param = 1;

		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNSUBLANCA" ) );
		ps.setInt( param++, codlanca );
		ps.setInt( param++, codsublanca );

		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "CPFORNECED" ) );
		ps.setInt( param++, codfor );

		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
		ps.setString( param++, codplan );
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
		ps.setInt( param++, codpag );
		ps.setInt( param++, nparcpag );


		if ("".equals( codcc )) {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.CHAR );
			ps.setNull( param++, Types.INTEGER );
		} else {
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNCC" ) );
			ps.setInt( param++, anocc );
			ps.setString( param++, codcc );
		}

		ps.setDate( param++, Funcoes.dateToSQLDate( 
				ConversionFunctions.strDateToDate( dtitpag ) )  ) ;

		ps.setDate( param++, Funcoes.strDateToSqlDate( datasublanca ) );
		ps.setDate( param++, Funcoes.strDateToSqlDate( dtprevsublanca) );

		ps.setBigDecimal( param++, vlrsublanca );
		ps.setString( param++, tiposublanca );

		if (codcontr==null || coditcontr==null) {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
		} else {
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDITCONTRATO" ) );
			ps.setInt( param++, codcontr );
			ps.setInt( param++, coditcontr );
		}

		ps.executeUpdate();

	}


	public Integer pesquisarDoc(Integer docpag) throws SQLException {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer retorno = null;
		sql.append( "select codpag from fnpagar where codemp=? and codfilial=? and docpag=?" );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
		ps.setInt( 3, docpag );

		rs = ps.executeQuery();

		if (rs.next()) {
			retorno = rs.getInt("codpag");
		}

		rs.close();
		ps.close();

		return retorno;
	}

	public Integer pesquisarPedido(Integer codcompra) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer retorno = null;
		sql.append( "select codpag from fnpagar where codemp=? and codfilial=? and codcompra=? " );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGAR" ) );
		ps.setInt( 3, codcompra );

		rs = ps.executeQuery();

		if (rs.next()) {
			retorno = rs.getInt( "codpag" );
		}

		rs.close();
		ps.close();

		return retorno;
	}


	public Map<String, Object> getPrefere() throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		Map<String, Object> retorno = new HashMap<String, Object>();

		sql.append( "SELECT ANOCENTROCUSTO,CODHISTREC, CODPLANJR, CODPLANDC," );
		sql.append( "CODHISTPAG, CODPLANJP, CODPLANDR, LANCAFINCONTR " );
		sql.append( "FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=? " );

		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, Aplicativo.iCodEmp );
		ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

		rs = ps.executeQuery();

		if (rs.next()) {
			//AMBOS
			retorno.put("anocc", rs.getInt("ANOCENTROCUSTO"));

			//RECEBER
			retorno.put("codhistrec", rs.getInt("CODHISTREC"));
			retorno.put("codplanjr", getString(rs.getString("CODPLANJR")));
			retorno.put("codplandc", getString(rs.getString("CODPLANDC")));

			//PAGAR
			retorno.put("codhistpag", rs.getInt("CODHISTPAG") );
			retorno.put("codplanjp", getString(rs.getString("CODPLANJP")));
			retorno.put("codplandr", getString(rs.getString("CODPLANDR")));
			retorno.put("lancafincontr", getString(rs.getString("LANCAFINCONTR")));
		}

		rs.close();
		ps.close();

		getConn().commit();
		return retorno;
	}
}