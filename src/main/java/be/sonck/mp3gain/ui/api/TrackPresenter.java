package be.sonck.mp3gain.ui.api;

import java.math.BigDecimal;

import be.sonck.itunes.api.model.Playlist;

public interface TrackPresenter {

	void changePlaylist(Playlist playlist);
	void changeTargetVolume(BigDecimal targetVolume);
	void performTrackAnalysis(boolean selectedTracksOnly);
	void performAlbumAnalysis(boolean selectedTracksOnly);
	void applyTrackGain(boolean selectedTracksOnly);
	void applyAlbumGain(boolean selectedTracksOnly);
}
