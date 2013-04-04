/**
 * 
 */
package fr.upyourbizz.aspiration.magento;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;

import fr.upyourbizz.aspiration.composants.Article;
import fr.upyourbizz.aspiration.composants.Category;
import fr.upyourbizz.aspiration.composants.Contexte;
import fr.upyourbizz.aspiration.composants.Navigation;
import fr.upyourbizz.aspiration.composants.ResultatParsingSousCategorie;
import fr.upyourbizz.utils.Utils;
import fr.upyourbizz.utils.filescan.FileUtil;

/**
 * @author Mikael THIBAULT
 */
public class Parser {

    private Contexte contexte;

    private Navigation navigation;

    private Utils utils;

    public List<String> extractProductUrlFromInternet() {
        String urlAccueilSite = contexte.getHomePageUrl();
        boolean enregistrerFichier = contexte.isSaveFile();

        // La première étape consite à extraire les liens de la barre de
        // navigation de l'écran d'accueil
        Navigation barreNavigation = parsingAccueil(urlAccueilSite, enregistrerFichier);

        System.out.println(barreNavigation.toString());

        List<String> urlsSousCategorie = barreNavigation.extraireUrlSousCategorie();
        int compteurSousCategorieParcourues = 0;
        List<String> urlsArticlesTrouves = new ArrayList<String>();

        // while (!urlsSousCategorie.isEmpty()) {
        // List<String> urlSousCategoriesSup = new ArrayList<String>();
        // for (String urlSousCategorie : urlSousCategoriesSup) {
        // if (parcoursAutorise(compteurSousCategorieParcourues)) {
        // ResultatParsingSousCategorie resultatParsingSousCategorie =
        // parsingSousCategorie(
        // urlSousCategorie, enregistrerFichier);
        // urlsArticlesTrouves.addAll(resultatParsingSousCategorie.getListeUrlArticlesTrouves());
        // urlSousCategoriesSup.addAll(resultatParsingSousCategorie.getListeAutresSousCategories());
        // }
        // else {
        // break;
        // }
        // }
        // urlsSousCategorie = urlSousCategoriesSup;
        // }
        for (String urlArticlesTrouves : urlsArticlesTrouves) {
            System.out.println(urlArticlesTrouves);
        }
        return null;
    }

    public List<String> extractProductUrlFromDisk(String webSiteFolderName) {
        // La première étape consite à extraire les liens de la barre de
        // navigation de l'écran d'accueil
        parseHomePageFromDisk(webSiteFolderName);
        extractSubCategoriesImg();
        navigation.createCategoriesCsvFile();
        navigation.extraireLienSousCategorie();
        Map<Category, List<Category>> mapTmp = new HashMap<Category, List<Category>>();
        mapTmp.putAll(navigation.getMapNavigation());

        while (!mapTmp.isEmpty()) {
            Map<Category, List<Category>> mapNewSubCategory = new HashMap<Category, List<Category>>();
            for (Entry<Category, List<Category>> category : mapTmp.entrySet()) {
                // Category
                for (Category subCategory : category.getValue()) {
                    ResultatParsingSousCategorie resultatParsingSousCategorie = parseSubCategoryFromDisk(
                            subCategory, true);
                    subCategory.setUrlRelatedProducts(resultatParsingSousCategorie.getListeUrlArticlesTrouves());

                    if (!resultatParsingSousCategorie.getMapAutresSousCategories().isEmpty()) {
                        for (Entry<Category, List<Category>> newCategory : resultatParsingSousCategorie.getMapAutresSousCategories().entrySet()) {
                            if (!navigation.getMapNavigation().containsKey(newCategory)) {
                                navigation.getMapNavigation().put(newCategory.getKey(),
                                        newCategory.getValue());
                            }
                            else {
                                navigation.getMapNavigation().get(newCategory).addAll(
                                        newCategory.getValue());
                            }
                        }
                        mapNewSubCategory.putAll(resultatParsingSousCategorie.getMapAutresSousCategories());

                    }
                }
            }
            mapTmp = mapNewSubCategory;
        }

        for (Entry<Category, List<Category>> category : navigation.getMapNavigation().entrySet()) {
            System.out.println("Categorie = " + category.getKey().getName());
            for (Category subCategory : category.getValue()) {
                List<String> listeUrl = subCategory.getUrlRelatedProducts();
                int compteur = 10;
                if (listeUrl.size() < 10) {
                    compteur = listeUrl.size();
                }
                for (int i = 0; i < compteur; i++) {
                    Document documentHTML;
                    try {
                        documentHTML = utils.readFileOrUrl(listeUrl.get(i));

                        Article article = ParserArticle.parserInformationArticle(documentHTML);
                        System.out.println(article.extractCsvInfos());
                    }
                    catch (IOException e) {
                        if (contexte.isDownloadFileIfMissing()) {
                            downloadFile(e);
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                }
            }
            break;
        }
        // System.out.println(barreNavigation.toString());
        // List<String> urlsSousCategorie = barreNavigation
        // .extraireLienSousCategorie();
        // int compteurSousCategorieParcourues = 0;
        // List<String> urlsArticlesTrouves = new ArrayList<String>();
        //
        // while (!urlsSousCategorie.isEmpty()) {
        // List<String> urlSousCategoriesSup = new ArrayList<String>();
        // for (String urlSousCategorie : urlsSousCategorie) {
        // if (parcoursAutorise(compteurSousCategorieParcourues)) {
        // ResultatParsingSousCategorie resultatParsingSousCategorie =
        // parseSubCategoryFromDisk(
        // urlSousCategorie, true);
        // urlsArticlesTrouves.addAll(resultatParsingSousCategorie
        // .getListeUrlArticlesTrouves());
        // urlSousCategoriesSup.addAll(resultatParsingSousCategorie
        // .getListeAutresSousCategories());
        // } else {
        // break;
        // }
        // }
        // urlsSousCategorie = new ArrayList<String>();
        // for (String urlSousCAtegorieSup : urlSousCategoriesSup) {
        // urlsSousCategorie
        // .add(FileUtil.convertUrlToRelativePath(urlSousCAtegorieSup));
        // }
        // }
        // for (String urlArticlesTrouves : urlsArticlesTrouves) {
        // System.out.println(urlArticlesTrouves);
        // }
        return null;
    }

    private void extractSubCategoriesImg() {
        // TODO Auto-generated method stub

    }

    private boolean parcoursAutorise(int compteurSousCategorieParcourues) {
        return (contexte.getNbSousCategorieAParcourir() < 0 || compteurSousCategorieParcourues < contexte.getNbSousCategorieAParcourir());
    }

    private Navigation parsingAccueil(String urlAccueilSite, boolean enregistrerFichier) {
        Navigation navigation = new Navigation();
        try {
            navigation = ParseHomePage.parseHomePageFromInternet(urlAccueilSite, enregistrerFichier);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return navigation;
    }

    private void parseHomePageFromDisk(String urlAccueilSite) {

        try {
            // TODO ne remonter qu'un mapper
            Navigation navigationTmp = new Navigation();
            navigationTmp = ParseHomePage.parseHomePageFromDisk(urlAccueilSite);
            navigation.setMapNavigation(navigationTmp.getMapNavigation());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private ResultatParsingSousCategorie parsingSousCategorie(String
    // urlSousCategorie,
    // boolean enregistrerFichier) {
    // ResultatParsingSousCategorie resultatParsingSousCategorie = new
    // ResultatParsingSousCategorie();
    // try {
    // resultatParsingSousCategorie = ParserSousCategorie.parserSousCategorie(
    // urlSousCategorie, true, enregistrerFichier);
    // List<String> urlsSousPage =
    // resultatParsingSousCategorie.getListeAutrePagesAParserMemeCategorie();
    // if (!urlsSousPage.isEmpty()) {
    // for (String urlSousPage : urlsSousPage) {
    // ResultatParsingSousCategorie
    // resultatParsingSousCategoriePageSupplementaire =
    // ParserSousCategorie.parserSousCategorie(
    // urlSousPage, false, true);
    // resultatParsingSousCategorie.getListeUrlArticlesTrouves().addAll(
    // resultatParsingSousCategoriePageSupplementaire.getListeUrlArticlesTrouves());
    // }
    // }
    //
    // }
    // catch (IOException e) {
    // e.printStackTrace();
    // }
    // return resultatParsingSousCategorie;
    // }

    private ResultatParsingSousCategorie parseSubCategoryFromDisk(Category subCategory,
            boolean downloadFileIfMissing) {
        ResultatParsingSousCategorie resultatParsingSousCategorie = new ResultatParsingSousCategorie();
        try {
            Document documentHTML = utils.readFileOrUrl(subCategory.getUrl());
            resultatParsingSousCategorie = ParserSousCategorie.parserSousCategorie(subCategory,
                    documentHTML, true);
            List<String> urlsSousPage = resultatParsingSousCategorie.getListeAutrePagesAParserMemeCategorie();
            if (!urlsSousPage.isEmpty()) {
                for (String urlSousPage : urlsSousPage) {
                    documentHTML = utils.readFileOrUrl(urlSousPage);
                    ResultatParsingSousCategorie resultatParsingSousCategoriePageSupplementaire = ParserSousCategorie.parserSousCategorie(
                            null, documentHTML, false);
                    resultatParsingSousCategorie.getListeUrlArticlesTrouves().addAll(
                            resultatParsingSousCategoriePageSupplementaire.getListeUrlArticlesTrouves());
                }
            }

        }
        catch (IOException e) {
            if (contexte.isDownloadFileIfMissing()) {
                downloadFile(e);
            }
            else {
                e.printStackTrace();
            }
        }
        return resultatParsingSousCategorie;
    }

    private void downloadFile(IOException e) {
        String message = e.getMessage();
        String[] messageSplit = message.split(" ");
        String fileNotFound = messageSplit[0];
        System.err.println("Fichier " + messageSplit[0] + " non trouvé");
        try {
            System.out.println("Re-téléchargement du fichier " + fileNotFound);
            FileUtil.saveFileToDisk("http://" + fileNotFound);
        }
        catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // ==== Accesseurs =================================

    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

}
