package bluepaledot.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {

    private JTextArea chatArea;
    private JTextField chatInput;
    private JButton sendButton;
    private GameClient gameClient;

    public ChatPanel(GameClient gameClient) {
        this.gameClient = gameClient;
        initChatPanel();
    }

    private void initChatPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(200, Constants.BOARD_HEIGHT));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        chatInput = new JTextField();
        sendButton = new JButton("Enter");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputPanel.add(chatInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            gameClient.send("CHAT " + gameClient.getName() + ": " + message);
            chatInput.setText("");
        }
    }

    public void addMessage(String message) {
        chatArea.append(message + "\n");
    }
}
