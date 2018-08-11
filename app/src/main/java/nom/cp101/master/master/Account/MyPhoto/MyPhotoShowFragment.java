package nom.cp101.master.master.Account.MyPhoto;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.ExperienceArticle.ExperienceComment;
import nom.cp101.master.master.ExperienceArticle.ExperienceCommentAdapter;
import nom.cp101.master.master.ExperienceArticle.Experience;
import nom.cp101.master.master.ExperienceArticle.ExperienceContentCommentAdapter;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ZoomImageView;
import nom.cp101.master.master.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static nom.cp101.master.master.Main.Common.getTimeAsName;
import static nom.cp101.master.master.Main.Common.showToast;

public class MyPhotoShowFragment extends Fragment implements View.OnClickListener {
    private String postId;
    private Context context;

    private ZoomImageView zivPhoto;
    private ImageView ivClear, ivSave;
    private TextView tvContent;
    private ShineButton sbLaud;
    private TextView tvLaud, tvMessage;
    private DialogPlus dialogPlus;
    private RecyclerView rvComment;
    private LinearLayoutManager layoutManager;
    private EditText etSend;
    private Experience experienceData = null;
    private ExperienceContentCommentAdapter experienceContentCommentAdapter = null;

    private List<ExperienceComment> commentList;
    private final String COLOR_PRIMARY = "#64aae7";
    private final int PAGE_COUNT = 10;
    private Bitmap bitmap = null;

    private LruCache<String, Bitmap> lruCache;
    private final String TAG = "MyPhotoShowFragment";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int lastVisibleItemPosition = 0;
    //收起 extendStr
    //展開 collapseStr
    private SpannableString extendStr;
    private SpannableString collapseStr;
    public static final int REQUEST_READ_WRITE = 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_photo_show_item, container, false);
        findViews(view);
        context = getActivity();
        initMemoryCache();

        if (getArguments() != null) {
            //設定顯示畫面
            postId = getArguments().getString("postId");
            experienceData = ConnectionServer.getExperience(Common.getUserName(getActivity()), Integer.parseInt(postId));
            int commentCount = ConnectionServer.getExperienceCommentCount(experienceData.getPostId());

            if (lruCache.get(postId) != null) {
                bitmap = lruCache.get(postId);
                Log.d(TAG, "load by cache");

            } else {
                bitmap = ConnectionServer.getPhotoByPostId(postId);
                if (bitmap != null) {
                    lruCache.put(postId, bitmap);
                    Log.d(TAG, "load by server: " + postId);
                }
            }
            zivPhoto.setImageBitmap(bitmap);

            //自訂mothed,實現文過長展合收縮
            getLastIndexForLimit(context
                    , tvContent
                    , experienceData.getPostContent()
                    , (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9)
                    , 1);

            sbLaud.setChecked(experienceData.isPostLike());
            tvLaud.setTextColor(sbLaud.isChecked() ? Color.parseColor(COLOR_PRIMARY) : Color.WHITE);
            tvMessage.setText(commentCount + getResources().getString(R.string.comment_count));
        }

        ivClear.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        sbLaud.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (Common.checkUserName(context, Common.getUserName(context))) {
                    ConnectionServer.getExperiencePostLikeRefresh(Common.getUserName(context), Integer.parseInt(postId), checked);

                    if (checked) {
                        tvLaud.setTextColor(Color.parseColor(COLOR_PRIMARY));
                    } else {
                        tvLaud.setTextColor(Color.WHITE);
                    }
                } else {
                    sbLaud.setChecked(false);
                }
            }
        });
        return view;
    }

    private void findViews(View view) {
        zivPhoto = (ZoomImageView) view.findViewById(R.id.zivPhoto);
        ivClear = (ImageView) view.findViewById(R.id.ivClear);
        ivSave = (ImageView) view.findViewById(R.id.ivSave);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        sbLaud = (ShineButton) view.findViewById(R.id.sbLaud);
        tvLaud = (TextView) view.findViewById(R.id.tvLaud);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClear:
                getActivity().finish();
                break;

            case R.id.ivSave:
                //儲存
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    savePhoto();
                }

                Common.askPermissionByFragment((Activity) context,
                        MyPhotoShowFragment.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_READ_WRITE);
                break;

            //comment_dialog
            case R.id.tvMessage:
                commentList = ConnectionServer.getExperienceComment(experienceData.getPostId());
                dialogPlusMessage();
                break;

            //comment_finish
            case R.id.tvFinish:
                dialogPlus.onBackPressed(dialogPlus);
                break;

            //comment_send
            case R.id.btnSend:
                if (Common.checkUserName(context, Common.getUserName(context))) {
                    String etStr = etSend.getText().toString().trim();
                    if (etStr != null && !etStr.equals("")) {
                        int insertOK = ConnectionServer.setExperienceComment(Common.getUserName(context), experienceData.getPostId(), etStr);
                        if (insertOK == 1) {
                            showToast(context, getResources().getString(R.string.comment_success));
                            commentList = ConnectionServer.getExperienceComment(experienceData.getPostId());

                            experienceContentCommentAdapter = new ExperienceContentCommentAdapter(context,
                                    getFragmentManager(),
                                    commentList,
                                    false,
                                    1,
                                    experienceData.getPostContent());
                            rvComment.setAdapter(experienceContentCommentAdapter);
                        }

                        rvComment.scrollToPosition(experienceContentCommentAdapter.getItemCount() - 1);
                        //close_keyboard
                        InputMethodManager imm = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        //refresh_comment_count
                        int commentCount = ConnectionServer.getExperienceCommentCount(experienceData.getPostId());
                        tvMessage.setText(commentCount + " " + getResources().getString(R.string.comment_count));

                    } else {
                        showToast(context, getResources().getString(R.string.comment_empty));
                    }
                    etSend.setText("");
                }
                break;

            case R.id.tvContent:
                if (v.isSelected()) {
                    tvContent.setText(collapseStr);
                    tvContent.setSelected(false);
                } else {
                    tvContent.setText(extendStr);
                    tvContent.setSelected(true);
                }
                break;

            default:
                break;
        }
    }

    private void savePhoto() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //getFilesDir()-內部儲存(internal)該區隨著app卸載而刪除
            //getExternalFilesDir-外部儲存(external)該區隨著app卸載而刪
            //getExternalStorageDirectory-外部儲存(external)該區不會隨著app卸載而刪除,公有外存
            //getExternalStoragePublicDirectory()-同上,sdk<8則不支援
            //getExternalFilesDir()-外部儲存(external)該區隨著app卸載而刪除,私有外存,可訪問但無價值
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, getTimeAsName() + "_picture.jpg");

            FileOutputStream fos = null;
            boolean isSuccess = false;
            try {
                fos = new FileOutputStream(file);
                isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (isSuccess) {
                    Uri uri = Uri.fromFile(file);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    showToast(context, context.getResources().getString(R.string.save_photo));

                } else {
                    showToast(context, context.getResources().getString(R.string.un_save_photo));
                }
            }
        }
    }

    //comment_message_dialog
    private void dialogPlusMessage() {
        dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.account_photo_show_comment_item))
                .setMargin(50, 30, 50, 50)
                .create();

        TextView tvFinish = dialogPlus.getHolderView().findViewById(R.id.tvFinish);
        rvComment = dialogPlus.getHolderView().findViewById(R.id.rvComment);
        etSend = dialogPlus.getHolderView().findViewById(R.id.etSend);
        Button btnSend = dialogPlus.getHolderView().findViewById(R.id.btnSend);

        experienceContentCommentAdapter = new ExperienceContentCommentAdapter(context,
                getFragmentManager(),
                loadCommentDatas(0, PAGE_COUNT),
                loadCommentDatas(0, PAGE_COUNT).size() > 0 ? true : false,
                1,
                experienceData.getPostContent());
        layoutManager = new LinearLayoutManager(context);
        rvComment.setLayoutManager(layoutManager);
        rvComment.setAdapter(experienceContentCommentAdapter);
        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //若滑已置底
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //若加載條沒有隱藏&&最後position的位置為總數量-1
                    //position位置為總數量-1!!
                    if (experienceContentCommentAdapter.isHiddenHint() == false && lastVisibleItemPosition + 1 == experienceContentCommentAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(experienceContentCommentAdapter.getLastPosition(), experienceContentCommentAdapter.getLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    //若加載條隱藏&&最後position的位置為總數量-2,因加載條被隱藏,表示與顯示數量少1
                    if (experienceContentCommentAdapter.isHiddenHint() == true && lastVisibleItemPosition + 2 == experienceContentCommentAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView(experienceContentCommentAdapter.getLastPosition(), experienceContentCommentAdapter.getLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //取得最後position
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
        dialogPlus.show();

        tvFinish.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    //自訂method,分頁加載
    private List<ExperienceComment> loadCommentDatas(final int firstIndex, final int lastIndex) {
        List<ExperienceComment> loadList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < commentList.size()) {
                loadList.add(commentList.get(i));
            }
        }
        return loadList;
    }

    //LruCache機制
    public void initMemoryCache() {
        //緩存記憶體空間
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        //override存於緩存中的圖片大小
        lruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    //自訂method,加載數據
    private void updateRecyclerView(int fromIndex, int toIndex) {
        // 获取从fromIndex到toIndex的数据
        List<ExperienceComment> updateList = loadCommentDatas(fromIndex, toIndex);
        if (updateList.size() > 0) {
            experienceContentCommentAdapter.updateList(updateList, true);

        } else {
            experienceContentCommentAdapter.updateList(null, false);
        }
    }

    //取得TextView中最長字串的字元index
    @TargetApi(Build.VERSION_CODES.M)
    private void getLastIndexForLimit(Context context, final TextView tv, String content, int width, int maxLine) {
        //繪製文字的class
        TextPaint textPaint = tv.getPaint();
        //用於TextView拆行處理的class
        StaticLayout staticLayout = StaticLayout.Builder.obtain(content, 0, content.length(), textPaint, width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0, 1)
                .setMaxLines(1)
                .build();

        if (staticLayout.getLineCount() > maxLine) {
//            String contentStr = content + "  " + context.getResources().getString(R.string.extend);
//
//            //Spannable.SPAN_EXCLUSIVE_EXCLUSIVE-參數用於改變文字樣式,(前後不包括)
//            collapseStr = new SpannableString(contentStr);
//            collapseStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff"))
//                    , contentStr.length() - 2
//                    , contentStr.length()
//                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            int index = staticLayout.getLineStart(maxLine) - 1;
            String subString = content.substring(0, index - 2) + "..." + context.getResources().getString(R.string.go_on_msg);
            extendStr = new SpannableString(subString);
            extendStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff"))
                    , subString.length() - 2
                    , subString.length()
                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(extendStr);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentList = ConnectionServer.getExperienceComment(experienceData.getPostId());
                    dialogPlusMessage();
                }
            });
            tv.setSelected(true);

        } else {
            tv.setText(content);
            tv.setSelected(false);
            tv.setOnClickListener(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_WRITE:
                List<Integer> integerList = new ArrayList<>();
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        integerList.add(grantResult);
                    }
                }
                if (grantResults.length > 0 && integerList.size() == 0) {
                    savePhoto();

                } else {
                    //shouldShowRequestPermissionRationale第一次會返回true,因使用者未拒絕使用權限
                    // 接下來如果再次使用功能出現權限dialog,勾選了不在顯示(Don,t ask again!)時,則此method會返回false
                    // 此時再次要使用功能時,Permission dialog則不在提示,需額外自訂dialog引導使用者開啟權限的介面
                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            && !ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        new AlertDialog.Builder(context)
                                .setView(R.layout.master_prefession_dialog_item)
                                .setPositiveButton(context.getResources().getString(R.string.setProfession), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //前往管理權限介面
                                        Intent localIntent = new Intent();
                                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if (Build.VERSION.SDK_INT >= 9) {
                                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                                            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                                        } else if (Build.VERSION.SDK_INT <= 8) {
                                            localIntent.setAction(Intent.ACTION_VIEW);
                                            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                                        }
                                        startActivity(localIntent);
                                    }
                                })
                                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (resultCode) {
                case REQUEST_READ_WRITE:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        //getFilesDir()-內部儲存(internal)該區隨著app卸載而刪除
                        //getExternalFilesDir-外部儲存(external)該區隨著app卸載而刪
                        //getExternalStorageDirectory-外部儲存(external)該區不會隨著app卸載而刪除,公有外存
                        //getExternalStoragePublicDirectory()-同上,sdk<8則不支援
                        //getExternalFilesDir()-外部儲存(external)該區隨著app卸載而刪除,私有外存,可訪問但無價值
                        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                        if (!file.exists()) {
                            file.mkdir();
                        }
                        file = new File(file, getTimeAsName() + "_picture.jpg");

                        FileOutputStream fos = null;
                        boolean isSuccess = false;
                        try {
                            fos = new FileOutputStream(file);
                            isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
                            fos.flush();
                            fos.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (isSuccess) {
                                Uri uri = Uri.fromFile(file);
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                                showToast(context, context.getResources().getString(R.string.save_photo));

                            } else {
                                showToast(context, context.getResources().getString(R.string.un_save_photo));
                            }
                        }
                    }
                    break;
            }
        }
    }
}
