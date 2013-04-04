/**
 * 
 */
package fr.upyourbizz.utils.filescan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author Mikael THIBAULT
 *
 */
public class FileUtil {

    public static final List<File> getFilesRecursive( final File basedir, final FileFilter filter ) {

            List<File> files = new ArrayList<File>();

            if ( basedir != null && basedir.isDirectory() ) {

                    for ( File subdir : basedir.listFiles( TypeFilter.DIR ) ) {
                            files.addAll( FileUtil.getFilesRecursive( subdir, filter ) );
                    }

                    files.addAll( Arrays.asList( basedir.listFiles( filter ) ) );
            }

            return files;
    }
    
	public static File saveFileToDisk(String urlFichierAccueil)
			throws MalformedURLException, IOException {
		URL url = new URL(urlFichierAccueil);
		File destination = new File(urlFichierAccueil.replace("http://", ""));

		FileUtils.copyURLToFile(url, destination);
		return destination;
	}
	
	public static String convertUrlToRelativePath(String url)
	{
		return url.replace("http://", "");
	}
}
