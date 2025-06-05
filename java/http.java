import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Http {
    public static void main(String[] args) {
        // Create a fixed-size thread pool with 10 threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server *:8080 started, listening for connections...");

            while (true) {
                // Accept a client connection
                Socket clientSocket = server.accept();
                // Create a new task for handling the client request
                Runnable clientTask = new ClientHandler(clientSocket);
                // Submit the task to the thread pool
                executorService.submit(clientTask);
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown the executor service when the server stops (though this code runs indefinitely)
            // For a real server, you'd have a way to gracefully shut down.
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

    // Runnable task to handle client requests
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            // Try-with-resources to ensure the socket and streams are closed automatically
            try (Socket socket = this.clientSocket; // effectively final for try-with-resources
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream();
                 PrintWriter pw = new PrintWriter(output, true)) {

                // Read the request (simple read, no parsing)
                byte[] requestBuffer = new byte[2048];
                input.read(requestBuffer, 0, 2048);
                // System.out.println("Received request from: " + socket.getInetAddress()); // Optional: log request

                // Send the HTTP "Hello World!" response
                pw.println("HTTP/1.1 200 OK");
                pw.println("Connection: close");
                pw.println("Content-Type: text/html; charset=UTF-8\r\n"); // Ensure CRLF after headers
                pw.println("Hello World!");

            } catch (Exception e) {
                // Print an error message if an exception occurs during request handling
                System.err.println("Error handling client request: " + e.getMessage());
                // e.printStackTrace(); // Optional: print stack trace for debugging
            }
            // Socket and streams are closed automatically due to try-with-resources
        }
    }
}
