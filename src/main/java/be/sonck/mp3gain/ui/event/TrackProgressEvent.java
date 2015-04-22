package be.sonck.mp3gain.ui.event;

public class TrackProgressEvent extends AbstractProgressEvent {

	public TrackProgressEvent(int currentValue, int endValue, String text) {
		super(currentValue, endValue, text);
	}

	public TrackProgressEvent(int value, String text) {
		super(value, text);
	}

	public TrackProgressEvent(int value) {
		super(value);
	}
}
