![Logo de Jibrax](/img/logojibrax_min.png "Logo de Jibrax").

[TOC]

## Status

<img src="https://gitlab2.istic.univ-rennes1.fr/tduluard/jibrax/badges/main/pipeline.svg" alt="Pipeline Status" width="150"/> <img src="https://gitlab2.istic.univ-rennes1.fr/tduluard/jibrax/badges/main/coverage.svg" alt="Code Coverage" width="150"/>

## Context

Jibrax is a project developed as part of the Advanced Software Architecture course in the Master’s program in Software Engineering at ISTIC, University of Rennes.
The primary objective of this project is to apply the concepts covered during the lectures, including:
- Designing and implementing the Java Persistence API (JPA),
- Exploring advanced software architecture techniques,
- Handling persistence, data modeling, and transactions,
- Integrating with a relational database (PostgreSQL) through JPA mappings.

Beyond practicing these technical aspects, we decided to implement a team-oriented project management system. The system is structured around four main entities:
- Users → individuals registered in the platform,
- Teams → groups of users collaborating together,
- Projects → initiatives managed by teams,
- Tasks → work items that make up each project.

This allows us to simulate a real-world collaborative environment while applying advanced software design and persistence principles.


## Database Architecture

The backend relies on a PostgreSQL database running inside a Docker container. 
- Default exposed port: 5432 (can be remapped if needed). 
- Database schema and tables are generated automatically based on JPA entity definitions.

You can interact with the database using:
- The psql CLI, 
- A GUI client such as pgAdmin or DBeaver, 
- Or directly from the container with: ``docker exec -it <container_name> psql -U postgres -d <database_name>`` 

The following class diagram explains how entities are organised and linked:
```mermaid
---
title: Jibrax PostgreSQL Architecture
---
classDiagram
    Assignee <|-- User
    Assignee <|-- Team

    User "1..*" -- "1" Team : "belongs to"
    Assignee "1" -- "*" Project : "is project leader of"
    Project "1" -- "1..*" Task : "contains"
    Task "*" -- "1" Assignee : "is assigned to"
    User "1" -- "0..1" Team : "is team leader of"
    
    class Assignee {
        Long assigneeId
        String username
        byte[] image
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    class User{
        String email
        String password
        String firstname
        String lastname
        Boolean isAdmin
        LocalDateTime lastLogin
    }

    class Team{
    }

    class Project{
        Long projectId
        String projectName
        String projectDescription
        Date projectStartDate
    }
    
    class Task{
        Long taskId
        String taskName
        String description
        TaskPriority priority
        TaskStatus status
        TaskType type
        Project project
    }
    
    <<Abstract>> Assignee
```

The previous class diagram shows the following architecture choices:
- The abstract class _Assignee_ **is implemented by** concrete classes _User_ and _Team_
- A team contains **one to many** (1..*) users.
- A user must be part of **one** (1) team.
- A team has **one**(1) user named team leader which must be a team member.
- A user **can** be the team leader of **one team** (0..1).
- A project contains **one to many** (1..*) tasks.
- A task is part of **one** (1) project.
- A project has **one** project leader which is an _Assignee_.
- An assignee **can** be the leader of **multiple** (*) project.
- A task has **one** (1) assignee entity.
- An assignee **can** be assigned to **multiple** (*) tasks.

## How to Run

### Requirements:
- Java 17+ 
- Maven 
- Docker & Docker Compose

### Local setup:
1. Clone or download the project: 
```
git clone https://gitlab2.istic.univ-rennes1.fr/tduluard/jibrax.git
cd jibrax
```
2. Start the service with Docker Compose (PostgreSQL + pgAdmin): 
```
./startPostgres.sh
```
3. Then run Java Persistance API test (JpaTest)
4. (Optionnal) Verify that the data has been correctly inserted into PostgreSQL by querying the database with psql or a GUI client.

### External setup:

1. A PostgreSQL database is hosted on a personal TrueNAS server. **IMPORTANT**: Use it with extreme care, any misuse could result in data corruption or loss affecting other users.
2. Only the following public user is provided:
```
url: jdbc:postgresql://jibrax.tduluard.fr:5432/jibrax
username: dbuser
password: pwddbuser
```
This account has permissions limited to creating and reading tables, as well as ingesting and querying data.

3.Remember to update the ``persistenceUnitName`` in your main file to: ``postgresql-nas``.


## Author

[Théo DULUARD](mailto:theo.duluard@etudiant.univ-rennes.fr) - Student in Software Engineering - ISTIC, University of Rennes

[Fabien GUILLOU](mailto:fabien.guillou@etudiant.univ-rennes.fr) - Student in Software Engineering - ISTIC, University of Rennes
