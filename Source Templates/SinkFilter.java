import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/******************************************************************************************************************
 * File: SinkFilterTemplate.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
 *
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
 *
 * Parameters: None
 *
 * Internal Methods:
 *
 * public void run() - this method must be overridden by this class.
 *
 ******************************************************************************************************************/

public class SinkFilter extends FilterFramework {
	
    byte[] dataBytes = null;
    String fileName = null;
    DataOutputStream out = null;
    
	public SinkFilter(String _fileName)
	{
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
    		
    	}
    	
        while (true) {
            try {
                /*************************************************************
                 * Here we read a byte from the input port. Note that regardless how the data is
                 * written, data must be read one byte at a time from the input pipe. This has been
                 * done to adhere to the pipe and filter paradigm and provide a high degree of
                 * portability between filters. However, you must convert output data as needed on
                 * your own.
                 **************************************************************/
            	
            	HashMap<Integer, Double> m = null;
            	dataBytes = readNextFilterInputPort();
            	Object o = deserialize(dataBytes);
            	
            	if (o instanceof HashMap<?, ?>)
            	{
            		m = (HashMap<Integer, Double>)o;
            	}   

                /*************************************************************
                 * The programmer can insert code for the filter operations here to include writing
                 * the data to some device or file.
                 **************************************************************/
            	
            	Double data = null;
            	for (int i = 0; i < 3; i++)
            	{
            		data = m.get(i);
            		if (data != null)
            		{
            			out.write(serialize((Object)data));
            		}            		
            	}
            	
                //System.out.println("Sink received: " + dataByte);
            } catch (EndOfStreamException e) {

                /***************************************************************
                 * When we reach the end of the input stream, an exception is thrown which is shown
                 * below. At this point, you should finish up any processing, close your ports and exit.
                 ***************************************************************/

                closePorts();
                break;
            } // catch
            catch (IOException e) {
                closePorts();
                break;
			} // catch
            catch (ClassNotFoundException e) {
            	closePorts();
                break;
			} // catch
        } // while
    } // run
} // FilterTemplate
