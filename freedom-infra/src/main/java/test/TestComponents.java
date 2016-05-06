package test;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.freedom.infra.util.text.DecimalDocument;
import org.freedom.infra.x.UIMaker.Button;
import org.freedom.infra.x.UIMaker.Label;
import org.freedom.infra.x.UIMaker.Panel;
import org.freedom.infra.x.UIMaker.TextField;
import org.freedom.infra.x.UIMaker.effect.Effect;
import org.freedom.infra.x.UIMaker.effect.Fade;
import org.freedom.infra.x.UIMaker.effect.FadeTransition;
import org.freedom.infra.x.UIMaker.effect.SetEffect;

public class TestComponents extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private Button botao01 = null;

	// private Label label01 = null;

	private Panel panel01 = null;

	// private Panel panel02 = null;

	private TextField textField01 = null;

	/**
	 * This is the default constructor
	 */
	public TestComponents() {

		super();
		initialize();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		FadeTransition transition = new FadeTransition(panel01, getImagesDescanso(), 5, 1000, 0.1f);
		transition.doStart();
	}

	private List<Image> getImagesDescanso() {

		List<Image> imagens = new ArrayList<Image>();

		try {
			File subdir = new File("C:\\opt\\eclipse\\workspace\\freedom\\src\\org\\freedom\\images");
			if (subdir.exists()) {
				File[] imgs = subdir.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".jpg") || name.endsWith(".gif");
					}
				});
				int i = 0;
				for (File f : imgs) {
					imagens.add(Toolkit.getDefaultToolkit().getImage(f.toString()));
					i++;
				}
			}
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}

		return imagens;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		this.setSize(400, 400);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {

		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBackground(Color.BLACK);
			jContentPane.add(getTextField01(), null);
			jContentPane.add(getBotao01(), null);
			jContentPane.add(getPanel01(), null);
			/*
			 * jContentPane.add(getPanel02(), null);
			 * jContentPane.add(getLabel01(), null);
			 */
		}
		return jContentPane;
	}

	private Button getBotao01() {

		if (botao01 == null) {
			botao01 = new Button();
			botao01.setBounds(new Rectangle(50, 10, 120, 70));
			botao01.setPressedIcon(new ImageIcon(getClass().getResource("/test/botao_1_press.jpg")));
			botao01.setIcon(new ImageIcon(getClass().getResource("/test/botao_1.jpg")));
			botao01.setBorderPainted(true);
			botao01.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// Effect effect1 = new ShineInBorder( panel01,
					// ShineInBorder.SPEED_FAST );
					Effect effect2 = new Fade(panel01);
					( ( Fade ) effect2 ).setMaxAlfa(0.8f);
					( ( Fade ) effect2 ).setColor(Color.ORANGE);
					( ( Fade ) effect2 ).setAction(Fade.ACTION_OUT);

					SetEffect set = new SetEffect();
					// set.addEffect( effect1 );
					set.addEffect(effect2);
					set.doStart();
					/*
					 * getLabel01().setValue( new Integer( 55 ) );
					 * JOptionPane.showMessageDialog( jContentPane,
					 * getTextField01().getValue() + "\n" +
					 * getTextField01().getValue().getClass().getName() );
					 */
				}
			});

		}
		return botao01;
	}

	/*
	 * private Label getLabel01() {
	 * 
	 * if ( label01 == null ) { label01 = new Label(); label01.setDisplay(
	 * "<a href=#>testes</a><BR>\"<teste 1>\"\nteste 2\nteste 3" );
	 * label01.setBounds(new Rectangle(15, 150, 120, 90)); //label01.setIcon(new
	 * ImageIcon(getClass().getResource("/test/botao_1.jpg")));
	 * //label01.setIconTextGap( 20 ); //label01.setLocation( 14, 105 ); }
	 * return label01; }
	 */
	/**
	 * This method initializes panel01
	 * 
	 * @return javax.swing.JPanel
	 */
	private Panel getPanel01() {

		if (panel01 == null) {
			panel01 = new Panel();
			panel01.setBounds(new Rectangle(40, 90, 400, 200));
			Label teste = new Label("Teste de label");
			panel01.add(teste, 10, 10, 120, 20);
			panel01.setWallpaper(new ImageIcon(getClass().getResource("/test/botao_1.jpg")).getImage());
		}
		return panel01;
	}

	/**
	 * This method initializes panel02
	 * 
	 * @return javax.swing.JPanel
	 */
	/*
	 * private Panel getPanel02() {
	 * 
	 * if ( panel02 == null ) { panel02 = new Panel( ); panel02.setBounds(new
	 * Rectangle(60, 100, 150, 800)); Label teste = new Label(
	 * "TESTE DE LABEL 02" ); teste.setBorder(
	 * BorderFactory.createEtchedBorder() ); panel02.add( teste , 10, 10, 120,
	 * 30 ); panel02.setArc( 25, 25 ); panel02.setBackground( Color.BLACK );
	 * panel02.setOpaque( true ); } return panel02; }
	 */

	/**
	 * This method initializes textField01
	 * 
	 * @return javax.swing.JTextField
	 */
	private TextField getTextField01() {

		if (textField01 == null) {
			textField01 = new TextField(new DecimalDocument(8, 2));
			// textField01 = new TextField( new IntegerDocument() );
			textField01.setBounds(20, 100, 200, 30);
			textField01.setAlfaColor(0.5f, Color.BLUE);
			textField01.setForeground(Color.WHITE);
			// textField01.setFocusable( false );
		}
		return textField01;
	}

	public static void main(String[] args) {
		new TestComponents();
	}

} // @jve:decl-index=0:visual-constraint="187,10"
