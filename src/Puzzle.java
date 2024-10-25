import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

public class Puzzle extends JFrame {
    JButton[][] btn_list; // 2차원 JButton 배열 선언 (퍼즐 타일)
    int row, col; // 퍼즐의 행과 열의 수를 저장하는 변수
    JButton blank;  // 빈칸을 나타내는 blank 선언
    String blank_caption = "빈칸";    // blank의 텍스트

    Container puzzlePanel;      // 퍼즐판 패널
    Random random; // 랜덤 생성기

    public Puzzle(int r, int c) {   // 퍼즐 클래스 생성자
        this.row = r;
        this.col = c;
        this.setTitle("퍼즐게임"); // 제목
        this.setSize(800, 800); // 창의 크기
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 프로그램 종료
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
                    SaveLoad.save(Puzzle.this); // 현재 상태 저장
                }
                System.exit(0); // 프로그램 종료
            }
        });

        puzzlePanel = this.getContentPane();        // puzzlePanel으로 getContentPane()에 접근
        puzzlePanel.setLayout(new GridLayout(row, col)); // 격자형태로 배치

        this.btn_list = new JButton[row][col]; // 2차원 배열로 버튼 리스트 생성
        random = new Random(); // 랜덤 객체 생성

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                JButton btn = new JButton("" + (i * col + j));  // 버튼 생성, 버튼 텍스트 지정
                btn.addActionListener(new ClickListener());   // 생성한 버튼의 이벤트 리스너 추가
                btn_list[i][j] = btn;  // 2차원 배열에 버튼 저장
            }
        }

        blank = btn_list[row - 1][col - 1];  // 마지막 버튼을 빈칸으로 설정
        blank.setText("");  // 빈칸 버튼의 텍스트 지정

        // 2차원 배열의 모든 버튼을 퍼즐판에 추가
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                puzzlePanel.add(btn_list[i][j]);
            }
        }

        shuffle(2); // 퍼즐 섞기
        this.setVisible(true); // 퍼즐판이 보이도록 설정
    }   // Puzzle 생성자 끝

    void shuffle(int moves) {  // 빈칸을 move만큼 이동하여 퍼즐을 섞는 함수
        for (int i = 0; i < moves; i++) {
            int direction = random.nextInt(4); // 0: 위, 1: 아래, 2: 왼쪽, 3: 오른쪽
            int blank_x = -1, blank_y = -1;

            // 빈칸의 위치 찾기
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < col; k++) {
                    if (btn_list[j][k] == blank) {
                        blank_x = k;
                        blank_y = j;
                        break;
                    }
                }
            }

            // 빈칸에 따라 이동 방향 결정
            int new_x = blank_x, new_y = blank_y;
            switch (direction) {
                case 0: new_y--; break; // 위로 이동
                case 1: new_y++; break; // 아래로 이동
                case 2: new_x--; break; // 왼쪽으로 이동
                case 3: new_x++; break; // 오른쪽으로 이동
            }

            // 유효한 이동인지 확인하고 이동
            if (!invalid_coord(new_x, new_y)) {
                swap_button(new int[]{blank_y, blank_x}, new int[]{new_y, new_x}); // 빈칸과 버튼 교환
                refresh(); // 화면 새로고침
            }
        }
    }

    boolean invalid_coord(int x, int y) {  // 유효하지 않은 좌표 확인
        return x < 0 || y < 0 || x >= this.col || y >= this.row;
    }

    boolean is_blank(int x, int y) {  // 빈칸 확인
        if (invalid_coord(x,y)) return false;   //유효하지 않은 좌표면 false
        return btn_list[y][x] == this.blank;    //btn_list[y][x]의 버튼이 빈칸이면 true, 아니면 false
    }

    int[] find_blank(JButton btn) {  // 빈칸 위치 찾기
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (btn_list[i][j] == btn) {
                    if (is_blank(j - 1, i)) return new int[]{i, j - 1}; // 좌
                    if (is_blank(j + 1, i)) return new int[]{i, j + 1}; // 우
                    if (is_blank(j, i - 1)) return new int[]{i - 1, j}; // 상
                    if (is_blank(j, i + 1)) return new int[]{i + 1, j}; // 하
                }
            }
        }
        return null;
    }

    void swap_button(int[] clicked, int[] blank) {  // 매개변수로 받은 두 버튼의 위치 교환
        JButton temp = btn_list[clicked[0]][clicked[1]];
        btn_list[clicked[0]][clicked[1]] = btn_list[blank[0]][blank[1]];
        btn_list[blank[0]][blank[1]] = temp;
    }

    void refresh() {  // 화면 새로고침
        puzzlePanel.removeAll();    // 모든 버튼 퍼즐판에서 제거
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                puzzlePanel.add(btn_list[i][j]);    // exchange를 반영하여 버튼들을 퍼즐판에 추가
            }
        }
        revalidate();   // 변경사항을 레이아웃에 적용
        repaint();  // 화면에 다시 그림
    }

    boolean click_button(JButton btn) {  // 버튼 클릭 처리 함수
        int[] blank_pos = find_blank(btn);
        if (blank_pos == null) return false;

        // 클릭된 버튼의 위치 찾기
        int[] clicked_pos = null;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (btn_list[i][j] == btn) {
                    clicked_pos = new int[]{i, j};
                    break;
                }
            }
        }
        // 위치 교환 처리
        if (clicked_pos != null) {
            swap_button(clicked_pos, blank_pos);   // 버튼 위치 교환
            refresh();  // 퍼즐판 새로고침
            is_end();   // 퍼즐이 완성되었는지 확인
            return true;
        }
        return false;
    }

    boolean is_end() {      //퍼즐 완성 여부 함수
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


        String[] after_end_options = {"메뉴로", "종료"};   // 퍼즐 완성 시 메시지 처리
        int select = JOptionPane.showOptionDialog(this,
                "퍼즐을 완성했습니다.\n",
                "성공!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                after_end_options,
                null);

        if (select == 0) { // 메뉴를 선택하면
            this.dispose(); // 현재 창 닫기
            PuzzleMenu.showMenu(); // 메뉴 호출
        }

        else {  //종료를 선택하면
            System.exit(0); // 프로그램 종료
        }

        return true;
    }

    class ClickListener implements ActionListener {     //클릭 처리 리스너
        public void actionPerformed(ActionEvent e1) {
            JButton btn = (JButton) e1.getSource();
            click_button(btn);
        }
    }
}
