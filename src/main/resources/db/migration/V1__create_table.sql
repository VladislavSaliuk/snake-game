
create table public.users (
    id bigserial not null,
    login varchar(255) not null unique,
    password varchar(255) not null,
    record bigint not null,
    primary key (id)
)