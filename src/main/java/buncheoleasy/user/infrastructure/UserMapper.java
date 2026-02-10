package buncheoleasy.user.infrastructure;

import buncheoleasy.user.domain.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insert(User user);

    Optional<User> findById(Long id);
}
