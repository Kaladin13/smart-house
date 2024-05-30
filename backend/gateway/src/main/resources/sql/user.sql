create table if not exists "user"
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login         varchar(256) unique not null,
    password_hash varchar(256)        not null
)