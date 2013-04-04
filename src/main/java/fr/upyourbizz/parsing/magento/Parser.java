/**
 * 
 */
package fr.upyourbizz.parsing.magento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;

import fr.upyourbizz.core.Article;
import fr.upyourbizz.core.Category;
import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.core.Navigation;
import fr.upyourbizz.export.csv.ExportProducts;
import fr.upyourbizz.utils.Utils;

/**
 * @author Mikael THIBAULT
 */
public class Parser {

    private Contexte contexte;

    private Navigation navigation;

    private Utils utils;

    /**
     * @return
     */
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

        List<Article> listeArticle = new ArrayList<Article>();
        for (Entry<Category, List<Category>> category : navigation.getMapNavigation().entrySet()) {
            System.out.println("Categorie = " + category.getKey().getName());
            for (Category subCategory : category.getValue()) {
                List<String> listeUrl = subCategory.getUrlRelatedProducts();
                for (String url : listeUrl) {
                    Document documentHTML;
                    try {
                        documentHTML = utils.readFileOrUrl(url);
                        if (documentHTML != null) {
                            Article article = ParserArticle.parserInformationArticle(documentHTML);
                            article.setCategorie(subCategory.calculateProductCategoriesCsv());
                            listeArticle.add(article);
                            System.out.println(article.extractCsvInfos());
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            break;
        }

        try {
            ExportProducts.createProductsCsvFile(listeArticle);
        }
        catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return resultatParsingSousCategorie;
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
