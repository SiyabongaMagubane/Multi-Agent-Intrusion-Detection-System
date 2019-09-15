package classification;

import java.io.FileNotFoundException;
import java.util.HashMap;

public interface classificationInterface {


	static final int DURATION = 1;
	static final int SOURCE_BYTES = 5;
	static final int DESTINATION_BYTES = 6;
	static final int WRONG_FRAGMENT = 8;
	static final int URGENT = 9;
	static final int HOT = 10;
	static final int NUMBER_FAILED_LOGINS = 11;
	static final int NUMBER_COMPROMISED = 13;
	static final int ROOT_SHELL = 14;
	static final int SU_ATTEMPTED = 15;
	static final int NUM_ROOT = 16;
	static final int NUMBER_FILES_CREATION = 17;
	static final int NUMBER_OF_SHELL_PROMPTS = 18;
	static final int NUM_ACCESS_FILES = 19;
	static final int NUM_OUTBOUNDS_CMDB = 20;
	static final int COUNT = 23;
	static final int SRV_COUNT = 24;
	static final int SERROR_RATE = 25;
	static final int SRV_SERROR_RATE = 26;
	static final int RERROR_RATE = 27;
	static final int SRV_ERROR_RATE = 28;
	static final int SAME_SRV_RATE = 29;
	static final int DIFF_SRV_RATE = 30;
	static final int SRV_DIFF_HOST_RATE = 31;
	static final int DST_HOST_COUNT = 32;
	static final int DST_HOST_SRV_COUNT = 33;
	static final int DST_HOST_SAME_SRV_RATE = 34;
	static final int DST_HOST_DIFF_SRV_RATE = 35;
	static final int DST_HOST_SAME_SRV_PORT_RATE = 36;
	static final int DST_HOST_SRV_DIFF_HOST_RATE = 37;
	static final int DST_HOST_SERROR_RATE = 38;
	static final int DST_HOST_SRV_SERROR_RATE = 39;
	static final int DST_HOST_RERROR_RATE = 40;
	static final int DST_HOST_SRV_RERROR_RATE = 41;
	static final int CLASS = 42;
	

	void LoadData(String NameOfFile) throws FileNotFoundException;
	
	void FeatureExtractionTraining();
	
	void SplitData();

	void FeatureExtractionTest();


	String[][] FeatureExtraction(HashMap<Integer, String[]> dataset);
	
	
}
