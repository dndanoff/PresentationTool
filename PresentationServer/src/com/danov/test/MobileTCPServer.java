import com.danov.tcp.PresentationServer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author x
 */

public class MobileTCPServer {

    public static void main(String[] args) {
        int port = 19999;
        PresentationServer server = new PresentationServer.Builder(port).build();
        System.out.println("MultipleSocketServer starting....");
        Thread thread= new Thread(server);
    }
}
