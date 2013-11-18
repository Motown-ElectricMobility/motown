/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ChargingStation extends AbstractAnnotatedAggregateRoot {

    private static final Logger log = LoggerFactory.getLogger(BootChargingStationCommandHandler.class);

    @AggregateIdentifier
    private ChargingStationId id;

    private List<Connector> connectors;

    private Boolean isRegistered = false;

    protected ChargingStation() {
    }

    @CommandHandler
    public ChargingStation(CreateChargingStationCommand command) {
        this();
        log.info("Creating new chargingstation {}", command.getChargingStationId());
        apply(new ChargingStationCreatedEvent(command.getChargingStationId(), command.getConnectors(), command.getAttributes()));
    }

    /**
     * Handles a BootChargingStationCommand.
     *
     * In contrast to the other command handling methods in ChargingStation, this method is not annotated with {@link
     * org.axonframework.commandhandling.annotation.CommandHandler}. This command is handled by a dedicated command
     * handler class, {@link io.motown.domain.chargingstation.BootChargingStationCommandHandler}, to handle creating a
     * ChargingStation instance when a {@link io.motown.domain.api.chargingstation.BootChargingStationCommand} is
     * received for a non-existent ChargingStation.
     *
     * @param command the command which needs to be applied to the ChargingStation.
     */
    public ChargingStationRegistrationStatus handle(BootChargingStationCommand command) {
        log.info("Received bootnotification for chargingstation {}", command.getChargingStationId());

        ChargingStationRegistrationStatus status;

        if(this.isRegistered){
            status = ChargingStationRegistrationStatus.REGISTERED;
        }
        else{
            status = ChargingStationRegistrationStatus.UNREGISTERED;
        }

        apply(new ChargingStationBootedEvent(this.id, command.getAttributes()));

        return status;
    }

    @CommandHandler
    public void handle(RequestUnlockConnectorCommand command) {
        if (command.getConnectorId() > connectors.size()) {
            apply(new ConnectorNotFoundEvent(this.id, command.getConnectorId()));
        } else {
            if (command.getConnectorId() == Connector.ALL) {
                for (Connector connector : connectors) {
                    apply(new UnlockConnectorRequestedEvent(this.id, connector.getConnectorId()));
                }
            } else {
                apply(new UnlockConnectorRequestedEvent(this.id, command.getConnectorId()));
            }
        }
    }

    @CommandHandler
    public void handle(RegisterChargingStationCommand command) {
        log.info("Registering chargingstation {}", command.getChargingStationId());
        apply(new ChargingStationRegisteredEvent(this.id));
    }

    @EventHandler
    public void handle(ChargingStationRegisteredEvent event) {
        this.isRegistered = true;
        log.info("Registered chargingstation {}", event.getChargingStationId());
    }

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        this.id = event.getChargingStationId();
        this.connectors = event.getConnectors();

        log.info("Created new chargingstation {}", event.getChargingStationId());
    }
}
