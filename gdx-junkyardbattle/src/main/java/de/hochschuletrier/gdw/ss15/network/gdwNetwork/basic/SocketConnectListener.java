package de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

public interface SocketConnectListener
{
	void loginFinished(ConnectStatus status);
}
