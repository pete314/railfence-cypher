package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * File Encryptor encrypts files based on the RailFence cypher
 * 
 * @author Peter Nagy
 * @since 2015 December
 *
 */
public class FileCryptor extends RailFence {
	private String filePath;
	
	public FileCryptor(String filePath){
		this.filePath = filePath;
	}
	
	/**
	 * Encrypt file Content with streams
	 */
	public void encryptFileContent(){
		String newFileName = filePath + ".rf";
		final int randomKey = generateRandomKey();
		
		Stream<String> filePlainContent = new FileParser().parsePlainTextFile(filePath);

		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(newFileName))) {
			filePlainContent.forEach((string) -> {
				try {
					bw.write(encrypt(string, randomKey) + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Find best key for content for each line and use it for de-cryption
	 */
	public void decryptFileContent(){
		File file = new File(filePath);
		if(file.exists() && filePath.contains(".rf")){
			Result absoluteHighScore = new Result("DummyResult", -1, -399999);
			List<CypherBreaker> breakers = new ArrayList<>();
			readScoreContent(breakers, filePath);
			
			//find absolute high score from all lines
			for(CypherBreaker cb : breakers)
				if(cb.getHighScore() != null && cb.getHighScore().getScore() > absoluteHighScore.getScore())
					absoluteHighScore = cb.getHighScore();
			
			decryptFile(absoluteHighScore.getKey(), filePath);
			
		}else{
			System.out.println("File does not exist or not in right format!");
		}
	}
	
	/**
	 * Read content and store the best score for each line
	 * 
	 * @param breakers
	 * @param filePath
	 */
	private void readScoreContent(List<CypherBreaker> breakers, String filePath){
		try(BufferedReader br = Files.newBufferedReader(Paths.get(filePath))){
			String line;
			while((line = br.readLine()) != null){
				if(line != null) breakers.add(new CypherBreaker(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param key The best key for the 
	 * @param path
	 */
	private void decryptFile(int key, String path){
		try(BufferedReader br = Files.newBufferedReader(Paths.get(path));
				BufferedWriter bw = Files.newBufferedWriter(Paths.get(path + ".dec"))){
			RailFence rf = new RailFence();
			String line;
			while((line = br.readLine()) != null){
				bw.write(rf.decrypt(line, key) + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate random key
	 * 
	 * @return integer 1-15 range
	 */
	private int generateRandomKey(){
		int randomKey;
		while(( randomKey = new SecureRandom().nextInt(15)) == 0);//make sure number is not 0
		return randomKey;
	}
}
