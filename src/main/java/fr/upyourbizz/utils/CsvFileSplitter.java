/**
 * © 2013, Upyourbizz - All right reserved
 */
package fr.upyourbizz.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CvsFileSplitter
 */
public class CsvFileSplitter {

    // ===== Attributs statiques ==============================================

    // @SuppressWarnings("unused");
    private final Logger logger = LoggerFactory.getLogger(CsvFileSplitter.class);

    // ===== Méthodes statiques ===============================================

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    public static void splitCsvFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int nbCsvFile = 1;
        String fileName = "csv/products" + nbCsvFile + ".csv";
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        String line;
        int nbLineRead = 0;
        while ((line = br.readLine()) != null) {
            // process the line.
            out.println(line);
            nbLineRead++;
            if (nbLineRead > 300) {
                nbCsvFile++;
                nbLineRead = 0;
                fileName = "csv/products" + nbCsvFile + ".csv";
                out.close();
                out = new PrintWriter(new FileWriter(fileName));
            }
        }
        out.close();
        br.close();
    }

    // ===== Accesseurs =======================================================

    // ===== Classes imbriquées ===============================================
}
