package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools.*;

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