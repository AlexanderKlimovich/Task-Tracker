package com.klymovych.tasktracker.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
class ToDoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testDatabaseConnection() {
        String query = "SELECT 1";
        Integer result = (Integer) entityManager.getEntityManager()
                .createNativeQuery(query)
                .getSingleResult();

        Assertions.assertNotNull(result);
    }

}