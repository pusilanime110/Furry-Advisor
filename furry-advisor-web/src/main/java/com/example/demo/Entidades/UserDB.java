package com.example.demo.Entidades;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Clase de la entidad User en la BD
@Entity
public class UserDB implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int user_id;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false,unique=true)
	private String nickname;
	
	private String email;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "userRef", cascade=CascadeType.ALL, orphanRemoval = true)
	private List<ReviewDB> reviews;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private List<String> roles;

	@Lob
	//@Column(columnDefinition="TEXT")
	@JsonIgnore
	private String profPhoto;
	
	public UserDB(){};
	
	public UserDB(String nk, String psw, String em, String photo, String rol) {
		setNickname(nk);
		setPassword(psw);
		email = em;
		profPhoto = photo;
		roles = new ArrayList<>();
		roles.add(rol);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProf_photo() {
		return profPhoto;
	}

	public void setProf_photo(String prof_photo) {
		this.profPhoto = prof_photo;
	}
	
	public List<ReviewDB> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewDB> reviews) {
		this.reviews = reviews;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
