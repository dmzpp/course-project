package main.services;

import main.dao.ChampionshipDAO;
import main.models.Championship;
import main.models.Region;

import java.util.List;
import java.util.function.Supplier;

public class ChampionshipService<T extends ChampionshipDAO> implements EntityService<Championship> {
    private final Supplier<T> supplier;

    public ChampionshipService(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Championship find( int id) {
        return supplier.get().findById(id);
    }

    @Override
    public void save(Championship championship) {
        supplier.get().save(championship);
    }

    @Override
    public void delete( Championship championship) {
        supplier.get().remove(championship);
    }

    @Override
    public void update(Championship championship) {
        supplier.get().update(championship);
    }

    @Override
    public List<Championship> findAll() {
        return supplier.get().findAll();
    }
}