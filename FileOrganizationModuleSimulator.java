/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Scanner;
import java.io.IOException;

/**
 *
 * @author Catharina Bermal
 */

public class FileOrganizationModuleSimulator {

    public static void main(String[] args) throws IOException {

        // declaracao das variaveis
        FileOrganizationManagerBits fomB;
  
        int desaloc[];
        int freeBlck[];
        int usedBlck[];
        int op, aux, id;        
        Scanner sc;      // para pegar a op do menu
        String arquivo;  // para pegar por linha de comando o arquivos de blocos
        String politica; // para pegar por linha de comando a politica
        String resposta; // para armazenar a resposta do metodo getDataBlockInfo
        
        // instaciação das variaveis
        politica = args[0]; 
        arquivo = args[1];
        sc = new Scanner(System.in); 
        resposta = "";
        
        
        // logica para politica de vetor de bits
        if(politica.equals("vetor")){
            System.out.println("Politica: Vetor de Bits\n");
            fomB = new FileOrganizationManagerBits(arquivo); //só deve ser instanciado aqui
                        
            while(true){
                System.out.println("\n\nEscolha uma das opcoes listadas abaixo: ");
                System.out.println ("1: compact");                                      
                System.out.println ("2: allocateDataBlock");                            
                System.out.println ("3: freeDataBlocks");                               
                System.out.println ("4: format");                                       
                System.out.println ("5: getDataBlockInfo");                             
                System.out.println ("6: getEmptyFileBlockList");                        
                System.out.println ("7: getUsedFileBlockList");                         
                System.out.println ("8: saveToFile");                                   
                System.out.println ("0: sair");
                do{
                    op = sc.nextInt();
                    if((op>8)||(op<0)){
                        System.out.println("Error: Opcao invalida. Tente novamente.");
                        System.out.print(">> ");
                    }
                }while((op>8)||(op<0));

                switch (op){
                    case 1:
                        fomB.compact();
                    break;

                    case 2:
                        int num;
                        System.out.println("\nQuantos blocos quer alocar?");
                        num = sc.nextInt();
                        fomB.allocateDataBlock(num);
                    break;

                    case 3:
                        System.out.println("\nQuantos blocos voce deseja desalocar?");
                        aux = sc.nextInt();
                        desaloc = new int [aux];
                        System.out.println("Digite o INDICE dos blocos separados por um enter. (LEMBRANDO QUE COMECA EM 0!)");
                        for(int i=0; i<aux; i++){
                            desaloc[i] = sc.nextInt();
                        }
                        fomB.freeDataBlocks(desaloc);
                    break;

                    case 4:
                        fomB.format();
                    break;

                    case 5:
                        System.out.println("\nDigite o indice do bloco que deseja obter a informacao.");
                        id = sc.nextInt();
                        resposta = fomB.getDataBlockInfo(id);
                        System.out.println(resposta);
                                
                    break;
                    
                    case 6:

                        System.out.print("\nLista de blocos livres: ");
                        freeBlck = fomB.getEmptyFileBlockList();
                        for (int i=0; i<freeBlck.length; i++){
                            System.out.print(freeBlck[i]+" ");
                        }

                    break;
                    
                    case 7:

                        System.out.print("\nLista de blocos livres: ");
                        usedBlck = fomB.getUsedFileBlockList();
                        for (int i=0; i<usedBlck.length; i++){
                            System.out.print(usedBlck[i]+" ");
                        }

                    break;
                    
                    case 8:

                    break;

                    case 0:
                        System.exit(0);
                    break;
                }
            }   
        }else if (politica.equals("lista")){
            System.out.println("A Politica de lista interligada ainda não foi implementada");
            //fomI = new FileOrganizationManagerInterlig(arquivo);
        }
        
        // caso a politica passada seja invalida
        else {
            System.out.println("Quebrouuu");
            System.exit(-1);
        }
                
    }
    
}
