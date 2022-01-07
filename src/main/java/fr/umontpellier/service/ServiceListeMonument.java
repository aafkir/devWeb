package fr.umontpellier.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.umontpellier.dao.MonumentDAO;
import fr.umontpellier.dao.MonumentRepository;
import packageTestHibernate.AssocieA;
import packageTestHibernate.Celebrite;
import packageTestHibernate.EcoleMusicale;
import packageTestHibernate.Lieu;
import packageTestHibernate.Role;
import packageTestHibernate.User;
import packageTestHibernate.Monument;
import packageTestHibernate.PisteCyclable;
import packageTestHibernate.SiteSportDeNature;

@Service
@Transactional
public class ServiceListeMonument implements IServiceListeMonument{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BdPersist");
	EntityManager em = emf.createEntityManager();

	EntityTransaction ex = em.getTransaction();
	
    
    
    
	@Autowired
	MonumentDAO monumentDAO;
	
	@Autowired
	MonumentRepository monumentRepository;
	
	public ServiceListeMonument(EntityManager em) {
		this.em=em;
	}
	
	public boolean save(Monument monument) throws Exception  
	{
		monumentDAO.save(monument);
		return false;
	}
	
	  public Collection<Monument> findByNom(String name) {
		  
		    Query query = em.createQuery("SELECT p FROM Monument p where nom like :name");
		    query.setParameter("name", name);
		    return (Collection<Monument>) query.getResultList();
		    //return em.find(Monument.class,nom);
		  }
	  
	  public Collection<Monument> findByGeohach(String geohash) {
		  
		    Query query = em.createQuery("SELECT p FROM Monument p where geohash like :geo");
		    query.setParameter("geo", geohash);
		    return (Collection<Monument>) query.getResultList();
		    //return em.find(Monument.class,geohash);
		  }
	  
	  public Collection<User> findAdminByLoginPassword(String login,String password) {
		  
		    Query query = em.createQuery("SELECT p FROM User p where login "
		    		+ "like :login and password like :password");
		    query.setParameter("login", login);
		    query.setParameter("password", password);
		    return (Collection<User>) query.getResultList();
		    //return em.find(Monument.class,geohash);
		  }
	  
	  public Collection<User> findTouristeByLoginPassword(String login,String password) {
		  
		    Query query = em.createQuery("SELECT p FROM User p where login "
		    		+ "like :login and password like :password");
		    query.setParameter("login", login);
		    query.setParameter("password", password);
		    return (Collection<User>) query.getResultList();
		    //return em.find(Monument.class,geohash);
		  }
	  
	  
	public Iterable<Monument> findAll(){
		 Query query = em.createQuery("SELECT p FROM Monument p ORDER BY geohash DESC");
		 query.setMaxResults(10);
		 return (Iterable<Monument>) query.getResultList();	}
	
	  public Monument createMonument(String geohash, String nom, String proprietaire, String typeM, float longitude, float latitude,String codeInsee) {
		  Monument p = new Monument(geohash,nom, proprietaire,typeM, longitude, latitude,codeInsee);
	  
	    return p;
	  }
	  
		public Iterable<Lieu> findAllLieu(){
			 Query query = em.createQuery("SELECT p FROM Lieu p ");
			 query.setMaxResults(10);
			 return (Iterable<Lieu>) query.getResultList();	
			 }
		
		public Iterable<EcoleMusicale> findAllEcole(){
			 Query query = em.createQuery("SELECT p FROM EcoleMusicale p ORDER BY codeInsee DESC");
			 query.setMaxResults(10);
			 return (Iterable<EcoleMusicale>) query.getResultList();	
			 }
		
		public Iterable<PisteCyclable> findAllPisteCyclable(){
			 Query query = em.createQuery("SELECT p FROM PisteCyclable p");
			 query.setMaxResults(10);
			 return (Iterable<PisteCyclable>) query.getResultList();	
			 }
		
		public Iterable<SiteSportDeNature> findAllSiteSportNature(){
			 Query query = em.createQuery("SELECT p FROM SiteSportDeNature p ORDER BY codeInsee DESC");
			 query.setMaxResults(10);
			 return (Iterable<SiteSportDeNature>) query.getResultList();	
			 }
		
		public Iterable<AssocieA> findAllAssocie(){
			 Query query = em.createQuery("SELECT a FROM AssocieA a");
			 return (Iterable<AssocieA>) query.getResultList();	
			 }
		
		public Iterable<User> findAllUser(){
			 Query query = em.createQuery("SELECT a FROM User a");
			 return (Iterable<User>) query.getResultList();	
			 }
		
		public String findUserTouriste(){
			ex.begin();
			ex.commit();
			String a =	"";
			Role role = em.find(Role.class,1 );
	        for(User aa: role.getUsers()) {
	        	a += "\n\r"+aa.getLogin();
	        }
	     return a;
	    }
		
		public String findUserAdmin(){
			ex.begin();
			ex.commit();
			String a =	"";
			Role role = em.find(Role.class,2 );
	        for(User aa: role.getUsers()) {
	        	a += "\n\r"+aa.getLogin();
	        }
	     return a;
	    }
	
		public Iterable<Monument> findMonumentByLieu(){
			 Query query = em.createQuery("SELECT  m FROM Lieu l, Monument m where  l.codeInsee = m.codeInsee and nomCom='PEZENAS' ");
			 
			 return (Iterable<Monument>) query.getResultList();	
			 }
		
		
		public List nbrMonumentByLieu(){
			 Query query = em.createQuery("SELECT  count(m.nom), l.nomCom ,l.codeInsee FROM Lieu l, Monument m where  l.codeInsee = m.codeInsee group by l.nomCom ");
			 
			 return  (List) query.getResultList();	
			 }
		
		
		public List nbrEcoleMusicByLieu(){
			 Query query = em.createQuery("SELECT  count(e.nomEcole), l.nomCom,l.codeInsee  FROM Lieu l, EcoleMusicale e where  l.codeInsee = e.codeInsee group by l.nomCom ");
			 query.setMaxResults(5);
			 return  (List) query.getResultList();	
			 }
		
		
		//nbr ecolemusic par lieu SELECT  count(m.nomEcole), l.nomCom ,l.codeInsee FROM Lieu l, ecolemusicale m where  l.codeInsee = m.codePostal group by l.nomCom ;

		
		
		//Faire methode pour la requete suivante:
		//ATTENTION: requete sensible a la cass: les attibuts doitvent
		//etre identique à ceux de la class associe : numCelebrite =! numcelebrite
		//	select c.nom from associea a, celebrite c ,monument m where c.numcelebrite = a.numcelebrite and m.geohash = a.codem;
		public Iterable<Celebrite> findCelebriteByMonument(String nomMonument){
			 Query query = em.createQuery("SELECT  c FROM AssocieA a,"
			 		+ "Monument m, Celebrite c  where c.numCelebrite = a.numCelebrite "
			 		+ "and m.geohash = a.codeM and m.nom like :nomMonument");
			 query.setParameter("nomMonument", nomMonument);
			 return (Iterable<Celebrite>) query.getResultList();	
			 }

		
	


}
