/**
 * @version 08/07/2013 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.infra.beans <BR>
 *         Classe:
 * @(#)Usuario.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *     Objeto responsável por armazenar informações dos usuários
 * 
 */

package org.freedom.infra.beans;

import java.util.Date;

public class Usuario {
	private Integer codemp;
	private Integer codfilial;
	private String idusu;
	private String nomeusu;
	private Integer codempig;
	private Integer codfilialig;
	private String idgrpusu;
	private Integer codempcc;
	private Integer codfilialcc;
	private Integer anocc;
	private String codcc;
	private String pnomeusu;
	private String unomeusu;
	private String comentusu;
	private String baixocustousu;
	private Integer codempam;
	private Integer codfilialam;
	private Integer codalmox;
	private String abregavetausu;
	private String aprovcpsolicitacaousu;
	private String almoxarifeusu;
	private Integer codempae;
	private Integer codfilialae;
	private String tipoage;
	private Integer codage;
	private String aprovrmausu;
	private String comprasusu;
	private String altparcvenda;
	private String aprovreceita;
	private String ativcli;
	private String liberacredusu;
	private Integer coragenda;
	private Integer codempce;
	private Integer codfilialce;
	private Integer codconfemail;
	private String cancelaop;
	private String vendapatrimusu;
	private String rmaoutcc;
	private String ativousu;
	private String visualizalucr;
	private String liberacampopesagem;
	private String aprovordcp;
	private String acesopbtcadlote;
	private String acesopbtrma;
	private String acesopbtqualid;
	private String acesopbtdistr;
	private String acesopbtfase;
	private String acesopbtcanc;
	private String acesopbtsubprod;
	private String acesopbtremessa;
	private String acesopbtretorno;
	private String acesopveritens;
	private Date dtins;
	private String hins;
	private String idusuins;
	private Date dtalt;
	private String halt;
	private String idusualt;
	private String senha;
	
	public Integer getCodemp() {
		return codemp;
	}
	public Integer getCodfilial() {
		return codfilial;
	}
	public String getIdusu() {
		if (idusu==null) {
			idusu = "";
		}
		return idusu;
	}
	public String getNomeusu() {
		return nomeusu;
	}
	public Integer getCodempig() {
		return codempig;
	}
	public Integer getCodfilialig() {
		return codfilialig;
	}
	public String getIdgrpusu() {
		return idgrpusu;
	}
	public Integer getCodempcc() {
		return codempcc;
	}
	public Integer getCodfilialcc() {
		return codfilialcc;
	}
	public String getCodcc() {
		return codcc;
	}
	public void setCodcc(String codcc) {
		this.codcc = codcc;
	}
	public Integer getAnocc() {
		return anocc;
	}
	public String getPnomeusu() {
		return pnomeusu;
	}
	public String getUnomeusu() {
		return unomeusu;
	}
	public String getComentusu() {
		return comentusu;
	}
	public String getBaixocustousu() {
		return baixocustousu;
	}
	public Integer getCodempam() {
		return codempam;
	}
	public Integer getCodfilialam() {
		return codfilialam;
	}
	public Integer getCodalmox() {
		return codalmox;
	}
	public String getAbregavetausu() {
		return abregavetausu;
	}
	public String getAprovcpsolicitacaousu() {
		return aprovcpsolicitacaousu;
	}
	public String getAlmoxarifusu() {
		return almoxarifeusu;
	}
	public Integer getCodempae() {
		return codempae;
	}
	public Integer getCodfilialae() {
		return codfilialae;
	}
	public String getTipoage() {
		return tipoage;
	}
	public Integer getCodage() {
		return codage;
	}
	public String getAprovrmausu() {
		return aprovrmausu;
	}
	public String getComprausu() {
		return comprasusu;
	}
	public String getAltparcvenda() {
		return altparcvenda;
	}
	public String getAprovreceita() {
		return aprovreceita;
	}
	public String getAtivcli() {
		return ativcli;
	}
	public String getLiberacredusu() {
		return liberacredusu;
	}
	public Integer getCoragenda() {
		return coragenda;
	}
	public Integer getCodempce() {
		return codempce;
	}
	public Integer getCodfilialce() {
		return codfilialce;
	}
	public Integer getCodconfemail() {
		return codconfemail;
	}
	public String getCancelaop() {
		return cancelaop;
	}
	public String getVendapatrimusu() {
		return vendapatrimusu;
	}
	public String getRmaoutcc() {
		return rmaoutcc;
	}
	public String getAtivousu() {
		return ativousu;
	}
	public String getVisualizalucr() {
		return visualizalucr;
	}
	public String getLiberacampopesagem() {
		return liberacampopesagem;
	}
	public String getAprovordcp() {
		return aprovordcp;
	}
	public String getAcesopbtcadlote() {
		return acesopbtcadlote;
	}
	public String getAcesopbtrma() {
		return acesopbtrma;
	}
	public String getAcesopbtqualid() {
		return acesopbtqualid;
	}
	public String getAcesopbtdistr() {
		return acesopbtdistr;
	}
	public String getAcesopbtfase() {
		return acesopbtfase;
	}
	public String getAcesopbtcanc() {
		return acesopbtcanc;
	}
	public String getAcesopbtsubprod() {
		return acesopbtsubprod;
	}
	public String getAcesopbtremessa() {
		return acesopbtremessa;
	}
	public String getAcesopbtretorno() {
		return acesopbtretorno;
	}
	public String getAcesopveritens() {
		return acesopveritens;
	}
	public Date getDtins() {
		return dtins;
	}
	public String getHins() {
		return hins;
	}
	public String getIdusuins() {
		return idusuins;
	}
	public Date getDtalt() {
		return dtalt;
	}
	public String getHalt() {
		return halt;
	}
	public String getIdusualt() {
		return idusualt;
	}
	public void setCodemp(Integer codemp) {
		this.codemp = codemp;
	}
	public void setCodfilial(Integer codfilial) {
		this.codfilial = codfilial;
	}
	public void setIdusu(String idusu) {
		this.idusu = idusu;
	}
	public void setNomeusu(String nomeusu) {
		this.nomeusu = nomeusu;
	}
	public void setCodempig(Integer codempig) {
		this.codempig = codempig;
	}
	public void setCodfilialig(Integer codfilialig) {
		this.codfilialig = codfilialig;
	}
	public void setIdgrpusu(String idgrpusu) {
		this.idgrpusu = idgrpusu;
	}
	public void setCodempcc(Integer codempcc) {
		this.codempcc = codempcc;
	}
	public void setCodfilialcc(Integer codfilialcc) {
		this.codfilialcc = codfilialcc;
	}
	public void setAnocc(Integer anocc) {
		this.anocc = anocc;
	}
	public void setPnomeusu(String pnomeusu) {
		this.pnomeusu = pnomeusu;
	}
	public void setUnomeusu(String unomeusu) {
		this.unomeusu = unomeusu;
	}
	public void setComentusu(String comentusu) {
		this.comentusu = comentusu;
	}
	public void setBaixocustousu(String baixocustousu) {
		this.baixocustousu = baixocustousu;
	}
	public void setCodempam(Integer codempam) {
		this.codempam = codempam;
	}
	public void setCodfilialam(Integer codfilialam) {
		this.codfilialam = codfilialam;
	}
	public void setCodalmox(Integer codalmox) {
		this.codalmox = codalmox;
	}
	public void setAbregavetausu(String abregavetausu) {
		this.abregavetausu = abregavetausu;
	}
	public void setAprovcpsolicitacaousu(String aprovcpsolicitacaousu) {
		this.aprovcpsolicitacaousu = aprovcpsolicitacaousu;
	}
	public void setAlmoxarifeusu(String almoxarifeusu) {
		this.almoxarifeusu = almoxarifeusu;
	}
	public void setCodempae(Integer codempae) {
		this.codempae = codempae;
	}
	public void setCodfilialae(Integer codfilialae) {
		this.codfilialae = codfilialae;
	}
	public void setTipoage(String tipoage) {
		this.tipoage = tipoage;
	}
	public void setCodage(Integer codage) {
		this.codage = codage;
	}
	public void setAprovrmausu(String aprovrmausu) {
		this.aprovrmausu = aprovrmausu;
	}
	public void setComprasusu(String comprasusu) {
		this.comprasusu = comprasusu;
	}
	public void setAltparcvenda(String altparcvenda) {
		this.altparcvenda = altparcvenda;
	}
	public void setAprovreceita(String aprovreceita) {
		this.aprovreceita = aprovreceita;
	}
	public void setAtivcli(String ativcli) {
		this.ativcli = ativcli;
	}
	public void setLiberacredusu(String liberacredusu) {
		this.liberacredusu = liberacredusu;
	}
	public void setCoragenda(Integer coragenda) {
		this.coragenda = coragenda;
	}
	public void setCodempce(Integer codempce) {
		this.codempce = codempce;
	}
	public void setCodfilialce(Integer codfilialce) {
		this.codfilialce = codfilialce;
	}
	public void setCodconfemail(Integer codconfemail) {
		this.codconfemail = codconfemail;
	}
	public void setCancelaop(String cancelaop) {
		this.cancelaop = cancelaop;
	}
	public void setVendapatrimusu(String vendapatrimusu) {
		this.vendapatrimusu = vendapatrimusu;
	}
	public void setRmaoutcc(String rmaoutcc) {
		this.rmaoutcc = rmaoutcc;
	}
	public void setAtivousu(String ativousu) {
		this.ativousu = ativousu;
	}
	public void setVisualizalucr(String visualizalucr) {
		this.visualizalucr = visualizalucr;
	}
	public void setLiberacampopesagem(String liberacampopesagem) {
		this.liberacampopesagem = liberacampopesagem;
	}
	public void setAprovordcp(String aprovordcp) {
		this.aprovordcp = aprovordcp;
	}
	public void setAcesopbtcadlote(String acesopbtcadlote) {
		this.acesopbtcadlote = acesopbtcadlote;
	}
	public void setAcesopbtrma(String acesopbtrma) {
		this.acesopbtrma = acesopbtrma;
	}
	public void setAcesopbtqualid(String acesopbtqualid) {
		this.acesopbtqualid = acesopbtqualid;
	}
	public void setAcesopbtdistr(String acesopbtdistr) {
		this.acesopbtdistr = acesopbtdistr;
	}
	public void setAcesopbtfase(String acesopbtfase) {
		this.acesopbtfase = acesopbtfase;
	}
	public void setAcesopbtcanc(String acesopbtcanc) {
		this.acesopbtcanc = acesopbtcanc;
	}
	public void setAcesopbtsubprod(String acesopbtsubprod) {
		this.acesopbtsubprod = acesopbtsubprod;
	}
	public void setAcesopbtremessa(String acesopbtremessa) {
		this.acesopbtremessa = acesopbtremessa;
	}
	public void setAcesopbtretorno(String acesopbtretorno) {
		this.acesopbtretorno = acesopbtretorno;
	}
	public void setAcesopveritens(String acesopveritens) {
		this.acesopveritens = acesopveritens;
	}
	public void setDtins(Date dtins) {
		this.dtins = dtins;
	}
	public void setHins(String hins) {
		this.hins = hins;
	}
	public void setIdusuins(String idusuins) {
		this.idusuins = idusuins;
	}
	public void setDtalt(Date dtalt) {
		this.dtalt = dtalt;
	}
	public void setHalt(String halt) {
		this.halt = halt;
	}
	public void setIdusualt(String idusualt) {
		this.idusualt = idusualt;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
