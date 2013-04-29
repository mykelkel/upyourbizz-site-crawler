/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.parsing.magento;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.upyourbizz.core.Article;
import fr.upyourbizz.core.Category;
import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.core.Navigation;
import fr.upyourbizz.export.csv.ExportCategories;
import fr.upyourbizz.export.csv.ExportProducts;
import fr.upyourbizz.utils.FileDownloader;
import fr.upyourbizz.utils.Reader;

/**
 * @author Mikaël THIBAULT
 */
public class Parser {

    // ===== Attributs statiques ==============================================

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

    // ===== Méthodes statiques ===============================================

    // ===== Attributs ========================================================

    private Contexte contexte;

    private Navigation navigation;

    private Reader reader;

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    /**
     * Parse the website whose url is provided in the property file
     * 
     * @throws MalformedURLException
     * @throws IOException
     */
    public void parseWebSite() throws MalformedURLException, IOException {
        logger.debug("parseWebSite");
        // La première étape consite à extraire les liens de la barre de
        // navigation de l'écran d'accueil
        File homePage = saveHomePageOnDisk(contexte.getWebSiteUrl(),
                contexte.getWebSiteParseResultFolder());
        parseHomePageFromDisk(homePage);
        extractSubCategoriesImg();
        // navigation.extraireLienSousCategorie();
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

        Map<String, Article> listeArticle = new HashMap<String, Article>();
        for (Entry<Category, List<Category>> category : navigation.getMapNavigation().entrySet()) {
            // System.out.println("Categorie = " + category.getKey().getName());
            for (Category subCategory : category.getValue()) {
                List<String> listeUrl = subCategory.getUrlRelatedProducts();
                for (String url : listeUrl) {
                    Document documentHTML;
                    try {
                        documentHTML = reader.readFileOrUrl(url);
                        if (documentHTML != null) {
                            Article article = ParserArticle.parserInformationArticle(documentHTML);
                            article.setCategorie(subCategory.getName());
                            String mapKey = article.getCategorie() + article.getNom();
                            if (!listeArticle.containsKey(mapKey)) {
                                listeArticle.put(mapKey, article);
                            }
                            else {
                                logger.warn("Produit: " + article.getCategorie() + ","
                                        + article.getNom() + " déjà présent");
                            }

                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<Article> listeArticleAAjouter = retirerArticleDejaPresents(new ArrayList<Article>(
                listeArticle.values()));
        try {
            ExportCategories.createCategoriesCsvFile(navigation.getMapNavigation());
            ExportProducts.createProductsCsvFile(listeArticleAAjouter);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Article> retirerArticleDejaPresents(List<Article> listeArticle) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("csv/produitsExistants.csv"));
        String line;
        List<String> productNameList = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            // process the line.
            productNameList.add(line.replace("\"", ""));
        }
        logger.info("Nombre d'article déjà présent en base de données:" + productNameList.size());
        Iterator<Article> it = listeArticle.iterator();
        int nbArticleTrouve = 0;
        while (it.hasNext()) {
            Article article = it.next();
            if (productNameList.contains(article.getNom())) {
                nbArticleTrouve++;
                it.remove();
            }
        }
        logger.info("Nb articles déjà présent trouvés = " + nbArticleTrouve);
        logger.info("Nb articles restant = " + listeArticle.size());
        br.close();

        return listeArticle;
    }

    private void extractSubCategoriesImg() {
        // TODO Auto-generated method stub

    }

    private File saveHomePageOnDisk(String url, String destinationFolder)
            throws MalformedURLException, IOException {
        if (url != null && !url.isEmpty() && !destinationFolder.isEmpty()) {
            String destinationPath = destinationFolder + "/" + "accueil.html";
            return FileDownloader.saveFileToDisk(url, destinationPath);
        }
        else {
            throw new MalformedURLException("Problem with the url provided in the property file");
        }
    }

    private void parseHomePageFromDisk(File homePage) {

        try {
            Navigation navigationTmp = ParseHomePage.parseHomePageFromDisk(homePage);
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
            Document documentHTML = reader.readFileOrUrl(subCategory.getUrl());
            resultatParsingSousCategorie = ParserSousCategorie.parserSousCategorie(subCategory,
                    documentHTML, true);
            List<String> urlsSousPage = resultatParsingSousCategorie.getListeAutrePagesAParserMemeCategorie();
            if (!urlsSousPage.isEmpty()) {
                for (String urlSousPage : urlsSousPage) {
                    documentHTML = reader.readFileOrUrl(urlSousPage);
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

    /**
     * Affecte contexte
     * 
     * @param contexte contexte à affecter
     */
    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    /**
     * Affecte navigation
     * 
     * @param navigation navigation à affecter
     */
    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    /**
     * Affecte reader
     * 
     * @param reader reader à affecter
     */
    public void setReader(Reader reader) {
        this.reader = reader;
    }

}
