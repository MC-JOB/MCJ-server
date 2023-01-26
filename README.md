# Introduction

## 소개

마인크래프트라는 게임은 개인 서버를 창설할 수 있어 개발자 기획자 디자이너등을 구인 구직합니다.
<br/>
이러한 구인구직은 네이버 카페, 디스코드 앱으로 보통 진행이됩니다.
<br/>
굉장히 불편하고 매번 자기소개를 새롭게 해야하는 등 귀찮은 부분이 많습니다.
<br/>
그래서 마인크래프트 관련 구인 구직을 위한 사이트를 만들기 위해 해당 프로젝트를 시작 했습니다.

## 개발

- 개발 기간 : 2022.9 ~ ing
- 팀 구성 : 준혁, 경민
- 주요 기능 : 채용 프로세스 api 개발
- 개발 기술
    - Java : Spring-boot, JPA, flyway
    - issue : Jira, Slack
    - CI/CD : github action, EC2, S3, CodeDeploy, RDS
- 서버 목록
    - API server : http://ec2-15-165-81-53.ap-northeast-2.compute.amazonaws.com:8080
    - Rest Docs : http://mcjrestdocs.s3-website.ap-northeast-2.amazonaws.com
- 작업 현황
    - 준혁 : https://github.com/MC-JOB/MCJ-server/pulls?q=is%3Apr+is%3Aclosed+assignee%3Apeubel
    - 경민 : https://github.com/MC-JOB/MCJ-server/pulls?q=is%3Apr+is%3Aclosed+assignee%3Arud2403
- 작업 흐름
    - Jira 툴을 사용하여 이슈를 생성
    - IntelliJ를 이용한 이슈 브랜치 생성 및 작업 후 Pull Request ( Github action 테스트 코드 시작 )
    - 다른 작업자가 코드 리뷰 후 Merge ( Github action 테스트 코드 시작 및 배포 )