package com.bingo.ticketgenerator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toUnmodifiableList;

public class TicketGenerator {
    private static final int DEFAULT_NUMBER_OF_SWAPS_BETWEEN_TICKETS = 100;

    private final RandomNumberProvider randomNumberProvider;
    private final int numberOfSwapsBetweenTickets;

    public TicketGenerator(RandomNumberProvider randomNumberProvider) {
        this(randomNumberProvider, DEFAULT_NUMBER_OF_SWAPS_BETWEEN_TICKETS);
    }

    public TicketGenerator(RandomNumberProvider randomNumberProvider, int numberOfSwapsBetweenTickets) {
        this.randomNumberProvider = randomNumberProvider;
        this.numberOfSwapsBetweenTickets = numberOfSwapsBetweenTickets;
    }

    public List<Ticket> generateTickets() {
        List<TicketBuilder> ticketBuilders = getInitiallyPopulatedTicketBuilders();

        swapElementsInTicketBuilders(ticketBuilders);

        Map<NumberGroup, List<Integer>> numbersToPropagate = Arrays.stream(NumberGroup.values())
                .collect(Collectors.toMap(Function.identity(), ng -> {
                    ArrayList<Integer> numbers = new ArrayList<>(ng.getNumbers());
                    Collections.shuffle(numbers);
                    return numbers;
                }));

        return ticketBuilders.stream()
                .map(tb -> tb.build(numbersToPropagate))
                .collect(Collectors.toList());
    }

    private void swapElementsInTicketBuilders(List<TicketBuilder> ticketBuilders) {
        for (int i = 0; i < numberOfSwapsBetweenTickets; i++) {
            int randomNumber = randomNumberProvider.nextInt(90);
            int firstTicketToSwap = randomNumber / 15;
            int firstRowToSwap = (randomNumber / 5) % 3;
            int firstColumnToSwap = (randomNumber / 3) % 5;

            randomNumber = randomNumberProvider.nextInt(90);
            int secondTicketToSwap = randomNumber / 15;
            int secondRowToSwap = (randomNumber / 5) % 3;
            int secondColumnToSwap = (randomNumber / 3) % 5;

            if (ticketElementsCanBeSwapped(ticketBuilders, firstTicketToSwap, firstRowToSwap, firstColumnToSwap, secondTicketToSwap, secondRowToSwap, secondColumnToSwap)) {
                swapTicketElements(ticketBuilders, firstTicketToSwap, firstRowToSwap, firstColumnToSwap, secondTicketToSwap, secondRowToSwap, secondColumnToSwap);
            }
        }
    }

    private void swapTicketElements(List<TicketBuilder> ticketBuilders, int firstTicketToSwap, int firstRowToSwap, int firstColumnToSwap, int secondTicketToSwap, int secondRowToSwap, int secondColumnToSwap) {
        NumberGroup tmp = ticketBuilders.get(firstTicketToSwap).getElement(firstRowToSwap, firstColumnToSwap);
        ticketBuilders.get(firstTicketToSwap).swapElements(firstRowToSwap, firstColumnToSwap, ticketBuilders.get(secondTicketToSwap).getElement(secondRowToSwap, secondColumnToSwap));
        ticketBuilders.get(secondTicketToSwap).swapElements(secondRowToSwap, secondColumnToSwap, tmp);
    }

    private boolean ticketElementsCanBeSwapped(List<TicketBuilder> ticketBuilders, int firstTicket, int firstRow, int firstColumn, int secondTicket, int secondRow, int secondColumn) {
        return ticketBuilders.get(firstTicket).canSwap(firstRow, firstColumn, ticketBuilders.get(secondTicket).getElement(secondRow, secondColumn))
                && ticketBuilders.get(secondTicket).canSwap(secondRow, secondColumn, ticketBuilders.get(firstTicket).getElement(firstRow, firstColumn));
    }

    private List<TicketBuilder> getInitiallyPopulatedTicketBuilders() {
        return List.of(new TicketBuilder()
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH, NumberGroup.FOURTH))
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.NINTH)),
                new TicketBuilder()
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH, NumberGroup.FOURTH))
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.NINTH)),
                new TicketBuilder()
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.THIRD, NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH)),
                new TicketBuilder()
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.THIRD, NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH)),
                new TicketBuilder()
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SECOND, NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH)),
                new TicketBuilder()
                        .addRow(List.of(NumberGroup.FIRST, NumberGroup.SECOND, NumberGroup.THIRD, NumberGroup.FOURTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH, NumberGroup.FIFTH))
                        .addRow(List.of(NumberGroup.SECOND, NumberGroup.SIXTH, NumberGroup.SEVENTH, NumberGroup.EIGHTH, NumberGroup.NINTH))
        );
    }

    @RequiredArgsConstructor
    private enum NumberGroup {
        FIRST(IntStream.rangeClosed(1, 9).boxed().collect(toUnmodifiableList())),
        SECOND(IntStream.rangeClosed(10, 19).boxed().collect(toUnmodifiableList())),
        THIRD(IntStream.rangeClosed(20, 29).boxed().collect(toUnmodifiableList())),
        FOURTH(IntStream.rangeClosed(30, 39).boxed().collect(toUnmodifiableList())),
        FIFTH(IntStream.rangeClosed(40, 49).boxed().collect(toUnmodifiableList())),
        SIXTH(IntStream.rangeClosed(50, 59).boxed().collect(toUnmodifiableList())),
        SEVENTH(IntStream.rangeClosed(60, 69).boxed().collect(toUnmodifiableList())),
        EIGHTH(IntStream.rangeClosed(70, 79).boxed().collect(toUnmodifiableList())),
        NINTH(IntStream.rangeClosed(80, 90).boxed().collect(toUnmodifiableList()));

        @Getter
        private final List<Integer> numbers;
    }

    private class TicketBuilder {
        private final List<List<NumberGroup>> rows = new ArrayList<>();

        public TicketBuilder addRow(List<NumberGroup> row) {
            rows.add(new ArrayList<>(row));
            return this;
        }

        public boolean canSwap(int rowIndex, int elementIndex, NumberGroup numberGroup) {
            List<NumberGroup> newRow = new ArrayList<>(rows.get(rowIndex));
            newRow.set(elementIndex, numberGroup);

            return isValidAfterReplacement(rowIndex, newRow);
        }

        public void swapElements(int rowIndex, int elementIndex, NumberGroup numberGroup) {
            rows.get(rowIndex).set(elementIndex, numberGroup);
        }

        public NumberGroup getElement(int rowIndex, int elementIndex) {
            return rows.get(rowIndex).get(elementIndex);
        }

        public Ticket build(Map<NumberGroup, List<Integer>> numbersToPropagate) {
            Integer[][] ticketMatrix = new Integer[3][9];
            for (int i = 0; i < 3; i++) {
                List<NumberGroup> row = rows.get(i);
                for (NumberGroup numberGroup : row) {
                    int number = numbersToPropagate.get(numberGroup).size() > 0 ?
                            numbersToPropagate.get(numberGroup).remove(randomNumberProvider.nextInt(numbersToPropagate.get(numberGroup).size()))
                            :
                            numbersToPropagate.get(numberGroup).remove(0);
                    ticketMatrix[i][numberGroup.ordinal()] = number;
                }
            }
            sortColumns(ticketMatrix);
            return new Ticket(ticketMatrix);
        }

        private void sortColumns(Integer[][] rows) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                boolean swapHappened = false;
                if (shouldSwapElements(rows, columnIndex, 0, 1)) {
                    swapElements(rows, columnIndex, 0, 1);
                    swapHappened = true;
                }
                if (shouldSwapElements(rows, columnIndex, 1, 2)) {
                    swapElements(rows, columnIndex, 1, 2);
                    swapHappened = true;
                }

                if (swapHappened && shouldSwapElements(rows, columnIndex, 0, 1)) {
                    swapElements(rows, columnIndex, 0, 1);
                }

                if (shouldSwapElements(rows, columnIndex, 0, 2)) {
                    swapElements(rows, columnIndex, 0, 2);
                }
            }
        }

        private boolean shouldSwapElements(Integer[][] rows, int columnIndex, int rowIndex1, int rowIndex2) {
            return rows[rowIndex1][columnIndex] != null && rows[rowIndex2][columnIndex] != null && rows[rowIndex1][columnIndex] > rows[rowIndex2][columnIndex];
        }

        private void swapElements(Integer[][] rows, int column, int row1, int row2) {
            int temp = rows[row1][column];
            rows[row1][column] = rows[row2][column];
            rows[row2][column] = temp;
        }

        private boolean isValidAfterReplacement(int rowIndex, List<NumberGroup> newRow) {
            HashSet<NumberGroup> ticketNumberGroups = new HashSet<>(newRow);
            if (ticketNumberGroups.size() < 5) {
                return false;
            }
            ticketNumberGroups.addAll(rows.get((rowIndex + 1) % 3));
            ticketNumberGroups.addAll(rows.get((rowIndex + 2) % 3));
            return ticketNumberGroups.size() >= 9;
        }
    }
}
