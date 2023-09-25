package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/**************************************************************************************************
 * Autor: Fernando Rodrigo Pinheiro de Sousa
 * 
 * Classe que recebe um fluxo de dados do Socket para construir uma requisição
 * e exibe as principais informações de cabeçalho. 
 * ************************************************************************************************/

public class RequisicaoHTTP {
	
	private String protocolo;
    private String recurso;
    private String metodo;
    private boolean manterViva = true;
    private int tempoLimite = 3000;
    private Map<String, List<String>> cabecalhos;
	
    public static RequisicaoHTTP lerRequisicao(InputStream entrada) throws IOException {
    	
    	RequisicaoHTTP requisicao = new RequisicaoHTTP();
        
    	BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
        
    	System.out.println("Requisição: ");
        
        String linhaRequisicao = buffer.readLine();
        System.out.println(linhaRequisicao);
        
        String[] dadosReq = linhaRequisicao.split(" ");
    	
    	requisicao.setMetodo(dadosReq[0]);        
        requisicao.setRecurso(dadosReq[1]);        
        requisicao.setProtocolo(dadosReq[2]);   
                
        String dadosHeader = buffer.readLine();        
        
        while (dadosHeader != null && !dadosHeader.isEmpty()) {
            System.out.println(dadosHeader);
            String[] linhaCabecalho = dadosHeader.split(":");
            requisicao.setCabecalho(linhaCabecalho[0], linhaCabecalho[1].trim().split(","));
            dadosHeader = buffer.readLine();
        }
        
        if (requisicao.getCabecalhos().containsKey("Connection")) {
            
            requisicao.setManterViva(requisicao.getCabecalhos().get("Connection").get(0).equals("keep-alive"));
        }
        return requisicao;    	
    }
    
	private void setCabecalho(String chave, String[] valores) {
		if (cabecalhos == null) {
            cabecalhos = new TreeMap<>();
        }
        cabecalhos.put(chave, Arrays.asList(valores));		
	}

	public String getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public boolean isManterViva() {
		return manterViva;
	}
	public void setManterViva(boolean manterViva) {
		this.manterViva = manterViva;
	}
	public int getTempoLimite() {
		return tempoLimite;
	}
	public void setTempoLimite(int tempoLimite) {
		this.tempoLimite = tempoLimite;
	}
	public Map<String, List<String>> getCabecalhos() {
		return cabecalhos;
	}
	    

}
