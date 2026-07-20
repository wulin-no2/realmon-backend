# Repository Working Agreement

These instructions apply to the entire repository.

## Language

- Use English for all repository content, including source code, comments, documentation, commit messages, branch names, pull requests, and issues.
- Do not add Chinese or other non-English prose to tracked files.

## Git workflow

- Never commit directly to `main`.
- Create work branches from the latest `origin/main`.
- Codex-authored branches must use the `codex/` prefix.
- Keep each branch focused on one concern.
- Commit only files that belong to the current change.
- Open a draft pull request for review before merging.
- Merge only after required checks pass and the user explicitly approves the merge.

## Quality gates

- Run `./mvnw --batch-mode verify` before publishing backend changes.
- Add or update tests when behavior changes.
- Treat database migrations, authentication, authorization, secrets, and external-service changes as high-risk work that requires explicit review notes.
- Do not claim that production behavior or data has been verified unless it was actually inspected.

## Database changes

- Use versioned Flyway migrations for every schema change.
- Never use Hibernate schema mutation in deployed environments.
- Never edit a migration that may already have been applied; add a new migration instead.
- Follow `docs/DEPLOYMENT.md` for deployment and migration procedures.
- Back up an existing database before its first Flyway-managed deployment.
- Enable `FLYWAY_BASELINE_ON_MIGRATE` only for the documented first migration of an existing database, then remove it.

## Documentation and handoff

- Update operational documentation in the same pull request as the related behavior.
- Record durable architectural decisions under `docs/adr/`.
- Track unfinished, actionable work in GitHub Issues rather than relying on chat history or personal memory.
- Use the pull request template and document testing, deployment impact, rollback, and follow-up work.
