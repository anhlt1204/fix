package com.edso.sbfix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class FIXClient implements Application {

    private static volatile SessionID sessionID;

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("OnCreate");
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.info("OnLogon");
        FIXClient.sessionID = sessionID;
    }

    @Override
    public void onLogout(SessionID sessionID) {
        log.info("OnLogout");
        FIXClient.sessionID = null;
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        log.info("ToAdmin");
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("FromAdmin");
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        log.info("ToApp: " + message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("FromApp");
    }

    public static void main(String[] args) throws ConfigError, FileNotFoundException, InterruptedException, SessionNotFound {
        SessionSettings settings = new SessionSettings("D:/Test/sb-fix-client/initiator.cfg");

        Application application = new FIXClient();
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true);
        MessageFactory messageFactory = new DefaultMessageFactory();

        Initiator initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory, messageFactory);
        initiator.start();

        while (sessionID == null) {
            Thread.sleep(1000);
        }

        final String orderId = "342";
        NewOrderSingle newOrder = new NewOrderSingle(new ClOrdID(orderId), new HandlInst('1'), new Symbol("6758.T"),
                new Side(Side.BUY), new TransactTime(LocalDateTime.now()), new OrdType(OrdType.MARKET));

        Session.sendToTarget(newOrder, sessionID);
        Thread.sleep(5000);
    }

}
