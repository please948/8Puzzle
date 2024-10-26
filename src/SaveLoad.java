import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class SaveLoad {
    public static void save(Puzzle puzzle) {
        try (FileWriter writer = new FileWriter("savefile.txt")) {
            writer.write(puzzle.row + " " + puzzle.col + "\n");
            for (int i = 0; i < puzzle.row; i++) {
                for (int j = 0; j < puzzle.col; j++) {
                    String text = puzzle.btn_list[i][j].getText();
                    writer.write(text.equals("") ? "blank" : text);
                    if (j < puzzle.col - 1) writer.write(" ");
                }
                writer.write("\n");
            }
            JOptionPane.showMessageDialog(puzzle, "저장 완료");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(puzzle, "저장 중 오류 발생: " + e.getMessage());
        }
    }

    public static Puzzle load() {
        File file = new File("savefile.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "저장된 퍼즐이 없습니다.");
            return null;
        }

        try (Scanner scanner = new Scanner(file)) {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            Puzzle puzzle = new Puzzle(row, col);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    String text = scanner.next();
                    puzzle.btn_list[i][j].setText(text.equals("blank") ? "" : text);
                    if (text.equals("blank")) puzzle.blank = puzzle.btn_list[i][j];
                }
            }
            puzzle.repaint();
            return puzzle;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
