/**
 * 
 */
package com.paperpad.mybox.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.paperpad.mybox.models.Beneficiary;
import com.paperpad.mybox.models.BillingAddress;
import com.paperpad.mybox.models.CartItem;
import com.paperpad.mybox.models.CategoriesGetter;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Console;
import com.paperpad.mybox.models.IdCoffret;
import com.paperpad.mybox.models.Illustration;
import com.paperpad.mybox.models.LanguageString;
import com.paperpad.mybox.models.MyBox;
import com.paperpad.mybox.models.ShippingAddress;
import com.paperpad.mybox.models.StringImagesBox;
import com.paperpad.mybox.models.StringValidityBox;
import com.paperpad.mybox.models.UserAccount;
import com.paperpad.mybox.models.UserCreateAccount;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class DatabaseController extends OrmLiteSqliteOpenHelper {

	private Context context;
	private Dao<MyBox, Integer> myBoxDao;
	private Dao<CategoriesGetter, Integer> myBoxCategoriesDao;
	private Dao<StringImagesBox, Integer> stringImagesBox;
	private Dao<StringValidityBox, Integer> stringValidityBox;
	private Dao<Illustration, Integer> illustrationDao;
	private Dao<IdCoffret, Integer> idCoffretDao;
	private Dao<LanguageString, Integer> myLanguageStringDao;
	private Dao<Console, Integer> consoleDao;
	private Dao<CartItem, Integer> cartItemDao;
	private Dao<Colors, Integer> colorsDao;
	private Dao<BillingAddress, Integer> billingAddressDao;
	private Dao<ShippingAddress, Integer> shippingAddressDao;
	private Dao<UserAccount, Integer> userDao;
	private Dao<Beneficiary, Integer> beneficiaryDao;
	private Dao<UserCreateAccount, Integer> userCreateAccountDao;

	public DatabaseController(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
		// TODO Auto-generated constructor stub
	}

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "MyBox.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 9;

	public DatabaseController(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	
	/**
	 * Check if the database exist
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase() {
	    SQLiteDatabase checkDB = null;
	    try {
	        checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null,
	                SQLiteDatabase.OPEN_READONLY);
	        checkDB.close();
	    } catch (SQLiteException e) {
	    	e.printStackTrace();
	        // database doesn't exist yet.
	    }
	    return checkDB != null ? true : false;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		dropAllTables(arg1);
		createAllTables(arg1);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		dropAllTables(arg1);
		createAllTables(arg1);
		
	}
	
	/**create All Tables when requested by an update or new install of the app
	 * @param connectionSource 
	 * @return true if all tables were successfully created, false if not.
	 */
	public Boolean createAllTables(ConnectionSource connectionSource) {
		try {

			TableUtils.createTable(connectionSource, MyBox.class);
			TableUtils.createTable(connectionSource, IdCoffret.class);
			TableUtils.createTable(connectionSource, Console.class);
			TableUtils.createTable(connectionSource, LanguageString.class);
			TableUtils.createTable(connectionSource, StringImagesBox.class);
			TableUtils.createTable(connectionSource, StringValidityBox.class);
			TableUtils.createTable(connectionSource, CategoriesGetter.class);
			TableUtils.createTable(connectionSource, Illustration.class);
			TableUtils.createTable(connectionSource, Colors.class);
			TableUtils.createTable(connectionSource, BillingAddress.class);
			TableUtils.createTable(connectionSource, ShippingAddress.class);
			TableUtils.createTable(connectionSource, Beneficiary.class);
			TableUtils.createTable(connectionSource, UserCreateAccount.class);
			
//			Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: ");

			return true;
		} catch (SQLException e) {
			// throw new RuntimeException(e);
			Crashlytics.logException(e);
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Boolean dropAllTables(ConnectionSource connectionSource) {
		try {
			TableUtils.dropTable(connectionSource, MyBox.class, true);
			TableUtils.dropTable(connectionSource, IdCoffret.class, true);
			TableUtils.dropTable(connectionSource, Console.class, true);
			TableUtils.dropTable(connectionSource, LanguageString.class, true);
			TableUtils.dropTable(connectionSource, StringImagesBox.class, true);
			TableUtils.dropTable(connectionSource, StringValidityBox.class, true);
			TableUtils.dropTable(connectionSource, CategoriesGetter.class, true);
			TableUtils.dropTable(connectionSource, Illustration.class, true);
			TableUtils.dropTable(connectionSource, Colors.class, true);
			TableUtils.dropTable(connectionSource, BillingAddress.class, true);
			TableUtils.dropTable(connectionSource, ShippingAddress.class, true);
			TableUtils.dropTable(connectionSource, Beneficiary.class, true);
			TableUtils.dropTable(connectionSource, UserCreateAccount.class, true);
			
			
			return true;
		} catch (SQLException e) {
//			throw new RuntimeException(e);
			return false;
		}
	}
	
	public boolean populateDatabase(){
		return false;	
		
	}
	
	public void saveToDBConsole(Console console){
		if (console.getActive_languages() != null) {
			for (Iterator<String> iterator = console.getActive_languages().iterator(); iterator.hasNext();) {
				String lang = (String) iterator.next();
				LanguageString langu = new LanguageString(console, lang);
				try {
					getLanguageStringDao().createOrUpdate(langu);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		try {
			getConsoleDao().createOrUpdate(console);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToDBMybox(List<MyBox> boxs) {
		//				MyBox box = mapper.readValue(unparsedData, MyBox.class);
		for (int j = 0; j < boxs.size(); j++) {
			MyBox box = boxs.get(j);
			try {
				getMyBoxDao().createOrUpdate(box);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (box.getLien_images().size() > 0) {
				box.fillIllustrations();
				for (int i = 0; i < box.getLien_images().size(); i++) {
					StringImagesBox imagesBox = new StringImagesBox(box
							.getLien_images().get(i));
					imagesBox.setMyBox(box);
					try {
						getStringImagesBoxDao().createOrUpdate(
								imagesBox);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (Iterator<Illustration> iterator = box.getIllustrations().iterator(); iterator
						.hasNext();) {
					Illustration illustration = (Illustration) iterator.next();
					illustration.setMyBox(box);
					try {
						getIllustrationDao().createOrUpdate(illustration);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (box.getValiditee().size() > 0) {
				for (int i = 0; i < box.getValiditee().size(); i++) {
					StringValidityBox validityBox = new StringValidityBox(
							box.getValiditee().get(i));
					validityBox.setMyBox(box);
					try {
						getStringValidityBoxDao().createOrUpdate(
								validityBox);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}
	
	public void saveToDBMyboxCategories(List<CategoriesGetter> categoriesMyBoxs) {
		CategoriesGetter allCategories = new CategoriesGetter("", 0, true);
		try {
			getMyBoxCategoriesDao().createOrUpdate(allCategories);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < categoriesMyBoxs.size(); j++) {
			CategoriesGetter categoriesMyBox = categoriesMyBoxs.get(j);
			try {
				getMyBoxCategoriesDao().createOrUpdate(categoriesMyBox);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (categoriesMyBox.getId_coffret().size() > 0) {
				for (int i = 0; i < categoriesMyBox.getId_coffret()
						.size(); i++) {
					IdCoffret id = new IdCoffret(categoriesMyBox.getId_coffret().get(i));
					id.setCategoriesGetter(categoriesMyBox);
					try {
						getIdCoffretDao().createOrUpdate(id);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link Illustration} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Illustration,Integer> getIllustrationDao() throws SQLException {
		if (illustrationDao == null) {
			illustrationDao = getDao(Illustration.class);
		}
		return illustrationDao;
	}
	
	
	public Dao<IdCoffret,Integer> getIdCoffretDao() throws SQLException {
		if (idCoffretDao == null) {
			idCoffretDao = getDao(IdCoffret.class);
		}
		return idCoffretDao;
	}
	

	public Dao<Beneficiary,Integer> getBeneficiaryDao() throws SQLException {
		if (beneficiaryDao == null) {
			beneficiaryDao = getDao(Beneficiary.class);
		}
		return beneficiaryDao;
	}
	
	
	public Dao<UserCreateAccount,Integer> getUserCreateAccountDao() throws SQLException {
		if (userCreateAccountDao == null) {
			userCreateAccountDao = getDao(UserCreateAccount.class);
		}
		return userCreateAccountDao;
	}
	public Dao<StringImagesBox,Integer> getStringImagesBoxDao() throws SQLException {
		if (stringImagesBox == null) {
			stringImagesBox = getDao(StringImagesBox.class);
		}
		return stringImagesBox;
	}
	
	
	public Dao<StringValidityBox,Integer> getStringValidityBoxDao() throws SQLException {
		if (stringValidityBox == null) {
			stringValidityBox = getDao(StringValidityBox.class);
		}
		return stringValidityBox;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link MyBox} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<MyBox,Integer> getMyBoxDao() throws SQLException {
		if (myBoxDao == null) {
			myBoxDao = getDao(MyBox.class);
		}
		return myBoxDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link CategoriesGetter} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<CategoriesGetter,Integer> getMyBoxCategoriesDao() throws SQLException {
		if (myBoxCategoriesDao == null) {
			myBoxCategoriesDao = getDao(CategoriesGetter.class);
		}
		return myBoxCategoriesDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link LanguageString} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<LanguageString,Integer> getLanguageStringDao() throws SQLException {
		if (myLanguageStringDao == null) {
			myLanguageStringDao = getDao(LanguageString.class);
		}
		return myLanguageStringDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link Console} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Console,Integer> getConsoleDao() throws SQLException {
		if (consoleDao == null) {
			consoleDao = getDao(Console.class);
		}
		return consoleDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link com.paperpad.mybox.models.CartItem} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<CartItem,Integer> getCartItemDao() throws SQLException {
		if (cartItemDao == null) {
			cartItemDao = getDao(CartItem.class);
		}
		return cartItemDao;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our {@link com.paperpad.mybox.models.CartItem} class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Colors,Integer> getColorsDao() throws SQLException {
		if (colorsDao == null) {
			colorsDao = getDao(Colors.class);
		}
		return colorsDao;
	}
	
	public Dao<BillingAddress,Integer> getBillingAddressDao() throws SQLException {
		if (billingAddressDao == null) {
			billingAddressDao = getDao(BillingAddress.class);
		}
		return billingAddressDao;
	}
	
	
	public Dao<ShippingAddress,Integer> getShippingAddressDao() throws SQLException {
		if (shippingAddressDao == null) {
			shippingAddressDao = getDao(ShippingAddress.class);
		}
		return shippingAddressDao;
	}
	
	public Dao<UserAccount,Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(UserAccount.class);
		}
		return userDao;
	}

	
	public CategoriesGetter getBoxCategory(MyBox box){
		List<CategoriesGetter> cats = null;
		try {
			cats = getMyBoxCategoriesDao().queryForEq("", box.getId_categorie());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cats != null && cats.size()>0) {
			return cats.get(0);
		}else {
			return null;
		}
		
		
	}
	
	public List<MyBox> getAllCategoryBoxs(int... id) {
		List<MyBox> result = new ArrayList<MyBox>();
		List<MyBox> boxs = new ArrayList<MyBox>();
		try {
            if(id.length > 1)
                if(id[0] == 0)
			        boxs = getMyBoxDao().queryBuilder().orderBy("duree_de_validitee", true).where().between("prix", id[1], id[2]).query();//.queryForAll();
                else
                    boxs = getMyBoxDao().queryBuilder().orderBy("duree_de_validitee", true).where().between("prix", id[1], id[2]).and().eq("id_categorie", id[0]).query();
            else
                boxs = getMyBoxDao().queryForAll();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (boxs != null && boxs.size()>0) {
			if (id[0] == 0) {
				return boxs;
			}
			for (int i = 0; i < boxs.size(); i++) {
				if (boxs.get(i).getId_categorie() == id[0]) {
					result.add(boxs.get(i));
				}
			}
		}
		return result;
	}
	
	public List<MyBox> getAllCategoryBoxsABCOrder(int... id) {
		List<MyBox> result = new ArrayList<MyBox>();
		List<MyBox> boxs = null;
		try {
            if(id.length > 1)
			    boxs = getMyBoxDao().queryBuilder().orderBy("titre_coffret", true).where().between("prix", id[1], id[2])/*.and().eq("id_categorie", id[0])*/.query();
            else
                boxs = getMyBoxDao().queryBuilder().orderBy("titre_coffret", true).where().eq("id_categorie", id[0]).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (boxs != null && boxs.size()>0) {
			if (id[0] == 0) {
				return boxs;
			}
			for (int i = 0; i < boxs.size(); i++) {
				if (boxs.get(i).getId_categorie() == id[0]) {
					result.add(boxs.get(i));
				}
			}
		}
		return result;
	}
	
	public List<CategoriesGetter> getAllCategoriesOrderById() {
		
		List<CategoriesGetter> cats = new ArrayList<CategoriesGetter>();
		try {
			cats = getMyBoxCategoriesDao().queryBuilder().orderBy("id_categorie", true).query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cats ;
		
	}
	
	public MyBox getBoxByIdServer(int id){
		try {
			List<MyBox> boxs = getMyBoxDao().queryForEq("id_coffret", id);
			if (boxs.size()>0) {
				return  boxs.get(0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	/**
	 * create table for cartItem's if it doesn't exist
	 */
	public void createIfNotExistsCart(){
		try {
			TableUtils.createTableIfNotExists(getConnectionSource(), CartItem.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete cart elements from precedent session
	 */
	public void emptyCartItems(){
		try {
			TableUtils.dropTable(getConnectionSource(), CartItem.class, true);
			TableUtils.createTable(getConnectionSource(), CartItem.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void addCartItem(CartItem item) {
		try {
			getCartItemDao().createOrUpdate(item);
		} catch (SQLException e) {
			Toast.makeText(context, "Problem adding to cart!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	public void addColors(Colors colors) {
		try {
			getColorsDao().createOrUpdate(colors);
		} catch (SQLException e) {
			Toast.makeText(context, "Problem adding colors!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	

	public CategoriesGetter getCategoryById(int id) {
		List<CategoriesGetter> cats = new ArrayList<CategoriesGetter>();
		try {
			cats = getMyBoxCategoriesDao().queryForEq("id_categorie", id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cats.size()>0 ? cats.get(0) : null;
	}
	
	public int getMax(int id_cat) {
		long max = -1;
//		MyBox foo = null;
		if (id_cat != 0) {
			try {
				max = getMyBoxDao().queryRawValue( "select max(prix) from mybox where id_categorie = ?", id_cat+"");
				// now perform a second query to get the max row
				//			foo = getMyBoxDao().queryBuilder().where().eq("prix", max).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}else {
			try {
				max = getMyBoxDao().queryRawValue( "select max(prix) from mybox");
				// now perform a second query to get the max row
				//			foo = getMyBoxDao().queryBuilder().where().eq("prix", max).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		int all = Integer.parseInt(max+"");
		int rounded = all+(10-(all%10));
		
		
		return rounded;
		
		
	}

	public int getMin(int id_cat) {
		long min = -1;
		if (id_cat != 0) {
			try {
				min = getMyBoxDao().queryRawValue( "select min(prix) from mybox where id_categorie = ?", id_cat+"");
				// now perform a second query to get the max row
				//			foo = getMyBoxDao().queryBuilder().where().eq("prix", min).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				min = getMyBoxDao().queryRawValue( "select min(prix) from mybox ");
				// now perform a second query to get the max row
				//			foo = getMyBoxDao().queryBuilder().where().eq("prix", min).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int all = Integer.parseInt(min+"");
		int rounded = all-(all%10);
		
		
		return rounded;
		
		
	}

	
	public void addBillingAddress(BillingAddress billingAddress) {
		try {
			TableUtils.clearTable(connectionSource, BillingAddress.class);
			getBillingAddressDao().createOrUpdate(billingAddress);
		} catch (SQLException e) {
			Toast.makeText(context, "Problem adding billing Address!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	public void addShippingAddress(ShippingAddress shippingAddress) {
		try {
			TableUtils.clearTable(connectionSource, ShippingAddress.class);
			getShippingAddressDao().createOrUpdate(shippingAddress);
		} catch (SQLException e) {
			Toast.makeText(context, "Problem adding shipping Address!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	public void saveUserToDB(UserAccount account){
		try {
			TableUtils.dropTable(connectionSource, UserAccount.class, true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TableUtils.createTable(connectionSource, UserAccount.class);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			
			getUserDao().createIfNotExists(account);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public List<MyBox> getBoxsInRange(int id_cat, int minPrice, int maxPrice) {
		List<MyBox> boxs = new ArrayList<MyBox>();
		if (id_cat != 0) {
			try {
				boxs = getMyBoxDao().queryBuilder().where().between("prix", minPrice, maxPrice).and().eq("id_categorie", id_cat).query();
//				boxs = getMyBoxDao().queryRaw( "select * from mybox where id_categorie = ?", id_cat+"")/*queryRawValue( "select * from mybox where id_categorie = ?", id_cat+"")*/;
				// now perform a second query to get the max row
//							MyBox foo = getMyBoxDao().queryBuilder().where().eq("prix", min).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				boxs = getMyBoxDao().queryBuilder().where().between("prix", minPrice, maxPrice).query();
				// now perform a second query to get the max row
				//			foo = getMyBoxDao().queryBuilder().where().eq("prix", min).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return boxs;
	}
	
	public void saveBenificiary(Beneficiary beneficiary) {
		
		try {
			TableUtils.dropTable(connectionSource, Beneficiary.class, true);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			TableUtils.createTable(connectionSource, Beneficiary.class);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			getBeneficiaryDao().createOrUpdate(beneficiary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveUserTemporary(UserCreateAccount useAccount) {

		try {
			TableUtils.dropTable(connectionSource, UserCreateAccount.class, true);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			TableUtils.createTable(connectionSource, UserCreateAccount.class);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			getUserCreateAccountDao().createOrUpdate(useAccount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
