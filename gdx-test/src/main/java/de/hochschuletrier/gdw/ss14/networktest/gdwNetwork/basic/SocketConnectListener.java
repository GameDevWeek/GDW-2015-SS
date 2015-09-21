package gdwNetwork.basic;

import gdwNetwork.enums.ConnectStatus;

public interface SocketConnectListener
{
	void loginFinished(ConnectStatus status);
}
