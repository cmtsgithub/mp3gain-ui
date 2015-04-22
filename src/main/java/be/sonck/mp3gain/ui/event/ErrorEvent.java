package be.sonck.mp3gain.ui.event;

public class ErrorEvent implements Event {

	private final Exception exception;

	public ErrorEvent(Exception exception) {
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
}
