package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;

public class StartServer {
    public static void main(String[] args) {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.readConfiguration(new FileInputStream("logging.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Server();
    }
}
