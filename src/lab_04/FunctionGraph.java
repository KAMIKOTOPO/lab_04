package lab_04;

import java.util.Arrays;
import java.util.Scanner;

public class FunctionGraph {
	public static void main(String[] args) {
		menu();
	}
	public static void menu() {
		Scanner scr = new Scanner(System.in);
		int x;
		try {
		do {
			System.out.println("\nМеню:\n" + "1)Вывести таблицу 3 функций\n" + "2)Вывести график функции S1\n"
					+ "3)Закончить программу");
			System.out.print("Выберите одну из предложенных операций: ");
			x = scr.nextInt();
			try {
				System.out.println();
				switch (x) {
				case 1:
					printTable(partMenu(scr));
					break;
				case 2:
					printGraph(partMenu2(scr), scr);
					break;
				case 3:
					System.out.println("Завершение программы...");
					break;
				default:
					System.out.println("Нет такой команды!!!");
				}
			} catch (java.util.InputMismatchException  e) {
				System.out.println();
				System.out.println("Вы ввели некоректные данные");
				scr.nextLine();
			}
			
		} while (x != 3);
	}catch (Exception e) {
		System.out.println();
		System.out.println("Вы ввели некоректные данные");
		menu();
	}
	}
    public static Graph partMenu(Scanner scr) {
    	System.out.print("Введите первое значение: ");
		double first = scr.nextDouble();
		System.out.print("Введите шаг: ");
		double step = scr.nextDouble();
		System.out.print("Введите последнее значение значение: ");
		double end = scr.nextDouble();
		
		Graph gr = new Graph(first, step, end);
		return gr;
	}
    public static Graph partMenu2(Scanner scr) {
    	System.out.print("Введите первое значение: ");
		double first = scr.nextDouble();
		System.out.print("Введите последнее значение значение: ");
		double end = scr.nextDouble();
		Graph gr = new Graph(first, end);
		return gr;
    }
		
	
    public static void printTable(Graph gr) {
    	System.out.println();
    	System.out.println(gr.printTableFunctions());
    }
    public static void printGraph(Graph gr, Scanner scr) {
    	System.out.print("Введите количество засечек от 4 до 8: ");
    	int serifs = scr.nextInt();
    	System.out.println();
    	System.out.println(gr.printGraphFunctions(serifs));
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
    

	public Graph(double firstValue, double endValue) {
		this.firstValue = firstValue;
		this.endValue = endValue;
	}


	public double s1(double x) {
//		return x * x;
		return 2 * Math.log(x) - (1 / x);
	}

	public double s2(double x) {
		return (Math.pow(x, 3) - 7 * x) + 6.5;
	}

	public double s3(double x) {
		return Math.pow(Math.E, -(Math.abs(s1(x)) + Math.abs(s2(x))));
	}

    public StringBuilder printTableFunctions() {
    	StringBuilder tableFunctions = new StringBuilder();
    	if((firstValue>endValue&&step>=0)||(firstValue<endValue&&step<=0)) {
    		tableFunctions.append(("\nС такими данными нельзя построить таблицу, так как никогда не получится конечного значения!!!"))  ;
    	}
    	else {
		String sep = "+----------+----------+----------+----------+\n";
        tableFunctions.append(sep);
        tableFunctions.append(String.format("|%10s|%10s|%10s|%10s|%n", "x", "S1", "S2", "S3"));
        tableFunctions.append(sep);

		for (double x = firstValue; x <= endValue; x += step) {
			if(x<=0) {
				tableFunctions.append(String.format("|%10.3f|%10s|%10.3f|%10s|%n", x, "-", s2(x), "-"));
			}else {
			tableFunctions.append(String.format("|%10.3f|%10.3f|%10.3f|%10.3f|%n", x, s1(x), s2(x), s3(x)));
			}
		}

        tableFunctions.append(sep);
    	}
        return tableFunctions;
        
    }

    public StringBuilder printGraphFunctions(int serifs) {
    	StringBuilder graph = new StringBuilder();
    	if(firstValue<=0&&endValue<=0) {
    		graph.append("Графика нет смысла выводить, так как на этих промежутках он не имеет значений");
    	}
    	else if(firstValue>endValue||(firstValue<endValue&&step<=0)) {
    		graph.append("Такого нет");
    	}
    	
    	else {
    	int width = 80;
        int height = 20;
        char[][] plot = new char[height][width];
        for (char[] line : plot) {
            Arrays.fill(line, '-');
        }
        double minY = Integer.MAX_VALUE;
        double maxY = Integer.MIN_VALUE;
        for (double x = firstValue; x <= endValue; x += step) {
            if (x > 0) {
                double y = s1(x);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
        }
        double end = endValue;
        for (double x = firstValue; x <= endValue; x += step) {
            if (x > 0) {
                double y = s1(x);

                double xInterpolated = (x - firstValue) * (height - 1) / (endValue - firstValue);
                double yInterpolated = (y - minY) * (width - 1) / (maxY - minY);
                int row = (int) Math.round(xInterpolated);
                int col = (int) Math.round(yInterpolated);

                plot[row][col] = '*';
            }
        }
        int numWidth = 6;
        int spaceWidth = (width - numWidth * serifs) / (serifs - 1);
        int extraSpaces = (width - numWidth * serifs) % (serifs - 1);
        double serifStep = (maxY - minY) / (serifs - 1);
        graph.append(" ".repeat(8));
        for (int i = 0; i < serifs - 1; i++) {
            double serifVal = minY + serifStep * i;
            graph.append(String.format("%-6.3f", serifVal));
            graph.append(" ".repeat(spaceWidth));

            if (extraSpaces > 0) {
                graph.append(" ");
                extraSpaces--;
            }
        }
        graph.append(String.format("%6.3f", maxY));
        graph.append("\n");
        
        double x = firstValue;
        double y = x;
		for (char[] line : plot) {
            graph.append(String.format("%6.3f |", x));
			graph.append(String.valueOf(line)).append('\n');
            x += (end-y)/(height-1);
		}
    	}
        return graph;
    	
    }

	public double roundZero(double num) {
		return num < 1e-10 ? 0 : num;
	}
    
}

