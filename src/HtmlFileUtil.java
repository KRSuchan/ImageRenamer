import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlFileUtil {

    public List<String> tagLiner(List<String> html) {
        List<String> res = new ArrayList<String>();
        for (String s : html) {
            res.addAll(Arrays.stream(s.split(">")).toList());
        }
        return res;
    }

}
