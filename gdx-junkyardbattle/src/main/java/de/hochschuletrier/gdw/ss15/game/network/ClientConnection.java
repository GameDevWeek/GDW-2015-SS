package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketDisconnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import de.hochschuletrier.gdw.ss15.states.GameplayState;
import de.hochschuletrier.gdw.ss15.states.MainMenuState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class ClientConnection implements SendPacketClientEvent.Listener,
                                         SocketConnectListener,
                                         SocketDisconnectListener,
                                         SocketListener {
    private Clientsocket clientSocket = null;

    public ClientConnection()
    {
    }

    public void init()
    {
        Main.getInstance().console.register(clientCommand);
    }

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);
    ConsoleCmd clientCommand = new ConsoleCmd("client", 0, "Connected und disconncted fom server", 1) {
        @Override
        public void execute(List<String> list){
            String info = list.get(1);
            if(info.equals("connect"))
            {
                if(list.size()>2){//zusätzlich ip
                    logger.info("Try to connection to "+list.get(2));
                    connect(list.get(2),12345);
                }
                else {logger.info("Try to connection to localhost");
                   connect("localhost", 12345);
                }
            }
            else if(info.equals("disconnect"))
            {
                disconnect();
            }
            else
            {
                logger.error(info+" falsches parameter für command client");
            }
        }
    };


    public void update()
    {
        if(clientSocket!=null) {
            //clientSocket.justCallDisconnectHandler();
            clientSocket.callListeners();
            /*if(!clientSocket.isConnected() && !clientSocket.isByConnect())
            {
                logger.warn("Lost connection to server");
                clientSocket.close();
                clientSocket=null;
            }*/
        }
    }

    public Clientsocket getSocket()
    {
        return clientSocket;
    }

    public boolean connect(String ip,int port)
    {
        if(clientSocket!=null && clientSocket.isConnected())
        {
            logger.warn("Client bereits verbunden");
            return false;
        }
        if(clientSocket!=null)
        {
            clientSocket.close();
            clientSocket=null;
        }
        clientSocket = new Clientsocket(ip,port,true);
        // Listener registrieren
        clientSocket.registerConnectListner(this);
        clientSocket.registerDisconnectListener(this);
        clientSocket.registerListener(this);

        clientSocket.connect();
        SendPacketClientEvent.registerListener(this);
        return true;
    }

    public void disconnect()
    {
        if(clientSocket==null)
        {
            //logger.error("Clint war nicht verbunden");
        }
        else
        {
            clientSocket.close();
            clientSocket=null;
            logger.info("Connection beendet");

            //final MainMenuState mainMenuState = new MainMenuState(Main.getInstance().getAssetManager(),1);
            //Main.getInstance().addPersistentState(mainMenuState);
            //Main.getInstance().changeState(Main.getInstance().get, null, null);
           // MainMenuState state = (MainMenuState) Main.getInstance().getPersistentState(MainMenuState.class);


           // MenuPageEnterIP page= new MenuPageEnterIP( state.getSkin(), state.getMenumanager(), "menu_bg");
            //state.getMenumanager().addLayer(page);
            //state.getMenumanager().pushPage(page);

            MainMenuState state = (MainMenuState) Main.getInstance().getPersistentState(MainMenuState.class);
            state.getMenumanager().popPage();
            Main.getInstance().changeState(Main.getInstance().getPersistentState(MainMenuState.class));
        }
    }


    public void onSendSClientPacket(Packet pack,boolean save)
    {
        //System.out.print("Packet send");
        if(clientSocket!=null && clientSocket.isConnected())
        {
            if(save == true)
            {
                clientSocket.sendPacketSave(pack);
            }
            else
            {
                clientSocket.sendPacketUnsave(pack);
            }
        }
    }

    @Override
    public void loginFinished(ConnectStatus status) {

        ConnectTryFinishEvent.emit(status == ConnectStatus.Succes);
    }

    @Override
    public void socketDisconnected() {
        //System.out.print("called from socket");
        DisconnectEvent.emit();
        disconnect();
    }

    @Override
    public void receivedPacket(Packet packet, boolean receivedSave) {

        if(packet.getPacketId() == PacketIds.Simple.getValue())
        {
            SimplePacket pack = (SimplePacket)packet;
            if(pack.m_SimplePacketId == SimplePacket.SimplePacketId.StartGame.getValue())
            {
                if (!Main.getInstance().isTransitioning()) {
                     Game game = new Game();
                    game.init(Main.getInstance().getAssetManager(),(int)pack.m_Moredata);
                    Main.getInstance().changeState(new GameplayState(Main.getInstance().getAssetManager(), game), new SplitHorizontalTransition(500), null);
                }
            }
            else if(pack.m_SimplePacketId == SimplePacket.SimplePacketId.StopGame.getValue()) {
                //stop game
                //to lobby
                //MainMenuState state = (MainMenuState) Main.getInstance().getPersistentState(MainMenuState.class);
                Main.getInstance().changeState(Main.getInstance().getPersistentState(MainMenuState.class));
            }
        }

        DoNotTouchPacketEvent.emit(packet);
    }
}
