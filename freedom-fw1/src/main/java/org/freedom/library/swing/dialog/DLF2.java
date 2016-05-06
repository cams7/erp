/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)DLF2.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários da classe.....
 */

package org.freedom.library.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.Campo;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.util.SwingParams;

public class DLF2 extends FFDialogo implements KeyListener, WindowFocusListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabelPad lbPesq = new JLabelPad("Código");

	private JTextFieldPad txtPesq = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private JPanelPad pnBordCab = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));

	private JPanelPad pinCab = new JPanelPad();

	private DefaultTableCellRenderer cabAnt = new DefaultTableCellRenderer();

	private DefaultTableCellRenderer cab = new DefaultTableCellRenderer();

	public JTablePad tab = new JTablePad();

	private JScrollPane spnCentro = new JScrollPane(tab);

	private ListaCampos lcF2 = null;

	private DbConnection cnF2 = null;

	private PreparedStatement sqlF2 = null;

	private ResultSet rsF2 = null;

	private boolean bConsultaAtiva = false;

	private String sTextoAnt = "";

	public JButtonPad btExecuta = new JButtonPad(Icone.novo("btExecuta.png"));

	boolean bPrimeira = false;

	private boolean multiselecao;
	
	int ColunaAtiva = -1;

	String sNomeCampoAtual = "";

	String sSqlF2 = "";

	String sWhereAdic = "";

/*	public DLF2(ListaCampos lc, Component cOrig) {
		this(lc, cOrig, false);
	}*/
	
	public DLF2(ListaCampos lc, Component cOrig) {

		super(cOrig);
		setMultiselecao(lc.isMuiltiselecaoF2());
		setAlwaysOnTop(true);
		btExecuta.setFocusable(false);
		cnF2 = lc.getConexao();
		if (cnF2 == null) {
			Funcoes.mensagemErro(this, "Conexão nula!");
			OK = false;
			setVisible(false);
		}
		setTitulo("Pesquisa (" + lc.getNomeTabela().trim() + ")");
		setAtribos(650, 400);
		setResizable(true);
		lcF2 = lc;

		pnBordCab.setPreferredSize(new Dimension(300, 55));

		pinCab = new JPanelPad(390, 45);
		pinCab.adic(lbPesq, 7, 3, 270, 20);
		pinCab.adic(btExecuta, 290, 13, 30, 30);
		pinCab.adic(txtPesq, 7, 23, 270, 20);

		pnBordCab.add(pinCab);

		c.add(pnBordCab, BorderLayout.NORTH);
		c.add(spnCentro, BorderLayout.CENTER);

		txtPesq.addKeyListener(this);
		txtPesq.setEnterSai(false);
		tab.addKeyListener(this);

		addWindowFocusListener(this);

		btExecuta.setToolTipText("Executa consulta para campos não alfa-numéricos");
		btExecuta.addActionListener(this);

		montaColunas();

		trocaColuna();
		trocaColuna();

		setPrimeiroFoco(txtPesq);
		
		
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
		
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "acaoOk");
		am.put("acaoOk", enterKey);
		
		if (multiselecao) {
			tab.setRowSelectionAllowed(true);
			tab.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}

	}

	public void montaColunas() {

		String tit = "";
		String sComma = ",";
		int tam = 0;
		int nchar = 0;
		Campo campo = null;
		sSqlF2 = "SELECT ";
		for (int i = 0; i < lcF2.getComponentCount(); i++) {
			tit = ( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getTituloCampo();
			tab.adicColuna(tit);
			if (i == lcF2.getComponentCount() - 1)
				sComma = " ";
			sSqlF2 += ( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getNomeCampo() + sComma;
		}
		for (int i = 0; i < lcF2.getComponentCount(); i++) {

			tam = ( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getTamanhoCampo();

			if (tam == 0) {

				try {
					campo = ( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getCampo();
					nchar = campo.getTamanho();
				}
				catch (Exception e) {
					System.out.println("Erro ao contar caracteres do campo");
				}

				int tamanhofonte = SwingParams.FONT_SIZE_MED;
				Float fator = ( ( new Float(tamanhofonte) ).floatValue() / ( new Float(1.5) ).floatValue() );

				tam = nchar * fator.intValue();

			}
			tab.setTamColuna(tam, i);
		}
		sSqlF2 += " FROM " + lcF2.getNomeTabela();
		System.out.println("QUERY F2: " + sSqlF2);
	}

	public void trocaColuna() {

		int iTipo = 0;
		int iTam = 1;
		int iDec = 0;
		int iMascara = 0;

		if (tab.getNumColunas() > 0) {
			if (ColunaAtiva == ( tab.getNumColunas() - 1 )) {
				tab.getColumnModel().getColumn(ColunaAtiva).setHeaderRenderer(cabAnt);
				ColunaAtiva = 0;
			}
			else
				ColunaAtiva++;
			if (ColunaAtiva > 0) {
				tab.getColumnModel().getColumn(ColunaAtiva - 1).setHeaderRenderer(cabAnt);
			}
			cabAnt = ( DefaultTableCellRenderer ) tab.getColumnModel().getColumn(ColunaAtiva).getHeaderRenderer();
			cab.setBackground(Color.gray);
			cab.setForeground(Color.yellow);
			tab.getColumnModel().getColumn(ColunaAtiva).setHeaderRenderer(cab);
			lbPesq.setText(( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getTituloCampo());
			if (( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo() != null) {
				iTipo = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getTipoCampo();
				iTam = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getTamanho();
				iDec = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getDecimal();
				iMascara = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getIMascara();
			}
			else {
				iTipo = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getTipo();
			}
			txtPesq.setTipo(iTipo, iTam, iDec);
			if (txtPesq.getEhMascara())
				txtPesq.setMascara(iMascara);
			else
				txtPesq.setEhMascara(false);
			sNomeCampoAtual = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getNomeCampo();
			repaint();
		}
		habBtPesq();

	}

	private void habBtPesq() {

		int iTipo = txtPesq.getTipo();

		if (verifTipoPesq(iTipo)) { // Se for numérico deve habilitar o botão de pesquisa.
			btExecuta.setEnabled(true);
		}
		else {
			btExecuta.setEnabled(false);
		}
	}

	public void voltaColuna() {

		int iTipo = 0;
		int iTam = 0;
		int iDec = 0;
		int iMascara = 0;
		if (tab.getNumColunas() > 0) {
			if (ColunaAtiva == ( 0 )) {
				tab.getColumnModel().getColumn(ColunaAtiva).setHeaderRenderer(cabAnt);
				ColunaAtiva = tab.getNumColunas() - 1;
			}
			else
				ColunaAtiva--;
			if (ColunaAtiva < tab.getNumColunas() - 1) {
				tab.getColumnModel().getColumn(ColunaAtiva + 1).setHeaderRenderer(cabAnt);
			}
			cabAnt = ( DefaultTableCellRenderer ) tab.getColumnModel().getColumn(ColunaAtiva).getHeaderRenderer();
			cab.setBackground(Color.gray);
			cab.setForeground(Color.yellow);
			tab.getColumnModel().getColumn(ColunaAtiva).setHeaderRenderer(cab);
			lbPesq.setText(( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getTituloCampo());
			if (( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo() != null) {
				iTipo = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getTipoCampo();
				iTam = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getTamanho();
				iDec = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getDecimal();
				iMascara = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getCampo().getIMascara();
			}
			else {
				iTipo = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getTipo();
			}
			txtPesq.setTipo(iTipo, iTam, iDec);
			if (txtPesq.getEhMascara())
				txtPesq.setMascara(iMascara);
			sNomeCampoAtual = ( ( GuardaCampo ) ( lcF2.getComponent(ColunaAtiva) ) ).getNomeCampo();
			repaint();
		}
		habBtPesq();
	}

	public void executaSql() {

		String sNomeCampoX = "";
		String sVal = "";
		try {
			rsF2 = sqlF2.executeQuery();
			while (rsF2.next()) {
				Vector<Object> data = new Vector<Object>();
				for (int i = 0; i < tab.getNumColunas(); i++) {
					sNomeCampoX = ( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getNomeCampo();
					if (( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getTipo() == JTextFieldPad.TP_DATE) {
						sVal = rsF2.getString(sNomeCampoX) != null ? StringFunctions.sqlDateToStrDate(rsF2.getDate(sNomeCampoX)) : "";
					}
					else {
						sVal = rsF2.getString(sNomeCampoX) != null ? rsF2.getString(sNomeCampoX) : "";
					}
					data.addElement(sVal);
				}
				tab.adicLinha(data);
			}
			// rsF2.close();
			// sqlF2.close();
			// cnF2.commit();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro ao relizar consulta!\n" + err.getMessage(), true, con, err);
		}
	}

	private boolean verifTipoPesq(int iTipo) {

		return ( ( iTipo == JTextFieldPad.TP_INTEGER ) || ( iTipo == JTextFieldPad.TP_DATE ) || ( iTipo == JTextFieldPad.TP_DECIMAL ) );
	}

	public void montaSql() {

		bConsultaAtiva = true;
		boolean bString1 = false;
		String sSep = "";
		String sWhere = "";
		String sOrderBy = "";
		boolean bLike = ( txtPesq.getText().trim().length() < txtPesq.iTamanho );
		int iTipo = txtPesq.getTipo();
		if (txtPesq.iTamanho == 1) {
			bString1 = true;
		}
		if (tab.getNumLinhas() > 0) {
			tab.limpa();
		}
		sWhere += " WHERE " + ( lcF2.getUsaME() ? "CODEMP=" + Aplicativo.iCodEmp + ( lcF2.getUsaFI() ? " AND CODFILIAL=" + lcF2.getCodFilial() : "" ) : "" );
		sSep = ( lcF2.getUsaME() ? " AND " : "" );
		sWhereAdic = lcF2.inDinWhereAdic(lcF2.getWhereAdic(), lcF2.vTxtValor);
		try {
			sWhere += sWhereAdic.trim().equals("") ? "" : sSep + sWhereAdic;
			sSep = sWhereAdic.trim().equals("") ? sSep : " AND ";
			if (verifTipoPesq(iTipo)) {
				if (sWhere.trim().equals("WHERE"))
					sWhere = "";
				sWhere += ( !txtPesq.getVlrString().trim().equals("") ? sSep + sNomeCampoAtual + "=?" : "" );
				sOrderBy = " ORDER BY " + sNomeCampoAtual;
				sqlF2 = cnF2.prepareStatement(sSqlF2 + sWhere + sOrderBy);
				if (!txtPesq.getVlrString().trim().equals("")) {
					if (txtPesq.getTipo() == JTextFieldPad.TP_INTEGER)
						sqlF2.setInt(1, txtPesq.getVlrInteger().intValue());
					else if (txtPesq.getTipo() == JTextFieldPad.TP_DATE)
						sqlF2.setDate(1, Funcoes.dateToSQLDate(txtPesq.getVlrDate()));
					else if (txtPesq.getTipo() == JTextFieldPad.TP_DECIMAL)
						sqlF2.setBigDecimal(1, txtPesq.getVlrBigDecimal());
				}
			}
			else if (( ( txtPesq.getTipo() == JTextFieldPad.TP_STRING ) || ( txtPesq.getEhMascara() ) ) && ( !bString1 )) {
				// Foi usada essa variavel booleana (blike) por que quando o
				// campo estiver todo
				// preenchido (do tamnho do length) nao eh preciso fazer LIKE.
				sWhere += sSep + sNomeCampoAtual + ( bLike ? ( " LIKE '" + txtPesq.getVlrString().trim() + "%'" ) : ( "='" + txtPesq.getVlrString().trim() + "'" ) ) + " ORDER BY " + sNomeCampoAtual;
				sqlF2 = cnF2.prepareStatement(sSqlF2 + sWhere);
				// sqlF2.setString( 1, txtPesq.getVlrString().trim() + ( bLike ?
				// "%" : "" ) );
			}
			else if (bString1) {
				sOrderBy = " ORDER BY " + sNomeCampoAtual;
				sWhere += sWhereAdic.trim().equals("") ? "" : sSep + sWhereAdic;
				sWhere += ( !txtPesq.getVlrString().trim().equals("") ? sSep + sNomeCampoAtual + "=?" : "" );
				if (sWhere.trim().equals("WHERE"))
					sWhere = "";
				sqlF2 = cnF2.prepareStatement(sSqlF2 + sWhere + sOrderBy);
				if (!txtPesq.getVlrString().trim().equals(""))
					sqlF2.setString(1, txtPesq.getVlrString().trim());
			}
		}
		catch (SQLException e) {
			System.out.println("ERRO AO MONTAR A SQL!:\n" + e.getMessage());
			dispose();
		}
		System.out.println("DLF2 -> " + sSqlF2 + sWhere);
	}

	public Vector<Object> getMultiValor() {
		Vector<Object> result = new Vector<Object>();
		
		for (int i=0; i<tab.getSelectedRows().length; i++) {
			int row = tab.getSelectedRows()[i];
			result.addElement(tab.getLinha(row));
		}

		return result;
	}

	public Object getValor(String sNomeCampo) {

		int ind = -1;

		for (int i = 0; i < tab.getNumColunas(); i++) {
			// if ((((GuardaCampo)(lcF2.getComponent(i))).getNomeCampo()) ==
			// (sNomeCampo)) {
			if (( ( ( GuardaCampo ) ( lcF2.getComponent(i) ) ).getNomeCampo() ).equalsIgnoreCase(sNomeCampo)) {
				ind = i;
				break;
			}
		}
		if (ind < 0)
			return null;
		if (bPrimeira)
			return tab.getValor(0, ind);
		else if (( ind >= 0 ) & ( tab.getLinhaSel() >= 0 ))
			return tab.getValor(tab.getLinhaSel(), ind);
		return null;
	}

	private void acaoOk() {
				
		btOK.doClick();
		
	}
	
	public void keyPressed(KeyEvent kevt) {

		if (kevt.getSource() == txtPesq) {
			if (( kevt.getKeyCode() == KeyEvent.VK_UP ) & ( txtPesq.getText().trim().length() == 0 )) {
				voltaColuna();
				bConsultaAtiva = false;
			}
			if (kevt.getKeyCode() == KeyEvent.VK_ENTER) {
				if (txtPesq.getText().trim().length() == 0) {
					trocaColuna();
					bConsultaAtiva = false;
				}
				else {
					if (txtPesq.getText().compareTo(sTextoAnt) != 0) {
						montaSql();
						executaSql();
					}
					else {
						tab.requestFocus();
					}
					if (tab.getNumLinhas() > 0) {
						tab.setRowSelectionInterval(0, 0);
					}
				}
				sTextoAnt = txtPesq.getText(); 
			}
			else if (kevt.getKeyCode() == KeyEvent.VK_DOWN) {
				tab.requestFocus();
			}
		}
		else if (kevt.getSource() == tab) {
			if (( kevt.getKeyCode() == KeyEvent.VK_UP ) & ( tab.getLinhaSel() == 0 )) {
				txtPesq.requestFocus();
			}
		}
	}

	public void keyReleased(KeyEvent kevt) {

		if (( kevt.getKeyCode() == KeyEvent.VK_ENTER ) & ( !bConsultaAtiva )) {
			txtPesq.setVlrString("");
			txtPesq.requestFocus();
		}
	}

	public void windowGainedFocus(WindowEvent e) {

		txtPesq.requestFocus();
	}

	public void windowLostFocus(WindowEvent e) {

	}

	public void keyTyped(KeyEvent kevt) {

	}

	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == btExecuta) {
			montaSql();
			executaSql();
			sTextoAnt = txtPesq.getText();
		}

		super.actionPerformed(arg0);

	}

	public boolean isMultiselecao() {
		return multiselecao;
	}

	public void setMultiselecao(boolean multiselecao) {
		this.multiselecao = multiselecao;
	}
}
