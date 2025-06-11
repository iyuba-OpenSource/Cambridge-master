package com.iyuba.camstory.sqlite;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.iyuba.camstory.frame.CrashApplication;
/**
 * 数据库服务
 * @author chentong
 * 
 */
public class DatabaseService {

	protected static SQLiteDatabase database;

	public static final  DBOpenHelper mdbhelper= new DBOpenHelper(CrashApplication.getInstance());

	protected DatabaseService() {
		synchronized (this) {
			if (database == null || !database.isOpen()) {
				database = mdbhelper.getReadableDatabase();
			}
		}
	}

	/**
	 * 删除表
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName) {
		database.execSQL(
				"DROP TABLE IF EXISTS " + tableName);

	}

	/**
	 * 关闭数据库
	 * 这个方法调用了个寂寞，我暂时先把他删除
	 */
	public void closeDatabase() {
		database.close();
	}

	/**
	 * 删除数据库表数据
	 * 
	 * @param tableName
	 * @param id
	 */
	public void deleteItemData(String tableName, Integer id) {
		database.execSQL(
				"delete from " + tableName + " where _id=?",
				new Object[] { id });
		database.close();
	}

	/**
	 * 删除数据库表数据
	 * 
	 * @param tableName
	 * @param ids
	 *            ids格式为"","","",""
	 */
	public void deleteItemsData(String tableName, String ids) {
		database.execSQL(
				"delete from " + tableName + " where voaid in (" + ids + ")",
				new Object[] {});
		database.close();
	}

	/**
	 * 获取数据库表项数
	 * 
	 * @param tableName
	 * @return
	 */
	public long getDataCount(String tableName) {
		Cursor cursor = database.rawQuery(
				"select count(*) from " + tableName, null);
		cursor.moveToFirst();
		database.close();
		return cursor.getLong(0);
	}

}
