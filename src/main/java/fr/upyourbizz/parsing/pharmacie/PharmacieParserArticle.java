/**
 * 
 */
package fr.upyourbizz.parsing.pharmacie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.upyourbizz.core.Article;
import fr.upyourbizz.utils.constantes.Consts;

/**
 * @author Mikaël THIBAULT
 */
public class PharmacieParserArticle {

    // ===== Attributs statiques ==============================================

    private static Logger logger = LoggerFactory.getLogger(PharmacieParser.class);

    private static String NEW_LINE = "\";\"";// System.getProperty("line.separator");

    // ===== Méthodes statiques ===============================================

    public static Article parserInformationArticle(Document document) throws IOException {
        String nom = Consts.EMPTY_STRING;
        String categorie = Consts.EMPTY_STRING;
        String prix = Consts.EMPTY_STRING;
        String description = Consts.EMPTY_STRING;
        String marque = Consts.EMPTY_STRING;
        String urlImgMarqueProduit = Consts.EMPTY_STRING;
        List<String> listeImages = new ArrayList<String>();
        List<String> listeImagesCarousel = new ArrayList<String>();

        Element prTitle = document.select("div.pr_title").first();
        nom = prTitle.text();

        Element prLaboratory = document.select("div.pr_laboratory").first();
        marque = prLaboratory.text();

        Element productPrice = document.select("span.product_price").first();
        prix = productPrice.text();

        StringBuilder descriptionSb = new StringBuilder();

        Element prDesc = document.select("div.pr_desc").first();
        // descriptionSb.append("Description du produit:");
        descriptionSb.append(prDesc.text());

        Element contentDesc = document.getElementById("content_description");
        descriptionSb.append(NEW_LINE);
        // descriptionSb.append("Complément:");
        descriptionSb.append(contentDesc.text());

        Element contentUsage = document.getElementById("content_usage");
        descriptionSb.append(NEW_LINE);
        // descriptionSb.append("Conseils d'utilisation:");
        descriptionSb.append(contentUsage.text());

        Element contentComposition = document.getElementById("content_composition");
        descriptionSb.append(NEW_LINE);
        // descriptionSb.append("Composition:");
        descriptionSb.append(contentComposition.text());

        description = descriptionSb.toString();

        Element imgProduct = document.getElementById("img_product");
        listeImages.add("http://www.1001pharmacies.com/"
                + imgProduct.attributes().get("src").toLowerCase());

        return new Article(categorie, nom, prix, description, marque, urlImgMarqueProduit,
                listeImages, listeImagesCarousel);
    }

    private static String replaceDoubleQuote(String string) {

        string = string.replaceAll("[\n\r]", "");
        return string.replace("\"", "'");
    }

    private static String replaceSpace(String string) {
        // Search and replace all non ASCII letters:
        string = string.replaceAll("[^\\x00-\\x7F]", "");
        // string = string.replaceAll("\\s|€", "");
        // return string.replaceAll(",", ".");
        return string;
    }

    public static boolean estFicheProduit(String lienFichier) throws IOException {
        File input = new File(lienFichier);
        Document doc = Jsoup.parse(input, "UTF-8");
        Elements content = doc.getElementsByClass("product-view");
        return content.size() > 0;
    }

    public static boolean estFicheProduit(File fichier) throws IOException {
        Document doc = Jsoup.parse(fichier, "UTF-8");
        Elements content = doc.getElementsByClass("product-view");
        return content.size() > 0;
    }

}
