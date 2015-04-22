package be.sonck.mp3gain.ui.swing;

import be.sonck.mp3gain.ui.api.EventBus;

public final class EventWorker extends MySwingWorker<Void, Void> {

	private final EventCreator eventCreator;

	public EventWorker(EventBus eventBus, EventCreator eventCreator) {
		super(eventBus);
		this.eventCreator = eventCreator;
	}

	@Override
	protected final Void inBackground() throws Exception {
		getEventBus().post(eventCreator.create());
		return null;
	}
}
