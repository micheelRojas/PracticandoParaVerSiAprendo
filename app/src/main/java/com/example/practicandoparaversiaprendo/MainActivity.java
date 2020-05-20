package com.example.practicandoparaversiaprendo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // El recycler
    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    // el adater del recycler
    private AdaterDatos adaterDatos;
    // la lista de datos del recycler
    private ArrayList<Musica> musicas = new ArrayList<Musica>();
    public  static ArrayList<Musica>lista = new ArrayList<>();
    // Para los mensajes
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    // el URL el original no tiene la s (https) si no (http) por alguna razon  da error asi (http)
    private static final String USGS_REQUEST_URL = "https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=b284db959637031077380e7e2c6f2775&format=json";
    // esto es como una cola y maneja lo de asyncronas
    private RequestQueue queue;
    ArrayList<MiMusica> Datos;

    ConexionSQLiteHelper conn;



    @Override
    protected void onRestart() {
        super.onRestart();
        ObtenerDatos();
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista= new ArrayList<>();
       conn=new ConexionSQLiteHelper(this,"db_musica",null,1);
        Datos= new ArrayList<>();
        // se instancia el array
            musicas= new ArrayList<>();

        // se instancia el queue el cual se le manda el contexto de esta clase
        queue = Volley.newRequestQueue(this);
        //se llama al metodo para la obtencion de datosregistrarMiMusica();
        registrarMiMusica();
        ObtenerDatos();



    }
    // obtencion de los datos de la api
    private void ObtenerDatos(){



        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, USGS_REQUEST_URL, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //code of return the api
                        try {
                            //le asigno el recycler del layaut
                            RecyclerView = (androidx.recyclerview.widget.RecyclerView) findViewById(R.id.RecyclerViewId);
                            RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                           // El objeto que se va a traer
                            JSONObject jsonObjectTracks = response.getJSONObject("tracks");
                            // lo que nos proporcona el api es un array de tracks por lo tanto
                            JSONArray jsonArrayTrack = jsonObjectTracks.getJSONArray("track");

                            //se recorre la lista con todos  y se va parceando
                            for(int i=0; i < jsonArrayTrack.length(); i++){

                                JSONObject JsonObjectMusic = jsonArrayTrack.getJSONObject(i);
                              JSONObject jsonObjectArtist = JsonObjectMusic.getJSONObject("artist");

                                String name = JsonObjectMusic.getString("name");
                                String duration = JsonObjectMusic.getString("duration");
                                String artist =jsonObjectArtist.getString("name");

                                // se van agg los tracks a la lista de musica
                                musicas.add(new Musica(name, duration,artist));


                            }
                            lista = new ArrayList<>();
                            lista = ConsultarBase();
                            for (Musica item : lista){
                                musicas.add(item);
                            }
                            // al adatador se le envia la lista
                            adaterDatos = new AdaterDatos(musicas);
                            //se envia el adatador al recyclerview
                            RecyclerView.setAdapter(adaterDatos);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //code in error
                Log.d(LOG_TAG, "Se produjo un error en el Volley ");
                error.printStackTrace();
            }
        });
        this.queue.add(request);
    }



    public ArrayList<Musica> Musicas(){
        ArrayList<Musica> musics = new ArrayList<>();
        for (int i =0; i<20; i++) {
            Musica musica = new Musica("lala", "nono","ll");
            musics.add(musica);
        }
        return musics;
    }
    public void Consultar(View view) {
        Intent intent = new Intent(MainActivity.this, Consultar.class);
        startActivity(intent);
    }

    private void registrarMiMusica() {
        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null){
            String name = bundle.getString("name");
            //valido que el nombre no este vacio por si acaso
            if (name!=null){
                name = bundle.getString("name");
                String duration= bundle.getString("duration");
                String artist = bundle.getString("artist");
                //toast para saber que que es lo que se envia
                Toast.makeText(getApplicationContext(), name+"  "+duration, Toast.LENGTH_LONG ).show();
                // se agg el la nueva a lista
                musicas.add(new Musica(name, duration,artist));
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"db_musica",null,1);
                SQLiteDatabase db=conn.getWritableDatabase();
                try {

                    ContentValues values=new ContentValues();
                    values.put(Utilidades.CAMPO_Name ,name);
                    values.put(Utilidades.CAMPO_Duration , duration);
                    values.put(Utilidades.CAMPO_Artista,artist);

                    long idResultante = db.insert(Utilidades.TABLA_Musica,Utilidades.CAMPO_Name,values);
                    if (idResultante != -1){
                        Toast.makeText(getApplicationContext(),"Id Registro:"+ idResultante,Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                    else  Toast.makeText(getApplicationContext(),"Error:"+ idResultante,Toast.LENGTH_SHORT).show();

                }
                finally {
                    db.close();
                    onBackPressed();
                }
            }

        }




    }
private  ArrayList<Musica> ConsultarBase() {
    //
    //
    String namem;
    String durationm;
    String artistm;

    SQLiteDatabase db = conn.getReadableDatabase();
    MiMusica musica = null;
    Musica apimusica = null;
    ArrayList<Musica> basemusica = new ArrayList<>();

    Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_Musica, null);
    while (cursor.moveToNext()) {

        musica = new MiMusica();
        musica.setName(cursor.getString(0));
        musica.setDuratio(cursor.getString(1));
        musica.setArtist(cursor.getString(2));
        namem = musica.getName().toString();
        durationm = musica.getDuratio().toString();
        artistm = musica.getArtist().toString();
        Datos.add(musica);
        if (Datos != null) {

            apimusica = new Musica(namem, durationm, artistm);
            basemusica.add(apimusica);

        }

        db.close();

        //
    }
    return  basemusica;
}

}
