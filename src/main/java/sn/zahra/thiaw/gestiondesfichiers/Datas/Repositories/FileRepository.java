package sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.FileEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends BaseRepository<FileEntity, Long> {
    // Recherche par nom de fichier (case-insensitive)
    Page<FileEntity> findByFileNameContainingIgnoreCase(String fileName, Pageable pageable);
}
