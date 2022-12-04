import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.io.*;
import java.nio.charset.*;

public class Aplicacao {
	public static int comparacao, troca;

	public static Jogo pesquisa(Jogo[] vetor, String string) {
        String data = string.split(";")[0];
        String selecao1 = string.split(";")[1];

        int dia = Integer.parseInt(data.split("/")[0]);
        int mes = Integer.parseInt(data.split("/")[1]);
        int ano = Integer.parseInt(data.split("/")[2]);

		for(Jogo jogo : vetor) {
			if((jogo.getDia() == dia) && (jogo.getMes() == mes) && (jogo.getAno() == ano) && jogo.getSelecao1().equals(selecao1)) {
				comparacao += 4;
				return jogo;
			}
		}

        return null;
	}
	
	public static Jogo[] quicksort(Jogo[] array, int esq, int dir) {
		
		int part;
		if (esq < dir){
			comparacao++;
			part = particao(array, esq, dir);
			quicksort(array, esq, part - 1);
			quicksort(array, part + 1, dir);
		}
		return array;
	}
					
	private static int particao(Jogo[] array, int inicio, int fim) {
			
		Jogo pivot = array[fim];
		int part = inicio - 1;
		for (int i = inicio; i < fim; i++) {
			if (array[i].getAno() < pivot.getAno()) {
				part++;
				swap(array, part, i);
				comparacao++;
				troca++;
			}else if((array[i].getAno() == pivot.getAno()) && (array[i].getMes() < pivot.getMes())){
				part++;
				swap(array, part, i);
				comparacao += 2;
				troca++;
			}else if((array[i].getMes() == pivot.getMes()) && (array[i].getDia() < pivot.getDia())) {
				part++;
				swap(array, part, i);
				comparacao += 3;
				troca++;
			}else if((array[i].getDia() == pivot.getDia()) && (array[i].getEtapa().compareTo(pivot.getEtapa())) < 0) {
				part++;
				swap(array, part, i);
				comparacao += 4;
				troca++;
			}else if((array[i].getEtapa().compareTo(pivot.getEtapa())) == 0 && (array[i].getSelecao1().compareTo(pivot.getSelecao1())) < 0) {     
				part++;
				swap(array, part, i);
				comparacao += 5;
				troca++;
			}
			comparacao++;
		}
		part++;
		swap(array, part, fim);
		return (part);
	}
		
	private static void swap(Jogo[] array, int i, int j) {
		      
		Jogo temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public static void main(String[] args) throws Exception {
		long tempoInicial = System.currentTimeMillis();
		MyIO.setCharset("UTF-8");
		
		Jogo[] copa, ordenado;
		String entrada = "";
		ArquivoTextoLeitura arquivo = new ArquivoTextoLeitura("src/tmp/partidas.txt");
		
		copa = arquivo.carregar(1000);
		
		int tamanho = MyIO.readInt();
		ordenado = new Jogo[tamanho];
		
		for(int i = 0; i < tamanho; i++) {
        	entrada = MyIO.readLine();
            Jogo jogoEncontrado = pesquisa(copa, entrada);
            ordenado[i] = jogoEncontrado;
            comparacao++;
        }

		ordenado = quicksort(ordenado, 0, tamanho-1);
		
		for(int i=0; i<tamanho; i++) {
			ordenado[i].imprimirJogo();
			comparacao++;
		}
		
		long tempoFinal = System.currentTimeMillis() - tempoInicial;
		
		escritor("1202168_quicksort.txt", Integer.toString((int) tempoFinal), Integer.toString(comparacao), Integer.toString(troca));
		
	}

	public static void escritor(String nome, String tempoFinal, String comparacao, String troca) throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(nome));
		buffWrite.append("1202168" + "\t" + tempoFinal + "\t" + comparacao + "\t" + troca);
		buffWrite.close();
	}
	
}



class ArquivoTextoLeitura {
    private BufferedReader entrada;
    private int Linhas;
    
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
        
        this.setLinhas(i);
        
        this.fecharArquivo();

        return copa;
    }
    
    public void setLinhas(int quantidade) {
        this.Linhas = quantidade;
    }

    public int getLinhas() {
        return this.Linhas;
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
