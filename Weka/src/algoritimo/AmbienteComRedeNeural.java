package algoritimo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class AmbienteComRedeNeural {

    private List<Classifier> populacaoRedesNeurais;
    private static final int TAMANHO_POPULACAO = 2;
    private static final int PASSOS_POPULACAO = 200;

    private static final double DIA = 0;
    private static final double NOITE = 1;

    private static final double MOVER = 1;
    private static final double ESPERAR = 0;

    private static final double PENALIDADE = 0.1;
    private static final double LIMIAR = 0.5;
    public static double estadoAtual;

    private int passosAteAprendizado = 10;
    private int contadorPassos = 0;
    
    private static Random random = new Random();

    private Attribute attEstado;
    private Attribute attAcao;

    private Instances dataset;

    public AmbienteComRedeNeural() throws Exception {
        // Definir atributos
        attEstado = new Attribute("Estado", 2);  // DIA ou NOITE
        attAcao = new Attribute("Acao", 2);     // Mover ou não

        // Criar dataset
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(attEstado);
        attributes.add(attAcao);

        dataset = new Instances("Ambiente", attributes, 1);
        dataset.setClass(attAcao);

        // Adicionar instâncias de treinamento
        adicionarInstanciasDeTreinamento();

        // Inicializar a população de redes neurais
        inicializarPopulacaoRedesNeurais();
    }

    private void adicionarInstanciasDeTreinamento() {
        // Exemplo de instâncias de treinamento (ajuste conforme necessário)
        for(int i = 0; i < 1000; i++) {
            adicionarInstanciaTreinamento(random.nextInt(2), random.nextInt(2));
        }
        System.out.println(dataset.size());
    }

    private void adicionarInstanciaTreinamento(double estado, double acao) {
        Instance instancia = new DenseInstance(2);
        instancia.setValue(attEstado, estado);
        instancia.setValue(attAcao, acao);
        instancia.setDataset(dataset);
        dataset.add(instancia);
    }

    private void inicializarPopulacaoRedesNeurais() throws Exception {
        populacaoRedesNeurais = new ArrayList<>();
        for (int i = 0; i < TAMANHO_POPULACAO; i++) {
            // Criar uma nova instância de MultilayerPerceptron para cada indivíduo
            MultilayerPerceptron redeNeuralIndividuo = new MultilayerPerceptron();
            redeNeuralIndividuo.setOptions(weka.core.Utils.splitOptions("-L 0.1 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 3"));
            redeNeuralIndividuo.buildClassifier(dataset);

            populacaoRedesNeurais.add(redeNeuralIndividuo);
        }
    }

    public double passo() throws Exception {
        // Criar instância para a entrada da rede neural
        Instance instancia = new DenseInstance(2);

        double adicional = (estadoAtual == DIA) ? 0 : 0.5;
        double estado = random.nextDouble(0.5);

        instancia.setValue(attEstado, estado + adicional);

        // Obter o índice do indivíduo atual no loop
        int individuoIndex = populacaoRedesNeurais.indexOf(instancia);

        if (individuoIndex != -1) {
            // Restante do código dentro do if
            double[] distribution = populacaoRedesNeurais.get(individuoIndex).distributionForInstance(instancia);
            
            // Ajustar para considerar a política de esperar durante a NOITE
            double acaoPrevista = distribution[1];  // A classe 1 representa a ação de MOVER
            
            double acao = (acaoPrevista >= LIMIAR) ? MOVER : ESPERAR;
            
            // Aplicar penalidade se necessário (penalidade para MOVER durante a NOITE)
            double penalidade = (acao == MOVER && estadoAtual == NOITE) ? PENALIDADE : 0;
            
            // Retornar a ação com a penalidade aplicada
            return acao - penalidade;
        } else {
            // Lógica para lidar com o caso em que a instância não foi encontrada na lista
            return 0; // Ou outra ação padrão, dependendo do seu caso
        }
    }

    public static void main(String[] args) throws Exception {
        AmbienteComRedeNeural ambiente = new AmbienteComRedeNeural();

        // Reiniciar o ambiente
        double estadoInicial = DIA;
        System.out.println("Estado inicial: " + (estadoInicial == DIA ? "DIA" : "NOITE"));

        // Realizar algumas ações e observar as recompensas de cada indivíduo
        for (int passo = 0; passo < PASSOS_POPULACAO; passo++) {
            System.out.println("Passo: " + (passo + 1));
            System.out.println("Agora é: " + (estadoAtual == DIA ? "DIA" : "NOITE"));
            for (int individuoIndex = 0; individuoIndex < TAMANHO_POPULACAO; individuoIndex++) {
                double acao = ambiente.passo();
                System.out.print("Indivíduo " + individuoIndex + ": ");
                System.out.println("Ação: " + (acao == MOVER ? "MOVEU. " : "ESPEROU. "));
            }

            // Mudar para o próximo estado (alternar entre DIA e NOITE)
            estadoAtual = (estadoAtual == DIA) ? NOITE : DIA;

            System.out.println();
        }
    }
}
