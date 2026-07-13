# HyperExecute SE Assignment Submission Notes

## Task 1: Fixed HyperExecute YAML

Corrected YAML:

- `yaml/linux/v1/hyperexecute_assignment_task1_task2.yaml`

Run command:

```bash
./hyperexecute --config yaml/linux/v1/hyperexecute_assignment_task1_task2.yaml --force-clean-artifacts --download-artifacts
```

Issues fixed/validated:

- `runson` is set to `linux`, matching the sample Linux TestNG suite and the Java runtime used by the assignment.
- `runtime.language` and `runtime.version` are set to Java 11, matching `pom.xml`.
- `testDiscovery` emits the known TestNG test names from `xml/testng_linux.xml` as static autosplit units, which avoids local OS differences before HyperExecute uploads the job.
- `testRunnerCommand` passes `-Dplatname=linux`, so Maven resolves `xml/testng_linux.xml` through `pom.xml`.
- `testRunnerCommand` passes `-DselectedTests=$test`, so each autosplit test name discovered by HyperExecute is handed to TestNG.
- Maven uses `-Dmaven.repo.local=./.m2`, matching the configured cache directory and avoiding repeated dependency downloads.

Evidence:

- Dashboard/job URL: https://hyperexecute.lambdatest.com/hyperexecute/task?jobId=67668873-ae72-436d-841d-3af646aad4ff
- Status: completed
- Total tasks: 4
- Failed tasks: 0
- CLI summary: discovery, pre, and test stages all passed at 100%.

## Task 2: Environment Variables

Updated YAML:

- `yaml/linux/v1/hyperexecute_assignment_task1_task2.yaml`

Relevant YAML section:

```yaml
env:
  ENVIRONMENT: staging

pre:
  - 'echo "ENVIRONMENT from HyperExecute pre step:"'
  - printenv ENVIRONMENT
  - mvn -Dmaven.repo.local=./.m2 dependency:resolve
```

Test code:

```java
String environment = System.getenv().getOrDefault("ENVIRONMENT", "ENVIRONMENT_NOT_SET");
System.out.println("ENVIRONMENT from TestNG test case: " + environment);
```

The code is in `src/test/java/Test1.java` inside `test1_element_addition_1`.

Expected log lines:

```text
ENVIRONMENT from HyperExecute pre step:
staging
ENVIRONMENT from TestNG test case: staging
```

Evidence:

- Dashboard/job URL: https://hyperexecute.lambdatest.com/hyperexecute/task?jobId=67668873-ae72-436d-841d-3af646aad4ff
- Downloaded pre-step log: `logs/67668873-ae72-436d-841d-3af646aad4ff/tasks/HYPL-3221364-1783925680266175290NCB/pre`
- Downloaded TestNG scenario log: `logs/67668873-ae72-436d-841d-3af646aad4ff/scenarios/Test_1`
- Pre-step log output:

```text
ENVIRONMENT from HyperExecute pre step:
staging
```

- Test execution log output:

```text
ENVIRONMENT from TestNG test case: staging
```

## Task 3: Force a Failure and Configure Retries

Intentional failure code:

- `src/test/java/ForceFailureTest.java`

Retry suite:

- `xml/testng_assignment_retry_linux.xml`

Retry YAML:

- `yaml/linux/v1/hyperexecute_assignment_task3_retry.yaml`

Run command:

```bash
./hyperexecute --config yaml/linux/v1/hyperexecute_assignment_task3_retry.yaml --force-clean-artifacts --download-artifacts
```

Relevant YAML section:

```yaml
retryOnFailure: true
maxRetries: 1
```

Expected log line from the test:

```text
Intentional failure test executed. HyperExecute should retry this test.
```

Evidence:

- Dashboard/job URL: https://hyperexecute.lambdatest.com/hyperexecute/task?jobId=0463a85b-2b9d-4137-ab35-42dc4b2f560e
- Status: failed as expected because the test has a hard `Assert.fail`.
- CLI retry evidence:

```text
x [1]  Test_Force_Failure (6s)
x [1]  {retry 1} Test_Force_Failure (3s)
```

- Job summary evidence:

```text
Failed test stage percentage: 100.00%
Failed Tasks: 1
```

## Task 4: Linux/Unix Basics

Sample input file:

- `assignment-sample.log`

Find all lines containing `FAIL`:

```bash
grep 'FAIL' assignment-sample.log
```

Prints only lines containing `FAIL`.

Sample output:

```text
2026-07-13T10:01:04Z FAIL staging Test_Force_Failure retry scheduled
2026-07-13T10:01:09Z FAIL staging Test_Force_Failure retry exhausted
```

Extract the second column from a space-delimited file:

```bash
awk '{print $2}' assignment-sample.log
```

Prints the status column.

Sample output:

```text
INFO
PASS
ERROR
FAIL
FAIL
```

Replace `staging` with `production`:

```bash
sed 's/staging/production/g' assignment-sample.log
```

Prints the file with every `staging` value replaced by `production`.

Sample output:

```text
2026-07-13T10:00:01Z INFO production Test_1 started
2026-07-13T10:00:22Z PASS production Test_1 completed
2026-07-13T10:01:01Z ERROR production Test_Force_Failure intentional failure
2026-07-13T10:01:04Z FAIL production Test_Force_Failure retry scheduled
2026-07-13T10:01:09Z FAIL production Test_Force_Failure retry exhausted
```

Pipe `grep` into `awk`:

```bash
grep 'FAIL' assignment-sample.log | awk '{print $4}'
```

Finds failed lines, then prints only the test name column.

Sample output:

```text
Test_Force_Failure
Test_Force_Failure
```

## Submission Checklist

- Include this `SUBMISSION.md` file with notes for all four tasks.
- Include the corrected Task 1/2 YAML: `yaml/linux/v1/hyperexecute_assignment_task1_task2.yaml`.
- Include the retry YAML: `yaml/linux/v1/hyperexecute_assignment_task3_retry.yaml`.
- Include the Java code changes: `src/test/java/Test1.java` and `src/test/java/ForceFailureTest.java`.
- Include the retry suite XML: `xml/testng_assignment_retry_linux.xml`.
- Include the Linux/Unix sample input file: `assignment-sample.log`.
- Keep the HyperExecute dashboard links in this file as job evidence.
- Keep `LT_USERNAME` and `LT_ACCESS_KEY` outside the repository.
- Do not commit generated local files such as `hyperexecute.exe`, `hyperexecute-cli.log`, `.hyperexecute/`, `logs/`, `target/`, or `.m2/`.
- Push the repo to GitHub and share the GitHub repo link for submission.
