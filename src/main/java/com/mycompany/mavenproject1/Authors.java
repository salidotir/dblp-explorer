/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author salidotir
 */
public class Authors {
    List<Author> authors = new ArrayList<Author>();
    
    @Override
    public String toString() {
        String str = "[";
        for(int i = 0; i < this.authors.size(); i++) {
            str += '{' +
                    "name='" + this.authors.get(i).getName() + "'" + 
                    ", id='" + this.authors.get(i).getId().toString()  + "'" +
                    ", org='" + this.authors.get(i).getOrg().toString()  + "'" +
                    '}';
            str += ",";
        }
        str += ']';
        return str;
    }
    
    public List<Author> getAuthors(){
        return this.authors;
    }
    
    public void setAuthors(List<Author> l){
        this.authors = l;
    }    
    
    public void addAuthor(Author a) {
        this.authors.add(a);
    }
}
