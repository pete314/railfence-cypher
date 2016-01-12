package ie.gmit.sw;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * File Writer - writes file content
 * 
 * @author Peter Nagy
 * @since 2015 December
 */
public class FileWriter {
	private String filePath;
	
	public FileWriter(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Write Stream to file
	 * @param contentStream
	 */
	public void streamedFileWriter(Stream<String> contentStream){
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
		    contentStream.forEach((string) -> {
		    	try {
					writer.write(string);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
