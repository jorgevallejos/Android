package core;

import java.util.ArrayList;
import java.util.List;

public class AsyncFuture<R> {

	public static <R> void wheneverResolved(AsyncFuture<R> asyncFuture,
			Function<R, ?> function) {
		asyncFuture.functions.add(function);
		asyncFuture.notifyExistingResults(function);
	}

	private List<R> results;
	private List<Function<R, ?>> functions;

	public AsyncFuture() {
		functions = new ArrayList<Function<R, ?>>();
		results = new ArrayList<R>();
	}

	public void setResult(R result) {
		this.results.add(result);
		notifyResult(result);
	}

	private void notifyExistingResults(Function<R, ?> function) {
		for (R result : results) {
			function.applyTo(result);
		}
	}

	private void notifyResult(R result) {
		for (Function<R, ?> function : functions) {
			function.applyTo(result);
		}
	}

}
