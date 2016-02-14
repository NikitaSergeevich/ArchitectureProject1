package SystemC;

import CommonFiles.*;

import java.util.ArrayList;
import java.util.List;

public class Plumber {

    public static void main(String argv[]) throws InterruptedException {

        /****************************************************************************
         * Here we instantiate three filters.
         ****************************************************************************/

        SourceFilter sourceSubSetA = new SourceFilter("SubSetA.dat");
        SourceFilter sourceSubSetB = new SourceFilter("SubSetB.dat");
        SinkFilter sinkOutputC = new SinkFilter("OutputC.dat");
        SinkFilter sinkWildPoints = new SinkFilter("PressureWildPoints.dat");
        SinkFilter sinkAltitude = new SinkFilter("LessThan10K.dat");

        ColumnRemover columnRemover = new ColumnRemover(getColumnsToShowInOutputFile());
        ColumnRemover columnRemoverWildPoints = new ColumnRemover(getColumnsToShowInWildPointFile());

        FilterByRowsAltitudeLessThan10k filterLessThan10kSystemA = new FilterByRowsAltitudeLessThan10k();
        FilterByRowsAltitudeMoreThan10k filterMoreThan10kSystemA = new FilterByRowsAltitudeMoreThan10k();

        FilterByRowsAltitudeLessThan10k filterLessThan10kSystemB = new FilterByRowsAltitudeLessThan10k();
        FilterByRowsAltitudeMoreThan10k filterMoreThan10kSystemB = new FilterByRowsAltitudeMoreThan10k();

        FrameValuesConverter converterTemperatureAndAltitude = new FrameValuesConverter(createConverters());
        FilterByRowsWildPoints rowFilterWildPoints = new FilterByRowsWildPoints();
        PutExtrapolatedPressureUnderRightId rowFilterOutputFile = new PutExtrapolatedPressureUnderRightId();

        FilterMerger mergerMoreThan10k = new FilterMerger();
        FilterMerger mergerLessThan10k = new FilterMerger();

        FilterWildPoints wildPoints = new FilterWildPoints();

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect filter2 to the source filter (filter3). You
         * must connect filters starting with the sink filter and working your way back to the
         * source as shown here.
         ****************************************************************************/

        sinkOutputC.connect(converterTemperatureAndAltitude);
        converterTemperatureAndAltitude.connect(columnRemover);
        columnRemover.connect(rowFilterOutputFile);
        rowFilterOutputFile.connect(wildPoints);

        sinkWildPoints.connect(columnRemoverWildPoints);
        columnRemoverWildPoints.connect(rowFilterWildPoints);
        rowFilterWildPoints.connect(wildPoints);

        wildPoints.connect(mergerMoreThan10k);

        mergerMoreThan10k.connect(filterMoreThan10kSystemA);
        filterMoreThan10kSystemA.connect(sourceSubSetA);

        mergerMoreThan10k.connect(filterMoreThan10kSystemB);
        filterMoreThan10kSystemB.connect(sourceSubSetB);

        //Altitude less than 10K
        sinkAltitude.connect(mergerLessThan10k);

        mergerLessThan10k.connect(filterLessThan10kSystemA);
        mergerLessThan10k.connect(filterLessThan10kSystemB);

        filterLessThan10kSystemA.connect(sourceSubSetA);
        filterLessThan10kSystemB.connect(sourceSubSetB);
        /****************************************************************************
         * Here we start the filters up.
         ****************************************************************************/

        sourceSubSetA.start();
        sourceSubSetB.start();
        Thread.sleep(90);
        filterLessThan10kSystemA.start();
        filterLessThan10kSystemB.start();
        Thread.sleep(90);
        filterMoreThan10kSystemA.start();
        filterMoreThan10kSystemB.start();
        Thread.sleep(90);
        mergerLessThan10k.start();
        mergerMoreThan10k.start();
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
        sinkAltitude.start();
        sinkOutputC.start();
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
