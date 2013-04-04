/**
 * 
 */
package fr.upyourbizz.utils.filescan;

import java.util.regex.Pattern;

/*
* SuffixFilter.java
* accepts files that match a given suffix and type
*/
public class SuffixFilter extends RegExFilter {

        public SuffixFilter( final TypeFilter type, final String suffix ) {

                super( type, "^.*" + Pattern.quote( suffix ) + "$" );
        }
}
