package project.mini.batch3.vttp.miniprojectserver.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import project.mini.batch3.vttp.miniprojectserver.exceptions.AppException;
import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocument;
import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocumentWithId;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;

@Repository
public class RouteRepository {

    @Value("${google.api.key}")
    private String API_KEY;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SVC_HOST = "https://routes.googleapis.com";
    private static final String SVC_PATH = "/directions/v2:computeRoutes";
    private static final String FIELD_MASK = "routes.routeLabels,routes.localizedValues,routes.description,routes.legs.startLocation,routes.legs.endLocation,routes.legs.steps.localizedValues,routes.legs.steps.startLocation,routes.legs.steps.endLocation,routes.legs.steps.transitDetails,routes.legs.stepsOverview,routes.legs.steps.navigationInstruction,routes.legs.steps.travelMode,routes.routeToken";
    private static final String C_ROUTES = "routeRequests";
    private static final String F_ID = "_id";
    private static final String F_INDEXES = "indexes";
    private static final String F_ROUTEREQUESTS = "routeRequest";

    private static final String getAllSavedRouteIdsByUserId = "select RouteRequestId from saved_routes where user_id = ?;";
    private static final String insertRouteIdByUserId = "insert into saved_routes (user_id, RouteRequestId) values (?, ?);";
    private static final String deleteRouteByUserIdAndObjId = "delete from saved_routes where user_id = ? and RouteRequestId = ?";

    public String queryRoutes(RouteRequest routeReq) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", API_KEY);
        headers.set("X-Goog-FieldMask", FIELD_MASK);
        headers.set("Referer", "https://transit-production.up.railway.app");

        HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(routeReq).toString(), headers);
        String response = rest.postForObject(SVC_HOST + SVC_PATH, request, String.class);
        return response;
    }

    @Transactional
    public ObjectId saveRouteDocument(RouteRequestDocument routeReq) {
        Document doc = Document.parse(new Gson().toJson(routeReq).toString());
        try {
            Document newDoc = mongoTemplate.insert(doc, C_ROUTES);
            return newDoc.getObjectId(F_ID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("saveRouteDocument fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean saveRouteId(String routeId, String userId) {
        try {
            Integer iResult = jdbcTemplate.update(insertRouteIdByUserId, userId, routeId);
            return iResult > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("saveRouteId fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateRouteIndex(String id, Integer index) {
        Query query = new Query().addCriteria(Criteria.where(F_ID).is(new ObjectId(id)));
        Update update = new Update().addToSet(F_INDEXES, index);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

        mongoTemplate.findAndModify(query, update, options, RouteRequestDocument.class, C_ROUTES);
    }

    public List<RouteRequestDocumentWithId> getSavedRouteRequests(List<String> requestIds) {
        List<ObjectId> requestObjIds = new ArrayList<>();
        for (String id : requestIds) {
            ObjectId objId = new ObjectId(id);
            requestObjIds.add(objId);
        }

        AggregationOperation projectionOp = context -> {
            Document toString = new Document("$toString", "$_id");
            Document projectFields = new Document("_id", toString).append(F_ROUTEREQUESTS, 1).append(F_INDEXES, 1);
            Document project = new Document("$project", projectFields);

            return project;
        };
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(F_ID).in(requestObjIds)),
                projectionOp);
        List<RouteRequestDocumentWithId> result = mongoTemplate
                .aggregate(aggregation, C_ROUTES, RouteRequestDocumentWithId.class).getMappedResults();
        return result;
    }

    public Optional<List<String>> getUserSavedRoutes(String userId) {
        Optional<List<String>> optSavedRoutes = Optional.empty();

        List<String> results = jdbcTemplate.query(getAllSavedRouteIdsByUserId, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        }, userId);

        optSavedRoutes = Optional.ofNullable(results);

        return optSavedRoutes;
    }

    // add method to remove saved routes
    @Transactional
    public void deleteRouteIndex(String id, Integer indexToRemove) {
        Query query = new Query(Criteria.where(F_ID).is(new ObjectId(id)));
        Update update = new Update().pull(F_INDEXES, indexToRemove);
        mongoTemplate.updateFirst(query, update, RouteRequestDocumentWithId.class, C_ROUTES);
    }

    @Transactional
    public boolean checkAndDeleteIfEmpty(String id) {
        Query query = new Query(Criteria.where(F_ID).is(new ObjectId(id)));
        RouteRequestDocumentWithId request = mongoTemplate.findOne(query, RouteRequestDocumentWithId.class, C_ROUTES);

        if (request != null && request.getIndexes() != null && request.getIndexes().length == 0) {
            mongoTemplate.remove(query, RouteRequestDocumentWithId.class, C_ROUTES);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteRouteFromUser(String id, String userId) {
        Integer dResult = jdbcTemplate.update(deleteRouteByUserIdAndObjId, userId, id);

        return dResult > 0;
    }
}
