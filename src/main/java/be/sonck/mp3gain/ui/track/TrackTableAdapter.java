package be.sonck.mp3gain.ui.track;

import javax.swing.ListModel;
import javax.swing.SwingConstants;

import be.sonck.mp3gain.api.model.Clipping;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

class TrackTableAdapter extends AbstractTableAdapter<TrackTO> {

	public static final Column[] COLUMNS = new Column[] {
		new Column("Name", 200, SwingConstants.LEFT, "the track's name"),
		new Column("Album", 250, SwingConstants.LEFT, "the album that this track is a part of"),
		new Column("Volume", 0, SwingConstants.RIGHT, "the track's current volume"),
		new Column("dB Modification", 100, SwingConstants.RIGHT, "the dB modification required to bring the track to the desired volume"),
		new Column("Clipping", 0, SwingConstants.CENTER, "indicates whether the track will be clipped after the dB modification is applied"),
		new Column("Max No Clip", 0, SwingConstants.RIGHT, "the maximum volume allowed without clipping the track"),
		new Column("Album Volume", 100, SwingConstants.RIGHT, "the album's current volume"),
		new Column("Album dB Modification", 150, SwingConstants.RIGHT, "the dB modification required to bring the album to the desired volume"),
		new Column("Album Clipping", 100, SwingConstants.CENTER, "indicates whether the track will be clipped after the album dB modification is applied"),
		new Column("Album Max No Clip", 125, SwingConstants.RIGHT, "the maximum volume allowed without clipping any track on the album"),
	};
	
	public TrackTableAdapter(ListModel listModel) {
		super(listModel, getColumnNames());
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TrackTO track = getRow(rowIndex);
		
		switch (columnIndex) {
			case 0: return track.getName();
			case 1: return track.getAlbum();
			case 2: return track.getVolume();
			case 3: return track.getGain();
			case 4: return toString(track.getClipping());
			case 5: return track.getMaxNoClipVolume();
			case 6: return track.getAlbumVolume();
			case 7: return track.getAlbumGain();
			case 8: return toString(track.getAlbumClipping());
			case 9: return track.getAlbumMaxNoClipVolume();
		}
		
		return null;
	}

	private String toString(Clipping clipping) {
		if (clipping == null) { return null; }
		
		switch (clipping) {
			case NO : return "N";
			case YES : return "Y";
			default : return "?";
		}
	}
	
	private static String[] getColumnNames() {
		String[] columnNames = new String[COLUMNS.length];
		
		for (int i = 0; i < COLUMNS.length; i++ ) {
			columnNames[i] = COLUMNS[i].getColumnName();
		}
		
		return columnNames;
	}
}
