import java.util.ArrayList;
import java.util.Collections;

public class FFNNPopulation {

	public static final int numInitial = Constants.POPULATIONSIZE;
	ArrayList<FeedForwardNeuralNet> networks = new ArrayList<FeedForwardNeuralNet>();
	
	public FFNNPopulation()
	{
		for(int i=0; i<numInitial; i++)
		{
			networks.add(new FeedForwardNeuralNet(Constants.NUMNODES, 3, 2));
		}
		
		if(networks.size() != numInitial)
		{
			System.out.println("Please check FFNNPopulation constructor, tree size and array size differ");
		}
	}
	
	public FFNNPopulation(ArrayList<FeedForwardNeuralNet> networks)
	{
		this.networks=networks;
	}
	
	public void sortTrees()
	{
		Collections.sort(networks);
		Collections.reverse(networks);//we want higher fitness first
	}
	
	public double getBestFitness()
	{
		sortTrees();
		return networks.get(0).getFitness();
	}
	
	public FeedForwardNeuralNet getBest()
	{
		sortTrees();
		return networks.get(0);
	}
	
	public FeedForwardNeuralNet getNetworkRankSelection()//Using the tournament-based ranked selection described in one of the readings
	//Make sure you've called sorting before your run this, otherwise it is absolute nonsense.
	{
		int individual1 = SeededRandom.rnd.nextInt(networks.size());
		int individual2 = SeededRandom.rnd.nextInt(networks.size());
		
		if(individual1 < individual2)
		{
			return networks.get(individual1);
		}
		else
		{
			return networks.get(individual2);
		}
	}
	
	public static final double CROSSOVER_PROB = 0.3;
	public static final double SUBSTITUTION_PROB = 0.65;
	public static final double CLONING_PROB = 0.05;
	
	public FFNNPopulation getNextGeneration()
	{
		ArrayList<FeedForwardNeuralNet> nextGen = new ArrayList<FeedForwardNeuralNet>();
		sortTrees();
		//ELITISM LINE BELOW
//		nextGen.add(trees.get(0));
		//END ELITISM
		while(nextGen.size() < numInitial)
		{
			double roll = SeededRandom.rnd.nextDouble(1);
			if(roll < CROSSOVER_PROB)
			{
				FeedForwardNeuralNet p1 = getNetworkRankSelection().clone();
				FeedForwardNeuralNet p2 = getNetworkRankSelection().clone();
				
				p1.recombine(p2);;
				
				if(nextGen.size() == numInitial)
				{
					nextGen.add(p1);
				}
				else
				{
					nextGen.add(p1);
					nextGen.add(p2);
				}
			}
			else if(roll < CROSSOVER_PROB + SUBSTITUTION_PROB)
			{
				FeedForwardNeuralNet toMutate = getNetworkRankSelection().clone();
				toMutate.mutate();
				nextGen.add(toMutate);
			}
			else if(roll <= CROSSOVER_PROB + SUBSTITUTION_PROB + CLONING_PROB)
			{
				nextGen.add(getNetworkRankSelection().clone());
			}
			else
			{
				System.out.println("nextgen error");
				return null;
			}
		}
		FFNNPopulation newPopulation = new FFNNPopulation(nextGen);
		return newPopulation;
	}
}
