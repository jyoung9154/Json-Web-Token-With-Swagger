package toy.project.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toy.project.jwt.common.RegistrationEncryption;
import toy.project.jwt.domain.User;
import toy.project.jwt.dto.JwtToken;
import toy.project.jwt.dto.LoginRequest;
import toy.project.jwt.dto.SignupRequest;
import toy.project.jwt.exception.ErrorCode;
import toy.project.jwt.exception.ExceptionResponse;
import toy.project.jwt.jwt.JwtUtil;
import toy.project.jwt.repository.UserRepository;
import toy.project.jwt.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    /* 기능 객체 */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RegistrationEncryption registrationEncryption;

    /* 회원 가입 기능 */
    @Override
    @SneakyThrows
    public ResponseEntity<User> signup(SignupRequest signupReq) {

        /* 회원가입 중복 확인 - ID 중복 */
        if (userRepository.existsByUserId(signupReq.getUserId())) {
            throw new ExceptionResponse(ErrorCode.USER_DUPLICATION_ID);
        }

        /* 사용자 이름으로 우선 조회 */
        Optional<User> user = userRepository.findByName(signupReq.getName());

        /* 사용자 존재 여부 확인 */
        if (user.isPresent()) {

            /* 사용자 주민등록번호 복호화 */
            String userRegNo= registrationEncryption.decryptRegistrationNumber(user.get().getRegNo());

            /* 주민등록번호 중복 확인*/
            if (signupReq.getRegNo().equals(userRegNo)) {
                throw new ExceptionResponse(ErrorCode.USER_DUPLICATION_REG,"등록된 사용자 ID: " + user.get().getUserId());

            }
        }

        /* 민감정보 - 패스워드 단방향 암호화 bcrypt */
        String encodedPassword = passwordEncoder.encode(signupReq.getPassword());

        /* 민감정보 - 주민등록번호 검사 및 양방향 암호화 AES */
        if(!validateRegNo(signupReq.getRegNo())){
            throw new ExceptionResponse(ErrorCode.USER_REG_ERROR);
        }
        String encryptedRegNo = registrationEncryption.encryptRegistrationNumber(signupReq.getRegNo());

        /* 데이터 저장 */
        try {
            User newUser =  User.builder()
                .userId(signupReq.getUserId())
                .password(encodedPassword)
                .name(signupReq.getName())
                .regNo(encryptedRegNo)
                .build();

            return ResponseEntity.ok().body(userRepository.save(newUser));

        }catch(Exception e) {
            throw new ExceptionResponse(ErrorCode.DB_FAILD_SAVE,"비정상적인 ID 및 패스워드로 DB 저장 가능한 문자열 길이 초과");
        }
    }

    /* 로그인 기능 */
    @Override
    public ResponseEntity<JwtToken> login(LoginRequest loginRequest) {

        /* 사용자 확인 */
        User user = loadUserByUserId(loginRequest.getUserId());

        /* 패스워드 확인 */
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ExceptionResponse(ErrorCode.INVALID_PASSWORD);
        }

        /* 토큰 생성 */
        String accessToken = jwtUtil.createAccessToken(user.getUserId(),"access");
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        return ResponseEntity.ok().body(new JwtToken(accessToken, refreshToken));

    }

    /* DB 사용자 조회 */
    public User loadUserByUserId(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId).orElseThrow(() -> new ExceptionResponse(ErrorCode.USER_NOT_FONUD));
    }

    /* 주민등록번호 검증 */
    public static boolean validateRegNo(String regNo) {
        /* 주민등록번호 형식 검사 */
        if (regNo.length() != 14 || regNo.charAt(6) != '-') {
            return false;
        }

        try {
            /* 출생연도 끝 두자리 추출 */
            int yearTwoDigits = Integer.parseInt(regNo.substring(0, 2));

            /* 출생 월 추출 */
            int month = Integer.parseInt(regNo.substring(2, 4));

            /* 출생 일 추출 */
            int day = Integer.parseInt(regNo.substring(4, 6));

            /* 성별 추출 */
            int gender = Integer.parseInt(regNo.substring(7, 8));

            /* 개인을 식별하는 일련의 숫자 추출 */
            int[] identifyingNumbers = new int[6];
            for (int i = 0; i < 6; i++) {
                identifyingNumbers[i] = Integer.parseInt(regNo.substring(8 + i * 1, 9 + i * 1));
            }

            /* 출생년도 계산 */
            int year = (yearTwoDigits >= 0 && yearTwoDigits <= 23) ? 2000 + yearTwoDigits : 1900 + yearTwoDigits;

            /* 출생일 검사 */
            LocalDate birthDate = LocalDate.of(year, month, day);
            if (birthDate.isAfter(LocalDate.now())) {
                return false;
            }

            /* 성별 검사 */
            if (gender < 1 || gender > 4) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
