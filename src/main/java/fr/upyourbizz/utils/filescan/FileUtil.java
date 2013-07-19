/**
 * 
 */
package fr.upyourbizz.utils.filescan;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikaël THIBAULT
 */
public class FileUtil {

    // System.out.println( FileUtil.getFilesRecursive( f, new SuffixFilter(
    // TypeFilter.FILE, ".java" ) ) );
    public static final List<File> getFilesRecursive(final File basedir, final FileFilter filter) {

        List<File> files = new ArrayList<File>();

        if (basedir != null && basedir.isDirectory()) {

            for (File subdir : basedir.listFiles(TypeFilter.DIR)) {
                files.addAll(FileUtil.getFilesRecursive(subdir, filter));
            }

            files.addAll(Arrays.asList(basedir.listFiles(filter)));
        }

        return files;
    }

    /**
     * copie le fichier source dans le fichier resultat retourne vrai si cela
     * réussit
     */
    public static boolean copyFile(File source, File dest) {
        try {
            // Declaration et ouverture des flux
            java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);

            try {
                java.io.FileOutputStream destinationFile = null;

                try {
                    destinationFile = new FileOutputStream(dest);

                    // Lecture par segment de 0.5Mo
                    byte buffer[] = new byte[512 * 1024];
                    int nbLecture;

                    while ((nbLecture = sourceFile.read(buffer)) != -1) {
                        destinationFile.write(buffer, 0, nbLecture);
                    }
                }
                finally {
                    destinationFile.close();
                }
            }
            finally {
                sourceFile.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false; // Erreur
        }

        return true; // Résultat OK
    }
}
