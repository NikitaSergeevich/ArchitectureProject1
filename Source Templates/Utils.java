import java.io.*;

/**
 * Created by Пользователь on 12.02.2016.
 */
public class Utils {

    private Utils() {}

    public static byte[] convertObjectToByteArray(Object obj) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public static Object convertByteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
            return null;
        }
    }
}
