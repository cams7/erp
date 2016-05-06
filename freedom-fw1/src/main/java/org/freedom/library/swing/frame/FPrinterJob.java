/**
 * @version 14/11/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.telas <BR>
 * Classe:
 * @(#)FPrinterJob.java <BR>
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
 * Comentários para a classe...
 */

package org.freedom.library.swing.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.freedom.bmps.Icone;
import org.freedom.bmps.Imagem;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.object.EmailBean;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.ImprimeLayout;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;

public class FPrinterJob extends FFilho implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pnCli = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 1));

	private JPanelPad pnCab = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));

	private ImprimeLayout impLay = new ImprimeLayout();

	private ImprimeOS imp = null;

	private JButtonPad btSair = new JButtonPad("Sair", Icone.novo("btSair.png"));

	private JButtonPad btImp = new JButtonPad("Imprimir", Icone.novo("btImprime.png"));

	private JButtonPad btProx = new JButtonPad(Icone.novo("btProx.png"));

	private JButtonPad btAnt = new JButtonPad(Icone.novo("btAnt.png"));

	private JButtonPad btPrim = new JButtonPad(Icone.novo("btPrim.png"));

	private JButtonPad btUlt = new JButtonPad(Icone.novo("btUlt.png"));

	private JLabelPad lbPag = new JLabelPad("1 de 1");

	private JasperPrint relJasper = null;

	private JPanelPad pinCab = new JPanelPad(232, 45);

	private JScrollPane spn = new JScrollPane();

	private JButtonPad btZoom100 = new JButtonPad(Icone.novo("btZoom100.gif"));

	private JButtonPad btZoomIn = new JButtonPad(Icone.novo("btZoomIn.gif"));

	private JButtonPad btZoomPag = new JButtonPad(Icone.novo("btZoomPag.gif"));

	private JTextFieldPad txtZoom = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 3, 0);

	private JButtonPad btMais = new JButtonPad(Icone.novo("btZoomMais.gif"));

	private JButtonPad btMenos = new JButtonPad(Icone.novo("btZoomMenos.gif"));

	private PageFormat pag = null;

	private JPanelPad pinTools = new JPanelPad(87, 45);

	private JButtonPad btTxt = new JButtonPad(Icone.novo("btTXT.gif"));

	private JButtonPad btPdf = new JButtonPad(Icone.novo("btPdf.gif"));

	private JTextArea txa = new JTextArea();

	// private JScrollPane spn2 = new JScrollPane(txa);

	public String strTemp = "";

	private int iZoomAtual = 100;

	boolean bVisualiza = false;
	
	boolean bExporta = false;
	

	public FPrinterJob(ImprimeLayout impL, JInternalFrame ifOrig) {

		super(false);
		impLay = impL;

		setTitulo("Visualizar Impressão Gráfica", this.getClass().getName());

		setBounds(50, 50, 500, 400);

		txtZoom.setTipo(JTextFieldPad.TP_INTEGER, 3, 0);
		txtZoom.setEnterSai(false);

		Container c = getContentPane();

		c.add(pnCab, BorderLayout.NORTH);
		btSair.setPreferredSize(new Dimension(80, 30));
		pnCab.add(pinCab);
		pinCab.adic(btZoom100, 7, 5, 30, 30);
		pinCab.adic(btZoomIn, 40, 5, 30, 30);
		pinCab.adic(btZoomPag, 73, 5, 30, 30);
		pinCab.adic(txtZoom, 106, 5, 50, 30);
		pinCab.adic(btMais, 159, 5, 30, 30);
		pinCab.adic(btMenos, 192, 5, 30, 30);

		pinTools.adic(btPdf, 7, 5, 30, 30);
		pinTools.adic(btTxt, 17, 15, 30, 30);
		btPdf.setToolTipText("Exporta para formato PDF");
		btTxt.setToolTipText("Exporta para formato de texto");

		pnCab.add(pinTools);
		// monta a area de visualização:

		spn.setViewportView(impLay);
		pnCli.add(spn);

		c.add(pnCli, BorderLayout.CENTER);

		// monta tela atual:

		JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
		JPanelPad pnFCenter = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));

		JPanelPad pnDir = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnDir.add(btImp);
		pnDir.add(btSair);
		pnRod.add(pnDir, BorderLayout.EAST);

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

		btZoom100.addActionListener(this);
		btZoomIn.addActionListener(this);
		btZoomPag.addActionListener(this);
		btMais.addActionListener(this);
		btMenos.addActionListener(this);
		btSair.addActionListener(this);
		txtZoom.addKeyListener(this);

		btPdf.addActionListener(this);

		impLay.montaG();
		impLay.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent mevt) {

				if (mevt.getButton() == 1) {
					if (iZoomAtual < 990) {
						setZoom(iZoomAtual + 10);
						impLay.repaint();
					}
				}
				else {
					if (iZoomAtual > 10) {
						setZoom(iZoomAtual - 10);
						impLay.repaint();
					}
				}
				txtZoom.setVlrString("" + iZoomAtual);
			}
		});

		impLay.setCursor(getToolkit().createCustomCursor(Imagem.novo("curZoom.gif"), new Point(5, 5), "Zoom"));

		upContaPag(impLay.getPagAtual(), impLay.getNumPags());
		pag = impLay.getPFPad();
		txtZoom.setVlrString("100");

		ifOrig.getDesktopPane().add(this);

		try {
			setMaximum(true);
		}
		catch (Exception err) {
			err.printStackTrace();
		}

	}

	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, ResultSet rs, HashMap<String, Object> hParamRel, JInternalFrame ifOrig) {
		this(sLayout, sTituloRel, sFiltros, rs, hParamRel, ifOrig, null);
	}

	/**
	 * Construção do FPrinterJob utilizando JasperReports através de resultset.
	 * 
	 * @param sLayout
	 * @param sTituloRel
	 * @param sFiltros
	 * @param rs
	 * @param ifOrig
	 */
	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, ResultSet rs, HashMap<String, Object> hParamRel, JInternalFrame ifOrig, EmailBean mail) {

		super(false);
		setTitulo(sTituloRel, this.getClass().getName());
		setBounds(50, 50, 500, 400);

		ifOrig.getDesktopPane().add(this);
			
		try {

			HashMap<String, Object> hParam = Aplicativo.empresa.getAll();

			hParam.put("USUARIO", Aplicativo.getUsuario().getIdusu());
			hParam.put("FILTROS", sFiltros);
			hParam.put("TITULO", sTituloRel);
			
			if (hParamRel != null) {
				hParam.putAll(hParamRel);
			}
			
			JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);

			String root_dir = "";
			
			if(sLayout.indexOf("/")!=0) {
				root_dir = "/org/freedom/";
			}
			
			System.out.println(FPrinterJob.class.getResourceAsStream( root_dir + sLayout));

			Object SUBREPORT_DIR = hParam.get("SUBREPORT_DIR");
			
			if(SUBREPORT_DIR == null) {
				String subreport_dir = "";
				if(sLayout.lastIndexOf("/")>0){
					if ("".equals(root_dir)) {
						subreport_dir = sLayout.substring(0, sLayout.lastIndexOf("/"));
					} else {
						subreport_dir = root_dir.substring(1) + sLayout.substring(0, sLayout.lastIndexOf("/"));
					}
				}
				
				hParam.put("SUBREPORT_DIR", subreport_dir + "/");
			}
			
			relJasper = JasperFillManager.fillReport(FPrinterJob.class.getResourceAsStream(root_dir + sLayout), hParam, jrRS);

			JRViewerPad viewer = new JRViewerPad(relJasper, mail);
			this.setContentPane(viewer);
		}
		catch (JRException err) {
			err.printStackTrace();
		}


		try {
			setMaximum(true);
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}
	
	
	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, String caminhoXml, String xPath, HashMap<String, Object> hParamRel, JInternalFrame ifOrig, EmailBean mail) {

		super(false);
		setTitulo(sTituloRel, this.getClass().getName());
		setBounds(50, 50, 500, 400);

		ifOrig.getDesktopPane().add(this);
			
		try {

			HashMap<String, Object> hParam = Aplicativo.empresa.getAll();

			hParam.put("USUARIO", Aplicativo.getUsuario().getIdusu());
			hParam.put("FILTROS", sFiltros);
			hParam.put("TITULO", sTituloRel);
			
			if (hParamRel != null) {
				hParam.putAll(hParamRel);
			}
			
			JRXmlDataSource xml = new JRXmlDataSource(caminhoXml, xPath);

			String root_dir = "";
			
			if(sLayout.indexOf("/")!=0) {
				root_dir = "/org/freedom/";
			}
			
			System.out.println(FPrinterJob.class.getResourceAsStream( root_dir + sLayout));

			Object SUBREPORT_DIR = hParam.get("SUBREPORT_DIR");
			
			if(SUBREPORT_DIR == null) {
				String subreport_dir = "";
				if(sLayout.lastIndexOf("/")>0){
					if ("".equals(root_dir)) {
						subreport_dir = sLayout.substring(0, sLayout.lastIndexOf("/"));
					} else {
						subreport_dir = root_dir.substring(1) + sLayout.substring(0, sLayout.lastIndexOf("/"));
					}
				}
				
				hParam.put("SUBREPORT_DIR", subreport_dir + "/");
			}
			
			relJasper = JasperFillManager.fillReport(FPrinterJob.class.getResourceAsStream(root_dir + sLayout), hParam, xml);

			JRViewerPad viewer = new JRViewerPad(relJasper, mail);
			this.setContentPane(viewer);
		}
		catch (JRException err) {
			err.printStackTrace();
		}


		try {
			setMaximum(true);
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}
	

	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, JInternalFrame ifOrig, HashMap<String, Object> hParamRel, DbConnection con) {
		this(sLayout, sTituloRel, sFiltros, ifOrig, hParamRel, con, null);
	}

	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, JInternalFrame ifOrig, HashMap<String, Object> hParamRel, DbConnection con, EmailBean mail, boolean externo) {
		this(sLayout, sTituloRel, sFiltros, ifOrig, hParamRel, con, null, externo, true, true);
	}
	
	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, JInternalFrame ifOrig, HashMap<String, Object> hParamRel, 
			DbConnection con, EmailBean mail, boolean imprimir, boolean exportar) {
		this(sLayout, sTituloRel, sFiltros, ifOrig, hParamRel, con, mail, false, imprimir, exportar);
	}


	/**
	 * Construção do FPrinterJob utilizando JasperReports através de query no
	 * JasperReport.
	 * 
	 * @param sLayout
	 * @param sTituloRel
	 * @param sFiltros
	 * @param rs
	 * @param ifOrig
	 */
	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, JInternalFrame ifOrig, HashMap<String, Object> hParamRel, DbConnection con, EmailBean mail) {
		this(sLayout, sTituloRel, sFiltros, ifOrig, hParamRel, con, mail, false, true, true);
	}

	public FPrinterJob(String sLayout, String sTituloRel, String sFiltros, JInternalFrame ifOrig, 
			HashMap<String, Object> hParamRel, DbConnection con, EmailBean mail, boolean externo, boolean imprimir, boolean exportar) {

		super(false);
		setTitulo(sTituloRel, this.getClass().getName());
		setBounds(50, 50, 500, 400);

		ifOrig.getDesktopPane().add(this);
		
		try {
			HashMap<String, Object> hParam = Aplicativo.empresa.getAll();

			hParam.put("USUARIO", Aplicativo.getUsuario().getIdusu());
			hParam.put("FILTROS", sFiltros);
			hParam.put("TITULO", sTituloRel);

			if (hParamRel != null) {
				hParam.putAll(hParamRel);
			}
			
			
			String root_dir = "";
			
			if(sLayout.indexOf("/")!=0) {
				root_dir = "/org/freedom/";
			}
			
			System.out.println(FPrinterJob.class.getResourceAsStream( root_dir + sLayout));

			Object SUBREPORT_DIR = hParam.get("SUBREPORT_DIR");
			
			if(SUBREPORT_DIR == null) {
				String subreport_dir = "";
				if(sLayout.lastIndexOf("/")>0){
					subreport_dir = root_dir.substring(1) + sLayout.substring(0, sLayout.lastIndexOf("/"));
				}
				
				hParam.put("SUBREPORT_DIR", subreport_dir + "/");
			}
			

			relJasper = JasperFillManager.fillReport(externo ? new FileInputStream(sLayout) : FPrinterJob.class.getResourceAsStream(root_dir + sLayout), hParam, con.getConnection());

			JRViewerPad viewer = new JRViewerPad(relJasper, mail, imprimir, exportar);
			this.setContentPane(viewer);
		}
		catch (JRException err) {
			err.printStackTrace();
		}
		catch (Exception err) {
			err.printStackTrace();
		}

		try {
			setMaximum(true);
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}

	public JasperPrint getRelatorio() {

		return this.relJasper;
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btSair)
			dispose();
		else if (evt.getSource() == btImp) {
			if (imp == null) {
				impLay.imprimir(false);
			}
			else {
				imp.print();
			}
		}
		else if (evt.getSource() == btAnt)
			impLay.toPagina(impLay.getPagAtual() - 1);
		else if (evt.getSource() == btProx)
			impLay.toPagina(impLay.getPagAtual() + 1);
		else if (evt.getSource() == btPrim)
			impLay.toPagina(1);
		else if (evt.getSource() == btUlt)
			impLay.toPagina(impLay.getNumPags());
		else if (evt.getSource() == btZoom100) {
			setZoom(100);
			impLay.repaint();
		}
		else if (evt.getSource() == btZoomIn) {
			setZoom(getPercEncaixaY());
			impLay.repaint();
		}
		else if (evt.getSource() == btZoomPag) {
			setZoom(getPercEncaixaX());
			impLay.repaint();
		}
		else if (evt.getSource() == btMais) {
			if (iZoomAtual < 990) {
				setZoom(iZoomAtual + 10);
				impLay.repaint();
			}
		}
		else if (evt.getSource() == btMenos) {
			if (iZoomAtual > 10) {
				setZoom(iZoomAtual - 10);
				impLay.repaint();
			}
		}
		else if (evt.getSource() == btPdf) {
			exportaPdf();
		}
		txtZoom.setVlrString("" + iZoomAtual);

		upContaPag(impLay.getPagAtual(), impLay.getNumPags());
	}

	private boolean exportaPdf() {

		boolean bRetorno = false;
		File fArq = Funcoes.buscaArq(this, "pdf");
		if (fArq == null)
			return false;
		impLay.gravaPdf(fArq.getPath());
		return bRetorno;
	}

	public void keyPressed(KeyEvent kevt) {

		if (kevt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (txtZoom.getText().trim().length() == 0) {
				txtZoom.setVlrString("" + iZoomAtual);
			}
			else {
				setZoom(txtZoom.getVlrInteger().intValue());
				pnCli.updateUI();
				impLay.repaint();
			}
		}
	}

	private void setZoom(int iVal) {

		if (imp == null) {
			if (iVal == 0) {
				Funcoes.mensagemErro(this, "Não é poissível ajustar o zoom para 0%!");
				return;
			}

			// double dX = (double)iVal/100.0;
			double dX = iVal / 100.0;
			// double dY = (double)iVal/100.0;
			double dY = iVal / 100.0;
			Dimension dAnt = new Dimension(( int ) pag.getWidth(), ( int ) pag.getHeight());
			impLay.setEscala(dX, dY);
			dAnt.setSize(dAnt.getWidth() * dX, dAnt.getHeight() * dY);
			impLay.setPreferredSize(dAnt);
			impLay.revalidate();
			iZoomAtual = iVal;
		}
		else {
			Font fAtual = getFonte();
			int newsize = ( fAtual.getSize() * iVal ) / 100;

			txa.setFont(new Font(fAtual.getName(), fAtual.getStyle(), newsize));
			txa.updateUI();
			iZoomAtual = iVal;
		}
	}

	private int getPercEncaixaX() {

		double dLargPag = pag.getWidth();
		double dLarg = impLay.getVisibleRect().getWidth();
		double dFat = ( dLarg / dLargPag );
		return ( int ) ( dFat * 100.0 );
	}

	private int getPercEncaixaY() {

		double dAltPag = pag.getHeight();
		double dAlt = impLay.getVisibleRect().getHeight();
		double dFat = ( dAlt / dAltPag );
		return ( int ) ( dFat * 100.0 );
	}

	public void upContaPag(int Atual, int Num) {

		lbPag.setText(Atual + " de " + Num);
	}

	public void keyTyped(KeyEvent kevt) {

	}

	public void keyReleased(KeyEvent kevt) {

	}

	public FPrinterJob(ImprimeOS imp, JInternalFrame ifOrig) {
		super(false);

		this.imp = imp;

		txa.setText(imp.lePagina(1));
		txa.setFont(getFonte());

		setTitulo("Visualizar Impressão Gráfica", this.getClass().getName());

		setBounds(50, 50, 500, 400);

		txtZoom.setTipo(JTextFieldPad.TP_INTEGER, 3, 0);
		txtZoom.setEnterSai(false);

		Container c = getContentPane();

		c.add(pnCab, BorderLayout.NORTH);
		btSair.setPreferredSize(new Dimension(80, 30));
		pnCab.add(pinCab);
		pinCab.adic(btZoom100, 7, 5, 30, 30);
		pinCab.adic(btZoomIn, 40, 5, 30, 30);
		pinCab.adic(btZoomPag, 73, 5, 30, 30);
		pinCab.adic(txtZoom, 106, 5, 50, 30);
		pinCab.adic(btMais, 159, 5, 30, 30);
		pinCab.adic(btMenos, 192, 5, 30, 30);

		pinTools.adic(btPdf, 7, 5, 30, 30);
		btPdf.setToolTipText("Exporta para formato PDF");
		pnCab.add(pinTools);
		// monta a area de visualização:

		spn.setViewportView(txa);
		pnCli.add(spn);

		c.add(pnCli, BorderLayout.CENTER);

		// monta tela atual:

		JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());
		JPanelPad pnFCenter = new JPanelPad(JPanelPad.TP_JPANEL, new FlowLayout(FlowLayout.CENTER, 0, 0));

		JPanelPad pnDir = new JPanelPad(JPanelPad.TP_JPANEL, new GridLayout(1, 2));
		pnDir.add(btImp);
		pnDir.add(btSair);
		pnRod.add(pnDir, BorderLayout.EAST);

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

		btZoom100.addActionListener(this);
		btZoomIn.addActionListener(this);
		btZoomPag.addActionListener(this);
		btMais.addActionListener(this);
		btMenos.addActionListener(this);
		btSair.addActionListener(this);
		txtZoom.addKeyListener(this);

		btPdf.addActionListener(this);

		impLay.montaG();
		impLay.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent mevt) {

				if (mevt.getButton() == 1) {
					if (iZoomAtual < 990) {
						setZoom(iZoomAtual + 10);
						impLay.repaint();
					}
				}
				else {
					if (iZoomAtual > 10) {
						setZoom(iZoomAtual - 10);
						impLay.repaint();
					}
				}
				txtZoom.setVlrString("" + iZoomAtual);
			}
		});

		impLay.setCursor(getToolkit().createCustomCursor(Imagem.novo("curZoom.gif"), new Point(5, 5), "Zoom"));

		upContaPag(impLay.getPagAtual(), impLay.getNumPags());
		pag = impLay.getPFPad();
		txtZoom.setVlrString("100");

		ifOrig.getDesktopPane().add(this);

		try {
			setMaximum(true);
		}
		catch (Exception err) {
			err.printStackTrace();
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
	
	public static Blob getLogo(DbConnection con){
		Blob logo = null;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			if (con == null) {
				con = Aplicativo.getInstace().getConexao();
			}
			ps = con.prepareStatement( "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=? " );
			ps.setInt( 1, Aplicativo.iCodEmp );
	
			rs = ps.executeQuery();
			if (rs.next()) {
				logo = rs.getBlob( "FOTOEMP" );
			}

		} catch (Exception e) {
			e.printStackTrace();
		}	
		finally {
			try {
				con.commit();
				rs.close();
				ps.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return logo;
	}
	
}
