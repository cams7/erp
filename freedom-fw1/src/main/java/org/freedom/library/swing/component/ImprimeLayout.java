/**
 * @version 21/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)ImprimeLayout.java <BR>
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
 * Classe mãe para relatórios gráficos.
 * 
 */

package org.freedom.library.swing.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jfree.chart.JFreeChart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class ImprimeLayout extends JPanelPad implements Printable, VetoableChangeListener {
	private static final long serialVersionUID = 1L;
	public static final byte AL_CEN = 0; // Centro
	public static final byte AL_BCEN = 1; // Centro
	public static final byte AL_DIR = 2; // Direita
	public static final byte AL_LL = 3; // Lado a Lado
	public static final byte AL_BDIR = 4; // Borda Direita
	public static final byte AL_CDIR = 5; // Borda Direita menos o recuo
	// (definido na variável de
	// largura/tamanho)
	public static String sNomeFilial = "";
	public static String sCNPJFilial = "";
	public static String sEndFilial = "";
	private static final Font mRodFont = new Font("Serif", Font.ITALIC, 7);
	private int iMX = 0;
	private int iMY = 0;
	private int iMXPdf = -1;
	private int iMYPdf = -1;
	private String mDataStr = Funcoes.dateToStrDate(new Date());
	private Font fnTopEmp = new Font("Arial", Font.BOLD, 14);
	private PaginaPad pfPad = new PaginaPad(new Paper());
	private int iPagAtual = 0;
	private int iNumPags = 0;
	private org.w3c.dom.Document doc;
	private Element pag;
	private Element rel;
	private File fArq;
	private Font fnAtual = null;
	private Color cCorPreenc = ( new Color(255, 255, 255) );
	private Color cCorBorda = ( new Color(0, 0, 0) );
	double dEscalaX = 1;
	double dEscalaY = 1;
	boolean bRodape = true;
	boolean bimpRaz = true;

	public ImprimeLayout() {
		try {
			fArq = Funcoes.criaArqTemp("xml");
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			rel = doc.createElement("RELATORIO");
			pag = doc.createElement("PAGINA_" + iNumPags);
		}
		catch (Exception err) {
			Funcoes.mensagemErro(null, "Erro ao carregar novo XML!\n" + err.getMessage());
		}
		setPreferredSize(new Dimension(( int ) pfPad.getWidth(), ( int ) pfPad.getHeight()));
		addVetoableChangeListener(this);
	}

	/**
	 * Esta funcão é responsável por mandar o relatório para impressora.
	 * 
	 * @param bMonta
	 *            - Se true monta o relatorio e false manda direto para
	 *            impressora.
	 * @return bRet - Retorna se o relatório foi impresso corretamente.
	 */
	public void imprimeRodape(boolean bRod) {
		bRodape = bRod;
	}

	public void impRaz(boolean bImp) {
		bimpRaz = bImp;
	}

	public boolean imprimir(boolean bMonta) {
		if (bMonta)
			montaG();

		boolean bRet = false;
		PrinterJob printJob = PrinterJob.getPrinterJob();
		PrintRequestAttributeSet aset = Funcoes.getImpAtrib();
		if (printJob.printDialog(aset)) {
			try {
				printJob.setPrintable(this, new PaginaPad(printJob.defaultPage().getPaper()));
				printJob.print(aset);
				aset.remove(Copies.class);

				Funcoes.setImpAtrib(aset);
				bRet = true;
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return bRet;
	}

	/**
	 * Funcões para setar margens do PDF
	 */

	public void setMargemPdf(int iMXPdf, int iMYPdf) {
		this.iMXPdf = iMXPdf;
		this.iMYPdf = iMYPdf;
	}

	/**
	 * (Não mexa!!!)...só para saber: esta função é responsável por montar o
	 * relatório na hora que ele vai para a impressora. Não mexa aqui porque
	 * esta classe esta ligada com o printJob.
	 * 
	 * @see java.awt.print.Printable#print(java.awt.Graphics,
	 *      java.awt.print.PageFormat, int)
	 */

	public int print(Graphics g, PageFormat pf, int pg) throws PrinterException {
		if (pg >= iNumPags) {
			return Printable.NO_SUCH_PAGE;
		}
		prepG(( Graphics2D ) g, pf, pg);
		lerRel(( Graphics2D ) g, pg);
		return Printable.PAGE_EXISTS;
	}

	public boolean prepPagina(Graphics g, PageFormat pf, int pg) throws PrinterException {
		if (pg >= iNumPags) {
			return false;
		}
		prepG(( Graphics2D ) g, pf, pg);
		lerRel(( Graphics2D ) g, pg);
		return true;
	}

	/**
	 * Esta função sobrepos a classe JPanelPad para desenhar o relatório<BR>
	 * no modo de visualização.
	 * 
	 * * Eu não sei por que o java não atualiza a tela algumas vezes<BR>
	 * quem ler isso, por favor de uma ajudinha!!!
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = ( Graphics2D ) g.create();
		posG(g2d);
		prepG(g2d, pfPad, iPagAtual);

		lerRel(g2d, iPagAtual);

	}

	private void posG(Graphics2D g) {
		g.scale(dEscalaX, dEscalaY);

		int iLargLivre = ( int ) ( g.getClipBounds().width - pfPad.getWidth() );
		int iAltLivre = ( int ) ( g.getClipBounds().height - pfPad.getHeight() );

		if (iLargLivre > 0)
			g.translate(iLargLivre / 2, 0);
		if (iAltLivre > 0)
			g.translate(0, iAltLivre / 2);

		g.setColor(cCorPreenc);
		g.fillRect(0, 0, ( int ) pfPad.getWidth(), ( int ) pfPad.getHeight());

		g.setColor(cCorBorda);
	}

	private void prepG(Graphics2D g, PageFormat pf, int iPag) {
		g.translate(( int ) pf.getImageableX(), ( int ) pf.getImageableY());
		iMX = ( int ) ( pf.getImageableWidth() );
		iMY = ( int ) ( pf.getImageableHeight() );
		if (bRodape) {
			adicRodape(g, iPag);
		}

		fnAtual = g.getFont();
	}

	public int getiMX() {
		return iMX;
	}

	public int getiMY() {
		return iMY;
	}

	private void adicRodape(Graphics g, int iPag) {
		Graphics2D g2d = ( Graphics2D ) g.create();
		g2d.setPaint(Color.black);
		g2d.setFont(mRodFont);
		LineMetrics metrics = mRodFont.getLineMetrics(mDataStr, g2d.getFontRenderContext());
		int iTamTexto = getFontMetrics(mRodFont).stringWidth(mDataStr);
		int iPosYTexto = ( int ) ( iMY - metrics.getDescent() - metrics.getLeading() );
		int iPosRodY = ( int ) ( iMY - metrics.getHeight() );
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(0, iPosRodY, iMX, iPosRodY);
		g2d.drawString(( iPag + 1 ) + " de " + iNumPags, 0, iPosYTexto);
		g2d.drawString(mDataStr, iMX - iTamTexto, iPosYTexto);
		g2d.dispose();
	}

	public boolean gravaPdf(String sArquivo) {
		boolean bRetorno = false;
		com.lowagie.text.Document document = new com.lowagie.text.Document();

		try {

			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(sArquivo));

			// step 3: we open the document
			document.open();

			// step 4: we grab the ContentByte and do some stuff with it

			// we create a fontMapper and read all the fonts in the font
			// directory
			// DefaultFontMapper mapper = new DefaultFontMapper();
			// mapper.insertDirectory("c:\\windows\\fonts");
			// we create a template and a Graphics2D object that corresponds
			// with it
			int wt = 0;
			int ht = 0;
			// int wg = 0;
			// int hg = 0;
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = null;
			Graphics2D g2 = null;
			PrinterJob printJob = PrinterJob.getPrinterJob();
			Paper paper = printJob.defaultPage().getPaper();
			PaginaPad pf = new PaginaPad(paper);
			wt = ( int ) ( pf.getWidth() );
			ht = ( int ) ( pf.getHeight() );
			if (( iMXPdf != -1 ) && ( iMYPdf != -1 )) {
				paper.setImageableArea(iMXPdf, iMYPdf, wt, ht);
				pf = new PaginaPad(paper);
			}
			// wg = (int)(pf.getImageableWidth());
			// hg = (int)(pf.getImageableHeight());
			// System.out.println("wt:"+wt+"  ht:"+ht+"  wg:"+wg+"  hg:"+hg);
			// PrintRequestAttributeSet aset = Funcoes.getImpAtrib();
			// w = printJob.

			for (int i = 0; i < getNumPags(); i++) {
				if (i > 0) {
					document.newPage();
				}
				tp = cb.createTemplate(wt, ht);
				g2 = tp.createGraphics(wt, ht);
				try {
					prepPagina(g2, pf, i);
				}
				catch (PrinterException pe) {
					Funcoes.mensagemErro(null, "Erro criando graphics: " + pe.getMessage());
				}
				tp.setWidth(wt);
				tp.setHeight(ht);
				g2.dispose();
				cb.addTemplate(tp, 0, 0);

			}

		}
		catch (DocumentException de) {
			System.err.println(de.getMessage());
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();

		return bRetorno;
	}

	// Navegação:

	// Só dentro desta classe que as paginas começam de zero
	// as funções externas são tratadas com as paginas começando de um.
	public void toPagina(int iPag) {
		if (iPag > iNumPags || iPag <= 0)
			return;
		iPagAtual = iPag - 1;
		repaint();
	}

	public int getPagAtual() {
		return iPagAtual + 1;
	}

	public int getNumPags() {
		return iNumPags;
	}

	public void termPagina() {
		rel.appendChild(pag);
		pag = doc.createElement("PAGINA_" + ( ++iNumPags ));
	}

	private void lerRel(Graphics2D gr, int iPag) {
		try {
			Document docTmp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fArq);
			NodeList nohs = docTmp.getDocumentElement().getElementsByTagName("PAGINA_" + iPag);
			if (nohs != null) {

				// Como a primeira tag é: RELATORIO eu baixo um nivel indo para:
				// PAGINA_#:

				Node noh = nohs.item(0);
				if (noh != null) {
					nohs = noh.getChildNodes();

					if (nohs != null) {
						for (int j = 0; j < nohs.getLength(); j++) {
							trataNoh(gr, nohs.item(j));
						}
					}
				}
			}
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}

	// Ferramentas de desenho:

	public void setFonte(Font font) {
		Element eFonte = doc.createElement("FONTE");
		eFonte.setAttribute("nome", font.getName());
		eFonte.setAttribute("estilo", "" + font.getStyle());
		eFonte.setAttribute("tamanho", "" + font.getSize());
		pag.appendChild(eFonte);
	}

	public void setCor(Color corPreenc, Color corBorda) {
		Element eCor = doc.createElement("COR");
		eCor.setAttribute("rCorPreenc", "" + corPreenc.getRed());
		eCor.setAttribute("gCorPreenc", "" + corPreenc.getGreen());
		eCor.setAttribute("bCorPreenc", "" + corPreenc.getBlue());
		eCor.setAttribute("rCorBorda", "" + corBorda.getRed());
		eCor.setAttribute("gCorBorda", "" + corBorda.getGreen());
		eCor.setAttribute("bCorBorda", "" + corBorda.getBlue());
		pag.appendChild(eCor);
	}

	public void drawLinha(int x1, int y1, int x2, int y2) {
		Element eLinha = doc.createElement("LINHA");
		eLinha.setAttribute("x1", "" + x1);
		eLinha.setAttribute("y1", "" + y1);
		eLinha.setAttribute("x2", "" + x2);
		eLinha.setAttribute("y2", "" + y2);
		pag.appendChild(eLinha);
	}

	public void drawLinha(int x, int y, int tam, int alt, byte al) { // Linha
		// alinhada.
		Element eLinhaAl = doc.createElement("LINHA_AL");
		eLinhaAl.setAttribute("x", "" + x);
		eLinhaAl.setAttribute("y", "" + y);
		eLinhaAl.setAttribute("tam", "" + tam);
		eLinhaAl.setAttribute("alt", "" + alt);
		eLinhaAl.setAttribute("al", "" + al);
		pag.appendChild(eLinhaAl);
	}

	public void drawTexto(String str, int x, int y) {
		str = str != null ? str : "";
		Element eTexto = doc.createElement("TEXTO");
		eTexto.appendChild(doc.createCDATASection(str));
		eTexto.setAttribute("x", "" + x);
		eTexto.setAttribute("y", "" + y);
		pag.appendChild(eTexto);
	}

	public void drawTexto(String str, int x, int y, int tam, byte al) { // Texto
		// alinhado.
		Element eTextoAl = doc.createElement("TEXTO_AL");
		eTextoAl.appendChild(doc.createCDATASection(str));
		eTextoAl.setAttribute("x", "" + x);
		eTextoAl.setAttribute("y", "" + y);
		eTextoAl.setAttribute("tam", "" + tam);
		eTextoAl.setAttribute("al", "" + al);
		pag.appendChild(eTextoAl);
	}

	public void drawRetangulo(int x, int y, int larg, int alt) { // Retangulo
		// Simples.
		Element eRetangulo = doc.createElement("RETANGULO");
		eRetangulo.setAttribute("x", "" + x);
		eRetangulo.setAttribute("y", "" + y);
		eRetangulo.setAttribute("larg", "" + larg);
		eRetangulo.setAttribute("alt", "" + alt);
		pag.appendChild(eRetangulo);
	}

	public void drawRetangulo(int x, int y, int larg, int alt, String sPreenche) { // Retangulo
		// que
		// coisa
		// dentro.
		Element eRetangulo = doc.createElement("RETANGULO");
		eRetangulo.setAttribute("x", "" + x);
		eRetangulo.setAttribute("y", "" + y);
		eRetangulo.setAttribute("larg", "" + larg);
		eRetangulo.setAttribute("alt", "" + alt);
		eRetangulo.setAttribute("enc", sPreenche);
		pag.appendChild(eRetangulo);
	}

	public void drawRetangulo(int x, int y, int tam, int alt, byte al) { // Retangulo
		// alinhado.
		Element eRetanguloAl = doc.createElement("RETANGULO_AL");
		eRetanguloAl.setAttribute("x", "" + x);
		eRetanguloAl.setAttribute("y", "" + y);
		eRetanguloAl.setAttribute("tam", "" + tam);
		eRetanguloAl.setAttribute("alt", "" + alt);
		eRetanguloAl.setAttribute("al", "" + al);
		pag.appendChild(eRetanguloAl);
	}

	public void drawElipse(int x, int y, int larg, int alt, String sPreenche) { // Elipse
		// com
		// coisa
		// dentro.
		Element eElipse = doc.createElement("ELIPSE");
		eElipse.setAttribute("x", "" + x);
		eElipse.setAttribute("y", "" + y);
		eElipse.setAttribute("larg", "" + larg);
		eElipse.setAttribute("alt", "" + alt);
		eElipse.setAttribute("enc", sPreenche);
		pag.appendChild(eElipse);
	}

	public void drawElipse(int x, int y, int larg, int alt, String sPreenche, byte al) { // Elipse
		// que
		// tem
		// tudo.
		Element eElipseAl = doc.createElement("ELIPSE_AL");
		eElipseAl.setAttribute("x", "" + x);
		eElipseAl.setAttribute("y", "" + y);
		eElipseAl.setAttribute("larg", "" + larg);
		eElipseAl.setAttribute("alt", "" + alt);
		eElipseAl.setAttribute("al", "" + al);
		eElipseAl.setAttribute("enc", sPreenche);
		pag.appendChild(eElipseAl);
	}

	public void setPincel(BasicStroke stro) {
		Element ePincel = doc.createElement("PINCEL");
		ePincel.setAttribute("larg", "" + stro.getLineWidth());
		ePincel.setAttribute("ponta", "" + stro.getEndCap());
		ePincel.setAttribute("juncao", "" + stro.getLineJoin());
		ePincel.setAttribute("vertice", "" + stro.getMiterLimit());
		pag.appendChild(ePincel);
	}

	public void drawImagem(ImageIcon img, int x, int y, int larg, int alt) {
		Element eImagem = doc.createElement("IMAGEM");
		eImagem.setAttribute("x", "" + x);
		eImagem.setAttribute("y", "" + y);
		eImagem.setAttribute("larg", "" + larg);
		eImagem.setAttribute("alt", "" + alt);
		BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		bi.getGraphics().drawImage(img.getImage(), 0, 0, null);
		try {
			File fTmp = Funcoes.criaArqTemp();
			ImageIO.write(bi, "jpg", fTmp);
			eImagem.setAttribute("arq", "" + fTmp.getPath());
		}
		catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar imagem para relatório!!" + err.getMessage());
		}
		pag.appendChild(eImagem);
	}

	public void drawGrafico(JFreeChart chart, int x, int y, int larg, int alt) {
		Element eImagem = doc.createElement("GRAFICO");
		eImagem.setAttribute("x", "" + x);
		eImagem.setAttribute("y", "" + y);
		eImagem.setAttribute("larg", "" + larg);
		eImagem.setAttribute("alt", "" + alt);
		try {
			File fTmp = Funcoes.criaArqTemp();
			FileOutputStream out = new FileOutputStream(fTmp);
			ObjectOutputStream oout = new ObjectOutputStream(out);

			/*
			 * double[][] data = new double[][] {{10.0, 4.0, 15.0, 14.0}, {-5.0,
			 * -7.0, 14.0, -3.0}, {6.0, 17.0, -12.0, 7.0}, {7.0, 15.0, 11.0,
			 * 0.0}, {-8.0, -6.0, 10.0, -9.0}, {9.0, 8.0, 0.0, 6.0}, {-10.0,
			 * 9.0, 7.0, 7.0}, {11.0, 13.0, 9.0, 9.0}, {-3.0, 7.0, 11.0,
			 * -10.0}};
			 * 
			 * CategoryDataset dataset =
			 * DatasetUtilities.createCategoryDataset("Series ", "Category ",
			 * data);
			 * 
			 * JFreeChart chart = ChartFactory.createBarChart3D(
			 * "3D Bar Chart Demo", // chart title "Category", // domain axis
			 * label "Value", // range axis label dataset, // data
			 * PlotOrientation.VERTICAL, // orientation true, // include legend
			 * true, // tooltips false // urls );
			 * 
			 * CategoryPlot plot = chart.getCategoryPlot(); CategoryAxis axis =
			 * plot.getDomainAxis(); axis.setVerticalCategoryLabels(true); //
			 * chart.draw(gr,new Rectangle2D.Double(15,105,500,500));
			 */
			oout.writeObject(chart);
			oout.flush();
			eImagem.setAttribute("arq", "" + fTmp.getPath());
		}
		catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar grafico para relatório!!" + err.getMessage());
		}
		pag.appendChild(eImagem);
	}

	public void setBordaRel() {
		Element eBorda = doc.createElement("BORDA_REL");
		pag.appendChild(eBorda);
	}

	private void trataNoh(Graphics2D gr, Node noh) {
		if (noh.getNodeName().equals("TEXTO")) {
			Node nVal = noh.getChildNodes().item(0);
			String sVal = "";
			if (nVal != null && ( sVal = nVal.getNodeValue() ) != null)
				gr.drawString(sVal, Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()));
		}
		else if (noh.getNodeName().equals("FONTE")) {
			fnAtual = new Font(noh.getAttributes().getNamedItem("nome").getNodeValue(), Integer.parseInt(noh.getAttributes().getNamedItem("estilo").getNodeValue()), Integer.parseInt(noh
					.getAttributes().getNamedItem("tamanho").getNodeValue()));
			gr.setFont(fnAtual);
		}
		else if (noh.getNodeName().equals("COR")) {
			cCorPreenc = new Color(Integer.parseInt(noh.getAttributes().getNamedItem("rCorPreenc").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("gCorPreenc").getNodeValue()),
					Integer.parseInt(noh.getAttributes().getNamedItem("bCorPreenc").getNodeValue()));
			cCorBorda = new Color(Integer.parseInt(noh.getAttributes().getNamedItem("rCorBorda").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("gCorBorda").getNodeValue()),
					Integer.parseInt(noh.getAttributes().getNamedItem("bCorBorda").getNodeValue()));
			gr.setBackground(cCorPreenc);
			gr.setColor(cCorBorda);
		}
		else if (noh.getNodeName().indexOf("_AL") > 0) {
			trataAl(gr, noh); // Funções de alinhamento.
		}
		else if (noh.getNodeName().equals("LINHA")) {
			gr.drawLine(Integer.parseInt(noh.getAttributes().getNamedItem("x1").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y1").getNodeValue()), Integer.parseInt(noh
					.getAttributes().getNamedItem("x2").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y2").getNodeValue()));
		}
		else if (noh.getNodeName().equals("PINCEL")) {
			gr.setStroke(new BasicStroke(Float.parseFloat(noh.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("ponta").getNodeValue()), Integer
					.parseInt(noh.getAttributes().getNamedItem("juncao").getNodeValue()), Float.parseFloat(noh.getAttributes().getNamedItem("vertice").getNodeValue())));
		}
		else if (noh.getNodeName().equals("RETANGULO")) {
			if (noh.getAttributes().getNamedItem("enc") != null)
				if (noh.getAttributes().getNamedItem("enc").getNodeValue().equals("S")) {
					gr.fillRect(Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer.parseInt(noh
							.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
				}
				else {
					gr.drawRect(Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer.parseInt(noh
							.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
				}
			else
				gr.drawRect(Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer.parseInt(noh
						.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
		}

		else if (noh.getNodeName().equals("ELIPSE")) {
			if (noh.getAttributes().getNamedItem("enc").getNodeValue().equals("S")) {
				gr.fillOval(Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer.parseInt(noh
						.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
			}
			else {
				gr.drawOval(Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer.parseInt(noh
						.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
			}
		}

		else if (noh.getNodeName().equals("BORDA_REL")) {
			gr.drawRect(0, 0, iMX, iMY);
		}
		else if (noh.getNodeName().equals("IMAGEM")) {
			ImageIcon imVal = new ImageIcon(noh.getAttributes().getNamedItem("arq").getNodeValue());
			if (imVal != null) {
				gr.drawImage(imVal.getImage(), Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer
						.parseInt(noh.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()), null);
			}
		}
		else if (noh.getNodeName().equals("GRAFICO")) {
			File fTmp = new File(noh.getAttributes().getNamedItem("arq").getNodeValue());
			JFreeChart chart = null;
			try {
				Rectangle2D dim = new Rectangle2D.Double(Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()),
						Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), Integer.parseInt(noh.getAttributes().getNamedItem("larg").getNodeValue()), Integer.parseInt(noh
								.getAttributes().getNamedItem("alt").getNodeValue()));
				FileInputStream in = new FileInputStream(fTmp);
				ObjectInputStream oin = new ObjectInputStream(in);
				chart = ( JFreeChart ) oin.readObject();
				chart.draw(gr, dim);
			}
			catch (Exception err) {
				err.printStackTrace();
			}
		}
	}

	private void trataAl(Graphics2D gr, Node noh) {
		int iX[] = new int[2]; // Posição inicial e posição final.
		switch (Byte.parseByte(noh.getAttributes().getNamedItem("al").getNodeValue())) {
		case AL_LL:
			iX[0] = 0;
			iX[1] = iMX;
			break;
		case AL_CEN:
			iX[0] = ( iMX - Integer.parseInt(noh.getAttributes().getNamedItem("tam").getNodeValue()) ) / 2;
			iX[1] = iX[0] + Integer.parseInt(noh.getAttributes().getNamedItem("tam").getNodeValue());
			break;

		case AL_BCEN:
			Node nVal = noh.getChildNodes().item(0);
			String sVal = "";
			int tamStr = 0;
			if (nVal != null && ( sVal = nVal.getNodeValue() ) != null)
				tamStr = getFontMetrics(fnAtual).stringWidth(sVal);

			iX[0] = Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue()) + ( ( Integer.parseInt(noh.getAttributes().getNamedItem("tam").getNodeValue()) - tamStr ) / 2 );
			iX[1] = iX[0] + Integer.parseInt(noh.getAttributes().getNamedItem("tam").getNodeValue());
			break;

		case AL_DIR:
			iX[1] = Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue());
			iX[0] = iX[1] - Integer.parseInt(noh.getAttributes().getNamedItem("tam").getNodeValue());
			break;
		case AL_BDIR:
			iX[0] = Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue());
			iX[1] = iMX;
			break;
		case AL_CDIR:
			iX[0] = Integer.parseInt(noh.getAttributes().getNamedItem("x").getNodeValue());
			iX[1] = iMX - Integer.parseInt(noh.getAttributes().getNamedItem("tam").getNodeValue());
			break;

		}
		if (noh.getNodeName().equals("LINHA_AL")) {
			gr.drawLine(iX[0], Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), iX[1], Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue())
					+ Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
		}
		else if (noh.getNodeName().equals("TEXTO_AL")) {
			Node nVal = noh.getChildNodes().item(0);
			String sVal = "";
			if (nVal != null && ( sVal = nVal.getNodeValue() ) != null) {
				gr.drawString(sVal, iX[0], Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()));
			}
		}
		else if (noh.getNodeName().equals("RETANGULO_AL")) {
			gr.drawRect(iX[0], Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), iX[1] - iX[0], Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
		}
		else if (noh.getNodeName().equals("ELIPSE_AL")) {
			gr.drawOval(iX[0], Integer.parseInt(noh.getAttributes().getNamedItem("y").getNodeValue()), iX[1] - iX[0], Integer.parseInt(noh.getAttributes().getNamedItem("alt").getNodeValue()));
		}

	}

	/**
	 * Isso aqui foi uma tentativa de resolver o problema<BR>
	 * da não atualização da tela quando ele quer.
	 * 
	 * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
	 */
	public void vetoableChange(PropertyChangeEvent evt) {
		repaint();
	}

	/**
	 * Esta função ajusta a escala de zoom X/Y. A escala padrão é 1, isso
	 * aumenta ou diminui<BR>
	 * mudando a escala.<BR>
	 * por exemplo: <BR>
	 * 2.0 - duplica a escala ou aumenta 100%<BR>
	 * 1.5 - aumenta a escala em 50%<BR>
	 * 
	 * @param x
	 *            - Escala em X.
	 * @param y
	 *            - Escala em Y.
	 * 
	 */
	public void setEscala(double x, double y) {
		dEscalaX = x;
		dEscalaY = y;
	}

	/**
	 * Esta função grava o arquivo temporário XML. Esta função deve ser chamada
	 * só depois que o <BR>
	 * relatório tiver montado.
	 * 
	 */
	public void finaliza() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fArq));
			DOMSource source = new DOMSource(rel);
			StreamResult result = new StreamResult(out);
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.ENCODING, Aplicativo.strCharSetRel);
			trans.transform(source, result);
			out.close();
		}
		catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Erro ao escrever o arquivo temporário.\n" + err.getMessage());
		}
	}

	/**
	 * Esta função será sobreposta pelas classes filhas. Esta função é sempre
	 * chamada pela classe DLPrintJob<BR>
	 * quando esta no modo visualização.
	 * 
	 */
	public void montaG() {
	}

	/**
	 * Esta função monta um cabeçalho padrão com informações <BR>
	 * da empresa.
	 * 
	 * @param con
	 */

	public void montaCabEmp(DbConnection con) {
		double dAltLogo = 30;
		StringBuffer sql = new StringBuffer();
		try {

			sql.append("SELECT F.NOMEFILIAL,F.CNPJFILIAL,F.FONEFILIAL,F.FAXFILIAL,");
			sql.append("E.FOTOEMP,F.ENDFILIAL,F.NUMFILIAL AS NUMEMP ");
			sql.append("FROM SGEMPRESA E, SGFILIAL F WHERE E.CODEMP=? AND F.CODEMP=E.CODEMP AND F.CODFILIAL=?");

			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setInt(1, Aplicativo.iCodEmp);
			ps.setInt(2, Aplicativo.iCodFilial);
			ResultSet rs = ps.executeQuery();
			int iX = 0;

			if (rs.next()) {

				setFonte(fnTopEmp);
				int iLargLogo = 0;
				byte[] bVals = new byte[650000];
				Blob bVal = rs.getBlob("FotoEmp");
				if (bVal != null) {
					try {
						bVal.getBinaryStream().read(bVals, 0, bVals.length);
					}
					catch (IOException err) {
						Funcoes.mensagemErro(null, "Erro ao recuperar dados!\n" + err.getMessage());
						err.printStackTrace();
					}

					ImageIcon img = new ImageIcon(bVals);

					double dFatProp = dAltLogo / img.getIconHeight();
					drawImagem(img, 5, 3, ( int ) ( img.getIconWidth() * dFatProp ), ( int ) dAltLogo);
					iLargLogo = ( int ) ( img.getIconWidth() * dFatProp );
				}

				sNomeFilial = rs.getString("NomeFilial").trim();
				sCNPJFilial = Funcoes.setMascara(rs.getString("CnpjFilial"), "##.###.###/####-##");
				sEndFilial = rs.getString("EndFilial").trim() + ", " + rs.getInt("NumFilial");

				iX += 15 + iLargLogo;

				if (bimpRaz)
					drawTexto(sNomeFilial, iX, 14);

				setFonte(new Font("Arial", Font.PLAIN, 8));

				drawTexto("C.N.P.J.:   " + sCNPJFilial, iX, 25);
				drawTexto("Telefone.:   " + Funcoes.setMascara(rs.getString("FoneFilial"), "####-####"), 185 + iLargLogo, 25);
				drawTexto("Fax.:   " + Funcoes.setMascara(rs.getString("FaxFilial"), "####-####"), 330 + iLargLogo, 25);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException err) {
			Funcoes.mensagemErro(this, "Erro ao montar o cabeçalho da empresa!!!\n" + err.getMessage());
		}
	}

	/**
	 * Esta função retorna o PageFormat padrão para este org.freedom.layout.
	 * 
	 * @return page - PaginaPad padrão.
	 */
	public PageFormat getPFPad() {
		return new PaginaPad(new Paper());
	}
}

class PaginaPad extends PageFormat {
	public PaginaPad(Paper pp) {
		pp.setImageableArea(pp.getImageableX() - 50.0, pp.getImageableY() - 50.0, pp.getImageableWidth() + 100.0, pp.getImageableHeight() + 105.0);
		setPaper(pp);
	}
}