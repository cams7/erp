
package org.freedom.tef.driver.dedicate;

import org.freedom.tef.app.TefAction;

public class DedicatedTefEvent {

	private DedicatedTefListener dedicateTefListener;

	private TefAction action;

	private String message;

	public DedicatedTefEvent( final DedicatedTefListener dedicateTefListener, final TefAction action ) {

		this( dedicateTefListener, action, null );
	}

	public DedicatedTefEvent( final DedicatedTefListener dedicateTefListener, final TefAction action, final String message ) {

		super();
		this.dedicateTefListener = dedicateTefListener;
		this.action = action;
		this.message = message;
	}

	public DedicatedTefListener getSource() {

		return dedicateTefListener;
	}

	public TefAction getAction() {

		return action;
	}

	public String getMessage() {

		return this.message;
	}
}
