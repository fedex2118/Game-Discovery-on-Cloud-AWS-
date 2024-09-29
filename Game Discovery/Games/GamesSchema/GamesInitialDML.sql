USE games;

-- Generating predefined genres
INSERT INTO games.genres (genre_id, genre_name) VALUES (1, "Roguelike");
INSERT INTO games.genres (genre_id, genre_name) VALUES (2, "Action RPG");
INSERT INTO games.genres (genre_id, genre_name) VALUES (3, "Beat 'em up");
INSERT INTO games.genres (genre_id, genre_name) VALUES (4, "Shooter");
INSERT INTO games.genres (genre_id, genre_name) VALUES (5, "Third-person shooter");
INSERT INTO games.genres (genre_id, genre_name) VALUES (6, "Non-linear gameplay");
INSERT INTO games.genres (genre_id, genre_name) VALUES (7, "Dynamic Adventure");
INSERT INTO games.genres (genre_id, genre_name) VALUES (8, "Independent Video Game");
INSERT INTO games.genres (genre_id, genre_name) VALUES (9, "Adventure");
INSERT INTO games.genres (genre_id, genre_name) VALUES (10, "Strategy");
INSERT INTO games.genres (genre_id, genre_name) VALUES (11, "RTS");
INSERT INTO games.genres (genre_id, genre_name) VALUES (12, "Action video game");
INSERT INTO games.genres (genre_id, genre_name) VALUES (13, "Tactical wargame");
INSERT INTO games.genres (genre_id, genre_name) VALUES (14, "Sandbox");
INSERT INTO games.genres (genre_id, genre_name) VALUES (15, "Metroidvania");
INSERT INTO games.genres (genre_id, genre_name) VALUES (16, "MMO");
INSERT INTO games.genres (genre_id, genre_name) VALUES (17, "Casual game");
INSERT INTO games.genres (genre_id, genre_name) VALUES (18, "Simulation video game");
INSERT INTO games.genres (genre_id, genre_name) VALUES (19, "Turn-based strategic game");
INSERT INTO games.genres (genre_id, genre_name) VALUES (20, "Free-to-play");
INSERT INTO games.genres (genre_id, genre_name) VALUES (21, "Tactical Shooter");
INSERT INTO games.genres (genre_id, genre_name) VALUES (22, "Co-Op");
INSERT INTO games.genres (genre_id, genre_name) VALUES (23, "4X");
INSERT INTO games.genres (genre_id, genre_name) VALUES (24, "RPG");
INSERT INTO games.genres (genre_id, genre_name) VALUES (25, "Early Access");
INSERT INTO games.genres (genre_id, genre_name) VALUES (26, "Other");

-- Generating predefined platforms
INSERT INTO games.platforms (platform_id, platform_name) VALUES (1, "Microsoft Windows");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (2, "Linux");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (3, "Macintosh");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (4, "Nintendo Switch");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (5, "Playstation 5");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (6, "Playstation 4");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (7, "Playstation 3");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (8, "Playstation 2");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (9, "Playstation 1");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (10, "GeForce Now");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (11, "Xbox One");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (12, "Android");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (13, "iOS");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (14, "Nintendo DS");
INSERT INTO games.platforms (platform_id, platform_name) VALUES (15, "Other");

-- Generating predefined developers
INSERT INTO games.developers (developer_id, developer_name) VALUES (1, "Massive Monster");
INSERT INTO games.developers (developer_id, developer_name) VALUES (2, "Motion Twin");
INSERT INTO games.developers (developer_id, developer_name) VALUES (3, "Playdigious");
INSERT INTO games.developers (developer_id, developer_name) VALUES (4, "Iron Gate Studio");
INSERT INTO games.developers (developer_id, developer_name) VALUES (5, "FISHLABS Entertainment GmbH");
INSERT INTO games.developers (developer_id, developer_name) VALUES (6, "Piktiv");
INSERT INTO games.developers (developer_id, developer_name) VALUES (7, "Hazelight Studios");
INSERT INTO games.developers (developer_id, developer_name) VALUES (8, "Ubisoft");
INSERT INTO games.developers (developer_id, developer_name) VALUES (9, "Red Storm");
INSERT INTO games.developers (developer_id, developer_name) VALUES (10, "Supergiant Games");
INSERT INTO games.developers (developer_id, developer_name) VALUES (11, "LLC");
INSERT INTO games.developers (developer_id, developer_name) VALUES (12, "Bethesda Game Studios");
INSERT INTO games.developers (developer_id, developer_name) VALUES (13, "Aspyr Media");
INSERT INTO games.developers (developer_id, developer_name) VALUES (14, "Firaxis Games");
INSERT INTO games.developers (developer_id, developer_name) VALUES (15, "Re-Logic");
INSERT INTO games.developers (developer_id, developer_name) VALUES (16, "The Creative Assembly");
INSERT INTO games.developers (developer_id, developer_name) VALUES (17, "Feral Interactive");
INSERT INTO games.developers (developer_id, developer_name) VALUES (18, "ConcernedApe");
INSERT INTO games.developers (developer_id, developer_name) VALUES (19, "Pearl Abyss");
INSERT INTO games.developers (developer_id, developer_name) VALUES (20, "Valve Corporation");
INSERT INTO games.developers (developer_id, developer_name) VALUES (21, "Team17");
INSERT INTO games.developers (developer_id, developer_name) VALUES (22, "Ghost Ship Games");
INSERT INTO games.developers (developer_id, developer_name) VALUES (23, "Coffee Stain Studios");
INSERT INTO games.developers (developer_id, developer_name) VALUES (24, "Rebellion Developments");
INSERT INTO games.developers (developer_id, developer_name) VALUES (25, "Slavic Magic");
INSERT INTO games.developers (developer_id, developer_name) VALUES (26, "Other");

-- Generating predefined publishers
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (1, "Electronic Arts");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (2, "Ubisoft");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (3, "Team17");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (4, "Coffee Stain Publishing");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (5, "Coffee Stain Studios");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (6, "Deep Silver");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (7, "Double Eleven");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (8, "Motion Twin");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (9, "Rebellion");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (10, "Game Source Entertainment");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (11, "Fireshine Games");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (12, "Devolver Digital");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (13, "Supergiant Games");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (14, "Bethesda Game Studios");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (15, "2K");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (16, "Re-Logic");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (17, "Electronic Arts EA");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (18, "Feral Interactive");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (19, "Sega");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (20, "ConcernedApe");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (21, "Pearl Abyss");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (22, "Valve Corporation");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (23, "Hooded Horse");
INSERT INTO games.publishers (publisher_id, publisher_name) VALUES (24, "Other");

-- Generating predefined games
-- A Way Out
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (1, "A Way Out", "2018-09-23", 29.99, false, true, false, true, "A Way Out description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (1, 8); 
INSERT INTO games.game_genres (game_id, genre_id) VALUES (1, 9); 
INSERT INTO games.game_genres (game_id, genre_id) VALUES (1, 22);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (1, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (1, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (1, 11);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (1, 7);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (1, 1);

-- Far Cry 5
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (2, "Far Cry 5", "2018-03-27", 59.99, true, true, false, true, "Far Cry 5 description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (2, 4);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (2, 7);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (2, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (2, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (2, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (2, 11);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (2, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (2, 8);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (2, 9);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (2, 2);

-- WMD Worms
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (3, "WMD Worms", "2016-08-23", 29.99, true, true, true, true, "WMD Worms description", true, false, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (3, 10);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (3, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (3, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (3, 11);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (3, 12);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (3, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (3, 21);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (3, 3);

-- Valheim
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (4, "Valheim", "2021-02-2", 19.99, true, true, false, true, "Valheim description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (4, 3);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (4, 8);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (4, 9);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (4, 22);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (4, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (4, 2);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (4, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (4, 11);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (4, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (4, 4);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (4, 5);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (4, 6);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (4, 4);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (4, 5);

-- Deep Rock Galactic
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (5, "Deep Rock Galactic", "2018-02-28", 29.99, true, true, false, true, "Deep Rock Galactic description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (5, 4);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (5, 22);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (5, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (5, 5);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (5, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (5, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (5, 11);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (5, 22);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (5, 4);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (5, 5);

-- Goat Simulator
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (6, "Goat Simulator", "2014-03-28", 9.75, true, false, false, true, "Goat Simulator description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (6, 3);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (6, 8);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (6, 9);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (6, 17);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (6, 18);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (6, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (6, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (6, 11);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (6, 12);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (6, 14);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (6, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (6, 23);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (6, 4);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (6, 5);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (6, 6);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (6, 7);

-- Dead Cells
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (7, "Dead Cells", "2018-08-6", 24.99, true, false, false, true, "Dead Cells description", true, false, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (7, 1);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (7, 3);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (7, 15);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (7, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (7, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (7, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (7, 2);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (7, 3);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (7, 24);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (7, 8);

-- Sniper Elite 4
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (8, "Sniper Elite 4", "2017-02-13", 59.99, true, true, true, true, "Sniper Elite 4 description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (8, 9);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (8, 21);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (8, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (8, 4);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (8, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (8, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (8, 11);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (8, 24);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (8, 9);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (8, 10);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (8, 11);

-- Cult of the Lamb
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (9, "Cult of the Lamb", "2022-08-11", 22.99, true, false, false, true, "Cult of the Lamb description", true, false, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (9, 1);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (9, 7);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (9, 10);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (9, 4);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (9, 5);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (9, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (9, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (9, 1);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (9, 12);

-- Hades
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (10, "Hades", "2020-09-17", 24.50, true, false, false, true, "Hades description", true, false, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (10, 1);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (10, 2);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (10, 3);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (10, 4);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (10, 4);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (10, 5);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (10, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (10, 13);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (10, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (10, 10);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (10, 11);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (10, 13);

-- Fallout 4
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (11, "Fallout 4", "2015-11-10", 19.99, true, false, false, true, "Fallout 4 description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (11, 2);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (11, 3);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (11, 5);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (11, 6);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (11, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (11, 5);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (11, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (11, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (11, 11);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (11, 12);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (11, 14);

-- Civilization VI
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (12, "Civilization VI", "2016-10-21", 19.99, true, true, true, true, "Civilization VI description", true, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (12, 10);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (12, 19);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (12, 23);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 2);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 4);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 11);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 12);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (12, 13);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (12, 13);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (12, 14);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (12, 15);

-- Terraria
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (13, "Terraria", "2011-05-16", 9.75, true, true, true, true, "Terraria description", true, false, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (13, 6);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (13, 7);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (13, 22);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (13, 4);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (13, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (13, 12);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (13, 13);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (13, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (13, 15);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (13, 16);

-- Medieval II: Total War
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (14, "Medieval II: Total War", "2006-11-15", 24.99, true, true, true, true, "Medieval II: Total War description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (14, 11);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (14, 13);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (14, 19);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (14, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (14, 3);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (14, 12);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (14, 13);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (14, 16);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (14, 17);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (14, 18);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (14, 19);

-- Stardew Valley
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (15, "Stardew Valley", "2016-02-26", 13.99, true, true, false, true, "Stardew Valley description", true, false, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (15, 8);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (15, 22);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (15, 23);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (15, 3);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (15, 4);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (15, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (15, 12);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (15, 13);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (15, 15);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (15, 11);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (15, 18);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (15, 20);

-- Manor Lords
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (16, "Manor Lords", "2024-04-26", 39.99, true, false, false, true, "Manor Lords description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (16, 10);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (16, 24);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (16, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (16, 11);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (16, 25);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (16, 23);

-- Black Desert
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (17, "Black Desert", "2017-05-24", 9.99, false, true, true, true, "Black Desert description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (17, 3);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (17, 6);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (17, 10);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (17, 16);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (17, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (17, 6);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (17, 10);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (17, 11);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (17, 19);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (17, 21);

-- Counter-Strike 2
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (18, "Counter-Strike 2", "2023-09-27", 0.00, false, true, true, true, "Counter-Strike 2 description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (18, 20);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (18, 21);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (18, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (18, 2);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (18, 20);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (18, 22);

-- Total War: WARHAMMER III
INSERT INTO games.games (game_id, game_name, release_date, price, is_singleplayer, is_multiplayer, is_pvp, is_pve, description, is_2d, is_3d, cover_art_url, average_rating, review_quantity, status)
VALUES (19, "Total War: WARHAMMER III", "2022-02-17", 59.99, true, true, true, true, "Total War: WARHAMMER III description", false, true, "cover_art_placeholder", 0.00, 0, 1);

INSERT INTO games.game_genres (game_id, genre_id) VALUES (19, 11);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (19, 12);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (19, 13);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (19, 19);
INSERT INTO games.game_genres (game_id, genre_id) VALUES (19, 22);

INSERT INTO games.game_platforms (game_id, platform_id) VALUES (19, 1);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (19, 2);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (19, 3);
INSERT INTO games.game_platforms (game_id, platform_id) VALUES (19, 10);

INSERT INTO games.game_developers (game_id, developer_id) VALUES (19, 16);
INSERT INTO games.game_developers (game_id, developer_id) VALUES (19, 17);

INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (19, 18);
INSERT INTO games.game_publishers (game_id, publisher_id) VALUES (19, 19);


