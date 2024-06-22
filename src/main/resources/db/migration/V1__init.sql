create table users (
    id bigserial,
    username varchar(50) not null unique,
    password varchar(100) not null,
    email varchar(100) not null unique,
    primary key (id)
);

create table roles (
    id serial,
    name varchar(50) not null unique,
    primary key (id)
);

create table users_roles (
    user_id bigint not null,
    role_id int not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)
);

insert into roles (name) 
values 
    ('ROLE_USER'), ('ROLE_ADMIN');

insert into users (username, password, email) 
values ('user', '$2a$10$Vp9R8ApgPext9yxgWy.CgOn11SgXFopcAKgv2T9IZ4iOqfrg5TZV6', 'user@mail.ru'),
    ('admin', '$2a$10$Vp9R8ApgPext9yxgWy.CgOn11SgXFopcAKgv2T9IZ4iOqfrg5TZV6', 'admin@mail.ru');

insert into users_roles (user_id, role_id) 
values (1, 1),
    (2, 2);