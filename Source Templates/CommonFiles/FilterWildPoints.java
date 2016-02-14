package CommonFiles;

import java.util.LinkedList;
import java.util.List;

public class FilterWildPoints extends FilterFramework {

    private List<Frame> framesToReplace = new LinkedList<>();
    private Frame lastKnownValid = null;

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (isFrameValid(frame, lastKnownValid)) {
                    if (!framesToReplace.isEmpty()) {
                        for (Frame invalidFrame :
                                framesToReplace) {
                            if (lastKnownValid == null) {
                                invalidFrame.put(Frame.EXTRAPOLATED_PRESSURE,
                                        frame.get(Frame.PRESSURE));
                            } else {
                                invalidFrame.put(Frame.EXTRAPOLATED_PRESSURE,
                                        (invalidFrame.get(Frame.PRESSURE) + frame.get(Frame.PRESSURE)) / 2);
                            }
                            writeNextFilterOutputPort(invalidFrame);
                        }
                    }
                    framesToReplace.clear();
                    writeNextFilterOutputPort(frame);
                    lastKnownValid = frame;
                } else {
                    framesToReplace.add(frame);
                }


            }
        }
    }

    private boolean isFrameValid(Frame frame, Frame lastKnownValid) {
        if (frame.get(Frame.PRESSURE) > 0 && (lastKnownValid == null ||
                Math.abs(frame.get(Frame.PRESSURE) - lastKnownValid.get(Frame.PRESSURE)) < 10)) {
            return true;
        } else {
            return false;
        }
    }
} // FilterTemplate

