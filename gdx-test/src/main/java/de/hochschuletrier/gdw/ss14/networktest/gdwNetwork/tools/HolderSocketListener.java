package gdwNetwork.tools;

import gdwNetwork.basic.SocketListener;
import gdwNetwork.data.Packet;

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
