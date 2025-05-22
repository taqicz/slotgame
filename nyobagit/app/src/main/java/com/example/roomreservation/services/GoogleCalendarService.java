package com.example.roomreservation.services;

import android.content.Context;
import android.util.Log;

import com.example.roomreservation.db.models.Reservation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Collections;
import java.util.TimeZone;

public class GoogleCalendarService {
    private static final String TAG = "GoogleCalendarService";
    private Context context;
    private Calendar mService;

    public GoogleCalendarService(Context context) {
        this.context = context;
        initializeCalendarAPI();
    }

    private void initializeCalendarAPI() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    context, 
                    Collections.singleton(CalendarScopes.CALENDAR)
            );
            credential.setSelectedAccount(account.getAccount());

            mService = new Calendar.Builder(
                    AndroidHttp.newCompatibleTransport(), 
                    GsonFactory.getDefaultInstance(), 
                    credential)
                    .setApplicationName("Room Reservation")
                    .build();
        }
    }

    public void addReservationToCalendar(Reservation reservation) {
        if (mService == null) {
            Log.e(TAG, "Google Calendar service not initialized");
            return;
        }

        try {
            Event event = new Event()
                    .setSummary("Room Reservation: " + reservation.getRoomName())
                    .setDescription("Room reservation details");

            // Convert reservation times to EventDateTime
            EventDateTime startTime = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(reservation.getStartTime()))
                    .setTimeZone(TimeZone.getDefault().getID());

            EventDateTime endTime = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(reservation.getEndTime()))
                    .setTimeZone(TimeZone.getDefault().getID());

            event.setStart(startTime);
            event.setEnd(endTime);

            // Insert event
            String calendarId = "primary";
            Event createdEvent = mService.events().insert(calendarId, event).execute();
            Log.d(TAG, "Event created: " + createdEvent.getHtmlLink());

        } catch (IOException e) {
            Log.e(TAG, "Error adding event to calendar", e);
        }
    }

    public void deleteReservationFromCalendar(String eventId) {
        if (mService == null) {
            Log.e(TAG, "Google Calendar service not initialized");
            return;
        }

        try {
            mService.events().delete("primary", eventId).execute();
            Log.d(TAG, "Event deleted successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error deleting event from calendar", e);
        }
    }
}
