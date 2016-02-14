import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterByRowsWildPoints extends FilterFramework {

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (frame.get(Frame.EXTRAPOLATED_PRESSURE) != null) {
                    writeNextFilterOutputPort(frame);
                }
            }
        } // while
    } // run

} // FilterTemplate
