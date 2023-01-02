# 개요
- 프로젝트 기간 : 2022.10.21 ~ 2022.12.25
- 프로젝트의 목적 : 그동안 공부한 스프링부트와 스프링 데이터 JPA에 대한 지식을 실제로 적용시켜보기위해 간단한 쇼핑몰 웹사이트를 구현해봤습니다.
- 실제 배포 서버 링크 : http://ec2-43-200-168-173.ap-northeast-2.compute.amazonaws.com
- `id : admin / 비밀번호 : 1234` 로 로그인하시면 어드민계정으로 로그인 됩니다.
- 사용된 언어
  - JAVA 
  - HTML 
  - CSS
  - JS
- 사용된 기술 스택
  - SpringBoot
  - SpringDataJPA
  - SpringSecurity 
  - Thymeleaf 
  - jQuery 
  - QueryDSL
- ERD / svg 파일링크 - https://drive.google.com/drive/folders/19c5TbPtjITjNQtt4ZC4njXTSrFERT7w2
![image](https://user-images.githubusercontent.com/84609913/209609314-6fd06a5b-f5cd-4d7a-916f-5125175af988.png)

<br>

---
# 주요 트러블슈팅 & 기능 구현 사항
- N + 1 문제해결
  - 한번에 여러개의 엔티티객체가 필요할 경우 N + 1문제가 발생할 수 있습니다. 
  - 이러한 문제를 막고자 페치조인을 사용하여 문제를 해결했습니다. 
  - 세부구현 - https://github.com/Grara/porfol_shop/issues/1

<br>

- 쿼리DSL을 이용한 동적쿼리와 페이징 처리
  - 동적쿼리의 경우 단순 JPA로만 구현하기가 꽤 까다롭습니다.
  - 페이징 역시 마찬가지로 처음부터 모든걸 구현하기엔 번거롭습니다.
  - 이 둘 모두를 해결하기에 쿼리DSL이 적합하다고 생각되었습니다.
  - 세부구현 - https://github.com/Grara/porfol_shop/issues/2

<br>

- OAuth2로그인 구현
  - OAuth2를 이용해서 구글과 네이버 로그인을 구현했습니다.
  - Oauth
  - 세부구현 - https://github.com/Grara/porfol_shop/issues/3

<br>

- 로그설정
  - Controller, Service, Repository의 총 실행시간을 로그로 남기고싶어서 TimeLogAop를 구현했습니다.
  - 세부구현 -  https://github.com/Grara/porfol_shop/issues/4

<br>

- 기타AOP설정
  - Controller에서 발생하는 공통적인 예외를 처리하기 위해 AOP를 구현했습니다.
  - 세부구현 - https://github.com/Grara/porfol_shop/issues/5

<br>

- 무중단 배포 구현
  - 현재 만든 애플리케이션을 로컬PC에서만 실행하는게 아니라 실제 사이트로 배포해보고싶었습니다.
  - TravisCI, AWS, Nginx를 이용해서 무중단 배포를 구현했습니다.
  - 세부구현 -  https://github.com/Grara/porfol_shop/issues/7

---
# 끝나고 아쉬웠던 점
- 일단 급하게 만들어본다고 TDD를 적용해보지 못한게 아쉽습니다. 규모가 작을 떄는 괜찮았는데 규모가 커질수록 새로운 기능을 추가하거나 리팩토링했을 때 모든 기능이 정상적으로 작동하는지 테스트해보는게 굉장히 힘들다는걸 알았습니다. <br> 
다음에 프로젝트를 진행할 때는 TDD를 적용시켜봐야겠다고 생각이 들었습니다.
- 코딩을 직접 하는 것 만큼 기획이나 설계에도 많은 노력을 들여야된다는걸 느꼈습니다. <br>
`'처음에는 가볍게 대강 만들고 나중에 필요하면 추가하는 식으로 하자'` 이런식으로 진행을 했는데 직접 해보니까 기존에 만든 코드에서 변경하고 추가하는게 결과적으로 시간이 더 들었던 것 같습니다.
