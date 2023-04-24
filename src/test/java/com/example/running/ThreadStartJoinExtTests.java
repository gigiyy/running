package com.example.running;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
public class ThreadStartJoinExtTests {

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
	public void captureShouldCheckForExpectations(CapturedOutput capture) throws InterruptedException {
		Thread thread = new Thread(new HelloRunnable());
		thread.start();

		// without join() method call, the test might finish first, and the thread prints later.
		thread.join();

		assertThat(capture).contains("Thread");
	}

	@Test
	public void startThreadShouldPrintMessage(CapturedOutput capture) throws InterruptedException {
		System.out.println("Hello Test!");
		Thread thread = (new Thread(new HelloRunnable()));
		thread.start();

		// with join() method call, assert thread output will succeed.
		thread.join();

		assertThat(capture).contains("Test!");
		// assert thread output
		// fail if new thread is not joined with current thread.
		assertThat(capture).contains("Thread!");
	}

}
