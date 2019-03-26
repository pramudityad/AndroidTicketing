package com.ericsson.ixt;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewTicketRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://uat.eid-tools.com/p_ticket/open_ticket/android";
    private Map<String, String> params;


    public NewTicketRequest(String user_comment, String ar_mid, String event_type, String ar_target_grup_string, String account, String project, String email, String mkdir, String cu_id, String sl_ack, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_comment",user_comment); // string is key which we will use on server side
        params.put("key2open",ar_mid); // array is key which we will use on server side
        params.put("event_type",event_type); // string is key which we will use on server side
        params.put("target_user",ar_target_grup_string); // array is key which we will use on server side
        params.put("account_id",account); // array is key which we will use on server side
        params.put("project_id",project); // array is key which we will use on server side
        params.put("email",email); // array is key which we will use on server side
        params.put("mkdir",mkdir); // array is key which we will use on server side
        params.put("cuid",cu_id); // array is key which we will use on server side
        params.put("slyesornot",sl_ack); // array is key which we will use on server side

        //params.put("picname", picname);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
