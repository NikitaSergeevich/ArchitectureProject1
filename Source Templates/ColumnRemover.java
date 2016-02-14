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
                for (Integer key : getKeys(frame)) {
                    if (!columnsToShow.contains(key)) {
                        frame.remove(key);
                    }
                }
                writeNextFilterOutputPort(frame);
            }

        } // while
    } // run

    private Set<Integer> getKeys(Frame frame) {
        //New Set is created to avoit ConcurrentModificationException
        return new HashSet<>(frame.getKeySet());
    }
} // FilterTemplate
