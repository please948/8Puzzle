import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Puzzle extends JFrame {
    JButton[][] btn_list;
    int row, col;
    JButton blank;
    String blank_caption = "빈칸";
    Container puzzlePanel;
    JPanel controlPanel;
    Random random;

    public Puzzle(int r, int c) {
        this.row = r;
        this.col = c;
        this.setTitle("퍼즐게임");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null,
                        "퍼즐 상태를 저장하고 종료하시겠습니까?",
                        "종료 확인",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    SaveLoad.save(Puzzle.this);
                }
                System.exit(0);
            }
        });

        // 퍼즐 패널 설정
        puzzlePanel = new JPanel();
        puzzlePanel.setLayout(new GridLayout(row, col));
        this.add(puzzlePanel, BorderLayout.CENTER);

        btn_list = new JButton[row][col];
        random = new Random();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                JButton btn = new JButton("" + (i * col + j));
                btn.addActionListener(new ClickListener());
                btn_list[i][j] = btn;
                puzzlePanel.add(btn);
            }
        }

        blank = btn_list[row - 1][col - 1];
        blank.setText("");

        shuffle(200);


        controlPanel = new JPanel();
        JButton computerModeBtn = new JButton("컴퓨터 모드");
        computerModeBtn.addActionListener(e -> startComputerMode());        //컴퓨터가 플레이

        JButton hintBtn = new JButton("힌트");
        hintBtn.addActionListener(e -> showHint()); // 힌트를 제공

        controlPanel.add(computerModeBtn);
        controlPanel.add(hintBtn);


        this.add(controlPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void startComputerMode() {


    }

    private void showHint() {

    }

    void shuffle(int moves) {       //moves만큼 빈칸을 인접한 버튼과 위치를 교환하여 섞는 함수
        for (int i = 0; i < moves; i++) {       //moves만큼 반복
            int direction = random.nextInt(4);      //0~3까지 랜덤수 지정
            int blank_x = -1, blank_y = -1;     //초기 빈칸의 위치를 지정한다.

            for (int j = 0; j < row; j++) {     //반복문을 돌려서 현재 빈칸의 위치를 찾는다.
                for (int k = 0; k < col; k++) {
                    if (btn_list[j][k] == blank) {
                        blank_x = k;
                        blank_y = j;
                        break;
                    }
                }
            }

            int new_x = blank_x, new_y = blank_y;
            switch (direction) {        // case에 맞게 좌표이동
                case 0: new_y--; break;
                case 1: new_y++; break;
                case 2: new_x--; break;
                case 3: new_x++; break;
            }

            if (!invalid_coord(new_x, new_y)) {     //유효하지 않은 위치가 아니라면 (=유효한 위치라면)
                swap_button(new int[]{blank_y, blank_x}, new int[]{new_y, new_x});  //기존의 빈칸 위치와 new위치 교환
                refresh();      //새로고침
            }
        }
    }

    boolean invalid_coord(int x, int y) {       //유효하지 않은 위치인가 확인하는 함수
        return x < 0 || y < 0 || x >= this.col || y >= this.row;
    }

    boolean is_blank(int x, int y) {        //빈칸인가 확인하는 함수
        if (invalid_coord(x,y)) return false;
        return btn_list[y][x] == this.blank;
    }

    int[] find_blank(JButton btn) {     //현재 빈칸의 위치를 찾는 함수
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (btn_list[i][j] == btn) {
                    if (is_blank(j - 1, i)) return new int[]{i, j - 1};
                    if (is_blank(j + 1, i)) return new int[]{i, j + 1};
                    if (is_blank(j, i - 1)) return new int[]{i - 1, j};
                    if (is_blank(j, i + 1)) return new int[]{i + 1, j};
                }
            }
        }
        return null;
    }

    void swap_button(int[] clicked, int[] blank) {      //클릭한 버튼과 빈칸을 교환하는 함수
        JButton temp = btn_list[clicked[0]][clicked[1]];
        btn_list[clicked[0]][clicked[1]] = btn_list[blank[0]][blank[1]];
        btn_list[blank[0]][blank[1]] = temp;
    }

    void refresh() {        //퍼즐판을 새로고침하는 함수
        puzzlePanel.removeAll();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                puzzlePanel.add(btn_list[i][j]);
            }
        }
        revalidate();
        repaint();
    }

    boolean click_button(JButton btn) {
        int[] blank_pos = find_blank(btn);
        if (blank_pos == null) return false;

        int[] clicked_pos = null;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (btn_list[i][j] == btn) {
                    clicked_pos = new int[]{i, j};
                    break;
                }
            }
        }

        if (clicked_pos != null) {
            swap_button(clicked_pos, blank_pos);
            refresh();
            is_end();
            return true;
        }
        return false;
    }

    boolean is_end() {      //퍼즐 완성 처리 함수
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int expected = i * col + j;

                if (btn_list[i][j].getText().equals("")) {
                    continue;
                }

                if (!btn_list[i][j].getText().equals("" + expected)) {
                    return false;
                }
            }
        }

        String[] after_end_options = {"메뉴로", "종료"};
        int select = JOptionPane.showOptionDialog(this,
                "퍼즐을 완성했습니다.\n",
                "성공!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                after_end_options,
                null);

        if (select == 0) {
            this.dispose();
            PuzzleMenu.showMenu();
        } else {
            System.exit(0);
        }
        return true;
    }

    class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e1) {
            JButton btn = (JButton) e1.getSource();
            click_button(btn);
        }
    }
}
