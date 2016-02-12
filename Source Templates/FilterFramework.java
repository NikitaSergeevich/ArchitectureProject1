/******************************************************************************************************************
 * File: FilterFramework.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
 * <p>
 * Description:
 * <p>
 * This superclass defines a skeletal filter framework that defines a filter in terms of the input
 * and output ports. All filters must be defined in terms of this framework - that is, filters must
 * extend this class in order to be considered valid system filters. Filters as standalone threads
 * until the input port no longer has any data - at which point the filter finishes up any work it
 * has to do and then terminates.
 * <p>
 * Parameters:
 * <p>
 * inputReadPort: This is the filter's input port. Essentially this port is connected to another
 * filter's piped output steam. All filters connect to other filters by connecting their input ports
 * to other filter's output ports. This is handled by the Connect() method.
 * <p>
 * outputWritePort: This the filter's output port. Essentially the filter's job is to read data from
 * the input port, perform some operation on the data, then write the transformed data on the output
 * port.
 * <p>
 * inputFilter: This is a reference to the filter that is connected to the instance filter's
 * input port. This reference is to determine when the upstream filter has stopped sending data
 * along the pipe.
 * <p>
 * Internal Methods:
 * <p>
 * public void connect(FilterFramework filter)
 * public byte readFilterInputPort()
 * public void writeFilterOutputPort(byte datum)
 * public boolean endOfInputStream()
 ******************************************************************************************************************/

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FilterFramework extends Thread {

    // Define filter input and output ports
    private List<PipedInputStream> inputReadPorts = new ArrayList<PipedInputStream>();
    private List<PipedOutputStream> outputWritePorts = new ArrayList<PipedOutputStream>();

    // The following reference to a filter is used because java pipes are able to reliably
    // detect broken pipes on the input port of the filter. This variable will point to
    // the previous filter in the network and when it dies, we know that it has closed its
    // output pipe and will send no more data.

    private List<FilterFramework> inputFilters = new ArrayList<FilterFramework>();

    protected byte[] l_arr = new byte[8];
    protected byte[] s_arr = new byte[4];
    int curr_i = 0;
    int curr_o = 0;

    /***************************************************************************
     * InnerClass:: endOfStreamExeception
     * <p>
     * Purpose: This
     * <p>
     * Arguments: none
     * <p>
     * Returns: none
     * <p>
     * Exceptions: none
     ****************************************************************************/

    class EndOfStreamException extends Exception {
        static final long serialVersionUID = 0; // the version for streaming

        EndOfStreamException() {
            super();
        }

        EndOfStreamException(String s) {
            super(s);
        }
    } // class

    /***************************************************************************
     * CONCRETE METHOD:: connect
     * <p>
     * Purpose: This method connects filters to each other. All
     * connections are through the input port of each filter. That is each filter's
     * input port is connected to another filter's output port through this method.
     * <p>
     * Arguments: FilterFramework - this is the filter that this filter will connect to.
     * <p>
     * Returns: void
     * <p>
     * Exceptions: IOException
     ****************************************************************************/

    void connect(FilterFramework filter) {
        try {
            // Connect this filter's input to the upstream pipe's output stream
            PipedInputStream pis = new PipedInputStream();
            pis.connect(filter.getFreePort());
            inputFilters.add(filter);
            inputReadPorts.add(pis);
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);
        } // try-catch
    } // connect

    PipedOutputStream getFreePort() {
        PipedOutputStream pos = new PipedOutputStream();
        outputWritePorts.add(pos);
        return pos;
    }

    /***************************************************************************
     * CONCRETE METHOD:: readFilterInputPort
     * <p>
     * Purpose: This method reads data from the input port one
     * byte at a time.
     * <p>
     * Arguments: void
     * <p>
     * Returns: byte of data read from the input port of the filter.
     * <p>
     * Exceptions: IOExecption, EndOfStreamException (rethrown)
     ****************************************************************************/

    byte[] readNextFilterInputPort() throws EndOfStreamException {
        int size = 0;
        PipedInputStream pis = null;
        byte[] buffer = null;

        /***********************************************************************
         * Since delays are possible on upstream filters, we first wait until there is data
         * available on the input port. We check,... if no data is available on the input port we
         * wait for a quarter of a second and check again. Note there is no timeout enforced here at
         * all and if upstream filters are deadlocked, then this can result in infinite waits in
         * this loop. It is necessary to check to see if we are at the end of stream in the wait
         * loop because it is possible that the upstream filter completes while we are waiting. If
         * this happens and we do not check for the end of stream, then we could wait forever on an
         * upstream pipe that is long gone. Unfortunately Java pipes do not throw exceptions when
         * the input pipe is broken. So what we do here is to see if the upstream filter is alive.
         * if it is, we assume the pipe is still open and sending data. If the filter is not alive,
         * then we assume the end of stream has been reached.
         ***********************************************************************/

        try {
            pis = inputReadPorts.get(curr_i);
            while (pis.available() == 0) {
                if (endOfInputStream(curr_i)) {
                    throw new EndOfStreamException("End of input stream reached");
                } //if
                sleep(250);
            } // while
            setNextCurrentInputPort();
        } catch (EndOfStreamException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " Error in read port wait loop::" + e);
        } // try-catch

        /***********************************************************************
         * If at least one byte of data is available on the input pipe we can read it. We read and
         * write one byte to and from ports.
         ***********************************************************************/

        try {
            pis.read(s_arr);
            size = ByteBuffer.wrap(s_arr).getInt();
            buffer = new byte[size];
            pis.read(buffer);

            return buffer;
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
            return buffer;
        } // try-catch
    } // ReadFilterPort

    private void setNextCurrentInputPort() {
        curr_i++;
        if (curr_i == getInputSize())
            curr_i = 0;
    }

    private int getInputSize() {
        return inputReadPorts.size();
    }

    /***************************************************************************
     * CONCRETE METHOD:: writeFilterOutputPort
     * <p>
     * Purpose: This method writes data to the output port
     * one byte at a time.
     * <p>
     * Arguments: byte datum - This is the byte that will be written on the output port.of the
     * filter.
     * <p>
     * Returns: void
     * <p>
     * Exceptions: IOException
     ****************************************************************************/

    void writeNextFilterOutputPort(byte[] datum) {
        try {
            PipedOutputStream pos = outputWritePorts.get(curr_o);
            pos.write(datum);
            pos.flush();
            setNextCurrentOutputPort();
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + e);
        } // try-catch
    } // writeFilterPort

    private void setNextCurrentOutputPort() {
        curr_o++;
        if (curr_o == getOutputSize()) {
            curr_o = 0;
        }
    }

    private int getOutputSize() {
        return outputWritePorts.size();
    }

    /***************************************************************************
     * CONCRETE METHOD:: endOfInputStream
     * <p>
     * Purpose: This method is used within this framework which
     * is why it is private It returns a true when there is no more data to read on the input port
     * of the instance filter. What it really does is to check if the upstream filter is still
     * alive. This is done because Java does not reliably handle broken input pipes and will often
     * continue to read (junk) from a broken input pipe.
     * <p>
     * Arguments: void
     * <p>
     * Returns: A value of true if the previous filter has stopped sending data, false if it is
     * still alive and sending data.
     * <p>
     * Exceptions: none
     ****************************************************************************/

    private boolean endOfInputStream(int i) {
        return !inputFilters.get(i).isAlive();
    } // endOfInputStream

    /***************************************************************************
     * CONCRETE METHOD:: closePorts
     * <p>
     * Purpose: This method is used to close the input and output ports
     * of the filter. It is important that filters close their ports before the filter thread exits.
     * <p>
     * Arguments: void
     * <p>
     * Returns: void
     * <p>
     * Exceptions: IOExecption
     ****************************************************************************/

    void closePorts() {
        try {
            for (PipedInputStream itr : inputReadPorts) {
                itr.close();
            }
            for (PipedOutputStream itr : outputWritePorts) {
                itr.close();
            }
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " ClosePorts error::" + e);
        } // try-catch
    } // ClosePorts

    /***************************************************************************
     * CONCRETE METHOD:: run
     * <p>
     * Purpose: This is actually an abstract method defined by Thread. It is
     * called when the thread is started by calling the Thread.start() method. In this case, the
     * run() method should be overridden by the filter programmer using this framework superclass
     * <p>
     * Arguments: void
     * <p>
     * Returns: void
     * <p>
     * Exceptions: IOExecption
     ****************************************************************************/

    public void run() {
        // The run method should be overridden by the subordinate class. Please
        // see the example applications provided for more details.
    } // run

} // FilterFramework
