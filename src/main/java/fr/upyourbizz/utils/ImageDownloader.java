/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImageDownloader
 */
public class ImageDownloader {

    // ===== Attributs statiques ==============================================

    // ===== Méthodes statiques ===============================================

    private static Logger logger = LoggerFactory.getLogger(ImageDownloader.class);

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    // ===== Méthodes =========================================================

    public static void downloadImg(String relativeDirectoryPath, String urlImg) {
        try {
            URL url = new URL(urlImg);
            BufferedImage image = ImageIO.read(url);
            String nomFichier = url.getFile();
            int dotPosition = nomFichier.lastIndexOf('.');
            int slashPosition = nomFichier.lastIndexOf('/');
            String fileName = nomFichier.substring(slashPosition + 1, dotPosition);
            File directory = new File(relativeDirectoryPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    logger.error("Failed to create directory!");
                }
            }
            ImageIO.write(image, "jpg", new File(relativeDirectoryPath + "/" + fileName + ".jpg"));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ===== Accesseurs =======================================================

    // ===== Classes imbriquées ===============================================
}
