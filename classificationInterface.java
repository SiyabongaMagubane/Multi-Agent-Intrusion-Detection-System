package classification;

import java.io.FileNotFoundException;
import java.util.HashMap;

public interface classificationInterface {

	static final int DURATION = 0;
	static final int SOURCE_BYTES = 4;
	static final int DESTINATION_BYTES = 5;
	static final int WRONG_FRAGMENT = 7;
	static final int URGENT = 8;
	static final int HOT = 9;
	static final int NUMBER_FAILED_LOGINS = 10;
	static final int NUMBER_COMPROMISED = 12;
	static final int ROOT_SHELL = 13;
	static final int SU_ATTEMPTED = 14;
	static final int NUM_ROOT = 15;
	static final int NUMBER_FILES_CREATION = 16;
	static final int NUMBER_OF_SHELL_PROMPTS = 17;
	static final int NUM_ACCESS_FILES = 18;
	static final int NUM_OUTBOUNDS_CMDB = 19;
	static final int COUNT = 22;
	static final int SRV_COUNT = 23;
	static final int SERROR_RATE = 24;
	static final int SRV_SERROR_RATE = 25;
	static final int RERROR_RATE = 26;
	static final int SRV_ERROR_RATE = 27;
	static final int SAME_SRV_RATE = 28;
	static final int DIFF_SRV_RATE = 29;
	static final int SRV_DIFF_HOST_RATE = 30;
	static final int DST_HOST_COUNT = 31;
	static final int DST_HOST_SRV_COUNT = 32;
	static final int DST_HOST_SAME_SRV_RATE = 33;
	static final int DST_HOST_DIFF_SRV_RATE = 34;
	static final int DST_HOST_SAME_SRV_PORT_RATE = 35;
	static final int DST_HOST_SRV_DIFF_HOST_RATE = 36;
	static final int DST_HOST_SERROR_RATE = 37;
	static final int DST_HOST_SRV_SERROR_RATE = 38;
	static final int DST_HOST_RERROR_RATE = 39;
	static final int DST_HOST_SRV_RERROR_RATE = 40;
	static final int CLASS = 41;

	void LoadData(String NameOfFile) throws FileNotFoundException;

	void FeatureExtractionTraining();

	void SplitData();

	void FeatureExtractionTest();

	String[][] FeatureExtraction(HashMap<Integer, String[]> dataset);

}
