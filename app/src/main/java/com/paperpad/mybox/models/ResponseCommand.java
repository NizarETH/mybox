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
@JsonPropertyOrder({ "total_commande", "paiemant_http" })
public class ResponseCommand {

	@JsonProperty("total_commande")
	private Integer total_commande;
	
	@JsonProperty("paiemant_http")
	private String paiemant_http;
	
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("total_commande")
	public Integer getTotal_commande() {
		return total_commande;
	}

	@JsonProperty("total_commande")
	public void setTotal_commande(Integer total_commande) {
		this.total_commande = total_commande;
	}

	@JsonProperty("paiemant_http")
	public String getPaiemant_http() {
		return paiemant_http;
	}

	@JsonProperty("paiemant_http")
	public void setPaiemant_http(String paiemant_http) {
		this.paiemant_http = paiemant_http;
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
