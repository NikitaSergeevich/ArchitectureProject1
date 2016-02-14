public class AltitudeConv implements Converter {

    @Override
    public void convert(Frame frame) {
        frame.put(Frame.ALTITUDE, frame.get(Frame.ALTITUDE) / 3.2808);
    }
}
