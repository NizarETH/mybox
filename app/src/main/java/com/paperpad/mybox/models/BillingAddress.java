package com.paperpad.mybox.models;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BillingAddress")
public class BillingAddress {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String mail;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String prenom;

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String nom;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String adresse;

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String complement_adresse;

	public String getComplement_adresse() {
		return complement_adresse;
	}

	public void setComplement_adresse(String complement_adresse) {
		this.complement_adresse = complement_adresse;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.INTEGER)
	private int code_postale;

	public int getCode_postale() {
		return code_postale;
	}

	public void setCode_postale(int code_postale) {
		this.code_postale = code_postale;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String ville;

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.INTEGER)
	private int pays;

	public int getPays() {
		return pays;
	}

	public void setPays(Integer pays) {
		this.pays = pays;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String tel_fixe;

	public String getTel_fixe() {
		return tel_fixe;
	}

	public void setTel_fixe(String tel_fixe) {
		this.tel_fixe = tel_fixe;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String tel_mobile;

	public String getTel_mobile() {
		return tel_mobile;
	}

	public void setTel_mobile(String tel_mobile) {
		this.tel_mobile = tel_mobile;
	}

	@DatabaseField(canBeNull = true, dataType = DataType.STRING)
	private String civilite;

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public BillingAddress(String mail, String prenom, String nom,
			String adresse, String complement_adresse, int code_postale, String ville, int pays,
			String tel_fixe, String tel_mobile, String civilite) {
		super();
		this.mail = mail;
		this.prenom = prenom;
		this.nom = nom;
		this.adresse = adresse;
		this.code_postale = code_postale;
		this.ville = ville;
		this.pays = pays;
		this.tel_fixe = tel_fixe;
		this.tel_mobile = tel_mobile;
		this.civilite = civilite;
		this.complement_adresse = complement_adresse;
	}

	public BillingAddress() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AddressAccount getAddressAccount() {
		return new AddressAccount(mail, prenom, nom, adresse, complement_adresse, code_postale, ville, pays, tel_fixe, tel_mobile, civilite);
	}


}
