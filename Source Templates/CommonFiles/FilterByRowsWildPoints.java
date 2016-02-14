package CommonFiles;

public class FilterByRowsWildPoints extends FilterFramework {

    public FilterByRowsWildPoints() {
        setName("FilterByRowsWildPoints");
    }

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (skipEndFrame(frame)) continue;
                if (frame.get(Frame.EXTRAPOLATED_PRESSURE) != null) {
                    writeNextFilterOutputPort(frame);
                } else {
                    writeNextFilterOutputPort(null);
                }
            }
        }
    }

    private boolean skipEndFrame(Frame frame) {
        if (frame.isEndFrame()) {
            writeNextFilterOutputPort(frame);
            return true;
        }
        return false;
    }
}
