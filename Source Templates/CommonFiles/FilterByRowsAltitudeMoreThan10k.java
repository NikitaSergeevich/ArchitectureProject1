package CommonFiles;

public class FilterByRowsAltitudeMoreThan10k extends FilterFramework {

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (skipEndFrame(frame)) break;
                if (frame.get(Frame.ALTITUDE) >= 10000) {
                    writeNextFilterOutputPort(frame);
                }
            }
        } // while
    } // run

    private boolean skipEndFrame(Frame frame) {
        if (frame.isEndFrame()) {
            writeNextFilterOutputPort(frame);
            return true;
        }
        return false;
    }
} // FilterTemplate
