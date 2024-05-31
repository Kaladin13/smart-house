create table if not exists "user"
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login         varchar(255) unique not null,
    password_hash varchar(255)        not null
);

CREATE TABLE IF NOT EXISTS house
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    user_id UUID REFERENCES "user" (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS device
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS house_devices
(
    house_id  BIGINT REFERENCES house (id) ON DELETE CASCADE,
    device_id BIGINT REFERENCES device (id) ON DELETE CASCADE,
    PRIMARY KEY (house_id, device_id)
);

create table if not exists device_actions
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    device_id   BIGINT REFERENCES device (id)
);

insert into device
values (1, 'cleaner'),
       (2, 'light'),
       (3, 'climate');

insert into device_actions
values (1, 'clean', 'Start clean house', 1),
       (2, 'on', 'Turn on the light', 2),
       (3, 'off', 'Turn off the light', 2),
       (4, 'get', 'Get current illumination in lumens', 2),
       (5, 'set t <int>', 'Set temperature value', 3),
       (6, 'set p <int>', 'Set humidity value', 3);
