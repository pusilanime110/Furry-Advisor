package com.example.demo.Controladores;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entidades.PlaceDB;
import com.example.demo.Entidades.UserDB;
import com.example.demo.Interfaces.PlaceDBInterface;
import com.example.demo.Interfaces.UserDBInterface;

@Controller
public class ProfileController implements CommandLineRunner {
	
	private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"),"images");
	@Autowired
	private UserDBInterface userRepository;
	@Autowired 
	private PlaceDBInterface placeRepository;

	@PostMapping("/profile")
	public String profile(HttpSession http, Model model, @RequestParam String userName, @RequestParam String userPassword) {
	 
		UserDB actualUser = userRepository.findByNickname(userName).get(0);
		http.setAttribute("actUser", actualUser);
		
		model.addAttribute("name", actualUser.getNickname());
		model.addAttribute("password",userPassword);
		model.addAttribute("user",actualUser);
		
		List<PlaceDB> places = placeRepository.findAll();
		model.addAttribute("n1",places.get(0).getName());
		model.addAttribute("t1",places.get(0).getType());
		model.addAttribute("v1",places.get(0).getRating());
		
		return "profile";
	}
	
	@PostMapping("/upload_image")
	public String uploadImage(HttpSession http, @RequestParam MultipartFile image) throws IOException {
		/*Files.createDirectories(IMAGES_FOLDER);
		Path imagePath = IMAGES_FOLDER.resolve("perfil.jpg");
		image.transferTo(imagePath);*/
		UserDB user = (UserDB)http.getAttribute("actUser");
		user.setProf_photo(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
		userRepository.save(user);
		return "redirect:profile";
	}	
	
	@GetMapping("/image")
	public ResponseEntity<Object> downloadImage(HttpSession http, Model model) throws MalformedURLException, SQLException {
		UserDB user = (UserDB)http.getAttribute("actUser");
		if (user.getProf_photo() != null) {
			Resource image = new InputStreamResource(user.getProf_photo().getBinaryStream());
			return ResponseEntity.ok()
					 .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					 .contentLength(user.getProf_photo().length())
					 .body(image);
		}else {
			System.out.println("No hay foto");
			return  ResponseEntity.notFound().build();
		}
		
	}
	
	//La pagina de login y de register son distintas, luego hacer 2 htmls y 2 controladores por separado
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}