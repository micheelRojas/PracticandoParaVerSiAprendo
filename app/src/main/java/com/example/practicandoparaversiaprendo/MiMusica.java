package com.example.practicandoparaversiaprendo;

import java.io.Serializable;
import java.nio.file.SecureDirectoryStream;

public class MiMusica implements Serializable {
    String name;
    String duratio;
    String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public MiMusica() {

    }

    public MiMusica(String name, String duratio, String artist) {
        this.name = name;
        this.duratio = duratio;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuratio() {
        return duratio;
    }

    public void setDuratio(String duratio) {
        this.duratio = duratio;
    }
}
