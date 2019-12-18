--select * from user;
--HansMuster
insert into user values (1, TRUE, '$2a$10$y046R1LdT1wsdexyUmQDGulpj8/7fQbeufqTWfD/P3YyEWabIdU1O', 'hans.muster@gmail.com');
--1234
insert into user values (2, TRUE, '$2a$10$florpQw0Y6L8sEx14xn4Duq3csCVdiljW.n3tbix5BPaCK.eFGo/W', 'vreni.meier@gmail.com');
--Pierre
insert into user values (3, TRUE, '$2a$10$fi6VKUYAVjGghz5nkDHGK.Zo8HW7cT0Kpcgbxmal95FXApvo/7Gee', 'peter.mueller@gmail.com');

--select * from collection;
insert into collection values (1, 'Hauptstädte Europa', TRUE, 'Hauptstädte Europa', 1);
insert into collection values (2, 'Hauptstädte Südamerika', TRUE, 'Hauptstädte Südamerika', 1);
insert into collection values (3, 'Hauptstädte Asien', TRUE, 'Hauptstädte Asien', 1);
insert into collection values (4, 'Fragen zu Wolkenformationen', FALSE, 'Wetter', 1);
insert into collection values (5, 'Französisch Lektion 35', TRUE, 'Französisch', 2);
insert into collection values (6, 'Basiskommandos Kürzel', FALSE, 'Assembler', 2);
INSERT INTO collection values (7, 'Basiswortschatz Spanisch', TRUE, 'Spanisch', 3);
INSERT INTO collection VALUES (8, 'SQL-Befehle', TRUE, 'SQL', 3);

--select * from role;
insert into role values (1, 'ROLE_USER');

--select * from role_users;
--leer

--select * from user_roles;
insert into user_roles values (1,1);
insert into user_roles values (2,1);
insert into user_roles values (3,1);

--select * from card;
insert into card values (1, 'Bern', FALSE, 'Schweiz', 1);
insert into card values (2, 'Paris', FALSE, 'Frankreich', 1);
insert into card values (3, 'Madrid', FALSE, 'Spanien', 1);
insert into card values (4, 'Tirana', FALSE, 'Albanien', 1);
insert into card values (5, 'Kopenhagen', FALSE, 'Dänemark', 1);
insert into card values (6, 'Andorra la Vella', FALSE, 'Andorra', 1);
insert into card values (7, 'Sofia', FALSE, 'Bulgarien', 1);
insert into card values (8, 'Berlin', FALSE, 'Deutschland', 1);
insert into card values (9, 'Dublin', FALSE, 'Irland', 1);
insert into card values (10, 'Oslo', FALSE, 'Norwegen', 1);
insert into card values (11, 'Moskau', FALSE, 'Russland', 1);
insert into card values (12, 'Minsk', FALSE, 'Weissrussland', 1);
insert into card values (13, 'Ankara', FALSE, 'Türkei', 1);

insert into card values (14, 'Buenos Aires', FALSE, 'Argentinien', 1);
insert into card values (15, 'Rio de Janeiro', FALSE, 'Brasilien', 1);
insert into card values (16, 'Lima', FALSE, 'Peru', 1);
insert into card values (17, 'Montevideo', FALSE, 'Uruguay', 1);

insert into card values (18, 'Bejing', TRUE, 'China', 1);
insert into card values (19, 'Astana', TRUE, 'Kasachstan', 1);
insert into card values (20, 'Neu-Delhi', TRUE, 'Indien', 1);
insert into card values (21, 'Islamabad', TRUE, 'Pakistan', 1);
insert into card values (22, 'Bankok', TRUE, 'Thailand', 1);

insert into card values (23, 'Wolkenbildung bezeichnet den Prozess der Entstehung von Wolken durch Kondensation
oder auch Resublimation von Wasserdampf an Kondensationskernen in der Troposphäre und teilweise auch Stratosphäre.',
                         FALSE, 'Was ist Wolkenbildung?', 1);

insert into card values (24, 'Thermische Aufwinde oder Hangaufwinde, Zufuhr von kälteren Luftmassen, Zufuhr von feuchteren Luftmassen',
                         FALSE, 'Wodurch können Wolken entstehen?', 1);

insert into card values (25, 'Cirrocumulus, Altocumulus, Cumulonimbus ',
                         FALSE, 'Was sind Mutterwolken der Gattung Cirrus?', 1);

insert into card values (26, 'avare', TRUE, 'geizig', 2);
insert into card values (27, 'essayer', TRUE, 'versuchen', 2);
insert into card values (28, 'la lune', TRUE, 'der Mond', 2);
insert into card values (29, 'voyaer', TRUE, 'reisen', 2);
insert into card values (30, 'la tournure', TRUE, 'die Redewendung', 2);
insert into card values (31, 'bon marché', TRUE, 'billig', 2);

insert into card values (32, 'MOV', TRUE, 'verschieben, Daten kopieren', 2);
insert into card values (33, 'RCL', TRUE, 'links rotieren mit carry', 2);
insert into card values (34, 'RET', TRUE, 'return aus Prozedur', 2);
insert into card values (35, 'CLC', TRUE, 'löschen vom carry-Flag', 2);

INSERT INTO card values (36, 'buenos días', TRUE, 'Guten Tag', 3);
INSERT INTO card values (37, 'la manzana', TRUE, 'der Apfel', 3);
INSERT INTO card values (38, 'la casa', TRUE, 'das Haus', 3);
INSERT INTO card values (39, 'estudiar', TRUE, 'studieren', 3);
INSERT INTO card values (40, 'la escuela', TRUE, 'die Schule', 3);
INSERT INTO card values (41, 'la informática', TRUE, 'die Informatik', 3);
INSERT INTO card values (42, 'el árbol', TRUE, 'der Baum', 3);
INSERT INTO card values (43, 'el mar', TRUE, 'das Meer', 3);
INSERT INTO card values (44, 'bailar', TRUE, 'tanzen', 3);
INSERT INTO card values (45, 'la computadora', TRUE, 'der Computer', 3);

INSERT INTO card VALUES (46, 'CREATE TABLE table_name (
    column1 datatype,
    column2 datatype,
   ....
); ', TRUE, 'Tabelle erstellen', 3);
INSERT INTO card VALUES (47, 'DROP TABLE table_name;', TRUE, 'Tabelle löschen', 3);


--select * from card_collections;
insert into card_collections values (1, 1),(2, 1),(3, 1),(4, 1),(5, 1),(6, 1),(7, 1),(8, 1),(9, 1),(10, 1),(11, 1),(12, 1),(13, 1),
                                    (14, 2),(15, 2),(16, 2),(17, 2),
                                    (18, 3),(19, 3),(20, 3),(21, 3),(22, 3),
                                    (23, 4),(24, 4),(25, 4),
                                    (26, 5),(27, 5),(28, 5),(29, 5),(30, 5),(31, 5),
                                    (32, 6),(33, 6),(34, 6),(35, 6),
                                    (36, 7),(37, 7),(38, 7),(39, 7),(40, 7),(41, 7),(42, 7),(43, 7),(44, 7),(45, 7),
                                    (46, 8),(47, 8);