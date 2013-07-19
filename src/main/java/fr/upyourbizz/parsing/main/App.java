package fr.upyourbizz.parsing.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.parsing.pharmacie.PharmacieParser;
import fr.upyourbizz.utils.CsvFileSplitter;
import fr.upyourbizz.utils.filescan.FileUtil;
import fr.upyourbizz.utils.filescan.SuffixFilter;
import fr.upyourbizz.utils.filescan.TypeFilter;

/**
 * Upyourbizz site crawler!!
 */
public class App {

    // ===== Attributs statiques ==============================================

    private static Logger logger = LoggerFactory.getLogger(App.class);

    // ===== Méthodes statiques ===============================================

    /**
     * Main function
     * 
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {
        // try {
        // Document doc2 =
        // Jsoup.connect("http://www.1001pharmacies.com/parapharmacie/list/0").data(
        // "category_id", "29").data("offset", "0").data("params[per_page]",
        // "0").data(
        // "uri_string", "parapharmacie/14/sante/29/douleurs-maux").post();
        // Elements productList = doc2.select("div.product_content");
        // System.out.print("Nombre articles = " + productList.size());
        // }
        // catch (IOException e) {
        // // TODO Bloc catch auto-généré
        // e.printStackTrace();
        // }
        parser();
        // String imgSrcFolderPath = "/Users/mykelkel/Pictures/relaisduson";
        // String imgDestFolderPath = "/Users/mykelkel/Pictures/img/p/";
        // finalisationMajImg(imgSrcFolderPath, imgDestFolderPath);
    }

    private static void finalisationMajImg(String imgSrcFolderPath, String imgDestFolderPath) {
        File f = new File(imgSrcFolderPath);
        List<File> fichiers = FileUtil.getFilesRecursive(f, new SuffixFilter(TypeFilter.FILE,
                ".jpg"));
        logger.info("Nombre fichier img trouvés: " + fichiers.size());
        for (File image : fichiers) {
            String fileNameWithExtension = image.getName();
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.length() - 4);
            String path = getPath(fileName, imgDestFolderPath);
            // logger.info(path);
            File dest = new File(path);
            if (dest.exists()) {
                if (!FileUtil.copyFile(image, dest)) {
                    logger.info("Erreur copie du fichier: " + image.getPath() + " vers " + path);
                }
            }
            else {
                // logger.error("Le fichier: " + path + " n'existe pas");

            }
        }

    }

    public static String getPath(String imgName, String imgDestFolderPath) {
        String imgIdStr = String.valueOf(imgName);
        StringBuilder url = new StringBuilder();
        url.append(imgDestFolderPath);
        int compteur = 0;
        while (compteur < imgIdStr.length()) {
            url.append(imgIdStr.charAt(compteur) + "/");
            compteur++;
        }
        url.append(imgIdStr + ".jpg");
        return url.toString();
    }

    private static void parser() {
        logger.info("© 2013, Upyourbizz - All right reserved");
        try {
            String executionPath = System.getProperty("user.dir");
            logger.info("Executing at =>" + executionPath.replace("\\", "/"));
        }
        catch (Exception e) {
            logger.error("Exception caught =" + e.getMessage());
        }

        // create and configure beans
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Contexte contexte = context.getBean("contexte", Contexte.class);
        contexte.setFromDisk(true);
        contexte.setSaveFile(true);
        // contexte.setWebSiteUrl("http://www.terredeson.com");
        // contexte.setWebSiteParseResultFolder("www.terredeson.com");
        contexte.setWebSiteUrl("http://www.1001pharmacies.com");
        contexte.setWebSiteParseResultFolder("www.1001pharmacies.com");
        contexte.setBaseUrlForPostRequest("http://www.1001pharmacies.com/parapharmacie/list/0");

        // Parser parser = context.getBean("parser", Parser.class);
        PharmacieParser parser = context.getBean("pharmacieParser", PharmacieParser.class);
        try {
            parser.parseWebSite();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            CsvFileSplitter.splitProductsCsvFile("csv/products.csv", 100);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ((ClassPathXmlApplicationContext) context).close();
        return;
    }

}
