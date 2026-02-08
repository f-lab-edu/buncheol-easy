package buncheoleasy.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import buncheoleasy.global.exception.domain.BusinessException;
import buncheoleasy.global.exception.domain.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("User 엔티티 테스트")
class UserTest {

    @Nested
    @DisplayName("User 생성 테스트")
    class CreateUserTest {

        @Test
        @DisplayName("정상적인 값으로 User를 생성할 수 있다")
        void createUser_Success() {
            // given
            String socialId = "kakao_123456";
            String nickname = "테스트유저";
            String email = "test@example.com";

            // when
            User user = User.create(socialId, nickname, email);

            // then
            assertThat(user).isNotNull();
            assertThat(user.getSocialId()).isEqualTo(socialId);
            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getPhoneNumber()).isNull();
            assertThat(user.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("최대 길이의 값으로 User를 생성할 수 있다")
        void createUser_WithMaxLength_Success() {
            // given
            String socialId = "a".repeat(64);  // 최대 64자
            String nickname = "a".repeat(10);  // 최대 10자
            String email = "a".repeat(255);     // 최대 255자

            // when
            User user = User.create(socialId, nickname, email);

            // then
            assertThat(user).isNotNull();
            assertThat(user.getSocialId()).hasSize(64);
            assertThat(user.getNickname()).hasSize(10);
            assertThat(user.getEmail()).hasSize(255);
        }
    }

    @Nested
    @DisplayName("SocialId 검증 테스트")
    class ValidateSocialIdTest {

        @Test
        @DisplayName("socialId가 null이면 예외가 발생한다")
        void validateSocialId_Null_ThrowsException() {
            // given
            String socialId = null;
            String nickname = "테스트유저";
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_SOCIAL_ID_REQUIRED.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_SOCIAL_ID_REQUIRED);
        }

        @Test
        @DisplayName("socialId가 빈 문자열이면 예외가 발생한다")
        void validateSocialId_Empty_ThrowsException() {
            // given
            String socialId = "";
            String nickname = "테스트유저";
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_SOCIAL_ID_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("socialId가 공백 문자열이면 예외가 발생한다")
        void validateSocialId_Blank_ThrowsException() {
            // given
            String socialId = "   ";
            String nickname = "테스트유저";
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_SOCIAL_ID_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("socialId가 100자를 초과하면 예외가 발생한다")
        void validateSocialId_TooLong_ThrowsException() {
            // given
            String socialId = "a".repeat(101);  // 101자
            String nickname = "테스트유저";
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_SOCIAL_ID_LENGTH_INVALID.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_SOCIAL_ID_LENGTH_INVALID);
        }
    }

    @Nested
    @DisplayName("Nickname 검증 테스트")
    class ValidateNicknameTest {

        @Test
        @DisplayName("nickname이 null이면 예외가 발생한다")
        void validateNickname_Null_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = null;
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_NICKNAME_REQUIRED.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NICKNAME_REQUIRED);
        }

        @Test
        @DisplayName("nickname이 빈 문자열이면 예외가 발생한다")
        void validateNickname_Empty_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "";
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_NICKNAME_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("nickname이 공백 문자열이면 예외가 발생한다")
        void validateNickname_Blank_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "   ";
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_NICKNAME_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("nickname이 10자를 초과하면 예외가 발생한다")
        void validateNickname_TooLong_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "a".repeat(11);  // 11자
            String email = "test@example.com";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_NICKNAME_LENGTH_INVALID.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NICKNAME_LENGTH_INVALID);
        }
    }

    @Nested
    @DisplayName("Email 검증 테스트")
    class ValidateEmailTest {

        @Test
        @DisplayName("email이 null이면 예외가 발생한다")
        void validateEmail_Null_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "테스트유저";
            String email = null;

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_EMAIL_REQUIRED.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_EMAIL_REQUIRED);
        }

        @Test
        @DisplayName("email이 빈 문자열이면 예외가 발생한다")
        void validateEmail_Empty_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "테스트유저";
            String email = "";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_EMAIL_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("email이 공백 문자열이면 예외가 발생한다")
        void validateEmail_Blank_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "테스트유저";
            String email = "   ";

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_EMAIL_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("email이 320자를 초과하면 예외가 발생한다")
        void validateEmail_TooLong_ThrowsException() {
            // given
            String socialId = "kakao_123456";
            String nickname = "테스트유저";
            String email = "a".repeat(321);  // 321자

            // when & then
            assertThatThrownBy(() -> User.create(socialId, nickname, email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_EMAIL_LENGTH_INVALID.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_EMAIL_LENGTH_INVALID);
        }
    }

    @Nested
    @DisplayName("PhoneNumber 검증 테스트")
    class ValidatePhoneNumberTest {

        @Test
        @DisplayName("phoneNumber가 null이면 예외가 발생한다")
        void validatePhoneNumber_Null_ThrowsException() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            String phoneNumber = null;

            // when & then
            assertThatThrownBy(() -> user.updatePhoneNumber(phoneNumber))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_PHONE_NUMBER_REQUIRED.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_PHONE_NUMBER_REQUIRED);
        }

        @Test
        @DisplayName("phoneNumber가 빈 문자열이면 예외가 발생한다")
        void validatePhoneNumber_Empty_ThrowsException() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            String phoneNumber = "";

            // when & then
            assertThatThrownBy(() -> user.updatePhoneNumber(phoneNumber))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_PHONE_NUMBER_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("phoneNumber가 공백 문자열이면 예외가 발생한다")
        void validatePhoneNumber_Blank_ThrowsException() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            String phoneNumber = "   ";

            // when & then
            assertThatThrownBy(() -> user.updatePhoneNumber(phoneNumber))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_PHONE_NUMBER_REQUIRED.getMessage());
        }

        @Test
        @DisplayName("phoneNumber가 13자를 초과하면 예외가 발생한다")
        void validatePhoneNumber_TooLong_ThrowsException() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            String phoneNumber = "01012345678901";  // 14자

            // when & then
            assertThatThrownBy(() -> user.updatePhoneNumber(phoneNumber))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(ErrorCode.USER_PHONE_NUMBER_LENGTH_INVALID.getMessage())
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_PHONE_NUMBER_LENGTH_INVALID);
        }

        @Test
        @DisplayName("올바른 phoneNumber로 업데이트할 수 있다")
        void updatePhoneNumber_Success() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            String phoneNumber = "010-1234-5678";

            // when
            user.updatePhoneNumber(phoneNumber);

            // then
            assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
        }
    }
}
