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
	private List<Classifier> populacaoNeuralPedal, populacaoNeuralFarol, populacaoNeuralVolante;

	List<String> desempenhos = Arrays.asList("RUIM" , "MEDIO", "BOM");

	
	List<String> cores = Arrays.asList("VERDE", "AMARELO", "VERMELHO");
	List<String> acoes = Arrays.asList("ACELERAR", "FREAR", "PARAR");
	
	List<String> estados = Arrays.asList("MANHA", "TARDE", "NOITE");
	List<String> farol = Arrays.asList("LIGADO" , "DESLIGADO");
	
	List<String> sensores = Arrays.asList("NENHUM" , "FRONTAL" , "DIREITO", "ESQUERDO", "FRONTALDIREITO", "FRONTALESQUERDO", "DIREITAESQUERDA", "TODOS");
	List<String> volantes = Arrays.asList("PARADO" , "DIREITA", "ESQUERDA");
	
	//VARIAVEL PARA SALVAR ARQUIVOS
	public boolean saveArff = true;
	//QUANTIDADE DE REGISTROS INICIAIS PARA SEREM ESTUDADOS
	private static final double BASE_INICIAL = 800;
	
    private static Random random = new Random();

    //CRIAR OS ATRIBUTOS
    private Attribute attSemaforo;
    private Attribute attAcao;
    private Attribute attDesempenho;
    private Attribute attEstado;
    private Attribute attFarol;
    private Attribute attSensor; 
    private Attribute attDistanciaObstaculo; 
    private Attribute attVolante; 
    //BASE DE DADOS
    private Instances datasetPedal, datasetFarol, datasetVolante;
    
    public SemaforoNeural(int nvl) throws Exception {
    	//INSTACIAR ATRIBUTOS (NOME DO ATRIBUTO, E O TOTAL DE VARIAÇÕES)
    	attDesempenho = new Attribute("Desempenho", desempenhos); 
    	attSemaforo = new Attribute("Estado", cores); 
        attAcao = new Attribute("Acao", acoes); 
        attEstado = new Attribute("Estado", estados);
        attFarol = new Attribute("Farol", farol);
        attSensor = new Attribute("Sensor", sensores);
        attVolante = new Attribute("Volante", volantes);
        attDistanciaObstaculo = new Attribute("Obstaculo", 1000);
        //=============================================================
        //INSTANCIAR O AMBIENTE E DEFINIR QUAL O TARGET
        ArrayList<Attribute> attributesPedal = new ArrayList<>();
        attributesPedal.add(attDesempenho);
        attributesPedal.add(attSemaforo);
        //attributesPedal.add(attDistanciaObstaculo);
        attributesPedal.add(attSensor);
        attributesPedal.add(attAcao);
        datasetPedal = new Instances("Ambiente", attributesPedal, 1);
        datasetPedal.setClass(attAcao); //AQUI DEFINO A CLASSE PREVISORA
        //=============================================================
        ArrayList<Attribute> attributesFarol = new ArrayList<>();
        attributesFarol.add(attEstado);
        attributesFarol.add(attFarol);
        attributesFarol.add(attDesempenho);
        datasetFarol = new Instances("Ambiente2", attributesFarol, 1);
        datasetFarol.setClass(attFarol); //AQUI DEFINO A CLASSE PREVISORA
        //=============================================================
        ArrayList<Attribute> attributesVolante = new ArrayList<>();
        attributesVolante.add(attSensor);
        attributesVolante.add(attVolante);
        attributesVolante.add(attDesempenho);
        datasetVolante = new Instances("Ambiente3", attributesVolante, 1);
        datasetVolante.setClass(attVolante); //AQUI DEFINO A CLASSE PREVISORA
        //=============================================================
        //POPULAR A BASE DE DADOS APRA SER USADA NO TREINAMENTO
        PopularBase(nvl);
        //CRIAR REDE NEURAL
        inicializarPopulacaoRedesNeurais(nvl);
    }
    //AVALIAR OS COMPORTAMENTOS DOS REGISTROS ALEATORIOS, PARA OS NOVOS REGISTROS QUE FOREM ESTUDAR, ESTUDAREM DE BASES CORRETAS.
    public int classificarDesempenhoPedal(int acao, int cor, int sensor, int distancia) {
    	int desempenho = 0;
    	if(sensor != 7) {
    		if(cor == 0 && acao == 0) {
        		desempenho+=2;
        	}else if(cor == 0 && acao == 1) {
        		desempenho+=1;
        	}else if(cor == 0 && acao == 2) {
        		
        	}else if(cor == 1 && acao == 0) {
        		desempenho+=1;
        	}else if(cor == 1 && acao == 1) {
        		desempenho+=2;
        	}else if(cor == 1 && acao == 2) {
        		desempenho+=1;
        	}else if(cor == 2 && acao == 0) {
        		
        	}else if(cor == 2 && acao == 1) {
        		desempenho+=1;
        	}else if(cor == 2 && acao == 2) {
        		desempenho+=2;
        	}
    	}else if(sensor == 7){
    		if(acao == 0) {
    			
        	}else if(acao == 1) {
        		desempenho+=2;
        	}else if(acao == 2) {
        		desempenho+=1;
        	}
    	}
    	
    	return desempenho;
    }
    public int classificarDesempenhoVolante(int sensor, int volante) {
    	//List<String> sensores = Arrays.asList("NENHUM" , "FRONTAL" , "DIREITO", "ESQUERDO");
    	//List<String> volantes = Arrays.asList("PARADO" , "DIREITA", "ESQUERDA");
    	int desempenho = 0;
    	if(sensor == 0 && volante == 0) {
    		desempenho = 2;
    	}else if(sensor == 0 && volante == 1) {
    		desempenho = 2;
    	}else if(sensor == 0 && volante == 2) {
    		desempenho = 2;
    	}else if(sensor == 1 && volante == 0) {
    		desempenho = 0;
    	}else if(sensor == 1 && volante == 1) {
    		desempenho = 2;
    	}else if(sensor == 1 && volante == 2) {
    		desempenho = 2;
    	}else if(sensor == 2 && volante == 0) {
    		desempenho = 2;
    	}else if(sensor == 2 && volante == 1) {
    		desempenho = 0;
    	}else if(sensor == 2 && volante == 2) {
    		desempenho = 1;
    	}else if(sensor == 3 && volante == 0) {
    		desempenho = 2;
    	}else if(sensor == 3 && volante == 1) {
    		desempenho = 1;
    	}else if(sensor == 3 && volante == 2) {
    		desempenho = 0;
    	}else if(sensor == 4 && volante == 0) {
    		desempenho = 0;
    	}else if(sensor == 4 && volante == 1) {
    		desempenho = 0;
    	}else if(sensor == 4 && volante == 2) {
    		desempenho = 2;
    	}else if(sensor == 5 && volante == 0) {
    		desempenho = 0;
    	}else if(sensor == 5 && volante == 1) {
    		desempenho = 2;
    	}else if(sensor == 5 && volante == 2) {
    		desempenho = 0;
    	}else if(sensor == 6 && volante == 0) {
    		desempenho = 2;
    	}else if(sensor == 6 && volante == 1) {
    		desempenho = 0;
    	}else if(sensor == 6 && volante == 2) {
    		desempenho = 0;
    	}else if(sensor == 7 && volante == 0) {
    		desempenho = 2;
    	}else if(sensor == 7 && volante == 1) {
    		desempenho = 0;
    	}else if(sensor == 7 && volante == 2) {
    		desempenho = 0;
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
    		
   
    private void PopularBase(int nvl) {
        //POPULANDO A BASE DE TREINAMENTO DE ACORDO COM A QUANTIDADE INFORMADA NA BASE_INICIAL
        for(int i = 0; i < BASE_INICIAL; i++) {
        	int sensor;
        	int desempenhoVolante = 3;
        	int volante;
        	do {
        		sensor = random.nextInt(sensores.size()); 
        		volante = random.nextInt(3);
        		desempenhoVolante = classificarDesempenhoVolante(sensor, volante);
        	}while(desempenhoVolante != nvl);
        		
        	int desempenhoPedal = 3;
        	int acao;
        	int cor;
        	int distancia;
        	
        	do {
        		acao = random.nextInt(3);
            	cor = random.nextInt(3);
            	
            	distancia = random.nextInt(1000);
            	desempenhoPedal = classificarDesempenhoPedal(acao, cor, sensor, distancia);
        	}while(desempenhoPedal != nvl);
        		
        	
        	int estado;
        	int farol;
        	int desempenhoFarol;
        	
        	do {
        		estado = random.nextInt(3); 
            	farol = random.nextInt(2);
            	desempenhoFarol = classificarDesempenhoFarol(estado, farol);
        	}while(desempenhoFarol != nvl);

        	//ADICIONAR O REGISTRO NAS BASES DE DADOS
            adicionarRegistro(0,cor, acao, desempenhoPedal, estado, farol, sensor, distancia, volante);
            adicionarRegistro(1,cor, acao, desempenhoFarol, estado, farol, sensor, distancia, volante);
            adicionarRegistro(2,cor, acao, desempenhoVolante, estado, farol, sensor, distancia, volante);
        }
    }
    
    private void adicionarRegistro(int base,int cor, int acao, int desempenho, int estado, int farolDecisao, int sensor, int distancia,int volante) {
    	if(base == 0) {
    		Instance instanciaPedal = new DenseInstance(4);
    		instanciaPedal.setValue(attDesempenho, desempenhos.get(desempenho));
            instanciaPedal.setValue(attSemaforo, cores.get(cor));
            //instanciaPedal.setValue(attDistanciaObstaculo, distancia);
            instanciaPedal.setValue(attSensor, sensores.get(sensor));
            instanciaPedal.setValue(attAcao, acoes.get(acao));
            instanciaPedal.setDataset(datasetPedal);
            datasetPedal.add(instanciaPedal);
    	}else if(base == 1) {
    		Instance instanciaFarol = new DenseInstance(3);
            instanciaFarol.setValue(attEstado, estados.get(estado));
            instanciaFarol.setValue(attFarol, farol.get(farolDecisao));
            instanciaFarol.setValue(attDesempenho, desempenhos.get(desempenho));
            datasetFarol.add(instanciaFarol);
    	}else if(base == 2) {
    		Instance instanciaVolante = new DenseInstance(3);
            instanciaVolante.setValue(attSensor, sensores.get(sensor));
            instanciaVolante.setValue(attVolante, volantes.get(volante));
            instanciaVolante.setValue(attDesempenho, desempenhos.get(desempenho));
            datasetVolante.add(instanciaVolante);
    	}
    }
    
    private void inicializarPopulacaoRedesNeurais(int nvl) throws Exception {
    	// CRIA UMA NOVA REDE NEURAL PRA CADA INDIVIDUO
        populacaoNeuralPedal = new ArrayList<>();
        populacaoNeuralFarol = new ArrayList<>();
        populacaoNeuralVolante = new ArrayList<>();
        
        MultilayerPerceptron redeNeuralPedal = new MultilayerPerceptron();
        redeNeuralPedal.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
        redeNeuralPedal.buildClassifier(datasetPedal);
        populacaoNeuralPedal.add(redeNeuralPedal);
        //============================================================
        MultilayerPerceptron redeNeuralFarol = new MultilayerPerceptron();
        redeNeuralFarol.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
        redeNeuralFarol.buildClassifier(datasetFarol);
        populacaoNeuralFarol.add(redeNeuralFarol);
        //============================================================
        MultilayerPerceptron redeNeuralVolante = new MultilayerPerceptron();
        redeNeuralVolante.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
        redeNeuralVolante.buildClassifier(datasetVolante);
        populacaoNeuralVolante.add(redeNeuralVolante);
        //CRIAR UMA NOVA BASE ALEATÓRIA PARA QUE OS INDIVIDUOS TENHA REFERENCIAS DIFERENTES.
        System.out.println("BASE CARREGADA===================");
        if(saveArff) {
        	exportarDatasetFarol("res/farol_dataset/dataset_farol_"+count+".arff");
            exportarDatasetPedal("res/pedal_dataset/dataset_pedal_"+count+".arff");
            exportarDatasetVolante("res/volante_dataset/dataset_volante_"+count+".arff");
            count++;
        }
        //LIMPAR BASES, PARA QUE OUTROS INDIVIDUOS APRENDAM COM OUTRAS EXPERIENCIAS
        datasetPedal.clear();
        datasetFarol.clear();
        datasetVolante.clear();
        //POPULAR UMA NOVA BASE
        PopularBase(nvl);    
        
    }
    
    public String passo(int acao,int idIndividuo, int cor, int experiencia, int estado, int sensor, int distancia,int volante) throws Exception {
        //CRIANDO INSTANCIA PARA UM NOVO REGISTRO SER CLASSIFICADO
    	String acaoPrevista = "";
    	if(acao == 0) {
    		Instance instanciaPedal = new DenseInstance(5);
            instanciaPedal.setValue(attSemaforo, cores.get(cor));
            instanciaPedal.setValue(attDesempenho, desempenhos.get(experiencia));
            instanciaPedal.setValue(attSensor, sensores.get(sensor));
           // instanciaPedal.setValue(attDistanciaObstaculo, distancia);
            instanciaPedal.setDataset(datasetPedal);
            
            Classifier classificador = populacaoNeuralPedal.get(idIndividuo);
            acaoPrevista = acoes.get((int) classificador.classifyInstance(instanciaPedal));
    	}else if(acao == 1){
    		Instance instanciaFarol = new DenseInstance(3);
            instanciaFarol.setValue(attEstado, estados.get(estado));
            instanciaFarol.setValue(attDesempenho, desempenhos.get(experiencia));
            instanciaFarol.setDataset(datasetFarol);
            
            Classifier classificador = populacaoNeuralFarol.get(idIndividuo);
            acaoPrevista = farol.get((int) classificador.classifyInstance(instanciaFarol));
    	}else if(acao == 2){
    		Instance instanciaVolante = new DenseInstance(3);
            instanciaVolante.setValue(attSensor, sensores.get(sensor));
            instanciaVolante.setValue(attDesempenho, desempenhos.get(experiencia));
            instanciaVolante.setDataset(datasetVolante);
            
            Classifier classificador = populacaoNeuralVolante.get(idIndividuo);
            acaoPrevista = volantes.get((int) classificador.classifyInstance(instanciaVolante));
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
    public void exportarDatasetVolante(String caminhoArquivo) throws Exception {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(datasetVolante);
        saver.setFile(new java.io.File(caminhoArquivo));
        saver.writeBatch();
    }
    
}
