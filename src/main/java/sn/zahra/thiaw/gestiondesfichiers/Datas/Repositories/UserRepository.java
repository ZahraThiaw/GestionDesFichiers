package sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories;

import org.springframework.stereotype.Repository;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

}