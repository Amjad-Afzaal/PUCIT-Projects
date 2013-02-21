/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Amjad Afzaal
 */
public class Server 
{
    private ServerSocket sock;
    private Socket connected;
    private String sendData, recvData;
    private BufferedReader br;
    private PrintWriter pw;
    private int flag;
    
    public Server()
    {
        this.flag = 0;
        this.sendData = "";
        this.recvData = "";
    }
    
    public void on ()
    {
        JOptionPane.showMessageDialog(null, "Waiting for Client . . .");
        System.out.println("\n\n\n\nWaiting for Client . . .");
        try 
        {
            this.sock = new ServerSocket(5000);
            this.connected = this.sock.accept();
            JOptionPane.showMessageDialog(null, "Connected Client with IP = "
                    + this.connected.getInetAddress() + "\nPort = " +
                    this.connected.getPort());
            
            System.out.println("\n\n\n\n\n\nConnected Client with IP = "
                    + this.connected.getInetAddress() + "\nPort = " +
                    this.connected.getPort());
            
            this.br = new BufferedReader(new InputStreamReader (this.connected.getInputStream()));
            this.pw = new PrintWriter(this.connected.getOutputStream(), true);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        
        flag = 1;
    }
    
    public void off() 
    {
        try 
        {
            this.br.close();
            this.connected.close();
            this.pw.close();
            this.sock.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.recvData = "";
        this.sendData = "";
        flag = 0;
    }
    
    public void send(String name)
    {
        this.sendData = name;
        this.pw.println(this.sendData);
    }
    
    public String recv()
    {
        try 
        {
            this.recvData = br.readLine();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        return this.recvData;
    }
    
    public boolean isOn()
    {
        if(flag == 0)
            return false;
        return true;
    }
    
}
