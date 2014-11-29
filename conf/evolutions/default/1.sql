# --- !Ups

create table persona (
  dni	 						number(10,0) not null,
  nombre	                    varchar(30) not null,
  --apellido						varchar(30) null,	
  constraint pk_persona   primary key (dni))
;



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists persona;

SET REFERENTIAL_INTEGRITY TRUE;

--drop sequence if exists seq_MLK_ANUNCIOS;