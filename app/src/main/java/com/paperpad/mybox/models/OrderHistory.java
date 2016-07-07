package com.paperpad.mybox.models;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
"id_commande",
"send_mode",
"status",
"products"
})

public class OrderHistory {

@JsonProperty("id_commande")
private int id_commande;
@JsonProperty("send_mode")
private String send_mode;
@JsonProperty("status")
private String status;
@JsonProperty("products")
private List<Product> products = new ArrayList<Product>();
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("id_commande")
public int getId_commande() {
return id_commande;
}

@JsonProperty("id_commande")
public void setId_commande(int id_commande) {
this.id_commande = id_commande;
}

public OrderHistory withId_commande(int id_commande) {
this.id_commande = id_commande;
return this;
}

@JsonProperty("send_mode")
public String getSend_mode() {
return send_mode;
}

@JsonProperty("send_mode")
public void setSend_mode(String send_mode) {
this.send_mode = send_mode;
}

public OrderHistory withSend_mode(String send_mode) {
this.send_mode = send_mode;
return this;
}

@JsonProperty("status")
public String getStatus() {
return status;
}

@JsonProperty("status")
public void setStatus(String status) {
this.status = status;
}

public OrderHistory withStatus(String status) {
this.status = status;
return this;
}

@JsonProperty("products")
public List<Product> getProducts() {
return products;
}

@JsonProperty("products")
public void setProducts(List<Product> products) {
this.products = products;
}

public OrderHistory withProducts(List<Product> products) {
this.products = products;
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

