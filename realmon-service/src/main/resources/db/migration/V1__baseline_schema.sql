CREATE TABLE daily_quest_progress (
    completed BIT NOT NULL,
    date DATE,
    progress INTEGER NOT NULL,
    reward_coins INTEGER NOT NULL,
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    extra_info TEXT,
    quest_type ENUM ('BONUS_QUEST', 'NEW_REALMON_CHALLENGE', 'TODAYS_CREATURE'),
    PRIMARY KEY (id)
);

CREATE TABLE inat_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    updated_at DATETIME(6),
    token VARCHAR(500),
    PRIMARY KEY (id)
);

CREATE TABLE `user` (
    coins INTEGER NOT NULL,
    id BIGINT NOT NULL AUTO_INCREMENT,
    avatar_url VARCHAR(255),
    expo_push_token VARCHAR(255),
    external_id VARCHAR(255),
    password VARCHAR(255),
    source VARCHAR(255),
    username VARCHAR(255),
    role VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE species (
    id VARCHAR(255) NOT NULL,
    image_url1 VARCHAR(255),
    image_url2 VARCHAR(255),
    image_url3 VARCHAR(255),
    name VARCHAR(255),
    scientific_name VARCHAR(255),
    wiki_url VARCHAR(255),
    category ENUM ('ALGAE', 'AMPHIBIAN', 'ARACHNID', 'BIRD', 'CRUSTACEAN', 'FISH', 'FUNGI', 'INSECT', 'MAMMAL', 'MARINE_INVERTEBRATE', 'MOLLUSC', 'OTHER', 'PLANT', 'REPTILE'),
    PRIMARY KEY (id)
);

CREATE TABLE observation (
    latitude DOUBLE,
    longitude DOUBLE,
    id BIGINT NOT NULL AUTO_INCREMENT,
    observed_at DATETIME(6),
    user_id BIGINT,
    image_url VARCHAR(255),
    source VARCHAR(255),
    species_id VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_observation_species FOREIGN KEY (species_id) REFERENCES species (id),
    CONSTRAINT fk_observation_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);

CREATE TABLE notification (
    created_at DATETIME(6),
    id BIGINT NOT NULL AUTO_INCREMENT,
    sent_at DATETIME(6),
    user_id BIGINT,
    body VARCHAR(500),
    data_json TEXT,
    title VARCHAR(255),
    status ENUM ('FAILED', 'PENDING', 'SENT'),
    PRIMARY KEY (id),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);

CREATE TABLE post (
    latitude DOUBLE,
    longitude DOUBLE,
    created_at DATETIME(6),
    id BIGINT NOT NULL AUTO_INCREMENT,
    observation_id BIGINT,
    observed_at DATETIME(6),
    species_id BIGINT,
    user_id BIGINT,
    avatar_url VARCHAR(255),
    description VARCHAR(255),
    image_url VARCHAR(255),
    location VARCHAR(255),
    species_icon VARCHAR(255),
    species_name VARCHAR(255),
    username VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE realmon (
    latitude DOUBLE,
    longitude DOUBLE,
    id BIGINT NOT NULL AUTO_INCREMENT,
    icon VARCHAR(255),
    image_url VARCHAR(255),
    name VARCHAR(255),
    species VARCHAR(255),
    wiki_url VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE species_details (
    fun_fact TEXT,
    icon VARCHAR(255),
    life_cycle TEXT,
    name VARCHAR(255),
    protection_level TEXT,
    scientific_name VARCHAR(255),
    species_id VARCHAR(255) NOT NULL,
    symbolism TEXT,
    texture TEXT,
    wiki_url VARCHAR(255),
    category ENUM ('ALGAE', 'AMPHIBIAN', 'ARACHNID', 'BIRD', 'CRUSTACEAN', 'FISH', 'FUNGI', 'INSECT', 'MAMMAL', 'MARINE_INVERTEBRATE', 'MOLLUSC', 'OTHER', 'PLANT', 'REPTILE'),
    PRIMARY KEY (species_id)
);

CREATE TABLE user_species (
    collected_at DATETIME(6),
    id BIGINT NOT NULL AUTO_INCREMENT,
    observation_id BIGINT,
    user_id BIGINT,
    species_id VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_user_species_observation UNIQUE (observation_id),
    CONSTRAINT fk_user_species_observation FOREIGN KEY (observation_id) REFERENCES observation (id),
    CONSTRAINT fk_user_species_species FOREIGN KEY (species_id) REFERENCES species (id),
    CONSTRAINT fk_user_species_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);

CREATE INDEX idx_daily_quest_user_date ON daily_quest_progress (user_id, date);
CREATE INDEX idx_observation_location ON observation (latitude, longitude);
CREATE INDEX idx_observation_observed_at ON observation (observed_at);
CREATE INDEX idx_post_created_at ON post (created_at);
CREATE INDEX idx_user_species_user ON user_species (user_id);
