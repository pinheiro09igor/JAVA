
















import java.io.*;
import java.io.*;
import java.nio.charset.*;

public class Aplicacao {

	public static Jogo pesquisa(Jogo[] vetor, String string) {
        String data = string.split(";")[0];
        String selecao1 = string.split(";")[1];

        int dia = Integer.parseInt(data.split("/")[0]);
        int mes = Integer.parseInt(data.split("/")[1]);
        int ano = Integer.parseInt(data.split("/")[2]);

		for(Jogo jogo : vetor) {
			if((jogo.getDia() == dia) && (jogo.getMes() == mes) && (jogo.getAno() == ano) && jogo.getSelecao1().equals(selecao1)) {
				return jogo;
			}
		}

        return null;
	}
	
	public static void main(String[] args) throws Exception {
		MyIO.setCharset("UTF-8");
		
		Jogo[] jogo;
		Pilha pilhaJogos = new Pilha(), pilhaJogosImprimir = new Pilha();
		String entrada = "";
		ArquivoTextoLeitura arquivo = new ArquivoTextoLeitura("src/tmp/partidas.txt");

		jogo = arquivo.carregar(1000);
		
		entrada = MyIO.readLine();
		while (!entrada.equals("FIM")) {

            Jogo jogoEncontrado = pesquisa(jogo, entrada);
            
            try {
            	pilhaJogos.empilhar(jogoEncontrado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            entrada = MyIO.readLine();
        }
		
		int tamanho = MyIO.readInt();
		
		for(int i = 0; i < tamanho; i++) {
        	entrada = MyIO.readLine();

        	if(entrada.charAt(0) == 'E') {
                entrada = entrada.substring(2);
                Jogo jogoEncontrado = pesquisa(jogo, entrada);

                try {
                    pilhaJogos.empilhar(jogoEncontrado);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
        	} else if(entrada.equals("D")) {
        		try {
        			System.out.print("(D) ");
        			pilhaJogos.desempilhar().imprimirJogo();
                 } catch (Exception e) {
                    System.out.println(e.getMessage());          	 
                 }
        	}
        }
		int t = pilhaJogos.tamanho();
		for(int i = 0; i < t; i++) {
			Jogo desempilado = null;
			try {
    			desempilado = pilhaJogos.desempilhar();
             } catch (Exception e) {
                System.out.println(e.getMessage());  	 
             }
			try {
				pilhaJogosImprimir.empilhar(desempilado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
		}
		
		pilhaJogosImprimir.imprimir();
	}

}


class ArquivoTextoLeitura {
	private BufferedReader entrada;
	ArquivoTextoLeitura(String nomeArquivo) {
		try {
			entrada = new BufferedReader(new FileReader(nomeArquivo));
		}
		catch (FileNotFoundException excecao) {
			System.out.println("Arquivo nao encontrado");
		}
	}
	public void fecharArquivo() {
		try {
			entrada.close();
		}
		catch (IOException excecao) {
			System.out.println("Erro no fechamento do arquivo de leitura: " + excecao);
		}
	}
	@SuppressWarnings("finally")
	public String ler() {
		String textoEntrada = null;
		try {
			textoEntrada = entrada.readLine();
		}
		catch (EOFException excecao) { //Excecao de final de arquivo.
			textoEntrada = null;
		}
		catch (IOException excecao) {
			System.out.println("Erro de leitura: " + excecao);
			textoEntrada = null;
		}
		finally {
			return textoEntrada;
		}
	}
	
	public Jogo[] carregar(int tam) {
		Jogo[] copa = new Jogo[tam];
		String string = null;
		int i = 0;
		
		string = this.ler();
		
		while (string != null) {

			int ano = Integer.parseInt(string.split("#")[0]);
			String etapa = string.split("#")[1];
			int dia = Integer.parseInt(string.split("#")[2]);
			int mes = Integer.parseInt(string.split("#")[3]);
			String selecao1 = string.split("#")[4];
			int placarSelecao1 = Integer.parseInt(string.split("#")[5]);
			int placarSelecao2 = Integer.parseInt(string.split("#")[6]);
			String selecao2 = string.split("#")[7];
			String local = string.split("#")[8];

			copa[i++] = new Jogo(ano, etapa, dia, mes, selecao1, placarSelecao1, placarSelecao2, selecao2, local);
			string = this.ler();
		}

		this.fecharArquivo();

		return copa;
	}
}


class Celula {

	private Jogo item;
	private Celula proximo;
	
	public Celula(Jogo novo) {
	
  	item = novo;
		proximo = null;
	}
	
	public Celula() {
		
		item = new Jogo();
		proximo = null;
	}
	
	public Jogo getItem() {
		return item;
	}
	public void setItem(Jogo item) {
		this.item = item;
	}
	
	public Celula getProximo() {
		return proximo;
	}
	public void setProximo(Celula proximo) {
		this.proximo = proximo;
	}
}


class Pilha {

	private Celula topo;
	private Celula fundo;
	
	Pilha(){
		Celula sentinela;
		
		sentinela = new Celula();
		topo = sentinela;
		fundo = sentinela;
	}
	
	public boolean pilhaVazia() {

		if (topo == fundo)
			return true;
		else
			return false;
	}
	
	public void empilhar(Jogo novo) {

		Celula novaCelula;

		novaCelula = new Celula(novo);

		novaCelula.setProximo(topo);
		topo = novaCelula;
	}
	
	public Jogo desempilhar() throws Exception {

		Jogo celulaDesempilhada;

		if (! pilhaVazia()) {

			celulaDesempilhada = topo.getItem();
			topo = topo.getProximo();
			return celulaDesempilhada;
		} else
			throw new Exception("Não foi possível desempilhar: a pilha está vazia!");
	}
	
	public void imprimir() throws Exception {

		Celula aux;
		int posicao = 0;
		
		if (! pilhaVazia()) {

			aux = topo;

			while (aux != null) {
				System.out.print("[" + posicao + "]");
				aux.getItem().imprimirJogo();
				aux = aux.getProximo();
				posicao++;
				if(aux.getItem().getLocal() == null)
					aux = null;
			}
			
		} else
			throw new Exception ("Não foi possível imprimir o conteúdo da fila: a fila está vazia!");
	}
	
	public Jogo consultarTopo() throws Exception {
		
		if (! pilhaVazia()) {
			return(topo.getItem());
		} else
			throw new Exception("Não foi possível consultar o topo da pilha: a pilha está vazia!");
	}
	
	public int tamanho() {

		Celula aux= topo;
		int posicao = 0;
		
		while (aux != null) {
			aux = aux.getProximo();
			posicao++;
			if(aux.getItem().getLocal() == null)
				aux = null;
		}
		
		return posicao;
	}
}



class Jogo {

    private int ano, dia, mes, placarSelecao1, placarSelecao2;
    private String etapa, selecao1, selecao2, local;
    
    public Jogo() { 
    	
    }
    
    public Jogo(int ano, String etapa, int dia, int mes, String selecao1, int placarSelecao1, int placarSelecao2, String selecao2, String local) {
        this.setAno(ano);
        this.setEtapa(etapa);
        this.setDia(dia);
        this.setMes(mes);
        this.setSelecao1(selecao1);
        this.setPlacarSelecao1(placarSelecao1);
        this.setPlacarSelecao2(placarSelecao2);
        this.setSelecao2(selecao2);
        this.setLocal(local);
    }
    
    public int getAno() {
        return this.ano;
    }

    public String getEtapa() {
        return this.etapa;
    }

    public int getDia() {
        return this.dia;
    }

    public int getMes() {
        return this.mes;
    }

    public String getSelecao1() {
        return this.selecao1;
    }

    public int getPlacarSelecao1() {
        return placarSelecao1;
    }

    public int getPlacarSelecao2() {
        return placarSelecao2;
    }

    public String getSelecao2() {
        return selecao2;
    }

    public String getLocal() {
        return local;
    }
    
    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public void setSelecao1(String selecao1) {
        this.selecao1 = selecao1;
    }

    public void setSelecao2(String selecao2) {
        this.selecao2 = selecao2;
    }

    public void setPlacarSelecao1(int placarSelecao1) {
        this.placarSelecao1 = placarSelecao1;
    }

    public void setPlacarSelecao2(int placarSelecao2) {
        this.placarSelecao2 = placarSelecao2;
    }

    public void setLocal(String local) {
        this.local = local;
    }
    
    public void ler(String entrada) {
        
    }
    
    public Jogo clone() {
		Jogo copia;
		copia = new Jogo(this.ano, this.etapa, this.dia, this.mes, this.selecao1, this.placarSelecao1, this.placarSelecao2, this.selecao2, this.local);
		return copia;
	}
    
    public void imprimirJogo() {
		System.out.println("[COPA "+ano+"] ["+etapa+"] ["+dia+"/"+mes+"] ["+selecao1+" ("+placarSelecao1+") x ("+placarSelecao2+") "+selecao2+"] ["+local+"]");
	}
    
}


class MyIO {

   private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in, Charset.forName("ISO-8859-1")));
   private static String charset = "ISO-8859-1";

   public static void setCharset(String charset_){
      charset = charset_;
      in = new BufferedReader(new InputStreamReader(System.in, Charset.forName(charset)));
   }

   public static void print(){
   }

   public static void print(int x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void print(double x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void print(String x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void print(boolean x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void print(char x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void println(){
   }

   public static void println(int x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.println(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void println(double x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.println(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void println(String x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.println(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void println(boolean x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.println(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void println(char x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.println(x);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static void printf(String formato, double x){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.printf(formato, x);// "%.2f"
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
   }

   public static double readDouble(){
      double d = -1;
      try{
         d = Double.parseDouble(readString().trim().replace(",","."));
      }catch(Exception e){}
      return d;
   }

   public static double readDouble(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      return readDouble();
   }

   public static float readFloat(){
      return (float) readDouble();
   }

   public static float readFloat(String str){
      return (float) readDouble(str);
   }

   public static int readInt(){
      int i = -1;
      try{
         i = Integer.parseInt(readString().trim());
      }catch(Exception e){}
      return i;
   }

   public static int readInt(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      return readInt();
   }

   public static String readString(){
      String s = "";
      char tmp;
      try{
         do{
            tmp = (char)in.read();
            if(tmp != '\n' && tmp != ' ' && tmp != 13){
               s += tmp;
            }
         }while(tmp != '\n' && tmp != ' ');
      }catch(IOException ioe){
         System.out.println("lerPalavra: " + ioe.getMessage());
      }
      return s;
   }

   public static String readString(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      return readString();
   }

   public static String readLine(){
      String s = "";
      char tmp;
      try{
         do{
            tmp = (char)in.read();
            if(tmp != '\n' && tmp != 13){
               s += tmp;
            }
         }while(tmp != '\n');
      }catch(IOException ioe){
         System.out.println("lerPalavra: " + ioe.getMessage());
      }
      return s;
   }

   public static String readLine(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      return readLine();
   }

   public static char readChar(){
      char resp = ' ';
      try{
         resp  = (char)in.read();
      }catch(Exception e){}
      return resp;
   }

   public static char readChar(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      return readChar();
   }

   public static boolean readBoolean(){
      boolean resp = false;
      String str = "";

      try{
         str = readString();
      }catch(Exception e){}

      if(str.equals("true") || str.equals("TRUE") || str.equals("t") || str.equals("1") || 
            str.equals("verdadeiro") || str.equals("VERDADEIRO") || str.equals("V")){
         resp = true;
            }

      return resp;
   }

   public static boolean readBoolean(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      return readBoolean();
   }

   public static void pause(){
      try{
         in.read();
      }catch(Exception e){}
   }

   public static void pause(String str){
      try {
         PrintStream out = new PrintStream(System.out, true, charset);
         out.print(str);
      }catch(UnsupportedEncodingException e){ System.out.println("Erro: charset invalido"); }
      pause();
   }
}


