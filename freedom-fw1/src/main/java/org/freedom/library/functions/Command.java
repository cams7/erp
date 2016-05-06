package org.freedom.library.functions;

public class Command {

	public static final Command STX = new Command(0x02);

	public static final Command CR = new Command(0x0D);

	public static final Command E = new Command(0x45);

	public static final Command G = new Command(0x47);

	public static final Command L = new Command(0x4C);

	public static final Command ORDINANCE_DEFAULT = new Command(0x31);

	public static final Command ORDINANCE_ROTATION_270 = new Command(0x32);

	public static final Command ORDINANCE_ROTATION_180 = new Command(0x33);

	public static final Command ORDINANCE_ROTATION_90 = new Command(0x34);

	// Fonts ...

	public static final Command FONT_DEFAULT = new Command(0x32);

	public static final Command SUBTYPE_FONT_DEFAULT = new Command(0x30);

	public static final Command HORIZONTAL_MULTIPLIER_DEFAULT = new Command(0x31);

	public static final Command VERTICAL_MULTIPLIER_DEFAULT = new Command(0x31);

	public static final Command FONT_Roman8 = new Command(0x30);

	public static final Command FONT_ECMA94 = new Command(0x31);

	public static final Command FONT_PC_set = new Command(0x32);

	public static final Command FONT_PC_set_A = new Command(0x33);

	public static final Command FONT_PC_set_B = new Command(0x34);

	public static final Command FONT_LEGAL = new Command(0x35);

	public static final Command SUBTYPE_FONT_4_POINTS = new Command(0x30);

	public static final Command SUBTYPE_FONT_6_POINTS = new Command(0x31);

	public static final Command SUBTYPE_FONT_8_POINTS = new Command(0x32);

	public static final Command SUBTYPE_FONT_10_POINTS = new Command(0x33);

	public static final Command SUBTYPE_FONT_12_POINTS = new Command(0x34);

	public static final Command SUBTYPE_FONT_14_POINTS = new Command(0x35);

	public static final Command SUBTYPE_FONT_18_POINTS = new Command(0x36);

	public static final Command SUBTYPE_FONT_24_POINTS = new Command(0x37);

	// Barcode ...

	public static final Command BARCODE_A = new Command('A');

	public static final Command BARCODE_B = new Command('B');

	public static final Command BARCODE_C = new Command('C');

	public static final Command BARCODE_a = new Command('a');

	public static final Command BARCODE_b = new Command('b');

	public static final Command BARCODE_c = new Command('c');

	public static final Command BARCODE_WIDE_BAR_DEFAULT = new Command(0x35);

	public static final Command BARCODE_FINE_BAR_DEFAULT = new Command(0x32);

	/**
	 * Armazena o valor hexadecimal do comando.
	 */
	private int command;

	public Command(int command) {

		this.command = command;
	}

	/**
	 * 
	 * @return inteiro com o valor hexadecimal do comando.
	 */
	public char getCommand() {

		return ( char ) this.command;
	}
}
