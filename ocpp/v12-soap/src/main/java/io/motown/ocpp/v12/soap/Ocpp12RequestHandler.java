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
package io.motown.ocpp.v12.soap;

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp12Client;
import org.axonframework.common.annotation.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Ocpp12RequestHandler implements OcppRequestHandler {

    public static final String ADD_ON_TYPE = "OCPPS12";
    private static final Logger LOG = LoggerFactory.getLogger(Ocpp12RequestHandler.class);
    private DomainService domainService;
    private ChargingStationOcpp12Client chargingStationOcpp12Client;
    private AddOnIdentity addOnIdentity;

    @Override
    public void handle(ConfigurationItemsRequestedEvent event) {
        throw new UnsupportedOperationException("GetConfigurationItems not supported in OCPP/S 1.2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            chargingStationOcpp12Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());
        } else {
            LOG.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 SoftResetChargingStationRequestedEvent");
        chargingStationOcpp12Client.softReset(event.getChargingStationId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 HardResetChargingStationRequestedEvent");
        chargingStationOcpp12Client.hardReset(event.getChargingStationId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 StartTransactionRequestedEvent");
        chargingStationOcpp12Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getEvseId());
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 UnlockEvseRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.unlockConnector(event.getChargingStationId(), event.getEvseId());

        switch (requestResult) {
            case SUCCESS:
                domainService.informUnlockEvse(event.getChargingStationId(), event.getEvseId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to unlock evse {} on chargingstation {}", event.getEvseId(), event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unknown unlock evse response status: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToInoperative(event.getChargingStationId(), chargingStationEvseId);

        switch (requestResult) {
            case SUCCESS:
                domainService.changeChargingStationAvailabilityToInoperative(event.getChargingStationId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of chargingstation {} to inoperative", event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unknown status for change availability to inoperative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToOperative(event.getChargingStationId(), chargingStationEvseId);

        switch (requestResult) {
            case SUCCESS:
                domainService.changeChargingStationAvailabilityToOperative(event.getChargingStationId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of chargingstation {} to operative", event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unknown status for change availability to operative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToInoperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 ChangeComponentAvailabilityToInoperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToInoperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        switch (requestResult) {
            case SUCCESS:
                domainService.changeComponentAvailabilityToInoperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of evse {} on chargingstation {} to inoperative", event.getComponentId().getId(), event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unknown status for change component availability to inoperative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToOperativeRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 ChangeComponentAvailabilityToOperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToOperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        switch (requestResult) {
            case SUCCESS:
                domainService.changeComponentAvailabilityToOperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of evse {} on chargingstation {} to operative", event.getComponentId().getId(), event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unknown status for change component availability to operative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(DataTransferRequestedEvent event, CorrelationToken correlationToken) {
        throw new UnsupportedOperationException("DataTransfer not supported in OCPP/S 1.2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ChangeConfigurationItemRequestedEvent event, CorrelationToken correlationToken) {
        final boolean hasConfigurationChanged = chargingStationOcpp12Client.changeConfiguration(event.getChargingStationId(), event.getConfigurationItem());

        if (hasConfigurationChanged) {
            domainService.changeConfiguration(event.getChargingStationId(), event.getConfigurationItem(), correlationToken, addOnIdentity);
        }
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp12Client.getDiagnostics(event.getChargingStationId(), event.getDiagnosticsUploadSettings());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename, correlationToken, addOnIdentity);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.2 ClearCacheRequestedEvent");
        boolean result = chargingStationOcpp12Client.clearCache(event.getChargingStationId());

        if (result) {
            domainService.informCacheCleared(event.getChargingStationId(), correlationToken, addOnIdentity);
        } else {
            LOG.info("Unable to clear cache for [{}]", event.getChargingStationId());
        }
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("OCPP 1.2 FirmwareUpdateRequestedEvent");
        Map<String, String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if (attributes != null) {
            attrNumRetries = attributes.get(FirmwareUpdateAttributeKey.NUM_RETRIES);
            attrRetryInterval = attributes.get(FirmwareUpdateAttributeKey.RETRY_INTERVAL);
        }
        Integer numRetries = (attrNumRetries != null && !"".equals(attrNumRetries)) ? Integer.parseInt(attrNumRetries) : null;
        Integer retryInterval = (attrRetryInterval != null && !"".equals(attrRetryInterval)) ? Integer.parseInt(attrRetryInterval) : null;

        chargingStationOcpp12Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken correlationToken) {
        throw new UnsupportedOperationException("GetAuthorizationListVersion not supported in OCPP/S 1.2");
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken correlationToken) {
        throw new UnsupportedOperationException("SendAuthorizationList not supported in OCPP/S 1.2");
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken correlationToken) {
        throw new UnsupportedOperationException("ReserveNow not supported in OCPP/S 1.2");
    }

    @Override
    public void handle(CancelReservationRequestedEvent event, CorrelationToken correlationToken) {
        throw new UnsupportedOperationException("CancelReservation not supported in OCPP/S 1.2");
    }

    public void setChargingStationOcpp12Client(ChargingStationOcpp12Client chargingStationOcpp12Client) {
        this.chargingStationOcpp12Client = chargingStationOcpp12Client;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * Sets the add-on id. The add-on is hardcoded, the add-on id should be different for every instance (in a distributed configuration)
     * to be able to differentiate between add-on instances.
     *
     * @param id add-on id.
     */
    public void setAddOnId(String id) {
        addOnIdentity = new TypeBasedAddOnIdentity(ADD_ON_TYPE, id);
    }

}
