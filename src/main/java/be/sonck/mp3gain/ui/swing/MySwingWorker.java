package be.sonck.mp3gain.ui.swing;

import javax.swing.SwingWorker;

import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.event.ErrorEvent;
import be.sonck.mp3gain.ui.event.IdleEvent;

public abstract class MySwingWorker<T, V> extends SwingWorker<T, V> {
	
	private final EventBus eventBus;

	public MySwingWorker(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	protected final T doInBackground() throws Exception {
		try {
			return inBackground();
		} catch (Exception e) {
			eventBus.post(new ErrorEvent(e));
			eventBus.post(new IdleEvent());
			throw e;
		}
	}

	protected EventBus getEventBus() {
		return eventBus;
	}

	protected abstract T inBackground() throws Exception;
}
