import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class HtmlUtils {
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
        connection.addRequestProperty("User-agent","Mozilla/4.0");

        return connection;

    }
}
