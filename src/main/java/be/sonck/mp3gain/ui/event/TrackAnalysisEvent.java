package be.sonck.mp3gain.ui.event;

public class TrackAnalysisEvent extends Mp3GainEvent {

	public TrackAnalysisEvent(boolean selectedTracksOnly) {
		super(selectedTracksOnly);
	}
}
