package be.sonck.mp3gain.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.event.AlbumAnalysisEvent;
import be.sonck.mp3gain.ui.event.AlbumGainEvent;
import be.sonck.mp3gain.ui.event.Event;
import be.sonck.mp3gain.ui.event.TrackAnalysisEvent;
import be.sonck.mp3gain.ui.event.TrackGainEvent;
import be.sonck.mp3gain.ui.swing.EventCreator;
import be.sonck.mp3gain.ui.swing.EventWorker;
import be.sonck.mp3gain.ui.swing.SwingWorkerManager;

import com.google.inject.Inject;

public class Mp3GainMenuBar extends JMenuBar {

	private final EventBus eventBus;
	
	@Inject
	public Mp3GainMenuBar(EventBus eventBus) {
		this.eventBus = eventBus;
		add(createFileMenu());
	}

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		
		menu.add(createTrackAnalysisMenuItem());
		menu.add(createAlbumAnalysisMenuItem());
		menu.addSeparator();
		menu.add(createTrackGainMenuItem());
		menu.add(createAlbumGainMenuItem());
		
		return menu;
	}

	private JMenuItem createAlbumGainMenuItem() {
		return createMenuItem("Apply Album Gain", new EventCreator() {
			@Override
			public Event create() {
				return new AlbumGainEvent(false);
			}
		});
	}

	private JMenuItem createTrackGainMenuItem() {
		return createMenuItem("Apply Track Gain", new EventCreator() {
			@Override
			public Event create() {
				return new TrackGainEvent(false);
			}
		});
	}

	private JMenuItem createAlbumAnalysisMenuItem() {
		return createMenuItem("Perform Album Analysis", new EventCreator() {
			@Override
			public Event create() {
				return new AlbumAnalysisEvent(false);
			}
		});
	}

	private JMenuItem createTrackAnalysisMenuItem() {
		return createMenuItem("Perform Track Analysis", new EventCreator() {
			@Override
			public Event create() {
				return new TrackAnalysisEvent(false);
			}
		});
	}

	private JMenuItem createMenuItem(String text, final EventCreator eventCreator) {
		JMenuItem menuItem = new JMenuItem(text);
		
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				SwingWorkerManager.getInstance().execute(new EventWorker(eventBus, eventCreator));
			}
		});
		
		return menuItem;
	}
}
