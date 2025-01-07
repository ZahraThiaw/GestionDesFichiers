package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;

import sn.zahra.thiaw.gestiondesfichiers.Datas.Repositories.BaseRepository;
import sn.zahra.thiaw.gestiondesfichiers.Services.BaseService;

import java.util.List;

public abstract class BaseServiceImpl<E, ID> implements BaseService<E, ID> {

    private final BaseRepository<E, ID> repository;

    public BaseServiceImpl(BaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    @Override
    public E create(E entity) {
        return repository.save(entity);
    }

    @Override
    public E update(ID id, E entity) {
        return repository.save(entity);
    }

    @Override
    public E getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
    }

    @Override
    public List<E> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}
