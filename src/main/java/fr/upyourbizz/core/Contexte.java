/**
 * 
 */
package fr.upyourbizz.core;

import fr.upyourbizz.utils.constantes.Consts;

/**
 * @author Mikaël THIBAULT
 */
public class Contexte {

    private String webSiteParseResultFolder = Consts.EMPTY_STRING;

    private String webSiteUrl = Consts.EMPTY_STRING;

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

    /**
     * Retourne webSiteParseResultFolder
     * 
     * @return webSiteParseResultFolder
     */
    public String getWebSiteParseResultFolder() {
        return webSiteParseResultFolder;
    }

    /**
     * Affecte webSiteParseResultFolder
     * 
     * @param webSiteParseResultFolder webSiteParseResultFolder à affecter
     */
    public void setWebSiteParseResultFolder(String webSiteParseResultFolder) {
        this.webSiteParseResultFolder = webSiteParseResultFolder;
    }

    /**
     * Retourne webSiteUrl
     * 
     * @return webSiteUrl
     */
    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    /**
     * Affecte webSiteUrl
     * 
     * @param webSiteUrl webSiteUrl à affecter
     */
    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    /**
     * Retourne fromDisk
     * 
     * @return fromDisk
     */
    public boolean isFromDisk() {
        return fromDisk;
    }

    /**
     * Affecte fromDisk
     * 
     * @param fromDisk fromDisk à affecter
     */
    public void setFromDisk(boolean fromDisk) {
        this.fromDisk = fromDisk;
    }

    /**
     * Retourne saveFile
     * 
     * @return saveFile
     */
    public boolean isSaveFile() {
        return saveFile;
    }

    /**
     * Affecte saveFile
     * 
     * @param saveFile saveFile à affecter
     */
    public void setSaveFile(boolean saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Retourne downloadFileIfMissing
     * 
     * @return downloadFileIfMissing
     */
    public boolean isDownloadFileIfMissing() {
        return downloadFileIfMissing;
    }

    /**
     * Affecte downloadFileIfMissing
     * 
     * @param downloadFileIfMissing downloadFileIfMissing à affecter
     */
    public void setDownloadFileIfMissing(boolean downloadFileIfMissing) {
        this.downloadFileIfMissing = downloadFileIfMissing;
    }
}
