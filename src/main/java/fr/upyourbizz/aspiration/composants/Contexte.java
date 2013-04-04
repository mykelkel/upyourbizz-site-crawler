/**
 * 
 */
package fr.upyourbizz.aspiration.composants;

import fr.upyourbizz.utils.constantes.Consts;

/**
 * @author Mikael THIBAULT
 */
public class Contexte {

    private String homePagePath = Consts.EMPTY_STRING;

    private String homePageUrl = Consts.EMPTY_STRING;

    private int nbSousCategorieAParcourir = -1;

    /**
     * if true, the website will be read from the disk, if false it will be read
     * from internet
     */
    private boolean fromDisk = false;

    /**
     * If fromDisk = false, the website will be download from internet. If
     * saveFile = true, every files will be save to disk
     */
    private boolean saveFile = false;

    private boolean downloadFileIfMissing = true;

    public String getHomePagePath() {
        return homePagePath;
    }

    public void setHomePagePath(String homePagePath) {
        this.homePagePath = homePagePath;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public int getNbSousCategorieAParcourir() {
        return nbSousCategorieAParcourir;
    }

    public void setNbSousCategorieAParcourir(int nbSousCategorieAParcourir) {
        this.nbSousCategorieAParcourir = nbSousCategorieAParcourir;
    }

    public boolean isFromDisk() {
        return fromDisk;
    }

    public void setFromDisk(boolean fromDisk) {
        this.fromDisk = fromDisk;
    }

    public boolean isSaveFile() {
        return saveFile;
    }

    public void setSaveFile(boolean saveFile) {
        this.saveFile = saveFile;
    }

    public boolean isDownloadFileIfMissing() {
        return downloadFileIfMissing;
    }

    public void setDownloadFileIfMissing(boolean downloadFileIfMissing) {
        this.downloadFileIfMissing = downloadFileIfMissing;
    }
}
