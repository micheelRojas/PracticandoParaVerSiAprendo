package com.example.practicandoparaversiaprendo;

public class Utilidades {
    //constantes de la tabla
    public static String TABLA_Musica = "mimusica";
    public static String CAMPO_Name = "name";
    public static String CAMPO_Duration = "duration";
    public static String CAMPO_Artista = "artist";


    public static final  String CREAR_TABLA_MUSICA="CREATE TABLE" +
            " " + TABLA_Musica + " (" +CAMPO_Name+" "+
            "TEXT, "+ CAMPO_Duration+" " +" TEXT," +CAMPO_Artista+" "+ "TEXT)";
}
