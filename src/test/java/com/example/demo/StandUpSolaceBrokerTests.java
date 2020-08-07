package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class StandUpSolaceBrokerTests {
    @SuppressWarnings("rawtypes")
    @Container
    public GenericContainer solace = new GenericContainer("solace/solace-pubsub-standard")
            .withEnv("username_admin_globalaccesslevel", "admin")
            .withEnv("username_admin_password", "admin")
            .withSharedMemorySize(2L * 1024L * 1024L * 1024L)
            .withExposedPorts(55555, 8080);

    @Test
    void test() {
        assertTrue(solace.isCreated());
        assertTrue(solace.isRunning());
        assertTrue(solace.getMappedPort(55555) > 0);
        assertTrue(solace.getMappedPort(8080) > 0);
    }

}
