package buncheoleasy.global.exception.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * USR - 유저 관련 에러
     */
    USER_SOCIAL_ID_REQUIRED("USR-001", "소셜 고유 ID는 필수입니다.", HttpStatus.BAD_REQUEST),
    USER_SOCIAL_ID_LENGTH_INVALID("USR-002", "소셜 고유 ID는 64자 이하여야 합니다.", HttpStatus.BAD_REQUEST),

    USER_NICKNAME_REQUIRED("USR-003", "닉네임은 필수입니다.", HttpStatus.BAD_REQUEST),
    USER_NICKNAME_LENGTH_INVALID("USR-004", "닉네임은 1자 이상 10자 이하여야 합니다.", HttpStatus.BAD_REQUEST),

    USER_EMAIL_REQUIRED("USR-005", "이메일은 필수입니다.", HttpStatus.BAD_REQUEST),
    USER_EMAIL_LENGTH_INVALID("USR-006", "이메일은 255자 이하여야 합니다.", HttpStatus.BAD_REQUEST),

    USER_PHONE_NUMBER_REQUIRED("USR-007", "전화번호는 필수입니다.", HttpStatus.BAD_REQUEST),
    USER_PHONE_NUMBER_LENGTH_INVALID("USR-008", "전화번호는 13자 이하여야 합니다.", HttpStatus.BAD_REQUEST),


    /**
     * C - 공통 에러
     */
    INVALID_INPUT_VALUE("C-001", "적절하지 않은 입력값입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("C-002", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public ProblemDetail toProblemDetail() {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problemDetail.setProperty("code", code);
        return problemDetail;
    }
}
