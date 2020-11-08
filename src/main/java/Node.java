import java.io.IOException;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Node {

  private String parent;
  private HashSet<String> children;

  public Node(String parent) {
    this.parent = parent;
  }

  public String getParent() {
    return parent;
  }

  public HashSet<String> getChildren() {
    setChildren();
    return children;
  }

  private void setChildren() {
    this.children = new HashSet<>();
    try {
      Document connect = Jsoup.connect(parent)
          .get();
      Elements elements = connect.getElementsByTag("a");
      String matchingSite = "^" + parent + "*.*/*/$";

      elements
          .forEach(element -> {
            String url = element.absUrl("href");
            if (!children.contains(url) && url.matches(matchingSite)) {
              children.add(url);
            }
          });

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
