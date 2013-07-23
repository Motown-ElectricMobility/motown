package io.motown.operatorapi.json;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.Connector;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class OperatorApiService {

    private Random random;

    private CommandBus commandBus;

    private ChargingStationRepository repository;

    public void sendBootNotification() {
        List<Connector> connectors = new LinkedList<Connector>();
        connectors.add(new Connector(1, "CONTYPE", 32));

        commandBus.dispatch(new GenericCommandMessage<BootChargingStationCommand>(new BootChargingStationCommand(new ChargingStationId("CP-" + random.nextInt(100)), "TUBE", connectors)));
    }

    public List<ChargingStation> findAllChargingStations() {
        return repository.findAll();
    }

    @Autowired
    public void setRandom(Random random) {
        this.random = random;
    }

    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}