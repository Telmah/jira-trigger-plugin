# Setting Java

This project is still running an old Java version. Once you install Java, make sure you are setting it to your
environment variables.

Example in macOS:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home
export PATH=/Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/bin:$PATH
```

# Building Locally

- Running test and integration test: `./gradlew clean test`
- Running Jenkins with the plugin pre-installed: `./gradlew server`

See Jenkins [Gradle JPI Plugin page](https://wiki.jenkins-ci.org/display/JENKINS/Gradle+JPI+Plugin) for more details.

# Running Integration Test with JIRA

You will need to run JIRA locally to be able to execute the JIRA Integration Test of this plugin which is available from
vagrant. More details can be found
at [atlassian site](https://developer.atlassian.com/static/connect/docs/latest/developing/developing-locally.html).

Quick start:

1. `vagrant up`
2. `vagrant ssh`
3. Accept Oracle license term for Java
4. `atlas-run-standalone --product jira --version 7.0.0 --plugins com.atlassian.jira.tests:jira-testkit-plugin:7.0.111`
5. Go to http://localhost:2990/jira in your browser and setup a JIRA project with name TEST (port is forwarded)
6. `./gradlew jiraIntegrationTest`
7. Restart JIRA if it starts to complain about license (It is using [timebomb license](https://developer.atlassian.com/market/add-on-licensing-for-developers/timebomb-licenses-for-testing) by default).

Result of the acceptance test will be available at `$buildDir/reports/jiraIntegrationTest/index.html`.

# Release

1. Run `./gradlew clean build`
2. `git tag -m vx.x.x vx.x.x`
3. `./gradlew clean publish`

   Make sure your credentials are set correctly in ~/.jenkins-ci.org. Also check out [the official documentation](https://wiki.jenkins-ci.org/display/JENKINS/Gradle+JPI+Plugin) if there's problem.

4. `git push --tags`
5. Update CHANGELOG.md
