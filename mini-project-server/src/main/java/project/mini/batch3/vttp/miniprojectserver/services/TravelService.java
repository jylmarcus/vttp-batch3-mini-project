package project.mini.batch3.vttp.miniprojectserver.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import project.mini.batch3.vttp.miniprojectserver.exceptions.AppException;
import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocument;
import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocumentWithId;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;
import project.mini.batch3.vttp.miniprojectserver.repositories.RouteRepository;

@Service
public class TravelService {

    @Autowired
    RouteRepository routeRepo;

    public String queryRoutes(RouteRequest request) {
        return routeRepo.queryRoutes(request);
    }

    // save request in mongodb, obtain _id
    // save user_id and _id in mysql
    @Transactional(rollbackFor = { AppException.class, DataAccessException.class })
    public String saveRoute(RouteRequestDocument routeRequestDocument, String userId) throws AppException {
        String objId = null;

        try {
            objId = routeRepo.saveRouteDocument(routeRequestDocument).toString();

            if (!routeRepo.saveRouteId(objId, userId)) {
                throw new AppException("Unable to save route", HttpStatus.BAD_REQUEST);
            }

        } catch (DataAccessException e) {
            throw new AppException("Database error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new AppException("Unexpected error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return objId;
    }

    public void updateRouteIndex(String userId, Integer index) {
        routeRepo.updateRouteIndex(userId, index);
    }

    // get _id s from mysql
    // query for requests and indexes using _id s in mongodb
    public String getSavedRoutes(String userId) {
        Optional<List<String>> requestIdsOpt = routeRepo.getUserSavedRoutes(userId);
        if (requestIdsOpt.isEmpty()) {
            throw new AppException("You do not have any saved routes", HttpStatus.NOT_FOUND);
        }

        List<RouteRequestDocumentWithId> requestDocuments = routeRepo.getSavedRouteRequests(requestIdsOpt.get());
        
        return new Gson().toJson(requestDocuments);
    }

    // delete index of indexes in mongodb
    // if indexes is empty, delete user_id and _id from mysql
    @Transactional(rollbackFor = {AppException.class, DataAccessException.class})
    public void deleteRoute(String id, Integer index, String userId) {
        try{
            routeRepo.deleteRouteIndex(id, index);
            boolean result = routeRepo.checkAndDeleteIfEmpty(id);
            if(result) {
                boolean sqldresult = routeRepo.deleteRouteFromUser(id, userId);
                if(!sqldresult) {
                    throw new AppException("Unable to delete route sqlfail", HttpStatus.NOT_FOUND);
                }
            }

        } catch (DataAccessException e) {
            throw new AppException("Database error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("Unexpected error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
