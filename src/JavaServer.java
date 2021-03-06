/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sonle
 */
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import thriftDemo.*;

public class JavaServer {

    public static APIsHandler handler;

    public static APIs.Processor processor;

    public static void main(String[] args) {
        try {
            handler = new APIsHandler();
            processor = new APIs.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };
            Runnable secure = new Runnable() {
                public void run() {
                    secure(processor);
                }
            };

            new Thread(simple).start();
            //TODO temporary commented out
            //new Thread(secure).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void secure(APIs.Processor processor) {
        try {
            /*
             * Use TSSLTransportParameters to setup the required SSL parameters. In this example
             * we are setting the keystore and the keystore password. Other things like algorithms,
             * cipher suites, client auth etc can be set. 
             */
            TSSLTransportParameters params = new TSSLTransportParameters();
            // The Keystore contains the private key
            params.setKeyStore("../../lib/java/test/.keystore", "thrift", null, null);

            /*
             * Use any of the TSSLTransportFactory to get a server transport with the appropriate
             * SSL configuration. You can use the default settings if properties are set in the command line.
             * Ex: -Djavax.net.ssl.keyStore=.keystore and -Djavax.net.ssl.keyStorePassword=thrift
             * 
             * Note: You need not explicitly call open(). The underlying server socket is bound on return
             * from the factory class. 
             */
            TServerTransport serverTransport = TSSLTransportFactory.getServerSocket(9091, 0, null, params);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            // Use this for a multi threaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            System.out.println("Starting the secure server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void simple(APIs.Processor processor) {
        try 
        {
//            // use the TSimpleServer
//            TServerTransport serverTransport = new TServerSocket(9090);
//            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

//            // use the TNonBlockingServer
//            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9090);
//            TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).processor(processor));
            
//            // use the THsHaServer
//            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9090);
//            TServer server = new THsHaServer(new THsHaServer.Args(serverTransport).processor(processor));
            
//            // use the TThreadPoolServer
//            TServerTransport serverTransport = new TServerSocket(9090);
//            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            // use the TThreadedSelectorServer
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9090);
            TServer server = new TThreadedSelectorServer(new TThreadedSelectorServer.Args(serverTransport).processor(processor));
            
            System.out.println("Starting the server...");
            server.serve();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
