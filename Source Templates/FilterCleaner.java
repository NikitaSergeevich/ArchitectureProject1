/******************************************************************************************************************
 * File: FilterTemplate.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1 Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
 * <p>
 * Description:
 * This class serves as a template for creating filters. The details of threading, filter
 * connections, input, and output are contained in the FilterFramework super class. In order to use
 * this template the program should rename the class. The template includes the run() method which
 * is executed when the filter is started. The run() method is the guts of the filter and is where
 * the programmer should put their filter specific code. In the template there is a main read-write
 * loop for reading from the input port of the filter and writing to the output port of the filter.
 * This template assumes that the filter is a "normal" that it both reads and writes data. That is
 * both the input and output ports are used - its input port is connected to a pipe from an
 * up-stream filter and its output port is connected to a pipe to a down-stream filter. In cases
 * where the filter is a source or sink, you should use the SourceFilterTemplate.java or
 * SinkFilterTemplate.java as a starting point for creating source or sink filters.
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods:
 * <p>
 * public void run() - this method must be overridden by this class.
 ******************************************************************************************************************/

public class FilterCleaner extends FilterFramework {

    int[] id_filter = null;
    static int num = 0;
    String name = "";

    public FilterCleaner(int[] ids) {
        id_filter = ids;
        name = "FilterCleaner" + num;
        num++;
    }

    public void run() {
        while (true) {
            try {
                System.out.print(name + "is goint to read \n");
                Object o = readNextFilterInputPort();
                System.out.print(name + "read an object\n");
                System.out.print(o + "\n");
                if (o instanceof Frame) {
                    Frame frame = (Frame) o;

                    for (int i : id_filter) {
                        if (frame.get(i) != null) {
                            frame.remove(i);
                        }
                    }
                    System.out.print(name + " going to write an object\n");
                    writeNextFilterOutputPort(frame);
                    System.out.print(name + " writted an object\n");
                }
            } catch (EndOfStreamException e) {
                closePorts();
                break;
            }
        } // while
    } // run
} // FilterTemplate
