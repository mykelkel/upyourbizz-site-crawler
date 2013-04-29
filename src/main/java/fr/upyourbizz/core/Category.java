/**
 * © 2013, Upyourbizz - Tous droits réservés
 */
package fr.upyourbizz.core;

import java.util.List;

import com.google.common.base.Objects;

import fr.upyourbizz.utils.constantes.Consts;

/**
 * @author Mikaël THIBAULT
 */
public class Category {

    // ===== Attributs statiques ==============================================

    private final String name;

    private final String description;

    private Category parentCategory;

    private final String url;

    private final String urlImage;

    private List<String> urlRelatedProducts;

    // ===== Méthodes statiques ===============================================

    // ===== Attributs ========================================================

    // ===== Constructeurs ====================================================

    /**
     * Constructor
     * 
     * @param name The name of the category
     * @param url The url of the category
     */
    public Category(String name, String url) {
        super();
        this.name = removeForbiddenChar(name);
        this.url = url;
        this.urlImage = Consts.EMPTY_STRING;
        this.description = Consts.EMPTY_STRING;
        parentCategory = null;
    }

    public Category(String name, String url, Category parentCategory) {
        super();
        this.name = removeForbiddenChar(name);
        this.url = url;
        this.urlImage = Consts.EMPTY_STRING;
        this.description = Consts.EMPTY_STRING;
        this.parentCategory = parentCategory;
    }

    public Category(String name, String description, Category parentCategory, String url,
            String urlImage) {
        super();
        this.name = removeForbiddenChar(name);
        this.description = description;
        this.parentCategory = null;
        this.url = url;
        this.urlImage = urlImage;
    }

    // ===== Méthodes =========================================================

    private String removeForbiddenChar(String categoryName) {
        categoryName = categoryName.replace("<", "");
        categoryName = categoryName.replace(">", "");
        categoryName = categoryName.replace(";", "");
        categoryName = categoryName.replace("=", "");
        categoryName = categoryName.replace("#", "");
        categoryName = categoryName.replace("{", "");
        categoryName = categoryName.replace("}", "");
        return categoryName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        return Objects.equal(this.name, other.getName());
    }

    /**
     * Retourne parentCategory
     * 
     * @return parentCategory
     */
    public Category getParentCategory() {
        return parentCategory;
    }

    /**
     * Affecte parentCategory
     * 
     * @param parentCategory parentCategory à affecter
     */
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     * Retourne urlRelatedProducts
     * 
     * @return urlRelatedProducts
     */
    public List<String> getUrlRelatedProducts() {
        return urlRelatedProducts;
    }

    /**
     * Affecte urlRelatedProducts
     * 
     * @param urlRelatedProducts urlRelatedProducts à affecter
     */
    public void setUrlRelatedProducts(List<String> urlRelatedProducts) {
        this.urlRelatedProducts = urlRelatedProducts;
    }

    /**
     * Retourne name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne description
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retourne url
     * 
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retourne urlImage
     * 
     * @return urlImage
     */
    public String getUrlImage() {
        return urlImage;
    }

}
