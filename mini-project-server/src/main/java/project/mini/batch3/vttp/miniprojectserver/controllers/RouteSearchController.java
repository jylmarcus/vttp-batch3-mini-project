package project.mini.batch3.vttp.miniprojectserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocument;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.Waypoint;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RouteTravelMode;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RoutingPreference;
import project.mini.batch3.vttp.miniprojectserver.services.TravelService;

@RestController
@RequestMapping("/api")
public class RouteSearchController {

    @Autowired
    TravelService travelSvc;

    @GetMapping(path = "/routeSearch", produces = "application/json")
    // initial params: origin, destination, departure date, departure time
    // default params: routetravelmode + routingpreference,
    // computeAlternativeRoutes: false,
    // units: "METRIC"
    // params to not touch because not in scope: languageCode, regionCode,
    // routeModifiers, trafficModel, transitPreferences.allowedtravelmodes,
    // transitpreferences.transitRoutingPreference
    public ResponseEntity<String> queryRoutes(@RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) RouteTravelMode travelMode,
            @RequestParam(required = false) String departureTime,
            @RequestParam(required = false) String arrivalTime,
            @RequestParam(required = false) boolean computeAlternativeRoutes,
            @RequestParam(required = false) String units) {

        RouteRequest request = new RouteRequest(
                new Waypoint(origin),
                new Waypoint(destination),
                travelMode != null ? travelMode : RouteTravelMode.TRANSIT,
                travelMode == RouteTravelMode.DRIVE ? RoutingPreference.TRAFFIC_AWARE
                        : RoutingPreference.ROUTING_PREFERENCE_UNSPECIFIED,
                departureTime != null ? departureTime : null,
                arrivalTime != null ? arrivalTime : null,
                computeAlternativeRoutes ? true : false,
                units);
        try {
            String resp = travelSvc.queryRoutes(request);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
    }

    @GetMapping(path = "/savedRoutes", produces="application/json")
    public ResponseEntity<String> getSavedRoutes() {
        String userId = "123"; //TODO - Implement user logins
        try {
            String resp = travelSvc.getSavedRoutes(userId);
            return ResponseEntity.ok().body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(404).body("Not found");
        }
    }

    @PostMapping(path = "/saveRoute", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveRoute(@RequestBody RouteRequestDocument request) {
        try {
            String objId = travelSvc.saveRoute(request);
            String resp = "{\"objId\": \"" + objId + "\"}";
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Unable to save route");
        }
    }

    @PostMapping(path = "/saveRoute/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateRouteIndex(@PathVariable String id, @RequestBody Integer index) {
        try {
            travelSvc.updateRouteIndex(id, index);
            String resp = "{\"message\": \"Route saved successfully\"}";
            return ResponseEntity.ok().body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Unable to save route");
        }
    }
}
