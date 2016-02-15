import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plumber {

    public static void main(String argv[]) throws InterruptedException {

        //Source fileA
        String sourceFileA = argv[0];
        //Source fileB
        String sourceFileB = argv[1];

        /****************************************************************************
         * Here we instantiate three filters.
         ****************************************************************************/

        SourceFilter sourceSubSetA = new SourceFilter(sourceFileA);
        SourceFilter sourceSubSetB = new SourceFilter(sourceFileB);
        SinkFilter sinkOutputC = new SinkFilter("OutputC.dat");
        SinkFilter sinkWildPoints = new SinkFilter("PressureWildPoints.dat") {
            @Override
            protected String setTableHeader() {
                return "Time: Pressure (psi): \n";
            }

            @Override
            protected void setOutputFormat(Frame frame, StringBuilder sb) {
                DateFormat dateFormat = new SimpleDateFormat("YYYY:MM:DD:HH:mm:ss");
                NumberFormat pressureFormat = new DecimalFormat("#00.00000");
                sb.append(dateFormat.format(new Date(frame.getTime())));
                sb.append(" ");
                sb.append(pressureFormat.format(frame.get(Frame.PRESSURE)));
                sb.append("\n");
            }
        };
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
        mergerMoreThan10k.connect(filterMoreThan10kSystemB);

        filterMoreThan10kSystemA.connect(sourceSubSetA);
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

        Thread.sleep(190);
        filterLessThan10kSystemA.start();
        filterLessThan10kSystemB.start();

        filterMoreThan10kSystemA.start();
        filterMoreThan10kSystemB.start();

        Thread.sleep(190);
        mergerLessThan10k.start();
        mergerMoreThan10k.start();

        Thread.sleep(190);
        wildPoints.start();

        Thread.sleep(190);
        rowFilterWildPoints.start();
        rowFilterOutputFile.start();

        Thread.sleep(190);
        columnRemoverWildPoints.start();
        columnRemover.start();

        Thread.sleep(190);
        converterTemperatureAndAltitude.start();

        Thread.sleep(190);
        sinkAltitude.start();
        sinkOutputC.start();
        sinkWildPoints.start();

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
