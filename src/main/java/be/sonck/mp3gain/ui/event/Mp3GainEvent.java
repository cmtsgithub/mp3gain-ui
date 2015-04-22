package be.sonck.mp3gain.ui.event;

abstract class Mp3GainEvent implements Event {

	private final boolean selectedTracksOnly;

	public Mp3GainEvent(boolean selectedTracksOnly) {
		this.selectedTracksOnly = selectedTracksOnly;
	}

	public boolean isSelectedTracksOnly() {
		return selectedTracksOnly;
	}
}
