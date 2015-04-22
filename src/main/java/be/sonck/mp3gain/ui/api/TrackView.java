package be.sonck.mp3gain.ui.api;

import java.util.Collection;
import java.util.List;

import be.sonck.mp3gain.ui.track.TrackTO;


public interface TrackView {

	void clearTracks();
	void addTracks(Collection<TrackTO> tracks);
	void removeTracks(Collection<TrackTO> tracks);
	List<TrackTO> getSelectedTracks();
	List<TrackTO> getTracks();
	void setEnabled(boolean enabled);
}
