package org.nhnnext.android.basicsqlite;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity implements OnClickListener {

	private SQLiteDatabase database1;
	
	private Button mainButtonTableCreate;
	private Button mainButtonTableDrop;
	
	private EditText mainEditTextInsert;
	private Button mainButtonInsert;
	
	private EditText mainEditTextColumnNumber;
	private EditText mainEditTextUpdate;
	private Button mainButtonUpdate;
	private Button mainButtonDelete;
	
	private ListView mainListView1;
	
	
	private static final String TABLE_NAME = "articles";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sqLiteInitialize();
		
		mainButtonTableCreate = (Button)findViewById(R.id.mainButtonTableCreate);
		mainButtonTableDrop = (Button)findViewById(R.id.mainButtonTableDrop);
		
		mainEditTextInsert = (EditText)findViewById(R.id.mainEditTextInsert);
		mainButtonInsert = (Button)findViewById(R.id.mainButtonInsert);
		
		mainEditTextColumnNumber = (EditText)findViewById(R.id.mainEditTextColumnNumber);
		mainEditTextUpdate = (EditText)findViewById(R.id.mainEditTextUpdate);
		mainButtonUpdate = (Button)findViewById(R.id.mainButtonUpdate);
		mainButtonDelete = (Button)findViewById(R.id.mainButtonDelete);
		
		mainButtonTableCreate.setOnClickListener(this);
		mainButtonTableDrop.setOnClickListener(this);
		mainButtonInsert.setOnClickListener(this);
		mainButtonUpdate.setOnClickListener(this);
		mainButtonDelete.setOnClickListener(this);
		
		mainListView1 = (ListView)findViewById(R.id.mainListView1);
	}


	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.mainButtonTableCreate:
			if(isTableExist()) {
				toaster("테이블이 이미 만들어져 있습니다.");
				return;
			}
			tableCreate();
			listRefresh();
			break;
		case R.id.mainButtonTableDrop:
			if(!isTableExist()) {
				toaster("테이블이 없습니다. 테이블을 만들어주세요.");
				return;
			}
			tableDrop();
			listRefresh();
			break;
		case R.id.mainButtonInsert:
			if(!isTableExist()) {
				toaster("테이블이 없습니다. 테이블을 만들어주세요.");
				return;
			}
			columnInsert();
			listRefresh();
			break;
		case R.id.mainButtonUpdate:
			if(!isTableExist()) {
				toaster("테이블이 없습니다. 테이블을 만들어주세요.");
				return;
			}
			columnUpdate();
			listRefresh();
			break;
		case R.id.mainButtonDelete:
			if(!isTableExist()) {
				toaster("테이블이 없습니다. 테이블을 만들어주세요.");
				return;
			}
			columnDelete();
			listRefresh();
			break;
		}
	}



	private void sqLiteInitialize() {
		database1 = openOrCreateDatabase("sqliteTest.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

		database1.setLocale(Locale.getDefault());
		database1.setVersion(1);
	}
	
	
	private void tableCreate() {
		toaster("테이블을 생성합니다.");
		String sql = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, Title text not null);";
		database1.execSQL(sql);
	}
	
	
	private void tableDrop() {
		toaster("테이블을 삭제합니다.");
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
		database1.execSQL(sql);
	}
	
	
	private void columnInsert() {
		String insertData = mainEditTextInsert.getText().toString();
		
		if("".equals(insertData)) {
			toaster("추가할 데이터 내용이 없습니다.");
			return;
		}
		
 		toaster("데이터를 추가합니다.[" + insertData + "]");
		
		ContentValues values = new ContentValues();
		values.put("Title", insertData);
		database1.insert(TABLE_NAME, null, values);
		
		mainEditTextInsert.setText("");
		
	}
	
	
	private void columnUpdate() {
		String columnNumber = mainEditTextColumnNumber.getText().toString();
		String updateData = mainEditTextUpdate.getText().toString();
		
		if("".equals(columnNumber) || "".equals(updateData)) {
			toaster("수정할 데이터나 컬럼번호가 없습니다.");
			return;
		}
		
		toaster("데이터를 수정합니다.[" + columnNumber + "]" + updateData);
		
		ContentValues values = new ContentValues();
		values.put("Title", updateData);

		database1.update(TABLE_NAME, values, "_id=" + columnNumber, null);
		
		mainEditTextUpdate.setText("");
		
	}
	

	private void columnDelete() {
		String columnNumber = mainEditTextColumnNumber.getText().toString();
		
		if("".equals(columnNumber)) {
			toaster("삭제할 데이터의 컬럼 번호가 없습니다.");
			return;
		}
		
		toaster("데이터를 삭제합니다.[" + columnNumber + "]");
		
		database1.delete(TABLE_NAME, "_id=" + columnNumber, null);
		
		mainEditTextUpdate.setText("");
		mainEditTextColumnNumber.setText("");
	}
	
	
	private void listRefresh() {
		ArrayList<String> list1 = new ArrayList<String>();
		
		if(isTableExist()) {
			Cursor cursor = database1.query(TABLE_NAME, null, null, null, null, null, "_id");
			
			if (cursor != null) {
				cursor.moveToFirst();
				while (!(cursor.isAfterLast())) {
					list1.add("[" + cursor.getString(0) + "]" + cursor.getString(1));
					cursor.moveToNext();
				}
			}

			cursor.close();
		}
		
		if(list1.size() == 0) {
			toaster("리스트에 표시할 데이터가 없습니다.");
		}
		
		mainListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list1));
	}
	
	
	
	private boolean isTableExist() {
		String searchTable = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME + "';";
		Cursor cursor = database1.rawQuery(searchTable, null);
		
		if(cursor.getCount()==0) {
			return false;
		}
		
		cursor.close();
		
		return true;
		
	}
	
	
	private void toaster(String bread) {
		Toast toast = Toast.makeText(getApplicationContext(), bread, Toast.LENGTH_SHORT);
		toast.show();
	}

}
