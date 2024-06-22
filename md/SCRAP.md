# 스크래핑 서비스

## 개요
이 프로젝트는 사용자의 개인정보를 이용하여 세금 관련 정보를 스크래핑하고, 이를 데이터베이스에 저장하는 기능을 제공합니다.

## 주요 기능
1. **사용자 정보 조회**
   - `userService.loadUserByUserId(userId)` 메서드를 통해 사용자 정보를 조회합니다.
2. **스크래핑 요청 객체 생성**
   - `createScrapRequest(User user)` 메서드에서 사용자 이름과 주민등록번호(복호화)를 이용하여 `ScrapRequest` 객체를 생성합니다.
3. **스크래핑 API 호출**
   - `callScrapApi(ScrapRequest scrapRequest)` 메서드에서 `WebClient`를 이용하여 스크래핑 API를 호출하고, 응답 결과를 문자열로 반환합니다.
4. **결정세액에 필요한 데이터 추출**
   - 스크래핑 API 응답 결과에서 `data` 필드가 `null`이 아닌 경우, `taxService.taxProcessing(scrapJson, userId)` 메서드를 호출하여 결정세액에 필요한 데이터를 추출합니다.
5. **데이터 저장**
   - 추출된 `TaxLiability` 객체를 `taxLiabilityRepository.save(taxLiability)` 메서드를 통해 데이터베이스에 저장합니다.

## 예외 처리
- 스크래핑 API 응답 결과에서 `data` 필드가 `null`인 경우, `ExceptionResponse(ErrorCode.TAX_NO_INFO)` 예외를 발생시킵니다.

## 의존성
- `UserRepository`
- `TaxLiabilityRepository`
- `RegistrationEncryption`
- `TaxService`
- `UserService`

## 코드 설명
1. **사용자 정보 조회**
   - `userService.loadUserByUserId(userId)` 메서드를 통해 사용자 정보를 조회합니다.
2. **스크래핑 요청 객체 생성**
   - `createScrapRequest(User user)` 메서드에서 사용자 이름과 주민등록번호(복호화)를 이용하여 `ScrapRequest` 객체를 생성합니다.
3. **스크래핑 API 호출**
   - `callScrapApi(ScrapRequest scrapRequest)` 메서드에서 `WebClient`를 이용하여 스크래핑 API를 호출하고, 응답 결과를 문자열로 반환합니다.
4. **결정세액에 필요한 데이터 추출**
   - 스크래핑 API 응답 결과에서 `data` 필드가 `null`이 아닌 경우, `taxService.taxProcessing(scrapJson, userId)` 메서드를 호출하여 결정세액에 필요한 데이터를 추출합니다.
5. **데이터 저장**
   - 추출된 `TaxLiability` 객체를 `taxLiabilityRepository.save(taxLiability)` 메서드를 통해 데이터베이스에 저장합니다.
