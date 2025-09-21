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

    private User johnDoe;
    private User janeSmith;
    private User bobWilson;
    private Team devTeam;
    private Team managementTeam;
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
        userDAO.findAll().forEach(user -> userDAO.delete(user));
        teamDAO.findAll().forEach(team -> teamDAO.delete(team));
        entityManager.close();
    }

    private void createTestData() {
        LocalDateTime now = LocalDateTime.now();

        devTeam = new Team();
        devTeam.setUsername("Development Team");
        devTeam.setActive(true);

        managementTeam = new Team();
        managementTeam.setUsername("Management Team");
        managementTeam.setActive(true);

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

        managementTeam.setTeamLeader(bobWilson);
        devTeam.setTeamLeader(johnDoe);

        teamDAO.save(devTeam);
        teamDAO.save(managementTeam);

        johnDoe.setTeam(devTeam);
        johnDoe.setLeadingTeam(devTeam);
        userDAO.update(johnDoe);

        janeSmith.setTeam(devTeam);
        userDAO.update(janeSmith);

        bobWilson.setTeam(managementTeam);
        bobWilson.setLeadingTeam(managementTeam);
        userDAO.update(bobWilson);
    }

    @Test
    void testFindByEmail() {
        List<User> result = userDAO.findByEmail(johnDoe.getEmail());

        assertEquals(1, result.size());
        assertEquals(johnDoe.getEmail(), result.getFirst().getEmail());
        assertEquals(johnDoe.getFirstname(), result.getFirst().getFirstname());
        assertEquals(johnDoe.getLastname(), result.getFirst().getLastname());
    }

    @Test
    void testFindByEmail_NotFound() {
        String email = "nonexistent@example.com";

        List<User> result = userDAO.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByFirstName() {
        List<User> result = userDAO.findByFirstName(janeSmith.getFirstname());

        assertEquals(1, result.size());
        assertEquals(janeSmith.getFirstname(), result.getFirst().getFirstname());
        assertEquals(janeSmith.getLastname(), result.getFirst().getLastname());
    }

    @Test
    void testFindByLastName() {
        List<User> result = userDAO.findByLastName(bobWilson.getLastname());

        assertEquals(1, result.size());
        assertEquals(bobWilson.getFirstname(), result.getFirst().getFirstname());
        assertEquals(bobWilson.getLastname(), result.getFirst().getLastname());
    }

    @Test
    void testFindByFirstNameAndLastName() {
        List<User> result = userDAO.findByFirstNameAndLastName(johnDoe.getFirstname(), johnDoe.getLastname());

        assertEquals(1, result.size());
        assertEquals(johnDoe.getFirstname(), result.getFirst().getFirstname());
        assertEquals(johnDoe.getLastname(), result.getFirst().getLastname());
    }

    @Test
    void testFindByLogInBefore() {
        LocalDateTime searchDate = baseDate.plusDays(1);

        List<User> result = userDAO.findByLogInBefore(searchDate);

        assertEquals(1, result.size());
        assertEquals(janeSmith.getFirstname(), result.getFirst().getFirstname());
    }

    @Test
    void testFindByLogInAfter() {
        LocalDateTime searchDate = baseDate.plusDays(1);

        List<User> result = userDAO.findByLogInAfter(searchDate);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(user -> johnDoe.getFirstname().equals(user.getFirstname())));
        assertTrue(result.stream().anyMatch(user -> bobWilson.getFirstname().equals(user.getFirstname())));
    }

    @Test
    void testFindByLogInBetween() {
        LocalDateTime start = baseDate.minusHours(1);
        LocalDateTime end = baseDate.plusDays(1);

        List<User> result = userDAO.findByLogInBetween(start, end);

        assertEquals(1, result.size());
        assertEquals(johnDoe.getFirstname(), result.getFirst().getFirstname());
    }

    @Test
    void testFindByTeam() {
        List<User> result = userDAO.findByTeam(devTeam);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(user -> johnDoe.getFirstname().equals(user.getFirstname())));
        assertTrue(result.stream().anyMatch(user -> janeSmith.getFirstname().equals(user.getFirstname())));
    }

    @Test
    void testFindByLeadingTeam() {
        User result = userDAO.findLeaderByTeam(managementTeam);

        assertNotNull(result);
        assertEquals(bobWilson.getFirstname(), result.getFirstname());
        assertEquals(managementTeam.getAssigneeId(), result.getLeadingTeam().getAssigneeId());
    }

    @Test
    void testFindByTeam_EmptyResult() {
        EntityTransaction transaction = entityManager.getTransaction();

        Team emptyTeam = new Team();
        emptyTeam.setUsername("Empty Team");
        transaction.begin();
        entityManager.persist(emptyTeam);
        transaction.commit();

        List<User> result = userDAO.findByTeam(emptyTeam);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDatabase_Connection() {
        assertTrue(postgres.isRunning());
        assertNotNull(entityManager);
        assertTrue(entityManager.isOpen());
    }
    
    @Test
    void testFindByUsername(){
        List<User> result = userDAO.findByUsername(johnDoe.getUsername());

        assertEquals(1, result.size());
        assertEquals(johnDoe.getFirstname(), result.getFirst().getFirstname());
    }

    @Test
    void testFindByAssigneeId(){
        User result = userDAO.findByAssigneeId(johnDoe.getAssigneeId());

        assertNotNull(result);
        assertEquals(johnDoe.getFirstname(), result.getFirstname());
    }

    @Test
    void testFindByIsActive(){
        List<User> result = userDAO.findByIsActive(true);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(user -> johnDoe.getFirstname().equals(user.getFirstname())));
        assertTrue(result.stream().anyMatch(user -> bobWilson.getFirstname().equals(user.getFirstname())));

        result = userDAO.findByIsActive(false);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(user -> janeSmith.getFirstname().equals(user.getFirstname())));
    }

    @Test
    void testFindCreateAfterDate() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(4);
        List<User> results = userDAO.findCreateAfterDate(cutoff);
        assertTrue(results.contains(janeSmith));
        assertTrue(results.contains(bobWilson));
        assertFalse(results.contains(johnDoe));
    }

    @Test
    void testFindCreateBeforeDate() {
        System.out.println(johnDoe.getCreatedAt());


        LocalDateTime cutoff = LocalDateTime.now().minusDays(4);
        List<User> results = userDAO.findCreateBeforeDate(cutoff);
        assertTrue(results.contains(johnDoe));
        assertFalse(results.contains(janeSmith));
        assertFalse(results.contains(bobWilson));
    }

    @Test
    void testFindCreateBetweenDates() {
        LocalDateTime start = LocalDateTime.now().minusDays(4);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<User> results = userDAO.findCreateBetweenDates(start, end);
        assertTrue(results.contains(janeSmith));
        assertTrue(results.contains(bobWilson));
        assertFalse(results.contains(johnDoe));
    }

    @Test
    void testFindUpdateAfterDate() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        List<User> results = userDAO.findUpdateAfterDate(cutoff);
        assertTrue(results.contains(janeSmith));
        assertTrue(results.contains(bobWilson));
        assertTrue(results.contains(johnDoe));
    }

    @Test
    void testFindUpdateBeforeDate() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        List<User> results = userDAO.findUpdateBeforeDate(cutoff);
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindUpdateBetweenDates() {
        LocalDateTime start = LocalDateTime.now().minusDays(3);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        List<User> results = userDAO.findUpdateBetweenDates(start, end);
        assertTrue(results.contains(janeSmith));
        assertTrue(results.contains(bobWilson));
        assertTrue(results.contains(johnDoe));
    }

    @Test
    void testFindOne() {
        User user = userDAO.findOne(johnDoe.getAssigneeId());
        assertNotNull(user);
        assertEquals(johnDoe.getFirstname(), user.getFirstname());
    }

    @Test
    void testDeleteById() {
        User user = new User();
        user.setFirstname("Mickael");
        user.setLastname("Jackson");
        user.setEmail("mickael@jackson.usa");
        userDAO.save(user);

        user = userDAO.findOne(user.getAssigneeId());
        assertNotNull(user);
        assertEquals("Mickael", user.getFirstname());

        userDAO.deleteById(user.getAssigneeId());

        user = userDAO.findOne(user.getAssigneeId());
        assertNull(user);
    }

    @Test
    void testTransaction_Rollback() {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            User newUser = new User();
            newUser.setEmail("test@rollback.com");
            newUser.setFirstname("Test");
            newUser.setLastname("Rollback");

            transaction.begin();
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
