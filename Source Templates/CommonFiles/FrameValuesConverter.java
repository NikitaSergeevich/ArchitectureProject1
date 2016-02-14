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
                for (Converter converter :
                        converters) {
                    converter.convert(frame);
                }
                writeNextFilterOutputPort(frame);
            }
        }
    }
}
