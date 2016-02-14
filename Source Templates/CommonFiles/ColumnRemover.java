package CommonFiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColumnRemover extends FilterFramework {

    List<Integer> columnsToShow = new ArrayList<>();

    public ColumnRemover(List<Integer> columnsToShow) {
        this.columnsToShow.addAll(columnsToShow);
    }

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (skipEndFrame(frame)) break;
                for (Integer key : getKeys(frame)) {
                    if (!columnsToShow.contains(key)) {
                        frame.remove(key);
                    }
                }
                writeNextFilterOutputPort(frame);
            } else {
                writeNextFilterOutputPort(null);
            }

        } // while
    } // run

    private Set<Integer> getKeys(Frame frame) {
        //New Set is created to avoit ConcurrentModificationException
        return new HashSet<>(frame.getKeySet());
    }

    private boolean skipEndFrame(Frame frame) {
        if (frame.isEndFrame()) {
            writeNextFilterOutputPort(frame);
            return true;
        }
        return false;
    }
} // FilterTemplate
