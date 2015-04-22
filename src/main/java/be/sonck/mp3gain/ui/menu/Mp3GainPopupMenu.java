package be.sonck.mp3gain.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import be.sonck.mp3gain.ui.Constants;
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

public class Mp3GainPopupMenu extends JPopupMenu {

	private final EventBus eventBus;

	@Inject
	public Mp3GainPopupMenu(EventBus eventBus) {
		this.eventBus = eventBus;
		
//		add(createTrackAnalysisMenuItem());
//		add(createAlbumAnalysisMenuItem());
		add(createTrackGainMenuItem());
		add(createAlbumGainMenuItem());
	}

	private JMenuItem createTrackAnalysisMenuItem() {
		return createMenuItem("Perform Track Analysis For Selected Tracks", new EventCreator() {
			@Override
			public Event create() {
				return new TrackAnalysisEvent(true);
			}
		});
	}
	
	private JMenuItem createAlbumAnalysisMenuItem() {
		return createMenuItem("Perform Album Analysis For Selected Tracks", new EventCreator() {
			@Override
			public Event create() {
				return new AlbumAnalysisEvent(true);
			}
		});
	}
	
	private JMenuItem createTrackGainMenuItem() {
		return createMenuItem("Apply Track Gain To Selected Tracks", new EventCreator() {
			@Override
			public Event create() {
				return new TrackGainEvent(true);
			}
		});
	}
	
	private JMenuItem createAlbumGainMenuItem() {
		return createMenuItem("Apply Album Gain To Selected Tracks", new EventCreator() {
			@Override
			public Event create() {
				return new AlbumGainEvent(true);
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
		
		menuItem.setFont(getFont().deriveFont(Constants.FONT_SIZE));
		
		return menuItem;
	}
}
