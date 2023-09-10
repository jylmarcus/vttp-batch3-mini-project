package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransitLine {
    
    TransitAgency[] agencies;
    String name;
    String uri;
    String color;
    String iconUri;
    String nameShort;
    String textColor;
    TransitVehicle vehicle;
}
