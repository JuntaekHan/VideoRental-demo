package video.rental.demo.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum PersistenceManager {
	INSTANCE;
	 
	private EntityManagerFactory emFactory;
	  
	private PersistenceManager() {
		// "video" was the value of the name attribute of the
		// persistence-unit element.
	    emFactory = Persistence.createEntityManagerFactory("video");
	}
	
	public EntityManager getEntityManager() {
	    return emFactory.createEntityManager();
	}
	
	public void close() {
	    emFactory.close();
	}
}
