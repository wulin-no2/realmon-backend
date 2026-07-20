# Deployment Runbook

This runbook is the required checklist for deploying the RealMon backend. Do not rely on chat history or memory for deployment steps.

## Before every deployment

- [ ] Confirm the target environment and database.
- [ ] Confirm the pull request was reviewed and all required checks passed.
- [ ] Confirm the release commit is present on `main`.
- [ ] Review all Flyway migrations added since the currently deployed release.
- [ ] Confirm required secrets and environment variables are configured outside the repository.
- [ ] Confirm a rollback owner and maintenance window when the release changes the database.

## Standard deployment

1. Build and test the exact release commit:

   ```bash
   ./mvnw --batch-mode clean verify
   ```

2. Confirm `FLYWAY_BASELINE_ON_MIGRATE` is unset or `false`.
3. Deploy the application through the environment's normal deployment process.
4. Confirm the application starts successfully and Hibernate schema validation passes.
5. Check the health endpoint or a known read-only API request.
6. Check application logs for migration, database, authentication, and external-service errors.
7. Verify the latest expected version in `flyway_schema_history`:

   ```sql
   SELECT installed_rank, version, description, success, installed_on
   FROM flyway_schema_history
   ORDER BY installed_rank DESC;
   ```

## First Flyway deployment to an existing database

This procedure applies only when the database already contains application tables and does not contain `flyway_schema_history`.

1. Schedule a maintenance window and stop writes to the application.
2. Create a database backup and verify that it can be restored.
3. Confirm that `flyway_schema_history` does not exist.
4. Check for duplicate usernames:

   ```sql
   SELECT username, COUNT(*)
   FROM `user`
   WHERE username IS NOT NULL
   GROUP BY username
   HAVING COUNT(*) > 1;
   ```

5. Check for duplicate user collections:

   ```sql
   SELECT user_id, species_id, COUNT(*)
   FROM user_species
   WHERE user_id IS NOT NULL AND species_id IS NOT NULL
   GROUP BY user_id, species_id
   HAVING COUNT(*) > 1;
   ```

6. Stop if either query returns rows. Resolve the data deliberately and repeat both checks.
7. Set `FLYWAY_BASELINE_ON_MIGRATE=true` for this deployment only.
8. Deploy the application. Flyway will record baseline version 1 and apply later migrations.
9. Confirm that versions 1 through 3 are successful in `flyway_schema_history`.
10. Remove `FLYWAY_BASELINE_ON_MIGRATE` or set it to `false`.
11. Restart the application and confirm it starts normally with baseline disabled.
12. Resume application traffic and perform the post-deployment checks below.

Never enable baseline-on-migrate for a new empty database or as a general permanent setting. If the existing schema differs from the expected legacy schema, stop and investigate rather than forcing the migration.

## Post-deployment checks

- [ ] Application startup completed without Flyway or Hibernate validation errors.
- [ ] The expected Flyway version is marked successful.
- [ ] Authentication and one representative read-only request work.
- [ ] Error rates and database connectivity remain normal.
- [ ] The temporary baseline setting is disabled.
- [ ] The deployment result and any follow-up work are recorded in the release or issue.

## Rollback

Flyway migrations in this project are forward-only. Do not edit or delete an applied migration.

1. Stop application traffic if the release risks data integrity.
2. Roll back the application artifact only when the previous version is compatible with the migrated schema.
3. If the database must be restored, use the verified pre-deployment backup and follow the environment's database restore procedure.
4. Record the failure and recovery steps in a GitHub Issue.
5. Implement corrective schema work as a new Flyway migration.

The exact hosting platform and production deployment command are not yet documented. Add platform-specific commands here before the first production deployment.
