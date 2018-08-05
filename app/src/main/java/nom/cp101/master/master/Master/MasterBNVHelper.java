package nom.cp101.master.master.Master;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import java.lang.reflect.Field;

//改變其BottomNavigation移位與顯示模式
public class MasterBNVHelper {
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView bottomNavigationView) {

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        try {
            //取得BottomNavigationMenuView定義的屬性,因爲斯有變數,則調用getDeclaredField()
            //private boolean mShiftingMode = true;
            //mShiftingMode主要控制個菜單間的寬度
            Field shiftingMode = bottomNavigationMenuView.getClass().getDeclaredField("mShiftingMode");

            //欲訪問private成員,消除異常需添加
            shiftingMode.setAccessible(true);
            //改變其屬性值
            shiftingMode.setBoolean(bottomNavigationMenuView, false);
            shiftingMode.setAccessible(false);
            //for迴圈,處理Menu內的Item
            for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(i);
                //將其移位模式設為false
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}


