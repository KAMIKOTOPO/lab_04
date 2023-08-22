package lab_04;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.DoubleUnaryOperator;

public class FunctionGraph {
	public static void main(String[] args) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		Functions functions = new Functions();
		Function s1 = new Function("S1", x -> 2 * Math.log(x) - (1 / x));
		Function s2 = new Function("S2", x -> Math.pow(x, 3) - 7 * x + 6.5);
		Function s3 = new Function("S3", x -> Math.pow(Math.E, -(Math.abs(s1.value(x)) + Math.abs(s2.value(x)))));
		functions.add(s1);
		functions.add(s2);
		functions.add(s3);
		System.out.println();
		while (true)
			try {
				System.out.println();
				System.out.println("""
						Меню:
						1)Задать ширину и высоту графика
						2)Вывести таблицу функций
						3)Вывести график одной из функций
						4)Завершить работу программы
						""");
				System.out.print("Выберите одну из предложенных операций: ");
				int x = scanner.nextInt();
				switch (x) {
				case 1:
					graphic(functions);
					break;
				case 2:
					partMenuTable(functions);
					break;
				case 3:
					partMenuGraphic(functions);
					break;
				case 4:
					System.out.println("Завершение программы...");
					return;
				default:
					System.out.println("\nНет такой команды!!!");
				}
			} catch (java.util.InputMismatchException e) {
				System.out.println("\nВы ввели некорректные данные");
				scanner.nextLine();
			} catch (IncorrectGraphDataException e) {
				System.out.println(e.getMessage());
				scanner.nextLine();
			}
	}

	public static void partMenuTable(Functions functions) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		double first = inputDouble("Введите первое значение: ");
		double step = checkNegativ("Введите шаг: ");
		double end = inputDouble("Введите последнее значение значение: ");
		if (first > end) {
			double temp = first;
			first = end;
			step *= -1;
			end = temp;
		}
		System.out.println(functions.createTable(first, step, end));
	}

	public static void partMenuGraphic(Functions functions) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		StringBuilder enumerationFunctions = new StringBuilder("График какой функции вывести?\n");
		for (int i = 0; i < functions.size(); i++) {
			enumerationFunctions.append(
					String.format("%d)Вывести график функции %s\n", i + 1, functions.getFunction(i).toString()));
		}
		int numFunction = (int) inputDouble(enumerationFunctions.toString(), x -> x >= 1 && x < functions.size(),
				"Под этим номером нет графика\n");
		double first = inputDouble("Введите первое значение: ");
		double end = inputDouble("Введите последнее значение: ");
		int serifs = (int) inputDouble("Введите количество засечек: ");
		System.out.println(functions.creatGraphic(first, end, serifs, numFunction));
	}

	public static void graphic(Functions functions) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		int width = (int) checkNegativ("Введите ширину графика: ");
		int height = (int) checkNegativ("Введите высоту графика: ");
		System.out.printf("\nВы задали ширину %d и высоту %d", width, height);
		functions.setWidth(width);
		functions.setHeight(height);
	}

	public interface Checker {
		boolean check(double val);
	}

	public static double inputDouble(String prompt, Checker checker, String errMsg) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print(prompt);
				double x = scanner.nextDouble();
				if (checker.check(x)) {
					return x;
				}
				System.out.println(errMsg);
			} catch (Exception e) {
				System.out.println("Вы ввели некорректные данные");
				scanner.nextLine();
			}
		}
	}

	public static int checkNegativ(String prompt) {
		String errMsg = "Для этого значения не может использоваться отрицательное или равное нулю число";
		return (int) inputDouble(prompt, x -> x > 0, errMsg);
	}

	public static double inputDouble(String prompt) {
		return (int) inputDouble(prompt, x -> true, "");
	}
}

class Function {
	private String name;
	private DoubleUnaryOperator func;

	public Function(String name, DoubleUnaryOperator func) {
		this.name = name;
		this.func = func;
	}

	public double value(double x) {
		return func.applyAsDouble(x);
	}

	public String toString() {
		return name;
	}
}

class Functions {
	private int width;
	private int height;
	private List<Function> functions;

	public Functions() {
		this(80, 20);
	}

	public Functions(int width, int height) {
		this.width = width;
		this.height = height;
		this.functions = new ArrayList<Function>();
	}

	public void add(Function func) {
		functions.add(func);
	}

	public int size() {
		return functions.size();
	}

	public Function getFunction(int i) {
		return functions.get(i);
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String createTable(double firstValue, double step, double endValue) throws IncorrectGraphDataException {
//		if (step <= 0) {
//			throw new IncorrectGraphDataException("\nС таким шагом нельзя создать таблицу");
//		}
		StringBuilder tableFunctions = new StringBuilder();
		String sep = String.format("%s", "+----------".repeat(functions.size() + 1));
		tableFunctions.append(sep);
		tableFunctions.append("+\n");
		tableFunctions.append(String.format("|%10s", "x"));
		for (int i = 0; i < functions.size(); i++) {
			tableFunctions.append(String.format("|%10s", functions.get(i).toString()));
		}
		tableFunctions.append("|\n");
		tableFunctions.append(sep);
		tableFunctions.append("+\n");
		for (double x = firstValue; x <= endValue; x += step) {
			tableFunctions.append(String.format("|%10.3f", x));
			for (int i = 0; i < functions.size(); i++) {
				double value = functions.get(i).value(x);
				if (Double.isNaN(value)) {
					tableFunctions.append(String.format("|%10s", "-"));
				} else {
					tableFunctions.append(String.format("|%10.3f", value));
				}
			}
			tableFunctions.append("|\n");
		}
		tableFunctions.append(sep);
		tableFunctions.append("+\n");
		return tableFunctions.toString();
	}

	public String creatGraphic(double firstValue, double endValue, int serifs, int numFunction)
			throws IncorrectGraphDataException {
		StringBuilder graph = new StringBuilder();
		StringBuilder plot = new StringBuilder("-".repeat(width));
		double minY = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		double step = (endValue - firstValue) / (height - 1);
		for (double x = firstValue; x <= endValue; x += step) {
			double y = functions.get(numFunction - 1).value(x);
			if (!Double.isNaN(y)) {
				minY = Math.min(minY, y);
				maxY = Math.max(maxY, y);
			}
		}
		if (minY > maxY) {
			return "График нет смысла выводить, так как нет ни одной точки";
		}
		int numWidth = 6;
		int realSerifs = width / numWidth;
		if (realSerifs < serifs) {
			serifs = Math.max(1, realSerifs);
			System.out.printf("С такой шириной вы можете поместить максимум %d засечек\n", realSerifs);
		}
		int spaceWidth = 0;
		int extraSpaces = 0;
		double serifStep = 0;
		if (serifs != 1) {
			spaceWidth = (width - numWidth * serifs) / (serifs - 1);
			extraSpaces = (width - numWidth * serifs) % (serifs - 1);
			serifStep = (maxY - minY) / (serifs - 1);
		}
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
		for (double x = firstValue; x <= endValue; x += step) {
			double y = functions.get(numFunction - 1).value(x);
			if (Double.isNaN(y)) {
				graph.append(String.format("\n%6.3f |", x));
				graph.append(plot);
			} else {
				graph.append(String.format("\n%6.3f |", x));
				double yInterpolated = (y - minY) * (width - 1) / (maxY - minY);
				int col = (int) Math.round(yInterpolated);
				plot.setCharAt(col, '*');
				graph.append(plot);
				plot.setCharAt(col, '-');
			}
		}
		return graph.toString();
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