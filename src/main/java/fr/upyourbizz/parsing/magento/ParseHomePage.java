/**
 * 
 */
package fr.upyourbizz.parsing.magento;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.upyourbizz.core.Category;
import fr.upyourbizz.core.Navigation;
import fr.upyourbizz.utils.filescan.FileUtil;

/**
 * @author Mikaël THIBAULT
 */
public class ParseHomePage {

    public static Navigation parserLiensAccueil(File fichierAccueil) throws IOException {
        Document accueil = Jsoup.parse(fichierAccueil, "UTF-8");
        return parserLiensAccueil(accueil);
    }

    /**
     * Parse home page, seeking navigation informations
     * 
     * @param urlHomePage
     * @param saveFilesOnDisk
     * @return
     * @throws IOException
     */
    public static Navigation parseHomePageFromInternet(String urlHomePage, boolean saveFilesOnDisk)
            throws IOException {
        if (saveFilesOnDisk) {
            URL url = new URL(urlHomePage);
            File destination = new File(FileUtil.convertUrlToRelativePath(urlHomePage)
                    + "/accueil.html");

            FileUtils.copyURLToFile(url, destination);
            return parserLiensAccueil(destination);
        }
        else {
            Document accueil = Jsoup.connect(urlHomePage).get();
            return parserLiensAccueil(accueil);
        }
    }

    /**
     * Parse home page, seeking navigation informations
     * 
     * @param urlHomePage
     * @param saveFilesOnDisk
     * @return
     * @throws IOException
     */
    public static Navigation parseHomePageFromDisk(String webSiteFolderName) throws IOException {
        File webSiteHomePageOnDisk = new File(webSiteFolderName + "/accueil.html");
        Document homePage = Jsoup.parse(webSiteHomePageOnDisk, "UTF-8");
        return parserLiensAccueil(homePage);
    }

    private static Navigation parserLiensAccueil(Document accueil) {
        Element mainNavigation = accueil.select("#main_navigation").first();
        Navigation navigation = new Navigation();
        if (mainNavigation != null) {
            Elements navigationTopItems = mainNavigation.select("li.navigation-top-item");
            if (navigationTopItems != null) {
                navigationTopItemsParsing(navigationTopItems, navigation);
            }
        }
        return navigation;
    }

    private static void navigationTopItemsParsing(Elements navigationTopItems, Navigation navigation) {
        for (Element navigationTopItem : navigationTopItems) {
            Element navigationTopItemLink = navigationTopItem.select("a.level-top").first();
            if (navigationTopItemLink != null) {
                String navigationTopItemUrl = navigationTopItemLink.attributes().get("href").toLowerCase();
                String navigationTopItemName = navigationTopItemLink.text();
                Category parentCategory = navigation.ajouterNouvelleCategorie(
                        navigationTopItemName, navigationTopItemUrl);
                Elements navigationItems = navigationTopItem.select("li.item");
                if (navigationItems != null) {
                    for (Element navigationItem : navigationItems) {
                        navigationItemParsing(navigationItem, parentCategory, navigation);
                    }
                }
            }

        }
    }

    private static void navigationItemParsing(Element navigationItem, Category parentCategory,
            Navigation navigation) {
        Element navigationItemLink = navigationItem.select("a.item").first();
        if (navigationItemLink != null) {
            String navigationItemLinkUrl = navigationItemLink.attributes().get("href").toLowerCase();
            Element navigationItemNameElem = navigationItem.select("span.nom_bm").first();
            if (navigationItemLink != null) {
                String navigationItemName = navigationItemNameElem.text();
                navigation.ajouterNouvelleSousCategorie(parentCategory, navigationItemName,
                        navigationItemLinkUrl);
            }
        }
    }

}
