package maxx.scbo;

/**
 * Intermediate, text-only representation of a raw-resource relationship. Needed to handle
 * the rawresources in a second pass after the raw config interpretation.
 * 
 * @author phorvath
 *
 */
public class RawRelation implements Comparable<RawRelation> {
  private Id id = new Id();

  private String parent;
  private String child;
  private int no;

  RawRelation(String parent, String child, int no) {
    setParent(parent);
    setChild(child);
    setNo(no);
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getChild() {
    return child;
  }

  public void setChild(String child) {
    this.child = child;
  }

  public int getNo() {
    return no;
  }

  public void setNo(int no) {
    this.no = no;
  }

  public Integer getId() {
    return id.getId();
  }

  @Override
  public int compareTo(RawRelation rawRelation) {
    return rawRelation.getId().compareTo(getId());
  }
}