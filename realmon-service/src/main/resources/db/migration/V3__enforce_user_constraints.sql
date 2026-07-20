UPDATE `user` SET role = 'USER' WHERE role IS NULL;

ALTER TABLE `user`
    MODIFY COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

ALTER TABLE `user`
    ADD CONSTRAINT uk_user_username UNIQUE (username);

ALTER TABLE user_species
    ADD CONSTRAINT uk_user_species_user_species UNIQUE (user_id, species_id);
