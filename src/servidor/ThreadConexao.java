package servidor;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/**************************************************************************************************
 * Autor: Fernando Rodrigo Pinheiro de Sousa
 * 
 * Classe processa a criação de requisicao a partir do InputStream do cliente;
 * Abre o arquivo pelo caminho conseguido da requisição;
 * Cria a resposta passando o conteúdo do arquivo e o cabeçalho da resposta no formato pardão HTTP;
 * cria o canal de resposta utilizando o outputStream.
 * ************************************************************************************************/

public class ThreadConexao implements Runnable {
	
	private final Socket socket;
    private boolean conectado;

    public ThreadConexao(Socket socket) {
        this.socket = socket;
    }
    
	@Override
	public void run() {

		conectado = true;
		
		while (conectado) {
			try {
				
				RequisicaoHTTP requisicao = RequisicaoHTTP.lerRequisicao(socket.getInputStream());
				
				if (requisicao.isManterViva()) {
					socket.setKeepAlive(true);
					socket.setSoTimeout(requisicao.getTempoLimite());
				} else {					
					socket.setSoTimeout(300);
				}
								
				if (requisicao.getRecurso().equals("/")) {
					requisicao.setRecurso("calculadora/index.html");
				}				
				
				File arquivo = new File(requisicao.getRecurso());
				
				RespostaHTTP resposta;
				
				if (arquivo.exists()) {
					resposta = new RespostaHTTP(requisicao.getProtocolo(), 200, "OK");
				} else {
					
					resposta = new RespostaHTTP(requisicao.getProtocolo(), 404, "Not Found");
					arquivo = new File("calculadora/recursos/404.html");
				}
				
				
				resposta.setConteudoResposta(Files.readAllBytes(arquivo.toPath()));				
				
				String dataFormatada = formatarDataGMT(new Date());								
				
				resposta.setCabecalho("Location", "http://localhost:9090/");
				resposta.setCabecalho("Date", dataFormatada);
				resposta.setCabecalho("Server", "MeuServidor/1.0");
				resposta.setCabecalho("Content-Type", "text/html");
				resposta.setCabecalho("Content-Length", resposta.getTamanhoResposta());
				 
				resposta.setSaida(socket.getOutputStream());
				resposta.enviar();
				
			} catch (IOException ex) {
				
				if (ex instanceof SocketTimeoutException) {
					try {
						conectado = false;
						socket.close();
					} catch (IOException ex1) {
						ex1.printStackTrace();
					}
				}
			}

		}
		
	}
	
	public static String formatarDataGMT(Date date) {
		
        SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date data = new Date();
       
        return formatador.format(data) + " GMT";
	}

}
