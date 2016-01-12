package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CliHelper - command line instruction routing and command builder
 * 
 * @author Peter Nagy
 * @since 2015 December
 * 
 */
public class CliHelper {
	private final String MENU_OPTIONS_TEXT = "\n1) Enter encrypted message\n"
			+ "2) Add path to encrypted file\n" + "3) Encrypt message\n"
			+ "4) Encrypt file\n" + "5) exit";
	private final BufferedReader br;
	private long  currentMilis;
	
	CliHelper() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Cli command router - execution selector
	 */
	public void commandRouter() {
		int choice = 0;
		String messagePlain;
		do {
			choice = cliInputOptionReader();
			switch (choice) {
				case 1:
					messagePlain = cliStringReader("Plase enter the message to encrypt: ");
					measureElapsedTime(true);
					new CypherBreaker(messagePlain);
					break;
				case 2:
					messagePlain = cliStringReader("Plase enter the encrypted file path: ");
					measureElapsedTime(true);
					new FileCryptor(messagePlain).decryptFileContent();
					printHelper("File encrypted, new name is: " + messagePlain + ".dec", false);
					break;
				case 3:
					messagePlain = cliStringReader("Plase enter the message to encrypt: ");
					//if plain text chars <10 key = 2, <20 key = 3 else 5
					measureElapsedTime(true);
					String messageHash = new RailFence().encrypt(messagePlain, messagePlain.length() < 10 ? 2 : messagePlain.length() > 20 ? 5 : 3);
					printHelper("The enccrypted text is: " + messageHash, false);
					break;
				case 4:
					measureElapsedTime(true);
					messagePlain = cliStringReader("Please give the file path to encrypt: ");
					new FileCryptor(messagePlain).encryptFileContent();
					printHelper("File encrypted, new name is: " + messagePlain + ".rf", false);
					break;
			}
			measureElapsedTime(false);
		} while (choice != 5);
		printHelper("bye", false);

		System.exit(1);
	}
	
	/**
	 * Mesaure the execution time for task
	 * 
	 * @param isStart reset the currentTime
	 */
	private void measureElapsedTime(boolean isStart){
		if(isStart){
			currentMilis = System.currentTimeMillis();
		}else{
			printHelper("\n~~~~~~~EXECUTED IN: " + (System.currentTimeMillis() - currentMilis) +"ms\n", false);
		}
	}
	
	/**
	 * Reads a the line entered to command prompt
	 * 
	 * @param helpMessage The instruction for the message to enter
	 * @return line on string entered
	 */
	private String cliStringReader(String helpMessage){
		try {
			printHelper(helpMessage, true);
			return br.readLine();
		} catch (IOException e) {
			printHelper(e.toString(), false);
		}
		return null;
	}
	
	/**
	 * 
	 * @return integer value entered
	 */
	private int cliInputOptionReader() {
		int choice = 0;
		do {
			printHelper(MENU_OPTIONS_TEXT, false);
			printHelper("Please choose an option: ", true);
			try {
				String number = br.readLine();
				if (safeIntParser(number))
					choice = Integer.parseInt(number);
				else
					continue;
			} catch (IOException e) {
				printHelper(e.toString(), false);
			}
		} while (choice < 0 || choice > 5);

		return choice;
	}

	/**
	 * Integer to String parsing helper, to avoid NumberFormatException
	 * 
	 * @param value
	 *            The number as string
	 * @return true if value can be parsed as integer
	 */
	private boolean safeIntParser(String value) {
		try {
			int number = Integer.parseInt(value);
			return true;
		} catch (NumberFormatException ex) {
			// nothing to solve, user will try again
		}
		return false;
	}

	/**
	 * Message print helper for cli messages
	 * 
	 * @param message
	 *            The message body to print
	 * @param isInlineMessage
	 *            Selector for in-line printing
	 */
	private void printHelper(String message, boolean isInlineMessage) {
		if (isInlineMessage)
			System.out.printf("\n %s", message);
		else
			System.out.println(message);
	}
}
