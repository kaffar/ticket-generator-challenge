package com.bingo.ticketgenerator;

@FunctionalInterface
public interface RandomNumberProvider {
    int nextInt(int bound);
}
