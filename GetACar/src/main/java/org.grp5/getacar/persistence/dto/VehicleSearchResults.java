package org.grp5.getacar.persistence.dto;

import org.grp5.getacar.resource.form.SearchVehiclesForm;

import java.util.List;

/**
 * Represents vehicle search results.
 */
public class VehicleSearchResults {
    private List<VehicleSearchResult> vehicleSearchResults;
    private SearchVehiclesForm searchParameters;

    public List<VehicleSearchResult> getVehicleSearchResults() {
        return vehicleSearchResults;
    }

    public void setVehicleSearchResults(List<VehicleSearchResult> vehicleSearchResults) {
        this.vehicleSearchResults = vehicleSearchResults;
    }

    public SearchVehiclesForm getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(SearchVehiclesForm searchParameters) {
        this.searchParameters = searchParameters;
    }
}
