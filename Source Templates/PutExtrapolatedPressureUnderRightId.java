public class PutExtrapolatedPressureUnderRightId extends FilterFramework {

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (frame.get(Frame.EXTRAPOLATED_PRESSURE) != null) {
                    frame.put(Frame.PRESSURE, frame.get(Frame.EXTRAPOLATED_PRESSURE));
                }
                writeNextFilterOutputPort(frame);
            }

        } // while
    } // run

} // FilterTemplate
