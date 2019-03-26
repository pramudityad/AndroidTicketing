package com.ericsson.ixt;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericsson on 3/15/2017.
 */

public class ProfileRequest extends StringRequest {
    private static final String PROFIL_URL = "https://android.eid-tools.com/ixt_20_UAT/profil.php";
    private Map<String, String> params;

    public ProfileRequest(String email, Response.Listener<String> listener) {
        super(Method.POST, PROFIL_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
