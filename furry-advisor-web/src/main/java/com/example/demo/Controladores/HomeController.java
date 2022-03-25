package com.example.demo.Controladores;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.hibernate.engine.jdbc.BlobProxy;
//import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Entidades.DealDB;
import com.example.demo.Entidades.LocationDB;
import com.example.demo.Entidades.PlaceDB;
import com.example.demo.Entidades.PlaceTypeDB;
import com.example.demo.Entidades.ReviewDB;
import com.example.demo.Entidades.UserDB;
import com.example.demo.Interfaces.DealDBInterface;
import com.example.demo.Interfaces.PlaceDBInterface;
import com.example.demo.Interfaces.ReviewDBInterface;
import com.example.demo.Interfaces.UserDBInterface;
import com.example.demo.Services.DealService;
import com.example.demo.Services.LocationService;
import com.example.demo.Services.PlaceService;
import com.example.demo.Services.PlaceTypeService;
import com.example.demo.Services.ReviewService;
import com.example.demo.Services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

//Clase del controlador encargado de gestionar las peticiones surgidas en el HTML Home
//y de inicializar las entidades ejemplo usadas en la aplicación web
@Controller
public class HomeController {

	//Lo del csrf y el problema de los - en las urls de los endpoints
	private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"),"images");
	
	
	@Autowired
	private PlaceService placeRepository;
	
	@Autowired
	private PlaceTypeService placeTypeRepository;
	
	@Autowired
	private LocationService locationRepository;
	
	@Autowired
	private UserService userRepository;
	
	@Autowired
	private DealService dealRepository;
	
	@Autowired
	private ReviewService reviewRepository;

	@PostConstruct
	public void init() throws ParseException, IOException, URISyntaxException {
		
		//El problema era que no se usaba el mismo objeto para hacer hash (BCryptPasswordEncoder vs PasswordEncoder)
		/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String pass1 = encoder.encode("sasageyo");
		String pass2 = encoder.encode("tierrasanta");
		String pass3 = encoder.encode("blockchain");
		String pass4 = encoder.encode("asia");
		String pass5 = encoder.encode("huevoscocidos");
		String pass6 = encoder.encode("brumbrum");
		
		UserDB use1 = new UserDB("xxVicente69xx",pass1,"manitas@gmail.com",null,"ROLE_ADMIN");
		UserDB use2 = new UserDB("Javier",pass2,"h0iboy@hotmail.com",null,"ROLE_USER");
		UserDB use3 = new UserDB("Vico420",pass3,"c.ham.pion@outlook.com",null,"ROLE_USER");
		UserDB use4 = new UserDB("Javapor",pass4,"vaperwave@hotmail.com",null,"ROLE_USER");
		UserDB use5 = new UserDB("CMarrano",pass5,"sunday_girl@gmail.com",null,"ROLE_USER");
		UserDB use6 = new UserDB("LoboCastellano",pass6,"motorstormer@gmail.com",null,"ROLE_USER");
		
		LocationDB loc1 = new LocationDB("Mostoles");
		LocationDB loc2 = new LocationDB("Badajoz");
		LocationDB loc3 = new LocationDB("Oviedo");
		LocationDB loc4 = new LocationDB("Castellon");
		LocationDB loc5 = new LocationDB("Madrid");
		LocationDB loc6 = new LocationDB("Valladolid");
		LocationDB loc7 = new LocationDB("Albacete");
		
		PlaceTypeDB type1 = new PlaceTypeDB("Restaurante");
		PlaceTypeDB type2 = new PlaceTypeDB("Bar");
		PlaceTypeDB type3 = new PlaceTypeDB("Club");
		PlaceTypeDB type4 = new PlaceTypeDB("Parque");
		
		PlaceDB pla1 = new PlaceDB("Panda Ramen",type1,loc1,"Descripcion1","URL1",3,"C/Don Juan","Schedule1",null);
		PlaceDB pla2 = new PlaceDB("Simba's Breakfast",type1,loc2,"Descripcion2","URL2",4,"C/Recuerdo","Schedule2",null);
		PlaceDB pla3 = new PlaceDB("Escupe el Fuego",type1,loc3,"Descripcion3","URL3",1,"C/Hincada","Schedule3",null);
		PlaceDB pla4 = new PlaceDB("La Pelusa",type2,loc4,"Descripcion4","URL4",5,"C/Margarina","Schedule4",null);
		PlaceDB pla5 = new PlaceDB("Foxxes Bar",type2,loc5,"Descripcion5","URL5",3,"C/Carrera","Schedule5",null);
		PlaceDB pla6 = new PlaceDB("Pelusa Picarona",type3,loc6,"Descripcion6","URL6",4,"C/Me Falta Un Tornillo","Schedule6",null);
		PlaceDB pla7 = new PlaceDB("Parque Aguadulce",type4,loc7,"Descripcion7","URL7",2,"C/Severo Ochoa","Schedule7",null);
		
		DealDB deal1 = new DealDB("Comisiones abiertas","Description1",null,pla2);
		DealDB deal2 = new DealDB("10% en ramen","Description2",null,pla1);
		DealDB deal3 = new DealDB("2x1 en chupitos de absenta","Description3",null,pla6);
		DealDB deal4 = new DealDB("Galletas con nata gratis","Description4",null,pla3);
		DealDB deal5 = new DealDB("Reunion en Parque Aguadulce","Description5",null,pla7);
		
		String txt1 = "El lugar no esta bien. No ofrecen lo que dicen";
		Date dtR1 = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01");
		ReviewDB rev1 = new ReviewDB(2,txt1,dtR1,47,use5,pla3);//use5
		String txt2 = "Ramen asqueroso";
		Date dtR2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-26");
		ReviewDB rev2 = new ReviewDB(1,txt2,dtR2,96,use3,pla1);//use3
		String txt3 = "El mejor día de mi vida <3";
		Date dtR3 = new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-14");
		ReviewDB rev3 = new ReviewDB(5,txt3,dtR3,24,use1,pla7);//use1
		String txt4 = "RECOMENDADISIMO!!";
		Date dtR4 = new SimpleDateFormat("yyyy-MM-dd").parse("2014-04-20");
		ReviewDB rev4 = new ReviewDB(4,txt4,dtR4,5,use6,pla5);//use6
	
		
		List<ReviewDB> aux6 = new ArrayList<>();
		aux6.add(rev1);
		List<ReviewDB> aux7 = new ArrayList<>();
		aux7.add(rev2);
		List<ReviewDB> aux8 = new ArrayList<>();
		aux8.add(rev3);
		List<ReviewDB> aux9 = new ArrayList<>();
		aux9.add(rev4);
		use5.setReviews(aux6);
		use3.setReviews(aux7);
		use1.setReviews(aux8);
		use6.setReviews(aux9);
		use2.setReviews(new ArrayList<>());
		use4.setReviews(new ArrayList<>());
		
		placeTypeRepository.save(type1);
		placeTypeRepository.save(type2);
		placeTypeRepository.save(type3);
		placeTypeRepository.save(type4);
		
		locationRepository.save(loc1);
		locationRepository.save(loc2);
		locationRepository.save(loc3);
		locationRepository.save(loc4);
		locationRepository.save(loc5);
		locationRepository.save(loc6);
		locationRepository.save(loc7);
		
		
		
		placeRepository.save(pla1);
		placeRepository.save(pla2);
		placeRepository.save(pla3);
		placeRepository.save(pla4);
		placeRepository.save(pla5);
		placeRepository.save(pla6);
		placeRepository.save(pla7);
		
		dealRepository.save(deal1);
		dealRepository.save(deal2);
		dealRepository.save(deal3);
		dealRepository.save(deal4);
		dealRepository.save(deal5);
		
		reviewRepository.save(rev1);
		reviewRepository.save(rev2);
		reviewRepository.save(rev3);
		reviewRepository.save(rev4);
		
		userRepository.save(use1);
		userRepository.save(use2);
		userRepository.save(use3);
		userRepository.save(use4);
		userRepository.save(use5);
		userRepository.save(use6);
		
		
		Path imagePath1 = IMAGES_FOLDER.resolve("perfil1.jpg");
		File img1 = new File(imagePath1.toUri());
		FileInputStream input1 = new FileInputStream(img1);
		use1.setProf_photo(BlobProxy.generateProxy(input1, Files.size(imagePath1)));
		userRepository.save(use1);
		Path imagePath2 = IMAGES_FOLDER.resolve("unknown.jpg");
		File img2 = new File(imagePath2.toUri());
		FileInputStream input2 = new FileInputStream(img2);
		use2.setProf_photo(BlobProxy.generateProxy(input2, Files.size(imagePath2)));
		userRepository.save(use2);
		File img3 = new File(imagePath2.toUri());
		FileInputStream input3 = new FileInputStream(img3);
		use3.setProf_photo(BlobProxy.generateProxy(input3, Files.size(imagePath2)));
		userRepository.save(use3);
		File img4 = new File(imagePath2.toUri());
		FileInputStream input4 = new FileInputStream(img4);
		use4.setProf_photo(BlobProxy.generateProxy(input4, Files.size(imagePath2)));
		userRepository.save(use4);
		File img5 = new File(imagePath2.toUri());
		FileInputStream input5 = new FileInputStream(img5);
		use5.setProf_photo(BlobProxy.generateProxy(input5, Files.size(imagePath2)));
		userRepository.save(use5);
		File img6 = new File(imagePath2.toUri());
		FileInputStream input6 = new FileInputStream(img6);
		use6.setProf_photo(BlobProxy.generateProxy(input6, Files.size(imagePath2)));
		userRepository.save(use6);
		
		Path imagePath3 = IMAGES_FOLDER.resolve("restaurante.jpg");
		File img7 = new File(imagePath3.toUri());
		FileInputStream input7 = new FileInputStream(img7);
		pla1.setPlacePic(BlobProxy.generateProxy(input7, Files.size(imagePath3)));
		placeRepository.save(pla1);
		File img8 = new File(imagePath3.toUri());
		FileInputStream input8 = new FileInputStream(img8);
		pla2.setPlacePic(BlobProxy.generateProxy(input8, Files.size(imagePath3)));
		placeRepository.save(pla2);
		File img9 = new File(imagePath3.toUri());
		FileInputStream input9 = new FileInputStream(img9);
		pla3.setPlacePic(BlobProxy.generateProxy(input9, Files.size(imagePath3)));
		placeRepository.save(pla3);
		File img10 = new File(imagePath3.toUri());
		FileInputStream input10 = new FileInputStream(img10);
		pla4.setPlacePic(BlobProxy.generateProxy(input10, Files.size(imagePath3)));
		placeRepository.save(pla4);
		File img11 = new File(imagePath3.toUri());
		FileInputStream input11 = new FileInputStream(img11);
		pla5.setPlacePic(BlobProxy.generateProxy(input11, Files.size(imagePath3)));
		placeRepository.save(pla5);
		File img12 = new File(imagePath3.toUri());
		FileInputStream input12 = new FileInputStream(img12);
		pla6.setPlacePic(BlobProxy.generateProxy(input12, Files.size(imagePath3)));
		placeRepository.save(pla6);
		File img13 = new File(imagePath3.toUri());
		FileInputStream input13 = new FileInputStream(img13);
		pla7.setPlacePic(BlobProxy.generateProxy(input13, Files.size(imagePath3)));
		placeRepository.save(pla7);
		
		Path imagePath4 = IMAGES_FOLDER.resolve("oferta2.jpg");
		File img14 = new File(imagePath4.toUri());
		FileInputStream input14 = new FileInputStream(img14);
		deal1.setDealPic(BlobProxy.generateProxy(input14, Files.size(imagePath4)));
		dealRepository.save(deal1);
		File img15 = new File(imagePath4.toUri());
		FileInputStream input15 = new FileInputStream(img15);
		deal2.setDealPic(BlobProxy.generateProxy(input15, Files.size(imagePath4)));
		dealRepository.save(deal2);
		File img16 = new File(imagePath4.toUri());
		FileInputStream input16 = new FileInputStream(img16);
		deal3.setDealPic(BlobProxy.generateProxy(input16, Files.size(imagePath4)));
		dealRepository.save(deal3);
		File img17 = new File(imagePath4.toUri());
		FileInputStream input17 = new FileInputStream(img17);
		deal4.setDealPic(BlobProxy.generateProxy(input17, Files.size(imagePath4)));
		dealRepository.save(deal4);
		File img18 = new File(imagePath4.toUri());
		FileInputStream input18 = new FileInputStream(img18);
		deal5.setDealPic(BlobProxy.generateProxy(input18, Files.size(imagePath4)));
		dealRepository.save(deal5);*/
	}


	@GetMapping("/home")
	public String home(Model model,HttpSession http) {
		UserDB actualUser = (UserDB)http.getAttribute("actUser");
		model.addAttribute("user",actualUser);
		List<DealDB> deals = dealRepository.findAllByPlaceOriginIsNotNull();
		
		int max = deals.size();
		int min = 0;
		int dl1 = (int) (Math.random() * (max - min - 1) + min);
		DealDB dealDB1 = deals.get(dl1);
		int dl2 = (int) (Math.random() * (max - min - 1) + min);
		while(dl1==dl2) {
			dl2 = (int) (Math.random() * (max - min - 1) + min);
		}
		DealDB dealDB2 = deals.get(dl2);
		
		model.addAttribute("place_name1", dealDB1.getPlaceOrigin().getName());
		model.addAttribute("place_name2", dealDB2.getPlaceOrigin().getName());
		model.addAttribute("deal_image1", dealDB1.getDealPic());
		model.addAttribute("deal_image2", dealDB2.getDealPic());
		model.addAttribute("deal_header1", dealDB1.getHeader());
		model.addAttribute("deal_header2", dealDB2.getHeader());
		
		List<PlaceDB> places = placeRepository.findAll();

        int maxPlace = places.size();
        int minPlace = 0;
        int pl1=(int)Math.random()*(maxPlace - minPlace - 1) + minPlace;
        int pl2=(int)Math.random()*(maxPlace - minPlace - 1) + minPlace;
        PlaceDB place3 = places.get(pl1);
        PlaceDB place4 = places.get(pl2);
        model.addAttribute("place_name3",place3.getName());
        model.addAttribute("place_horario1",place3.getSchedule());
        model.addAttribute("place_bio1",place3.getDescription());
        model.addAttribute("place_name4",place4.getName());
        model.addAttribute("place_horario2",place4.getSchedule());
        model.addAttribute("place_bio2",place4.getDescription());
        model.addAttribute("place",dealDB1.getPlaceOrigin().getName());
        model.addAttribute("offer",dealDB1.getHeader());
        
        http.setAttribute("place", dealDB1.getPlaceOrigin().getName());
        http.setAttribute("offer", dealDB1.getHeader());
		
		return "home";
	}
	
	@GetMapping("/perfil")
	public ResponseEntity<Object> perfil(HttpSession http, Model model) throws MalformedURLException, SQLException {
		List<DealDB> deals = dealRepository.findAllByPlaceOriginIsNotNull();
		int maxPlace = deals.size();
        int minPlace = 0;
        int pl1=(int)Math.random()*(maxPlace - minPlace - 1) + minPlace;
		DealDB dealDB1 = deals.get(pl1);
		
		if (dealDB1.getDealPic() != null) {
			Resource image = new InputStreamResource(dealDB1.getDealPic().getBinaryStream());
			return ResponseEntity.ok()
					 .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					 .contentLength(dealDB1.getDealPic().length())
					 .body(image);
		}else {
			System.out.println("No hay foto");
			return  ResponseEntity.notFound().build();
		}
		
	}
	
	@GetMapping("/checkRest")
	public ModelAndView checkRest() {
		RestTemplate rest = new RestTemplate();
		String base = "http://localhost:8080";
		String url = base+"/existingDeal";
		//DEVUELVE ARRAYNODE, NO OBJENODE. ESOS ESTAN DENTROn
		ArrayNode data = rest.getForObject(url, ArrayNode.class);
		for(int i = 0; i<data.size();i++) {
			System.out.println(data.get(i).get("header").asText());
		}
		
		List<DealDB> deals = dealRepository.findByHeader("cabecera");
		System.out.println(deals.get(0).getHeader());
		
		System.out.println("La mamba negra de Aisayan es chiquita");
		return new ModelAndView("redirect:/home");
	}
	
	/*@PostMapping("/prueba")
	public ModelAndView prueba() {
		RestTemplate rest = new RestTemplate();

		DealDB newDeal = new DealDB("cabecera","descripcion",null, placeRepository.findByName("Panda Ramen").get(0));

		String base = "http://localhost:8080";
		String url = base+"/pruebaRest";
		rest.postForEntity(url, newDeal,DealDB.class);
		System.out.println("Prueba");
		return new ModelAndView("redirect:/home");
	}
	
	@PostMapping("/addNewDeal")
	public ModelAndView addNewDeal(Model model,HttpSession http) throws URISyntaxException {
		RestTemplate rest = new RestTemplate();
		DealDB newDeal = new DealDB("cabecera","descripcion",null, placeRepository.findByName("Panda Ramen").get(0));
		
		/*HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<DealDB> entity = new HttpEntity<>(newDeal,headers);*/
		/*String base = "http://localhost:8080";
		String url = base+"/addDeal";
		URI uri = new URI(url);
		ResponseEntity<DealDB> aux = rest.postForEntity(uri, newDeal,DealDB.class);

	    model.addAttribute("place",http.getAttribute("place"));
	    model.addAttribute("offer",http.getAttribute("offer"));
		System.out.println("Subido");
		return new ModelAndView("redirect:/home");
	}*/
}
