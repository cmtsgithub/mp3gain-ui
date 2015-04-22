package be.sonck.mp3gain.ui.track;

import java.math.BigDecimal;

import be.sonck.itunes.api.model.FileTrack;
import be.sonck.mp3gain.api.model.Analysis;
import be.sonck.mp3gain.api.model.Clipping;
import be.sonck.mp3gain.api.service.AnalysisInterpreter;
import be.sonck.mp3gain.api.service.AnalysisInterpreterFactory;

final class TrackTOFactory {

	private TrackTOFactory() {
	}

	public static TrackTO create(FileTrack fileTrack, Analysis analysis, Analysis albumAnalysis, BigDecimal targetVolume) {

		String name = fileTrack.getName();
		String album = fileTrack.getAlbum();
		int trackNumber = fileTrack.getTrackNumber();
		int discNumber = fileTrack.getDiscNumber();
		String location = fileTrack.getLocation().getAbsolutePath();

		AnalysisInterpreter interpreter = AnalysisInterpreterFactory.create(analysis);
		BigDecimal gain = interpreter.determineDbModification(targetVolume);
		BigDecimal volume = interpreter.determineVolume();
		Clipping clipping = interpreter.determineClipping(targetVolume);
		BigDecimal maxNoClipDbGain = interpreter.determineMaxNoClipDbGain();
		BigDecimal maxNoClipVolume = interpreter.determineMaxNoClipDb();

		interpreter = AnalysisInterpreterFactory.create(albumAnalysis);
		BigDecimal albumGain = interpreter.determineDbModification(targetVolume);
		BigDecimal albumVolume = interpreter.determineVolume();
		Clipping albumClipping = interpreter.determineClipping(targetVolume);
		BigDecimal albumMaxNoClipDbGain = interpreter.determineMaxNoClipDbGain();
		BigDecimal albumMaxNoClipVolume = interpreter.determineMaxNoClipDb();

		return new TrackTO(name, album, trackNumber, discNumber, location, volume, gain, clipping, maxNoClipDbGain,
				maxNoClipVolume, albumVolume, albumGain, albumClipping, albumMaxNoClipDbGain, albumMaxNoClipVolume);
	}
}
