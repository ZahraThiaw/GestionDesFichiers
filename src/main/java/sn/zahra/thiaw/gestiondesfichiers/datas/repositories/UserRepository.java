package sn.zahra.thiaw.gestiondesfichiers.datas.repositories;

import org.springframework.stereotype.Repository;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);
}