/**
 * @version 05/07/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.componentes <BR>
 * Classe:
 * @(#)NF.java <BR>
 * 
 * Este programa é licenciado de acordo com a LPG-PC (Licença Pública Geral para
 * Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * A LPG-PC deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR> 
 * Caso uma cópia da LPG-PC não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários sobre a classe...
 *  
 */

package org.freedom.library.business.component;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.TabVector;
import org.freedom.library.swing.frame.Aplicativo;

public class NF {

	public static final int TPNF_NONE = -1;
	public static final int TPNF_ENTRADA = 0;
	public static final int TPNF_SAIDA = 1;

	public static final int T_CAB = 0;
	public static final int T_ITENS = 1;
	public static final int T_PARC = 2;
	public static final int T_ADIC = 3;
	public static final int T_FRETE = 4;

	// cab
	public static final int C_CODPED = 0;
	public static final int C_CODEMIT = 1;
	public static final int C_RAZEMIT = 2;
	public static final int C_CNPJEMIT = 3;
	public static final int C_CPFEMIT = 4;
	public static final int C_ENDEMIT = 5;
	public static final int C_NUMEMIT = 6;
	public static final int C_COMPLEMIT = 7;
	public static final int C_BAIREMIT = 8;
	public static final int C_CEPEMIT = 9;
	public static final int C_CIDEMIT = 10;
	public static final int C_UFEMIT = 11;
	public static final int C_FONEEMIT = 12;
	public static final int C_FAXEMIT = 13;
	public static final int C_DDDEMIT = 14;
	public static final int C_INSCEMIT = 15;
	public static final int C_RGEMIT = 16;
	public static final int C_EMAILEMIT = 17;
	public static final int C_SITEEMIT = 18;
	public static final int C_CONTEMIT = 19;
	public static final int C_DTEMITPED = 20;
	public static final int C_DOC = 21;
	public static final int C_INCRAEMIT = 22;
	public static final int C_DTSAIDA = 23;
	public static final int C_CODPLANOPG = 24;
	public static final int C_DESCPLANOPAG = 25;
	public static final int C_OBSPED = 26;
	public static final int C_NOMEVEND = 27;
	public static final int C_EMAILVEND = 28;
	public static final int C_DESCFUNC = 29;
	public static final int C_CODCLCOMIS = 30;
	public static final int C_PERCCOMISVENDA = 31;
	public static final int C_CODVEND = 32;
	public static final int C_ENDCOBEMIT = 33;
	public static final int C_CIDCOBEMIT = 34;
	public static final int C_UFCOBEMIT = 35;
	public static final int C_BAIRCOBEMIT = 36;
	public static final int C_NUMCOBEMIT = 37;
	public static final int C_PERCMCOMISPED = 38;
	public static final int C_NOMEEMIT = 39;
	public static final int C_ENDENTEMIT = 40;
	public static final int C_NUMENTEMIT = 41;
	public static final int C_COMPLENTEMIT = 42;
	public static final int C_BAIRENTEMIT = 43;
	public static final int C_CIDENTEMIT = 44;
	public static final int C_UFENTEMIT = 45;
	public static final int C_CODBANCO = 46;
	public static final int C_NOMEBANCO = 47;
	public static final int C_DESCSETOR = 48;
	public static final int C_VLRDESCITPED = 49;
	public static final int C_DIASPAG = 50;
	public static final int C_PEDEMIT = 51;
	public static final int C_VLRLIQPED = 52;
	public static final int C_VLRPRODPED = 53;
	public static final int C_VLRADICPED = 54;
	public static final int C_VLRBASEICMSPED = 55;
	public static final int C_VLRICMSPED = 56;
	public static final int C_VLRIPIPED = 57;
	public static final int C_BASEISS = 58;
	public static final int C_VLRISS = 59;
	public static final int C_PERCISS = 60;
	public static final int C_HALT = 61;
	public static final int C_CALCCOFINSVENDA = 62;
	public static final int C_IMPCOFINSVENDA = 63;
	public static final int C_CALCCSOCIALVENDA = 64;
	public static final int C_IMPCSOCIALVENDA = 65;
	public static final int C_CALCICMSVENDA = 66;
	public static final int C_IMPICMSVENDA = 67;
	public static final int C_CALCIPIVENDA = 68;
	public static final int C_IMPIPIVENDA = 69;
	public static final int C_CALCIRVENDA = 70;
	public static final int C_IMPIRVENDA = 71;
	public static final int C_CALCISSVENDA = 72;
	public static final int C_IMPIISSVENDA = 73;
	public static final int C_CALCPISVENDA = 74;
	public static final int C_IMPPISVENDA = 75;
	public static final int C_VLRDESCVENDA = 76;
	public static final int C_VLRPISVENDA = 77;
	public static final int C_VLRCOFINSVENDA = 78;
	public static final int C_VLRIRVENDA = 79;
	public static final int C_VLRCSOCIALVENDA = 80;
	public static final int C_VLRBASEISSVENDA = 81;
	public static final int C_TIPOMOV = 82;
	public static final int C_MENSAGENS = 83;
	public static final int C_VLRBASEICMSST = 84;
	public static final int C_VLRICMSST = 85;
	public static final int C_ENDFILIAL = 86;
	public static final int C_NUMFILIAL = 87;
	public static final int C_BAIRFILIAL = 88;
	public static final int C_CIDFILIAL = 89;
	public static final int C_UFFILIAL = 90;
	public static final int C_CEPFILIAL = 91;
	public static final int C_FONEFILIAL = 92;
	public static final int C_WWWFILIAL = 93;
	public static final int C_EMAILFILIAL = 94;
	public static final int C_CEPENTEMIT = 95;
	public static final int C_VLRFUNRURALCOMPRA = 96;
	public static final int TAM_CAB = 97;

	// itens
	public static final int C_CODITPED = 0;
	public static final int C_CODPROD = 1;
	public static final int C_REFPROD = 2;
	public static final int C_DESCPROD = 3;
	public static final int C_OBSITPED = 4;
	public static final int C_CODUNID = 5;
	public static final int C_QTDITPED = 6;
	public static final int C_VLRLIQITPED = 7;
	public static final int C_PERCIPIITPED = 8;
	public static final int C_PERCICMSITPED = 9;
	public static final int C_VLRIPIITPED = 10;
	public static final int C_IMPDTSAIDA = 11;
	public static final int C_VLRPRODITPED = 12;
	public static final int C_DESCNAT = 13;
	public static final int C_CODNAT = 14;
	public static final int C_CODLOTE = 15;
	public static final int C_VENCLOTE = 16;
	public static final int C_ORIGFISC = 17;
	public static final int C_CODTRATTRIB = 18;
	public static final int C_VLRADICITPED = 19;
	public static final int C_CONTAITENS = 20;
	public static final int C_DESCFISC = 21;
	public static final int C_DESCFISC2 = 22;
	public static final int C_CODFISC = 23;
	public static final int C_TIPOPROD = 24;
	public static final int C_VLRISSITPED = 25;
	public static final int C_VLRDESCITPROD = 26;
	public static final int C_CODBAR = 27;
	public static final int C_CODCLASSFISC = 28;
	public static final int C_PERCDESCITVENDA = 29;
	public static final int C_CODFABPROD = 30;
	public static final int C_QTDEMBALAGEM = 31;
	public static final int C_ALIQFUNRURALITCOMPRA = 32;

	public static final int TAM_ITENS = 33;

	// adic
	public static final int C_CODAUXV = 0;
	public static final int C_CPFEMITAUX = 1;
	public static final int C_NOMEEMITAUX = 2;
	public static final int C_CIDEMITAUX = 3;
	public static final int C_UFEMITAUX = 4;

	// parc
	public static final int C_DTVENCTO = 0;
	public static final int C_VLRPARC = 1;
	public static final int C_NPARCITREC = 2;

	// frete
	public static final int C_CODTRAN = 0;
	public static final int C_RAZTRANSP = 1;
	public static final int C_NOMETRANSP = 2;
	public static final int C_INSCTRANSP = 3;
	public static final int C_CNPJTRANSP = 4;
	public static final int C_TIPOTRANSP = 5;
	public static final int C_ENDTRANSP = 6;
	public static final int C_NUMTRANSP = 7;
	public static final int C_CIDTRANSP = 8;
	public static final int C_UFTRANSP = 9;
	public static final int C_TIPOFRETE = 10;
	public static final int C_PLACAFRETE = 11;
	public static final int C_UFFRETE = 12;
	public static final int C_QTDFRETE = 13;
	public static final int C_ESPFRETE = 14;
	public static final int C_MARCAFRETE = 15;
	public static final int C_PESOBRUTO = 16;
	public static final int C_PESOLIQ = 17;
	public static final int C_VLRFRETEPED = 18;
	public static final int C_CONHECFRETEPED = 19;
	public static final int C_CPFTRANSP = 20;
	public static final int C_ADICFRETEBASEICM = 21;
	public static final int C_ALIQICMSFRETEVD = 22;
	public static final int C_VLRICMSFRETEVD = 23;
	public static final int C_DDDTRANSP = 24;
	public static final int C_FONETRANSP = 25;

	protected TabVector cab = null;
	protected TabVector itens = null;
	protected TabVector parc = null;
	protected TabVector adic = null;
	protected TabVector frete = null;
	protected int tipoNF = TPNF_NONE;

	protected int casasDec = Aplicativo.casasDec;
	protected int casasDecFin = Aplicativo.casasDecFin;
	protected int casasDecPre = Aplicativo.casasDecPre;

	private DbConnection con = null;

	public NF(int casasDec) {
		super();
		this.casasDec = casasDec;
	}

	protected void setConexao(DbConnection arg0) {
		con = arg0;
	}

	public DbConnection getConexao() {
		return con;
	}

	public int getCasasDec() {
		return casasDec;
	}

	public int getCasasDecFin() {
		return casasDecFin;
	}

	public int getCasasDecPre() {
		return casasDecPre;
	}
	
	public int getTipoNF() {
		return tipoNF;
	}

	public TabVector getTabVector(int vector) {
		TabVector t = null;
		switch (vector) {
		case T_CAB:
			t = cab;
			break;
		case T_ITENS:
			t = itens;
			break;
		case T_PARC:
			t = parc;
			break;
		case T_ADIC:
			t = adic;
			break;
		case T_FRETE:
			t = frete;
			break;
		}
		return t;
	}

}
