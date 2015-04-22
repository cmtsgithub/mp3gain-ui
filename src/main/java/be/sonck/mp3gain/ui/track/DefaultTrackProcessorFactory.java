package be.sonck.mp3gain.ui.track;

import be.sonck.itunes.api.model.Playlist;
import be.sonck.itunes.api.service.ITunesBridge;
import be.sonck.mp3gain.api.service.Mp3GainAnalyst;
import be.sonck.mp3gain.api.service.Mp3GainChanger;
import be.sonck.mp3gain.ui.api.EventBus;

import com.google.inject.Inject;

public class DefaultTrackProcessorFactory implements TrackProcessorFactory {
	
	private final ITunesBridge iTunesBridge;
	private final Mp3GainAnalyst mp3GainAnalyst;
	private final EventBus eventBus;
	private final Mp3GainChanger mp3GainChanger;

	
	@Inject
	public DefaultTrackProcessorFactory(ITunesBridge iTunesBridge, Mp3GainAnalyst mp3GainAnalyst, Mp3GainChanger mp3GainChanger, EventBus eventBus) {
		this.iTunesBridge = iTunesBridge;
		this.mp3GainAnalyst = mp3GainAnalyst;
		this.mp3GainChanger = mp3GainChanger;
		this.eventBus = eventBus;
	}


	@Override
	public TrackProcessor create(Playlist playlist) {
		return new TrackProcessor(iTunesBridge, mp3GainAnalyst, mp3GainChanger, eventBus, playlist);
	}
}
