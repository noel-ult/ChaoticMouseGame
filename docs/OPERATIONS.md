# ChaoticMouseGame: verification and operations

Reviewed against [default-branch commit d9627688880f](https://github.com/noel-ult/ChaoticMouseGame/commit/d9627688880f27da915c0a1c1feefdd0ffe095b4) on 2026-10-28. Start with the [developer guide](PROJECT_GUIDE.md) for installation and architecture, and the [source reference](REFERENCE.md) for exact scripts, configuration readers, routes, and source locations.

## Java Swing desktop application

Compile the four Java sources with a JDK. In a dedicated graphical session, check pause/resume, effect toggles, cursor count/shape, game start/end, and application exit.

These are acceptance scenarios for a maintainer to run in the documented environment. Their inclusion is not a claim that live integrations or production credentials were tested during this documentation update.

## Deployment and failure diagnosis

Swing requires a graphical desktop; a headless server cannot display the application. Check desktop pointer-control permissions and rebuild the tracked Java sources for the target JDK.

## Maintaining the contract

When behavior changes, update the guide and examples in the same PR. Check the entry point and working directory before copying commands. Use the linked source readers to distinguish build-time configuration from process environment. For failures, capture the first application error, runtime version, selected mode, and redacted configuration names before later proxy or port errors.

Keep changes tied to evidence: a declared command is not proof it passes, a process health response is not a successful end-to-end workflow, and a historical report describes its own revision. Record actual check results and any unavailable external services in the PR.
