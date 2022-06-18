package com.NetObjex.Test;

import static com.NetObjex.constants.Constants.SERVER_PORT;
import static com.NetObjex.constants.Constants.HOST;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;


public class TestClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, SERVER_PORT);

        // Insertion
        DataOutputStream serverWriteStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverReadStream = new DataInputStream(socket.getInputStream());

        sendInputStreamToServer(serverWriteStream, 4, 1, 1, 1, 15);

        // Get Size
        sendInputStreamToServer(serverWriteStream, 2, 3, 1);


       sendInputStreamToServer(serverWriteStream, 4, 1, 1, 2, 30);

        // Get Size
        sendInputStreamToServer(serverWriteStream, 2, 3, 1);

      // Get Key Value
        sendInputStreamToServer(serverWriteStream, 3, 4, 1, 1);
        sendInputStreamToServer(serverWriteStream, 3, 4, 2, 2);

        // Range
        sendInputStreamToServer(serverWriteStream, 6, 6, 1, 2, 3, 12, 32);

        // Removal
        sendInputStreamToServer(serverWriteStream, 3, 2, 1, 1);

        // Disconnect
        sendInputStreamToServer(serverWriteStream, 1, 5);

        while (true) {
            System.out.print(serverReadStream.readInt() + " ");
        }

    }

    private static void sendInputStreamToServer(DataOutputStream outputStream, int... values) {
        Arrays.stream(values).forEach(value -> {
            try {
                outputStream.writeInt(value);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to socket", e);
            }
        });
    }

}
