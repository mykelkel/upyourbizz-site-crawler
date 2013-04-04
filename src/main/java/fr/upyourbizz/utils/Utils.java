/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.utils.constantes.Consts;
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
     * Read and parse an HTML file from Internet or from the disk, depending on
     * the context. If an error occurred and if the variable
     * downloadFileIfMissing is set to true, the function will try to
     * re-download the file
     * 
     * @param filePathOrUrl the file path or an url
     * @return The result of the parser
     * @throws IOException IO Exception
     */
    public Document readFileOrUrl(String filePathOrUrl) throws IOException {
        boolean fromDisk = contexte.isFromDisk();
        boolean saveFile = contexte.isSaveFile();
        Document document = Jsoup.parse(Consts.JSOUP_PARSER_DEFAULT_PARSING_STRING);
        if (fromDisk) {
            String filePath = FileUtil.convertUrlToRelativePath(filePathOrUrl);
            try {
                File file = new File(filePath);
                document = Jsoup.parse(file, "UTF-8");
            }
            catch (IOException e) {
                if (contexte.isDownloadFileIfMissing()) {
                    downloadFile(e);
                    File file = new File(filePath);
                    document = Jsoup.parse(file, "UTF-8");
                }
                else {
                    e.printStackTrace();
                }
            }
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

    // ===== Accesseurs =======================================================

    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    // ===== Classes imbriquées ===============================================
}
