package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import io.grpc.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodedWaypoint {
    Status geocoderStatus;
    String[] type;
    Boolean partialMatch;
    String placeId;
    Integer intermediateWaypointRequestIndex;
}
