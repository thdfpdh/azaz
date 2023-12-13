package test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Application {
    private static final String SERVER_IP = "192.168.0.27"; // 서버의 IP 주소
    private int server_port;
    private BufferedReader in;
    private PrintWriter out;
    public static void main(String[] args) {
        launch(args);
    }
    public int getServer_port() {
        return server_port;
    }
    public void setServer_port(int server_port) {
        this.server_port = server_port;
    }
    @Override
    public void start(Stage primaryStage) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("접속할 port를 입력하세요: ");
        server_port = Integer.parseInt(br.readLine());
        
        // 이름 입력 받기
        System.out.print("사용할 이름을 입력하세요: ");
        String clientName = br.readLine();
        try {
//            Font.loadFont(getClass().getResourceAsStream("/path/to/korean/font.ttf"), 12);
            Socket socket = new Socket(SERVER_IP, server_port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
//            primaryStage.setTitle("Client 1");
            TextArea chatArea = new TextArea();
            TextField inputField = new TextField();
            inputField.setPromptText("메시지를 입력하세요");
            inputField.setOnAction(e -> {
                String message = clientName + ": " + inputField.getText();
                out.println(message);
                inputField.clear();
            });
            VBox layout = new VBox(10);
            layout.getChildren().addAll(chatArea, inputField);
            Scene scene = new Scene(layout, 300, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
            // 서버에서 오는 메시지를 출력하는 부분
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        final String finalMessage = receivedMessage;
                        Platform.runLater(() -> {
                            // 수신한 메시지를 화면에 출력하거나 필요한 처리 수행
                            chatArea.appendText(finalMessage + "\n");
                        });
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}