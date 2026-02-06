package buncheoleasy.user.infrastructure;

import buncheoleasy.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findById(Long id);
    User findBySocialId(String socialId);
    void save(User user);
    void updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
    void updatePhoneNumber(@Param("id") Long id, @Param("phoneNumber") String phoneNumber);
    void softDelete(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);
}
