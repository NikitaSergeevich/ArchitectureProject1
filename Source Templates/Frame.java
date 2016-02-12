import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Frame implements Serializable {

    private static final long serialVersionUID = -5618572043151736521L;

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
        //TODO
    }

    public void clear() {
        map.clear();
    }
}
