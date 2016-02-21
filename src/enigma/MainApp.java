package enigma;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;


/*
 * Simple Enigma mainapp.
 * It loads encoding combinations into rotors from file (rotors.txt) 
 * and initial starting position of rotors from file (init.txt).
 * Next You set destination file to write encrypted text, and last
 * load text to encrypt from file.
 * 
 * @author Paweł Czernek
 */

public class MainApp {

	static Scanner in = null;

	public static void main(String[] args) {

		String plainText = "";
		String encodedText = "";
		Enigma enigma;

		int[] rotor1Table = new int[64];
		int[] rotor2Table = new int[64];
		int[] rotor3Table = new int[64];
		int[] reverseRotorTable = new int[64];
		int[] initTable = new int[3];

		// Reading rotor tables from file rotors.txt
		String userInput = "";
		do {
			System.out.println("Podaj nazwę pliku z zapisem połączeń bębnów.");
			try {
				in = new Scanner(System.in);
				userInput = in.nextLine();
				in = new Scanner(Paths.get(userInput));
				in.useDelimiter(Pattern.compile("[\\r\\n-]"));

				rotor1Table = writeRotorTable(rotor1Table);
				rotor2Table = writeRotorTable(rotor2Table);
				rotor3Table = writeRotorTable(rotor3Table);
				reverseRotorTable = writeReversingRotor(reverseRotorTable);
				userInput = "";

			} catch (IOException e) {
				System.out.println("Nieprawidłowa ścieżka pliku!");
			}
		} while (!userInput.equals(""));

		// Reading init position from file
		do {
			System.out.println("Podaj nazwę pliku z położeniem początkowym bębnów.");
			try {
				in = new Scanner(System.in);
				userInput = in.nextLine();
				in = new Scanner(Paths.get(userInput));
				in.useDelimiter(Pattern.compile("[\\r\\n]"));
				// System.out.println(in.next());
				in.next();
				for (int i = 0; i < 3; i++) {
					initTable[i] = in.nextInt();
				}
				userInput = "";
			} catch (IOException e) {
				System.out.println("Nieprawidłowa ścieżka pliku!");
			}
		} while (!userInput.equals(""));

		enigma = new Enigma(rotor1Table, rotor2Table, rotor3Table, reverseRotorTable, initTable);

		// Wprowadzenie ścieżki do pliku docelowego zakodowanego tekstu.
		System.out.println("Podaj nazwę pliku do którego zaszyfrować tekst");

		PrintWriter out = null;
		String savePath = "";
		try {
			in = new Scanner(System.in);
			userInput = in.nextLine();
			out = new PrintWriter(userInput);
			savePath = userInput;
			userInput = "";
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// Wprowadzenie ścieżki pliku z tekstem do zakodowania

		do {
			System.out.println("Podaj nazwę pliku z tekstem który chcesz zaszyfrować.");
			try {
				in = new Scanner(System.in);
				userInput = in.nextLine();
				in = new Scanner(Paths.get(userInput));
				in.useDelimiter(Pattern.compile("[\\r\\n]+"));

				while (in.hasNext()) {
					plainText = in.nextLine().toUpperCase();
					System.out.println(plainText);

					for (int i = 0; i < plainText.length(); i++) {
						if (plainText.charAt(i) < 32 || plainText.charAt(i) > 95) {
							encodedText += plainText.charAt(i);
							continue;
						}
						encodedText += Character.toString(enigma.encodeCharacter(plainText.charAt(i)));

					}

					System.out.println(encodedText);

					// Saving encoded text to File
					out.println(encodedText);
					System.out.println();
					encodedText = "";
					userInput = "";

				}
				System.out.println("Zakodowany tekst zapisano w pliku: "+ savePath);
			} catch (IOException e) {
				System.out.println("Nie ma takiego pliku! Nieprawidłowa ścieżka!");
			} finally {
				out.close();
			}
		} while (!userInput.equals(""));

	}

	static int[] writeRotorTable(int[] table) {
		in.next();
		int outPin;
		int inPin;
		for (int i = 0; i < 64; i++) {
			outPin = in.nextInt();
			inPin = in.nextInt();
			table[inPin] = outPin;
		}
		return table;
	}

	static int[] writeReversingRotor(int[] table) {
		in.next();
		int outPin;
		int inPin;
		for (int i = 0; i < 32; i++) {
			outPin = in.nextInt();
			inPin = in.nextInt();
			table[inPin] = outPin;
			table[outPin] = inPin;
		}
		return table;
	}
}
