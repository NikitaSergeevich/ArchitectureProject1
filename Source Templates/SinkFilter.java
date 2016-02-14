import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/******************************************************************************************************************
 * File: SinkFilterTemplate.java
 * Course: MSIT-SE
 * Project: Assignment 1
 * Copyright: Copyright (c) 2016 Innopolis University
 * Versions: 1.0 February 2016
 * <p>
 * <p>
 * Description:
 * This class serves as a template for creating sink filters. The details of threading, connections
 * writing output are contained in the FilterFramework super class. In order to use this template
 * the program should rename the class. The template includes the run() method which is executed
 * when the filter is started. The run() method is the guts of the filter and is where the
 * programmer should put their filter specific code. In the template there is a main read-write loop
 * for reading from the input port of the filter. The programmer is responsible for writing the data
 * to a file, or device of some kind. This template assumes that the filter is a sink filter that
 * reads data from the input file and writes the output from this filter to a file or device of some
 * kind. In this case, only the input port is used by the filter. In cases where the filter is a
 * standard filter or a source filter, you should use the FilterTemplate.java or the
 * SourceFilterTemplate.java as a starting point for creating standard or source filters.
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods:
 * <p>
 * public void run() - this method must be overridden by this class.
 ******************************************************************************************************************/

public class SinkFilter extends FilterFramework {

    byte[] dataBytes = null;
    String fileName = null;
    DataOutputStream out = null;

    public SinkFilter(String _fileName) {

        fileName = _fileName;
    }

    public void run() {

        /*************************************************************
         * This is the main processing loop for the filter. Since this is a sink filter, we read
         * until there is no more data available on the input port.
         **************************************************************/

        try {
            out = new DataOutputStream(new FileOutputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source writing file...");
        } catch (Exception ex) {
            closePorts();
        }

        while (true) {
            try {
                /*************************************************************
                 * Here we read an object from the input port. Then we check if
                 * object is an instance of Frame class. If so,
                 **************************************************************/

                Frame frame = readNextFilterInputPort();

                if (frame != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("id: %s , value: %s", 0, new Date(frame.getTime())));
                    for (Integer key : frame.getKeySet()) {
                        sb.append(String.format("id: %s , value: %s", key, frame.get(key)));
                    }
                    sb.append("\n");
                    out.write(sb.toString().getBytes());
                }
                //System.out.println("Sink received: " + dataByte);
            }
            catch (IOException e) {
                closePorts();
                break;
            } // catch
        } // while
    } // run
} // FilterTemplate
