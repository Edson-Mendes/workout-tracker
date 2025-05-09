<h1 align="center"> Workout Tracker API </h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em%20Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=openjdk)
![Badge Spring](https://img.shields.io/static/v1?label=Springboot&message=v3.4.5&color=6DB33F&style=for-the-badge&logo=springboot)
![Badge Maven](https://img.shields.io/static/v1?label=Maven&message=v3.8.7&color=C71A36&style=for-the-badge&logo=apache+maven)

## :book: Resumo do projeto
Workout Tracker API é uma REST API onde você pode montar o seu treino, verificar progressão de carga ao longo do tempo
e muito mais para auxiliá-lo no seu treinamento e maximizar seus ganhos.

## Diagramas

### :capital_abcd: Diagrama entidade relacionamento

```mermaid
---
    title: Database Schema
---
    erDiagram
        WORKOUT {
            bigserial id PK
            varchar(100) name
            varchar(255) description
            boolean isCurrently
            timestamp created_at
        }
        EXERCISE {
            bigserial id PK
            varchar(100) name
            varchar(255) description
            varchar(150) addtional
            int sets
            timestamp created_at
            timestamp updated_at
            bigint workout_id FK
            bigint weight_id FK
        }
        WEIGHT {
            bigserial id PK
            real value
            varchar(50) unit
            timestamp created_at
            bigint exercise_id FK
        }
        WORKOUT ||--o{ EXERCISE : contains
        EXERCISE ||--|| WEIGHT : with
```