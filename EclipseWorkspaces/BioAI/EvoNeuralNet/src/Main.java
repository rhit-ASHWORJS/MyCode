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
		
		HashMap<double[], double[]> testData = new HashMap<double[],double[]>();
//		testData.put(tar("zz"), tar("z"));
//		testData.put(tar("zo"), tar("o"));
//		testData.put(tar("oz"), tar("o"));
//		testData.put(tar("oo"), tar("z"));
//		System.out.println(testData);
		
		//----------bigboi
		testData.put(tar("zzz"), tar("zz"));
		testData.put(tar("zzo"), tar("zz"));
		testData.put(tar("zoz"), tar("zz"));
		testData.put(tar("zoo"), tar("zo"));
		testData.put(tar("ozz"), tar("oz"));
		testData.put(tar("ozo"), tar("zz"));
		testData.put(tar("ooz"), tar("zo"));
		testData.put(tar("ooo"), tar("zz"));
		FeedForwardNeuralNet.functionData = testData;

		
		ArrayList<FFNNPopulation> generations = new ArrayList<FFNNPopulation>();
		FFNNPopulation initialPopulation = new FFNNPopulation();
		generations.add(initialPopulation);
		
		PrintWriter csvWriter;
		File csvFile = new File("data.csv");
		
		
		NetVisualizer nv = new NetVisualizer(initialPopulation.getBest());
		try {
            csvFile.createNewFile();
            csvWriter = new PrintWriter(csvFile);
            csvWriter.println("generation,max");
            csvWriter.println(0 + "," + initialPopulation.getBestFitness());
            int gen = 0;
            while(generations.get(generations.size()-1).getBestFitness() != 1 && gen < 200000)
//            while(gen < 1000)
            {
            	gen++;
            	FFNNPopulation newGen = generations.get(generations.size()-1).getNextGeneration();
            	System.out.println("Best fitness of generation "+(generations.size()-1)+":"+newGen.getBestFitness());
//            	System.out.println("Best tree:" + newGen.networks.get(0).infixString());
            	generations.add(newGen);
            	nv.swapNetwork(generations.get(generations.size()-1).getBest());
            	nv.updateFrame();
            	csvWriter.println(generations.size()-1 + "," + newGen.getBestFitness());
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            System.err.println("CSV file not created");
        }
		
		FeedForwardNeuralNet best = generations.get(generations.size()-1).getBest();
		
		best.printTruthTable01();
		best.printEdgeWeights();
	}
	
	public static double[] tar(String s)
	{
		double[] array = new double[s.length()];
		for(int i=0; i<array.length; i++)
		{
			if(s.charAt(i)=='o')
			{
				array[i]=1;
			}
		}
		return array;
	}
}
