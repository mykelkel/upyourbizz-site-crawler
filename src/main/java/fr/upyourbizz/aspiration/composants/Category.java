/**
 * 
 */
package fr.upyourbizz.aspiration.composants;

import java.util.List;

import com.google.common.base.Objects;

import fr.upyourbizz.utils.constantes.Consts;

/**
 * @author Mikael THIBAULT
 */
public class Category {

    private final String name;

    private final String description;

    private Category parentCategory;

    private final String url;

    private final String urlImage;

    private List<String> urlRelatedProducts;

    public Category(String name, String url) {
        super();
        this.name = name;
        this.url = url;
        this.urlImage = Consts.EMPTY_STRING;
        this.description = Consts.EMPTY_STRING;
        parentCategory = null;
    }

    public Category(String name, String url, Category parentCategory) {
        super();
        this.name = name;
        this.url = url;
        this.urlImage = Consts.EMPTY_STRING;
        this.description = Consts.EMPTY_STRING;
        this.parentCategory = parentCategory;
    }

    public Category(String name, String description, Category parentCategory, String url,
            String urlImage) {
        super();
        this.name = name;
        this.description = description;
        this.parentCategory = null;
        this.url = url;
        this.urlImage = urlImage;
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

    public List<String> getUrlRelatedProducts() {
        return urlRelatedProducts;
    }

    public void setUrlRelatedProducts(List<String> urlRelatedProducts) {
        this.urlRelatedProducts = urlRelatedProducts;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlImage() {
        return urlImage;
    }

}
