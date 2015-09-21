package gdwNetwork.tools;

import gdwNetwork.basic.SocketConnectListener;
import gdwNetwork.enums.ConnectStatus;

import java.lang.ref.WeakReference;

public class HolderSocketConnectListener<T> extends WeakReference<T>
{
    private final SocketConnectListener action;
    
    public HolderSocketConnectListener(SocketConnectListener action, T target)
    {
        super(target);
        this.action=action;
    }
    
    public void LoginFinished(ConnectStatus status)
    {
        T t=get();
        if(t!=null)
        {
        	action.loginFinished(status);
        }
        else
    	{
        	System.out.println("target collected");
    	}
    }
}
