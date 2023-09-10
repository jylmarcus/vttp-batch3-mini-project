package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteLocalizedValues {
    LocalizedText distance;
    LocalizedText duration;
    LocalizedText staticDuration;
    LocalizedText transitFare;
}
