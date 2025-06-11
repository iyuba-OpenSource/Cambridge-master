package tv.lycam.ijkplayer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.WindowManager;

import tv.lycam.ijkplayer.utils.logger.Logger;

/**
 * 资源文件ID获取工具
 */
public class ResourceUtils {
    private static final String TAG = ResourceUtils.class.getSimpleName();

    /**
     * 判断当前的线程是不是在主线程
     */
    public static boolean isRunInMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    private ResourceUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取资源id
     *
     * @param context   上下文
     * @param className 资源类名称如（layout,drawable,mipmap,id）
     * @param name      资源名称如（tab_layout.xml 名称为tab_layout ）
     * @return 资源id
     */
    public static int getResourceIdByName(Context context, String className, String name) {
        int id = 0;
        if (context == null) {
            return id;
        } else {
            String packageName = context.getPackageName();

            try {
                String var10 = packageName + ".R$" + className;
                Class desireClass = Class.forName(var10);
                if (desireClass != null) {
                    id = desireClass.getField(name).getInt(desireClass);
                }
            } catch (ClassNotFoundException var7) {
                Logger.e("ClassNotFoundException: class=%s fieldname=%s", className, name);
            } catch (IllegalArgumentException var8) {
                Logger.e("IllegalArgumentException: class=%s fieldname=%s", className, name);
            } catch (SecurityException var9) {
                Logger.e("SecurityException: class=%s fieldname=%s", className, name);
            } catch (IllegalAccessException var101) {
                Logger.e("IllegalAccessException: class=%s fieldname=%s", className, name);
            } catch (NoSuchFieldException var11) {
                Logger.e("NoSuchFieldException: class=%s fieldname=%s", className, name);
            }

            return id;
        }
    }

    /**
     * 获取属性集TypedArray
     *
     * @param context 上下文
     * @param attrs   attr属性集
     * @param name    资源名称
     * @return 属性集TypedArray
     */
    public static TypedArray getTypedArray(Context context, AttributeSet attrs, String name) {

        TypedArray typedArray = null;
        String className = "styleable";
        if (context == null) {
            return null;
        } else {
            String packageName = context.getPackageName();

            try {
                String var10 = packageName + ".R$" + className;
                Class desireClass = Class.forName(var10);
                int[] ids;
                if (desireClass != null) {
                    ids = (int[]) desireClass.getField(name).get(desireClass);
                    typedArray = context.obtainStyledAttributes(attrs, ids);
                }
            } catch (ClassNotFoundException var7) {
                Logger.e("ClassNotFoundException: class=%s fieldname=%s", className, name);
            } catch (IllegalArgumentException var8) {
                Logger.e("IllegalArgumentException: class=%s fieldname=%s", className, name);
            } catch (SecurityException var9) {
                Logger.e("SecurityException: class=%s fieldname=%s", className, name);
            } catch (IllegalAccessException var101) {
                Logger.e("IllegalAccessException: class=%s fieldname=%s", className, name);
            } catch (NoSuchFieldException var11) {
                Logger.e("NoSuchFieldException: class=%s fieldname=%s", className, name);
            }

            return typedArray;
        }
    }

    /**
     * dip转换px
     */
    public static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度(像素)
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

}
