package com.example.daocrud;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // ==========================================
    // VARIABLES
    // ==========================================

    ListView lAlumno;

    ArrayList<alumnos> listarAlumno = new ArrayList<>();

    ArrayAdapter<alumnos> arrayAdapter;

    EditText inmatricula;
    EditText innombre;
    EditText incorreo;
    EditText intelefono;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // UUID del alumno seleccionado
    String alumnoSeleccionadoUuid = null;


    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);


        // ==========================================
        // EDGE TO EDGE
        // ==========================================

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );


        // ==========================================
        // TOOLBAR
        // ==========================================

        Toolbar menu = findViewById(R.id.menu_crud);

        setSupportActionBar(menu);


        // ==========================================
        // CAMPOS
        // ==========================================

        inmatricula = findViewById(R.id.txtMatricula);

        innombre = findViewById(R.id.txtNombre);

        incorreo = findViewById(R.id.txtCorreo);

        intelefono = findViewById(R.id.txtTelefono);


        // ==========================================
        // LISTVIEW
        // ==========================================

        lAlumno = findViewById(R.id.lalumnos);


        // ==========================================
        // FIREBASE OFFLINE
        // ==========================================

        FirebaseApp.initializeApp(this);

        firebaseDatabase =
                FirebaseDatabase.getInstance();

        // Activar persistencia local
        firebaseDatabase.setPersistenceEnabled(true);

        databaseReference =
                firebaseDatabase.getReference();


        // ==========================================
        // ADAPTADOR
        // ==========================================

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listarAlumno
        );

        lAlumno.setAdapter(arrayAdapter);


        // ==========================================
        // SELECCIONAR ALUMNO
        // ==========================================

        lAlumno.setOnItemClickListener(
                (parent, view, position, id) -> {

                    alumnos alumnoSeleccionado =
                            listarAlumno.get(position);

                    // Guardar UUID seleccionado
                    alumnoSeleccionadoUuid =
                            alumnoSeleccionado.getUuid();


                    // Cargar matrícula
                    inmatricula.setText(
                            alumnoSeleccionado.getMatricula()
                    );


                    // Cargar nombre
                    innombre.setText(
                            alumnoSeleccionado.getNombre_completo()
                    );


                    // Cargar correo
                    incorreo.setText(
                            alumnoSeleccionado.getCorreo()
                    );


                    // Cargar teléfono
                    intelefono.setText(
                            alumnoSeleccionado.getTelefono()
                    );


                    Toast.makeText(
                            MainActivity.this,
                            "Alumno seleccionado",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // ==========================================
        // CARGAR DATOS
        // ==========================================

        listardatos();
    }


    // ==========================================
    // LISTAR DATOS
    // ==========================================

    private void listardatos() {

        DatabaseReference alumnosReference =
                databaseReference.child("alumnos");


        alumnosReference.addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {

                        listarAlumno.clear();


                        for (DataSnapshot consultarAlumnos :
                                snapshot.getChildren()) {

                            alumnos dataParaLista =
                                    consultarAlumnos.getValue(
                                            alumnos.class
                                    );


                            if (dataParaLista != null) {

                                listarAlumno.add(
                                        dataParaLista
                                );
                            }
                        }


                        // Actualizar ListView
                        arrayAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {

                        Toast.makeText(
                                MainActivity.this,
                                "Error al cargar datos: "
                                        + error.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }


    // ==========================================
    // CREAR MENÚ
    // ==========================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_crud,
                menu
        );

        return true;
    }


    // ==========================================
    // OPCIONES DEL MENÚ
    // ==========================================

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item) {

        int id = item.getItemId();


        // ==========================================
        // LIMPIAR
        // ==========================================

        if (id == R.id.btn_limpiar) {

            limpiarCampos();

            Toast.makeText(
                    this,
                    "Campos limpiados",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }


        // ==========================================
        // AGREGAR
        // ==========================================

        else if (id == R.id.btn_agregar) {

            agregarAlumno();

            return true;
        }


        // ==========================================
        // ACTUALIZAR
        // ==========================================

        else if (id == R.id.btn_actualizar) {

            actualizarAlumno();

            return true;
        }


        // ==========================================
        // ELIMINAR
        // ==========================================

        else if (id == R.id.btn_elliminar) {

            eliminarAlumno();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    // ==========================================
    // AGREGAR ALUMNO
    // ==========================================

    private void agregarAlumno() {

        // Obtener datos

        String matricula =
                inmatricula.getText().toString().trim();

        String nombre =
                innombre.getText().toString().trim();

        String correo =
                incorreo.getText().toString().trim();

        String telefono =
                intelefono.getText().toString().trim();


        // ==========================================
        // VALIDAR CAMPOS
        // ==========================================

        if (matricula.isEmpty() ||
                nombre.isEmpty() ||
                correo.isEmpty() ||
                telefono.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // CREAR ALUMNO
        // ==========================================

        alumnos nuevoAlumno =
                new alumnos();


        // Generar UUID

        nuevoAlumno.setUuid(
                UUID.randomUUID().toString()
        );


        nuevoAlumno.setMatricula(
                matricula
        );


        nuevoAlumno.setNombre_completo(
                nombre
        );


        nuevoAlumno.setCorreo(
                correo
        );


        nuevoAlumno.setTelefono(
                telefono
        );


        // ==========================================
        // GUARDAR EN FIREBASE
        // ==========================================

        databaseReference
                .child("alumnos")
                .child(nuevoAlumno.getUuid())
                .setValue(nuevoAlumno)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Alumno agregado correctamente",
                            Toast.LENGTH_SHORT
                    ).show();


                    // Limpiar después de agregar
                    limpiarCampos();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Error al agregar: "
                                    + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // ==========================================
    // ACTUALIZAR ALUMNO
    // ==========================================

    private void actualizarAlumno() {

        // ==========================================
        // VERIFICAR SELECCIÓN
        // ==========================================

        if (alumnoSeleccionadoUuid == null) {

            Toast.makeText(
                    this,
                    "Selecciona un alumno de la lista",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // OBTENER DATOS
        // ==========================================

        String matricula =
                inmatricula.getText().toString().trim();

        String nombre =
                innombre.getText().toString().trim();

        String correo =
                incorreo.getText().toString().trim();

        String telefono =
                intelefono.getText().toString().trim();


        // ==========================================
        // VALIDAR CAMPOS
        // ==========================================

        if (matricula.isEmpty() ||
                nombre.isEmpty() ||
                correo.isEmpty() ||
                telefono.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // CREAR ALUMNO ACTUALIZADO
        // ==========================================

        alumnos alumnoActualizado =
                new alumnos();


        alumnoActualizado.setUuid(
                alumnoSeleccionadoUuid
        );


        alumnoActualizado.setMatricula(
                matricula
        );


        alumnoActualizado.setNombre_completo(
                nombre
        );


        alumnoActualizado.setCorreo(
                correo
        );


        alumnoActualizado.setTelefono(
                telefono
        );


        // ==========================================
        // ACTUALIZAR FIREBASE
        // ==========================================

        databaseReference
                .child("alumnos")
                .child(alumnoSeleccionadoUuid)
                .setValue(alumnoActualizado)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Alumno actualizado correctamente",
                            Toast.LENGTH_SHORT
                    ).show();


                    // Limpiar después de actualizar
                    limpiarCampos();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Error al actualizar: "
                                    + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // ==========================================
    // ELIMINAR ALUMNO
    // ==========================================

    private void eliminarAlumno() {

        // ==========================================
        // VERIFICAR SELECCIÓN
        // ==========================================

        if (alumnoSeleccionadoUuid == null) {

            Toast.makeText(
                    this,
                    "Selecciona un alumno de la lista",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // ELIMINAR DE FIREBASE
        // ==========================================

        databaseReference
                .child("alumnos")
                .child(alumnoSeleccionadoUuid)
                .removeValue()

                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Alumno eliminado correctamente",
                            Toast.LENGTH_SHORT
                    ).show();


                    // Limpiar campos
                    limpiarCampos();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Error al eliminar: "
                                    + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }


    // ==========================================
    // LIMPIAR CAMPOS
    // ==========================================

    private void limpiarCampos() {

        // Vaciar matrícula
        inmatricula.setText("");


        // Vaciar nombre
        innombre.setText("");


        // Vaciar correo
        incorreo.setText("");


        // Vaciar teléfono
        intelefono.setText("");


        // Quitar alumno seleccionado
        alumnoSeleccionadoUuid = null;


        // Quitar foco
        inmatricula.clearFocus();
        innombre.clearFocus();
        incorreo.clearFocus();
        intelefono.clearFocus();
    }
}