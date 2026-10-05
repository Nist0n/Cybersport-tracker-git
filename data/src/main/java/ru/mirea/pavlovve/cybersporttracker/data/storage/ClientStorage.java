package ru.mirea.pavlovve.cybersporttracker.data.storage;

import ru.mirea.pavlovve.cybersporttracker.data.storage.model.ClientInfo;

public interface ClientStorage {

    ClientInfo get();

    boolean save(ClientInfo info);

    boolean clear();
}
