package be.sonck.mp3gain.ui.track;

import be.sonck.itunes.api.model.Playlist;

public interface TrackProcessorFactory {

	TrackProcessor create(Playlist playlist);
}
