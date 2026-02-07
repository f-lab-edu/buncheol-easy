package buncheoleasy.user.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import buncheoleasy.user.domain.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@DisplayName("UserMapper 테스트")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("User를 저장하고 ID가 자동 생성된다")
        void save_Success() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");

            // when
            userMapper.save(user);

            // then
            assertThat(user.getId()).isNotNull();
            assertThat(user.getId()).isPositive();
        }

        @Test
        @DisplayName("같은 User를 여러 번 저장하면 새로운 ID가 각각 생성된다")
        void save_Multiple_Success() {
            // given
            User user1 = User.create("kakao_123456", "유저1", "user1@example.com");
            User user2 = User.create("kakao_789012", "유저2", "user2@example.com");

            // when
            userMapper.save(user1);
            userMapper.save(user2);

            // then
            assertThat(user1.getId()).isNotNull();
            assertThat(user2.getId()).isNotNull();
            assertThat(user1.getId()).isNotEqualTo(user2.getId());
        }
    }

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 User를 조회할 수 있다")
        void findById_Success() {
            // given
            User savedUser = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(savedUser);
            Long userId = savedUser.getId();

            // when
            User foundUser = userMapper.findById(userId);

            // then
            assertThat(foundUser).isNotNull();
            assertThat(foundUser.getId()).isEqualTo(userId);
            assertThat(foundUser.getSocialId()).isEqualTo("kakao_123456");
            assertThat(foundUser.getNickname()).isEqualTo("테스트유저");
            assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
            assertThat(foundUser.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 null을 반환한다")
        void findById_NotFound_ReturnsNull() {
            // given
            Long nonExistentId = 999999L;

            // when
            User foundUser = userMapper.findById(nonExistentId);

            // then
            assertThat(foundUser).isNull();
        }

        @Test
        @DisplayName("소프트 삭제된 User는 조회되지 않는다")
        void findById_SoftDeleted_ReturnsNull() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();

            // 소프트 삭제
            userMapper.softDelete(userId, LocalDateTime.now());

            // when
            User foundUser = userMapper.findById(userId);

            // then
            assertThat(foundUser).isNull();
        }
    }

    @Nested
    @DisplayName("findBySocialId 테스트")
    class FindBySocialIdTest {

        @Test
        @DisplayName("SocialId로 User를 조회할 수 있다")
        void findBySocialId_Success() {
            // given
            String socialId = "kakao_123456";
            User savedUser = User.create(socialId, "테스트유저", "test@example.com");
            userMapper.save(savedUser);

            // when
            User foundUser = userMapper.findBySocialId(socialId);

            // then
            assertThat(foundUser).isNotNull();
            assertThat(foundUser.getSocialId()).isEqualTo(socialId);
            assertThat(foundUser.getNickname()).isEqualTo("테스트유저");
            assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("존재하지 않는 SocialId로 조회하면 null을 반환한다")
        void findBySocialId_NotFound_ReturnsNull() {
            // given
            String nonExistentSocialId = "kakao_nonexistent";

            // when
            User foundUser = userMapper.findBySocialId(nonExistentSocialId);

            // then
            assertThat(foundUser).isNull();
        }

        @Test
        @DisplayName("소프트 삭제된 User는 SocialId로 조회되지 않는다")
        void findBySocialId_SoftDeleted_ReturnsNull() {
            // given
            String socialId = "kakao_123456";
            User user = User.create(socialId, "테스트유저", "test@example.com");
            userMapper.save(user);

            // 소프트 삭제
            userMapper.softDelete(user.getId(), LocalDateTime.now());

            // when
            User foundUser = userMapper.findBySocialId(socialId);

            // then
            assertThat(foundUser).isNull();
        }
    }

    @Nested
    @DisplayName("updateNickname 테스트")
    class UpdateNicknameTest {

        @Test
        @DisplayName("닉네임을 업데이트할 수 있다")
        void updateNickname_Success() {
            // given
            User user = User.create("kakao_123456", "이전닉네임", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();
            String newNickname = "새로운닉네임";

            // when
            int updatedCount = userMapper.updateNickname(userId, newNickname);

            // then
            assertThat(updatedCount).isEqualTo(1);
            User updatedUser = userMapper.findById(userId);
            assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
        }

        @Test
        @DisplayName("소프트 삭제된 User의 닉네임은 업데이트되지 않는다")
        void updateNickname_SoftDeleted_DoesNotUpdate() {
            // given
            User user = User.create("kakao_123456", "이전닉네임", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();

            // 소프트 삭제
            userMapper.softDelete(userId, LocalDateTime.now());

            String newNickname = "새로운닉네임";

            // when
            int updatedCount = userMapper.updateNickname(userId, newNickname);

            // then - 업데이트된 row가 0개여야 함
            assertThat(updatedCount).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("updatePhoneNumber 테스트")
    class UpdatePhoneNumberTest {

        @Test
        @DisplayName("전화번호를 업데이트할 수 있다")
        void updatePhoneNumber_Success() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();
            String phoneNumber = "010-1234-5678";

            // when
            int updatedCount = userMapper.updatePhoneNumber(userId, phoneNumber);

            // then
            assertThat(updatedCount).isEqualTo(1);
            User updatedUser = userMapper.findById(userId);
            assertThat(updatedUser.getPhoneNumber()).isEqualTo(phoneNumber);
        }

        @Test
        @DisplayName("이미 전화번호가 있는 User의 전화번호를 변경할 수 있다")
        void updatePhoneNumber_ChangeExisting_Success() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();

            String oldPhoneNumber = "010-1111-2222";
            userMapper.updatePhoneNumber(userId, oldPhoneNumber);

            String newPhoneNumber = "010-9999-8888";

            // when
            int updatedCount = userMapper.updatePhoneNumber(userId, newPhoneNumber);

            // then
            assertThat(updatedCount).isEqualTo(1);
            User updatedUser = userMapper.findById(userId);
            assertThat(updatedUser.getPhoneNumber()).isEqualTo(newPhoneNumber);
        }

        @Test
        @DisplayName("소프트 삭제된 User의 전화번호는 업데이트되지 않는다")
        void updatePhoneNumber_SoftDeleted_DoesNotUpdate() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();

            // 소프트 삭제
            userMapper.softDelete(userId, LocalDateTime.now());

            String phoneNumber = "010-1234-5678";

            // when
            int updatedCount = userMapper.updatePhoneNumber(userId, phoneNumber);

            // then - 업데이트된 row가 0개여야 함
            assertThat(updatedCount).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("softDelete 테스트")
    class SoftDeleteTest {

        @Test
        @DisplayName("User를 소프트 삭제할 수 있다")
        void softDelete_Success() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();
            LocalDateTime deletedAt = LocalDateTime.now();

            // when
            int deletedCount = userMapper.softDelete(userId, deletedAt);

            // then
            assertThat(deletedCount).isEqualTo(1);
            User foundUser = userMapper.findById(userId);
            assertThat(foundUser).isNull();  // 소프트 삭제된 사용자는 조회되지 않음
        }

        @Test
        @DisplayName("이미 소프트 삭제된 User는 다시 삭제되지 않는다")
        void softDelete_AlreadyDeleted_DoesNotUpdate() {
            // given
            User user = User.create("kakao_123456", "테스트유저", "test@example.com");
            userMapper.save(user);
            Long userId = user.getId();

            LocalDateTime firstDeletedAt = LocalDateTime.now().minusDays(1);
            userMapper.softDelete(userId, firstDeletedAt);

            LocalDateTime secondDeletedAt = LocalDateTime.now();

            // when
            int deletedCount = userMapper.softDelete(userId, secondDeletedAt);

            // then - 이미 삭제되어 있으므로 업데이트된 row가 0개
            assertThat(deletedCount).isEqualTo(0);
        }

        @Test
        @DisplayName("존재하지 않는 User를 삭제해도 에러가 발생하지 않는다")
        void softDelete_NonExistent_NoError() {
            // given
            Long nonExistentId = 999999L;
            LocalDateTime deletedAt = LocalDateTime.now();

            // when
            int deletedCount = userMapper.softDelete(nonExistentId, deletedAt);

            // then - 존재하지 않으므로 삭제된 row가 0개
            assertThat(deletedCount).isEqualTo(0);
        }
    }
}
