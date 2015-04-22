package be.sonck.mp3gain.ui.main;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.api.MainView;
import be.sonck.mp3gain.ui.api.PlaylistView;
import be.sonck.mp3gain.ui.api.ProgressView;
import be.sonck.mp3gain.ui.api.TrackView;
import be.sonck.mp3gain.ui.api.VolumeView;
import be.sonck.mp3gain.ui.event.Event;
import be.sonck.mp3gain.ui.event.InitializePlaylistsEvent;
import be.sonck.mp3gain.ui.menu.Mp3GainMenuBar;
import be.sonck.mp3gain.ui.progress.MainProgress;
import be.sonck.mp3gain.ui.progress.TrackProgress;
import be.sonck.mp3gain.ui.swing.EventCreator;
import be.sonck.mp3gain.ui.swing.EventWorker;
import be.sonck.mp3gain.ui.swing.SwingWorkerManager;

import com.google.inject.Inject;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class DefaultMainView extends JFrame implements MainView {

	private static final String BORDER = "5dlu";

	private final TrackView trackView;
	private final PlaylistView playlistView;
	private final VolumeView volumeView;
	private final EventBus eventBus;
	private final ProgressView mainProgressView;
	private final ProgressView trackProgressView;
	private final Mp3GainMenuBar menuBar;


	@Inject
	public DefaultMainView(EventBus eventBus, PlaylistView playlistView, TrackView trackView, VolumeView volumeView,
			@MainProgress ProgressView mainProgressView, @TrackProgress ProgressView trackProgressView, Mp3GainMenuBar menuBar) {
		super("MP3 Gain");

		this.eventBus = eventBus;
		this.playlistView = playlistView;
		this.trackView = trackView;
		this.volumeView = volumeView;
		this.mainProgressView = mainProgressView;
		this.trackProgressView = trackProgressView;
		this.menuBar = menuBar;
	}

	@Override
	public void initialize() {
		SwingWorkerManager.getInstance().execute(new EventWorker(eventBus, new EventCreator() {
			@Override
			public Event create() {
				return new InitializePlaylistsEvent();
			}
		}));

		setPreferredSize(new Dimension(1550, 900));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(createPanel());
		setJMenuBar(menuBar);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void setBusy(boolean busy) {
		if (busy) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private JPanel createPanel() {
		String columnSpecs = BORDER + ", fill:pref:grow, " + BORDER;
		String rowSpecs = BORDER + ", pref, " + BORDER + ", fill:pref:grow, " + BORDER + ", pref, pref, " + BORDER;

		JPanel panel = new JPanel(new FormLayout(columnSpecs, rowSpecs));
		panel.add((Component) volumeView, CC.xy(2, 2));
		panel.add(createSplitPane(), CC.xy(2, 4));
		panel.add((Component) trackProgressView, CC.xy(2, 6));
		panel.add((Component) mainProgressView, CC.xy(2, 7));

		return panel;
	}

	private JSplitPane createSplitPane() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, (Component) playlistView,
				(Component) trackView);

		// the splitpane has an ugly line border by default
		splitPane.setBorder(BorderFactory.createEmptyBorder());

		return splitPane;
	}
}
