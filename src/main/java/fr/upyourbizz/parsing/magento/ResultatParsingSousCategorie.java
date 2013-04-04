/**
 * 
 */
package fr.upyourbizz.parsing.magento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.upyourbizz.core.Category;

/**
 * @author Mikael THIBAULT
 */
public class ResultatParsingSousCategorie {

    List<String> listeAutrePagesAParserMemeCategorie = new ArrayList<String>();

    List<String> listeUrlArticlesTrouves = new ArrayList<String>();

    Map<Category, List<Category>> mapAutresSousCategories = new HashMap<Category, List<Category>>();

    public ResultatParsingSousCategorie() {
        super();
    }

    public List<String> getListeAutrePagesAParserMemeCategorie() {
        return listeAutrePagesAParserMemeCategorie;
    }

    public List<String> getListeUrlArticlesTrouves() {
        return listeUrlArticlesTrouves;
    }

    public Map<Category, List<Category>> getMapAutresSousCategories() {
        return mapAutresSousCategories;
    }

    public void setMapAutresSousCategories(Map<Category, List<Category>> mapAutresSousCategories) {
        this.mapAutresSousCategories = mapAutresSousCategories;
    }
}
