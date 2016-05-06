/**
 * @version 08/08/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.layout <BR>
 *         Classe:
 * @(#)NFEntrada.java <BR>
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
 *                    Comentários sobre a classe...
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

public class NFEntrada extends NF {

	protected int tipoNF = TPNF_ENTRADA;

	public NFEntrada( int casasDec ) {

		super( casasDec );
	}

	public int getTipoNF() {

		return this.tipoNF;
	}

	public boolean carregaTabelas( DbConnection con, Vector<?> params ) {

		boolean retorno = true;

		StringBuffer sql = new StringBuffer();
		BigDecimal qtdfrete = new BigDecimal( 0 );
		int cont = 0;

		try {

			setConexao( con );

			sql.append( "SELECT C.CODCOMPRA, C.CODFOR, F.RAZFOR,  F.CNPJFOR, F.CPFFOR, F.ENDFOR, F.NUMFOR, F.COMPLFOR," );
			sql.append( "F.BAIRFOR, F.CEPFOR, F.CIDFOR, F.UFFOR, F.FONEFOR, F.FAXFOR, F.DDDFONEFOR, F.INSCFOR, F.RGFOR, " );
			sql.append( "C.VLRDESCITCOMPRA, F.EMAILFOR, F.SITEFOR, F.CONTFOR, C.DTEMITCOMPRA, C.DOCCOMPRA, C.DTENTCOMPRA, " );
			sql.append( "C.CODPLANOPAG, PG.DESCPLANOPAG, C.CODBANCO, C.OBSERVACAO, " );
			sql.append( "(SELECT B.NOMEBANCO FROM FNBANCO B WHERE B.CODEMP=C.CODEMPBO AND B.CODFILIAL=C.CODFILIALBO AND B.CODBANCO=C.CODBANCO), " );
			sql.append( "C.VLRLIQCOMPRA,C.VLRPRODCOMPRA,C.VLRADICCOMPRA,C.VLRICMSCOMPRA,C.VLRBASEICMSCOMPRA,C.VLRIPICOMPRA, " );
			sql.append( "C.VLRFRETECOMPRA, C.HALT, C.QTDFRETECOMPRA, TM.TIPOMOV, C.VLRFUNRURALCOMPRA " );
			sql.append( "FROM CPCOMPRA C, CPFORNECED F, FNPLANOPAG PG, EQTIPOMOV TM " );
			sql.append( "WHERE F.CODEMP=C.CODEMPFR AND F.CODFILIAL=C.CODFILIALFR AND F.CODFOR=C.CODFOR " );
			sql.append( "AND PG.CODEMP=C.CODEMPPG AND PG.CODFILIAL=C.CODFILIALPG AND PG.CODPLANOPAG=C.CODPLANOPAG " );
			sql.append( "AND TM.CODEMP=C.CODEMPTM AND TM.CODFILIAL=C.CODFILIALTM AND TM.CODTIPOMOV=C.CODTIPOMOV " );
			sql.append( "AND C.CODEMP=? AND C.CODFILIAL=? AND C.CODCOMPRA=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) params.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) params.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) params.elementAt( 2 ) ).intValue() );
			ResultSet rs = ps.executeQuery();

			cont++;
			cab = new TabVector( TAM_CAB );

			frete = new TabVector( 26 );
			frete.addRow();

			while ( rs.next() ) {
				cab.addRow();
				cab.setInt( C_CODPED, rs.getInt( "CODCOMPRA" ) );
				cab.setInt( C_CODEMIT, rs.getInt( "CODFOR" ) );
				cab.setString( C_RAZEMIT, rs.getString( "RAZFOR" ) != null ? rs.getString( "RAZFOR" ) : "" );
				cab.setString( C_CNPJEMIT, rs.getString( "CNPJFOR" ) != null ? rs.getString( "CNPJFOR" ) : "" );
				cab.setString( C_CPFEMIT, rs.getString( "CPFFOR" ) != null ? rs.getString( "CPFFOR" ) : "" );
				cab.setString( C_ENDEMIT, rs.getString( "ENDFOR" ) != null ? rs.getString( "ENDFOR" ) : "" );
				cab.setInt( C_NUMEMIT, rs.getInt( "NUMFOR" ) );
				cab.setString( C_COMPLEMIT, rs.getString( "COMPLFOR" ) != null ? rs.getString( "COMPLFOR" ) : "" );
				cab.setString( C_BAIREMIT, rs.getString( "BAIRFOR" ) != null ? rs.getString( "BAIRFOR" ) : "" );
				cab.setString( C_CEPEMIT, rs.getString( "CEPFOR" ) != null ? rs.getString( "CEPFOR" ) : "" );
				cab.setString( C_CIDEMIT, rs.getString( "CIDFOR" ) != null ? rs.getString( "CIDFOR" ) : "" );
				cab.setString( C_UFEMIT, rs.getString( "UFFOR" ) != null ? rs.getString( "UFFOR" ) : "" );
				cab.setString( C_FONEEMIT, rs.getString( "FONEFOR" ) != null ? rs.getString( "FONEFOR" ) : "" );
				cab.setString( C_FAXEMIT, rs.getString( "FAXFOR" ) != null ? rs.getString( "FAXFOR" ) : "" );
				cab.setString( C_DDDEMIT, rs.getString( "DDDFONEFOR" ) != null ? rs.getString( "DDDFONEFOR" ) : "" );
				cab.setString( C_INSCEMIT, rs.getString( "INSCFOR" ) != null ? rs.getString( "INSCFOR" ) : "" );
				cab.setString( C_RGEMIT, rs.getString( "RGFOR" ) != null ? rs.getString( "RGFOR" ) : "" );
				cab.setString( C_EMAILEMIT, rs.getString( "EMAILFOR" ) != null ? rs.getString( "EMAILFOR" ) : "" );
				cab.setString( C_SITEEMIT, rs.getString( "SITEFOR" ) != null ? rs.getString( "SITEFOR" ) : "" );
				cab.setString( C_CONTEMIT, rs.getString( "CONTFOR" ) != null ? rs.getString( "CONTFOR" ) : "" );
				cab.setDate( C_DTEMITPED, rs.getDate( "DTEMITCOMPRA" ) );
				cab.setInt( C_DOC, rs.getInt( "DOCCOMPRA" ) );
				cab.setString( C_INCRAEMIT, "" );
				cab.setDate( C_DTSAIDA, rs.getDate( "DTENTCOMPRA" ) );
				cab.setString( C_CODPLANOPG, ( rs.getString( "CODPLANOPAG" ) != null ? rs.getString( "CODPLANOPAG" ) : "" ) );
				cab.setString( C_DESCPLANOPAG, ( rs.getString( "DESCPLANOPAG" ) != null ? rs.getString( "DESCPLANOPAG" ) : "" ) );
				cab.setString( C_OBSPED, ( rs.getString( "OBSERVACAO" ) != null ? rs.getString( "OBSERVACAO" ) : "" ) );
				cab.setString( C_NOMEVEND, "" );
				cab.setString( C_EMAILVEND, "" );
				cab.setString( C_DESCFUNC, "" );
				cab.setString( C_CODCLCOMIS, "" );
				cab.setBigDecimal( C_PERCCOMISVENDA, new BigDecimal( "0.00" ) );
				cab.setInt( C_CODVEND, 0 );
				cab.setString( C_ENDCOBEMIT, "" );
				cab.setString( C_CIDCOBEMIT, "" );
				cab.setString( C_UFCOBEMIT, "" );
				cab.setString( C_BAIRCOBEMIT, "" );
				cab.setInt( C_NUMCOBEMIT, 0 );
				cab.setBigDecimal( C_PERCMCOMISPED, new BigDecimal( "0.00" ) );
				cab.setString( C_NOMEEMIT, "" );
				cab.setString( C_ENDENTEMIT, "" );
				cab.setInt( C_NUMENTEMIT, 0 );
				cab.setString( C_COMPLENTEMIT, "" );
				cab.setString( C_BAIRENTEMIT, "" );
				cab.setString( C_CIDENTEMIT, "" );
				cab.setString( C_UFENTEMIT, "" );
				cab.setString( C_CODBANCO, ( rs.getString( "CODBANCO" ) != null ? rs.getString( "CODBANCO" ).trim() : "" ) );
				cab.setString( C_NOMEBANCO, ( rs.getString( 28 ) != null ? rs.getString( 28 ).trim() : "" ) );
				cab.setString( C_DESCSETOR, "" );
				cab.setBigDecimal( C_VLRDESCITPED, rs.getBigDecimal( "VLRDESCITCOMPRA" ) );
				cab.setInt( C_DIASPAG, 0 );
				cab.setString( C_PEDEMIT, "" );
				cab.setBigDecimal( C_VLRLIQPED, rs.getBigDecimal( "VLRLIQCOMPRA" ) );
				cab.setBigDecimal( C_VLRPRODPED, rs.getBigDecimal( "VLRPRODCOMPRA" ) );
				cab.setBigDecimal( C_VLRADICPED, rs.getBigDecimal( "VLRADICCOMPRA" ) );
				cab.setBigDecimal( C_VLRICMSPED, rs.getBigDecimal( "VLRICMSCOMPRA" ) );
				cab.setBigDecimal( C_VLRBASEICMSPED, rs.getBigDecimal( "VLRBASEICMSCOMPRA" ) );
				cab.setBigDecimal( C_VLRIPIPED, rs.getBigDecimal( "VLRIPICOMPRA" ) );
				cab.setBigDecimal( C_BASEISS, new BigDecimal( "0.00" ) );
				cab.setBigDecimal( C_VLRISS, new BigDecimal( "0.00" ) );
				cab.setBigDecimal( C_VLRFRETEPED, rs.getBigDecimal( "VLRFRETECOMPRA" ) );
				qtdfrete = rs.getBigDecimal( "QTDFRETECOMPRA" );
				cab.setBigDecimal( C_QTDFRETE, qtdfrete );
				cab.setString( C_HALT, rs.getString( "HALT" ) );
				cab.setString( C_TIPOMOV, rs.getString( "TIPOMOV" ) );
				cab.setBigDecimal( C_VLRFUNRURALCOMPRA, rs.getBigDecimal( "VLRFUNRURALCOMPRA" ) );

				// XXX

				frete.setBigDecimal( C_VLRFRETEPED, rs.getBigDecimal( "VLRFRETECOMPRA" ) );

				frete.setString( C_DDDTRANSP, "" );
				frete.setString( C_FONETRANSP, "" );
				frete.setInt( C_CODTRAN, 0 );
				frete.setString( C_RAZTRANSP, "" );
				frete.setString( C_NOMETRANSP, "" );
				frete.setString( C_INSCTRANSP, "" );
				frete.setString( C_CNPJTRANSP, "" );
				frete.setString( C_TIPOTRANSP, "" );
				frete.setString( C_ENDTRANSP, "" );
				frete.setInt( C_NUMTRANSP, 0 );
				frete.setString( C_CIDTRANSP, "" );
				frete.setString( C_UFTRANSP, "" );
				frete.setString( C_TIPOFRETE, "" );
				frete.setString( C_PLACAFRETE, "" );
				frete.setString( C_UFFRETE, "" );

				if ( qtdfrete != null ) {
					frete.setBigDecimal( C_QTDFRETE, qtdfrete );
				}
				else {
					frete.setBigDecimal( C_QTDFRETE, new BigDecimal( 0 ) );
				}

				frete.setString( C_ESPFRETE, "" );
				frete.setString( C_MARCAFRETE, "" );

				frete.setBigDecimal( C_PESOBRUTO, new BigDecimal( "0.00" ) );
				frete.setBigDecimal( C_PESOLIQ, new BigDecimal( "0.00" ) );

				frete.setString( C_CONHECFRETEPED, "" );
				frete.setString( C_CPFTRANSP, "" );
				frete.setString( C_ADICFRETEBASEICM, "" );

				frete.setBigDecimal( C_ALIQICMSFRETEVD, new BigDecimal( "0.00" ) );
				frete.setBigDecimal( C_VLRICMSFRETEVD, new BigDecimal( "0.00" ) );

			}

			rs.close();
			ps.close();

			con.commit();

			cab.setRow( -1 );

			sql = new StringBuffer();
			sql.append( "SELECT I.CODITCOMPRA, I.CODPROD, I.QTDITCOMPRA, I.VLRLIQITCOMPRA, I.PERCIPIITCOMPRA, I.VLRIPIITCOMPRA, " );
			sql.append( "I.PERCICMSITCOMPRA, I.VLRPRODITCOMPRA, C.VLRBASEIPICOMPRA, P.REFPROD, P.DESCPROD, I.OBSITCOMPRA, P.CODUNID, " );
			sql.append( "I.CODNAT, N.DESCNAT, N.IMPDTSAIDANAT, I.CODLOTE, P.CODFISC, P.TIPOPROD, I.VLRDESCITCOMPRA, " );
			sql.append( "I.VLRADICITCOMPRA, " );
			sql.append( "(SELECT L.VENCTOLOTE FROM EQLOTE L WHERE L.CODEMP=I.CODEMPLE AND L.CODFILIAL=I.CODFILIALLE AND " );
			sql.append( "L.CODPROD=I.CODPROD AND L.CODLOTE=I.CODLOTE) VENCTOLOTE, " );
			sql.append( "(SELECT COUNT(IC.CODITCOMPRA) FROM CPITCOMPRA IC WHERE IC.CODCOMPRA=C.CODCOMPRA AND " );
			sql.append( "IC.CODEMP=C.CODEMP AND IC.CODFILIAL=C.CODFILIAL) QTDITENS, " );

			sql.append( "(SELECT M.MENS " );
			sql.append( "FROM LFMENSAGEM M, LFITCLFISCAL CL " );
			sql.append( "WHERE M.CODMENS=CL.CODMENS AND M.CODFILIAL=CL.CODFILIALME AND M.CODEMP=CL.CODEMPME " );
			sql.append( "AND CL.CODFISC=P.CODFISC AND CL.CODEMP=P.CODEMPFC AND CL.CODFILIAL=P.CODFILIALFC AND CL.GERALFISC='S'" );
			sql.append( ") MENS," );

			sql.append( "P.CODBARPROD, P.CODFABPROD, P.QTDEMBALAGEM, I.ALIQFUNRURALITCOMPRA " );
			sql.append( "FROM CPITCOMPRA I, CPCOMPRA C, EQPRODUTO P, LFNATOPER N " );
			sql.append( "WHERE I.CODEMP=C.CODEMP AND I.CODFILIAL=C.CODFILIAL AND I.CODCOMPRA=C.CODCOMPRA " );
			sql.append( "AND I.CODNAT=N.codnat AND I.CODEMPNT=N.CODEMP AND I.CODFILIALNT=N.CODFILIAL " );
			sql.append( "AND I.CODPROD=P.CODPROD AND I.CODEMPPD=P.CODEMP AND I.CODFILIALPD=P.CODFILIAL " );
			sql.append( "AND C.CODEMP=? AND C.CODFILIAL=? AND C.CODCOMPRA=?" );

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, ( (Integer) params.elementAt( 0 ) ).intValue() );
			ps.setInt( 2, ( (Integer) params.elementAt( 1 ) ).intValue() );
			ps.setInt( 3, ( (Integer) params.elementAt( 2 ) ).intValue() );
			rs = ps.executeQuery();

			cont++;
			itens = new TabVector( TAM_ITENS );

			while ( rs.next() ) {

				itens.addRow();
				itens.setInt( C_CODITPED, rs.getInt( "CODITCOMPRA" ) );
				itens.setInt( C_CODPROD, rs.getInt( "CODPROD" ) );
				itens.setString( C_REFPROD, rs.getString( "REFPROD" ) != null ? rs.getString( "REFPROD" ) : "" );
				itens.setString( C_DESCPROD, rs.getString( "DESCPROD" ) != null ? rs.getString( "DESCPROD" ) : "" );
				itens.setString( C_OBSITPED, ( rs.getString( "OBSITCOMPRA" ) != null ? rs.getString( "OBSITCOMPRA" ) : "" ) );
				itens.setString( C_CODUNID, rs.getString( "CODUNID" ) != null ? rs.getString( "CODUNID" ) : "" );
				itens.setBigDecimal( C_QTDITPED, rs.getBigDecimal( "QTDITCOMPRA" ) );
				itens.setBigDecimal( C_VLRLIQITPED, rs.getBigDecimal( "VLRLIQITCOMPRA" ) );
				itens.setBigDecimal( C_PERCIPIITPED, rs.getBigDecimal( "PERCIPIITCOMPRA" ) );
				itens.setBigDecimal( C_PERCICMSITPED, rs.getBigDecimal( "PERCICMSITCOMPRA" ) );
				itens.setBigDecimal( C_VLRIPIITPED, rs.getBigDecimal( "VLRIPIITCOMPRA" ) );
				itens.setString( C_IMPDTSAIDA, rs.getString( "IMPDTSAIDANAT" ) != null ? rs.getString( "IMPDTSAIDANAT" ) : "" );
				itens.setBigDecimal( C_VLRPRODITPED, rs.getBigDecimal( "VLRPRODITCOMPRA" ) );
				itens.setString( C_DESCNAT, rs.getString( "DESCNAT" ) != null ? rs.getString( "DESCNAT" ) : "" );
				itens.setInt( C_CODNAT, rs.getInt( "CODNAT" ) );
				itens.setString( C_CODLOTE, rs.getString( "CODLOTE" ) != null ? rs.getString( "CODLOTE" ) : "" );
				itens.setDate( C_VENCLOTE, rs.getDate( "VENCTOLOTE" ) );
				itens.setString( C_ORIGFISC, "" );
				itens.setString( C_CODTRATTRIB, "" );
				itens.setBigDecimal( C_VLRADICITPED, rs.getBigDecimal( "VLRADICITCOMPRA" ) );
				itens.setInt( C_CONTAITENS, rs.getInt( "QTDITENS" ) );
				itens.setString( C_DESCFISC, ( rs.getString( "MENS" ) != null ? rs.getString( "MENS" ) : "" ) );
				itens.setString( C_DESCFISC2, "" );
				itens.setString( C_CODFISC, rs.getString( "CODFISC" ) != null ? rs.getString( "CODFISC" ) : "" );
				itens.setString( C_TIPOPROD, rs.getString( "TIPOPROD" ) != null ? rs.getString( "TIPOPROD" ) : "" );
				itens.setBigDecimal( C_VLRISSITPED, new BigDecimal( "0.00" ) );
				itens.setBigDecimal( C_VLRDESCITPROD, rs.getBigDecimal( "VLRDESCITCOMPRA" ) );
				itens.setString( C_CODBAR, rs.getString( "CODBARPROD" ) );
				itens.setString( C_CODFABPROD, rs.getString( "CODFABPROD" ) );
				itens.setBigDecimal( C_QTDEMBALAGEM, rs.getBigDecimal( "QTDEMBALAGEM" ) );
				itens.setBigDecimal( C_ALIQFUNRURALITCOMPRA, rs.getBigDecimal( "ALIQFUNRURALITCOMPRA" ) );

			}

			rs.close();
			ps.close();
			con.commit();

			itens.setRow( -1 );
			frete.setRow( -1 );

			adic = new TabVector( 5 );
			parc = new TabVector( 3 );

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( null, "Erro na NFEntrada\n" + cont + "\n" + e.getMessage() );
			e.printStackTrace();
			retorno = false;
		}

		return retorno;
	}
}
