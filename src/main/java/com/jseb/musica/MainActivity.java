package com.jseb.musica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;

import java.util.Arrays;

public class MainActivity extends Activity {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		findViewById(R.id.splash_image).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Session.getActiveSession().isOpened())updateView();
				else connectToFacebook(null);
			}
		});

		connectToFacebook(savedInstanceState);
    }

	public void connectToFacebook(Bundle savedInstanceState) {
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			if (session == null) session = new Session(this);

			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) session.openForRead(new Session.OpenRequest(this).setPermissions(Arrays.asList("user_groups", "user_about_me")).setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK).setCallback(statusCallback));
		} else if (session.getState().equals(SessionState.CLOSED)) {
			Session.openActiveSession(this, true, statusCallback);
		} else if (session.getState().equals(SessionState.CLOSED_LOGIN_FAILED)) {
			// show user error
		}

		updateView();
	}

	public void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			startActivity(new Intent(this, StreamActivity.class));
		} else {
			if (!session.getState().equals(SessionState.OPENING) && !session.getState().equals(SessionState.CLOSED)) session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
		}
	}

	public static void logout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) session.closeAndClearTokenInformation();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			updateView();
		}
	}
}
