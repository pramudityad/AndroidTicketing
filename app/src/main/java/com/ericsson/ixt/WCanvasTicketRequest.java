package com.ericsson.ixt;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WCanvasTicketRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://android.eid-tools.com/ixt_20_UAT/closeTicketRp.php";
    private Map<String, String> params;


    public WCanvasTicketRequest(String email,String requestor,String ticketid,String mid,String evactivity,String projid,String custid,String compid,String r_id,String cuid,String usercomment,String ar_report_string, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email",email); // string is key which we will use on server side
        params.put("requestor",requestor); // string is key which we will use on server side
        params.put("ticketid",ticketid); // array is key which we will use on server side
        params.put("mid",mid); // array is key which we will use on server side
        params.put("evactivity",evactivity); // string is key which we will use on server side
        params.put("projid",projid); // array is key which we will use on server side
        params.put("custid",custid); // array is key which we will use on server side
        params.put("compid",compid); // array is key which we will use on server side
        params.put("r_id",r_id); // array is key which we will use on server side
        params.put("cuid",cuid); // array is key which we will use on server side
        params.put("usercomment",usercomment); // array is key which we will use on server side
        params.put("ar_report_string",ar_report_string); // array is key which we will use on server side

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
