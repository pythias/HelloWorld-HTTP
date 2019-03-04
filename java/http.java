import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Http {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("Server *:8080 started.");

            while (true) {
                Socket client = server.accept();

                byte[] r = new byte[2048];
                client.getInputStream().read(r, 0, 2048);

                PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                pw.println("HTTP/1.1 200 OK");
                pw.println("Connection: close");
                pw.println("Content-Type: text/html; charset=UTF-8\r\n");
                pw.println("Hello World!");

                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
