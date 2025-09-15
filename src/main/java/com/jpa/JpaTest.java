package com.jpa;

import com.domain.*;
import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class JpaTest {

    private final EntityManager entityManager;

    public JpaTest(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory("postgresql");
        EntityManager manager = factory.createEntityManager();

        JpaTest jpaTest = new JpaTest(manager);

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {

            jpaTest.addData();

        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();

        manager.close();
        factory.close();
    }


    private void addData(){
        Team team = createTeam();
        Collection<User> users = createUsers(team);
        team.setTeamMembers(users);

        if(users.isEmpty()){
            throw new IllegalStateException("No users in team !");
        }

        team.setTeamLeader(((List<User>) users).getFirst());

        entityManager.persist(team);
        for(User user : users) entityManager.persist(user);

        for(Project project : createProjects(team)){
            entityManager.persist(project);
            for(Task task : project.getTasks()) entityManager.persist(task);
        }


        entityManager.flush();
        entityManager.close();
    }


    private Collection<User> createUsers(Team team){
        List<User> users = new ArrayList<>();

        for(int i=0; i<10; i++){
            User user = new User();
            user.setEmail("email"+i+"@gmail.com");
            user.setPassword("password"+i);
            user.setActive(true);
            user.setFirstname("firstname"+i);
            user.setLastname("lastname"+i);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setUsername("username"+i);
            user.setImage(team.getImage());
            user.setTeam(team);

            users.add(user);
        }

        return users;
    }

    private Team createTeam(){
        Team newTeam = new Team();
        newTeam.setActive(true);
        newTeam.setCreatedAt(LocalDateTime.now());
        newTeam.setUpdatedAt(LocalDateTime.now());
        newTeam.setUsername("ISTIC team");
        try {
            newTeam.setImage(Files.readAllBytes(new File("src/main/resources/img/logoisticfr.png").toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newTeam;
    }

    private List<Project> createProjects(Team team){
        List<Project> projects = new ArrayList<>();
        Random random = new Random();

        for(int i=0; i<5; i++){
            Project project = new Project();
            project.setProjectName("project"+i);
            if (random.nextBoolean()) {
                project.setProjectLeader(team);
            } else {
                project.setProjectLeader(((List<User>) team.getTeamMembers()).getFirst());
            }
            project.setProjectDescription(String.format("description"+i));
            project.setProjectStartDate(Date.from(Instant.now()));

            List<Task> tasks = createTasks(team, project);
            project.setTasks(tasks);

            projects.add(project);
        }

        return projects;
    }

    private List<Task> createTasks(Team team, Project project){
        List<Task> tasks = new ArrayList<>();

        for(int i=0; i<10; i++){
            Task task = new Task();
            task.setProject(project);
            task.setTaskName("task"+i+"_"+project.getProjectName());
            task.setPriority(TaskPriority.URGENT);
            task.setStatus(TaskStatus.NEW);
            task.setAssigned(((List<User>)team.getTeamMembers()).get(i));
            task.setType(TaskType.BUGFIX);

            tasks.add(task);
        }
        return tasks;
    }
}

