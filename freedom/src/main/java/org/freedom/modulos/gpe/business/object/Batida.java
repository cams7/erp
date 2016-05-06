/**
 * @version 17/09/2011 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gpe.object <BR>
 *         Classe:
 * @(#)Batida.java <BR>
 * 
 *                Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *              Classe de constantes e bean de transporte, utilizada em tela FBatida e na classe DAOBatida.
 * 
 */
package org.freedom.modulos.gpe.business.object;

import java.util.Date;


public class Batida {
	public static enum PREFS {LANCAPONTOAF, TOLREGPONTO};
	public static enum PARAM_PROC_CARREGA {NONE, CODEMP, CODFILIAL, IDUSU, AFTELA, TOLREGPONTO };
	public static enum PARAM_PROC_INSERE {NONE, CODEMP, CODFILIAL, DTBAT, HBAT, CODEMPEP, CODFILIALEP, MATEMPR }
	public static enum COL_PROC {CARREGAPONTO, DATAPONTO, HORAPONTO, CODEMPAE, 
		CODFILIALAE, CODATEND, CODEMPEP, CODFILIALEP, MATEMPR };
	public static enum EColPonto { SEL, DTBAT, HBAT, TIPOBAT, MATEMPR, IDUSUINS };

		
	private Integer codemp;
	
	private Integer codfilial;
	
	private Integer codempus;
	
	private Integer codfilialus;
	
	private String idusu;
	
	private String aftela;
	
	private String carregaponto;
	
	private Date dataponto;
	
	private String horaponto;
	
	private Integer codempae;
	
	private Integer codfilialae;
	
	private Integer codatend;
	
	private Integer codempep;
	
	private Integer codfilialep;
	
	private Integer matempr;

	private Integer tolregponto;
	
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


	public Integer getCodempus() {
	
		return codempus;
	}

	
	public void setCodempus( Integer codempus ) {
	
		this.codempus = codempus;
	}

	
	public Integer getCodfilialus() {
	
		return codfilialus;
	}

	
	public void setCodfiliaus( Integer codfilialus ) {
	
		this.codfilialus = codfilialus;
	}

	
	public String getIdusu() {
	
		return idusu;
	}

	
	public void setIdusu( String idusu ) {
	
		this.idusu = idusu;
	}

	
	public String getAftela() {
	
		return aftela;
	}

	
	public void setAftela( String aftela ) {
	
		this.aftela = aftela;
	}

	
	public String getCarregaponto() {
	
		return carregaponto;
	}

	
	public void setCarregaponto( String carregaponto ) {
	
		this.carregaponto = carregaponto;
	}

	
	public Date getDataponto() {
	
		return dataponto;
	}

	
	public void setDataponto( Date dataponto ) {
	
		this.dataponto = dataponto;
	}

	
	public String getHoraponto() {
	
		return horaponto;
	}

	
	public void setHoraponto( String horaponto ) {
	
		this.horaponto = horaponto;
	}

	
	public Integer getCodempae() {
	
		return codempae;
	}

	
	public void setCodempae( Integer codempae ) {
	
		this.codempae = codempae;
	}

	
	public Integer getCodfilialae() {
	
		return codfilialae;
	}

	
	public void setCodfilialae( Integer codfilialae ) {
	
		this.codfilialae = codfilialae;
	}

	
	public Integer getCodatend() {
	
		return codatend;
	}

	
	public void setCodatend( Integer codatend ) {
	
		this.codatend = codatend;
	}

	
	public Integer getCodempep() {
	
		return codempep;
	}

	
	public void setCodempep( Integer codempep ) {
	
		this.codempep = codempep;
	}

	
	public Integer getCodfilialep() {
	
		return codfilialep;
	}

	
	public void setCodfilialep( Integer codfilialep ) {
	
		this.codfilialep = codfilialep;
	}

	
	public Integer getMatempr() {
	
		return matempr;
	}

	
	public void setMatempr( Integer matempr ) {
	
		this.matempr = matempr;
	}



	
	public Integer getTolregponto() {
	
		return tolregponto;
	}



	
	public void setTolregponto( Integer tolregponto ) {
	
		this.tolregponto = tolregponto;
	}
	
}
