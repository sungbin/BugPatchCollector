package mweka;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class Orderer {

	static String bowFolderPath = "/Users/imseongbin/Desktop/lottie-android-bow";
	static String outpath = "/Users/imseongbin/Desktop/";
	
	static String git_path = "/Users/imseongbin/Desktop/lottie-android";

	public static void main(String[] args) throws IOException, NoHeadException, GitAPIException {

		ArrayList<String> filePathes = new ArrayList<>();
		function1(filePathes);

		ArrayList<String> directoryPathes = new ArrayList<>();
		function2(directoryPathes);

//		check(filePathes,directoryPathes);
		
		String sh_1 = sh_1(filePathes.get(filePathes.size()-1));
		System.out.println("sh_1: " + sh_1);
		
		for(String str : filePathes) {
			String s = sh_1(str);
			if(s.equals(sh_1)) {
				System.out.println("call: " + str.substring(40));
			}
		}
		
		Git git = Git.open(new File(git_path));
		Repository repo = git.getRepository();
		Iterable<RevCommit> commitList = git.log().call();
		
		for(RevCommit commit : commitList) {
			if(commit.getParentCount() < 0) {
				continue;
			}
			if(!commit.getName().equals(sh_1)) {
				continue;
			}
			
			RevCommit parent = commit.getParent(0);
			
			final List<DiffEntry> diffs = git.diff().setOldTree(prepareTreeParser(repo, parent.getId().name()))
					.setNewTree(prepareTreeParser(repo, commit.getId().name())).call();
			
			
			for (DiffEntry diff : diffs) {
				String oldPath = diff.getOldPath();
				String newPath = diff.getNewPath();
				
				System.out.println("new: " + newPath);
			}
		}
		
	}

	static void function1(ArrayList<String> filePathes) throws IOException {
		File input = new File(outpath + "data.arff");

		String content = FileUtils.readFileToString(input);

		String[] lines = content.split("\n");

		for (int i = 7; i < lines.length; i++) {
			String line = lines[i];

			String[] substring = line.split(",");
			int len = substring.length;

			filePathes.add(substring[len - 2].substring(6));

		}
	}

	static void function2(ArrayList<String> directoryPathes) {
		File dir1 = new File(bowFolderPath + File.separator + "buggy");
		File dir2 = new File(bowFolderPath + File.separator + "clean");

		for (String name : dir1.list()) {
			directoryPathes.add(name);
		}
		for (String name : dir2.list()) {
			directoryPathes.add(name);
		}
	}

	static void check(ArrayList<String> filePathes, ArrayList<String> directoryPathes) {
		for (int i = 0; i < directoryPathes.size(); i++) {
			String f1 = filePathes.get(i);
			String f2 = directoryPathes.get(i);

			if ((f1.equals(f2))) {
				System.out.println(f1);
			}
		}
	}
	
	static String sh_1(String path) {
		return path.substring(0,40);
	}
	
	public static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
		// from the commit we can build the tree which allows us to construct the
		// TreeParser
		// noinspection Duplicates
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(repository.resolve(objectId));
			RevTree tree = walk.parseTree(commit.getTree().getId());

			CanonicalTreeParser treeParser = new CanonicalTreeParser();
			try (ObjectReader reader = repository.newObjectReader()) {
				treeParser.reset(reader, tree.getId());
			}

			walk.dispose();

			return treeParser;
		}
	}
}
