import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FeedForwardNeuralNet implements Comparable<FeedForwardNeuralNet>{
	double[][] networkWeights;
	double[] thresholds;
	int inputNodes;
	int outputNodes;
	public static HashMap<double[],double[]> functionData;
	
	public FeedForwardNeuralNet(double[][] networkWeights, double[] thresholds, int inputNodes, int outputNodes)
	{
		this.networkWeights = networkWeights;
		if(networkWeights.length != networkWeights[0].length)
		{
			System.err.println("FFNN declared with non-square matrix");
			this.networkWeights = null;
		}
		this.thresholds = thresholds;
		this.inputNodes = inputNodes;
		this.outputNodes = outputNodes;
		this.enforceConstraints();
	}
	
	public FeedForwardNeuralNet(int numNodes, int inputs, int outputs)
	{
		if(numNodes < inputs+outputs)
		{
			System.err.println("Can't have more inputs/outputs than nodes in a neural net");
		}
		this.networkWeights = new double[numNodes][numNodes];
		for(int i=0; i<numNodes; i++)
		{
			for(int j=i+1; j<numNodes; j++)
			{
				networkWeights[i][j] = SeededRandom.rnd.nextDouble(Constants.MAXEDGEWEIGHT*2)-Constants.MAXEDGEWEIGHT;
			}
		}
		this.thresholds = new double[numNodes];
		for(int i=0; i<numNodes; i++)
		{
			thresholds[i]=1;//SeededRandom.rnd.nextDouble(Constants.MAXNODETHRESHOLD*2)-Constants.MAXNODETHRESHOLD;
		}
		
		this.inputNodes = inputs;
		this.outputNodes = outputs;
		this.enforceConstraints();
//		if(!this.enforceConstraints())
//		{
//			System.out.println("Random generation weird, please fix");
//		}
	}
	
//	public boolean followsRules() {
//		
//	}
	
	public double[] runNeuralNetwork(double[] inputs)
	{
		if(inputs.length != inputNodes)
		{
			System.err.println("Wrong number of inputs to neural network.  Expected " +inputNodes + " but got "+inputs.length);
			return null;
		}
		
		double[] nodeValues = new double[networkWeights.length];
		for(int input=0; input<inputNodes; input++)
		{
			nodeValues[input] = inputs[input];
		}
		
		for(int workingNode=inputNodes; workingNode < networkWeights.length; workingNode++)
		{
			double sumOfInputs = 0;
			for(int otherNode=0; otherNode<workingNode; otherNode++)
			{
				if(nodeValues[otherNode] != 0)
				{
					sumOfInputs += nodeValues[otherNode]*networkWeights[otherNode][workingNode];
				}
			}
//			System.out.println(sumOfInputs);
			if(sumOfInputs >= thresholds[workingNode])
			{
				nodeValues[workingNode] = 1;
			}
			else
			{
				nodeValues[workingNode] = 0;
			}
		}
		
		double[] outputs = new double[outputNodes];
		for(int output = 0; output < outputNodes; output++)
		{
			outputs[output] = nodeValues[nodeValues.length-outputNodes+output];
		}
		return outputs;
	}
	
//	public double[] runNeuralNetworkComplex(double[] inputs)
//	{
//		if(inputs.length != inputNodes)
//		{
//			System.err.println("Wrong number of inputs to neural network.  Expected " +inputNodes + " but got "+inputs.length);
//			return null;
//		}
//		
//		double[] nodeValues = new double[networkWeights.length];
//		for(int input=0; input<inputNodes; input++)
//		{
//			nodeValues[input] = inputs[input];
//		}
//		
//		for(int workingNode=inputNodes; workingNode < networkWeights.length; workingNode++)
//		{
//			int sumOfInputs = 0;
//			for(int otherNode=0; otherNode<workingNode; otherNode++)
//			{
//				if(nodeValues[otherNode] != 0)
//				{
//					sumOfInputs += nodeValues[otherNode]*networkWeights[otherNode][workingNode];
//				}
//			}
//			if(sumOfInputs >= thresholds[workingNode])
//			{
//				nodeValues[workingNode] = 1;
//			}
//			else
//			{
//				nodeValues[workingNode] = 0;
//			}
//		}
//		
//		double[] outputs = new double[outputNodes];
//		for(int output = 0; output < outputNodes; output++)
//		{
//			outputs[output] = nodeValues[nodeValues.length-outputNodes+output];
//		}
//		return outputs;
//	}
	
	public double getMSE(HashMap<double[], double[]> desired)
	{
		double sumSquareDifferences = 0;
		for(double[] input : desired.keySet())
		{
			double[] neuralOutput = this.runNeuralNetwork(input);
			for(int output=0; output<neuralOutput.length; output++)
			{
				double difference = (double)neuralOutput[output] - (double)desired.get(input)[output];
				sumSquareDifferences += (double)Math.pow(difference, 2);
			}
		}
		return (double)Math.sqrt(sumSquareDifferences);
	}
	
	public int getNumErrors(HashMap<double[], double[]> desired)
	{
		int errors = 0;
		for(double[] input : desired.keySet())
		{
			double[] neuralOutput = this.runNeuralNetwork(input);
			for(int output=0; output<neuralOutput.length; output++)
			{
				double difference = (double)neuralOutput[output] - (double)desired.get(input)[output];
				if(Math.abs(difference) > 0.0001)
				{
					errors += 1;
				}
			}
		}
		return errors;
	}
	
	public double getFitness(HashMap<double[], double[]> desired)
	{
		return (double)1/((double)1+getMSE(desired));
//		int outputsize = 0;
//		for(double[] in : desired.keySet())
//		{
//			outputsize += desired.get(in).length;
//		}
//		return ((double)outputsize-(double)getNumErrors(desired))/(double)outputsize;
	}
	
	public double getFitness()
	{
		if(functionData == null)
		{
			System.out.println("Please initialize test data");
			return -1;
		}
		return getFitness(functionData);
	}
	
	public void printTruthTable01()
	{
		System.out.println("Truth table for test neural network");
		double[] input = new double[inputNodes];
		
		for(int inputNum=0; inputNum<Math.pow(2, inputNodes); inputNum++)
		{
			//setup input array using overly complex math because I want to
			int testInputNum = inputNum;
			int i = 0;
			for(int test=(int) Math.pow(2, inputNodes-1); test>=1; test/=2)
			{
				if(testInputNum >= test)
				{
					testInputNum -= test;
					input[input.length-1-i]=1;
				}
				else
				{
					input[input.length-1-i]=0;
				}
				i++;
			}
			
			//now run our test
			System.out.println(Arrays.toString(input)+":"+Arrays.toString(runNeuralNetwork(input)));
		}
	}
	
//	public void printTruthTable01Complex()
//	{
//		System.out.println("Truth table for test neural network");
//		double[] input = new double[inputNodes];
//		
//		for(int inputNum=0; inputNum<Math.pow(2, inputNodes); inputNum++)
//		{
//			//setup input array using overly complex math because I want to
//			int testInputNum = inputNum;
//			int i = 0;
//			for(int test=(int) Math.pow(2, inputNodes-1); test>=1; test/=2)
//			{
//				if(testInputNum >= test)
//				{
//					testInputNum -= test;
//					input[input.length-1-i]=1;
//				}
//				else
//				{
//					input[input.length-1-i]=0;
//				}
//				i++;
//			}
//			
//			//now run our test
//			System.out.println(Arrays.toString(runNeuralNetworkComplex(input)));
//		}
//	}
	
	public void printEdgeWeights()
	{
		for(int i=0; i<networkWeights.length; i++)
		{
			for(int j=i+1; j<networkWeights.length; j++)
			{
				if(networkWeights[i][j]!=0)
				{
					System.out.println(i + "-->" + j + ":" + networkWeights[i][j]);
				}
			}
		}
	}
	
	public FeedForwardNeuralNet clone() {
		double[][] newNWweights = new double[networkWeights.length][networkWeights[0].length];
		double[] newThresholds = new double[thresholds.length];
		
		for(int i=0; i<thresholds.length; i++)
		{
			newThresholds[i]=thresholds[i];
		}
		for(int i=0; i<networkWeights.length;i++)
		{
			for(int j=0; j<networkWeights[0].length;j++)
			{
				newNWweights[i][j]=networkWeights[i][j];
			}
		}
		
		return new FeedForwardNeuralNet(newNWweights, newThresholds, inputNodes, outputNodes);
	}
	
	public void mutateNetwork()
	{
		for(int i=0; i<networkWeights.length; i++)
		{
			for(int j=0; j<networkWeights.length; j++)
			{
				if(i==j)
				{
					continue;
				}
				double roll = SeededRandom.rnd.nextDouble();
				if(roll < Constants.MUTATIONRATE)
				{
					networkWeights[i][j] = networkWeights[i][j] + networkWeights[i][j]*generatePercentChange();
				}
				if(roll < Constants.RMUTATIONRATE)
				{
					networkWeights[i][j] = SeededRandom.rnd.nextDouble(-Constants.MAXEDGEWEIGHT, Constants.MAXEDGEWEIGHT);
				}
				
				double killroll = SeededRandom.rnd.nextDouble();
				if(killroll<Constants.DELETIONRATE)
				{
					networkWeights[i][j]=0;
				}
			}
		}
		this.enforceConstraints();
	}
	
	public void mutateThreshold()
	{
		for(int i=0; i<thresholds.length; i++)
		{
			double roll = SeededRandom.rnd.nextDouble();
			if(roll < Constants.THRESHOLDMUTATIONRATE)
			{
				thresholds[i] = thresholds[i] + thresholds[i]*generatePercentChange();
			}
			if(roll < Constants.RTHRESHOLDMUTATIONRATE)
			{
				thresholds[i] = SeededRandom.rnd.nextDouble(-Constants.MAXNODETHRESHOLD, Constants.MAXNODETHRESHOLD);
			}
		}
		this.enforceConstraints();
	}
	
	public boolean enforceConstraints()
	{
		boolean good = true;
		for(int i=0; i<networkWeights.length; i++)
		{
			for(int j=0; j<networkWeights.length; j++)
			{
				if(networkWeights[i][j] != 0)
				{
					if(i>=j)
					{
						good = false;
						networkWeights[i][j] = 0;
					}
					if(j < inputNodes)
					{
						good = false;
						networkWeights[i][j] = 0;
					}
				}
				if(Math.abs(networkWeights[i][j]) > Constants.MAXEDGEWEIGHT)
				{
					good = false;
					if(networkWeights[i][j] <0)
					{
						networkWeights[i][j]=-Constants.MAXEDGEWEIGHT;
					}
					else
					{
						networkWeights[i][j]=Constants.MAXEDGEWEIGHT;
					}
				}
			}
		}
		
		for(int i=0; i<thresholds.length; i++)
		{
			if(Math.abs(thresholds[i]) > Constants.MAXNODETHRESHOLD)
			{
				good = false;
				if(thresholds[i] <0)
				{
					thresholds[i]=-Constants.MAXNODETHRESHOLD;
				}
				else
				{
					thresholds[i]=Constants.MAXNODETHRESHOLD;
				}
			}
		}
		
		return good;
	}
	
	int variation = 5;
	private double generatePercentChange()
	{
		double maxInterval = Constants.MAXIMUMPERCENTCHANGE / variation;
		double generated=0;
		for(int i=0; i<variation; i++)
		{
			generated += SeededRandom.rnd.nextDouble(-maxInterval, maxInterval);
		}
		return generated;
	}
	
	public void mutate() 
	{
		mutateNetwork();
		mutateThreshold();
		enforceConstraints();
	}
	
	public void recombine(FeedForwardNeuralNet other)
	{
		int numPositions = networkWeights.length * networkWeights[0].length;
		int crossoverPOS = SeededRandom.rnd.nextInt(numPositions);
		
		for(int pos=0; pos<crossoverPOS; pos++)
		{
			int posx = pos/networkWeights.length;
			int posy = pos-(posx*networkWeights.length);
			
			double temp = this.networkWeights[posx][posy];
			this.networkWeights[posx][posy]=other.networkWeights[posx][posy];
			other.networkWeights[posx][posy] = temp;
		}
		
		int crossoverThresholdPOS = SeededRandom.rnd.nextInt(thresholds.length);
		for(int pos=0; pos<crossoverThresholdPOS; pos++)
		{
			double temp = this.thresholds[pos];
			this.thresholds[pos]=other.thresholds[pos];
			other.thresholds[pos]=temp;
		}
		other.enforceConstraints();
		this.enforceConstraints();
	}
	
	public int numberZeroes()
	{
		int numZeroes = 0;
		for(int i=0; i<networkWeights.length; i++)
		{
			for(int j=0; j<networkWeights.length; j++)
			{
				if(networkWeights[i][j]==0)
				{
					numZeroes++;
				}
			}
		}
		return numZeroes;
	}
	
	@Override
	public int compareTo(FeedForwardNeuralNet o) {
		if(functionData == null)
		{
			System.out.println("Sorting function not set in FeedForwardNeuralNetwork class");
			return 0;
		}
		else
		{
			if(this.getFitness(functionData) > o.getFitness(functionData))
			{
				return 1;
			}
			else if(this.getFitness(functionData) == o.getFitness(functionData))
			{
				return 0;
				
				//more zeroes better
//				if(this.numberZeroes() > o.numberZeroes())
//				{
//					return 1;
//				}
//				else if(this.numberZeroes() == o.numberZeroes())
//				{
//					return 0;
//				}
//				else
//				{
//					return -1;
//				}
			}
			else
			{
				return -1;
			}
		}
	}
}
