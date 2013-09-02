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
			tableCreate();
			listRefresh();
			break;
		case R.id.mainButtonTableDrop:
			tableDrop();
			//listRefresh();
			break;
		case R.id.mainButtonInsert:
			columnInsert();
			listRefresh();
			break;
		case R.id.mainButtonUpdate:
			columnUpdate();
			listRefresh();
			break;
		case R.id.mainButtonDelete:
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
		String sql = "create table articles(_id integer primary key autoincrement, Title text not null);";
		database1.execSQL(sql);
	}
	
	
	private void tableDrop() {
		toaster("테이블을 삭제합니다.");
		String sql = "DROP TABLE IF EXISTS articles;";
		database1.execSQL(sql);
	}
	
	
	private void columnInsert() {
		String insertData = mainEditTextInsert.getText().toString();
		toaster("데이터를 추가합니다.[" + insertData + "]");
		
		ContentValues values = new ContentValues();
		values.put("Title", insertData);
		database1.insert("articles", null, values);
		
	}
	
	
	private void columnUpdate() {
		String columnNumber = mainEditTextColumnNumber.getText().toString();
		String updateData = mainEditTextUpdate.getText().toString();
		toaster("데이터를 수정합니다.[" + columnNumber + "]" + updateData);
		
		ContentValues values = new ContentValues();
		values.put("Title", updateData);

		database1.update("articles", values, "_id=" + columnNumber, null);
		
	}
	

	private void columnDelete() {
		String columnNumber = mainEditTextColumnNumber.getText().toString();
		toaster("데이터를 삭제합니다.[" + columnNumber + "]");
		
		database1.delete("articles", "_id=" + columnNumber, null);
		
	}
	
	
	private void listRefresh() {
		ArrayList<String> list1 = new ArrayList<String>();
		Cursor cursor = database1.query("articles", null, null, null, null, null, "_id");
		
		if (cursor != null) {
			cursor.moveToFirst();
			while (!(cursor.isAfterLast())) {
				list1.add("[" + cursor.getString(0) + "]" + cursor.getString(1));
				cursor.moveToNext();
			}
		}

		cursor.close();
		
		if(list1.size() == 0) {
			toaster("리스트에 표시할 데이터가 없습니다.");
		}
		
		mainListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list1));
	}
	
	private void toaster(String bread) {
		Toast toast = Toast.makeText(getApplicationContext(), bread, Toast.LENGTH_SHORT);
		toast.show();
	}

}
