package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {
    public static void main(String[] args) {
        // lesson04 //
        ExecutorService service= Executors.newFixedThreadPool(4);
        service.execute(() -> new Server());
        service.shutdown();
        // lesson04 //
    }
}
