package project.mini.batch3.vttp.miniprojectserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDocument {
    
    RouteRequest routeRequest;
    Integer[] indexes;
}
