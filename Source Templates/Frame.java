import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Frame implements Serializable {

    private static final long serialVersionUID = -5618572043151736521L;
    private long time;

    public static int VELOCITY = 1;
    public static int ALTITUDE = 2;
    public static int PRESSURE = 3;
    public static int TEMPERATURE = 4;
    public static int ATTITUDE = 5;

    private final Map<Integer, Double> map = new HashMap<>();

    public void put(Integer id, Double value) {
        map.put(id, value);
    }

    public Double get(Integer id) {
        return map.get(id);
    }

    public int size() {
        return map.size();
    }

    public void remove(int i) {
        map.remove(i);
    }
    
    public Set<Integer> getKeySet() {
    	return map.keySet();
    }

    public void clear() {
        map.clear();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
