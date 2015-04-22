package be.sonck.mp3gain.ui.event;

public class TrackGainEvent extends Mp3GainEvent {

	public TrackGainEvent(boolean selectedTracksOnly) {
		super(selectedTracksOnly);
	}
}
