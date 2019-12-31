package mweka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;

public class Temp {

	static HashSet<String> not_processed_set = new HashSet<>();
	static HashSet<String> total_word_set = new HashSet<>();
	static HashSet<String> processed_set = new HashSet<>();

	public static void main(String[] args) throws IOException {
		String path = "/Users/imseongbin/Desktop/data.arff";
		String path2 = "/Users/imseongbin/Desktop/inst.arff";
		File data, pro;

		data = new File(path);
		pro = new File(path2);

		ArrayList<String> attributes = pullAtrributes(pro);
		SnowballStemmer stemmer = new SnowballStemmer();

		process(data, attributes, stemmer);

		System.out.println("processed words: " + processed_set.size());
		System.out.println("not processed words: " + not_processed_set.size());
		System.out.println("total words: " + total_word_set.size());

	}

	static ArrayList<String> pullAtrributes(File data) throws FileNotFoundException {
		ArrayList<String> lst = new ArrayList<>();

		Scanner in = new Scanner(new BufferedReader(new FileReader(data)));

		while (in.hasNext()) {
			String line = in.nextLine();

			String[] items = line.split(" ");

			if (items.length == 3 && items[0].equals("@attribute")) {
				lst.add(items[1]);
			}
		}

		return lst;
	}

	static void process(File f, ArrayList<String> attributes, Stemmer stemmer) throws IOException {
		Scanner in = new Scanner(new BufferedReader(new FileReader(f)));

//		FileWriter writer = new FileWriter(result);
		HashMap<String, Integer> map = new HashMap<>();

		while (in.hasNext()) {
			String line = in.nextLine();

			if (line.toCharArray().length < 1) {

				continue;
			}
			if (line.charAt(0) == '\'') {

				int end = 0;

				for (int i = 1; i < line.toCharArray().length; i++) {
					char c = line.charAt(i);
					if (c == '\'' && line.charAt(i - 1) != '\\') {
						end = i;
						break;
					}
				}

				String sub = line.substring(1, end);
				String deli = "\\\\n|\"| |\r|\\n|\t|\\.|,|;|:|\\\\|'|\"|\\(|\\)|\\?|!|\"|'";

				for (String str : sub.split(deli)) {

					str = stemmer.stem(str);
					total_word_set.add(str);

					if (str.replaceAll("\n+", "").equals("")) {
						continue;
					}

					int index = attributes.indexOf(str);

					if (index == -1) {
						not_processed_set.add(str);
						continue;
					} else {
						processed_set.add(str);
					}

					Integer cnt = map.get(str);

					if (cnt == null) {
						map.put(str, 1);
					} else {
						map.put(str, cnt + 1);
					}
				}
			}
		}
	}
}