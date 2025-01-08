package sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository <T,ID> extends JpaRepository<T, ID> {
    List<T> findByDeletedFalse();

    Page<T> findByDeletedFalse(Pageable pageable);

    Optional<T> findByIdAndDeletedFalse(ID id);
}
