package com.paperpad.mybox.models;



public class AddressAccount {


	
	private String mail;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	
	private String prenom;

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	
	private String nom;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	
	private String adresse;

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	
	private String complement_adresse;

	public String getComplement_adresse() {
		return complement_adresse;
	}

	public void setComplement_adresse(String complement_adresse) {
		this.complement_adresse = complement_adresse;
	}

	private int code_postale;

	public int getCode_postale() {
		return code_postale;
	}

	public void setCode_postale(int code_postale) {
		this.code_postale = code_postale;
	}

	
	private String ville;

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	private int pays;

	public int getPays() {
		return pays;
	}

	public void setPays(Integer pays) {
		this.pays = pays;
	}

	
	private String tel_fixe;

	public String getTel_fixe() {
		return tel_fixe;
	}

	public void setTel_fixe(String tel_fixe) {
		this.tel_fixe = tel_fixe;
	}

	
	private String tel_mobile;

	public String getTel_mobile() {
		return tel_mobile;
	}

	public void setTel_mobile(String tel_mobile) {
		this.tel_mobile = tel_mobile;
	}

	
	private String civilite;

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	
	public BillingAddress getBillingAddress() {
		return new BillingAddress(mail, prenom, nom, adresse, complement_adresse, code_postale, ville, pays, tel_fixe, tel_mobile, civilite);
	}
	
	public ShippingAddress getShippingAddress() {
		return new ShippingAddress(mail, prenom, nom, adresse, complement_adresse, code_postale, ville, pays, tel_fixe, tel_mobile, civilite);
	}

	public AddressAccount(String mail, String prenom, String nom,
			String adresse, String complement_adresse, int code_postale,
			String ville, int pays, String tel_fixe, String tel_mobile,
			String civilite) {
		super();
		this.mail = mail;
		this.prenom = prenom;
		this.nom = nom;
		this.adresse = adresse;
		this.complement_adresse = complement_adresse;
		this.code_postale = code_postale;
		this.ville = ville;
		this.pays = pays;
		this.tel_fixe = tel_fixe;
		this.tel_mobile = tel_mobile;
		this.civilite = civilite;
	}

	public AddressAccount() {
		// TODO Auto-generated constructor stub
	}


}
