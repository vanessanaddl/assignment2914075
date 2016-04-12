package assignment2914075;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DirectoryServlet extends DropboxServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String value = req.getParameter("button");
		
		if (value == null) {
			
			System.err.println("[DirectoryServlet] - invalid value for button determination");
			
		} else if (value.compareToIgnoreCase("add") == 0) {
			
			System.out.println("[DirectoryServlet] - add button has been pressed");
			addDirectory(req);

		} else if (value.compareToIgnoreCase("change") == 0) {

			System.out.println("[DirectoryServlet] - change button has been pressed");
			changeDirectory(req);

		} else if (value.compareToIgnoreCase("delete") == 0) {

			System.out.println("[DirectoryServlet] - delete button has been pressed");
			deleteDirectory(req);

		}

		resp.sendRedirect("/");
	}

	private void addDirectory(HttpServletRequest req) {

		// load DIRECTORYNAME
		String newDirectoryName = req.getParameter("textfield");
		newDirectoryName = newDirectoryName.trim().replace("/", "") + "/";
		System.out.println("[addDirectory] - User want to add the directory "+newDirectoryName);
		
		// load CURRENTDIRECTORY
		DropboxDirectory currentDirectory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));
		

		//does directory EXIST?
		if (!currentDirectory.subdirExists(newDirectoryName)) {
			
			// ADD
			currentDirectory.addSubDir(newDirectoryName);
			// GENERATE
			DropboxDirectory newDirectory = generateDirectory(getCurrentPath(req) + newDirectoryName);
			// UPDATE PARENT
			Datastore.writeElement(currentDirectory);
			// UPDATE DIRECTORY
			Datastore.writeElement(newDirectory);
		}
		Datastore.commit();

	}

	private void changeDirectory(HttpServletRequest req) {

		// load TARGET DIRECTORY
		String targetDirectory = req.getParameter("key_value");
		if (targetDirectory.compareTo("../") == 0) {

			String path = getCurrentPath(req).trim();
			String[] split = path.split("/");

			String parentPath = "";
			for (int i = 1; i < split.length - 1; i++) {
				parentPath += "/" + split[i];
			}
			parentPath += "/";
			
			System.out.println("[changeDirectory] - User want to change directory to "+parentPath);
			setCurrentPath(req, parentPath);

		} else {
			
			System.out.println("[changeDirectory] - User want to change directory to "+getCurrentPath(req) + targetDirectory);
			setCurrentPath(req, getCurrentPath(req) + targetDirectory);
		}

	}

	private void deleteDirectory(HttpServletRequest req) {

		String targetDirectory = req.getParameter("key_value");

		System.out.println("[deleteDirectory] - User want to delete directory "+getCurrentPath(req)
				+ targetDirectory);
		DropboxDirectory directory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req)+targetDirectory);
		
	
		// subDir EMPTY?
		if (directory.isEmpty()) {

			Datastore.deleteElement(directory);
			DropboxDirectory parentDirectory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));
			parentDirectory.deleteSubDir(targetDirectory);
			Datastore.writeElement(parentDirectory);
		}
		Datastore.commit();
	}

}
