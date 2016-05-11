package org.freedom.modulos.crm.business.object;

import java.util.Date;

public class Atendimento {
	/*    iu char(1),   codemp,   codfilial,   codatendo,   codempto,  codfilialto,   codtpatendo,
    codempae,   codfilialae,   codatend, codempsa ,  codfilialsa,   codsetor, 
     docatendo,   dataatendo,  dataatendofin,  horaatendo,   horaatendofin, 
     obsatendo ,   codempcl ,  codfilialcl ,  codcli ,
     codempct,   codfilialct,   codcontr,  coditcontr,
      codempir ,    codfilialir ,     codrec ,  nparcitrec ,
    codempch ,    codfilialch,    codchamado, 
     obsinterno ,    concluichamado , codempea,    codfilialea,  codespec ,
     codempus integer,    codfilialus smallint,
    idusu varchar(128),    statusatendo char(2))*/
	
	public static enum PROC_IU {
		NONE, IU, CODEMP, CODFILIAL, CODATENDO, CODEMPTO, CODFILIALTO, CODTPATENDO, 
		CODEMPAE, CODFILIALAE, CODATEND, CODEMPSA, CODFILIALSA, CODSETOR, 
		DOCATENDO, DATAATENDO, DATAATENDOFIN, HORAATENDO, HORAATENDOFIN, 
		OBSATENDO, CODEMPCL, CODFILIALCL, CODCLI,
		CODEMPCT, CODFILIALCT, CODCONTR, CODITCONTR, 
		CODEMPIR, CODFILIALIR, CODREC, NPARCITREC, 
		CODEMPCH, CODFILIALCH,CODCHAMADO, 
		OBSINTERNO, CONCLUICHAMADO, CODEMPEA, CODFILIALEA, CODESPEC, 
		CODEMPUS, CODFILIALUS, IDUSU, STATUSATENDO, CODEMPTA, CODFILIALTA, CODTAREFA, CODEMPOC, CODFILIALOC, TIPOORC, CODORC, SITATENDO,
		CODEMPAG, CODFILIALAG, TIPOAGE, CODAGE, CODAGD
	}
	
	public static enum EColExped {
		SITREVEXPEDIMG, SITREVEXPED, DTEXPED, HORASEXPED, HINITURNO, HINIINTTURNO, HFIMINTTURNO, HFIMTURNO
	}
	
	public static enum EColAtend { CODATENDO, SITREVATENDOIMG, SITREVATENDO, DATAATENDO, HORAATENDO, 
		 HORAATENDOFIN, HORABATIDA, INIFINTURNO, INTERVATENDO, TOTALGERAL, TOTALMIN,
		 CODESPEC, DESCESPEC, CODMODEL, DESCMODEL, HORAINI, HORAFIN
	}

	public static enum INIFINTURNO {I, F}
	
	public static enum SITREV {PE, E1O, E1I, E2O, E2I };  
	
	public static enum PREFS { CODEMPMI, CODFILIALMI, CODMODELMI, DESCMODELMI, CODEMPME, CODFILIALME, CODMODELME, 
		DESCMODELME, TEMPOMAXINT, TOLREGPONTO, CODEMPIA, CODFILIALIA, CODESPECIA, DESCESPECIA, 
		CODEMPFI, CODFILIALFI, CODMODELFI, DESCMODELFI, CODEMPFJ, CODFILIALFJ, CODMODELFJ, DESCMODELFJ,
		CODEMPAP, CODFILIALAP, CODMODELAP, DESCMODELAP, BLOQATENDIMENTO, PERIODOBLOQ, CODMODELMC, CONTROLEACESATEND,AGENDAOBRIGORC};
	
	public static enum PARAM_PRIM_LANCA {NONE, CODEMP, CODFILIAL, DATAATENDO, CODEMPAE, CODFILIALAE, CODATEND, HORAATENDO, HORAATENDOFIN};
	
	private Integer codemp;
	
	private Integer codfilial;
	
	private Integer codatendo;
	
	private Integer codempto;
		
	private Integer codfilialto;

	private Integer codtpatendo ;
	
	private Integer codatend ;

	private Integer codempsa;
	
	private Integer codfilialsa;
	
	private Integer codsetat ;
	
	private String statusatendo;
	
	private String docatendo;
	
	private Date dataatendo;
	
	private Date dataatendofin;
	
	private String horaatendo;
	
	private String horaatendofin;
	
	private String obsatendo;
	
	private Integer codempcl;
	
	private Integer codfilialcl;
	
	private Integer codcli;
	
	private Integer codcontr;
	
	private Integer codempir;
	
	private Integer codfilialir;
	
	private Integer codrec;
	
	private Integer nparcitrec;
	
	private Integer codempct;
	
	private Integer codfilialct;
	
	private Integer coditcontr;
	
	private Integer codchamado;
	
	private String obsinterno;
	
	private Integer codempea;
	
	private Integer codfilialea;
	
	private Integer codespec;
	
	private Integer codempch;
	
	private Integer codfilialch;
	
	private Integer codempta;
	
	private Integer codfilialta;
	
	private Integer codtarefa;
	
	private String concluichamado;
	
	private Integer codempus;
	
	private Integer codfilialus;
	
	private String idusu;
	
	private Integer codempca;
	
	private Integer codfilialca;
	
	private Integer codclasatendo;
	
	private Integer codempcv;
	
	private Integer codfilialcv;
	
	private Integer codconv;	
	
	private Integer codempoc;
	
	private Integer codfilialoc;
	
	private String tipoorc;
	
	private Integer codorc;
	
	private String sitatendo;
	
	private Integer codempag;
	
	private Integer codfilialag;
	
	private String tipoage;
	
	private Integer codage;
	
	private Integer codagd;
	

	public Integer getCodemp() {
		
		return codemp;
	}

	
	public void setCodemp( Integer codemp ) {
	
		this.codemp = codemp;
	}

	
	public Integer getCodfilial() {
	
		return codfilial;
	}

	
	public void setCodfilial( Integer codfilial ) {
	
		this.codfilial = codfilial;
	}

	
	public Integer getCodatendo() {
	
		return codatendo;
	}

	
	public void setCodatendo( Integer codatendo ) {
	
		this.codatendo = codatendo;
	}
	
	public Integer getCodtpatendo() {
	
		return codtpatendo;
	}
	
	public void setCodtpatendo( Integer codtpatendo ) {
	
		this.codtpatendo = codtpatendo;
	}
	
	public Integer getCodatend() {
	
		return codatend;
	}
	
	public void setCodatend( Integer codatend ) {
	
		this.codatend = codatend;
	}
	
	public Integer getCodsetat() {
	
		return codsetat;
	}
	
	public void setCodsetat( Integer codsetat ) {
	
		this.codsetat = codsetat;
	}

	
	public String getStatusatendo() {
	
		return statusatendo;
	}

	
	public void setStatusatendo( String statusatendo ) {
	
		this.statusatendo = statusatendo;
	}

	
	public String getDocatendo() {
	
		return docatendo;
	}

	
	public void setDocatendo( String docatendo ) {
	
		this.docatendo = docatendo;
	}

	public String getObsatendo() {
	
		return obsatendo;
	}

	
	public void setObsatendo( String obsatendo ) {
	
		this.obsatendo = obsatendo;
	}

	
	
	public Integer getCodcli() {
	
		return codcli;
	}

	
	public void setCodcli( Integer codcli ) {
	
		this.codcli = codcli;
	}

	public Integer getCodcontr() {
	
		return codcontr;
	}

	
	
	public Integer getCodempcl() {
	
		return codempcl;
	}

	
	public void setCodempcl( Integer codempcl ) {
	
		this.codempcl = codempcl;
	}

	
	public Integer getCodfilialcl() {
	
		return codfilialcl;
	}

	
	public void setCodfilialcl( Integer codfilialcl ) {
	
		this.codfilialcl = codfilialcl;
	}

	public void setCodcontr( Integer codcontr ) {
	
		this.codcontr = codcontr;
	}

	
	public Integer getCoditcontr() {
	
		return coditcontr;
	}

	
	public void setCoditcontr( Integer coditcontr ) {
	
		this.coditcontr = coditcontr;
	}

	
	
	public Integer getCodrec() {
	
		return codrec;
	}

	
	public void setCodrec( Integer codrec ) {
	
		this.codrec = codrec;
	}

	
	public Integer getNparcitrec() {
	
		return nparcitrec;
	}

	
	public void setNparcitrec( Integer nparcitrec ) {
	
		this.nparcitrec = nparcitrec;
	}

	public Integer getCodchamado() {
	
		return codchamado;
	}

	
	public void setCodchamado( Integer codchamado ) {
	
		this.codchamado = codchamado;
	}

	
	public Integer getCodespec() {
	
		return codespec;
	}


	public Date getDataatendo() {
	
		return dataatendo;
	}

	
	public void setDataatendo( Date dataatendo ) {
	
		this.dataatendo = dataatendo;
	}

	
	public Date getDataatendofin() {
	
		return dataatendofin;
	}

	
	public void setDataatendofin( Date dataatendofin ) {
	
		this.dataatendofin = dataatendofin;
	}

	
	public String getHoraatendo() {
	
		return horaatendo;
	}

	
	public void setHoraatendo( String horaatendo ) {
	
		this.horaatendo = horaatendo;
	}

	
	public String getHoraatendofin() {
	
		return horaatendofin;
	}

	
	public void setHoraatendofin( String horaatendofin ) {
	
		this.horaatendofin = horaatendofin;
	}
	
	public String getObsinterno() {
		
		return obsinterno;
	}

	
	public void setObsinterno( String obsinterno ) {
	
		this.obsinterno = obsinterno;
	}

	
	public Integer getCodempch() {
	
		return codempch;
	}

	
	public void setCodempch( Integer codempch ) {
	
		this.codempch = codempch;
	}

	
	public Integer getCodfilialch() {
	
		return codfilialch;
	}

	
	public void setCodfilialch( Integer codfilialch ) {
	
		this.codfilialch = codfilialch;
	}

	
	public Integer getCodempae() {
	
		return codempea;
	}

	
	public void setCodempae( Integer codempae ) {
	
		this.codempea = codempae;
	}

	
	public Integer getCodfilialae() {
	
		return codfilialea;
	}

	
	public void setCodfilialae( Integer codfilialae ) {
	
		this.codfilialea = codfilialae;
	}

	
	public String getConcluichamado() {
	
		return concluichamado;
	}

	
	public void setConcluichamado( String concluichamado ) {
	
		this.concluichamado = concluichamado;
	}

	
	public Integer getCodempir() {
	
		return codempir;
	}

	
	public void setCodempir( Integer codempir ) {
	
		this.codempir = codempir;
	}

	
	public Integer getCodfilialir() {
	
		return codfilialir;
	}

	
	public void setCodfilialir( Integer codfilialir ) {
	
		this.codfilialir = codfilialir;
	}

	
	public Integer getCodempto() {
	
		return codempto;
	}

	
	public void setCodempto( Integer codempto ) {
	
		this.codempto = codempto;
	}

	
	public Integer getCodfilialto() {
	
		return codfilialto;
	}

	
	public void setCodfilialto( Integer codfilialto ) {
	
		this.codfilialto = codfilialto;
	}

	
	public Integer getCodempsa() {
	
		return codempsa;
	}

	
	public void setCodempsa( Integer codempsa ) {
	
		this.codempsa = codempsa;
	}

	
	public Integer getCodfilialsa() {
	
		return codfilialsa;
	}

	
	public void setCodfilialsa( Integer codfilialsa ) {
	
		this.codfilialsa = codfilialsa;
	}

	
	public Integer getCodempct() {
	
		return codempct;
	}

	
	public void setCodempct( Integer codempct ) {
	
		this.codempct = codempct;
	}

	
	public Integer getCodfilialct() {
	
		return codfilialct;
	}

	
	public void setCodfilialct( Integer codfilialct ) {
	
		this.codfilialct = codfilialct;
	}

	
	public Integer getCodempea() {
	
		return codempea;
	}

	
	public void setCodempea( Integer codempea ) {
	
		this.codempea = codempea;
	}

	
	public Integer getCodfilialea() {
	
		return codfilialea;
	}

	
	public void setCodfilialea( Integer codfilialea ) {
	
		this.codfilialea = codfilialea;
	}

	
	public Integer getCodempus() {
	
		return codempus;
	}

	
	public void setCodempus( Integer codempus ) {
	
		this.codempus = codempus;
	}

	
	public Integer getCodfilialus() {
	
		return codfilialus;
	}

	
	public void setCodfilialus( Integer codfilialus ) {
	
		this.codfilialus = codfilialus;
	}

	
	public String getIdusu() {
	
		return idusu;
	}

	
	public void setIdusu( String idusu ) {
	
		this.idusu = idusu;
	}

	
	public void setCodespec( Integer codespec ) {
	
		this.codespec = codespec;
	}

	
	public Integer getCodempca() {
	
		return codempca;
	}

	
	public void setCodempca( Integer codempca ) {
	
		this.codempca = codempca;
	}

	
	public Integer getCodfilialca() {
	
		return codfilialca;
	}

	
	public void setCodfilialca( Integer codfilialca ) {
	
		this.codfilialca = codfilialca;
	}

	
	public Integer getCodclasatendo() {
	
		return codclasatendo;
	}

	
	public void setCodclasatendo( Integer codclasatendo ) {
	
		this.codclasatendo = codclasatendo;
	}

	
	public Integer getCodempcv() {
	
		return codempcv;
	}

	
	public void setCodempcv( Integer codempcv ) {
	
		this.codempcv = codempcv;
	}

	
	public Integer getCodfilialcv() {
	
		return codfilialcv;
	}

	
	public void setCodfilialcv( Integer codfilialcv ) {
	
		this.codfilialcv = codfilialcv;
	}

	
	public Integer getCodconv() {
	
		return codconv;
	}

	
	public void setCodconv( Integer codconv ) {
	
		this.codconv = codconv;
	}


	
	public Integer getCodempta() {
	
		return codempta;
	}


	
	public void setCodempta( Integer codempta ) {
	
		this.codempta = codempta;
	}


	
	public Integer getCodfilialta() {
	
		return codfilialta;
	}


	
	public void setCodfilialta( Integer codfilailta ) {
	
		this.codfilialta = codfilailta;
	}


	
	public Integer getCodtarefa() {
	
		return codtarefa;
	}


	
	public void setCodtarefa( Integer codtarefa ) {
	
		this.codtarefa = codtarefa;
	}





	
	
	public Integer getCodempoc() {
	
		return codempoc;
	}


	
	public void setCodempoc( Integer codempoc ) {
	
		this.codempoc = codempoc;
	}


	
	public Integer getCodfilialoc() {
	
		return codfilialoc;
	}


	
	public void setCodfilialoc( Integer codfilialoc ) {
	
		this.codfilialoc = codfilialoc;
	}


	public String getTipoorc() {
	
		return tipoorc;
	}


	
	public void setTipoorc( String tipoorc ) {
	
		this.tipoorc = tipoorc;
	}


	
	public Integer getCodorc() {
	
		return codorc;
	}


	
	public void setCodorc( Integer codorc ) {
	
		this.codorc = codorc;
	}


	
	public String getSitatendo() {
	
		return sitatendo;
	}


	
	public void setSitatendo( String sitatendo ) {
	
		this.sitatendo = sitatendo;
	}


	
	public Integer getCodempag() {
	
		return codempag;
	}


	
	public void setCodempag( Integer codempag ) {
	
		this.codempag = codempag;
	}


	
	public Integer getCodfilialag() {
	
		return codfilialag;
	}


	
	public void setCodfilialag( Integer codfilialag ) {
	
		this.codfilialag = codfilialag;
	}


	
	public String getTipoage() {
	
		return tipoage;
	}


	
	public void setTipoage( String tipoage ) {
	
		this.tipoage = tipoage;
	}


	
	public Integer getCodage() {
	
		return codage;
	}


	
	public void setCodage( Integer codage ) {
	
		this.codage = codage;
	}


	
	public Integer getCodagd() {
	
		return codagd;
	}


	
	public void setCodagd( Integer codagd ) {
	
		this.codagd = codagd;
	}

}