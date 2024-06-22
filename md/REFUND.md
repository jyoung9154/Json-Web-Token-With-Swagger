# 세금 서비스

## 개요
이 프로젝트는 사용자의 세금 관련 정보를 처리하고, 결정세액을 계산하는 기능을 제공합니다.

## 주요 기능
1. **세금 정보 처리**
   - `taxProcessing(JSONObject scrapJson, String userId)` 메서드에서 스크래핑 데이터를 이용하여 `TaxLiability` 객체를 생성합니다.
2. **국민연금 공제액 합산**
   - `calNpsDeduction(JSONArray nationalPensionDeductions)` 메서드에서 국민연금 공제액을 합산합니다.
3. **신용카드 공제액 합산**
   - `calCreditCardDeduction(JSONObject creditCardDeduction)` 메서드에서 신용카드 공제액을 합산합니다.
4. **결정세액 조회**
   - `refund(String userId)` 메서드에서 사용자 정보와 세금 정보를 조회하고, 결정세액을 계산하여 `RefundResponse` 객체를 반환합니다.
5. **산출세액 추출**
   - `calculateTax(long taxableIncome)` 메서드에서 과세표준에 따른 산출세액을 계산합니다.

## 예외 처리
- 사용자 정보 또는 세금 정보가 존재하지 않는 경우, `ExceptionResponse(ErrorCode.TAX_NO_SAVEED)` 예외를 발생시킵니다.

## 의존성
- `TaxLiabilityRepository`
- `UserRepository`

## 코드 설명
1. **세금 정보 처리**
   - `taxProcessing(JSONObject scrapJson, String userId)` 메서드에서 스크래핑 데이터를 이용하여 `TaxLiability` 객체를 생성합니다.
2. **국민연금 공제액 합산**
   - `calNpsDeduction(JSONArray nationalPensionDeductions)` 메서드에서 국민연금 공제액을 합산합니다.
3. **신용카드 공제액 합산**
   - `calCreditCardDeduction(JSONObject creditCardDeduction)` 메서드에서 신용카드 공제액을 합산합니다.
4. **결정세액 조회**
   - `refund(String userId)` 메서드에서 사용자 정보와 세금 정보를 조회하고, 결정세액을 계산하여 `RefundResponse` 객체를 반환합니다.
5. **산출세액 추출**
   - `calculateTax(long taxableIncome)` 메서드에서 과세표준에 따른 산출세액을 계산합니다.

## 예외 처리
- 사용자 정보 또는 세금 정보가 존재하지 않는 경우, `ExceptionResponse(ErrorCode.TAX_NO_SAVEED)` 예외를 발생시킵니다.
