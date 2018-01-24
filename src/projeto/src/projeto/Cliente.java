package projeto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Cliente {

	private String nome;
	private String numero;
	private Socket socket;
	private Map<String, Cliente> listaDeContatos ;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public Cliente(String nome, Socket socket) {
		this.nome = nome;
		this.socket = socket;
	}

	
	public Cliente(String nome, String numero, Socket socket) {
		this.nome = nome;
		this.numero = numero;
		this.socket = socket;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		criaIO();
	}

	public void addContato(Cliente contato) {
		if(listaDeContatos==null) {
			listaDeContatos = new HashMap<String,Cliente>();
		}
		listaDeContatos.put(contato.getNome(),contato);
	}
	
	public String temContato(String nome) {
		if(listaDeContatos.containsKey(nome))
			return listaDeContatos.get(nome).getNumero();
		else
			return null;
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	public BufferedReader getReader() {
		return reader;
	}
	
	public void close() throws IOException {
		socket.close();
	}
	
	private void criaIO() {
		try {
			if(!socket.isClosed()) {
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public synchronized void enviarMensagem(String m, Cliente destinatario) {
		String label = "";
		for (Cliente c : destinatario.listaDeContatos.values()) {
			if(c.getNumero().equals(numero)) {
				label = c.getNome();
			}
		}
		destinatario.getWriter().write("@"+label+":"+m);
	}
	
	public String getListaDeContatos() {
		String label = "*\n";
		for (Cliente c : listaDeContatos.values()) {
			if(c.getNumero().equals(numero)) {
				label += "|"+c.getNome()+":"+c.getNumero()+"\n";
			}
		}
		return label;
	}
}
