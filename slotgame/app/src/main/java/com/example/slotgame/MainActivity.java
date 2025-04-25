package com.example.slotgame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView slot1View, slot2View, slot3View;
    private Button btnStart;
    private TextView resultText;
    private boolean isPlaying = false;

    private SlotTask slotTask1, slotTask2, slotTask3;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        slot1View = findViewById(R.id.id_Slot1);
        slot2View = findViewById(R.id.id_Slot2);
        slot3View = findViewById(R.id.id_Slot3);
        btnStart = findViewById(R.id.id_BtPlay);
        resultText = findViewById(R.id.tv_hasil);

        // Set initial images
        slot1View.setImageResource(R.drawable.slotbar);
        slot2View.setImageResource(R.drawable.slotbar);
        slot3View.setImageResource(R.drawable.slotbar);

        btnStart.setOnClickListener(this);

        // Create thread pool
        executorService = Executors.newFixedThreadPool(3);

        // Initialize slot tasks
        slotTask1 = new SlotTask(slot1View);
        slotTask2 = new SlotTask(slot2View);
        slotTask3 = new SlotTask(slot3View);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_BtPlay) {
            if (!isPlaying) {
                startSpinning();
            } else {
                stopSpinning();
            }
        }
    }

    private void startSpinning() {
        slotTask1.reset();
        slotTask2.reset();
        slotTask3.reset();
        resultText.setVisibility(View.INVISIBLE);

        executorService.execute(slotTask1);
        executorService.execute(slotTask2);
        executorService.execute(slotTask3);

        btnStart.setText("Stop");
        isPlaying = true;
    }

    private void stopSpinning() {
        slotTask1.stop();
        slotTask2.stop();
        slotTask3.stop();

        checkWinCondition();

        btnStart.setText("Play");
        isPlaying = false;
    }

    private void checkWinCondition() {
        boolean allSame = slotTask1.getImageId() == slotTask2.getImageId() &&
                slotTask2.getImageId() == slotTask3.getImageId();

        resultText.setVisibility(View.VISIBLE);
        resultText.setText(allSame ? "ANDA MENANG!" : "ANDA KURANG BERUNTUNG");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private class SlotTask implements Runnable {
        private final ImageView slotImageView;
        private final Random random = new Random();
        private final ArrayList<String> imageUrls = new ArrayList<>();

        private boolean isRunning = true;
        private int currentImageId = -1;

        // Local fallback images
        private final int[] localImages = {
                R.drawable.slot1, R.drawable.slot2, R.drawable.slot3,
                R.drawable.slot4, R.drawable.slot5, R.drawable.slotbar
        };

        public SlotTask(ImageView slotImageView) {
            this.slotImageView = slotImageView;
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    Pair<String, Integer> result = getRandomImage();
                    currentImageId = result.second;

                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (result.first.startsWith("http")) {
                            // Load image from URL
                            Glide.with(slotImageView.getContext())
                                    .load(result.first)
                                    .into(slotImageView);
                        } else {
                            // Load local image
                            try {
                                int resId = Integer.parseInt(result.first);
                                slotImageView.setImageResource(resId);
                            } catch (NumberFormatException e) {
                                slotImageView.setImageResource(R.drawable.slotbar);
                            }
                        }
                    });

                    Thread.sleep(random.nextInt(300) + 200); // 200-500ms delay
                } catch (InterruptedException e) {
                    Log.e("SlotTask", "Thread interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
        }

        private Pair<String, Integer> getRandomImage() {
            // Try to get image from API first
            if (imageUrls.isEmpty()) {
                loadImagesFromApi();
            }

            // If we have URLs, use them
            if (!imageUrls.isEmpty()) {
                int randomIndex = random.nextInt(imageUrls.size());
                return new Pair<>(imageUrls.get(randomIndex), randomIndex);
            }

            // Fallback to local images
            return getLocalRandomImage();
        }

        private void loadImagesFromApi() {
            String apiUrl = "https://662e87fba7dda1fa378d337e.mockapi.io/api/v1/fruits";
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        imageUrls.add(jsonArray.getJSONObject(i).getString("url"));
                    }
                } else {
                    Log.e("SlotTask", "HTTP error: " + responseCode);
                }
            } catch (IOException | JSONException e) {
                Log.e("SlotTask", "Error loading images", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("SlotTask", "Error closing reader", e);
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        private Pair<String, Integer> getLocalRandomImage() {
            int randomIndex = random.nextInt(localImages.length);
            return new Pair<>(String.valueOf(localImages[randomIndex]), randomIndex);
        }

        public void stop() {
            isRunning = false;
        }

        public void reset() {
            isRunning = true;
        }

        public int getImageId() {
            return currentImageId;
        }
    }

    private static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }
}