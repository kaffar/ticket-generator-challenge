package com.bingo;

import com.bingo.ticketgenerator.Ticket;
import com.bingo.ticketgenerator.TicketGenerator;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random(System.currentTimeMillis());
        TicketGenerator ticketGenerator = new TicketGenerator(random::nextInt);

        List<Ticket> tickets = ticketGenerator.generateTickets();

        tickets.forEach(t -> System.out.println(t + "\n"));
    }
}
