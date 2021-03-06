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

import io.motown.domain.api.security.IdentityContext;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code CancelReservationRequestedEvent} is request is made to cancel a reservation. Protocol add-ons should respond
 * to this event (if applicable) and request a charging station to cancel a reservation.
 */
public final class CancelReservationRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final ReservationId reservationId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code CancelReservationRequestedEvent}.
     *
     * @param chargingStationId charging station identifier.
     * @param protocol          protocol identifier.
     * @param reservationId     reservation identifier.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code reservationId} or {@code identityContext} is {@code null}.
     */
    public CancelReservationRequestedEvent(ChargingStationId chargingStationId, String protocol, ReservationId reservationId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
        this.reservationId = checkNotNull(reservationId);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the reservation identifier.
     *
     * @return reservation identifier.
     */
    public ReservationId getReservationId() {
        return reservationId;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, protocol, reservationId, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CancelReservationRequestedEvent other = (CancelReservationRequestedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.reservationId, other.reservationId) && Objects.equals(this.identityContext, other.identityContext);
    }
}
