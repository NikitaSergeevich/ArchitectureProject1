/******************************************************************************************************************
 * File: FrameValuesConverter.java
 * Course: MSIT-SE
 * Project: Assignment 1
 * Copyright: Copyright (c) 2016 Innopolis University
 * Versions: 1.0 February 2016
 * <p>
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

public class FilterWildPoints extends FilterFramework {

    public FilterWildPoints() {
    }

    public void run() {
        try {

            Frame f1 = null;
            Frame f2 = null;
            Frame f3 = null;

            double p1 = 0.0;
            double p2 = 0.0;
            double p3 = 0.0;

            Object o = new Object();
            while (!(o instanceof Frame)) {
                o = readNextFilterInputPort();
            }
            f1 = (Frame) o;

            while (true) {
                o = readNextFilterInputPort();
                if (o instanceof Frame) {
                    f2 = (Frame) o;
                    p1 = f1.get(Frame.PRESSURE);
                    p2 = f2.get(Frame.PRESSURE);

                    if (Math.abs(p2 - p1) > 10) {

                        while (!(o instanceof Frame)) {
                            o = readNextFilterInputPort();
                        }
                        f3 = (Frame) o;
                        p3 = f3.get(Frame.PRESSURE);
                        p2 = (p3 + p1) / 2;
                        f2.put(Frame.PRESSURE, p2);
                        System.out.print("Extrapolated:" + p2);
                        writeNextFilterOutputPort(f1);
                        writeNextFilterOutputPort(f2);
                        f1 = f3;
                        f2 = null;
                        f3 = null;
                    } else {
                        writeNextFilterOutputPort(f1);
                        f1 = f2;
                        f2 = null;
                    }

                }

            }

        } // while
        catch (Exception e) {
            closePorts();
        }

    } // run
} // FilterTemplate

