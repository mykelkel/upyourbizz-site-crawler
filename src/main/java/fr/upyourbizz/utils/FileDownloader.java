/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * FileDownloader
 */
public class FileDownloader {

    // ===== Attributs statiques ==============================================

    // ===== Méthodes statiques ===============================================

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    /**
     * Save the file from internet to the disk.
     * 
     * @param urlFile The file url
     * @return The new file
     * @throws MalformedURLException
     * @throws IOException
     */
    public static File saveFileToDisk(String urlFile) throws MalformedURLException, IOException {
        return saveFileToDisk(urlFile, urlFile.replace("http://", ""));
    }

    /**
     * Save the file from internet to the disk.
     * 
     * @param urlFile The file url
     * @param destinationPath The destination path
     * @return The new file
     * @throws MalformedURLException
     * @throws IOException
     */
    public static File saveFileToDisk(String urlFile, String destinationPath)
            throws MalformedURLException, IOException {
        URL url = new URL(urlFile);
        File destination = new File(destinationPath);

        FileUtils.copyURLToFile(url, destination);
        return destination;
    }

    // ===== Accesseurs =======================================================

    // ===== Classes imbriquées ===============================================
}
