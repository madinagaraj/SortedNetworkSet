package com.NetObjex.DTO;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputSequence {

    private int argumentsCount;
    private int operationId;
    private List<Integer> arguments;

    public InputSequence(DataInputStream inputStream) throws IOException {
        argumentsCount = inputStream.readInt();
        operationId = inputStream.readInt();
        arguments = setInputArguments(inputStream, argumentsCount - 1);
    }

    private List<Integer> setInputArguments(DataInputStream inputStream, int noOfArguments) throws IOException {
        List<Integer> arguments = new ArrayList<>(noOfArguments);

        for (int i = 0; i < noOfArguments; i++) {
            int argument = inputStream.readInt();
            arguments.add(argument);
        }
        return arguments;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    int getOperationId() {
        return operationId;
    }

    List<Integer> getArguments() {
        return arguments;
    }

}
