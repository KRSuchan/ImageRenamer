import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        // 사용자 입력 시작
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nhtml 파일의 경로 입력\n");
        String htmlFilePath = br.readLine();
        System.out.println("\n현재 이미지 파일들이 저장된 디렉토리 경로 입력\n");
        String imgDirPath = br.readLine();
        if (!imgDirPath.endsWith("\\")) {
            imgDirPath += "\\";
        }
        String savePath = imgDirPath.substring(0, imgDirPath.substring(0, imgDirPath.lastIndexOf("\\")).lastIndexOf("\\"));
        System.out.println("\n수정된 이미지 파일들이 저장될 새 디렉토리 이름 입력\n");
        String newDirName = br.readLine();
        if (!newDirName.endsWith("\\")) {
            newDirName += "\\";
        }
        System.out.println("\n변경후 파일명 입력\n");
        String newFileName = br.readLine();
        // 사용자 입력 끝
        // html 파일 read
        BufferedReader reader = new BufferedReader(new FileReader(htmlFilePath));
        ArrayList<String> names = new ArrayList<>(); // 순수 파일명 색출
        String str;
        while ((str = reader.readLine()).length() < 10000) { // 원하는 구간만 선택함(for 개발자)
        }
        reader.close();
        // split으로
        String split = "_files/";
        for (int i = 0; i < str.length(); i++) {
            if (str.startsWith(split, i)) {
                for (int j = i; j < str.length(); j++) {
                    if (str.startsWith("\"", j)) {
                        names.add(str.substring(i + split.length(), j));
                        break;
                    }
                }
            }
        }
        // 새 dir 생성 + 파일명 변경 + 파일 이동
        System.out.println("\n변경 시작");
        Path directoryPath = Paths.get(savePath + "\\" + newDirName);
        Files.createDirectories(directoryPath);
        System.out.println("디렉토리가 생성되었습니다.");

        for (int i = 0; i < names.size(); i++) {
            System.out.println(i);
            String s = names.get(i);
            String[] ss = s.split("\\.");
            Path path = Paths.get(imgDirPath + s);
            Path newPath = Paths.get(savePath + "\\" + newDirName + newFileName + " (" + (i + 1) + ")." + ss[ss.length - 1]);
            Files.move(path, newPath);
        }
        System.out.println("\n완료\n");
    }
}