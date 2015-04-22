package be.sonck.mp3gain.ui.playlist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.SortedSet;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import be.sonck.itunes.api.model.FolderPlaylist;
import be.sonck.itunes.api.model.Playlist;
import be.sonck.mp3gain.ui.Constants;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.PlaylistView;

import com.google.inject.Inject;

public class DefaultPlaylistView extends JScrollPane implements PlaylistView {

	private final EventBus eventBus;

	@Inject
	public DefaultPlaylistView(EventBus eventBus) {
		this.eventBus = eventBus;
		
		// the scrollpane has a black line border by default
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		// The default preferred size was bigger, but that cause the scrollpane to be cut off
		// when making the window smaller. The whole point of using a scrollpane is so that
		// scrollbars will appear when the window becomes smaller!
		// Adding some space to the preferred width allows some space between the longest playlist
		// name and the right edge of the scrollpane.
		setPreferredSize(new Dimension(250, 100));
	}
	
	@Override
	public void setPlaylists(SortedSet<Playlist> playlists) {
		setViewportView(createTree(playlists));
	}
	
	@Override
	public void setEnabled(boolean flag) {
		if (viewport == null) { return; }
		
		Component view = viewport.getView();
		if (view == null) { return; }
		
		view.setEnabled(flag);
	}

	private JTree createTree(SortedSet<Playlist> playlists) {
		JTree tree = new PlaylistTree(createRootNode(playlists));
		tree.addTreeSelectionListener(new PlaylistSelectionListener(eventBus));
		tree.setRootVisible(false);
		tree.setFont(tree.getFont().deriveFont(Constants.FONT_SIZE));
		return tree;
	}

	private DefaultMutableTreeNode createRootNode(SortedSet<Playlist> playlists) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Playlists");
		addPlaylists(root, playlists);
		return root;
	}

	private static void addPlaylists(DefaultMutableTreeNode parentNode, SortedSet<Playlist> playlists) {
		for (Playlist playlist : playlists) {
			DefaultMutableTreeNode playlistNode = new DefaultMutableTreeNode(playlist);
			parentNode.add(playlistNode);

			if (playlist instanceof FolderPlaylist) {
				addPlaylists(playlistNode, ((FolderPlaylist) playlist).getChildren());
			}
		}
	}

}
