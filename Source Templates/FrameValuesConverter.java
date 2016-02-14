import java.util.List;

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

public class FrameValuesConverter extends FilterFramework {

    private List<Converter> converters;

    public FrameValuesConverter(List<Converter> converters) {
        this.converters = converters;
    }

    public void run() {
        while (true) {

            /***************************************************************
             * The program can insert code for the filter operations here. Note that data must be
             * received and sent one byte at a time. This has been done to adhere to the pipe and
             * filter paradigm and provide a high degree of portabilty between filters. However, you
             * must reconstruct data on your own. First we read a byte from the input stream...
             ***************************************************************/

            try {
                /***************************************************************
                 * Here we could insert code to operate on the input stream... Then we write a byte
                 * out to the output port.
                 ***************************************************************/

                Frame frame = readNextFilterInputPort();

                if (frame != null) {
                    for (Converter converter :
                            converters) {
                        converter.convert(frame);
                    }

                    writeNextFilterOutputPort(frame);
                }
            } catch (EndOfStreamException e) {

                /***************************************************************
                 * When we reach the end of the input stream, an exception is thrown which is shown
                 * below. At this point, you should finish up any processing, close your ports and exit.
                 ***************************************************************/

                closePorts();
                break;
            }
        } // while
    } // run
} // FilterTemplate
