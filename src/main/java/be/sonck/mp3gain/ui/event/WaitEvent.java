package be.sonck.mp3gain.ui.event;


public class WaitEvent implements Event {

	private final boolean all;

	public WaitEvent(boolean all) {
		this.all = all;
	}

	public boolean isAll() {
		return all;
	}
}
