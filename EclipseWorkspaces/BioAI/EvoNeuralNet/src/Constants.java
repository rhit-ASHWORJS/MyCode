


/**
 * This file should list exactly the strings to be referenced from the code
 * base. It will load the user specified values from a configuration file and
 * other files that are not meant to be user adjustable.
 * 
 * @author Jason Yoder
 *
 */
public class Constants {

	// Properties (user configurable)
	public static final int MAXEDGEWEIGHT = Integer.parseInt(PropParser.getProperty(ConstantToPropertyMap.MAXEDGEWEIGHT));
	public static final int MAXNODETHRESHOLD = Integer.parseInt(PropParser.getProperty(ConstantToPropertyMap.MAXNODETHRESHOLD));
	public static final double MUTATIONRATE = Double.parseDouble(PropParser.getProperty(ConstantToPropertyMap.MUTATIONRATE));
	public static final double THRESHOLDMUTATIONRATE = Double.parseDouble(PropParser.getProperty(ConstantToPropertyMap.THRESHOLDMUTATIONRATE));
	public static final double RMUTATIONRATE = Double.parseDouble(PropParser.getProperty(ConstantToPropertyMap.RMUTATIONRATE));
	public static final double RTHRESHOLDMUTATIONRATE = Double.parseDouble(PropParser.getProperty(ConstantToPropertyMap.RTHRESHOLDMUTATIONRATE));
	public static final double MAXIMUMPERCENTCHANGE = Double.parseDouble(PropParser.getProperty(ConstantToPropertyMap.MAXIMUMPERCENTCHANGE));
	public static final int POPULATIONSIZE = Integer.parseInt(PropParser.getProperty(ConstantToPropertyMap.POPULATIONSIZE));
	public static final int NUMNODES = Integer.parseInt(PropParser.getProperty(ConstantToPropertyMap.NUMNODES));
	public static final double DELETIONRATE = Double.parseDouble(PropParser.getProperty(ConstantToPropertyMap.DELETIONRATE));
	
	public static final String PATH_CONF_FILE = "src/config/default.properties";

}
