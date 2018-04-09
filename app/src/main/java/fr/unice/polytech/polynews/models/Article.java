package fr.unice.polytech.polynews.models;

/**
 * Created by Alex on 25/03/2018.
 */

public class Article {

    private int id;
    private String titre;
    private String texte;
    private String auteur;
    private String date;
    private String categorie;
    private String type;
    private String url;

    public Article(int id, String titre, String texte, String auteur, String date, int categorie, int type, String url) {
        this.id = id;
        this.titre = titre;
        this.texte = texte;
        this.auteur = auteur;
        this.date = date;
        setCategorie(categorie);
        setType(type);
        this.url = url;
    }

    private void setCategorie(int cat){
        if(cat == 1)
            this.categorie = "politique";
        else if (cat == 2)
            this.categorie = "société";
        else
            this.categorie = "autre";
    }

    private void setType(int ty){
        if(ty == 0)
            this.type = "photo";
        else if (ty == 1)
            this.type = "vidéo";
        else
            this.type = "autre";
    }

    @Override
    public String toString() {
        return titre;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getTexte() {
        return texte;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getDate() {
        return date;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
