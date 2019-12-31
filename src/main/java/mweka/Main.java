package mweka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Main {

	static String bowFolderPath = "/Users/imseongbin/Desktop/lottie-android-bow";
	static String outpath = "/Users/imseongbin/Desktop/";

	public static void main(String[] args) throws Exception {

		TextDirectoryLoader testLoader = new TextDirectoryLoader();
		ArffSaver arffSaver = new ArffSaver();

		String option = "-F -dir " + bowFolderPath;

		testLoader.setOptions(option.split(" "));
		Instances dataRaw = testLoader.getDataSet();

		arffSaver = new ArffSaver();
		arffSaver.setInstances(dataRaw);
		arffSaver.setFile(new File(outpath + "data.arff"));
		arffSaver.writeBatch();

		SnowballStemmer stemmer = new SnowballStemmer();
		StringToWordVector filter = new StringToWordVector();

		filter.setStemmer(stemmer);
		filter.setInputFormat(dataRaw);

		Instances dataFiltered = Filter.useFilter(dataRaw, filter);

		arffSaver = new ArffSaver();
		arffSaver.setInstances(dataFiltered);
		arffSaver.setFile(new File(outpath + "inst.arff"));
		arffSaver.writeBatch();

	}

	public static void write(String path, String content) throws IOException {
		File f = new File(path);

		FileWriter writer = new FileWriter(f);

		writer.append(content);
		writer.flush();
		writer.close();
	}

}