package org.freedom.modulos.grh.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Blob;
import org.freedom.infra.model.jdbc.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import org.freedom.bmps.Icone;
import org.freedom.infra.x.swing.JFrame;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.Login;
import org.jfree.ui.Align;

public class ProcessaPonto extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnGeral = new JPanelPad(new BorderLayout());

	private JPanelPad pnLogo = new JPanelPad(new BorderLayout());

	private JPanelPad pnFoto = new JPanelPad();

	private JPanelPad pnCampos = new JPanelPad();

	private JTextFieldPad txtMatricula = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private JTextFieldPad txtData = new JTextFieldPad(JTextFieldPad.TP_DATE, 12, 0);

	private JTextFieldPad txtRelogio = new JTextFieldPad(JTextFieldPad.TP_TIME, 20, 0);

	private JLabelPad lbStatus = null;

	private JLabelPad lbLogo = new JLabelPad(Icone.novo("bannerPonto.jpg"));

	private PainelImagem piFoto = new PainelImagem(260000);

	private javax.swing.Timer timer;

	private JTextFieldPad txtApelido = new JTextFieldPad(JTextFieldPad.TP_STRING, 30, 0);

	private static DbConnection con = null;

	private Font fntPad01 = new Font("Arial", Font.BOLD, 16);

	public ProcessaPonto() {

		super();
		initialize();
	}

	private void initialize() {

		this.setSize(new Dimension(508, 325));
		this.setAlwaysOnTop(true);
		this.setTitle("Ponto eletrônico");
		this.setLocation(200, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		montaTela();
		disparaRelogio();

		txtMatricula.addKeyListener(this);
	}

	private void montaTela() {

		add(pnGeral);

		txtMatricula.setFont(fntPad01);

		txtApelido.setFont(fntPad01);
		txtApelido.setHorizontalAlignment(Align.CENTER);
		txtApelido.setBackground(Color.WHITE);
		txtMatricula.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		piFoto.setBorder(null);

		txtRelogio.setFont(fntPad01);
		txtData.setFont(fntPad01);

		txtData.setBorder(null);
		txtRelogio.setBorder(null);
		txtApelido.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		txtApelido.setForeground(Color.BLUE);

		pnLogo.setPreferredSize(new Dimension(510, 50));

		pnGeral.add(pnLogo, BorderLayout.NORTH);

		pnLogo.add(lbLogo, BorderLayout.CENTER);

		pnFoto.setPreferredSize(new Dimension(154, 200));
		pnGeral.add(pnFoto, BorderLayout.EAST);
		pnCampos.setPreferredSize(new Dimension(410, 100));
		pnGeral.add(pnCampos, BorderLayout.WEST);

		pnCampos.adic(new JLabelPad("Matricula"), 7, 5, 300, 20);

		pnCampos.adic(txtMatricula, 7, 25, 150, 26);

		pnCampos.adic(txtData, 180, 30, 85, 20);
		pnCampos.adic(txtRelogio, 265, 30, 75, 20);

		pnFoto.adic(piFoto, 5, 5, 142, 189);

		pnFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

		piFoto.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		pnFoto.setBackground(Color.WHITE);
		pnFoto.adic(txtApelido, 5, 205, 142, 26);

		txtData.setVlrDate(new Date());

		txtData.setEditable(false);
		txtRelogio.setEditable(false);
		txtApelido.setEditable(false);

		txtMatricula.requestFocus();

	}

	private boolean carregaInfo() {

		String Sstatus = "";
		String sFoto = "";
		StringBuffer sSQL = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean bRet = false;

		sSQL.append("SELECT APELIDOEMPR, FOTOEMPR FROM RHEMPREGADO ");
		sSQL.append("WHERE CODEMP=? AND CODFILIAL=? AND MATEMPR=?");

		try {

			if (con != null) {

				ps = con.prepareStatement(sSQL.toString());
				ps.setInt(1, 5);
				ps.setInt(2, 1);
				ps.setInt(3, txtMatricula.getVlrInteger());

				rs = ps.executeQuery();

				if (rs.next()) {

					txtApelido.setVlrString(rs.getString("APELIDOEMPR"));

					Blob bVal = rs.getBlob(2);
					if (bVal != null) {
						piFoto.setVlrBytes(bVal.getBinaryStream());
					}
					bRet = true;
				}
			} else {
				System.out.println("CONEXÃO NULA!");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return bRet;
		// pnCampos.adic( lbStatus, 320, 65, 50, 50 );

	}

	private void disparaRelogio() {

		if (timer == null) {
			timer = new javax.swing.Timer(1000, this);
			timer.setInitialDelay(0);
			timer.start();
		} else if (!timer.isRunning()) {
			timer.restart();
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == timer) {
			GregorianCalendar calendario = new GregorianCalendar();
			int h = calendario.get(GregorianCalendar.HOUR_OF_DAY);
			int m = calendario.get(GregorianCalendar.MINUTE);
			int s = calendario.get(GregorianCalendar.SECOND);
			String hora = ((h < 10) ? "0" : "") + h + ":" + ((m < 10) ? "0" : "") + m + ":" + ((s < 10) ? "0" : "") + s;
			txtRelogio.setVlrString(hora);
		}
	}

	public static void main(String[] args) {

		try {
			ProcessaPonto pp = new ProcessaPonto();
			con = getConexao("SYSDBA", "masterkey");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static DbConnection getConexao(String sUsu, String sSenha) {

		DbConnection con = null;
		try {
			Aplicativo.setLookAndFeel(null);
			String strBanco = Aplicativo.getParameter("banco");
			String strDriver = Aplicativo.getParameter("driver");
			con = Login.getConexao(strBanco, strDriver, sUsu, sSenha);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	private void insertPonto() {

		StringBuffer sInsert = new StringBuffer();
		PreparedStatement ps = null;
	}

	public void keyPressed(KeyEvent e) {

		if (e.getSource() == txtMatricula) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {

				if (carregaInfo()) {
					txtMatricula.requestFocus();
					// insertPonto();

				} else {
					Funcoes.mensagemInforma(null, "Matricula não encontrada!");
					txtMatricula.requestFocus();
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}
}
