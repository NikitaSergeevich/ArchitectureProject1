
public class AltitudeConv implements Converter{
	
	@Override
	public void convert(Frame frame) {              
        frame.put(Frame.AlTITUDE, frame.get(Frame.AlTITUDE) / 3.2808);		
	}
}
