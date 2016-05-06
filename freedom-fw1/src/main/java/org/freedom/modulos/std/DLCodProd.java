/**
 * @version 16/03/2006 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 *
 * Projeto: Freedom <BR>
 *  
 * Pacote: org.freedom.modulos.std <BR>
 * Classe: @(#)DLCodProd.java <BR>
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
 * Tela para busca de produto pelo código de barras
 */

package org.freedom.modulos.std;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLCodProd extends FFDialogo implements KeyListener {

	private static final long serialVersionUID = 1L;

	public JTablePad tab = new JTablePad();

	private JScrollPane spnCentro = new JScrollPane(tab);
	private String codprod = "";	
	private boolean bFilCodProd = false;
	private boolean bFilRefProd = false;
	private boolean bFilCodBar = false;
	private boolean bFilCodFab = false;
	private boolean bFilCodProdFor = false;

	private Integer codfor = null;

	private Vector<String> vProds = new Vector<String>();
	private Vector<String> vUsaLote = new Vector<String>();
	private JComponent proxFoco = null;
	
	private boolean referencia = false;

	public DLCodProd(DbConnection con, JComponent proxFoco, Integer codfor) {

		setTitulo("Pesquisa auxiliar");
		setAtribos(575, 260);
		setResizable(true);
		setConexao(con);

		this.codfor = codfor;
		this.proxFoco = proxFoco;

		c.add(spnCentro, BorderLayout.CENTER);

		addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				if (tab.getNumLinhas() > 0)
					tab.requestFocus();
				else
					btCancel.requestFocus();
			}
		});

		tab.adicColuna("Cód.prod.");
		tab.adicColuna("Ref.prod.");
		tab.adicColuna("Cód.bar.prod.");
		tab.adicColuna("Cód.fab.prod.");
		tab.adicColuna("Descrição do produto");
		tab.adicColuna("Cód.amox.");
		tab.adicColuna("lote");
		tab.adicColuna("Validade");
		tab.adicColuna("Saldo");
		tab.setTamColuna(80, 0);
		tab.setTamColuna(80, 1);
		tab.setTamColuna(80, 2);
		tab.setTamColuna(80, 3);
		tab.setTamColuna(160, 4);
		tab.setTamColuna(80, 5);
		tab.setTamColuna(80, 6);
		tab.setTamColuna(80, 7);
		tab.setTamColuna(80, 8);

		tab.addKeyListener(this);

		getPrefere();

	}

	public void passaFocus() {
		if (proxFoco != null)
			proxFoco.requestFocus();
	}

//	public boolean buscaCodProd(String valor ) {
//		return buscaCodProd(valor, false);
//	}
	public boolean buscaCodProd(String valor, boolean referenciax) {

		valor = StringFunctions.alltrim(valor);
		referencia = referenciax;
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		ResultSet rs = null;
		ResultSet rs2 = null;

		String sql = null;
		String sqladiclote = null;
		String sqladic = null;
		String sqlfor = "";

		String where = "";

		String sTemp = null;

		boolean usaOR = false;
		boolean adicCodProd = false;

		int ilinha = 0;

		if (valor == null || valor.trim().length() <= 0) {
			return false;
		}

		try {

			vProds.clear();
			vUsaLote.clear();
			tab.limpa();
			codprod = "0";

			sqladiclote = "SELECT P.CODPROD, P.REFPROD, P.CODBARPROD, P.CODFABPROD, P.DESCPROD, " 
						+ "L.CODLOTE, L.VENCTOLOTE, L.SLDLOTE, A.CODALMOX " 
						+ "FROM EQPRODUTO P, EQLOTE L, EQALMOX A "
						+ "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.CODPROD=? " 
						+ "AND A.CODEMP=P.CODEMPAX AND A.CODFILIAL=P.CODFILIALAX AND A.CODALMOX=P.CODALMOX "
						+ "AND L.CODEMP=P.CODEMP AND L.CODFILIAL=P.CODFILIAL AND L.CODPROD=P.CODPROD " 
						+ "AND L.VENCTOLOTE = ( SELECT MIN(VENCTOLOTE) " 
						+ "FROM EQLOTE LS "
						+ "WHERE LS.CODPROD=L.CODPROD AND LS.CODFILIAL=L.CODFILIAL " 
						+ "AND LS.CODEMP=L.CODEMP AND VENCTOLOTE >= CAST('today' AS DATE) ) ";

			sqladic = "SELECT P.CODPROD, P.REFPROD, P.CODBARPROD, P.CODFABPROD, P.DESCPROD, A.CODALMOX " 
					+ "FROM EQPRODUTO P, EQALMOX A " 
					+ "WHERE P.CODEMP=? AND P.CODFILIAL=? AND " + (referencia ? "P.REFPROD=? " : "P.CODPROD=? ") 								
					+ "AND A.CODEMP=P.CODEMPAX AND A.CODFILIAL=P.CODFILIALAX AND A.CODALMOX=P.CODALMOX ";
					

			sql = "SELECT P.CODPROD, P.REFPROD, P.CLOTEPROD FROM EQPRODUTO P WHERE P.CODEMP=? AND P.CODFILIAL=? ";

			if (bFilCodBar) {

				where = "AND (P.CODBARPROD=?) ";

			}

			if (bFilCodProd) {

				try {

					if(Funcoes.ehInteiro(valor)) {
					
						int val = Integer.parseInt(valor);
	
						if (val < Integer.MAX_VALUE && val > Integer.MIN_VALUE) {
	
							if (where.length() > 0) {
	
								where += "OR (P.CODPROD=?) ";
	
								usaOR = true;
	
							}
							else {
	
								where += "AND P.CODPROD=? ";
	
							}
	
							adicCodProd = true;
	
						}
					}
				}
				catch (NumberFormatException e) {
					System.out.println("Erro ao fazer busca generica de produtos\n" + e.getMessage());
				}
			}

			if (bFilRefProd) {

				if (where.length() > 0) {

					where += "OR (P.REFPROD=?) ";
					usaOR = true;

				}
				else {

					where = "AND (P.REFPROD=?) ";

				}
			}

			if (bFilCodFab) {

				if (where.length() > 0) {

					where += "OR (P.CODFABPROD=?) ";
					usaOR = true;

				}
				else {

					where = "AND (P.CODFABPROD=?) ";

				}
			}
			if (bFilCodProdFor) {

				sqlfor = "UNION SELECT PF.CODPROD, P.REFPROD, P.CLOTEPROD FROM CPPRODFOR PF, EQPRODUTO P " 
					   + "WHERE P.CODEMP = PF.CODEMP AND P.CODFILIAL=PF.CODFILIAL AND P.CODPROD=PF.CODPROD "
					   + "AND PF.CODEMP=? AND PF.CODFILIAL=? AND PF.REFPRODFOR = ? " 
					   + ( codfor != null ? ( " AND PF.CODFOR=" + codfor ) : "" );

			}

			if (usaOR) {
				where = " AND (" + where.substring(4, where.length()) + ") ";
			}

			sql = sql + where + sqlfor;

			ps = con.prepareStatement(sql);

			int iparam = 1;

			ps.setInt(iparam++, Aplicativo.iCodEmp);

			ps.setInt(iparam++, ListaCampos.getMasterFilial("EQPRODUTO"));

			if (bFilCodBar) {
				ps.setString(iparam++, valor);
			}

			if (adicCodProd) {
				ps.setString(iparam++, valor);
			}

			if (bFilRefProd) {
				ps.setString(iparam++, valor);
			}

			if (bFilCodFab) {
				ps.setString(iparam++, Funcoes.copy(valor, 15)); 
			}
			if (bFilCodProdFor) {

				ps.setInt(iparam++, Aplicativo.iCodEmp);
				ps.setInt(iparam++, ListaCampos.getMasterFilial("EQPRODUTO"));
				ps.setString(iparam++, valor);

			}

			rs = ps.executeQuery();

			while (rs.next()) {
				
				if(referencia) {
					vProds.addElement( rs.getString("REFPROD") != null ? rs.getString("REFPROD") : "0");
				}
				else {
					vProds.addElement( rs.getString("CODPROD") != null ? rs.getString("CODPROD") : "0");
				}
				
				vUsaLote.addElement(rs.getString("CLOTEPROD") != null ? rs.getString("CLOTEPROD") : "N");
			}

			if (vProds.size() <= 0) {
				Funcoes.mensagemErro(this, "Código Invalido!");
				return false;
			}
			else if (vProds.size() == 1) {
				codprod = vProds.elementAt(0);
				ok();
				return true;
			}

			for (int i = 0; i < vProds.size(); i++) {
				if (vUsaLote.elementAt(i).equals("S"))
					sTemp = sqladiclote;
				else
					sTemp = sqladic;

				ps2 = con.prepareStatement(sTemp);
				ps2.setInt(1, Aplicativo.iCodEmp);
				ps2.setInt(2, ListaCampos.getMasterFilial("EQPRODUTO"));
				ps2.setString(3, vProds.elementAt(i));

				rs2 = ps2.executeQuery();

				if (rs2.next()) {
					if (vUsaLote.elementAt(i).equals("S")) {
						tab.adicLinha(new Object[] { ( rs2.getString("CODPROD") != null ? rs2.getString("CODPROD").trim() : "" ),
								( rs2.getString("REFPROD") != null ? rs2.getString("REFPROD").trim() : "" ), ( rs2.getString("CODBARPROD") != null ? rs2.getString("CODBARPROD").trim() : "" ),
								( rs2.getString("CODFABPROD") != null ? rs2.getString("CODFABPROD").trim() : "" ), ( rs2.getString("DESCPROD") != null ? rs2.getString("DESCPROD").trim() : "" ),
								( rs2.getString("CODALMOX") != null ? rs2.getString("CODALMOX").trim() : "" ), ( rs2.getString("CODLOTE") != null ? rs2.getString("CODLOTE").trim() : "" ),
								( rs2.getString("VENCTOLOTE") != null ? rs2.getString("VENCTOLOTE").trim() : "" ), ( rs2.getString("SLDLOTE") != null ? rs2.getString("SLDLOTE").trim() : "" ) });
					}
					else {
						tab.adicLinha(new Object[] { ( rs2.getString("CODPROD") != null ? rs2.getString("CODPROD").trim() : "" ),
								( rs2.getString("REFPROD") != null ? rs2.getString("REFPROD").trim() : "" ), ( rs2.getString("CODBARPROD") != null ? rs2.getString("CODBARPROD").trim() : "" ),
								( rs2.getString("CODFABPROD") != null ? rs2.getString("CODFABPROD").trim() : "" ), ( rs2.getString("DESCPROD") != null ? rs2.getString("DESCPROD").trim() : "" ),
								( rs2.getString("CODALMOX") != null ? rs2.getString("CODALMOX").trim() : "" ), "", "", "" });
					}
					ilinha++;
				}
			}

			if (ilinha <= 0) {
				Funcoes.mensagemErro(this, "Código Invalido!");
				return false;
			}
			else if (ilinha == 1) {
				if(referencia) {
					codprod = vProds.elementAt(1);
				}
				else {
					codprod = vProds.elementAt(0);
				}
				
				ok();
				return true;
			}

			tab.changeSelection(0, 0, true, true);
			tab.setLinhaSel(0);
			setVisible(true);

		}
		catch (SQLException e) {
			Funcoes.mensagemErro(this, "Erro ao buscar produtos por código de barras!\n" + e.getMessage(), true, con, e);
			e.printStackTrace();
			return false;
		}
		finally {
			ps = null;
			rs = null;
			sql = null;
		}
		
		int ilin = tab.getLinhaSel();
		
		if (tab.getNumLinhas() > 0 && ilin >= 0) {
			if(referencia) {
				codprod = ( String ) tab.getValor(ilin, 1);
			}
			else {
				codprod =  ( String ) tab.getValor(ilin, 0);
			}
			passaFocus();
			super.ok();
		}
		else {
			Funcoes.mensagemInforma(this, "Nenhum produto foi selecionado.");
			codprod = "0";
			return false;
		} 
		
		return true;


	}

	public String getCodProd() {
		return codprod;
	}

	private void getPrefere() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;

		try {

			sSQL = "SELECT FILBUSCGENPROD, FILBUSCGENREF, FILBUSCGENCODBAR, FILBUSCGENCODFAB, FILBUSCGENCODFOR " + "FROM SGPREFERE1 " + "WHERE CODEMP=? AND CODFILIAL=?";

			ps = con.prepareStatement(sSQL);
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGPREFERE1"));

			rs = ps.executeQuery();

			if (rs.next()) {

				bFilCodProd = ( rs.getString("FILBUSCGENPROD") != null && rs.getString("FILBUSCGENPROD").equals("S") ) ? true : false;
				bFilRefProd = ( rs.getString("FILBUSCGENREF") != null && rs.getString("FILBUSCGENREF").equals("S") ) ? true : false;
				bFilCodBar = ( rs.getString("FILBUSCGENCODBAR") != null && rs.getString("FILBUSCGENCODBAR").equals("S") ) ? true : false;
				bFilCodFab = ( rs.getString("FILBUSCGENCODFAB") != null && rs.getString("FILBUSCGENCODFAB").equals("S") ) ? true : false;
				bFilCodProdFor = ( rs.getString("FILBUSCGENCODFOR") != null && rs.getString("FILBUSCGENCODFOR").equals("S") ) ? true : false;

			}

			// Garante pelo menos um filtro na busca...
			// Caso se defina no preferencias so a busca generica
			// e não escolha as opções...
			if (!bFilCodProd && !bFilRefProd && !bFilCodBar && !bFilCodFab)
				bFilCodProd = true;

		}
		catch (SQLException e) {
			Funcoes.mensagemErro(this, "Erro ao buscar filtros!\n" + e.getMessage(), true, con, e);
			e.printStackTrace();
		}
		finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

	}

	public void keyPressed(KeyEvent kevt) {
		if (kevt.getSource() == tab && kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			int ilin = tab.getLinhaSel();
			codprod = "0";
			if (tab.getNumLinhas() > 0 && ilin >= 0) {
				if(referencia) {
					codprod = ( String ) tab.getValor(ilin, 1);
				}
				else {
					codprod = ( String ) tab.getValor(ilin, 0);
				}
				passaFocus();
				super.ok();
			}
			else {
				Funcoes.mensagemInforma(this, "Nenhum produto foi selecionado.");
				codprod = "0";
			}
		}
		else
			super.keyPressed(kevt);
	}

	public void keyReleased(KeyEvent kevt) {
	}

	public void keyTyped(KeyEvent kevt) {
	}

}
