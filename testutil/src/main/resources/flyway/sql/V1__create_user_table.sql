CREATE TABLE oauth2_google (
    oauth2GoogleId BIGINT NOT NULL AUTO_INCREMENT,
    subject VARCHAR(50) NOT NULL,
    idToken VARCHAR(1500) NOT NULL,
    accessToken VARCHAR(50) NOT NULL,
    issuedAt DATETIME NOT NULL,
    expiresAt DATETIME NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    PRIMARY KEY (oauth2GoogleId),
    UNIQUE KEY (subject)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
;

CREATE TABLE user (
    userId BIGINT NOT NULL AUTO_INCREMENT,
    userHash VARCHAR(50) NOT NULL,
    userName VARCHAR(50) NOT NULL,
    role VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    locale VARCHAR(50) NOT NULL,
    passwordHash VARCHAR(100) NULL,
    accessToken VARCHAR(50) NULL,
    googleSubject VARCHAR(50) NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    PRIMARY KEY (userId),
    UNIQUE KEY (userHash),
    UNIQUE KEY (email),
    CONSTRAINT fk_user_for_oauth2_google FOREIGN KEY (googleSubject) REFERENCES oauth2_google(subject)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
;

CREATE TABLE verification (
    verificationId BIGINT NOT NULL AUTO_INCREMENT,
    verificationHash VARCHAR(50) NOT NULL,
    userId BIGINT NOT NULL,
    verificationType ENUM('EMAIL', 'PASSWORD') NOT NULL,
    expiresAt DATETIME NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    PRIMARY KEY (verificationId),
    CONSTRAINT fk_verification_for_user FOREIGN KEY (userId) REFERENCES user(userId)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
;

