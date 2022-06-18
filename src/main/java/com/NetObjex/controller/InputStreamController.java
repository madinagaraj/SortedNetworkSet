package com.NetObjex.controller;

import static com.NetObjex.constants.Constants.EXIT_CODE;

import com.NetObjex.DTO.InputSequence;
import com.NetObjex.DTO.ResponseSequence;
import com.NetObjex.exception.CommonException;
import com.NetObjex.server.SortedSet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class InputStreamController implements Runnable {

    private final SortedSet sortedSet;
    private final Socket socket;

    public InputStreamController(SortedSet sortedSet, Socket socket) {
        this.sortedSet = sortedSet;
        this.socket = socket;
    }

    private void generateResponseSequence(ResponseSequence response, DataOutputStream output) throws IOException {
        output.writeInt(response.getArgumentCount());
        List<Integer> arguments = response.getArguments();
        arguments.forEach(argument -> {
            try {
                output.writeInt(argument);
            } catch (IOException e) {
                throw new CommonException("Failed to write Response sequence.", e);
            }
        });

        output.flush();
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            processInputSequence(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processInputSequence(DataInputStream input, DataOutputStream output) throws IOException {
        boolean shutdown = false;

        while (!shutdown) {
            InputSequence request = new InputSequence(input);
            ResponseSequence response = ResponseSequence.process(request, sortedSet);
            System.out.println(response);
            if (response.getArgumentCount() == EXIT_CODE) {
                shutdown = true;
            } else {
                generateResponseSequence(response, output);
            }
        }
    }


}
