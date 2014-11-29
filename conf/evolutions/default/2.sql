# --- !Ups

--insert into persona (dni, nombre, apellido) values (1, 'yann', 'tiersen');
insert into persona (dni, nombre) values (1, 'yann');


# --- !DownsSaveOrUpdateImage
delete from persona;
