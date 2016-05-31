package album_pedro_bernardo2.ipleiria.pt.album;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import album_pedro_bernardo2.ipleiria.pt.album.R;

public class MainActivity extends AppCompatActivity {

    ListView listamusica;
    private ArrayList<String> searchresults;
    private ArrayList<String> musicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listamusica = (ListView) findViewById(R.id.playlist_list);

        listamusica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object lista_item = listamusica.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), String.valueOf(lista_item), Toast.LENGTH_SHORT).show();
                String[] click_split = String.valueOf(lista_item).split("\\|");
                String dados_name = click_split[0];
                String dados_artist = click_split[1];
                String dados_album = click_split[2];
                String dados_year = click_split[3];
                String dados_rating = click_split[4];
                String dados_rating_dividido[] = String.valueOf(click_split[4]).split("☆");
                String dados_rating_final = dados_rating_dividido[0];

                //Toast.makeText(MainActivity.this, dados_rating_dividido[0], Toast.LENGTH_SHORT).show();
                float dados_rating_parsed = Float.valueOf(dados_rating_dividido[0]);


                TextView meta1 = (TextView) findViewById(R.id.meta_song);
                TextView meta2 = (TextView) findViewById(R.id.meta_artist);
                TextView meta3 = (TextView) findViewById(R.id.meta_album);
                TextView meta4 = (TextView) findViewById(R.id.meta_year);
                RatingBar starsbar = (RatingBar) findViewById(R.id.ratingstars);

                meta1.setText(getString(R.string.nome) + dados_name);
                meta2.setText(getString(R.string.artist) + dados_artist);
                meta3.setText(getString(R.string.album) + dados_album);
                meta4.setText(getString(R.string.ano) + dados_year);
                starsbar.setRating(dados_rating_parsed);



            }
        });


        SharedPreferences preferencias = getSharedPreferences("appmusicas",0);
        Set<String> musicas_set = preferencias.getStringSet("musicas_key", new HashSet<String>());
        musicas= new ArrayList<String>(musicas_set);

        /*musicas.add("Wake Up Inside | Evanescence | Heavy Sleep | 2007 | 5☆");
        musicas.add("Can't Wake Up | Evanescence | Heavy Sleep | 2008 | 1☆");
        musicas.add("Save Me | Coastguard | Lifesaver | 2010 | 4☆");
        musicas.add("Sandstorm | Darude | Before the Storm | 2000 | 5☆");
        musicas.add("The Sound of Silence | Simon/Garfunkel | Wednesday Morning, 3 AM | 1964 | 4☆");
        musicas.add("Moonlight Sonata | Beethoven | Sonata No. 14 | 1801 | 2☆");*/


        ArrayAdapter<String> adaptamusica = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,musicas);
        listamusica.setAdapter(adaptamusica);

        Spinner spinop = (Spinner) findViewById(R.id.spinner_categoria);
        List<String> modos = new ArrayList<String>();
        modos.add(getString(R.string.all_spiner));
        modos.add(getString(R.string.song_spiner));
        modos.add(getString(R.string.artist_spiner));
        modos.add(getString(R.string.album_spiner));
        modos.add(getString(album_pedro_bernardo2.ipleiria.pt.album.R.string.ano_spiner));
        modos.add(getString(album_pedro_bernardo2.ipleiria.pt.album.R.string.rating_spiner));

        ArrayAdapter<String> modoadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,modos);
        spinop.setAdapter(modoadapter);

        listamusica.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(MainActivity.this, "Clicou no item " + position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder build2 = new AlertDialog.Builder(MainActivity.this);
                LinearLayout alert = new LinearLayout(MainActivity.this);
                alert.setOrientation(LinearLayout.VERTICAL);
                build2.setCancelable(true);
                build2.setMessage(R.string.are_you_sure_delete);

                build2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        musicas.remove(position);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, musicas);
                        ListView listView = (ListView) findViewById(R.id.playlist_list);
                        listView.setAdapter(adapter);

                        Toast.makeText(MainActivity.this, "Saving...", Toast.LENGTH_SHORT).show();

                        SharedPreferences preferencias = getSharedPreferences("appmusicas",0);
                        SharedPreferences.Editor editar = preferencias.edit();

                        HashSet musicas_set = new HashSet(musicas);
                        editar.putStringSet("musicas_key", musicas_set);

                        editar.commit();
                    }
                });
                build2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                    }
                });

                build2.show();



                return false;

            }
        });


        
    }

    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(MainActivity.this, "Saving...", Toast.LENGTH_SHORT).show();

        SharedPreferences preferencias = getSharedPreferences("appmusicas",0);
        SharedPreferences.Editor editar = preferencias.edit();

        HashSet musicas_set = new HashSet(musicas);
        editar.putStringSet("musicas_key", musicas_set);

        editar.commit();
    }

    public void OnClick_search(View view){
        EditText searchtext = (EditText)findViewById(R.id.termo_search);
        searchtext.setEnabled(false);
        searchtext.setEnabled(true);
        Spinner spin = (Spinner)findViewById(R.id.spinner_categoria);
        ListView lista = (ListView)findViewById(R.id.playlist_list);
        int found_mus = 0;

        searchresults = new ArrayList<String>();
        String termo = searchtext.getText().toString();
        String categoria = spin.getSelectedItem().toString();

        for(String match_termo : musicas){
            if(categoria.equals(getString(R.string.all_spiner))) {
                if (match_termo.contains(termo)) {
                    searchresults.add(match_termo);
                    found_mus++;
                }
            }else if(categoria.equals(getString(R.string.artist_spiner))) {
                String[] split = match_termo.split("\\|");
                String artist = split[1];

                if (artist.contains(termo)) {

                    searchresults.add(match_termo);
                    found_mus++;

                }
            }else if(categoria.equals(getString(R.string.album_spiner))){
                String[] split = match_termo.split("\\|");
                String album = split[2];
                if(album.contains(termo)) {

                    searchresults.add(match_termo);
                    found_mus++;
                }
            }else if(categoria.equals(getString(album_pedro_bernardo2.ipleiria.pt.album.R.string.ano_spiner))){
                String[] split = match_termo.split("\\|");
                String ano = split[3];
                if(ano.contains(termo)) {

                    searchresults.add(match_termo);
                    found_mus++;
                }
            }else if(categoria.equals(getString(album_pedro_bernardo2.ipleiria.pt.album.R.string.rating_spiner))){
                String[] split = match_termo.split("\\|");
                String rating_mus = split[4];
                if(rating_mus.contains(termo) && termo.matches("\\d+") && !termo.isEmpty()) {
                    searchresults.add(match_termo);
                    found_mus++;
                }else if(!termo.matches("\\d+")){
                    Toast.makeText(MainActivity.this, R.string.rating_error, Toast.LENGTH_SHORT).show();
                }
            }else if(categoria.equals(getString(R.string.song_spiner))){
                String[] split = match_termo.split("\\|");
                String name = split[0];
                if(name.contains(termo)) {

                    searchresults.add(match_termo);
                    found_mus++;
                }
            }
        }
        if (searchresults.isEmpty()){
            Toast.makeText(MainActivity.this, R.string.no_results, Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, searchresults);

        lista.setAdapter(adapter3);
        if(found_mus != 0) {
            Toast.makeText(MainActivity.this, getString(R.string.found1) + found_mus + getString(R.string.found2), Toast.LENGTH_SHORT).show();
        }

    }




    public void onClick_Add(View view){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        LinearLayout alert = new LinearLayout(this);
        alert.setOrientation(LinearLayout.VERTICAL);
        final EditText nameinput = new EditText(this);
        final EditText artistinput = new EditText(this);
        final EditText albuminput = new EditText(this);
        final EditText yearinput = new EditText(this);
        final EditText ratinginput = new EditText(this);
        build.setTitle(R.string.new_song_enter);
        build.setCancelable(true);
        alert.addView(nameinput);
        alert.addView(artistinput);
        alert.addView(albuminput);
        alert.addView(yearinput);
        alert.addView(ratinginput);
        build.setView(alert);
        nameinput.setHint(R.string.hint_song);
        artistinput.setHint(R.string.hint_artist);
        albuminput.setHint(R.string.hint_album);
        yearinput.setHint(R.string.hint_year);
        ratinginput.setHint(R.string.hint_rating);


        build.setPositiveButton(R.string.acept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nameadd = nameinput.getText().toString();
                String artistadd = artistinput.getText().toString();
                String albumadd = albuminput.getText().toString();
                String yearadd = yearinput.getText().toString();
                String ratingadd = ratinginput.getText().toString();
                if (yearadd.matches("\\d+") && !yearadd.isEmpty() && !nameadd.isEmpty() && !artistadd.isEmpty() && !albumadd.isEmpty() && !ratingadd.isEmpty()) {
                    musicas.add(String.valueOf(nameadd) + " | " + String.valueOf(artistadd) + " | " + String.valueOf(albumadd) + " | " + String.valueOf(yearadd) + " | " + String.valueOf(ratingadd) + "☆");
                    Toast.makeText(MainActivity.this, R.string.add_success, Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                } else if(!yearadd.matches("\\d+") && !yearadd.isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.add_fail1, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                } else if(!ratingadd.matches("\\d+") && !ratingadd.isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.add_fail2, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                } else if(yearadd.isEmpty() || nameadd.isEmpty() || artistadd.isEmpty() || albumadd.isEmpty() || ratingadd.isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.add_fail3, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
        build.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        build.show();
        ArrayAdapter<String> adaptamusica = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,musicas);
        listamusica.setAdapter(adaptamusica);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
