package org.freedom.infra.x.swing;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class JMyComboBoxRenderer extends BasicComboBoxRenderer {
	Vector<String> label;
	
	public JMyComboBoxRenderer(Vector<String> label ) {
		this.label = label;
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
			if (-1 < index) {
				list.setToolTipText(label.get(index));
			}
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setFont(list.getFont());
		setText((value == null) ? "" : value.toString());
		return this;
	}
}
