import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageRenamerApp {
    public static final String DEFAULT_PATH = "C:\\Users\\lsc48\\OneDrive\\바탕 화면";

    public static void run() throws IOException {
        // 사용자 입력 시작
        ArrayList<File> list = getFilesAt(DEFAULT_PATH + "\\");
        for (File htmlFile : list) {
            String htmlFilePath = htmlFile.getPath();
            String imgDirPath = htmlFilePath.replace(".html", "_files");
            if (!imgDirPath.endsWith("\\")) {
                imgDirPath += "\\";
            }
            // " - "의 위치 찾기
            int endIdx = imgDirPath.indexOf(" - ");
            String fileName = "";
            System.out.println(imgDirPath);
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
            Path directoryPath = Paths.get(DEFAULT_PATH + "\\" + newDirName);
            Files.createDirectories(directoryPath);
            System.out.println("디렉토리가 생성되었습니다.");

            for (int i = 0; i < names.size(); i++) {
                try {
                    System.out.println(i);
                    String s = names.get(i);
                    String[] ss = s.split("\\.");
                    Path path = Paths.get(imgDirPath + s);
                    Path newPath = Paths.get(DEFAULT_PATH + "\\" + newDirName + fileName + " (" + (i + 1) + ")." + ss[ss.length - 1]);
                    Files.copy(path, newPath);
                    Files.delete(path);
                } catch (NoSuchFileException e) {
                    System.err.println("html UUID 태그 리스트가 없거나 중복된 경우, 해당 파일은 스킵됩니다.");
                }
            }

        }
        System.out.println("\n완료\n");

    }

    public static ArrayList<File> getFilesAt(String directoryPath) {
        ArrayList<File> result = new ArrayList<>();
        // File 객체 생성
        File folder = new File(directoryPath);

        // 디렉토리가 존재하고 디렉토리일 경우에만 실행
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null && files.length > 0) {
                System.out.println("파일 목록:");
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".html")) {
                        System.out.println("발견된 파일명:\t" + file.getName());
                        System.out.println("파일 경로:\t" + file.getPath());
                        result.add(file);
                    }
                }
            } else {
                System.out.println("디렉토리에 파일이 없습니다.");
            }
        } else {
            System.out.println("디렉토리를 찾을 수 없거나 유효하지 않습니다.");
        }
        return result;
    }
}
