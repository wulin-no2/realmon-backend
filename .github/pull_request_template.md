## Description

Describe the problem, the root cause when applicable, and the solution.

## Related issues

- Closes #[issue-id] (if applicable)

## Validation

List the commands and manual checks used to validate this change.

```text
./mvnw --batch-mode verify
```

## Change checklist

- [ ] The change is focused and contains no unrelated files.
- [ ] Tests were added or updated for changed behavior.
- [ ] Documentation was updated where needed.
- [ ] API compatibility with frontend consumers was considered.
- [ ] No secrets or environment-specific credentials are included.
- [ ] All repository content added by this PR is in English.

## Database and deployment impact

- [ ] No database change.
- [ ] Includes a new Flyway migration that has been reviewed.
- [ ] Requires a new or changed environment variable.
- [ ] Requires a special deployment step documented below.

Describe deployment ordering, compatibility requirements, and verification steps. Use `docs/DEPLOYMENT.md` for database releases.

## Rollback

Explain how to disable or roll back the change. State when a database restore or forward-fix migration would be required.

## Follow-up work

Link GitHub Issues for work intentionally left out of this pull request. Write `None` when there is no follow-up.
