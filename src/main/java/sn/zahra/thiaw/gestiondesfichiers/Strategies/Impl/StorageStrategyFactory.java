// StorageStrategyFactory.java
package sn.zahra.thiaw.gestiondesfichiers.Strategies.Impl;


import org.springframework.stereotype.Service;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.Strategies.StorageStrategy;

import java.util.List;

@Service
public class StorageStrategyFactory {

    private final List<StorageStrategy> strategies;

    public StorageStrategyFactory(List<StorageStrategy> strategies) {
        this.strategies = strategies;
    }

    public StorageStrategy getStrategy(StorageType storageType) {
        return strategies.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported storage type: " + storageType));
    }
}
