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
public class Author {
    private String name;
    private String id;
    private String org = null;
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String n){
        this.name = n;
    }
    
    public String getId(){
        return this.id;
    }
    
    public void setID(String i){
        this.id = i;
    }   
    
    public String getOrg(){
        if(this.org == null) {
            return "";
        }
        return this.org;
    }
    
    public void setOrg(String o){
        this.org = o;
    }   
}
