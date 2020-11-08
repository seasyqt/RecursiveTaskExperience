import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class RecursiveTaskSite extends RecursiveTask<Set<String>> {

  private final Node parent;
  private HashSet<String> set;


  public RecursiveTaskSite(Node parent) {
    this.parent = parent;
    set = new HashSet<>();
  }

  public RecursiveTaskSite(Node parent, HashSet<String> set) {
    this.parent = parent;
    this.set = new HashSet<>(set);
  }

  @Override
  protected Set<String> compute() {
    TreeSet<String> sortedSet = new TreeSet<>();
    Set<RecursiveTaskSite> taskSiteArrayList = new HashSet<>();
    HashSet<String> listUrls = new HashSet<>(set);
    listUrls.add(parent.getParent());
    listUrls.addAll(parent.getChildren());
    for (String child : parent.getChildren()) {
      if (!set.contains(child)) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        RecursiveTaskSite task = new RecursiveTaskSite(new Node(child), listUrls);
        task.fork();
        taskSiteArrayList.add(task);
      }
    }
    for (RecursiveTaskSite tsk : taskSiteArrayList) {
      sortedSet.add(tsk.parent.getParent());
      sortedSet.addAll(tsk.join());
    }

    return sortedSet
        .stream()
        .filter(l -> !l.isBlank() && !l.isEmpty())
        .collect(Collectors.toSet());
  }
}
