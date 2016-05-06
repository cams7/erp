package org.freedom.modulos.std.dao;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.modulos.std.business.object.TabelaPreco;


public class DAOManutPreco extends AbstractDAO {
	
	public DAOManutPreco(DbConnection cn) {
		super(cn);
	}
	
	public void atualizaPreco(String tipooper, Integer codemp, Integer codfilial, 
			Integer codemptb, Integer codfilialtb, Integer codtab,
			Integer codemppg, Integer codfilialpg, Integer codplanopag,
			Integer codempcc, Integer codfilialcc, Integer codclascli,
			Integer codempmc, Integer codfilialmc, String codmarca,
			Integer codempgp, Integer codfilialgp, String codgrup,
			String  origem, String operador, BigDecimal multiplicando, Integer codprod ) {
		
		StringBuilder sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int RegistroAtualizados = 0;
		int RegistrosIncluidos = 0;
		int RegistrosErros = 0;
		MathContext mcPerc= new MathContext( 5, RoundingMode.HALF_EVEN   );
		
		if ( Funcoes.mensagemConfirma( null, "Confirma processamento?" ) != JOptionPane.YES_OPTION ) {
			return;
		}
		
		try {
			
			sql = new StringBuilder();
			sql.append("select pd.codemp, pd.codfilial, pp.codprecoprod, pd.codprod, pd.descprod, pd.precobaseprod, pd.custoinfoprod ");
			sql.append(", pp.codemptb, pp.codfilialtb, pp.codtab , pp.precoprod ");
			sql.append(", pp.codempcc, pp.codfilialcc, pp.codclascli, pp.codemppg, pp.codfilialpg,  pp.codplanopag, pp.precoprod ");
			sql.append(", pp.tipoprecoprod ");
			sql.append("from eqproduto pd ");
			sql.append("left outer join vdprecoprod pp on ");
			sql.append("pp.codemp=pd.codemp and pp.codfilial=pd.codfilial and pp.codprod=pd.codprod  and pp.ativoprecoprod='S' ");
			sql.append("and pp.codemptb=? and pp.codfilialtb=? and pp.codtab=? ");
			// Amarração para plano de pagamento
			if(codplanopag > 0) {
				sql.append("and pp.codemppg=? and pp.codfilialpg=? and pp.codplanopag=? ");
			} else {
				sql.append(" and pp.codemppg is null and pp.codfilialpg is null and pp.codplanopag is null ");
			}
			// Amaração para classificação de cliente
			if(codclascli > 0) {
				sql.append(" and pp.codempcc=? and pp.codfilialcc=? and pp.codclascli=? ");
			} else {
				sql.append(" and pp.codempcc is null and pp.codfilialcc is null and pp.codclascli is null ");
			}
			
			sql.append("where pd.codemp=? and pd.codfilial=? and pd.ativoprod='S' and pd.cvprod in ('V','A') ");
			if(codprod > 0) {
				sql.append(" and pd.codprod=?" );
			} 			
			
			// Amarração para tabela de preço
			/*if("T".equals( origem )) {
				sql.append("and pp.codemptb=? and pp.codfilialtb=? and pp.codtab=? ");
			}// Filtro por Marca
*/			if(codmarca != null && !"".equals( codmarca ) ){
				sql.append("and pd.codempmc=? and pd.codfilialmc=? and pd.codmarca=? ");	
			}
			// Filtro por Grupo
			if(codgrup != null && !"".equals( codgrup )) {
				sql.append("and pd.codempgp=? and pd.codfilialgp=? and pd.codgrup like ? ");	
			}
		
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;

			ps.setInt( param++, codemptb );
			ps.setInt( param++, codfilialtb );
			ps.setInt( param++, codtab );
			
			if(codplanopag > 0) {
				ps.setInt( param++, codemppg );
				ps.setInt( param++, codfilialpg );
				ps.setInt( param++, codplanopag );
			}
			if(codclascli > 0) {
				ps.setInt( param++, codempcc );
				ps.setInt( param++, codfilialcc );
				ps.setInt( param++, codclascli );
			}
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			if(codprod > 0) {
				ps.setInt( param++, codprod );
			} 			
			
			
			
	/*		if("T".equals( origem )) {
				ps.setInt( param++, codemptb );
				ps.setInt( param++, codfilialtb );
				ps.setInt( param++, codtab );
			}*/
			if(codmarca != null && !"".equals( codmarca ) ){
				ps.setInt( param++, codempmc );
				ps.setInt( param++, codfilialmc );
				ps.setString( param++, codmarca );
			}
			if(codgrup != null && !"".equals( codgrup )) {
				ps.setInt( param++, codempgp );
				ps.setInt( param++, codfilialgp );
				ps.setString( param++, codgrup+"%");
			}
			rs = ps.executeQuery();
		
			while(rs.next()) {
				
				TabelaPreco tabPreco = new TabelaPreco();
				
				if ("I".equals( origem ) || "B".equals( origem )) {
					tabPreco.setCodemp( rs.getInt( "CODEMP" ) );
					tabPreco.setCodfilial( rs.getInt( "CODFILIAL" ) );
					tabPreco.setCodprod( rs.getInt( "CODPROD" ) );
					tabPreco.setCodprecoprod( rs.getInt( "CODPRECOPROD" ) );
			
					tabPreco.setCodemptb( codemptb );
					tabPreco.setCodfilialtb( codfilial );
					tabPreco.setCodtab( codtab );
					
					tabPreco.setCodempcc( codempcc );
					tabPreco.setCodfilialcc( codfilialcc );
					tabPreco.setCodclascli( codclascli );
					
					tabPreco.setCodemppg( codemppg );
					tabPreco.setCodfilialpg( codfilialpg );
					tabPreco.setCodplanopag( codplanopag );
					tabPreco.setTipoprecoprod( origem );
				} else {
					tabPreco.setCodemp( rs.getInt( "CODEMP" ) );
					tabPreco.setCodfilial( rs.getInt( "CODFILIAL" ) );
					tabPreco.setCodprod( rs.getInt( "CODPROD" ) );
					tabPreco.setCodprecoprod( rs.getInt( "CODPRECOPROD" ) );
			
					tabPreco.setCodemptb( rs.getInt( "CODEMP" ) );
					tabPreco.setCodfilialtb( rs.getInt( "CODFILIAL" ) );
					tabPreco.setCodtab( rs.getInt( "CODTAB"	) );
					
					tabPreco.setCodempcc( rs.getInt( "CODEMPCC" ) );
					tabPreco.setCodfilialcc( rs.getInt( "CODFILIALCC" ) );
					tabPreco.setCodclascli( rs.getInt( "CODCLASCLI" ) );
					
					tabPreco.setCodemppg( rs.getInt( "CODEMPPG" ) );
					tabPreco.setCodfilialpg( rs.getInt( "CODFILIALPG" ) );
					tabPreco.setCodplanopag( rs.getInt( "CODPLANOPAG" ) );
					tabPreco.setTipoprecoprod( rs.getString( "TIPOPRECOPROD" ) );
					
				}
			
				if("I".equals( origem )){
					tabPreco.setPrecoprod( getBigDecimal( rs.getBigDecimal( "CUSTOINFOPROD" ) ) );
				} else if("B".equals( origem ) ) { 
					tabPreco.setPrecoprod( getBigDecimal( rs.getBigDecimal( "PRECOBASEPROD" ) ) );
				} else if("T".equals( origem ) ) {
					tabPreco.setPrecoprod( getBigDecimal( rs.getBigDecimal( "PRECOPROD" ) ) );
				} 		
				
				//Se o preço do produto for menor ou igual a zero impede sua atualização
				if ( tabPreco.getPrecoprod().compareTo( new BigDecimal(0) ) <= 0 ) {
					RegistrosErros++;

					if ( Funcoes.mensagemConfirma( null, "O produto " + tabPreco.getCodprod() + " não foi atualizado pois o preço origem está zerado!\n" + "Continuar o processamento?" ) != JOptionPane.YES_OPTION ) {
						break;
					}
				} else {
				
					if ( operador.equals( "/" ) )
						tabPreco.setPrecoprod( tabPreco.getPrecoprod().divide( multiplicando ,  mcPerc ) ); //= Funcoes.arredDouble( dePrecoProd / deMultiplic, iCasasDec );
					else
						tabPreco.setPrecoprod( tabPreco.getPrecoprod().multiply( multiplicando , mcPerc ) ); 
				}
				
				
				//Em caso de atualização da tabela de Preço
				if ("P".equals(tipooper)) {
					if( tabPreco.getCodprecoprod() > 0 ){
						updateTabelaPreco(tabPreco);	
					}
				
					insertTabelaPreco(tabPreco);
				
					RegistrosIncluidos++;
				} else {
					updatePrecoBase(tabPreco);
					RegistroAtualizados ++;
				}
			}
			
			getConn().commit();	
			Funcoes.mensagemInforma( null, "Registros incluídos: " + RegistrosIncluidos + "\n" + "Registros atualizados: " + RegistroAtualizados + "\n" + "Registros com erro: " + RegistrosErros + "\n" + "Total processados: " + ( RegistroAtualizados + RegistrosIncluidos ) );
				
		} catch(Exception e) {
			try {
				getConn().rollback();
			} catch ( SQLException e1 ) {
				e1.printStackTrace();
			}
			Funcoes.mensagemErro(null, "Erro ao buscar preço");
			e.printStackTrace();
		}
		
	}
	
	public void updatePrecoBase(TabelaPreco tab) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "update eqproduto pd set pd.precobaseprod=? where " );
		sql.append( "pd.codemp=? and pd.codfilial=? and pd.codprod=? ");
		
		int param = 1;
		
		PreparedStatement ps =  getConn().prepareStatement( sql.toString() );
		ps.setBigDecimal( param++, tab.getPrecoprod() );
		ps.setInt( param++, tab.getCodemp() );
		ps.setInt( param++, tab.getCodfilial() );
		ps.setInt( param++, tab.getCodprod() );
		
		ps.executeUpdate();
		ps.close();
	}
	
	public void updateTabelaPreco(TabelaPreco tab) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append( "update vdprecoprod pp set pp.ativoprecoprod='N' where " );
		sql.append( "pp.codemp=? and pp.codfilial=? and pp.codprod=? and pp.codprecoprod=? ");
		if(tab.getCodplanopag() > 0) {
			sql.append(" and pp.codemppg=? and pp.codfilialpg=? and pp.codplanopag=? ");
		} else {
			sql.append(" and pp.codemppg is null and pp.codfilialpg is null and pp.codplanopag is null ");
		}
		if(tab.getCodclascli() > 0) {
			sql.append(" and pp.codempcc=? and pp.codfilialcc=? and pp.codclascli=? ");
		} else {
			sql.append(" and pp.codempcc is null and pp.codfilialcc is null and pp.codclascli is null ");
		}
		int param = 1;
		
		PreparedStatement ps =  getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, tab.getCodemp() );
		ps.setInt( param++, tab.getCodfilial() );
		ps.setInt( param++, tab.getCodprod() );
		ps.setInt( param++, tab.getCodprecoprod() );
		if(tab.getCodplanopag() > 0) {
			ps.setInt( param++, tab.getCodemppg() );
			ps.setInt( param++, tab.getCodfilialpg() );
			ps.setInt( param++, tab.getCodplanopag() );
		}
		if(tab.getCodclascli() > 0) {
			ps.setInt( param++, tab.getCodempcc() );
			ps.setInt( param++, tab.getCodfilialcc() );
			ps.setInt( param++, tab.getCodclascli()  );
		}
		ps.executeUpdate();
		ps.close();
		
	}
	
	
	public void insertTabelaPreco(TabelaPreco tab) throws SQLException {
		
		StringBuilder sql = new StringBuilder();
		sql.append( "INSERT INTO VDPRECOPROD  "); 
		sql.append( "(CODEMP,CODFILIAL,CODPROD,CODPRECOPROD,CODEMPTB,CODFILIALTB,CODTAB, "); 
		sql.append( "CODEMPCC,CODFILIALCC,CODCLASCLI,CODEMPPG,CODFILIALPG,CODPLANOPAG,PRECOPROD,TIPOPRECOPROD) "); 
		sql.append( "VALUES (?,?,?,( " );
		sql.append( "COALESCE( (SELECT MAX(CODPRECOPROD)+1 FROM VDPRECOPROD WHERE CODEMP=? AND "); 
		sql.append( "CODFILIAL=? AND CODPROD=?) , 1) "); 
		sql.append( "),?,?,?,?,?,?,?,?,?,?,?) ");
		int param = 1;
		
		PreparedStatement ps =  getConn().prepareStatement( sql.toString() );
		ps.setInt( param++, tab.getCodemp() );
		ps.setInt( param++, tab.getCodfilial() );
		ps.setInt( param++, tab.getCodprod() );
		ps.setInt( param++, tab.getCodemp() );
		ps.setInt( param++, tab.getCodfilial() );
		ps.setInt( param++, tab.getCodprod() );
		
		ps.setInt( param++, tab.getCodemptb() );
		ps.setInt( param++, tab.getCodfilialtb() );
		ps.setInt( param++, tab.getCodtab() );
		
		if(tab.getCodclascli() > 0) {
			ps.setInt( param++, tab.getCodempcc() );
			ps.setInt( param++, tab.getCodfilialcc() );
			ps.setInt( param++, tab.getCodclascli());
		} else {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
		}

		if(tab.getCodplanopag() > 0) {
			ps.setInt( param++, tab.getCodemppg());
			ps.setInt( param++, tab.getCodfilialpg() );
			ps.setInt( param++, tab.getCodplanopag() );	
		} else {
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
			ps.setNull( param++, Types.INTEGER );
		}
		
		ps.setBigDecimal( param++, tab.getPrecoprod() );
		ps.setString( param++, "T" );
		//	sql.setNull( 13, Aplicativo.iCodEmp );
		ps.execute();
		ps.close();
				
	}
	
	
		
/*	public ClienteFor getClienteFor(Integer codemp, Integer codfilial, Integer codcli, 
			Integer codempfr, Integer codfilialfr, Integer codfor, 
			Integer codemptf, Integer codfilialtf, Integer codtipofor) throws SQLException {
		ClienteFor result = new ClienteFor();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  RAZCLI");
		sql.append(", NOMECLI, PESSOACLI, CNPJCLI, CPFCLI, INSCCLI, ENDCLI");
		sql.append(", NUMCLI, BAIRCLI, CODMUNIC, SIGLAUF, CODPAIS, RGCLI");
		sql.append(", DDDCLI, FONECLI, FAXCLI, CELCLI");
		sql.append(" FROM VDCLIENTE" );
		sql.append(" WHERE CODEMP=? AND CODFILIAL=? AND CODCLI=? ");
		
		PreparedStatement ps = getConn().prepareStatement( sql.toString() );
		ps.setInt( 1, codemp );
		ps.setInt( 2, codfilial );
		ps.setInt( 3, codcli );
		ResultSet rs = ps.executeQuery();
		if(rs.next()){

			result.setCodemp( codempfr );
			result.setCodfilial( codfilialfr );
			result.setCodfor( codfor );
			result.setRazfor( rs.getString( "RAZCLI" ) );
			result.setCodemptf( codemptf );
			result.setCodfilialtf( codfilialtf );
			result.setCodtipofor( codtipofor );
			result.setNomefor( rs.getString( "NOMECLI" ) );
			result.setPessoafor( rs.getString( "PESSOACLI" ) );
			result.setCnpjfor( rs.getString( "CNPJCLI" ) );
			result.setCpffor( rs.getString( "CPFCLI" ) );
			result.setInscfor( rs.getString( "INSCCLI" ) );
			result.setEndfor( rs.getString( "ENDCLI" ) );
			result.setNumfor( rs.getInt( "NUMCLI" ) );
			result.setBairfor( rs.getString( "BAIRCLI" ) );
			result.setCodmunic( rs.getString("CODMUNIC") );
			result.setSiglauf( rs.getString( "SIGLAUF" ) );
			result.setCodpais( rs.getInt( "CODPAIS" ) );
			result.setRgfor( rs.getString( "RGCLI" ) );
			result.setDddfonefor( rs.getString( "DDDCLI" ) );
			result.setFonefor( rs.getString( "FONECLI" ) );
			result.setFaxfor( rs.getString( "FAXCLI" ) );
			result.setCelfor( rs.getString( "CELCLI" ) );
	
		}		
		return result;
	}
*/	
	
/*	public Map<String, Object> getPrefere(Integer codemp, Integer codfilial, String usu, Integer codemppf, Integer codfilialpf) throws SQLException {

		Map<String, Object> retorno = new HashMap<String, Object>();
		StringBuilder sSQL = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			sSQL.append( "SELECT P.SETORVENDA, P.RGCLIOBRIG, P.CLIMESMOCNPJ, P.CNPJOBRIGCLI," );
			sSQL.append( "P.CONSISTEIECLI, P.CONSISTCPFCLI, P.CONSISTEIEPF, " );
			sSQL.append( "(CASE WHEN P.USUATIVCLI='N' THEN 'S' " );
			sSQL.append( "WHEN P.USUATIVCLI='S' AND U.ATIVCLI='S' THEN 'S' " );
			sSQL.append( "ELSE 'N' " );
			sSQL.append( "END) HABATIVCLI, COALESCE (P.CODTIPOFOR,0) CODTIPOFOR, USAIBGECLI, USAIBGEFOR, USAIBGETRANSP, BUSCACEP " );
			sSQL.append( "FROM SGPREFERE1 P LEFT OUTER JOIN SGUSUARIO U " );
			sSQL.append( "ON U.CODEMP=? AND U.CODFILIAL=? AND U.IDUSU=? " );
			sSQL.append( "WHERE P.CODEMP=? AND P.CODFILIAL=?" );

			ps = getConn().prepareStatement( sSQL.toString() );
			ps.setInt( 1, codemp );
			ps.setInt( 2, codfilial );
			ps.setString( 3, usu );
			ps.setInt( 4, codemppf );
			ps.setInt( 5, codfilialpf );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				retorno.put( "SETORVENDA", new Boolean( "CA".indexOf( rs.getString( "SetorVenda" ) ) >= 0 ) );
				retorno.put( "RGCLIOBRIG", new Boolean( "S".equals( rs.getString( "RGCLIOBRIG" ) ) ) );
				retorno.put( "CLIMESMOCNPJ", new Boolean( "S".equals( rs.getString( "CLIMESMOCNPJ" ) ) ) );
				retorno.put( "CLIMESMOCNPJ", new Boolean( "S".equals( rs.getString( "CLIMESMOCNPJ" ) ) ) );
				retorno.put( "CONSISTEIECLI", new Boolean( "S".equals( rs.getString( "CONSISTEIECLI" ) ) ) );
				retorno.put( "CONSISTCPFCLI", new Boolean( "S".equals( rs.getString( "CONSISTCPFCLI" ) ) ) );
				retorno.put( "HABATIVCLI", new Boolean( "S".equals( rs.getString( "HABATIVCLI" ) ) ) );
				retorno.put( "CODTIPOFOR", rs.getInt( "CODTIPOFOR" ) );
				retorno.put( "USAIBGECLI", new Boolean( "S".equals( rs.getString( "USAIBGECLI" ) ) ) );
				retorno.put( "USAIBGEFOR", new Boolean( "S".equals( rs.getString( "USAIBGEFOR" ) ) ) );
				retorno.put( "USAIBGETRANSP", new Boolean( "S".equals( rs.getString( "USAIBGETRANSP" ) ) ) );
				retorno.put( "BUSCACEP", new Boolean( "S".equals( rs.getString( "BUSCACEP" ) ) ) );
				retorno.put( "CONSISTEIEPF", new Boolean( "S".equals( rs.getString( "CONSISTEIEPF" ) ) ) );

			}

			rs.close();
			ps.close();

			getConn().commit();
	
		} finally {
			sSQL = null;
			ps = null;
			rs = null;
		}
		return retorno;
	}
*/
}


