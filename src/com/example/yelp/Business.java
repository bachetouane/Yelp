package com.example.yelp;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Business implements Serializable{
	private String id;
	private String name;
	private String phone;
	private String imageUrl;
	private double rating;
	private String snippetText;
	private String ratingImgUrl;
	private String snippetImgUrl;
	
	public Business(){
		super();
	}
	
	 public static Business fromJson(JSONObject jsonObject) {
		 //Parser les données récupérées au format JSON en Objet
		  	Business b = new Business();
		    try {
		  		b.id = jsonObject.getString("id");
		        	b.name = jsonObject.getString("name");
		        	if (!jsonObject.get("phone").toString().isEmpty())
		        	{
		        		b.phone = jsonObject.getString("display_phone");
		        	}
		        	b.imageUrl = jsonObject.getString("image_url");
		        	b.rating = jsonObject.getDouble("rating");
		        	b.ratingImgUrl = jsonObject.getString("rating_img_url");
		        	b.snippetText = jsonObject.getString("snippet_text");
		        	b.snippetImgUrl =jsonObject.getString("snippet_image_url");
		        } catch (JSONException e) {
		            e.printStackTrace();
		            return null;
		        }
		  	return b;
		  }

	  public static ArrayList<Business> fromJson(JSONArray jsonArray) {
		  //On va Parcourir notre JSON et en faire une liste d'objet Business
	      ArrayList<Business> businesses = new ArrayList<Business>(jsonArray.length());
	      // Pour cela on va envoyer chaque objet json de la liste dans la fonction du dessus
	      for (int i=0; i < jsonArray.length(); i++) {
	          JSONObject businessJson = null;
	          try {
	        	//On recupere dans un objet JSON l'element i du tableau JSON récupéré  
	          	businessJson = jsonArray.getJSONObject(i);
	          } catch (Exception e) {
	              e.printStackTrace();
	              continue;
	          }
	          //On va ensuite envoyer cet Objet JSON a notre premiere fonction pour en créer un objet 
	          Business business = Business.fromJson(businessJson);
	          if (business != null) {
	        	//On va ensuite l'ajouter à notre liste d'objet business
	          	businesses.add(business);
	          }
	      }

	      return businesses;
	  }
	  
	  
	  /* GETTER AND SETTERS */
		public String getSnippetImgUrl() {
			return snippetImgUrl;
		}
		public void setSnippetImgUrl(String snippetImgUrl) {
			this.snippetImgUrl = snippetImgUrl;
		}
		public double getRating() {
			return rating;
		}
		public void setRating(double rating) {
			this.rating = rating;
		}
		public String getSnippetText() {
			return snippetText;
		}
		public void setSnippetText(String snippetText) {
			this.snippetText = snippetText;
		}
		public String getRatingImgUrl() {
			return ratingImgUrl;
		}
		public void setRatingImgUrl(String ratingImgUrl) {
			this.ratingImgUrl = ratingImgUrl;
		}
		public void setId(String id) {
			this.id = id;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getPhone() {
			return this.phone;
		}
		
		public String getImageUrl() {
			return this.imageUrl;
		}
	  
		
}