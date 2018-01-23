package projeto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorClientes extends Thread {

    private Cliente cliente;
    private BufferedReader leitor;
    private PrintWriter escritor;

    /* O comando abaixo cria uma variavel static onde tem o controle de todos 
    *  os clientes no chat
    *  Map = Padrão chave valor 
     */
    private static final Map<String, GerenciadorClientes> clientes = new HashMap<String, GerenciadorClientes>();

    /* Metodo gerenciador de clientes 
    * Gerenciamento de Clientes em Paralelo
     */
    public GerenciadorClientes(Socket socket) {
        this.cliente.setSocket(socket);
        start();
        /* Startando a Thread */
    }

    /**
     * Método que é responsavel pelas conversas entre clientes
     */
    @Override
    public void run() {
        try {
            /* O leitor captura a mensagem do cliente */
            leitor = cliente.getReader();

            /* O codigo a seguir prepara a leitura 
           * das mensagens enviadas pelo cliente 
             */
            escritor = cliente.getWriter();
            escritor.println("Escreva seu nome: ");
            String mensagem = leitor.readLine();
            cliente.setNome(mensagem.toLowerCase());
            escritor.println("Olá, seja bem vindo! " + cliente.getNome());
            clientes.put(cliente.getNome(), this);
            /* Toda vez que o cliente logar, ele será adicionado ao map */


 /* O codigo a seguir recebe todas as mensagens do cliente */
            while (true) {
                mensagem = leitor.readLine();
                /* O bloco de codigo abaixo fecha a conexao do cliente */
                if (mensagem.equalsIgnoreCase("::SAIR")) {
                    this.cliente.close();
                } else if (mensagem.toLowerCase().startsWith("@")) {
                    String nome_Destinatario = mensagem.substring(1, mensagem.length());
                    System.out.println("Enviando para " + nome_Destinatario);
                    GerenciadorClientes destinatario = clientes.get(nome_Destinatario);

                    if (destinatario == null) {
                        escritor.println("O usuario informado nao está logado.");
                    } else {
                        escritor.println("Digite uma mensagem para " + destinatario.getNomeCliente());
                        destinatario.getEscritor().println(cliente.getNome() + " Disse: " + leitor.readLine());
                    }
                } else {
                    escritor.println(cliente.getNome()+ ", Você disse: " + mensagem);
                }

            }
        } catch (IOException e) {
            System.err.println("O cliente fechou a conexão");
            e.printStackTrace();
        }

    }

    public PrintWriter getEscritor() {
        return escritor;
    }

    public String getNomeCliente() {
        return cliente.getNome();
    }

}
