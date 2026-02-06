package buncheoleasy.user.domain;

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
        this.socialId = socialId;
        validateNickname(nickname);
        this.nickname = nickname;
        validateEmail(email);
        this.email = email;
    }

    private void validateSocialId(final String socialId) {
        if (socialId == null || socialId.isBlank()) {
            throw new RuntimeException();
        }
    }

    private void validateNickname(final String nickname) {
        if(nickname == null || nickname.isBlank() || nickname.length() > 10) {
            throw new RuntimeException();
        }
    }

    private void validateEmail(final String email) {
        if(email == null || email.isBlank()) {
            throw new RuntimeException();
        }
    }

    public void updatePhoneNumber(final String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 13) {
            throw new RuntimeException();
        }
    }
}
