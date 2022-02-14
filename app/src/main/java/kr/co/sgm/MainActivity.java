package kr.co.sgm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyDBHelper myDBHelper;
    EditText edtName, edtNumber, edtNameResult, edtNumberResult;
    Button btnInit, btnInsert, btnSelect;
    SQLiteDatabase sqLiteDatabase;

    public class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context) {
           super(context, "groupDB.db", null, 1);
        }

        // 데이터베이스가 처음 생성될때 호출됨
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // 테이블 생성하는 기능
            sqLiteDatabase.execSQL("CREATE TABLE groupTB (gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        // 데이터베이스가 업그레이드될 필요가 있을때 호출됨
        // DB 버전이 올라가면 호출됨 (기존 테이블을 삭제하고 새로 만들어줌)
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // 테이블을 삭제한후 다시 생성
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS groupTB");
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        setTitle("가수 그룹 관리 앱");

        edtName = findViewById(R.id.edtName);
        edtNumber = findViewById(R.id.edtNumber);
        edtNameResult = findViewById(R.id.edtNameResult);
        edtNumberResult = findViewById(R.id.edtNumberResult);

        btnInit = findViewById(R.id.btnInit);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);

        // 액티비티에서는 먼저 SQLiteOpenHelper 객체를 생성함함
       myDBHelper = new MyDBHelper(this);

       btnInsert.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              // DB가 필요하면 호출 => SQLiteOpenHelper 객체 반환(DB)
              sqLiteDatabase = myDBHelper.getWritableDatabase();
              sqLiteDatabase.execSQL("INSERT INTO groupTB VALUES ('"
                      + edtName.getText().toString() + "', "
                      + edtNumber.getText().toString() + ");");
              sqLiteDatabase.close();
               Toast.makeText(getApplicationContext(),"데이터입력됨", Toast.LENGTH_SHORT).show();
           }
       });

       btnSelect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sqLiteDatabase = myDBHelper.getReadableDatabase();
               Cursor cursor;

               // 쿼리의 결과가 커서로 반환됨
               cursor = sqLiteDatabase.rawQuery("SELECT * FROM groupTB;", null);

               String strNames = "그룹이름" + "\r\n" + "---------------" + "\r\n";
               String strNumbers = "인원" + "\r\n" + "---------------" + "\r\n";

               while (cursor.moveToNext()) {
                   strNames += cursor.getString(0) + "\r\n";
                   strNumbers += cursor.getString(1) + "\r\n";
               }

               edtNameResult.setText(strNames);
               edtNumberResult.setText(strNumbers);

               cursor.close();
               sqLiteDatabase.close();
           }
       });


    }
}