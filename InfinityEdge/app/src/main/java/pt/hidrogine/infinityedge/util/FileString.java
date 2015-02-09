package pt.hidrogine.infinityedge.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileString {


    public static String read(Context context, int resource) {

        try {

            InputStream inputStream = context.getResources().openRawResource(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            inputStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean exists(Context ctx, String path) {
        try {
            ctx.openFileInput(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String read(Context ctx, String path) {
        FileInputStream inputStream;
        StringBuilder sb = new StringBuilder();

        try {
            inputStream = ctx.openFileInput(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);

            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
