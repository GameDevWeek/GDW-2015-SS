package gdwNetwork.tools;

import gdwNetwork.basic.SocketDisconnectListener;

import java.lang.ref.WeakReference;

public class HolderSocketDisconnectListener<T> extends WeakReference<T>
{
    private final SocketDisconnectListener action;
    
    public HolderSocketDisconnectListener(SocketDisconnectListener action, T target)
    {
        super(target);
        this.action=action;
    }
    
    public void SocketDisconnected()
    {
        T t=get();
        if(t!=null)
        {
        	action.socketDisconnected();
        }
        else
    	{
        	System.out.println("target collected");
    	}
    }
}