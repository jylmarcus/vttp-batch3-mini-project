package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeResponse.enums.Speed;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeedReadingInterval {
    Integer startPolylinePointIndex;
    Integer endPolylinePointIndex;
    Speed speed;
}
