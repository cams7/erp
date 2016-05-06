package test;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import org.freedom.infra.x.UIMaker.Panel;
import org.freedom.infra.x.UIMaker.effect.FadeTransition;

public class TesteTransition extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLayeredPane jContentPane = null;

	private Panel fadePanel = null;

	private FadeTransition transition;

	/**
	 * This is the default constructor
	 */
	public TesteTransition() {

		super();

		transition = new FadeTransition(true, getImagesDescanso(), 5, 4000, 0.05f);

		initialize();

		transition.doStart();
	}

	private void initialize() {

		this.setSize(800, 600);
		this.setContentPane(getJContentPane());
		this.setTitle("Fade Transition");

		fadePanel = transition.getFadePanel();
		// fadePanel = new Panel();
		fadePanel.setOpaque(true);

		jContentPane.add(fadePanel);
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

	private JLayeredPane getJContentPane() {

		if (jContentPane == null) {
			jContentPane = new JLayeredPane();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setOpaque(true);
		}
		return jContentPane;
	}
}
