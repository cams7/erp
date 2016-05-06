package org.freedom.modulos.std.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.exceptions.ExceptionCarregaDados;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.std.business.object.VDContrOrc;
import org.freedom.modulos.std.business.object.VDContrato;
import org.freedom.modulos.std.business.object.VDItContrato;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaOrc;

public class DAOBuscaOrc extends AbstractDAO {


	private Vector<Object> vValidos = new Vector<Object>();

	private Map<String, Object> prefs = null;

	public enum COL_PREFS { USAPEDSEQ, AUTOFECHAVENDA, ADICORCOBSPED, ADICOBSORCPED, FATORCPARC, APROVORCFATPARC, SOLDTSAIDA, BLOQVDPORATRASO, NUMDIASBLOQVD };

	public DAOBuscaOrc( DbConnection connection ) {

		super( connection );

	}
	
	public void commit() throws SQLException {
		getConn().commit();
	}

	public int getCodcli(int codorc, int codemp, int codfilial) throws ExceptionCarregaDados {
		int result = -1;
		StringBuilder sql = new StringBuilder();
		sql.append("select codcli from vdorcamento where codemp=? and codfilial=? and tipoorc='O' and codorc=?");
		try {
			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codorc );
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt("codcli");
			}
			rs.close();
			rs.close();
			getConn().commit();
		} catch (SQLException err) {
			try {
				getConn().rollback();
			} catch (SQLException err2) {
				err2.printStackTrace();
			}
			throw new ExceptionCarregaDados( err.getMessage() );
		}
		return result;
	}
	
	public void insertVDContrOrc(VDContrOrc contrOrc) throws SQLException {
		
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append("insert into vdcontrorc (codemp, codfilial, codcontr, coditcontr, ");
		sql.append("codempor, codfilialor, tipoorc, codorc, coditorc ");
		sql.append(") values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		int param = 1;
		
		ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, contrOrc.getCodemp() );
		ps.setInt( param++, contrOrc.getCodFilial() );
		ps.setInt( param++, contrOrc.getCodContr() );
		ps.setInt( param++, contrOrc.getCodItContr() );
		ps.setInt( param++, contrOrc.getCodEmpOr() );
		ps.setInt( param++, contrOrc.getCodFilialOr() );
		ps.setString( param++, contrOrc.getTipoOrc() );
		ps.setInt( param++, contrOrc.getCodOrc() );
		ps.setInt( param++, contrOrc.getCodItOrc() );
		
		ps.execute();
		
	}
	
	
	public void insertVDContrato( VDContrato contrato ) throws SQLException {
		
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append("insert into vdcontrato (codemp, codfilial, codcontr, desccontr, codempcl, codfilialcl, codcli, dtinicio, dtfim, tpcobcontr, diavenccontr,");
		sql.append( "diafechcontr, indexcontr, tpcontr, dtprevfin, ativo ");
		sql.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		int param = 1;
		ps = getConn().prepareStatement( sql.toString() );
		
		ps.setInt( param++, contrato.getCodEmp() );
		ps.setInt( param++, contrato.getCodFilial() );
		ps.setInt( param++, contrato.getCodContr() );
		ps.setString( param++, contrato.getDescContr() );
		ps.setInt( param++, contrato.getCodEmpCl() );
		ps.setInt( param++, contrato.getCodFilialCl() );
		ps.setInt( param++, contrato.getCodCli() );
		ps.setDate( param++, Funcoes.dateToSQLDate(contrato.getDtInicio()));
		ps.setDate( param++, Funcoes.dateToSQLDate(contrato.getDtFim()));
		ps.setString( param++, contrato.getTpCobContr() );
		ps.setInt( param++, contrato.getDiaVencContr() );
		ps.setInt( param++, contrato.getDiaFechContr() );
		ps.setInt( param++, contrato.getIndexContr() );
		ps.setString( param++, contrato.getTpcontr() );
		ps.setDate( param++, Funcoes.dateToSQLDate(contrato.getDtPrevFin()));
		ps.setString( param++, contrato.getAtivo() );
		
		ps.execute();
	}
	
	
	public void insertVDItContrato(VDItContrato itemContrato) throws SQLException {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append( "insert into vditcontrato (codemp, codfilial, codcontr, coditcontr, descitcontr, codemppd, codfilialpd, codprod, qtditcontr, vlritcontr, "); 
		sql.append("codemppe, codfilialpe, codprodpe, vlritcontrexced, indexitcontr, acumuloitcontr, franquiaitcontr ");
		sql.append( ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?) ");
		
		int param = 1;
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );

		ps.setInt( param++, itemContrato.getCodEmp());
		ps.setInt( param++, itemContrato.getCodFilial());
		ps.setInt( param++, itemContrato.getCodContr());
		ps.setInt( param++, itemContrato.getCodItContr());
		ps.setString( param++, itemContrato.getDescItContr());
		ps.setInt( param++, itemContrato.getCodEmpPd());
		ps.setInt( param++, itemContrato.getCodFilialPd());
		ps.setInt( param++, itemContrato.getCodProd());
		ps.setBigDecimal( param++, itemContrato.getQtdItContr());
		ps.setBigDecimal( param++, itemContrato.getVlrItContr());
		ps.setInt( param++, itemContrato.getCodEmpPe());
		ps.setInt( param++, itemContrato.getCodFilialPe());
		ps.setInt( param++, itemContrato.getCodProdPe());
		ps.setBigDecimal( param++, itemContrato.getVlrItContrRexCed());
		ps.setInt( param++, itemContrato.getIndexItContr());
		ps.setInt( param++, itemContrato.getAcumuloItContr());
		ps.setString( param++, itemContrato.getFranquiaItContr());
		
		
		ps.execute();
	}

	
	public int getMaxCodContr(Integer codemp, Integer codfilial) throws SQLException {

		PreparedStatement ps = null;
		String sql = " select max(ct.codcontr) codcontr from vdcontrato ct where ct.codemp=? and ct.codfilial=?";
		int param = 1;
		ps = getConn().prepareStatement( sql.toString() );
		int codcontr = 0;
		
		
		ps.setInt( param++, codemp);
		ps.setInt( param++, codfilial);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			codcontr = rs.getInt("codcontr") + 1;
		}
		
		return codcontr;
	}
	
	
	public void atualizaLoteItVenda( String codlote, int irow, int codorc, int coditorc ) throws SQLException {

		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		int param = 1;


		sql.append( "UPDATE VDITORCAMENTO SET CODEMPLE=?, CODFILIALLE=?, CODLOTE=? " );
		sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODORC=? AND CODITORC=?" );

		ps = getConn().prepareStatement( sql.toString() );

		ps.setInt( param++, Aplicativo.iCodEmp);
		ps.setInt( param++, ListaCampos.getMasterFilial("EQLOTE"));
		ps.setString( param++, codlote);
		ps.setInt( param++, Aplicativo.iCodEmp);
		ps.setInt( param++, ListaCampos.getMasterFilial("VDORCAMENTO"));
		ps.setInt( param++, (Integer) codorc);
		ps.setInt( param++, (Integer) coditorc);

		ps.execute();


		getConn().commit();
		
	}
	

	public int executaVDAdicVendaORCSP(Integer codorc, Integer codfilialoc, Integer codempoc, String tipovenda, Integer codvenda, Date datasaida) throws SQLException {
		int icodvenda = 0;
		int param = 1;

		String sql = "SELECT IRET FROM VDADICVENDAORCSP(?,?,?,?,?,?)";
		PreparedStatement ps = getConn().prepareStatement( sql );
		ps.setInt( param++, codorc);
		ps.setInt( param++, codfilialoc );
		ps.setInt( param++, codempoc );
		ps.setString( param++, tipovenda );
		ps.setInt( param++, codvenda);
		ps.setDate( param++, Funcoes.dateToSQLDate( datasaida == null ? new Date() : datasaida ));
		ResultSet rs = ps.executeQuery();

		if ( rs.next() )
			icodvenda = rs.getInt( 1 );

		rs.close();
		ps.close();

		return icodvenda;
	}

	
	public void executaVDAdicItVendaORCSP(Integer codfilial, Integer codvenda, Integer codorc, Integer coditorc, Integer codfilialoc, Integer codempoc, 
			String tipovenda, String tpagr, BigDecimal qtdprod, BigDecimal qtdafatitorc, BigDecimal desc) throws SQLException {

		String sql = "EXECUTE PROCEDURE VDADICITVENDAORCSP(?,?,?,?,?,?,?,?,?,?)";
		int param = 1;

		PreparedStatement ps = getConn().prepareStatement( sql );

		ps.setInt( param++, codfilial);
		ps.setInt( param++, codvenda );
		ps.setInt( param++, codorc);
		ps.setInt( param++, coditorc );
		ps.setInt( param++, codfilialoc);
		ps.setInt( param++, codempoc);

		ps.setString( param++, tipovenda );
		ps.setString( param++, tpagr);

		// Verificação dos excessos de produção

		if( qtdprod.compareTo( qtdafatitorc ) > 0 && 
				( Funcoes.mensagemConfirma( null,  

						"A quantidade produzida do ítem \n" + desc.toString().trim() + " \n" +
								"excede a quantidade solicitada pelo cliente.\n" +
								"Deseja faturar a quantidade produzida?\n\n" +
								"Quantidade solicitada: " + Funcoes.bdToStrd( qtdafatitorc ) + "\n" +
								"Quantidade produzida : " + Funcoes.bdToStrd( qtdprod ) + "\n\n"

						) == JOptionPane.YES_OPTION ) ) {

			ps.setBigDecimal( param++, qtdprod );

		}
		else {
			ps.setBigDecimal( param++, qtdafatitorc );	
		}


		ps.setBigDecimal( param++, desc );

		ps.execute();
		ps.close();
	}


	public void executaVDAtuDescVendaORCSP(Integer codemp, Integer codfilial, String tipovenda, Integer codvenda) throws SQLException {
		String sql = null;
		int param = 1;

		// Atualiza o desconto na venda de acordo com o desconto dado no orçamento.
		sql = "EXECUTE PROCEDURE VDATUDESCVENDAORCSP(?,?,?,?)";
		PreparedStatement ps = getConn().prepareStatement( sql );
		ps.setInt( param++, codemp);
		ps.setInt( param++, codfilial);
		ps.setString( param++, tipovenda);
		ps.setInt( param++, codvenda);

		ps.execute();
		ps.close();
	}


	public void atualizaObsPed( final StringBuffer obs, final int iCodVenda ) throws SQLException {

		String sql = null;
		int param = 1;

		sql = "UPDATE VDVENDA SET OBSVENDA=? WHERE " + "CODEMP=? AND CODFILIAL=? AND CODVENDA=?";

		PreparedStatement ps = getConn().prepareStatement( sql );
		String obsupdate = obs.toString().replace( "\n", " - " );

		ps.setString( param++, obsupdate.length() > 10000 ? obsupdate.substring( 0, 10000 ) : obsupdate );
		ps.setInt( param++, Aplicativo.iCodEmp );
		ps.setInt( param++, Aplicativo.iCodFilial );
		ps.setInt( param++, iCodVenda );

		ps.execute();


	}


	public String testaPgto(String tipomov, int codcli, int codempcl, int codfilialcl ) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";

		try {

			// Se for devolução não deve verificar parcelas em aberto...

			if ( ( ! TipoMov.TM_DEVOLUCAO_VENDA.getValue().equals( tipomov ) ) && ( ! TipoMov.TM_DEVOLUCAO_REMESSA.getValue().equals( tipomov ) ) ) {

				String sSQL = "SELECT RETORNO FROM FNCHECAPGTOSP(?,?,?,?,?)";
				
				int numdiasbloqvd = (Integer) getPrefs().get( COL_PREFS.NUMDIASBLOQVD.name() );
				String bloqvdporatraso = (String) getPrefs().get( COL_PREFS.BLOQVDPORATRASO.name() );

				int param = 1;
				ps = getConn().prepareStatement( sSQL );
				ps.setInt( param++, codcli );
				ps.setInt( param++, codempcl );
				ps.setInt( param++, codfilialcl );
				ps.setString( param++, bloqvdporatraso );
				ps.setInt( param++, numdiasbloqvd );
				
				rs = ps.executeQuery();

				if ( rs.next() ) {
					result = rs.getString( "RETORNO" );
					if (result==null) {
						result = "";
					} else {
						result = result.trim();
						// Caso o retorno seja S (Pagamentos OK).
						if ("S".equals( result )) {
							result = ""; // Retornar condição em branco para evitar mensagem desenecessária
						}
					}
				}
				else {
					throw new Exception( "Não foi possível checar os pagamentos do cliente !" );
				}

				rs.close();
				ps.close();
				getConn().commit();
				
				if ( ( !"".equals( result ) ) && ( "N".equals( result.substring( 0, 1 ) ) ) ) {
					if (result.length()>2) {
						int numreg = Integer.parseInt(result.substring(2));
						if (numreg>0) {
							result = "Cliente possui "+numreg+" parcelas em aberto atraso !!!";
						}
					}
				}
			}
		} catch ( SQLException err ) {
			try {
				getConn().rollback();
			} catch (SQLException err2) {
				err2.printStackTrace();
			}
			err.printStackTrace();
			throw new Exception( "Não foi possível verificar os pagamentos do cliente !\n"+err.getMessage() );
		}

		return result;

	}

	public Vector<Vector<Object>> buscar(Integer codorc, Integer codcli, Integer codconv, String busca) throws ExceptionCarregaDados{

		PreparedStatement ps = null;
		ResultSet rs = null;
		String mensagemErro = "";
		String sql = null;
		String sWhere = null;
		Vector<Object> vVals = null;
		boolean bOrc = false;
		boolean bConv = false;
		int iCod = -1;
		Vector<Vector<Object>> vector = null;

		if (codorc.intValue() > 0) {
			iCod = codorc.intValue();
			sWhere = ", VDCLIENTE C WHERE O.CODORC = ? AND O.CODFILIAL = ? AND O.CODEMP = ? AND C.CODEMP=O.CODEMPCL AND C.CODFILIAL=O.CODFILIALCL AND C.CODCLI=O.CODCLI ";
			bOrc = true;
		}
		else {
			if (busca.equals("L") &&  codcli > 0) {
				iCod = codcli.intValue();
				if (iCod == 0) {
					mensagemErro = "Código do cliente inválido!";
					vector = null;
					throw new ExceptionCarregaDados(mensagemErro);
				}
				sWhere = ", VDCLIENTE C WHERE C.CODCLI=? AND C.CODFILIAL=? AND C.CODEMP=? AND O.CODCLI=C.CODCLI AND" +
						" O.CODFILIALCL=C.CODFILIAL AND O.CODEMPCL=C.CODEMP AND O.STATUSORC IN ('OL','FP','OP') ";

			}
			else if (busca.equals( "O" ) && codconv > 0) {
				iCod = codconv.intValue();
				if (iCod == 0) {
					mensagemErro = "Código do conveniado inválido!";
					vector = null;
					throw new ExceptionCarregaDados(mensagemErro);
				}
				sWhere = ", ATCONVENIADO C WHERE C.CODCONV=? AND C.CODFILIAL=? AND C.CODEMP=? AND O.CODCONV=C.CODCONV AND" +
						" O.CODFILIALCV=C.CODFILIAL AND O.CODEMPCV=C.CODEMP AND O.STATUSORC IN ('OL','FP') ";

				bConv = true;
			}
			else if (iCod == -1) {
				mensagemErro = "Número do orçamento inválido!";
				vector = null;
				throw new ExceptionCarregaDados(mensagemErro);
			}

		}

		try {

			sql = "SELECT O.CODORC," + ( bConv ? "O.CODCONV,C.NOMECONV," : "O.CODCLI,C.NOMECLI," ) 
					+ "(SELECT COUNT(IT.CODITORC) FROM VDITORCAMENTO IT WHERE IT.CODORC=O.CODORC "
					+ "AND IT.CODFILIAL=O.CODFILIAL AND IT.CODEMP=O.CODEMP),"
					+ "(SELECT COUNT(IT.CODITORC) FROM VDITORCAMENTO IT WHERE IT.CODORC=O.CODORC " 
					+ "AND IT.CODFILIAL=O.CODFILIAL AND IT.CODEMP=O.CODEMP " 
					+ "AND IT.ACEITEITORC='S' AND IT.APROVITORC='S'),"
					+ "(SELECT SUM(IT.VLRLIQITORC) FROM VDITORCAMENTO IT WHERE IT.CODORC=O.CODORC "
					+ "AND IT.CODFILIAL=O.CODFILIAL AND IT.CODEMP=O.CODEMP),"
					+ "(SELECT SUM(IT.VLRLIQITORC) FROM VDITORCAMENTO IT WHERE IT.CODORC=O.CODORC " 
					+ "AND IT.CODFILIAL=O.CODFILIAL AND IT.CODEMP=O.CODEMP "
					+ "AND IT.ACEITEITORC='S' AND IT.APROVITORC='S'), O.STATUSORC, COALESCE(O.OBSORC,'') OBSORC " 
					+ "FROM VDORCAMENTO O" 
					+ sWhere + " ORDER BY O.CODORC";

			ps = getConn().prepareStatement( sql );
			ps.setInt( 1, iCod );
			ps.setInt( 2, ListaCampos.getMasterFilial( bOrc ? "VDORCAMENTO" : ( bConv ? "ATCONVENIADO" : "VDCLIENTE" ) ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			rs = ps.executeQuery();
			vector =  new Vector<Vector<Object>>();

			while (rs.next()) {
				if (rs.getString(8).equals("OL") || rs.getString(8).equals("OP") || rs.getString(8).equals("FP")) {

					vVals = new Vector<Object>();
					vVals.addElement( new Boolean( true ) );
					vVals.addElement( new Integer( rs.getInt( "CodOrc" ) ) );
					vVals.addElement( new Integer( rs.getInt( 2 ) ) );
					vVals.addElement( rs.getString( 3 ).trim() );
					vVals.addElement( new Integer( rs.getInt( 4 ) ) );
					vVals.addElement( new Integer( rs.getInt( 5 ) ) );
					vVals.addElement( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( 6 ) != null ? rs.getString( 6 ) : "0" ) );
					vVals.addElement( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( 7 ) != null ? rs.getString( 7 ) : "0" ) );
					vVals.addElement( rs.getString( "OBSORC" ) );
					vector.add(vVals);

				}
				else {
					mensagemErro =  "ORÇAMENTO NÃO ESTÁ LIBERADO!";
					vector = null;
					throw new ExceptionCarregaDados(mensagemErro);
				}
			}

			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			mensagemErro = "Erro ao buscar orçamentos!\n";
			vector = null;
			throw new ExceptionCarregaDados(mensagemErro);
		}

		ps = null;
		rs = null;
		sql = null;
		sWhere = null;
		vVals = null;

		return vector;
	}


	public Vector<Vector<Object>>  carregar( Vector<Vector<Object>> tabOrc, boolean aprovorcfatparc, String origem ) throws SQLException {

		Vector<Object> vVals = null;
		Vector<Vector<Object>> vector = new Vector<Vector<Object>>();

		Vector<String> vcodorcs = new Vector<String>();
		Vector<Vector<String>> vorcs = new Vector<Vector<String>>();
		vorcs.add( vcodorcs );

		int count = 0;

		for (int i = 0; i < tabOrc.size(); i++) {

			if (!((Boolean) tabOrc.elementAt(i).get(0)).booleanValue()) {
				continue;
			}

			vcodorcs.add(String.valueOf(tabOrc.elementAt(i).get(1)));
			count++;

			if (count == 1000) {
				vcodorcs = new Vector<String>();
				vorcs.add(vcodorcs);
				count = 0;
			}
		}


		for (Vector<String> v : vorcs) {

			String scodorcs = "";

			for (int i = 0; i < v.size(); i++) {
				if (scodorcs.length() > 0) {
					scodorcs += ",";
				}
				scodorcs += v.get( i );
			}

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT IT.CODORC,IT.CODITORC,IT.CODPROD,P.DESCPROD," );
			sql.append( "IT.QTDITORC,IT.QTDFATITORC,IT.QTDAFATITORC,IT.PRECOITORC,IT.VLRDESCITORC,IT.VLRLIQITORC," );
			sql.append( "IT.VLRPRODITORC, P.CLOTEPROD, IT.CODLOTE, coalesce(ip.qtdfinalproditorc,0) qtdfinalproditorc, ip.codop, it.codalmox ");

			sql.append( "FROM EQPRODUTO P, VDORCAMENTO O, VDITORCAMENTO IT  " );
			sql.append( "LEFT OUTER JOIN PPOPITORC IP ON IP.CODEMPOC=IT.CODEMP AND IP.CODFILIALOC=IT.CODFILIAL AND IP.TIPOORC=IT.TIPOORC AND IP.CODORC=IT.CODORC AND IP.CODITORC=IT.CODITORC ");

			sql.append( "WHERE O.CODEMP=IT.CODEMP AND O.CODFILIAL=IT.CODFILIAL AND O.TIPOORC=IT.TIPOORC AND O.CODORC=IT.CODORC AND ");
			sql.append( "P.CODPROD=IT.CODPROD AND P.CODFILIAL=IT.CODFILIALPD AND " );
			sql.append( "P.CODEMP=IT.CODEMPPD AND ");
			sql.append( "((IT.ACEITEITORC='S' AND IT.FATITORC IN ('N','P') AND IT.APROVITORC='S' AND IT.SITPRODITORC='NP') OR ");
			sql.append( "(IT.SITPRODITORC='PD' AND IT.APROVITORC='S' AND IT.FATITORC IN ('N','P') )) ");
			if (aprovorcfatparc) {
				sql.append( " AND O.STATUSORC NOT IN ('OV','FP') " ); 
			}
			sql.append( " AND IT.CODEMP=? AND IT.CODFILIAL=? AND IT.CODORC IN " );
			sql.append( "(" + scodorcs + ") " );

			//Caso a origem for a tela de Contrato busca apenas produtos com o tipo Serviço.
			if ("Contrato".equals( origem )) {
				sql.append( " AND P.TIPOPROD = 'S' " );
				sql.append( " AND NOT EXISTS( SELECT * FROM VDCONTRORC CO WHERE CO.CODEMPOR=IT.CODEMP AND CO.CODFILIALOR=IT.CODFILIAL AND CO.TIPOORC=IT.TIPOORC ");
				sql.append( " AND CO.CODORC=IT.CODORC AND CO.CODITORC=IT.CODITORC) " );
			}


			sql.append( " ORDER BY IT.CODORC,IT.CODITORC " );

			PreparedStatement ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				// vVals = new Vector<Object>();

				vVals = new Vector<Object>();
				vVals.addElement( new Boolean( true ));
				vVals.addElement( new Integer( rs.getInt( "CodItOrc" )));
				vVals.addElement( new Integer( rs.getInt( "CodProd" )));
				vVals.addElement( rs.getString( "DescProd" ).trim());
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDec, rs.getString( DLBuscaOrc.GRID_ITENS.QTDITORC.toString() ) != null ? rs.getString( DLBuscaOrc.GRID_ITENS.QTDITORC.toString() ) : "0" ));
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDec, rs.getString( DLBuscaOrc.GRID_ITENS.QTDAFATITORC.toString()) != null ? rs.getString(DLBuscaOrc.GRID_ITENS.QTDAFATITORC.toString() ) : "0" ));
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDec, rs.getString( DLBuscaOrc.GRID_ITENS.QTDFATITORC.toString() ) != null ? rs.getString( DLBuscaOrc.GRID_ITENS.QTDFATITORC.toString() ) : "0" ));
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDec, rs.getString( DLBuscaOrc.GRID_ITENS.QTDFINALPRODITORC.toString() ) != null ? rs.getString( DLBuscaOrc.GRID_ITENS.QTDFINALPRODITORC.toString() ) : "0" ));
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDecPre, rs.getString( "PrecoItOrc" ) != null ? rs.getString( "PrecoItOrc" ) : "0" ));
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDecPre, rs.getString( "VlrDescItOrc" ) != null ? rs.getString( "VlrDescItOrc" ) : "0" ));
				vVals.addElement( Funcoes.strDecimalToStrCurrencyd( Aplicativo.casasDecPre, rs.getString( "VlrLiqItOrc" ) != null ? rs.getString( "VlrLiqItOrc" ) : "0" ));
				vVals.addElement( "");
				vVals.addElement( "");
				vVals.addElement( "0,00");
				vVals.addElement( rs.getInt( "CodOrc" ));
				// 	private enum GRID_ITENS { SEL, CODITORC, CODPROD, DESCPROD, QTD, QTDAFAT, QTDFAT, QTD_PROD, PRECO, DESC, VLRLIQ, TPAGR, PAI, VLRAGRP, CODORC, USALOTE, CODLOTE };	
				vVals.addElement( rs.getString( "CLOTEPROD" ));
				vVals.addElement( rs.getString( "CODLOTE" ) == null ? "" : rs.getString( "CODLOTE" ));
				vVals.addElement( rs.getString( "CODALMOX" ) == null ? "" : rs.getString( "CODALMOX" ));
				vVals.addElement( rs.getString( "CODOP" ) == null ? "" : rs.getString( "CODOP" ));

				vValidos.addElement( new int[] { rs.getInt( "CodOrc" ), rs.getInt( "CodItOrc" ) } );

				vector.add( vVals );
			}

			getConn().commit();
		}

		return vector;
	}


	public Map<String, Object> getPrefs() throws SQLException {
		Map<String, Object> result = null;

		if (prefs==null) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			StringBuilder sql = null;
			result = new HashMap<String, Object>();
	
			sql = new StringBuilder("SELECT P1.USAPEDSEQ, P4.AUTOFECHAVENDA, P1.ADICORCOBSPED, P1.ADICOBSORCPED, P1.FATORCPARC, P1.APROVORCFATPARC, P1.SOLDTSAIDA " );
			sql.append( ", P1.BLOQVDPORATRASO, P1.NUMDIASBLOQVD ");
			sql.append( "FROM SGPREFERE1 P1, SGPREFERE4 P4 " );
			sql.append( "WHERE P1.CODEMP=? AND P1.CODFILIAL=? " );
			sql.append( "AND P4.CODEMP=P1.CODEMP AND P4.CODFILIAL=P4.CODFILIAL");
	
			ps = getConn().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
	
			if ( rs.next() ) { 
	
				result.put( COL_PREFS.USAPEDSEQ.name(), new Boolean("S".equals( rs.getString( COL_PREFS.USAPEDSEQ.name()))));
				result.put( COL_PREFS.AUTOFECHAVENDA.name(), new Boolean("S".equals( rs.getString( COL_PREFS.AUTOFECHAVENDA.name()))));
				result.put( COL_PREFS.ADICORCOBSPED.name(), new Boolean("S".equals( rs.getString( COL_PREFS.ADICORCOBSPED.name()))));
				result.put( COL_PREFS.ADICOBSORCPED.name(), new Boolean("S".equals( rs.getString( COL_PREFS.ADICOBSORCPED.name()))));
				result.put( COL_PREFS.FATORCPARC.name(), new Boolean("S".equals( rs.getString( COL_PREFS.FATORCPARC.name()))));
				result.put( COL_PREFS.APROVORCFATPARC.name(), new Boolean("S".equals( rs.getString( COL_PREFS.APROVORCFATPARC.name()))));
				result.put( COL_PREFS.SOLDTSAIDA.name(), new Boolean("S".equals( rs.getString( COL_PREFS.SOLDTSAIDA.name()))));
				if (rs.getString( COL_PREFS.BLOQVDPORATRASO.name())==null) {
					result.put( COL_PREFS.BLOQVDPORATRASO.name(), "N");
				} else {
					result.put( COL_PREFS.BLOQVDPORATRASO.name(), rs.getString( COL_PREFS.BLOQVDPORATRASO.name()));
				}
				result.put( COL_PREFS.NUMDIASBLOQVD.name(), new Integer(rs.getInt( COL_PREFS.NUMDIASBLOQVD.name())));
	
			}
	
			rs.close();
			ps.close();
		}
		else {
			result = prefs;
		}
			
		return result;
	}



	public Vector<Object> getvValidos() {

		return vValidos;
	}

}
