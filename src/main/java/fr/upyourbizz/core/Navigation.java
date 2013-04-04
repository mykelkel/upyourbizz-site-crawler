/**
 * 
 */
package fr.upyourbizz.core;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

import fr.upyourbizz.utils.filescan.FileUtil;

/**
 * @author Mikael THIBAULT
 */
public class Navigation {

    /**
     * Map contenant en clef le nom d'une catégorie et en valeur, l'ensemble des
     * urls de ses sous catégories
     */
    private Map<Category, List<Category>> mapNavigation = new HashMap<Category, List<Category>>();

    public Category ajouterNouvelleCategorie(String categorieName, String categorieUrl) {
        Category nouvelleCategory = new Category(categorieName, categorieUrl);
        if (!mapNavigation.containsKey(nouvelleCategory)) {
            mapNavigation.put(nouvelleCategory, new ArrayList<Category>());
        }
        return nouvelleCategory;
    }

    public void ajouterNouvelleSousCategorie(Category parentCategory, String subCategoryName,
            String subCategoryUrl) {
        if (!mapNavigation.containsKey(parentCategory)) {
            System.err.println("Pas de catégorie associée à la sous catégorie " + subCategoryName);
        }
        else {
            mapNavigation.get(parentCategory).add(
                    new Category(subCategoryName, subCategoryUrl, parentCategory));
        }
    }

    public List<String> extraireUrlSousCategorie() {
        List<String> urlsSousCategorie = new ArrayList<String>();
        for (Entry<Category, List<Category>> entry : mapNavigation.entrySet()) {
            for (Category subCategory : entry.getValue()) {
                urlsSousCategorie.add(subCategory.getUrl());
            }
        }
        return urlsSousCategorie;
    }

    public List<String> extraireLienSousCategorie() {
        List<String> urlsSousCategorie = new ArrayList<String>();
        for (Entry<Category, List<Category>> entry : mapNavigation.entrySet()) {
            for (Category subCategory : entry.getValue()) {
                urlsSousCategorie.add(FileUtil.convertUrlToRelativePath(subCategory.getUrl()));
            }
        }
        return urlsSousCategorie;
    }

    public void createCategoriesCsvFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("categories.csv"));

            for (Entry<Category, List<Category>> entry : mapNavigation.entrySet()) {
                // The characters <>;=#{} are forbidden
                String parentCategory = removeForbiddenChar(entry.getKey().getName());
                out.println("1;" + parentCategory + ";;0");
                for (Category subCategory : entry.getValue()) {
                    String category = removeForbiddenChar(subCategory.getName());
                    out.println("1;" + category + ";" + parentCategory + ";0");
                }
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removeForbiddenChar(String categoryName) {
        categoryName.replace("<", "");
        categoryName.replace(">", "");
        categoryName.replace(";", "");
        categoryName.replace("=", "");
        categoryName.replace("#", "");
        categoryName.replace("{", "");
        categoryName.replace("}", "");
        return categoryName;
    }

    @Override
    public String toString() {
        ToStringHelper stringHelper = Objects.toStringHelper(this);
        for (Entry<Category, List<Category>> entry : mapNavigation.entrySet()) {
            stringHelper.add("\rCatégorie", entry.getKey().getName());
            for (Category subCategory : entry.getValue()) {
                stringHelper.add("\rName", subCategory.getName());
                stringHelper.add("\r--Url", subCategory.getUrl());
            }
        }
        return stringHelper.toString();
    }

    // ========= Accesseurs ============================

    public Map<Category, List<Category>> getMapNavigation() {
        return mapNavigation;
    }

    public void setMapNavigation(Map<Category, List<Category>> mapNavigation) {
        this.mapNavigation = mapNavigation;
    }

}