/**
 * 
 */
package fr.upyourbizz.utils.filescan;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikaël THIBAULT
 */
public class FileUtil {

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
}
