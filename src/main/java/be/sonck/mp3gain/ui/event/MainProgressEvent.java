package be.sonck.mp3gain.ui.event;

public class MainProgressEvent extends AbstractProgressEvent {

	public MainProgressEvent(int currentValue, int endValue, String text) {
		super(currentValue, endValue, text);
	}

	public MainProgressEvent(int value, String text) {
		super(value, text);
	}

	public MainProgressEvent(int value) {
		super(value);
	}
}
