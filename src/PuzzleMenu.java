import javax.swing.*;

class PuzzleMenu { // 퍼즐 메뉴판 처리
    public static void showMenu() {
        String[] options = {"퍼즐 시작", "불러오기", "종료"}; // 메뉴 옵션 배열

        int choice = JOptionPane.showOptionDialog(
                null,
                "메뉴",
                "퍼즐 게임",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            String[] size_options = {"3x3", "4x4", "5x5"}; // 퍼즐판 크기 설정 배열

            int size_choice = JOptionPane.showOptionDialog(
                    null,
                    "퍼즐 크기",
                    "퍼즐 게임",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    size_options,
                    size_options[0]
            );

            if (size_choice != -1) {        //choice가 유효하지 않은 입력값이 아니라면 (=유효한 입력값이라면)
                int size = Integer.parseInt(size_options[size_choice].substring(0, 1));
                new Puzzle(size, size); // 선택된 크기로 퍼즐 생성
            } else {
                System.exit(0); // 선택이 없으면 프로그램 종료
            }
        } else if (choice == 1) { // 불러오기를 선택하면
            Puzzle puzzle = SaveLoad.load(); // 저장된 상태 불러오기
            if (puzzle == null) {
                JOptionPane.showMessageDialog(null, "저장된 퍼즐이 없습니다.");
            }
        } else {
            System.exit(0); // 종료 선택
        }
    }
}
