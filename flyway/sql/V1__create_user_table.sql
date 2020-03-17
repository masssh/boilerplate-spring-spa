CREATE TABLE oauth2_google (
    subject VARCHAR(50) NOT NULL,
    idToken VARCHAR(1500) NOT NULL,
    accessToken VARCHAR(50) NOT NULL,
    issuedAt DATETIME NOT NULL,
    expiresAt DATETIME NOT NULL,
    PRIMARY KEY (subject)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
;

CREATE TABLE user (
    userId VARCHAR(50) NOT NULL,
    userName VARCHAR(50) NOT NULL,
    role VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    locale VARCHAR(50) NOT NULL,
    passwordHash VARCHAR(100) NULL,
    accessToken VARCHAR(50) NULL,
    googleSubject VARCHAR(50) NULL,
    PRIMARY KEY (userId),
    UNIQUE KEY (email),
    CONSTRAINT fk_user_for_oauth2_google FOREIGN KEY (googleSubject) REFERENCES oauth2_google(subject)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
;

