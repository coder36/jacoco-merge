package org.coder36.jacoco;

import org.coder36.jacoco.utils.RegexUtils;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.data.*;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Mark Middleton
 */
public class CoverageData {

    private boolean resetCounters;

    private SessionInfoStore sessionInfos = new SessionInfoStore();
    private ExecutionDataStore executionData = new ExecutionDataStore();

    public void readFromAgent(String url, boolean resetCounters) {

        String [] args = RegexUtils.extract( url, "(.*):(\\d{4})" );
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try(Socket socket = new Socket(InetAddress.getByName(host), port) ) {
            RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
            RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());
            reader.setSessionInfoVisitor( sessionInfos );
            reader.setExecutionDataVisitor( executionData );
            writer.visitDumpCommand(true, resetCounters);
            reader.read();
            socket.close();
        }
        catch( Exception e ) {
            e.printStackTrace();
            System.out.println( "continuing....." );
        }

    }

    public void readFromFile(String filename) {
        try {
            ExecutionDataReader reader = new ExecutionDataReader( new FileInputStream(filename) );
            reader.setExecutionDataVisitor(executionData);
            reader.setSessionInfoVisitor(sessionInfos);
            reader.read();
        }
        catch( IOException e ) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Merge execs together
     */
    public void save( String filename )  {
        try(OutputStream os = new FileOutputStream( filename ) ) {
            ExecutionDataWriter dataWriter = new ExecutionDataWriter( os );
            sessionInfos.accept(dataWriter);
            executionData.accept(dataWriter);
            dataWriter.flush();
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }


    public void writeReport( String execFilename, String reportDir, String classesDir, String sourceDir ) {
        try {
            ExecFileLoader loader = new ExecFileLoader();
            loader.load( new File( execFilename ) );
            HTMLFormatter htmlFormatter = new HTMLFormatter();
            IReportVisitor visitor = htmlFormatter.createVisitor(new FileMultiReportOutput(new File(reportDir)));
            visitor.visitInfo(loader.getSessionInfoStore().getInfos(),loader.getExecutionDataStore().getContents());
            CoverageBuilder coverageBuilder = new CoverageBuilder();
            Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), coverageBuilder);
            analyzer.analyzeAll(new File(classesDir));
            IBundleCoverage bundleCoverage = coverageBuilder.getBundle("Coverage");
            visitor.visitBundle(bundleCoverage, new DirectorySourceFileLocator(new File(sourceDir), "utf-8", 4));
            visitor.visitEnd();
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }



}

