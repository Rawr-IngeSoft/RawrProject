package com.example.david.rawr.MainActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.rawr.Interfaces.CreateResponse;
import com.example.david.rawr.Interfaces.GetFriendRequestsResponse;
import com.example.david.rawr.Interfaces.GetFriendsResponse;
import com.example.david.rawr.Interfaces.GetMessagesHistoryResponse;
import com.example.david.rawr.Interfaces.GetPetsResponse;
import com.example.david.rawr.Interfaces.ValidateResponse;
import com.example.david.rawr.Models.Friend;
import com.example.david.rawr.Models.FriendRequest;
import com.example.david.rawr.Models.Message;
import com.example.david.rawr.Models.Pet;
import com.example.david.rawr.R;
import com.example.david.rawr.SQLite.SQLiteHelper;
import com.example.david.rawr.Tasks.CreateOwner;
import com.example.david.rawr.Tasks.GetFriendRequests;
import com.example.david.rawr.Tasks.GetFriends;
import com.example.david.rawr.Tasks.GetMessagesHistory;
import com.example.david.rawr.Tasks.GetPets;
import com.example.david.rawr.Tasks.ValidateUser;

import java.util.ArrayList;


public class Loading_screen extends Activity implements ValidateResponse, CreateResponse, GetFriendsResponse, GetPetsResponse, GetMessagesHistoryResponse, GetFriendRequestsResponse {

    String username, serviceType;
    SharedPreferences sharedpreferences;
    SQLiteHelper SQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteHelper = new SQLiteHelper(this);
        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_loading_screen);
        ImageView img = (ImageView)findViewById(R.id.loading_screen_imageView_animation);
        //AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        //frameAnimation.start();
        serviceType = getIntent().getStringExtra("serviceType");
        // Si el tipo de conexion es con el login propio se valida el usuario usando la tarea asincrona ValidateUser
        if(serviceType.compareTo("logIn") == 0) {
            username = getIntent().getStringExtra("username");
            String password = getIntent().getStringExtra("password");
            // Validate User
            ValidateUser validateUser = new ValidateUser(username, password, this);
            validateUser.execute();
        // Si el tipo de conexion es con facebook se crea el usuario usando la tarea asincrona CreateOwner.
        // En caso de que el usuario ya exista se hara caso omiso al retorno de la tarea asincrona.
        }else if (serviceType.compareTo("facebook") == 0){
            // Create facebook user
            username = sharedpreferences.getString("username","");
            CreateOwner createOwner = new CreateOwner(username,"facebook",sharedpreferences.getString("name",""), sharedpreferences.getString("lastName",""),this, this);
            createOwner.execute();
        // Si el tipo de conexion es crear usuario con el servicio propio se creara usando la tarea asincrona CreateOwner
        }else if (serviceType.compareTo("signUp") == 0){
            // Create Owner
            username = getIntent().getStringExtra("username");
            String password = getIntent().getStringExtra("password");
            String name = getIntent().getStringExtra("name");
            String lastName = getIntent().getStringExtra("lastName");
            CreateOwner createOwner = new CreateOwner(username,password,name, lastName,this,this);
            createOwner.execute();
        // En caso de que el usuario ya este logeado se redirigira al newsfeed
        }else if (serviceType.compareTo("logged") == 0) {
            String welcomeMsg = "Welcome " + sharedpreferences.getString("name", "") + " " + sharedpreferences.getString("lastName", "");
            Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Newsfeed_screen.class);
            startActivity(intent);
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loading_screen, menu);
        return true;
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

    /**
     * Guarda en la base de datos local los datos del duenho en caso de ser una
     * validacion exitosa.
     * @param  data En este arreglo se encuentra el resultado de la validacion en la posicion 0,
     *              el nombre del duenho en la posicion 1 y el apellido del duenho en la posicion 2.
     */
    @Override
    public void validateFinish(ArrayList<String> data) {
        String status = data.get(0);
        Intent intent;
        if (status.compareTo("1") == 0){
            String name = data.get(1);
            String lastName = data.get(2);
            //save owner info in shared preferences
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username", username);
            editor.putString("name", name );
            editor.putString("lastName", lastName );
            editor.commit();
            GetPets getPets = new GetPets(username, this);
            getPets.execute();
        }else{
            intent = new Intent(this, LogIn_screen.class);
            Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            this.finish();
        }

    }

    /**
     * Muestra el resultado de la creacion de un duenho
     * @param  status  Indica el resultado de la creacion: 1 si fue exitosa y 0 en caso contrario.
     */
    @Override
    public void createFinish(String status) {
        Intent intent;
        if (serviceType.compareTo("facebook") == 0) {
            if (sharedpreferences.contains("username")) {
                GetPets getPets = new GetPets(sharedpreferences.getString("username", ""), this);
                getPets.execute();
            }else {
                intent = new Intent(this, Newsfeed_screen.class);
                startActivity(intent);
                this.finish();
            }
        }else{
            if (status.compareTo("1") == 0) {
                String msg = "Welcome " + sharedpreferences.getString("name","") + " " + sharedpreferences.getString("lastName","");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                intent = new Intent(this, SignUp_add_photo_screen.class);
            }else{
                Toast.makeText(this, "This username is already in use", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, SignUp_screen.class);
            }
            startActivity(intent);
            this.finish();
        }
    }

    /**
     * Guarda en la base de datos local las mascotas amigas de la seleccionada
     * @param  output En el se encuentran todas las mascotas amigas de la mascota
     *                seleccionada actualmente
     */
    @Override
    public void getFriendsFinish(ArrayList<Friend> output) {
        SQLiteHelper SQLiteHelper = new SQLiteHelper(this);
        for (Friend friend: output){
            SQLiteHelper.addFriend(friend);
        }
        Log.e("status", "getting message history");
        GetMessagesHistory getMessagesHistory = new GetMessagesHistory(sharedpreferences.getString("petUsername", ""), this);
        getMessagesHistory.execute();
    }

    /**
     * Guarda en la base de datos local las mascotas del duenho conectado
     * @param  output En el se encuentran todas las mascotas del duenho conectado
     */
    @Override
    public void getPetsFinish(ArrayList<Pet> output) {
        for(Pet pet: output){
            SQLiteHelper.addPet(pet);
        }
        if (!output.isEmpty()) {
            Pet pet = output.get(0);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("petName", pet.getPetName());
            editor.putString("petUsername", pet.getIdPet());
            editor.putString("petType", pet.getPetType());
            editor.putString("petGender", pet.getPetGender());
            editor.putString("petPicture", pet.getPath());
            editor.commit();
            Log.e("status", "getting friend request");
            GetFriendRequests getFriendRequests = new GetFriendRequests(sharedpreferences.getString("petUsername", ""), this);
            getFriendRequests.execute();
        }else{
            Intent intent = new Intent(this, Newsfeed_screen.class);
            startActivity(intent);
            this.finish();
        }
    }

    /**
     * Guarda en la base de datos local las conversaciones de la mascota seleccionada
     * @param  output En el se encuentran todas las conversaciones de la mascota
     *                seleccionada actualmente
     */
    @Override
    public void getMessageHistoryFinish(ArrayList<Message> output) {
        for(Message message: output){
            SQLiteHelper.addMessage(message);
        }
        String welcomeMsg = "Welcome " + sharedpreferences.getString("name", "") + " " + sharedpreferences.getString("lastName", "");
        Toast.makeText(getApplicationContext(), welcomeMsg, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Newsfeed_screen.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * Guarda en la base de datos local las solicitudes de amistad de la
     * mascota seleccionada
     * @param  output En el se encuentran todas las solicitudes de amistad
     *                de la mascota seleccionada actualmente
     */
    @Override
    public void getRequestsFinish(ArrayList<FriendRequest> output) {
        for (FriendRequest friendRequest: output){
            SQLiteHelper.addFriendRequest(friendRequest);
        }
        Log.e("status", "getting friends");
        GetFriends getFriends = new GetFriends(sharedpreferences.getString("petUsername", ""), this);
        getFriends.execute();
    }
}
