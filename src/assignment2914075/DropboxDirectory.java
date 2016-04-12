package assignment2914075;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class DropboxDirectory {

	@PrimaryKey
	@Persistent
	private Key id;

	@Persistent
	private String dirName;

	@Persistent
	private ArrayList<String> subDirectories;

	@Persistent(mappedBy = "owner")
	private List<DropboxFile> files;

	public DropboxDirectory(Key id, String dirName) {
		this.id = id;
		this.dirName = dirName;
	}

	public boolean addSubDir(String subDirName) {

		if (subdirExists(subDirName)) {

			return false;

		} else {
			if (subDirectories == null) {

				subDirectories = new ArrayList<String>();
			}
			subDirectories.add(subDirName);
			return true;
		}
	}

	public boolean subdirExists(String subDirName) {

		if (subDirectories == null) {

			return false;
		}

		for (int i = 0; i < subDirectories.size(); i++) {

			if (subDirName.compareToIgnoreCase(subDirectories.get(i)) == 0) {

				return true;
			}
		}

		return false;
	}

	public boolean isEmpty() {

		boolean noFiles = false;
		boolean noSubDirectories = false;

		if (subDirectories == null) {

			noSubDirectories = true;

		} else {

			if (subDirectories.size() == 0) {

				noSubDirectories = true;
			}
		}

		if (files == null) {

			noFiles = true;

		} else {

			if (files.size() == 0) {

				noFiles = true;
			}
		}

		return noFiles && noSubDirectories;
	}

	public boolean fileExists(String fileName) {

		if (files == null) {

			return false;
		}

		for (int i = 0; i < files.size(); i++) {

			if (fileName.compareToIgnoreCase(files.get(i).getName()) == 0) {

				return true;
			}
		}

		return false;

	}

	public boolean addFile(DropboxFile file) {

		if (files == null) {

			files = new ArrayList<DropboxFile>();
		}

		if (fileExists(file.getName())) {

			return false;
		} else {

			files.add(file);
			return true;
		}
	}

	public void deleteFile(DropboxFile file) {

		if (files == null) {
			return;
		}

		for (int i = 0; i < files.size(); i++) {

			if (file.getName().compareToIgnoreCase(files.get(i).getName()) == 0) {

				files.remove(i);
				return;
			}

		}
	}

	public void deleteSubDir(String subDirName) {

		if (subDirectories == null) {
			return;
		}

		for (int i = 0; i < subDirectories.size(); i++) {

			if (subDirName.compareToIgnoreCase(subDirectories.get(i)) == 0) {

				subDirectories.remove(i);
				return;
			}
		}
	}

	public List<String> directories() {
		return subDirectories;
	}
	
	public List<DropboxFile> files() {
		return files;
	}
	
	public String getDirName() {
		return dirName;
	}


}
