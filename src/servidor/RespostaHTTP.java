package servidor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/**************************************************************************************************
 * Autor: Fernando Rodrigo Pinheiro de Sousa
 * 
 * Classe que recebe o protocolo, codigo e a mensagem, para construir a resposta HTTP. 
 * ************************************************************************************************/

public class RespostaHTTP {
	
	private String protocolo;
    private int codigoResposta;
    private String mensagem;
    private byte[] conteudoResposta;
    private Map<String, List<String>> cabecalhos;
    private OutputStream saida;
       
    
	public RespostaHTTP() {
	}

	public RespostaHTTP(String protocolo, int codigoResposta, String mensagem) {		
		this.protocolo = protocolo;
		this.codigoResposta = codigoResposta;
		this.mensagem = mensagem;
	}
	
	public void enviar() throws IOException {
        
        saida.write(montaCabecalho());        
        saida.write(conteudoResposta);        
        saida.flush();
    }
	
	public void setCabecalho(String chave, String... valores) {
        if (cabecalhos == null) {
            cabecalhos = new TreeMap<>();
        }
        cabecalhos.put(chave, Arrays.asList(valores));
    }
	
	public String getTamanhoResposta() {
        return getConteudoResposta().length + "";
    }
	
	private byte[] montaCabecalho() {

		return this.toString().getBytes();
	}
	
	public String getProtocolo() {
		return protocolo;
	}
	
	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	
	public int getCodigoResposta() {
		return codigoResposta;
	}
	
	public void setCodigoResposta(int codigoResposta) {
		this.codigoResposta = codigoResposta;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public byte[] getConteudoResposta() {
		return conteudoResposta;
	}
	
	public void setConteudoResposta(byte[] conteudoResposta) {
		this.conteudoResposta = conteudoResposta;
	}
	
	public Map<String, List<String>> getCabecalhos() {
		return cabecalhos;
	}
	
	public void setCabecalhos(Map<String, List<String>> cabecalhos) {
		this.cabecalhos = cabecalhos;
	}
	
	public OutputStream getSaida() {
		return saida;
	}
	
	public void setSaida(OutputStream saida) {
		this.saida = saida;
	}

	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
        str.append(protocolo).append(" ").append(codigoResposta).append(" ").append(mensagem).append("\r\n");
        for (Map.Entry<String, List<String>> entry : cabecalhos.entrySet()) {
            str.append(entry.getKey());
            String stringCorrigida = Arrays.toString(entry.getValue().toArray()).replace("[", "").replace("]", "");
            str.append(": ").append(stringCorrigida).append("\r\n");
        }
        
        str.append("\r\n");
        return str.toString();
	}
    
	
    

}
