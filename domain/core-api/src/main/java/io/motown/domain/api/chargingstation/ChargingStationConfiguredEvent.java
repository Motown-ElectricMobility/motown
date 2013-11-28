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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChargingStationConfiguredEvent} is the event which is published when a charging station has been configured.
 */
public class ChargingStationConfiguredEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final int numberOfConnectors;

    private final Map<String, String> configurationItems;

    /**
     * Creates a {@code ChargingStationConfiguredEvent} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param numberOfConnectors the number of connectors with which the charging station has been configured.
     * @param configurationItems the configuration items with which the charging station has been configured.
     * @throws NullPointerException if {@code chargingStationId}, {@code connectors}, or {@code configurationItems} is
     * {@code null}.
     */
    public ChargingStationConfiguredEvent(ChargingStationId chargingStationId, int numberOfConnectors, Map<String, String> configurationItems) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.numberOfConnectors = numberOfConnectors;
        this.configurationItems = ImmutableMap.copyOf(checkNotNull(configurationItems));
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
     * Gets the connectors with which the charging station has been configured.
     *
     * @return number of connectors.
     */
    public int getNumberOfConnectors() {
        return numberOfConnectors;
    }

    /**
     * Gets the configuration items with which the charging station has been configured.
     *
     * These configuration items are additional information provided with which the charging station has been configured
     * but which are not required by Motown.
     *
     * @return an immutable {@link java.util.Map} of configuration items.
     */
    public Map<String, String> getConfigurationItems() {
        return configurationItems;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("chargingStationId", chargingStationId)
                .add("numberOfConnectors", numberOfConnectors)
                .add("configurationItems", configurationItems)
                .toString();
    }
}