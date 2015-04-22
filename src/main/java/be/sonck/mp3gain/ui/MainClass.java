package be.sonck.mp3gain.ui;

import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.event.InitializeEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MainClass {

	public static void main(String[] args) throws Exception {
		// use JVM argument '-Xdock:name="MP3 Gain"' to override the default classname
		// entry in the menu dock
		
		// make sure the frame's menu bar is displayed in the dock instead of the window
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		Injector injector = Guice.createInjector(new Mp3GainModule());
		EventBus eventBus = injector.getInstance(EventBus.class);
		eventBus.post(new InitializeEvent());
	}
}
