package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.general.Dialect;
import com.fhv.uebersetzer.model.general.German;

import java.util.List;

public interface DialectDAL {
    //create
    void createNewLanguageCollection(String dialectLanguage);
    Dialect createDialectEntry(String dialectLanguageCollection, Dialect dialectEntry);
    //read
    List<Dialect> getAll(String collection);
    Dialect findDialectEntryById(String dialectLanguageCollection, String id);
    List<Dialect> findEntriesByName(String dialectLanguageCollection, String name);
    List<Dialect> findAllDialectEntriesPaginated(String dialectLanguageCollection, int pageNumber, int pageSize);
    List<Dialect> findDialectEntriesByNamePaginated(String dialectLanguageCollection, String entryName, int pageNumber, int pageSize);
    //update
    Dialect updateEntry(String dialectLanguageCollection, Dialect dialectEntry);
    //delete

    //String removeOneEntryById(String dialectLanguageCollection, String id); // should return reference To german entry if exists
    //void deleteEntryByObject(String dialectLanguageCollection, Dialect obj);
    //void deleteGermanReferenceFromDialect(String dialectLanguageCollection, String id);


    void addGermanReferenceToDialect(String dialectCollection, String dialectId, String germanId);
    String deleteGermanReferenceFromDialect(String dialectCollection, String dialectId);


    void addOneSynonym(String ownDialectCollection, String ownId, String synId);

    void removeOneSynonym(String ownDialectCollection, String ownId, String synId);

    German getGermanByDialectId(String dialectLanguage, String dialectId);

    Dialect findOneByIdAndRemove(String dialectLanguage, String id);

}
