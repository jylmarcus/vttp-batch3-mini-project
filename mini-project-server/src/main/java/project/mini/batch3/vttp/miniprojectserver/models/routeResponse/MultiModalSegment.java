package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RouteTravelMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiModalSegment {
    NavigationInstruction navigationInstruction;
    RouteTravelMode travelMode;
    Integer stepStartIndex;
    Integer stepEndIndex;
}
