package be.sonck.mp3gain.ui.track;

import java.util.Collection;

public interface TrackProcessorListener {

	void tracksProcessed(Collection<TrackTO> tracks, int currentIndex, int lastIndex);
	void done();
}
