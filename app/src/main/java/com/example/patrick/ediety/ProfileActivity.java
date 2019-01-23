package com.example.patrick.ediety;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity{

    private ArrayList<String> itemArrayList; //tu zmieniam z <ClassListItems>  na <String>
    private MyAppAdapter myAppAdapter;
    private ListView listView;
    private boolean success = false;

    private int lol = 0;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> itemArrayList2;

    //private static final String DB_URL = "212.182.24.105:8085/phpmyadmin/zurekr";
    private static final String DB_URL = "jdbc:mysql://212.182.24.105:8085/phpmyadmin/zurekr";

    private static final String USER = "zurekr";
    private static final String PASS = "(!eDiety)=18";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        listView = (ListView) findViewById(R.id.listView1);
        itemArrayList = new ArrayList<String>(); //tu też zmieniam z <ClassListItems> na <String

        itemArrayList2 = new ArrayList<String>();


        //calling async task
        SyncData orderData = new SyncData();
        orderData.execute();
    }

    private class SyncData extends AsyncTask<String,String,String> {
        String msg = "yyyyyyy";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ProfileActivity.this, "Synchronising",
                    "ListView loading, Please wait...",true);
        }
        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); //Connection Object
                Connection conn = DriverManager.getConnection("jdbc:mysql://212.182.24.105:3306/zurekr","zurekr","(!eDiety)=18"); //zmieniłem z 8085 na 3306
                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.
                    String query = "SELECT nazwa FROM Produkt";
                    Statement stmt = conn.createStatement();
                    //ResultSet rs = stmt.executeQuery(query);
                    ResultSet rs = stmt.executeQuery("SELECT nazwa FROM Produkt");
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                //itemArrayList.add(new ClassListItems(rs.getString("nazwa")));
                                itemArrayList.add(rs.getString("nazwa"));
                                lol+=1;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found"+lol;
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my ListView
        {
            progress.dismiss();
            Toast.makeText(ProfileActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    //myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);

                    //tuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu item array list to array list
                    ArrayAdapter<String> itemsAdapter2 = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, itemArrayList);

                    //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    //listView.setAdapter(myAppAdapter);
                    listView.setAdapter(itemsAdapter2);
                } catch (Exception ex) {

                }

            }
        }
    }
    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder {
            TextView textName;
        }

        public List<ClassListItems> parkingList;

        public Context context;
        ArrayList<ClassListItems> arraylist;

        private MyAppAdapter(List<ClassListItems> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems>();
            arraylist.addAll(parkingList);
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) // inflating the layout and initializing widgets
        {

            View rowView = convertView;
            ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_content, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = (TextView) rowView.findViewById(R.id.textName);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textName.setText(parkingList.get(position).getName() + "");

            return rowView;
        }
    }

}
