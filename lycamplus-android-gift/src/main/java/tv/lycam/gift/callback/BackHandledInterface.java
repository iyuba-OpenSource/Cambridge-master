package tv.lycam.gift.callback;


import androidx.fragment.app.Fragment;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 判断是否Fragment是否消耗回退键
 */
public interface BackHandledInterface<T> {

    Queue<Fragment> fragmentQuene = new LinkedBlockingQueue<>();

    void setSelectedFragment(T selectedFragment);
}  