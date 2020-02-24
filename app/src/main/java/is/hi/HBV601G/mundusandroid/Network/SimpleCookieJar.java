package is.hi.HBV601G.mundusandroid.Network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class SimpleCookieJar implements CookieJar, Serializable {

    private List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies =  cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (cookies != null) {
            return cookies;
        }
        return new ArrayList<Cookie>();

    }



}