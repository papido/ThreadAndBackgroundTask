package edu.utem.ftmk.threadandbackgroundtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.utem.ftmk.threadandbackgroundtask.databinding.ActivitySecondCamBinding;


public class SecondActivityCam extends AppCompatActivity {

    Executor executor;
    Handler handler;
    Bitmap bitmap = null;
    ActivitySecondCamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondCamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            binding.btnAsyncTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL ImageURL = new URL("https://ftmk.utem.edu.my/web/wp-content/uploads/2020/02/cropped-Logo-FTMK.png");
                                HttpURLConnection connection = (HttpURLConnection) ImageURL.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream inputStream = connection.getInputStream();
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.RGB_565;
                                bitmap = BitmapFactory.decodeStream(inputStream,null,options);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.imgVwSelfie.setImageBitmap(bitmap);
                                }
                            });
                        }
                    });
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Network!! Please add data plan or connect to wifi network!", Toast.LENGTH_SHORT).show();
        }
    }
}