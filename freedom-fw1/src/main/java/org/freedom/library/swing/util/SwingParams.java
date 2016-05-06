package org.freedom.library.swing.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class SwingParams {

	public static int FONT_SIZE_MIN = 8;

	public static int FONT_SIZE_MED = 11;

	public static int FONT_SIZE_PAD = 12;
	
	public static int FONT_SIZE_MED_MAX = 14;

	public static int FONT_SIZE_MAX = 16;

	public static int FONT_STYLE_PAD = Font.PLAIN;

	public static int FONT_STYLE_BOLD = Font.BOLD;

	public static int FONT_STYLE_ITALIC = Font.ITALIC;

	public static String FONT_PAD = "Arial";

	public static int TAMANHO_FONTE = 0;

	public static Color COR_FUNDO_BARRA_BOTOES = new Color(238, 238, 238);
	
	public static Color COR_VERDE_FREEDOM = new Color( 45, 190, 60 );

	public static Font getFontpad() {
		return new Font(FONT_PAD, FONT_STYLE_PAD, FONT_SIZE_PAD + TAMANHO_FONTE);
	}

	public static Font getFontpadmed() {
		return new Font(FONT_PAD, FONT_STYLE_PAD, FONT_SIZE_MED + TAMANHO_FONTE);
	}
	
	public static Font getFontpadmax() {
		return new Font(FONT_PAD, FONT_STYLE_PAD, FONT_SIZE_MAX + TAMANHO_FONTE);
	}
	
	public static Font getFontpadmedmax() {
		return new Font(FONT_PAD, FONT_STYLE_PAD, FONT_SIZE_MED_MAX + TAMANHO_FONTE);
	}
	
	public static Font getFontboldmedmax() {
		return new Font(FONT_PAD, FONT_STYLE_BOLD, FONT_SIZE_MED_MAX + TAMANHO_FONTE);
	}

	public static Font getFontpadmin() {
		return new Font(FONT_PAD, FONT_STYLE_PAD, FONT_SIZE_MIN + TAMANHO_FONTE);
	}

	public static Font getFontbold() {
		return new Font(FONT_PAD, FONT_STYLE_BOLD, FONT_SIZE_PAD + TAMANHO_FONTE);
	}

	public static Font getFontboldmax() {
		return new Font(FONT_PAD, FONT_STYLE_BOLD, FONT_SIZE_MAX + TAMANHO_FONTE);
	}
	
	public static Font getFontboldmax(int mais) {
		return new Font(FONT_PAD, FONT_STYLE_BOLD, FONT_SIZE_MAX + TAMANHO_FONTE + mais);
	}

	public static Font getFontboldextra(int adic) {
		return new Font(FONT_PAD, FONT_STYLE_BOLD, FONT_SIZE_MAX + adic + TAMANHO_FONTE);
	}

	public static Font getFontboldmed() {
		return new Font(FONT_PAD, FONT_STYLE_BOLD, FONT_SIZE_MED + TAMANHO_FONTE);
	}

	public static Font getFontitalicmed() {
		return new Font(FONT_PAD, FONT_STYLE_ITALIC, FONT_SIZE_MED + TAMANHO_FONTE);
	}

	public static Border blackline = BorderFactory.createLineBorder(Color.black);
	public static Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	public static Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	public static Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	public static Border loweredbevel = BorderFactory.createLoweredBevelBorder();

	public static Border getPanelLabel(String label, Color cortitulo, int justificacao) {
		Border border = BorderFactory.createTitledBorder(loweredetched, label, justificacao, TitledBorder.TOP, SwingParams.getFontbold(), cortitulo);
		return border;

	}

	public static Border getPanelLabel(String label, Color cortitulo) {
		Border border = getPanelLabel(label, cortitulo, TitledBorder.DEFAULT_JUSTIFICATION);
		return border;
	}

	public static Color getVerdeFreedom() {
		return COR_VERDE_FREEDOM;
	}

}
