package com.example.persistencia_de_datos_practica1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView name, date, amount, unitPrice, stock, supplier, datos;
    Button save, search, clean;

    SharedPreferences data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        data = getSharedPreferences("data", Context.MODE_PRIVATE);
    }


    private Boolean checkData (TextView[] textViews){

        for(TextView i : textViews){
            if(i.getText().toString().isEmpty()){
                Toast.makeText(this, "Se ha ingresado un valor vacío", Toast.LENGTH_SHORT).show();
                return false;
            }

            try{
                int num = Integer.parseInt(i.getText().toString());
                if(num == 0){
                    Toast.makeText(this, "0 No es un valor válido", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch (NumberFormatException e){
                System.out.println("Error controlado");
            }

        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Datos
        name = findViewById(R.id.nombre_prod);
        date = findViewById(R.id.fecha);
        amount = findViewById(R.id.cantidad);
        unitPrice = findViewById(R.id.precio_unit);
        stock = findViewById(R.id.stock);
        supplier = findViewById(R.id.proovedor);
        datos = findViewById(R.id.datos);


        //Botones
        save = findViewById(R.id.save);
        search = findViewById(R.id.search);
        clean = findViewById(R.id.clean);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog fec = new DatePickerDialog( MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
                    }
                }, 2024, 8, 1);

                fec.show();
            }
        });

        //Funcion para guardar, utilizando un metodo diferente al OnClick para intentar cosas nuevas
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Shared Preferences
                SharedPreferences.Editor editor = data.edit();
                if(!checkData(new TextView[]{name, date, amount, unitPrice, stock, supplier}))
                    return;

                String validacion = data.getString(name.getText().toString(), "");

                if(name.getText().toString().toLowerCase().equals(validacion)){
                    Toast.makeText(MainActivity.this, "El producto ya existe!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String key = name.getText().toString().toLowerCase();
                editor.putString(
                        key, key
                );
                editor.putString(
                        "date"+key, date.getText().toString()
                );
                editor.putInt(
                        "amount"+key, Integer.parseInt(amount.getText().toString())
                );
                editor.putFloat(
                        "unitPrice"+key, Float.parseFloat(unitPrice.getText().toString())
                );
                editor.putInt(
                        "stock"+key, Integer.parseInt(stock.getText().toString())
                );
                editor.putString(
                        "supplier"+key, supplier.getText().toString()
                );


                editor.apply();
                Toast.makeText(MainActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n") //Android estudio me recomendaba ponerlo, sirve para algo de la internacionalización pero no comprendí bien
            @Override
            public void onClick(View v) {
                checkData(new TextView[]{name});



                String name_S, fecha_S, supplier_S, key; // _S Significa string (Parano confundir con los Texview)
                int amount_I, stock_I; // _I = integer
                Float unitPrice_F; // _F = float

                key = name.getText().toString().toLowerCase();

                String validacion = data.getString(key, "");

                if(!(name.getText().toString().toLowerCase().equals(validacion))){
                    Toast.makeText(MainActivity.this, "El producto no existe!", Toast.LENGTH_SHORT).show();
                    return;
                }
                name_S = data.getString(key, key);
                fecha_S = data.getString("date"+key, "");
                supplier_S = data.getString("supplier"+key, "");
                amount_I = data.getInt("amount"+key, 0);
                stock_I = data.getInt("stock"+key, 0);
                unitPrice_F = data.getFloat("unitPrice"+key, 0);


                datos.setText(
                        "Nombre del producto: " + name_S.substring(0,1).toUpperCase()+name_S.substring(1) + "\n"
                        +"Fecha: " + fecha_S + "\n"
                        +"Cantidad: " + amount_I + "\n"
                        +"Precio unitario: " + unitPrice_F + "\n"
                        +"Stock: " + stock_I + "\n"
                        +"Proveedor: " + supplier_S
                );



            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                date.setText("");
                amount.setText("");
                unitPrice.setText("");
                stock.setText("");
                supplier.setText("");
                datos.setText("");
            }
        });

    }


}