package lab_04;

import java.util.Arrays;
import java.util.Scanner;

public class FunctionGraph {
    public static void main(String[] args) {
    	try (Scanner scr = new Scanner(System.in)) {
			System.out.println("Меню:\n"+"1)Вывести таблицу 3 функций\n"+"2)Вывести график функции S1");
			int x = scr.nextInt();
			if(x==1) {
				System.out.println("Введите первое значение:");
				double first = scr.nextDouble();
				System.out.println("Введите шаг:");
				double step = scr.nextDouble();
				System.out.println("Введите последнее значение значение:");
				double end = scr.nextDouble();
				Graph gr = new Graph(first, step, end);
				System.out.println(gr.printTableFunctions());
				System.out.println("Хотите вывести график функции? ДА/НЕТ");
				String answer = scr.next();
				if(answer.toUpperCase().equals("ДА")){
					System.out.println("Введите количество засечек:");
					int serifs = scr.nextInt(); 
					System.out.println(gr.printGraphFunctions(serifs));
				}
				else {
					System.out.println("Спасибо");
				}
			
			}else if (x == 2) {
				System.out.println("Введите первое значение:");
				double first = scr.nextDouble();
				System.out.println("Введите шаг:");
				double step = scr.nextDouble();
				System.out.println("Введите последнее значение значение:");
				double end = scr.nextDouble();
				Graph gr = new Graph(first, step, end);
				System.out.println("Введите количество засечек:");
				int serifs = scr.nextInt(); 
				System.out.println(gr.printGraphFunctions(serifs));
			}else{
				System.out.println("Такой функции нет");
			}
		}catch (Exception e) {
			System.out.println("Ошибка, ввода");
		}
    
    
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
		String sep = "+----------+----------+----------+----------+\n";
        tableFunctions.append(sep);
        tableFunctions.append(String.format("|%10s|%10s|%10s|%10s|%n", "x", "S1", "S2", "S3"));
        tableFunctions.append(sep);

		for (double x = firstValue; x <= endValue; x += step) {
			double y1 = x <= 0 ? 0 : s1(x);
			tableFunctions.append(String.format("|%10.3f|%10.3f|%10.3f|%10.3f|%n", x, y1, s2(x), s3(x)));
		}

        tableFunctions.append(sep);
        return tableFunctions;
        
    }

    public StringBuilder printGraphFunctions(int serifs) {
    	StringBuilder graph = new StringBuilder();
    	if(firstValue<=0&&endValue<=0) {
    		graph.append("Графика нет смысла выводить");
    	}
    	
    	else {
    	int width = 80;
        int height = 20;
        char[][] plot = new char[height][width];
        for (char[] line : plot) {
            Arrays.fill(line, '-');
        }

        

        double min = s1(firstValue);
        double max = min;

        for (double x = firstValue; x <= endValue; x += step) {
            double y = s1(x);
            min = Math.min(min, y);
            max = Math.max(max, y);
        }
        
        double end = 0;
        for (double x = firstValue; x <= endValue; x += step) {
            double y = s1(x);
             end = x;
            int row = (int) Math.round((x - firstValue) * height / (endValue - firstValue));
            int col = (int) Math.round((y - min) * width / (max - min));

			row = Math.max(0, Math.min(row, height - 1));
			col = Math.max(0, Math.min(col, width - 1));

            plot[row][col] = '*';
        }
        double nSerifs = (max-min)/(serifs-1);
        double z = min+nSerifs;
        int serifsStay = serifs;
        graph.append(String.format("%.3f", min));
        while(serifs>1) {
        	
        	String yAxis = String.format("%s%.3f", " ".repeat(width/serifsStay), z);
        	z += nSerifs;
        	serifs--;
        	graph.append(yAxis);
        }
//        String yAxis = String.format("%s%-5.3f%s%5.3f%n", " ".repeat(8), roundZero(min), " ".repeat(width - 10), roundZero(max));
       graph.append("\n");

        double x = firstValue;
        double y = x;
		for (char[] line : plot) {
            graph.append(String.format("%6.3f |", x));
			graph.append(String.valueOf(line)).append('\n');
            x += (end-y)/19;
		}
    	}
        return graph;
    	
    }

	public double roundZero(double num) {
		return num < 1e-10 ? 0 : num;
	}
    
}

