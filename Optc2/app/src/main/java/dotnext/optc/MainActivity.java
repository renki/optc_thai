package dotnext.optc;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.AbstractAQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private AQuery aq;
    public void asyncJson(){
       
        String url = "http://renki.github.io/dun.json";
        aq = new AQuery(this);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){

            public void callback(String url, JSONObject ja, AjaxStatus status) {


                if (ja != null) {

                    try {
                        JSONObject result = ja.getJSONObject("results");

                        JSONArray dun = result.getJSONArray("dun");


                        ArrayList<Dungeon> newUsers = Dungeon.fromJson(dun);



                        DunAdapter adapter = new DunAdapter(getBaseContext(), newUsers);

                        //adapter.addAll(newUsers);

                        ListView listView = (ListView) findViewById(R.id.listv);
                        listView.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        asyncJson();
        ListView lvItems = (ListView) findViewById(R.id.listv);


    }

    public static class DunAdapter extends ArrayAdapter<Dungeon> {


        private static class ViewHolder {
            TextView name;
            TextView home;
        }
        ArrayList<Dungeon> dun;
        public DunAdapter(Context context, ArrayList<Dungeon> users) {
            super(context, R.layout.list1, users);
            dun = users;

        }

        @Override
        public int getCount(){
            Log.d("","dun====>"+ dun.size());
            return dun.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Dungeon dz = getItem(position);
            Log.d("","====>"+position);

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list1, parent, false);
                viewHolder.name = (TextView) convertView.findViewById(R.id.dunname);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object
            viewHolder.name.setText(dz.dun_name);
            // Return the completed view to render on screen
            return convertView;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class Dungeon {
        /*public String dun_name;
        public String date;

        public Dungeon(String name, String date) {
            this.dun_name = name;
            this.date = date;
        }*/

        public String dun_name;
        public String date;

        public Dungeon(JSONObject object){
            try {
                this.dun_name = object.getString("dun_name");
                this.date = object.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Factory method to convert an array of JSON objects into a list of objects
        // User.fromJson(jsonArray);
        public static ArrayList<Dungeon> fromJson(JSONArray jsonObjects) {
            ArrayList<Dungeon> users = new ArrayList<Dungeon>();
            for (int i = 0; i < jsonObjects.length(); i++) {
                try {
                    Log.d("", "-=====>" +jsonObjects.getJSONObject(i));
                    users.add(new Dungeon(jsonObjects.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return users;
        }

    }



}
