package com.scolti.informes_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static int RESULT_LOAD_IMAGE = 1;

    EditText et_titulo;
    EditText et_realizador;
    EditText et_escenario;
    EditText et_localizacion;
    EditText et_pag_guion;
    EditText et_secuencias;
    EditText et_permisos;
    ImageView ImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        et_titulo = (EditText) findViewById(R.id.et_titulo);
        et_realizador = (EditText) findViewById(R.id.et_realizador);
        et_escenario = (EditText) findViewById(R.id.et_escenario);
        et_localizacion = (EditText) findViewById(R.id.et_localizacion);
        et_pag_guion = (EditText) findViewById(R.id.et_pag_guion);
        et_secuencias = (EditText) findViewById(R.id.et_secuencias);
        et_permisos = (EditText) findViewById(R.id.et_permisos);
        ImgView = (ImageView) findViewById(R.id.ImgView);

        Button buttonLoadImage = (Button) findViewById(R.id.btn_img);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.ImgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

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

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_folder) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_document) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createPDF(View view) {
        EditText txt = (EditText) findViewById(R.id.et_titulo);
        EditText txt2 = (EditText) findViewById(R.id.et_realizador);
        EditText txt3 = (EditText) findViewById(R.id.et_escenario);
        EditText txt4 = (EditText) findViewById(R.id.et_localizacion);
        EditText txt5 = (EditText) findViewById(R.id.et_pag_guion);
        EditText txt6 = (EditText) findViewById(R.id.et_secuencias);
        EditText txt7 = (EditText) findViewById(R.id.et_permisos);
        ImageView img = (ImageView) findViewById(R.id.ImgView);

        Document doc = new Document();

        String outPath = Environment.getExternalStorageDirectory() + "/informe.pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outPath));
            doc.open();

            //Dos maneras de hacerlo:
            //1. Paragraph p=new Paragraph("Título: "+txt.getText().toString());
            //1.2 doc.add(p);
            //2. doc.add(new Paragraph("Título: "+(txt.getText().toString())));

            doc.add(new Paragraph("Título: " + (txt.getText().toString())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Realizador: " + (txt2.getText().toString())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Escenario: " + (txt3.getText().toString())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Localización: " + (txt4.getText().toString())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Página guión: " + (txt5.getText().toString())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Secuencias: " + (txt6.getText().toString())));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Permisos: " + (txt7.getText().toString())));
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph(" "));
            doc.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

       //Toast.makeText(getBaseContext(), "El PDF se ha generado con éxito :)", Toast.LENGTH_LONG).show();
        Snackbar.make(view, "El PDF se ha generado con éxito :)", Snackbar.LENGTH_LONG).show();
    }
}