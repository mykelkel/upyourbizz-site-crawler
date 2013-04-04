/**
 * 
 */
package fr.upyourbizz.core;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

import fr.upyourbizz.utils.constantes.Consts;

/**
 * @author Mikaël THIBAULT
 */
public class Article {

    private String categorie;

    private String nom;

    private String prix;

    private String description;

    private String marque;

    private String urlImgMarqueProduit;

    private List<String> listeUrlImages;

    private List<String> listeUrlImagesCarousel;

    public Article(String categorie, String nom, String prix, String description, String marque,
            String urlImgMarqueProduit, List<String> listeUrlImages,
            List<String> listeUrlImagesCarousel) {
        super();
        this.categorie = categorie;
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.marque = marque;
        this.urlImgMarqueProduit = urlImgMarqueProduit;
        this.listeUrlImages = listeUrlImages;
        this.listeUrlImagesCarousel = listeUrlImagesCarousel;
    }

    public String genererClef() {
        return categorie + Consts.SEPARATOR + nom;
    }

    public String extractCsvInfos() {
        StringBuilder result = new StringBuilder();
        result.append(categorie + Consts.CSV_ITEM_SEPARATOR + nom + Consts.CSV_ITEM_SEPARATOR
                + prix + Consts.CSV_ITEM_SEPARATOR + "\"" + description + "\""
                + Consts.CSV_ITEM_SEPARATOR + marque + Consts.CSV_ITEM_SEPARATOR
                + urlImgMarqueProduit);
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Article))
            return false;

        Article other = (Article) obj;

        return Objects.equal(categorie, other.categorie) && Objects.equal(nom, other.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categorie, nom, prix, description);
    }

    @Override
    public String toString() {
        ToStringHelper stringHelper = Objects.toStringHelper(this).add("Catégorie", categorie).add(
                "Nom article", nom).add("Prix", prix).add("Marque", marque).add("Img marque",
                urlImgMarqueProduit.length() > 0).add("Description", description.length()).add(
                "Nombre grandes images", listeUrlImages.size()).add("Nombre images carousel",
                listeUrlImagesCarousel.size());
        // for (String urlImage : listeUrlImages) {
        // stringHelper.add("Image", urlImage);
        // }
        return stringHelper.toString();
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getUrlImgMarqueProduit() {
        return urlImgMarqueProduit;
    }

    public void setUrlImgMarqueProduit(String urlImgMarqueProduit) {
        this.urlImgMarqueProduit = urlImgMarqueProduit;
    }

    public List<String> getListeUrlImages() {
        return listeUrlImages;
    }

    public void setListeUrlImages(List<String> listeUrlImages) {
        this.listeUrlImages = listeUrlImages;
    }

    public List<String> getListeUrlImagesCarousel() {
        return listeUrlImagesCarousel;
    }

    public void setListeUrlImagesCarousel(List<String> listeUrlImagesCarousel) {
        this.listeUrlImagesCarousel = listeUrlImagesCarousel;
    }
}
