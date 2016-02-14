package CommonFiles;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilterFramework extends Thread {

    // Define filter input and output ports
    private List<ObjectInputStream> inputReadPorts = new ArrayList<ObjectInputStream>();
    private List<ObjectOutputStream> outputWritePorts = new ArrayList<ObjectOutputStream>();

    private List<PipedInputStream> inputPipedReadPorts = new ArrayList<PipedInputStream>();
    private List<PipedOutputStream> outputPipedWritePorts = new ArrayList<PipedOutputStream>();

    /***************************************************************************
     * CONCRETE METHOD:: connect
     * <p>
     * Purpose: This method connects filters to each other. All
     * connections are through the input port of each filter. That is each filter's
     * input port is connected to another filter's output port through this method.
     * <p>
     * Arguments: CommonFiles.FilterFramework - this is the filter that this filter will connect to.
     * <p>
     * Returns: void
     * <p>
     * Exceptions: IOException
     ****************************************************************************/

    public void connect(FilterFramework filter) {
        try {
            // Connect this filter's input to the upstream pipe's output stream
            PipedInputStream pis = new PipedInputStream();
            PipedOutputStream pos = new PipedOutputStream();
            pis.connect(pos);
            inputPipedReadPorts.add(pis);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(pos);
            ObjectInputStream objectInputStream = new ObjectInputStream(pis);
            inputReadPorts.add(objectInputStream);
            filter.addOutputStream(pos, objectOutputStream);
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " CommonFiles.FilterFramework error connecting::" + Error);
        } // try-catch
    } // connect

    void addOutputStream(PipedOutputStream pos, ObjectOutputStream objectOutputStream) throws IOException {
        outputPipedWritePorts.add(pos);
        outputWritePorts.add(objectOutputStream);
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

    Frame readNextFilterInputPort () {
        return readNextFilterInputPort(0);
    }

    Frame readNextFilterInputPort(int inputPort) {
        try {
            ObjectInputStream ois = inputReadPorts.get(inputPort);
            Object readObject = ois.readObject();
            if (readObject instanceof Frame) {
                return (Frame) readObject;
            }
            return null;
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
            return null;
        } // try-catch
    } // ReadFilterPort

    int getInputSize() {
        return inputPipedReadPorts.size();
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

    void writeNextFilterOutputPort(Frame frame) {
        try {
            for (ObjectOutputStream osr : outputWritePorts) {
                osr.writeObject(frame);
                osr.flush();
            }
            sleep(100);
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + e);
        } // try-catch
    } // writeFilterPort

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
            for (ObjectInputStream itr : inputReadPorts) {
                itr.close();
            }
            for (ObjectOutputStream itr : outputWritePorts) {
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

} // CommonFiles.FilterFramework
