package be.sonck.mp3gain.ui;

import be.sonck.itunes.api.service.ITunesBridge;
import be.sonck.mp3gain.api.service.Mp3GainAnalyst;
import be.sonck.mp3gain.api.service.Mp3GainChanger;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.MainPresenter;
import be.sonck.mp3gain.ui.api.MainView;
import be.sonck.mp3gain.ui.api.PlaylistPresenter;
import be.sonck.mp3gain.ui.api.PlaylistView;
import be.sonck.mp3gain.ui.api.ProgressPresenter;
import be.sonck.mp3gain.ui.api.ProgressView;
import be.sonck.mp3gain.ui.api.TrackPresenter;
import be.sonck.mp3gain.ui.api.TrackView;
import be.sonck.mp3gain.ui.api.VolumeView;
import be.sonck.mp3gain.ui.event.DefaultEventBus;
import be.sonck.mp3gain.ui.main.DefaultMainPresenter;
import be.sonck.mp3gain.ui.main.DefaultMainView;
import be.sonck.mp3gain.ui.menu.Mp3GainMenuBar;
import be.sonck.mp3gain.ui.menu.Mp3GainPopupMenu;
import be.sonck.mp3gain.ui.playlist.DefaultPlaylistPresenter;
import be.sonck.mp3gain.ui.playlist.DefaultPlaylistView;
import be.sonck.mp3gain.ui.progress.DefaultProgressView;
import be.sonck.mp3gain.ui.progress.MainProgress;
import be.sonck.mp3gain.ui.progress.MainProgressPresenter;
import be.sonck.mp3gain.ui.progress.TrackProgress;
import be.sonck.mp3gain.ui.progress.TrackProgressPresenter;
import be.sonck.mp3gain.ui.provider.ITunesBridgeProvider;
import be.sonck.mp3gain.ui.provider.Mp3GainAnalystProvider;
import be.sonck.mp3gain.ui.provider.Mp3GainChangerProvider;
import be.sonck.mp3gain.ui.track.DefaultTrackPresenter;
import be.sonck.mp3gain.ui.track.DefaultTrackProcessorFactory;
import be.sonck.mp3gain.ui.track.DefaultTrackView;
import be.sonck.mp3gain.ui.track.TrackProcessorFactory;
import be.sonck.mp3gain.ui.volume.DefaultVolumeView;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class Mp3GainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MainView.class).to(DefaultMainView.class).in(Scopes.SINGLETON);
		bind(MainPresenter.class).to(DefaultMainPresenter.class).in(Scopes.SINGLETON);
		
		bind(Mp3GainMenuBar.class).in(Scopes.SINGLETON);
		bind(Mp3GainPopupMenu.class).in(Scopes.SINGLETON);
		
		bind(PlaylistView.class).to(DefaultPlaylistView.class).in(Scopes.SINGLETON);
		bind(PlaylistPresenter.class).to(DefaultPlaylistPresenter.class).in(Scopes.SINGLETON);
		
		bind(TrackPresenter.class).to(DefaultTrackPresenter.class).in(Scopes.SINGLETON);
		bind(TrackView.class).to(DefaultTrackView.class).in(Scopes.SINGLETON);
		bind(TrackProcessorFactory.class).to(DefaultTrackProcessorFactory.class).in(Scopes.SINGLETON);
		
		bind(ProgressPresenter.class).annotatedWith(MainProgress.class).to(MainProgressPresenter.class).in(Scopes.SINGLETON);
		bind(ProgressPresenter.class).annotatedWith(TrackProgress.class).to(TrackProgressPresenter.class).in(Scopes.SINGLETON);
		bind(ProgressView.class).annotatedWith(MainProgress.class).toInstance(new DefaultProgressView());
		bind(ProgressView.class).annotatedWith(TrackProgress.class).toInstance(new DefaultProgressView());
		
		bind(VolumeView.class).to(DefaultVolumeView.class).in(Scopes.SINGLETON);
		
		bind(ITunesBridge.class).toProvider(ITunesBridgeProvider.class).in(Scopes.SINGLETON);
		bind(Mp3GainAnalyst.class).toProvider(Mp3GainAnalystProvider.class).in(Scopes.SINGLETON);
		bind(Mp3GainChanger.class).toProvider(Mp3GainChangerProvider.class).in(Scopes.SINGLETON);
		
		bind(EventBus.class).to(DefaultEventBus.class).in(Scopes.SINGLETON);
	}
}
