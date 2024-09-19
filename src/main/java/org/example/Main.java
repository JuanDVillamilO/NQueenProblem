package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static Set<Integer> rowBlock = new HashSet<>();
    public static Set<Integer> colBlock = new HashSet<>();
    public static Set<String> diagUDBlock = new HashSet<>();
    public static Set<String> diagDUBlock = new HashSet<>();
    public static Stack<String> queens = new Stack<>();
    public static Stack<String> queensNoSolution = new Stack<>();

    public static void main(String[] args) {
        System.out.print("Número de filas/columnas/reinas: ");
        Scanner scan = new Scanner(System.in);
        final int quantity = scan.nextInt();

        Instant start = Instant.now();

        executeCalculates(0, quantity, queens);

        if (queens.size() == quantity) {
            System.out.println("************************************************");
            System.out.println("************************************************");
            System.out.println("*************** ENCONTRADO    ******************");
            System.out.println("************************************************");
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Duración: " + timeElapsed.toMillis() + " milisegundos");

    }

    private static void executeCalculates(int numIter, int quantity, Stack<String> queens) {
        int numIterInit = numIter;
        int flag = 1;

        for (int row = 0; row < quantity; row = row + flag) {
            int posibilitiesFound = 0;
            for (int col = numIter; col < quantity; col ++ ) {

                //Double coord = convertTwoIntToDouble(row, col);
                String coord = row + "." + col;
                if (validate(row, col, quantity, queens)) {
                    queens.push(coord);
                    posibilitiesFound++;
                    break;
                }
            }
            printTable(queens, new int[quantity][quantity]);
            if (queens.size() < row+1) {
                cleanStackOneRow(queensNoSolution, row+1);
                if (row-1 == 0) {
                    cleanStack(queensNoSolution);//queensNoSolution.clear();
                }

                if (validateEndRow(queens.peek(), quantity)) {
                    cleanStackOneRow(queensNoSolution, row-1);
                    cleanStackOneRow(queensNoSolution, row);
                    queens.pop();
                    flag = -2;
                } else {
                    flag = -1;
                }
                queensNoSolution.push(queens.pop());

                if (row-1 <= -1) {
                    return;
                }
            } else {
                flag = 1;
            }
            numIter = 0;
        }

        printTable(queens, new int[quantity][quantity]);
    }

    public static boolean validate(int row, int col, int quantity, Stack<String> queens) {
        if (queens.isEmpty() && queensNoSolution.isEmpty())
            return true;
        queens.forEach(coord -> {
            String[] arr = String.valueOf(coord).split("\\.");
            int rowCoord = Integer.parseInt(arr[0]);
            int colCoord = Integer.parseInt(arr[1]);
            chargeRestrictions(rowCoord, colCoord, quantity);
        });
        boolean result = isValid(row, col, row +"."+ col);
        resetSets();
        return result;
    }

    public static boolean isValid(int rowNumber, int colNumber, String coord) {
        if (rowBlock.contains(rowNumber)) return false;
        if (colBlock.contains(colNumber)) return false;
        if (diagUDBlock.contains(coord)) return false;
        if (diagDUBlock.contains(coord)) return false;
        if (queensNoSolution.contains(coord)) return false;
        return true;
    }

    public static void resetSets() {
        rowBlock.clear();
        colBlock.clear();
        diagUDBlock.clear();
        diagDUBlock.clear();
    }

    public static void chargeRestrictions(int row, int col, int quantity) {
        rowBlock.add(row);
        colBlock.add(col);
        diagUDBefore(row, col);
        diagUDAfter(row, col, quantity);
        diagDUBefore(row, col, quantity);
        diagDUAfter(row, col, quantity);
    }


    public static void printTable(Stack<String> coordQueens, int[][] matrix) {
        char c = '_';
        String separator = "\n" + String.valueOf(c).repeat(4).repeat(matrix.length);
        System.out.println(separator);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                if (!coordQueens.isEmpty() && coordQueens.contains(row+"."+ col)) {
                    System.out.print("R" + "\t|");
                } else {
                    System.out.print(matrix[row][col] + "\t|");
                }
            }
            System.out.println(separator);
        }
        System.out.println("\n*****************************\n");
    }

    public static double convertTwoIntToDouble(int num1, int num2) {
        return new Double(num1+"."+num2).doubleValue();
    }

    public static void diagUDBefore(int row, int col) {
        if (row <= -1 || col <= -1)
            return;
        diagUDBlock.add(row+"."+col);//convertTwoIntToDouble(row,col));

        diagUDBefore(row-1, col-1);
    }

    public static void diagUDAfter(int row, int col, int quantity) {
        if (row >= quantity || col >= quantity)
            return;
        diagUDBlock.add(row+"."+col);//convertTwoIntToDouble(row,col));

        diagUDAfter(row+1, col+1, quantity);
    }

    public static void diagDUBefore(int row, int col, int quantity) {
        if (row >= quantity || col <= -1)
            return;
        diagDUBlock.add(row+"."+col);//convertTwoIntToDouble(row,col));

        diagDUBefore(row+1, col-1, quantity);
    }

    public static void diagDUAfter(int row, int col, int quantity) {
        if (row <= -1 || col >= quantity)
            return;
        diagDUBlock.add(row+"."+col);//convertTwoIntToDouble(row,col));

        diagDUAfter(row-1, col+1, quantity);
    }

    public static void cleanStack(Stack<String> stack) {
        List<String> list = stack.stream().filter(item -> Integer.valueOf(item) < 1).collect(Collectors.toList());
        stack.clear();
        list.forEach(item -> stack.push(item));
    }

    public static void cleanStackOneRow(Stack<String> stack, int row) {
        List<String> list = stack.stream().filter(item -> Double.valueOf(item) < row || Double.valueOf(item) >= row+1).collect(Collectors.toList());
        stack.clear();
        list.forEach(item -> stack.push(item));
    }

    public static boolean validateEndRow(String coord, int quantity) {
        final String[] a2rr = coord.split("\\.");
        return Integer.valueOf(a2rr[1]).intValue() == (quantity-1) ? true : false;
    }
}