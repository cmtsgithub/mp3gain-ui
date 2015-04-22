package be.sonck.mp3gain.ui.playlist;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import be.sonck.itunes.api.model.FolderPlaylist;
import be.sonck.itunes.api.model.Playlist;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.event.Event;
import be.sonck.mp3gain.ui.event.PlaylistSelectedEvent;
import be.sonck.mp3gain.ui.swing.EventCreator;
import be.sonck.mp3gain.ui.swing.EventWorker;
import be.sonck.mp3gain.ui.swing.SwingWorkerManager;

class PlaylistSelectionListener implements TreeSelectionListener {
	
	private final EventBus eventBus;
	
	public PlaylistSelectionListener(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		Object node = event.getPath().getLastPathComponent();
		if (node instanceof DefaultMutableTreeNode) { 
			Object userObject = ((DefaultMutableTreeNode) node).getUserObject();
			if (userObject instanceof Playlist && !(userObject instanceof FolderPlaylist)) {
				postEvent((Playlist) userObject);
			}
		}
	}

	private void postEvent(final Playlist playlist) {
		SwingWorkerManager.getInstance().execute(new EventWorker(eventBus, new EventCreator() {
			@Override
			public Event create() {
				return new PlaylistSelectedEvent(playlist);
			}
		}));
	}
}