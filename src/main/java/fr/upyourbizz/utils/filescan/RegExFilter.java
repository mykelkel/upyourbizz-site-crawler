/**
 * 
 */
package fr.upyourbizz.utils.filescan;

import java.io.File;
import java.io.FileFilter;

/*
* RegExFilter.java
* accepts files that match a given pattern and type
*/
public class RegExFilter implements FileFilter {

        private final TypeFilter type;
        private final String pattern;

        public RegExFilter( final TypeFilter type, final String pattern ) {

                super();
                this.type = type;
                this.pattern = pattern;
        }

        public boolean accept( final File file ) {

                return this.type.accept( file ) && file.getName().matches( this.pattern );
        }
}
