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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestCancelReservationCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.viewmodel.model.RequestCancelReservationApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

class RequestCancelReservationJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestCancelReservation";

    private DomainCommandGateway commandGateway;

    private ChargingStationRepository repository;

    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) {
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.isAccepted()) {

                RequestCancelReservationApiCommand command = gson.fromJson(commandObject, RequestCancelReservationApiCommand.class);

                commandGateway.send(new RequestCancelReservationCommand(new ChargingStationId(chargingStationId), command.getReservationId(), identityContext), new CorrelationToken());
            } else {
                throw new IllegalStateException("It is not possible to cancel a reservation on a charging station that is not registered");
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Cancel reservation command is not able to parse the payload, is your json correctly formatted?", ex);
        }
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}