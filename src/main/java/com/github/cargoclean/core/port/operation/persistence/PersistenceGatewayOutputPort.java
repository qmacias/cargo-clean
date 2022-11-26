package com.github.cargoclean.core.port.operation.persistence;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.CargoInfo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.handling.EventId;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.handling.HandlingHistory;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.model.location.UnLocode;
import com.github.cargoclean.core.model.report.ExpectedArrivals;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PersistenceGatewayOutputPort {

    TrackingId nextTrackingId();

    EventId nextEventId();

    /**
     * Load all {@code Locations} from the database.
     *
     * @return list of locations
     */
    List<Location> allLocations();

    /**
     * Load {@code Location} with matching {@code unLocode} from the database
     *
     * @param unLocode UN location code
     * @return fully resolved location
     */
    Location obtainLocationByUnLocode(UnLocode unLocode);

    /**
     * Persist {@code Cargo} instance, and return instance with {@code id} generated by the database
     *
     * @param cargoToSave {@code Cargo}
     * @return saved {@code Cargo} instance with loaded relations, will have {@code id} not {@code null}
     */
    Cargo saveCargo(Cargo cargoToSave);

    /**
     * Load {@code Cargo} with matching {@code trackingId} from the database.
     *
     * @param trackingId tracking ID of the cargo
     * @return fully resolved {@code Cargo} instance
     */
    Cargo obtainCargoByTrackingId(TrackingId trackingId);

    /**
     * Delete {@code Cargo} with matching tracking ID.
     *
     * @param trackingId tracking ID of the cargo
     */
    void deleteCargo(TrackingId trackingId);

    List<ExpectedArrivals> queryForExpectedArrivals();

    void recordHandlingEvent(HandlingEvent event);

    HandlingHistory handlingHistory(TrackingId cargoId);

    void rollback();

    default Map<UnLocode, Region> allRegionsMap() {
        return allLocations().stream()
                .collect(Collectors.toUnmodifiableMap(Location::getUnlocode,
                        Location::getRegion));
    }

    default Map<UnLocode, Location> allLocationsMap() {
        return allLocations().stream()
                .collect(Collectors.toMap(Location::getUnlocode, Function.identity()));
    }

    boolean locationExists(Location location);

    Location saveLocation(Location location);

    List<CargoInfo> allCargoes();
}