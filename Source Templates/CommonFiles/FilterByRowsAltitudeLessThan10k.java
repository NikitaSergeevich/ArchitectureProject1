package CommonFiles;

public class FilterByRowsAltitudeLessThan10k extends FilterFramework {

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (frame.get(Frame.ALTITUDE) < 10000) {
                    writeNextFilterOutputPort(frame);
                }
            }
        } // while
    } // run

} // FilterTemplate
