package com.gabrielxavier.fasttooth;

import android.app.StatusBarManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.Window;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.gabrielxavier.fasttooth.slides.carroselFotosAdapter;

import java.util.ArrayList;

public class InicioActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<Integer> images = new ArrayList<>();
    carroselFotosAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //barra de notificacoes preta
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        //resolvendo carrosel de fotos
        viewPager = findViewById(R.id.viewPager_carroselFotos_inicio);
        images.add(R.drawable.foto_carrosel1);
        images.add(R.drawable.foto_carrosel1);
        images.add(R.drawable.foto_carrosel1);

        adapter = new carroselFotosAdapter(this, images);
        viewPager.setAdapter(adapter);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigatio);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_dashboard));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_watch));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_person));

        //se clicar em alguma opcao do menu(obrigatorio)
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(MainActivity.this, "clicou em "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        //se clicar novamente em alguma opcao do menu(obrigatorio para nao crashar)
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(MainActivity.this, "clicou de novo em "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        //quando animacao do menu for concluida(obrigatorio para nao crashar)
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String nome = "";
                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        nome = "inicio";
                        break;
                    case 2:
                        nome = "servicos";
                        break;
                    case 3:
                        nome = "agenda";
                        break;
                    case 4:
                        nome = "perfil";
                        break;
                    default:
                        nome = "";
                }
                Toast.makeText(InicioActivity.this, "pagina de: " + nome, Toast.LENGTH_SHORT).show();
            }

        });
        //começando com a pagina home selecionada
        bottomNavigation.show(1, true);
    }
}