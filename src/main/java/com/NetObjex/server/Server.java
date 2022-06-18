package com.NetObjex.server;

import static com.NetObjex.constants.Constants.SERVER_PORT;

import com.NetObjex.controller.InputStreamController;
import com.NetObjex.exception.CommonException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {

    private static final int POOL_SIZE = getProcessorCount();
    private Socket socket;
    private ServerSocket serverSocket;
    private SortedSet sortedSet;
    private boolean shutdown;

    private Server() {
        sortedSet = new SynchronizedSortedSet();
    }

    private static int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void main(String[] args) {
        Server server = new Server();
        Thread mainThread = new Thread(server);
        mainThread.start();
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
        startListening(executorService);
    }

    private void startListening(ExecutorService executorService) {
        try {
            listen(executorService);
        } catch (IOException e) {
            executorService.shutdown();
            throw new CommonException("Failed to create a server socket.", e);
        }
    }

    private void listen(ExecutorService executorService) throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("SERVER: Started listening on: " + SERVER_PORT);
        while (!shutdown) {
            processRequest(executorService);
        }
    }

    private void processRequest(ExecutorService executorService) throws IOException {
        Socket clientSocket = serverSocket.accept();
        InputStreamController streamController = new InputStreamController(sortedSet, clientSocket);
        executorService.execute(streamController);
    }
}
