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

    //ˇ請求server端連至db回傳專業項目之所有名稱
    public static final List<String> takeProjectNameList() {
        List<String> projectNameList = new ArrayList<>();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("master", "projectName");
        MasterAsyncTask masterAsyncTask = new MasterAsyncTask(jsonObject.toString());
        try {
            projectNameList = masterAsyncTask.execute(Common.URL + "/MasterServlet").get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectNameList;
    }
}
