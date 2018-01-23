package projeto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorSocket {
    
    public static void main(String[] ars) throws IOException{
        
        /* Try catch para tratamento de erros*/
        try {
            System.out.println("Iniciando o Servidor");
            ServerSocket servidor = new ServerSocket(9999); /* Instanciando a Classe ServerSocket*/
            System.out.println("O servidor foi inciado");
            
            /* Preparando conexão com os clientes */
            while(true){
                Socket cliente = servidor.accept();
                new GerenciadorClientes(cliente);
            }
        } catch (IOException e) {
            System.err.println("A porta está fechada");
            e.printStackTrace();
        }
    
    }
    
}
