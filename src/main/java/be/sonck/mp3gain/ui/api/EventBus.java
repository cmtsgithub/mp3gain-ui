package be.sonck.mp3gain.ui.api;

import be.sonck.mp3gain.ui.event.Event;

public interface EventBus {

	void post(Event event);
}
