package assignment2914075;

import javax.servlet.http.HttpServlet;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import javax.servlet.http.*;

@SuppressWarnings("serial")
public abstract class DropboxServlet extends HttpServlet {
	
	protected User getCurrentUser() {

		UserService us = UserServiceFactory.getUserService();
		return us.getCurrentUser();

	}

	protected boolean isCurrentUser() {

		return getCurrentUser() != null;
	}

	// returns currentPath if set; otherwise it gets set and returns root
	protected String getCurrentPath(HttpServletRequest req) {

		String currentPath = (String) req.getSession().getAttribute(
				"currentPath");
		if (currentPath != null && currentPath.length() > 0) {
			System.out.println("[getCurrentPath] - Current Path (contained in Cookie): "+currentPath);
			
		} else {
			currentPath = "/";
			System.out.println("[getCurrentPath] - Current Path (NOT contained in Cookie): "+currentPath);
			req.getSession().setAttribute("currentPath", "/");
		}
		
		return currentPath;
	}

	protected void setCurrentPath(HttpServletRequest req, String path) {

		System.out.println("[setCurrentPath] - Current Path set to Cookie: "+path);
		req.getSession().setAttribute("currentPath", path);
	}

	
	protected DropboxDirectory generateDirectory(String path) {

		if (!isCurrentUser()) {
			System.err
					.println("[generateDirectory] - can't generate directory: "
							+ path + " User is not logged in");
			return null;
		}
		
		Key directory_key = KeyFactory.createKey("DropboxDirectory", getCurrentUser().getUserId() + path);
		DropboxDirectory directory = new DropboxDirectory(directory_key, path);
		System.out.println("[generateDirectory] - Directory generated: "+path);
		return directory;
	}

	

}
