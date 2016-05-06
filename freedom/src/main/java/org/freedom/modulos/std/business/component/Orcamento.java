package org.freedom.modulos.std.business.component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.freedom.infra.pojos.Constant;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class Orcamento {

	public static final Constant STATUS_ABERTO = new Constant( "Aberto", "*" );

	public static final Constant STATUS_PENDENTE = new Constant( "Pendente", "OA" );

	public static final Constant STATUS_COMPLETO = new Constant( "Completo/Impresso", "OC" );

	public static final Constant STATUS_APROVADO = new Constant( "Liberado/Aprovado", "OL" );

	public static final Constant STATUS_FATURADO = new Constant( "Faturado", "OV" );

	public static final Constant STATUS_FATURADO_PARCIAL = new Constant( "Fat. parcial", "FP" );

	public static final Constant STATUS_PRODUZIDO = new Constant( "Produzido", "OP" );

	public static final Constant STATUS_CANCELADO = new Constant( "Cancelado", "CA" );

	public enum PrefOrc {
		USAREFPROD, USALIQREL, TIPOPRECOCUSTO, CODTIPOMOV2, DESCCOMPPED, USAORCSEQ, OBSCLIVEND, RECALCPCORC, USABUSCAGENPROD, USALOTEORC, CONTESTOQ, 
		TITORCTXT01, VENDAMATPRIM, VISUALIZALUCR, DIASVENCORC, CODCLI, CODPLANOPAG, PRAZO, CLASSORC, DESCORC, CONTRIBIPI, ABATRANSP, ORDNOTA, TIPOCUSTO, 
		HABVLRTOTITORC, SMODONOTA, COMISSAODESCONTO, VDPRODQQCLAS, BLOQDESCCOMPORC, BLOQPRECOORC, BLOQCOMISSORC, PERMITIMPORCANTAP, BLOQEDITORCAPOSAP, CODMODELOR;
	}

	public static Date getVencimento( int diasvencorc ) {

		Date dtRet = null;

		try {

			GregorianCalendar clVenc = new GregorianCalendar();
			clVenc.add( Calendar.DATE, diasvencorc );

			dtRet = clVenc.getTime();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar a data de vencimento.\n" + err.getMessage(), true, err );
			err.printStackTrace();
		}

		return dtRet;
	}

	public static Object[] getPrefere() {

		Object[] oRetorno = new Object[ Orcamento.PrefOrc.values().length ];
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			sql.append( "SELECT P1.USAREFPROD, P1.USALIQREL, P1.TIPOPRECOCUSTO, COALESCE(P1.CODTIPOMOV2, 0) CODTIPOMOV2 , P1.CONTESTOQ " );
			sql.append( ",P1.ORDNOTA, P1.DESCCOMPPED, P1.USAORCSEQ, P1.OBSCLIVEND, P1.RECALCPCORC,COALESCE(P1.TITORCTXT01,'') TITORCTXT01 " );
			sql.append( ",P1.VENDAMATPRIM, P1.TABTRANSPORC, P1.VISUALIZALUCR, P1.CLASSORC, COALESCE(P1.DESCORC,'Orçamento') DESCORC " );
			sql.append( ",P4.USALOTEORC, P4.USABUSCAGENPROD, COALESCE(P4.DIASVENCORC,0) DIASVENCORC, COALESCE(P4.CODCLI,0) CODCLI " );
			sql.append( ",COALESCE(P4.CODPLANOPAG,0) CODPLANOPAG, COALESCE(P4.PRAZO,0) PRAZO " );
			sql.append( ",FI.CONTRIBIPIFILIAL, P1.TIPOCUSTOLUC, HabVlrTotItOrc, P1.COMISSAODESCONTO, P1.VDPRODQQCLAS " );
			sql.append( ", P1.BLOQDESCCOMPORC, P1.BLOQPRECOORC, P1.BLOQCOMISSORC, P1.PERMITIMPORCANTAP, P1.BLOQEDITORCAPOSAP, P3.CODMODELOR ");

			sql.append( "FROM SGPREFERE1 P1,  SGPREFERE3 P3, SGPREFERE4 P4, SGFILIAL FI " );
			sql.append( "WHERE P1.CODEMP=? AND P1.CODFILIAL=? AND " );
			sql.append( "P3.CODEMP=P1.CODEMP AND P3.CODFILIAL=P1.CODFILIAL AND " );
			sql.append( "P4.CODEMP=P1.CODEMP AND P4.CODFILIAL=P1.CODFILIAL AND " );
			sql.append( "FI.CODEMP=P1.CODEMP AND FI.CODFILIAL=P1.CODFILIAL " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				if ( rs.getString( "USALIQREL" ) == null ) {
					oRetorno[ Orcamento.PrefOrc.USALIQREL.ordinal() ] = new Boolean( false );
					Funcoes.mensagemInforma( null, "Preencha opção de desconto em preferências!" );
				}
				else {
					oRetorno[ Orcamento.PrefOrc.USALIQREL.ordinal() ] = new Boolean( rs.getString( "UsaLiqRel" ).trim().equals( "S" ) );
				}

				oRetorno[ Orcamento.PrefOrc.USAREFPROD.ordinal() ] = new Boolean( rs.getString( "UsaRefProd" ).trim().equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.TIPOPRECOCUSTO.ordinal() ] = new Boolean( rs.getString( "TipoPrecoCusto" ).equals( "M" ) );
				oRetorno[ Orcamento.PrefOrc.CODTIPOMOV2.ordinal() ] = new Integer( rs.getInt( "CODTIPOMOV2" ) );
				oRetorno[ Orcamento.PrefOrc.DESCCOMPPED.ordinal() ] = new Boolean( rs.getString( "DescCompPed" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.USAORCSEQ.ordinal() ] = new Boolean( rs.getString( "UsaOrcSeq" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.OBSCLIVEND.ordinal() ] = new Boolean( rs.getString( "ObsCliVend" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.RECALCPCORC.ordinal() ] = new Boolean( rs.getString( "ReCalcPCOrc" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.USABUSCAGENPROD.ordinal() ] = new Boolean( rs.getString( "USABUSCAGENPROD" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.USALOTEORC.ordinal() ] = new Boolean( rs.getString( "USALOTEORC" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.CONTESTOQ.ordinal() ] = new Boolean( rs.getString( "CONTESTOQ" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.TITORCTXT01.ordinal() ] = rs.getString( "TitOrcTxt01" );
				oRetorno[ Orcamento.PrefOrc.VENDAMATPRIM.ordinal() ] = "S".equals( rs.getString( "VendaMatPrim" ) );
				oRetorno[ Orcamento.PrefOrc.VISUALIZALUCR.ordinal() ] = "S".equals( rs.getString( "VISUALIZALUCR" ) );
				oRetorno[ Orcamento.PrefOrc.DIASVENCORC.ordinal() ] = rs.getInt( "DIASVENCORC" );
				oRetorno[ Orcamento.PrefOrc.CODCLI.ordinal() ] = rs.getString( "CODCLI" );
				oRetorno[ Orcamento.PrefOrc.CODPLANOPAG.ordinal() ] = rs.getString( "CODPLANOPAG" );
				oRetorno[ Orcamento.PrefOrc.PRAZO.ordinal() ] = rs.getString( "PRAZO" );
				oRetorno[ Orcamento.PrefOrc.CLASSORC.ordinal() ] = rs.getString( "CLASSORC" );
				oRetorno[ Orcamento.PrefOrc.DESCORC.ordinal() ] = rs.getString( "DESCORC" );
				oRetorno[ Orcamento.PrefOrc.ORDNOTA.ordinal() ] = rs.getString( "ORDNOTA" );
				oRetorno[ Orcamento.PrefOrc.CONTRIBIPI.ordinal() ] = rs.getString( "CONTRIBIPIFILIAL" );
				oRetorno[ Orcamento.PrefOrc.ABATRANSP.ordinal() ] = rs.getString( "TABTRANSPORC" );
				oRetorno[ Orcamento.PrefOrc.TIPOCUSTO.ordinal() ] = rs.getString( "TIPOCUSTOLUC" );
				oRetorno[ Orcamento.PrefOrc.HABVLRTOTITORC.ordinal() ] = new Boolean( rs.getString( "HabVlrTotItOrc" ).equals( "S" ) );
				oRetorno[ Orcamento.PrefOrc.COMISSAODESCONTO.ordinal() ] = "S".equals( rs.getString( "COMISSAODESCONTO" ) );
				oRetorno[ Orcamento.PrefOrc.VDPRODQQCLAS.ordinal() ] = "S".equals( rs.getString( Orcamento.PrefOrc.VDPRODQQCLAS.toString() ) );
				oRetorno[ Orcamento.PrefOrc.BLOQDESCCOMPORC.ordinal() ] = "S".equals( rs.getString( Orcamento.PrefOrc.BLOQDESCCOMPORC.toString() ) );
				oRetorno[ Orcamento.PrefOrc.BLOQPRECOORC.ordinal() ] = "S".equals( rs.getString( Orcamento.PrefOrc.BLOQPRECOORC.toString() ) );
				oRetorno[ Orcamento.PrefOrc.BLOQCOMISSORC.ordinal() ] = "S".equals( rs.getString( Orcamento.PrefOrc.BLOQCOMISSORC.toString() ) );
				oRetorno[ Orcamento.PrefOrc.PERMITIMPORCANTAP.ordinal() ] = "S".equals( rs.getString( Orcamento.PrefOrc.PERMITIMPORCANTAP.toString() ) );
				oRetorno[ Orcamento.PrefOrc.BLOQEDITORCAPOSAP.ordinal() ] = "S".equals( rs.getString( Orcamento.PrefOrc.BLOQEDITORCAPOSAP.toString() ) );
				oRetorno[ Orcamento.PrefOrc.CODMODELOR.ordinal() ] = rs.getInt(  Orcamento.PrefOrc.CODMODELOR.toString() ) ;
			}

			rs.close();
			ps.close();

			String sSQL = "SELECT IMPGRAFICA FROM SGESTACAOIMP WHERE CODEMP=? AND CODFILIAL=? AND IMPPAD='S' AND CODEST=?";
			ps = Aplicativo.getInstace().getConexao().prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGESTACAOIMP" ) );
			ps.setInt( 3, Aplicativo.iNumEst );
			rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( ( rs.getString( "IMPGRAFICA" ) != null ) && ( !rs.getString( "IMPGRAFICA" ).equals( "S" ) ) ) {
					oRetorno[ Orcamento.PrefOrc.SMODONOTA.ordinal() ] = "T";
				}
				else {
					oRetorno[ Orcamento.PrefOrc.SMODONOTA.ordinal() ] = "G";
				}
			}

			rs.close();
			ps.close();
			Aplicativo.getInstace().getConexao().commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( null, "Erro ao carregar a tabela SGPREFERE1!\n" + err.getMessage(), true, err );
		} finally {
			ps = null;
			rs = null;
		}

		return oRetorno;
	}

}
