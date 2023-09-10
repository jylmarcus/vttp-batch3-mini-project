package project.mini.batch3.vttp.miniprojectserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedRouteResponse {
    
    String response;
    Integer[] indexes;
}
