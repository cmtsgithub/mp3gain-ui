package be.sonck.mp3gain.ui.event;

public abstract class AbstractProgressEvent implements Event {

	private final int value;
	private final String text;

	public AbstractProgressEvent(int currentValue, int endValue, String text) {
		if (currentValue < 0) {
			throw new IllegalArgumentException("value " + currentValue + " is not a valid progress indicator");
		}

		if (currentValue > endValue) {
			this.value = 10000 / endValue;
		} else {
			this.value = (currentValue * 100) / endValue;
		}

		this.text = text;
	}
	
	public AbstractProgressEvent(int value, String text) {
		this(value, 100, text);
	}

	public AbstractProgressEvent(int value) {
		this(value, null);
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}
}
