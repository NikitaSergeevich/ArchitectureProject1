import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

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

    public FilterCleaner(int[] ids) {
        id_filter = ids;
    }

    public void run() {
        byte[] dataBytes;

        while (true) {

            /***************************************************************
             * The program can insert code for the filter operations here. Note that data must be
             * received and sent one byte at a time. This has been done to adhere to the pipe and
             * filter paradigm and provide a high degree of portabilty between filters. However, you
             * must reconstruct data on your own. First we read a byte from the input stream...
             ***************************************************************/

            try {
                dataBytes = readNextFilterInputPort();

                /***************************************************************
                 * Here we could insert code to operate on the input stream... Then we write a byte
                 * out to the output port.
                 ***************************************************************/

                HashMap<Integer, Double> m = null;
                Object o = Utils.convertByteArrayToObject(dataBytes);

                if (o instanceof HashMap<?, ?>) {
                    m = (HashMap<Integer, Double>) o;

                    for (int i : id_filter) {
                        if (m.get(i) != null) {
                            m.remove(i);
                        }
                    }

                    byte[] data_buf = Utils.convertObjectToByteArray((Object) m);
                    int data_size = data_buf.length;
                    byte[] size_buf = ByteBuffer.allocate(4).putInt(data_size).array();
                    byte[] frame_buf = new byte[4 + data_size];
                    System.arraycopy(size_buf, 0, frame_buf, 0, 4);
                    System.arraycopy(data_buf, 0, frame_buf, 4, data_buf.length);
                    writeNextFilterOutputPort(dataBytes);
                }
            } catch (EndOfStreamException e) {

                /***************************************************************
                 * When we reach the end of the input stream, an exception is thrown which is shown
                 * below. At this point, you should finish up any processing, close your ports and exit.
                 ***************************************************************/

                closePorts();
                break;
            } // catch
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } // while
    } // run
} // FilterTemplate
