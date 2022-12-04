import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public static void main(String[] args) throws IOException {
		long tempoInicial = System.currentTimeMillis();
		MyIO.setCharset("UTF-8");
		
		Jogo[] jogo;
		ABB arvoreJogos = new ABB();
		String entrada = "";
		ArquivoTextoLeitura arquivo = new ArquivoTextoLeitura("src/tmp/partidas.txt");

		jogo = arquivo.carregar(1000);
		
		entrada = MyIO.readLine();
		while (!entrada.equals("FIM")) {

            Jogo jogoEncontrado = pesquisa(jogo, entrada);
            
            try {
            	arvoreJogos.inserir(jogoEncontrado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            
            entrada = MyIO.readLine();
        }
		
		entrada = MyIO.readLine();
		while (!entrada.equals("FIM")) {
			
			Jogo pesquisado;
			pesquisado = arvoreJogos.pesquisar(entrada);
			
			if(pesquisado == null) 
				System.out.println("NÃO");
			else
				System.out.println("SIM");
			
			entrada = MyIO.readLine();
		}

		long tempoFinal = System.currentTimeMillis() - tempoInicial;
		
		escritor("1201268_arvoreBinaria.txt", Integer.toString((int) tempoFinal), Integer.toString(ABB.comparacao));
	}
	
	public static void escritor(String nome, String tempoFinal, String comparacao) throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(nome));
		buffWrite.append("1202168" + "\t" + tempoFinal + "s\t" + comparacao);
		buffWrite.close();
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


public class Nodo {
		
	private Jogo item;
	private Nodo esquerda;
	private Nodo direita;

	Nodo(Jogo item) {
		this.item = item;
		this.esquerda = this.direita = null;
	}

	public Jogo getItem() {
		return item;
	}

	public void setItem(Jogo item) {
		this.item = item;
	}

	public Nodo getEsquerda() {
		return esquerda;
	}

	public Nodo setEsquerda(Nodo esquerda) {
		this.esquerda = esquerda;
		return esquerda;
	}

	public Nodo getDireita() {
		return direita;
	}

	public void setDireita(Nodo direita) {
		this.direita = direita;
	}
		
}


public class ABB {
	
	private Nodo raiz;
	public static int comparacao;
	
	public ABB() {
		raiz = null;
	}
	
	public boolean arvoreVazia() {
				
		if (raiz == null)
			return true;
		else
			return false;
	}
	
	public Jogo pesquisar(String chave) {
		 return pesquisar(chave, raiz);
	}
	
	public void inserir(Jogo novo) throws Exception {
		raiz = inserir(novo, raiz);
	}
	
	private Nodo inserir(Jogo novo, Nodo raizArvore) throws Exception{
		
		if (raizArvore == null)
			raizArvore = new Nodo(novo);
		else if (raizArvore.getItem().getAno() > novo.getAno())
			raizArvore.setEsquerda(inserir(novo, raizArvore.getEsquerda()));
		else if (raizArvore.getItem().getAno() == novo.getAno() && raizArvore.getItem().getMes() > novo.getMes())
			raizArvore.setEsquerda(inserir(novo, raizArvore.getEsquerda()));
		else if (raizArvore.getItem().getMes() == novo.getMes() && raizArvore.getItem().getDia() > novo.getDia())
			raizArvore.setEsquerda(inserir(novo, raizArvore.getEsquerda()));
		else if (raizArvore.getItem().getDia() == novo.getDia() && (raizArvore.getItem().getSelecao1().compareTo(novo.getSelecao1())) > 0)      
			raizArvore.setEsquerda(inserir(novo, raizArvore.getEsquerda()));
		// caso maior
		else if (raizArvore.getItem().getAno() < novo.getAno())
			raizArvore.setDireita(inserir(novo, raizArvore.getDireita()));
		else if (raizArvore.getItem().getAno() == novo.getAno() && raizArvore.getItem().getMes() < novo.getMes())
			raizArvore.setDireita(inserir(novo, raizArvore.getDireita()));
		else if (raizArvore.getItem().getMes() == novo.getMes() && raizArvore.getItem().getDia() < novo.getDia())
			raizArvore.setDireita(inserir(novo, raizArvore.getDireita()));
		else if (raizArvore.getItem().getDia() == novo.getDia() && (raizArvore.getItem().getSelecao1().compareTo(novo.getSelecao1())) < 0)      
			raizArvore.setDireita(inserir(novo, raizArvore.getDireita()));
		else
			throw new Exception("Erro ao tentar inserir na árvore: chave repetida!");
		
		return raizArvore;
	} 
	
	private Jogo pesquisar(String chave, Nodo raizArvore) {
		
		Jogo pesquisado;
		
		String data = chave.split(";")[0];
        String selecao1 = chave.split(";")[1];

        int dia = Integer.parseInt(data.split("/")[0]);
        int mes = Integer.parseInt(data.split("/")[1]);
        int ano = Integer.parseInt(data.split("/")[2]);
		
		if (raizArvore == null)
			pesquisado = null;
		else {
			if (raizArvore.getItem().getDia() == dia && raizArvore.getItem().getMes() == mes && raizArvore.getItem().getAno() == ano && raizArvore.getItem().getSelecao1().equals(selecao1)) {   
				comparacao = comparacao + 4;
				imprimiComparado(raizArvore.getItem());
				pesquisado = raizArvore.getItem();
			}
			else if (raizArvore.getItem().getAno() > ano) {
				comparacao++;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getEsquerda());
			}
			else if (raizArvore.getItem().getAno() == ano && raizArvore.getItem().getMes() > mes){
				comparacao = comparacao + 2;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getEsquerda());
			}
			else if (raizArvore.getItem().getMes() == mes && raizArvore.getItem().getDia() > dia) {
				comparacao = comparacao + 3;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getEsquerda());
			}
			else if (raizArvore.getItem().getDia() == dia && (raizArvore.getItem().getSelecao1().compareTo(selecao1) > 0)){
				comparacao = comparacao + 4;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getEsquerda());
			}
			// caso maior
			else if (raizArvore.getItem().getAno() < ano) {
				comparacao = comparacao++;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getDireita());
			}
			else if (raizArvore.getItem().getAno() == ano && raizArvore.getItem().getMes() < mes){
				comparacao = comparacao + 2;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getDireita());
			}
			else if (raizArvore.getItem().getMes() == mes && raizArvore.getItem().getDia() < dia) {
				comparacao = comparacao + 3;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getDireita());
			}
			else if (raizArvore.getItem().getDia() == dia && (raizArvore.getItem().getSelecao1().compareTo(selecao1) < 0)){
				comparacao = comparacao + 4;
				imprimiComparado(raizArvore.getItem());
				pesquisado = pesquisar(chave, raizArvore.getDireita());
			}
			else
				pesquisado = null;
		}
			
		return pesquisado;
	}
	
	public void imprimiComparado(Jogo comparado) {
		System.out.print("[" + comparado.getDia() + "/" + comparado.getMes() + "/" + comparado.getAno() + ";" + comparado.getSelecao1() + "] - ");
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
