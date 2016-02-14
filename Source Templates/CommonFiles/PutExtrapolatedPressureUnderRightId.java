package CommonFiles;

public class PutExtrapolatedPressureUnderRightId extends FilterFramework {

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (skipEndFrame(frame)) break;
                if (frame.get(Frame.EXTRAPOLATED_PRESSURE) != null) {
                    frame.put(Frame.PRESSURE, frame.get(Frame.EXTRAPOLATED_PRESSURE));
                }
                writeNextFilterOutputPort(frame);
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
