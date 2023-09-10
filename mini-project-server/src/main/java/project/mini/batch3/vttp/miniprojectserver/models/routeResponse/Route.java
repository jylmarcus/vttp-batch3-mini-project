package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeResponse.enums.RouteLabel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    
    RouteLabel[] routeLabels;
    RouteLeg[] legs;
    Integer distanceMeters;
    String duration;
    String staticDuration;
    Polyline polyline;
    String description;
    String[] warnings;
    Viewport viewport;
    RouteTravelAdvisory travelAdvisory;
    RouteLocalizedValues localizedValues;
    String routeToken;

}
