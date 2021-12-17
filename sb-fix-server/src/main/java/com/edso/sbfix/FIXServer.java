package com.edso.sbfix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quickfix.*;

import java.io.FileNotFoundException;

@SpringBootApplication
@Slf4j
public class FIXServer implements Application {

    @Override
    public void onCreate(SessionID sessionID) {
    }

    @Override
    public void onLogon(SessionID sessionID) {
    }

    @Override
    public void onLogout(SessionID sessionID) {
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("FromApp: " + message);
    }

    public static void main(String[] args) throws ConfigError, FileNotFoundException, InterruptedException, SessionNotFound {
        SessionSettings settings = new SessionSettings("D:/Test/sb-fix-server/acceptor.cfg");

        Application application = new FIXServer();
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true);
        MessageFactory messageFactory = new DefaultMessageFactory();

        Acceptor initiator = new SocketAcceptor(application, messageStoreFactory, settings, logFactory, messageFactory);
        initiator.start();
    }

}
