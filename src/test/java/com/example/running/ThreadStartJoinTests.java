package com.example.running;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import com.example.running.CaptureSystemOutput.OutputCapture;
import org.junit.jupiter.api.Test;

public class ThreadStartJoinTests {

	static class HelloRunnable implements Runnable {

		@Override
		public void run() {
			try {
				// without sleep, assert thread output is non-deterministic
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("Hello Thread!");
		}
	}

	@Test
	@CaptureSystemOutput
	public void captureShouldCheckForExpectations(OutputCapture capture) throws InterruptedException {
		Thread thread = new Thread(new HelloRunnable());
		thread.start();

		// without join() method call, the test might finish first, and the thread prints later.
		thread.join();

		capture.expect(containsString("Thread"));
	}

	@Test
	@CaptureSystemOutput
	public void startThreadShouldPrintMessage(OutputCapture capture) throws InterruptedException {
		System.out.println("Hello Test!");
		Thread thread = (new Thread(new HelloRunnable()));
		thread.start();

		// with join() method call, assert thread output will succeed.
		thread.join();

		String output = capture.toString();
		assertThat(output).contains("Test!");
		// assert thread output
		// fail if new thread is not joined with current thread.
		assertThat(output).contains("Thread!");

		// another way to verify the captured output, after test finished
		capture.expect(containsString("Thread!"));
	}

}
