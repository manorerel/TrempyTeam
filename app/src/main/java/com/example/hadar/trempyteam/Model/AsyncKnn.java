//package com.example.hadar.trempyteam.Model;
//
//import android.os.AsyncTask;
//
//import org.json.JSONArray;
//
//import java.util.List;
//
///**
// * Created by Hadar on 11-Sep-17.
// */
//
//public class AsyncKnn extends AsyncTask<List<Tremp>, Integer, List<Tremp>> {
//
//    @Override
//    protected List<Tremp> doInBackground(List<Tremp>... params) {
//        final JSONArray trempsJson_to_return = new JSONArray(save_server_response);
//
//        for (int i = 0; i < params[0].length(); i++) {
//
//            int j = i;
//            checkIfRelevant(trempsJson, trempsJson_to_return, j, tremps);
//        }
//        return null;
//    }
//
//    protected void onPostExcecute(Tremp result){
////        (result);
//    }
//
//}
