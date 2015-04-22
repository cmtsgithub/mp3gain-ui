package be.sonck.mp3gain.ui.track;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import be.sonck.itunes.api.model.Playlist;
import be.sonck.mp3gain.ui.Constants;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.TrackPresenter;
import be.sonck.mp3gain.ui.api.TrackView;
import be.sonck.mp3gain.ui.event.IdleEvent;
import be.sonck.mp3gain.ui.event.MainProgressEvent;
import be.sonck.mp3gain.ui.event.WaitEvent;

import com.google.inject.Inject;

public class DefaultTrackPresenter implements TrackPresenter {

	private static final String UPDATING_VOLUME = "Updating Volume";
	private static final String RETRIEVING_TRACKS = "Retrieving Tracks";
	private static final String ANALYZING_TRACKS = "Analyzing Tracks";
	private static final String APPLYING_TRACK_GAIN = "Applying Track Gain";
	private static final String APPLYING_ALBUM_GAIN = "Applying Album Gain";

	private final EventBus eventBus;
	private final TrackView trackView;
	private final TrackProcessorFactory trackProcessorFactory;

	private BigDecimal targetVolume = Constants.DEFAULT_VOLUME;

	private TrackProcessor trackProcessor;

	private class DefaultTrackProcessorListener implements TrackProcessorListener {

		private final String message;

		public DefaultTrackProcessorListener(String message) {
			this.message = message;
		}

		@Override
		public void tracksProcessed(Collection<TrackTO> tracks, int currentIndex, int lastIndex) {
			eventBus.post(new MainProgressEvent(currentIndex, lastIndex, message));
			updateTracksInView(tracks);
		}

		@Override
		public void done() {
			eventBus.post(new MainProgressEvent(0));
			eventBus.post(new IdleEvent());
		}
	}

	@Inject
	public DefaultTrackPresenter(EventBus eventBus, TrackProcessorFactory trackProcessorFactory, TrackView trackView) {
		this.eventBus = eventBus;
		this.trackProcessorFactory = trackProcessorFactory;
		this.trackView = trackView;
	}

	@Override
	public void changeTargetVolume(BigDecimal targetVolume) {
		this.targetVolume = targetVolume;

		if (trackProcessor != null) {
			eventBus.post(new WaitEvent(false));
			eventBus.post(new MainProgressEvent(0, UPDATING_VOLUME));

			trackProcessor.getTracks(targetVolume, new DefaultTrackProcessorListener(UPDATING_VOLUME));
		}
	}

	@Override
	public void changePlaylist(final Playlist playlist) {
		if (trackProcessor != null) {
			trackProcessor.cancel();
		}
		
		eventBus.post(new WaitEvent(false));
		eventBus.post(new MainProgressEvent(0, RETRIEVING_TRACKS));
		clearView();

		trackProcessor = trackProcessorFactory.create(playlist);
		trackProcessor.getTracks(targetVolume, new DefaultTrackProcessorListener(RETRIEVING_TRACKS));
	}

	@Override
	public void performTrackAnalysis(boolean selectedTracksOnly) {
		eventBus.post(new WaitEvent(true));
		eventBus.post(new MainProgressEvent(0, ANALYZING_TRACKS));
		
		List<TrackTO> tracks = (selectedTracksOnly ? trackView.getSelectedTracks() : trackView.getTracks());
		trackProcessor.performTrackAnalysis(tracks, targetVolume, new DefaultTrackProcessorListener(ANALYZING_TRACKS));
	}

	@Override
	public void performAlbumAnalysis(boolean selectedTracksOnly) {
		eventBus.post(new WaitEvent(true));
		eventBus.post(new MainProgressEvent(0, ANALYZING_TRACKS));
		
		List<TrackTO> tracks = (selectedTracksOnly ? trackView.getSelectedTracks() : trackView.getTracks());
		trackProcessor.performAlbumAnalysis(tracks, targetVolume, new DefaultTrackProcessorListener(ANALYZING_TRACKS));
	}

	@Override
	public void applyTrackGain(boolean selectedTracksOnly) {
		eventBus.post(new WaitEvent(true));
		eventBus.post(new MainProgressEvent(0, APPLYING_TRACK_GAIN));
		
		List<TrackTO> tracks = (selectedTracksOnly ? trackView.getSelectedTracks() : trackView.getTracks());
		trackProcessor.applyTrackGain(tracks, targetVolume, new DefaultTrackProcessorListener(APPLYING_TRACK_GAIN));
	}

	@Override
	public void applyAlbumGain(boolean selectedTracksOnly) {
		eventBus.post(new WaitEvent(true));
		eventBus.post(new MainProgressEvent(0, APPLYING_ALBUM_GAIN));
		
		List<TrackTO> tracks = (selectedTracksOnly ? trackView.getSelectedTracks() : trackView.getTracks());
		trackProcessor.applyAlbumGain(tracks, targetVolume, new DefaultTrackProcessorListener(APPLYING_ALBUM_GAIN));
	}

	private void clearView() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				trackView.clearTracks();
			}
		});
	}

	private void updateTracksInView(final Collection<TrackTO> tracks) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				trackView.removeTracks(tracks);
				trackView.addTracks(tracks);
			}
		});
	}
}
