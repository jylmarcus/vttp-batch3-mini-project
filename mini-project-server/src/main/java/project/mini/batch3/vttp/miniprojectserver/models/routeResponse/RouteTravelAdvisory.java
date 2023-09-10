package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteTravelAdvisory {
    TollInfo tollInfo;
    SpeedReadingInterval[] speedReadingIntervals;
    String fuelConsumptionMicroliters;
    Boolean routeRestrictionsPartiallyIgnored;
    Money transitFare;
}
