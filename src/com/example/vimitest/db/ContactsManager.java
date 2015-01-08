package com.example.vimitest.db;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vimitest.entity.Category;
import com.example.vimitest.entity.Contact;

/**
 * Êý¾Ý²Ù×÷²ã
 * 
 * @author crazywen
 * 
 */
public class ContactsManager extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ContactManager";

	private static final String TABLE_CATEGORIES = "categories";
	private static final String TABLE_CONTACTS = "contacts";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_URL = "url";
	private static final String KEY_CATEGORY_ID = "category_id";

	public ContactsManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_NAME + " TEXT UNIQUE" + ")";
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME
				+ " TEXT," + KEY_CATEGORY_ID + " INTEGER," + KEY_URL + " TEXT,"
				+ "FOREIGN KEY(" + KEY_CATEGORY_ID + ") REFERENCES "
				+ TABLE_CATEGORIES + "(" + KEY_ID + ")" + ")";
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		// Create tables again
		onCreate(db);
	}

	public void addContact(Contact contact) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_URL, contact.getUrl());
		values.put(KEY_CATEGORY_ID, contact.getCategory().getId());
		db.insert(TABLE_CONTACTS, null, values);
		db.close();
	}

	public void delContact(Contact contact) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + "= ? ",
				new String[] { String.valueOf(contact.getId()) });
		db.close();
	}

	public Contact getContact(int id) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_URL, KEY_CATEGORY_ID }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		if (null != cursor)
			cursor.moveToFirst();

		Category category = getCategory(cursor.getInt(3));

		Contact contact = new Contact(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), category);

		return contact;

	}

	public List<Contact> getAllContacts() {
		List<Contact> contacts = new LinkedList<Contact>();
		SQLiteDatabase db = getReadableDatabase();

		String sql = "select * from " + TABLE_CONTACTS;

		Cursor cursor = db.rawQuery(sql, null);

		Contact contact = null;

		if (cursor.moveToFirst()) {
			do {
				contact = new Contact(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), getCategory(cursor.getInt(3)));
				contacts.add(contact);
			} while (cursor.moveToNext());
		}
		return contacts;
	}

	public int updateContact(Contact contact) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_ID, contact.getId());
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_URL, contact.getUrl());
		values.put(KEY_CATEGORY_ID, contact.getCategory().getId());

		return db.update(TABLE_CONTACTS, values, KEY_ID + "=?",
				new String[] { String.valueOf(contact.getId()) });

	}

	public void addCateGory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, category.getName());

		db.insert(TABLE_CATEGORIES, null, values);

		db.close();
	}

	public void delCateGory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_CATEGORIES, KEY_ID,
				new String[] { String.valueOf(category.getId()) });
		db.close();
	}

	public void updateCateGory(Category category) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, category.getId());
		values.put(KEY_NAME, category.getName());

		db.update(TABLE_CATEGORIES, values, KEY_ID + "=?",
				new String[] { String.valueOf(category.getId()) });
		db.close();
	}

	public Category getCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { KEY_ID,
				KEY_NAME }, KEY_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {

				Category category = new Category(Integer.parseInt(cursor
						.getString(0)), cursor.getString(1));

				// return contact
				return category;
			}
		}
		return null;
	}

	public Category getCategory(String name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { KEY_ID,
				KEY_NAME }, KEY_NAME + "=?", new String[] { name }, null, null,
				null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				Category category = new Category(Integer.parseInt(cursor
						.getString(0)), cursor.getString(1));
				return category;
			}
		}
		// return contact
		return null;
	}

	public List<Category> getCategories() {
		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "select * from " + TABLE_CATEGORIES;
		List<Category> categories = new LinkedList<Category>();
		Cursor cursor = db.rawQuery(sql, null);
		Category category = null;
		if (cursor.moveToFirst()) {
			do {
				category = new Category(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1));
				categories.add(category);
			} while (cursor.moveToNext());
		}
		// return contact
		return categories;
	}

	// Getting all contacts of a category
	public List<Contact> getContactsByCategoryId(int categoryId) {
		List<Contact> contacts = new LinkedList<Contact>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_URL, KEY_CATEGORY_ID }, KEY_CATEGORY_ID + "=?",
				new String[] { String.valueOf(categoryId) }, null, null, null,
				null);

		// iterate over all retrieved rows
		Contact contact = null;
		if (cursor.moveToFirst()) {
			do {
				contact = new Contact();
				contact.setId(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setUrl(cursor.getString(2));

				Category category = getCategory(Integer.parseInt(cursor
						.getString(3)));
				contact.setCategory(category);

				// Add contact to contacts
				contacts.add(contact);
			} while (cursor.moveToNext());
		}

		// return contacts
		return contacts;
	}

}
