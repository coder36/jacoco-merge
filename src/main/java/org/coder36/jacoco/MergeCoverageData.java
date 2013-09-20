package org.coder36.jacoco;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark Middleton
 */
public class MergeCoverageData {

    public static final String URL = "url=";
    public static final String FILE = "file=";
    public static final String OUT = "out=";
    public static final String REPORT_DIR = "reportDir=";
    public static final String SRC_DIR = "srcDir=";
    public static final String CLASS_DIR = "classDir=";
    public static final String RESET = "reset";

    public static void main( String [] args ) {

        List<String> urls = new ArrayList<>();
        List<String> files = new ArrayList<>();

        String outFile = null;
        boolean resetCounters = false;
        String reportOutDir = null;
        String reportSrcDir = null;
        String reportClassDir = null;

        for( String arg: args ) {
            if ( arg.startsWith(URL) ) urls.add( arg.substring(URL.length()) );
            if ( arg.startsWith(FILE) ) files.add( arg.substring(FILE.length()) );
            if ( arg.startsWith(OUT) ) outFile = arg.substring(OUT.length());
            if ( arg.startsWith(REPORT_DIR) ) reportOutDir = arg.substring(REPORT_DIR.length());
            if ( arg.startsWith(SRC_DIR) ) reportSrcDir = arg.substring(SRC_DIR.length());
            if ( arg.startsWith(CLASS_DIR) ) reportClassDir = arg.substring(CLASS_DIR.length());
            if ( arg.equals(RESET) ) resetCounters = true;
        }

        if ( outFile == null ) {
            System.err.println( "Supported Args: " + URL + "<host:port> "+ URL +"<host:port> " +
                                        "[" + RESET + "] " +
                                        FILE + "<file> " + FILE + "<file> " +
                                        OUT + "<execFile> " + REPORT_DIR + "<outputFolder> " +
                                        CLASS_DIR + "<classFolder> " +  SRC_DIR + "<sourceFolder>" );
            System.exit(0);
        }

        CoverageData rep = new CoverageData();
        for ( String url : urls ) {
            rep.readFromAgent( url, resetCounters );
        }

        for ( String file : files ) {
            rep.readFromFile(file);
        }

        rep.save( outFile );

        if ( reportOutDir != null ) {
            // generate report
            rep.writeReport( outFile, reportOutDir, reportClassDir, reportSrcDir );
        }
    }
}
