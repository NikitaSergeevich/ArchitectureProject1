package CommonFiles;

import java.util.ArrayList;
import java.util.List;

public class FrameValuesConverter extends FilterFramework {

    private List<Converter> converters = new ArrayList<>();

    public FrameValuesConverter(List<Converter> converters) {
        this.converters.addAll(converters);
    }

    public void run() {
        while (true) {
            Frame frame = readNextFilterInputPort();
            if (frame != null) {
                if (skipEndFrame(frame)) break;
                for (Converter converter :
                        converters) {
                    converter.convert(frame);
                }
                writeNextFilterOutputPort(frame);
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
