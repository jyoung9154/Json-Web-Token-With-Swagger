# JWT 토큰 관련 토이 프로젝트
***
* 서버를 실행 후 접속해주시기 바랍니다.
## Swagger
- [스웨거 접속](http://localhost:8080/jwt/swagger.html)
## Database
- [DB 접속](http://localhost:8080/h2-console/)

***
## 주요 기능 설명

* ### 1. 설정
  * [스프링 시큐리티 설정](./md/SECURITY.md)
* ### 2. 필터
  *  [JWT 인증 필터 설정](./md/JWTFILTER.md)
* ### 3. 서비스
  * [사용자 서비스](./md/USER.md)
  
* ### 4. 테스트
  * JUnit 테스트 
    * [JWT 토큰 테스트](./src/test/java/toy/project/jwt/jwt/JwtTest.java)
    * [사용자 서비스 테스트](./src/test/java/toy/project/jwt/controller/JwtUserControllerTest.java)
  
* ### JWT 토큰 관련 
  * Refresh Token 발급은 하고 있으나 별도의 저장 및 갱신 처리는 하지 않음 
  * 단, Access Token 외 접속 불가 처리
  * * 참고사항 * application-KEY.yml 파일은 설명을 위해 올렸으나 실제로 ignore 처리 되어있음
***

## 제한사항
* ### 회원가입 
  * 회원가입시 등록된 사용자 회원가입 불가 (이름, 주민등록번호로 구분)
  * 아이디는 임의로 설정 가능하나 회원 삭제 기능이 없어 등록된 사용자가 회원가입 시 등록된 ID 반환
  * 아이디 중복 불가
  * 아이디 및 패스워드 DB 저장 시 varchar(255) 초과하는 입력시 오류 반환 (* 패턴 지정 X)

* ### 로그인
  * 등록된 ID 외 로그인 불가
  * 패스워드 오류시 로그인 불가
