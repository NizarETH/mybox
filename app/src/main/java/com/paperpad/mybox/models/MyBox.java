package com.paperpad.mybox.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@DatabaseTable(tableName = "mybox")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
	"id_coffret",
	"titre_coffret",
	"lien_web",
	"introduction",
	"description",
	"prix",
	"nombre_personnes",
	"id_categorie",
	"validitee",
	"lien_images",
	"duree_de_validitee"
})
public class MyBox {

	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField(canBeNull = false)
	@JsonProperty("id_coffret")
	private Integer id_coffret;
	
	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	@JsonProperty("titre_coffret")
	private String titre_coffret;
	
	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	@JsonProperty("lien_web")
	private String lien_web;
	
	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	@JsonProperty("introduction")
	private String introduction;
	
	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	@JsonProperty("description")
	private String description;
	
	@DatabaseField(canBeNull = false)
	@JsonProperty("prix")
	private Integer prix;
	
	@DatabaseField(canBeNull = false)
	@JsonProperty("nombre_personnes")
	private Integer nombre_personnes;
	
	@DatabaseField(canBeNull = false)
	@JsonProperty("id_categorie")
	private Integer id_categorie;
	
	@JsonProperty("validitee")
	private List<String> validitee = new ArrayList<String>();
	
	@ForeignCollectionField
	private Collection<StringValidityBox> validitee1 = new ArrayList<StringValidityBox>();
	
	@JsonProperty("lien_images")
	private List<String> lien_images = new ArrayList<String>();
	
	@ForeignCollectionField
	private Collection<StringImagesBox> lien_images1 = new ArrayList<StringImagesBox>();
	
	@ForeignCollectionField
	private Collection<Illustration> illustrations = new ArrayList<Illustration>();
	
	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	@JsonProperty("duree_de_validitee")
	private String duree_de_validitee;
	
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id_coffret")
	public Integer getId_coffret() {
		return id_coffret;
	}

	@JsonProperty("id_coffret")
	public void setId_coffret(Integer id_coffret) {
		this.id_coffret = id_coffret;
	}

	@JsonProperty("titre_coffret")
	public String getTitre_coffret() {
		return titre_coffret;
	}

	@JsonProperty("titre_coffret")
	public void setTitre_coffret(String titre_coffret) {
		this.titre_coffret = titre_coffret;
	}

	@JsonProperty("lien_web")
	public String getLien_web() {
		return lien_web;
	}

	@JsonProperty("lien_web")
	public void setLien_web(String lien_web) {
		this.lien_web = lien_web;
	}

	@JsonProperty("introduction")
	public String getIntroduction() {
		return introduction;
	}

	@JsonProperty("introduction")
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("prix")
	public Integer getPrix() {
		return prix;
	}

	@JsonProperty("prix")
	public void setPrix(Integer prix) {
		this.prix = prix;
	}

	@JsonProperty("nombre_personnes")
	public Integer getNombre_personnes() {
		return nombre_personnes;
	}

	@JsonProperty("nombre_personnes")
	public void setNombre_personnes(Integer nombre_personnes) {
		this.nombre_personnes = nombre_personnes;
	}

	@JsonProperty("id_categorie")
	public Integer getId_categorie() {
		return id_categorie;
	}

	@JsonProperty("id_categorie")
	public void setId_categorie(Integer id_categorie) {
		this.id_categorie = id_categorie;
	}

	@JsonProperty("validitee")
	public List<String> getValiditee() {
		return validitee;
	}

	@JsonProperty("validitee")
	public void setValiditee(List<String> validitee) {
		this.validitee = validitee;
	}

	@JsonProperty("lien_images")
	public List<String> getLien_images() {
		return lien_images;
	}

	@JsonProperty("lien_images")
	public void setLien_images(List<String> lien_images) {
		this.lien_images = lien_images;
	}

	@JsonProperty("duree_de_validitee")
	public String getDuree_de_validitee() {
		return duree_de_validitee;
	}

	@JsonProperty("duree_de_validitee")
	public void setDuree_de_validitee(String duree_de_validitee) {
		this.duree_de_validitee = duree_de_validitee;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the validitee1
	 */
	public Collection<StringValidityBox> getValiditee1() {
		return validitee1;
	}

	/**
	 * @param validitee1 the validitee1 to set
	 */
	public void setValiditee1(Collection<StringValidityBox> validitee1) {
		this.validitee1 = validitee1;
	}

	/**
	 * @return the lien_images1
	 */
	public Collection<StringImagesBox> getLien_images1() {
		return lien_images1;
	}

	/**
	 * @param lien_images1 the lien_images1 to set
	 */
	public void setLien_images1(Collection<StringImagesBox> lien_images1) {
		this.lien_images1 = lien_images1;
	}
	
	public void fillIllustrations() {
		for (int i = 0; i < lien_images.size(); i++) {
			if (!lien_images.get(i).equalsIgnoreCase("false")) {
			Illustration illustration = new Illustration(lien_images.get(i), "", false);
			illustrations.add(illustration);
		}
			
		}
	}

	/**
	 * @return the illustrations
	 */
	public Collection<Illustration> getIllustrations() {
		return illustrations;
	}

	/**
	 * @param illustrations the illustrations to set
	 */
	public void setIllustrations(Collection<Illustration> illustrations) {
		this.illustrations = illustrations;
	}

	public MyBox() {
		super();
		// TODO Auto-generated constructor stub
	}

}