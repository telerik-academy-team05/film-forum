
CREATE TABLE roles

(
    role_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    role    VARCHAR(255) NOT NULL
);

CREATE TABLE users
(
    user_id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    username          VARCHAR(255) UNIQUE NOT NULL,
    password          VARCHAR(255)        NOT NULL,
    first_name        VARCHAR(255)        NOT NULL,
    last_name         VARCHAR(255)        NOT NULL,
    email             VARCHAR(255) UNIQUE NOT NULL,
    role_id           INTEGER             NOT NULL,
    phone             VARCHAR(50),
    profile_photo_url VARCHAR(500),
    is_blocked        BOOLEAN             NOT NULL DEFAULT FALSE,
    CONSTRAINT users_roles_role_id_fk
        FOREIGN KEY (role_id) REFERENCES roles (role_id)
);


CREATE TABLE posts
(
    post_id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    author_id  INTEGER      NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_posts_author
        FOREIGN KEY (author_id) REFERENCES users (user_id)
);

CREATE TABLE comments
(
    comment_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    content    TEXT    NOT NULL,
    author_id  INTEGER NOT NULL,
    post_id    INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id) REFERENCES users (user_id),
    CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

CREATE TABLE likes
(
    like_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    post_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_likes_post
        FOREIGN KEY (post_id) REFERENCES posts (post_id),
    CONSTRAINT fk_likes_user
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT uq_likes_post_user UNIQUE (post_id, user_id)
);

CREATE TABLE tags
(
    tag_id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    tag_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE posts_tags
(
    post_id INTEGER NOT NULL,
    tag_id  INTEGER NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk_posts_tags_post
        FOREIGN KEY (post_id) REFERENCES posts (post_id),
    CONSTRAINT fk_posts_tags_tag
        FOREIGN KEY (tag_id) REFERENCES tags (tag_id)
);