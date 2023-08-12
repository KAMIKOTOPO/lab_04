package lab_04;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class FunctionGraph {
	public static void main(String[] args) throws IncorrectGraphDataException {
		Scanner scr = new Scanner(System.in);
		Graph graph = new Graph();
		while (true)
			try {
				System.out.println();
				System.out.println("""
						Меню:
						0)Задать ширину и высоту графика
						1)Вывести таблицу 3 функций
						2)Вывести график функции S1
						3)Закончить программу
						""");
				System.out.print("Выберите одну из предложенных операций: ");
				int x = scr.nextInt();
				switch (x) {
				case 0:
					graph = sizeGraph();
					break;
				case 1:
					partMenuTable(graph);
					break;
				case 2:
					partMenuGraph(graph);
					break;
				case 4:
					System.out.println("Завершение программы...");
					return;
				default:
					System.out.println("\nНет такой команды!!!");
				}

			} catch (java.util.InputMismatchException e) {
				System.out.println("\nВы ввели некоректные данные");
				scr.nextLine();
			} catch (IncorrectGraphDataException e) {
				System.out.println(e.getMessage());
				scr.nextLine();
			}
	}

	public static void partMenuTable(Graph gr) throws IncorrectGraphDataException {
		if(Objects.isNull(gr)==false) {
			throw new IncorrectGraphDataException("Вы не задали ширину и длину графика");
		}
		Scanner scanner = new Scanner(System.in);
		System.out.print("Введите первое значение: ");
		double first = scanner.nextDouble();
		System.out.print("Введите шаг: ");
		double step = scanner.nextDouble();
		System.out.print("Введите последнее значение значение: ");
		double end = scanner.nextDouble();
		if (first > end ) {
			double temp = first;
			first = end;
			step *= -1;
			end = temp;
		}
		System.out.println(gr.printTableFunctions(first, step, end));

	}

	public static void partMenuGraph(Graph gr) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Введите первое значение: ");
		double first = scanner.nextDouble();
		System.out.print("Введите последнее значение: ");
		double end = scanner.nextDouble();
		System.out.print("Введите количество засечек от 4 до 8: ");
		int serifs = scanner.nextInt();
		System.out.println();
		System.out.println(gr.printGraphFunctions(first, end, serifs));
	}
	public static Graph sizeGraph() throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Введите ширину графика: ");
		int width = scanner.nextInt();
		System.out.print("Введите высоту графика: ");
		int height = scanner.nextInt();
		if(width<=0||height<=0) {
			throw new IncorrectGraphDataException("Ширина и высота должна быть больше нуля");
		}
		System.out.printf("\nВы задали ширину %d и высоту %d", width, height);
		return new Graph(width, height);
	}
}
 
class Graph {
	private double firstValue;
	private double endValue;
	private double step;
	private int widht;
	private int heigth;



	public Graph() throws IncorrectGraphDataException {
		
	}
	public  Graph(int width, int height) {
		this.widht = width;
		this.heigth = height;
	}

	public double s1(double x) {
//		return x * x;
		return 2 * Math.log(x) - (1 / x);
	}

	public double s2(double x) {
		return Math.pow(x, 3) - 7 * x + 6.5;
	}

	public double s3(double x) {
		return Math.pow(Math.E, -(Math.abs(s1(x)) + Math.abs(s2(x))));
	}

	public StringBuilder printTableFunctions(double firstValue, double step, double endValue) throws IncorrectGraphDataException {
		if (step <= 0) {
			throw new IncorrectGraphDataException("\nС таким шагом нельзя создать таблицу");
			}
		StringBuilder tableFunctions = new StringBuilder();

		String sep = "+----------+----------+----------+----------+\n";
		tableFunctions.append(sep);
		tableFunctions.append(String.format("|%10s|%10s|%10s|%10s|%n", "x", "S1", "S2", "S3"));
		tableFunctions.append(sep);

		for (double x = firstValue; x <= endValue; x += step) {
			if (x <= 0) {
				tableFunctions.append(String.format("|%10.3f|%10s|%10.3f|%10s|%n", x, "-", s2(x), "-"));
			} else {
				tableFunctions.append(String.format("|%10.3f|%10.3f|%10.3f|%10.3f|%n", x, s1(x), s2(x), s3(x)));
			}
		}

		tableFunctions.append(sep);
		return tableFunctions;

	}


	
	public StringBuilder printGraphFunctions(double firstValue, double endValue, int serifs) throws IncorrectGraphDataException {
		if (firstValue <= 0 && endValue <= 0) {
			throw new IncorrectGraphDataException(
					"\nГрафика нет смысла выводить, так как на этом отрезке у функции нет значений");
		}
		if (serifs > 8 || serifs < 4) {
			throw new IncorrectGraphDataException("Введите пожалуйста засечку в диапозоне от 4 до 8");
		}
		
		StringBuilder graph = new StringBuilder();
		int height = this.heigth;
		int width = this.widht;
		
		char[][] plot = new char[height][width];
		double minY = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		for (char[] line : plot) {
			Arrays.fill(line, '-');
		}
		step = (endValue - firstValue) / (height - 1);

		
		for (double x = firstValue; x <= endValue; x += step) {
			if (x > 0) {
				double y = s1(x);
				minY = Math.min(minY, y);
				maxY = Math.max(maxY, y);
			}
		}

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
		int realSerifs = width/numWidth;
		if(realSerifs<serifs) {
			if(realSerifs==1) {
				serifs = 0;
				System.out.printf("С такой шириной вы можете поместить максимум 1 засечку\n");
			}
			if(realSerifs<1) {
				serifs = realSerifs;
				System.out.printf("С такой шириной вы можете поместить максимум 1 засечку\n");
			}
			else {
			serifs = realSerifs;
			System.out.printf("С такой шириной вы можете поместить максимум %d засечек\n", realSerifs);
			}
		}
		int spaceWidth = (width - numWidth * serifs)/ (serifs - 1);
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
		for (char[] line : plot) {
			graph.append(String.format("%6.3f |", x));
			graph.append(String.valueOf(line)).append('\n');
			x += step;
		}
		return graph;

}
	public double roundZero(double num) {
		return num < 1e-10 ? 0 : num;
	}

}

class IncorrectGraphDataException extends Exception {
	public IncorrectGraphDataException(String message) {
		super(message);
	}

}