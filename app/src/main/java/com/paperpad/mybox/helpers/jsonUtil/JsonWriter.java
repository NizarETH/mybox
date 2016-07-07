/**
 * 
 */
package com.paperpad.mybox.helpers.jsonUtil;

import android.util.Log;

import com.paperpad.mybox.models.AddressAccount;
import com.paperpad.mybox.models.CartItem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author euphordev02
 *
 */
public class JsonWriter {

	/**
	 * 
	 */
	public JsonWriter() {
		// TODO Auto-generated constructor stub
	}
	
	public static String writeAddressAccount(AddressAccount account) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("societe", "societe");
		map.put("prenom", account.getPrenom());
		map.put("adresse", account.getAdresse());
		map.put("ville", account.getVille());
		map.put("civilite", 3/*account.getCivilite()*/);
		map.put("nom", account.getNom());
		map.put("pays", "1020"/*account.getPays()*/);
		map.put("tel_fix", account.getTel_fixe());
		map.put("tel_gsm", account.getTel_mobile());
		map.put("service", "service");
		map.put("complement_adresse", "test"/*account.getComplement_adresse()*/);
		map.put("indicatif_tel_fix", "33");
		map.put("code_postal", ""+account.getCode_postale());
		map.put("indicatif_tel_gsm", "33");

		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}
	
	public static String writeProducts(List<CartItem> items){
		JSONArray jsonArray = new JSONArray();

		for (CartItem item : items) {
			jsonArray.add(writeProduct(item));
		}
		return jsonArray.toString();
		
	}

	private static JSONObject writeProduct(CartItem item) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message_beneficiaire", !item.getMessage().isEmpty()?item.getMessage():"    ");
		map.put("email_beneficiaire", item.getEmail()!=null?item.getEmail():"    ");
		try {
			map.put("nid", item.getMybox().getId_coffret());
		} catch (Exception e) {
			Log.e("JsonWriter", e.getMessage());
			e.printStackTrace();
		}
		map.put("nom_beneficiaire", item.getLast_name());
		map.put("prenom_beneficiaire", item.getFirst_name());
		map.put("qte", item.getQuantity());
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

}
