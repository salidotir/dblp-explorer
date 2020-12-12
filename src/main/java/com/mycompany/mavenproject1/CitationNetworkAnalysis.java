/*
 * To read the inpucorrt json file correctly, add the below line to the end of json file.
 * { "EOF": "true" }

 * input.json : the json file as input to be read
 * keyword : to search in the titles of each document for this keyword
 * N : to check the information of papaers all tiers up to level N

 * At first reading the json file, search in the titles for the keyword.
 * At the next N-times reading the json file, search for the references of the papers.
 * Print the information of found papares at each level.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author salidotir
 */

import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.print.Collation;
import org.codehaus.jackson.JsonNode;

public class CitationNetworkAnalysis {

    public static final String DOC_ID = "id";
    public static final String DOC_TITLE = "title";
    
    public static final String DOC_AUTHORS = "authors";
    public static final String AUTHOR_ID = "id";
    public static final String AUTHOR_NAME = "name";
    public static final String AUTHOR_ORG = "org";
    
    public static final String DOC_N_CITATION = "n_citation";
    public static final String DOC_REFERENCES = "references";
    
    // used to trash the id-field in venue so that doesn not changes the doc-id
    public static final String DOC_VENUE = "venue";
    public static final String DOC_VENUE_ID = "id";
    public static final String DOC_VENUE_RAW = "raw";
    
    // used to check the end of each document
    public static final String DOC_FOS = "fos";
    public static final String DOC_FOS_NAME = "name";
    public static final String DOC_FOS_W = "w";

    // used to check end of json file
    // a new object {"EOF": "true"} is added at the end of json file
    public static final String EOF = "EOF";
    
    // to print citation ids with details in function """ process_find_tiers_k"""
    String str;

    public static void main(String[] args) {
        CitationNetworkAnalysis citationNetworkAnalysis = new CitationNetworkAnalysis();
        List<String> searched_documents = new ArrayList<String>();
        String jsonFilePath = "S:\\mavenproject1\\sample.json";
        String keyword = "for";
        int N = 2;
        
        searched_documents = citationNetworkAnalysis.process_find_keywork(jsonFilePath, keyword);
        
        List<String> temp_list_of_ids;
        // read json file N times to look for documents' ids that ids in searched_documents are in their references.
        System.out.println("______________________________");
        for (int i = 0; i < N; i++) {
            System.out.println("Tier-" + Integer.toString(i+1) + " : ");
            temp_list_of_ids = citationNetworkAnalysis.process_find_tier_k(jsonFilePath, searched_documents);
//            System.out.println(temp_list_of_ids);
            System.out.println("______________________________");
            Collections.copy(searched_documents, temp_list_of_ids);     // do a deep copy of ids
            temp_list_of_ids.clear();
        }
    }

    public List<String> process_find_keywork(String jsonFilePath, String keyword){
        
        boolean temp = false;   // flag for end of document
        boolean temp1 = false;  // flag for end of json file
        boolean temp2 = false;  // flag for adding document ids that their title contains keyword
        List<String> list_of_documents_with_keyword = new ArrayList<String>();
        
        File jsonFile = new File(jsonFilePath);
        JsonFactory jsonfactory = new JsonFactory(); //init factory
        try {
            JsonParser jsonParser = jsonfactory.createJsonParser(jsonFile); //create JSON parser
            int numberOfRecords = 0;
            Document document = new Document();
            JsonToken jsonToken = jsonParser.nextToken();
            while (true){ //Iterate all elements of array
                if(jsonToken != JsonToken.END_ARRAY && temp1 == true) {
                    // ended parsing json
                    break;
                }
                
                String fieldname = jsonParser.getCurrentName(); //get current name of token

//                System.out.println("fieldname: " + fieldname);
//                System.out.println("jsontoken: " + jsonToken.asString());
//                System.out.println("temp: " + temp);
//                System.out.println("numberOfRecords: " + numberOfRecords);
//                System.out.println("_____________________");

                if (DOC_ID.equals(fieldname)) {
                    jsonToken = jsonParser.nextToken(); //read next token
                    document.setDocumentId(jsonParser.getText());
                }

                else if (DOC_TITLE.equals(fieldname)) {
                    jsonToken = jsonParser.nextToken();
                    String title = jsonParser.getText();
                    document.setTitle(title);
                    if (title.toLowerCase().contains(keyword.toLowerCase())) {
                        temp2 = true;
                    }
                }

                else if (DOC_AUTHORS.equals(fieldname)) {
                    JsonToken jsonToken1 = jsonParser.nextToken();
                    Authors authors = new Authors();
                    Author author = new Author();

                    while(jsonToken1!= JsonToken.END_ARRAY) {
                        String t = jsonParser.getCurrentName(); //get current name of token

                        if (AUTHOR_NAME.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                            author.setName(jsonParser.getText());
                        }

                        if (AUTHOR_ID.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                            author.setID(jsonParser.getText());
                        }
                        
                        if (AUTHOR_ORG.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                            author.setOrg(jsonParser.getText());
                        }
                        
                        if(jsonToken1==JsonToken.END_OBJECT){
                            //do some processing, Indexing, saving in DB etc.
                            //add author to list of authors.
                            authors.addAuthor(author);
                            author = new Author();
                        }

                        jsonToken1 = jsonParser.nextToken();                        
                    }

                    //System.out.println("authors=" + authors);
                    //add authors to authors-field of documnet
                    document.setAuthors(authors);
                    jsonToken = jsonParser.nextToken();
                }

                else if (DOC_N_CITATION.equals(fieldname)) {
                    jsonToken = jsonParser.nextToken();
                    document.setNCitation(jsonParser.getIntValue());
                    //System.out.println(document.getNCitation());
                }

                else if (DOC_REFERENCES.equals(fieldname)) {
                    List<String> references = new ArrayList<String>();
                    JsonToken jsonToken1 = jsonParser.nextToken();

                    while(jsonToken1!= JsonToken.END_ARRAY) {
                        if (jsonToken1 == JsonToken.VALUE_STRING) {
                            references.add(jsonParser.getText());
                        }

                        jsonToken1 = jsonParser.nextToken();                      
                    }

//                    System.out.println("references=" + references);
                    //add references to document.references
                    document.setReferences(references);
                }
                
                // to trash the venue and its inside raw and id
                // so it does not change the document id after it is read
                else if(DOC_VENUE.equals(fieldname)) {
                    JsonToken jsonToken1 = jsonParser.nextToken();
                    
                    // we don't need venue information
                    while(jsonToken1!= JsonToken.END_OBJECT) {
                        String t = jsonParser.getCurrentName(); //get current name of token

                        if (DOC_VENUE_ID.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        if (DOC_VENUE_RAW.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        jsonToken1 = jsonParser.nextToken();                        
                    }

                    // all venue is read
                    jsonToken = jsonParser.nextToken();
                }

                // to check end of each docment
                // it is considered that fos is the last fiels of all json objects
                // in the next while loop inside if, we get all the fields of fos
                // and make the flag temp true
                else if(DOC_FOS.equals(fieldname)) {
                    temp = true;
                    
                    JsonToken jsonToken1 = jsonParser.nextToken();
                    
                    while(jsonToken1!= JsonToken.END_ARRAY) {
                        String t = jsonParser.getCurrentName(); //get current name of token

                        if (DOC_FOS_NAME.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        if (DOC_FOS_W.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        if(jsonToken1==JsonToken.END_OBJECT){
                            //do some processing, Indexing, saving in DB etc.
                            // we don't need fos
                        }

                        jsonToken1 = jsonParser.nextToken();                        
                    }

                    // all fos is read
                    jsonToken = jsonParser.nextToken();
                }
                
                else if(EOF.equals(fieldname)) {
                    temp1 = true;
                    jsonParser.nextToken();
                }

                if(jsonToken==JsonToken.END_OBJECT){
                    if(temp == true) {
                        temp = false;
                        System.out.println(document.toString());
                        
                        if (temp2 == true) {
                            temp2 = false;
                            list_of_documents_with_keyword.add(document.getDocumentId());
                        }
                        
                        //do some processing, Indexing, saving in DB etc..
                        document = new Document();
                        numberOfRecords++;
                        continue;
                    }
                }

            jsonToken = jsonParser.nextToken();
        }

            System.out.println("Total Records Found : "+numberOfRecords);
            System.out.println("Documents with keyword " + keyword + " : "+list_of_documents_with_keyword.toString());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list_of_documents_with_keyword;
    }
    
    public List<String> process_find_tier_k(String jsonFilePath, List<String> list_of_ids){
        
        boolean temp = false;   // flag for end of document
        boolean temp1 = false;  // flag for end of json file
        boolean temp2 = false;  // flag for adding document ids that their title contains keyword
        
        List<String> list_of_documents_cited_by_input_ids = new ArrayList<String>();
        
        File jsonFile = new File(jsonFilePath);
        JsonFactory jsonfactory = new JsonFactory(); //init factory
        try {
            JsonParser jsonParser = jsonfactory.createJsonParser(jsonFile); //create JSON parser
            int numberOfRecords = 0;
            Document document = new Document();
            JsonToken jsonToken = jsonParser.nextToken();
            while (true){ //Iterate all elements of array
                if(jsonToken != JsonToken.END_ARRAY && temp1 == true) {
                    // ended parsing json
                    break;
                }
                
                String fieldname = jsonParser.getCurrentName(); //get current name of token

                if (DOC_ID.equals(fieldname)) {
                    jsonToken = jsonParser.nextToken(); //read next token
                    document.setDocumentId(jsonParser.getText());
                }

                else if (DOC_TITLE.equals(fieldname)) {
                    jsonToken = jsonParser.nextToken();
                    String title = jsonParser.getText();
                    document.setTitle(title);
                }

                else if (DOC_AUTHORS.equals(fieldname)) {
                    JsonToken jsonToken1 = jsonParser.nextToken();
                    Authors authors = new Authors();
                    Author author = new Author();

                    while(jsonToken1!= JsonToken.END_ARRAY) {
                        String t = jsonParser.getCurrentName(); //get current name of token

                        if (AUTHOR_NAME.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                            author.setName(jsonParser.getText());
                        }

                        if (AUTHOR_ID.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                            author.setID(jsonParser.getText());
                        }
                        
                        if (AUTHOR_ORG.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                            author.setOrg(jsonParser.getText());
                        }
                        
                        if(jsonToken1==JsonToken.END_OBJECT){
                            //do some processing, Indexing, saving in DB etc.
                            //add author to list of authors.
                            authors.addAuthor(author);
                            author = new Author();
                        }

                        jsonToken1 = jsonParser.nextToken();                        
                    }

                    //System.out.println("authors=" + authors);
                    //add authors to authors-field of documnet
                    document.setAuthors(authors);
                    jsonToken = jsonParser.nextToken();
                }

                else if (DOC_N_CITATION.equals(fieldname)) {
                    jsonToken = jsonParser.nextToken();
                    document.setNCitation(jsonParser.getIntValue());
                    //System.out.println(document.getNCitation());
                }

                else if (DOC_REFERENCES.equals(fieldname)) {
                    List<String> references = new ArrayList<String>();
                    JsonToken jsonToken1 = jsonParser.nextToken();

                    while(jsonToken1!= JsonToken.END_ARRAY) {
                        if (jsonToken1 == JsonToken.VALUE_STRING) {
                            references.add(jsonParser.getText());
                        }

                        jsonToken1 = jsonParser.nextToken();                      
                    }

//                    System.out.println("references=" + references);
                    //add references to document.references
                    document.setReferences(references);
                    
                    // check if any of the ids in input argument "list_of_ids" is in the references list
                    str = "[";
                    for (int i = 0; i < list_of_ids.size(); i++) {
                        if (references.contains(list_of_ids.get(i))) {
                            temp2 = true;
                            str += list_of_ids.get(i) + ",";
                        }
                    }
                    str = str.substring(0, str.length()-1);
                    str += "]\n";
//                    System.out.println(str);
//                    list_of_documents_cited_by_input_ids.add(str);
                }
                
                // to trash the venue and its inside raw and id
                // so it does not change the document id after it is read
                else if(DOC_VENUE.equals(fieldname)) {
                    JsonToken jsonToken1 = jsonParser.nextToken();
                    
                    // we don't need venue information
                    while(jsonToken1!= JsonToken.END_OBJECT) {
                        String t = jsonParser.getCurrentName(); //get current name of token

                        if (DOC_VENUE_ID.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        if (DOC_VENUE_RAW.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        jsonToken1 = jsonParser.nextToken();                        
                    }

                    // all venue is read
                    jsonToken = jsonParser.nextToken();
                }

                // to check end of each docment
                // it is considered that fos is the last fiels of all json objects
                // in the next while loop inside if, we get all the fields of fos
                // and make the flag temp true
                else if(DOC_FOS.equals(fieldname)) {
                    temp = true;
                    
                    JsonToken jsonToken1 = jsonParser.nextToken();
                    
                    while(jsonToken1!= JsonToken.END_ARRAY) {
                        String t = jsonParser.getCurrentName(); //get current name of token

                        if (DOC_FOS_NAME.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        if (DOC_FOS_W.equals(t)) {
                            jsonToken1 = jsonParser.nextToken(); //read next token
                        }

                        if(jsonToken1==JsonToken.END_OBJECT){
                            //do some processing, Indexing, saving in DB etc.
                            // we don't need fos
                        }

                        jsonToken1 = jsonParser.nextToken();                        
                    }

                    // all fos is read
                    jsonToken = jsonParser.nextToken();
                }
                
                else if(EOF.equals(fieldname)) {
                    temp1 = true;
                    jsonParser.nextToken();
                }

                if(jsonToken==JsonToken.END_OBJECT){
                    if(temp == true) {
                        temp = false;
                        
                        if (temp2 == true) {
                            temp2 = false;
                            System.out.println(document.getDocumentId() + " : " + str);
                            list_of_documents_cited_by_input_ids.add(document.getDocumentId());
                        }
                        
                        //do some processing, Indexing, saving in DB etc..
                        document = new Document();
                        numberOfRecords++;
                        continue;
                    }
                }

            jsonToken = jsonParser.nextToken();
        }

//            System.out.println("Total Records Found : "+numberOfRecords);
//            System.out.println("Documents cited by any of " + list_of_ids.toString() + " : "+list_of_documents_cited_by_input_ids.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list_of_documents_cited_by_input_ids;
    }
}
