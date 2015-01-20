package com.example.yelp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RenderItems extends BaseAdapter {

	ArrayList<Business> listBusiness;
	LayoutInflater layoutInflater;
	ViewHolder holder;

	public RenderItems(Context context, ArrayList<Business> listBusiness) {
		this.listBusiness = listBusiness;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// Fonction qui retourne le nombre d'elements de notre
		return listBusiness.size();
	}

	@Override
	public Object getItem(int position) {
		// Fonction qui return l'item
		return listBusiness.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Fonction qui retourne l'id d'un item
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Fonction qui va définir les élements de la liste à setter
		if (convertView == null) {
			// On appelle notre feuille xml
			convertView = layoutInflater.inflate(R.layout.items, null);
			holder = new ViewHolder();
			// On attribue nos variables à un element de notre fichier xml
			holder.logo = (ImageView) convertView.findViewById(R.id.logo);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.rating = (TextView) convertView.findViewById(R.id.rating);
			holder.snippet = (TextView) convertView.findViewById(R.id.snippet);
			//holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.ratingImg = (ImageView) convertView
					.findViewById(R.id.rating_img);
			holder.snippetImg = (ImageView) convertView
					.findViewById(R.id.snippet_img);
			// tagguer notre objet pour pouvoir le récupérer à la prochaine mise
			// à jour graphique.
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// mettre les données dans chaque composante associée
		holder.name.setText(listBusiness.get(position).getName());
		holder.snippet.setText(listBusiness.get(position).getSnippetText());
		// On convertit notre double en string
		NumberFormat nm = NumberFormat.getNumberInstance();
		holder.rating
				.setText(nm.format(listBusiness.get(position).getRating()));
		// holder.phone.setText(listBusiness.get(position).getPhone());

		// On va récupérer nos images à partir de l'URL
		// On récupère dans un premier temps nos URL
		getBitmapFromUrl req = new getBitmapFromUrl(listBusiness.get(position)
				.getImageUrl());
		getBitmapFromUrl req1 = new getBitmapFromUrl(listBusiness.get(position)
				.getRatingImgUrl());
		getBitmapFromUrl req2 = new getBitmapFromUrl(listBusiness.get(position)
				.getSnippetImgUrl());
		Bitmap logo;
		Bitmap ratingImg;
		Bitmap snippetImg;
		try {
			// On lance la requete, on récupère donc nos images
			logo = req.execute().get();
			ratingImg = req1.execute().get();
			snippetImg = req2.execute().get();
			// On attribue nos images aux elements de la liste
			holder.logo.setImageBitmap(logo);
			holder.ratingImg.setImageBitmap(ratingImg);
			holder.snippetImg.setImageBitmap(snippetImg);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;

	}

	// Cette classe nous permet de mémoriser la liste
	static class ViewHolder {
		TextView snippet;
		ImageView snippetImg;
		ImageView ratingImg;
		ImageView logo;
		TextView name;
		TextView phone;
		TextView rating;

	}

	class getBitmapFromUrl extends AsyncTask<String, Bitmap, Bitmap> {
		//Tache asynchrone permettant de parser et de récuperer les images a partir d'un string  
		//qui fait office d'URL
		String src;
		Bitmap img;
		
		public getBitmapFromUrl(String src) {
			//On rempli notre objet
			this.src = src;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				//On parse notre string en URL
				URL url = new URL(src);
				//On ouvre une connexion
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				//On recupere ce que l'URL contient
				InputStream input = connection.getInputStream();
				//On parse l'objet récupéré en Bitmap
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				//On remplit notre objet pour pouvoir l'utiliser à tout endroit
				this.img = myBitmap;
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
