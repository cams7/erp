package org.freedom.modulos.crm.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.business.object.FichaOrc;
import org.freedom.modulos.crm.business.object.Orcamento;


public class DAOFicha extends AbstractDAO {
	
	private Object prefs[] = null;
	
	public DAOFicha( DbConnection cn) {

		super( cn );
	}
	
	public StringBuilder getSqlFichaAval(boolean temitens) {
		StringBuilder sql= new StringBuilder();
		
		sql.append("select f.razfilial, f.dddfilial, f.fonefilial "); 
		sql.append(", f.endfilial, f.numfilial, f.siglauf siglauff ");  
		sql.append(", f.bairfilial, f.cnpjfilial,f.emailfilial, f.wwwfranqueadora, f.unidfranqueada, f.marcafranqueadora "); 
		sql.append(", m.nomemunic nomemunicf ");  
		sql.append(", c.codemp codempct, c.codfilial codfilialct, c.codcto, c.razcto, c.endcto, c.numcto, c.complcto, c.baircto ");  
		sql.append(", c.siglauf siglaufc, c.cpfcto, c.dddcto ");  
		sql.append(", c.fonecto, c.cnpjcto, c.celcto  "); 
		sql.append(", c.contcto, mc.nomemunic nomemunicc, c.pessoacto, c.emailcto, c.edificiocto, c.codorigcont, oc.descorigcont ");  
		sql.append(", fa.codemp codempfa, fa.codfilial codfilialfa, fa.seqfichaaval, fa.codmotaval, ma.descmotaval, fa.dtfichaaval ");  
		sql.append(", fa.localfichaaval, fa.predentrfichaaval, fa.andarfichaaval, fa.pontoreffichaaval ");  
		sql.append(", fa.cobertfichaaval, fa.estrutfichaaval, fa.ocupadofichaaval, fa.mobilfichaaval, fa.janelafichaaval ");
		sql.append(", fa.qtdjanelafichaaval, fa.qtdsacadafichaaval, fa.descoutrosfichaaval ");
		sql.append(", fa.finalicrifichaaval, fa.finalianifichaaval, fa.finalioutfichaaval ");
		sql.append(", fa.sacadafichaaval, fa.outrosfichaaval, fa.obsfichaaval, fa.ocupadofichaaval, fa.codvend, vd.nomevend, fa.DtVisitaFichaAval, fa.hvisitafichaaval ");  
		if (temitens) {
			sql.append(", itfa.seqitfichaaval, itfa.codprod , pd.descprod   ");
			sql.append(", itfa.altitfichaaval ");
			sql.append(", itfa.compitfichaaval, itfa.m2itfichaaval, itfa.codambaval, am.descambaval, itfa.descitfichaaval ");
			sql.append(", itfa.vlrtotitfichaaval, itfa.vlrunititfichaaval, itfa.tiritfichaaval ");
			sql.append(", itfa.codvarg1, itfa.seqitvarg1, v1.descitvarg DESCVARG1 ");
			sql.append(", itfa.codvarg2, itfa.seqitvarg2, v2.descitvarg DESCVARG2 ");
			sql.append(", itfa.codvarg3, itfa.seqitvarg3, v3.descitvarg DESCVARG3 ");
			sql.append(", itfa.codvarg4, itfa.seqitvarg4, v4.siglaitvarg SIGLAITVARG4 ");
			sql.append(", itfa.codvarg5, itfa.seqitvarg5, v5.siglaitvarg SIGLAITVARG5 ");
			sql.append(", itfa.codvarg6, itfa.seqitvarg6, v6.siglaitvarg SIGLAITVARG6 ");
			sql.append(", itfa.codvarg7, itfa.seqitvarg7, v7.siglaitvarg SIGLAITVARG7 ");
			sql.append(", itfa.codvarg8, itfa.seqitvarg8, v8.siglaitvarg SIGLAITVARG8 ");
		}
		sql.append("from sgfilial f   ");
		sql.append("left outer join sgmunicipio m on ");  
		sql.append("m.codmunic=f.codmunic and m.codpais=f.codpais ");  
		sql.append("and m.siglauf=f.siglauf ");  
		sql.append("inner join tkcontato c on ");  
		sql.append("c.codemp=? and c.codfilial=? and c.codcto=? ");
		sql.append("left outer join sgmunicipio mc on ");  
		sql.append("mc.codmunic=c.codmunic and mc.codpais=c.codpais ");  
		sql.append("and mc.siglauf=c.siglauf "); 
		sql.append("left outer join tkorigcont oc on ");  
		sql.append("oc.codemp=c.codempoc and oc.codfilial=c.codfilialoc and oc.codorigcont=c.codorigcont ");  
		sql.append("left outer join crfichaaval fa on ");  
		sql.append("fa.codemp=? and fa.codfilial=? and fa.seqfichaaval=? ");
		sql.append("and fa.codempco = c.codemp and fa.codfilialco= c.codfilial and fa.codcto=c.codcto ");
		sql.append("left outer join crmotivoaval ma  on ");
		sql.append("ma.codemp = fa.codempma and ma.codfilial= fa.codfilialma and ma.codmotaval= fa.codmotaval ");
		sql.append("left outer join vdvendedor vd on ");  
		sql.append("vd.codemp = fa.codempvd and vd.codfilial= fa.codfilialvd and vd.codvend= fa.codvend ");
		
		if (temitens) {		
			sql.append("left outer join critfichaaval itfa on ");  
			sql.append("itfa.codemp = fa.codemp and itfa.codfilial= fa.codfilial and itfa.seqfichaaval= fa.seqfichaaval ");  
			sql.append("left outer join crambienteaval am  on ");
			sql.append("am.codemp = itfa.codempam and am.codfilial= itfa.codfilialam and am.codambaval= itfa.codambaval ");
			sql.append("left outer join eqproduto  pd on ");  
			sql.append("pd.codemp = itfa.codemppd and pd.codfilial= itfa.codfilialpd and pd.codprod= itfa.codprod ");
			sql.append("left outer join eqitvargrade  v1 on ");
			sql.append("v1.codemp = itfa.codempv1 and v1.codfilial= itfa.codfilialv1 and v1.codvarg= itfa.codvarg1 and v1.seqitvarg = itfa.seqitvarg1 ");
			sql.append("left outer join eqitvargrade  v2 on ");
			sql.append("v2.codemp = itfa.codempv2 and v2.codfilial= itfa.codfilialv2 and v2.codvarg= itfa.codvarg2 and v2.seqitvarg = itfa.seqitvarg2 ");
			sql.append("left outer join eqitvargrade  v3 on ");
			sql.append("v3.codemp = itfa.codempv3 and v3.codfilial= itfa.codfilialv3 and v3.codvarg= itfa.codvarg3 and v3.seqitvarg = itfa.seqitvarg3 ");
			sql.append("left outer join eqitvargrade  v4 on ");
			sql.append("v4.codemp = itfa.codempv4 and v4.codfilial= itfa.codfilialv4 and v4.codvarg= itfa.codvarg4 and v4.seqitvarg = itfa.seqitvarg4 ");
			sql.append("left outer join eqitvargrade  v5 on ");
			sql.append("v5.codemp = itfa.codempv5 and v5.codfilial= itfa.codfilialv5 and v5.codvarg= itfa.codvarg5 and v5.seqitvarg = itfa.seqitvarg5 ");
			sql.append("left outer join eqitvargrade  v6 on ");
			sql.append("v6.codemp = itfa.codempv6 and v6.codfilial= itfa.codfilialv6 and v6.codvarg= itfa.codvarg6 and v6.seqitvarg = itfa.seqitvarg6 ");
			sql.append("left outer join eqitvargrade  v7 on ");
			sql.append("v7.codemp = itfa.codempv7 and v7.codfilial= itfa.codfilialv7 and v7.codvarg= itfa.codvarg7 and v7.seqitvarg = itfa.seqitvarg7 ");
			sql.append("left outer join eqitvargrade  v8 on ");
			sql.append("v8.codemp = itfa.codempv8 and v8.codfilial= itfa.codfilialv8 and v8.codvarg= itfa.codvarg8 and v8.seqitvarg = itfa.seqitvarg8 ");
		}
		sql.append("where f.codemp=? and f.codfilial=? ");
 		
		return sql;
	}
	
	public String geraCli(){
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT IRET FROM TKCONTCLISP(?,?,?,?,?,?,?,?,?)" );	
		
		return sql.toString();
	}
	
	public Integer getCliente(int codemp, int codfilial, int codcto) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		Integer codcli = null;
		
		try {
			sql = new StringBuilder();
			sql.append( "select c.codcli from tkcontcli c where c.codempcto=? and c.codfilialcto =? and c.codcto=?" );
			
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcto );
						
			rs = ps.executeQuery();
			
			if(rs.next()){
				codcli = rs.getInt( "CODCLI" );
			}
			
		} finally {
			rs = null;
			ps = null;
			sql = null;
		}
		
		return codcli;
	}
	
	public Integer getTransp(Integer codemp, Integer codfilial, Integer codcli) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs =null;
		Integer codtran = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select c.codtran from vdcliente c where c.codemp=? and c.codfilial=? and c.codcli=?");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcli );
			rs = ps.executeQuery();
			
			if(rs.next()){
				codtran = rs.getInt( "codtran" );
			}
		
		} finally {
			rs.close();
			ps.close();
		}
			
		return codtran;
	}
	
	public BigDecimal getPrecoBase(Integer codemp, Integer codfilial, Integer codprod) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs =null;
		BigDecimal precobase = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select eq.precobaseprod from eqproduto eq where eq.codemp=? and eq.codfilial=? and eq.codprod=?");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codprod );
			rs = ps.executeQuery();
			
			if(rs.next()){
				precobase = rs.getBigDecimal( "precobaseprod" );
			}
		
		} finally {
			rs.close();
			ps.close();
		}
			
		return precobase;
	}
	
	public Integer getCodAlmox(Integer codemp, Integer codfilial, Integer codprod) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs =null;
		Integer codalmox = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select eq.codalmox from eqproduto eq where eq.codemp=? and eq.codfilial=? and eq.codprod=?");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codprod );
			rs = ps.executeQuery();
			
			if(rs.next()){
				codalmox = rs.getInt(  "codalmox" );
			}
		
		} finally {
			rs.close();
			ps.close();
		}
			
		return codalmox;
	}
	
	public Integer getVendedor(Integer codemp, Integer codfilial, Integer codcli) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs =null;
		Integer codcomiss = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select c.codvend from vdcliente c where c.codemp=? and c.codfilial=? and c.codcli=?");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, codcli );
			rs = ps.executeQuery();
			
			if(rs.next()){
				codcomiss = rs.getInt( "codvend" );
			}
		
		} finally {
			rs.close();
			ps.close();
		}

		return codcomiss;
	}
	
	public BigDecimal getVlrProdOrc(Integer codemp, Integer codfilial, Integer seqficha) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs =null;
		BigDecimal vlrprodorc = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(vlrtotitfichaaval) vlrtotitfichaaval from critfichaaval where codemp=? and codfilial=? and seqfichaaval=?");
			ps = getConn().prepareStatement( sql.toString() );
			int param = 1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			ps.setInt( param++, seqficha );
			rs = ps.executeQuery();
			
			if(rs.next()){
				vlrprodorc = rs.getBigDecimal( "vlrtotitfichaaval" );
			}
		
		} finally {
			rs.close();
			ps.close();
		}

		return vlrprodorc;
	}
	
	public Integer gravaCabOrc(Integer codemp, Integer codfilial, Integer seqficha, Integer codcto, Date dtorc, Date dtvencorc, Integer codplanopag, Integer codtranpf, Integer codvendpf, Integer codcli, Integer codempvd , Integer codfilialvd, Integer codvend) throws SQLException{
		
		//Integer codvend = null;
		Integer codorc = null;
		Integer codtran = null;	
		BigDecimal vlrprodorc = null;
		
		if(codcli == 0){
			
		}

		codtran = getTransp( codemp, codfilial, codcli );
		if(codtran<=0){
			codtran = codtranpf;
		}
		
		//Se vendedor não foi marcado na ficha aval. vai buscar vendedor no cadastro do cliente e se continuar sem vendedor, por ultimo, vai pegar no preferência geral.
		if(codvend<=0){
			codvend = getVendedor( codemp, codfilial, codcli );
			if(codvend<=0){
				codvend = codvendpf;
			}
		}
		
		vlrprodorc = getVlrProdOrc(codemp, codfilial, seqficha);
		
		if ( ( (Boolean) prefs[ FichaOrc.PREFS.USAORCSEQ.ordinal() ] ).booleanValue() ) {
		codorc = testaCodPK("VDORCAMENTO");
		} else {
			codorc = loadUltCodOrc();	
		}
			
		Orcamento orcamento = new Orcamento();
		orcamento.setCodemp( codemp );
		orcamento.setCodfilial( codfilial );
		orcamento.setTipoorc("O");
		orcamento.setCodorc( codorc );
		orcamento.setCodempcl( Aplicativo.iCodEmp );
		orcamento.setCodfilialcl( ListaCampos.getMasterFilial( "VDCLIENTE" ) );
		orcamento.setCodcli( codcli );
		orcamento.setDtorc( dtorc );
		orcamento.setDtvencorc( dtvencorc );
		orcamento.setCodempvd( Aplicativo.iCodEmp );
		orcamento.setCodfilialvd( ListaCampos.getMasterFilial( "VDVEND" )  );
		orcamento.setCodvend( codvend );
		orcamento.setCodemppg( Aplicativo.iCodEmp );
		orcamento.setCodfilialpg( ListaCampos.getMasterFilial( "FNPLANOPAG" )  );
		orcamento.setCodplanopag( codplanopag );
		orcamento.setCodemptn( Aplicativo.iCodEmp );
		orcamento.setCodfilialtn( ListaCampos.getMasterFilial( "VDTRANSP" )  );
		orcamento.setCodtran( codtran );
		orcamento.setStatusorc( "*" );
		orcamento.setVlrprodorc( vlrprodorc );
		
		insert_orc( orcamento );
		
		return codorc;
		
	}
	
	
	public Integer testaCodPK( String sTabela ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer retorno = new Integer( 0 );

		try {
			ps = getConn().prepareStatement( "SELECT ISEQ FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );

			if ( sTabela.equals( "VDVENDA" ) ) {
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setString( 3, "VD" );
			}
			else if ( sTabela.equals( "VDORCAMENTO" ) ) {
				ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
				ps.setString( 3, "OC" );
			}

			rs = ps.executeQuery();
			rs.next();

			retorno = new Integer( rs.getString( 1 ) );

			rs.close();
			ps.close();

			getConn().commit();

		} catch ( SQLException err ) {
			//Funcoes.mensagemErro( this, "Erro ao confirmar número do pedido!\n" + err.getMessage(), true, getConn(), err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
		}

		return retorno;

	}
		
	public void insert_orc(Orcamento orc) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		sql.append( "INSERT INTO VDORCAMENTO ( ")
		   .append("CODEMP, CODFILIAL, TIPOORC, CODORC, DTORC, DTVENCORC, " )
		   .append("CODEMPCL, CODFILIALCL, CODCLI,") 
		   .append("CODEMPVD, CODFILIALVD, CODVEND, ")
		   .append("CODEMPPG, CODFILIALPG, CODPLANOPAG, ")
		   .append("CODEMPTN, CODFILIALTN, CODTRAN, STATUSORC ) ")
		   .append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
		 //  .append("CODEMPTN, CODFILIALTN, CODTRAN, STATUSORC,VLRPRODORC ) ")
		  // .append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
		
		ps = getConn().prepareStatement( sql.toString() );
		
		ps.setInt( Orcamento.INSERT_ORC.CODEMP.ordinal() , orc.getCodemp() );
		ps.setInt( Orcamento.INSERT_ORC.CODFILIAL.ordinal() , orc.getCodfilial() );
		ps.setString( Orcamento.INSERT_ORC.TIPOORC.ordinal() , orc.getTipoorc() );
		ps.setInt( Orcamento.INSERT_ORC.CODORC.ordinal() , orc.getCodorc() );
		ps.setDate( Orcamento.INSERT_ORC.DTORC.ordinal() , Funcoes.dateToSQLDate( orc.getDtorc() ));
		ps.setDate( Orcamento.INSERT_ORC.DTVENCORC.ordinal() ,Funcoes.dateToSQLDate( orc.getDtvencorc()) );
		ps.setInt( Orcamento.INSERT_ORC.CODEMPCL.ordinal() , orc.getCodempcl() );
		ps.setInt( Orcamento.INSERT_ORC.CODFILIALCL.ordinal() , orc.getCodfilialcl() );
		ps.setInt( Orcamento.INSERT_ORC.CODCLI.ordinal() , orc.getCodcli() );
		ps.setInt( Orcamento.INSERT_ORC.CODEMPVD.ordinal() , orc.getCodempvd() );
		ps.setInt( Orcamento.INSERT_ORC.CODFILIALVD.ordinal() , orc.getCodfilialvd() );
		ps.setInt( Orcamento.INSERT_ORC.CODVEND.ordinal() , orc.getCodvend() );
		ps.setInt( Orcamento.INSERT_ORC.CODEMPPG.ordinal() , orc.getCodemppg() );
		ps.setInt( Orcamento.INSERT_ORC.CODFILIALPG.ordinal() , orc.getCodfilialpg() );
		ps.setInt( Orcamento.INSERT_ORC.CODPLANOPAG.ordinal() , orc.getCodplanopag() );
		ps.setInt( Orcamento.INSERT_ORC.CODEMPTN.ordinal() , orc.getCodemptn() );
		ps.setInt( Orcamento.INSERT_ORC.CODFILIALTN.ordinal() , orc.getCodfilialtn() );
		ps.setInt( Orcamento.INSERT_ORC.CODTRAN.ordinal() , orc.getCodtran() );
		ps.setString(Orcamento.INSERT_ORC.STATUSORC.ordinal() , orc.getStatusorc() );
	//	ps.setBigDecimal( Orcamento.INSERT_ORC.VLRPRODORC.ordinal() , orc.getVlrprodorc() );
		
		ps.executeUpdate();
		ps.close();
		
	}
	
	
	public void insert_item_orc( Integer codorc,  Integer codempfi, Integer codfilialfi, Integer seqfichaaval) throws SQLException {
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		sql.append( "INSERT INTO VDITORCAMENTO ( ")
		   .append("CODEMP, CODFILIAL, TIPOORC, CODORC, CODITORC, " )
		   .append("CODEMPPD, CODFILIALPD, CODPROD,") 
		   .append("CODEMPAX, CODFILIALAX, CODALMOX, QTDITORC, PRECOITORC, VLRPRODITORC, VLRLIQITORC  ) " )
		   .append("SELECT FI.CODEMP, FI.CODFILIAL, 'O', ? , FI.SEQITFICHAAVAL, "  )
		   .append("FI.CODEMPPD, FI.CODFILIALPD, FI.CODPROD, PD.CODEMPAX, PD.CODFILIALAX, PD.CODALMOX, "  )
		   .append("FI.M2ITFICHAAVAL, FI.VLRUNITITFICHAAVAL, FI.VLRTOTITFICHAAVAL, FI.VLRTOTITFICHAAVAL "  )
		   .append("FROM CRITFICHAAVAL FI ")
		   .append("LEFT OUTER JOIN EQPRODUTO PD ON "  )
		   .append("PD.CODEMP=FI.CODEMPPD AND PD.CODFILIAL = FI.CODFILIALPD AND PD.CODPROD=FI.CODPROD "  )
		   .append("WHERE FI.CODEMP=? AND FI.CODFILIAL=? AND FI.SEQFICHAAVAL=? " );
		 
		ps2 = getConn().prepareStatement( sql.toString() );
		int param = 1;
		ps2.setInt( param++ , codorc);
		ps2.setInt( param++ , codempfi );
		ps2.setInt( param++ , codfilialfi );
		ps2.setInt( param++ , seqfichaaval );
		
		/*
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODEMP.ordinal() , item.getCodemp() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODFILIAL.ordinal() , item.getCodfilial() );
		ps.setString( ItOrcamento.INSERT_ITEM_ORC.TIPOORC.ordinal() , item.getTipoorc() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODORC.ordinal() , item.getCodorc() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODITORC.ordinal() , item.getCoditorc());
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODEMPPD.ordinal() , item.getCodemppd() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODFILIALPD.ordinal() , item.getCodfilialpd() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODPROD.ordinal() , item.getCodprod() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODEMPAX.ordinal() , item.getCodempax() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODFILIALAX.ordinal() , item.getCodfilialax() );
		ps.setInt( ItOrcamento.INSERT_ITEM_ORC.CODALMOX.ordinal() , item.getCodalmox() );
		ps.setBigDecimal(  ItOrcamento.INSERT_ITEM_ORC.QTDITORC.ordinal() , item.getQtditorc() );
		ps.setBigDecimal( ItOrcamento.INSERT_ITEM_ORC.PRECOITORC.ordinal() , item.getPrecoitorc() );
		*/
		ps2.executeUpdate();
		ps2.close();
		

		
	}
	
	public void insert_fichaorc( String tipoorc, Integer codorc, Integer codempfi, Integer codfilialfi, Integer seqfichaaval ) throws SQLException{
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps3 = null;

		sql.append(" INSERT INTO CRFICHAORC ( CODEMP, CODFILIAL, SEQFICHAAVAL, SEQITFICHAAVAL, CODEMPOR, CODFILIALOR, TIPOORC, CODORC, CODITORC ) ");
		sql.append(" SELECT FI.CODEMP, FI.CODFILIAL, FI.SEQFICHAAVAL, FI.SEQITFICHAAVAL, FI.CODEMP CODEMPOR, FI.CODFILIAL CODFILIALOR, ");
		sql.append(" ?, ? , FI.SEQITFICHAAVAL CODITORC ");
		sql.append(" FROM CRITFICHAAVAL FI WHERE FI.CODEMP=? AND FI.CODFILIAL=? AND FI.SEQFICHAAVAL=? ");
		
		ps3 = getConn().prepareStatement( sql.toString() );
		
		ps3.setString( FichaOrc.INSERT_FICHAORC.TIPOORC.ordinal() , tipoorc );
		ps3.setInt( FichaOrc.INSERT_FICHAORC.CODORC.ordinal() , codorc );
		ps3.setInt( FichaOrc.INSERT_FICHAORC.CODEMP.ordinal() ,codempfi );
		ps3.setInt( FichaOrc.INSERT_FICHAORC.CODFILIAL.ordinal() , codfilialfi );
		ps3.setInt( FichaOrc.INSERT_FICHAORC.SEQFICHAAVAL.ordinal() , seqfichaaval );
	
		ps3.executeUpdate();
		ps3.close();
		
	}
	
	public Integer loadUltCodOrc() throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psOrc = null;
		ResultSet rsOrc = null;
		Integer codorc = null;
		 
		try {
			StringBuilder sql = new StringBuilder();
			sql.append( "select max(o.codorc) + 1 codorc from vdorcamento o " );
			ps = getConn().prepareStatement( sql.toString() );
			rs = ps.executeQuery();
			if(rs.next()){
				codorc = rs.getInt( "codorc" );
			}

		}finally{
			ps.close();
			rs.close();
		}
		return codorc;
	}
	/*
	public ArrayList<Orcamento> loadOrcamento(Integer codemp, Integer codfilial, Integer seqfichaaval) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs =null;
		Orcamento result = null;
		StringBuilder sql = new StringBuilder();
		ArrayList<Orcamento> itens_orc = new ArrayList<Orcamento>();
		
		sql.append(" select o.codorc, o.codempcl, o.codfilialcl, o.codcli, c.razcli, o.dtorc, ")
		   .append( "o.dtvencorc, o.codemppg, o.codfilialpg, o.codplanopag, p.descplanopag, ")
		   .append(" it.coditorc, it.qtditorc, it.precoitorc, o.tipoorc ")
		   .append(" from crfichaorc fo, vdorcamento o, vdcliente c, fnplanopag p, vditorcamento it ")
		   .append(" where fo.codemp=? and fo.codfilial=? and fo.seqfichaaval=? and ")
		   .append(" it.codemp=fo.codemp and it.codfilial=fo.codfilial and ")
		   .append(" it.codorc=fo.codorc and it.tipoorc=fo.tipoorc and it.coditorc= fo.coditorc and ")
		   .append(" o.codemp=it.codemp and o.codfilial=it.codfilial and o.codorc=it.codorc and o.tipoorc=it.tipoorc and ")
		   .append(" c.codemp=o.codempcl and c.codfilial=o.codfilialcl and c.codcli=o.codcli and ")
		   .append(" p.codemp=o.codemppg and p.codfilial=o.codfilialpg and p.codplanopag=o.codplanopag ")
		   .append(" order by fo.codorc, fo.coditorc ");
		
		ps = getConn().prepareStatement( sql.toString() );
		int param = 1;
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setInt( param++, seqfichaaval );
		rs = ps.executeQuery();
		
		while(rs.next()){
			result = new Orcamento();
			result.setCodemp( codemp );
			result.setCodfilial( codfilial );
			result.setCodorc( rs.getInt( "codorc" ) );
			result.setCoditorc( rs.getInt( "coditorc" ));
			result.setCodempcl( rs.getInt( "codempcl" ) );
			result.setCodfilialcl( rs.getInt( "codfilialcl" ) );
			result.setCodcli( rs.getInt( "codcli" ) );
			result.setDtorc( rs.getDate("dtorc" ) );
			result.setDtvencorc( rs.getDate("dtvencorc") );
			result.setCodemppg( rs.getInt( "codemppg" ) );
			result.setCodfilialpg( rs.getInt( "codfilialpg" ) );
			result.setCodplanopag( rs.getInt( "codplanopag" ) );
			result.setQtditorc( rs.getBigDecimal( "qtditorc" ));
			result.setPrecoitorc(rs.getBigDecimal("precoitorc"));
			result.setTipoorc(rs.getString( "tipoorc" ));	
			
			itens_orc.add( result );
		}
		return itens_orc;
	}
	*/
	
	public ArrayList<Orcamento> loadOrcamento(Integer codemp, Integer codfilial, Integer seqfichaaval) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs =null;
		Orcamento result = null;
		StringBuilder sql = new StringBuilder();
		ArrayList<Orcamento> itens_orc = new ArrayList<Orcamento>();
		
		sql.append(" select o.codorc, o.codempcl, o.codfilialcl, o.codcli, c.razcli, o.dtorc, ")
		   .append( "o.dtvencorc, o.codemppg, o.codfilialpg, o.codplanopag, p.descplanopag, ")
		   .append(" o.vlrprodorc, o.tipoorc, count(*) qtditens ")
		   .append(" from crfichaorc fo, vdorcamento o, vdcliente c, fnplanopag p ")
		   .append(" where fo.codemp=? and fo.codfilial=? and fo.seqfichaaval=? and ")
		   .append(" o.codemp=fo.codemp and o.codfilial=fo.codfilial and ")
		   .append(" o.codorc=fo.codorc and o.tipoorc=fo.tipoorc and ")
		   .append(" c.codemp=o.codempcl and c.codfilial=o.codfilialcl and c.codcli=o.codcli and ")
		   .append(" p.codemp=o.codemppg and p.codfilial=o.codfilialpg and p.codplanopag=o.codplanopag ")
		   .append(" group by 1,2,3,4,5,6,7,8,9,10,11,12,13 ")
		   .append(" order by o.codorc ");

		
		ps = getConn().prepareStatement( sql.toString() );
		int param = 1;
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setInt( param++, seqfichaaval );
		rs = ps.executeQuery();
		
		while(rs.next()){
			result = new Orcamento();
			result.setCodemp( codemp );
			result.setCodfilial( codfilial );
			result.setCodorc( rs.getInt( "codorc" ) );
			result.setCodempcl( rs.getInt( "codempcl" ) );
			result.setCodfilialcl( rs.getInt( "codfilialcl" ) );
			result.setCodcli( rs.getInt( "codcli" ) );
			result.setDtorc( rs.getDate("dtorc" ) );
			result.setDtvencorc( rs.getDate("dtvencorc") );
			result.setCodemppg( rs.getInt( "codemppg" ) );
			result.setCodfilialpg( rs.getInt( "codfilialpg" ) );
			result.setCodplanopag( rs.getInt( "codplanopag" ) );
			result.setPrecoitorc(rs.getBigDecimal("vlrprodorc"));
			result.setTipoorc(rs.getString( "tipoorc" ));	
			result.setQtditens(rs.getInt( "qtditens" ));
			
			itens_orc.add( result );
		}
		return itens_orc;
	}
	
	
	public String buscaDesc(Integer codvarg) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select descvarg from eqvargrade where codemp=? and codfilial=? and codvarg=? ");
		String desc = null;
		
		try {
			int param = 1;
			ps = getConn().prepareStatement(sql.toString());
			ps.setInt(param++, Aplicativo.iCodEmp);
			ps.setInt(param++, ListaCampos.getMasterFilial("EQVARGRADE"));
			ps.setInt(param++, codvarg);
			rs = ps.executeQuery();

			if(rs.next()) {
				desc = rs.getString( "descvarg" );
			}

			rs.close();
			ps.close();

		}
		catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemErro(null, "Erro ao buscar descrição da combobox");
		}
		return desc;
	}
	
	
	public HashMap<String, Vector<Object>> montaComboFicha( Integer codvarg, String textonulo) {

		Vector<Object> vVals = new Vector<Object>();
		Vector<Object> vLabs = new Vector<Object>();
		HashMap<String, Vector<Object>> ret = new HashMap<String, Vector<Object>>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append("select v.seqitvarg, v.descitvarg from eqitvargrade v where v.codemp=? and v.codfilial=? and v.codvarg=? order by v.descitvarg");
		
		try {
			int param = 1;
			ps = getConn().prepareStatement(sql.toString());
			ps.setInt(param++, Aplicativo.iCodEmp);
			ps.setInt(param++, ListaCampos.getMasterFilial("EQITVARGRADE"));
			ps.setInt(param++, codvarg);
			rs = ps.executeQuery();

			vVals.addElement(0);
			vLabs.addElement(textonulo);

			while (rs.next()) {
				vVals.addElement(new Integer(rs.getInt("seqitvarg")));
				vLabs.addElement(rs.getString("descitvarg"));
			}

			ret.put("VAL", vVals);
			ret.put("LAB", vLabs);

			rs.close();
			ps.close();

			getConn().commit();

		}
		catch (SQLException err) {
			err.printStackTrace();
			Funcoes.mensagemErro(null, "Erro ao buscar dados do tabela EqItVarGrade");
		}
		return ret;
	}
	
	public void setPrefs(Integer codemp, Integer codfilial) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		
		prefs = new Object[ FichaOrc.PREFS.values().length];
		
		try {
			sql = new StringBuilder(" select  p1.codtran, p1.codvend , p1.UsaOrcSeq, p3.usactoseq, p3.layoutfichaaval, p3.layoutprefichaaval, p4.codplanopag ");
            sql.append(" , p3.codvarg1, p3.codvarg2, p3.codvarg3, p3.codvarg4, p3.codvarg5, p3.codvarg6, p3.codvarg7, p3.codvarg8 ");
            sql.append("from sgprefere1 p1, sgprefere3 p3 , sgprefere4 p4 ");
            sql.append("where p1.codemp=? and p1.codfilial=? and p3.codemp=p1.codemp and p3.codfilial=p1.codfilial  and p4.codemp=p3.codemp and p4.codfilial=p3.codfilial ");
			
			ps = getConn().prepareStatement( sql.toString() );
			int param=1;
			ps.setInt( param++, codemp );
			ps.setInt( param++, codfilial );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				
				prefs[ FichaOrc.PREFS.USACTOSEQ.ordinal() ] = rs.getString( FichaOrc.PREFS.USACTOSEQ.toString() );
				prefs[ FichaOrc.PREFS.USAORCSEQ.ordinal() ] = new Boolean( rs.getString( "UsaOrcSeq" ).equals( "S" ) );
				prefs[ FichaOrc.PREFS.LAYOUTFICHAAVAL.ordinal() ] = rs.getString( FichaOrc.PREFS.LAYOUTFICHAAVAL.toString() );
				prefs[ FichaOrc.PREFS.LAYOUTPREFICHAAVAL.ordinal() ] = rs.getString( FichaOrc.PREFS.LAYOUTPREFICHAAVAL.toString() );
				prefs[ FichaOrc.PREFS.CODPLANOPAG.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODPLANOPAG.toString() );
				prefs[ FichaOrc.PREFS.CODVARG1.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG1.toString() );
				prefs[ FichaOrc.PREFS.CODVARG2.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG2.toString() );
				prefs[ FichaOrc.PREFS.CODVARG3.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG3.toString() );
				prefs[ FichaOrc.PREFS.CODVARG4.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG4.toString() );
				prefs[ FichaOrc.PREFS.CODVARG5.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG5.toString() );
				prefs[ FichaOrc.PREFS.CODVARG6.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG6.toString() );
				prefs[ FichaOrc.PREFS.CODVARG7.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG7.toString() );
				prefs[ FichaOrc.PREFS.CODVARG8.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVARG8.toString() );
				prefs[ FichaOrc.PREFS.CODTRAN.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODTRAN.toString() );
				prefs[ FichaOrc.PREFS.CODVEND.ordinal() ] = rs.getInt(  FichaOrc.PREFS.CODVEND.toString() );
				
			}
			rs.close();
			ps.close();
			getConn().commit();
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
	}
	
	public Object[] getPrefs() {
		return this.prefs;
	}

}
