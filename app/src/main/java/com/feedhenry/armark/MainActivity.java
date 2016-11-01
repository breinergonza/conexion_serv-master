/**
 * Copyright 2015 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feedhenry.armark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.feedhenry.armark.fragmentos.Almacen_fragment;
import com.feedhenry.armark.fragmentos.Categorias_fragment;
import com.feedhenry.armark.fragmentos.Promociones_fragment;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private String varControl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //inicializador de api de facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);



            //  adaptador para los fragmentos
            SeccionPageradapter seccionPageradapter1 = new SeccionPageradapter(getSupportFragmentManager(),
                    MainActivity.this);


            // Set up the ViewPager with the sections adapter.
            ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
            assert mViewPager != null;
            mViewPager.setAdapter(seccionPageradapter1);
            mViewPager.setOffscreenPageLimit(0); //  Guarda la cantidad (int 0)de fragment que esta en el viewPager.  este caso es 0, las destruye de una todas las posiciones de fragment

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            assert tabLayout != null;
            tabLayout.setupWithViewPager(mViewPager);


            Inicio_feedHenry_sdk iniciar = new Inicio_feedHenry_sdk();
            iniciar.InicializarFH(getApplicationContext());



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(AccessToken.getCurrentAccessToken()== null) {
            goLogin();
            Log.e("error","no debo entrar aqui");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.accion_actualizar) {
            Inicio_feedHenry_sdk iniciar = new Inicio_feedHenry_sdk();
            iniciar.InicializarFH(getApplicationContext());

            return true;
        }
        if ( id == R.id.accion_cerrarsesion){

            // cerramos cesion con cuenta de facebook
            if (AccessToken.getCurrentAccessToken() != null) { //  si esto es null quiere decir que no tenemos la seccion iniciada
                LoginManager.getInstance().logOut();
                goLogin();

            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public void onStop() {
        super.onStop();
      // FH.stop();
    }


    private void goLogin() {
        Intent intent = new Intent(this,Loggin.class);

        // AÑADIMOS banderas que nos permite limpiar el recorrido anterior, cuando presionemos atras no nos devuelve al MainActivity.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


  //**********************INICIO************************************************************************************************
  public class SeccionPageradapter extends FragmentStatePagerAdapter {
      final int PAGE_COUNT = 3;
      private Context context;

      public SeccionPageradapter(FragmentManager fm, Context context) {
          super(fm);
          this.context = context;
      }

      @Override
      public Fragment getItem(int position) {
          //PaginaActual = position;

          switch (position){
              case 0:

                  return Promociones_fragment.newInstance(position+1,null,null);

              case 1:

                  return Almacen_fragment.newInstance(position+2);

              case 2:
                  return Categorias_fragment.newInstance(position+3);

              default:
                  return null;

          }

      }

      @Override
      public int getCount() {
          return PAGE_COUNT;
      }
      @Override
      public CharSequence getPageTitle(int position) {
          switch (position) {
              case 0:
                  return "Promociones";
              case 1:
                  return "Almacen";
              case 2:
                  return "Categorias";
          }
          return null;
      }
  }
    //*************************************************************************************************************************



}
