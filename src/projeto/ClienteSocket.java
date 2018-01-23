package projeto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClienteSocket {
    public static void main(String[] agrs){
        
        /* Try Catch para tratamento de erros*/
        try{
            final Socket cliente = new Socket("10.11.1.206",9999); /* Conectando o cliente ao servidor */
            
            new Thread(){
                @Override
                public void run() {
                    
                    try {
                       /* O comando abaixo lê as mensagens que chegam ao cliente */
                       BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream())); 
                
                       while(true){
                           String mensagem = leitor.readLine();
                           System.out.println("" + mensagem);
                       }
                    } catch (IOException e) {
                        System.out.println("Impossivel ler a mensagem");
                        e.printStackTrace();
                    }

                }
                
            }.start(); /* Start () para iniciar a Thread */
            
            /* Escrevendo mensagens */
            PrintWriter escritor = new PrintWriter(cliente.getOutputStream(),true);
            BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));
            
            while(true){
                String mensagemTerminal = leitorTerminal.readLine();
                escritor.println(mensagemTerminal);
                if(mensagemTerminal.equalsIgnoreCase("::SAIR")){
                    System.exit(0);
                }
            
            }
        
        }catch (UnknownHostException e){
            System.out.println("O endereço passado é inválido!");
            e.printStackTrace();
        }catch(IOException e){
            System.out.println("O Servidor pode estar fora do AR");
            e.printStackTrace();
        } 
    
    }
}
