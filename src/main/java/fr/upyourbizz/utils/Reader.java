/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.utils.constantes.Consts;

/**
 * Reader
 */
public class Reader {

    // ===== Attributs statiques ==============================================

    private Contexte contexte;

    // ===== Méthodes statiques ===============================================

    private static Logger logger = LoggerFactory.getLogger(Reader.class);

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
            String filePath = convertUrlToRelativePath(filePathOrUrl);
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
                File file = FileDownloader.saveFileToDisk(filePathOrUrl);
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
        // System.err.println("File " + messageSplit[0] + " not found");
        try {
            logger.info("Downloading: " + fileNotFound);
            FileDownloader.saveFileToDisk("http://" + fileNotFound);
        }
        catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Remove http:// from an url
     * 
     * @param url The url
     * @return the url without http://
     */
    public static String convertUrlToRelativePath(String url) {
        return url.replace("http://", "");
    }

    // ===== Accesseurs =======================================================

    /**
     * Affecte contexte
     * 
     * @param contexte contexte à affecter
     */
    public void setContexte(Contexte contexte) {
        this.contexte = contexte;
    }

    // ===== Classes imbriques ===============================================
}
