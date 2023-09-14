package project.mini.batch3.vttp.miniprojectserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocument;
import project.mini.batch3.vttp.miniprojectserver.models.UserDto;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.Waypoint;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RouteTravelMode;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.enums.RoutingPreference;
import project.mini.batch3.vttp.miniprojectserver.services.TravelService;

@RestController
@RequestMapping("/api/routes")
public class RouteSearchController {

    @Autowired
    TravelService travelSvc;

    @GetMapping(path = "/routeSearch", produces = "application/json")
    // initial params: origin, destination, departure date, departure time
    // default params: routetravelmode + routingpreference,
    // computeAlternativeRoutes: true,
    // units: "METRIC"
    // params to not touch because not in scope: languageCode, regionCode,
    // routeModifiers, trafficModel, transitPreferences.allowedtravelmodes,
    // transitpreferences.transitRoutingPreference
    // ran out of time to implement controls for default params, arrival time
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
                true,
                units);
        try {
            String resp = travelSvc.queryRoutes(request);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
    }

    @GetMapping(path = "/savedRoutes", produces="text/html")
    public ResponseEntity<String> getSavedRoutes(Authentication authentication) {
        UserDto user = (UserDto)authentication.getPrincipal();
        String userId = user.getId();
        try {
            String requests = travelSvc.getSavedRoutes(userId);
            return ResponseEntity.ok().body(requests);
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @PostMapping(path = "/saveRoute", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveRoute(@RequestBody RouteRequestDocument request, Authentication authentication) {
        UserDto user = (UserDto)authentication.getPrincipal();
        String userId = user.getId();
        System.out.println(userId);
        try {
            String objId = travelSvc.saveRoute(request, userId);
            String resp = "{\"objId\": \"" + objId + "\"}";
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @PutMapping(path = "/saveRoute/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateRouteIndex(@PathVariable String id, @RequestBody Integer index) {
        try {
            travelSvc.updateRouteIndex(id, index);
            String resp = "{\"message\": \"Route saved successfully\"}";
            return ResponseEntity.ok().body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @DeleteMapping(path="/deleteRoute/{id}", consumes ="application/json", produces = "application/json")
    public ResponseEntity<String> deleteRoute(@PathVariable String id, @RequestBody Integer index, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        String userId = user.getId();
        try{
            travelSvc.deleteRoute(id, index, userId);
            String resp = "{\"message\": \"Route deleted successfully\", \"index\":" + index + "}";
            return ResponseEntity.ok().body(resp);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }
}
