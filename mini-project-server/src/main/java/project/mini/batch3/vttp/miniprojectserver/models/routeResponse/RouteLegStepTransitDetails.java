package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteLegStepTransitDetails {
    
    TransitStopDetails stopDetails;
    TransitDetailsLocalizedValues localizedValues;
    String headsign;
    String headway;
    TransitLine transitLine;
    Integer stopCount;
    String tripShortText;
}
