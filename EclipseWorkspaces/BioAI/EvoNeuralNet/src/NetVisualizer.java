import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class NetVisualizer {
	double[][] networkWeights;
	double[] thresholds;
	JFrame frame;
	
	public NetVisualizer(double[][] networkWeights)
	{
		this.networkWeights = networkWeights;
		this.initializeFrame();
	}
	
	public NetVisualizer(double[][] networkWeights, double[] thresholds)
	{
		this.networkWeights = networkWeights;
		this.thresholds = thresholds;
		this.initializeFrame();
	}
	
	public NetVisualizer(FeedForwardNeuralNet network)
	{
		this.networkWeights = network.networkWeights;
		this.thresholds = network.thresholds;
		this.initializeFrame();
	}
	
	public void swapNetwork(double[][] newNetworkWeights)
	{
		this.networkWeights = newNetworkWeights;
	}
	
	public void swapNetwork(FeedForwardNeuralNet net)
	{
		this.networkWeights = net.networkWeights;
		this.thresholds = null;
	}
	
	public void initializeFrame()
	{
		this.frame = new JFrame();
		this.frame.setSize(new Dimension(1000,1000));
		this.frame.add(new NetworkComponent());
		this.frame.setVisible(true);
		this.updateFrame();
	}
	
	public void updateFrame()
	{
		this.frame.repaint();
	}
	
	public class NetworkComponent extends JComponent {
		
		int circleRad = 100;
		int xoffset = 50;
		int yoffset = 50;
		int xstep = 200;
		int ystep = 200;
		
		int maxEdgeSize = 5;
		
		public NetworkComponent() {
			
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			g.translate(xoffset, yoffset);
                    
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			
			int numNodes = networkWeights.length;//Won't work if it isn't a square
			
			int nodesPerRow=(int) Math.ceil(Math.sqrt(numNodes));
			
			int nodeDrawing = 0;
			while(nodeDrawing < numNodes)
			{
				g2d.setStroke(new BasicStroke((float) maxEdgeSize));
				int rowNum = nodeDrawing / nodesPerRow;
				int colNum = nodeDrawing - (nodesPerRow*rowNum);
//				System.out.println("Drawing: " +rowNum+","+colNum);
				
				g2d.drawOval(rowNum*xstep, colNum*ystep, circleRad, circleRad);
				if(thresholds == null)
				{
					g2d.drawString("" + nodeDrawing, rowNum*xstep+circleRad/3, colNum*ystep+circleRad/2);
				}
				else
				{
					g2d.drawString("" + nodeDrawing + ":" + thresholds[nodeDrawing], rowNum*xstep+circleRad/3, colNum*ystep+circleRad/2);
				}
				
				for(int otherNode=0; otherNode<numNodes; otherNode++)
				{
					if(otherNode == nodeDrawing)
					{
						continue;
					}
					
					if(networkWeights[nodeDrawing][otherNode] != 0)
					{
						double edgeSize = maxEdgeSize * (networkWeights[nodeDrawing][otherNode] / Constants.MAXEDGEWEIGHT);
						int row2Num = otherNode / nodesPerRow;
						int col2Num = otherNode - (nodesPerRow*row2Num);
						
						if(edgeSize < 0)
						{
							edgeSize = Math.abs(edgeSize);
							g2d.setColor(Color.RED);
						}
						g2d.setStroke(new BasicStroke((float) edgeSize));
						g2d.drawLine(rowNum*xstep+circleRad/2, colNum*ystep+circleRad/2, row2Num*xstep+circleRad/2, col2Num*ystep+circleRad/2);
//						g2d.drawString(""+(double)Math.round(networkWeights[nodeDrawing][otherNode]*100)/100, (3*rowNum*xstep+circleRad/2+row2Num*xstep+circleRad/2)/4, (3*colNum*ystep+circleRad/2+col2Num*ystep+circleRad/2)/4);
						g2d.setColor(Color.BLACK);
					}
				}
				
				nodeDrawing++;
			}
			
			g.translate(-xoffset, -yoffset);
		}
	}
}
