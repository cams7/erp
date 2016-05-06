/**
 * @version 05/06/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe: @(#)DLVisualiza.java <BR>
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
 * Comentários da classe.....
 */

package org.freedom.library.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JInternalFrame;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import org.freedom.bmps.Icone;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.Aplicativo;

public class DLVisualiza extends FFDialogo implements ActionListener, CaretListener {
	private static final long serialVersionUID = 1L;

	private JPanelPad pnCab = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));
	private JPanelPad pnCli = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));
	private JTextArea txa = new JTextArea();
	private JScrollPane spn = new JScrollPane(txa);
	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));
	private JButtonPad btImp = new JButtonPad("Imprimir", Icone.novo("btImprime.png"));
	private JButtonPad btProx = new JButtonPad(Icone.novo("btProx.png"));
	private JButtonPad btAnt = new JButtonPad(Icone.novo("btAnt.png"));
	private JButtonPad btPrim = new JButtonPad(Icone.novo("btPrim.png"));
	private JButtonPad btUlt = new JButtonPad(Icone.novo("btUlt.png"));
	private JLabelPad lbPag = new JLabelPad();
	private JLabelPad lbImp = new JLabelPad("Impressora: ");
	private JPanelPad pinCab = new JPanelPad(81, 45);
	private JButtonPad btMais = new JButtonPad(Icone.novo("btZoomMais.gif"));
	private JButtonPad btMenos = new JButtonPad(Icone.novo("btZoomMenos.gif"));
	private JPanelPad pinTools = new JPanelPad(81, 45);
	private JButtonPad btTxt = new JButtonPad(Icone.novo("btTXT.gif"));
	private JButtonPad btPdf = new JButtonPad(Icone.novo("btPdf.gif"));
	public ImprimeOS imp = null;
	public String strTemp = "";
	boolean bVisualiza = false;
	boolean bProcessaPos = true;

	public DLVisualiza(ImprimeOS impOS, JInternalFrame pai) {
		super(pai);
		// super(false);
		// Prepara arquivo temp:
		imp = impOS;
		// monta a area de visualização:
		setTitulo("Visualizar Impressão");

		txa.setFont(getFonte());

		txa.setEditable(false);

		pnCli.add(spn);

		Container c = getContentPane();
		c.removeAll(); // Removido todos os componentes da classe mae.

		c.add(pnCab, BorderLayout.NORTH);
		btSair.setPreferredSize(new Dimension(80, 30));
		pnCab.add(pinCab);
		pinCab.adic(btMais, 7, 5, 30, 30);
		pinCab.adic(btMenos, 40, 5, 30, 30);

		pinTools.adic(btTxt, 7, 5, 30, 30);
		pinTools.adic(btPdf, 40, 5, 30, 30);
		btTxt.setToolTipText("Exporta para arquivo TXT");
		btPdf.setToolTipText("Exporta para formato PDF");
		pnCab.add(pinTools);

		c.add(pnCli, BorderLayout.CENTER);

		JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
		JPanelPad pnFCenter = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));

		JPanelPad pnDir = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnDir.add(btImp);
		pnDir.add(btSair);
		pnRod.add(pnDir, BorderLayout.EAST);
		pnRod.add(lbImp, BorderLayout.WEST);

		JPanelPad pnCenter = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 5));
		pnFCenter.add(pnCenter);

		pnCenter.add(btPrim);
		pnCenter.add(btAnt);
		pnCenter.add(lbPag);
		pnCenter.add(btProx);
		pnCenter.add(btUlt);
		pnRod.add(pnFCenter, BorderLayout.CENTER);

		c.add(pnRod, BorderLayout.SOUTH);

		// Configura os Listeners e Componentes

		lbPag.setHorizontalAlignment(SwingConstants.CENTER);
		btSair.addActionListener(this);
		btImp.addActionListener(this);
		btProx.addActionListener(this);
		btAnt.addActionListener(this);
		btPrim.addActionListener(this);
		btUlt.addActionListener(this);
		btPdf.addActionListener(this);
		btTxt.addActionListener(this);
		btMais.addActionListener(this);
		btMenos.addActionListener(this);
		txa.addCaretListener(this);
		txa.setText(imp.lePagina(1));
		upContaPag(imp.getPagAtual(), imp.getNumPags());

	}

	public void setEnabledBotaoImp( boolean enabledBotaoImp ) {
		btImp.setEnabled( enabledBotaoImp );
	}
	
	public void actionPerformed(ActionEvent evt) {
		String sConteudo = "";
		if (evt.getSource() == btSair)
			dispose();
		else if (evt.getSource() == btImp)
			imp.print();
		else if (evt.getSource() == btAnt)
			sConteudo = imp.lePagina(imp.getPagAtual() - 1);
		else if (evt.getSource() == btProx)
			sConteudo = imp.lePagina(imp.getPagAtual() + 1);
		else if (evt.getSource() == btPrim)
			sConteudo = imp.lePagina(1);
		else if (evt.getSource() == btUlt)
			sConteudo = imp.lePagina(imp.getNumPags());
		else if (evt.getSource() == btTxt)
			imp.exportaTXT(this);
		else if (evt.getSource() == btPdf)
			imp.exportaPDF(this);
		else if (evt.getSource() == btMais)
			zoomMais();
		else if (evt.getSource() == btMenos)
			zoomMenos();
		if (sConteudo.trim().length() != 0) {
			txa.setText(sConteudo);
			upContaPag(imp.getPagAtual(), imp.getNumPags());
			txa.select(0, 1);
		}
	}

	public void zoomMais() {
		Font fAtual = txa.getFont();
		txa.setFont(new Font(fAtual.getName(), fAtual.getStyle(), fAtual.getSize() + 1));
		txa.updateUI();
	}

	public void zoomMenos() {
		Font fAtual = txa.getFont();
		txa.setFont(new Font(fAtual.getName(), fAtual.getStyle(), fAtual.getSize() - 1));
		txa.updateUI();
	}

	public void upContaPag(int Atual, int Num) {
		lbPag.setText(Atual + " de " + Num);
		lbPag.updateUI();
	}

	public void setNomeImp(String sNome) {
		lbImp.setText(" Impressora:  " + sNome);
	}

	public void caretUpdate(CaretEvent cevt) {
		int iLinha = 0;
		int iIni = 0;
		int iFim = 0;
		if (cevt.getSource() == txa && bProcessaPos) {
			bProcessaPos = false;
			try {
				iLinha = txa.getLineOfOffset(cevt.getDot());
				iIni = txa.getLineStartOffset(iLinha);
				iFim = txa.getLineEndOffset(iLinha);
				if (iIni + 1 != txa.getSelectionStart() && iFim != txa.getSelectionEnd())
					txa.select(iIni + 1, iFim);
			}
			catch (BadLocationException err) {
			}
			bProcessaPos = true;
		}
	}

	private Font getFonte() {
		Font ret = null;
		;
		StringBuilder sql = new StringBuilder();
		try {

			if (con == null) {
				con = Aplicativo.getInstace().getConexao();
			}

			sql.append("select coalesce(fontetxt,'Courier New'),coalesce(tamfontetxt,12) ");
			sql.append("from sgestacao ");
			sql.append("where ");
			sql.append("codemp=? and codfilial=? and codest=? ;");

			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, ListaCampos.getMasterFilial("SGESTACAO"));
			ps.setInt(3, Aplicativo.iNumEst);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				ret = new Font(rs.getString(1).trim(), Font.BOLD, rs.getInt(2));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				con.commit();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

}
