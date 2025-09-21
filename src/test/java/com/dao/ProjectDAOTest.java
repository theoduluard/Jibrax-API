package com.dao;

import com.dao.implementation.ProjectDAO;
import com.dao.implementation.TaskDAO;
import com.dao.implementation.TeamDAO;
import com.domain.Project;
import com.domain.Task;
import com.domain.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectDAOTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("jibrax")
            .withUsername("testuser")
            .withPassword("testpwd");

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;

    private Task devTask;
    private Task archiTask;
    private Task deliveryTask;
    private Team team;
    private Project project;
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

        taskDAO = new TaskDAO();
        taskDAO.setEntityManager(entityManager);

        projectDAO = new ProjectDAO();
        projectDAO.setEntityManager(entityManager);

        baseDate = LocalDateTime.of(2023, 12, 15, 10, 30);

        createTestData();
    }

    @AfterEach
    void tearDown() {
        taskDAO.findAll().forEach(task -> taskDAO.delete(task));
        projectDAO.findAll().forEach(projectRes -> projectDAO.delete(projectRes));
        entityManager.close();
    }

    private void createTestData() {
        team = new Team();
        team.setUsername("Main team");
        TeamDAO teamDAO = new TeamDAO();
        teamDAO.setEntityManager(entityManager);
        teamDAO.save(team);

        project = new Project();
        project.setProjectName("Jibrax Platform");
        project.setProjectDescription("A Jira-like project management tool");
        project.setProjectStartDate(Date.from(baseDate.atZone(ZoneId.systemDefault()).toInstant()));

        devTask = new Task();
        devTask.setTaskName("Implement DAO layer");
        devTask.setProject(project);

        archiTask = new Task();
        archiTask.setTaskName("Design database schema");
        archiTask.setProject(project);

        deliveryTask = new Task();
        deliveryTask.setTaskName("Deploy MVP to staging");
        deliveryTask.setProject(project);

        project.setTasks(Arrays.asList(devTask, archiTask, deliveryTask));
        project.setProjectLeader(team);

        projectDAO.save(project);
    }

    @Test
    void testFindByProjectId() {
        Project found = projectDAO.findByProjectId(project.getProjectId());
        assertNotNull(found);
        assertEquals(project.getProjectName(), found.getProjectName());
    }

    @Test
    void testFindByProjectName() {
        Project projectRes = projectDAO.findByProjectName(project.getProjectName());
        assertNotNull(projectRes);
        assertEquals(project.getProjectId(), projectRes.getProjectId());
    }

    @Test
    void testFindByProjectDescription() {
        List<Project> projects = projectDAO.findByProjectDescription("Jira-like");
        assertFalse(projects.isEmpty());
        assertEquals(project.getProjectName(), projects.getFirst().getProjectName());
    }

    @Test
    void testFindByProjectStartDateBefore() {
        Date later = Date.from(baseDate.plusDays(10).atZone(ZoneId.systemDefault()).toInstant());
        List<Project> projects = projectDAO.findByProjectStartDateBefore(later);
        assertEquals(1, projects.size());
    }

    @Test
    void testFindByProjectStartDateAfter() {
        Date earlier = Date.from(baseDate.minusDays(10).atZone(ZoneId.systemDefault()).toInstant());
        List<Project> projects = projectDAO.findByProjectStartDateAfter(earlier);
        assertEquals(1, projects.size());
    }

    @Test
    void testFindByProjectStartDateBetween() {
        Date start = Date.from(baseDate.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(baseDate.plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        List<Project> projects = projectDAO.findByProjectStartDateBetween(start, end);
        assertEquals(1, projects.size());
    }

    @Test
    void testFindByTask() {
        Project projectRes = projectDAO.findByTask(devTask);
        assertNotNull(projectRes);
        assertTrue(projectRes.getTasks().stream()
                .anyMatch(t -> t.getTaskName().equals(devTask.getTaskName())));
    }

    @Test
    void testFindByProjectLeader(){
        List<Project> projects = projectDAO.findByProjectLeader(team);
        assertEquals(1, projects.size());
        assertTrue(projects.stream().anyMatch(p -> p.getProjectName().equals(project.getProjectName())));
    }

    @Test
    void testDeleteProject() {
        projectDAO.delete(project);

        Project deleted = projectDAO.findOne(project.getProjectId());
        assertNull(deleted);
    }
}