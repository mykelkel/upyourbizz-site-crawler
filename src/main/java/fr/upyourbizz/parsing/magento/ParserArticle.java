/**
 * 
 */
package fr.upyourbizz.parsing.magento;

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
public class ParserArticle {

    // ===== Attributs statiques ==============================================

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

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
        Element productView = document.select("div.product-view").first();
        if (productView != null) {
            Element productEssential = productView.select("div.product-essential").first();
            if (productEssential != null) {
                Element productShop = productEssential.select("div.product-shop").first();
                if (productShop != null) {
                    Element nomElem = productShop.select("h2").first();
                    if (nomElem != null) {
                        nom = nomElem.text();
                    }
                    Element marqueElem = productShop.select("h1").first();
                    if (marqueElem != null) {
                        marque = marqueElem.text();
                    }

                    Element marqueProduitElem = productShop.select("img[src]").first();
                    if (marqueProduitElem != null) {
                        urlImgMarqueProduit = marqueProduitElem.attributes().get("src").toLowerCase();
                    }
                }
                Element price = productEssential.select("span.price").first();
                if (price != null) {
                    prix = price.text();
                }
                Element productImgBox = productEssential.select("div.product-img-box").first();
                if (productImgBox != null) {
                    Elements imgsBig = productImgBox.select("img.big-media");
                    if (imgsBig != null) {
                        for (Element imgBig : imgsBig) {
                            listeImages.add(imgBig.attributes().get("src").toLowerCase());
                        }
                    }
                    Element carouselMiniature = productImgBox.select("#carousel-maniatures").first();
                    if (carouselMiniature != null) {
                        Elements images = carouselMiniature.getElementsByTag("img");
                        if (images != null) {
                            for (Element img : images) {
                                listeImagesCarousel.add(img.attributes().get("src").toLowerCase());
                            }
                            if (listeImagesCarousel.size() > 1) {
                                logger.debug("Carousel");
                            }
                        }
                    }
                }
            }
            Element divDescriptionProduit = productView.select("div.block-content").first();
            if (divDescriptionProduit != null) {
                description = divDescriptionProduit.html();
            }
        }
        return new Article(categorie, nom, prix, replaceDoubleQuote(description), marque,
                urlImgMarqueProduit, listeImages, listeImagesCarousel);
    }

    private static String replaceDoubleQuote(String string) {

        string = string.replaceAll("[\n\r]", "");
        return string.replace("\"", "'");
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
