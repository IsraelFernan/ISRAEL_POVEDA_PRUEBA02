package app004.flagquizapp;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
public class InicioSesion extends AppCompatActivity {
    View ingresar,registrar;
    TextView nombre,clave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        ingresar = findViewById(R.id.inicio_sesion_ingresar);
        registrar = findViewById(R.id.inicio_sesion_registrar);
        nombre = findViewById(R.id.inicio_sesion_usuario);
        clave = findViewById(R.id.inicio_sesion_clave);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences referencias = getSharedPreferences(String.valueOf(nombre.getText()),Context.MODE_PRIVATE);
                if(referencias.getString("clave","") == ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(InicioSesion.this);
                    builder.setMessage("Usuario no existente.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }else{
                    Intent i = new Intent(InicioSesion.this,MainActivity.class);
                    i.putExtra("usuario",String.valueOf(nombre.getText()));
                    startActivity(i);
                }
            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences referencias = getSharedPreferences(String.valueOf(nombre.getText()),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = referencias.edit();
                editor.putString("clave",String.valueOf(clave.getText()));
                editor.commit();
                nombre.setText("");
                clave.setText("");
            }
        });
    }
}