package project.mini.batch3.vttp.miniprojectserver.models.routeResponse;

import com.mongodb.client.model.geojson.LineString;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Polyline {
    String encodedPolyline;
    LineString geoJsonLineString;
}
