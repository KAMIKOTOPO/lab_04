package lab_04;

import java.io.PrintStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.DoubleUnaryOperator;

public class FunctionGraph {
	public static void main(String[] args) throws IncorrectGraphDataException {
		Scanner scr = new Scanner(System.in);
		Functions functions = new Functions();
		functions.add(new Function("S1", x-> 2* Math.log(x)-(1/x)));
		functions.add(new Function("S2", x->  Math.pow(x, 3) - 7 * x + 6.5));
//		functions.add(new Function("S3", x-> x <=0? Double.NaN : Math.pow(Math.E, -(Math.abs(2* Math.log(x)-(1/x))) + Math.abs(Math.pow(x, 3) - 7 * x + 6.5))));

		System.out.println( );
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
				int x = scr.nextInt();
				switch (x) {
				case 1:
					Graphic(functions);
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
				System.out.println("\nВы ввели некоректные данные");
				scr.nextLine();
			} catch (IncorrectGraphDataException e) {
				System.out.println(e.getMessage());
				scr.nextLine();
			}
	}

	public static void partMenuTable(Functions functions) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Введите первое значение: ");
		double first = scanner.nextDouble();
		System.out.print("Введите шаг: ");
		double step = scanner.nextDouble();
		System.out.print("Введите последнее значение значение: ");
		double end = scanner.nextDouble();
		if (first > end) {
			double temp = first;
			first = end;
			step *= -1;
			end = temp;
		}
		System.out.println(functions.creatTable(first, step, end));
	}

	public static void partMenuGraphic(Functions functions) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		StringBuilder enumerationFunctions = new StringBuilder("График какой функции вывести?\n");
		for (int i = 0; i < functions.size(); i++) {
			enumerationFunctions.append(String.format("Вывести график функции %s\n", functions.getFunction(i).toString()));
		}
		System.out.println(enumerationFunctions.toString());
		int numFunction = scanner.nextInt();
		if(numFunction  < 1 || functions.size() < numFunction) {
			throw new IncorrectGraphDataException("Под этим номером нет графика");
		}
		System.out.print("Введите первое значение: ");
		double first = scanner.nextDouble();
		System.out.print("Введите последнее значение: ");
		double end = scanner.nextDouble();
		System.out.print("Введите количество засечек: ");
		int serifs = scanner.nextInt();
		if (serifs <= 0) {
			throw new IncorrectGraphDataException("Засечек должно быть больше 0");
		}
		System.out.println(functions.creatGraphic(first, end, serifs, numFunction));
	}

	public static void Graphic(Functions functions) throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Введите ширину графика: ");
		int width = scanner.nextInt();
		System.out.print("Введите высоту графика: ");
		int height = scanner.nextInt();
		if (width <= 0 || height <= 0) {
			throw new IncorrectGraphDataException("Ширина и высота должна быть больше нуля");
		}
		System.out.printf("\nВы задали ширину %d и высоту %d", width, height);
		functions.setWidth(width);
		functions.setHeigth(height);
	}
//	public void addFunction(Function function) {
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Введите название функции");
//		String string = scanner.nextLine();
//		System.out.println("Введите функцию используя");
//	}
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
	private int heigth;
	private List<Function> functions;
	public Functions() {
		this(80, 20);
	}
	public Functions(int width, int height) {
		this.width = width;
		this.heigth = height;
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
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}
	public String creatTable(double firstValue, double step, double endValue) throws IncorrectGraphDataException {
		if (step <= 0) {
			throw new IncorrectGraphDataException("\nС таким шагом нельзя создать таблицу");
		}
		StringBuilder tableFunctions = new StringBuilder();
		String sep = String.format("%s\n", "+----------".repeat(functions.size() + 1));
		tableFunctions.append(sep);
		tableFunctions.append(String.format("|%10s", "x"));
		for (int i = 0; i < functions.size(); i++) {
			tableFunctions.append(String.format("|%10s", functions.get(i).toString()));
		}
		tableFunctions.append("|\n");
		tableFunctions.append(sep);
		for (double x = firstValue; x <= endValue; x += step) {
			tableFunctions.append(String.format("|%10.3f", x));
			for (int i = 0; i < functions.size(); i++) {
				double value = functions.get(i).value(x);
				if (Double.isFinite(value)) {
					tableFunctions.append(String.format("|%10.3f", value));	
				} else {
					tableFunctions.append(String.format("|%10s", "-"));				}
			}
			tableFunctions.append("|\n");
		}
		tableFunctions.append(sep);
		return tableFunctions.toString();
	}

	public String creatGraphic(double firstValue, double endValue, int serifs, int numFunction)throws IncorrectGraphDataException {
		StringBuilder graph = new StringBuilder();
		int height = this.heigth;
		int width = this.width;
		StringBuilder plot = new StringBuilder("-".repeat(width));
		double minY = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		double step = (endValue - firstValue) / (height - 1);
		for (double x = firstValue; x <= endValue; x += step) {
			double y = functions.get(numFunction - 1).value(x);
			if (Double.isFinite(y)) {
				minY = Math.min(minY, y);
				maxY = Math.max(maxY, y);
			} else {
				continue;
			}
		}
		if(minY == Integer.MAX_VALUE && maxY == Integer.MIN_VALUE) {
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
		double x = firstValue;
		for (x = firstValue; x <= endValue; x += step) {
			double y = functions.get(numFunction - 1).value(x);
			if (Double.isFinite(y)) {
				graph.append(String.format("\n%6.3f |", x));
				double yInterpolated = (y - minY) * (width - 1) / (maxY - minY);
				int col = (int) Math.round(yInterpolated);
				plot.setCharAt(col, '*');
				graph.append(plot);
				plot.setCharAt(col, '-');
			} else {
				graph.append(String.format("\n%6.3f |", x));
				graph.append(plot);
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