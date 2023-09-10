package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeResponse.enums.TransitVehicleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransitVehicle {
    
    LocalizedText name;
    TransitVehicleType type;
    String iconUri;
    String localIconUri;
}
