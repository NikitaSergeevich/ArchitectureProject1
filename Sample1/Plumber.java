/******************************************************************************************************************
 * File: Plumber.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example to illustrate how to use the PlumberTemplate to create a main
 * thread that instantiates and connects a set of filters. This example consists of three filters: a
 * source, a middle filter that acts as a pass-through filter (it does nothing to the data), and a
 * sink filter which illustrates all kinds of useful things that you can do with the input stream of
 * data.
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/
public class Plumber {
    public static void main(String argv[]) {
        /****************************************************************************
         * Here we instantiate three filters.
         ****************************************************************************/

        SourceFilter filter1 = new SourceFilter();
        MiddleFilter filter2 = new MiddleFilter();
        SinkFilter filter3 = new SinkFilter();

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect Filter2 to the source filter (filter3).
         ****************************************************************************/

        filter3.connect(filter2); // This esstially says, "connect filter3 input port to filter2 output port
        filter2.connect(filter1); // This esstially says, "connect filter2 intput port to filter1 output port

        /****************************************************************************
         * Here we start the filters up. All-in-all,... its really kind of boring.
         ****************************************************************************/

        filter1.start();
        filter2.start();
        filter3.start();
    } // main
} // Plumber
