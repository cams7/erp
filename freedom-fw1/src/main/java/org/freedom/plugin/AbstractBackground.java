package org.freedom.plugin;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freedom.bmps.Icone;

public abstract class AbstractBackground extends JPanelPad implements MouseListener {

	private static final long serialVersionUID = 1l;

	private JLabelPad lbfundo = new JLabelPad(Icone.novo("bgFreedomSTD2.jpg"));

	private PanelImagemEmpresa pnEmpresa = new PanelImagemEmpresa();

	protected JLabelPad lbAgenda = new JLabelPad();

	protected JLabelPad lbVenda = new JLabelPad();

	protected JLabelPad lbReceber = new JLabelPad();

	protected JLabelPad lbPagar = new JLabelPad();

	protected JLabelPad lbProduto = new JLabelPad();

	protected JLabelPad lbOrcamento = new JLabelPad();

	protected JLabelPad lbCliente = new JLabelPad();

	protected DbConnection con = null;

	public AbstractBackground() {

		super();

		setBorder(null);
		setSize(500, 400);

		adic(pnEmpresa, 180, 126, 147, 147);
		adic(lbAgenda, 130, 2, 92, 92);
		adic(lbVenda, 319, 12, 92, 92);
		adic(lbReceber, 397, 130, 92, 81);
		adic(lbPagar, 371, 257, 92, 81);
		adic(lbProduto, 197, 289, 92, 92);
		adic(lbOrcamento, 41, 236, 92, 92);
		adic(lbCliente, 3, 98, 92, 92);
		adic(lbfundo, 0, 0, 500, 400);

		lbAgenda.addMouseListener(this);
		lbVenda.addMouseListener(this);
		lbPagar.addMouseListener(this);
		lbReceber.addMouseListener(this);
		lbProduto.addMouseListener(this);
		lbOrcamento.addMouseListener(this);
		lbCliente.addMouseListener(this);
	}

	public void setConexao(DbConnection con) {

		this.con = con;
		carregaImagemEmpresa();
	}

	private void carregaImagemEmpresa() {

		try {

			String sql = "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Aplicativo.iCodEmp);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				Blob blob = rs.getBlob("FOTOEMP");
				BufferedInputStream bi = new BufferedInputStream(blob.getBinaryStream());
				byte[] bytes = new byte[65000];

				try {
					bi.read(bytes, 0, bytes.length);
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				Image image = new ImageIcon(bytes).getImage();
				pnEmpresa.setImage(image);
			}

			con.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {

		if (e.getSource() instanceof JLabel) {
			( ( JLabel ) e.getSource() ).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	public void mouseExited(MouseEvent e) {

		if (e.getSource() instanceof JLabel) {
			( ( JLabel ) e.getSource() ).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	private class PanelImagemEmpresa extends JPanel {

		private static final long serialVersionUID = 1l;

		private Image image = null;

		public PanelImagemEmpresa() {

			super();
			setOpaque(false);
		}

		public void setImage(Image image) {

			this.image = image;
			repaint();
		}

		public void paint(Graphics g) {

			super.paint(g);

			try {

				Dimension tam = getSize();

				if (image != null) {

					g.drawImage(image, 0, 0, tam.width, tam.height, this);
				}

			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
