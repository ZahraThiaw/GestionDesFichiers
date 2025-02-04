package sn.zahra.thiaw.gestiondesfichiers.service.impl;

import sn.zahra.thiaw.gestiondesfichiers.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository compteRepository;

    public CustomUserDetailsService(UserRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retourner directement l'entité User qui implémente déjà UserDetails
        return compteRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
