package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

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
    	}
    }
}
