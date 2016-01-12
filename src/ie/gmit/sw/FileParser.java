package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class FileParser {
	private Map<String, Double> quadgramMap;

	public FileParser(){
		quadgramMap = new ConcurrentHashMap<String, Double>();
	}
	
	public FileParser(ConcurrentHashMap<String, Double> quadgramMap) {
		super();
		this.quadgramMap = quadgramMap;
	}
	
	/**
	 * Parse 4grams file content into concurentHashmap
	 * 
	 * @param filePath The file path for 4grams
	 * @return concurentHashMap with parsed value
	 */
	public ConcurrentHashMap<String, Double> parseQuadGramFile(String filePath){
		try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))){
			String line;
			while((line = input.readLine()) != null){
				String[] bits = line.split(" ");
				quadgramMap.put(bits[0], Double.parseDouble(bits[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (ConcurrentHashMap<String, Double>) quadgramMap;
	}
	
	/**
	 * Parse a file content into stream
	 * 
	 * @param filePath The path to file
	 * @return
	 */
	public Stream<String> parsePlainTextFile(String filePath){
		File file = new File(filePath);
		if(file.exists()){
			try {
				return Files.lines(Paths.get(file.getPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("The file does not exist!");
		}
		return null;
	}
}
