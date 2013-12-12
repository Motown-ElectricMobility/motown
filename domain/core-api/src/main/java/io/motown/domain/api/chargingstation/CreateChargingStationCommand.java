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
package io.motown.domain.api.chargingstation;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code CreateChargingStationCommand} is the command which is published when a charging station should be created.
 */
public class CreateChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final boolean accept;
    /**
     * Creates a {@code CreateChargingStationCommand} with an identifier.
     *
     *
     * @param chargingStationId the identifier of the charging station.
     * @param accept
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public CreateChargingStationCommand(ChargingStationId chargingStationId, boolean accept) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.accept = accept;
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * Gets the acceptance state for this station.
     *
     * @return the acceptance state.
     */
    public Boolean isAccepted() {
        return accept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateChargingStationCommand that = (CreateChargingStationCommand) o;

        if (accept != that.accept) return false;
        if (!chargingStationId.equals(that.chargingStationId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + (accept ? 1 : 0);
        return result;
    }
}
