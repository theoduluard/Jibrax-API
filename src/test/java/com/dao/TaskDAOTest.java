package com.dao;

import com.dao.implementation.ProjectDAO;
import com.dao.implementation.TaskDAO;
import com.dao.implementation.TeamDAO;
import com.dao.implementation.UserDAO;
import com.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskDAOTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("jibrax")
            .withUsername("testuser")
            .withPassword("testpwd");

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private TaskDAO taskDAO;
    private ProjectDAO projectDAO;

    private Task devTask;
    private Task archiTask;
    private Task deliveryTask;
    private User assignedUser;
    private Team assignedTeam;

    private Project project;

    @BeforeAll
    static void initContainer() {
        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        props.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        props.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        props.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "true");

        entityManagerFactory = Persistence.createEntityManagerFactory("postgresql-junit", props);
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

        taskDAO = new TaskDAO();
        taskDAO.setEntityManager(entityManager);

        projectDAO = new ProjectDAO();
        projectDAO.setEntityManager(entityManager);

        createTestData();
    }

    @AfterEach
    void tearDown() {
        taskDAO.findAll().forEach(task -> taskDAO.delete(task));
        projectDAO.findAll().forEach(projectRes -> projectDAO.delete(projectRes));
        entityManager.close();
    }

    private void createTestData() {
        UserDAO userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);

        TeamDAO teamDAO = new TeamDAO();
        teamDAO.setEntityManager(entityManager);

        assignedUser = new User();
        assignedUser.setUsername("testuser");
        userDAO.save(assignedUser);

        assignedTeam = new Team();
        assignedTeam.setUsername("testteam");
        teamDAO.save(assignedTeam);

        project = new Project();
        project.setProjectName("Jibrax");
        project.setProjectDescription("A Jira-like tool");
        projectDAO.save(project);

        devTask = new Task();
        devTask.setTaskName("Develop login");
        devTask.setDescription("Implement login with JWT");
        devTask.setPriority(TaskPriority.HIGH);
        devTask.setStatus(TaskStatus.NEW);
        devTask.setType(TaskType.NEW_FEATURE);
        devTask.setProject(project);
        devTask.setAssigned(assignedUser);
        taskDAO.save(devTask);

        archiTask = new Task();
        archiTask.setTaskName("Architecture review");
        archiTask.setDescription("Check hexagonal architecture");
        archiTask.setPriority(TaskPriority.MEDIUM);
        archiTask.setStatus(TaskStatus.IN_PROGRESS);
        archiTask.setType(TaskType.DOCUMENTATION);
        archiTask.setProject(project);
        archiTask.setAssigned(assignedTeam);
        taskDAO.save(archiTask);

        deliveryTask = new Task();
        deliveryTask.setTaskName("Deploy prod");
        deliveryTask.setDescription("Deploy application to production");
        deliveryTask.setPriority(TaskPriority.URGENT);
        deliveryTask.setStatus(TaskStatus.DONE);
        deliveryTask.setType(TaskType.BUGFIX);
        deliveryTask.setProject(project);
        taskDAO.save(deliveryTask);
    }

    @Test
    void testFindByTaskId() {
        Task found = taskDAO.findByTaskId(devTask.getTaskId());
        assertNotNull(found);
        assertEquals("Develop login", found.getTaskName());
    }

    @Test
    void testFindByTaskName() {
        List<Task> tasks = taskDAO.findByTaskName("Deploy prod");
        assertEquals(1, tasks.size());
        assertEquals(deliveryTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByDescription() {
        List<Task> tasks = taskDAO.findByDescription("Implement login with JWT");
        assertEquals(1, tasks.size());
        assertEquals(devTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByPriority() {
        List<Task> tasks = taskDAO.findByPriority(TaskPriority.URGENT);
        assertEquals(1, tasks.size());
        assertEquals(deliveryTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByStatus() {
        List<Task> tasks = taskDAO.findByStatus(TaskStatus.IN_PROGRESS);
        assertEquals(1, tasks.size());
        assertEquals(archiTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByStatusAndPriority() {
        List<Task> tasks = taskDAO.findByStatusAndPriority(TaskPriority.HIGH, TaskStatus.NEW);
        assertEquals(1, tasks.size());
        assertEquals(devTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByType() {
        List<Task> tasks = taskDAO.findByType(TaskType.BUGFIX);
        assertEquals(1, tasks.size());
        assertEquals(deliveryTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByTypeAndPriority() {
        List<Task> tasks = taskDAO.findByTypeAndPriority(TaskPriority.MEDIUM, TaskType.DOCUMENTATION);
        assertEquals(1, tasks.size());
        assertEquals(archiTask.getTaskId(), tasks.getFirst().getTaskId());
    }

    @Test
    void testFindByProject() {
        List<Task> tasks = taskDAO.findByproject(project);
        assertEquals(3, tasks.size());
    }

    @Test
    void testFindByAssignee() {
        List<Task> tasks = taskDAO.findByAssignee(assignedUser);
        assertEquals(1, tasks.size());
        assertEquals(devTask.getTaskId(), tasks.getFirst().getTaskId());

        tasks = taskDAO.findByAssignee(assignedTeam);
        assertEquals(1, tasks.size());
        assertEquals(archiTask.getTaskId(), tasks.getFirst().getTaskId());
    }
}

