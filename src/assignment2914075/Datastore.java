package assignment2914075;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

public class Datastore {
	
	private static PersistenceManager pm;
	
	/**
	 * Writes/updates an item in datastore
	 * @param element element which should be written or updated
	 */
	public static void writeElement(Object element){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
			
		}
		pm.makePersistent(element);
		System.out.println("[Datastore][WRITE] - Element has been written to database, classtype: "+element.getClass().getSimpleName());
		
	}
	
	/**
	 * Deletes an item in datastore
	 * @param element element which should be deleted
	 */
	public static void deleteElement(Object element){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		}
		pm.deletePersistent(element);
		System.out.println("[Datastore][DELETE] - Element has been removed from database, classtype: "+element.getClass().getSimpleName());
	}
	
	/**
	 * Reads a dropbox directory
	 * @param u current user
	 * @param path full path of the directory
	 * @return directory or null (in case of an error)
	 */
	public static DropboxDirectory readDropboxDirectory(User u, String path){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		}
	
		if (u == null) {
			System.err.println("[Datastore][READ][Directory] - can't load directory: "
					+ path + " google user is null");
			return null;
		}
		Key directory_key = KeyFactory.createKey("DropboxDirectory",u.getUserId() + path);
		
		DropboxDirectory directory = null;
		try {
			directory = pm.getObjectById(DropboxDirectory.class, directory_key);
		} catch (Exception e) {
			
			System.err.println("[Datastore][READ][Directory] - can't load directory: "
					+ path);
			return null;
		}
		return directory;
	}
	
	/**
	 * Reads a dropbox user 
	 * @param u current user
	 * @return user or null (in case of an error)
	 */
	public static DropboxUser readDropboxUser(User u){
		if(pm == null){
			pm = PMF.get().getPersistenceManager();
		}
	
		if (u == null) {
			System.err.println("[Datastore][READ][User] - can't load user since google user is null");
			return null;
		}
		Key user_key = KeyFactory.createKey("DropboxUser", u.getUserId());
		DropboxUser du = null;		
		try {
			du = pm.getObjectById(DropboxUser.class, user_key);
			
			
		} catch (Exception e) {
			System.err.println("[Datastore][READ][User] - can't load user");
			return null;
		}
		return du;
	}
	
	
	
	public static void commit(){
		if(pm != null){
			pm.close();
			pm = null;
		}
	}
	
	

}
