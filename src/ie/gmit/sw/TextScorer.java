package ie.gmit.sw;

import java.util.Map;

public class TextScorer {
	private Map<String, Double> map = null;
	
	public TextScorer(Map<String, Double> m){
		this.map = m;
	}
	
	/**
	 * Get the score calculate on string based on 4grmas
	 * 
	 * @param text The string to score on
	 * @return the score for the current String
	 */
	public double getScore(String text){
		double score = 0;

		for (int i = 0; i < text.length(); i++){
			if (i + QuadGramMap.GRAM_SIZE <= text.length() -1){
				score += computeLogScore(text.substring(i, i + QuadGramMap.GRAM_SIZE));
			}
		}
		//debug only
		//System.out.println("The plain text" + text + " \tThe score " + score + " current size " + map.size());
		return score;
	}
	
	/**
	 * Get the log score for the quadgram
	 * 
	 * @param quadgram The string to calculate the score for
	 * @return the score if quadgram found
	 */
	public double computeLogScore(String quadgram){
		if (map.containsKey(quadgram)){
			double frequency = map.get(quadgram);
			double total = (double) map.size();
			double probability = (double) (frequency/total);
			
			return Math.log10(probability);
		}else{
			return 0;
		}
	}
}
