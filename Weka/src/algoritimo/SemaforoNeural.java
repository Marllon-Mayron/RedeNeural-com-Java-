package algoritimo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class SemaforoNeural {
	
	private List<Classifier> populacaoRedesNeurais;
	
	List<String> cores = Arrays.asList("VERDE", "AMARELO", "VERMELHO");
	List<String> acoes = Arrays.asList("ACELERAR" , "FREAR", "PARAR");
	List<String> desempenhos = Arrays.asList("RUIM" , "MEDIO", "BOM");

	private static final int TAMANHO_POPULACAO = 1;
	private static final double BASE_INICIAL = 100;
	
    private static Random random = new Random();

    //CRIAR OS ATRIBUTOS
    private Attribute attSemaforo;
    private Attribute attAcao;
    private Attribute attDesempenho;
    
    private Instances dataset;
    
    public SemaforoNeural() throws Exception {
    	//INSTACIAR ATRIBUTOS (NOME DO ATRIBUTO, E O TOTAL DE VARIAÇÕES)
    	attSemaforo = new Attribute("Estado", cores); 
        attAcao = new Attribute("Acao", acoes); 
        attDesempenho = new Attribute("Desempenho", desempenhos); 
        //=============================================================
        //INSTANCIAR O AMBIENTE E DEFINIR QUAL O TARGET
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(attSemaforo);
        attributes.add(attAcao);
        attributes.add(attDesempenho);
        dataset = new Instances("Ambiente", attributes, 1);
        dataset.setClass(attAcao); //AQUI DEFINO A CLASSE PREVISORA
        //=============================================================
        //POPULAR A BASE DE DADOS APRA SER USADA NO TREINAMENTO
        PopularBase();
        //CRIAR REDE NEURAL
        inicializarPopulacaoRedesNeurais();
    }
    public int classificarDesempenho(int acao, int cor) {
    	if(cor == 0 && acao == 0) {
    		return 2;
    	}else if(cor == 0 && acao == 1) {
    		return 1;
    	}else if(cor == 0 && acao == 2) {
    		return 0;
    	}else if(cor == 1 && acao == 0) {
    		return 0;
    	}else if(cor == 1 && acao == 1) {
    		return 2;
    	}else if(cor == 1 && acao == 2) {
    		return 1;
    	}else if(cor == 2 && acao == 0) {
    		return 0;
    	}else if(cor == 2 && acao == 1) {
    		return 1;
    	}else if(cor == 2 && acao == 2) {
    		return 2;
    	}
    	return 0;
    }
    private void PopularBase() {
        //POPULANDO A BASE DE TREINAMENTO DE ACORDO COM A QUANTIDADE INFORMADA NA BASE_INICIAL
        for(int i = 0; i < BASE_INICIAL; i++) {
        	int acao = random.nextInt(3);
        	int cor = random.nextInt(3);
        	int desempenho = classificarDesempenho(acao, cor);
            adicionarRegistro(cor, acao, desempenho);
        }
    }
    
    private void adicionarRegistro(int cor, int acao, int desempenho) {
    	Instance instancia = new DenseInstance(3);
        instancia.setValue(attSemaforo, cores.get(cor));
        instancia.setValue(attAcao, acoes.get(acao));
        instancia.setValue(attDesempenho, desempenhos.get(desempenho));
        instancia.setDataset(dataset);
        dataset.add(instancia);
    }
    
    private void inicializarPopulacaoRedesNeurais() throws Exception {
    	// CRIA UMA NOVA REDE NEURAL PRA CADA INDIVIDUO
        populacaoRedesNeurais = new ArrayList<>();
        for (int i = 0; i < TAMANHO_POPULACAO; i++) {
            MultilayerPerceptron redeNeuralIndividuo = new MultilayerPerceptron();
            redeNeuralIndividuo.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
            redeNeuralIndividuo.buildClassifier(dataset);
            populacaoRedesNeurais.add(redeNeuralIndividuo);
            //CRIAR UMA NOVA BASE ALEATÓRIA PARA QUE OS INDIVIDUOS TENHA REFERENCIAS DIFERENTES.
            
            for(int j = 0; j < dataset.size(); j++) {
            	//System.out.println(dataset.get(j));
            }
            System.out.println("BASE CARREGADA===================");
            dataset.clear();
            PopularBase();
        }
    }
    
    public String passo(int idIndividuo, int cor, int experiencia) throws Exception {
        // Criar instância para a entrada da rede neural
        Instance instancia = new DenseInstance(3);
        instancia.setValue(attSemaforo, cores.get(cor));
        instancia.setValue(attDesempenho, desempenhos.get(experiencia));

        instancia.setDataset(dataset);

        Classifier classificador = populacaoRedesNeurais.get(idIndividuo);
        String acaoPrevista = acoes.get((int) classificador.classifyInstance(instancia));
        return acaoPrevista;
    }
    
}
