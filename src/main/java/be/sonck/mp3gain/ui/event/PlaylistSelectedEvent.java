package be.sonck.mp3gain.ui.event;

import be.sonck.itunes.api.model.Playlist;

public class PlaylistSelectedEvent implements Event {

	private final Playlist playlist;

	public PlaylistSelectedEvent(Playlist playlist) {
		this.playlist = playlist;
	}

	public Playlist getPlaylist() {
		return playlist;
	}
}
