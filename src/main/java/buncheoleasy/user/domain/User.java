package buncheoleasy.user.domain;

import buncheoleasy.global.exception.domain.BusinessException;
import buncheoleasy.global.exception.domain.ErrorCode;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;
    private String socialId;
    private String nickname;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static User create(final String socialId, final String nickname, final String email) {
        return new User(socialId, nickname, email);
    }

    private User(final String socialId, final String nickname, final String email) {
        validateSocialId(socialId);
        validateNickname(nickname);
        validateEmail(email);

        this.socialId = socialId;
        this.nickname = nickname;
        this.email = email;
    }

    private void validateSocialId(final String socialId) {
        if (socialId == null || socialId.isBlank()) {
            throw new BusinessException(ErrorCode.USER_SOCIAL_ID_REQUIRED);
        }
        if(socialId.length() > 100) {
            throw new BusinessException(ErrorCode.USER_SOCIAL_ID_LENGTH_INVALID);
        }
    }

    private void validateNickname(final String nickname) {
        if(nickname == null || nickname.isBlank()) {
            throw new BusinessException(ErrorCode.USER_NICKNAME_REQUIRED);
        }
        if(nickname.length() > 10) {
            throw new BusinessException(ErrorCode.USER_NICKNAME_LENGTH_INVALID);
        }
    }

    private void validateEmail(final String email) {
        if(email == null || email.isBlank()) {
            throw new BusinessException(ErrorCode.USER_EMAIL_REQUIRED);
        }
        if(email.length() > 320) {
            throw new BusinessException(ErrorCode.USER_EMAIL_LENGTH_INVALID);
        }
    }

    public void updatePhoneNumber(final String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isBlank()) {
            throw new BusinessException(ErrorCode.USER_PHONE_NUMBER_REQUIRED);
        }
        if(phoneNumber.length() > 13) {
            throw new BusinessException(ErrorCode.USER_PHONE_NUMBER_LENGTH_INVALID);
        }
    }
}
