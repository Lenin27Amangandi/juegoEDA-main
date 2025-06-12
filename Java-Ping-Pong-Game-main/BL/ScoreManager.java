package BL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ScoreManager {

    private static final String FILE_PATH = "Java-Ping-Pong-Game-main/scores.csv";
    public static String mensajeCola = null;

    public static void saveScore(String player1, int score1, String player2, int score2) {
        Queue<String> scoreQueue = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scoreQueue.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String result = "Empate";
        if (score1 > score2)
            result = "Ganador: " + player1;
        else if (score2 > score1)
            result = "Ganador: " + player2;

        String newScore = String.format("%s,%d,%s,%d,%s,%s", player1, score1, player2, score2, result, date);
        scoreQueue.add(newScore);

        if (scoreQueue.size() > 10) {
            String eliminado = scoreQueue.poll();
            mensajeCola = "Se eliminó la partida más antigua por límite de 10 (modo cola):\n" + eliminado;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (String s : scoreQueue) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadScores() {
        List<String> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No hay archivo de scores aún.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public static void deleteLastScore() {
        Stack<String> scoreStack = new Stack<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scoreStack.push(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (scoreStack.isEmpty()) {
            System.out.println("No hay partidas para eliminar.");
            return;
        }
        scoreStack.pop();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (String line : scoreStack) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}