package be.sonck.mp3gain.ui.api;

import java.util.SortedSet;

import be.sonck.itunes.api.model.Playlist;

public interface PlaylistView {

	void setPlaylists(SortedSet<Playlist> playlists);
	void setEnabled(boolean enabled);
}
