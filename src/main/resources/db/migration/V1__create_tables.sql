CREATE TABLE users (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username    VARCHAR(50)  NOT NULL,
                       email       VARCHAR(255) NOT NULL,
                       password    VARCHAR(255) NOT NULL,
                       created_at  TIMESTAMP    NOT NULL
);

CREATE TABLE projects (
                          id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name        VARCHAR(50)  NOT NULL,
                          description VARCHAR(500),
                          created_at  TIMESTAMP    NOT NULL,
                          owner_id    BIGINT       NOT NULL,
                          CONSTRAINT fk_project_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE tasks (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title       VARCHAR(100) NOT NULL,
                       description VARCHAR(1000),
                       status      VARCHAR(20)  NOT NULL,
                       priority    VARCHAR(20)  NOT NULL,
                       due_date    DATE,
                       created_at  TIMESTAMP    NOT NULL,
                       project_id  BIGINT       NOT NULL,
                       assignee_id BIGINT,
                       CONSTRAINT fk_task_project  FOREIGN KEY (project_id)  REFERENCES projects (id),
                       CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users (id)
);