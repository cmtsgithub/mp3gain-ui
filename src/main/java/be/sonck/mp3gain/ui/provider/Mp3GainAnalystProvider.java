package be.sonck.mp3gain.ui.provider;

import be.sonck.mp3gain.api.service.Mp3GainAnalyst;
import be.sonck.mp3gain.api.service.Mp3GainAnalystFactory;

import com.google.inject.Provider;

public class Mp3GainAnalystProvider implements Provider<Mp3GainAnalyst> {

	@Override
	public Mp3GainAnalyst get() {
		return Mp3GainAnalystFactory.create();
	}
}
