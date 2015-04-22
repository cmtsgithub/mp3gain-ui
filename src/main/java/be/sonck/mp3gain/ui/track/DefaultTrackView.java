package be.sonck.mp3gain.ui.track;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListModel;

import be.sonck.mp3gain.ui.api.TrackView;
import be.sonck.mp3gain.ui.menu.Mp3GainPopupMenu;

import com.google.inject.Inject;
import com.jgoodies.common.collect.ArrayListModel;

public class DefaultTrackView extends JScrollPane implements TrackView {
	
	private static final Comparator<TrackTO> COMPARATOR = new TrackTOComparator();
	
	private final Mp3GainPopupMenu popupMenu;
	private List<TrackTO> tracks;

	@Inject
	public DefaultTrackView(Mp3GainPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
		
		clearTracks();
		
		// the scrollpane has a black line border by default
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		// The default preferred size was bigger, but that cause the scrollpane to be cut off
		// when making the window smaller. The whole point of using a scrollpane is so that
		// scrollbars will appear when the window becomes smaller!
		setPreferredSize(new Dimension(100, 100));
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (viewport == null) { return; }
		
		Component view = viewport.getView();
		if (view == null) { return; }
		
		view.setEnabled(enabled);
	}
	
	@Override
	public void clearTracks() {
		tracks = new ArrayListModel<TrackTO>();
		setViewportView(createTable((ListModel) tracks));
	}


	@Override
	public void addTracks(Collection<TrackTO> tracks) {
		this.tracks.addAll(tracks);
		Collections.sort(this.tracks, COMPARATOR);
	}

	@Override
	public void removeTracks(Collection<TrackTO> tracks) {
		this.tracks.removeAll(tracks);
	}
	
	@Override
	public List<TrackTO> getTracks() {
		return Collections.unmodifiableList(this.tracks);
	}

	@Override
	public List<TrackTO> getSelectedTracks() {
		List<TrackTO> selectedTracks = new ArrayList<TrackTO>();
		
		TrackTable trackTable = getTrackTable();
		TrackTableAdapter trackTableAdapter = (TrackTableAdapter) trackTable.getModel();
		int[] selectedRows = trackTable.getSelectedRows();
		
		for (int i = 0; i < selectedRows.length; i++) {
			selectedTracks.add(trackTableAdapter.getRow(selectedRows[i]));
		}
		
		return selectedTracks;
	}

	private TrackTable getTrackTable() {
		JViewport viewport = getViewport();
		if (viewport == null) { return null; }
		
		Component view = viewport.getView();
		if (!(view instanceof TrackTable)) { return null; }
		
		return (TrackTable) view;
	}

	private Component createTable(ListModel tracks) {
		TrackTable trackTable = new TrackTable(new TrackTableAdapter(tracks));
		
		trackTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		
		return trackTable;
	}
}
