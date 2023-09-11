package project.mini.batch3.vttp.miniprojectserver.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import project.mini.batch3.vttp.miniprojectserver.exceptions.AppException;
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

    @Transactional(rollbackFor = AppException.class)
    public String saveRoute(RouteRequestDocument routeRequestDocument, String userId) throws AppException {
        String objId = routeRepo.saveRouteDocument(routeRequestDocument).toString();
        //method to save objId to mysql database

        if(!routeRepo.saveRouteId(objId, userId)) {
            throw new AppException("Unable to save route", HttpStatus.BAD_REQUEST);
        }

        routeRepo.saveRouteId(objId, userId);
        
        return objId;
    }

    public void updateRouteIndex(String userId, Integer index) {
        routeRepo.updateRouteIndex(userId, index);
    }

    public String getSavedRoutes(String userId) {
        //method to get objIds from sql
        Optional<List<String>> requestIdsOpt = routeRepo.getUserSavedRoutes(userId);
        if(requestIdsOpt.isEmpty()) {
            throw new AppException("You do not have any saved routes", HttpStatus.NOT_FOUND);
        }

        List<RouteRequestDocument> requestDocuments = routeRepo.getSavedRouteRequests(requestIdsOpt.get());
        //every route request produces a response
        List<SavedRouteResponse> routeQueries = new LinkedList<>();
        for(RouteRequestDocument doc : requestDocuments) {
            SavedRouteResponse resp = new SavedRouteResponse(routeRepo.queryRoutes(doc.getRouteRequest()), doc.getIndexes());
            routeQueries.add(resp);
        }
        return new Gson().toJson(routeQueries).toString();
    }
}
