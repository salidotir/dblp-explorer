/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author salidotir
 */

import java.util.List;

public class Document {
    String documentId;
    String title;
    Authors authors;
    int nCitation;
    List<String> refereneces;

    @Override
    public String toString() {
        return "Document{" + '\n' +
                "  documentId='" + documentId + "'" + '\n' +
                ", title='" + title + "'" + '\n' +
                ", authors='" + authors.toString() + "'" + '\n' +
                ", n_citation='" + nCitation + "'" + '\n' +
                ", refrences='" + refereneces + "'" +
                '}' + "\n~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.\n";
    }

    public String getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String t) {
        this.title = t;
    }
    
    public Authors getAuthors(){
        return this.authors;
    }
    
    public void setAuthors(Authors a){
        this.authors = a;
    }
    
    public int getNCitation() {
        return this.nCitation;
    }
    
    public void setNCitation(int n) {
        this.nCitation = n;
    }

    public List<String> getReferences() {
        return this.refereneces;
    }

    public void setReferences(List<String> ref) {
        this.refereneces = ref;
    }
}