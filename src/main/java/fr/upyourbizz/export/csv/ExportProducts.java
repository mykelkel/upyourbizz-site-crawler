/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.export.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import fr.upyourbizz.core.Article;
import fr.upyourbizz.utils.constantes.Consts;

/**
 * ExportProducts
 */
public class ExportProducts {

    // ===== Attributs statiques ==============================================

    // ===== Méthodes statiques ===============================================

    /**
     * Create a csv file that include all products provides in the arguments of
     * the function
     * 
     * @param productsList The list of the products that will be add in the csv
     *            file
     * @throws IOException Exception throws if an error occurred during the csv
     *             file creation
     */
    public static void createProductsCsvFile(List<Article> productsList) throws IOException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("csv/products.csv"));
            for (Article article : productsList) {
                out.print("1;" + "\"" + article.getCategorie() + "\"" + Consts.CSV_ITEM_SEPARATOR
                        + "\"" + article.getNom() + "\"" + Consts.CSV_ITEM_SEPARATOR + "\""
                        + article.getPrix() + "\"" + Consts.CSV_ITEM_SEPARATOR + "\""
                        + article.getMarque() + "\"" + Consts.CSV_ITEM_SEPARATOR);
                for (int i = 0; i < article.getListeUrlImages().size(); i++) {
                    if (i > 0) {
                        out.print(",");
                    }
                    out.print(article.getListeUrlImages().get(i));
                }
                out.println(Consts.CSV_ITEM_SEPARATOR + "\"" + article.getDescription() + "\"");
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    // ===== Accesseurs =======================================================

    // ===== Classes imbriquées ===============================================
}
