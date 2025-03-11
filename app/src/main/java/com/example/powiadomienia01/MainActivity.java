package com.example.powiadomienia01;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    // Stała identyfikująca kanał powiadomień
    private static final String CHANNEL_ID = "notification_channel";
    // Identyfikator powiadomienia
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sprawdzenie i żądanie uprawnień do wysyłania powiadomień (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        // Tworzenie kanału powiadomień dla wersji Androida 8.0+
        createNotificationChannel();

        // Ustawienie nasłuchiwania przycisku do wywołania powiadomienia
        findViewById(R.id.button).setOnClickListener(v -> showNotification());
    }

    // Tworzenie kanału powiadomień (wymagane dla Androida 8.0+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    // Metoda wyświetlająca powiadomienie
    private void showNotification() {
        // Sprawdzenie uprawnień do wysyłania powiadomień (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // Jeśli brak uprawnień, nie wysyłamy powiadomienia
        }

        // Tworzenie intencji do otwarcia aplikacji po kliknięciu powiadomienia
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, DrugaAktywnosc.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Tworzenie powiadomienia
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Ikona powiadomienia
                .setContentTitle("Powiadomienie") // Tytuł powiadomienia
                .setContentText("To jest przykładowe powiadomienie.") // Treść powiadomienia
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priorytet powiadomienia
                .setContentIntent(pendingIntent) // Intencja uruchamiana po kliknięciu
                .setAutoCancel(true); // Automatyczne zamknięcie po kliknięciu

        // Wyświetlenie powiadomienia
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }
}
