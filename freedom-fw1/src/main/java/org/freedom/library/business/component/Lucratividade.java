package org.freedom.library.business.component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

public class Lucratividade {

	private BigDecimal totfat = null;
	private BigDecimal totcusto = null;
	private BigDecimal totlucro = null;
	private BigDecimal itemfat = null;
	private BigDecimal itemcusto = null;
	private BigDecimal itemlucro = null;
	private BigDecimal vlrcustopeps = null;
	private BigDecimal vlrcustouc = null;
	private BigDecimal vlrcustoinfo = null;
	private BigDecimal vlrlucro = null;
	private BigDecimal vlrprod = null;
	private BigDecimal vlrdesc = null;
	private BigDecimal vlricms = null;
	private BigDecimal vlrcomis = null;
	private BigDecimal vlrfrete = null;
	private BigDecimal vlradic = null;
	private BigDecimal vlripi = null;
	private BigDecimal vlrpis = null;
	private BigDecimal vlrcofins = null;
	private BigDecimal vlrir = null;
	private BigDecimal vlrcsocial = null;
	private BigDecimal vlrcustompmit = null;
	private BigDecimal vlrcustopepsit = null;
	private BigDecimal vlrcustoucit = null;
	private BigDecimal vlrcustoinfoit = null;
	private BigDecimal vlrlucroit = null;
	private BigDecimal vlrprodit = null;
	private BigDecimal vlrdescit = null;
	private BigDecimal vlricmsit = null;
	private BigDecimal vlrpisit = null;
	private BigDecimal vlrcofinsit = null;
	private BigDecimal vlrcsocialit = null;
	private BigDecimal vlrcomisit = null;
	private BigDecimal vlrfreteit = null;
	private BigDecimal vlradicit = null;
	private BigDecimal vlripiit = null;
	private BigDecimal vlririt = null;
	private BigDecimal perclucr = null;
	private BigDecimal perclucrit = null;
	private String tipofrete = null;
	private String adicfrete = null;
	private BigDecimal fatLucro = new BigDecimal(1);
	private BigDecimal vlrcustompm = null;
	private DbConnection con = null;

	public Lucratividade(Integer codcab, String tipo, Integer item, BigDecimal fatLucro, String tipocusto, DbConnection con, boolean descipi) {

		this.con = con;

		if (fatLucro != null) {
			this.fatLucro = fatLucro;
		}

		// Se for a lucratividade de uma venda

		if (codcab != null && "V".equals(tipo)) {
			carregaVenda(codcab, tipo);
			carregaItemVenda(codcab, tipo, item);
		}
		// Se for a lucratividade de um orçamento
		else {
			carregaOrcamento(codcab, tipo, descipi);
			carregaItemOrcamento(codcab, tipo, item, descipi);

		}

		calcTotFat(descipi);
		calcTotCusto(tipocusto, descipi);
		calcTotLucro();

		calcItemFat(descipi);
		calcItemCusto(tipocusto);
		calcItemLucro();

	}

	public BigDecimal getTotfat() {

		return totfat;
	}

	public void setTotfat(BigDecimal totfat) {

		this.totfat = totfat;
	}

	public BigDecimal getVlrprod() {
		if (vlrprod == null) {
			return new BigDecimal(0);
		}
		return vlrprod;
	}

	public void setVlrprod(BigDecimal vlrprodvenda) {

		this.vlrprod = vlrprodvenda;
	}

	public BigDecimal getVlrdesc() {
		if (vlrdesc == null) {
			return new BigDecimal(0);
		}

		return vlrdesc;
	}

	public void setVlrdesc(BigDecimal vlrdescvenda) {

		this.vlrdesc = vlrdescvenda;
	}

	public BigDecimal getVlricms() {
		if (vlricms == null) {
			return new BigDecimal(0);
		}

		return vlricms;
	}

	public void setVlricms(BigDecimal vlricmsvenda) {

		this.vlricms = vlricmsvenda;
	}

	public BigDecimal getVlrcomis() {
		if (vlrcomis == null) {
			return new BigDecimal(0);
		}
		if (fatLucro.floatValue() > 1) {
			return vlrcomis.multiply(fatLucro);
		}

		return vlrcomis;
	}

	public void setVlrcomis(BigDecimal vlrcomis) {

		this.vlrcomis = vlrcomis;
	}

	public BigDecimal getVlrfrete() {
		if (vlrfrete == null) {
			return new BigDecimal(0);
		}

		return vlrfrete;
	}

	public void setVlrfrete(BigDecimal vlrfrete) {

		this.vlrfrete = vlrfrete;
	}

	public BigDecimal getVlradic() {
		if (vlradic == null) {
			return new BigDecimal(0);
		}

		return vlradic;
	}

	public void setVlradic(BigDecimal vlradic) {

		this.vlradic = vlradic;
	}

	public BigDecimal getVlripi() {
		if (vlripi == null) {
			return new BigDecimal(0);
		}

		return vlripi;
	}

	public void setVlripi(BigDecimal vlripi) {

		this.vlripi = vlripi;
	}

	public BigDecimal getVlrcofins() {
		if (vlrcofins == null) {
			return new BigDecimal(0);
		}

		return vlrcofins;
	}

	public void setVlrcofins(BigDecimal vlrcofins) {

		this.vlrcofins = vlrcofins;
	}

	public BigDecimal getVlrpis() {
		if (vlrpis == null) {
			return new BigDecimal(0);
		}

		return vlrpis;
	}

	public void setVlrpis(BigDecimal vlrpis) {
		this.vlrpis = vlrpis;
	}

	public BigDecimal getVlrir() {
		if (vlrir == null) {
			return new BigDecimal(0);
		}

		return vlrir;
	}

	public void setVlrir(BigDecimal vlrir) {

		this.vlrir = vlrir;
	}

	public BigDecimal getVlrcsocial() {
		if (vlrcsocial == null) {
			return new BigDecimal(0);
		}

		return vlrcsocial;
	}

	public void setVlrcsocial(BigDecimal vlrcsocial) {

		this.vlrcsocial = vlrcsocial;
	}

	public BigDecimal getVlrpisit() {
		if (vlrpisit == null) {
			return new BigDecimal(0);
		}

		return vlrpisit;
	}

	public void setVlrpisit(BigDecimal vlrpisit) {
		this.vlrpisit = vlrpisit;
	}

	public BigDecimal getVlrcofinsit() {
		if (vlrcofinsit == null) {
			return new BigDecimal(0);
		}

		return vlrcofinsit;
	}

	public void setVlrcofinsit(BigDecimal vlrcofinsit) {
		this.vlrcofinsit = vlrcofinsit;
	}

	public BigDecimal getVlrcsocialit() {
		if (vlrcsocialit == null) {
			return new BigDecimal(0);
		}

		return vlrcsocialit;
	}

	public void setVlrcsocialit(BigDecimal vlrcsocialit) {
		this.vlrcsocialit = vlrcsocialit;
	}

	public BigDecimal getVlririt() {
		if (vlririt == null) {
			return new BigDecimal(0);
		}

		return vlririt;
	}

	public void setVlririt(BigDecimal vlririt) {
		this.vlririt = vlririt;
	}

	private void calcTotCusto(String tipocusto, boolean descipi) {
		BigDecimal calc = null;

		try {
			if (tipocusto == null || "M".equals(tipocusto)) {
				calc = getVlrcustompmvenda();
			}
			else if ("P".equals(tipocusto)) {
				calc = getVlrcustopeps();
			}
			else if ("U".equals(tipocusto)) {
				calc = getVlrcustouc();
			}
			else if ("I".equals(tipocusto)) {
				calc = getVlrcustoinfo();
			}

			System.out.println("VALOR TOT. CUSTO COMPRA: (" + tipocusto + "):" + calc);

			// Se frete for FOB e não destacado na nota é custo.
			if ("F".equals(tipofrete) && "N".equals(adicfrete)) {
				calc = calc.add(getVlrfrete());
				System.out.println("VALOR TOT. FRETE: " + getVlrfrete());
			}
			// Se frete for CIF e destacado na nota é custo, pois é faturado.
			else if ("C".equals(tipofrete) && "S".equals(adicfrete)) {
				calc = calc.add(getVlrfrete());
				System.out.println("VALOR TOT. FRETE: " + getVlrfrete());
			}

			if (calc != null) {
				calc = calc.add(getVlrcomis());
				System.out.println("VALOR TOT. COMISSÃO: " + getVlrcomis());

				calc = calc.add(getVlrpis());
				System.out.println("VALOR TOT. PIS: " + getVlrpis());

				calc = calc.add(getVlrcofins());
				System.out.println("VALOR TOT. COFINS: " + getVlrcofins());

				calc = calc.add(getVlricms());
				System.out.println("VALOR TOT. ICMS: " + getVlricms());

				calc = calc.add(getVlrcsocial());
				System.out.println("VALOR TOT. CSOCIAL: " + getVlrcsocial());

				calc = calc.add(getVlrir());
				System.out.println("VALOR TOT. IR: " + getVlrir());
				
				calc = calc.add(getVlripi());
				
				System.out.println("VALOR TOT. IPI: " + getVlripi());

				setTotcusto(calc);
				setTotlucro(getTotfat().subtract(calc));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void calcItemCusto(String tipocusto) {
		BigDecimal calc = null;

		try {
			if ("M".equals(tipocusto)) { // Custo médio
				calc = getVlrcustompmit();
			}
			else if ("P".equals(tipocusto)) { // Custo Peps
				calc = getVlrcustopepsit();
			}
			else if ("U".equals(tipocusto)) { // Custo da ultima compra
				calc = getVlrcustoucit();
			}
			else if ("I".equals(tipocusto)) { // Custo informado
				calc = getVlrcustoinfoit();
			}

			System.out.println("VALOR ITEM CUSTO COMPRA: (" + tipocusto + "):" + calc);

			// Se frete for FOB e não destacado na nota é custo.
			if ("F".equals(tipofrete) && "N".equals(adicfrete)) {
				calc = calc.add(getVlrfreteit());
				System.out.println("VALOR ITEM FRETE: " + getVlrfreteit());
			}
			// Se frete for CIF e destacado na nota é custo, pois é faturado.
			else if ("C".equals(tipofrete) && "S".equals(adicfrete)) {
				calc = calc.add(getVlrfrete());
				System.out.println("VALOR ITEM FRETE: " + getVlrfrete());
			}

			if (calc != null) {
				calc = calc.add(getVlrcomisit());
				System.out.println("VALOR ITEM COMISSÃO: " + getVlrcomisit());

				calc = calc.add(getVlrpisit());
				System.out.println("VALOR ITEM PIS: " + getVlrpisit());

				calc = calc.add(getVlrcofinsit());
				System.out.println("VALOR ITEM COFINS: " + getVlrcofinsit());

				calc = calc.add(getVlricmsit());
				System.out.println("VALOR ITEM ICMS: " + getVlricmsit());

				calc = calc.add(getVlrcsocialit());
				System.out.println("VALOR ITEM CSOCIAL: " + getVlrcsocialit());

				calc = calc.add(getVlririt());
				System.out.println("VALOR ITEM IR: " + getVlririt());

				calc = calc.add(getVlripiit());
				System.out.println("VALOR ITEM IPI: " + getVlripiit());

				setItemcusto(calc);
				setItemlucro(getItemfat().subtract(calc));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public BigDecimal getVlrcustompmvenda() {

		return vlrcustompm;
	}

	public void setVlrcustompm(BigDecimal vlrcustompm) {

		this.vlrcustompm = vlrcustompm;
	}

	public BigDecimal getVlrcustopeps() {

		return vlrcustopeps;
	}

	public void setVlrcustopeps(BigDecimal vlrcustopeps) {

		this.vlrcustopeps = vlrcustopeps;
	}

	public BigDecimal getVlrcustouc() {

		return vlrcustouc;
	}

	public void setVlrcustouc(BigDecimal vlrcustouc) {

		this.vlrcustouc = vlrcustouc;
	}

	public BigDecimal getVlrcustoinfo() {

		return vlrcustoinfo;
	}

	public void setVlrcustoinfo(BigDecimal vlrcustoinfo) {

		this.vlrcustoinfo = vlrcustoinfo;
	}

	public BigDecimal getVlrlucro() {

		return vlrlucro;
	}

	public void setVlrlucro(BigDecimal vlrlucro) {

		this.vlrlucro = vlrlucro;
	}

	public BigDecimal getTotcusto() {
		if (totcusto != null)
			return totcusto;
		else
			return new BigDecimal(0);
	}

	public void setTotcusto(BigDecimal totcusto) {

		this.totcusto = totcusto;
	}

	private void calcTotFat( boolean descipi ) {
		BigDecimal calc = null;
		try {

			// calc = vlrprod.multiply(fatLucro) ;

			if (vlrprod != null) {
				calc = vlrprod.add(vlradic);
				calc = calc.subtract(vlrdesc);

				calc = calc.multiply(fatLucro);

				// Se frete for destacado na nota, entra como valor faturado
				if ("S".equals(adicfrete)) {
					calc = calc.add(vlrfrete);
				}

				if(!descipi &&  !(fatLucro.compareTo(new BigDecimal(1)) > 0) ) {
					calc = calc.add(vlripi);
				}

				setTotfat(calc);
				
				System.out.println("VALOR FATURADO:" + calc.toString());
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void calcItemFat(boolean descipi) {
		BigDecimal calc = null;
		try {

			// calc = vlrprodit.multiply(fatLucro) ;
			if (vlrprodit != null) {
				calc = vlrprodit.add(vlradicit);
				calc = calc.subtract(vlrdescit);

				calc = calc.multiply(fatLucro); // Desconto multiplicado pelo
				// fator também

				// Se frete for destacado na nota, entra como valor faturado
				if ("S".equals(adicfrete)) {
					calc = calc.add(vlrfreteit);
				}

				if(!descipi &&  !(fatLucro.compareTo(new BigDecimal(1)) > 0) ) {
					calc = calc.add(vlripiit);
				}
				
				setItemfat(calc);

				System.out.println("VALOR FATURADO ITEM:" + calc.toString());
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void calcTotLucro() {
		BigDecimal perclucro = null;
		BigDecimal vlrlucro = null;

		try {

			if (getTotfat() != null) { 
				
				if(getTotfat().compareTo(new BigDecimal(0))>0) {
				
					vlrlucro = getTotfat().subtract(getTotcusto());
	
					
					perclucro = ( vlrlucro.multiply(new BigDecimal(100)) ).divide(getTotfat(), 0, BigDecimal.ROUND_DOWN);
	
					setVlrlucro(vlrlucro);
					
				}
								
				setPerclucrvenda(perclucro);
				
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calcItemLucro() {
		BigDecimal percitemlucro = null;
		BigDecimal vlritemlucro = null;

		try {

			if (getItemfat() != null && getItemfat().floatValue() > 0) {
				vlritemlucro = getItemfat().subtract(getItemcusto());

				percitemlucro = ( vlritemlucro.multiply(new BigDecimal(100)) ).divide(getItemfat(), 0, BigDecimal.ROUND_DOWN);

				setVlrlucroitvenda(vlritemlucro);

				setPerclucrit(percitemlucro);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTipofrete() {

		return tipofrete;
	}

	public void setTipofrete(String tipofrete) {

		this.tipofrete = tipofrete;
	}

	public BigDecimal getPerclucrvenda() {
		if (perclucr == null) {
			perclucr = new BigDecimal(0);
		}
		return perclucr;
	}

	public void setPerclucrvenda(BigDecimal perclucrvenda) {

		this.perclucr = perclucrvenda;
	}

	public String getAdicfretevd() {

		return adicfrete;
	}

	public void setAdicfrete(String adicfrete) {

		this.adicfrete = adicfrete;
	}

	public BigDecimal getTotlucro() {
		if (totlucro == null) {
			return new BigDecimal(0);
		}
		return totlucro;
	}

	public void setTotlucro(BigDecimal totlucro) {

		this.totlucro = totlucro;
	}

	public BigDecimal getVlrcustompmit() {

		if (vlrcustompmit == null) {
			return new BigDecimal(0);
		}

		return vlrcustompmit;
	}

	public void setVlrcustompmit(BigDecimal vlrcustompmit) {

		this.vlrcustompmit = vlrcustompmit;
	}

	public BigDecimal getVlrcustopepsit() {

		if (vlrcustopepsit == null) {
			return new BigDecimal(0);
		}

		return vlrcustopepsit;
	}

	public void setVlrcustopepsit(BigDecimal vlrcustopepsit) {

		this.vlrcustopepsit = vlrcustopepsit;
	}

	public BigDecimal getVlrcustoucit() {

		if (vlrcustoucit == null) {
			return new BigDecimal(0);
		}

		return vlrcustoucit;
	}

	public void setVlrcustoucit(BigDecimal vlrcustoucitvenda) {

		this.vlrcustoucit = vlrcustoucitvenda;
	}

	public BigDecimal getVlrcustoinfoit() {

		if (vlrcustoinfoit == null) {
			return new BigDecimal(0);
		}

		return vlrcustoinfoit;
	}

	public void setVlrcustoinfoit(BigDecimal vlrcustoinfoitvenda) {

		this.vlrcustoinfoit = vlrcustoinfoitvenda;
	}

	public BigDecimal getVlrlucroitvenda() {

		if (vlrlucroit == null) {
			return new BigDecimal(0);
		}

		return vlrlucroit;
	}

	public void setVlrlucroitvenda(BigDecimal vlrlucroitvenda) {

		this.vlrlucroit = vlrlucroitvenda;
	}

	public BigDecimal getVlrproditvenda() {

		if (vlrprodit == null) {
			return new BigDecimal(0);
		}

		return vlrprodit;
	}

	public void setVlrprodit(BigDecimal vlrprodit) {

		this.vlrprodit = vlrprodit;
	}

	public BigDecimal getVlrdescit() {

		if (vlrdescit == null) {
			return new BigDecimal(0);
		}

		return vlrdescit;
	}

	public void setVlrdescit(BigDecimal vlrdescit) {

		this.vlrdescit = vlrdescit;
	}

	public BigDecimal getVlricmsit() {

		if (vlricmsit == null) {
			return new BigDecimal(0);
		}

		return vlricmsit;
	}

	public void setVlricmsit(BigDecimal vlricmsit) {

		this.vlricmsit = vlricmsit;
	}

	public BigDecimal getVlrcomisit() {

		if (vlrcomisit == null) {
			return new BigDecimal(0);
		}
		if (fatLucro.floatValue() > 1) {
			return vlrcomisit.multiply(fatLucro);
		}

		return vlrcomisit;
	}

	public void setVlrcomisit(BigDecimal vlrcomisit) {

		this.vlrcomisit = vlrcomisit;
	}

	public BigDecimal getVlrfreteit() {

		if (vlrfreteit == null) {
			return new BigDecimal(0);
		}

		return vlrfreteit;
	}

	public void setVlrfreteit(BigDecimal vlrfreteit) {

		this.vlrfreteit = vlrfreteit;
	}

	public BigDecimal getVlradicit() {

		if (vlradicit == null) {
			return new BigDecimal(0);
		}

		return vlradicit;
	}

	public void setVlradicit(BigDecimal vlradicit) {

		this.vlradicit = vlradicit;
	}

	public BigDecimal getVlripiit() {

		if (vlripiit == null) {
			return new BigDecimal(0);
		}

		return vlripiit;
	}

	public void setVlripiit(BigDecimal vlripiit) {

		this.vlripiit = vlripiit;
	}

	public BigDecimal getItemfat() {
		if (itemfat == null) {
			return new BigDecimal(0);
		}
		return itemfat;
	}

	public void setItemfat(BigDecimal itemfat) {

		this.itemfat = itemfat;
	}

	public BigDecimal getItemcusto() {
		if (itemcusto == null) {
			return new BigDecimal(0);
		}

		return itemcusto;
	}

	public void setItemcusto(BigDecimal itemcusto) {

		this.itemcusto = itemcusto;
	}

	public BigDecimal getItemlucro() {
		if (itemlucro == null) {
			return new BigDecimal(0);
		}

		return itemlucro;
	}

	public void setItemlucro(BigDecimal itemlucro) {

		this.itemlucro = itemlucro;
	}

	public BigDecimal getPerclucrit() {
		if (perclucrit == null) {
			return new BigDecimal(0);
		}
		return perclucrit;
	}

	public void setPerclucrit(BigDecimal perclucrit) {

		this.perclucrit = perclucrit;
	}

	private void carregaVenda(Integer codvenda, String tipovenda) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		try {

			sql.append("select ");

			sql.append("coalesce(vd.vlrprodvenda,0) vlrprodvenda , coalesce(vd.vlrdescitvenda,0) vlrdescvenda, coalesce(vd.vlricmsvenda,0) vlricmsvenda, ");
			sql.append("coalesce(vd.vlrcomisvenda,0) vlrcomisvenda, coalesce(vd.vlradicvenda,0) vlradicvenda, ");
			sql.append("coalesce(vd.vlripivenda,0) vlripivenda, coalesce(vd.vlrpisvenda,0) vlrpisvenda, coalesce(vd.vlrcofinsvenda,0) vlrcofinsvenda, ");
			sql.append("coalesce(vd.vlrirvenda,0) vlrirvenda, coalesce(vd.vlrcsocialvenda,0) vlrcsocialvenda, ");
			sql.append("coalesce(fr.vlrfretevd,0) vlrfretevd, fr.tipofretevd, fr.adicfretevd, ");

			sql.append("sum(icv.vlrcustopeps * iv.qtditvenda) as vlrcustopeps, ");
			sql.append("sum(icv.vlrcustompm * iv.qtditvenda) as vlrcustompm, ");
			sql.append("sum(icv.vlrprecoultcp * iv.qtditvenda) as vlrcustouc, ");
			sql.append("sum(pd.custoinfoprod * iv.qtditvenda) as vlrcustoinfo ");

			sql.append("from ");
			sql.append("vdvenda vd left outer join vdfretevd fr on ");
			sql.append("fr.codemp=vd.codemp and fr.codfilial=vd.codfilial and fr.codvenda=vd.codvenda ");
			sql.append("and fr.tipovenda=vd.tipovenda, ");

			sql.append("vditvenda iv left outer join vditcustovenda icv on ");
			sql.append("icv.codemp=iv.codemp and icv.codfilial=iv.codfilial and icv.codvenda = iv.codvenda ");
			sql.append("and icv.tipovenda=iv.tipovenda and icv.coditvenda=iv.coditvenda ");

			sql.append("left outer join eqproduto pd on ");
			sql.append("pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd and pd.codprod=iv.codprod ");

			sql.append("where vd.codemp=? and vd.codfilial=? and vd.codvenda=? and vd.tipovenda=? ");
			sql.append("and iv.codemp=vd.codemp and iv.codfilial=vd.codfilial and iv.tipovenda=vd.tipovenda and ");
			sql.append("iv.codvenda=vd.codvenda ");

			sql.append("group by 1,2,3,4,5,6,7,8,9,10,11,12,13 ");

			System.out.println(sql.toString());

			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("VDVENDA"));
			ps.setInt(3, codvenda);
			ps.setString(4, tipovenda);

			rs = ps.executeQuery();

			if (rs.next()) {
				setVlrprod(rs.getBigDecimal("vlrprodvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrprodvenda"));
				setVlrdesc(rs.getBigDecimal("vlrdescvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrdescvenda"));
				setVlricms(rs.getBigDecimal("vlricmsvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlricmsvenda"));
				setVlrcomis(rs.getBigDecimal("vlrcomisvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcomisvenda"));
				setVlrfrete(rs.getBigDecimal("vlrfretevd") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrfretevd"));
				setVlradic(rs.getBigDecimal("vlradicvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlradicvenda"));
				setVlripi(rs.getBigDecimal("vlripivenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlripivenda"));
				setVlrpis(rs.getBigDecimal("vlrpisvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrpisvenda"));
				setVlrcofins(rs.getBigDecimal("vlrcofinsvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcofinsvenda"));
				setVlrir(rs.getBigDecimal("vlrirvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrirvenda"));
				setVlrcsocial(rs.getBigDecimal("vlrcsocialvenda") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcsocialvenda"));
				setVlrcustouc(rs.getBigDecimal("vlrcustouc") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcustouc"));
				setVlrcustompm(rs.getBigDecimal("vlrcustompm") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcustompm"));
				setVlrcustopeps(rs.getBigDecimal("vlrcustopeps") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcustopeps"));
				setVlrcustoinfo(rs.getBigDecimal("vlrcustoinfo") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcustoinfo"));
				setTipofrete(rs.getString("tipofretevd") == null ? "F" : rs.getString("tipofretevd"));
				setAdicfrete(rs.getString("adicfretevd") == null ? "N" : rs.getString("adicfretevd"));
			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void carregaItemVenda(Integer codvenda, String tipovenda, Integer coditvenda) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		try {

			sql.append("select ");
			sql.append("coalesce(iv.vlrproditvenda,0) vlrproditvenda, coalesce(iv.vlrdescitvenda,0) vlrdescitvenda, coalesce(iv.vlricmsitvenda,0) vlricmsitvenda, ");
			sql.append("coalesce(iv.vlrcomisitvenda,0) vlrcomisitvenda, coalesce(iv.vlradicitvenda,0) vlradicitvenda, ");
			sql.append("coalesce(iv.vlripiitvenda,0) vlripiitvenda, coalesce(iv.vlrfreteitvenda,0) vlrfreteitvenda, ");
			sql.append("coalesce(lfi.vlrir,0) vlrir, coalesce(lfi.vlrcsocial,0) vlrcsocial, coalesce(lfi.vlrpis,0) vlrpis, coalesce(lfi.vlrcofins,0) vlrcofins, ");

			sql.append("(icv.vlrcustopeps * iv.qtditvenda) as vlrcustoitpeps, ");
			sql.append("(icv.vlrcustompm * iv.qtditvenda) as vlrcustoitmpm, ");
			sql.append("(icv.vlrprecoultcp * iv.qtditvenda) as vlrcustoituc, ");
			sql.append("(pd.custoinfoprod * iv.qtditvenda) as vlrcustoitinfo ");

			sql.append("from ");

			sql.append("vditvenda iv left outer join vditcustovenda icv on ");
			sql.append("icv.codemp=iv.codemp and icv.codfilial=iv.codfilial and icv.codvenda = iv.codvenda ");
			sql.append("and icv.tipovenda=iv.tipovenda and icv.coditvenda=iv.coditvenda ");

			sql.append("left outer join lfitvenda lfi on ");
			sql.append("lfi.codemp=iv.codemp and lfi.codfilial=iv.codfilial and lfi.codvenda=iv.codvenda ");
			sql.append("and lfi.tipovenda=iv.tipovenda and lfi.coditvenda=iv.coditvenda  ");

			sql.append("left outer join eqproduto pd on ");
			sql.append("pd.codemp=iv.codemppd and pd.codfilial=iv.codfilialpd and pd.codprod=iv.codprod ");

			sql.append("where iv.codemp=? and iv.codfilial=? and iv.codvenda=? and iv.tipovenda=? and iv.coditvenda=? ");

			System.out.println(sql.toString());

			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("VDVENDA"));
			ps.setInt(3, codvenda);
			ps.setString(4, tipovenda);
			ps.setInt(5, coditvenda);

			rs = ps.executeQuery();

			if (rs.next()) {
				setVlrprodit(rs.getBigDecimal("vlrproditvenda"));
				setVlrdescit(rs.getBigDecimal("vlrdescitvenda"));
				setVlricmsit(rs.getBigDecimal("vlricmsitvenda"));
				setVlrcomisit(rs.getBigDecimal("vlrcomisitvenda"));
				setVlrfreteit(rs.getBigDecimal("vlrfreteitvenda"));
				setVlradicit(rs.getBigDecimal("vlradicitvenda"));
				setVlripiit(rs.getBigDecimal("vlripiitvenda"));
				setVlrcustoucit(rs.getBigDecimal("vlrcustoituc"));
				setVlrcustompmit(rs.getBigDecimal("vlrcustoitmpm"));
				setVlrcustopepsit(rs.getBigDecimal("vlrcustoitpeps"));
				setVlrcustoinfoit(rs.getBigDecimal("vlrcustoitinfo"));

				setVlrpisit(rs.getBigDecimal("vlrpis"));
				setVlrcofinsit(rs.getBigDecimal("vlrcofins"));
				setVlririt(rs.getBigDecimal("vlrir"));
				setVlrcsocialit(rs.getBigDecimal("vlrcsocial"));
			}

			rs.close();
			ps.close();
			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void carregaOrcamento(Integer codorc, String tipoorc, boolean descipi) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		try {

			sql.append("select ");
			sql.append("coalesce(oc.vlrprodorc,0) vlrprod , coalesce(oc.vlrliqorc,0) vlrliqorc, coalesce(oc.vlrdescorc,0) vlrdesc, ");
			sql.append("coalesce(oc.vlradicorc,0) vlradic, coalesce(oc.vlrfreteorc,0) vlrfrete, ");
			sql.append("oc.tipofrete, oc.adicfrete, ");
			sql.append("coalesce(sum(io.vlrcomisitorc),0) as vlrcomis, ");
			sql.append("coalesce(sum(ico.vlrcustopeps * io.qtditorc),0) as vlrcustopeps, ");
			sql.append("coalesce(sum(ico.vlrcustompm * io.qtditorc),0) as vlrcustompm, ");
			sql.append("coalesce(sum(ico.vlrprecoultcp * io.qtditorc),0) as vlrcustouc, ");
			sql.append("coalesce(sum(pd.custoinfoprod * io.qtditorc),0) as vlrcustoinfo, ");
			sql.append("coalesce(sum(pt.vlricmsitorc),0) as vlricms, ");
			sql.append("coalesce(sum(pt.vlripiitorc),0) as vlripi, ");
			sql.append("coalesce(sum(pt.vlrpisitorc),0) as vlrpis, ");
			sql.append("coalesce(sum(pt.vlrcofinsitorc),0) as vlrcofins, ");
			sql.append("coalesce(sum(pt.vlriritorc),0) as vlrir, ");
			sql.append("coalesce(sum(pt.vlrcsocialitorc),0) as vlrcsocial, ");
			sql.append("coalesce(sum(pt.vlrissitorc),0) as vlriss ");

			sql.append("from vdorcamento oc, vditorcamento io ");
			sql.append("left outer join vditcustoorc ico on ");
			sql.append("ico.codemp=io.codemp and ico.codfilial=io.codfilial and ico.codorc=io.codorc ");
			sql.append("and ico.tipoorc=io.tipoorc and ico.coditorc=io.coditorc ");
			sql.append("left outer join vdprevtribitorc pt on ");
			sql.append("pt.codemp=io.codemp and pt.codfilial=io.codfilial and pt.codorc=io.codorc ");
			sql.append("and pt.tipoorc=io.tipoorc and pt.coditorc=io.coditorc ");
			sql.append("left outer join eqproduto pd on ");
			sql.append("pd.codemp=io.codemppd and pd.codfilial=io.codfilialpd and pd.codprod=io.codprod ");

			sql.append("where oc.codemp=? and oc.codfilial=? and oc.codorc=? and oc.tipoorc=? ");
			sql.append("and io.codemp=oc.codemp and io.codfilial=oc.codfilial and io.tipoorc=oc.tipoorc and io.codorc=oc.codorc ");

			sql.append("group by 1,2,3,4,5,6,7 ");

			System.out.println(sql.toString());

			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("VDORCAMENTO"));
			ps.setInt(3, codorc);
			ps.setString(4, tipoorc);

			rs = ps.executeQuery();

			if (rs.next()) {
				
				setVlrprod(rs.getBigDecimal("vlrprod"));
				setVlrdesc(rs.getBigDecimal("vlrdesc"));
				setVlradic(rs.getBigDecimal("vlradic"));
				setVlrfrete(rs.getBigDecimal("vlrfrete"));

				setTipofrete(rs.getString("tipofrete") == null ? "F" : rs.getString("tipofrete"));
				setAdicfrete(rs.getString("adicfrete") == null ? "N" : rs.getString("adicfrete"));

				setVlrcustouc(rs.getBigDecimal("vlrcustouc"));
				setVlrcustompm(rs.getBigDecimal("vlrcustompm"));
				setVlrcustopeps(rs.getBigDecimal("vlrcustopeps"));
				setVlrcustoinfo(rs.getBigDecimal("vlrcustoinfo"));
					
				if(descipi) {
				
					
					BigDecimal vlrliq = rs.getBigDecimal("vlrliqorc");
					BigDecimal vlripi = rs.getBigDecimal("vlripi");
					BigDecimal vlrpis = rs.getBigDecimal("vlrpis");
					BigDecimal vlrcofins = rs.getBigDecimal("vlrcofins");
					BigDecimal vlrir = rs.getBigDecimal("vlrir");
					BigDecimal vlrcsocial = rs.getBigDecimal("vlrcsocial");
					BigDecimal vlricms = rs.getBigDecimal("vlricms");
					BigDecimal vlrcomis = rs.getBigDecimal("vlrcomis");
										
					BigDecimal aliqmedipi= vlripi.divide(vlrliq, BigDecimal.ROUND_CEILING);		
					BigDecimal aliqmedpis = vlrpis.divide(vlrliq, BigDecimal.ROUND_CEILING);
					BigDecimal aliqmedcofins = vlrcofins.divide(vlrliq, BigDecimal.ROUND_CEILING);
					BigDecimal aliqmedir = vlrir.divide(vlrliq, BigDecimal.ROUND_CEILING);
					BigDecimal aliqmedcsocial = vlrcsocial.divide(vlrliq, BigDecimal.ROUND_CEILING);
					BigDecimal aliqmedicms = vlricms.divide(vlrliq, BigDecimal.ROUND_CEILING);
					BigDecimal percmedcomis = vlrcomis.divide(vlrliq, BigDecimal.ROUND_CEILING);
					
					BigDecimal percipi = aliqmedipi.multiply(new BigDecimal(100));
					
					BigDecimal preco_bruto = vlrliq;
					BigDecimal preco_liquido_menos_ipi = preco_bruto.divide( new BigDecimal( 1 ).add( percipi.divide( new BigDecimal( 100 ) ) ), BigDecimal.ROUND_CEILING );

					setVlripi( preco_bruto.subtract(preco_liquido_menos_ipi) );

					setVlrpis( preco_liquido_menos_ipi.multiply(aliqmedpis) );
					
					setVlrcofins( preco_liquido_menos_ipi.multiply(aliqmedcofins) );
					setVlrir( preco_liquido_menos_ipi.multiply(aliqmedir) );
					setVlrcsocial( preco_liquido_menos_ipi.multiply(aliqmedcsocial) );
					setVlricms( preco_liquido_menos_ipi.multiply(aliqmedicms) );
					
					setVlrcomis( preco_liquido_menos_ipi.multiply(percmedcomis));
					
				}
				else {
					
					setVlrcomis(rs.getBigDecimal("vlrcomis") == null ? new BigDecimal(0) : rs.getBigDecimal("vlrcomis"));
					
					setVlricms(rs.getBigDecimal("vlricms"));
					setVlripi(rs.getBigDecimal("vlripi"));
					setVlrpis(rs.getBigDecimal("vlrpis"));
					setVlrcofins(rs.getBigDecimal("vlrcofins"));
					setVlrir(rs.getBigDecimal("vlrir"));
					setVlrcsocial(rs.getBigDecimal("vlrcsocial"));

					
				}
					
			
						
				
			
				// implementar iss e simples futuramente

			}

			rs.close();
			ps.close();

			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void carregaItemOrcamento(Integer codorc, String tipoorc, Integer coditorc, boolean descipi) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		try {

			sql.append("select ");
			sql.append("coalesce(io.vlrproditorc,0) vlrprodit, ");
			sql.append("coalesce(io.vlrdescitorc,0) vlrdescit, ");
			sql.append("coalesce(io.vlrcomisitorc,0) vlrcomisit, ");
			sql.append("0 as vlradicit, coalesce(io.vlrfreteitorc,0) vlrfreteit, ");

			sql.append("coalesce((co.vlrcustopeps * io.qtditorc),0) vlrcustoitpeps, ");
			sql.append("coalesce((co.vlrcustompm * io.qtditorc),0) vlrcustoitmpm, ");
			sql.append("coalesce((co.vlrprecoultcp * io.qtditorc),0) vlrcustoituc, ");
			sql.append("coalesce((pd.custoinfoprod * io.qtditorc),0) vlrcustoitinfo, ");
			sql.append("coalesce(pt.vlricmsitorc,0) vlricmsit, ");
			sql.append("coalesce(pt.vlripiitorc,0) vlripiit, ");
			sql.append("coalesce(pt.vlriritorc,0) vlririt, ");
			sql.append("coalesce(pt.vlrcsocialitorc,0) vlrcsocialit, ");
			sql.append("coalesce(pt.vlrpisitorc,0) vlrpisit, ");
			sql.append("coalesce(pt.vlrcofinsitorc,0) vlrcofinsit, ");
			sql.append("coalesce(io.vlrliqitorc,0) vlrliqitorc ");

			sql.append("from ");
			sql.append("vditorcamento io ");
			sql.append("left outer join vditcustoorc co on ");
			sql.append("co.codemp=io.codemp and co.codfilial=io.codfilial and co.codorc=io.codorc and co.tipoorc=io.tipoorc and co.coditorc=io.coditorc ");

			sql.append("left outer join vdprevtribitorc pt on ");
			sql.append("pt.codemp=io.codemp and pt.codfilial=io.codfilial and pt.codorc=io.codorc and pt.tipoorc=io.tipoorc and pt.coditorc=io.coditorc ");
			sql.append("left outer join eqproduto pd on ");
			sql.append("pd.codemp=io.codemppd and pd.codfilial=io.codfilialpd and pd.codprod=io.codprod ");

			sql.append("where io.codemp=? and io.codfilial=? and io.codorc=? and io.tipoorc=? and io.coditorc=?");

			System.out.println(sql.toString());

			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("VDORCAMENTO"));
			ps.setInt(3, codorc);
			ps.setString(4, tipoorc);
			ps.setInt(5, coditorc);

			rs = ps.executeQuery();

			if (rs.next()) {
				setVlrprodit(rs.getBigDecimal("vlrprodit"));
				setVlrdescit(rs.getBigDecimal("vlrdescit"));
				
				
				setVlrfreteit(rs.getBigDecimal("vlrfreteit"));
				setVlradicit(rs.getBigDecimal("vlradicit"));
				
				if(descipi) {
					
					BigDecimal vlrliqit = rs.getBigDecimal("vlrliqitorc");
					
					BigDecimal percipi = rs.getBigDecimal("vlripiit").divide(vlrliqit, BigDecimal.ROUND_CEILING).multiply(new BigDecimal(100));
					BigDecimal percpis = rs.getBigDecimal("vlrpisit").divide(vlrliqit, BigDecimal.ROUND_CEILING);
					BigDecimal perccofins = rs.getBigDecimal("vlrcofinsit").divide(vlrliqit, BigDecimal.ROUND_CEILING);
					BigDecimal percir = rs.getBigDecimal("vlririt").divide(vlrliqit, BigDecimal.ROUND_CEILING);
					BigDecimal perccsocial = rs.getBigDecimal("vlrcsocialit").divide(vlrliqit, BigDecimal.ROUND_CEILING);
					BigDecimal percicms = rs.getBigDecimal("vlricmsit").divide(vlrliqit, BigDecimal.ROUND_CEILING);
					BigDecimal perccomis = rs.getBigDecimal("vlrcomisit").divide(vlrliqit, BigDecimal.ROUND_CEILING);
					
					BigDecimal preco_liquido_menos_ipi = vlrliqit.divide( new BigDecimal( 1 ).add( percipi.divide( new BigDecimal( 100 ) ) ), BigDecimal.ROUND_CEILING );

					BigDecimal vlripi = vlrliqit.subtract(preco_liquido_menos_ipi); 
					BigDecimal vlrpis = preco_liquido_menos_ipi.multiply(percpis);
					BigDecimal vlrcofins = preco_liquido_menos_ipi.multiply(perccofins);
					BigDecimal vlrir = preco_liquido_menos_ipi.multiply(percir);
					BigDecimal vlrcsocial = preco_liquido_menos_ipi.multiply(perccsocial);
					BigDecimal vlricms = preco_liquido_menos_ipi.multiply(percicms);
					BigDecimal vlrcomisit = preco_liquido_menos_ipi.multiply(perccomis);
					
					setVlripiit( vlripi );
					
					setVlrpisit(vlrpis);
					setVlrcofinsit(vlrcofins);
					setVlririt(vlrir);
					setVlrcsocialit(vlrcsocial);
					setVlricmsit(vlricms);
					setVlrcomisit(vlrcomisit);
					
				}
				else {
					
					setVlrcomisit(rs.getBigDecimal("vlrcomisit"));
					
					setVlricmsit(rs.getBigDecimal("vlricmsit"));
					setVlripiit(rs.getBigDecimal("vlripiit"));
					
					setVlrpisit(rs.getBigDecimal("vlrpisit"));
					setVlrcofinsit(rs.getBigDecimal("vlrcofinsit"));
					setVlririt(rs.getBigDecimal("vlririt"));
					setVlrcsocialit(rs.getBigDecimal("vlrcsocialit"));
					
				}
				
				setVlrcustoucit(rs.getBigDecimal("vlrcustoituc"));
				setVlrcustompmit(rs.getBigDecimal("vlrcustoitmpm"));
				setVlrcustopepsit(rs.getBigDecimal("vlrcustoitpeps"));
				setVlrcustoinfoit(rs.getBigDecimal("vlrcustoitinfo"));
				
				
			}

			rs.close();
			ps.close();
			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
