package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import org.springframework.beans.factory.parsing.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RouteTravelMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteLegStep {
    Integer distanceMeters;
    String staticDuration;
    Polyline polyline;
    Location starLocation;
    Location endLocation;
    NavigationInstruction navigationInstruction;
    RouteLegStepTravelAdvisory travelAdvisory;
    RouteLegStepLocalizedValues localizedValues;
    RouteLegStepTransitDetails transitDetails;
    RouteTravelMode travelMode;
}
