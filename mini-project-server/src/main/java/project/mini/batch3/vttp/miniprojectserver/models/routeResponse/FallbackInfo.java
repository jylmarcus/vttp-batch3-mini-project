package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeResponse.enums.FallbackReason;
import project.mini.batch3.vttp.miniprojectserver.models.routeResponse.enums.FallbackRoutingMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FallbackInfo {
    FallbackRoutingMode routingMode;
    FallbackReason reason;
}
