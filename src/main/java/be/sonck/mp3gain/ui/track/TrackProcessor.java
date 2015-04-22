package be.sonck.mp3gain.ui.track;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import be.sonck.itunes.api.model.FileTrack;
import be.sonck.itunes.api.model.Playlist;
import be.sonck.itunes.api.service.ITunesBridge;
import be.sonck.mp3gain.api.model.AlbumAnalysis;
import be.sonck.mp3gain.api.model.Analysis;
import be.sonck.mp3gain.api.service.AnalysisInterpreter;
import be.sonck.mp3gain.api.service.AnalysisInterpreterFactory;
import be.sonck.mp3gain.api.service.Mp3GainAnalysisListener;
import be.sonck.mp3gain.api.service.Mp3GainAnalyst;
import be.sonck.mp3gain.api.service.Mp3GainChangeListener;
import be.sonck.mp3gain.api.service.Mp3GainChanger;
import be.sonck.mp3gain.ui.api.EventBus;
import be.sonck.mp3gain.ui.event.ErrorEvent;
import be.sonck.mp3gain.ui.event.TrackProgressEvent;

public class TrackProcessor {

	private final class AnalysisListener implements Mp3GainAnalysisListener {
		private final BigDecimal targetVolume;
		private final int numberOfTracks;
		private final TrackProcessorListener listener;
		
		private int trackNumber = 0;

		private AnalysisListener(BigDecimal targetVolume, int numberOfTracks, TrackProcessorListener listener) {
			this.targetVolume = targetVolume;
			this.numberOfTracks = numberOfTracks;
			this.listener = listener;
		}

		@Override
		public void notifyTrackComplete(Analysis trackAnalysis) {
			replaceTrackAnalysisInCache(trackAnalysis);

			listener.tracksProcessed(asCollection(createTrackTO(trackAnalysis, targetVolume)), ++trackNumber, numberOfTracks);
		}

		@Override
		public void notifyProgress(File file, int trackNumber, int numberOfTracks, int progress) {
			eventBus.post(new TrackProgressEvent(progress, 100, "Analyzing \"" + file.getName() + "\""));
		}

		@Override
		public void notifyError(Exception e) {
			eventBus.post(new ErrorEvent(e));
		}

		@Override
		public void notifyAlbumComplete(AlbumAnalysis albumAnalysis) {
			Analysis album = albumAnalysis.getAlbum();
			if (album != null) {
				replaceAlbumAnalysisInCache(albumAnalysis);
				listener.tracksProcessed(
						createAlbumTracks(albumAnalysis, fileTrackMap.get(determineKey(albumAnalysis)), targetVolume),
						trackNumber, numberOfTracks);
			}
			
			eventBus.post(new TrackProgressEvent(0));
			
			if (trackNumber == numberOfTracks) {
				listener.done();
			}
		}
	}
	
	private final class ChangeListener implements Mp3GainChangeListener {
		private final BigDecimal targetVolume;
		private final int numberOfTracks;
		private final TrackProcessorListener listener;
		
		private int trackNumber = 0;
		
		public ChangeListener(BigDecimal targetVolume, int numberOfTracks, TrackProcessorListener listener) {
			this.targetVolume = targetVolume;
			this.numberOfTracks = numberOfTracks;
			this.listener = listener;
		}

		@Override
		public void notifyProgress(File file, int trackNumber, int numberOfTracks, int progress) {
			eventBus.post(new TrackProgressEvent(progress, 100, "Applying volume change to \"" + file.getName() + "\""));
		}

		@Override
		public void notifyTrackComplete(File file) {
			replaceTrackAnalysisInCache(file);

			eventBus.post(new TrackProgressEvent(0));
			listener.tracksProcessed(asCollection(createTrackTO(file, targetVolume)), ++trackNumber, numberOfTracks);
		}

		@Override
		public void notifyError(Exception e) {
			eventBus.post(new ErrorEvent(e));
		}
	}
	
	private final class InterruptableTrackProcessorListener implements TrackProcessorListener {
		
		private final TrackProcessorListener listener;

		public InterruptableTrackProcessorListener(TrackProcessorListener listener) {
			this.listener = listener;
		}

		@Override
		public void tracksProcessed(Collection<TrackTO> tracks, int currentIndex, int lastIndex) {
			synchronized (LOCK) {
				if (cancel) {
					canceled = true;
					throw new RuntimeException(new InterruptedException("correlationId mismatch for playlist " + playlist.getName()));
				}
				
				listener.tracksProcessed(tracks, currentIndex, lastIndex);
				
				if (cancel) {
					canceled = true;
					throw new RuntimeException(new InterruptedException("correlationId mismatch for playlist " + playlist.getName()));
				}
			}
		}

		@Override
		public void done() {
			synchronized (LOCK) {
				if (cancel) {
					canceled = true;
					throw new RuntimeException(new InterruptedException("correlationId mismatch for playlist " + playlist.getName()));
				}
				
				listener.done();
				
				if (cancel) {
					canceled = true;
					throw new RuntimeException(new InterruptedException("correlationId mismatch for playlist " + playlist.getName()));
				}
			}
		}
	}
	
	private static final Object LOCK = new Object();
	
	private final ITunesBridge iTunesBridge;
	private final Mp3GainAnalyst mp3GainAnalyst;
	private final Mp3GainChanger mp3GainChanger;
	private final EventBus eventBus;

	private final Playlist playlist;

	private Map<String, List<FileTrack>> fileTrackMap;
	private Map<String, AlbumAnalysis> analysisMap;

	private boolean cancel;
	private boolean canceled;


	public TrackProcessor(ITunesBridge iTunesBridge, Mp3GainAnalyst mp3GainAnalyst, Mp3GainChanger mp3GainChanger, EventBus eventBus, Playlist playlist) {
		this.iTunesBridge = iTunesBridge;
		this.mp3GainAnalyst = mp3GainAnalyst;
		this.mp3GainChanger = mp3GainChanger;
		this.eventBus = eventBus;
		this.playlist = playlist;
	}
	
	public void cancel() {
		cancel = true;
		
		synchronized (LOCK) {
			canceled = true;
			return;
		}
	}

	public void getTracks(BigDecimal targetVolume, TrackProcessorListener listener) {
		try {
			getTracksInterruptable(targetVolume, new InterruptableTrackProcessorListener(listener));
			
		} catch (RuntimeException e) {
			// ignore interruptedexceptions
			if (!(e.getCause() instanceof InterruptedException)) {
				throw e;
			}
		}
	}
	
	public void performTrackAnalysis(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		try {
			performTrackAnalysisInterruptable(selectedTracks, targetVolume, new InterruptableTrackProcessorListener(listener));
			
		} catch (RuntimeException e) {
			// ignore interruptedexceptions
			if (!(e.getCause() instanceof InterruptedException)) {
				throw e;
			}
		}
	}
	
	public void performAlbumAnalysis(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		try {
			performAlbumAnalysisInterruptable(selectedTracks, targetVolume, new InterruptableTrackProcessorListener(listener));
			
		} catch (RuntimeException e) {
			// ignore interruptedexceptions
			if (!(e.getCause() instanceof InterruptedException)) {
				throw e;
			}
		}
	}
	
	public void applyTrackGain(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		try {
			applyTrackGainInterruptable(selectedTracks, targetVolume, new InterruptableTrackProcessorListener(listener));
			
		} catch (RuntimeException e) {
			// ignore interruptedexceptions
			if (!(e.getCause() instanceof InterruptedException)) {
				throw e;
			}
		}
	}
	
	public void applyAlbumGain(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		try {
			applyAlbumGainInterruptable(selectedTracks, targetVolume, new InterruptableTrackProcessorListener(listener));
			
		} catch (RuntimeException e) {
			// ignore interruptedexceptions
			if (!(e.getCause() instanceof InterruptedException)) {
				throw e;
			}
		}
	}
	
	private void getTracksInterruptable(BigDecimal targetVolume, TrackProcessorListener listener) {
		if (fileTrackMap == null) {
			initialize();
			createTracksWithoutCache(targetVolume, listener);
			listener.done();
		} else {
			createTracksFromCache(targetVolume, listener);
			listener.done();
		}
	}

	private void performTrackAnalysisInterruptable(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		List<File> files = new ArrayList<File>();
		
		SortedSet<TrackTO> set = new TreeSet<TrackTO>(new TrackTOComparator());
		set.addAll(selectedTracks);

		for (TrackTO track : set) {
			// ignore files that already have mp3gain metadata
			if (track.getVolume() == null) {
				files.add(new File(track.getLocation()));
			}
		}

		mp3GainAnalyst.analyse(files, true, new AnalysisListener(targetVolume, files.size(), listener));
	}
	
	private void performAlbumAnalysisInterruptable(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		Map<String, List<File>> files = new HashMap<String, List<File>>();
		
		int size = 0;
		
		// use an array of keys to force processing order
		String[] keys = new String[10000];
		int keyIndex = 0;
		
		for (TrackTO track : selectedTracks) {
			// ignore files that already have mp3gain metadata
			if (track.getAlbumVolume() != null) { continue; }
			
			String key = determineKey(track.getLocation());
			if (files.containsKey(key)) { continue; }
			
			keys[keyIndex++] = key;
			
			List<File> list = new ArrayList<File>();
			for (FileTrack fileTrack : fileTrackMap.get(key)) {
				list.add(fileTrack.getLocation());
				size++;
			}
			
			files.put(key, list);
		}
		
		Mp3GainAnalysisListener mp3GainListener = new AnalysisListener(targetVolume, size, listener);

		for (String key : keys) {
			if (key == null) { break; }
			mp3GainAnalyst.analyse(files.get(key), false, mp3GainListener);
		}
	}
	
	private void applyTrackGainInterruptable(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		List<File> files = new ArrayList<File>();
		
		SortedSet<TrackTO> set = new TreeSet<TrackTO>(new TrackTOComparator());
		set.addAll(selectedTracks);

		for (TrackTO track : set) {
			// ignore files that have no mp3gain metadata
			if (track.getVolume() != null) {
				files.add(new File(track.getLocation()));
			}
		}
		
		ChangeListener changeListener = new ChangeListener(targetVolume, files.size(), listener);
		
		for (File file : files) {
			mp3GainChanger.change(file, changeListener, getTechnicalTrackModification(file, targetVolume));
		}
		
		listener.done();
	}
	
	private void applyAlbumGainInterruptable(List<TrackTO> selectedTracks, BigDecimal targetVolume, TrackProcessorListener listener) {
		Map<String, List<File>> files = new HashMap<String, List<File>>();
		
		int size = 0;
		
		// use an array of keys to force processing order
		String[] keys = new String[10000];
		int keyIndex = 0;
		
		for (TrackTO track : selectedTracks) {
			// ignore files that don't have mp3gain metadata
			if (track.getAlbumVolume() == null) { continue; }
			
			String key = determineKey(track.getLocation());
			if (files.containsKey(key)) { continue; }
			if (getTechnicalAlbumModification(key, targetVolume) == 0) { continue; }
			
			keys[keyIndex++] = key;
			
			List<File> list = new ArrayList<File>();
			for (FileTrack fileTrack : fileTrackMap.get(key)) {
				list.add(fileTrack.getLocation());
				size++;
			}
			
			files.put(key, list);
		}
		
		ChangeListener changeListener = new ChangeListener(targetVolume, size, listener);
		int index = 0;
		
		for (String key : keys) {
			if (key == null) { break; }
			
			System.out.println("applying album gain to " + key);
			
			List<File> list = files.get(key);
			mp3GainChanger.change(list, changeListener, getTechnicalAlbumModification(key, targetVolume));
			index += list.size();
			createTracksWithoutCache(targetVolume, listener, key, index, size);
		}
		
		listener.done();
	}

	private int getTechnicalAlbumModification(String key, BigDecimal targetVolume) {
		try {
			Analysis analysis = analysisMap.get(key).getAlbum();
			return getTechnicalModification(targetVolume, analysis);
		} catch (Exception e) {
			System.err.println("exception for key '" + key + "'");
			e.printStackTrace();
			return 0;
		}
	}

	private int getTechnicalTrackModification(File file, BigDecimal targetVolume) {
		String key = determineKey(file.getAbsolutePath());
		Analysis analysis = findAnalysisForFile(analysisMap.get(key).getTracks(), file);
		return getTechnicalModification(targetVolume, analysis);
	}
	
	private int getTechnicalModification(BigDecimal targetVolume, Analysis analysis) {
		AnalysisInterpreter interpreter = AnalysisInterpreterFactory.create(analysis);
		return interpreter.determineTechnicalModification(targetVolume).intValue();
	}

	private void replaceAlbumAnalysisInCache(AlbumAnalysis albumAnalysis) {
		analysisMap.put(determineKey(albumAnalysis), albumAnalysis);
	}

	private String determineKey(AlbumAnalysis albumAnalysis) {
		return determineKey(albumAnalysis.getTracks().get(0).getFile().getAbsolutePath());
	}

	private void replaceTrackAnalysisInCache(Analysis trackAnalysis) {
		String key = determineKey(trackAnalysis.getFile().getAbsolutePath());

		AlbumAnalysis albumAnalysis = analysisMap.get(key);

		List<Analysis> trackAnalyses = new ArrayList<Analysis>(albumAnalysis.getTracks());
		trackAnalyses.set(trackAnalyses.indexOf(trackAnalysis), trackAnalysis);

		analysisMap.put(key, new AlbumAnalysis(albumAnalysis.getAlbum(), trackAnalyses));
	}

	public void replaceTrackAnalysisInCache(File file) {
		String key = determineKey(file.getAbsolutePath());

		AlbumAnalysis albumAnalysis = analysisMap.get(key);

		List<Analysis> trackAnalyses = new ArrayList<Analysis>(albumAnalysis.getTracks());
		
		Analysis cachedAnalysis = findAnalysisForFile(trackAnalyses, file);
		Analysis newAnalysis = getStoredAnalysisFromMp3Gain(file);
		trackAnalyses.set(trackAnalyses.indexOf(cachedAnalysis), newAnalysis);

		analysisMap.put(key, new AlbumAnalysis(albumAnalysis.getAlbum(), trackAnalyses));
	}

	private void initialize() {
		SortedSet<FileTrack> fileTracks = iTunesBridge.getTracks(playlist);

		if (CollectionUtils.isEmpty(fileTracks)) {
			fileTrackMap = new HashMap<String, List<FileTrack>>();
		} else {
			fileTrackMap = createMap(fileTracks);
		}

		analysisMap = new HashMap<String, AlbumAnalysis>();
	}

	private void createTracksWithoutCache(BigDecimal targetVolume, TrackProcessorListener listener) {
		Set<String> keySet = fileTrackMap.keySet();

		int size = keySet.size();
		int index = 0;

		for (String key : keySet) {
			createTracksWithoutCache(targetVolume, listener, key, index++, size);
		}
	}

	private void createTracksWithoutCache(BigDecimal targetVolume, TrackProcessorListener listener, String key, int index,
			int size) {
		List<TrackTO> tracks = createAlbumTracks(getStoredAlbumAnalysisFromMp3Gain(key), fileTrackMap.get(key), targetVolume);
		listener.tracksProcessed(tracks, index, size);
	}

	private void createTracksFromCache(BigDecimal targetVolume, TrackProcessorListener listener) {
		Set<String> keySet = fileTrackMap.keySet();

		int size = keySet.size();
		int index = 0;

		for (String key : keySet) {
			List<TrackTO> tracks = createAlbumTracks(analysisMap.get(key), fileTrackMap.get(key), targetVolume);
			listener.tracksProcessed(tracks, index++, size);
		}
	}

	private List<TrackTO> createAlbumTracks(AlbumAnalysis albumAnalysis, List<FileTrack> fileTracks, BigDecimal targetVolume) {
		List<TrackTO> tracks = new ArrayList<TrackTO>();

		for (FileTrack fileTrack : fileTracks) {
			Analysis trackAnalysis = findAnalysisForFile(albumAnalysis.getTracks(), fileTrack.getLocation());
			tracks.add(TrackTOFactory.create(fileTrack, trackAnalysis, albumAnalysis.getAlbum(), targetVolume));
		}

		return tracks;
	}

	private AlbumAnalysis getStoredAlbumAnalysisFromMp3Gain(String key) {
		List<File> files = new ArrayList<File>();

		for (FileTrack fileTrack : fileTrackMap.get(key)) {
			files.add(fileTrack.getLocation());
		}

		AlbumAnalysis storedAnalysis = mp3GainAnalyst.getStoredAnalysis(files);

		// store for later use
		analysisMap.put(key, storedAnalysis);

		return storedAnalysis;
	}
	
	private Analysis getStoredAnalysisFromMp3Gain(File file) {
		return mp3GainAnalyst.getStoredAnalysis(file);
	}

	private Analysis findAnalysisForFile(List<Analysis> analyses, File file) {
		for (Analysis analysis : analyses) {
			if (file.equals(analysis.getFile())) {
				return analysis;
			}
		}

		return null;
	}

	private FileTrack findFileTrackForFile(List<FileTrack> fileTracks, File file) {
		for (FileTrack fileTrack : fileTracks) {
			if (file.equals(fileTrack.getLocation())) {
				return fileTrack;
			}
		}

		return null;
	}

	private Map<String, List<FileTrack>> createMap(SortedSet<FileTrack> tracks) {
		Map<String, List<FileTrack>> map = new HashMap<String, List<FileTrack>>();

		for (FileTrack track : tracks) {
			String fileName = track.getLocation().getAbsolutePath();

			// ignore non-mp3 files
			if (!fileName.toLowerCase().endsWith(".mp3")) {
				continue;
			}

			String key = determineKey(fileName);
			List<FileTrack> list = map.get(key);

			if (list == null) {
				list = new ArrayList<FileTrack>();
				map.put(key, list);
			}

			list.add(track);
		}

		return map;
	}

	private String determineKey(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('/'));
	}

	private TrackTO createTrackTO(Analysis trackAnalysis, final BigDecimal targetVolume) {
		File file = trackAnalysis.getFile();
		String key = determineKey(file.getAbsolutePath());

		return TrackTOFactory.create(findFileTrackForFile(fileTrackMap.get(key), file), trackAnalysis, 
				analysisMap.get(key).getAlbum(), targetVolume);
	}
	
	private TrackTO createTrackTO(File file, final BigDecimal targetVolume) {
		String key = determineKey(file.getAbsolutePath());

		return TrackTOFactory.create(findFileTrackForFile(fileTrackMap.get(key), file),
				findAnalysisForFile(analysisMap.get(key).getTracks(), file), analysisMap.get(key).getAlbum(), targetVolume);
	}

	private Collection<TrackTO> asCollection(TrackTO trackTO) {
		Collection<TrackTO> collection = new ArrayList<TrackTO>();
		collection.add(trackTO);
		return collection;
	}
}
