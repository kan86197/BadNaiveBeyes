import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


//Failed
public class SpaghettiBrah {
	public static void main(String[] args) {
		File f =  new File("C:\\Users\\anklebreaker1875\\Desktop\\MLData\\TrainingSet.csv");
		TreeMap<String, Double> probTable = new TreeMap<String, Double>();
		TreeMap<String, Double> positiveProbTable = new TreeMap<String, Double>();
		TreeMap<String, Double> negativeProbTable = new TreeMap<String, Double>();
		try {
			String [] input;
			String [] hello;
			String [] review;
			String s;
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String sInput = reader.readLine();
			while(sInput != null) {
				input = sInput.split(",");
				for(int a = 4; a < input.length - 1; a++) {
					s = input[a].replace("\"", "");
					s = s.replace(".", "").replace("'", "");
					s = s.replace(")", "").replace("(","");
					s = s.replace(" a ", " ").replace(" the ", " ").replace(" and ", " ");
					hello = s.split(" ");
					for(int b = 0; b < hello.length; b++) {
						if(probTable.get("" + hello[b]) != null) {
							if(input[0].equals("truthful")) {
								positiveProbTable.put(hello[b], positiveProbTable.get("" + hello[b]) + 1);
								probTable.put(hello[b], probTable.get("" + hello[b]) + 1);
								if(b > 0) {
									if(probTable.get(hello[b-1] + " " + hello[b]) != null){
										probTable.put(hello[b-1] + " " + hello[b], probTable.get(hello[b-1] + " " + hello[b]) + 1);
										negativeProbTable.put(hello[b-1] + " " + hello[b], (negativeProbTable.get(hello[b-1] + " " + hello[b]))/probTable.get(hello[b-1] + " " + hello[b]));
										positiveProbTable.put(hello[b-1] + " " + hello[b], (positiveProbTable.get(hello[b-1] + " " + hello[b]) + 1)/probTable.get(hello[b-1] + " " + hello[b]));
									}	else {
										probTable.put(hello[b-1] + " " + hello[b], 1.0);
										positiveProbTable.put(hello[b-1] + " " + hello[b], 1.0);
										negativeProbTable.put(hello[b-1] + " " + hello[b], 0.000001);
									}
								}
							}	else {
								negativeProbTable.put(hello[b], negativeProbTable.get("" + hello[b]) + 1);
								probTable.put(hello[b], probTable.get("" + hello[b]) + 1);
								if(b > 0) {
									if(probTable.get(hello[b-1] + " " + hello[b]) != null){
										probTable.put(hello[b-1] + " " + hello[b], probTable.get(hello[b-1] + " " + hello[b]) + 1);
										negativeProbTable.put(hello[b-1] + " " + hello[b], (negativeProbTable.get(hello[b-1] + " " + hello[b]) + 1)/probTable.get(hello[b-1] + " " + hello[b]));
										positiveProbTable.put(hello[b-1] + " " + hello[b], (positiveProbTable.get(hello[b-1] + " " + hello[b]))/probTable.get(hello[b-1] + " " + hello[b]));
									}	else {
										probTable.put(hello[b-1] + " " + hello[b], 1.0);
										positiveProbTable.put(hello[b-1] + " " + hello[b], 0.000001);
										negativeProbTable.put(hello[b-1] + " " + hello[b], 1.0);
									}
								}
							}
						} else {
							probTable.put(hello[b], 1.0);
							if(input[0].equals("truthful")) {
								positiveProbTable.put(hello[b], 1.0);
								negativeProbTable.put(hello[b], 0.000001);
							}	else {
								positiveProbTable.put(hello[b], 0.000001);
								negativeProbTable.put(hello[b], 1.0);
							}
						}
					}
				}
				sInput = reader.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(Map.Entry<String, Double> set : positiveProbTable.entrySet()) {
			String key = set.getKey();
			double value = set.getValue();
			positiveProbTable.put(key, value/probTable.get(key));
			System.out.println(key + " :" + value);
		}

		for(Map.Entry<String, Double> set : negativeProbTable.entrySet()) {
			String key = set.getKey();
			double value = set.getValue();
			negativeProbTable.put(key, value/probTable.get(key));
			//			System.out.println(set.getValue());
		}




		File file =  new File("C:\\Users\\anklebreaker1875\\Desktop\\MLData\\Testset.csv");
		try {
			String [] input;

			String twoGramWord = "";
			BufferedReader secReader = new BufferedReader(new FileReader(file));
			String sInput = secReader.readLine();
			String s;
			String [] hello;
			double correct = 0;
			double count = 0;
			while(sInput != null) {
				input = sInput.split(",");
				double positiveProb = 0;
				double negativeProb = 0; 
				for(int a = 4; a < input.length - 1; a++) {
					s = input[a].replace("\"", "");
					s = s.replace(".", "").replace("'", "");
					s = s.replace(")", "").replace("(","");
					s = s.replace(" a ", " ").replace(" the ", " ").replace(" and ", " ");
					hello = s.split(" ");

					positiveProb = 1;
					negativeProb = 1;
					for(int i =0;i<hello.length;i++) {
						if(i > 0) {
							twoGramWord = hello[i - 1] + hello[i];
						}
						for(Map.Entry<String, Double> set : positiveProbTable.entrySet()) {
							String key = set.getKey();
							double value = set.getValue();
							if(key.equals(hello[i])) {
								positiveProb*=value;
								continue;
							}	else if(key.equals(twoGramWord)) {
								positiveProb*=positiveProbTable.get(twoGramWord);
								continue;
							}
							
						}
						for(Map.Entry<String, Double> set : negativeProbTable.entrySet()) {
							String key = set.getKey();
							double value = set.getValue();
							if(key.equals(hello[i])) {
								negativeProb*=value;
								continue;
							}	else if(key.equals(twoGramWord)) {
								negativeProb*=negativeProbTable.get(twoGramWord);
								continue;
							}
						}	
					}
					if ((input[0].equals("truthful") && positiveProb >= negativeProb) || (input[0].equals("deceptive") && positiveProb <= negativeProb)) correct++;						
					count++;
				}
				System.out.printf("+: %2.8f, -: %2.8f\n", positiveProb*100, negativeProb*100);
				sInput = secReader.readLine();
			}
			System.out.println("Accuracy: " + correct/count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
