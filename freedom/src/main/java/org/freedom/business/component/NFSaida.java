/**
 * @version 08/08/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.layout <BR>
 *         Classe:
 * @(#)NFSaida.java <BR>
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
 *                  Comentários sobre a classe...
 * 
 */

package org.freedom.business.component;

import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.NF;
import org.freedom.library.component.TabVector;
import org.freedom.library.functions.Funcoes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class NFSaida extends NF {

	protected int tipoNF = TPNF_SAIDA;

	public NFSaida( int casasDec ) {

		super( casasDec );
	}

	public int getTipoNF() {

		return this.tipoNF;
	}

	public boolean carregaTabelas( DbConnection con, Vector<?> parans ) {

		boolean retorno = true;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		try {

			setConexao( con );
			sql.append( "SELECT V.CODVENDA, V.CODCLI, C.RAZCLI, C.CNPJCLI, C.CPFCLI, C.ENDCLI, C.NUMCLI, C.COMPLCLI, C.BAIRCLI, C.CEPCLI, " );
			sql.append( "C.CIDCLI, C.UFCLI, C.FONECLI, C.FAXCLI, C.DDDCLI, C.INSCCLI, C.RGCLI, C.EMAILCLI, C.SITECLI, C.CONTCLI, " );
			sql.append( "V.DTEMITVENDA, V.DOCVENDA, C.INCRACLI, V.DTSAIDAVENDA, V.CODPLANOPAG, PG.DESCPLANOPAG, V.OBSVENDA, VEND.NOMEVEND, VEND.EMAILVEND," );
			sql.append( "(SELECT F.DESCFUNC FROM RHFUNCAO F WHERE F.CODFUNC=VEND.CODFUNC AND F.CODEMP=VEND.CODEMPFU AND F.CODFILIAL=VEND.CODFILIALFU)," );
			sql.append( "V.CODCLCOMIS, V.PERCCOMISVENDA, V.CODVEND, C.ENDCOB, C.CIDCOB, C.NUMCOB, C.UFCOB, C.BAIRCOB, V.PERCMCOMISVENDA ,C.NOMECLI ," );
			sql.append( "C.ENDENT, C.NUMENT, C.COMPLENT, C.CEPENT, C.BAIRENT, C.CIDENT, C.UFENT, V.CODBANCO, V.VLRDESCITVENDA, " );
			sql.append( "(SELECT B.NOMEBANCO FROM FNBANCO B WHERE B.CODEMP=V.CODEMPBO AND B.CODFILIAL=V.CODFILIALBO AND B.CODBANCO=V.CODBANCO), " );
			sql.append( "(SELECT S.DESCSETOR FROM VDSETOR S WHERE S.CODSETOR=C.CODSETOR AND S.CODFILIAL=C.CODFILIALSR AND S.CODEMP=C.CODEMPSR), " );
			sql.append( "V.VLRLIQVENDA,V.VLRICMSVENDA,V.VLRBASEICMSVENDA, V.VLRPRODVENDA, V.VLRBASEISSVENDA, V.VLRISSVENDA, V.VLRPRODVENDA, V.VLRADICVENDA, " );
			sql.append( "V.VLRIPIVENDA,V.PEDCLIVENDA, F.PERCISSFILIAL PERCISS, V.HALT, V.CALCCOFINSVENDA, V.IMPCOFINSVENDA, V.CALCCSOCIALVENDA, V.IMPCSOCIALVENDA, " );
			sql.append( "V.CALCICMSVENDA, V.IMPICMSVENDA, V.CALCIPIVENDA, V.IMPIPIVENDA, V.CALCIRVENDA, V.IMPIRVENDA, V.CALCISSVENDA, V.IMPIISSVENDA, " );
			sql.append( "V.CALCPISVENDA, V.IMPPISVENDA, V.VLRDESCVENDA, V.VLRPISVENDA, V.VLRCOFINSVENDA, V.VLRIRVENDA, V.VLRCSOCIALVENDA, V.VLRBASEISSVENDA, " );
			sql.append( "TM.TIPOMOV,V.VLRICMSSIMPLES,V.PERCICMSSIMPLES," );
			sql.append( "(SELECT M.MENS FROM LFMENSAGEM M WHERE M.CODMENS=P1.CODMENSICMSSIMPLES AND M.CODFILIAL=P1.CODFILIALMS AND M.CODEMP=P1.CODEMPMS) AS MENS1, " );
			sql.append( "V.VLRBASEICMSSTVENDA, V.VLRICMSSTVENDA, " );
			sql.append( "F.ENDFILIAL, F.NUMFILIAL, F.BAIRFILIAL, F.CIDFILIAL, F.UFFILIAL, F.CEPFILIAL, F.FONEFILIAL, F.WWWFILIAL, F.EMAILFILIAL " );
			sql.append( "FROM VDVENDA V, VDCLIENTE C, FNPLANOPAG PG, VDVENDEDOR VEND, SGEMPRESA EMP, EQTIPOMOV TM, SGPREFERE1 P1, SGFILIAL F " );
			sql.append( "WHERE EMP.CODEMP=V.CODEMP AND C.CODEMP=V.CODEMPCL AND C.CODFILIAL=V.CODFILIALCL AND C.CODCLI=V.CODCLI " );
			sql.append( "AND V.CODEMPPG=PG.CODEMP AND V.CODFILIALPG=PG.CODFILIAL AND V.CODPLANOPAG=PG.CODPLANOPAG " );
			sql.append( "AND V.CODVEND=VEND.CODVEND AND V.CODEMPVD=VEND.CODEMP AND V.CODFILIALVD=VEND.CODFILIAL AND C.CODCLI=V.CODCLI " );
			sql.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.TIPOVENDA='V' AND V.CODVENDA=? " );
			sql.append( "AND TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND TM.CODTIPOMOV=V.CODTIPOMOV " );
			sql.append( "AND F.CODEMP=V.CODEMP AND F.CODFILIAL=V.CODFILIAL AND P1.CODEMP=F.CODEMP " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) parans.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) parans.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) parans.elementAt( 2 ) ).intValue() );
			rs = ps.executeQuery();
			sql.delete( 0, sql.length() );
			cab = new TabVector( TAM_CAB );

			while ( rs.next() ) {
				cab.addRow();
				cab.setInt( C_CODPED, rs.getInt( "CODVENDA" ) );
				cab.setInt( C_CODEMIT, rs.getInt( "CODCLI" ) );
				cab.setString( C_RAZEMIT, ( rs.getString( "RAZCLI" ) != null ? rs.getString( "RAZCLI" ) : "" ) );
				cab.setString( C_CNPJEMIT, ( rs.getString( "CNPJCLI" ) != null ? rs.getString( "CNPJCLI" ) : "" ) );
				cab.setString( C_CPFEMIT, ( rs.getString( "CPFCLI" ) != null ? rs.getString( "CPFCLI" ) : "" ) );
				cab.setString( C_ENDEMIT, ( rs.getString( "ENDCLI" ) != null ? rs.getString( "ENDCLI" ) : "" ) );
				cab.setInt( C_NUMEMIT, rs.getInt( "NUMCLI" ) );
				cab.setString( C_COMPLEMIT, ( rs.getString( "COMPLCLI" ) != null ? rs.getString( "COMPLCLI" ) : "" ) );
				cab.setString( C_BAIREMIT, ( rs.getString( "BAIRCLI" ) != null ? rs.getString( "BAIRCLI" ) : "" ) );
				cab.setString( C_CEPEMIT, ( rs.getString( "CEPCLI" ) != null ? rs.getString( "CEPCLI" ) : "" ) );
				cab.setString( C_CEPENTEMIT, ( rs.getString( "CEPENT" ) != null ? rs.getString( "CEPENT" ) : "" ) );
				cab.setString( C_CIDEMIT, ( rs.getString( "CIDCLI" ) != null ? rs.getString( "CIDCLI" ) : "" ) );
				cab.setString( C_UFEMIT, ( rs.getString( "UFCLI" ) != null ? rs.getString( "UFCLI" ) : "" ) );
				cab.setString( C_FONEEMIT, ( rs.getString( "FONECLI" ) != null ? rs.getString( "FONECLI" ) : "" ) );
				cab.setString( C_FAXEMIT, ( rs.getString( "FAXCLI" ) != null ? rs.getString( "FAXCLI" ) : "" ) );
				cab.setString( C_DDDEMIT, ( rs.getString( "DDDCLI" ) != null ? rs.getString( "DDDCLI" ) : "" ) );
				cab.setString( C_INSCEMIT, ( rs.getString( "INSCCLI" ) != null ? rs.getString( "INSCCLI" ) : "" ) );
				cab.setString( C_RGEMIT, ( rs.getString( "RGCLI" ) != null ? rs.getString( "RGCLI" ) : "" ) );
				cab.setString( C_EMAILEMIT, ( rs.getString( "EMAILCLI" ) != null ? rs.getString( "EMAILCLI" ) : "" ) );
				cab.setString( C_SITEEMIT, ( rs.getString( "SITECLI" ) != null ? rs.getString( "SITECLI" ) : "" ) );
				cab.setString( C_CONTEMIT, ( rs.getString( "CONTCLI" ) != null ? rs.getString( "CONTCLI" ) : "" ) );
				cab.setDate( C_DTEMITPED, rs.getDate( "DTEMITVENDA" ) );
				cab.setInt( C_DOC, rs.getInt( "DOCVENDA" ) );
				cab.setString( C_INCRAEMIT, ( rs.getString( "INCRACLI" ) != null ? rs.getString( "INCRACLI" ) : "" ) );
				cab.setDate( C_DTSAIDA, rs.getDate( "DTSAIDAVENDA" ) );
				cab.setString( C_CODPLANOPG, ( rs.getString( "CODPLANOPAG" ) != null ? rs.getString( "CODPLANOPAG" ) : "" ) );
				cab.setString( C_DESCPLANOPAG, ( rs.getString( "DESCPLANOPAG" ) != null ? rs.getString( "DESCPLANOPAG" ) : "" ) );
				cab.setString( C_OBSPED, ( rs.getString( "OBSVENDA" ) != null ? rs.getString( "OBSVENDA" ) : "" ) );
				cab.setString( C_NOMEVEND, ( rs.getString( "NOMEVEND" ) != null ? rs.getString( "NOMEVEND" ) : "" ) );
				cab.setString( C_EMAILVEND, ( rs.getString( "EMAILVEND" ) != null ? rs.getString( "EMAILVEND" ) : "" ) );
				cab.setString( C_DESCFUNC, ( rs.getString( 30 ) != null ? rs.getString( 30 ) : "" ) );
				cab.setString( C_CODCLCOMIS, ( rs.getString( "CODCLCOMIS" ) != null ? rs.getString( "CODCLCOMIS" ) : "" ) );
				cab.setBigDecimal( C_PERCCOMISVENDA, rs.getBigDecimal( "PERCCOMISVENDA" ) );
				cab.setInt( C_CODVEND, rs.getInt( "CODVEND" ) );
				cab.setString( C_ENDCOBEMIT, ( rs.getString( "ENDCOB" ) != null ? rs.getString( "ENDCOB" ).trim() : "" ) );
				cab.setString( C_CIDCOBEMIT, ( rs.getString( "CIDCOB" ) != null ? rs.getString( "CIDCOB" ).trim() : "" ) );
				cab.setString( C_UFCOBEMIT, ( rs.getString( "UFCOB" ) != null ? rs.getString( "UFCOB" ).trim() : "" ) );
				cab.setString( C_BAIRCOBEMIT, ( rs.getString( "BAIRCOB" ) != null ? rs.getString( "BAIRCOB" ).trim() : "" ) );
				cab.setInt( C_NUMCOBEMIT, rs.getInt( "NUMCOB" ) );
				cab.setBigDecimal( C_PERCMCOMISPED, rs.getBigDecimal( "PERCMCOMISVENDA" ) );
				cab.setString( C_NOMEEMIT, ( rs.getString( "NOMECLI" ) != null ? rs.getString( "NOMECLI" ).trim() : "" ) );
				cab.setString( C_ENDENTEMIT, ( rs.getString( "ENDENT" ) != null ? rs.getString( "ENDENT" ).trim() : "" ) );
				cab.setInt( C_NUMENTEMIT, rs.getInt( "NUMENT" ) );
				cab.setString( C_COMPLENTEMIT, ( rs.getString( "COMPLENT" ) != null ? rs.getString( "COMPLENT" ).trim() : "" ) );
				cab.setString( C_BAIRENTEMIT, ( rs.getString( "BAIRENT" ) != null ? rs.getString( "BAIRENT" ).trim() : "" ) );
				cab.setString( C_CIDENTEMIT, ( rs.getString( "CIDENT" ) != null ? rs.getString( "CIDENT" ).trim() : "" ) );
				cab.setString( C_UFENTEMIT, ( rs.getString( "UFENT" ) != null ? rs.getString( "UFENT" ).trim() : "" ) );
				cab.setString( C_CODBANCO, ( rs.getString( "CODBANCO" ) != null ? rs.getString( "CODBANCO" ).trim() : "" ) );
				cab.setString( C_NOMEBANCO, ( rs.getString( 49 ) != null ? rs.getString( 49 ).trim() : "" ) );
				cab.setString( C_DESCSETOR, ( rs.getString( 50 ) != null ? rs.getString( 50 ).trim() : "" ) );
				cab.setBigDecimal( C_VLRDESCITPED, rs.getBigDecimal( "VLRDESCITVENDA" ) );
				cab.setInt( C_DIASPAG, 0 );
				cab.setString( C_PEDEMIT, ( rs.getString( "PEDCLIVENDA" ) != null ? rs.getString( "PEDCLIVENDA" ).trim() : "" ) );
				cab.setBigDecimal( C_VLRLIQPED, rs.getBigDecimal( "VLRLIQVENDA" ) );
				cab.setBigDecimal( C_VLRPRODPED, rs.getBigDecimal( "VLRPRODVENDA" ) );
				cab.setBigDecimal( C_VLRADICPED, rs.getBigDecimal( "VLRADICVENDA" ) );
				cab.setBigDecimal( C_VLRICMSPED, rs.getBigDecimal( "VLRICMSVENDA" ) );
				cab.setBigDecimal( C_VLRBASEICMSPED, rs.getBigDecimal( "VLRBASEICMSVENDA" ) );
				cab.setBigDecimal( C_VLRIPIPED, rs.getBigDecimal( "VLRIPIVENDA" ) );
				cab.setBigDecimal( C_BASEISS, rs.getBigDecimal( "VLRBASEISSVENDA" ) );
				cab.setBigDecimal( C_VLRISS, rs.getBigDecimal( "VLRISSVENDA" ) );
				cab.setBigDecimal( C_PERCISS, rs.getBigDecimal( "PERCISS" ) );

				cab.setString( C_CALCCOFINSVENDA, rs.getString( "CALCCOFINSVENDA" ) != null ? rs.getString( "CALCCOFINSVENDA" ) : "" );
				cab.setString( C_IMPCOFINSVENDA, rs.getString( "IMPCOFINSVENDA" ) != null ? rs.getString( "IMPCOFINSVENDA" ) : "" );
				cab.setString( C_CALCCSOCIALVENDA, rs.getString( "CALCCSOCIALVENDA" ) != null ? rs.getString( "CALCCSOCIALVENDA" ) : "" );
				cab.setString( C_IMPCSOCIALVENDA, rs.getString( "IMPCSOCIALVENDA" ) != null ? rs.getString( "IMPCSOCIALVENDA" ) : "" );
				cab.setString( C_CALCICMSVENDA, rs.getString( "CALCICMSVENDA" ) != null ? rs.getString( "CALCICMSVENDA" ) : "" );
				cab.setString( C_IMPICMSVENDA, rs.getString( "IMPICMSVENDA" ) != null ? rs.getString( "IMPICMSVENDA" ) : "" );
				cab.setString( C_CALCIPIVENDA, rs.getString( "CALCIPIVENDA" ) != null ? rs.getString( "CALCIPIVENDA" ) : "" );
				cab.setString( C_IMPIPIVENDA, rs.getString( "IMPIPIVENDA" ) != null ? rs.getString( "IMPIPIVENDA" ) : "" );
				cab.setString( C_CALCIRVENDA, rs.getString( "CALCIRVENDA" ) != null ? rs.getString( "CALCIRVENDA" ) : "" );
				cab.setString( C_IMPIRVENDA, rs.getString( "IMPIRVENDA" ) != null ? rs.getString( "IMPIRVENDA" ) : "" );
				cab.setString( C_CALCISSVENDA, rs.getString( "CALCISSVENDA" ) != null ? rs.getString( "CALCISSVENDA" ) : "" );
				cab.setString( C_IMPIISSVENDA, rs.getString( "IMPIISSVENDA" ) != null ? rs.getString( "IMPIISSVENDA" ) : "" );
				cab.setString( C_CALCPISVENDA, rs.getString( "CALCPISVENDA" ) != null ? rs.getString( "CALCPISVENDA" ) : "" );
				cab.setString( C_IMPPISVENDA, rs.getString( "IMPPISVENDA" ) != null ? rs.getString( "IMPPISVENDA" ) : "" );

				cab.setBigDecimal( C_VLRDESCVENDA, rs.getBigDecimal( "VLRDESCVENDA" ) );
				cab.setBigDecimal( C_VLRPISVENDA, rs.getBigDecimal( "VLRPISVENDA" ) );
				cab.setBigDecimal( C_VLRCOFINSVENDA, rs.getBigDecimal( "VLRCOFINSVENDA" ) );
				cab.setBigDecimal( C_VLRIRVENDA, rs.getBigDecimal( "VLRIRVENDA" ) );
				cab.setBigDecimal( C_VLRCSOCIALVENDA, rs.getBigDecimal( "VLRCSOCIALVENDA" ) );
				cab.setBigDecimal( C_VLRBASEISSVENDA, rs.getBigDecimal( "VLRBASEISSVENDA" ) );
				cab.setBigDecimal( C_VLRBASEICMSST, rs.getBigDecimal( "VLRBASEICMSSTVENDA" ) );
				cab.setBigDecimal( C_VLRICMSST, rs.getBigDecimal( "VLRICMSSTVENDA" ) );

				cab.setString( C_TIPOMOV, rs.getString( "TIPOMOV" ) );

				cab.setString( C_ENDFILIAL, rs.getString( "ENDFILIAL" ) != null ? rs.getString( "ENDFILIAL" ) : "" );
				cab.setString( C_NUMFILIAL, rs.getString( "NUMFILIAL" ) != null ? rs.getString( "NUMFILIAL" ) : "" );
				cab.setString( C_BAIRFILIAL, rs.getString( "BAIRFILIAL" ) != null ? rs.getString( "BAIRFILIAL" ) : "" );
				cab.setString( C_CIDFILIAL, rs.getString( "CIDFILIAL" ) != null ? rs.getString( "CIDFILIAL" ) : "" );
				cab.setString( C_UFFILIAL, rs.getString( "UFFILIAL" ) != null ? rs.getString( "UFFILIAL" ) : "" );
				cab.setString( C_CEPFILIAL, rs.getString( "CEPFILIAL" ) != null ? rs.getString( "CEPFILIAL" ) : "" );
				cab.setString( C_FONEFILIAL, rs.getString( "FONEFILIAL" ) != null ? rs.getString( "FONEFILIAL" ) : "" );
				cab.setString( C_WWWFILIAL, rs.getString( "WWWFILIAL" ) != null ? rs.getString( "WWWFILIAL" ) : "" );
				cab.setString( C_EMAILFILIAL, rs.getString( "EMAILFILIAL" ) != null ? rs.getString( "EMAILFILIAL" ) : "" );
				cab.setString( C_HALT, rs.getString( "HALT" ) != null ? rs.getString( "HALT" ) : "" );

				geraMensagens( rs.getString( "MENS1" ), rs.getBigDecimal( "VLRICMSSIMPLES" ), rs.getBigDecimal( "PERCICMSSIMPLES" ) );

			}
			rs.close();
			ps.close();
			con.commit();

			cab.setRow( -1 );

			sql.append( "SELECT I.CODITVENDA, I.CODPROD, P.REFPROD, P.DESCPROD, I.OBSITVENDA, P.CODUNID, I.VLRIPIITVENDA," );
			sql.append( "I.QTDITVENDA, I.VLRLIQITVENDA, I.PERCIPIITVENDA, I.PERCICMSITVENDA, N.IMPDTSAIDANAT, I.VLRPRODITVENDA, " );
			sql.append( "N.DESCNAT, N.CODNAT, I.VLRISSITVENDA, I.CODLOTE, I.ORIGFISC, I.CODTRATTRIB, " );
			sql.append( "V.VLRBASEICMSVENDA, V.VLRADICVENDA, P.CODFISC, P.TIPOPROD, I.VLRDESCITVENDA," );
			sql.append( "(SELECT L.VENCTOLOTE FROM EQLOTE L WHERE L.CODEMP=I.CODEMPLE AND L.CODFILIAL=I.CODFILIALLE AND L.CODPROD=I.CODPROD AND L.CODLOTE=I.CODLOTE)," );
			sql.append( "(SELECT COUNT(IC.CODITVENDA) FROM VDITVENDA IC WHERE IC.CODVENDA=V.CODVENDA AND IC.CODEMP=V.CODEMP AND IC.CODFILIAL=V.CODFILIAL AND IC.TIPOVENDA=V.TIPOVENDA)," );
			sql.append( "(SELECT M.MENS FROM LFMENSAGEM M WHERE M.CODMENS=CL.CODMENS AND M.CODFILIAL=CL.CODFILIALME AND M.CODEMP=CL.CODEMPME)," );
			sql.append( "(SELECT M.MENS FROM LFMENSAGEM M WHERE M.CODMENS=I.CODMENS AND M.CODFILIAL=I.CODFILIALME AND M.CODEMP=I.CODEMPME), " );
			sql.append( "P.CODBARPROD, P.CODFISC, I.PERCDESCITVENDA, P.CODFABPROD, P.QTDEMBALAGEM " );
			sql.append( "FROM VDITVENDA I, VDVENDA V, EQPRODUTO P, LFNATOPER N, LFITCLFISCAL CL " );
			sql.append( "WHERE P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD " );
			sql.append( "AND N.CODEMP=I.CODEMPNT AND N.CODFILIAL=I.CODFILIALNT " );
			sql.append( "AND N.CODNAT=I.CODNAT AND I.CODEMP=V.CODEMP AND I.CODFILIAL=V.CODFILIAL " );
			sql.append( "AND CL.CODFISC=I.CODFISC AND CL.CODEMP=I.CODEMPIF AND CL.CODFILIAL=I.CODFILIALIF AND CL.CODITFISC=I.CODITFISC  " );
			sql.append( "AND I.CODVENDA=V.CODVENDA AND I.TIPOVENDA=V.TIPOVENDA " );
			sql.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.CODVENDA=? AND V.TIPOVENDA='V' " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) parans.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) parans.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) parans.elementAt( 2 ) ).intValue() );
			rs = ps.executeQuery();
			sql.delete( 0, sql.length() );
			itens = new TabVector( TAM_ITENS );
			while ( rs.next() ) {
				itens.addRow();
				itens.setInt( C_CODITPED, rs.getInt( "CODITVENDA" ) );
				itens.setInt( C_CODPROD, rs.getInt( "CODPROD" ) );
				itens.setString( C_REFPROD, ( rs.getString( "REFPROD" ) != null ? rs.getString( "REFPROD" ) : "" ) );
				itens.setString( C_DESCPROD, ( rs.getString( "DESCPROD" ) != null ? rs.getString( "DESCPROD" ) : "" ) );
				itens.setString( C_OBSITPED, ( rs.getString( "OBSITVENDA" ) != null ? rs.getString( "OBSITVENDA" ) : "" ) );
				itens.setString( C_CODUNID, ( rs.getString( "CODUNID" ) != null ? rs.getString( "CODUNID" ) : "" ) );

				itens.setBigDecimal( C_QTDITPED, rs.getBigDecimal( "QTDITVENDA" ) );
				itens.setBigDecimal( C_VLRLIQITPED, rs.getBigDecimal( "VLRLIQITVENDA" ) );
				itens.setBigDecimal( C_PERCIPIITPED, rs.getBigDecimal( "PERCIPIITVENDA" ) );
				itens.setBigDecimal( C_PERCICMSITPED, rs.getBigDecimal( "PERCICMSITVENDA" ) );
				itens.setBigDecimal( C_VLRIPIITPED, rs.getBigDecimal( "VLRIPIITVENDA" ) );

				itens.setString( C_IMPDTSAIDA, ( rs.getString( "IMPDTSAIDANAT" ) != null ? rs.getString( "IMPDTSAIDANAT" ) : "" ) );
				itens.setBigDecimal( C_VLRPRODITPED, rs.getBigDecimal( "VLRPRODITVENDA" ) );

				itens.setString( C_DESCNAT, ( rs.getString( "DESCNAT" ) != null ? rs.getString( "DESCNAT" ) : "" ) );
				itens.setInt( C_CODNAT, rs.getInt( "CODNAT" ) );
				itens.setString( C_CODLOTE, ( rs.getString( "CODLOTE" ) != null ? rs.getString( "CODLOTE" ) : "" ) );
				itens.setDate( C_VENCLOTE, rs.getDate( 25 ) );
				itens.setString( C_ORIGFISC, ( rs.getString( "ORIGFISC" ) != null ? rs.getString( "ORIGFISC" ) : "" ) );
				itens.setString( C_CODTRATTRIB, ( rs.getString( "CODTRATTRIB" ) != null ? rs.getString( "CODTRATTRIB" ) : "" ) );

				itens.setBigDecimal( C_VLRADICITPED, rs.getBigDecimal( "VLRADICVENDA" ) );

				itens.setInt( C_CONTAITENS, rs.getInt( 26 ) );
				itens.setString( C_DESCFISC, ( rs.getString( 27 ) != null ? rs.getString( 27 ) : "" ) );
				itens.setString( C_DESCFISC2, ( rs.getString( 28 ) != null ? rs.getString( 28 ) : "" ) );
				itens.setString( C_CODFISC, rs.getString( "CODFISC" ) != null ? rs.getString( "CODFISC" ) : "" );
				itens.setString( C_TIPOPROD, rs.getString( "TIPOPROD" ) != null ? rs.getString( "TIPOPROD" ) : "" );

				itens.setBigDecimal( C_VLRISSITPED, rs.getBigDecimal( "VLRISSITVENDA" ) );
				itens.setBigDecimal( C_VLRDESCITPROD, rs.getBigDecimal( "VLRDESCITVENDA" ) );

				itens.setString( C_CODBAR, rs.getString( "CODBARPROD" ) != null ? rs.getString( "CODBARPROD" ) : "" );
				itens.setString( C_CODCLASSFISC, rs.getString( "CODFISC" ) != null ? rs.getString( "CODFISC" ) : "" );

				itens.setBigDecimal( C_PERCDESCITVENDA, rs.getBigDecimal( "PERCDESCITVENDA" ) );
				itens.setString( C_CODFABPROD, rs.getString( "CODFABPROD" ) );

				itens.setBigDecimal( C_QTDEMBALAGEM, rs.getBigDecimal( "QTDEMBALAGEM" ) );

			}
			rs.close();
			ps.close();
			con.commit();

			itens.setRow( -1 );

			sql.append( "SELECT AUX.CODAUXV, AUX.CPFCLIAUXV, AUX.NOMECLIAUXV, AUX.CIDCLIAUXV, AUX.UFCLIAUXV " );
			sql.append( "FROM  VDAUXVENDA AUX, VDVENDA V " );
			sql.append( "WHERE AUX.CODEMP=V.CODEMP AND AUX.CODFILIAL=V.CODFILIAL AND AUX.CODVENDA=V.CODVENDA AND AUX.TIPOVENDA=V.TIPOVENDA " );
			sql.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.CODVENDA=? AND V.TIPOVENDA='V' " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) parans.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) parans.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) parans.elementAt( 2 ) ).intValue() );
			rs = ps.executeQuery();
			sql.delete( 0, sql.length() );
			adic = new TabVector( 5 );
			while ( rs.next() ) {
				adic.addRow();
				adic.setInt( C_CODAUXV, rs.getInt( "CODAUXV" ) );
				adic.setString( C_CPFEMITAUX, ( rs.getString( "CPFCLIAUXV" ) != null ? rs.getString( "CPFCLIAUXV" ) : "" ) );
				adic.setString( C_NOMEEMITAUX, ( rs.getString( "NOMECLIAUXV" ) != null ? rs.getString( "NOMECLIAUXV" ) : "" ) );
				adic.setString( C_CIDEMITAUX, ( rs.getString( "CIDCLIAUXV" ) != null ? rs.getString( "CIDCLIAUXV" ) : "" ) );
				adic.setString( C_UFEMITAUX, ( rs.getString( "UFCLIAUXV" ) != null ? rs.getString( "UFCLIAUXV" ) : "" ) );
			}
			rs.close();
			ps.close();
			con.commit();

			adic.setRow( -1 );

			sql.append( "SELECT I.DTVENCITREC,I.VLRPARCITREC,I.NPARCITREC " );
			sql.append( "FROM FNRECEBER R, FNITRECEBER I, VDVENDA V " );
			sql.append( "WHERE R.CODVENDA=V.CODVENDA AND I.CODREC=R.CODREC " );
			sql.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.CODVENDA=? AND V.TIPOVENDA='V' " );
			sql.append( "ORDER BY I.DTVENCITREC" );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) parans.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) parans.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) parans.elementAt( 2 ) ).intValue() );
			rs = ps.executeQuery();
			sql.delete( 0, sql.length() );
			parc = new TabVector( 3 );

			while ( rs.next() ) {
				parc.addRow();
				parc.setDate( C_DTVENCTO, rs.getDate( "DTVENCITREC" ) );
				parc.setBigDecimal( C_VLRPARC, rs.getBigDecimal( "VLRPARCITREC" ) );
				parc.setInt( C_NPARCITREC, rs.getInt( "NPARCITREC" ) );
			}

			rs.close();
			ps.close();
			con.commit();

			parc.setRow( -1 );

			sql.append( "SELECT T.CODTRAN, T.RAZTRAN, T.NOMETRAN, T.INSCTRAN, T.CNPJTRAN, T.TIPOTRAN, " );
			sql.append( "T.ENDTRAN, T.NUMTRAN, T.CIDTRAN, T.UFTRAN , F.TIPOFRETEVD, F.PLACAFRETEVD, " );
			sql.append( "F.UFFRETEVD, F.QTDFRETEVD, F.ESPFRETEVD, F.MARCAFRETEVD, F.PESOBRUTVD, F.PESOLIQVD, " );
			sql.append( "(CASE WHEN V.VLRFRETEVENDA<=0 THEN F.VLRFRETEVD ELSE V.VLRFRETEVENDA END) VLRFRETEVENDA, " );
			sql.append( "F.CONHECFRETEVD, T.CPFTRAN, F.ADICFRETEBASEICM, F.ALIQICMSFRETEVD, F.VLRICMSFRETEVD," );
			sql.append( "T.DDDFONETRAN, T.FONETRAN " );
			sql.append( "FROM VDTRANSP T, VDFRETEVD F, VDVENDA V " );
			sql.append( "WHERE T.CODEMP=F.CODEMPTN AND T.CODFILIAL=F.CODFILIALTN AND T.CODTRAN=F.CODTRAN " );
			sql.append( "AND F.CODEMP=V.CODEMP AND F.CODFILIAL=V.CODFILIAL AND F.CODVENDA=V.CODVENDA AND F.TIPOVENDA=V.TIPOVENDA " );
			sql.append( "AND V.CODEMP=? AND V.CODFILIAL=? AND V.CODVENDA=? AND V.TIPOVENDA='V' " );
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) parans.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) parans.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) parans.elementAt( 2 ) ).intValue() );
			rs = ps.executeQuery();
			sql.delete( 0, sql.length() );
			frete = new TabVector( 26 );
			while ( rs.next() ) {
				frete.addRow();
				frete.setInt( C_CODTRAN, rs.getInt( "CODTRAN" ) );
				frete.setString( C_RAZTRANSP, ( rs.getString( "RAZTRAN" ) != null ? rs.getString( "RAZTRAN" ) : "" ) );
				frete.setString( C_NOMETRANSP, ( rs.getString( "NOMETRAN" ) != null ? rs.getString( "NOMETRAN" ) : "" ) );
				frete.setString( C_INSCTRANSP, ( rs.getString( "INSCTRAN" ) != null ? rs.getString( "INSCTRAN" ) : "" ) );
				frete.setString( C_CNPJTRANSP, ( rs.getString( "CNPJTRAN" ) != null ? rs.getString( "CNPJTRAN" ) : "" ) );
				frete.setString( C_TIPOTRANSP, ( rs.getString( "TIPOTRAN" ) != null ? rs.getString( "TIPOTRAN" ) : "" ) );
				frete.setString( C_ENDTRANSP, ( rs.getString( "ENDTRAN" ) != null ? rs.getString( "ENDTRAN" ) : "" ) );
				frete.setInt( C_NUMTRANSP, rs.getInt( "NUMTRAN" ) );
				frete.setString( C_CIDTRANSP, ( rs.getString( "CIDTRAN" ) != null ? rs.getString( "CIDTRAN" ) : "" ) );
				frete.setString( C_UFTRANSP, ( rs.getString( "UFTRAN" ) != null ? rs.getString( "UFTRAN" ) : "" ) );
				frete.setString( C_TIPOFRETE, ( rs.getString( "TIPOFRETEVD" ) != null ? rs.getString( "TIPOFRETEVD" ) : "" ) );
				frete.setString( C_PLACAFRETE, ( rs.getString( "PLACAFRETEVD" ) != null ? rs.getString( "PLACAFRETEVD" ) : "" ) );
				frete.setString( C_UFFRETE, ( rs.getString( "UFFRETEVD" ) != null ? rs.getString( "UFFRETEVD" ) : "" ) );

				frete.setBigDecimal( C_QTDFRETE, rs.getBigDecimal( "QTDFRETEVD" ) );
				frete.setString( C_ESPFRETE, ( rs.getString( "ESPFRETEVD" ) != null ? rs.getString( "ESPFRETEVD" ) : "" ) );
				frete.setString( C_MARCAFRETE, ( rs.getString( "MARCAFRETEVD" ) != null ? rs.getString( "MARCAFRETEVD" ) : "" ) );

				frete.setBigDecimal( C_PESOBRUTO, rs.getBigDecimal( "PESOBRUTVD" ) );
				frete.setBigDecimal( C_PESOLIQ, rs.getBigDecimal( "PESOLIQVD" ) );
				frete.setBigDecimal( C_VLRFRETEPED, rs.getBigDecimal( "VLRFRETEVENDA" ) );

				frete.setString( C_CONHECFRETEPED, ( rs.getString( "CONHECFRETEVD" ) != null ? rs.getString( "CONHECFRETEVD" ) : "" ) );
				frete.setString( C_CPFTRANSP, ( rs.getString( "CPFTRAN" ) != null ? rs.getString( "CPFTRAN" ) : "" ) );
				frete.setString( C_ADICFRETEBASEICM, ( rs.getString( "ADICFRETEBASEICM" ) != null ? rs.getString( "ADICFRETEBASEICM" ) : "" ) );

				frete.setBigDecimal( C_ALIQICMSFRETEVD, rs.getBigDecimal( "ALIQICMSFRETEVD" ) );
				frete.setBigDecimal( C_VLRICMSFRETEVD, rs.getBigDecimal( "VLRICMSFRETEVD" ) );

				frete.setString( C_DDDTRANSP, ( rs.getString( "DDDFONETRAN" ) != null ? rs.getString( "DDDFONETRAN" ) : "" ) );
				frete.setString( C_FONETRANSP, ( rs.getString( "FONETRAN" ) != null ? rs.getString( "FONETRAN" ) : "" ) );

			}
			rs.close();
			ps.close();
			con.commit();
			frete.setRow( -1 );
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( null, "Erro na NFSaida\n" + e.getMessage() );
			retorno = false;
		} finally {
			rs = null;
			ps = null;
			System.gc();
		}
		return retorno;
	}

	/* Método para tratamento de mensagem para crédito de ICMS de Empresa SIMPLES */
	private void geraMensagens( String mens1, BigDecimal vlricmssimples, BigDecimal percicmssimples ) {

		try {
			if ( ( vlricmssimples != null ) && ( percicmssimples != null ) && ( vlricmssimples.compareTo( new BigDecimal( 0 ) ) > 0 ) ) {
				if ( mens1 != null ) {
					mens1 = mens1.replaceAll( "#VALOR#", Funcoes.strDecimalToStrCurrency( 2, String.valueOf( vlricmssimples ) ) );
					mens1 = mens1.replaceAll( "#ALIQUOTA#", String.valueOf( percicmssimples ) + "% " );
					cab.setString( C_MENSAGENS, mens1 );
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

}
