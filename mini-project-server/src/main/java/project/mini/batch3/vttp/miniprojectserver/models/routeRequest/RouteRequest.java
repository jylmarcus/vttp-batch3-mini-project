package project.mini.batch3.vttp.miniprojectserver.models.routeRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RouteTravelMode;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RoutingPreference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteRequest {
    
    Waypoint origin;
    Waypoint destination;
    RouteTravelMode travelMode;
    RoutingPreference routingPreference;
    String departureTime;
    String arrivalTime;
    boolean computeAlternativeRoutes;
    String units;
}
