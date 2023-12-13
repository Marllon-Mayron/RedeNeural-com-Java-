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
import weka.core.converters.ArffSaver;



public class SemaforoNeural {
	
	public static int count = 0; 
	private List<Classifier> populacaoNeuralPedal, populacaoNeuralFarol;
	
	List<String> cores = Arrays.asList("VERDE", "AMARELO", "VERMELHO");
	List<String> acoes = Arrays.asList("ACELERAR", "FREAR", "PARAR");
	List<String> desempenhos = Arrays.asList("RUIM" , "MEDIO", "BOM");
	
	List<String> estados = Arrays.asList("MANHA", "TARDE", "NOITE");
	List<String> farol = Arrays.asList("LIGADO" , "DESLIGADO");
	//VARIAVEL PARA SALVAR ARQUIVOS
	public boolean saveArff = true;
	//QUANTIDADE DE REGISTROS INICIAIS PARA SEREM ESTUDADOS
	private static final double BASE_INICIAL = 400;
	
    private static Random random = new Random();

    //CRIAR OS ATRIBUTOS
    private Attribute attSemaforo;
    private Attribute attAcao;
    private Attribute attDesempenho;
    private Attribute attEstado;
    private Attribute attFarol;
    private Attribute attSensor; 
    private Attribute attDistanciaObstaculo; 
    //BASE DE DADOS
    private Instances datasetPedal, datasetFarol;
    
    public SemaforoNeural() throws Exception {
    	//INSTACIAR ATRIBUTOS (NOME DO ATRIBUTO, E O TOTAL DE VARIAÇÕES)
    	attSemaforo = new Attribute("Estado", cores); 
        attAcao = new Attribute("Acao", acoes); 
        attDesempenho = new Attribute("Desempenho", desempenhos); 
        attEstado = new Attribute("Estado", estados);
        attFarol = new Attribute("Farol", farol);
        attSensor = new Attribute("Sensor", 1);
        attDistanciaObstaculo = new Attribute("Obstaculo", 1000);
        //=============================================================
        //INSTANCIAR O AMBIENTE E DEFINIR QUAL O TARGET
        ArrayList<Attribute> attributesPedal = new ArrayList<>();
        attributesPedal.add(attSemaforo);
        attributesPedal.add(attAcao);
        attributesPedal.add(attDesempenho);
        //attributesPedal.add(attDistanciaObstaculo);
        //attributesPedal.add(attSensor);
        datasetPedal = new Instances("Ambiente", attributesPedal, 1);
        datasetPedal.setClass(attAcao); //AQUI DEFINO A CLASSE PREVISORA
        //=============================================================
        ArrayList<Attribute> attributesFarol = new ArrayList<>();
        attributesFarol.add(attEstado);
        attributesFarol.add(attFarol);
        attributesFarol.add(attDesempenho);
        datasetFarol = new Instances("Ambiente", attributesFarol, 1);
        datasetFarol.setClass(attFarol); //AQUI DEFINO A CLASSE PREVISORA
        //=============================================================
        //POPULAR A BASE DE DADOS APRA SER USADA NO TREINAMENTO
        PopularBase();
        //CRIAR REDE NEURAL
        inicializarPopulacaoRedesNeurais();
    }
    //AVALIAR OS COMPORTAMENTOS DOS REGISTROS ALEATORIOS, PARA OS NOVOS REGISTROS QUE FOREM ESTUDAR, ESTUDAREM DE BASES CORRETAS.
    public int classificarDesempenhoPedal(int acao, int cor) {
    	int desempenho = 0;
    	if(cor == 0 && acao == 0) {
    		desempenho = 2;
    	}else if(cor == 0 && acao == 1) {
    		desempenho = 1;
    	}else if(cor == 0 && acao == 2) {
    		desempenho = 0;
    	}else if(cor == 1 && acao == 0) {
    		desempenho = 1;
    	}else if(cor == 1 && acao == 1) {
    		desempenho = 2;
    	}else if(cor == 1 && acao == 2) {
    		desempenho = 1;
    	}else if(cor == 2 && acao == 0) {
    		desempenho = 0;
    	}else if(cor == 2 && acao == 1) {
    		desempenho = 1;
    	}else if(cor == 2 && acao == 2) {
    		desempenho = 2;
    	}

    	return desempenho;
    }
    public int classificarDesempenhoFarol(int estado, int farolDecisao) {
    	int desempenho = 0;
    	
    	if(estado == 0 && farolDecisao == 0) {
    		desempenho = 0;
    	}else if(estado == 0 && farolDecisao == 1) {
    		desempenho = 2;
    	}else if(estado == 1 && farolDecisao == 0) {
    		desempenho = 1;
    	}else if(estado == 1 && farolDecisao == 1) {
    		desempenho = 2;
    	}else if(estado == 2 && farolDecisao == 0) {
    		desempenho = 2;
    	}else if(estado == 2 && farolDecisao == 1) {
    		desempenho = 0;
    	}		
    	
    	return desempenho;
    }
    		
   
    private void PopularBase() {
        //POPULANDO A BASE DE TREINAMENTO DE ACORDO COM A QUANTIDADE INFORMADA NA BASE_INICIAL
        for(int i = 0; i < BASE_INICIAL; i++) {
        	int acao = random.nextInt(3);
        	int cor = random.nextInt(3);
        	int estado = random.nextInt(3);
        	int farol = random.nextInt(2);
        	int desempenhoPedal = classificarDesempenhoPedal(acao, cor);
        	int desempenhoFarol = classificarDesempenhoFarol(estado, farol);
        	int sensor = random.nextInt(2); 
        	int distancia = random.nextInt(1000);
        	//ADICIONAR O REGISTRO NAS BASES DE DADOS
            adicionarRegistro(0,cor, acao, desempenhoPedal, estado, farol, sensor, distancia);
            adicionarRegistro(1,cor, acao, desempenhoFarol, estado, farol, sensor, distancia);
        }
    }
    
    private void adicionarRegistro(int base,int cor, int acao, int desempenho, int estado, int farolDecisao, int sensor, int distancia) {
    	if(base == 0) {
    		Instance instanciaPedal = new DenseInstance(5);
            instanciaPedal.setValue(attSemaforo, cores.get(cor));
            instanciaPedal.setValue(attAcao, acoes.get(acao));
            instanciaPedal.setValue(attDesempenho, desempenhos.get(desempenho));
            instanciaPedal.setValue(attSensor, sensor);
           // instanciaPedal.setValue(attSensor, distancia);
            //instanciaPedal.setDataset(datasetPedal);
            datasetPedal.add(instanciaPedal);
    	}else if(base == 1) {
    		Instance instanciaFarol = new DenseInstance(3);
            instanciaFarol.setValue(attEstado, estados.get(estado));
            instanciaFarol.setValue(attFarol, farol.get(farolDecisao));
            instanciaFarol.setValue(attDesempenho, desempenhos.get(desempenho));
            datasetFarol.add(instanciaFarol);
    	}
    }
    
    private void inicializarPopulacaoRedesNeurais() throws Exception {
    	// CRIA UMA NOVA REDE NEURAL PRA CADA INDIVIDUO
        populacaoNeuralPedal = new ArrayList<>();
        populacaoNeuralFarol = new ArrayList<>();
        
        MultilayerPerceptron redeNeuralPedal = new MultilayerPerceptron();
        redeNeuralPedal.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
        redeNeuralPedal.buildClassifier(datasetPedal);
        populacaoNeuralPedal.add(redeNeuralPedal);
        //============================================================
        MultilayerPerceptron redeNeuralFarol = new MultilayerPerceptron();
        redeNeuralFarol.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
        redeNeuralFarol.buildClassifier(datasetFarol);
        populacaoNeuralFarol.add(redeNeuralFarol);
        //CRIAR UMA NOVA BASE ALEATÓRIA PARA QUE OS INDIVIDUOS TENHA REFERENCIAS DIFERENTES.
        System.out.println("BASE CARREGADA===================");
        if(saveArff) {
        	exportarDatasetFarol("res/farol_dataset/dataset_farol_"+count+".arff");
            exportarDatasetFarol("res/pedal_dataset/dataset_pedal_"+count+".arff");
            count++;
        }
        //LIMPAR BASES, PARA QUE OUTROS INDIVIDUOS APRENDAM COM OUTRAS EXPERIENCIAS
        datasetPedal.clear();
        datasetFarol.clear();
        //POPULAR UMA NOVA BASE
        PopularBase();    
        
    }
    
    public String passo(int acao,int idIndividuo, int cor, int experiencia, int estado) throws Exception {
        //CRIANDO INSTANCIA PARA UM NOVO REGISTRO SER CLASSIFICADO
    	String acaoPrevista = "";
    	if(acao == 0) {
    		Instance instanciaPedal = new DenseInstance(3);
            instanciaPedal.setValue(attSemaforo, cores.get(cor));
            instanciaPedal.setValue(attDesempenho, desempenhos.get(experiencia));
            instanciaPedal.setDataset(datasetPedal);
            
            Classifier classificador = populacaoNeuralPedal.get(idIndividuo);
            acaoPrevista = acoes.get((int) classificador.classifyInstance(instanciaPedal));
    	}else {
    		Instance instanciaFarol = new DenseInstance(3);
            instanciaFarol.setValue(attEstado, estados.get(estado));
            instanciaFarol.setValue(attDesempenho, desempenhos.get(experiencia));
            instanciaFarol.setDataset(datasetFarol);
            
            Classifier classificador = populacaoNeuralFarol.get(idIndividuo);
            acaoPrevista = farol.get((int) classificador.classifyInstance(instanciaFarol));
    	}
        return acaoPrevista;
    }
    
    // METODOS PARA EXPORTAR O DATASET EM ARQUIVO ARFF (USADO NO WEKA)
    public void exportarDatasetPedal(String caminhoArquivo) throws Exception {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(datasetPedal);
        saver.setFile(new java.io.File(caminhoArquivo));
        saver.writeBatch();
    }

    
    public void exportarDatasetFarol(String caminhoArquivo) throws Exception {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(datasetFarol);
        saver.setFile(new java.io.File(caminhoArquivo));
        saver.writeBatch();
    }
    
}
