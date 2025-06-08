package GUI;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel panel;
    JButton endGameButton;

    public GameFrame() {
        panel = new GamePanel(this);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

        endGameButton = new JButton("Terminar Juego");
        endGameButton.setFocusable(false);
        endGameButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres terminar la partida?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                panel.endGameManually();
            }
        });

        this.add(endGameButton, BorderLayout.SOUTH);
        this.setTitle("--Ping Pong Game--");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void setPlayerNames(String player1, String player2) {
        panel.setPlayerNames(player1, player2);
    }

    public void closeGameAndShowMenu() {
        this.dispose();
        MenuFrame menu = new MenuFrame();
        menu.setVisible(true); 
    }
    
}
