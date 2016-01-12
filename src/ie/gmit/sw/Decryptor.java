package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Decryptor decrypts messages with specified key with the RailFenceCypher
 * 
 * @author Peter Nagy
 * @since	2015 December
 */
public class Decryptor implements Runnable{
	private BlockingQueue<Resultable> resultQueue;
	private String cypherText;
	private int key;
	private final TextScorer textScorer;
	
	public Decryptor(BlockingQueue<Resultable> resultQueue, String cypherText,
			int key, ConcurrentHashMap<String, Double> quadgram) {
		super();
		this.resultQueue = resultQueue;
		this.cypherText = cypherText;
		this.key = key;
		this.textScorer = new TextScorer(quadgram);
	}

	@Override
	public void run() {
		RailFence rf = new RailFence();
		String plainText = rf.decrypt(cypherText, key);
		double score = textScorer.getScore(plainText.toUpperCase());
		
		Resultable r = new Result(plainText, key, score);
		try {
			resultQueue.put(r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
