/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Amjad Afzaal
 */
public class Client
{
    private Socket client;
    private String sendData, recvData;
    private BufferedReader br;
    private PrintWriter pw;
    private int flag;
    
    public Client()
    {
        this.flag = 0;
        this.sendData = "";
        this.recvData = "";
    }
    
    public void on() 
    {
        try 
        {
            this.client = new Socket ("localhost", 5000);
            this.br = new BufferedReader(new InputStreamReader (this.client.getInputStream()));
            this.pw = new PrintWriter(this.client.getOutputStream(), true);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        flag = 1;
    }
    
    public void off()
    {
        try 
        {
            this.br.close();
            this.client.close();
            this.pw.close();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
