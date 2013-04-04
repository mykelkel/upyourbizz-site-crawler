/**
 * 
 */
package fr.upyourbizz.parsing.magento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.upyourbizz.core.Category;

/**
 * @author Mikael THIBAULT
 */
public class ParserSousCategorie {

    public static ResultatParsingSousCategorie parserSousCategorie(Category subCategory,
            Document documentHTML, boolean premierAppel) throws IOException {
        return parserSousCategorie(subCategory, true, documentHTML);
    }

    private static ResultatParsingSousCategorie parserSousCategorie(Category subCategory,
            boolean premierAppel, Document sousCategorie) {
        ResultatParsingSousCategorie resultat = new ResultatParsingSousCategorie();
        Element productList = sousCategorie.select("div.products-list").first();
        if (productList != null) {
            Elements productsInfo = productList.select("div.info_product");
            if (productsInfo != null) {
                for (Element productInfo : productsInfo) {
                    Element productInfoLink = productInfo.select("a.name-link").first();
                    if (productInfoLink != null) {
                        String productInfoLinkUrl = productInfoLink.attributes().get("href").toLowerCase();
                        resultat.getListeUrlArticlesTrouves().add(productInfoLinkUrl);
                    }
                }
            }
            if (premierAppel) {
                resultat.getListeAutrePagesAParserMemeCategorie().addAll(
                        verificationAutrePageAParser(sousCategorie));
            }
        }
        else {
            Element categoryList = sousCategorie.select("div.category-list").first();
            if (categoryList != null) {
                Elements nouvellesSousCategorie = categoryList.select("h2.category-title");
                if (nouvellesSousCategorie != null) {
                    for (Element nouvelleSousCategorie : nouvellesSousCategorie) {
                        Element productInfoLink = nouvelleSousCategorie.select("a").first();
                        String nouvelleSousCategorieLink = productInfoLink.attributes().get("href").toLowerCase();
                        String nouvelleSousCategorieName = nouvelleSousCategorie.text();

                        // On crée la nouvelle catégorie
                        List<Category> nouvelleListeCategorie = new ArrayList<Category>();
                        Category newCategory = new Category(nouvelleSousCategorieName,
                                nouvelleSousCategorieLink);
                        newCategory.setParentCategory(subCategory);
                        nouvelleListeCategorie.add(newCategory);
                        if (!resultat.getMapAutresSousCategories().containsKey(subCategory)) {
                            resultat.getMapAutresSousCategories().put(subCategory,
                                    nouvelleListeCategorie);
                        }
                        else {
                            resultat.getMapAutresSousCategories().get(subCategory).add(
                                    new Category(nouvelleSousCategorieName,
                                            nouvelleSousCategorieLink));
                        }
                    }
                }
            }
        }

        return resultat;
    }

    private static List<String> verificationAutrePageAParser(Document sousCategorie) {
        List<String> autrePageAParser = new ArrayList<String>();
        Element pagination = sousCategorie.select("div.results-pages").first();
        if (pagination != null) {
            Elements liens = pagination.select("a:not(.previous), a:not(.next)");
            if (liens != null) {
                for (Element lienElem : liens) {
                    if (lienElem.className() != null && lienElem.className().isEmpty()) {
                        String lien = lienElem.attributes().get("href").toLowerCase();
                        autrePageAParser.add(lien);
                    }
                }
            }
        }
        return autrePageAParser;
    }
}
