package nom.cp101.master.master.Main;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;


/*
* ScaleGestureDetector.OnScaleGestureListener-onScale(), onScaleBegin(), onScaleEnd()
* */
public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    private final String TAG = "ZoomImageView";
    //默認縮放比例
    private float initScale = 1.0f;
    //最大比例
    private final float maxScale = 15.0f;
    //點擊放大比例
    private final float midScale = 15.0f;
    /*
    * 使用提供的MotionEvents檢測縮放轉換手勢,此類僅應與通過觸摸報告的MotionEvents一起使用
    * 需實做ScaleGestureDetector.OnScaleGestureListener
    * 用於處理縮放手勢
    */
    ScaleGestureDetector scaleGestureDetector = null;
    /*
    * 使用提供的MotionEvents檢測各種手勢和事件
    * onTouch()雖然也可以,但對於較複雜之手勢等較不易判斷,所以在此使用此類
    * 此類為android系統提供的手勢處理,已將常用之手勢包裝成方法便於使用
    */
    private GestureDetector gestureDetector;
    //用於防止縮放中部繼續執行,避免點擊繼續觸發
    private boolean isAutoScaling;
    /*
    * android對圖像處理的class
    * Matrix類包含一個3*3矩陣,用於轉換坐標
    * Matrix提供Translate(平移),Scale(縮放),Rotate(旋轉), Skew(偏斜)的操作,事實上2*2矩陣已夠用,不過為方便計算,引入齊次座標
     */
    Matrix matrix = new Matrix();
    //因Matrix為3*3,用於處理9個其次座標的值
    float[] matrixValue = new float[9];


    //偵測指頭數量
    private int lastPointCount;
    //判斷是否可拖曳
    private boolean isCanDrag;
    //紀錄點擊移動前的座標xy
    private float latX;
    private float latY;
    //存放系統默認觸發事件所需之最短距離
    private int touchSlop;
    //用於判斷可否上下拖動
    private boolean isCanTopAndBottom;
    //用於判斷可否左右拖動
    private boolean isCanLeftAndRight;

    boolean once = true;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //意指可使用Matrix來設置其ImageView,使用其中setImageMatrix()來實現矩陣
        setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(context, this);

        //因需做縮放後處理,則交由onTouchListener內做對應事件
        this.setOnTouchListener(this);

        /*
        *在我們認為用戶正在滾動之前,觸摸可以移動的像素距離
        * 意指獲得系統觸發事件之最短距離,用於判斷移動UI控件
        *若有套用viewPager,可使用之距離來判斷是否須置換之中的fragment
         */
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        /*
        * SimpleOnGestureListener- 當想使用系統預設手勢的子集時,所需產生之物件,並改寫欲使用之手勢內容
        * */
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            //因要實現點擊使其圖片做放大縮小之效果,則須改寫其onDoubleTap()
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //防止點擊時,繼續下縮
                if (isAutoScaling) {
                    return true;
                }
                float x = e.getX();
                float y = e.getY();
                //自訂getScale()取得當前縮放值,當縮放比例小於最小比例時,則開始放大
                getScale();
                if (getScale() < midScale) {
                    //將判斷縮小設為true,以防繼續縮小至無
                    isAutoScaling = true;
                    //ImageView內的method,使其ImageView放大移動時,延遲10ms
                    postDelayed(new AutoScaleRunble(midScale, x, y), 10);

                } else {
                    //若當前放大比例超出,則退回初始比例
                    isAutoScaling = true;
                    postDelayed(new AutoScaleRunble(initScale, x, y), 10);
                }
                return true;
            }
        });
    }

    //自訂mothed, 取得當前圖片之縮放比例
    private float getScale() {
        //取得matrix內的9個陣列值
        matrix.getValues(matrixValue);
        //取出圖寬之縮放比例
        return matrixValue[Matrix.MSCALE_X];
    }

    //處理圖片縮放
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //取得縮放前尺寸
        float scale = getScale();
        //取得當前縮放中尺寸
        float scaleFactor = detector.getScaleFactor();
        //圖為空則不做處理
        if (getDrawable() == null) {
            return true;
        }
        //若縮放前的尺寸小於最大尺寸和目前放大的尺寸超過1.0f時,或是縮放前尺寸大於默認尺寸和目前縮小的尺寸
        if ((scale < maxScale && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            //若比初始化圖小,依照等比例進行縮小
            if (scale * scaleFactor < initScale) {
                scaleFactor = initScale / scale;
                Log.d(TAG, "比初始化值還小,依等比例縮小:" + scaleFactor);
            }
            //若要比初始化圖在大,依照等比例進行放大
            if (scale * scaleFactor > maxScale) {
                scaleFactor = maxScale / scale;
                Log.d(TAG, "比最大值還大,依等比例放大:" + scaleFactor);
            }
            //設置縮放比例
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            //解決縮放時,可能會發生的位置變化
            checkBorderAndCenterWhenScale();
            //設置縮放
            setImageMatrix(matrix);
        }
        /*
        *返回值表示事件已被處理,detector則會重置縮放事件
        *如果未被處理,detector會繼續季換,直到修改getScaleFactor()返回值至結束
        *因此用於縮放比例至一定程度才進行處理
        */
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //在做縮放時,回傳值需為true,使其detector處理縮放事件,不然不會觸發onScale()
        return true;
    }

    //縮放結束
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    //自訂mothed, 獲取矩形縮放後之寬高大小
    private RectF getMatrixRectF() {
        Matrix mMatrix = matrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();

        if (drawable != null) {
            //left,right為左邊矩陣的座標點, top,bottom為右邊矩陣座標點,將left與top座標設為0即表示寬高等比例
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            //將RectF取得的座標點映射到matrix中,即可繪出矩陣
            mMatrix.mapRect(rectF);
        }
        return rectF;
    }

    //解決縮放時,周圍空白的情形
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        //取得ZoomImageView空件的寬高
        int width = getWidth();
        int height = getHeight();
        //若矩形寬大於螢幕寬,則控制範圍
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                //取得座標留白距離
                deltaX = -rectF.left;
                Log.d(TAG, "圖片左側對螢幕左側的距離:" + deltaX);
            }
            if (rectF.right < width) {
                //當螢幕寬佔據整個大小,得到右邊留白的寬度
                deltaX = width - rectF.right;
                Log.d(TAG, "圖片右側對螢幕右側的距離:" + deltaX);
            }
        }
        //若矩形高大於螢幕高,則控制範圍
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                //取得上方留白寬度
                deltaY = -rectF.top;
                Log.d(TAG, "圖片上側對螢幕上側的距離:" + deltaY);
            }
            if (rectF.bottom < height) {
                //取得下方留白寬度
                deltaY = height - rectF.bottom;
                Log.d(TAG, "圖片下側對螢幕下側的距離:" + deltaY);
            }
        }
        //當圖片寬小於螢幕寬,使其居中
        if (rectF.width() < width) {
            //矩形中心距離螢幕中心點計算
            deltaX = width * 0.5f - rectF.right + 0.5f * rectF.width();
        }
        //當圖片高小於螢幕高,使其居中
        if (rectF.height() < height) {
            //矩形中心距離螢幕中心點計算
            deltaY = height * 0.5f - rectF.bottom + 0.5f * rectF.height();
        }
        //將矩陣移至xy位置
        matrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //因在GestureDetector以改寫其onDoubleTap(),在此則不做處理
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        //將處理傳回給ScaleGestureDetector
        scaleGestureDetector.onTouchEvent(event);

        //取得當前指頭數量
        int pointCount = event.getPointerCount();
        //因為多指觸控,則不可再ACTION_DOWN內改寫其down方法,須額外處理
        //ACTION_DOWN本身只提供單指事件處理
        //取得各點xy
        float x = 0;
        float y = 0;
        for (int i = 0; i < pointCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        //取其平均值
        x /= pointCount;
        y /= pointCount;

        //當前偵測之多指數量若在圖片移動時又增加的話,其保持原先指頭數量
        //即為移動時不在偵測額外指頭來做計算
        if (lastPointCount != pointCount) {
            //將判斷可否列為拖曳對象設為false
            isCanDrag = false;
            //然後將點擊移動前之xy座標紀錄,帶魚下次移動時繼續使用
            latX = x;
            latY = y;
        }

        lastPointCount = pointCount;
        //呼叫自訂mothed,獲取矩陣的區域範圍
        RectF rectF = getMatrixRectF();

        switch (event.getAction()) {
            //當手指按下時,偵測矩陣的寬高其一大於螢幕寬高,則將移動的事件交由外層viewPager來做移動處理
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    //若要防止父控件(viewPager)來補獲在fragment內做的事件處理,Ex:矩陣在縮放滑動時,防止觸發父控件之事件處理,則需如下呼叫
                    //此為防止父控件捕獲touch事件處理
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            //偵測手指移動時的事件處理
            case MotionEvent.ACTION_MOVE:
                //同上,超出則交由父控件
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                //移動的距離
                float dx = x - latX;
                float dy = y - latY;

                //因可能手指變化而導致無法拖曳,需再次檢查是否可滑動
                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (isCanDrag) {
                    //圖片存在的話,將可上下左右移動設為true
                    if (getDrawable() != null) {
                        isCanTopAndBottom = isCanLeftAndRight = true;
                        //若矩形寬或高其一小於螢幕寬高,則不可滑動
                        if (rectF.width() < getWidth()) {
                            isCanLeftAndRight = false;
                            dx = 0;
                        }
                        if (rectF.height() < getHeight()) {
                            isCanTopAndBottom = false;
                            dy = 0;
                        }
                        //平移到dx,dy位置
                        matrix.postTranslate(dx, dy);
                        //解決放大時,邊出現留白現象
                        checkBorderAndCenterWhenTranslate();
                        setImageMatrix(matrix);
                    }
                }
                //紀錄點的位置,續用於下次移動時使用
                latX = x;
                latY = y;
                break;

            //當手指抬起或取消事件時(即為無動作),則將記錄指數的參數設為0
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointCount = 0;
                break;
        }
        return true;
    }

    //判斷是否可滑動
    private boolean isMoveAction(float dx, float dy) {
        //平方根算法
        return Math.sqrt(dx * dx + dy * dy) > touchSlop;
    }

    //自訂method,解決滑動時邊界留白問題
    private void checkBorderAndCenterWhenTranslate() {
        //取得當前矩形寬高等數據
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        //可以上下拖動且距離上方留白,系統依照座標為負值
        if (rectF.top > 0 && isCanTopAndBottom) {
            deltaY = -rectF.top;
        }
        //可以上下拖動且距離下方留白,系統依照座標為正值
        if (rectF.bottom < height && isCanTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        //可以左右拖動且距離左方留白,系統依照座標為負值
        if (rectF.left > 0 && isCanLeftAndRight) {
            deltaX = -rectF.left;
        }
        //可以左右拖動且距離右方留白,系統依照座標為正值
        if (rectF.right < width && isCanLeftAndRight) {
            deltaX = width - rectF.right;
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //因在onCreate()中View.getWidth和View.getHeight無法獲得所需寬高,因View的佈局在於onResume()後才完成
        //取得View的寬高-方法之一,實作OnGlobalLayoutListener
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //因在onAttachedToWindow()時,有對ZoomImageView下偵聽器,所以在這個view離開螢幕時,需做卸除的動作
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    //圖片初始化其大小,因view控件的寬高要在onResume()後才得以取得,所以這邊需調用onAttachedToWindow
    @Override
    public void onGlobalLayout() {
        if (!once) {
            return;
        }
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        //取得ZoomImageView的寬高
        int width = getWidth();
        int height = getHeight();
        //取得圖片(矩形)寬高
        int imgWidth = drawable.getIntrinsicWidth();
        int imgHeight = drawable.getIntrinsicHeight();

        float scale = 5.0f;
        //若圖片的寬大於ZoomImageView的寬,則縮至螢幕寬
        if (imgWidth > width && imgHeight <= height) {
            scale = (float) width / imgWidth;
        }
        //若圖片的高大於ZoomImageView的高,則縮至螢幕高
        if (imgHeight > height && imgWidth <= width) {
            scale = (float) height / imgHeight;
        }
        //若寬高皆大於螢幕寬高,則依等比例縮放
        if (imgWidth > width && imgHeight > height) {
            scale = Math.min((float) imgWidth / width, (float) imgHeight / height);
        }
        initScale = scale;
        //將圖片移至螢幕中心
        matrix.postTranslate((width - imgWidth) / 2, (height - imgHeight) / 2);
        matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
        setImageMatrix(matrix);
        once = false;
    }

    private class AutoScaleRunble implements Runnable {
        //縮放目標值
        private float targetScale;
        private float x;
        private float y;
        //存入BIGGER或SMALL的值
        private float tempScale;
        private float BIGGER = 1.10f;
        private float SMALLER = 0.90f;

        //View.postDelay()做點擊時判斷縮放
        public AutoScaleRunble(float enlargeScale, Object p1, Object p2) {
            this.targetScale = enlargeScale;
            this.x = (float) p1;
            this.y = (float) p2;
            //點擊時放大與縮小的判斷,並傳入參數給予tempScale
            if (getScale() < targetScale) {
                tempScale = BIGGER;
            }
            if (getScale() > targetScale) {
                tempScale = SMALLER;
            }
        }

        @Override
        public void run() {
            //執行縮放
            matrix.postScale(tempScale, tempScale, x, y);
            //解決縮放時,邊界留白的問題
            checkBorderAndCenterWhenScale();
            setImageMatrix(matrix);
            //取得當前縮放比例
            float currentScale = getScale();
            //若當前縮放比例是正在放大和放大的尺寸小於縮放目標值,或是縮放比例是正在縮小和縮小的尺寸大於縮放目標值
            if ((tempScale > 1.0f && currentScale < targetScale) || tempScale < 1.0f && currentScale > targetScale) {
                //即做縮放處理事件
                postDelayed(this, 10);
            } else {
                /*
                *代表無法進行縮放,可能圖片的放大已到最大或是梭表已到最小
                * 所以這邊使用targetScale/currentScale來或許縮放比
                * 用於縮放至初始值或是最大值
                * */
                float sclae = targetScale / currentScale;
                matrix.postScale(sclae, sclae, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(matrix);
                //然而重置
                isAutoScaling = false;
            }
        }
    }
}