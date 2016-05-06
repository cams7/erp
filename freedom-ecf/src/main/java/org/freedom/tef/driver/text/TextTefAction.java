
package org.freedom.tef.driver.text;

import org.freedom.tef.app.TefAction;

public class TextTefAction implements TefAction {

	public static final TextTefAction ERROR = new TextTefAction( 100 );

	public static final TextTefAction WARNING = new TextTefAction( 101 );

	public static final TextTefAction CONFIRM = new TextTefAction( 102 );

	public static final TextTefAction BEGIN_PRINT = new TextTefAction( 200 );

	public static final TextTefAction PRINT = new TextTefAction( 201 );

	public static final TextTefAction END_PRINT = new TextTefAction( 202 );

	public static final TextTefAction RE_PRINT = new TextTefAction( 203 );

	private int code;

	private TextTefAction( int code ) {

		this.code = code;
	}

	public int code() {

		return this.code;
	}

	@Override
	public boolean equals( Object obj ) {

		if ( obj == null || !( obj instanceof TextTefAction ) ) {
			return false;
		}
		return this.code == ( (TextTefAction) obj ).code();
	}
}
