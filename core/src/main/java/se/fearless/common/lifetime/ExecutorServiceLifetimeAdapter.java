package se.fearless.common.lifetime;

import java.util.concurrent.ExecutorService;

public class ExecutorServiceLifetimeAdapter implements LifetimeListener {
	private ExecutorService[] executorServices;

	public ExecutorServiceLifetimeAdapter(ExecutorService... executorServices) {
		this.executorServices = executorServices;
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onShutdown() {
		if (executorServices != null) {
			for (ExecutorService executorService : executorServices) {
				executorService.shutdown();
			}
		}
	}
}
