package co.com.misiontic.vmoyola.taskreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.com.misiontic.vmoyola.taskreminder.R;
import co.com.misiontic.vmoyola.taskreminder.view.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameEditText = this.findViewById(R.id.username);

        passwordEditText = this.findViewById(R.id.password);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        passwordEditText.setFilters(new InputFilter[] { filter });

        final Button loginButton = this.findViewById(R.id.login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){

                    Context context = LoginActivity.this;
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.users_persistence), Context.MODE_PRIVATE);

                    Set<String> savedUsers = sharedPref.getStringSet("registered_users", Collections.<String>emptySet());
                    List<String> mutableList = new ArrayList<>(savedUsers);

                    String loginUser = usernameEditText.getText().toString();
                    String loginUserPassword = passwordEditText.getText().toString();

                    if(mutableList.contains(loginUser) ) {

                        String savedUserPassword = sharedPref.getString(loginUser,"");

                        if(savedUserPassword.equals(loginUserPassword)) {

                            continueToMainActivity(loginUser);
                        } else {
                            Toast.makeText(LoginActivity.this, "las credenciales son invalidas",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mutableList.add(loginUser);
                        sharedPref.edit().putStringSet("registered_users", new HashSet<>(mutableList))
                                .putString(loginUser, loginUserPassword)
                                .commit();

                        continueToMainActivity(loginUser);
                    }
                }
            }
        });
    }

    private boolean validateFields(){
        usernameEditText.setError(null);
        passwordEditText.setError(null);

        if (usernameEditText.length() == 0) {
            usernameEditText.setError("El campo email es requerido");
            usernameEditText.requestFocus();
            return false;
        }

        String email = usernameEditText.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            usernameEditText.setError("La dirección de email no es valida");
            usernameEditText.requestFocus();
            return false;
        }

        if (passwordEditText.length() == 0) {
            passwordEditText.setError("El campo password es requerido");
            passwordEditText.requestFocus();
            return false;
        }

        if (passwordEditText.length() == 0) {
            passwordEditText.setError("El Password es requerido");
            passwordEditText.requestFocus();
            return false;

        } else if (passwordEditText.length() < 6) {
            passwordEditText.setError("El password debe ser de almenos de 6 caracteres");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void continueToMainActivity(String loggedUser){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("loggedUser", loggedUser);
        startActivity(intent);
    }

}