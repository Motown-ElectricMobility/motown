package io.motown.operatorapi.viewmodel;

import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargingStationEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationEventListener.class);

    private ChargingStationRepository repository;

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        log.info("ChargingStationBootedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation == null) {
            chargingStation = new ChargingStation(event.getChargingStationId().getId());
        }

        repository.save(chargingStation);
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}
