package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools.*;

import java.lang.ref.WeakReference;

public class HolderSocketListener<T> extends WeakReference<T>
{
    private final SocketListener action;
    
    public HolderSocketListener(SocketListener action, T target)
    {
        super(target);
        this.action=action;
    }
    
    public void ReceivedPacket(Packet packet,boolean receivedSave)
    {
        T t=get();
        if(t!=null)
        {
        	action.receivedPacket(packet,receivedSave);
        }
        else
    	{
        	System.out.println("target collected");
    	}
    }
}
