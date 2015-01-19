package com.example.yelp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Renderer extends Activity {
	ListView lv;
	ArrayList<Business> businesses;
	final Context context = this;

	public Renderer() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		try {
			// On instancie notre bundle object (de MainActivity)
			Bundle bundleObject = getIntent().getExtras();
			// Récuperer notre variable (arrayList<Business)
			businesses = (ArrayList<Business>) bundleObject
					.getSerializable("key");
			// On attribue notre variable à l'element du fichier xml
			// correspondant
			lv = (ListView) findViewById(R.id.listView1);
			// On va remplir notre liste avec les résultats
			lv.setAdapter(new RenderItems(this, businesses));
			// On va définir le click sur un element de la liste
			lv.setOnItemClickListener(new OnItemClickListener() {
				// Si on clique sur un element
				public void onItemClick(AdapterView<?> a, View v,
						final int position, long id) {
					// on va lancer une popup
					new AlertDialog.Builder(context)
							.setTitle("Action")
							.setMessage("Que voulez-vous faire?")
							.setPositiveButton("Appeler",
									new DialogInterface.OnClickListener() {
										// Si l'user choisit d'appeler
										public void onClick(
												DialogInterface dialog,
												int which) {
											// On recupère le numéro et on
											// appelle
											Intent callIntent = new Intent(
													Intent.ACTION_CALL);
											callIntent.setData(Uri.parse("tel:"
													+ businesses.get(position)
															.getPhone()));
											startActivity(callIntent);
										}
									})
							// Si l'utilisateur choisit l'itinéraire
							.setNegativeButton("Itinéraire",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// do nothing
										}
									})
							.setIcon(android.R.drawable.ic_dialog_info).show();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

}
