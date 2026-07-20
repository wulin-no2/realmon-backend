# ADR 0001: Use Flyway for Database Migrations

- Status: Accepted
- Date: 2026-07-20

## Context

The application previously used Hibernate `ddl-auto: update`. That setting changes a database implicitly during application startup, does not provide a reviewable migration history, and makes schema state difficult to reproduce across environments.

The project also needs a safe adoption path for an existing MySQL database that may predate migration history.

## Decision

- Flyway owns all database schema changes.
- Hibernate uses `ddl-auto: validate` to detect mismatches without changing the schema.
- New databases apply migrations from version 1.
- An existing non-empty database without Flyway history may be baselined at version 1 only during its first controlled Flyway deployment.
- Baseline-on-migrate is disabled by default and must not remain enabled after adoption.
- Precondition checks must stop a migration before new constraints are added when existing data violates those constraints.
- Applied migrations are immutable. Later corrections use new migration versions.

## Consequences

Schema changes become explicit, ordered, reviewable, and reproducible. Deployments that contain migrations require database-aware review and the checklist in `docs/DEPLOYMENT.md`.

The first deployment to an existing database requires a verified backup, duplicate-data checks, a temporary baseline setting, and post-migration verification. A failed migration may require restoring the backup because Flyway does not automatically undo MySQL DDL.

## Alternatives considered

### Continue using Hibernate schema update

Rejected because changes remain implicit and environment drift is difficult to audit.

### Recreate every database from scratch

Rejected because an existing environment may contain data that must be preserved.

### Use manual, unversioned SQL scripts

Rejected because they do not provide reliable ordering, checksums, or applied-version tracking.
