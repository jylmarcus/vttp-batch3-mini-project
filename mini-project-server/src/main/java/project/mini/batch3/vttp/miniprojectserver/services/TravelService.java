package project.mini.batch3.vttp.miniprojectserver.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocument;
import project.mini.batch3.vttp.miniprojectserver.models.SavedRouteResponse;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;
import project.mini.batch3.vttp.miniprojectserver.repositories.RouteRepository;

@Service
public class TravelService {

    @Autowired
    RouteRepository routeRepo;

    public String queryRoutes(RouteRequest request) {
        return routeRepo.queryRoutes(request);
    }

    public String saveRoute(RouteRequestDocument routeRequestDocument) {
        String objId = routeRepo.saveRoute(routeRequestDocument).toString();
        //method to save objId to mysql database
        return objId;
    }

    public void updateRouteIndex(String id, Integer index) {
        routeRepo.updateRouteIndex(id, index);
    }

    public String getSavedRoutes(String id) {
        //method to get objIds from sql
        List<String> requestIds = new LinkedList<>(); //TODO - implement sql method
        List<RouteRequestDocument> requestDocuments = routeRepo.getSavedRouteRequests(requestIds);
        //every route request produces a response
        List<SavedRouteResponse> routeQueries = new LinkedList<>();
        for(RouteRequestDocument doc : requestDocuments) {
            SavedRouteResponse resp = new SavedRouteResponse(routeRepo.queryRoutes(doc.getRouteRequest()), doc.getIndexes());
            routeQueries.add(resp);
        }
        return new Gson().toJson(routeQueries).toString();
    }
}
