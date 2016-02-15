import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SinkFilter extends FilterFramework {

    String fileName = null;
    DataOutputStream out = null;

    public SinkFilter(String fileName) {
        this.fileName = fileName;
        setName("Sink:" + fileName);
    }

    public void run() {

        /*************************************************************
         * This is the main processing loop for the filter. Since this is a sink filter, we read
         * until there is no more data available on the input port.
         **************************************************************/

        try {
            out = new DataOutputStream(new FileOutputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source writing file...");

            out.write(setTableHeader().getBytes());
        } catch (Exception ex) {
            closePorts();
        }

        while (true) {
            try {
                /*************************************************************
                 * Here we read an object from the input port. Then we check if
                 * object is an instance of CommonFiles.Frame class. If so,
                 **************************************************************/

                Frame frame = readNextFilterInputPort();
                if (frame != null) {
                    if (frame.isEndFrame()) {
                        System.out.println("All values are processed");
                        System.exit(0);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        setOutputFormat(frame, sb);
                        out.write(sb.toString().getBytes());
                    }

                }
            } catch (IOException e) {
                closePorts();
                break;
            } // catch
        } // while
    } // run

    protected String setTableHeader() {
        return "Time: Temperature (C): Altitude (m): Pressure (psi): \n";
    }

    protected void setOutputFormat(Frame frame, StringBuilder sb) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY:MM:DD:HH:mm:ss");
        NumberFormat temperatureFormat = new DecimalFormat("#000.00000");
        NumberFormat altitudeFormat = new DecimalFormat("#00000.00000");
        NumberFormat pressureFormat = new DecimalFormat("#00,00000");
        sb.append(dateFormat.format(new Date(frame.getTime())));
        sb.append(" ");
        sb.append(temperatureFormat.format(frame.get(Frame.TEMPERATURE)));
        sb.append(" ");
        sb.append(altitudeFormat.format(frame.get(Frame.ALTITUDE)));
        sb.append(" ");
        sb.append(pressureFormat.format(frame.get(Frame.PRESSURE)));
        if (frame.isInterpolated()) {
            sb.append("*");
        }
        sb.append("\n");
    }

} // FilterTemplate
