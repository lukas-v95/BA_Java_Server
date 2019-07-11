package com.fhv.uebersetzer.controller;

import com.fhv.uebersetzer.model.general.*;
import com.fhv.uebersetzer.model.general.ObjectMappers.DialectMapper;
import com.fhv.uebersetzer.model.general.enums.LinguisticEnum;
import com.fhv.uebersetzer.model.general.enums.PartOfSpeechEnum;
import com.fhv.uebersetzer.repository.DialectDAL;
import com.fhv.uebersetzer.repository.GermanDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dialect/")
public class RestControllerDialect { // namingconvention issue "RestController" not allowed

    private final GermanDAL germanDAL;
    private final DialectDAL dialectDAL;

    @Autowired
    public RestControllerDialect(GermanDAL germanDAL, DialectDAL dialectDAL) {
        this.germanDAL = germanDAL;
        this.dialectDAL = dialectDAL;
    }


    private static final Logger LOG = LoggerFactory.getLogger("DialectRestControllerLogger");

    @PostMapping("/example")
    public String example(@RequestBody ExampleClass example) {
        System.out.println(example);
        System.out.println("in example class##########################");
        return "hello?";
    }


    @GetMapping("/tester")
    public Dialect createB() {
        Dialect dialect = new Dialect("mywortEintrag", new PartOfSpeech(), new LinguisticUsage());
        //foo(dialect);
        dialect.setDialectEntry("Hoiasjkdfjklasdjklf");
        dialect.getLinguisticUsage().addOnelinguisticUsage(LinguisticEnum.ACAD);
        dialect.getLinguisticUsage().addOnelinguisticUsage(LinguisticEnum.SCIENCE);

        dialect.getPartOfSpeech().addOnePartOfSpeech(PartOfSpeechEnum.INTERJECTION);
        dialect.getPartOfSpeech().addOnePartOfSpeech(PartOfSpeechEnum.ARTICLE);

        System.out.println("RestControllerDialect OK");
        return foo(dialect);
    }

    @PostMapping("/foo")
    public Dialect foo(@RequestParam Dialect a) {

        //Dialect dialect = new Dialect();
        //System.out.println(dialect);
        return a;

    }

    @GetMapping("/getAllDialectEntries")
    public List<Dialect> getAll(@RequestParam String language) {
        return dialectDAL.getAll(language);
    }


    @PostMapping("/createDialectLanguage")
    public String createDialectLanguage(@RequestParam String language) {
        if(language.length() < 4){
            return null;
        }
        dialectDAL.createNewLanguageCollection(language);
        return "created Language.";
    }


    @PostMapping("firstTry")
    public Dialect firstTry(@RequestBody Dialect dialect) {


        System.out.println(dialect.getDialectId());
        PartOfSpeech partOfSpeech = new PartOfSpeech();
        partOfSpeech.addOnePartOfSpeech(PartOfSpeechEnum.INTERJECTION);
        LinguisticUsage linguisticUsage = new LinguisticUsage();
        linguisticUsage.addOnelinguisticUsage(LinguisticEnum.ACAD);
        //linguisticUsage.setEcon(true);

        Dialect entry = new Dialect("ersterTest", partOfSpeech, linguisticUsage, "einerandomID");


        return entry;

    }

    @PostMapping("createDialectEntry")
    public String secondTry(@RequestParam String dialectLanguage, @RequestBody Dialect dialectEntry) {

        System.err.println("create Dialect Entry wurde aufgerufen!!!");
        System.out.println(dialectEntry.getLinguisticUsage());
        System.out.println(dialectEntry.getPartOfSpeech());
        System.out.println(dialectEntry.getDialectEntry());
        if (DialectMapper.performObjectCreationChecks(dialectEntry) == null) {
            return "Unzulässige Parameter vermieden das Erstellung des dialekt Eintrages!";
        }

        dialectDAL.createDialectEntry(dialectLanguage.toLowerCase(), dialectEntry);
        return "created Entry?";

    }


    @GetMapping("/getDialectEntryById")
    public Dialect getDialectEntryById(@RequestParam String language, @RequestParam String id) {
        return dialectDAL.findDialectEntryById(language, id);
    }

    @GetMapping("/getDialectEntriesByName")
    public List<Dialect> getDialectEntriesByName(@RequestParam String language, @RequestParam String entryName) {
        return dialectDAL.findEntriesByName(language, entryName);
    }

    @GetMapping("/getAllDialectEntriesPaginated")
    public List<Dialect> getAllDialectEntriesPaginated(@RequestParam String language, @RequestParam int pageNumber, int pageSize) {

        return dialectDAL.findAllDialectEntriesPaginated(language, pageNumber, pageSize);
    }

    @GetMapping("/getDialectEntriesByNamePaginated")
    public List<Dialect> getDialectEntriesPaginated(@RequestParam String language, @RequestParam String entryName, @RequestParam int pageNumber, int pageSize) {

        return dialectDAL.findDialectEntriesByNamePaginated(language, entryName, pageNumber, pageSize);
    }

    @PutMapping("/updateDialectEntry")
    public Dialect updateDialectById(@RequestParam String dialectLanguage, @RequestBody Dialect dialectEntry) {

        if (DialectMapper.performObjectUpdateChecks(dialectEntry) == null) {
            System.err.println("insufficient update arguments");
            return null;
        }
        // wenn die mitgelieferte refToGerman ID von der alten abweicht: lösche referenz auch aus dem anderen Eintrag!
        Dialect oldDialectEntry = dialectDAL.findDialectEntryById(dialectLanguage, dialectEntry.getDialectId());
        if (dialectEntry.getRefToGermanId() == null) {
            System.err.println("Updating entry. No Reference defined! Nothing to do here!");
        } else {
            if (dialectEntry.getRefToGermanId().equals(oldDialectEntry.getRefToGermanId())) {
                System.out.println("alles okay, die referenz auf das deutsche wort hat sich nicht geändert!");
            } else {
                System.out.println("Die referenz auf ein deutsches Wort weicht ab! Es wird deshalb in beiden worten die referenz aufeinander gelöscht!");
                dialectEntry.setRefToGermanId(null);
                dialectEntry.setGermanEntry(null);
                deleteBiDirectionalReferences(dialectLanguage, oldDialectEntry.getDialectId(), oldDialectEntry.getRefToGermanId());
            }
        }

        return dialectDAL.updateEntry(dialectLanguage, dialectEntry);
    }


    @DeleteMapping(value = "deleteEntryById", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public String deleteEntryById(@RequestParam String language, @RequestParam String id) {
        System.out.println(language + ", " + id);

        deleteRefFromSynoyms(language, id);

        // es wird auch referenz in deuetsch mit gelöscht!
        Dialect dial = dialectDAL.findOneByIdAndRemove(language, id);
        String germanRef = dial.getRefToGermanId();

        if (germanRef != null) {
            germanDAL.deleteDialectReferenceFromGerman(language, germanRef);
        }

        return "deleted specified entry by Id";
    }

    @DeleteMapping("/deleteEntryByObject")
    public String deleteEntryByObject(@RequestParam String dialectlanguage, @RequestBody Dialect dialectEntry) { // TODO request params fehlen! TODO ansonnsten funktionierts!
        if (dialectEntry.getDialectId() == null) {
            return "You have to specify, which object you want to delete (Object ID missing)";
        }
        deleteRefFromSynoyms(dialectlanguage, dialectEntry.getDialectId());
        dialectDAL.findOneByIdAndRemove(dialectlanguage, dialectEntry.getDialectId());

        return "deleted specified entry by object";
    }

    public void deleteRefFromSynoyms(String language, String id) {
        Dialect d = dialectDAL.findDialectEntryById(language, id);
        List<String> synonymIds = d.getSynonymIdList();

        for (String synonymId : synonymIds) {
            Dialect synonymEntry = getDialectEntryById(language, synonymId);
            synonymEntry.removeSynonymObject(id);
            dialectDAL.updateEntry(language, synonymEntry);
            System.out.println("Removed synonym Id because the referenced dialect Entry got deleted.");
        }
    }

    /*
        @Deprecated
        @PostMapping("addGermanReferenceToDialect")
        public String addGermanReferenceToDialect(@RequestParam String dialectCollection, @RequestParam String dialectId, @RequestParam String germanId){
            dialectDAL.addGermanReferenceToDialect(dialectCollection, dialectId, germanId);
            return "added german Reference to Dialect.";
        }

     */
    // löscht auch refernz in deutsch!!!
    @DeleteMapping("/deleteGermanRefFromDialect")
    public String deleteGermanRefFromDialect(@RequestParam String dialectCollection, @RequestParam String dialectId) {
        String refToGerman = dialectDAL.deleteGermanReferenceFromDialect(dialectCollection, dialectId);

        if (refToGerman != null) {
            germanDAL.deleteDialectReferenceFromGerman(dialectCollection, dialectId);
        }

        return "deleted german Reference from dialect";
    }


    @PostMapping("addBiDirectionalRef")
    public String addBiDirectionalReferences(@RequestParam String dialectLanguage, @RequestParam String dialectId, @RequestParam String germanId) {

        Dialect dialectEntry = dialectDAL.findDialectEntryById(dialectLanguage, dialectId);

        //      System.out.println("aus addBiDirectionalRef: " );
        //       System.out.println(dialectEntry);
//        System.out.println(dialectEntry.getRefToGermanId());
        System.out.println("weiter gehts");
        if (dialectEntry == null) {
            System.out.println("Dialect Entry with Id" + dialectId + " Sprache: " + dialectLanguage + " not found.");
            return null;
        }
        if (dialectEntry.getRefToGermanId() != null) {
            return "remove german Reference ( " + dialectEntry.getDialectId() + " ) before adding a new one!";

        }
        ;


        germanDAL.addDialectReferenceToGerman(dialectLanguage, dialectId, germanId);
        dialectDAL.addGermanReferenceToDialect(dialectLanguage, dialectId, germanId);

        return "added bidirectional references from DialectRest.";
    }

    @DeleteMapping("removeBiDirectionalRef")
    public String deleteBiDirectionalReferences(@RequestParam String dialectLanguage, @RequestParam String dialectId, @RequestParam String germanId) {

        germanDAL.deleteDialectReferenceFromGerman(dialectLanguage, germanId);
        dialectDAL.deleteGermanReferenceFromDialect(dialectLanguage, dialectId);

        return "deleted references bidirectional.";
    }


    @PostMapping(value = "addBidirectionalSynonyms", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public String addSynonymsOneSynonym(@RequestParam String dialectLanguage, @RequestParam String id1, @RequestParam String id2) {
        dialectDAL.addOneSynonym(dialectLanguage, id1, id2);
        dialectDAL.addOneSynonym(dialectLanguage, id2, id1);
        return "added synonyms.";

    }


    @DeleteMapping("removeBidirectionalSynonyms")
    public String removeSynonyms(@RequestParam String dialectLanguage, @RequestParam String id1, @RequestParam String id2) {
        dialectDAL.removeOneSynonym(dialectLanguage, id1, id2);
        dialectDAL.removeOneSynonym(dialectLanguage, id2, id1);
        return "removed synonyms.";

    }

    /*
    // TODO sinnvoll nur einen eintrag zu löschen?
    @DeleteMapping("removeOnlyOneSynonym")
    public String removeOnlyOneSynonym(@RequestParam String dialectLanguage, @RequestParam String ownId, @RequestParam String idToRemove){
        dialectDAL.removeOneSynonym(dialectLanguage, ownId, idToRemove);
        return "removed one synonym";
    }

     */

    /**
     * AB HIER NUR MEHR METHODENAUFRUFE:
     */

    @GetMapping("/getCompleteEntryList")
    public List<SearchResult> getCompleteEntryList(@RequestParam String languageA, @RequestParam String languageB, @RequestParam String searchCriteria) {

        if (languageA.equalsIgnoreCase(languageB)) {
            return null;
        }


        // TODO noch zu berücksichtigen: was ist wenn es keine übersetzung zu einem 3er pack gibt?

        SearchResult aSearchResult = null;
        List<SearchResult> searchResults = new ArrayList<>();


        // hole alle Einträge (dialekt1) und speichere sie in eine Liste: Ziel: referenzId jedes Eitnrages auf german.
        List<Dialect> language1Entries = dialectDAL.findEntriesByName(languageA, searchCriteria);


        //Führe für jeden Eintrag bestimmte OPs durch:
        for (Dialect singleEntry : language1Entries) {

            // finde für den aktuellen Eintrag die zugehörige refenenz ID:
            German germanEntry = germanDAL.findGermanById(singleEntry.getRefToGermanId());
            if (germanEntry != null) {


                System.out.println("german Entry---->>>>" + germanEntry);
                List<String> allPossibleBackTranslations = germanEntry.getAllRetourUebersetzungIdsPerDialect(languageB);
                if (allPossibleBackTranslations == null) {
                    continue;
                }

                // es gibt mehrere mögliche  übersetzungen!
                // --> mit for loop über alle allPossibleBackTranslations iterieren und für jedes element komplette liste mitschicken!??
                for (String oneOfManyRetourUebersetzungenIds : allPossibleBackTranslations) {
                    Dialect oneDialectEntry = dialectDAL.findDialectEntryById(languageB, oneOfManyRetourUebersetzungenIds);

                    if (oneDialectEntry == null) {
                        System.err.println("Controller: Ein DialectEintrag war 'null', deshalb wurde der Loop abgebrochen und für einen anderen Eintrag weitergemacht!!!!!!");
                        break;
                    }


                    aSearchResult = new SearchResult();


                    // aSearchResult.setSynonyms(extractSynonyms(oneDialectEntry, languageB)); // TODO: double check if rigth collection!

                    aSearchResult.setDialectA(singleEntry);
                    aSearchResult.setGerman(germanEntry);
                    aSearchResult.setDialectB(oneDialectEntry);

                    searchResults.add(aSearchResult);
                }

            }

        }
        return searchResults;


    }

    private List<Dialect> extractSynonyms(Dialect oneDialectEntry, String collection) {
        ArrayList<Dialect> extractedSynonyms;
        ArrayList<String> synonymIds = (ArrayList<String>) oneDialectEntry.getSynonymIdList();
        if (synonymIds.size() > 0) {
            extractedSynonyms = new ArrayList<>();

            for (String synonymId : oneDialectEntry.getSynonymIdList()) {

                Dialect dialectEntry = dialectDAL.findDialectEntryById(collection, synonymId);
                extractedSynonyms.add(dialectEntry);
            }
            return extractedSynonyms;

        } else {
            return null;
        }
    }

    @PostMapping("addReferencesCompleteSet")
    public void addReferencesCompleteSet(
            @RequestParam String dialectLanguageA,
            @RequestParam String dialectIdA,

            @RequestParam String dialectLanguageB,
            @RequestParam String dialectIdB,
            @RequestParam String germanId) {

        addBiDirectionalReferences(dialectLanguageA, dialectIdA, germanId);
        addBiDirectionalReferences(dialectLanguageB, dialectIdB, germanId);

        System.out.println("puh, finished!");
        System.out.println("check references!: " + dialectLanguageA + ": dialektA Id--> " + dialectIdA);
        System.out.println(dialectLanguageB + " dialectB Id--> " + dialectIdB);
        System.out.println("germanID:" + germanId);
    }

    @DeleteMapping("removeReferencesCompleteSet")
    public void removeSynonymsCompleteSet(
            @RequestParam String dialectLanguageA,
            @RequestParam String dialectIdA,

            @RequestParam String dialectLanguageB,
            @RequestParam String dialectIdB,
            @RequestParam String germanId) {

        deleteBiDirectionalReferences(dialectLanguageA, dialectIdA, germanId);
        deleteBiDirectionalReferences(dialectLanguageB, dialectIdB, germanId);
    }
}




