package be.sonck.mp3gain.ui.event;

import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.MainPresenter;
import be.sonck.mp3gain.ui.api.PlaylistPresenter;
import be.sonck.mp3gain.ui.api.ProgressPresenter;
import be.sonck.mp3gain.ui.api.TrackPresenter;
import be.sonck.mp3gain.ui.progress.MainProgress;
import be.sonck.mp3gain.ui.progress.TrackProgress;

import com.google.inject.Inject;

public class DefaultEventBus implements EventBus {

	private final PlaylistPresenter playlistPresenter;
	private final TrackPresenter trackPresenter;
	private final MainPresenter mainPresenter;
	private final ProgressPresenter mainProgressPresenter;
	private final ProgressPresenter trackProgressPresenter;

	@Inject
	public DefaultEventBus(MainPresenter mainPresenter, PlaylistPresenter playlistPresenter, TrackPresenter trackPresenter,
			@MainProgress ProgressPresenter mainProgressPresenter, @TrackProgress ProgressPresenter trackProgressPresenter) {
		this.mainPresenter = mainPresenter;
		this.playlistPresenter = playlistPresenter;
		this.trackPresenter = trackPresenter;
		this.mainProgressPresenter = mainProgressPresenter;
		this.trackProgressPresenter = trackProgressPresenter;
	}

	@Override
	public void post(Event event) {
		if (event instanceof InitializeEvent) {
			mainPresenter.initialize();

		} else if (event instanceof InitializePlaylistsEvent) {
			playlistPresenter.initialize();

		} else if (event instanceof PlaylistSelectedEvent) {
			trackPresenter.changePlaylist(((PlaylistSelectedEvent) event).getPlaylist());

		} else if (event instanceof VolumeChangedEvent) {
			trackPresenter.changeTargetVolume(((VolumeChangedEvent) event).getVolume());

		} else if (event instanceof WaitEvent) {
			mainPresenter.setBusy(true, ((WaitEvent) event).isAll());

		} else if (event instanceof IdleEvent) {
			mainPresenter.setBusy(false, true);

		} else if (event instanceof MainProgressEvent) {
			MainProgressEvent progressEvent = (MainProgressEvent) event;
			mainProgressPresenter.setProgress(progressEvent.getValue(), progressEvent.getText());

		} else if (event instanceof TrackProgressEvent) {
			TrackProgressEvent progressEvent = (TrackProgressEvent) event;
			trackProgressPresenter.setProgress(progressEvent.getValue(), progressEvent.getText());

		} else if (event instanceof TrackAnalysisEvent) {
			trackPresenter.performTrackAnalysis(((Mp3GainEvent) event).isSelectedTracksOnly());

		} else if (event instanceof AlbumAnalysisEvent) {
			trackPresenter.performAlbumAnalysis(((Mp3GainEvent) event).isSelectedTracksOnly());

		} else if (event instanceof TrackGainEvent) {
			trackPresenter.applyTrackGain(((Mp3GainEvent) event).isSelectedTracksOnly());

		} else if (event instanceof AlbumGainEvent) {
			trackPresenter.applyAlbumGain(((Mp3GainEvent) event).isSelectedTracksOnly());
			
		} else if (event instanceof ErrorEvent) {
			((ErrorEvent) event).getException().printStackTrace();
			post(new TrackProgressEvent(0));
			post(new MainProgressEvent(0));
			post(new IdleEvent());
		}
	}
}
