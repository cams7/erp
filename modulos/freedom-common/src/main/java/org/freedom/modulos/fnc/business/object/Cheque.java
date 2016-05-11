package org.freedom.modulos.fnc.business.object;

import java.awt.Component;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Extenso;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class Cheque {

	public static final Constant TIPO_CHEQUE_PAG = new Constant( "Pago/Fornecedor", "PF" );

	public static final Constant TIPO_CHEQUE_REC = new Constant( "Recebido/Cliente", "RC" );

	public static final Constant SIT_CHEQUE_CADASTRADO = new Constant( "Cadastrado", "CA" );

	public static final Constant SIT_CHEQUE_EMITIDO = new Constant( "Emitido", "ED" );

	public static final Constant SIT_CHEQUE_COMPENSADO = new Constant( "Compensado", "CD" );

	public static final Constant SIT_CHEQUE_DEVOLVIDO = new Constant( "Devolvido", "DV" );
	
	public static final Constant SIT_CHEQUE_CANCELADO = new Constant( "Cancelado", "CN" );

	private Integer seqcheq;

	private String codbanc; 

	private String agenciacheq;

	private String contacheq;

	private Integer numcheq;

	private String nomeemitcheq;

	private String nomefavcheq;

	private Date dtemitcheq;

	private Date dtvenctocheq;

	private Date dtcompcheq;

	private String tipocheq;

	private String predatcheq;

	private String sitcheq;

	private BigDecimal vlrcheq;

	private String histcheq;

	private String cnpjemitcheq;

	private String cpfemitcheq;

	private String cnpjfavcheq;

	private String cpffavcheq;

	private String dddemitcheq;

	private String foneemitcheq;

	private String dddfavcheq;

	private String fonefavcheq;

	private Component corig;

	private String layoutcheq;

	private String codmoeda;

	private String nomemunic;

	private String singmoeda;

	private String plurmoeda;

	private String decsmoeda;

	private String decpmoeda;

	public static enum COLS_CHEQ {
		SEL, SEQ, DTEMIT, DTVENCTO, NUMCHEQ, CODFOR, NOMEFAVCHEQ, SITCHEQ, VLRCHEQ, NUMCONTA, HISTCHEQ
	};

	public Cheque( Integer seqcheq ) {

		setSeqcheq( seqcheq );
		getInfoCheque();
		getInfoPrefs();

	}
	
	public Cheque( ) {
		
	}

	private void getInfoCheque() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		GregorianCalendar cal = null;

		StringBuilder sql = new StringBuilder();

		try {
			sql.append( "select seqcheq, codbanc, agenciacheq, contacheq, numcheq, nomeemitcheq, nomefavcheq, dtemitcheq, dtvenctocheq, " );
			sql.append( "tipocheq, predatcheq, sitcheq, vlrcheq, histcheq, cnpjemitcheq, cpfemitcheq, cnpjfavcheq, cpffavcheq, dddemitcheq, foneemitcheq, dddfavcheq, fonefavcheq " );
			sql.append( "from fncheque " );
			sql.append( "where codemp=? and codfilial=? and seqcheq=?" );

			ps = Aplicativo.getInstace().con.prepareStatement( sql.toString() );
			cal = new GregorianCalendar();

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNCHEQUE" ) );
			ps.setInt( 3, getSeqcheq() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				setSeqcheq( rs.getInt( "seqcheq" ) );
				setCodbanc( rs.getString( "codbanc" ) );
				setAgenciacheq( rs.getString( "agenciacheq" ) );
				setContacheq( rs.getString( "contacheq" ) );
				setNumcheq( rs.getInt( "numcheq" ) );
				setNomeemitcheq( rs.getString( "nomeemitcheq" ) );
				setNomefavcheq( rs.getString( "nomefavcheq" ) );
				setDtemitcheq( rs.getDate( "dtemitcheq" ) );
				setDtvenctocheq( rs.getDate( "dtvenctocheq" ) );
				setTipocheq( rs.getString( "tipocheq" ) );
				setPredatcheq( rs.getString( "predatcheq" ) );
				setSitcheq( rs.getString( "sitcheq" ) );
				setVlrcheq( rs.getBigDecimal( "vlrcheq" ) );
				setHistcheq( rs.getString( "histcheq" ) );
				setCnpjemitcheq( rs.getString( "cnpjemitcheq" ) );
				setCpfemitcheq( rs.getString( "cpfemitcheq" ) );
				setCnpjfavcheq( rs.getString( "cnpjfavcheq" ) );
				setCpffavcheq( rs.getString( "cpffavcheq" ) );
				setDddemitcheq( rs.getString( "dddemitcheq" ) );
				setFoneemitcheq( rs.getString( "foneemitcheq" ) );
				setDddfavcheq( rs.getString( "dddfavcheq" ) );
				setFonefavcheq( rs.getString( "foneemitcheq" ) );

			}

			rs.close();
			ps.close();
			// Aplicativo.getInstace().con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao buscar informações do cheque!\n" + err.getMessage(), true, null, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			cal = null;
			sql = null;
		}
	}

	public static Vector<String> getLabelsTipoCheq() {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		ret.add( TIPO_CHEQUE_PAG.getName() );
		ret.add( TIPO_CHEQUE_REC.getName() );

		return ret;

	}

	public static Vector<Object> getValoresTipoCheq() {

		Vector<Object> ret = new Vector<Object>();

		ret.addElement( "" );

		ret.add( TIPO_CHEQUE_PAG.getValue().toString() );
		ret.add( TIPO_CHEQUE_REC.getValue().toString() );

		return ret;

	}

	public static Vector<String> getLabelsSitCheq() {

		Vector<String> ret = new Vector<String>();

		ret.addElement( "<--Selecione-->" );

		ret.add( SIT_CHEQUE_CADASTRADO.getName().toString() );
		ret.add( SIT_CHEQUE_COMPENSADO.getName().toString() );
		ret.add( SIT_CHEQUE_DEVOLVIDO.getName().toString() );
		ret.add( SIT_CHEQUE_EMITIDO.getName().toString() );
		ret.add( SIT_CHEQUE_CANCELADO.getName().toString() );

		return ret;

	}

	public static Vector<Object> getValoresSitCheq() {

		Vector<Object> ret = new Vector<Object>();

		ret.addElement( "" );

		ret.add( SIT_CHEQUE_CADASTRADO.getValue().toString() );
		ret.add( SIT_CHEQUE_COMPENSADO.getValue().toString() );
		ret.add( SIT_CHEQUE_DEVOLVIDO.getValue().toString() );
		ret.add( SIT_CHEQUE_EMITIDO.getValue().toString() );
		ret.add( SIT_CHEQUE_CANCELADO.getValue().toString() );

		return ret;

	}

	public Integer getSeqcheq() {

		return seqcheq;
	}

	public void setSeqcheq( Integer seqcheq ) {

		this.seqcheq = seqcheq;
	}

	public String getCodbanc() {

		return codbanc;
	}

	public void setCodbanc( String codbanc ) {

		this.codbanc = codbanc;
	}

	public String getAgenciacheq() {

		return agenciacheq;
	}

	public void setAgenciacheq( String agenciacheq ) {

		this.agenciacheq = agenciacheq;
	}

	public String getContacheq() {

		return contacheq;
	}

	public void setContacheq( String contacheq ) {

		this.contacheq = contacheq;
	}

	public Integer getNumcheq() {

		return numcheq;
	}

	public void setNumcheq( Integer numcheq ) {

		this.numcheq = numcheq;
	}

	public String getNomeemitcheq() {

		return nomeemitcheq;
	}

	public void setNomeemitcheq( String nomeemitcheq ) {

		this.nomeemitcheq = nomeemitcheq;
	}

	public String getNomefavcheq() {

		return nomefavcheq;
	}

	public void setNomefavcheq( String nomefavcheq ) {

		this.nomefavcheq = nomefavcheq;
	}

	public Date getDtemitcheq() {

		return dtemitcheq;
	}

	public void setDtemitcheq( Date dtemitcheq ) {

		this.dtemitcheq = dtemitcheq;
	}

	public Date getDtvenctocheq() {

		return dtvenctocheq;
	}

	public void setDtvenctocheq( Date dtvenctocheq ) {

		this.dtvenctocheq = dtvenctocheq;
	}

	public Date getDtcompcheq() {

		return dtcompcheq;
	}

	public void setDtcompcheq( Date dtcompcheq ) {

		this.dtcompcheq = dtcompcheq;
	}

	public String getTipocheq() {

		return tipocheq;
	}

	public void setTipocheq( String tipocheq ) {

		this.tipocheq = tipocheq;
	}

	public String getPredatcheq() {

		return predatcheq;
	}

	public void setPredatcheq( String predatcheq ) {

		this.predatcheq = predatcheq;
	}

	public String getSitcheq() {

		return sitcheq;
	}

	public void setSitcheq( String sitcheq ) {

		this.sitcheq = sitcheq;
	}

	public BigDecimal getVlrcheq() {

		return vlrcheq;
	}

	public void setVlrcheq( BigDecimal vlrcheq ) {

		this.vlrcheq = vlrcheq;
	}

	public String getHistcheq() {

		return histcheq;
	}

	public void setHistcheq( String histcheq ) {

		this.histcheq = histcheq;
	}

	public String getCnpjemitcheq() {

		return cnpjemitcheq;
	}

	public void setCnpjemitcheq( String cnpjemitcheq ) {

		this.cnpjemitcheq = cnpjemitcheq;
	}

	public String getCpfemitcheq() {

		return cpfemitcheq;
	}

	public void setCpfemitcheq( String cpfemitcheq ) {

		this.cpfemitcheq = cpfemitcheq;
	}

	public String getCnpjfavcheq() {

		return cnpjfavcheq;
	}

	public void setCnpjfavcheq( String cnpjfavcheq ) {

		this.cnpjfavcheq = cnpjfavcheq;
	}

	public String getCpffavcheq() {

		return cpffavcheq;
	}

	public void setCpffavcheq( String cpffavcheq ) {

		this.cpffavcheq = cpffavcheq;
	}

	public String getDddemitcheq() {

		return dddemitcheq;
	}

	public void setDddemitcheq( String dddemitcheq ) {

		this.dddemitcheq = dddemitcheq;
	}

	public String getFoneemitcheq() {

		return foneemitcheq;
	}

	public void setFoneemitcheq( String foneemitcheq ) {

		this.foneemitcheq = foneemitcheq;
	}

	public String getDddfavcheq() {

		return dddfavcheq;
	}

	public void setDddfavcheq( String dddfavcheq ) {

		this.dddfavcheq = dddfavcheq;
	}

	public String getFonefavcheq() {

		return fonefavcheq;
	}

	public void setFonefavcheq( String fonefavcheq ) {

		this.fonefavcheq = fonefavcheq;
	}

	public Component getCorig() {

		return corig;
	}

	public void setCorig( Component corig ) {

		this.corig = corig;
	}

	public String getLayoutcheq() {

		return layoutcheq;
	}

	public void setLayoutcheq( String layoutcheq ) {

		this.layoutcheq = layoutcheq;
	}

	public String getCodmoeda() {

		return codmoeda;
	}

	public void setCodmoeda( String codmoeda ) {

		this.codmoeda = codmoeda;
	}

	public String getNomemunic() {

		return nomemunic;
	}

	public void setNomemunic( String nomemunic ) {

		this.nomemunic = nomemunic;
	}

	public String getSingmoeda() {

		return singmoeda;
	}

	public void setSingmoeda( String singmoeda ) {

		this.singmoeda = singmoeda;
	}

	public String getPlurmoeda() {

		return plurmoeda;
	}

	public void setPlurmoeda( String plurmoeda ) {

		this.plurmoeda = plurmoeda;
	}

	public String getDecsmoeda() {

		return decsmoeda;
	}

	public void setDecsmoeda( String decsmoeda ) {

		this.decsmoeda = decsmoeda;
	}

	public String getDecpmoeda() {

		return decpmoeda;
	}

	public void setDecpmoeda( String decpmoeda ) {

		this.decpmoeda = decpmoeda;
	}

	public void montaLayoutCheq( ImprimeOS imp, Map<String, Object> itemMap ) {

		setLayoutcheq( Cheque.carregaLayoutCheque( getCodbanc(), getCorig() ));
		
		String[] layout = getLayoutcheq().toString().split( "\n" );
		Iterator<String> listKeys = null;
		String key = null;
		int lin = 0;
		int col = 0;
		String campo = "";
		String valor = "";
		String completa = "";
		int tam = 0;
		int pos = 0;
		int ini = 0;
		int posx = 0;
		boolean imprimiu = false;

		for ( int i = 0; i < layout.length; i++ ) {
			// [LIN=01|COL=50|TAM=12|CAMPO=VLRCHEQ]
			pos = layout[ i ].indexOf( "LIN=" );
			ini = -1;
			if ( pos >= 0 ) {
				pos += 4;
				lin = Integer.parseInt( layout[ i ].substring( pos, layout[ i ].indexOf( "|", pos ) ) );
				pos = layout[ i ].indexOf( "COL=", pos );
				if ( pos >= 0 ) {
					pos += 4;
					col = Integer.parseInt( layout[ i ].substring( pos, layout[ i ].indexOf( "|", pos ) ) );
					pos = layout[ i ].indexOf( "TAM=", pos );
					if ( pos >= 0 ) {
						pos += 4;
						tam = Integer.parseInt( layout[ i ].substring( pos, layout[ i ].indexOf( "|", pos ) ) );
						posx = pos;
						pos = layout[ i ].indexOf( "INI=", posx );
						if ( pos >= 0 ) {
							pos += 4;
							ini = Integer.parseInt( layout[ i ].substring( pos, layout[ i ].indexOf( "|", pos ) ) );
							posx = pos;
						}
						pos = layout[ i ].indexOf( "COMPL=", posx );
						if ( pos >= 0 ) {
							pos += 6;
							completa = layout[ i ].substring( pos, layout[ i ].indexOf( "|", pos ) );
						}
						else {
							completa = "";
						}
						pos = layout[ i ].indexOf( "CAMPO=", posx );
						if ( pos >= 0 ) {
							pos += 6;
							campo = layout[ i ].substring( pos, layout[ i ].indexOf( "]", pos ) );
							valor = itemMap.get( campo ).toString();
						}
						else {
							pos = layout[ i ].indexOf( "TEXTO=", posx );
							if ( pos >= 0 ) {
								pos += 6;
								valor = layout[ i ].substring( pos, layout[ i ].indexOf( "]", pos ) );
							}
						}
						if ( ini > 0 ) {
							if ( valor.length() >= ini ) {
								valor = valor.substring( ini - 1 );
							}
							else {
								valor = "";
							}
						}
						// caso o tamanho de valor seja maior que o tamanho desejado
						if ( valor.length() > tam ) {
							valor = valor.substring( 0, tam );

						} // caso o valor seja menor que tam e precise de complemento
						else if ( ( valor.length() < tam ) && ( !"".equals( completa ) ) ) {
							valor = Funcoes.completaTexto( valor, tam, completa );
						}
						if ( pos >= 0 ) {
							imp.say( lin, col, valor );
							if ( !imprimiu ) {
								imprimiu = true;
							}
						}
					}
				}
			}
		}
		if ( imprimiu ) {
			imp.say( imp.pRow(), imp.pCol() + 1, "\n" );
		}
	}

	public Map<String, Object> montaMap( Vector<Object> item ) {

		Map<String, Object> result = new HashMap<String, Object>();

		String str = item.elementAt( COLS_CHEQ.DTEMIT.ordinal() ).toString();
		result.put( "DTEMIT", str );

		str = Funcoes.dateToStrExtenso( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTEMIT.ordinal() ) ) ).toUpperCase();
		result.put( "DTEMITEX", str );

		str = String.valueOf( Funcoes.getDiaMes( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTEMIT.ordinal() ) ) ) );
		result.put( "DIAEMIT", str );

		str = Funcoes.getMesExtenso( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTEMIT.ordinal() ) ) ).toUpperCase();
		result.put( "MESEMIT", str );

		str = String.valueOf( Funcoes.getAno( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTEMIT.ordinal() ) ) ) );
		result.put( "ANOEMIT", str );

		str = item.elementAt( COLS_CHEQ.DTVENCTO.ordinal() ).toString();
		result.put( "DTVENCTO", str );

		str = Funcoes.dateToStrExtenso( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTVENCTO.ordinal() ) ) ).toUpperCase();
		result.put( "DTVENCTOEX", str );

		str = String.valueOf( Funcoes.getDiaMes( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTVENCTO.ordinal() ) ) ) );
		result.put( "DIAVENCTO", str );

		str = Funcoes.getMesExtenso( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTVENCTO.ordinal() ) ) ).toUpperCase();
		result.put( "MESVENCTO", str );

		str = String.valueOf( Funcoes.getAno( Funcoes.strDateToDate( (String) item.elementAt( COLS_CHEQ.DTVENCTO.ordinal() ) ) ) );
		result.put( "ANOVENCTO", str );

		str = item.elementAt( COLS_CHEQ.NUMCHEQ.ordinal() ).toString();
		result.put( "NUMCHEQ", str );

		str = item.elementAt( COLS_CHEQ.NOMEFAVCHEQ.ordinal() ).toString().trim().toUpperCase();
		result.put( "NOMEFAVCHEQ", str );

		str = "#( " + item.elementAt( COLS_CHEQ.VLRCHEQ.ordinal() ).toString() + " )#";
		result.put( "VLRCHEQ", str );

		str = "#( " + Extenso.extenso( ConversionFunctions.stringCurrencyToBigDecimal( (String) item.elementAt( COLS_CHEQ.VLRCHEQ.ordinal() ) ).doubleValue(), getSingmoeda(), getPlurmoeda(), getDecsmoeda(), getDecpmoeda() ).toUpperCase() + " )#";

		result.put( "VLRCHEQEX", str );

		str = getNomemunic().toUpperCase();
		result.put( "NOMEMUNIC", str );

		return result;
	}

	public Map<String, Object> montaMap(  ) {

		Map<String, Object> result = new HashMap<String, Object>();

		result.put( "DTEMIT", Funcoes.dateToStrDate( getDtemitcheq() ) );
		result.put( "DTEMITEX", Funcoes.dateToStrExtenso( getDtemitcheq() ) );
		result.put( "DIAEMIT", Funcoes.getDiaMes( getDtemitcheq() ));
		result.put( "MESEMIT", Funcoes.getMesExtenso( getDtemitcheq() ));
		result.put( "ANOEMIT", Funcoes.getAno( getDtemitcheq() ));
		result.put( "DTVENCTO", getDtvenctocheq() );		
		result.put( "DTVENCTOEX", Funcoes.dateToStrExtenso( getDtvenctocheq() ) );
		result.put( "DIAVENCTO", Funcoes.getDiaMes( getDtvenctocheq() ));
		result.put( "MESVENCTO", Funcoes.getMesExtenso( getDtvenctocheq() ));
		result.put( "ANOVENCTO", Funcoes.getAno( getDtvenctocheq() ));
		result.put( "NUMCHEQ", getNumcheq() );
		result.put( "NOMEFAVCHEQ", getNomefavcheq() );
		result.put( "VLRCHEQ", "#( " + Funcoes.bdToStr( getVlrcheq(), Aplicativo.casasDec ) + " )#" );

		result.put( "VLRCHEQEX", "#( " 
				+ Extenso.extenso( getVlrcheq().doubleValue(), getSingmoeda(), getPlurmoeda(), getDecsmoeda(), getDecpmoeda() ).toUpperCase()				
				+ " )#");

		
		result.put( "NOMEMUNIC", getNomemunic().toUpperCase() );

		return result;
	}

	public static String carregaLayoutCheque( String codbanco, Component corig ) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer( "SELECT B.LAYOUTCHEQBANCO FROM FNBANCO B " );
		String result = null;
		sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODBANCO=? " );

		DbConnection con = Aplicativo.getInstace().getConexao();

		try {
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNBANCO" ) );
			ps.setString( 3, codbanco );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				result = rs.getString( "LAYOUTCHEQBANCO" );
			}
			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( corig, "Erro consultando layout de cheque!\n" + e.getMessage() );
		}
		return result;
	}

	private void getInfoPrefs() {

		ResultSet rs = null;
		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer();

		sql.append( "SELECT P.CODMOEDA, MC.NOMEMUNIC, " );
		sql.append( "M.SINGMOEDA, M.PLURMOEDA, M.DECSMOEDA, M.DECPMOEDA " );
		sql.append( "FROM FNMOEDA M, SGPREFERE1 P, SGFILIAL F, SGMUNICIPIO MC " );
		sql.append( "WHERE M.CODMOEDA=P.CODMOEDA AND P.CODEMP=? AND P.CODFILIAL=? AND " );
		sql.append( "F.CODEMP=? AND F.CODFILIAL=? AND " );
		sql.append( "MC.CODPAIS=F.CODPAIS AND MC.SIGLAUF=F.SIGLAUF AND MC.CODMUNIC=F.CODMUNIC" );

		DbConnection con = Aplicativo.getInstace().getConexao();

		try {
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "SGFILIAL" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {

				setCodmoeda( rs.getString( "CODMOEDA" ) );
				setNomemunic( rs.getString( "NOMEMUNIC" ) );
				setSingmoeda( rs.getString( "SINGMOEDA" ) );
				setPlurmoeda( rs.getString( "PLURMOEDA" ) );
				setDecsmoeda( rs.getString( "DECSMOEDA" ) );
				setDecpmoeda( rs.getString( "DECPMOEDA" ) );

			}
			con.commit();
		} 		
		catch ( SQLException e ) {
			Funcoes.mensagemErro( getCorig(), "Erro consultando preferências!\n" + e.getMessage() );
		}
	}

}
