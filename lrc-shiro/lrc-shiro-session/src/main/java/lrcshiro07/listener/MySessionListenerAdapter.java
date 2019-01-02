package lrcshiro07.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

public class MySessionListenerAdapter extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {
        System.out.println("会话创建：" + session.getId());
    }
}
