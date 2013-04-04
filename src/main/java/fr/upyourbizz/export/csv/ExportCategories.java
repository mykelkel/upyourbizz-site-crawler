/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.export.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.upyourbizz.core.Category;

/**
 * ExportCategories
 */
public class ExportCategories {

    // ===== Attributs statiques ==============================================

    // ===== Méthodes statiques ===============================================

    /**
     * @param mapNavigation
     */
    /**
     * Create a csv file that include all categories provides in the arguments
     * of the function
     * 
     * @param mapNavigation The map of the categories that will be add in the
     *            csv file
     * @throws IOException Exception throws if an error occurred during the csv
     *             file creation
     */
    public static void createCategoriesCsvFile(Map<Category, List<Category>> mapNavigation)
            throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("categories.csv"));

        for (Entry<Category, List<Category>> entry : mapNavigation.entrySet()) {
            // The characters <>;=#{} are forbidden
            String parentCategory = entry.getKey().getName();
            out.println("1;" + parentCategory + ";;0");
            for (Category subCategory : entry.getValue()) {
                String category = subCategory.getName();
                out.println("1;" + category + ";" + parentCategory + ";0");
            }
        }
        out.close();
    }

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    // ===== Accesseurs =======================================================

    // ===== Classes imbriquées ===============================================
}
