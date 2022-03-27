package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class History {
    private static PrintWriter out;

    private static String getHistoryFileByLogin(String login) {
        File fileDir = new File("history/" + login);
        fileDir.mkdir();
        return "history/" + login + "/hist_[" + login + "].txt";
    }

    public static void start(String login) {
        try {
            out = new PrintWriter(new FileOutputStream(getHistoryFileByLogin(login), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (out != null) {
            out.close();
        }
    }

    public static void writeLine(String msg) {
        out.println(msg);
    }

    public static String getLast100LinesFromHistory(String login) {
        if (!Files.exists(Paths.get(getHistoryFileByLogin(login)))) {
            return "";
        }
        StringBuilder sb = new StringBuilder("Последние сто сообщений: Начало\n");
        try {
            List<String> historyList = Files.readAllLines(Paths.get(getHistoryFileByLogin(login)));
            int startPosition = 0;
            if (historyList.size() > 100) {
                startPosition = historyList.size() - 100;
            }
            for (int i = startPosition; i < historyList.size(); i++) {
                sb.append(historyList.get(i)).append(System.lineSeparator());
                //sb.append(historyList.get(i)).append("\n");
            }
            sb.append("Последние сто сообщений: Конец\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
