package fr.umontpellier.projetweb;

import java.io.IOException;
import java.util.Collection;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.umontpellier.service.ServiceListeMonument;
import packageTest.ListMonument;
import packageTest.ListVille;
import packageTest.MonumentUtils;
import packageTest.PersonneUtils;
import packageTestHibernate.Monument;
import packageTestHibernate.EcoleMusicale;
import packageTestHibernate.User;
import packageTestHibernate.Role;
@Controller
public class MainController extends HttpServlet{
    
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BdPersist");
	EntityManager em = emf.createEntityManager();
	
	EntityTransaction ex = em.getTransaction();

	@GetMapping(path = "/")
	public String home() {
		return "index";
	}
	
	@GetMapping(path = "/admin")
	public String homeAdmin() {
		return "admin";
	}
	
	@GetMapping(path = "/rechercher")
	public String rechercher() {
		return "rechercher";
	}
    /*
 @GetMapping(path = "/list")
	public String unMonument(@RequestParam(name = "name", required = false, defaultValue = "anonymous") String name,
			Model model) {
		model.addAttribute("name", name);
		model.addAttribute("monuments",new MonumentUtils(Persistence.createEntityManagerFactory("BdPersist").createEntityManager()).findAll_SameFirstLetter("SQUARE_MOLIERE"));
		
		
		
		
		
		return "list";
	}
	*/
  

    @GetMapping(path = "/list")
   	public String list(@RequestParam(name = "name", required = false, defaultValue = "anonymous") String name,
   			Model model) {
   		model.addAttribute("name", name);
   		model.addAttribute("monuments",new ServiceListeMonument(em).findAll());
   		model.addAttribute("lieux",new ServiceListeMonument(em).findAllLieu());
   		model.addAttribute("ecoles",new ServiceListeMonument(em).findAllEcole());
   		model.addAttribute("pistes",new ServiceListeMonument(em).findAllPisteCyclable());
   		model.addAttribute("sportnature",new ServiceListeMonument(em).findAllSiteSportNature());
   		model.addAttribute("user",new ServiceListeMonument(em).findAllUser());
   		model.addAttribute("userTouriste",new ServiceListeMonument(em).findUserTouriste());




   		model.addAttribute("monumentPezenas",new ServiceListeMonument(em).findMonumentByLieu());
   		model.addAttribute("nbrMonumentByLieu",new ServiceListeMonument(em).nbrMonumentByLieu());
   		model.addAttribute("nbrEcoleMusicByLieu",new ServiceListeMonument(em).nbrEcoleMusicByLieu());



   		return "list";
   	}
    
   

    /*@PostMapping(path = "/cnxAdmin")
   	public String findByLoginPassword(@RequestParam(name = "name", required = false, defaultValue = "anonymous") String name,
   	RedirectAttributes attributes,@RequestParam String login,@RequestParam String password) {
   		attributes.addFlashAttribute("cnxAdmin",new ServiceListeMonument(em).findAdminByLoginPassword(login,password));
   		return "redirect:/";
   	}*/
   
   	protected void doGet(HttpServletRequest request, HttpServletResponse response
   			) 
   	throws ServletException, IOException 
    
    {
   		this.getServletContext().getRequestDispatcher("/index.html").forward(request, response);
    }
    
   	public void doPost(HttpServletRequest request, HttpServletResponse response
   			) 
   	throws ServletException, IOException 
    
    {
   		String login = request.getParameter("login");
   		String password = request.getParameter("password");
   		
   		//les stocké en variables de session
   		HttpSession maSession = request.getSession(); // initie le moteur de session de Java EE
   		maSession.setAttribute("login",login); //met en memoire les infos
   		maSession.setAttribute("password",password);
   		
   		
   		this.getServletContext().getRequestDispatcher("/").forward(request, response);
   		
   	}
    
    @PostMapping(path = "/cnxTouriste")
   	public String findTouriByLoginPassword(@RequestParam(name = "name", required = false, defaultValue = "anonymous") String name,
   	RedirectAttributes attributes,@RequestParam String login,@RequestParam String password) {
   		attributes.addFlashAttribute("cnxTouriste",new ServiceListeMonument(em).findTouristeByLoginPassword(login,password));
   		return "redirect:/";
   	} 

    

    @PostMapping(path = "/unMonument")
   	public String findByZeohash(@RequestParam(name = "name", required = false, defaultValue = "anonymous") String name,
   	RedirectAttributes attributes,@RequestParam String geohach) {
   		attributes.addFlashAttribute("unmonument",new ServiceListeMonument(em).findByGeohach(geohach));


   		
   		return "redirect:/rechercher";
   	}
    @PostMapping(path = "/uneCelebrite")
   	public String findByMonument(@RequestParam(name = "name", required = false, defaultValue = "anonymous") String name,
   	RedirectAttributes attributes,@RequestParam String nomMonument) {
   		attributes.addFlashAttribute("unecelebrite",new ServiceListeMonument(em).findCelebriteByMonument(nomMonument));


   		
   		return "redirect:/rechercher";
   		//faire exemple avec 'PROMENADE DU PEYROU'
   	}
    


	@PostMapping(path="/ajout")
    
	public String ajoutmonument(@RequestParam String geohash, 
			                @RequestParam String nom,
			                @RequestParam String proprietaire,
			                @RequestParam String typeM,
			                @RequestParam float longitude,
			                @RequestParam float latitude,
			                @RequestParam String codeInsee,Model model) {
		Monument m = new Monument(geohash,nom, proprietaire,typeM, longitude, latitude,codeInsee);

		ex.begin();
		em.persist(m);
		ex.commit();
		
		

		return "redirect:/list";
		
	}
	
  
	@PostMapping(path="/remove")
    
	public String removemonument(@RequestParam String geohash) {
		 
		Monument p = em.find(Monument.class,geohash);
		 if (p != null) {
    	ex.begin();
		em.remove(p);				
		ex.commit();
		
		
		 }

		return "redirect:/list";
		
	}
	
	@PostMapping(path="/updateMonument")
    public String updateMonument(@RequestParam String geohash,@RequestParam String nomModifier) {
		ex.begin();
		Monument e = em.find(Monument.class,geohash);
		 if (e != null) {
	    	
	    	e.setNom(nomModifier);	
	    	//on ne peut pas porter la modification sur un attribut de type @Id
	    	//ici Nom  ne doit pas etre annoté par @Id
			
		
		
		 }
		 ex.commit();

		return "redirect:/list";
		
	}
	
	@PostMapping(path="/updateEcole")
   public String updateEcole(@RequestParam String nomEcole,@RequestParam String newCodeInsee) {
		ex.begin();
		EcoleMusicale e = em.find(EcoleMusicale.class, nomEcole);
		 if (e != null) {
	    	
	    	e.setCodeInsee(newCodeInsee);			
			
		
		
		 }
		 ex.commit();

		return "redirect:/list";
		
	}
	
	//VILLE ET PISTE CYCLABLE
	//select p.nomPiste, l.codeInsee, p.villeDepart  from pistecyclable p , lieu l where l.nomCom=p.villeDepart;
	
	
	

	






 

 


}
