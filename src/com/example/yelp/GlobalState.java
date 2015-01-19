package com.example.yelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import yelpApi.YelpAPI;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.ParseException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GlobalState extends Application {
	public static String version = "2.1";
	public DefaultHttpClient client;

	@Override
	public void onCreate() {
		super.onCreate();
		client = new DefaultHttpClient();
	}

	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		GlobalState.version = version;
	}

	public void alerter(String s) {
		// Fonction qui renvoie un toast avec un string défini par l'user
		Toast t = Toast.makeText(this, s, Toast.LENGTH_LONG);
		t.show();
	}

	private String convertStreamToString(InputStream in) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean verifReseau() {
		// On vérifie si le réseau est disponible,
		// si oui on change le statut du bouton de connexion
		ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

		String sType = "Aucun réseau détecté";
		Boolean bStatut = false;
		if (netInfo != null) {

			State netState = netInfo.getState();

			if (netState.compareTo(State.CONNECTED) == 0) {
				bStatut = true;
				int netType = netInfo.getType();
				switch (netType) {
				case ConnectivityManager.TYPE_MOBILE:
					sType = "Réseau mobile détecté";
					break;
				case ConnectivityManager.TYPE_WIFI:
					sType = "Réseau wifi détecté";
					break;
				}

			}
		}

		// a completer : doit renvoyer un booleen fonction de l'état du réseau
		this.alerter(sType);
		return bStatut;
	}

	public boolean verifSearchBar(EditText edtSearch) {
		alerter(edtSearch.getText().toString());

		return false;

	}

	public String requete(String qs) {
		if (qs != null) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String urlData = prefs.getString("urlData",
					"http://chaire-ecommerce.ec-lille.fr/ime5/data.php");
			HttpGet req = new HttpGet(urlData + "?" + qs);
			HttpResponse reponse;
			HttpEntity corpsReponse;
			InputStream is;
			try {
				reponse = client.execute(req); // exécuter requête
				corpsReponse = reponse.getEntity(); // récupère le résultat
				is = corpsReponse.getContent();
				String txtReponse = convertStreamToString(is);

				return txtReponse;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";
	}

	public void enableSubmitIfReady(EditText t, Button b) {
		b.setEnabled(true);
	}

	public static ArrayList<Business> Parsing(String searchResponseJSON) {
		// On parse le string récupéré par notre requête à l'API en JSON
		JSONObject response = null;
		try {
			// On parse le string en JSON pour pouvoir le traiter
			response = new JSONObject(searchResponseJSON);
			JSONArray rp = (JSONArray) response.get("businesses");
			// On envoie notre tableau JSON pour en faire une liste d'objet
			// Business
			ArrayList<Business> businesses = Business.fromJson(rp);
			// On renvoie notre List<Business>
			return businesses;
		} catch (ParseException pe) {
			System.out.println("Erreur: impossible de parser le jSON");
			System.out.println(searchResponseJSON);
			System.exit(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
