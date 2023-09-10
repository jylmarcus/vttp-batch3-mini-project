package project.mini.batch3.vttp.miniprojectserver.models.routeRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.AllowedTravelModes;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.TransitRoutingPreference;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransitPreferences {
    AllowedTravelModes[] allowedTravelModes;
    TransitRoutingPreference routingPreference;
}
