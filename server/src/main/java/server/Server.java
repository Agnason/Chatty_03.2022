package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    // lesson06 //
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    // lesson06 //
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        //     authService = new SimpleAuthService();
        //lesson02//
        if (!SQLHandler.connect()) {
            throw new RuntimeException("Не удалось подключить к БД");
        }
        authService = new DBAuthService();
        //lesson02//
        try {
            server = new ServerSocket(PORT);
            // lesson06 //
            // System.out.println("Server started");
            logger.info("Server started");
            // lesson06 //
            while (true) {
                socket = server.accept();
                // lesson06 //
                //System.out.println("Client connected");
                logger.info("Client connected " + socket.getRemoteSocketAddress());
                // lesson06 //
                new ClientHandler(this, socket);

            }

        } catch (IOException e) {
            e.printStackTrace();
            // lesson06 //
            logger.log(Level.SEVERE, e.getMessage(), e);
            // lesson06 //
        } finally {
            SQLHandler.disconnect();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                // lesson06 //
                logger.log(Level.SEVERE, e.getMessage(), e);
                // lesson06 //
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
                // lesson06 //
                logger.log(Level.SEVERE, e.getMessage(), e);
                // lesson06 //
            }
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg) {
        String message = String.format("[%s]: %s", sender.getNickname(), msg);

        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) {
        String message = String.format("[%s] to [%s]: %s", sender.getNickname(), receiver, msg);

        for (ClientHandler c : clients) {
            if (c.getNickname().equals(receiver)) {
                c.sendMsg(message);
                if (!sender.getNickname().equals(receiver)) {
                    sender.sendMsg(message);
                }
                return;
            }
        }

        sender.sendMsg("not found user: " + receiver);
    }

    public boolean isLoginAuthenticated(String login) {
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientlist");

        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getNickname());
        }

        String msg = sb.toString();

        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }
}
