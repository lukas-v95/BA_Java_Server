package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.general.Dialect;
import com.fhv.uebersetzer.model.general.German;
import com.fhv.uebersetzer.model.general.German;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class GermanDALImpl implements GermanDAL {

    private final MongoTemplate mongoTemplate;

    @Autowired
    GermanDALImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public German createGermanEntry(German germanEntry) {
        germanEntry.setGermanEntryLowerCase(germanEntry.getGermanEntry().toLowerCase());
        return mongoTemplate.save(germanEntry);
    }


    @Override
    public German findGermanById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        return mongoTemplate.findOne(query, German.class);
    }

    @Override
    public List<German> findEntriesByName(String name) {
        name = name.toLowerCase();

        Query query = new Query();
        query.addCriteria(Criteria.where("germanEntryLowerCase").is(name));
        return mongoTemplate.find(query, German.class);
    }


    @Override
    public German updateEntry(German germanEntry) {

        germanEntry.setGermanEntry(germanEntry.getGermanEntry().toLowerCase());

        mongoTemplate.save(germanEntry);
        return germanEntry;
    }

    @Override
    public List<German> findAllGermanEntriesPaginated(int pageNumber, int pageSize) {
        Query query = new Query();
        query.skip(pageNumber * pageSize);
        query.limit(pageSize);
        return mongoTemplate.find(query, German.class);
    }

    @Override
    public List<German> findGermanEntriesByNamePaginated(String entryName, int pageNumber, int pageSize) {
        entryName = entryName.toLowerCase();

        Query query = new Query();
        query.skip(pageNumber * pageSize);
        query.addCriteria(Criteria.where("germanEntryLowerCase").is(entryName));
        query.limit(pageSize);
        return mongoTemplate.find(query, German.class);
    }

    @Override
    public German findOneByIdAndRemove(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        return mongoTemplate.findAndRemove(query, German.class);

    }

    @Override
    public void addDialectReferenceToGerman(String otherCollection, String otherEntryId, String germanId) {
        otherCollection = otherCollection.toLowerCase();

        if(mongoTemplate.collectionExists(otherCollection)){

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(otherEntryId));

            if (mongoTemplate.exists(query, Dialect.class, otherCollection)){

                German g =  findGermanById(germanId);
                if(g == null){
                    System.out.println("Es konnte das deutsche Objekt nicht per Id gefunden werden." +
                            " Mache alle changes Rückgängig");
                    return;
                }
                g.setReverseTranslation(otherCollection, otherEntryId);

                mongoTemplate.save(g);
            }
        }
    }

    @Override
    public void deleteDialectReferenceFromGerman(String otherCollection, String germanId){
        otherCollection = otherCollection.toLowerCase();

        if(mongoTemplate.collectionExists(otherCollection)){

            German g = findGermanById(germanId);
            if(g == null ){
                System.out.println("no valid reference to dialect!");
                return;
            }
            g.removeOneRetourUebersetzung(otherCollection);

            mongoTemplate.save(g);
        }
    }

    @Override
    public Set<String> getAllLanguages() {
        return mongoTemplate.getCollectionNames();
    }

    @Override
    public List<German> getAllGermanEntries() {

        return mongoTemplate.findAll(German.class);
    }
}
