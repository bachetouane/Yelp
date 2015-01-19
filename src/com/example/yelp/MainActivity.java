package com.example.yelp;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONArray;

import yelpApi.YelpAPI;

import com.example.yelp.GlobalState;
import com.example.yelp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	EditText edtCategory;
	EditText edtLocation;
	EditText edtSearch;
	Button btnOk;
	GlobalState gs;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// On définit le design de notre page grace à la page xml créee
		setContentView(R.layout.activity_main);
		// On attribue nos objet aux élements de la page xml
		edtCategory = (EditText) findViewById(R.id.edtCategory);
		edtLocation = (EditText) findViewById(R.id.edtLocation);
		btnOk = (Button) findViewById(R.id.btnOk);
		// On autorise le clique sur le button
		btnOk.setOnClickListener(this);
		btnOk.setEnabled(true);
		gs = (GlobalState) getApplication();
	}

	@Override
	// Lors du click sur le bouton
	public void onClick(View v) {
		// On va créer notre requête
		YelpApiRequest req = new YelpApiRequest();
		// On définit les paramètres de la requête (lieu et categorie)
		req.setCategory(edtCategory.getText().toString());
		req.setLocation(edtLocation.getText().toString());
		// On execute la requête
		req.execute();
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	// Classe asychrone pour pouvoir réaliser une requete sur l'api en tâche de
	// fond
	@SuppressWarnings("unused")
	public class YelpApiRequest extends AsyncTask<Object, Object, Object> {
		private String category;
		private String location;
		
		@Override
		protected String doInBackground(Object... params) {
			// Créer la connexion
			YelpAPI conn = new YelpAPI();
			// Lancer la requête avec nos paramètres
			// On récupère la réponse sous forme de string
			String response = conn.searchForBusinessesByLocation(
					this.getCategory(), this.getLocation());
			//On envoie convertir le string en json pour pouvoir être traité
			ArrayList<Business> businesses = GlobalState.Parsing(response);
			// On crée un intent pour pouvoir changer d'activité
			Intent passIntent = new Intent();
			passIntent.setClass(MainActivity.this, Renderer.class);
			// On crée ensuite un Bundel pour pouvoir passer nos variables d'une vue à l'autre
			Bundle bundleObject = new Bundle();
			//On définit key comme valeur pour pouvoir récupérer businesses 
			//lors d'une autre activité
			bundleObject.putSerializable("key", businesses);
			// On passe notre parametre
			passIntent.putExtras(bundleObject);
			// On start la nouvelle activité
			startActivity(passIntent);
			return "";
		}

		/* GETTER AND SETTERS */
		public String getCategory() {
			return category;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public void setCategory(String category) {
			this.category = category;
		}

	}

}
