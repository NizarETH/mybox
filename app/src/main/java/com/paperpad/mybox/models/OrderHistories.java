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
"OrderHistory"
})
public class OrderHistories {

@JsonProperty("OrderHistory")
private List<OrderHistory> orderHistory = new ArrayList<OrderHistory>();
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("OrderHistory")
public List<OrderHistory> getOrderHistory() {
return orderHistory;
}

@JsonProperty("OrderHistory")
public void setOrderHistory(List<OrderHistory> orders) {
this.orderHistory = orders;
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