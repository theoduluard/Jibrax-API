package com.jpa;

import com.domain.Team;
import com.domain.User;
import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        List<User> users = createUsers(team);

        if(users.isEmpty()){
            throw new IllegalStateException("No users in team !");
        }

        team.setTeamLeader(users.getFirst());

        entityManager.persist(team);
        for(User user : users) entityManager.persist(user);
    }


    private List<User> createUsers(Team team){
        List<User> users = new ArrayList<>();

        for(int i=0; i<10; i++){
            User user = new User();
            user.setAdmin(false);
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
            newTeam.setImage(Files.readAllBytes(new File("src/main/resources/test/logoisticfr.png").toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newTeam;
    }


}

