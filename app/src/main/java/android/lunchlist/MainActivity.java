package android.lunchlist;

import android.SQLiteUtility.RestaurantHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Restaurant> model = new ArrayList<>();
    RestaurantAdapter adapter = null;
    EditText name=null;
    EditText address=null;
    EditText notes=null;
    RadioGroup types=null;
    TabHost tabHost=null;
    Restaurant current=null;
    RestaurantHelper helper;
    ListView list;
    Button save;
//    TextView selection;
//    Spinner spn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define helper and init tabHost
        defineHelperAndInitTabhost();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        helper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option, menu);
//        new MenuInflater(this).inflate(R.menu.option, menu);
        return true;
//        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.work_space){
            setContentView(R.layout.activity_main);
            String message = "No restaurant selected";

            if(current!=null)  {
                message = current.getNotes();
            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            defineHelperAndInitTabhost();
            return(true);
        }
        else if(item.getItemId() == R.id.add_account){
            setContentView(R.layout.activity_main);
            defineHelperAndInitTabhost();
            tabHost.setCurrentTab(1);
        }
        return(super.onOptionsItemSelected(item));
    }


    private View.OnClickListener onSave = new View.OnClickListener(){
        public void onClick(View v){
            current = new Restaurant();

//            name = findViewById(R.id.name);
//            address = findViewById(R.id.addr);

            current.setName(name.getText().toString());
            current.setAddress(address.getText().toString());
            current.setNotes(notes.getText().toString());

//            types = findViewById(R.id.types);
            switch (types.getCheckedRadioButtonId()){
                case R.id.take_out:
                    current.setType("take_out");
                    break;
                case R.id.sit_down:
                    current.setType("sit_down");
                    break;
                case R.id.delivery:
                    current.setType("delivery");
                    break;
            }
            helper.insert(current);
            adapter.add(current);
        }
    };

    //click on list view
    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            current = model.get(position);
            name.setText(current.getName());
            address.setText(current.getAddress());
            notes.setText(current.getNotes());
            if(current.getType().equals("sit_down")){
                types.check(R.id.sit_down);
            }
            else if(current.getType().equals("take_out")){
                types.check(R.id.take_out);
            }
            else{
                types.check(R.id.delivery);
            }
            tabHost.setCurrentTab(1);
        }
    };


    class RestaurantAdapter extends ArrayAdapter<Restaurant>{
        RestaurantAdapter(){
            super(MainActivity.this, android.R.layout.simple_list_item_1, model);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View row = convertView;
            RestaurantHolder holder = null;
            if(row == null){
                LayoutInflater inflater = getLayoutInflater();
                Log.i("layout null", "exist");
                row=inflater.inflate(R.layout.row, parent,false);
                holder = new RestaurantHolder(row);
                row.setTag(holder);
            }
            else{
                holder = (RestaurantHolder)row.getTag();
            }

            holder.populateForm((model.get(position)));
            return(row);
        }
    }

    static  class RestaurantHolder{
        private TextView name = null;
        private  TextView address = null;
        private ImageView icon = null;

        RestaurantHolder(View row){
            name = row.findViewById(R.id.title);
            address = row.findViewById(R.id.address);
            icon = row.findViewById(R.id.icon);
        }

        void populateForm(Restaurant r){
            name.setText(r.getName());
            address.setText(r.getAddress());

            if(r.getType().equals("sit_down")){
                icon.setImageResource(R.drawable.ball_red);
            }
            else if(r.getType().equals("take_out")){
                icon.setImageResource(R.drawable.ball_yellow);
            }
            else{
                icon.setImageResource(R.drawable.ball_green);
            }
        }
    }

    public void defineHelperAndInitTabhost(){
        helper = new RestaurantHelper(this);
        name = findViewById(R.id.name);
        address = findViewById(R.id.addr);
        types = findViewById(R.id.types);
        notes = findViewById(R.id.notes);

        save = findViewById(R.id.save);
        save.setOnClickListener(onSave);

        list = findViewById(R.id.restaurants);

        model = helper.getAll();
        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        //restaurants
        TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        tabHost.addTab(spec);

        //details
        spec = tabHost.newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);

        list.setOnItemClickListener(onListClick);
    }


}
