package be.sonck.mp3gain.ui.provider;

import be.sonck.itunes.api.service.ITunesBridge;
import be.sonck.itunes.api.service.ITunesBridgeFactory;

import com.google.inject.Provider;

public class ITunesBridgeProvider implements Provider<ITunesBridge> {

	@Override
	public ITunesBridge get() {
		return ITunesBridgeFactory.create();
	}
}
