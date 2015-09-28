package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;

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
    	}
    }
}