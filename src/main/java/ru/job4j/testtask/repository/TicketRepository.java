package ru.job4j.testtask.repository;

import ru.job4j.testtask.Ticket;

import java.util.List;

public interface TicketRepository {
    List<Ticket> findAll();

    List<Ticket> findByRoute(String origin, String destination);
}
