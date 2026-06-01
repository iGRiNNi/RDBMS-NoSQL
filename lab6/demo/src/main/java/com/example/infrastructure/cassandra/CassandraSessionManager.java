package com.example.infrastructure.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public final class CassandraSessionManager {

    private static final CassandraConfig CONFIG = CassandraConfig.load();

    private static CqlSession session;

    private CassandraSessionManager() {
    }

    public static synchronized CqlSession getSession() {
        if (session == null || session.isClosed()) {
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(
                            CONFIG.getContactPoint(),
                            CONFIG.getPort()
                    ))
                    .withLocalDatacenter(CONFIG.getLocalDatacenter())
                    .withKeyspace(CqlIdentifier.fromCql(CONFIG.getKeyspace()))
                    .build();
        }

        return session;
    }

    public static synchronized void close() {
        if (session != null && !session.isClosed()) {
            session.close();
        }
    }
}