# 개요
- 프로젝트 기간 : 2022.10.21 ~ 2022.12.25
- 프로젝트의 목적 : 그동안 공부한 스프링부트와 스프링 데이터 JPA에 대한 지식을 실제로 적용시켜보기위해 CRUD게시판을 직접 구현해봤습니다.
- 실제 배포 서버 링크 : http://ec2-43-200-168-173.ap-northeast-2.compute.amazonaws.com
- `id : admin / 비밀번호 : 1234` 로 로그인하시면 어드민계정으로 로그인 됩니다.
- HTML과 CSS에 대한 지식이 전무했기때문에 김영한님의 JPA강의에서 실습으로 만들어본 사이트를 토대로 시작했습니다.
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1
- ERD / svg 파일링크 - https://drive.google.com/drive/folders/19c5TbPtjITjNQtt4ZC4njXTSrFERT7w2
![image](https://user-images.githubusercontent.com/84609913/209609314-6fd06a5b-f5cd-4d7a-916f-5125175af988.png)

<br>

---
# 주요 트러블슈팅 & 기능 구현 사항
## N + 1 문제 해결
- 상품 상세 페이지를 출력하기 위해서는 상품, 상품의 후기, 후기 작성자 총 3개의 엔티티가 필요합니다.
![image](https://user-images.githubusercontent.com/84609913/209610226-d6b8dc04-39c8-482a-9bd7-fae3b7d4238f.png) 

<br>

- 이 경우 JPA에서 Item엔티티만 가져왔을 때 최악의 경우, 1 + 후기의 갯수 * 2 번 만큼 쿼리가 발생합니다.
- 이뿐만 아니라 OSIV를 비활성화할 경우에도 문제가 발생합니다.
- 이 문제를 해결하기 위해 페치조인을 사용했습니다.
![image](https://user-images.githubusercontent.com/84609913/209610672-9ee824d9-cc95-458b-bce9-605f19738ff3.png)

<br>

- ajax를 이용한 api요청과 응답 구현
- 프로젝트 초창기 때 JS에 대한 지식이 없었기때문에 가능한 스프링과 타임리프만을 이용해서 구현을 하려고했습니다.
- 하지만 장바구니화면에서 동적으로 상품을 선택해서 삭제하거나 주문을 하는 기능을 구현하려고했는데, 타임리프만으로는 동적인 처리에 대한 구현이 많이 어려웠습니다.
- 이를 해결하기위해 JS, jQuery, ajax를 이용할 수 밖에 없다고 생각했습니다.
- 이후 ajax를 이용한 api요청과 응답을 구현했습니다.
- 클라이언트 측 구현

    ![image](https://user-images.githubusercontent.com/84609913/209611716-f1ef3fe2-bbff-4837-990f-5288a67b336f.png)

    ![image](https://user-images.githubusercontent.com/84609913/209611761-2ad9235b-7ddb-4f1b-9034-0d6a169d0a73.png)

<br>

- 서버 측 구현

    ![image](https://user-images.githubusercontent.com/84609913/209674859-46676b80-cb3a-4f23-9af8-f12f5730a0b5.png)

<br>

---
## 이미지 파일 저장과 웹에서 출력
- 프로젝트를 진행하던 도중 회원의 프로필 사진이나 상품의 이미지를 어떻게 출력해야할지 고민하게 됐습니다.
- 현재 프로젝트는 굉장히 소규모여서 단순히 회원 엔티티에 프로필 이미지 파일의 경로를 저장해도 되긴하지만, 확장성을 고려해 파일의 메타데이터를 저장할 FileEntity를 생성해서 관리했습니다.
- FileEntity 구현 부분

    ![image](https://user-images.githubusercontent.com/84609913/209612634-5ba3ebbc-08a4-4f09-8b8c-ce1745deae89.png)

<br>

- Member(회원) 엔티티 구현 부분

    ![image](https://user-images.githubusercontent.com/84609913/209612792-1da64544-12a7-4439-8a18-e9caa33f4980.png)

<br>

- 클라이언트 측 구현

    ![image](https://user-images.githubusercontent.com/84609913/209613116-90bdf4fc-d0bd-4dac-92f0-d139536f953c.png)

<br>

- 서버 측 구현

    ![image](https://user-images.githubusercontent.com/84609913/209613210-85377c18-9e36-4c04-bf36-812de296b08b.png)


---
## 쿼리DSL을 이용한 동적쿼리와 페이징 처리
- 조건을 이용한 검색의 경우 동적쿼리가 필요하며, 동적쿼리의 경우 단순JPA만으로는 구현하기가 어렵습니다.
- 페이징 역시 모든걸 혼자서 구현하기에는 번거로운 부분이 많습니다.
- 2가지를 동시에 해결하기에 쿼리DSL이 적합하다고 생각이 되서 쿼리DSL을 이용해서 구현했습니다.

- 구현
  - 클라이언트 측 

    ![image](https://user-images.githubusercontent.com/84609913/209614708-aa2e8465-ba74-4f06-8e32-3cc5c5c7fbb8.png)

    ![image](https://user-images.githubusercontent.com/84609913/209614724-8cb2fcda-7624-4149-815e-c78e1cad7751.png)

  <br>

  - 서버 측
    - 쿼리DSL을 이용하기 위해서 아래의 클래스 다이어그램에 나온대로 상속을 적용했습니다.
    - ItemRepository를 이용해서 메소드를 사용하지만 실제 구현은 ItemQueryRepositoryImpl에서 합니다.

        ![image](https://user-images.githubusercontent.com/84609913/209640025-88062f31-b94b-4435-9ded-475bf357ae27.png)

    <br>

    - 클라이언트측에서 form으로 제출하면 아래에 나오는 ItemSearchCondition으로 받습니다.

        ![image](https://user-images.githubusercontent.com/84609913/209640448-25901f6b-7652-4cac-8bfe-550475de9bb7.png)

        ![image](https://user-images.githubusercontent.com/84609913/209640552-5b0c9cdc-cd01-4f0a-b483-aac96241dc33.png)
    
    <br>
    
    - ItemSearchCondition과 Pageable을 인자로 받아 searchWithPage를 호출합니다.
    - where 조건문에 있는 메소드들의 경우 null을 반환할 경우 해당 조건은 자동으로 무시됩니다.
    - 페이지네이션에 필요한 데이터도 같이 넘겨줍니다.
    - 실제 메소드 구현부분

        ![image](https://user-images.githubusercontent.com/84609913/209641123-bc5058d8-146e-4ef0-ba1f-8ab00cc9cc64.png)
        ![image](https://user-images.githubusercontent.com/84609913/209641695-c0bb9271-93b4-4741-8ec7-cb9dcb7c7ed9.png)
    
    <br>
    
    - Controller에서 반환받은 PageImpl을 이용해 Model에 데이터를 넣어줍니다.
    - 상품뿐만 아니라 회원이나 주문 검색도 있기때문에 공통적인 부분은 pagingCommonTask를 통해서 처리합니다.
    
        ![image](https://user-images.githubusercontent.com/84609913/209642243-76af96d8-d54c-4029-b161-1eb7284fdb48.png)
        ![image](https://user-images.githubusercontent.com/84609913/209642306-9e5df88a-2a50-4f7b-8490-aa009c85a102.png)
    
    <br>
    
    - 클라이언트측에서는 타임리프를 이용해서 페이지네이션을 해줍니다.
    
        ![image](https://user-images.githubusercontent.com/84609913/209642943-eceaf9ec-de9f-4129-b300-b07c0d682391.png)

    <br>

---
## OAuth2로그인 구현
  - OAuth2를 이용해서 구글과 네이버 로그인을 구현했습니다.
  - 구글과 네이버에서 api설정을 해준뒤 application.yml을 설정해줍니다.
    ```yml
    spring:  
        security:
        oauth2:
            client:
            registration:
                google:
                client-id: 클라이언트 id
                client-secret: 클라이언트 보안 번호
                scope: profile,email
                naver:
                client-id: 클라이언트 id
                client-secret: 클라이언트 보안 번호
                redirect-uri: https://localhost:8080/login/oauth2/code/naver
                authorization-grant-type: authorization_code
                client-authentication-method: POST
                scope: name,email,profile_image
                client-name: Naver
            provider:
                naver:
                authorization-uri: https://nid.naver.com/oauth2.0/authorize
                token-uri: https://nid.naver.com/oauth2.0/token
                user-info-uri: https://openapi.naver.com/v1/nid/me
                user-name-attribute: response
    ```
  - OAuth2로그인 시 세션 정보를 등록해주기 위해 OAuth2UserService를 구현한 클래스를 생성해줍니다.
  - 기존에 폼로그인의 세션 정보도 해당 클래스에서 등록합니다.
  
    ![image](https://user-images.githubusercontent.com/84609913/209649484-8050a824-1df6-4ada-a1f0-ed283ee92753.png)
  
    ```java
    @Service
    @RequiredArgsConstructor
    public class LoginService implements OAuth2UserService, UserDetailsService {
        private final MemberRepository memberRepository;
        private final MemberService memberService;
        private final PasswordEncoder encoder;

        //OAuth2 로그인 시 유저 정보 등록
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String userNameAttrName = userRequest.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();


            OAuthAttributes attr = OAuthAttributes.of(registrationId, userNameAttrName, oAuth2User.getAttributes());


            Member member = memberRepository.findByEmail(attr.getEmail()).orElse(attr.toEntity(encoder));
            MemberDto dto = new MemberDto(member);

            return new UserAdapter(dto, attr.getAttributes());
        }

        @Override
        //UserDetailsService의 메소드 구현
        //로그인 시 자동으로 시큐리티에서 관리할 유저정보를 생성
        public UserDetails loadUserByUsername(String username) {
            Member member = memberService.findByUserName(username);
            MemberDto dto = new MemberDto(member); //Proxy 관련 문제로 dto를 생성
            return new UserAdapter(dto);
        }
    }
    ```
  
  - 로그인한 포털에 따라 받는 데이터의 형식이 다르므로 OAuthAttributes를 구현하여 하나의 형식으로 통일시킵니다.
  
    ```java
    
    /**
    * OAuth2 로그인 시 속성으로 사용되는 클래스입니다.
    *
    */
    @Getter
    @ToString
    public class OAuthAttributes {

        private Map<String, Object> attributes;
        private String nameAttributeKey;
        private String name;
        private String email;

        @Builder
        public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
            this.attributes = attributes;
            this.nameAttributeKey = nameAttributeKey;
            this.name = name;
            this.email = email;
        }

        /**
        * 로그인한 포털에 따라 속성을 설정해서 반환해줍니다.
        *
        * @param registrationId 포털 이름
        * @param userNameAttributeName 유저의 id값의 key
        * @param attributes OAuth로그인 시 전달받은 속성값
        */
        public static OAuthAttributes of(String registrationId,
                                        String userNameAttributeName,
                                        Map<String, Object> attributes){
            if(registrationId.equals("google")) {
                return ofGoogle(userNameAttributeName, attributes);
            }


            return ofNaver(userNameAttributeName, attributes);

        }

        /**
        * 네이버 로그인 시 속성을 설정해서 반환해줍니다.
        *
        * @param userNameAttributeName 유저의 id값의 key
        * @param attributes OAuth로그인 시 전달받은 속성값
        */
        private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

            //key를 response해서 찾아야 됨
            Map<String, Object> res = (Map<String, Object>) attributes.get(userNameAttributeName);
            return OAuthAttributes.builder()
                    .name((String) res.get("name"))
                    .email((String) res.get("email"))
                    .attributes(res)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }

        /**
        * 구글 로그인 시 속성을 설정해서 반환해줍니다.
        *
        * @param userNameAttributeName 유저의 id값의 key
        * @param attributes OAuth로그인 시 전달받은 속성값
        */
        private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
            return OAuthAttributes.builder()
                    .name((String) attributes.get("name"))
                    .email((String) attributes.get("email"))
                    .attributes(attributes)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }

        /**
        * 현재 속성을 바탕으로 새로운 Member객체를 생성해서 반환합니다.
        *
        * @param encoder 사용할 PasswordEncoder
        */
        public Member toEntity(PasswordEncoder encoder) {
            return Member.builder()
                    .userName(name)
                    .email(email)
                    .password(encoder.encode("1234"))
                    .role(Role.ROLE_GUEST)
                    .build();
        }
    }
    ```
  - OAuthAttributes를 이용해 UserAdapter를 생성하여 세션정보를 등록합니다.
  - UserAdapter의 경우 OAuth2User와 UserDetails를 저장할 수 있는 클래스인 CustomUserDetails를 상속한 클래스입니다.
  - 따라서 UserAdapter를 등록하면 컨트롤러측에서 세션정보가 필요할 때 인자에 <br>
  `@AuthenticationPrincipal UserAdapter`를 사용할 경우 로그인 형식 구분없이 세션정보를 가져올 수 있습니다.
  
    ![image](https://user-images.githubusercontent.com/84609913/209647345-60a67cd0-5495-451b-8a0c-299ffe92b869.png)

    <br>

  - UserAdapter와 CustomUserDetails 구현부분
    ```java

    /**
    * CustomUserDetails을 상속받은 유저 세션 정보입니다. <br/>
    * Controller에서 실제로 사용하는 클래스입니다.
    */
    @Getter
    @ToString
    public class UserAdapter extends CustomUserDetails {
        private MemberDto member;
        private Map<String,Object> attributes;

        public UserAdapter(MemberDto member) {
            super(member);
            this.member = member;
        }

        public UserAdapter(MemberDto member, Map<String, Object> attributes) {
            super(member, attributes);
            this.member = member;
            this.attributes = attributes;
        }
    }
    ```

    ```java
    /**
    * 폼 로그인, OAuth2 로그인 시 등록되는 유저 세션 정보입니다.
    *
    */
    public class CustomUserDetails implements UserDetails, OAuth2User {

        private MemberDto member;
        private Map<String, Object> attributes;

        //폼 로그인 시
        public CustomUserDetails(MemberDto member) {
            this.member = member;
        }

        //OAuth로그인 시
        public CustomUserDetails(MemberDto member, Map<String, Object> attributes) {
            this.member = member;
            this.attributes = attributes;
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            return Arrays.asList(new SimpleGrantedAuthority(member.getRole().toString()));
        }

        public void setMember(MemberDto member) {
            this.member = member;
        }

        @Override
        public String getName() {
            return member.getUserName();
        }

        public void setName(String name) { this.member.setUserName(name); }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public String getPassword() {
            return member.getPassword();
        }

        @Override
        public String getUsername() {
            return member.getUserName();
        }
    
        ....

    }
    ```
    <br>

  - 다만 OAuth2로그인의 경우 이미 가입한 회원이 아니면 권한을 Guest로 설정하고, 회원가입을 하도록 합니다.

    ![image](https://user-images.githubusercontent.com/84609913/209650253-051e5b62-53ee-4bd2-8e9f-cd4b26bd5ddf.png)

<br>

---
## 로그 설정
  - 로그의 경우 로그백을 사용해서 구현했습니다.
  - AOP를 이용해서 Controller, Service, Repository의 실행완료까지 걸리는 시간을 로그에 남겨보기로 했습니다.
  - TimeLogAop 구현
    ```java
    @Aspect
    @Order(0)
    @Slf4j
    @Component
    public class TimeLogAop {

        Logger logger = LoggerFactory.getLogger(TimeLogAop.class);

        /**
        * Controller, ApiController, Service, Repository의 모든 메소드 작업 수행시간을 로그로 남겨줍니다.
        * <br/>메소드 실행 도중 예외가 발생한다면 로그에 남겨줍니다.
        *
        * @param joinPoint AOP가 적용되는 메소드
        * @return : 실행한 메소드의 리턴값
        */
        @Around("within(pofol.shop.controller.*) || within(pofol.shop.api.*) || within(pofol.shop.service.business.*) || execution(* pofol.shop.repository..*(..))")
        public Object logProcessTime(ProceedingJoinPoint joinPoint) throws Throwable {

            //이름에 File이나 Crud가 들어간건 제외함
            boolean isNotFileOrCrud = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

            long start = System.currentTimeMillis();

            if (isNotFileOrCrud) {

                if (joinPoint.toString().contains("Controller") || joinPoint.toString().contains("ApiController")) {
                    //다른 컨트롤러 호출과 구분하기위해 -----표시
                    logger.info("----------------------------------------------------------------");
                }

                logger.info("START: " + joinPoint.toString()); //현재 시작한 클래스, 메소드명 기록
            }

            try {
                return joinPoint.proceed();

            } catch (Exception e) {
                throw e;

            } finally { //종료된 클래스, 메소드명과 시작부터 종료까지 총 실행시간 기록
                if (isNotFileOrCrud) {
                    long finish = System.currentTimeMillis();
                    long processingTime = finish - start;
                    logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
                }
            }
        }
    }
    ```
  - logback-spring 설정
  - 프로파일 별로 저장위치를 다르게 설정했습니다.
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>

    <configuration scan="true">
        <springProfile name="test"> <!--프로파일이 test일 경우-->
            <property resource="logback-test.properties"/>
        </springProfile>
        <springProfile name="real1"> <!--프로파일이 real1일 경우-->
            <property resource="logback-deploy.properties"/>
        </springProfile>
        <springProfile name="real2"> <!--프로파일이 real2일 경우-->
            <property resource="logback-deploy.properties"/>
        </springProfile>


        <property name="ABS_PATH" value="${log.file.path}"/> <!--오늘 로그 저장 위치-->
        <property name="PREV_ABS_PATH" value="${log.file.prev-path}"/> <!--이전 로그 저장 위치-->


        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender"> <!--콘솔 로그 Appender-->
            <layout class="ch.qos.logback.classic.PatternLayout">
                <Pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{36} - %msg%n</Pattern>
            </layout>
        </appender>

        <!--콘솔로그에 찍힌걸 파일로 저장하는 Appender-->
        <appender name="CONSOLE_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${ABS_PATH}/console.log</file>
            <encoder>
                <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${PREV_ABS_PATH}/console.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>180</maxHistory>
            </rollingPolicy>
        </appender>

        <!--Error로그를 파일로 저장하는 Appender-->
        <appender name="ERROR_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${ABS_PATH}/error.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>ERROR</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${PREV_ABS_PATH}/error.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>180</maxHistory>
            </rollingPolicy>
        </appender>

        <!--Warn로그를 파일로 저장하는 Appender-->
        <appender name="WARN_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${ABS_PATH}/warn.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>WARN</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${PREV_ABS_PATH}/warn.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>180</maxHistory>
            </rollingPolicy>
        </appender>

        <!--Info로그를 파일로 저장하는 Appender-->
        <appender name="INFO_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${ABS_PATH}/info.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>INFO</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${PREV_ABS_PATH}/info.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>180</maxHistory>
            </rollingPolicy>
        </appender>

        <!--Debug로그를 파일로 저장하는 Appender-->
        <appender name="DEBUG_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${ABS_PATH}/debug.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>DEBUG</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${PREV_ABS_PATH}/debug.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>180</maxHistory>
            </rollingPolicy>
        </appender>

        <!--Trace로그를 파일로 저장하는 Appender-->
        <appender name="TRACE_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${ABS_PATH}/trace.log</file>
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <level>TRACE</level>
                <onMatch>ACCEPT</onMatch>
                <onMismatch>DENY</onMismatch>
            </filter>
            <encoder>
                <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${PREV_ABS_PATH}/trace.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>180</maxHistory>
            </rollingPolicy>
        </appender>

        <!--root로거 = 기본로거-->
        <root level="INFO">
            <appender-ref ref="STDOUT"/>
            <appender-ref ref="CONSOLE_LOG"/>
            <appender-ref ref="ERROR_LOG"/>
            <appender-ref ref="WARN_LOG"/>
        </root>

        <!--aop를 이용한 로거-->
        <logger name="pofol.shop.aop" additivity="false" level="DEBUG">
            <appender-ref ref="ERROR_LOG"/>
            <appender-ref ref="WARN_LOG"/>
            <appender-ref ref="INFO_LOG"/>
            <appender-ref ref="DEBUG_LOG"/>
            <appender-ref ref="TRACE_LOG"/>
        </logger>

        <!--sql로그는 test 프로파일 환경에서 콘솔에만-->
        <logger name="org.hibernate.SQL" additivity="false" level="${sql.log.level}">
            <appender-ref ref="STDOUT"/>
        </logger>

    </configuration>
    ```
  - logback-test.properties 설정
    ```
    log.file.path = ./logs
    log.file.prev-path = ./prev-logs
    sql.log.level= DEBUG
    ```
  - logback-deploy.properties 설정
    ```
    log.file.path = /home/ec2-user/app/helloshop/logs
    log.file.prev-path = /home/ec2-user/app/helloshop/prev-logs
    sql.log.level= INFO
    ```
  - 전체적인 구조

    ![image](https://user-images.githubusercontent.com/84609913/209655574-10fa46ff-e1c6-4b14-bcc4-16e64a7a0824.png)

  - info.log파일에 저장된 일부분

    ![image](https://user-images.githubusercontent.com/84609913/209656038-e2762590-c63c-4f3a-9db9-45f9062be58e.png)

<br>

---
## 그 외 AOP 설정
- 로그인 세션 정보가 필요한 Controller 메소드들의 경우 세션정보가 null일 경우 NPE가 발생합니다.
- 이를 막기위해 세션정보가 필요한 메소드에서 각각 일일이 null체크를 하는것은 너무 비효율적이라고 판단됐습니다.
- 때문에 세션정보가 null인지 미리 체크해주는 AOP를 구현했습니다.
- AuthAop가 발생시킨 예외는 더 아래에서 나올 CommonExceptionAop에서 처리해줍니다.
    ```java
    @Aspect
    @Order(2)
    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class AuthAop {

        private final PasswordEncoder encoder;
        private final LogService logService;
        Logger logger = LoggerFactory.getLogger(TimeLogAop.class);

        /**
        * Controller와 API요청 중 로그인 세션이 필요한 요청을 호출할 때 세션이 null인지 체크합니다.<br/>
        * null이라면 AuthenticationException를 던집니다.
        *
        * @param joinPoint 실행할 메소드
        * @param principal 메소드의 인자로 들어온 로그인 세션 정보
        * @return : 로그인을 안했으면 에러를 반환
        */
        @Order(1)
        @Around("( execution(* pofol.shop.api..*(..)) || execution(* pofol.shop.controller..*(..)) ) && args(.., principal)")
        public Object apiAuthNullCheck(ProceedingJoinPoint joinPoint, UserAdapter principal) throws Throwable {
            if (principal == null) {
                throw new AuthenticationException("로그인이 필요합니다.") {
                };
            }
            return joinPoint.proceed();
        }

        /**
        * Controller와 API요청 중 폼의 대상 회원과 요청을 보낸 로그인 세션이 일치하는지 확인합니다. <br/>
        * 일치하지 않으면 AuthenticationException을 던집니다.
        *
        * @param joinPoint 실행할 메소드
        * @param form      대상이 되는 유저이름을 필드로 지닌 폼 객체
        * @param principal 메소드의 인자로 들어온 로그인 세션 정보
        */
        @Order(2)
        @Around("( execution(* pofol.shop.api..*(..)) || execution(* pofol.shop.controller..*(..)) )&& args(form,.., principal)")
        public Object apiSessionCheck(ProceedingJoinPoint joinPoint, UserNameRequiredForm form, UserAdapter principal) throws Throwable {
            if (!form.getUserName().equals(principal.getUsername())) {
                throw new AuthenticationException("요청을 보낸 회원과 대상이 되는 회원이 다릅니다.") {
                };
            }
            return joinPoint.proceed();
        }
    }
    ```

- 메소드마다 특별하게 발생할 수 있는 예외는 각각의 메소드가 받아서 처리하고 공통적인 부분은 CommonExceptionAop가 처리해줍니다.
- 예를들어 Repository에서 엔티티객체를 Optional로 받고 orElseThrow를 사용했을 때 Optional에 객체가 없다면 NoSuchElementException이 발생합니다.
- 이러한 예외의 경우 모든 컨트롤러에서 공통적으로 발생할 수 있고, 일일이 메소드나 컨트롤러에서 처리하는게 비효율적이고 try-catch가 많아져서 코드의 가독성이 저하된다고 생각됐습니다.
- 때문에 메소드에서 이러한 예외를 던지면 처리해줄 AOP를 구현했습니다.
    ```java
    /**
    * Controller, ApiController들의 공통적인 예외를 처리하는 AOP 클래스입니다.
    *
    */
    @Aspect
    @Order(1)
    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class CommonExceptionAop {
        Logger logger = LoggerFactory.getLogger(TimeLogAop.class);
        private final LogService logService;

        /**
        * Controller와 API요청에 대한 공통적인 예외를 처리하는 메소드입니다.
        *
        */
        @Around("within(pofol.shop.controller.*) || within(pofol.shop.api.*)")
        public Object controllerExceptionCatch(ProceedingJoinPoint joinPoint) throws Throwable {
            boolean isNotAPI = (!joinPoint.toString().contains("Api")); //일반 Controller 요청일 경우

            try {
                return joinPoint.proceed();

            } catch (NoSuchElementException e) { //DB에서 가져온 Optional이 null일 경우
                logService.logError(joinPoint, e);
                if (isNotAPI) return "errors/noSuchElement";
                else {
                    return ResponseEntity
                            .badRequest()
                            .body(new ApiResponseBody<>(HttpStatus.BAD_REQUEST, e.getMessage(), false));
                }

            } catch (AuthenticationException e) { //AuthAop에서 예외가 생겼을 경우
                logService.logError(joinPoint, e);
                if(isNotAPI) return "errors/authenticationError";
                else {
                    return ResponseEntity
                            .internalServerError()
                            .body(new ApiResponseBody<>(HttpStatus.FORBIDDEN, e.getMessage(), false));
                }
            }
            catch (Exception e){ //그 외에 모든 예외
                logService.logError(joinPoint, e);
                if(isNotAPI) throw e; //에러페이지를 따로 만듬
                else {
                    return ResponseEntity
                            .internalServerError()
                            .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false));
                }
            }
        }
    }
    ```

- 전체적인 AOP구조
  - 바깥쪽에 있을수록 먼저 적용되며, 안쪽에 있는 AOP나 메소드의 return이나 throw를 이어받습니다.

    ![image](https://user-images.githubusercontent.com/84609913/209660135-9b9c6b25-5a74-42f2-9b8f-9b330e245111.png)

<br>

---
## Travis CI와 NginX를 이용한 무중단 배포 구현

- `스프링 부트와 AWS로 혼자 구현하는 웹 서비스` 라는 책을 참고했습니다.
- 배포의 전체적인 구조
  - EC2내부에서 애플리케이션 두개가 8081포트와 8082포트를 사용해서 실행됩니다.
  - NginX는 리버스 프록시를 이용해 둘 중 한개의 포트와 연결됩니다.
  - 배포가 진행되면 현재 NginX와 연결되어있지않은 포트로 새로운 애플리케이션이 실행되며, 이후에 NginX는 새로 실행된 애플리케이션의 포트로 연결을 바꿉니다.

    ![image](https://user-images.githubusercontent.com/84609913/209660781-d23a190e-12cb-46ac-9a58-7d66d8072aef.png)

<br>

- 보안과 관련되거나 AWS 웹콘솔을 이용한 설정 부분에 대한 설명은 생략했습니다.
- 보안과 관련된 전체구조는 다음과 같습니다.

    ![image](https://user-images.githubusercontent.com/84609913/209672141-276b783e-5dcc-463b-bf90-04a81107b502.png)

<br>

- 부분 별 구현
- 기본 설정
  - AWS의 EC2서버와 RDS를 준비해줍니다.
  - Travis CI에 들어간 후 GitHub저장소를 연동해줍니다.

<br>

- `TravisCI - AWS S3` 설정
  - 프로젝트내에 .travis.yml을 생성하고 아래와 같이 설정합니다.
    ```yml
    language: java
    jdk: 
    - openjdk11

    branches:
    only:
        - master

    cache:
    directories:
        - '$HOME/.m2/repository'
        - '$HOME/.gradle'

    script: "./gradlew clean build"

    notifications:
    email:
        recipients:
        - nomj18@gmail.com

    #배포전 실행할 명령어들
    before_deploy:
    - mkdir -p before-deploy
    - chmod +x scripts/*.sh #모든 sh파일에 실행 권한을 줌
    - cp scripts/*.sh before-deploy/
    - cp appspec.yml before-deploy/
    - cp build/libs/*.jar before-deploy/
    - cd before-deploy && zip -r before-deploy *
    - cd ../ && mkdir -p deploy
    - mv before-deploy/before-deploy.zip deploy/helloshop-webservice.zip


    deploy:
    - provider: s3 #아마존 s3 설정
        access_key_id: $AWS_ACCESS_KEY #AWS IAM 액세스 키
        secret_access_key: $AWS_SECRET_KEY #AWS IAM 비밀 키

        bucket: helloshop-build #s3 버킷이름
        region: ap-northeast-2 #지역
        skip_cleanup: true
        acl: private
        local_dir: deploy
        wait-until-deployed: true

    ```

<br>

- `TravisCI - CodeDeploy` 설정
  - .travis.yml에 아래 내용을 추가해줍니다.
    ```yml
    - provider: codedeploy #아마존 CodeDeploy 설정
        access_key_id: $AWS_ACCESS_KEY
        secret_access_key: $AWS_SECRET_KEY

        bucket: helloshop-build #s3 버킷이름
        key: helloshop-webservice.zip #전달받을 압축 파일명
        bundle_type: zip
        application: helloshop-webservice #CodeDeploy 애플리케이션명
        deployment_group: helloshop-webservice-group #CodeDeploy 배포그룹명
        region: ap-northeast-2
        wait-until-deployed: true
    ```

<br>

- `CodeDeploy - EC2` 설정
  - EC2에 빌드파일(.jar)과 쉘스크립트파일(.sh)을 인스톨할 위치와 인스톨된 후 실행할 명령을 설정합니다.
  - .appspec.yml에 설정
    ```yml
    version: 0.0
    os: linux
    files:
    - source: /
        destination: /home/ec2-user/app/step3/zip/
        overwrite: yes

    permissions:
    - object: /
        pattern: "**"
        owner: ec2-user
        group: ec2-user

    hooks:
    AfterInstall: #인스톨 직후 stop.sh 실행
        - location: stop.sh
        timeout: 60 #최대 대기시간
        runas: ec2-user

    ApplicationStart: #배포할 애플리케이션을 시작하기위해 start.sh 실행
        - location: start.sh
        timeout: 60
        runas: ec2-user

    #실행 이후 NginX의 연결포트를 방금 실행한 애플리케이션으로 바꾸기 위해 health.sh 실행
    ValidateService: 
        - location: health.sh
        timeout: 60
        runas: ec2-user
    ```

<br>

- 쉘스크립트 파일은 총 5개가 있는데 각각의 역할은 아래와 같습니다.
  - profile.sh : 현재 NginX와 연결되지않은 애플리케이션의 프로파일과 포트를 알아냅니다.
    ```sh
    #!/usr/bin/env bash

    function find_idle_profile(){

    #현재 실행중인 애플리케이션의 프로파일을 받아옴
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ]; then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]; then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"

    }

    function find_idle_port(){
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]; then
        echo "8081"
    else
        echo "8082"
    fi
    }
    ```

  - /profile로 매핑된 메소드
  
    ![image](https://user-images.githubusercontent.com/84609913/209667471-637a3afb-a659-4f3a-9232-9178559c2c57.png)

  - stop.sh : 현재 NginX와 연결되지않은 포트로 실행중인 애플리케이션이 있으면 해당 애플리케이션을 종료합니다.
    ```sh
    #!/usr/bin/env bash

    ABSPATH=$(readlink -f $0)
    ABSDIR=$(dirname $ABSPATH)
    source ${ABSDIR}/profile.sh

    IDLE_PORT=$(find_idle_port)

    echo "> $IDLE_PORT 에서 구동중인 애플리케이션 PID 확인"
    IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

    echo "> IDLE_PID: ${IDLE_PID}"

    if [ -z ${IDLE_PID} ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료안함"
    else
    echo "> kill -15 $IDLE_PID"
    kill -15 ${IDLE_PID}
    sleep 5
    fi
    ```
    <br>

  - start.sh : 배포할 애플리케이션을 NginX와 연결되지않은 포트로 실행합니다.
    ```sh
    #!/usr/bin/env bash

    ABSPATH=$(readlink -f $0)
    ABSDIR=$(dirname $ABSPATH)
    source ${ABSDIR}/profile.sh

    REPOSITORY=/home/ec2-user/app/step3
    PROJECT_NAME=helloshop-webservice

    echo "> Build 파일복사"
    echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

    cp $REPOSITORY/zip/*.jar $REPOSITORY/

    echo "> 새 애플리케이션 배포"
    JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

    echo "> jar 이름 : $JAR_NAME"

    echo "> $JAR_NAME에 실행 권한 추가"

    chmod +x $JAR_NAME

    echo "> $JAR_NAME 실행"

    IDLE_PROFILE=$(find_idle_profile)

    echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."
    nohup java -jar -Dspring.config.location=/home/ec2-user/app/helloshop/application.yml \
    -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
    ```
    <br>
  
  - health.sh : 배포할 애플리케이션이 정상적으로 실행됐는지 확인합니다. 이후 NginX를 배포할 애플리케이션의 포트와 연결시킵니다.
    ```sh
    #!/usr/bin/env bash

    ABSPATH=$(readlink -f $0)
    ABSDIR=$(dirname $ABSPATH)
    source ${ABSDIR}/profile.sh
    source ${ABSDIR}/switch.sh

    IDLE_PORT=$(find_idle_port)

    echo "> Health Check Start"
    echo "> IDLE_PORT : $IDLE_PORT"
    echo "> curl -s http://localhost:$IDLE_PORT/profile"
    sleep 10

    for RETRY_COUNT in {1..10}
    do
    RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
    UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)
    if [ ${UP_COUNT} -ge 1 ]; then
        echo "> Health check 성공"
        switch_proxy
        break
    else
        echo "> Health check의 응답을 알 수 없거나 실행 상태가 아닙니다."
        echo "> Health check: ${RESPONSE}"
    fi

    if [ ${RETRY_COUNT} -eq 10 ]; then
        echo "> Health check 실패"
        echo "> 엔진엑스에 연결하지않고 배포를 종료합니다."
    fi

    echo "> Health check 실패. 재시도"
    sleep 10
    done
    ```
    <br>

  - switch.sh : NginX연결을 바꿔주는 역할 수행, health.sh에서 메소드 사용
    ```sh
    #!/usr/bin/env bash

    ABSPATH=$(readlink -f $0)
    ABSDIR=$(dirname $ABSPATH)
    source ${ABSDIR}/profile.sh

    function switch_proxy(){
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
    echo "> 엔진엑스 Reload"
    sudo systemctl reload nginx
    }
    ```

<br>

---
# 끝나고 아쉬웠던 점
- 일단 급하게 만들어본다고 TDD를 적용해보지 못한게 아쉽습니다. 규모가 작을 떄는 괜찮았는데 규모가 커질수록 새로운 기능을 추가하거나 리팩토링했을 때 모든 기능이 정상적으로 작동하는지 테스트해보는게 굉장히 힘들다는걸 알았습니다. <br> 
다음에 프로젝트를 진행할 때는 TDD를 적용시켜봐야겠다고 생각이 들었습니다.
- 코딩을 직접 하는 것 만큼 기획이나 설계에도 많은 노력을 들여야된다는걸 느꼈습니다. <br>
`'처음에는 가볍게 대강 만들고 나중에 필요한 추가하는 식으로 하자'` 이런식으로 진행을 했는데 직접 해보니까 기존에 만든 코드에서 변경하고 추가하는게 결과적으로 시간이 더 들었던 것 같습니다.
