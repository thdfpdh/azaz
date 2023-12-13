package com.pcwk.ehr;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {

		try {
			String serverIp = "192.168.0.27";

			Socket socket = new Socket(serverIp, 8877);

			System.out.println("서버에 연결되었습니다.");
			Sender sender = new Sender(socket);
			Receiver receiver = new Receiver(socket);

			sender.start();
			receiver.start();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
