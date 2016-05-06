package org.freedom.infra.x.swing;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class FDPanel extends JLayeredPane {

	private static final long serialVersionUID = 1l;

	public FDPanel() {
		super();
		initialize();
	}

	public FDPanel(LayoutManager layout) {
		super();
		initialize();
		setLayout(layout);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(98, 18));
	}

	public void add(final JComponent component, final int x, final int y, final int width, final int height) {

		component.setBounds(x, y, width, height);
		add(component, JLayeredPane.DEFAULT_LAYER);
	}

	public void add(final JComponent component, final Point point, final Dimension dimension) {

		add(component, point.x, point.y, dimension.width, dimension.height);
	}

} // @jve:decl-index=0:visual-constraint="10,10"
