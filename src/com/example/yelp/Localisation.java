package com.example.yelp;

import java.io.Serializable;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yelp.ItineraireTask;
import com.example.yelp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Localisation extends Activity implements LocationListener, Serializable {

	  private LocationManager locationManager;
	  private GoogleMap gMap;
	  private Marker marker;
	  private String whereToGo;
	  boolean flag=true;


	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  protected void onCreate(final Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      Business myBusiness = (Business) getIntent().getSerializableExtra("whereToGo");
	      whereToGo = myBusiness.getAddress() + " " + myBusiness.getCity();
	      setContentView(R.layout.fragment);

	      gMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
	      marker = gMap.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));
	  }

	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  public void onResume() {
	      super.onResume();

	      //Obtention de la référence du service
	      locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

	      //Si le GPS est disponible, on s'y abonne
	      if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	          abonnementGPS();
	      }
	  }

	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  public void onPause() {
	      super.onPause();

	      desabonnementGPS();
	  }

	  /**
	  * Méthode permettant de s'abonner à la localisation par GPS.
	  */
	  public void abonnementGPS() {
	      //On s'abonne
	      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
	  }

	  /**
	  * Méthode permettant de se désabonner de la localisation par GPS.
	  */
	  public void desabonnementGPS() {

	      locationManager.removeUpdates(this);
	  }

	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  public void onLocationChanged(final Location location) {
	      //On affiche dans un Toat la nouvelle Localisation
		  //if (flag){
	      final StringBuilder msg = new StringBuilder("lat : ");
	      msg.append(location.getLatitude());
	      msg.append( "; lng : ");
	      msg.append(location.getLongitude());

	      Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();

	      //Mise à jour des coordonnées
	      final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());      
	      gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
	      marker.setPosition(latLng);	
	      
	      String myLoc = location.getLatitude()+ "," + location.getLongitude();
	      
	      new ItineraireTask(this, gMap, myLoc, whereToGo).execute();
		 // }
	  }

	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  public void onProviderDisabled(final String provider) {
	      //Si le GPS est désactivé on se désabonne
	      if("gps".equals(provider)) {
	          desabonnementGPS();
	      }        
	  }

	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  public void onProviderEnabled(final String provider) {
	      //Si le GPS est activé on s'abonne
	      if("gps".equals(provider)) {
	          abonnementGPS();
	      }
	  }

	  /**
	  * {@inheritDoc}
	  */
	  @Override
	  public void onStatusChanged(final String provider, final int status, final Bundle extras) { }
	}
