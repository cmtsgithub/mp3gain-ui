package be.sonck.mp3gain.ui.swing;

import javax.swing.SwingWorker;

public final class SwingWorkerManager {
	
	private static final SwingWorkerManager INSTANCE = new SwingWorkerManager();
	
	private SwingWorker<?, ?> worker;

	private SwingWorkerManager() {}
	
	public static SwingWorkerManager getInstance() {
		return INSTANCE;
	}
	
	public void execute(SwingWorker<?, ?> newWorker) {
		if (worker != null) {
			worker.cancel(true);
		}
		
		worker = newWorker;
		worker.execute();
	}
}
