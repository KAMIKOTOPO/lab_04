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
		functions.add(new Function("S1", x-> x <=0? Double.NaN : 2* Math.log(x)-(1/x)));
		System.out.println( );
		while (true)
			try {
				System.out.println();
				System.out.println("""
						Меню:
						0)Задать ширину и высоту графика
						1)Вывести таблицу 3 функций
						2)Вывести график одной из функций
						3)Завершить работу программы
						""");
				System.out.print("Выберите одну из предложенных операций: ");
				int x = scr.nextInt();
				switch (x) {
				case 0:
					functions = Graphic();
					break;
				case 1:
					partMenuTable(functions);
					break;
				case 2:
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
		System.out.println("""
				График какой функции вывести?
				1)Вывести график функции S1
				2)Вывести график функции S2
				3)Вывести график функции S3
				""");
		int numFunction = scanner.nextInt();
		System.out.print("Введите первое значение: ");
		double first = scanner.nextDouble();
		System.out.print("Введите последнее значение: ");
		double end = scanner.nextDouble();
		System.out.print("Введите количество засечек: ");
		int serifs = scanner.nextInt();
		if (serifs <= 0) {
			throw new IncorrectGraphDataException("Засечек должно быть больше 0");
		}
	}

	public static Functions Graphic() throws IncorrectGraphDataException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Введите ширину графика: ");
		int width = scanner.nextInt();
		System.out.print("Введите высоту графика: ");
		int height = scanner.nextInt();
		if (width <= 0 || height <= 0) {
			throw new IncorrectGraphDataException("Ширина и высота должна быть больше нуля");
		}
		System.out.printf("\nВы задали ширину %d и высоту %d", width, height);
		return new Functions(width, height);
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
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}
//	public double s1(double x) {
//		return 2 * Math.log(x) - (1 / x);
//	}
//
//	public double s2(double x) {
//		return Math.pow(x, 3) - 7 * x + 6.5;
//	}
//
//	public double s3(double x) {
//		return Math.pow(Math.E, -(Math.abs(s1(x)) + Math.abs(s2(x))));
//	}

	public String creatTable(double firstValue, double step, double endValue) throws IncorrectGraphDataException {
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
		return tableFunctions.toString();
	}

	public String creatGraphic(double firstValue, double endValue, int serifs) throws IncorrectGraphDataException {
		if (firstValue <= 0 && endValue <= 0) {
			throw new IncorrectGraphDataException(
					"\nГрафика нет смысла выводить, так как на этом отрезке у функции нет значений");
		}
		StringBuilder graph = new StringBuilder();
		int height = this.heigth;
		int width = this.width;
		StringBuilder plot = new StringBuilder("-".repeat(width));
		double minY = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		double step = (endValue - firstValue) / (height - 1);
		for (double x = firstValue; x <= endValue; x += step) {
			if (x > 0) {
				double y =  ((Function) functions).value(x);
				minY = Math.min(minY, y);
				maxY = Math.max(maxY, y);
			}
		}
		int numWidth = 6;
		StringBuilder ruler = new StringBuilder();
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
		ruler.append(" ".repeat(8));
		for (int i = 0; i < serifs - 1; i++) {
			double serifVal = minY + serifStep * i;
			ruler.append(String.format("%-6.3f", serifVal));
			ruler.append(" ".repeat(spaceWidth));

			if (extraSpaces > 0) {
				ruler.append(" ");
				extraSpaces--;
			}
		}
		graph.append(String.format("%6.3f", maxY));
		double x = firstValue;
		for (x = firstValue; x <= endValue; x += step) {
			if (x < 0) {
				graph.append(String.format("\n%6.3f |", x));
				graph.append(plot);
			} else {
				double y = s1(x);
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