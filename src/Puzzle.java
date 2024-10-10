import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Vector;

public class Puzzle extends JFrame {
    Vector<JButton> btn_list;
    int row, col;
    JButton blank;
    String blank_caption = "";
    Container cp;

    public Puzzle(int r, int c) {   //퍼즐 클래스
        this.row = r;
        this.col = c;
        this.setTitle("8퍼즐게임"); //제목
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //
        this.setSize(500, 400); // 사이즈 지정 (크기설정필요)
        cp = this.getContentPane();
        cp.setLayout(new GridLayout(row, col));

        this.btn_list = new Vector<JButton>();

        for (int i = 0; i < r * c; i++) {
            JButton btn = new JButton("" + i);
            btn.addActionListener(new Puzzle_Listener());
            btn_list.add(btn);
        }

        blank = btn_list.get(this.row * this.col - 1);
        blank.setText(this.blank_caption);

        Collections.shuffle(btn_list);

        for (JButton btn : btn_list)
            cp.add(btn);

        this.setVisible(true);
        this.setResizable(false);
    }

    int index(JButton btn) {
        return btn_list.indexOf(btn);
    }

    int xy_to_index(int x, int y) {
        return y * this.col + x;
    }

    boolean is_blank(int x, int y) {
        if (x < 0 || y < 0 || x >= this.col || y >= this.row) return false;
        return btn_list.get(xy_to_index(x, y)) == this.blank;
    }

    int find_blank(JButton btn) {
        int x = index(btn) % this.col;
        int y = index(btn) / this.col;
        if (is_blank(x + 1, y)) return xy_to_index(x + 1, y); // east
        if (is_blank(x - 1, y)) return xy_to_index(x - 1, y); // west
        if (is_blank(x, y + 1)) return xy_to_index(x, y + 1); // south
        if (is_blank(x, y - 1)) return xy_to_index(x, y - 1); // north
        return -1;
    }

    void add_button() {
        cp.removeAll();
        for (JButton b : btn_list)
            cp.add(b);
    }

    void exchange(int i, int j) {
        JButton t = btn_list.get(i);
        btn_list.set(i, btn_list.get(j));
        btn_list.set(j, t);
    }

    void refresh() {
        revalidate();
        repaint();
    }

    boolean relocate(JButton btn) {
        int clicked = index(btn);
        int fblank = find_blank(btn);
        if (fblank == -1) return false;
        exchange(clicked, fblank);
        add_button();
        refresh();
        check_win();
        return true;
    }

    boolean check_win() {
        for (int i = 0; i < btn_list.size() - 1; i++) {
            if (!btn_list.get(i).getText().equals("" + i)) {
                return false;
            }
        }
        this.setTitle("----------@@@성공@@@----------");
        return true;
    }

    public static void main(String[] args) {
        new Puzzle(3, 3);
        // new Puzzle(4, 5);
    }

    class Puzzle_Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            relocate(btn);
        }
    }

}
//감자탕 브랜치 체크용