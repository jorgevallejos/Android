package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session<T extends Value> {
	
	private Map<Predicate<T>, AsyncFuture<T>> readers = new HashMap<Predicate<T>, AsyncFuture<T>>();
	private  List<T> values = new ArrayList<T>();

	public AsyncFuture<T> read(Predicate<T> predicate) {
		AsyncFuture<T> futureValue = new AsyncFuture<T>();
		readers.put(predicate, futureValue);
		notifyExistingMatches(predicate);
		return futureValue;
	}

	private void notifyExistingMatches(Predicate<T> predicate) {
		for (T value : values) {
			if(predicate.applyTo(value)) {
				AsyncFuture<T> futureValue = readers.get(predicate);
				futureValue.setResult(value);
			}
		}
	}

	public void write(T value) {
		values.add(value);
		notifyReads(value);
	}

	private void notifyReads(T value) {
		for (AsyncFuture<T> future : findMatches(value)) {
			future.setResult(value);
		}
	}

	private List<AsyncFuture<T>> findMatches(T value) {
		List<AsyncFuture<T>> matches = new ArrayList<AsyncFuture<T>>();
		for (Predicate<T> predicate : readers.keySet()) {
			if (predicate.applyTo(value)) {
				matches.add(readers.get(predicate));
			}
		}
		return matches;
	}
}
