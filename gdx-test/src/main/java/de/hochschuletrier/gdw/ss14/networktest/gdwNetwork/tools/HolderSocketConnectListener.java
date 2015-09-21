package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools.*;

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
