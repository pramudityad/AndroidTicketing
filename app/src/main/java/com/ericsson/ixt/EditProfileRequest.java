package com.ericsson.ixt;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditProfileRequest extends StringRequest {
    private static final String EDITPROFIL_URL = "https://android.eid-tools.com/ixt_20_UAT/editprofil.php";
    private Map<String, String> params;

    public EditProfileRequest(String phonenum,String  email,Response.Listener<String> listener) {
        super(Method.POST, EDITPROFIL_URL, listener, null);
        params = new HashMap<>();
        params.put("Phonenum", phonenum);
        params.put("email", email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
