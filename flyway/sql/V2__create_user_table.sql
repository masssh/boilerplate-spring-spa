CREATE TABLE user (
    userName varchar(50) not null,
    userToken varchar(60) not null,
    role varchar(50) not null
);

INSERT INTO user VALUES
(
    'user',
    '$2y$10$5GyeNB5aCIw5CltzYtr4n.tkv4vz1bVKVMlPtR.4nkw4bOPrXylpa', -- 'user' encrypted by BCryptPasswordEncoder
    'ROLE_USER'
),
(
    'admin',
    '$2y$10$d7Q.kOopMLQ9rRiNXd0k2esu0ND9nQZUBo2KZokz81CkCUBBVapd6', -- 'admin' encrypted by BCryptPasswordEncoder
    'ROLE_ADMIN'
)
;
