package nom.cp101.master.master.Account.MyCourse.CLASS;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/4/26.
 */

public class MyCourseStudentManageAdapter extends RecyclerView.Adapter<MyCourseStudentManageAdapter.myViewHolder> {

    List<Apply> applies;
    Context context;
    Activity activity;
    LayoutInflater inflater;
    View frView;

    public MyCourseStudentManageAdapter(List<Apply> applies, Context context, Activity activity, LayoutInflater inflater, View frView) {
        this.applies = applies;
        this.context = context;
        this.activity = activity;
        this.inflater = inflater;
        this.frView = frView;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView studentImage;
        TextView studentName;
        TextView applyStatus;
        public myViewHolder(final View itemView) {
            super(itemView);
            studentImage = itemView.findViewById(R.id.studentImage);
            studentName = itemView.findViewById(R.id.studentName);
            applyStatus = itemView.findViewById(R.id.tvStudentStatus);
            ImageView studentStatusClick = itemView.findViewById(R.id.studentStatusClick);
            final TextView tvStudentStatus = itemView.findViewById(R.id.tvStudentStatus);
            studentStatusClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(),view);
                    popupMenu.inflate(R.menu.account_course_student_status_menu);
                    final MenuItem successItem = popupMenu.getMenu().findItem(R.id.menu_student_success);
                    final MenuItem unpayItem = popupMenu.getMenu().findItem(R.id.menu_student_unpay);
                    if(tvStudentStatus.getText().equals("報名成功")){
                        successItem.setVisible(false);
                        unpayItem.setVisible(false);
                    }
                    final Apply apply = applies.get(getAdapterPosition());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.menu_student_success:
                                    int result1 = updateApplyStatus(activity,apply,3);
                                    if(result1!= 0){
                                        tvStudentStatus.setText("報名成功");
                                        int start = getAdapterPosition();
                                        int end = getAdapterPosition() + (applies.size() - 1);
                                        //重新整理applies位置
                                        Apply newApply = apply;
                                        applies.remove(apply);
                                        applies.add(newApply);
                                        notifyItemMoved(start,end);
                                    }
                                    break;
                                case R.id.menu_student_unpay:
                                    int result2 = updateApplyStatus(activity,apply,2);
                                    if(result2 != 0){
                                        tvStudentStatus.setText("尚未繳費");
                                    }
                                    break;
                                case R.id.menu_student_delete:
                                    final AlertDialog.Builder addAlertBuilder= new AlertDialog.Builder(activity);
                                    View alertView = inflater.inflate(R.layout.account_dialog,null);
                                    TextView title = alertView.findViewById(R.id.alert_title);
                                    TextView content = alertView.findViewById(R.id.alert_content);
                                    Button okBtn = (Button)alertView.findViewById(R.id.alert_ok);
                                    Button cancelBtn = (Button)alertView.findViewById(R.id.alert_cancel);
                                    title.setText("WARNING!");
                                    content.setText("Are you sure you want to delete this student?");

                                    addAlertBuilder.setView(alertView);
                                    final AlertDialog alertDialog = addAlertBuilder.create();
                                    alertDialog.show();

                                    okBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            int result3 = deleteApply(activity,apply.getApply_id());
                                            if(result3 != 0){
                                                applies.remove(getAdapterPosition());
                                                notifyItemRemoved(getAdapterPosition());
                                                TextView studentCourseApplied = frView.findViewById(R.id.student_course_apply);
                                                studentCourseApplied.setText("總共 "+applies.size()+" 人");
                                            }
                                            alertDialog.dismiss();
                                        }
                                    });
                                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alertDialog.dismiss();
                                        }
                                    });

                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View itemView = layoutInflater.inflate(R.layout.account_course_student_item,parent,false);
        return new myViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        final Apply apply = applies.get(position);
        holder.studentImage.setImageResource(R.drawable.account_bulldog);
        holder.studentName.setText(apply.getUser_name());
        holder.applyStatus.setText(apply.getApply_status_name());
    }
    @Override
    public int getItemCount() {
        return applies.size();
    }

    public int updateApplyStatus(Activity activity, Apply apply, int status_id){
        String TAG = "MyCourseStudentManage";
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/applyServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "update");
            jsonObject.addProperty("apply_id",apply.getApply_id());
            jsonObject.addProperty("status", status_id);
            int count = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                count = Integer.valueOf(result);
                Log.d(TAG,apply.getApply_id()+"更改成功");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return count;
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(activity, R.string.msg_NoNetwork);
            return 0;
        }
    }

    public int deleteApply(Activity activity, int apply_id){
        String TAG = "MyCourseStudentManage";
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/applyServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "deleteByApplyId");
            jsonObject.addProperty("apply_id",apply_id);
            int count = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                count = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return count;
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(activity, R.string.msg_NoNetwork);
            return 0;
        }
    }
}
