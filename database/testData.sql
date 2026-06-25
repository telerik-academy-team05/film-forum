INSERT INTO roles (role_id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');

INSERT INTO users (user_id,
                   username,
                   password,
                   first_name,
                   last_name,
                   email,
                   role_id,
                   phone,
                   profile_photo_url,
                   is_blocked)
VALUES (1, 'cine_admin', 'password123', 'Maya', 'Stone', 'maya.stone@example.com', 1, '555-2001',
        '/uploads/profile-photos/cine-admin.jpg', FALSE),
       (2, 'filmfan92', 'password123', 'Leo', 'Martin', 'leo.martin@example.com', 2, '555-2002',
        '/uploads/profile-photos/filmfan92.jpg', FALSE),
       (3, 'noir_lover', 'password123', 'Nina', 'Clark', 'nina.clark@example.com', 2, NULL,
        '/uploads/profile-photos/noir-lover.jpg', FALSE),
       (4, 'spoilerking', 'password123', 'Oscar', 'Reed', 'oscar.reed@example.com', 2, '555-2004', NULL, TRUE);

INSERT INTO tags (tag_id, tag_name)
VALUES (1, 'action'),
       (2, 'drama'),
       (3, 'sci-fi'),
       (4, 'horror'),
       (5, 'recommendations');

INSERT INTO posts (post_id,
                   title,
                   content,
                   author_id,
                   created_at,
                   updated_at)
VALUES (1, 'Best Opening Scenes in Cinema',
        'What movie opening scene immediately pulled you in? For me, The Dark Knight is still hard to beat.', 1,
        '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
       (2, 'Is Interstellar Still Nolan''s Best Movie?',
        'I rewatched Interstellar last night and the emotional ending still works. Do you think it is Nolan''s best?',
        2, '2026-06-02 12:30:00', '2026-06-02 12:30:00'),
       (3, 'Classic Film Noir Recommendations',
        'I want to build a weekend watchlist with classic noir movies. Please recommend your favorites.', 3,
        '2026-06-03 09:15:00', '2026-06-03 09:15:00'),
       (4, 'Most Underrated Horror Movies',
        'Which horror movies deserve more attention? I am looking for something atmospheric, not only jump scares.', 2,
        '2026-06-04 18:45:00', '2026-06-04 19:00:00');

INSERT INTO comments (comment_id,
                      content,
                      author_id,
                      post_id,
                      created_at,
                      updated_at)
VALUES (1, 'The opening of Inglourious Basterds is incredible too. So tense from the first minute.', 2, 1,
        '2026-06-01 11:00:00', '2026-06-01 11:00:00'),
       (2, 'Interstellar is great, but I would still choose The Prestige as Nolan''s best.', 3, 2,
        '2026-06-02 13:00:00', '2026-06-02 13:00:00'),
       (3, 'Start with Double Indemnity and The Maltese Falcon. Both are essential noir films.', 1, 3,
        '2026-06-03 10:00:00', '2026-06-03 10:00:00'),
       (4, 'The Others is underrated if you like slow, atmospheric horror.', 4, 4, '2026-06-04 20:00:00',
        '2026-06-04 20:00:00'),
       (5, 'Arrival also belongs in the sci-fi discussion. It is quieter than Interstellar but just as emotional.', 1,
        2, '2026-06-05 08:30:00', '2026-06-05 08:30:00');

INSERT INTO likes (like_id, post_id, user_id)
VALUES (1, 1, 2),
       (2, 1, 3),
       (3, 2, 1),
       (4, 2, 3),
       (5, 3, 1),
       (6, 4, 3);

INSERT INTO posts_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 5),
       (2, 2),
       (2, 3),
       (3, 2),
       (3, 5),
       (4, 4),
       (4, 5);
