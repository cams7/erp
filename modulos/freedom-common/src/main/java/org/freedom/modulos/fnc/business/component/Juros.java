package org.freedom.modulos.fnc.business.component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class Juros {

	private static final char JURO_DIARIO = 'D';

	private static final char JURO_MENSAL = 'M';

	private static final char JURO_BIMESTRAL = 'B';

	private static final char JURO_TRIMESTRAL = 'T';

	private static final char JURO_SEMESTRAL = 'S';

	private static final char JURO_ANUAL = 'A';

	private Date dtvenc = null;

	private BigDecimal vlrjuros = new BigDecimal( 0 ); // Valor dos juros

	private BigDecimal vlrorig = null; // Valor original

	private BigDecimal vlrcomjuros = null; // Valor original

	private BigDecimal percjuros = null; // Percentual de juros da tabela

	private String tipotabjuros = null; // Tipo da Tabela de Juros;

	public Juros() {

		getTabJuros();
	}

	public Juros( Date dtvenc, BigDecimal vlrorig ) {

		if ( getTipotabjuros() == null ) {
			getTabJuros();
		}

		setVlrorig( vlrorig );
		setDtvenc( dtvenc );

		carregaValores( dtvenc, vlrorig );

	}

	public void carregaValores( Date dtvenc, BigDecimal vlrorig ) {

		setVlrorig( vlrorig );
		setDtvenc( dtvenc );

		if ( getPercjuros() != null && getPercjuros().floatValue() > 0 && getVlrorig() != null && getVlrorig().floatValue() > 0 ) {
			aplicaJuros();
		}
		else {
			System.out.println( "Tabela de juros, ou valor original inválidos." );
		}
	}

	public Date getDtvenc() {

		return dtvenc;
	}

	public void setDtvenc( Date dtvenc ) {

		this.dtvenc = dtvenc;
	}

	private BigDecimal getTabJuros() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		GregorianCalendar cal = null;

		StringBuilder sql = new StringBuilder();
		BigDecimal vlrjuros = new BigDecimal( 0 );

		try {
			sql.append( "select first 1 p.codtbj, t.tipotbj, it.percittbj " );
			sql.append( "from " );
			sql.append( "sgprefere1 p, fntbjuros t, fnittbjuros it " );
			sql.append( "where " );
			sql.append( "t.codemp=p.codemptj and t.codfilial=p.codfilialtj and t.codtbj=p.codtbj " );
			sql.append( "and it.codemp=t.codemp and it.codfilial=t.codfilial and it.codtbj=t.codtbj " );
			sql.append( "and it.anoittbj <= ? and it.mesittbj <= ? " );
			sql.append( "and p.codemp=? and p.codfilial=? " );
			sql.append( "order by it.anoittbj desc, it.mesittbj desc " );

			ps = Aplicativo.getInstace().con.prepareStatement( sql.toString() );
			cal = new GregorianCalendar();

			ps.setInt( 1, cal.get( Calendar.YEAR ) );
			ps.setInt( 2, cal.get( Calendar.MONTH ) + 1 );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, Aplicativo.iCodFilial );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				setPercjuros( rs.getBigDecimal( "percittbj" ) );
				setTipotabjuros( rs.getString( "tipotbj" ) );
			}

			rs.close();
			ps.close();
			// Aplicativo.getInstace().con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar tabela de juros do sistema!\n" + err.getMessage(), true, null, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			cal = null;
			sql = null;
		}
		return vlrjuros;
	}

	public void aplicaJuros() {

		char tipotabjuros = getTipotabjuros().toCharArray()[ 0 ];
		try {
			if ( getPercjuros() != null && getPercjuros().floatValue() > 0 ) {
				switch ( tipotabjuros ) {
					case JURO_DIARIO :
						setVlrjuros( vlrorig.multiply( new BigDecimal( ( Funcoes.getNumDiasAbs( getDtvenc(), new Date() ) ) * getPercjuros().doubleValue() ) ).divide( new BigDecimal( 100 ), 2, BigDecimal.ROUND_HALF_UP ) );
						break;
					case JURO_MENSAL :
						setVlrjuros( vlrorig.multiply( new BigDecimal( ( Funcoes.getNumDiasAbs( getDtvenc(), new Date() ) ) * getPercjuros().doubleValue() ) ).divide( new BigDecimal( 100 * 30 ), 2, BigDecimal.ROUND_HALF_UP ) );
						break;
					case JURO_BIMESTRAL :
						setVlrjuros( vlrorig.multiply( new BigDecimal( ( Funcoes.getNumDiasAbs( getDtvenc(), new Date() ) ) * getPercjuros().doubleValue() ) ).divide( new BigDecimal( 100 * 60 ), 2, BigDecimal.ROUND_HALF_UP ) );
						break;
					case JURO_TRIMESTRAL :
						setVlrjuros( vlrorig.multiply( new BigDecimal( ( Funcoes.getNumDiasAbs( getDtvenc(), new Date() ) ) * getPercjuros().doubleValue() ) ).divide( new BigDecimal( 100 * 90 ), 2, BigDecimal.ROUND_HALF_UP ) );
						break;
					case JURO_SEMESTRAL :
						setVlrjuros( vlrorig.multiply( new BigDecimal( ( Funcoes.getNumDiasAbs( getDtvenc(), new Date() ) ) * getPercjuros().doubleValue() ) ).divide( new BigDecimal( 100 * 182 ), 2, BigDecimal.ROUND_HALF_UP ) );
						break;
					case JURO_ANUAL :
						setVlrjuros( vlrorig.multiply( new BigDecimal( ( Funcoes.getNumDiasAbs( getDtvenc(), new Date() ) ) * getPercjuros().doubleValue() ) ).divide( new BigDecimal( 100 * 365 ), 2, BigDecimal.ROUND_HALF_UP ) );
						break;
				}
				setVlrcomjuros( getVlrorig().add( getVlrjuros() ) );
			}
		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao verificar juros do sistema!\n" + err.getMessage(), true, null, err );
			err.printStackTrace();
		}

	}

	public static boolean getJurosPosCalc() {

		boolean bRet = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT JUROSPOSCALC FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		try {
			ps = Aplicativo.getInstace().con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				bRet = rs.getString( "JUROSPOSCALC" ) != null && rs.getString( "JUROSPOSCALC" ).equals( "S" );
			}
			rs.close();
			ps.close();
			// Aplicativo.getInstace().con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar juros pos calculados.\n" + err.getMessage(), true, null, err );
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRet;
	}

	public BigDecimal getVlrjuros() {

		return vlrjuros;
	}

	public void setVlrjuros( BigDecimal vlrjuros ) {

		this.vlrjuros = vlrjuros;
	}

	public BigDecimal getVlrjuros( Date dtvenc, BigDecimal vlrorig ) {

		carregaValores( dtvenc, vlrorig );
		return getVlrjuros();
	}

	public BigDecimal getVlrorig() {

		return vlrorig;
	}

	public void setVlrorig( BigDecimal vlrorig ) {

		this.vlrorig = vlrorig;
	}

	public BigDecimal getVlrcomjuros() {

		return vlrcomjuros;
	}

	public void setVlrcomjuros( BigDecimal vlrcomjuros ) {

		this.vlrcomjuros = vlrcomjuros;
	}

	public String getTipotabjuros() {

		return tipotabjuros;
	}

	public void setTipotabjuros( String tipotabjuros ) {

		this.tipotabjuros = tipotabjuros;
	}

	public BigDecimal getPercjuros() {

		return percjuros;
	}

	public void setPercjuros( BigDecimal percjuros ) {

		this.percjuros = percjuros;
	}

}
