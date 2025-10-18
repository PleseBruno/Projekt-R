package hr.fer.zemris.pror;

import java.awt.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        int rows = 11, cols = 82;
        char[][] grid = new char[rows][cols];

        // fill with '-'
        for (int r = 0; r < rows; r++) {
            java.util.Arrays.fill(grid[r], '-');
        }
        for (int r = rows - 5; r < rows; r++) {
            java.util.Arrays.fill(grid[r], '#');
        }

        Point topLeft = new Point(2, 4);

        for (int i = topLeft.x; i < topLeft.x + 3; i++) {
            for (int j = topLeft.y; j < topLeft.y + 3; j++) {
                grid[j][i] = '*';
            }
        }

        for (int r = 0; r < rows; r++) {
            System.out.println(new String(grid[r]));
        }
        Scanner sc = new Scanner(System.in);
        char inp = sc.next().charAt(0);
        while (inp != 's') {
            for (int r = 0; r < rows; r++) {
                java.util.Arrays.fill(grid[r], '-');
            }
            for (int r = rows - 5; r < rows; r++) {
                java.util.Arrays.fill(grid[r], '#');
            }
            switch (inp) {
                case 'f':
                    topLeft.setLocation(topLeft.x+1, topLeft.y);
                    break;
                case 'b':
                    topLeft.setLocation(topLeft.x-1, topLeft.y);
                    break;
                case 'u':
                    topLeft.setLocation(topLeft.x, topLeft.y-1);
                    break;
                case 'd':
                    topLeft.setLocation(topLeft.x, topLeft.y+1);
                    break;
                case 'n':
                    topLeft.setLocation(topLeft.x, topLeft.y);
                    break;
                default:
                    break;
            }
            for (int i = topLeft.x; i < (topLeft.x + 3); i++) {
                for (int j = topLeft.y; j < (topLeft.y + 3); j++) {
                    grid[j][i]= '*';
                }
            }
            for (int r = 0; r < rows; r++) {
                System.out.println(new String(grid[r]));
            }
            inp = sc.next().charAt(0);
        }
    }
}
