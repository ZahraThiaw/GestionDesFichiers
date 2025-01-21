package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;


import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories.UserRepository;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.LoginUserDto;
import sn.zahra.thiaw.gestiondesfichiers.Web.Dtos.Requests.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final KeycloakSyncService keycloakSyncService;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            KeycloakSyncService keycloakSyncService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloakSyncService = keycloakSyncService;
    }

    public UserEntity signup(RegisterUserDTO input) {
        UserEntity user = new UserEntity();
        user.setNom(input.getNom());
        user.setPrenom(input.getPrenom());
        user.setEmail(input.getEmail());
        user.setRole(UserEntity.Role.user);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        // Sauvegarde dans la base de données locale
        UserEntity savedUser = userRepository.save(user);

        // Création de l'utilisateur dans Keycloak avec le même rôle
        keycloakSyncService.createKeycloakUser(input, savedUser.getRole());

        return savedUser;
    }

    public UserEntity authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
