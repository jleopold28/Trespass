create sequence sq_pk_entity;
create table tb_entity
(
	entity integer primary key default nextval('sq_pk_entity'),
	player_info json not null,
	username varchar(32) unique,
	password_hash text,
	created timestamp not null default now(),
	last_logged_in timestamp not null default now()
);

create sequence sq_pk_game;
create table tb_game
(
	game integer primary key default nextval('sq_pk_game'),
	player_1 integer not null references tb_entity,
	player_1_secret integer,
	player_1_tiles varchar,
	player_2 integer not null references tb_entity,
	player_2_secret integer,
	player_2_tiles varchar,
	created timestamp not null default now(),
	started timestamp,
	finished timestamp,
	aborted timestamp,
	winner integer references tb_entity
);

create sequence sq_pk_waiting_list;
create table tb_waiting_list
(
	waiting_list integer primary key default nextval('sq_pk_waiting_list'),
	player integer not null references tb_entity,
	tiles varchar,
	requested timestamp not null default now(),
	filled timestamp,
	invalidated timestamp
);

create sequence sq_pk_player_move;
create table tb_player_move
(
	player_move integer primary key default nextval('sq_pk_player_move'),
	game integer not null references tb_game,
	entity integer not null references tb_entity,
	from_position json not null,
	to_position json not null,
	game_piece integer check ( game_piece >= 0 and game_piece <= 9 ),
	moved timestamp not null default now()
);
create index on tb_player_move ( game, moved );