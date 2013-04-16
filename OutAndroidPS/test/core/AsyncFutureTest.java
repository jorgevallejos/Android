package core;

import static core.AsyncFuture.wheneverResolved;
import org.junit.Test;
import junit.framework.TestCase;

public class AsyncFutureTest extends TestCase{

	private static final String EXPECTED_VALUE = "Hello";
	private AsyncFuture<String> asyncFuture;
	private String result;
	
	@Test
	public void test() {
		asyncFuture = new AsyncFuture<String>();
		wheneverResolved(asyncFuture, new Function<String, String>() {
			@Override
			public String applyTo(String argument) {
				result = argument;
				System.out.println("Testing");
				return result;
			}
		});
		asyncFuture.setResult(EXPECTED_VALUE);
		assertEquals(result, EXPECTED_VALUE);
	}

}