package sn.zahra.thiaw.gestiondesfichiers.service.impl;

import sn.zahra.thiaw.gestiondesfichiers.domain.entity.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.repository.UserRepository;
import sn.zahra.thiaw.gestiondesfichiers.service.mapper.GenericMapper;
import sn.zahra.thiaw.gestiondesfichiers.service.UserService;
import sn.zahra.thiaw.gestiondesfichiers.model.responses.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

    private final UserRepository userRepository;
    private final GenericMapper<UserEntity, ?, UserResponseDTO> userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           GenericMapper<UserEntity, ?, UserResponseDTO> userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
}
