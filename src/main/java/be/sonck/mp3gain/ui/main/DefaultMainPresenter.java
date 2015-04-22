package be.sonck.mp3gain.ui.main;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import be.sonck.mp3gain.ui.api.MainPresenter;
import be.sonck.mp3gain.ui.api.MainView;
import be.sonck.mp3gain.ui.api.PlaylistView;
import be.sonck.mp3gain.ui.api.TrackView;
import be.sonck.mp3gain.ui.api.VolumeView;
import be.sonck.mp3gain.ui.menu.Mp3GainMenuBar;

import com.google.inject.Inject;

public class DefaultMainPresenter implements MainPresenter {

	private final MainView mainView;
	private final VolumeView volumeView;
	private final Mp3GainMenuBar menuBar;
	private final PlaylistView playlistView;
	private final TrackView trackView;

	@Inject
	public DefaultMainPresenter(MainView mainView, VolumeView volumeView, Mp3GainMenuBar menuBar, PlaylistView playlistView, TrackView trackView) {
		this.mainView = mainView;
		this.volumeView = volumeView;
		this.menuBar = menuBar;
		this.playlistView = playlistView;
		this.trackView = trackView;
	}
	
	@Override
	public void initialize() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainView.initialize();
			}
		});
	}

	@Override
	public void setBusy(final boolean busy, final boolean all) {
		if (SwingUtilities.isEventDispatchThread()) {
			doSetBusy(busy, all);
			
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						doSetBusy(busy, all);
					}
				});
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void doSetBusy(boolean busy, boolean all) {
		mainView.setBusy(busy);
		volumeView.setEnabled(!busy);
		menuBar.setEnabled(!busy);
		
		if (all) {
			playlistView.setEnabled(!busy);
		}
	}
}
