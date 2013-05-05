package f_ingjava.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import f_ingjava.Callback;
import f_ingjava.Function;
import f_ingjava.Procedure;

public class Examples
{
    static final Function<String, String> retrieveWebpage = new Function<String, String>()
    {
        public String call(String url) throws Exception
        {
            URL serverAddress = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();
            connection.connect();

            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder bodyBuilder /* lol */= new StringBuilder();
            while ((line = responseReader.readLine()) != null)
            {
                bodyBuilder.append(line);
                bodyBuilder.append("\n");
            }
            connection.disconnect();

            return bodyBuilder.toString().trim();
        }
    };

    public static void getIP()
    {
        Log.i("getIP", "Retrieving your public IP address from http://icanhazip.com");

        /*
         * Synchronously: on newer versions of Android this will throw a
         * NetworkOnMainThreadException ...and it will freeze the UI regardless.
         */
        try
        {
            String result = retrieveWebpage.call("http://icanhazip.com");
            if (!result.matches("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$"))
                throw new Exception("Response is not a valid IP address: " + result);
            Log.i("getIP", "Your IP address is " + result);
        }
        catch (Exception e)
        {
            Log.e("getIP", "An error occured retrieving IP: " + e.toString());
        }

        /*
         * This way the network code is executed asyncronously, with almost no
         * extra code or effort.
         */
        retrieveWebpage.callAsync("http://icanhazip.com", new Callback<String>()
        {
            public void onResult(String result)
            {
                if (!result.matches("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$"))
                    this.onError(new Exception("Response is not a valid IP address: " + result));
                Log.i("getIP", "Your IP address is " + result);
            }

            public void onError(Exception error)
            {
                Log.e("getIP", "An error occured retrieving IP: " + error.toString());
            }
        });
    }

    // Note: variables and methods can share the same name, so you can create
    // method wrappers for Functions:
    static String retrieveWebpage(String url) throws Exception
    {
        return retrieveWebpage.call(url);
    }

    static void retrieveWebpage(String url, Callback<String> cb) throws Exception
    {
        retrieveWebpage.callAsync(url, cb);
    }

    /*
     * If you need more flexibility in arguments, the easiest solution is
     * probably to use a closure method: a method that creates an argumentless
     * Function (called a Procedure) which can use the method's arguments in
     * it's code.
     */
    static Procedure<String[]> retrieveWebpages(final String... urls)
    {
        return new Procedure<String[]>()
        {
            public String[] call() throws Exception
            {
                String[] results = new String[urls.length];
                for (int i = 0; i < urls.length; i++)
                    results[i] = retrieveWebpage(urls[i]);
                return results;
            }
        };
    }

    public static void checkMirror(final String url1, final String url2, final Callback<Boolean> cb)
    {
        Log.i("checkMirror", "Checking whether " + url1 + " is a mirror of " + url2);

        retrieveWebpages(url1, url2).callAsync(new Callback<String[]>()
        {
            public void onResult(String[] results)
            {
                boolean mirror = results[0].equals(results[1]);
                Log.i("checkMirror", url1 + " is " + (mirror ? "" : "not") + " a mirror of " + url2);
                if (cb != null)
                    cb.onResult(mirror);
            }

            public void onError(Exception error)
            {
                Log.e("checkMirror", "An error occured comparing pages: " + error.toString());
                if (cb != null)
                    cb.onError(error);
            }
        });
    }
}
