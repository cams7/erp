
package org.freedom.tef.app;

public class ControllerTefEvent {

	private ControllerTef controllerTef;

	private TefAction action;

	private String message;

	public ControllerTefEvent( final ControllerTef controllerTef, final TefAction action ) {

		this( controllerTef, action, null );
	}

	public ControllerTefEvent( final ControllerTef controllerTef, final TefAction action, final String message ) {

		super();
		this.controllerTef = controllerTef;
		this.action = action;
		this.message = message;
	}

	public ControllerTef getSource() {

		return controllerTef;
	}

	public TefAction getAction() {

		return action;
	}

	public String getMessage() {

		return message;
	}
}
