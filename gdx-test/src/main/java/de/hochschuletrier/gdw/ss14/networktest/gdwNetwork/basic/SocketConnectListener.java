package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic;

import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.enums.ConnectStatus;

public interface SocketConnectListener
{
	void loginFinished(ConnectStatus status);
}
