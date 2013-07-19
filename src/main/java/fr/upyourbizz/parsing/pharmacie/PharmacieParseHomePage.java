/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.parsing.pharmacie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.upyourbizz.core.Category;
import fr.upyourbizz.core.Navigation;
import fr.upyourbizz.utils.Reader;

/**
 * @author Mikaël THIBAULT
 */
public class PharmacieParseHomePage {

    // ===== Attributs statiques ==============================================

    private static Logger logger = LoggerFactory.getLogger(PharmacieParseHomePage.class);

    // ===== Méthodes statiques ===============================================

    public static Navigation parserLiensAccueil(File fichierAccueil) throws IOException {
        Document accueil = Jsoup.parse(fichierAccueil, "UTF-8");
        return parserLiensAccueil(accueil);
    }

    /**
     * Parse home page, seeking navigation informations
     * 
     * @param urlHomePage
     * @param saveFilesOnDisk
     * @return
     * @throws IOException
     */
    public static Navigation parseHomePageFromInternet(String urlHomePage, boolean saveFilesOnDisk)
            throws IOException {
        if (saveFilesOnDisk) {
            URL url = new URL(urlHomePage);
            File destination = new File(Reader.convertUrlToRelativePath(urlHomePage)
                    + "/accueil.html");

            FileUtils.copyURLToFile(url, destination);
            return parserLiensAccueil(destination);
        }
        else {
            Document accueil = Jsoup.connect(urlHomePage).get();
            return parserLiensAccueil(accueil);
        }
    }

    /**
     * Parse home page, seeking navigation informations
     * 
     * @param homePageFile
     * @return
     * @throws IOException
     */
    public static Navigation parseHomePageFromDisk(File homePageFile) throws IOException {
        Document homePage = Jsoup.parse(homePageFile, "UTF-8");
        return parserLiensAccueil(homePage);
    }

    private static Navigation parserLiensAccueil(Document accueil) {
        Navigation navigation = new Navigation();
        List<Category> categoriesPrincipale = new ArrayList<Category>();
        Elements nomCategoriesPrincipales = accueil.select("span.cm_l1");
        for (Element categoriePrincipale : nomCategoriesPrincipales) {
            String categoriePrincipaleName = categoriePrincipale.text();
            String categoriePrincipaleUrl = categoriePrincipale.attributes().get("href").toLowerCase();
            Category parentCategory = navigation.ajouterNouvelleCategorie(categoriePrincipaleName,
                    categoriePrincipaleUrl);
            categoriesPrincipale.add(parentCategory);
        }

        for (int i = 0; i < nomCategoriesPrincipales.size(); i++) {
            String idCategorie = "bcm" + String.valueOf(i);
            Element categorie = accueil.getElementById(idCategorie);
            Elements navigationTopItemLink = categorie.select("a.cl_l2");
            for (Element navigationTopItem : navigationTopItemLink) {
                String navigationTopItemUrl = navigationTopItem.attributes().get("href").toLowerCase();
                String navigationTopItemName = navigationTopItem.text();
                navigation.ajouterNouvelleSousCategorie(categoriesPrincipale.get(i),
                        navigationTopItemName, navigationTopItemUrl);
            }
        }

        return navigation;
    }

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    // ===== Accesseurs =======================================================

    // ===== Classes imbriques ===============================================

}
