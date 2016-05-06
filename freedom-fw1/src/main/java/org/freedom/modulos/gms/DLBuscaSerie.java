/**
 * @version 23/02/2004 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 *
 * Projeto: Freedom <BR>
 *  
 * Pacote: org.freedom.modulos.std <BR>
 * Classe: @(#)DLBuscaEstoq.java <BR>
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
 * Tela para busca de saldos de estoque em vários almoxarifados.
 */

package org.freedom.modulos.gms;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;

import org.freedom.acao.TabelaSelEvent;
import org.freedom.acao.TabelaSelListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.dialog.DLF3;

public class DLBuscaSerie extends DLF3 implements TabelaSelListener {

	private static final long serialVersionUID = 1L;

	private StringBuilder sql = null;
	private ListaCampos lcItens = null;
	private ListaCampos lcProd = null;
	private ListaCampos lcSerie = null;
	private String numserie = null;
	boolean bRet = false;
	boolean salvar = false;
	private ImageIcon imgBaixa = Icone.novo("clPago.gif");
	private ImageIcon imgVisualiza = Icone.novo("clVencido.gif");
	private ImageIcon imgPadrao = Icone.novo("clPagoParcial.gif");
	// private ImageIcon imgColuna = null;
	public int iPadrao = 0;
	Vector<?> vLinhasProibidas = new Vector<Object>();

	public DLBuscaSerie(ListaCampos lcItens, ListaCampos lcSerie, ListaCampos lcProd, DbConnection con, String sCol, boolean salvar) {

		imgBaixa.setDescription("S");
		imgVisualiza.setDescription("N");
		imgPadrao.setDescription("P");

		setAtribos(575, 260);

		this.lcItens = lcItens;
		this.lcSerie = lcSerie;
		this.lcProd = lcProd;
		this.salvar = salvar;

		setConexao(con);

		tab.adicColuna("Num.Série");
		tab.adicColuna("Dt.Fabric.");
		tab.adicColuna("Dt.Validade");
		tab.adicColuna("Observações");

		tab.setTamColuna(200, 0);
		tab.setTamColuna(60, 1);
		tab.setTamColuna(60, 2);
		tab.setTamColuna(200, 3);

		tab.addTabelaSelListener(this);

		setTitulo("Números de série deste produto");

		tab.addKeyListener(this);
		
		// Evitando que o ENTER no grid simule o duplo click
		InputMap im =  tab.getInputMap();
		ActionMap am =  tab.getActionMap();
	
		im.clear();
		am.clear();
		
		Action enterKey = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				try	{					
					acaoOk();
				}
				catch (Exception ex){
					ex.printStackTrace();
				}					
			}
		};

		

	}

	public int getLinhaPadrao() {
		return iPadrao;
	}

	public Object getValor() {
		if (lcItens != null) {
			if (lcItens.getCampo("numserie") != null && this.OK)
				if (numserie != null) {
					lcItens.getCampo("numserie").setVlrString(numserie);
					lcSerie.carregaDados();
					if (salvar) {
						lcItens.post();
					}
				}
		}
		else
			System.out.println("Lista Campos nulo no busca serie!!!!");

		bRet = false;
		numserie = null;
		return oRetVal;
	}

	public void setValor(Object oVal) {
	}

	public boolean setValor(Object oVal, String sTipo) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		// String sImgColuna = null;
		bRet = false;

		sql = new StringBuilder();
		sql.append("select numserie, dtfabricserie, dtvalidserie, obsserie from eqserie ");
		sql.append("where codemp=? and codfilial=? and codprod=?");

		try {
			ps = con.prepareStatement(sql.toString());

			ps.setInt(1, lcProd.getCodEmp());
			ps.setInt(2, lcProd.getCodFilial());
			ps.setInt(3, ( lcProd.getCampo("codprod").getVlrInteger() ).intValue());

			tab.limpa();

			rs = ps.executeQuery();

			int iCont = 0;

			while (rs.next()) {

				tab
						.adicLinha(new Object[] { rs.getString("numserie"), rs.getDate("dtfabricserie") != null ? StringFunctions.sqlDateToStrDate(rs.getDate("dtfabricserie")) : "",
								rs.getDate("dtvalidserie") != null ? StringFunctions.sqlDateToStrDate(rs.getDate("dtvalidserie")) : "",
								rs.getString("obsserie") != null ? rs.getString("obsserie").trim() : "" });

				iCont++;

				if (iCont > 0)
					bRet = true;
				else
					bRet = false;

			}

			rs.close();
			ps.close();
			con.commit();

			if (( bRet ) && ( lcItens.getStatus() == 2 )) {
				tab.requestFocus();
				tab.setLinhaSel(iPadrao);
				setVisible(true);
			}

		}
		catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro ao buscar series do produto!\n" + err.getMessage(), true, con, err);
			err.printStackTrace();
		}
		finally {
			ps = null;
			rs = null;
			// sImgColuna = null;
		}
		return bRet;
	}

	public void actionPerformed(ActionEvent evt) {
		getValores();
		super.actionPerformed(evt);
	}

	public synchronized void valorAlterado(TabelaSelEvent tsevt) {
		try {
			if (tsevt.getTabela() == tab)
				getValores();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void acaoOk() {
		if (( tab.getNumLinhas() > 0 ) && ( tab.getLinhaSel() >= 0 )) {
			
			tab.setLinhaSel( tab.getLinhaSel() - 1);
			
		}
				
		btOK.doClick();
	}
	
	public void keyPressed(KeyEvent kevt) {
		/*
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (tab.getNumLinhas() > 0 && btOK.isEnabled())
				btOK.doClick();
			else if (!btOK.isEnabled()) {
				if (tab.getLinhaSel() == tab.getNumLinhas() - 1)
					tab.setLinhaSel(tab.getNumLinhas() - 2);
				else
					tab.setLinhaSel(tab.getLinhaSel() - 1);
			}
		}
		else if (kevt.getKeyCode() == KeyEvent.VK_ESCAPE)
			btCancel.doClick();
		*/	
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			acaoOk();
		}
			
	}

	public void ok() {
		numserie = tab.getValueAt(tab.getLinhaSel(), 0).toString();
		super.ok();
	}

	public void getValores() {
		if (tab.getNumLinhas() > 0) {
			if (bRet) {
				if (this.isVisible()) {
					// Implemnetar futuramente para verificar se lote pode ser
					// selecionado....
					/*
					 * if(((ImageIcon)tab.getValueAt(tab.getLinhaSel(),5)).
					 * getDescription().equals("N")) btOK.setEnabled(false);
					 * else btOK.setEnabled(true);
					 */
					btOK.setEnabled(true);
				}
			}
		}
	}

}
