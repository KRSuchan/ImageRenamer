import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageRenamerApp {
    public static void run() throws IOException {
        // 사용자 입력 시작
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nhtml 파일의 경로 입력\n");
        String htmlFilePath = br.readLine();
        htmlFilePath = htmlFilePath.substring(1, htmlFilePath.length() - 1);
//        System.out.println("\n현재 이미지 파일들이 저장된 디렉토리 경로 입력\n");
//        String imgDirPath = br.readLine();
        String imgDirPath = htmlFilePath.replace(".html", "_files");
//        imgDirPath = imgDirPath.substring(1, imgDirPath.length() - 1);
        if (!imgDirPath.endsWith("\\")) {
            imgDirPath += "\\";
        }
        String savePath = imgDirPath.substring(0, imgDirPath.substring(0, imgDirPath.lastIndexOf("\\")).lastIndexOf("\\"));

        // " - "의 위치 찾기
        int endIdx = imgDirPath.indexOf(" - ");
        String fileName = "";
        if (endIdx != -1) {
            // 마지막 "\" 이후의 문자열 추출
            int startIdx = imgDirPath.lastIndexOf("\\", endIdx) + 1;
            fileName = imgDirPath.substring(startIdx, endIdx);
        }
        System.out.println(fileName);

//        System.out.println("\n수정된 이미지 파일들이 저장될 새 디렉토리 이름 입력\n");
        String newDirName = fileName;
        if (!newDirName.endsWith("\\")) {
            newDirName += "\\";
        }

//        System.out.println("\n변경후 파일명 입력\n");
        String newFileName = fileName;

        // 사용자 입력 끝
        // html 파일 read
        BufferedReader reader = new BufferedReader(new FileReader(htmlFilePath));
        ArrayList<String> names = new ArrayList<>(); // 순수 파일명 색출
        String str;
        int len = 0;
        String target = "";
        while ((str = reader.readLine()) != null) {
            if (str.length() > len) {
                target = str;
                len = str.length();
            }
        }
        reader.close();
        // split으로
        String split = "_files/";
        for (int i = 0; i < target.length(); i++) {
            if (target.startsWith(split, i)) {
                for (int j = i; j < target.length(); j++) {
                    if (target.startsWith("\"", j)) {
                        names.add(target.substring(i + split.length(), j));
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
            try {
                System.out.println(i);
                String s = names.get(i);
                String[] ss = s.split("\\.");
                Path path = Paths.get(imgDirPath + s);
                Path newPath = Paths.get(savePath + "\\" + newDirName + newFileName + " (" + (i + 1) + ")." + ss[ss.length - 1]);
                Files.copy(path, newPath);
                Files.delete(path);
            } catch (NoSuchFileException e) {
                System.err.println("html UUID 태그 리스트가 없거나 중복된 경우, 해당 파일은 스킵됩니다.");
            }
        }
        System.out.println("\n완료\n");
    }
}
