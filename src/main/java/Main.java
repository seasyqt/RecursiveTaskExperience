import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

  private static final String path = "src/main/resources/site.txt";
  private static final String url = "https://skillbox.ru/";

  public static void main(String[] args) {
    HashSet<String> listUrls = (HashSet<String>) new ForkJoinPool()
        .invoke(new RecursiveTaskSite(
            (new Node(url))));

    try {
      Files.writeString(Path.of(path), mapSite(listUrls));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static int countForTab(String site) {
    Matcher matcher = Pattern.compile("/").matcher(site);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    count -= 3;
    return count;
  }

  private static String mapSite(Set set) {

    Map<String, Integer> mapOfList = new HashMap<>();
    set.forEach(l -> {
      String line = (String) l;
      int countTab = countForTab(line);
      mapOfList.put(line, countTab);
    });

    Map<String, Integer> linkedHashMap = mapOfList
        .entrySet()
        .stream()
        .sorted(Entry.comparingByValue())
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new
        ));

    StringBuilder line = new StringBuilder();
    linkedHashMap.entrySet().forEach(k -> {
      if (k.getValue() == 0) {
        line.append(k.getKey());
      } else {
        line.append("\n");
        for (int i = 0; i < k.getValue(); i++) {
          line.append("\t");
        }
        line.append(k.getKey());
      }
    });

    return line.toString();
  }

}
