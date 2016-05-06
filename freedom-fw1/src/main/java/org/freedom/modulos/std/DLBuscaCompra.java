/**
 * @version 15/08/2009 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 *  
 * Pacote: org.freedom.modulos.std <BR>
 * Classe: @(#)DLBuscaCompra.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 *
 * Tela para busca de compras para referenciar à devoluções.
 */

package org.freedom.modulos.std;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLBuscaCompra extends FFDialogo implements TabelaSelListener {

	private static final long serialVersionUID = 1L;

	public JTablePad tab = new JTablePad();

	private JScrollPane spnCentro = new JScrollPane(tab);

	private JPanelPad pnCab = new JPanelPad(new Dimension(500, 65));

	private Integer coditcompra = new Integer(0);

	private Integer codcompra = new Integer(0);

	private Integer codvenda = new Integer(0);

	private Integer codfor = null;

	private Integer codempfr = null;

	private Integer codfilialfr = null;

	private boolean bRet = false;

	private ListaCampos lcItens = null;

	private ListaCampos lcProd = null;

	private JTextFieldPad txtDataIni = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtDataFim = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JButtonPad btExec = new JButtonPad(Icone.novo("btExecuta.png"));

	public DLBuscaCompra(ListaCampos lcItens, ListaCampos lcProd, Integer codvenda, DbConnection con) {

		setTitulo("Selecione um íten de compra para devolução");
		setAtribos(550, 360);
		setResizable(true);

		setConexao(con);

		this.lcItens = lcItens;
		this.lcProd = lcProd;
		this.codvenda = codvenda;

		setAtribos(575, 260);

		c.add(pnCab, BorderLayout.NORTH);
		c.add(spnCentro, BorderLayout.CENTER);

		tab.adicColuna("Data");
		tab.adicColuna("Doc");
		tab.adicColuna("Cód.Cp.");
		tab.adicColuna("Cód.It.Cp.");
		tab.adicColuna("Lote");
		tab.adicColuna("Qtd.");
		tab.adicColuna("Preço");
		tab.adicColuna("Qtd.Dev.");

		tab.setTamColuna(75, 0);
		tab.setTamColuna(60, 1);
		tab.setTamColuna(75, 2);
		tab.setTamColuna(50, 3);
		tab.setTamColuna(50, 4);
		tab.setTamColuna(90, 5);
		tab.setTamColuna(70, 6);
		tab.setTamColuna(70, 7);

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder(BorderFactory.createTitledBorder("Período"));
		pnPeriodo.adic(new JLabelPad("De:"), 4, 0, 20, 20);
		pnPeriodo.adic(txtDataIni, 25, 0, 75, 20);
		pnPeriodo.adic(new JLabelPad("Até:"), 105, 0, 25, 20);
		pnPeriodo.adic(txtDataFim, 130, 0, 75, 20);

		pnCab.adic(pnPeriodo, 4, 4, 220, 50);
		pnCab.adic(btExec, 224, 12, 30, 40);

		Date dtRef = new Date();

		txtDataIni.setVlrDate(Funcoes.subtraiMes(dtRef, 1));
		txtDataFim.setVlrDate(dtRef);

		tab.addTabelaSelListener(this);
		tab.addKeyListener(this);

		buscaForneced();
		buscaCompras();

	}

	public HashMap<String, Integer> getValor() {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		try {
			ret.put("codcompra", codcompra);
			ret.put("coditcompra", coditcompra);
		}
		catch (Exception e) {
			Funcoes.mensagemErro(null, "Erro ao selecionar ítem de compra!");
		}
		finally {
			bRet = false;
			codcompra = null;
			coditcompra = null;
		}
		return ret;
	}

	// public void setValor(Object oVal) {}

	public boolean buscaCompras() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		bRet = false;
		StringBuilder sql = new StringBuilder();

		sql.append("select cp.doccompra, cp.dtemitcompra, ic.codcompra, ic.coditcompra, ic.codlote , ic.qtditcompra, ic.precoitcompra, ");
		sql.append("coalesce((select sum(dv.qtddev) from cpdevolucao dv where dv.codemp=ic.codemp ");
		sql.append("and dv.codfilial=ic.codfilial and dv.codcompra=ic.codcompra and dv.coditcompra=ic.coditcompra),0) qtddev ");
		sql.append("from ");
		sql.append("cpitcompra ic ");
		sql.append("left outer join cpcompra cp on ");
		sql.append("cp.codemp=ic.codemp and cp.codfilial=ic.codfilial and cp.codcompra=ic.codcompra ");
		sql.append("where ");
		sql.append("cp.codempfr=? and cp.codfilialfr=? and cp.codfor=? and ");
		sql.append("ic.codemppd=? and ic.codfilialpd=? and ic.codprod=? ");
		sql.append("order by cp.dtemitcompra desc");

		try {
			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, codempfr);
			ps.setInt(2, codfilialfr);
			ps.setInt(3, codfor.intValue());
			ps.setInt(4, lcProd.getCodEmp());
			ps.setInt(5, lcProd.getCodFilial());
			ps.setInt(6, ( lcProd.getCampo("codprod").getVlrInteger() ).intValue());

			tab.limpa();

			rs = ps.executeQuery();

			int irow = 0;
			int icol = 0;

			while (rs.next()) {

				tab.adicLinha();
				tab.setValor(rs.getString("dtemitcompra") != null ? Funcoes.dateToStrDate(rs.getDate("dtemitcompra")) : "", irow, icol++);
				tab.setValor(rs.getString("doccompra") != null ? rs.getString("doccompra") : "", irow, icol++);
				tab.setValor(rs.getString("codcompra") != null ? rs.getString("codcompra") : "", irow, icol++);
				tab.setValor(rs.getString("coditcompra") != null ? rs.getString("coditcompra") : "", irow, icol++);
				tab.setValor(rs.getString("codlote") != null ? rs.getString("codlote") : "", irow, icol++);
				tab.setValor(rs.getString("qtditcompra") != null ? Funcoes.strDecimalToStrCurrency(13, 2, rs.getString("qtditcompra")) : "", irow, icol++);
				tab.setValor(rs.getString("precoitcompra") != null ? Funcoes.strDecimalToStrCurrency(13, 2, rs.getString("precoitcompra")) : "", irow, icol++);
				tab.setValor(rs.getString("qtddev") != null ? Funcoes.strDecimalToStrCurrency(13, 2, rs.getString("qtddev")) : "", irow, icol++);

				irow++;
				icol = 0;
			}

			if (irow > 0) {
				tab.requestFocus();
				tab.setLinhaSel(0);
			}

			rs.close();
			ps.close();
			con.commit();

		}
		catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro ao buscar filiais almoxarifados e saldos!\n" + err.getMessage(), true, con, err);
			err.printStackTrace();
		}
		finally {
			ps = null;
			rs = null;

		}
		return bRet;
	}

	public void actionPerformed(ActionEvent evt) {
		// getValores();
		super.actionPerformed(evt);
	}

	public synchronized void valorAlterado(TabelaSelEvent tsevt) {
	}

	public void keyPressed(KeyEvent kevt) {
		/*
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (tab.getNumLinhas() > 0 && btOK.isEnabled()) {
				btOK.doClick();
			}
			else if (!btOK.isEnabled()) {
				if (tab.getLinhaSel() == tab.getNumLinhas() - 1) {
					tab.setLinhaSel(tab.getNumLinhas() - 2);
				}
				else {
					tab.setLinhaSel(tab.getLinhaSel() - 1);
				}
			}
		}
		else if (kevt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			btCancel.doClick();
		}
		*/
		
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
				btOK.doClick();
		}
		
	}

	public void ok() {
		codcompra = new Integer(Integer.parseInt(tab.getValueAt(tab.getLinhaSel(), 2).toString()));
		coditcompra = new Integer(Integer.parseInt(tab.getValueAt(tab.getLinhaSel(), 3).toString()));
		super.ok();
	}

	private void buscaForneced() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select cf.codempfr, cf.codfilialfr, cf.codfor ");
		sql.append("from vdvenda vd left outer join eqclifor cf on ");
		sql.append("cf.codemp=vd.codempcl and cf.codfilial=vd.codfilialcl and cf.codcli=cf.codcli ");
		sql.append("where vd.codemp=? and vd.codfilial=? and vd.codvenda=? and vd.tipovenda='V' ");

		try {
			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, lcItens.getCodEmp());
			ps.setInt(2, lcItens.getCodFilial());
			ps.setInt(3, codvenda.intValue());

			rs = ps.executeQuery();

			if (rs.next()) {
				codempfr = rs.getInt("codempfr");
				codfilialfr = rs.getInt("codfilialfr");
				codfor = rs.getInt("codfor");
			}
			else {
				Funcoes.mensagemInforma(null, "Não existe fornecedore vinculado ao cliente.");
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
