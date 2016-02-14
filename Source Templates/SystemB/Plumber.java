package SystemB;

import CommonFiles.*;

import java.util.ArrayList;
import java.util.List;

public class Plumber {

    public static void main(String argv[]) throws InterruptedException {

        /****************************************************************************
         * Here we instantiate three filters.
         ****************************************************************************/

        SourceFilter sourceSubSetA = new SourceFilter("SubSetA.dat");
        SinkFilter sinkOutputB = new SinkFilter("OutputB.txt");
        SinkFilter sinkWildPoints = new SinkFilter("WildPoints.txt");

        ColumnRemover columnRemover = new ColumnRemover(getColumnsToShowInOutputFile());
        ColumnRemover columnRemoverWildPoints = new ColumnRemover(getColumnsToShowInWildPointFile());

        FrameValuesConverter converterTemperatureAndAltitude = new FrameValuesConverter(createConverters());
        FilterByRowsWildPoints rowFilterWildPoints = new FilterByRowsWildPoints();
        PutExtrapolatedPressureUnderRightId rowFilterOutputFile = new PutExtrapolatedPressureUnderRightId();

        FilterWildPoints wildPoints = new FilterWildPoints();

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect filter2 to the source filter (filter3). You
         * must connect filters starting with the sink filter and working your way back to the
         * source as shown here.
         ****************************************************************************/

        sinkOutputB.connect(sourceSubSetA);
        converterTemperatureAndAltitude.connect(columnRemover);
        columnRemover.connect(rowFilterOutputFile);
        rowFilterOutputFile.connect(wildPoints);


        sinkWildPoints.connect(columnRemoverWildPoints);
        columnRemoverWildPoints.connect(rowFilterWildPoints);
        rowFilterWildPoints.connect(wildPoints);

        wildPoints.connect(sourceSubSetA);

        /****************************************************************************
         * Here we start the filters up.
         ****************************************************************************/

        sourceSubSetA.start();
        Thread.sleep(90);
        wildPoints.start();
        Thread.sleep(90);
        rowFilterWildPoints.start();
        rowFilterOutputFile.start();
        Thread.sleep(90);
        columnRemoverWildPoints.start();
        columnRemover.start();
        Thread.sleep(90);
        converterTemperatureAndAltitude.start();
        Thread.sleep(90);
        sinkOutputB.start();
        sinkWildPoints.start();
        Thread.sleep(90);

    } // main

    private static List<Integer> getColumnsToShowInWildPointFile() {
        List<Integer> columnsToShowInOutput = new ArrayList<>();
        columnsToShowInOutput.add(Frame.PRESSURE);
        return columnsToShowInOutput;
    }

    private static List<Integer> getColumnsToShowInOutputFile() {
        List<Integer> columnsToShowInOutput = new ArrayList<>();
        columnsToShowInOutput.add(Frame.TEMPERATURE);
        columnsToShowInOutput.add(Frame.ALTITUDE);
        columnsToShowInOutput.add(Frame.PRESSURE);
        return columnsToShowInOutput;
    }

    private static List<Converter> createConverters() {
        List<Converter> converters = new ArrayList<Converter>();
        converters.add(new TemperatureConv());
        converters.add(new AltitudeConv());
        return converters;
    }
} // PlumberTemplate
