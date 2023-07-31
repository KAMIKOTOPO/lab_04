package lab_04;


public class FunctionGraph {
	public static void main(String[] args) {
		
		Graph gr = new Graph(1, 0.05, 2);
		System.out.println(gr.showTableGraph());
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

	public StringBuilder showTableGraph() {
		StringBuilder str = new StringBuilder();
		str.append("-------------------------------------------\n"
				+ "|     x     |    S1     |    S2     |  S3   |\n"
				+ "|--------------------------------------------\n");
		

				
		while(firstValue<=endValue) {
			double S1 = 2 * Math.log(firstValue) - 1 / firstValue;
			double S2 = Math.pow(firstValue, 3) - 7 * firstValue + 6.5;
			double S3 = Math.pow(Math.E, -(S1+S2));
			String str2 = "";
			str2 = String.format("|%10.3g|%10.4g|%10.6g|%10.6g|\n", firstValue,S1,S2,S3);
			str.append(str2);
			
			firstValue += step;
			
		}
		return str.append("---------------------------------------------\n");
	}
//	public String showGraphFunction() {
//		
//	}

}
