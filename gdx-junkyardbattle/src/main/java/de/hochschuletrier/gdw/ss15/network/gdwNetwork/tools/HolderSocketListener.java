package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

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
    	}
    }
}
