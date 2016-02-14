import java.util.ArrayList;
import java.util.List;

/******************************************************************************************************************
 * File: PlumberTemplate.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
 * <p>
 * Description:
 * This class serves as a template for creating a main thread that will instantiate and connect a
 * set of filters. The details of the filter operations is totally self contained within the
 * filters, the "plumber" takes care of starting the filters and connecting each of the filters
 * together. The details of how to connect filters is taken care of by the FilterFramework. In order
 * to use this template the program should rename the class. The template includes the runFilter()
 * method which is executed when the filter is started. While simple, there are semantics for
 * instantiating, connecting, and starting the filters:
 * <p>
 * Step 1: Instantiate the filters as shown in the example below. You should create the filters
 * using the templates provided, and you must use the FilterFramework as a base class for all
 * filters. Every pipe and filter network must have a source filter where data originates, and a
 * sink filter where the data flow terminates.
 * <p>
 * Step 2: Connect the filters. Start with the sink and work backward to the source. Essentially you
 * are connecting the input of each filter to the up-stream filter's output until you get to the
 * source filter. Filter have a connect() method which accepts a FilterFramework type. This method
 * connects the calling filter's input to the passed filter's output. Again the example in the
 * comments below illustrates how this is done.
 * <p>
 * Step 3: Start the filters using the start() method.
 * <p>
 * Once the filters are started this main thread dies and the pipe and filter network processes data
 * until there is no more data movement from the source. Each filter will shutdown when data is no
 * longer available (provided you follow the read semantics described in the filter templates). on
 * their input ports.
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods: None
 ******************************************************************************************************************/
public class Plumber {

    public static void main(String argv[]) throws InterruptedException {

        /****************************************************************************
         * Here we instantiate three filters.
         ****************************************************************************/
        int[] ids1 = {Frame.ATTITUDE, Frame.VELOCITY};
        int[] ids2 = {Frame.ATTITUDE, Frame.VELOCITY, Frame.TEMPERATURE, Frame.ALTITUDE};

        SourceFilter sourceSubSetA = new SourceFilter("SubSetA.dat");
        SinkFilter sinkOutputB = new SinkFilter("OutputB.txt");
        SinkFilter sinkWildPoints = new SinkFilter("WildPoints.txt");

        FilterCleaner filterCleaner = new FilterCleaner(ids1);
        FilterCleaner filterCleanerWildPoints = new FilterCleaner(ids2);

        FrameValuesConverter converterTemperatureAndAltitude = new FrameValuesConverter(createConverters());

        FilterWildPoints wildPoints = new FilterWildPoints();

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect filter2 to the source filter (filter3). You
         * must connect filters starting with the sink filter and working your way back to the
         * source as shown here.
         ****************************************************************************/

        sinkOutputB.connect(converterTemperatureAndAltitude);
        converterTemperatureAndAltitude.connect(filterCleaner);
        filterCleaner.connect(wildPoints);

        sinkWildPoints.connect(filterCleanerWildPoints);
        filterCleanerWildPoints.connect(wildPoints);

        filterCleaner.connect(wildPoints);
        wildPoints.connect(sourceSubSetA);

        /****************************************************************************
         * Here we start the filters up.
         ****************************************************************************/

        sourceSubSetA.start();
        Thread.sleep(90);
        wildPoints.start();
        filterCleanerWildPoints.start();
        Thread.sleep(90);
        filterCleaner.start();
        Thread.sleep(90);
        converterTemperatureAndAltitude.start();
        Thread.sleep(90);
        sinkOutputB.start();
        sinkWildPoints.start();
        Thread.sleep(90);

    } // main

    private static List<Converter> createConverters() {
        List<Converter> converters = new ArrayList<Converter>();
        converters.add(new TemperatureConv());
        converters.add(new AltitudeConv());
        return converters;
    }
} // PlumberTemplate
