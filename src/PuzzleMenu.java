import javax.swing.*;

public class PuzzleMenu {       //퍼즐 메뉴판 처리
    public static void main(String[] args) {
        String[] options = {"3x3", "4x4", "5x5"};
        int choice = JOptionPane.showOptionDialog   //옵션
                (null,
                "퍼즐 크기",
                "퍼즐 게임",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice != -1) {     //choice 배열의 유효하지 않은 인덱스
            int size = Integer.parseInt(options[choice].substring(0, 1));
            new Puzzle(size, size); // 선택된 크기로 퍼즐 생성
        } else {
            System.exit(0); // 선택이 없으면 프로그램 종료
        }
    }
}
