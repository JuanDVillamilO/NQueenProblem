package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static Set<Integer> rowBlock = new HashSet<>();
    public static Set<Integer> colBlock = new HashSet<>();
    public static Set<Double> diagUDBlock = new HashSet<>();
    public static Set<Double> diagDUBlock = new HashSet<>();
    public static Set<Double> queens = new HashSet<>();
    public static Set<Set<Double>> queenstEST = new HashSet<>();

    public static void main(String[] args) {
        System.out.print("Número de filas/columnas/reinas: ");
        Scanner scan = new Scanner(System.in);
        final int quantity = scan.nextInt();

        final int[][] matrix = new int[quantity][quantity];

        int locatedQueens = 0;

        printTable(new HashSet<>(), matrix);

        
        for (int i = 0; i < quantity; i++) {
            executeCalculates(i, quantity, queens);
            System.out.println("************************************************");
            System.out.println("*************** \t\t" + i + "\t\t****************");
            System.out.println("************************************************");

            if (queens.size() == quantity) {
                System.out.println("************************************************");
                System.out.println("************************************************");
                System.out.println("************************************************");
                System.out.println("************************************************");
                System.out.println("************************************************");

                System.out.println("************************************************");
                System.out.println("*************** ENCONTRADO    ******************");
                System.out.println("************************************************");
                break;
            }
            resetSets();
            queens.clear();
        }

    }
/*
    public static boolean test (int row, int col, int quantity) {
        Double coord = convertTwoIntToDouble(row, col);
        if (queens.size() == quantity)
            return true;
        if (queens.size() < quantity)
            return false;
        if (!isValid(row, col, coord)) {
            //resetSets();
            test(row, col+1, quantity);
            col = 0;
        } else {
            queens.add(coord);
            save(queens, row, col, coord, quantity);
            return test(row+1, col, quantity);
        }
    }*/
    private static void executeCalculates(int numIter, int quantity, Set<Double> queens) {
        //if (numIter >= quantity)
          //  return;
        int numIterInit = numIter;
        int cont = 0;
        for (int row = 0; row < quantity; row ++) {
            for (int col = numIter; col < quantity; col ++ ) {

                Double coord = convertTwoIntToDouble(row, col);
                cont++;
                //if (!isValid(row, col, coord)) {
                if (!validate(row, col, quantity, queens)) {
                     //resetSets();
                     continue;
                }
                queens.add(coord);
                if (queenstEST.contains(queens))
                    queens.remove(coord);
                 //save(queens, row, col, coord, quantity);
            }
            queenstEST.add(Set.copyOf(queens));
            numIter = 0;
            cont=0;
            if (queens.size() < row+1) {
                printTable(queens, new int[quantity][quantity]);
                resetSets();
                queens.clear();
                return;
            }
        }
        if (queens.size() != quantity) {
            if (!queens.isEmpty()) {
                List<Double> list = queens.stream().collect(Collectors.toList());
                diagDUBlock.add(list.get(list.size() - 1));
                queens.remove(list.get(list.size() - 1));
            } else {
                resetSets();
                queens.clear();
            }
            //executeCalculates(numIterInit +1, quantity, queens);
        }
        printTable(queens, new int[quantity][quantity]);
    }

    public static boolean validate(int row, int col, int quantity, Set<Double> queens) {
        if (queens.isEmpty())
            return true;
        queens.forEach(coord -> {
            String[] arr = String.valueOf(coord).split("\\.");
            int rowCoord = Integer.parseInt(arr[0]);
            int colCoord = Integer.parseInt(arr[1]);
            //save(queens, rowCoord, colCoord, coord, quantity);
            rowBlock.add(rowCoord);
            colBlock.add(colCoord);
            diagUDBefore(rowCoord, colCoord);
            diagUDAfter(rowCoord, colCoord, quantity);
            diagDUBefore(rowCoord, colCoord, quantity);
            diagDUAfter(rowCoord, colCoord, quantity);
        });
        boolean result = isValid(row, col, convertTwoIntToDouble(row,col));
        resetSets();
        return result;
    }

    public static boolean isValid(int rowNumber, int colNumber, double coord) {
        if (rowBlock.contains(rowNumber)) return false;
        if (colBlock.contains(colNumber)) return false;
        if (diagUDBlock.contains(coord)) return false;
        if (diagDUBlock.contains(coord)) return false;

        return true;
    }

    public static void resetSets() {
        rowBlock.clear();
        colBlock.clear();
        diagUDBlock.clear();
        diagDUBlock.clear();
    }

    public static void save(Set<Double> coordQueens, int row, int col, double coord, int quantity) {
        rowBlock.add(row);
        colBlock.add(col);
        diagUDBefore(row, col);
        diagUDAfter(row, col, quantity);
        diagDUBefore(row, col, quantity);
        diagDUAfter(row, col, quantity);

        //resetSets();
        printTable(coordQueens, new int[quantity][quantity]);
    }


    public static void printTable(Set<Double> coordQueens, int[][] matrix) {
        char c = '_';
        String separator = "\n" + String.valueOf(c).repeat(4).repeat(matrix.length);
        System.out.println(separator);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                if (!coordQueens.isEmpty() && coordQueens.contains(convertTwoIntToDouble(row, col))) {
                    System.out.print("R" + "\t|");
                } else {
                    System.out.print(matrix[row][col] + "\t|");
                }
            }
            System.out.println(separator);
        }
    }

    public static double convertTwoIntToDouble(int num1, int num2) {
        return new Double(num1+"."+num2).doubleValue();
    }

    public static void diagUDBefore(int row, int col) {
        if (row <= -1 || col <= -1)
            return;
        diagUDBlock.add(convertTwoIntToDouble(row,col));

        diagUDBefore(row-1, col-1);
    }

    public static void diagUDAfter(int row, int col, int quantity) {
        if (row >= quantity || col >= quantity)
            return;
        diagUDBlock.add(convertTwoIntToDouble(row,col));

        diagUDAfter(row+1, col+1, quantity);
    }

    public static void diagDUBefore(int row, int col, int quantity) {
        if (row >= quantity || col <= -1)
            return;
        diagDUBlock.add(convertTwoIntToDouble(row,col));

        diagDUBefore(row+1, col-1, quantity);
    }

    public static void diagDUAfter(int row, int col, int quantity) {
        if (row <= -1 || col >= quantity)
            return;
        diagDUBlock.add(convertTwoIntToDouble(row,col));

        diagDUAfter(row-1, col+1, quantity);
    }
}