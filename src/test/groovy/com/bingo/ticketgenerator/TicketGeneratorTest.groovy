package com.bingo.ticketgenerator

import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Collectors


class TicketGeneratorTest extends Specification {
    private Random random = new Random(System.currentTimeMillis())
    @Subject
    private def ticketGenerator = new TicketGenerator(random::nextInt)

    def "Should generate valid tickets"() {
        when:
        List<Ticket> tickets = ticketGenerator.generateTickets()
        then: "Each ticket has number in every column + ech row has exactly 5 numbers"
        tickets.forEach(t -> validateCountOfNumbers(t))
        and: "All numbers from 1 to 90 occur across all 6 tickets"
        getSetOfAllNumbers(tickets).size() == 90
        and: "Numbers in each column of tickets are sorted"
        tickets.forEach(t -> validateOrderOfNumbersInColumns(t))
        and: "Values in each column of tickets have proper scope"
        tickets.forEach(t -> validateScopeOfValuesInColumns(t))
    }

    private static void validateCountOfNumbers(Ticket ticket) {
        boolean[] ticketColumnsWithValues = new boolean[9];
        for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
            int counterPerRow = 0
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                if (ticket.getElement(rowIndex, columnIndex).isPresent()) {
                    counterPerRow++
                    ticketColumnsWithValues[columnIndex] = true
                }
            }
            assert counterPerRow == 5
        }
        assert !ticketColumnsWithValues.contains(false)
    }

    private static Set<Integer> getSetOfAllNumbers(List<Ticket> tickets) {
        Set<Integer> allNumbersFromTickets = new HashSet<>();
        for (int ticketIndex = 0; ticketIndex < 6; ticketIndex++) {
            for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
                for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                    tickets.get(ticketIndex).getElement(rowIndex, columnIndex)
                            .ifPresent(number -> allNumbersFromTickets.add(number))
                }
            }
        }
        return allNumbersFromTickets;
    }

    private static void validateOrderOfNumbersInColumns(Ticket ticket) {
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            List<Integer> numbersInColumn = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
                ticket.getElement(rowIndex, columnIndex)
                        .ifPresent(n -> numbersInColumn.add(n))
            }
            assert numbersInColumn.stream().sorted().collect(Collectors.toList()).equals(numbersInColumn)
        }
    }

    private static void validateScopeOfValuesInColumns(Ticket ticket) {
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            List<Integer> numbersInColumn = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
                ticket.getElement(rowIndex, columnIndex)
                        .ifPresent(number -> {
                            if (columnIndex == 0) {
                                assert number <= 9
                            } else if (columnIndex < 8) {
                                assert number.intdiv(10) == columnIndex
                            } else {
                                assert number <= 90
                            }
                        })
            }
        }
    }
}
