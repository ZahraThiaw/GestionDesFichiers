package sn.zahra.thiaw.gestiondesfichiers.repository;

import org.springframework.stereotype.Repository;
import sn.zahra.thiaw.gestiondesfichiers.domain.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);
}