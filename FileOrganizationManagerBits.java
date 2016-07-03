import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Catharina Bermal
 */
public class FileOrganizationManagerBits implements ManagementInterface {
    
    // declaracao das variaveis
    int livre=0;
    int ocupado=0;
    ArrayList<String> vector = new ArrayList<>();
    RandomAccessFile raf; // para ler as linhas do arquivo
    String linha; // armazena a linha lida pelo buffer
    String arq;
    int linhas=1; // para contar quantas linhas tem o arquivo
    int tamanho=0; // tamanho total do vetor de bits
    int col=0; // para contar as colunas
    int blocos=0; // para armazenar a quantidade de blocos no arq
    public int vetor[]; // vetor que ira armazenar todos os blocos do arq
    int aux=0; 
    
        
    public FileOrganizationManagerBits(String nomeArquivo) throws IOException{
        
        this.arq = nomeArquivo;
        try {
            System.out.println("-----------------------------------------");
            System.out.println("Estes sao os dados do seu arquivo:");
            raf = new RandomAccessFile(arq, "rw");
            // lê a primeira linha do arquivo de blocos
            linha = raf.readLine();

            // salva a primeira linha num vetor
            String bits[] = linha.split(" "); //vetor de bits
            // conta a quantidade de blocos na linha (n colunas)
            col = bits.length;
            System.out.println("Numero de colunas: "+col);

            // conta a quantidade de linhas no arquivo (m linhas)
            while (raf.readLine() != null) linhas++;
            System.out.println("Numero de linhas: "+linhas);
            
            // conta a quantidade total de blocos no arquivo
            blocos = linhas * col;
            System.out.println("Nemero de blocos: "+blocos);

            // se a quantidade de blocos não for multiplo de 8 exit
            if (blocos % 8 != 0) {
                System.exit(-1);
            }
            else{
                raf.seek(0);
                vetor = new int [blocos];
                System.out.print("Vetor de bits resultante: ");
                    for(int i=0; i<linhas; i++){
                        linha = raf.readLine();
                        bits = linha.split(" ");
                        
                        for(int j=0; j<col; j++){
                            vetor[j] = Integer.parseInt(bits[j]);
                            vector.add(bits[j]);
                            System.out.print(vetor[j]);
                        }
                    }
            }
            System.out.print("\n-----------------------------------------");
            raf.close();
        } catch(IOException e){
            System.out.println("Error: falha no arquivo '"+nomeArquivo+".txt'");
        }    
    }       
    
    //********************FUNCIONA***************************
    @Override
    public void compact(){
        for(int i=0; i<vector.size(); i++){
            if(vector.get(i).equals("0")){
                livre++;
            }
            else 
                ocupado++;
        }
        System.out.println("\nBlocos livres = "+livre +" / "+ "Blocos ocupados = "+ ocupado);
        vector.clear();
        for(int j=0; j<blocos; j++){
            if(ocupado>0){
                //vector.remove(j);
                vector.add("1");
                ocupado--;
            }
            else{
                //vector.remove(j);
                vector.add("0");
            }
        } 
        System.out.print("Vetor de bits resultante apos a compactacao: \n"+vector);   
    }
    //*****************************************************
    
    //*************FUNCIONA********************************MAS FALTA CORRIGIR A ENTRADA E UM VALOR INVALIDO: 0 OU > QUE O NUMERO DE BLOCOS LIVRES
    @Override
    public int[] allocateDataBlock(int numberOfBlocks){
            int num;
            num = numberOfBlocks;
            for(int i=0; i<vector.size(); i++){
                if(vector.get(i).equals("0") && num>0){
                    vector.set(i, "1");
                    num--;
                }
            }
            System.out.print(vector);
            return vetor;
    }
    //****************************************************
    
    //**************FUNCIONA******************************
    // acho q tem que aqui imprimir de novo o vetor de bits antes de pedir os blocos
    @Override
    public boolean freeDataBlocks(int[] blockId){
        int id[];
        id = blockId;
        for(int i=0; i<vector.size(); i++){
            for(int j=0; j<blockId.length; j++){
                if(id[j] == i){
                    vector.set(i, "0");
                }
            }
        }
        System.out.println(vector);
        return true;
    }
    //***************************************************
    
    //*******************FUNCIONA************************
    @Override
    public void format(){
         vector.clear();
        for(int i=0; i<blocos; i++){
            vector.add("0");
        }
        System.out.print(vector);
    }
    //**************************************************
    
    //*********************FUNCIONA*********************SÓ PRECISA DEFINIR O INTERVALO VÁLIDO
    @Override
    public String getDataBlockInfo(int blockId){
        String resposta="";
        int blck;
        blck = blockId;
        for(int i=0; i<vector.size(); i++){
            if(i == blck){
                if(vector.get(i).equals("0"))
                    resposta = "Bloco livre";
                else
                    resposta = "Bloco alocado";
            }
                 
        }
        return resposta;  
    }
    //***************************************************
    
    @Override
    public int[] getEmptyFileBlockList(){
        int freeBlck[];

        for(int i=0; i<vector.size(); i++){
            if(vector.get(i).equals("0")){
                livre++;
            }
        }

        freeBlck = new int[livre];
        int aux = livre;
        int cont=0;
        for(int i=0; i<vector.size(); i++){
            if(vector.get(i).equals("0")){
                freeBlck[cont]=i;
                cont++;
            }
        }
        return freeBlck;
    }
    
    @Override
    public int[] getUsedFileBlockList(){
        int dois[];
        dois = new int[10];
        return dois;
    }
    
    @Override
    public boolean saveToFile(String fileName){
        return false;
    }
    
   
    
}
