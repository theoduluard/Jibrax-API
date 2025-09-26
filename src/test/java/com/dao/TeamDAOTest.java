package com.dao;

import com.dao.implementation.TeamDAO;
import com.dao.implementation.UserDAO;
import com.domain.Team;
import com.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeamDAOTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("jibrax")
            .withUsername("testuser")
            .withPassword("testpwd");

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private UserDAO userDAO;
    private TeamDAO teamDAO;

    private User johnDoe;
    private User janeSmith;
    private User bobWilson;
    private Team team;
    private LocalDateTime baseDate;


    @BeforeAll
    static void initContainer() {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        properties.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        properties.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");

        entityManagerFactory = Persistence.createEntityManagerFactory("postgresql-junit", properties);
    }

    @AfterAll
    static void closeContainer() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();

        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);

        teamDAO = new TeamDAO();
        teamDAO.setEntityManager(entityManager);

        baseDate = LocalDateTime.of(2023, 12, 15, 10, 30);

        createTestData();
    }


    @AfterEach
    void tearDown() {
        userDAO.findAll().forEach(userRes -> userDAO.delete(userRes));
        teamDAO.findAll().forEach(teamRes -> teamDAO.delete(teamRes));
        entityManager.close();
    }

    private void createTestData() {
        LocalDateTime now = LocalDateTime.now();

        team = new Team();
        team.setUsername("Team");
        team.setActive(true);

        johnDoe = new User();
        johnDoe.setEmail("john.doe@example.com");
        johnDoe.setUsername("jdoe");
        johnDoe.setFirstname("John");
        johnDoe.setLastname("Doe");
        johnDoe.setActive(true);
        johnDoe.setLastLogin(baseDate);
        johnDoe.setCreatedAt(now.minusDays(10));
        johnDoe.setUpdatedAt(now.minusDays(5));

        janeSmith = new User();
        janeSmith.setEmail("jane.smith@example.com");
        janeSmith.setUsername("jsmith");
        janeSmith.setFirstname("Jane");
        janeSmith.setLastname("Smith");
        janeSmith.setActive(false);
        janeSmith.setLastLogin(baseDate.plusDays(2));
        janeSmith.setCreatedAt(now.minusDays(3));
        janeSmith.setUpdatedAt(now.minusDays(2));

        bobWilson = new User();
        bobWilson.setEmail("bob.wilson@example.com");
        bobWilson.setUsername("bWilson");
        bobWilson.setFirstname("Bob");
        bobWilson.setLastname("Wilson");
        bobWilson.setActive(true);
        bobWilson.setLastLogin(baseDate.minusDays(1));

        userDAO.save(johnDoe);
        userDAO.save(janeSmith);
        userDAO.save(bobWilson);

        teamDAO.save(team);

        johnDoe.setTeam(team);
        userDAO.update(johnDoe);

        janeSmith.setTeam(team);
        userDAO.update(janeSmith);

        bobWilson.setTeam(team);
        userDAO.update(bobWilson);
    }

    @Test
    void testFindTeamMembers(){
        Team teamRes = teamDAO.findByMember(bobWilson);
        assertNotNull(teamRes);
        assertEquals(teamRes.getUsername(), team.getUsername());

        teamRes = teamDAO.findByMember(janeSmith);
        assertNotNull(teamRes);
        assertEquals(teamRes.getUsername(), team.getUsername());
    }
}