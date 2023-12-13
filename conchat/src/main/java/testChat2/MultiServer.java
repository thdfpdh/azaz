package testChat2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiServer {
    private static final int BASE_PORT = 12370;
    private static final int NUM_SERVERS = 3;
    private static List<ServerInstance> servers = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < NUM_SERVERS; i++) {
            int port = BASE_PORT + i;
            ServerInstance serverInstance = new ServerInstance(port);
            servers.add(serverInstance);
            new Thread(serverInstance).start();
        }
    }

    private static class ServerInstance implements Runnable {
        private int port;
        private List<ClientPair> clientPairs = new ArrayList<>();

        public ServerInstance(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("서버가 " + port + " 포트에서 시작되었습니다.");

                while (true) {
                    Socket client1 = serverSocket.accept();
                    Socket client2 = serverSocket.accept();

                    System.out.println("클라이언트 1이 연결되었습니다.");
                    System.out.println("클라이언트 2가 연결되었습니다.");

                    // 클라이언트 페어 생성 및 실행
                    ClientPair clientPair = new ClientPair(client1, client2);
                    clientPairs.add(clientPair);
                    new Thread(clientPair).start();

                    // 클라이언트 처리를 위한 핸들러 생성 및 실행
                    ClientHandler clientHandler1 = new ClientHandler(client1, clientPairs);
                    ClientHandler clientHandler2 = new ClientHandler(client2, clientPairs);
                    new Thread(clientHandler1).start();
                    new Thread(clientHandler2).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private List<ClientPair> clientPairs;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket, List<ClientPair> clientPairs) {
            this.clientSocket = socket;
            this.clientPairs = clientPairs;
            try {
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // 클라이언트로부터 메시지 수신 및 브로드캐스트
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("클라이언트로부터 수신: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 클라이언트 연결 종료 시 핸들러에서 제거
                clientPairs.removeIf(pair -> pair.contains(clientSocket));
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 모든 클라이언트에게 메시지 브로드캐스트
        private void broadcast(String message) {
            for (ClientPair pair : clientPairs) {
                if (pair.contains(clientSocket)) {
                    Socket otherSocket = pair.getOtherSocket(clientSocket);
                    if (otherSocket != null && otherSocket.isConnected()) {
                        PrintWriter otherOut = null;
                        try {
                            otherOut = new PrintWriter(otherSocket.getOutputStream(), true);
                            otherOut.println(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (otherOut != null) {
                                otherOut.close();
                            }
                        }
                    }
                }
            }
        }
    }

    private static class ClientPair implements Runnable {
        private Socket client1;
        private Socket client2;

        public ClientPair(Socket client1, Socket client2) {
            this.client1 = client1;
            this.client2 = client2;
        }

        public boolean contains(Socket clientSocket) {
            return client1.equals(clientSocket) || client2.equals(clientSocket);
        }

        public Socket getOtherSocket(Socket clientSocket) {
            if (client1.equals(clientSocket)) {
                return client2;
            } else if (client2.equals(clientSocket)) {
                return client1;
            }
            return null;
        }

        @Override
        public void run() {
            // 클라이언트 간의 통신 또는 다른 작업 수행
        }
    }
}
