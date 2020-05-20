package com.example.practicandoparaversiaprendo;

import java.io.Serializable;

public  class  Musica implements Serializable {
    String name;
    String duration;
    String artist;

    public String getArtist() {
        return artist;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Musica(String name, String duration, String artist) {
        this.name = name;
        this.duration = duration;
        this.artist=artist;
    }


}
