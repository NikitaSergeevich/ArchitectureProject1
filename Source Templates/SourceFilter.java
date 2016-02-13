import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

/******************************************************************************************************************
 * File: SourceFilter.java
 * Course: MSIT-SE
 * Project: Assignment 1
 * Copyright: Copyright (c) 2016 Innopolis University
 * Versions: 1.0 February 2016
 * <p>
 * Description:
 * This class serves as a template for creating source filters. The details of threading,
 * connections writing output are contained in the FilterFramework super class. In order to use this
 * template the program should rename the class. The template includes the run() method which is
 * executed when the filter is started. The run() method is the guts of the filter and is where the
 * programmer should put their filter specific code.The run() method is the main read-write loop for
 * reading data from some source and writing to the output port of the filter. This template assumes
 * that the filter is a source filter that reads data from a file, device (sensor),or generates the
 * data internally, and then writes data to its output port. In this case, only the output port is
 * used. In cases where the filter is a standard filter or a sink filter, you should use the
 * FilterTemplate.java or SinkFilterTemplate.java as a starting point for creating standard or sink
 * filters.
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods:
 * <p>
 * public void run() - this method must be overridden by this class.
 ******************************************************************************************************************/

public class SourceFilter extends FilterFramework {

    String fileName = null; // Input data file.
    DataInputStream in = null; // File stream reference.
    int bytesRead = 0; // Number of bytes read from the input file.
    int bytesWritten = 0; // Number of bytes written to the stream.

    public SourceFilter(String _fileName) {
        fileName = _fileName;
    }

    public void run() {

        try {
            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source reading file...");

            Frame frame = new Frame();
            int id = in.readInt();
            frame.setTime(in.readLong());
            while (in.available() > 0) {
                id = in.readInt();
                if (id == 0) {
                    writeNextFilterOutputPort(frame);
                    frame = new Frame();
                    frame.setTime(in.readLong());
                } else {
                    frame.put(id, in.readDouble());
                }
            }
            writeNextFilterOutputPort(frame);
            System.out.print(frame.getTime() + "\n");
            while (true) {


            }
        } catch (EOFException eoferr) {

            /***********************************************************************************
             * The following exception is raised when we hit the end of input file. Once we reach this
             * point, we close the input file, close the filter ports and exit.
             ***********************************************************************************/

            System.out.println("\n" + this.getName() + "::End of file reached...");
            try {
                in.close();
                closePorts();
                System.out.println("\n" + this.getName() + "::Read file complete, bytes read::" +
                        bytesRead + " bytes written: " + bytesWritten);
            } catch (Exception closeerr) {

                /***********************************************************************************
                 * The following exception is raised should we have a problem closing the file.
                 ***********************************************************************************/

                System.out.println(
                        "\n" + this.getName() + "::Problem closing input data file::" + closeerr);
            }
        } catch (IOException iox) {

            /***********************************************************************************
             * The following exception is raised should we have a problem opening the file.
             ***********************************************************************************/

            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        }
    }
} // SourceFilterTemplate
