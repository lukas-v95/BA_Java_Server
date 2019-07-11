package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.general.Dialect;
import com.fhv.uebersetzer.model.general.German;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DialectDALImpl implements DialectDAL {

    private final MongoTemplate mongoTemplate;
    private static final Logger LOG = LoggerFactory.getLogger("DialectDB logger: ");


    @Autowired
    DialectDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void createNewLanguageCollection(String dialectLanguage) {
        dialectLanguage = dialectLanguage.toLowerCase();

        LOG.info("createNewLanguageCollection called.");
        if (!mongoTemplate.collectionExists(dialectLanguage)) {
            LOG.debug("creating new collection");
            mongoTemplate.createCollection(dialectLanguage);
            System.out.println("logger not working?");
            LOG.info("created new collection.");
        } else {
            LOG.info("collection already there.");
            System.out.println("collection already there.");
        }

    }

    @Override
    public Dialect createDialectEntry(String dialectLanguageCollection, Dialect dialectEntry) {
        dialectLanguageCollection = dialectLanguageCollection.toLowerCase();
        dialectEntry.setDialectEntryLowerCase(dialectEntry.getDialectEntry().toLowerCase());

        if (mongoTemplate.collectionExists(dialectLanguageCollection)) {
            LOG.debug("creating new Entry in specified collection.");
            Dialect d = mongoTemplate.save(dialectEntry, dialectLanguageCollection);
            LOG.debug("created new entry in collection.");
            return d;
        }
        System.out.println("Error in creating Dialect Entry. You have to create the DialectLanguage first!");
        return null;
    }

    @Override
    public List<Dialect> getAll(String collection) {
        collection = collection.toLowerCase();
        //Query query = new Query();
        // query.addCriteria()
        //    List<Dialect> values =  mongoTemplate.findAll(Dialect.class, collection);
        return mongoTemplate.findAll(Dialect.class, collection);

        //return values;

    }

    @Override
    public Dialect findDialectEntryById(String dialectLanguageCollection, String id) {
        dialectLanguageCollection = dialectLanguageCollection.toLowerCase();

        if (mongoTemplate.collectionExists(dialectLanguageCollection)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id)); // TODO nicht hartcoden und variablenname ezu englisch ändern!


//            return mongoTemplate.findOne(query, Dialect.class, dialectLanguage);
            return mongoTemplate.findOne(query, Dialect.class, dialectLanguageCollection);
        }
        return null;
    }

    @Override
    public List<Dialect> findEntriesByName(String dialectLanguageCollection, String name) {
        dialectLanguageCollection = dialectLanguageCollection.toLowerCase();
        name = name.toLowerCase();

        if (mongoTemplate.collectionExists(dialectLanguageCollection)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("dialectEntryLowerCase").is(name));
            return mongoTemplate.find(query, Dialect.class, dialectLanguageCollection); // .find() findet alle!
        }
        return null;
    }

    @Override
    public List<Dialect> findAllDialectEntriesPaginated(String dialectLanguageCollection, int pageNumber, int pageSize) {
        dialectLanguageCollection = dialectLanguageCollection.toLowerCase();


        if (mongoTemplate.collectionExists(dialectLanguageCollection)) {
            Query query = new Query();
            query.skip(pageNumber * pageSize);
            query.limit(pageSize);
            return mongoTemplate.find(query, Dialect.class, dialectLanguageCollection);
        }
        return null;
    }

    @Override
    public List<Dialect> findDialectEntriesByNamePaginated(String dialectLanguageCollection, String entryName, int pageNumber, int pageSize) {
        dialectLanguageCollection = dialectLanguageCollection.toLowerCase();
        entryName = entryName.toLowerCase();

        if (mongoTemplate.collectionExists(dialectLanguageCollection)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("dialectEntryLowerCase").is(entryName)); // TODO hardgecodedes entfernen.
            query.skip(pageNumber * pageSize);
            query.limit(pageSize);
            return mongoTemplate.find(query, Dialect.class, dialectLanguageCollection);
        }

        return null;
    }


    @Override
    public Dialect updateEntry(String dialectLanguageCollection, Dialect dialectEntry) {
        dialectLanguageCollection = dialectLanguageCollection.toLowerCase();
        dialectEntry.setDialectEntryLowerCase(dialectEntry.getDialectEntry().toLowerCase());    // ...  avoids inconsitent DB

        if (mongoTemplate.collectionExists(dialectLanguageCollection)) {
            return mongoTemplate.save(dialectEntry, dialectLanguageCollection);    // This will perform an insert if the object is not already present, that is an 'upsert'.
        }
        return null;
    }

/*
    @Override
    public String removeOneEntryById(String dialectLanguageCollection, String id) {
        if(mongoTemplate.collectionExists(dialectLanguageCollection)){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));


            String refToGerman = findDialectEntryById(dialectLanguageCollection, id).getRefToGermanId();

            mongoTemplate.remove(query, dialectLanguageCollection); // Remove the given object from the collection by id.
            return refToGerman;
        }
        return null;
    }
    */


/*

    @Override
    public void deleteEntryByObject(String dialectLanguageCollection, Dialect obj) {
        if(mongoTemplate.collectionExists(dialectLanguageCollection)){
            mongoTemplate.remove(obj, dialectLanguageCollection); // Removes the given object from the given collection.
        }

    }

 */

    @Override
    public void addGermanReferenceToDialect(String dialectCollection, String dialectId, String germanId) {
        dialectCollection = dialectCollection.toLowerCase();


        if (mongoTemplate.collectionExists(dialectCollection)) {

            Dialect dial = findDialectEntryById(dialectCollection, dialectId);

            //überprüfe ob german Id überhaupt existiert!
            // wenn sie nicht existiert tue nichts!
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(germanId));

            if (mongoTemplate.exists(query, German.class)) {
                German ger = mongoTemplate.findOne(query, German.class);
                if(ger == null){
                    System.out.println("german entry darf nicht null sein!");
                    return;
                }

                // füge germanId dem Dialekt hinzu!
                dial.setRefToGermanId(germanId);
                dial.setGermanEntry(ger.getGermanEntry());

                mongoTemplate.save(dial, dialectCollection);
                // Update update = new Update();
                //update.
                return;
            }

            System.out.println("No entry with the specified GermanId found.");

        }
    }

    @Override
    public String deleteGermanReferenceFromDialect(String dialectCollection, String dialectId) {
        dialectCollection = dialectCollection.toLowerCase();

        // removes germanId whether or not it is set.
        if (mongoTemplate.collectionExists(dialectCollection)) {
            Dialect d = findDialectEntryById(dialectCollection, dialectId);
            if(d == null || d.getRefToGermanId() == null){
                return null;
            }
            String idToRemove = d.getRefToGermanId();
            d.setRefToGermanId(null);   // muss nicht null sein aber egal.
            d.setGermanEntry(null);
            updateEntry(dialectCollection, d);
            return idToRemove;
        }
        return null;
    }

    @Override
    public void addOneSynonym(String ownDialectCollection, String ownId, String synId) {
        ownDialectCollection = ownDialectCollection.toLowerCase();

        if (mongoTemplate.collectionExists(ownDialectCollection)) {

            // füge synonym hunzu.
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(ownId));

            Dialect entry = findDialectEntryById(ownDialectCollection, ownId);
            if (entry == null){
                System.err.println("could not find dialect EntryId --> kein synonym wurde eingetragen!");
                return;
            }

            Dialect containsSynonymName = findDialectEntryById(ownDialectCollection, synId);
            entry.addSynonymId(synId, containsSynonymName.getDialectEntry());
            updateEntry(ownDialectCollection, entry);
        }
    }


    @Override
    public void removeOneSynonym(String ownDialectCollection, String ownId, String synId) {
        ownDialectCollection = ownDialectCollection.toLowerCase();

        if (mongoTemplate.collectionExists(ownDialectCollection)) {

            // delete Synonym
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(ownId));
            Dialect entry = findDialectEntryById(ownDialectCollection, ownId);
            if(entry == null){
                System.err.println("[DialectDAL]you just tried to delete a non existent entry!");
            }else {
                entry.removeSynonymObject(synId);
            }

            updateEntry(ownDialectCollection, entry);
        }
    }


    @Override
    public German getGermanByDialectId(String dialectLanguage, String dialectId) {
        dialectLanguage = dialectLanguage.toLowerCase();

        if (mongoTemplate.collectionExists(dialectLanguage)) {
            Query query = new Query();
            // query.addCriteria(Criteria.where("_id").is(dialectId).andOperator(Crit))
        }
        return null;
    }


    @Override
    public Dialect findOneByIdAndRemove(String dialectLanguage, String id) {
        dialectLanguage = dialectLanguage.toLowerCase();

        if (mongoTemplate.collectionExists(dialectLanguage)) {

            Query q = new Query();
            q.addCriteria(Criteria.where("_id").is(id));
            return mongoTemplate.findAndRemove(q, Dialect.class, dialectLanguage);
        }

        return null;
    }

}
