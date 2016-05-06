package org.freedom.infra.x.UIMaker;

import java.awt.Color;
import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;

import org.freedom.infra.x.swing.FDLabel;

public class Label extends FDLabel {

	private static final long serialVersionUID = 1l;

	public Label() {
		super();
	}

	public Label(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public Label(Icon image) {
		super(image);
	}

	public Label(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public Label(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public Label(String text) {
		super(text);
	}

	public String getDisplay() {
		String text = super.getText();
		text = text.replaceAll("<.*?>", "").replaceAll("</?br>", "\n");
		return text;
	}

	public void setDisplay(String text) {

		if (text != null) {
			String texttmp = text.replaceAll("\n", "<br>");
			Pattern p = Pattern.compile("<.*?>");
			Matcher m = p.matcher(texttmp);
			if (m.find()) {
				texttmp = texttmp.replaceAll("</?html>|</?body>|</?HTML>|</?BODY>", "");
				text = "<html><body>" + texttmp + "</body></html>";
			}
		}

		super.setText(text);
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		// setOpaque( bg != null );
	}

	@Override
	public void setDisabledIcon(Icon disabledIcon) {
		super.setDisabledIcon(disabledIcon);
		if (disabledIcon != null) {
			Icon icon = getIcon() == null ? disabledIcon : getIcon();
			if (icon != null) {
				setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
				setSize(getPreferredSize());
			}
		}
	}

	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
		if (icon != null) {
			setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			setSize(getPreferredSize());
		}
	}

}
