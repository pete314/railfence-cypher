package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CypherBreaker breaks RailFence encrypted messages in a thread based execution
 * 
 * @author Peter Nagy
 * @since	2015 December
 *
 */
public class CypherBreaker {
	private static final int MAX_QUEUE_SIZE = 100;
	private BlockingQueue<Resultable> resultQueue;
	private String cypherText;
	private int counter;
	private int threadNum;
	private final ConcurrentHashMap<String, Double> quadgram;
	private final String QUADGRAM_FILE = "4grams.txt";
	private Result highScore;

	public CypherBreaker(String cypherText) {
		resultQueue = new ArrayBlockingQueue<Resultable>(MAX_QUEUE_SIZE);
		this.cypherText = cypherText;
		quadgram = new FileParser().parseQuadGramFile(QUADGRAM_FILE);
		init();
	}

	private void init() {

		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 2; i < cypherText.length() / 2; i++) {
			Thread t = new Thread(new Decryptor(resultQueue, cypherText, i,
					quadgram));
			t.start();
			threads.add(t);
			++threadNum;
		}

		Thread queueThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Result highScoreResult = new Result("DummyResult", -1, -399999);
				while (!resultQueue.isEmpty()) {

					try {
						Resultable r = resultQueue.take();
						//check if poison is processed
						if (r instanceof PoisonResult) {
							printHighScore(highScoreResult);
							setHighScore(highScoreResult);
							return;
						}
						
						//check if current result is better tan current high score
						if (r.getScore() > highScoreResult.getScore()) {
							highScoreResult = (Result) r;
						}

						increment();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		});
		queueThread.start();

		try {
			for (Thread t : threads){
					t.join();
			}
			
			queueThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setHighScore(Result r){
		highScore = r;
	}
	
	public Result getHighScore(){
		return this.highScore;
	}
	
	/**
	 * Print the result Object passed (high score) formatted
	 * @param highScoreResult
	 */
	public synchronized void printHighScore(Result highScoreResult) {
		System.out
				.printf("\n==========\nScore: %.0f\nKey: %d\nPlain Text: %s\n==========\n",
						highScoreResult.getScore(), highScoreResult.getKey(),
						highScoreResult.getPlainText());
	}

	/**
	 * Increment the processed element counter and compare with thread number
	 * If reached the maximum size or thread number add poisen element
	 */
	protected void increment() {
		++counter;
		if (counter == threadNum || counter == MAX_QUEUE_SIZE) {
			try {
				resultQueue.put(new PoisonResult());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
