package io.github.jokoframework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.persistence.Country;
import io.github.jokoframework.persistence.CountryDatabase;
import io.github.jokoframework.persistence.DatabaseHandler;

public class CronService extends Service {
    /**
     * Log tag for this service.
     */
    private static final String LOG_TAG = "CRON";

    private IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        handlerRESTServiceExecution(); // doPeriodicTasks();
    }

    public void handlerRESTServiceExecution() {
        handler.postDelayed(runRESTService, Constants.FIRST_TIME);
    }

    @Override
    public IBinder onBind(final Intent intent) {
        Log.i(LOG_TAG, "Entering onBind Method in CronService");
        return mBinder;
    }

    private final Handler handler = new Handler();
    Runnable runRESTService = new Runnable() {
        @Override
        public void run() {
            Utils.showToast(getBaseContext(), String.format("Consiguiendo lista de Paises..."));
            checkAPI();
            handler.postDelayed(this, Constants.ONE_HOUR);
        }

        private void checkAPI() {
            // Instanciar el RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            String url = getString(R.string.rest_URL);
            //Utils.showToast(getBaseContext(), String.format("URL is: " + url));

            // Solicitar un JSON Array como respuesta de la URL.
            JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response)
                {
                    // Mostrar el response (DEBUG)
                    //Utils.showToast(getBaseContext(), String.format("Response is: "+ response.toString()));

                    try{
                        //Parseamos el JSONArray
                        JSONObject current;
                        for (int i = 1; i < response.length()+1; ++i) {
                            current = response.getJSONObject(i);

                            Country country = new Country();
                            country.setCid(i);
                            country.setCountryName(current.getString("name"));
                            country.setCountryCode(current.getString("alpha2Code"));
                            DatabaseHandler.populateAsync(CountryDatabase.getAppDataBase(getBaseContext()), country);
                        }
                    } catch (Exception e){
                        Log.e(LOG_TAG, String.format("Error consiguiendo la lista de Paises."));
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Utils.showToast(getBaseContext(), String.format("ERROR: "+ error.toString()));
                    Log.e(LOG_TAG, String.format("Respuesta invalida: "+ error.toString()));
                }
            }
            );

            // Add the request to the RequestQueue.
            postRequest.setShouldCache(false);
            queue.add(postRequest);
        }
    };

    public class MyBinder extends Binder {
        public CronService getService() {
            return CronService.this;
        }
    }

}
