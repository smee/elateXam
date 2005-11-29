This module contains integration tests for the elateXam exam server.
It runs selenium tests via a locally installed firefox browser. This module makes the following assumptions:

* installed firefox
* no productive elateXam instance! BEWARE: The directory ${user.home}/ExamServerRepository_examServer gets deleted!
* free port 8152
* maven 2

Windows:
Run via 'mvn clean integration-test'

Linux:
* install xvfb
Run via 'mvn clean selenium:xvfb integration-test'

Mac OS X:
On 10.6 FF crashes during tests, details: http://jira.openqa.org/browse/SRC-743
Quick fix: replace '*firefox' with '*safari'

Screenshot of test failures can be found in ./target/surefire-reports/Command line suite/*.png
 