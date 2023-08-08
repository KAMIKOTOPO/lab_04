package lab_04;


import java.util.Arrays;
import java.util.Scanner;

public class FunctionGraph {
	public static void main(String[] args) {
		start();
	}
    public static void start(){
    		
    		try(Scanner scr = new Scanner(System.in)) {	
    			menu(scr);
    			
			} catch (Exception e) {
				System.out.println("Ошибка!!!");
				
			}
    }
    public static void menu(Scanner scr) {

		int x;
		do{
			System.out.println();
			System.out.println("Меню:\n"+"1)Вывести таблицу 3 функций\n"+"2)Вывести график функции S1\n"+"3)Закончить программу");
			System.out.print("Выберите одну из предложенных операций: ");
			x = scr.nextInt();
		switch(x){
		case 1:
			
			printTable(partMenu(scr));
			break;
		case 2:
			
			printGraph(partMenu(scr), scr);

			break;
		case 3:
			System.out.println("Завершение программы...");
			break;
		default:
            System.out.println("Нет такой команды!!!");
		}
		}while(x!=3);
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
    	if(firstValue>endValue||(firstValue<endValue&&step<=0)) {
    		tableFunctions.append("Такого нет");
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
    		graph.append("Графика нет смысла выводить");
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
        double min = 0;
       
        for (double x = firstValue; x <= endValue; x += step) {
        	if(x>0) {
        		min = x;
        		break;
        	}
        }
        double max = min;
        for (double x = firstValue; x <= endValue; x += step) {
            if(x<=0) {
            	continue;
            }else {
        	double y = s1(x);
            min = Math.min(min, y);
            max = Math.max(max, y);
            }
          }
        
        double end = endValue;
        for (double x = firstValue; x <= endValue; x += step) {
            if(x>0) {
        	double y = s1(x);
//            end = x;
            int row = (int) Math.round((x - firstValue) * height / (endValue - firstValue));
            int col = (int) Math.round((y - min) * width / (max - min));

			row = Math.max(0, Math.min(row, height - 1));
			col = Math.max(0, Math.min(col, width - 1));

            plot[row][col] = '*';
            }
        }
        double nSerifs = (max-min)/(serifs-1);
        double z = min+nSerifs;
        int serifsStay = serifs;
        int ser = serifs;
        graph.append(String.format("%.2f", min));
        while(ser>1) {
        	
        	String yAxis = String.format("%s%.2f", " ".repeat((width-((serifsStay-1)*3))/(serifsStay-1)), z);
        	z += nSerifs;
        	ser--;
        	graph.append(yAxis);
        }
//        String yAxis = String.format("%s%-5.3f%s%5.3f%n", " ".repeat(8), roundZero(min), " ".repeat(width - 10), roundZero(max));
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

