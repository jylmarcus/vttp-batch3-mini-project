package project.mini.batch3.vttp.miniprojectserver.repositories;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import project.mini.batch3.vttp.miniprojectserver.models.RouteRequestDocument;
import project.mini.batch3.vttp.miniprojectserver.models.routeRequest.RouteRequest;

@Repository
public class RouteRepository {

    @Value("${google.api.key}")
    private String API_KEY;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String SVC_HOST = "https://routes.googleapis.com";
    private static final String SVC_PATH = "/directions/v2:computeRoutes";
    private static final String FIELD_MASK = "routes.routeLabels,routes.localizedValues,routes.description,routes.legs.startLocation,routes.legs.endLocation,routes.legs.steps.localizedValues,routes.legs.steps.startLocation,routes.legs.steps.endLocation,routes.legs.steps.transitDetails,routes.legs.stepsOverview,routes.legs.steps.navigationInstruction,routes.legs.steps.travelMode,routes.routeToken";
    private static final String C_ROUTES = "routeRequests";
    private static final String F_ID = "_id";
    private static final String F_INDEXES = "indexes";

    public String queryRoutes(RouteRequest routeReq) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", API_KEY);
        headers.set("X-Goog-FieldMask", FIELD_MASK);

        HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(routeReq).toString(), headers);
        String response = rest.postForObject(SVC_HOST + SVC_PATH, request, String.class);
        System.out.println(response);

        return response;
    }

    public ObjectId saveRoute(RouteRequestDocument routeReq) {
        Document doc = Document.parse(new Gson().toJson(routeReq).toString());

        Document newDoc = mongoTemplate.insert(doc, C_ROUTES);
        return newDoc.getObjectId(F_ID);
    }

    public void updateRouteIndex(String id, Integer index) {
        Query query = new Query().addCriteria(Criteria.where(F_ID).is(new ObjectId(id)));
        Update update = new Update().addToSet(F_INDEXES, index);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

        mongoTemplate.findAndModify(query, update, options, RouteRequestDocument.class);
    }

    public List<RouteRequestDocument> getSavedRouteRequests(List<String> requestIds) {
        Query query = new Query(Criteria.where(F_ID).in(requestIds));
        query.fields().exclude(F_ID);
        List<RouteRequestDocument> result = mongoTemplate.find(query, RouteRequestDocument.class);
        return result;
    }
}
