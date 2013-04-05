/*
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * ImageDownloader
 */
public class ImageDownloader {

    // ===== Attributs statiques ==============================================

    // ===== Méthodes statiques ===============================================

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
            System.out.println(nomFichier);
            File directory = new File(relativeDirectoryPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.err.println("Failed to create directory!");
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
