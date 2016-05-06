package org.freedom.library.business.componet.integration.ebs;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.componet.integration.Contabil;
import org.freedom.library.business.componet.integration.ebs.vo.EmitenteDestinatarioVO;
import org.freedom.library.business.componet.integration.ebs.vo.EntradaVO;
import org.freedom.library.business.componet.integration.ebs.vo.HeaderEntradaVO;
import org.freedom.library.business.componet.integration.ebs.vo.HeaderSaidaVO;
import org.freedom.library.business.componet.integration.ebs.vo.ItemEntradaVO;
import org.freedom.library.business.componet.integration.ebs.vo.ItemSaidaVO;
import org.freedom.library.business.componet.integration.ebs.vo.ItemVO;
import org.freedom.library.business.componet.integration.ebs.vo.NotaItemVO;
import org.freedom.library.business.componet.integration.ebs.vo.PrazoVO;
import org.freedom.library.business.componet.integration.ebs.vo.SaidaVO;
import org.freedom.library.business.componet.integration.ebs.vo.TraillerEntradaVO;
import org.freedom.library.business.componet.integration.ebs.vo.TraillerSaidaVO;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;

/**
 * 
 * @author 
 *
 */
public class EbsContabil extends Contabil {

	private DbConnection con;

	private Date dtini;

	private Date dtfim;

	private int sequencial;

	private List<String> readrowsSaida = new ArrayList<String>();

	private List<String> readrowsItem = new ArrayList<String>();
	
	private List<String> readrowsNotaItem = new ArrayList<String>();

	public EbsContabil() {

		super();
		this.sequencial = 1;
	}

	@Override
	public void createFile(File filecontabil) throws Exception {

		sizeMax = readrows.size() + readrowsSaida.size() + readrowsItem.size() + readrowsNotaItem.size();

		if (sizeMax == 0) {
			throw new Exception("Nenhum registro encontrado para exportação!");
		}

		fireActionListenerForMaxSize();

		progressInRows = 1;

		File entradas = new File(filecontabil.getPath() + SEPDIR + "NOTAENT.TXT");
		entradas.createNewFile();

		FileWriter fwEntradas = new FileWriter(entradas);

		for (String row : readrows) {

			fwEntradas.write(row);
			fwEntradas.write(RETURN);
			fwEntradas.flush();

			progressInRows++;

			fireActionListenerProgressInRows();
		}

		fwEntradas.close();

		File saidas = new File(filecontabil.getPath() + SEPDIR + "NOTASAI.TXT");
		saidas.createNewFile();

		FileWriter fwSaidas = new FileWriter(saidas);

		for (String row : readrowsSaida) {

			fwSaidas.write(row);
			fwSaidas.write(RETURN);
			fwSaidas.flush();

			progressInRows++;

			fireActionListenerProgressInRows();
		}

		fwSaidas.close();

		File itens = new File(filecontabil.getPath() + SEPDIR + "ITEM.TXT");
		itens.createNewFile();

		FileWriter fwItens = new FileWriter(itens);

		for (String row : readrowsItem) {

			fwItens.write(row);
			fwItens.write(RETURN);
			fwItens.flush();

			progressInRows++;

			fireActionListenerProgressInRows();
		}

		fwItens.close();
		
		File notaItens = new File(filecontabil.getPath() + SEPDIR + "NOTAITEM.TXT");
		notaItens.createNewFile();

		FileWriter fwNotaItens = new FileWriter(notaItens);

		for (String row : readrowsNotaItem) {

			fwNotaItens.write(row);
			fwNotaItens.write(RETURN);
			fwNotaItens.flush();

			progressInRows++;

			fireActionListenerProgressInRows();
		}

		fwNotaItens.close();
	}

	public void execute(final DbConnection con, final Date dtini, final Date dtfim) throws Exception {

		this.con = con;
		this.dtini = dtini;
		this.dtfim = dtfim;

		readrows.clear();
		readrowsSaida.clear();
		readrowsItem.clear();

		sequencial = 1;
		entradas();
		sequencial = 1;
		saidas();
		sequencial = 1;
		produtos();
		sequencial = 1;
		notaItens();
	}

	private void emitente(char tipo, int codigo) throws Exception {

		StringBuilder sql = new StringBuilder();

		if (tipo == 'C') {
			sql.append("SELECT ");
			sql.append("C.RAZCLI RAZAO,");
			sql.append("C.NOMECLI NOME,");
			sql.append("C.CNPJCLI CNPJ, C.CPFCLI CPF, C.PESSOACLI PESSOA,");
			sql.append("COALESCE(C.INSCCLI,'ISENTO') INSC,");
			sql.append("M.CODMUNIC CODMUNIC,");
			sql.append("M.NOMEMUNIC MUNICIPIO,");
			sql.append("COALESCE(M.SIGLAUF,C.UFCLI) UF,");
			sql.append("M.CODPAIS PAIS, ");
			sql.append("C.ENDCLI ENDERECO,");
			sql.append("C.NUMCLI NUMERO,");
			sql.append("C.BAIRCLI BAIRRO,");
			sql.append("C.CEPCLI CEP,");
			sql.append("C.DDDCLI DDD,");
			sql.append("C.FONECLI FONE,");
			sql.append("C.COMPLCLI COMPLEMENTO, ");
			sql.append("C.CODCLICONTAB CODCONTAB, ");
			sql.append("U.CODUF, ");
			sql.append("C.PRODRURALCLI PRODRURAL ");
			sql.append("FROM VDCLIENTE C ");
			sql.append("LEFT OUTER JOIN SGMUNICIPIO M ");
			sql.append("ON M.CODPAIS=C.CODPAIS AND M.SIGLAUF=C.SIGLAUF AND M.CODMUNIC=C.CODMUNIC ");
			sql.append("INNER JOIN SGUF U ON U.SIGLAUF = M.SIGLAUF ");
			sql.append("WHERE C.CODEMP=? AND C.CODFILIAL=? AND C.CODCLI=?");
		}
		else if (tipo == 'F') {
			sql.append("SELECT ");
			sql.append("F.RAZFOR RAZAO,");
			sql.append("F.NOMEFOR NOME,");
			sql.append("F.CNPJFOR CNPJ, F.CPFFOR CPF, F.PESSOAFOR PESSOA,");
			sql.append("COALESCE(F.INSCFOR,'ISENTO') INSC,");
			sql.append("M.CODMUNIC CODMUNIC,");
			sql.append("M.NOMEMUNIC MUNICIPIO,");
			sql.append("COALESCE(M.SIGLAUF,F.UFFOR) UF,");
			sql.append("M.CODPAIS PAIS,");
			sql.append("F.ENDFOR ENDERECO,");
			sql.append("F.NUMFOR NUMERO,");
			sql.append("F.BAIRFOR BAIRRO,");
			sql.append("F.CEPFOR CEP,");
			sql.append("F.DDDFONEFOR DDD,");
			sql.append("F.FONEFOR FONE,");
			sql.append("F.COMPLFOR COMPLEMENTO, ");
			sql.append("F.CODFORCONTAB CODCONTAB, ");
			sql.append("U.CODUF, ");
			sql.append("'N' PRODRURAL ");
			sql.append("FROM CPFORNECED F ");
			sql.append("LEFT OUTER JOIN SGMUNICIPIO M ");
			sql.append("ON M.CODPAIS=F.CODPAIS AND M.SIGLAUF=F.SIGLAUF AND M.CODMUNIC=F.CODMUNIC ");
			sql.append("INNER JOIN SGUF U ON U.SIGLAUF = M.SIGLAUF ");
			sql.append("WHERE F.CODEMP=? AND F.CODFILIAL=? AND F.CODFOR=?");
		}

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial(tipo == 'C' ? "VDVENDA" : "CPCOMPRA"));
		ps.setInt(3, codigo);

		ResultSet rs = ps.executeQuery();
		EmitenteDestinatarioVO emitenteDestinatario = null;

		if (rs.next()) {

			emitenteDestinatario = new EmitenteDestinatarioVO();
			emitenteDestinatario.setRazaoSocial(rs.getString("RAZAO"));
			emitenteDestinatario.setNomeFantazia(rs.getString("NOME"));
			if ("J".equals(rs.getString("PESSOA"))) {
				emitenteDestinatario.setCnpj(rs.getString("CNPJ"));
			}
			else {
				emitenteDestinatario.setCpf(rs.getString("CPF"));
			}
			emitenteDestinatario.setInscricao(rs.getString("INSC"));
			emitenteDestinatario.setCidade(rs.getString("MUNICIPIO"));
			emitenteDestinatario.setMunicipio(0);
			//emitenteDestinatario.setMunicipio(convertMunic(rs.getInt("CODMUNIC")));
			emitenteDestinatario.setEstado(rs.getString("UF"));
			emitenteDestinatario.setPais(rs.getInt("CODPAIS"));
			emitenteDestinatario.setEndereco(rs.getString("ENDERECO"));
			emitenteDestinatario.setNumero(rs.getInt("NUMERO"));
			emitenteDestinatario.setBairro(rs.getString("BAIRRO"));
			emitenteDestinatario.setCep(rs.getInt("CEP"));
			try {
				emitenteDestinatario.setDdd(rs.getInt("DDD"));
				emitenteDestinatario.setTelefone(rs.getInt("FONE"));
			}
			catch (Exception e) {
				System.out.println("Erro ao formatar telefone do cliente/fornecedor[" + rs.getString("RAZAO").trim() + "]:\n" + e.getMessage());
			}
			emitenteDestinatario.setComplemento(rs.getString("COMPLEMENTO"));
			
			emitenteDestinatario.setIbge(rs.getString("CODUF")+rs.getString("CODMUNIC"));

			emitenteDestinatario.setContaCliente(0);
			emitenteDestinatario.setHistoricoCliente(0);
			emitenteDestinatario.setContaFornecedor(0);
			emitenteDestinatario.setProdutor("S".equals(rs.getString("PRODRURAL")));
			emitenteDestinatario.setHistoricoFornecedor(0);
			emitenteDestinatario.setIndentificacaoExterior(null);
			emitenteDestinatario.setSuframa(null);

			emitenteDestinatario.setSequencial(sequencial++);

			if (tipo == 'F') {
				readrows.add(emitenteDestinatario.toString());
			}
			else if (tipo == 'C') {
				readrowsSaida.add(emitenteDestinatario.toString());
			}
		}

		rs.close();
		ps.close();
	}
/*
	private int convertMunic(int codmunic)	{
		int result = 0;
		String tmp = String.valueOf(codmunic);
		if(tmp.length() > 4)	{
			tmp = tmp.substring(tmp.length()-4);					
		}
		
		result = Integer.parseInt(tmp);		
		return result ;
	}
*/
	private void headerEntradas() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.razfilial, f.cnpjfilial from sgfilial f where f.codemp=? and f.codfilial=?");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("sgfilial"));

		ResultSet rs = ps.executeQuery();
		String cnpj = null;

		if (rs.next()) {
			cnpj = rs.getString("cnpjfilial");
		}

		rs.close();
		ps.close();

		HeaderEntradaVO headerEntradas = new HeaderEntradaVO();
		headerEntradas.setDataArquivo(Calendar.getInstance().getTime());
		headerEntradas.setCnpj(cnpj);
		headerEntradas.setSequencial(sequencial++);

		readrows.add(headerEntradas.toString());
	}

	private void entradas() throws Exception {

		StringBuilder sqlprazo = new StringBuilder();
		StringBuilder sql = new StringBuilder();

		sqlprazo.append("select (case when ip.dtitpag=ip.dtvencitpag then 'E' else 'N' end) tipoparcela, ");
		sqlprazo.append("p.docpag, coalesce(tc.tiposped,'00') tipotitulo, ip.dtvencitpag, ip.vlritpag ");
		sqlprazo.append("from cpcompra c, fnpagar p, fnparcpag pp, fnplanopag pg, fnitpagar ip ");
		sqlprazo.append("left outer join fntipocob tc on ");
		sqlprazo.append("tc.codemp=ip.codemptc and tc.codfilial=ip.codfilialtc and tc.codtipocob=ip.codtipocob ");
		sqlprazo.append("where p.codempcp=c.codemp and p.codfilialcp=c.codfilial and p.codcompra=c.codcompra and ");
		sqlprazo.append("ip.codemp=p.codemp and ip.codfilial=p.codfilial and ip.codpag=p.codpag and ");
		sqlprazo.append("pp.codemp=p.codemppg and pp.codfilial=p.codfilialpg and pp.codplanopag=p.codplanopag and ");
		sqlprazo.append("pp.nroparcpag=ip.nparcpag and ");
		sqlprazo.append("pg.codemp=pp.codemp and pg.codfilial=pp.codfilial and pg.codplanopag=pp.codplanopag and ");
		sqlprazo.append("c.codemp=? and c.codfilial=? and c.dtentcompra between ? and ? and ");
		sqlprazo.append("exists (select i.coditcompra from cpitcompra i where i.codemp=c.codemp and ");
		sqlprazo.append("i.codfilial=c.codfilial and i.codcompra=c.codcompra ) ");
		sqlprazo.append("order by c.codcompra ");

		
		sql.append("select c.codcompra, c.codfor,");
		sql.append("c.dtentcompra, c.doccompra, c.dtemitcompra, c.serie, c.vlrliqcompra, c.vlrbaseipicompra, c.vlripicompra,");
		sql.append("tm.codmodnota, tm.especietipomov, coalesce(f.cnpjfor, f.cpffor) cnpjfor, p.datapag, f.codforcontab, ");
		sql.append("(case when tm.emitnfcpmov='S' then 'P' else 'T' end) tipoemissao, ");
		sql.append("coalesce(sum(lic.vlrbasepis),0) vlrbasepis, coalesce(sum(lic.vlrbasecofins),0) vlrbasecofins, ");
		sql.append("coalesce(sum(lic.vlrpis),0) vlrpis, coalesce(sum(vlrcofins),0) vlrcofins ");
		sql.append("from eqtipomov tm, lfmodnota mn, lfserie s, cpforneced f, fnpagar p, sgfilial fl, cpcompra c ");
		sql.append("left outer join lfitcompra lic on ");
		sql.append("lic.codemp=c.codemp and lic.codfilial=c.codfilialtc and lic.codcompra=c.codcompra ");
		sql.append("where c.codemp=? and c.codfilial=? and c.dtentcompra between ? and ? and ");
		sql.append("tm.codemp=c.codemptm and tm.codfilial=c.codfilialtm and tm.codtipomov=c.codtipomov and ");
		sql.append("mn.codemp=tm.codempmn and mn.codfilial=tm.codfilialmn and mn.codmodnota=tm.codmodnota and ");
		sql.append("s.codemp=c.codempse and s.codfilial=c.codfilialse and s.serie=c.serie and ");
		sql.append("f.codemp=c.codempfr and f.codfilial=c.codfilialfr and f.codfor=c.codfor and ");
		sql.append("p.codempcp=c.codemp and p.codfilialcp=c.codfilial and p.codcompra=c.codcompra and ");
		sql.append("exists (select i.coditcompra from cpitcompra i where i.codemp=c.codemp and i.codfilial=c.codfilial and i.codcompra=c.codcompra ) and ");
		sql.append("fl.codemp=? and fl.codfilial=? ");
		sql.append("group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 ");
		sql.append("order by c.codcompra ");
		
		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("CPCOMPRA"));
		ps.setDate(3, Funcoes.dateToSQLDate(dtini));
		ps.setDate(4, Funcoes.dateToSQLDate(dtfim));
		ps.setInt(5, Aplicativo.iCodEmp);
		ps.setInt(6, ListaCampos.getMasterFilial("SGFILIAL"));

		ResultSet rs = ps.executeQuery();
		EntradaVO entrada = null;
		boolean readHeader = true;
		TraillerEntradaVO traillerEntradas = null;

		while (rs.next()) {

			if (readHeader) {
				headerEntradas();
				readHeader = false;
				traillerEntradas = getTraillerEntrada();
			}

			entrada = new EntradaVO();
			entrada.setDataLancamento(rs.getDate("dtentcompra"));
			entrada.setNota(rs.getInt("doccompra"));
			entrada.setDataEmissao(rs.getDate("dtemitcompra"));
			entrada.setModeloNota(rs.getInt("codmodnota"));
			entrada.setSerie(rs.getString("serie"));
			entrada.setSubSerie(null);
			entrada.setVariacaoCfop(1);

			entrada.setCfop( Integer.valueOf( getCfopCompra(rs.getInt("codcompra")) ));
			entrada.setClassificacaoIntegracao(0);
			entrada.setClassificacaoIntegracao2(0);
			entrada.setCnpjFornecedor(rs.getString("cnpjfor"));
			entrada.setValorNota(rs.getBigDecimal("vlrliqcompra") != null ? rs.getBigDecimal("vlrliqcompra") : new BigDecimal("0.00"));
			entrada.setBasePIS(rs.getBigDecimal("vlrBasePis"));
			entrada.setBaseCOFINS(rs.getBigDecimal("vlrbasecofins"));
			entrada.setBaseCSLL(new BigDecimal("0.00"));
			entrada.setBaseIR(new BigDecimal("0.00"));

			StringBuilder sqlICMS = new StringBuilder();
			sqlICMS.append("select ic.percicmsitcompra aliquota, sum(ic.vlrbaseicmsitcompra) base, sum(ic.vlricmsitcompra) valor ");
			sqlICMS.append("from cpitcompra ic ");
			sqlICMS.append("where ic.codemp=? and ic.codfilial=? and ic.codcompra=? ");
			sqlICMS.append("group by ic.percicmsitcompra");

			PreparedStatement psICMS = con.prepareStatement(sqlICMS.toString());
			psICMS.setInt(1, Aplicativo.iCodEmp);
			psICMS.setInt(2, ListaCampos.getMasterFilial("CPITCOMPRA"));
			psICMS.setInt(3, rs.getInt("codcompra"));

			ResultSet rsICMS = psICMS.executeQuery();

			entrada.setBaseICMSa(new BigDecimal("0.00"));
			entrada.setAliquotaICMSa(new BigDecimal("0.00"));
			entrada.setValorICMSa(new BigDecimal("0.00"));
			entrada.setBaseICMSb(new BigDecimal("0.00"));
			entrada.setAliquotaICMSb(new BigDecimal("0.00"));
			entrada.setValorICMSb(new BigDecimal("0.00"));
			entrada.setBaseICMSc(new BigDecimal("0.00"));
			entrada.setAliquotaICMSc(new BigDecimal("0.00"));
			entrada.setValorICMSc(new BigDecimal("0.00"));
			entrada.setBaseICMSd(new BigDecimal("0.00"));
			entrada.setAliquotaICMSd(new BigDecimal("0.00"));
			entrada.setValorICMSd(new BigDecimal("0.00"));

			for (int i = 0; i < 4 && rsICMS.next(); i++) {

				if (i == 0) {
					entrada.setBaseICMSa(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					entrada.setAliquotaICMSa(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					entrada.setValorICMSa(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
				else if (i == 1) {
					entrada.setBaseICMSb(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					entrada.setAliquotaICMSb(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					entrada.setValorICMSb(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
				else if (i == 2) {
					entrada.setBaseICMSc(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					entrada.setAliquotaICMSc(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					entrada.setValorICMSc(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
				else if (i == 3) {
					entrada.setBaseICMSd(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					entrada.setAliquotaICMSd(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					entrada.setValorICMSd(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
			}
			rsICMS.close();
			psICMS.close();

			entrada.setValorICMSIsentas(new BigDecimal("0.00"));

			if (entrada.getBaseICMSa().floatValue() > 0) {

				if (entrada.getBaseICMSa().floatValue() == entrada.getValorNota().floatValue()) {
					entrada.setValorICMSOutras(new BigDecimal("0.00"));
				}
				else {
					entrada.setValorICMSOutras(( entrada.getValorNota().subtract(entrada.getBaseICMSa().add(entrada.getBaseICMSb()).add(entrada.getBaseICMSc()).add(entrada.getBaseICMSd())) ).abs());
				}
			}
			else {
				entrada.setValorICMSOutras(entrada.getValorNota());
			}

			entrada.setBaseIPI(rs.getBigDecimal("vlrbaseipicompra") != null ? rs.getBigDecimal("vlrbaseipicompra") : new BigDecimal("0.00"));
			entrada.setValorIPI(rs.getBigDecimal("vlripicompra") != null ? rs.getBigDecimal("vlripicompra") : new BigDecimal("0.00"));
			entrada.setValorIPIIsentas(new BigDecimal("0.00"));
			entrada.setValorIPIOutras(new BigDecimal("0.00"));
			entrada.setValorSubTributaria(new BigDecimal("0.00"));
			entrada.setBaseSubTributaria(new BigDecimal("0.00"));
			entrada.setValorICMSSubTributaria(new BigDecimal("0.00"));
			entrada.setValorDiferidas(new BigDecimal("0.00"));
			entrada.setObservacaoLivroFiscal(null);
			entrada.setEspecieNota(rs.getString("especietipomov"));
			entrada.setVendaAVista(rs.getDate("dtemitcompra").compareTo(rs.getDate("datapag")) == 0 ? "S" : "N");
			entrada.setCfopSubTributaria(0);
			entrada.setBasePISCOFINSSubTributaria(new BigDecimal("0.00"));
			entrada.setBaseISS(new BigDecimal("0.00"));
			entrada.setAliquotaISS(new BigDecimal("0.00"));
			entrada.setValorISS(new BigDecimal("0.00"));
			entrada.setValorISSIsentas(new BigDecimal("0.00"));
			entrada.setValorIRRF(new BigDecimal("0.00"));
			entrada.setValorPIS(rs.getBigDecimal("vlrpis"));
			entrada.setValorCOFINS(rs.getBigDecimal("vlrcofins"));
			entrada.setValorCSLL(new BigDecimal("0.00"));
			entrada.setDataPagamento(rs.getDate("datapag"));
			entrada.setCodigoOperacaoContabil(0);
			entrada.setIndentificacaoExterior(null);
			entrada.setValorINSS(new BigDecimal("0.00"));
			entrada.setValorFUNRURAL(new BigDecimal("0.00"));
			// Habilitar quando for necessário o envio de informações de serviço
			entrada.setCodigoItemServico(0);
			entrada.setIssRetido("N");
			entrada.setIssDevido("N");
			entrada.setUfPrestacao("  ");
			
			entrada.setTipoEmissao(rs.getString("tipoemissao"));

			emitente('F', rs.getInt("codfor"));

			entrada.setSequencial(sequencial++);
			readrows.add(entrada.toString());

			itensEntrada(rs.getInt("codcompra"));

			traillerEntradas.setValorNota(traillerEntradas.getValorNota().add(entrada.getValorNota()));
			traillerEntradas.setBasePIS(traillerEntradas.getBasePIS().add(entrada.getBasePIS()));
			traillerEntradas.setBaseCOFINS(traillerEntradas.getBaseCOFINS().add(entrada.getBaseCOFINS()));
			traillerEntradas.setBaseContribuicaoSocial(traillerEntradas.getBaseContribuicaoSocial().add(entrada.getBaseCSLL()));
			traillerEntradas.setBaseImpostoRenda(traillerEntradas.getBaseImpostoRenda().add(entrada.getBaseIR()));
			traillerEntradas.setBaseICMSa(traillerEntradas.getBaseICMSa().add(entrada.getBaseICMSa()));
			traillerEntradas.setValorICMSa(traillerEntradas.getValorICMSa().add(entrada.getValorICMSa()));
			traillerEntradas.setBaseICMSb(traillerEntradas.getBaseICMSb().add(entrada.getBaseICMSb()));
			traillerEntradas.setValorICMSb(traillerEntradas.getValorICMSb().add(entrada.getValorICMSb()));
			traillerEntradas.setBaseICMSc(traillerEntradas.getBaseICMSc().add(entrada.getBaseICMSc()));
			traillerEntradas.setValorICMSc(traillerEntradas.getValorICMSc().add(entrada.getValorICMSc()));
			traillerEntradas.setBaseICMSd(traillerEntradas.getBaseICMSd().add(entrada.getBaseICMSd()));
			traillerEntradas.setValorICMSd(traillerEntradas.getValorICMSd().add(entrada.getValorICMSd()));
			traillerEntradas.setValorICMSIsentas(traillerEntradas.getValorICMSIsentas().add(entrada.getValorICMSIsentas()));
			traillerEntradas.setValorICMSOutras(traillerEntradas.getValorICMSOutras().add(entrada.getValorICMSOutras()));
			traillerEntradas.setBaseIPI(traillerEntradas.getBaseIPI().add(entrada.getBaseIPI()));
			traillerEntradas.setValorIPI(traillerEntradas.getValorIPI().add(entrada.getValorIPI()));
			traillerEntradas.setValorIPIIsentas(traillerEntradas.getValorIPIIsentas().add(entrada.getValorIPIIsentas()));
			traillerEntradas.setValorIPIOutras(traillerEntradas.getValorIPIOutras().add(entrada.getValorIPIOutras()));
			traillerEntradas.setValorSubTributaria(traillerEntradas.getValorSubTributaria().add(entrada.getValorSubTributaria()));
			traillerEntradas.setBaseSubTriburaria(traillerEntradas.getBaseSubTriburaria().add(entrada.getBaseSubTributaria()));
			traillerEntradas.setValorICMSSubTributaria(traillerEntradas.getValorICMSSubTributaria().add(entrada.getValorICMSSubTributaria()));
			traillerEntradas.setValorDiferidas(traillerEntradas.getValorDiferidas().add(entrada.getValorDiferidas()));
		}

		rs.close();
		ps.close();
		
		// Executando sql de parcelas
        ps = con.prepareStatement(sqlprazo.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("CPCOMPRA"));
		ps.setDate(3, Funcoes.dateToSQLDate(dtini));
		ps.setDate(4, Funcoes.dateToSQLDate(dtfim));
        rs = ps.executeQuery();
        PrazoVO prazovo = null;
        while (rs.next()) {
        	prazovo = new PrazoVO();
        	prazovo.setTipoParcela(rs.getString("TIPOPARCELA"));
        	prazovo.setNrFatura(rs.getInt("DOCPAG"));
        	prazovo.setTipoTitulo(rs.getString("TIPOTITULO"));
        	prazovo.setDtVencimento(rs.getDate("DTVENCITPAG"));
        	prazovo.setVlrParcela(rs.getBigDecimal("VLRITPAG"));
			prazovo.setSequencial(sequencial++);
			readrows.add(prazovo.toString());
        }
        
		rs.close();
		ps.close();

        if (traillerEntradas != null) {

			traillerEntradas.setSequencial(sequencial++);
			readrows.add(traillerEntradas.toString());
		}


		con.commit();
	}

	private void itensEntrada(int compra) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("I.CODITCOMPRA,");
		sql.append("I.CODPROD, P.REFPROD,");
		sql.append("I.QTDITCOMPRA,");
		sql.append("(I.QTDITCOMPRA * I.PRECOITCOMPRA) VALOR,");
		sql.append("I.VLRDESCITCOMPRA,");
		sql.append("I.VLRBASEICMSITCOMPRA,");
		sql.append("I.VLRBASEIPIITCOMPRA,");
		sql.append("I.PERCICMSITCOMPRA,");
		sql.append("I.VLRICMSITCOMPRA, ");
		sql.append("I.VLRIPIITCOMPRA,");
		sql.append("I.PERCIPIITCOMPRA, ");
		sql.append("I.VLRBASEICMSSTITCOMPRA, ");
		sql.append("I.PERCICMSSTITCOMPRA, ");
		sql.append("I.VLRICMSSTITCOMPRA, ");
		sql.append("I.CODNAT, ");
		sql.append("P.CODUNID, ");
		sql.append("COALESCE(LIC.VLRPIS, 0) VLRPIS, ");
		sql.append("COALESCE(LIC.ALIQPIS,0) ALIQPIS, ");
		sql.append("COALESCE(LIC.VLRCOFINS, 0) VLRCOFINS, ");
		sql.append("COALESCE(LIC.ALIQCOFINS, 0) ALIQCOFINS, ");
		sql.append("COALESCE(LIC.VLRBASEPIS, 0) VLRBASEPIS, ");
		sql.append("COALESCE(LIC.VLRBASECOFINS, 0) VLRBASECOFINS, ");
		sql.append("COALESCE(LIC.CODSITTRIBPIS,0) CODSITTRIBPIS, ");
		sql.append("COALESCE(LIC.CODSITTRIBCOF,0) CODSITTRIBCOF, ");
		sql.append("COALESCE(LIC.CODSITTRIBIPI,0) CODSITTRIBIPI ");
		sql.append("FROM EQPRODUTO P, CPITCOMPRA I ");
		sql.append("LEFT OUTER JOIN LFITCOMPRA LIC  ON ");
		sql.append("LIC.CODEMP=I.CODEMP AND LIC.CODFILIAL=I.CODFILIAL AND LIC.CODCOMPRA=I.CODCOMPRA AND LIC.CODITCOMPRA = I.CODITCOMPRA ");
		sql.append("WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.CODCOMPRA=? AND ");
		sql.append("P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD ");	
		sql.append("GROUP BY 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26 ");
		sql.append("ORDER BY I.CODITCOMPRA");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("CPITCOMPRA"));
		ps.setInt(3, compra);

		ResultSet rs = ps.executeQuery();
		ItemEntradaVO itemEntrada = null;

		while (rs.next()) {

			itemEntrada = new ItemEntradaVO();
			itemEntrada.setCodigo(rs.getInt("CODPROD"));
			itemEntrada.setQuantidade(rs.getBigDecimal("QTDITCOMPRA") != null ? rs.getBigDecimal("QTDITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setValor(rs.getBigDecimal("VALOR") != null ? rs.getBigDecimal("VALOR") : new BigDecimal("0.00"));
			itemEntrada.setQuantidade2(null);
			itemEntrada.setDesconto(rs.getBigDecimal("VLRDESCITCOMPRA") != null ? rs.getBigDecimal("VLRDESCITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setBaseICMS(rs.getBigDecimal("VLRBASEICMSITCOMPRA") != null ? rs.getBigDecimal("VLRBASEICMSITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setAliquotaICMS(rs.getBigDecimal("PERCICMSITCOMPRA") != null ? rs.getBigDecimal("PERCICMSITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setValorIPI(rs.getBigDecimal("VLRIPIITCOMPRA") != null ? rs.getBigDecimal("VLRIPIITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setAliquotaIPI(rs.getBigDecimal("PERCIPIITCOMPRA") != null ? rs.getBigDecimal("PERCIPIITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setBaseIPI(rs.getBigDecimal("VLRBASEIPIITCOMPRA") != null ? rs.getBigDecimal("VLRBASEIPIITCOMPRA") : new BigDecimal("0.00"));
			itemEntrada.setIndentificacao(rs.getString("REFPROD"));
			itemEntrada.setBaseICMSSubTributaria(rs.getBigDecimal("VLRBASEICMSSTITCOMPRA"));
			itemEntrada.setPercentualReducaoBaseICMS(null);
			itemEntrada.setSituacaoTributaria(0);
			itemEntrada.setSituacaoTributariaIPI(rs.getInt("CODSITTRIBIPI"));
			itemEntrada.setSituacaoTributariaPIS(rs.getInt("CODSITTRIBPIS"));
			itemEntrada.setBasePIS(rs.getBigDecimal("VLRBASEPIS"));
			itemEntrada.setAliquotaPIS(rs.getBigDecimal("ALIQPIS"));
			itemEntrada.setQuantidadeBasePIS(null);
			itemEntrada.setValorAliquotaPIS(null);
			itemEntrada.setValorPIS(rs.getBigDecimal("VLRPIS"));
			itemEntrada.setSituacaoTributariaCOFINS(rs.getInt("CODSITTRIBCOF"));
			itemEntrada.setBaseCOFINS(rs.getBigDecimal("VLRBASECOFINS"));
			itemEntrada.setAliquotaCOFINS(rs.getBigDecimal("ALIQCOFINS"));
			itemEntrada.setQuantidadeBaseCOFINS(null);
			itemEntrada.setValorAliquotaCOFINS(null);
			itemEntrada.setValorCOFINS(rs.getBigDecimal("VLRCOFINS"));
			itemEntrada.setValorICMSSubTributaria(rs.getBigDecimal("VLRICMSSTITCOMPRA"));
			//Itens adicionado devido a novo layout de entradas.
			itemEntrada.setAliquotaICMSSubTributaria(rs.getBigDecimal("PERCICMSSTITCOMPRA"));
			itemEntrada.setValorICMS(rs.getBigDecimal("VLRICMSITCOMPRA"));
			itemEntrada.setNaturezaItem(rs.getString("CODNAT"));
			itemEntrada.setUnidade(rs.getString("CODUNID"));
			//Até aqui.
			itemEntrada.setSequencial(sequencial++);

			readrows.add(itemEntrada.toString());
		}

		rs.close();
		ps.close();
	}

	private TraillerEntradaVO getTraillerEntrada() {

		TraillerEntradaVO traillerEntradas = new TraillerEntradaVO();

		traillerEntradas.setValorNota(new BigDecimal("0.00"));
		traillerEntradas.setBasePIS(new BigDecimal("0.00"));
		traillerEntradas.setBaseCOFINS(new BigDecimal("0.00"));
		traillerEntradas.setBaseContribuicaoSocial(new BigDecimal("0.00"));
		traillerEntradas.setBaseImpostoRenda(new BigDecimal("0.00"));
		traillerEntradas.setBaseICMSa(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSa(new BigDecimal("0.00"));
		traillerEntradas.setBaseICMSb(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSb(new BigDecimal("0.00"));
		traillerEntradas.setBaseICMSc(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSc(new BigDecimal("0.00"));
		traillerEntradas.setBaseICMSd(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSd(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSIsentas(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSOutras(new BigDecimal("0.00"));
		traillerEntradas.setBaseIPI(new BigDecimal("0.00"));
		traillerEntradas.setValorIPI(new BigDecimal("0.00"));
		traillerEntradas.setValorIPIIsentas(new BigDecimal("0.00"));
		traillerEntradas.setValorIPIOutras(new BigDecimal("0.00"));
		traillerEntradas.setValorSubTributaria(new BigDecimal("0.00"));
		traillerEntradas.setBaseSubTriburaria(new BigDecimal("0.00"));
		traillerEntradas.setValorICMSSubTributaria(new BigDecimal("0.00"));
		traillerEntradas.setValorDiferidas(new BigDecimal("0.00"));

		return traillerEntradas;
	}

	private void headerSaida() throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("select f.razfilial, f.cnpjfilial from sgfilial f where f.codemp=? and f.codfilial=?");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("sgfilial"));

		ResultSet rs = ps.executeQuery();
		String cnpj = null;

		if (rs.next()) {
			cnpj = rs.getString("cnpjfilial");
		}

		rs.close();
		ps.close();

		HeaderSaidaVO headerSaida = new HeaderSaidaVO();
		headerSaida.setDataArquivo(Calendar.getInstance().getTime());
		headerSaida.setCnpj(cnpj);
		headerSaida.setSequencial(sequencial++);

		readrowsSaida.add(headerSaida.toString());
	}

	private void saidas() throws Exception {

		
		StringBuilder sqlprazo = new StringBuilder();
		sqlprazo.append("select (case when ir.dtitrec=ir.dtvencitrec then 'E' else 'N' end) tipoparcela, ");
		sqlprazo.append("r.docrec, coalesce(tc.tiposped,'00') tipotitulo, ir.dtvencitrec, ir.vlritrec ");
		sqlprazo.append("from vdvenda v, fnreceber r, fnparcpag pp, fnplanopag pg, fnitreceber ir ");
		sqlprazo.append("left outer join fntipocob tc on ");
		sqlprazo.append("tc.codemp=ir.codemp and tc.codfilial=ir.codfilialtc and tc.codtipocob=ir.codtipocob ");
		sqlprazo.append("where r.codempvd=v.codemptc and r.codfilialvd=v.codfilial and r.tipovenda= v.tipovenda and r.codvenda = v.codvenda and ");
		sqlprazo.append("ir.codemp=r.codemp and ir.codfilial=r.codfilial and ir.codrec=r.codrec and ");
		sqlprazo.append("pp.codemp=r.codempvd and pp.codfilial=r.codfilialvd and pp.codplanopag=r.codplanopag and ");
		sqlprazo.append("pp.nroparcpag=ir.nparcitrec and ");
		sqlprazo.append("pg.codemp=pp.codemp and pg.codfilial=pp.codfilial and pg.codplanopag=pp.codplanopag and ");
		sqlprazo.append("v.codemp=? and v.codfilial=? and v.dtemitvenda between ? and ? and ");
		sqlprazo.append("exists (select i.coditvenda from vditvenda i where i.codemp=v.codemp and ");
		sqlprazo.append("i.codfilial=v.codfilial and i.codvenda=v.codvenda and i.tipovenda=v.tipovenda ) ");
		sqlprazo.append("order by v.codvenda");
		
		StringBuilder sql = new StringBuilder();
		sql.append("select v.codvenda, v.tipovenda, v.codcli,");
		sql.append("v.dtemitvenda, v.docvenda, v.dtsaidavenda, v.serie, v.vlrliqvenda, v.vlrbaseipivenda, v.vlripivenda,");
		sql.append("tm.codmodnota, tm.especietipomov, c.cnpjcli, c.cpfcli, r.datarec, c.codclicontab, ");
		sql.append("coalesce(sum(liv.vlrbasepis), 0)  vlrbasepis, coalesce( sum(liv.vlrbasecofins), 0 ) vlrbasecofins, ");
		sql.append("coalesce(sum(liv.vlrpis),0) vlrpis, coalesce(sum(liv.vlrcofins),0) vlrcofins "); 
		sql.append("from eqtipomov tm, lfserie s, vdcliente c, fnreceber r, lfmodnota mn, vdvenda v ");
		sql.append("left outer join lfitvenda liv on ");
		sql.append("liv.codemp=v.codemp and liv.codfilial=v.codfilial and liv.codvenda=v.codvenda ");
		sql.append("where v.codemp=? and v.codfilial=? and v.tipovenda='V' and v.dtemitvenda between ? and ? and ");
		sql.append("tm.codemp=v.codemptm and tm.codfilial=v.codfilialtm and tm.codtipomov=v.codtipomov and ");
		sql.append("tm.fiscaltipomov='S' and ");
		sql.append("mn.codemp=tm.codempmn and mn.codfilial=tm.codfilialmn and mn.codmodnota=tm.codmodnota and ");
		sql.append("s.codemp=v.codempse and s.codfilial=v.codfilialse and s.serie=v.serie and ");
		sql.append("c.codemp=v.codempcl and c.codfilial=v.codfilialcl and c.codcli=v.codcli and ");
		sql.append("r.codempvd=v.codemp and r.codfilialvd=v.codfilial and r.codvenda=v.codvenda and r.tipovenda=v.tipovenda ");
		sql.append("group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16");
		sql.append("order by v.docvenda");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("VDVENDA"));
		ps.setDate(3, Funcoes.dateToSQLDate(dtini));
		ps.setDate(4, Funcoes.dateToSQLDate(dtfim));

		ResultSet rs = ps.executeQuery();
		SaidaVO saida = null;
		boolean readHeader = true;
		TraillerSaidaVO traillerSaida = null;

		while (rs.next()) {

			if (readHeader) {
				headerSaida();
				readHeader = false;
				traillerSaida = getTraillerSaida();
			}

			saida = new SaidaVO();
			saida.setDataLancamento(rs.getDate("DTEMITVENDA"));
			saida.setNumeroInicial(rs.getInt("DOCVENDA"));
			saida.setNumeroFinal(rs.getInt("DOCVENDA"));
			saida.setDataEmissao(rs.getDate("DTEMITVENDA"));
			saida.setModelo(rs.getInt("codmodnota"));
			saida.setSerie(rs.getString("SERIE"));
			saida.setSubSerie(null);
			
			saida.setCfop( Integer.valueOf( this.getCfopVenda(rs.getInt("codvenda"), rs.getString("tipovenda"))) );

			saida.setVariacaoCfop(1); // Se for 0 não não considera para recolhimento de tributos.
			saida.setClassificacao1(0); // Padrão do Cordilheira
			saida.setClassificacao2(0);
			saida.setCnpjDestinatario(rs.getString("cnpjcli"));
			saida.setCpfDestinatario(rs.getString("cpfcli"));
			saida.setValorNota(rs.getBigDecimal("VLRLIQVENDA") != null ? rs.getBigDecimal("VLRLIQVENDA") : new BigDecimal("0.00"));
			saida.setBasePIS(rs.getBigDecimal("vlrbasepis"));
			saida.setBaseCOFINS(rs.getBigDecimal("vlrbasecofins"));
			saida.setBaseCSLL(new BigDecimal("0.00"));
			saida.setBaseIRPJ(new BigDecimal("0.00"));

			StringBuilder sqlICMS = new StringBuilder();
			sqlICMS.append("select i.percicmsitvenda aliquota, sum(i.vlrbaseicmsitvenda) base, sum(i.vlricmsitvenda) valor ");
			sqlICMS.append("from vditvenda i ");
			sqlICMS.append("where i.codemp=? and i.codfilial=? and i.codvenda=? and i.tipovenda=?");
			sqlICMS.append("group by i.percicmsitvenda");

			PreparedStatement psICMS = con.prepareStatement(sqlICMS.toString());
			psICMS.setInt(1, Aplicativo.iCodEmp);
			psICMS.setInt(2, ListaCampos.getMasterFilial("VDITVENDA"));
			psICMS.setInt(3, rs.getInt("codvenda"));
			psICMS.setString(4, rs.getString("tipovenda"));

			ResultSet rsICMS = psICMS.executeQuery();

			saida.setBaseICMSa(new BigDecimal("0.00"));
			saida.setAliquotaICMSa(new BigDecimal("0.00"));
			saida.setValorICMSa(new BigDecimal("0.00"));
			saida.setBaseICMSb(new BigDecimal("0.00"));
			saida.setAliquotaICMSb(new BigDecimal("0.00"));
			saida.setValorICMSb(new BigDecimal("0.00"));
			saida.setBaseICMSc(new BigDecimal("0.00"));
			saida.setAliquotaICMSc(new BigDecimal("0.00"));
			saida.setValorICMSc(new BigDecimal("0.00"));
			saida.setBaseICMSd(new BigDecimal("0.00"));
			saida.setAliquotaICMSd(new BigDecimal("0.00"));
			saida.setValorICMSd(new BigDecimal("0.00"));

			for (int i = 0; i < 4 && rsICMS.next(); i++) {

				if (i == 0) {
					saida.setBaseICMSa(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					saida.setAliquotaICMSa(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					saida.setValorICMSa(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
				else if (i == 1) {
					saida.setBaseICMSb(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					saida.setAliquotaICMSb(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					saida.setValorICMSb(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
				else if (i == 2) {
					saida.setBaseICMSc(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					saida.setAliquotaICMSc(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					saida.setValorICMSc(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
				else if (i == 3) {
					saida.setBaseICMSd(rsICMS.getBigDecimal("base") != null ? rsICMS.getBigDecimal("base") : new BigDecimal("0.00"));
					saida.setAliquotaICMSd(rsICMS.getBigDecimal("aliquota") != null ? rsICMS.getBigDecimal("aliquota") : new BigDecimal("0.00"));
					saida.setValorICMSd(rsICMS.getBigDecimal("valor") != null ? rsICMS.getBigDecimal("valor") : new BigDecimal("0.00"));
				}
			}
			rsICMS.close();
			psICMS.close();

			saida.setValorICMSIsentas(new BigDecimal("0.00"));
			// saida.setValorICMSOutras( new BigDecimal( "0.00" ) );

			if (saida.getBaseICMSa().floatValue() == saida.getValorNota().floatValue()) {
				saida.setValorICMSOutras(new BigDecimal("0.00"));
			}
			else {
				saida.setValorICMSOutras(( saida.getValorNota().subtract(saida.getBaseICMSa().add(saida.getBaseICMSb()).add(saida.getBaseICMSc()).add(saida.getBaseICMSd())) ).abs());
			}

			saida.setValorIPI(rs.getBigDecimal("VLRIPIVENDA") != null ? rs.getBigDecimal("VLRIPIVENDA") : new BigDecimal("0.00"));

			if (saida.getValorIPI().floatValue() > 0) {
				saida.setBaseIPI(rs.getBigDecimal("VLRBASEIPIVENDA") != null ? rs.getBigDecimal("VLRBASEIPIVENDA") : new BigDecimal("0.00"));
			}
			else {
				saida.setBaseIPI(new BigDecimal("0.00"));
			}

			// System.out.println("BASE IPI:" + saida.getBaseIPI() + " VLR IPI:"
			// + saida.getValorIPI() + " DOC:" + saida.getNumeroInicial());

			saida.setValorIPIIsentas(new BigDecimal("0.00"));
			saida.setValorIPIOutras(new BigDecimal("0.00"));
			saida.setValorSubTributaria(new BigDecimal("0.00"));
			saida.setBaseSubTributaria(new BigDecimal("0.00"));
			saida.setValorICMSSubTributaria(new BigDecimal("0.00"));
			saida.setValorDiferidas(new BigDecimal("0.00"));
			saida.setBaseISS(new BigDecimal("0.00"));
			saida.setAliquotaISS(new BigDecimal("0.00"));
			saida.setValorISS(new BigDecimal("0.00"));
			saida.setValorISSIsentos(new BigDecimal("0.00"));
			saida.setValorIRRF(new BigDecimal("0.00"));
			saida.setObservacoesLivrosFiscais(null);
			saida.setEspecie(rs.getString("ESPECIETIPOMOV"));
			saida.setVendaAVista(rs.getDate("dtemitvenda").compareTo(rs.getDate("datarec")) == 0 ? "S" : "N");
			saida.setCfopSubTributaria(0);
			saida.setValorPISCOFINS(new BigDecimal("0.00"));
			saida.setModalidadeFrete(0);
			saida.setValorPIS(rs.getBigDecimal("vlrpis"));
			saida.setValorCOFINS(rs.getBigDecimal("vlrcofins"));
			saida.setValorCSLL(new BigDecimal("0.00"));
			saida.setDataRecebimento(rs.getDate("datarec"));
			saida.setOperacaoContabil(0);
			saida.setValorMateriais(new BigDecimal("0.00"));
			saida.setValorSubEmpreitada(new BigDecimal("0.00"));
			saida.setCodigoServico(0);
			saida.setClifor(0);
			saida.setIndentificadorExterior(null);

			emitente('C', rs.getInt("codcli"));

			saida.setSequencial(sequencial++);
			readrowsSaida.add(saida.toString());

			itensSaida(rs.getInt("codvenda"), rs.getString("tipovenda"));

			traillerSaida.setValorNota(traillerSaida.getValorNota().add(saida.getValorNota()));
			traillerSaida.setBasePIS(traillerSaida.getBasePIS().add(saida.getBasePIS()));
			traillerSaida.setBaseCOFINS(traillerSaida.getBaseCOFINS().add(saida.getBaseCOFINS()));
			traillerSaida.setBaseCSLL(traillerSaida.getBaseCSLL().add(saida.getBaseCSLL()));
			traillerSaida.setBaseIRPJ(traillerSaida.getBaseIRPJ().add(saida.getBaseIRPJ()));
			traillerSaida.setBaseICMSa(traillerSaida.getBaseICMSa().add(saida.getBaseICMSa()));
			traillerSaida.setValorICMSa(traillerSaida.getValorICMSa().add(saida.getValorICMSa()));
			traillerSaida.setBaseICMSb(traillerSaida.getBaseICMSb().add(saida.getBaseICMSb()));
			traillerSaida.setValorICMSb(traillerSaida.getValorICMSb().add(saida.getValorICMSb()));
			traillerSaida.setBaseICMSc(traillerSaida.getBaseICMSc().add(saida.getBaseICMSc()));
			traillerSaida.setValorICMSc(traillerSaida.getValorICMSc().add(saida.getValorICMSc()));
			traillerSaida.setBaseICMSd(traillerSaida.getBaseICMSd().add(saida.getBaseICMSd()));
			traillerSaida.setValorICMSd(traillerSaida.getValorICMSd().add(saida.getValorICMSd()));
			traillerSaida.setValorICMSIsentas(traillerSaida.getValorICMSIsentas().add(saida.getValorICMSIsentas()));
			traillerSaida.setValorICMSOutras(traillerSaida.getValorICMSOutras().add(saida.getValorICMSOutras()));
			traillerSaida.setBaseIPI(traillerSaida.getBaseIPI().add(saida.getBaseIPI()));
			traillerSaida.setValorIPI(traillerSaida.getValorIPI().add(saida.getValorIPI()));
			traillerSaida.setValorIPIIsentas(traillerSaida.getValorIPIIsentas().add(saida.getValorIPIIsentas()));
			traillerSaida.setValorIPIOutras(traillerSaida.getValorIPIOutras().add(saida.getValorIPIOutras()));
			traillerSaida.setValorMercadoriasSubTributaria(traillerSaida.getValorMercadoriasSubTributaria().add(saida.getValorSubTributaria()));
			traillerSaida.setBaseSubTributaria(traillerSaida.getBaseSubTributaria().add(saida.getBaseSubTributaria()));
			traillerSaida.setValorICMSSubTributarias(traillerSaida.getValorICMSSubTributarias().add(saida.getValorICMSSubTributaria()));
			traillerSaida.setValorDireridas(traillerSaida.getValorDireridas().add(saida.getValorDiferidas()));
			traillerSaida.setBaseISS(traillerSaida.getBaseISS().add(saida.getBaseISS()));
			traillerSaida.setValorISS(traillerSaida.getValorISS().add(saida.getValorISS()));
			traillerSaida.setValorISSIsentas(traillerSaida.getValorISSIsentas().add(saida.getValorISSIsentos()));
			traillerSaida.setValorIRRFISS(traillerSaida.getValorIRRFISS().add(saida.getValorIRRF()));
		}
		
		rs.close();
		ps.close();
		
		// Executando sql de parcelas
        ps = con.prepareStatement(sqlprazo.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("VDVENDA"));
		ps.setDate(3, Funcoes.dateToSQLDate(dtini));
		ps.setDate(4, Funcoes.dateToSQLDate(dtfim));
        rs = ps.executeQuery();
        PrazoVO prazovo = null;
		
        while (rs.next()) {
        	prazovo = new PrazoVO();
        	prazovo.setTipoParcela(rs.getString("TIPOPARCELA"));
        	prazovo.setNrFatura(rs.getInt("DOCREC"));
        	prazovo.setTipoTitulo(rs.getString("TIPOTITULO"));
        	prazovo.setDtVencimento(rs.getDate("DTVENCITREC"));
        	prazovo.setVlrParcela(rs.getBigDecimal("VLRITREC"));
			prazovo.setSequencial(sequencial++);
			readrowsSaida.add(prazovo.toString());
        }
        
		rs.close();
		ps.close();

		if (traillerSaida != null) {
			traillerSaida.setSequencial(sequencial++);
			readrowsSaida.add(traillerSaida.toString());
		}

		con.commit();
	}

	private void itensSaida(int venda, String tipovenda) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("I.CODITVENDA,");
		sql.append("I.CODPROD, P.REFPROD,");
		sql.append("I.QTDITVENDA,");
		sql.append("(I.QTDITVENDA * I.PRECOITVENDA) VALOR,");
		sql.append("I.VLRDESCITVENDA,");
		sql.append("I.VLRBASEICMSITVENDA,");
		sql.append("I.PERCICMSITVENDA,");
		sql.append("I.VLRICMSITVENDA, ");
		sql.append("I.VLRIPIITVENDA,");
		sql.append("I.PERCIPIITVENDA, ");
		sql.append("(CASE WHEN I.VLRICMSSTITVENDA>0 THEN I.PERCICMSITVENDA ELSE 0 END) PERCICMSSTITVENDA, ");
		sql.append("I.VLRICMSSTITVENDA, ");
		sql.append("I.CODNAT, ");
		sql.append("P.CODUNID, ");
		sql.append("COALESCE(LIV.VLRPIS, 0) VLRPIS, ");  
		sql.append("COALESCE(LIV.ALIQPIS, 0) ALIQPIS, "); 
		sql.append("COALESCE(LIV.VLRCOFINS, 0) VLRCOFINS, ");  
		sql.append("COALESCE(LIV.ALIQCOFINS, 0) ALIQCOFINS, ");  
		sql.append("COALESCE(LIV.VLRBASEPIS, 0) VLRBASEPIS, ");  
		sql.append("COALESCE(LIV.VLRBASECOFINS, 0) VLRBASECOFINS, ");  
		sql.append("COALESCE(LIV.CODSITTRIBPIS,0) CODSITTRIBPIS, ");  
		sql.append("COALESCE(LIV.CODSITTRIBCOF,0) CODSITTRIBCOF, ");  
	    sql.append("COALESCE(LIV.CODSITTRIBIPI,0) CODSITTRIBIPI ");  
		sql.append("FROM EQPRODUTO P, VDITVENDA I " ); 
		sql.append("LEFT OUTER JOIN LFITVENDA LIV  ON ");
		sql.append("LIV.CODEMP=I.CODEMP AND LIV.CODFILIAL=I.CODFILIAL AND LIV.CODVENDA=I.CODVENDA AND LIV.CODITVENDA = I.CODITVENDA ");
		sql.append("WHERE I.CODEMP=? AND I.CODFILIAL=? AND I.CODVENDA=? AND I.TIPOVENDA=? AND ");
		sql.append("P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD ");
		sql.append("GROUP BY 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24 ");
		sql.append("ORDER BY I.CODITVENDA");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("VDITVENDA"));
		ps.setInt(3, venda);
		ps.setString(4, tipovenda);

		ResultSet rs = ps.executeQuery();
		ItemSaidaVO itemSaida = null;

		while (rs.next()) {

			itemSaida = new ItemSaidaVO();
			itemSaida.setCodigoItem(rs.getInt("CODPROD"));
			itemSaida.setQuantidade(rs.getBigDecimal("QTDITVENDA") != null ? rs.getBigDecimal("QTDITVENDA") : new BigDecimal("0.00"));
			itemSaida.setValor(rs.getBigDecimal("VALOR"));
			itemSaida.setQuantidade2(null);
			itemSaida.setDesconto(rs.getBigDecimal("VLRDESCITVENDA") != null ? rs.getBigDecimal("VLRDESCITVENDA") : new BigDecimal("0.00"));
			itemSaida.setBaseICMS(rs.getBigDecimal("VLRBASEICMSITVENDA") != null ? rs.getBigDecimal("VLRBASEICMSITVENDA") : new BigDecimal("0.00"));
			itemSaida.setAliquotaICMS(rs.getBigDecimal("PERCICMSITVENDA") != null ? rs.getBigDecimal("PERCICMSITVENDA") : new BigDecimal("0.00"));
			itemSaida.setValorIPI(rs.getBigDecimal("VLRIPIITVENDA") != null ? rs.getBigDecimal("VLRIPIITVENDA") : new BigDecimal("0.00"));
			itemSaida.setAliquotaIPI(rs.getBigDecimal("PERCIPIITVENDA") != null ? rs.getBigDecimal("PERCIPIITVENDA") : new BigDecimal("0.00"));

			if (itemSaida.getValorIPI().floatValue() > 0) {
				itemSaida.setBaseIPI(rs.getBigDecimal("VALOR") != null ? rs.getBigDecimal("VALOR") : new BigDecimal("0.00"));
			}
			else {
				itemSaida.setBaseIPI(new BigDecimal("0.00"));
			}

			itemSaida.setIndentificacao(rs.getString("REFPROD"));
			itemSaida.setBaseICMSSubTributaria(null);
			itemSaida.setPercentualReducaoBaseICMS(null);
			itemSaida.setSituacaoTributaria(0);
			itemSaida.setSituacaoTributariaIPI(rs.getInt("CODSITTRIBIPI"));
			itemSaida.setSituacaoTributariaPIS(rs.getInt("CODSITTRIBPIS"));
			itemSaida.setBasePIS(rs.getBigDecimal("VLRBASEPIS"));
			itemSaida.setAliquotaPIS(rs.getBigDecimal("ALIQPIS"));
			itemSaida.setQuantidadeBasePIS(null);
			itemSaida.setValorAliquotaPIS(null);
			itemSaida.setValorPIS(rs.getBigDecimal("VLRPIS"));
			itemSaida.setSituacaoTributariaCOFINS(rs.getInt("CODSITTRIBCOF"));
			itemSaida.setBaseCOFINS(rs.getBigDecimal("VLRBASECOFINS"));
			itemSaida.setAliquotaCOFINS(rs.getBigDecimal("ALIQCOFINS"));
			itemSaida.setQuantidadeBaseCOFINS(null);
			itemSaida.setValorAliquotaCOFINS(null);
			itemSaida.setValorCOFINS(rs.getBigDecimal("VLRCOFINS"));
			itemSaida.setValorICMSSubTributaria(rs.getBigDecimal("VLRICMSSTITVENDA"));
			//Itens adicionado devido a novo layout de entradas.
			itemSaida.setAliquotaICMSSubTributaria(rs.getBigDecimal("PERCICMSSTITVENDA"));
			itemSaida.setValorICMS(rs.getBigDecimal("VLRICMSITVENDA"));
			itemSaida.setNaturezaItem(rs.getString("CODNAT"));
			itemSaida.setUnidade(rs.getString("CODUNID"));
			itemSaida.setCodServTelecom(0);
			itemSaida.setCodRecTelecom(0);
			//Até aqui.
			itemSaida.setSequencial(sequencial++);

			readrowsSaida.add(itemSaida.toString());
		}

		rs.close();
		ps.close();
	}

	private TraillerSaidaVO getTraillerSaida() {

		TraillerSaidaVO traillerSaida = new TraillerSaidaVO();

		traillerSaida.setValorNota(new BigDecimal("0.00"));
		traillerSaida.setBasePIS(new BigDecimal("0.00"));
		traillerSaida.setBaseCOFINS(new BigDecimal("0.00"));
		traillerSaida.setBaseCSLL(new BigDecimal("0.00"));
		traillerSaida.setBaseIRPJ(new BigDecimal("0.00"));
		traillerSaida.setBaseICMSa(new BigDecimal("0.00"));
		traillerSaida.setValorICMSa(new BigDecimal("0.00"));
		traillerSaida.setBaseICMSb(new BigDecimal("0.00"));
		traillerSaida.setValorICMSb(new BigDecimal("0.00"));
		traillerSaida.setBaseICMSc(new BigDecimal("0.00"));
		traillerSaida.setValorICMSc(new BigDecimal("0.00"));
		traillerSaida.setBaseICMSd(new BigDecimal("0.00"));
		traillerSaida.setValorICMSd(new BigDecimal("0.00"));
		traillerSaida.setValorICMSIsentas(new BigDecimal("0.00"));
		traillerSaida.setValorICMSOutras(new BigDecimal("0.00"));
		traillerSaida.setBaseIPI(new BigDecimal("0.00"));
		traillerSaida.setValorIPI(new BigDecimal("0.00"));
		traillerSaida.setValorIPIIsentas(new BigDecimal("0.00"));
		traillerSaida.setValorIPIOutras(new BigDecimal("0.00"));
		traillerSaida.setValorMercadoriasSubTributaria(new BigDecimal("0.00"));
		traillerSaida.setBaseSubTributaria(new BigDecimal("0.00"));
		traillerSaida.setValorICMSSubTributarias(new BigDecimal("0.00"));
		traillerSaida.setValorDireridas(new BigDecimal("0.00"));
		traillerSaida.setBaseISS(new BigDecimal("0.00"));
		traillerSaida.setValorISS(new BigDecimal("0.00"));
		traillerSaida.setValorISSIsentas(new BigDecimal("0.00"));
		traillerSaida.setValorIRRFISS(new BigDecimal("0.00"));

		return traillerSaida;
	}

	private void produtos() throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT P.CODPROD, P.DESCPROD, CL.CODNBM, P.CODUNID, P.PESOBRUTPROD, P.REFPROD, ");
		sql.append("(CASE WHEN TIPOPROD='P' THEN 1 ");
		sql.append("WHEN TIPOPROD='M' THEN 2 ");
		sql.append("WHEN TIPOPROD='F' THEN 5 ");
		sql.append("ELSE 99 END) TIPPROD ");
		sql.append("FROM EQPRODUTO P, LFCLFISCAL CL ");
		sql.append("WHERE P.CODEMP=? AND P.CODFILIAL=? AND ");
		sql.append("CL.CODEMP=P.CODEMPFC AND CL.CODFILIAL=P.CODFILIALFC AND CL.CODFISC=P.CODFISC ");
		sql.append("ORDER BY P.CODPROD");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setInt(1, Aplicativo.iCodEmp);
		ps.setInt(2, ListaCampos.getMasterFilial("EQPRODUTO"));

		ResultSet rs = ps.executeQuery();
		ItemVO item = null;

		while (rs.next()) {

			item = new ItemVO();
			item.setCodigo(rs.getInt("CODPROD"));
			item.setDescricao(rs.getString("DESCPROD"));
			item.setNcm(rs.getString("CODNBM") != null ? rs.getString("CODNBM").trim().replaceAll("\\.", "") : null);
			item.setUnidade(rs.getString("CODUNID"));
			item.setPeso(rs.getBigDecimal("PESOBRUTPROD"));
			item.setReferencia(rs.getString("REFPROD"));
			item.setTipoProduto(rs.getInt("TIPPROD"));
			item.setCstIcmsEntrada(0);
			item.setCstIpiEntrada(0);
			item.setCstIcmsSaida(0);
			item.setCstIpiSaida(0);
			item.setUnidade2("");
			item.setAliquotaICMS(new BigDecimal("0.00"));
			item.setAliquotaIPI(new BigDecimal("0.00"));
			item.setSequencial(sequencial++);
			
			readrowsItem.add(item.toString());
		}

		rs.close();
		ps.close();
	}
	
	private void notaItens()throws Exception{
		StringBuilder sqlFilial = new StringBuilder();
		sqlFilial.append("select f.razfilial, f.cnpjfilial from sgfilial f where f.codemp=? and f.codfilial=?");

		PreparedStatement psFilial = con.prepareStatement(sqlFilial.toString());
		psFilial.setInt(1, Aplicativo.iCodEmp);
		psFilial.setInt(2, ListaCampos.getMasterFilial("sgfilial"));
		ResultSet rsFilial = psFilial.executeQuery();
		String cnpj = null;
		if (rsFilial.next()) {
			cnpj = rsFilial.getString("cnpjfilial");
		}
		rsFilial.close();
		psFilial.close();
		
		//Header
		StringBuilder header = new StringBuilder();
		header.append("0");
		header.append(format(new Date()));
		header.append(format(Funcoes.setMascara(cnpj, "##.###.###/####-##"), 18));
		header.append(format("", 447));
		header.append(format("", 20));
		header.append(format(new Integer(1), 6));
		readrowsNotaItem.add(header.toString());
		
		
		//Saidas
		StringBuilder sqlSaida = new StringBuilder();
		sqlSaida.append("select v.codvenda, v.tipovenda, iv.coditvenda, tm.codmodnota, tm.serie, coalesce(vc.cnpjcli, vc.cpfcli) as cpfcnpj, ");
		sqlSaida.append("iv.vlrproditvenda, iv.qtditvenda, iv.vlrdescitvenda, iv.vlrbaseicmsitvenda, iv.vlripiitvenda, ");
		sqlSaida.append("iv.refprod, iv.vlrbaseipiitvenda, iv.percicmsitvenda, iv.percipiitvenda ");
		sqlSaida.append("from vditvenda iv ");
		sqlSaida.append("inner join vdvenda v on (v.codvenda = iv.codvenda and v.codemp = iv.codemp and v.codfilial = iv.codfilial) ");
		sqlSaida.append("inner join lfitvenda lfitv on (lfitv.codemp = v.codemp and lfitv.codfilial = v.codfilial and lfitv.codvenda = v.codvenda and lfitv.coditvenda = iv.coditvenda) ");
		sqlSaida.append("inner join vdcliente vc on (vc.codemp = iv.codemp and vc.codfilial = iv.codfilial and vc.codcli = v.codcli) ");
		sqlSaida.append("inner join eqtipomov tm on (tm.codemp = v.codemp and tm.codfilial = v.codfilial and v.codtipomov = tm.codtipomov) ");
		sqlSaida.append("where v.dtemitvenda between ? and ? ");
		sqlSaida.append("and v.codemp = ? and v.codfilial = ? ");
		sqlSaida.append("and v.tipovenda = 'V' ");

		
		PreparedStatement psSaida = con.prepareStatement(sqlSaida.toString());
		psSaida.setDate(1, Funcoes.dateToSQLDate(dtini));
		psSaida.setDate(2, Funcoes.dateToSQLDate(dtfim));
		psSaida.setInt(3, Aplicativo.iCodEmp);
		psSaida.setInt(4, ListaCampos.getMasterFilial("EQPRODUTO"));

		ResultSet rsSaida = psSaida.executeQuery();
		
		while (rsSaida.next()) {
			NotaItemVO notaItem = new NotaItemVO();

			notaItem.setTipoNota("S");
			
			notaItem.setCfop( this.getCfopVenda(rsSaida.getInt("codvenda"), rsSaida.getString("tipovenda")) );
			notaItem.setNumeroNota(rsSaida.getInt("codVenda"));
			notaItem.setModelo(rsSaida.getString("codmodnota"));
			notaItem.setSerie(rsSaida.getString("serie"));
			notaItem.setCpfCnpj(rsSaida.getString("cpfcnpj"));
			notaItem.setCodigo(rsSaida.getInt("coditvenda"));
			notaItem.setQuantidade(rsSaida.getBigDecimal("qtditvenda"));
			notaItem.setDesconto(rsSaida.getBigDecimal("vlrdescitvenda"));
			notaItem.setIdentificacao(rsSaida.getString("refprod"));
			notaItem.setBaseIPI(rsSaida.getBigDecimal("vlrbaseipiitvenda"));
			notaItem.setValorIPI(rsSaida.getBigDecimal("vlripiitvenda"));
			notaItem.setBaseICMS(rsSaida.getBigDecimal("vlrbaseicmsitvenda"));
			notaItem.setAliquotaICMS(rsSaida.getBigDecimal("percicmsitvenda"));
			notaItem.setAliquotaIPI(rsSaida.getBigDecimal("percipiitvenda"));
			
			notaItem.setSitTributaria(0);
			notaItem.setSitTributariaIPI(0);
			notaItem.setSitTributariaPIS(0);
			notaItem.setSitTributariaCofins(0);
			
			notaItem.setSequencial(sequencial++);
			
			readrowsNotaItem.add(notaItem.toString());
		}
		
		rsSaida.close();
		psSaida.close();
		
		//Entradas
		StringBuilder sqlEntrada = new StringBuilder();
		sqlEntrada.append("select c.codcompra, ic.coditcompra, tm.codmodnota, tm.serie, coalesce(cf.cnpjfor, cf.cpffor) as cpfcnpj, ");
		sqlEntrada.append("ic.vlrproditcompra, ic.qtditcompra, ic.vlrdescitcompra, ic.vlrbaseicmsitcompra, ic.vlripiitcompra, ");
		sqlEntrada.append("ic.refprod, ic.vlrbaseipiitcompra, ic.percicmsitcompra, ic.percipiitcompra ");
		sqlEntrada.append("from cpitcompra ic ");
		sqlEntrada.append("inner join cpcompra c on (c.codcompra = ic.codcompra and c.codemp = ic.codemp and c.codfilial = ic.codfilial) ");
		sqlEntrada.append("inner join lfitcompra lfitc on (lfitc.codemp = c.codemp and lfitc.codfilial = c.codfilial and lfitc.codcompra = c.codcompra and lfitc.coditcompra = ic.coditcompra) ");
		sqlEntrada.append("inner join cpforneced cf on (cf.codemp = ic.codemp and cf.codfilial = ic.codfilial and cf.codfor = c.codfor) ");
		sqlEntrada.append("inner join eqtipomov tm on (tm.codemp = c.codemp and tm.codfilial = c.codfilial and c.codtipomov = tm.codtipomov) ");
		sqlEntrada.append("where c.dtemitcompra between ? and ? ");
		sqlEntrada.append("and c.codemp = ? and c.codfilial = ? ");

		
		PreparedStatement psEntrada = con.prepareStatement(sqlEntrada.toString());
		psEntrada.setDate(1, Funcoes.dateToSQLDate(dtini));
		psEntrada.setDate(2, Funcoes.dateToSQLDate(dtfim));
		psEntrada.setInt(3, Aplicativo.iCodEmp);
		psEntrada.setInt(4, ListaCampos.getMasterFilial("EQPRODUTO"));

		ResultSet rsEntrada = psEntrada.executeQuery();
		
		while (rsEntrada.next()) {
			NotaItemVO notaItem = new NotaItemVO();

			notaItem.setTipoNota("E");
			
			notaItem.setCfop( this.getCfopCompra(rsEntrada.getInt("codcompra")) );
			notaItem.setNumeroNota(rsEntrada.getInt("codcompra"));
			notaItem.setModelo(rsEntrada.getString("codmodnota"));
			notaItem.setSerie(rsEntrada.getString("serie"));
			notaItem.setCpfCnpj(rsEntrada.getString("cpfcnpj"));
			notaItem.setCodigo(rsEntrada.getInt("coditcompra"));
			notaItem.setQuantidade(rsEntrada.getBigDecimal("qtditcompra"));
			notaItem.setDesconto(rsEntrada.getBigDecimal("vlrdescitcompra"));
			notaItem.setIdentificacao(rsEntrada.getString("refprod"));
			notaItem.setBaseIPI(rsEntrada.getBigDecimal("vlrbaseipiitcompra"));
			notaItem.setValorIPI(rsEntrada.getBigDecimal("vlripiitcompra"));
			notaItem.setBaseICMS(rsEntrada.getBigDecimal("vlrbaseicmsitcompra"));
			notaItem.setAliquotaICMS(rsEntrada.getBigDecimal("percicmsitcompra"));
			notaItem.setAliquotaIPI(rsEntrada.getBigDecimal("percipiitcompra"));
			
			notaItem.setSitTributaria(0);
			notaItem.setSitTributariaIPI(0);
			notaItem.setSitTributariaPIS(0);
			notaItem.setSitTributariaCofins(0);
			
			notaItem.setSequencial(sequencial++);
			
			readrowsNotaItem.add(notaItem.toString());
		}
		
		rsEntrada.close();
		psEntrada.close();
	}
	
	private String getCfopVenda(Integer codVenda, String tipoVenda) throws SQLException{
		StringBuilder sqlCFOP = new StringBuilder();
		sqlCFOP.append("select iv.codnat from vditvenda iv ");
		sqlCFOP.append("where iv.codemp=? and iv.codfilial=? and iv.codvenda=? and iv.tipovenda=? ");
		sqlCFOP.append("order by iv.coditvenda");

		PreparedStatement psCFOP = con.prepareStatement(sqlCFOP.toString());
		psCFOP.setInt(1, Aplicativo.iCodEmp);
		psCFOP.setInt(2, ListaCampos.getMasterFilial("VDVENDA"));
		psCFOP.setInt(3, codVenda);
		psCFOP.setString(4, tipoVenda);

		ResultSet rsCFOP = psCFOP.executeQuery();

		String cfop = null;
		if (rsCFOP.next()) {
			cfop = rsCFOP.getString("codnat");
		}
		rsCFOP.close();
		psCFOP.close();
		
		return cfop;
	}
	
	private String getCfopCompra(Integer codCompra) throws SQLException{
		StringBuilder sqlCFOP = new StringBuilder();
		sqlCFOP.append("select ic.codnat from cpitcompra ic ");
		sqlCFOP.append("where ic.codemp=? and ic.codfilial=? and ic.codcompra=? order by ic.coditcompra");

		PreparedStatement psCFOP = con.prepareStatement(sqlCFOP.toString());
		psCFOP.setInt(1, Aplicativo.iCodEmp);
		psCFOP.setInt(2, ListaCampos.getMasterFilial("CPITCOMPRA"));
		psCFOP.setInt(3, codCompra);
		
		ResultSet rsCFOP = psCFOP.executeQuery();

		String cfop = null;
		if (rsCFOP.next()) {
			cfop = rsCFOP.getString("codnat");
		}
		rsCFOP.close();
		psCFOP.close();
		
		return cfop;
	}

	public static String format(String text, int tam) {

		if (text == null) {
			text = "";
		}

		return Funcoes.adicionaEspacos(text, tam);
	}

	public static String format(BigDecimal value, int size, int decimal) {

		BigDecimal valueTmp = null;

		if (value == null) {
			value = new BigDecimal("0.00");
		}
		valueTmp = value.setScale(decimal, BigDecimal.ROUND_HALF_UP);

		return StringFunctions.strZero(String.valueOf(valueTmp).replace(".", ""), size);
	}

	public static String format(int value, int size) {

		return StringFunctions.strZero(String.valueOf(value), size);
	}

	public static String format(Date date) {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		StringBuffer result = new StringBuffer();
		result.append(StringFunctions.strZero(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2));
		result.append(StringFunctions.strZero(String.valueOf(cal.get(Calendar.MONTH) + 1), 2));
		result.append(StringFunctions.strZero(String.valueOf(cal.get(Calendar.YEAR)), 4));

		return result.toString();
	}
}
