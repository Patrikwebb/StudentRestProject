package se.yrgo.java15.patrik.studentapp.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.yrgo.java15.patrik.studentapp.R;
import se.yrgo.java15.patrik.studentapp.model.Course;
import se.yrgo.java15.patrik.studentapp.model.Courses;
import se.yrgo.java15.patrik.studentapp.model.Student;
import se.yrgo.java15.patrik.studentapp.remote.RetrofitAPI;
import se.yrgo.java15.patrik.studentapp.storage.LocalStorage;
import se.yrgo.java15.patrik.studentapp.storage.StorageManager;

/**
 *  Contains:   onCreate && onStart metod,
 *              @Bind connected to our components.
 *              JSONTask && XMLTask
 */
public class MainActivity extends AppCompatActivity {

    // ButterKnife API to collect all my componets
    @Bind(R.id.listView_Students) ListView listView_Students;
    @Bind(R.id.button_ClearStudents) Button button_ClearStudents;
    @Bind(R.id.button_Refresh) Button button_Refresh;
    @Bind(R.id.editText_id) EditText editText_id;
    @Bind(R.id.editText_name) EditText editText_name;
    @Bind(R.id.textView_output) TextView textView_output;
    @Bind(R.id.editText_getStudentById) EditText editText_getStudentById;
    @Bind(R.id.button_getStudentById) Button button_getStudentById;
    @Bind(R.id.button_fetchJsonData) Button button_fetchJsonData;
    @Bind(R.id.button_fetchXmlData) Button button_fetchXmlData;
    @Bind(R.id.editText_fakeListView) EditText editText_fakeListView;
    @Bind(R.id.button_delete) Button button_delete;

    private static final String LOG_TAG =
            MainActivity.class.getName();

    private LocalStorage localStorage;
    private MainActivity mainActivity;

    //URLZ
    private static final String JsonUrl =
            "http://46.194.13.231:8084/StudentAPI?format=json";

    private static final String XmlUrl =
            "http://46.194.13.231:8084/StudentAPI?format=xml";

    // Get referens to our List of students
    ArrayAdapter<Student> adapterStudentsList;

    // Need this list in two or more classes
    List<String> coursesOutputList = new ArrayList<String>();

    //////////////////////////////////////////////////////////////////////////////////
    /////     <-- onCreate(), onStart() && onCreateContextMenu() !! -->>         /////
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch json data from server if we got network
        if(networkIsConnected()){
            new JSONTask().execute(JsonUrl);
        }

        ButterKnife.bind(this);

        mainActivity = MainActivity.this;

        localStorage = StorageManager.getStorage(mainActivity);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(LOG_TAG, "onStart()");

        // Don't wanna se raw data on start from server
        textView_output.setVisibility(View.GONE);

        // Create Adapater for our ListView
        ArrayAdapter<Student> adapterStudentsList = new ArrayAdapter<Student>(
                mainActivity,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                localStorage.getStudents()
        );

        // Add adapter to our ListView
        listView_Students.setAdapter(adapterStudentsList);

        // Item click listener, -> Adds child element with student courses
        listView_Students.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // The view/parent you clicked
                String viewName = null;
                // Make an array to split our name
                String[] split;
                // Take our the id from our split
                String viewId = null;

                try {

                    viewName = ((TextView)view).getText().toString();

                    split = viewName.split(". ");

                    // Take our id from our split
                    viewId = split[0];

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                String JsonIdUrl =
                        "http://46.194.13.231:8084/StudentAPI?format=json&id=" + viewId;

                // To get the parents name first
                coursesOutputList.add(viewName);

                // 1. Makes connection
                // 2. Take out the parent courses
                // 3. Add them to our ListView
                new OnItemClickTask().execute(JsonIdUrl);

            } // END OF onItemClick
        });
        registerForContextMenu(listView_Students);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo listItemInfo =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        menu.add("Ta bort");
        menu.add("Slicka på");
        menu.add("Här finns inget");
        menu.add("Att hitta på");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = acmi.position;

        return true;
    }

    //////////////////////////////////////////////////////////////////////////////////
    /////       <-- @Onclick Events !! -->                                       /////
    //////////////////////////////////////////////////////////////////////////////////

    @OnClick(R.id.button_Keyboard)
    public void onClick_keyboardButton(){

        hideSoftKeyboard(mainActivity);

        Toast.makeText(mainActivity, "Keyboard hidden", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_Refresh)
    public void onClick_refreshButton(){

        hideSoftKeyboard(mainActivity);
        textView_output.setText("");
        editText_fakeListView.setVisibility(View.INVISIBLE);
        editText_fakeListView.setText("");
        editText_getStudentById.setText("");

        coursesOutputList.clear();

        Toast.makeText(mainActivity, "Refresh", Toast.LENGTH_SHORT).show();

        // Restart our activity ("The Landscape way")
        onPause();  // But theese onces sounds helpful
        onStop();   // Only Need theese two
        onStart();  // Only Need theese two
        onResume(); // But theese onces sounds helpful

    }

    @OnClick(R.id.button_ClearStudents)
    public void onClick_button(){

        hideSoftKeyboard(mainActivity);
        localStorage.clearAllStudents();
        // Generate a refresh
        onClick_refreshButton();

        Toast.makeText(mainActivity, "Cleared", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.button_getStudentById)
    public void onclick_getStudentByID(){

            hideSoftKeyboard(mainActivity);

            // To se feedback
            textView_output.setVisibility(View.VISIBLE);

            // Collect output from student_id
            Integer studentId = Integer.parseInt(editText_getStudentById.getText()
                    .toString());

            // To clear our List of students in UI
            listView_Students.setAdapter(null);

            editText_fakeListView.setVisibility(View.VISIBLE);


            // If we got input run our getStudentById metod in database
            if (editText_getStudentById.getText() != null) {

                // Run our getStudent metod and collect the student name
                String studentName = localStorage.getStudentById(studentId).toString();
                // Add the adapter again to add the student in our list
                listView_Students.setAdapter(adapterStudentsList);

                // .temp
                editText_fakeListView.setEnabled(true);
                editText_fakeListView.setText(localStorage.getStudentById(studentId)
                        .toString());

                Toast.makeText(mainActivity, "Loads student", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mainActivity, "No students found", Toast.LENGTH_SHORT).show();
            }

    }

    @OnClick(R.id.button_delete)
    public void onClick_delete(){
        Toast.makeText(mainActivity, "Feature not added", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_addStudent)
    public void onClick_addStudent(){

        hideSoftKeyboard(mainActivity);

        // To se feedback
        textView_output.setVisibility(View.VISIBLE);

        try{

            Integer studentId = Integer.parseInt(editText_id.getText().toString());
            String studentName = editText_name.getText().toString();

            if(editText_id.getText() != null && editText_name.getText() != null){
                localStorage.addStudent(new Student(studentId,studentName));
                Toast.makeText(mainActivity, "Student Added", Toast.LENGTH_SHORT).show();
            }
            textView_output.setText("");
            editText_id.setText("");
            editText_name.setText("");

        }catch (NumberFormatException nfe){
            textView_output.setText("You need to enter id and name");

            Toast.makeText(mainActivity, "Input error", Toast.LENGTH_SHORT).show();
            nfe.getStackTrace();
            Log.d(LOG_TAG, "addStudent Error");
        }
    }

    @OnClick(R.id.button_fetchJsonData)
    public void onClick_fetchJsonButton(){

        hideSoftKeyboard(mainActivity);

        // To se our raw data
        textView_output.setVisibility(View.VISIBLE);

        new JSONTask().execute(JsonUrl);

    }

    @OnClick(R.id.button_fetchXmlData)
    public void onClick_fetchXmlButton(){

        hideSoftKeyboard(mainActivity);

        // To se our raw data
        textView_output.setVisibility(View.VISIBLE);

        new XMLTask().execute(XmlUrl);

    }

    protected class JSONTask extends AsyncTask<String,String, String >{

            HttpURLConnection connection = null;

            @Override
            protected String doInBackground(String... params) {
                BufferedReader reader = null;

                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(100);
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line ="";
                    while ((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    // You can use Json Reader aswell

                    String jsonStringBuffer = buffer.toString();

                    // All Json data
                    JSONObject jsonBuffer = new JSONObject(jsonStringBuffer);
                    // Students array
                    JSONArray studentsArray = jsonBuffer.getJSONArray("students");

                    for (int i = 0; i < studentsArray.length(); i++){
                        JSONObject jsonObject = studentsArray.getJSONObject(i);

                        Gson gson = new Gson();
                        Student student = gson.fromJson(jsonObject.toString(), Student.class);

                        student.setStudentID(jsonObject.getInt("student_id"));
                        student.setStudentName(jsonObject.getString("student_name"));

                        Integer id = student.getStudentID();
                        String name = student.getStudentName();

                        localStorage.addStudent(new Student(id, name));

                    }

                    Log.d(LOG_TAG, "parentBufferObject" + jsonBuffer);
                    Log.d(LOG_TAG, "parentArray" + studentsArray);

                    return buffer.toString();

                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                    Log.d(LOG_TAG, "mainActivity.JSONTask.doInBackground.URLConn -> " +
                            "Can't open connection check URL. ");
                } catch (IOException ie) {
                    ie.printStackTrace();

                } catch (JSONException je){
                    je.printStackTrace();
                }

                finally {
                    try {
                        if(reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Using THIS to try out getResponseCode metod ->
            try{
                switch(connection.getResponseCode()){
                    case HttpURLConnection.HTTP_OK:
                        Toast.makeText(getApplicationContext(), "Load JSON Data",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                                "HTTP_OK");
                        break;
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), "HTTP_UNAVAILABLE",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                                "HTTP_UNAVAILABLE");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "HTTP something else",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                                "HTTP something else");
                }
                connection.disconnect();
            }catch (IOException ie){
                ie.printStackTrace();
            }
            // Check if we got any result back from our url stream
            if(result != null) {

                textView_output.setText(result);

            } else {

                if(networkIsConnected()){
                    Toast.makeText(getApplicationContext(), "No connection to server",
                            Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                            "No data, connection to server is down");
                } else {

                    Toast.makeText(getApplicationContext(), "No internet",
                            Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                            "No internet connection");
                }
            }
        }
    }

    protected class XMLTask extends AsyncTask<String,String, String > {

        HttpURLConnection connection = null;

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(100);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException mue) {
                mue.printStackTrace();
                Log.d(LOG_TAG, "mainActivity.XMLTask.doInBackground.URLConn -> " +
                        "Can't open connection check URL. ");
            } catch (IOException ie) {
                ie.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                switch (connection.getResponseCode()) {
                    case HttpURLConnection.HTTP_OK:
                        Toast.makeText(getApplicationContext(), "Load XML Data",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "mainActivity.XMLTask.onPostExecute ->  " +
                                "HTTP_OK");
                        break;
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), "HTTP_UNAVAILABLE",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "mainActivity.XMLTask.onPostExecute ->  " +
                                "HTTP_UNAVAILABLE");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "HTTP something else",
                                Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "mainActivity.XMLTask.onPostExecute ->  " +
                                "HTTP something else");
                }
                connection.disconnect();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            // Check if we got any result back from our url stream
            if(result != null) {

                textView_output.setText(result);

            } else {

                if (networkIsConnected()) {
                    Toast.makeText(getApplicationContext(), "No connection to server",
                            Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                            "No data, connection to server is down");
                } else {

                    Toast.makeText(getApplicationContext(), "No internet",
                            Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "mainActivity.JSONTask.onPostExecute ->  " +
                            "No internet connection");
                }
            }
        }
    }

    protected class OnItemClickTask extends AsyncTask<String, String, String>{

        HttpURLConnection connection = null;
        // Make global so I can use it in onPostExecute
        ArrayAdapter<String> adapterCoursesList;

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(100);
                connection.connect();
                // Make our conn stream
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                // Store data from stream in our buffer
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                // You can use Json Reader aswell

                String jsonStringBuffer = buffer.toString();

                // All Json data
                JSONObject jsonBuffer = new JSONObject(jsonStringBuffer);
                // Students array
                JSONArray coursesArray = jsonBuffer.getJSONArray("courses");

                for (int i = 0; i < coursesArray.length(); i++){

                    JSONObject jsonObject = coursesArray.getJSONObject(i);

                    Gson gson = new Gson();

                    Course course = gson.fromJson(jsonObject.toString(), Course.class);

                    course.setCourseCode(jsonObject.getString("course_code"));

                    String courseName = "      " + course.getCourseCode();

                    coursesOutputList.add(courseName);

                    // Create adapater for our ListView
                    adapterCoursesList = new ArrayAdapter<String>(
                            mainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            coursesOutputList
                    );
                }

                Log.d(LOG_TAG, "parentBufferObject" + jsonBuffer);
                Log.d(LOG_TAG, "parentArray" + coursesArray);

                return buffer.toString();

            } catch (MalformedURLException mue) {
                mue.printStackTrace();
                Log.d(LOG_TAG, "mainActivity.JSONTask.doInBackground.URLConn -> " +
                        "Can't open connection check URL. ");
            } catch (IOException ie) {
                ie.printStackTrace();

            } catch (JSONException je){
                je.printStackTrace();
            }

            finally {
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Need to do this on Main Thread
            listView_Students.setAdapter(adapterCoursesList);

            connection.disconnect();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    /////       <-- Unimportant stuff !! -->>                                    /////
    //////////////////////////////////////////////////////////////////////////////////

    // Only implemented to remove keyboard if you want
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow
                (activity.getCurrentFocus().getWindowToken(), 0);
    }

    public boolean networkIsConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService
                (this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");
    }
}
