package be.sonck.mp3gain.ui.playlist;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

import be.sonck.itunes.api.model.Playlist;
import be.sonck.mp3gain.ui.Constants;

class PlaylistTree extends JTree {

	public PlaylistTree(TreeNode root) {
		super(root);
		
		DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
		treeCellRenderer.setBackgroundSelectionColor(Constants.SELECTION_COLOR);
		treeCellRenderer.setBorderSelectionColor(Constants.SELECTION_COLOR);

		setCellRenderer(treeCellRenderer);
	}

	@Override
	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		if (value instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
			if (userObject instanceof Playlist) {
				return ((Playlist) userObject).getName();
			}
		}

		return value.toString();
	}
}