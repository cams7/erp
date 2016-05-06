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

package org.freedom.modulos.std;

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
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.dialog.DLF3;
import org.freedom.library.swing.frame.Aplicativo;

public class DLBuscaEstoq extends DLF3 implements TabelaSelListener {

	private static final long serialVersionUID = 1L;
	private String sSQL = null;
	private ListaCampos lcItens = null;
	private ListaCampos lcAlmox = null;
	private ListaCampos lcProd = null;
	Integer iCodAlmox = new Integer(0);
	boolean bRet = false;
	private ImageIcon imgBaixa = Icone.novo("clPago.gif");
	private ImageIcon imgVisualiza = Icone.novo("clVencido.gif");
	private ImageIcon imgPadrao = Icone.novo("clPagoParcial.gif");
	private ImageIcon imgColuna = null;
	public int iPadrao = 0;
	Vector<?> vLinhasProibidas = new Vector<Object>();

	public DLBuscaEstoq(ListaCampos lc1, ListaCampos lc2, ListaCampos lc3, DbConnection con, String sCol) {

		imgBaixa.setDescription("S");
		imgVisualiza.setDescription("N");
		imgPadrao.setDescription("P");

		setAtribos(575, 260);

		lcItens = lc1;
		lcAlmox = lc2;
		lcProd = lc3;

		setConexao(con);

		tab.adicColuna("Cd.Filial");
		tab.adicColuna("Nome da filial");
		tab.adicColuna("Cd.Almox.");
		tab.adicColuna("Nome Almoxarifado");
		tab.adicColuna("Saldo");
		tab.adicColuna("");
		tab.setTamColuna(60, 0);
		tab.setTamColuna(160, 1);
		tab.setTamColuna(60, 2);
		tab.setTamColuna(160, 3);
		tab.setTamColuna(90, 4);
		tab.setTamColuna(20, 5);
		tab.addTabelaSelListener(this);

		setTitulo("Saldo do produto nos almoxarifados");
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
		if (lcAlmox != null) {
			if (lcAlmox.getCampo("codalmox") != null && this.OK)
				if (iCodAlmox != null)
					lcAlmox.getCampo("codalmox").setVlrInteger(iCodAlmox);
		}
		else
			System.out.println("Lista Campos nulo no busca Estoq!!!!");

		bRet = false;
		iCodAlmox = null;
		return oRetVal;
	}

	public void setValor(Object oVal) {
	}

	public boolean setValor(Object oVal, String sTipo) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sImgColuna = null;
		bRet = false;

		sSQL = "SELECT A.CODFILIAL,F.NOMEFILIAL,A.CODALMOX,A.DESCALMOX, " 
				+ "(SELECT SP.SLDLIQPROD FROM EQSALDOPROD SP WHERE " 
				+ "SP.CODEMPAX=A.CODEMP AND SP.CODFILIALAX=A.CODFILIAL AND "
				+ "SP.CODALMOX=A.CODALMOX AND " + "SP.CODEMP=? AND SP.CODFILIAL=? AND SP.CODPROD=?), " 
				+ "(SELECT AF.BAIXAESTOQAF FROM EQALMOXFILIAL AF "
				+ "WHERE AF.CODEMP=A.CODEMP AND AF.CODFILIAL=A.CODFILIAL AND " 
				+ "AF.CODALMOX=A.CODALMOX AND AF.CODEMPAF=F.CODEMP AND " 
				+ "AF.CODFILIALAF=F.CODFILIAL), PD.CODALMOX "
				+ "FROM EQPRODUTO PD, EQALMOX A, SGEMPRESA E, SGFILIAL F " 
				+ "WHERE PD.CODEMP=A.CODEMP AND PD.CODFILIAL=A.CODFILIAL AND PD.CODPROD=? AND "
				+ "E.CODEMP=A.CODEMP AND A.CODEMP=? AND A.CODFILIAL=? AND " 
				+ "F.CODEMP=A.CODEMP AND F.CODFILIAL=A.CODFILIAL AND "
				+ "(E.MULTIALMOXEMP='N' OR EXISTS(SELECT CODALMOX FROM EQALMOXFILIAL AF " 
				+ "WHERE AF.CODEMP=A.CODEMP AND AF.CODFILIAL=A.CODFILIAL "
				+ "AND AF.CODALMOX=A.CODALMOX AND AF.CODEMPAF=? AND AF.CODFILIALAF=?)) "
				+ "ORDER BY A.CODFILIAL, A.CODALMOX ";

		try {
			ps = con.prepareStatement(sSQL);

			ps.setInt(1, lcProd.getCodEmp());
			ps.setInt(2, lcProd.getCodFilial());
			ps.setInt(3, ( lcProd.getCampo("codprod").getVlrInteger() ).intValue());
			ps.setInt(4, ( lcProd.getCampo("codprod").getVlrInteger() ).intValue());
			ps.setInt(5, Aplicativo.iCodEmp);
			ps.setInt(6, ListaCampos.getMasterFilial("EQALMOX"));
			ps.setInt(7, Aplicativo.iCodEmp);
			ps.setInt(8, ListaCampos.getMasterFilial("EQALMOXFILIAL"));

			tab.limpa();

			rs = ps.executeQuery();
			int iCont = 0;
			while (rs.next()) {
				if (iCont == 0)
					if (lcAlmox != null) {
						if (lcAlmox.getCampo("codalmox") != null) {
							if (lcAlmox.getCampo("codalmox").getVlrInteger().intValue() == 0)
								lcAlmox.getCampo("codalmox").setVlrInteger(new Integer(rs.getInt(3)));
						}
					}
				sImgColuna = rs.getString(6);
				if (sImgColuna != null) {
					if (sImgColuna.equals("S"))
						imgColuna = imgBaixa;
					else
						imgColuna = imgVisualiza;
				}
				else
					imgColuna = imgBaixa;

				if (rs.getString(3).equals(rs.getString(7))) {
					imgColuna = imgPadrao;
					iPadrao = iCont;
				}

				tab.adicLinha(new Object[] { rs.getString(1) != null ? rs.getString(1) : "", rs.getString(2) != null ? rs.getString(2).trim() : "", rs.getString(3) != null ? rs.getString(3) : "",
						rs.getString(4) != null ? rs.getString(4).trim() : "", rs.getString(5) != null ? Funcoes.strDecimalToStrCurrency(13, 2, rs.getString(5)) : "", imgColuna, });

				if (iCont > 0)
					bRet = true;
				else
					bRet = false;

				iCont++;
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
			Funcoes.mensagemErro(this, "Erro ao buscar filiais almoxarifados e saldos!\n" + err.getMessage(), true, con, err);
			err.printStackTrace();
		}
		finally {
			ps = null;
			rs = null;
			sImgColuna = null;
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

	public void keyPressed(KeyEvent kevt) {
		
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			acaoOk();
		}
		else if (kevt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			btCancel.doClick();
		}
			
		
	}
	
	private void acaoOk() {
		if (( tab.getNumLinhas() > 0 ) && ( tab.getLinhaSel() >= 0 )) {
			
		//	tab.setLinhaSel( tab.getLinhaSel() - 1);
			
		}
				
		btOK.doClick();
	}

	public void ok() {
		iCodAlmox = new Integer(Integer.parseInt(tab.getValueAt(tab.getLinhaSel(), 2).toString()));
		super.ok();
	}

	public void getValores() {
		if (tab.getNumLinhas() > 0) {
			if (bRet) {
				if (this.isVisible()) {
					if (( ( ImageIcon ) tab.getValueAt(tab.getLinhaSel(), 5) ).getDescription().equals("N"))
						btOK.setEnabled(false);
					else
						btOK.setEnabled(true);
				}
			}
		}
	}

}
