package projeto;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorClientes extends Thread {

	private Cliente cliente;
	

	/*
	 * O comando abaixo cria uma variavel static onde tem o controle de todos os
	 * clientes no chat Map = Padrão chave valor
	 */
	private static final Map<String, Cliente> clientes = new HashMap<String, Cliente>();

	/*
	 * Metodo gerenciador de clientes Gerenciamento de Clientes em Paralelo
	 */
	public GerenciadorClientes(Socket socket) {
		cliente = new Cliente("", socket);
		start();
		/* Startando a Thread */
	}

	public GerenciadorClientes(Socket socket, List<Cliente> clientesConectados) {
		this.cliente.setSocket(socket);
	}

	/**
	 * Método que é responsavel pelas conversas entre clientes
	 */
	@Override
	public void run() {
		try {
			
			cliente.getWriter().write("Use o comando \nPara cadastrar: #nome:numero\n Para logar: $nome:numero");
			String comando = cliente.getReader().readLine();
			if (comando.startsWith("#")) {
				try {
					String[] split = comando.substring(1).split(":");
					if (clientes.containsKey(split[1])) {
						cliente.getWriter().write(split[1] + " já está cadastrado");
						cliente.getWriter().write("0");
						cliente.getWriter().flush();
						cliente.getWriter().close();
						return;
					} else {
						cliente.setNome(split[0]);
						cliente.setNumero(split[1]);
						clientes.put(cliente.getNumero(), cliente);
						System.out.println(cliente.getNome() + " cadastrou-se com IP:"
								+ cliente.getSocket().getInetAddress().toString().substring(1));
						cliente.getWriter().write("Bem Vindo " + cliente.getNome());
					}

				} catch (Exception e) {
					cliente.getWriter().write("Comando errado!");
					return;
				}
			} else if (comando.startsWith("$")) {
				String[] split = comando.substring(1).split(":");
				if (clientes.containsKey(split[1]) && !clientes.get(split[1]).getSocket().isClosed()) {
					cliente.getWriter().write(split[1] + " já está logado!");
					cliente.getWriter().write("0");
					cliente.getWriter().flush();
					cliente.getWriter().close();
					return;
				} else if (clientes.containsKey(split[1]) && clientes.get(split[1]).getSocket().isClosed()) {
					clientes.get(split[1]).setSocket(cliente.getSocket());
					cliente = clientes.get(split[1]);
					System.out.println(cliente.getNome() + " logou-se com IP:"
							+ cliente.getSocket().getInetAddress().toString().substring(1));
					cliente.getWriter().write("Bem Vindo " + cliente.getNome());
				}

			} else {
				cliente.getWriter().write("Use o comando \nPara cadastrar: #nome:numero\n Para logar: $nome:numero");
				return;
			}

			String mensagem = "";
			while (true) {
				
				mensagem = cliente.getReader().readLine();
				if (mensagem.equals("?")) {
					cliente.getWriter().write(
							"Lista de Comandos: \n\n"
//						  + "#nome:numero \tCadastra\n"
//						  + "$nome:numero \tLogar\n"
						  + "&nome:numero \tAdicionar contato\n"
						  + "@nome \t Conversar com contato\n"
						  + "! \t\t Sair da conversa\n"
//						  + ": \n\n Sair do chat\n"
						  + "? \t\t Ajuda\n");
				}else if(mensagem.startsWith("&")) {
					try {
						String nome = mensagem.substring(1).split(":")[0],
							   numero = mensagem.substring(1).split(":")[1];
						if(clientes.containsKey(numero)) {
							cliente.addContato(new Cliente(nome, numero, null));
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else if(mensagem.startsWith("@")) {
					String nome = mensagem.substring(1);
					String numero = cliente.temContato(nome);
					if(numero!=null) {
						Cliente destinatario = clientes.get(numero);
						if(destinatario.getSocket().isClosed()) {
							cliente.getWriter().write("Contato não está online!");
						}else {
							cliente.getWriter().write("Digite a mensagem:");
							while(true) {
								String m = cliente.getReader().readLine();
								if(m.equals("?")) {
									cliente.getWriter().write(
											"Lista de Comandos: \n\n"
										  + "! \t\t Sair da conversa\n"
										  + "? \t\t Ajuda\n");
								}else if(m.equals("!")) {
									break;
								}else {
									cliente.enviarMensagem(m,destinatario);
									
								}
							}
						}
					}else {
						cliente.getWriter().write("Contato não encontrado!");
					}
				}
				else if (mensagem.equals(":")) {
					clientes.get(cliente.getNumero()).close();
					cliente.close();
					break;
				}
			}

//			/* O codigo a seguir recebe todas as mensagens do cliente */
//			while (true) {
//				mensagem = cliente.getReader().readLine();
//				/* O bloco de codigo abaixo fecha a conexao do cliente */
//				if (mensagem.equalsIgnoreCase("::SAIR")) {
//					this.cliente.close();
//				} else if (mensagem.toLowerCase().startsWith("@")) {
//					String nome_Destinatario = mensagem.substring(1, mensagem.length());
//					System.out.println("Enviando para " + nome_Destinatario);
//					GerenciadorClientes destinatario = clientes.get(nome_Destinatario);
//
//					if (destinatario == null) {
//						cliente.getWriter().println("O usuario informado nao está logado.");
//					} else {
//						cliente.getWriter().println("Digite uma mensagem para " + destinatario.getNomeCliente());
//						destinatario.getEscritor().println(cliente.getNome() + " Disse: " + cliente.getReader().readLine());
//					}
//				} else {
//					cliente.getWriter().println(cliente.getNome() + ", Você disse: " + mensagem);
//				}
//
//			}
		} catch (IOException e) {
			System.err.println("O cliente fechou a conexão");
			e.printStackTrace();
		}

	}

}
