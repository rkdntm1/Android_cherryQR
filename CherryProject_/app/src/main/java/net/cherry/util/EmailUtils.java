package net.cherry.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;

import net.cherry.BuildConfig;

public class EmailUtils {
    public static void sendEmailToAdmin(Context context, String title, String[] receivers) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, title);
        email.putExtra(Intent.EXTRA_EMAIL, receivers);
        email.putExtra(Intent.EXTRA_TEXT, String.format("App version: %s\nAndroid(SDK) : %d" +
                "(%s)\nDevice : \n내용 : ", BuildConfig.VERSION_NAME, Build.VERSION.SDK_INT, Build.VERSION.RELEASE));
        email.setType("message/rfc822");
        context.startActivity(email);
    }
}
