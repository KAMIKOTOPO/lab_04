package lab_04;

public class FunctionGraph {
	public static void main(String[] args) {

		Graph gr = new Graph(1, 0.05, 1.3);
		System.out.println(gr.printTableFunctions());
		System.out.println(gr.printGraphFunctions(8));
	}
}

class Graph {
	private double firstValue;
	private double firstValue2;
	private double endValue;
	private double step;

	public Graph(double firstValue, double step, double endValue) {

		this.firstValue = firstValue;
		this.firstValue2 = firstValue;
		this.step = step;
		this.endValue = endValue;
	}
	

	public StringBuilder printTableFunctions() {
		
		StringBuilder tableFunctions = new StringBuilder();
		
		tableFunctions.append(
				"---------------------------------------------\n" + "|     x    |    S1    |    S2    |    S3    |\n"
						+ "|--------------------------------------------\n");

		while (firstValue <= endValue+step) {
			double S1 = 2 * Math.log(firstValue) - 1 / firstValue;
			double S2 = Math.pow(firstValue, 3) - 7 * firstValue + 6.5;
			double S3 = Math.pow(Math.E, -(S1 + S2));
			String str2 = "";
			str2 = String.format("|%10.3g|%10.4g|%10.6g|%10.6g|\n", firstValue, S1, S2, S3);
			tableFunctions.append(str2);
			str2 = String.format("%10.3g|\n", firstValue);

			firstValue += step;

		}
		tableFunctions.append("---------------------------------------------\n");
		return tableFunctions;
	}

	public StringBuilder printGraphFunctions(int serifs) {
		int count = 0;
		StringBuilder graphFunction = new StringBuilder();
		StringBuilder srt1s = new StringBuilder();
		StringBuilder srt2s = new StringBuilder();
	
		while (count < serifs) {
			double S1 = 2 * Math.log(firstValue2) - 1 / firstValue2;
			String str1 = "";
			String str2 = "";
			str1 = String.format("%10.3g", S1);
			srt1s.append(str1);
			str2 = String.format("\n%.3g|", firstValue2);
			srt2s.append(str2);
			firstValue2 += step;
			count++;
		}
		return graphFunction=srt1s.append(srt2s);
	}

}
