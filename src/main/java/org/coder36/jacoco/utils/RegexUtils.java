package org.coder36.jacoco.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Methods for handling regular expressions (missing from the JDK !!!)
 * @author Mark Middleton
 */
public class RegexUtils {

    /**
     * Give a regEx return an array of matches
     * @param str
     * @param regEx
     * @return
     */
    public static String [] extract( String str, String regEx ) {
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher(str);

        List<String> l = new ArrayList<String>();
        if ( m.find() ) {
            for( int i=1; i<=m.groupCount(); i++ ) {
                l.add( m.group(i) );
            }
        }
        return l.toArray( new String[0] );
    }
}


