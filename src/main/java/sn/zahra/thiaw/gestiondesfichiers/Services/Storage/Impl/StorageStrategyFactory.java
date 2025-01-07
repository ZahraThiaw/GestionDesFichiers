// StorageStrategyFactory.java
package sn.zahra.thiaw.gestiondesfichiers.Services.Storage.Impl;


import org.springframework.stereotype.Service;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Services.Storage.StorageStrategy;

import java.util.List;

@Service
public class StorageStrategyFactory {

    private final List<StorageStrategy> strategies;

    public StorageStrategyFactory(List<StorageStrategy> strategies) {
        this.strategies = strategies;
    }

    public StorageStrategy getStrategy(StorageType storageType) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(storageType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported storage type: " + storageType));
    }
}
