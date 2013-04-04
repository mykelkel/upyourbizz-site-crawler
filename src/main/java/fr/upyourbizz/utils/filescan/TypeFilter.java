/**
 * 
 */
package fr.upyourbizz.utils.filescan;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Mikael THIBAULT
 *
 */
public enum TypeFilter implements FileFilter {

    FILE, DIR, ALL;

    public boolean accept( final File file ) {

            return file != null && ( this == ALL || this == FILE && file.isFile() || this == DIR && file.isDirectory() );
    }
}
