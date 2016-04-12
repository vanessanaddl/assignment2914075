    package assignment2914075;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet extends DropboxServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {


		resp.setContentType("text/html");
		
		this.setUserServiceParameters(req);

		if (isCurrentUser()) {
			
		
			initializeDropboxUser();
			
				
			DropboxDirectory currentDirectory = Datastore.readDropboxDirectory(getCurrentUser(), getCurrentPath(req));
			
			
			
			List<String> subDirectories = currentDirectory.directories();	
		
			
			req.setAttribute("subDirectories", subDirectories);
			req.setAttribute("fileNames", currentDirectory.files());
			
			
			req.setAttribute("currentPath", getCurrentPath(req));
			
		
			req.setAttribute("uploadURL",BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/files"));
			Datastore.commit();
		}
		

		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);

		
		
	}
	
	private void initializeDropboxUser(){
		
	//Datastore
		if(Datastore.readDropboxUser(getCurrentUser())== null){
			System.out.println("[initializeDropboxUser] - User was not set up, but will be set up now");
			
			
			Key user_key = KeyFactory.createKey("DropboxUser", getCurrentUser().getUserId());
			DropboxUser du = new DropboxUser(user_key);
			Datastore.writeElement(du);
			
			
			DropboxDirectory rootDirectory = generateDirectory("/");
			Datastore.writeElement(rootDirectory);
			
		}else{
			System.out.println("[initializeDropboxUser] - User is already set up");
		}
		
	}
	
	private void setUserServiceParameters(HttpServletRequest req) {

		
		UserService us = UserServiceFactory.getUserService();
		String login_url = us.createLoginURL("/");
		String logout_url = us.createLogoutURL("/");

		
		req.setAttribute("user", getCurrentUser());
		req.setAttribute("login_url", login_url);
		req.setAttribute("logout_url", logout_url);
		
		System.out.println("[setUserServiceParameters] - User parameters set, user is: "+getCurrentUser());
	}


	
}
