package com.bingo.ticketgenerator;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class Ticket {
    private final Integer[][] ticketMatrix;

    @Override
    public String toString() {
        return formatToString(ticketMatrix[0])
                + "\n" + formatToString(ticketMatrix[1])
                + "\n" + formatToString(ticketMatrix[2]);
    }

    private String formatToString(Integer[] array) {
        return Arrays.toString(Arrays.stream(array).map(e -> e == null ? "__" : String.format("%02d", e)).toArray());
    }

    public Optional<Integer> getElement(int rowIndex, int columnIndex) {
        return Optional.ofNullable(ticketMatrix[rowIndex][columnIndex]);
    }
}

