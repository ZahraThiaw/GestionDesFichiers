package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;

import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories.UserRepository;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Mappers.GenericMapper;
import sn.zahra.thiaw.gestiondesfichiers.Services.UserService;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Responses.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final GenericMapper<UserEntity, ?, UserResponseDTO> userMapper;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, GenericMapper<UserEntity, ?, UserResponseDTO> userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO getConnectedUserProfile() {
        String email = jwtService.extractUsernameFromToken(); // Récupérer l'e-mail depuis le JWT
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'e-mail : " + email));
        return userMapper.toResponseDto(user); // Mapper vers UserResponseDTO
    }

}
