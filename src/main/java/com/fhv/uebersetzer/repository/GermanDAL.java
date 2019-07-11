package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.general.German;

import java.util.List;
import java.util.Set;

public interface GermanDAL {
    //create
    German createGermanEntry(German germanEntry);
    //read
    German findGermanById(String id);
    List<German> findEntriesByName(String name);
    List<German> findAllGermanEntriesPaginated(int pageNumber, int pageSize);
    List<German> findGermanEntriesByNamePaginated(String entryName, int pageNumber, int pageSize);
    //update
    German updateEntry(German germanEntry);
    //delete
    German findOneByIdAndRemove(String id);



    void addDialectReferenceToGerman(String someDialectCollection, String someDialectEntryId, String germanId);

    void deleteDialectReferenceFromGerman(String otherCollection, String germanId);

    //void biDirectionalRefAdding(String dialectLanguage, String dialectId, String germanId);
    //void biDirectionalRefDeletion(String dialectLanguage, String dialectId, String germanId);

    Set<String> getAllLanguages();
    List<German> getAllGermanEntries();
}
