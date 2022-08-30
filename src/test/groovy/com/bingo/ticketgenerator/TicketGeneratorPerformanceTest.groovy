package com.bingo.ticketgenerator

import spock.lang.Specification

import java.util.stream.IntStream


class TicketGeneratorPerformanceTest extends Specification {
    private Random random = new Random(System.currentTimeMillis())

    def "Measure time of generating 10k strips of tickets"() {
        expect:
        int numberOfSwapsBetweenTickets = 100;
        def ticketGenerator = new TicketGenerator(random::nextInt, 100)
        long startTime = System.currentTimeMillis()

        IntStream.rangeClosed(1, 10000)
                .forEach(i -> ticketGenerator.generateTickets())

        long timeInMillis = System.currentTimeMillis() - startTime
        println "Time of generating 10k strips of tickets: $timeInMillis ms"
    }
}
