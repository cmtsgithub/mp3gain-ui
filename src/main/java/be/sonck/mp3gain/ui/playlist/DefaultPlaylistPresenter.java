package be.sonck.mp3gain.ui.playlist;

import java.util.SortedSet;

import javax.swing.SwingUtilities;

import be.sonck.itunes.api.model.Playlist;
import be.sonck.itunes.api.service.ITunesBridge;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.PlaylistPresenter;
import be.sonck.mp3gain.ui.api.PlaylistView;
import be.sonck.mp3gain.ui.event.IdleEvent;
import be.sonck.mp3gain.ui.event.WaitEvent;

import com.google.inject.Inject;

public class DefaultPlaylistPresenter implements PlaylistPresenter {

	private final PlaylistView playlistView;
	private final ITunesBridge iTunesBridge;
	private final EventBus eventBus;
	

	@Inject
	public DefaultPlaylistPresenter(EventBus eventBus, PlaylistView playlistView, ITunesBridge iTunesBridge) {
		this.eventBus = eventBus;
		this.playlistView = playlistView;
		this.iTunesBridge = iTunesBridge;
	}
	
	@Override
	public void initialize() {
		eventBus.post(new WaitEvent(true));
		updateView(iTunesBridge.getAllPlaylists());
	}

	private void updateView(final SortedSet<Playlist> playlists) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				playlistView.setPlaylists(playlists);
				eventBus.post(new IdleEvent());
			}
		});
	}
}
