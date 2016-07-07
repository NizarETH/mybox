package com.paperpad.mybox.models;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
"id_product",
"prenom_beneficiaire",
"nom_beneficiaire",
"date_validite",
"date_consommation"
})
public class Product {

@JsonProperty("id_product")
private int id_product;
@JsonProperty("prenom_beneficiaire")
private String prenom_beneficiaire;
@JsonProperty("nom_beneficiaire")
private String nom_beneficiaire;
@JsonProperty("date_validite")
private String date_validite;
@JsonProperty("date_consommation")
private String date_consommation;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("id_product")
public int getId_product() {
return id_product;
}

@JsonProperty("id_product")
public void setId_product(int id_product) {
this.id_product = id_product;
}

public Product withId_product(int id_product) {
this.id_product = id_product;
return this;
}

@JsonProperty("prenom_beneficiaire")
public String getPrenom_beneficiaire() {
return prenom_beneficiaire;
}

@JsonProperty("prenom_beneficiaire")
public void setPrenom_beneficiaire(String prenom_beneficiaire) {
this.prenom_beneficiaire = prenom_beneficiaire;
}

public Product withPrenom_beneficiaire(String prenom_beneficiaire) {
this.prenom_beneficiaire = prenom_beneficiaire;
return this;
}

@JsonProperty("nom_beneficiaire")
public String getNom_beneficiaire() {
return nom_beneficiaire;
}

@JsonProperty("nom_beneficiaire")
public void setNom_beneficiaire(String nom_beneficiaire) {
this.nom_beneficiaire = nom_beneficiaire;
}

public Product withNom_beneficiaire(String nom_beneficiaire) {
this.nom_beneficiaire = nom_beneficiaire;
return this;
}

@JsonProperty("date_validite")
public String getDate_validite() {
return date_validite;
}

@JsonProperty("date_validite")
public void setDate_validite(String date_validite) {
this.date_validite = date_validite;
}

public Product withDate_validite(String date_validite) {
this.date_validite = date_validite;
return this;
}

@JsonProperty("date_consommation")
public String getDate_consommation() {
return date_consommation;
}

@JsonProperty("date_consommation")
public void setDate_consommation(String date_consommation) {
this.date_consommation = date_consommation;
}

public Product withDate_consommation(String date_consommation) {
this.date_consommation = date_consommation;
return this;
}

@Override
public String toString() {
return "";
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperties(String name, Object value) {
this.additionalProperties.put(name, value);
}

}

