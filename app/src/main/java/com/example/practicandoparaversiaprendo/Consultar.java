package com.example.practicandoparaversiaprendo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Consultar extends AppCompatActivity {
    private Button btnBuscar;
    private EditText txtBuscar;
    private RequestQueue queue;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    //txt
    private TextView idCancion;
    private TextView idArtista;

    private TextView idDuracion;
    private TextView informacion;
    private TextView idAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);
        //para retroceder buscar la forma en que se hace desde el manifest
       //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //intancio el queue con el voley
        queue = Volley.newRequestQueue(this);
        // se refencia lo del layaut

        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        idCancion = (TextView) findViewById(R.id.idCancion);
        idArtista = (TextView) findViewById(R.id.idArtista);
        idAlbum = (TextView) findViewById(R.id.idAlbum);
        idDuracion = (TextView) findViewById(R.id.idDuracion);
        informacion = (TextView) findViewById(R.id.informacion);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);


    }
    public void addMusic(){
        //con el metodo addMusic() paso el nombre de la cancion al MainActivity, para eso valido que no se pasen datos nulos

        if(idCancion.getText() != ""){

            String cancion = idCancion.getText().toString();
            String duracion = idDuracion.getText().toString();
            String  artista = idArtista.getText().toString();
            Intent intent = new Intent(Consultar.this, MainActivity.class);
            Bundle  bundle = new Bundle();
            bundle.putString("name", cancion);
            bundle.putString("duration",duracion);
            bundle.putString("artist",artista);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_LONG).show();
        }
    }


   public void InsertaralRecycler(View view) {
     addMusic();

     onBackPressed();
    }
   public void Buscar(){
       //caturo la primera coinsidencia con la cancion  que se busca

       String Cancion = txtBuscar.getText().toString();


       String Url= "https://api.deezer.com/search?q="+Cancion;
       JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, Url, (String) null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                 //aqui se hace el parceo
                       try {


                           JSONArray jsonArrayTrack = response.getJSONArray("data");



                           JSONObject jsonObjectMusic = jsonArrayTrack.optJSONObject(0);
                           JSONObject jsonObjectArtist = jsonObjectMusic.getJSONObject("artist");
                           JSONObject jsonObjectAlbum = jsonObjectMusic.getJSONObject("album");
                           informacion.setText("Informacion:   ");
                           idCancion.setText("Nombre:   "+jsonObjectMusic.getString("title"));
                           idArtista.setText("Artist:   "+jsonObjectArtist.getString("name"));
                           idAlbum.setText("Album:   "+jsonObjectAlbum.getString("title"));
                           idDuracion.setText("Duracion:   "+jsonObjectMusic.getString("duration"));

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               //code in error
               Log.d(LOG_TAG, "error en el voley de consultar");
               error.printStackTrace();
           }
       });
       this.queue.add(request);
   }
    public void Buscar(View view) {
      Buscar();

    }
}
