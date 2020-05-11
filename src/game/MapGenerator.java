package game;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                map[i][j] = 1;
            }
        }
        brickWidth = 540/col;
        brickHeight = 150/row;
    }
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }

            }
        }
    }
    public void setBrickValue(int value, int row, int col){
        map[row][col] = value;
    }

    public static MapGenerator loadMapFromCSV(InputStream in) {
        int rows = 0;
        int cols = 0;
        ArrayList<Integer> values = new ArrayList<>();

        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            rows += 1;
            String line = scanner.nextLine();
            String[] parts = line.split(",|;");
            cols = parts.length;
            for (String s : parts) {
                values.add(Integer.valueOf(s));
            }
        }
        scanner.close();

        if (rows * cols != values.size()) {
            System.err.println("Something wrong man in CSV loading!" + rows + cols);
            return new MapGenerator(3, 7);
        }

        MapGenerator mapgen = new MapGenerator(rows, cols);
        for (int i=0; i<mapgen.map.length; i++) {
            for (int j=0; j< mapgen.map[i].length; j++) {
                mapgen.map[i][j] = values.get(i*cols + j);
            }
        }
        return mapgen;
    }
}
