import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args)
	{
		String filename = Constants.PATH_CONF_FILE;
		PropParser.load(filename);
		System.out.println("Beginning Program...");
		
		double[] zerozero = {0,0};
		double[] zeroone = {0,1};
		double[] onezero = {1,0};
		double[] oneone = {1,1};
		double[] one = {1};
		double[] zero = {0};
		HashMap<double[], double[]> testData = new HashMap<double[],double[]>();
		testData.put(zerozero, zero);
		testData.put(zeroone, one);
		testData.put(onezero, one);
		testData.put(oneone, zero);
		FeedForwardNeuralNet.functionData = testData;
		
		
		ArrayList<FFNNPopulation> generations = new ArrayList<FFNNPopulation>();
		FFNNPopulation initialPopulation = new FFNNPopulation();
		generations.add(initialPopulation);
		
		PrintWriter csvWriter;
		File csvFile = new File("data.csv");
		
		
		try {
            csvFile.createNewFile();
            csvWriter = new PrintWriter(csvFile);
            csvWriter.println("generation,max");
            csvWriter.println(0 + "," + initialPopulation.getBestFitness());
            NetVisualizer nv = new NetVisualizer(initialPopulation.getBest());
            int gen = 0;
            while(generations.get(generations.size()-1).getBestFitness() != 1 && gen < 20000)
//            while(gen < 1000)
            {
            	gen++;
            	FFNNPopulation newGen = generations.get(generations.size()-1).getNextGeneration();
            	System.out.println("Best fitness of generation "+(generations.size()-1)+":"+newGen.getBestFitness());
//            	System.out.println("Best tree:" + newGen.networks.get(0).infixString());
            	nv.swapNetwork(generations.get(generations.size()-1).getBest());
            	nv.updateFrame();
            	generations.add(newGen);
            	csvWriter.println(generations.size()-1 + "," + newGen.getBestFitness());
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            System.err.println("CSV file not created");
        }
		
		FeedForwardNeuralNet best = generations.get(generations.size()-1).getBest();
		best.printTruthTable01Complex();
	}
	
}