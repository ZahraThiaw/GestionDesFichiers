// StorageStrategyFactory.java
package sn.zahra.thiaw.gestiondesfichiers.strategies.factory;


import org.springframework.stereotype.Service;
import sn.zahra.thiaw.gestiondesfichiers.datas.enums.StorageType;
import sn.zahra.thiaw.gestiondesfichiers.strategies.StorageStrategy;

import java.util.List;

@Service
public class StorageStrategyFactory {

    // deux strategies LOCAL, DATABASE
    private final List<StorageStrategy> strategies;

    public StorageStrategyFactory(List<StorageStrategy> strategies) {
        this.strategies = strategies;
    }

    //todo utiliser filter apres stream pour filtrer sur la strategie
//    public StorageStrategy getStrategy(StorageType storageType /*LOCAL*/) {
//
//        return strategies.stream() //parcourir la liste des strategies [1]
//                .filter(strategy-> strategy.getStorageType().equals(storageType))
//                .findFirst()//[2] retourne la premiere strategie trouvee , supposons que cest DATABASE
//                .orElseThrow(() -> new IllegalArgumentException("Unsupported storage type: " + storageType));
//    }

    public StorageStrategy getStrategy(StorageType storageType) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(storageType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported storage type: " + storageType));
    }
}

