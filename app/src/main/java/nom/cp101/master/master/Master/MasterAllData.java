package nom.cp101.master.master.Master;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nom.cp101.master.master.Main.Common;

/**
 * Created by yujie on 2018/5/1.
 */

public class MasterAllData {

    public static final List<String> getProjectNameList(){
        List<String> projectNameList=new ArrayList<>();

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("master","projectName");
        MasterAsyncTask masterAsyncTask=new MasterAsyncTask(jsonObject.toString());

        try {
            projectNameList=masterAsyncTask.execute(Common.URL + "/MasterServlet").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return projectNameList;
    }
}
