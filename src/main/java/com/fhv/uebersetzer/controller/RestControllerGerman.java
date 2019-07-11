package com.fhv.uebersetzer.controller;

import com.fhv.uebersetzer.model.general.German;
import com.fhv.uebersetzer.model.general.ObjectMappers.GermanMapper;
import com.fhv.uebersetzer.model.general.ReverseTranslation;
import com.fhv.uebersetzer.repository.DialectDAL;
import com.fhv.uebersetzer.repository.GermanDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/german/")
public class RestControllerGerman {


    private final GermanDAL germanDAL;
    private final DialectDAL dialectDAL;

    @Autowired
    public RestControllerGerman(GermanDAL germanDAL, DialectDAL dialectDAL) {
        this.germanDAL = germanDAL;
        this.dialectDAL = dialectDAL;
    }


    private static final Logger LOG = LoggerFactory.getLogger("GermanRestControllerLogger");


    @PostMapping("/tester")
    public German tester(){
        German g = new German();
        g.setGermanEntry("Hallo");
        g.setGermanEntryLowerCase("hallo");
        ReverseTranslation rev = new ReverseTranslation();

        return g;
    }


    @PostMapping("/createGermanEntry")
    public String createGermanEntry(@RequestBody German germanEntry){
        if(GermanMapper.performObjectCreationChecks(germanEntry) == null){
            return "Unzulässige Parameter vermieden das Erstellung des deutschen Eintrages!";
        }

        germanDAL.createGermanEntry(germanEntry);

        return "created a german entry.";
    }
    /*
    @Deprecated
    @PostMapping("addDialectReferenceToGerman")
    public String addDialectReferenceToGerman(@RequestParam String dialectCollection, @RequestParam String dialectId, @RequestParam String germanId ){

        germanDAL.addDialectReferenceToGerman(dialectCollection, dialectId, germanId);
        return null;
    }

     */

    /*
    @Deprecated
    @DeleteMapping("removeDialectReferenceFromGerman")
    public String removeDialectReferenceFromGerman(@RequestParam String otherCollection, @RequestParam String germanId){
        germanDAL.deleteDialectReferenceFromGerman(otherCollection, germanId);
        return "deleted entry.";
    }

     */

    @GetMapping("/getGermanEntryById")
    public German getGermanEntryById(@RequestParam String id){
        German g = germanDAL.findGermanById(id);
        return g;
    }

    @GetMapping("/getGermanEntriesByName")
    public List<German> getGermanEntriesByName(@RequestParam String name){
        List<German> g = germanDAL.findEntriesByName(name);
        return g;
    }

    @GetMapping("/getAllGermanEntriesPaginated")
    public List<German> getAllGermanEntriesPaginated(@RequestParam int pageNumber, int pageSize){

        return germanDAL.findAllGermanEntriesPaginated(pageNumber, pageSize);
    }


    @GetMapping("/getGermanEntriesByNamePaginated")
    public List<German> getGermanEntriesPaginated(@RequestParam String entryName, @RequestParam int pageNumber, int pageSize){

        return germanDAL.findGermanEntriesByNamePaginated(entryName, pageNumber, pageSize);
    }


    @PutMapping("/updateGermanEntryById")
    public String updateEntryById(@RequestParam String germanId, String germanEntry){ // todo change changed value to optional!!!
        German g = germanDAL.findGermanById(germanId);
        g.setGermanEntry(germanEntry);


        germanDAL.updateEntry(g);
        return "updated entry!";
    }


    @PutMapping("/updateGermanEntryByObj")
    public String updateEntryByObj(@RequestBody German german){
        if(GermanMapper.performObjectUpdateChecks(german) == null){
            System.err.println("You must specify an Id");
            return "you have to specify an id!";
        }

        // wenn die referenz auf dialektwörter geändert wurde, dann ignoriere die changes der referenz auf dialekwörter:

        German oldGermanEntry = getGermanEntryById(german.getGermanId());


        if(checkForEqualReferences(oldGermanEntry, german)){
            germanDAL.updateEntry(german);
            return "updated entry";
        } else {
            System.out.println("could not update entry. Reason: invalid references to other languages!");
            return "could not update entry. Reason: invalid references to other languages!";
        }
    }

    // returns false for not equal!
    private boolean checkForEqualReferences(German g1, German g2){
        List<ReverseTranslation> g1List = g1.getReverseTranslations();
        List<ReverseTranslation> g2List = g2.getReverseTranslations();

        if(g1List.size() != g2List.size()){
            return false;
        }
        boolean equals = true;
        for(int i = 0; i < g1List.size(); i++){
            for(int j = 0 ; j < g1List.get(i).getReverseGerman2DialectIdList().size(); j++){
                if(!
                g1List.get(i).getReverseGerman2DialectIdList().get(j).equals( g2List.get(i).getReverseGerman2DialectIdList().get(j)) ){
                    return false;
                }
            }


        }
        return true;
    }

    /**
     * Deletes german Entry. After that, all existing dialect references pointing to the deleted german entry, get deleted too.
     */
    @DeleteMapping("/deleteEntryById")
    public String deleteEntry(@RequestParam String id){
        German g = germanDAL.findOneByIdAndRemove(id);
        if(g == null){
            System.out.println("There is no entry with ID "+ id);
            return null;
        }
        List<ReverseTranslation> reverseTranslationList = g.getReverseTranslations();
        System.out.println("Deleted entry with Id " + id);

        // delete references from each dialect:
        deleteReferencesAfterGermanEntryDeletion(reverseTranslationList, id);
        return "deleted specified entry";
    }

    private void deleteReferencesAfterGermanEntryDeletion(List<ReverseTranslation> reverseTranslationList, String germanId){
        for(int i = 0; i < reverseTranslationList.size(); i++ ){
            ReverseTranslation oneObject = reverseTranslationList.get(i);

            for(String id : oneObject.getReverseGerman2DialectIdList()){
                dialectDAL.deleteGermanReferenceFromDialect(oneObject.getReverseGerman2DialectLanguage(), germanId);
            }
        }
        System.out.println("Deleted all referenced Dialect entries of the deleted german entry with Id:" + germanId + ".");
    }


    @PostMapping("addBiDirectionalRef")
    public String addBiDirectionalReferences(@RequestParam String dialectCollection, @RequestParam String dialectId, @RequestParam String germanId){

        germanDAL.addDialectReferenceToGerman(dialectCollection, dialectId, germanId);
        dialectDAL.addGermanReferenceToDialect(dialectCollection, dialectId, germanId);

        return "added bidirectional references from DialectRest.";
    }
    @DeleteMapping("deleteBiDirectionalRef")
    public String deleteBiDirectionalReferences(@RequestParam String dialectLanguage, @RequestParam String dialectId, @RequestParam String germanId){

        germanDAL.deleteDialectReferenceFromGerman(dialectLanguage, germanId);
        dialectDAL.deleteGermanReferenceFromDialect(dialectLanguage, dialectId);

        return "deleted references bidirectional.";
    }

    @GetMapping(value="getAllLanguages", produces={MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public Set<String> getAllLanguages() {
        Set<String> set = germanDAL.getAllLanguages();
        set.remove("germancollection");
        set.remove("User");
        set.remove("users");
        set.remove("userroles");
        return set;
    }


    @GetMapping(value="getAllDEL")
        public Set<String> getAllLanguages2(){
        System.out.println("test successs????");
            return germanDAL.getAllLanguages();

    }

    @GetMapping(value="getAllGermanEntries")
    public List<German> getAllGermanEntries(){
        return germanDAL.getAllGermanEntries();
    }



}
