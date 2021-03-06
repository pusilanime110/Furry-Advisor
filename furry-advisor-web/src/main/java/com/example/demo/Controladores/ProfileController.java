package com.example.demo.Controladores;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.ImageUtils;
import com.example.demo.NewOffer;
import com.example.demo.Entidades.PlaceDB;
import com.example.demo.Entidades.ReviewDB;
import com.example.demo.Entidades.UserDB;
import com.example.demo.Interfaces.PlaceDBInterface;
import com.example.demo.Interfaces.ReviewDBInterface;
import com.example.demo.Interfaces.UserDBInterface;
import com.example.demo.Services.ReviewService;
import com.example.demo.Services.UserService;

//Clase del controlador encargado de gestionar las peticiones surgidas en el HTML Profile
@Controller
public class ProfileController implements CommandLineRunner {

	@Autowired
	private UserComponent component;
	
	@Autowired
	public NewOffer newOffer;

	@Autowired
	private UserService userRepository;
	
	@Autowired 
	private ReviewService reviewRepository;

	@GetMapping("/profile/{userName}")
	public String profile(HttpSession http, Model model, @PathVariable String userName, HttpServletRequest request) {
	 
		SecurityContextImpl aux = (SecurityContextImpl)http.getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication auth = aux.getAuthentication();
		
		UserDB actualUser = userRepository.findByNickname(auth.getName()).get(0);

		model.addAttribute("newoffer",newOffer.getNewOffer());
		component.setLoggedUser(actualUser);
		model.addAttribute("name", actualUser.getNickname());
		model.addAttribute("password",actualUser.getPassword());
		model.addAttribute("user",actualUser);
		model.addAttribute("place",http.getAttribute("place"));
	    model.addAttribute("offer",http.getAttribute("offer"));
	    
	    model.addAttribute("admin",request.isUserInRole("ADMIN"));
		
		List<ReviewDB> revs = reviewRepository.findByUserRef(actualUser);
		model.addAttribute("user_reviews",revs);
		
		return "profile";
			
			
	}
	
	
	
	
	@GetMapping("/image")
	public ResponseEntity<Object> downloadImage(HttpSession http, Model model) throws MalformedURLException, SQLException {
		UserDB user = component.getLoggedUser();
		if (user.getProf_photo() != null) {
			Resource image = ImageUtils.imageStringToResource(user.getProf_photo());
			return ResponseEntity.ok()
					 .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					 .contentLength(user.getProf_photo().length())
					 .body(image);
		}else {
			System.out.println("No hay foto");
			return  ResponseEntity.notFound().build();
		}
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
