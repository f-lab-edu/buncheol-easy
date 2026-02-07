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
    int updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
    int updatePhoneNumber(@Param("id") Long id, @Param("phoneNumber") String phoneNumber);
    int softDelete(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);
}
