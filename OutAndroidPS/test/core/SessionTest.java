package core;

import static core.AsyncFuture.wheneverResolved;

import org.junit.Test;
import junit.framework.TestCase;

public class SessionTest extends TestCase{
	private static final Integer COLD_THRESHOLD = 6;

	private Session<Temperature> session;

	@Test
	public void test() {
		session = new Session<Temperature>();
		
		// Client side (subscriber)
		AsyncFuture<Temperature> temperatureFuture = session.read(warmTemperatures());
		wheneverResolved(temperatureFuture, handleTemperature());
		
		// Service side (publisher)
		session.write(new Temperature(0));
		session.write(new Temperature(5));
		session.write(new Temperature(10));
		session.write(new Temperature(15));
	}

	private class Temperature extends Value {
		private final Integer temperature;

		Temperature(Integer temperature) {
			this.temperature = temperature;
		}

		Integer getValue() {
			return temperature;
		}
	}

	private Predicate<Temperature> warmTemperatures() {
		return new Predicate<Temperature>() {
			@Override
			public Boolean applyTo(Temperature temperature) {
				return temperature.getValue() > COLD_THRESHOLD;
			}
		};
	}
	
	private Function<Temperature, Temperature> handleTemperature() {
		return new Function<Temperature, Temperature>() {
			@Override
			public Temperature applyTo(Temperature temperature) {
				System.out.println("Warm temperature: " + temperature.getValue());
				return temperature;
			}
		};
	}	
}