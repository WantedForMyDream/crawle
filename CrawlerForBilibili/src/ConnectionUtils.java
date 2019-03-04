import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionUtils {
    private static DatagramSocket socket;
    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static URLConnection getConnection(String urlstring) throws IOException {
        //设置超时时间
        int timeOut = 50000;
        System.setProperty("sun.net.client.defaultConnectTimeout", String
                .valueOf(timeOut));// （单位：毫秒）
        System.setProperty("sun.net.client.defaultReadTimeout", String
                .valueOf(timeOut)); // （单位：毫秒）
        socket.setSoTimeout(timeOut);//设置超时时间

        URL url = new URL(urlstring);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        return connection;

    }
}
