package lab_04;

public class FunctionGraph {
	public static void main(String[] args) {

		Graph gr = new Graph(1, 0.1, 1.3);
		System.out.println(gr.printTableFunctions());
//		System.out.println(gr.printTableFunctions());
		System.out.println(gr.printGraphFunctions(4));
		System.out.println();
	}
}

class Graph {
	private double firstValue;
	private double endValue;
	private double step;

	public Graph(double firstValue, double step, double endValue) {

		this.firstValue = firstValue;
		this.step = step;
		this.endValue = endValue;
	}

	public StringBuilder printTableFunctions() {

		StringBuilder tableFunctions = new StringBuilder();
		double firstValueMethod = firstValue;
		double S1 = 0;
		tableFunctions.append(
				"---------------------------------------------\n" + String.format("|%10s|%10s|%10s|%10s|\n","x","S1","S2","S3")
						+ "|--------------------------------------------\n");

		while (firstValueMethod < endValue+step) {
			if(firstValueMethod<=0) {
				S1 = 0;
			}
			S1 = 2 * Math.log(firstValueMethod) - (1 / firstValueMethod);
			double S2 = (Math.pow(firstValueMethod, 3) - 7 * firstValueMethod) + 6.5;
			double S3 = Math.pow(Math.E, -(Math.abs(S1) + Math.abs(S2)));
			String str2 = "";
			str2 = String.format("|%10g|%10g|%10g|%10g|\n", firstValueMethod, S1, S2, S3);
			tableFunctions.append(str2);
			firstValueMethod += step;

		}
		tableFunctions.append("---------------------------------------------\n");
		return tableFunctions;
	}

	public StringBuilder printGraphFunctions(int serifs) {
		// создаем всякую чушь для печати графика
		char arrayLine[][] = new char[101][101];
		
		double firstValueLine = firstValue;    //это для масштабной линейки
		StringBuilder returnGraph = new StringBuilder();
		String str = "";
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		//
		//цикл где считаётся значения функции S2
		
		while (firstValueLine  < endValue+step) {
			double S1 = 2 * Math.log(Math.abs(firstValueLine)) - (1 / firstValueLine);
			
			
			if(S1 > max) {
				max = S1;
			}
			if(S1<min) {
				min = S1;
			}
			firstValueLine += step;
		}
//		System.out.println(min);
//		System.out.println(max);
		firstValueLine = firstValue;
		while (firstValueLine  < endValue+step) {
			double S1 = 2 * Math.log(Math.abs(firstValueLine)) - (1 / firstValueLine);
			int n =  (int) Math.round((Math.abs(S1)/((Math.abs(max)+Math.abs(min))/100)));
			int y = (int) Math.round((Math.abs(firstValueLine)/((Math.abs(firstValueLine)+Math.abs(endValue))/100)));
			arrayLine[n][y] = '*';
			firstValueLine += step;
		}
		String str2 = "";
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				if(arrayLine[i][j]=='*') {
					str2 = str2+(arrayLine[i][j]);
				}else {
					str2 = str2+("-");
				}
			}
			str2 = str2 +"\n";
		}
//		System.out.println(str);
		//это строка которая содержит масштабную линейку
		String str1 = String.format("%g%90s%g\n", min, " ",max);
		returnGraph.append(str1);
		returnGraph.append(str2);
		//
		return returnGraph;
	}

}

