package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Sendable {

    void pack(DataOutputStream dataOutput) throws IOException;
    void unpack(DataInputStream input) throws IOException;

}
