package project.mini.batch3.vttp.miniprojectserver.models.routeRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteModifiers {
    Boolean avoidTolls;
    Boolean avoidHighways;
    Boolean avoidFerries;
    Boolean avoidIndoor;
    VehicleInfo vehicleInfo;
    String tollPass;
}
