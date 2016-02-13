
public class TemperatureConv implements Converter{

	@Override
	public void convert(Frame frame) {
		
		double t_c = (frame.get(Frame.TEMPERATURE) * 1.8)+ 32;                    
        frame.put(Frame.TEMPERATURE, t_c);		
	}

}
