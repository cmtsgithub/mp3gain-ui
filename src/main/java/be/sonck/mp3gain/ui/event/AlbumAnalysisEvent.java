package be.sonck.mp3gain.ui.event;

public class AlbumAnalysisEvent extends Mp3GainEvent {

	public AlbumAnalysisEvent(boolean selectedTracksOnly) {
		super(selectedTracksOnly);
	}
}
