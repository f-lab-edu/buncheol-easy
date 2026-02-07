package buncheoleasy.user.service;

import buncheoleasy.user.infrastructure.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

}
