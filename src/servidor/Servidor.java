package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**************************************************************************************************
 * Autor: Fernando Rodrigo Pinheiro de Sousa
 * 
 * Classe que cria uma nova thread para cada nova solicitacao de conexao e excuta a aplicação Web.
 * ************************************************************************************************/

public class Servidor {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		
		ServerSocket servidor = new ServerSocket(9090);
        ExecutorService pool = Executors.newFixedThreadPool(20);

        while (true) {
            
            pool.execute(new ThreadConexao(servidor.accept()));
        }

	}

}
