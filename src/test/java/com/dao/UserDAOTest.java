package com.dao;

import com.dao.implementation.TeamDAO;
import com.dao.implementation.UserDAO;
import com.domain.Team;
import com.domain.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("jibrax")
            .withUsername("testuser")
            .withPassword("testpwd");

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private UserDAO userDAO;
    private TeamDAO teamDAO;

    private User testUser1;
    private User testUser2;
    private User testUser3;
    private Team testTeam1;
    private Team testTeam2;
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
        entityManager.getTransaction().begin();

        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);

        baseDate = LocalDateTime.of(2023, 12, 15, 10, 30);

        createTestData();
    }


    @AfterEach
    void tearDown() {
        if (entityManager != null) {
            EntityTransaction transaction = entityManager.getTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
        }
    }

    private void createTestData() {
        testTeam1 = new Team();
        testTeam1.setUsername("Development Team");

        testTeam2 = new Team();
        testTeam2.setUsername("Management Team");

        testUser1 = new User();
        testUser1.setEmail("john.doe@example.com");
        testUser1.setFirstname("John");
        testUser1.setLastname("Doe");
        testUser1.setLastLogin(baseDate);
        testUser1.setTeam(testTeam1);
        testUser1.setLeadingTeam(testTeam1);
        testTeam1.setTeamLeader(testUser1);

        testUser2 = new User();
        testUser2.setEmail("jane.smith@example.com");
        testUser2.setFirstname("Jane");
        testUser2.setLastname("Smith");
        testUser2.setLastLogin(baseDate.plusDays(2));
        testUser2.setTeam(testTeam1);

        testUser3 = new User();
        testUser3.setEmail("bob.wilson@example.com");
        testUser3.setFirstname("Bob");
        testUser3.setLastname("Wilson");
        testUser3.setLastLogin(baseDate.minusDays(1));
        testUser3.setTeam(testTeam2);
        testUser3.setLeadingTeam(testTeam2);
        testTeam2.setTeamLeader(testUser3);

        teamDAO.save(testTeam1);
        teamDAO.save(testTeam2);
        userDAO.save(testUser1);
        userDAO.save(testUser2);
        userDAO.save(testUser3);
    }

    @Test
    @Order(1)
    void testFindByEmail() {
        String email = "john.doe@example.com";

        List<User> result = userDAO.findByEmail(email);

        assertEquals(1, result.size());
        assertEquals(email, result.getFirst().getEmail());
        assertEquals("John", result.getFirst().getFirstname());
        assertEquals("Doe", result.getFirst().getLastname());
    }

    @Test
    @Order(2)
    void testFindByEmail_NotFound() {
        String email = "nonexistent@example.com";

        List<User> result = userDAO.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    @Order(3)
    void testFindByFirstName() {
        String firstname = "Jane";

        List<User> result = userDAO.findByFirstName(firstname);

        assertEquals(1, result.size());
        assertEquals("Jane", result.getFirst().getFirstname());
        assertEquals("Smith", result.getFirst().getLastname());
    }

    @Test
    @Order(4)
    void testFindByLastName() {
        String lastname = "Wilson";

        List<User> result = userDAO.findByLastName(lastname);

        assertEquals(1, result.size());
        assertEquals("Bob", result.getFirst().getFirstname());
        assertEquals("Wilson", result.getFirst().getLastname());
    }

    @Test
    @Order(5)
    void testFindByFirstNameAndLastName() {
        String firstname = "John";
        String lastname = "Doe";

        List<User> result = userDAO.findByFirstNameAndLastName(firstname, lastname);

        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstname());
        assertEquals("Doe", result.getFirst().getLastname());
    }

    @Test
    @Order(6)
    void testFindByLogInBefore() {
        LocalDateTime searchDate = baseDate.plusDays(1);

        List<User> result = userDAO.findByLogInBefore(searchDate);

        assertEquals(1, result.size());
        assertEquals("Jane", result.getFirst().getFirstname());
    }

    @Test
    @Order(7)
    void testFindByLogInAfter() {
        LocalDateTime searchDate = baseDate.plusDays(1);

        List<User> result = userDAO.findByLogInAfter(searchDate);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(user -> "John".equals(user.getFirstname())));
        assertTrue(result.stream().anyMatch(user -> "Bob".equals(user.getFirstname())));
    }

    @Test
    @Order(8)
    void testFindByLogInBetween() {
        LocalDateTime start = baseDate.minusHours(1);
        LocalDateTime end = baseDate.plusDays(1);

        List<User> result = userDAO.findByLogInBetween(start, end);

        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstname());
    }

    @Test
    @Order(9)
    void testFindByTeam() {
        List<User> result = userDAO.findByTeam(testTeam1);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(user -> "John".equals(user.getFirstname())));
        assertTrue(result.stream().anyMatch(user -> "Jane".equals(user.getFirstname())));
    }

    @Test
    @Order(10)
    void testFindByLeadingTeam() {
        User result = userDAO.findLeaderByTeam(testTeam2);

        assertNotNull(result);
        assertEquals("Bob", result.getFirstname());
        assertEquals(testTeam2.getAssigneeId(), result.getLeadingTeam().getAssigneeId());
    }

    @Test
    @Order(11)
    void testFindByTeam_EmptyResult() {
        EntityTransaction transaction = entityManager.getTransaction();

        Team emptyTeam = new Team();
        emptyTeam.setUsername("Empty Team");
        entityManager.persist(emptyTeam);
        transaction.commit();

        List<User> result = userDAO.findByTeam(emptyTeam);

        assertTrue(result.isEmpty());
    }

    @Test
    @Order(12)
    void testDatabase_Connection() {
        assertTrue(postgres.isRunning());
        assertNotNull(entityManager);
        assertTrue(entityManager.isOpen());
    }

    @Test
    @Order(13)
    void testTransaction_Rollback() {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            User newUser = new User();
            newUser.setEmail("test@rollback.com");
            newUser.setFirstname("Test");
            newUser.setLastname("Rollback");
            entityManager.persist(newUser);

            assertNotNull(newUser.getAssigneeId());

            transaction.rollback();

            List<User> result = userDAO.findByEmail("test@rollback.com");
            assertTrue(result.isEmpty());

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
