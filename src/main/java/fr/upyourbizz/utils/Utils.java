/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.utils;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import fr.upyourbizz.aspiration.composants.Contexte;
import fr.upyourbizz.utils.filescan.FileUtil;

/**
 * Utils
 */
public class Utils {

    // ===== Attributs statiques ==============================================

    private Contexte contexte;

    // ===== Méthodes statiques ===============================================

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    /**
     * Read and parse an HTML file from internet or from the disk, depending on
     * the context.
     * 
     * @param filePathOrUrl, the file path or an url
     * @return The result of the parser
     * @throws IOException IO Exception
     */
    public Document readFileOrUrl(String filePathOrUrl) throws IOException {
        boolean fromDisk = contexte.isFromDisk();
        boolean saveFile = contexte.isSaveFile();
        Document document = null;
        if (fromDisk) {
            String filePath = FileUtil.convertUrlToRelativePath(filePathOrUrl);
            File file = new File(filePath);
            document = Jsoup.parse(file, "UTF-8");
        }
        else {
            if (saveFile) {
                File file = FileUtil.saveFileToDisk(filePathOrUrl);
                document = Jsoup.parse(file, "UTF-8");
            }
            else {
                document = Jsoup.connect(filePathOrUrl).get();
            }
        }
        return document;
    }

    // ===== Accesseurs =======================================================

    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    // ===== Classes imbriquées ===============================================
}
