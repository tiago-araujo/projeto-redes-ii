package projeto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

	String nome;
	Socket socket;

	public Cliente(String nome, Socket socket) {
		this.nome = nome;
		this.socket = socket;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public PrintWriter getWriter() throws IOException{
		return new PrintWriter(socket.getOutputStream(), true);
	}
	public BufferedReader getReader() throws IOException{
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void close() throws IOException {
		socket.close();
	}
}
