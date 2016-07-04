import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author Andre Pessoni
 * @author Catharina Bermal
 * @author Jessica Temporal
 * @author Joao Paulo Peres
 */

public class FileOrganizationManagerInterlig implements ManagementInterface{
    // declaracao das variaveis
    int livre=0;
    int ocupado=0;
    ArrayList<String> vector = new ArrayList<>();
    RandomAccessFile raf; // para ler as linhas do arquivo
    RandomAccessFile salva;
    String linha; // armazena a linha lida pelo buffer
    String arq;
    String op;
    String bits[];
    int linhas=1; // para contar quantas linhas tem o arquivo
    int tamanho=0; // tamanho total do vetor de bits
    int col=0; // para contar as colunas
    int blocos=0; // para armazenar a quantidade de blocos no arq
    public int vetor[]; // vetor que ira armazenar todos os blocos do arq
    int aux=0;

    // construtor da classe
    public FileOrganizationManagerInterlig(String nomeArquivo) throws IOException{

        this.arq = nomeArquivo;
        System.out.println("-----------------------------------------");
        System.out.println("Estes sao os dados do seu arquivo:");

        try {
            raf = new RandomAccessFile(arq, "rw");
        }catch(IOException e){
            System.out.println("Erro: falha ao abrir o arquivo");
        }

        // lê a primeira linha do arquivo de blocos
        linha = raf.readLine();

        // salva os blocos da primeira linha num vetor
        bits = linha.split(" ");

        // imprime dados do arquivo
        imprimeDados(bits, raf);

        // se a quantidade de blocos não for multiplo de 8 exit
        if (blocos % 8 != 0) {
            System.exit(-1);
        }
        else {
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
    }

    /**
     * compacta os blocos utilizados do disco, movendo os blocos disponíveis para o final
     */
    @Override
    public void compact(){
        livre = contaBlocos("livre");
        ocupado = contaBlocos("ocupado");

        vector.clear();
        for(int j=0; j<blocos; j++){
            if(ocupado>0){
                vector.add("1");
                ocupado--;
            }
            else {
                vector.add("0");
            }
        }
        System.out.println("\nDisco compactado com sucesso!");
    }

    /**
     * aloca uma determinada quantidade de blocos livres
     */
    @Override
    public int[] allocateDataBlock(int numberOfBlocks){
        int num;
        num = numberOfBlocks;

        livre = contaBlocos("livre");
        if((num<0) || (num>vector.size()) || (num>livre)){
            System.out.println("Erro: Valor invalido.");
            System.exit(-1);
        }
        else {
            for(int i=0; i<vector.size(); i++){
                if(vector.get(i).equals("0") && num>0){
                    vector.set(i, "1");
                    num--;
                }
            }
        }
        System.out.print(vector);
        return vetor;
    }

    /**
     * disponibiliza blocos previamente alocados
     */
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
        return true;
    }

    /**
     * formata o sistema de arquivos, tornando todos os blocos disponíveis
     */
    @Override
    public void format(){
        vector.clear();
        for(int i=0; i<blocos; i++){
            vector.add("0");
        }
        System.out.println("Disco formatado com sucesso!");
    }

    /**
     * obtem informações sobre um bloco de dados
     */
    @Override
    public String getDataBlockInfo(int blockId){
        String resposta="";
        int emptyBlck[];
        if((blockId<0) || (blockId>vector.size())){
            System.out.println("Erro: ID de bloco fora do intervalo.");
            System.exit(-1);
        }
        else {
            if(vector.get(blockId).equals("1"))
                resposta = "Bloco alocado";
            else {
                // armazena a lista de indices de blocos livres
                emptyBlck = getBlocksIndex("livre");
                // pecorre essa lista
                for (int i=0; i<emptyBlck.length; i++){
                    // caso o bloco especificado não seja nem ultimo nem o penultimo
                    // bloco vazio imprima isto
                    if ((emptyBlck[i] == blockId) && (i < (emptyBlck.length - 2))){
                        resposta = "-1 -1 -1 -1 -1 -1 -1 "+(emptyBlck[i+1])+"\n-1 -1 -1 -1 -1 -1 -1 "+emptyBlck[i+2];
                    }
                    // caso o bloco seja o penultimo bloco vazio imprima isso
                    if ((emptyBlck[i] == blockId) && (i == (emptyBlck.length - 2))) {
                        resposta = "-1 -1 -1 -1 -1 -1 -1 "+(emptyBlck[i+1])+"\n-1 -1 -1 -1 -1 -1 -1";
                    }
                    // caso o bloco seja o ultimo bloco livre imprima isso:
                    if (blockId == emptyBlck[(emptyBlck.length-1)]){
                        resposta = "-1 -1 -1 -1 -1 -1 -1 -1";
                    }
                }
            }
        }
        return resposta;
    }

    /**
     * recupera a lista de blocos disponíveis no sistema
     */
    // Escrito desse jeito para evitar o erro de compilação
    @Override
    public int[] getEmptyFileBlockList(){
        int[] um;
        um = new int[10];
        return um;
    }

    /**
     * recuperar a lista de blocos alocados no sistema
     */
    // Escrito desse jeito para evitar o erro de compilação
    @Override
    public int[] getUsedFileBlockList(){
        int[] dois;
        dois = new int[10];
        return dois;
    }

    /**
     * salvar em arquivo texto as informações de gerenciamento de espaço
     * livre como um vetor de bits
     */
    @Override
    public boolean saveToFile(String fileName){
        String escrever="";
        col = countCol(bits); // quantidade de posicoes por linha
        try{
            salva = new RandomAccessFile(fileName, "rw");
            for(int i=0; i<vector.size(); i++){
                escrever +=(vector.get(i)+" ");
                if ((i+1) % col == 0) {
                    escrever += '\n';
                }
            }
            salva.writeChars(escrever);
            salva.close();
            return true;            
        } catch(IOException e) {
            System.out.println("Erro: falha na escrita do arquivo");
        }
        return false;
    }
    
    /**
     * Método auxiliar:
     * Conta a quantidade de blocos livres ou ocupados
     * recebe uma string como opção
     * "livre" -> conta os blocos livres
     * "ocupado" -> conta blocos ocupados
     * retorna a quantidade de blocos
     */
    public int contaBlocos(String op){
        int qntd;
        qntd=0;
        if(op.equals("livre")){
            for(int i=0; i<vector.size(); i++){
                if(vector.get(i).equals("0")){
                    qntd++;
                }
            }
        }
        else {
            for(int i=0; i<vector.size(); i++){
                if(vector.get(i).equals("1")){
                    qntd++;
                }
            }
        }
        return qntd;
    }

    /**
     * Metodo auxiliar:
     * recebe um vetor de strings conta a quantidade de blocos na linha
     * (n colunas) e retorna a quantidade de colunas
     */
    public int countCol(String bits[]){
        col = bits.length;
        return col;
    }

    /**
     * Método auxiliar:
     * recebe um ponteiro de acesso ao arquivo
     * conta a quantidade de linhas no arquivo (m linhas)
     * e retorna a quantidade de linhas do arquivo
     */
    public int countLinhas(RandomAccessFile raf){
        try{
            while (raf.readLine() != null) linhas++;
        } catch(IOException e){
            System.out.println("Error: falha no arquivo!");
        }
        return linhas;
    }

    /**
     * Método auxiliar:
     * Utilizado para imprimir os dados do arquivo lido
     */
    public void imprimeDados(String bits[], RandomAccessFile raf){
        col = countCol(bits);
        System.out.println("Numero de colunas: "+col);

        linhas = countLinhas(raf);
        System.out.println("Numero de linhas: "+linhas);

        blocos = col * linhas; 
        System.out.println("Nemero de blocos: "+blocos);
    }
    

    /**
     * Método auxiliar:
     * retorna a lista de indices dos blocos que nao estao alocados
     * recebe uma string
     * "livre" -> conta os blocos livres
     * "ocupado" -> conta blocos ocupados
     * retorna um vetor de indices dos blocos de interesse
     */
    public int[] getBlocksIndex(String type){
        int blck[];
        int aux=0;

        // pecorre o vetor de bits e salva os indices dos blocos de interesse
        if (type.equals("livre")){
            // conta blocos livres e instancia um vetor para armazenar os indices
            // desses blocos
            livre = contaBlocos("livre");
            blck = new int[livre];
            for(int i=0; i<vector.size(); i++){
                if(vector.get(i).equals("0")){
                    blck[aux]=i;
                    aux++;
                }
            }
        }
        else {
            // conta blocos ocupados e instancia um vetor para armazenar os indices
            // desses blocos
            ocupado = contaBlocos("ocupado");
            blck = new int[ocupado];
            for(int i=0; i<vector.size(); i++){
                if(vector.get(i).equals("1")){
                    blck[aux]=i;
                    aux++;
                }
            }
        }
        return blck;
    }
}
