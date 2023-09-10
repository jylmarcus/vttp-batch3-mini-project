package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteLeg {
    Integer distanceMeters;
    String duration;
    String staticDuration;
    Polyline polyline;
    Location startLocation;
    Location endLocation;
    RouteLegStep[] steps;
    RouteLegTravelAdvisory travelAdvisory;
    RouteLegLocalizedValues localizedValues;
    StepsOverview stepsOverview;
}
