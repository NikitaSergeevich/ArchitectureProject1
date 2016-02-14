import java.io.DataInputStream;
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

    private String fileName; // Input data file.

    public SourceFilter(String fileName) {
        this.fileName = fileName;
    }

    public void run() {

        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName));) {
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
        } catch (IOException iox) {

            /***********************************************************************************
             * The following exception is raised should we have a problem opening the file.
             ***********************************************************************************/

            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        }
        System.out.print("\n \n File is read compeletely");
        while (true) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
} // SourceFilterTemplate
